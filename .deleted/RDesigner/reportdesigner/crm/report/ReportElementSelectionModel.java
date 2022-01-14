/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.LineReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.util.ReportElementUtilities;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 10.10.2005
 * Time: 15:38:26
 */
public class ReportElementSelectionModel
{
    @NotNull
    private LinkedHashSet<BandElementModelListener> bandElementModelListeners;

    @NotNull
    private ArrayList<ReportElement> selectedElementInfos;
    @NotNull
    private ArrayList<ReportElement> nonDescendentsSelectedElementInfos;

    @NotNull
    private Report report;
    @NotNull
    private ReportDialog reportDialog;


    public ReportElementSelectionModel(@NotNull ReportDialog reportDialog, @NotNull Report report)
    {
        //noinspection ConstantConditions
        if (reportDialog == null)
        {
            throw new IllegalArgumentException("reportDialog must not be null");
        }
        //noinspection ConstantConditions
        if (report == null)
        {
            throw new IllegalArgumentException("report must not be null");
        }

        this.reportDialog = reportDialog;
        this.report = report;

        bandElementModelListeners = new LinkedHashSet<BandElementModelListener>();

        selectedElementInfos = new ArrayList<ReportElement>();
        nonDescendentsSelectedElementInfos = new ArrayList<ReportElement>();
    }


    @Nullable
    public ReportPanel getReportPanel(@NotNull ReportElement reportElement)
    {
        ArrayList<ReportBorderPanel> bands = reportDialog.getBands();
        if (bands != null)
        {
            for (ReportBorderPanel reportBorderPanel : bands)
            {
                if (reportElement.isDescendant(reportBorderPanel.getReportPanel().getBandToplevelReportElement()))
                {
                    return reportBorderPanel.getReportPanel();
                }
            }
        }

        return null;
    }


    @NotNull
    public Report getReport()
    {
        return report;
    }


    public void addBandElementModelListener(@NotNull BandElementModelListener bandElementModelListener)
    {
        bandElementModelListeners.add(bandElementModelListener);
    }


    public void removeBandElementModelListener(@NotNull BandElementModelListener bandElementModelListener)
    {
        bandElementModelListeners.remove(bandElementModelListener);
    }


    private void fireLayoutChanged()
    {
        LinkedHashSet<BandElementModelListener> beml = new LinkedHashSet<BandElementModelListener>(bandElementModelListeners);
        for (BandElementModelListener bandElementModelListener : beml)
        {
            bandElementModelListener.layoutChanged();
        }
    }


    private void fireSelectionChanged()
    {
        LinkedHashSet<BandElementModelListener> beml = new LinkedHashSet<BandElementModelListener>(bandElementModelListeners);
        for (BandElementModelListener bandElementModelListener : beml)
        {
            bandElementModelListener.selectionChanged();
        }
    }


    public void refresh()
    {
        report.validate();
        fireLayoutChanged();
    }


