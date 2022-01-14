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
package org.pentaho.reportdesigner.crm.report.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.BandElementModelListener;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.commands.CommandKeys;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.crm.report.properties.editors.ChartEditor;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.commands.DataContext;
import org.pentaho.reportdesigner.lib.client.commands.DataProvider;
import org.pentaho.reportdesigner.lib.client.commands.PopupMenuMouseAdapter;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 11.10.2005
 * Time: 15:32:23
 */
public class ReportTree extends JTree implements PropertyChangeListener, DataProvider
{
    private boolean ignoreEvents;

    @NotNull
    private ReportTreeModel reportTreeModel;
    @NotNull
    private ReportElementSelectionModel reportElementSelectionModel;


    public ReportTree(@NotNull final ReportDialog reportDialog, @NotNull final ReportElementSelectionModel reportElementSelectionModel)
    {
        reportTreeModel = new ReportTreeModel(reportElementSelectionModel.getReport());
        this.reportElementSelectionModel = reportElementSelectionModel;

        setModel(reportTreeModel);

        setCellRenderer(new ElementTreeCellRenderer());

        ignoreEvents = false;

        addTreeSelectionListener(new TreeSelectionListener()
        {
            public void valueChanged(@NotNull TreeSelectionEvent e)
            {
                if (ignoreEvents)
                {
                    return;
                }

                TreePath[] selectionPaths = getSelectionPaths();

                if (selectionPaths != null)
                {
                    ArrayList<ReportElement> elementInfos = new ArrayList<ReportElement>();

                    for (TreePath treePath : selectionPaths)
                    {
                        if (treePath.getLastPathComponent() instanceof ReportElement)
                        {
                            ReportElement reportElement = (ReportElement) treePath.getLastPathComponent();
                            elementInfos.add(reportElement);
                        }
                    }

                    reportElementSelectionModel.setSelection(elementInfos);
                }
                else
                {
                    reportElementSelectionModel.clearSelection();
                }
            }
        });

        reportElementSelectionModel.addBandElementModelListener(new BandElementModelListener()
        {
            public void layoutChanged()
            {
            }


            public void selectionChanged()
            {
                ignoreEvents = true;

                try
                {
                    ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getSelectedElementInfos();

                    clearSelection();
                    for (ReportElement elementInfo : selectedElementInfos)
                    {
                        addSelectionPath(new TreePath(elementInfo.getPath()));
                    }

                    TreePath[] selectionPaths = getSelectionPaths();
                    if (selectionPaths != null && selectionPaths.length > 0)
                    {
                        Rectangle pathBounds = getPathBounds(selectionPaths[0]);
                        if (pathBounds != null)
                        {
                            pathBounds.x = 0;
                            pathBounds.width = 0;
                            scrollRectToVisible(pathBounds);
                        }
                    }
                }
                finally
                {
                    ignoreEvents = false;
                }
            }
        });

        reportElementSelectionModel.getReport().addPropertyChangeListener(this);
        attachPropertyChangeListener(reportElementSelectionModel.getReport());


        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull final MouseEvent me)
            {
                if (me.getButton() != MouseEvent.BUTTON1)
                {
                    TreePath[] treePaths = getSelectionPaths();
                    TreePath treePath = getPathForLocation(me.getX(), me.getY());
                    if (treePath != null && !containes(treePaths, treePath))
                    {
                        setSelectionPath(treePath);
                    }
                }
            }


            private boolean containes(@Nullable TreePath[] treePaths, @Nullable TreePath treePath)
            {
                if (treePaths != null && treePath != null)
                {
                    for (TreePath path : treePaths)
                    {
                        if (path.equals(treePath))
                        {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        addMouseListener(new PopupMenuMouseAdapter(reportDialog, this, CommandSettings.SIZE_16));

        ToolTipManager.sharedInstance().registerComponent(this);

        setEditable(false);

        addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                if (e.getClickCount() >= 2)
                {
                    TreePath path = getPathForLocation(e.getX(), e.getY());
                    Object o = path.getLastPathComponent();
                    if (o instanceof DataSetReportElement)
                    {
                        DataSetReportElement dataSetReportElement = (DataSetReportElement) o;
                        if (dataSetReportElement.canConfigure())
                        {
                            dataSetReportElement.showConfigurationComponent(reportDialog, false);
                        }
                    }
                    else if (o instanceof ChartReportElement)
                    {
                        ChartReportElement chartReportElement = (ChartReportElement) o;
                        ChartEditor.showChartEditor(reportDialog.getRootJComponent(), TranslationManager.getInstance().getTranslation("R", "ChartEditor.Title"), chartReportElement, reportDialog);
                    }
                }
            }
        });
    }


    private void attachPropertyChangeListener(@NotNull ReportElement reportElement)
    {
        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement element : children)
        {
            element.addPropertyChangeListener(this);
            attachPropertyChangeListener(element);
        }
    }


    private void detachPropertyChangeListener(@NotNull ReportElement reportElement)
    {
        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement element : children)
        {
            element.removePropertyChangeListener(this);
            detachPropertyChangeListener(element);
        }
    }


    public void propertyChange(@NotNull PropertyChangeEvent evt)
    {
        if (evt.getSource() instanceof ReportElement)
        {
            ignoreEvents = true;

            ReportElement reportElement = (ReportElement) evt.getSource();
            reportTreeModel.nodeChanged(reportElement);

            if (PropertyKeys.CHILD_ADDED.equals(evt.getPropertyName()) || PropertyKeys.CHILD_REMOVED.equals(evt.getPropertyName()) || PropertyKeys.CHILD_MOVED.equals(evt.getPropertyName()))
            {
                reportTreeModel.fireTreeStructureChanged(this, new TreePath(reportTreeModel.getPathToRoot(reportElement)));
                detachPropertyChangeListener(reportElement);
                attachPropertyChangeListener(reportElement);

                ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getSelectedElementInfos();
                clearSelection();
                for (ReportElement elementInfo : selectedElementInfos)
                {
                    addSelectionPath(new TreePath(elementInfo.getPath()));
                }
            }

            ignoreEvents = false;
        }
    }


    @NotNull
    public DataContext getDataContext()
    {
        return new DataContext()
        {
            @Nullable
            public Object getData(@Nullable String key)
            {
                if (CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY.equals(key))
                {
                    ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getSelectedElementInfos();
                    return selectedElementInfos.toArray(new ReportElement[selectedElementInfos.size()]);
                }
                if (CommandKeys.KEY_REPORT_ELEMENT_MODEL.equals(key))
                {
                    return reportElementSelectionModel;
                }

                return null;
            }
        };
    }


    @NotNull
    public String getPlace()
    {
        return "ReportTree";
    }
}