    private void addChildren(@NotNull ReportElement reportElement, @NotNull ArrayList<ReportElement> reportElements)
    {
        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement element : children)
        {
            addChildren(element, reportElements);
            reportElements.add(element);
        }
    }


    @Nullable
    public ReportElement getNextElementInfo(@NotNull ReportElement root, @Nullable ReportElement elementBefore, @NotNull Point2D p)
    {
        ArrayList<ReportElement> reportElements = new ArrayList<ReportElement>();
        addChildren(root, reportElements);

        if (elementBefore != null && containsPoint(elementBefore, p))
        {
            boolean before = false;
            for (int i = reportElements.size() - 1; i >= 0; i--)
            {
                ReportElement elementInfo = reportElements.get(i);
                if (before && containsPoint(elementInfo, p))
                {
                    return elementInfo;
                }

                //noinspection ObjectEquality
                if (!before && elementBefore == elementInfo)
                {
                    before = true;
                }
            }
        }

        for (int i = reportElements.size() - 1; i >= 0; i--)
        {
            ReportElement elementInfo = reportElements.get(i);
            if (containsPoint(elementInfo, p))
            {
                return elementInfo;
            }
        }

        return null;
    }


    private boolean containsPoint(@NotNull ReportElement elementInfo, @NotNull Point2D p)
    {
        if (!ReportElementUtilities.isShowInLayoutGUI(elementInfo))
        {
            return false;
        }

        if (elementInfo instanceof LineReportElement)
        {
            LineReportElement lineReportElement = (LineReportElement) elementInfo;
            Rectangle2D.Double r = elementInfo.getRectangle();
            double lw = Math.max(3, lineReportElement.getLineDefinition().getWidth() / 2);
            Rectangle2D.Double rect = new Rectangle2D.Double(r.x - lw, r.y - lw, r.width + 2 * lw, r.height + 2 * lw);

            ReportElementUtilities.convertRectangle(rect, lineReportElement, null);
            return rect.contains(p);
        }
        else
        {
            Rectangle2D.Double rect = new Rectangle2D.Double();
            rect.setRect(elementInfo.getRectangle());
            ReportElementUtilities.convertRectangle(rect, elementInfo, null);
            return rect.contains(p);
        }
    }


    public void clearSelection()
    {
        Undo undo = reportDialog.getUndo();
        if (!undo.isInProgress() && !selectedElementInfos.isEmpty())
        {
            undo.startTransaction(UndoConstants.CLEAR_SELECTION);
            undo.add(new UndoEntry()
            {
                @NotNull
                private ArrayList<ReportElement> selection = new ArrayList<ReportElement>(selectedElementInfos);
                @NotNull
                private ArrayList<ReportElement> nonDescendents = new ArrayList<ReportElement>(nonDescendentsSelectedElementInfos);


                public void undo()
                {
                    selectedElementInfos.addAll(selection);
                    nonDescendentsSelectedElementInfos.addAll(nonDescendents);
                    fireSelectionChanged();
                }


                public void redo()
                {
                    clearSelection();
                }
            });
            undo.endTransaction();
        }

        selectedElementInfos.clear();
        nonDescendentsSelectedElementInfos.clear();
        fireSelectionChanged();
    }


    public void setSelection(@NotNull final Collection<? extends ReportElement> elementInfos)
    {
        Undo undo = reportDialog.getUndo();
        if (!undo.isInProgress())
        {
            undo.startTransaction(UndoConstants.SET_SELECTION);
            undo.add(new UndoEntry()
            {
                @NotNull
                private ArrayList<ReportElement> selection = new ArrayList<ReportElement>(selectedElementInfos);
                @NotNull
                private ArrayList<ReportElement> nonDescendents = new ArrayList<ReportElement>(nonDescendentsSelectedElementInfos);


                public void undo()
                {
                    selectedElementInfos.clear();
                    selectedElementInfos.addAll(selection);
                    nonDescendentsSelectedElementInfos.clear();
                    nonDescendentsSelectedElementInfos.addAll(nonDescendents);
                    fireSelectionChanged();
                }


                public void redo()
                {
                    setSelection(elementInfos);
                }
            });
            undo.endTransaction();
        }

        selectedElementInfos.clear();
        selectedElementInfos.addAll(elementInfos);

        nonDescendentsSelectedElementInfos.clear();
        for (ReportElement reportElement : selectedElementInfos)
        {
            addToNonDescendents(reportElement);
        }

        fireSelectionChanged();
    }


    public void addSelectedElement(@Nullable final ReportElement elementInfo)
    {
        if (elementInfo != null)
        {
            Undo undo = reportDialog.getUndo();
            if (!undo.isInProgress())
            {
                undo.startTransaction(UndoConstants.ADD_SELECTED_ELEMENT);
                undo.add(new UndoEntry()
                {
                    @NotNull
                    private ArrayList<ReportElement> selection = new ArrayList<ReportElement>(selectedElementInfos);
                    @NotNull
                    private ArrayList<ReportElement> nonDescendents = new ArrayList<ReportElement>(nonDescendentsSelectedElementInfos);


                    public void undo()
                    {
                        selectedElementInfos.clear();
                        selectedElementInfos.addAll(selection);
                        nonDescendentsSelectedElementInfos.clear();
                        nonDescendentsSelectedElementInfos.addAll(nonDescendents);
                        fireSelectionChanged();
                    }


                    public void redo()
                    {
                        addSelectedElement(elementInfo);
                    }
                });
                undo.endTransaction();
            }

            selectedElementInfos.add(elementInfo);

            addToNonDescendents(elementInfo);

            fireSelectionChanged();
        }
    }


    private void addToNonDescendents(@NotNull ReportElement reportElement)
    {
        for (Iterator<ReportElement> iterator = nonDescendentsSelectedElementInfos.iterator(); iterator.hasNext();)
        {
            ReportElement element = iterator.next();
            if (reportElement.isDescendant(element))
            {
                return;
            }
            if (element.isDescendant(reportElement))
            {
                iterator.remove();
            }
        }

        nonDescendentsSelectedElementInfos.add(reportElement);
    }


    @NotNull
    public ArrayList<ReportElement> getSelectedElementInfos()
    {
        return selectedElementInfos;
    }


    @NotNull
    public ArrayList<ReportElement> getNonDescendentsSelectedElementInfos()
    {
        return nonDescendentsSelectedElementInfos;
    }

}
