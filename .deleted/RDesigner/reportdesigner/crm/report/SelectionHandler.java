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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.lineal.GuideLine;
import org.pentaho.reportdesigner.crm.report.lineal.LinealModel;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.LineDirection;
import org.pentaho.reportdesigner.crm.report.model.LineReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.util.GraphicUtils;
import org.pentaho.reportdesigner.crm.report.util.ReportElementUtilities;
import org.pentaho.reportdesigner.lib.client.commands.CommandManager;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 11.10.2005
 * Time: 10:56:00
 */
public class SelectionHandler
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(SelectionHandler.class.getName());

    @NotNull
    private static final Color SELECTION_COLOR = Color.BLUE;
    @NotNull
    private static final Color DONT_SHOW_IN_LAYOUT_SELECTION_COLOR = new Color(0, 0, 255, 50);

    @NotNull
    private ReportPanel reportPanel;
    @NotNull
    private ReportElementSelectionModel reportElementSelectionModel;

    @NotNull
    private Point2D.Double pressedPoint;

    @NotNull
    private Point2D.Double tempPoint;


    private enum AdjustmentState
    {
        @NotNull NONE,
        @NotNull RESIZE_N,
        @NotNull RESIZE_S,
        @NotNull RESIZE_W,
        @NotNull RESIZE_E,
        @NotNull RESIZE_NE,
        @NotNull RESIZE_NW,
        @NotNull RESIZE_SE,
        @NotNull RESIZE_SW,

        @NotNull MOVE,
    }

    @NotNull
    private AdjustmentState adjustmentState;

    @Nullable
    private Rectangle2D.Double selectionRect;
    @Nullable
    private Rectangle2D.Double origSelectionRect;

    @Nullable
    private Rectangle2D.Double bandInsertionRect;
    @Nullable
    private BandReportElement insertionBand;

    @Nullable
    private Rectangle2D.Double c1;
    @Nullable
    private Rectangle2D.Double c2;
    @Nullable
    private Rectangle2D.Double c3;
    @Nullable
    private Rectangle2D.Double c4;

    @Nullable
    private Rectangle2D.Double e1;
    @Nullable
    private Rectangle2D.Double e2;
    @Nullable
    private Rectangle2D.Double e3;
    @Nullable
    private Rectangle2D.Double e4;

    @Nullable
    private ReportElement lastClickedElementInfo;

    private boolean selectedInMousePressed;

    private static final double SNAP_TO_GUIDLINE_THRESHOLD = 8;


    public SelectionHandler(@NotNull final ReportPanel reportPanel, @NotNull final ReportElementSelectionModel reportElementSelectionModel)
    {
        this.reportPanel = reportPanel;
        this.reportElementSelectionModel = reportElementSelectionModel;

        pressedPoint = new Point2D.Double();

        c1 = new Rectangle2D.Double();
        c2 = new Rectangle2D.Double();
        c3 = new Rectangle2D.Double();
        c4 = new Rectangle2D.Double();
        e1 = new Rectangle2D.Double();
        e2 = new Rectangle2D.Double();
        e3 = new Rectangle2D.Double();
        e4 = new Rectangle2D.Double();

        adjustmentState = AdjustmentState.NONE;

        selectedInMousePressed = false;

        tempPoint = new Point2D.Double();

        reportElementSelectionModel.addBandElementModelListener(new BandElementModelAdapter()
        {
            public void selectionChanged()
            {
                origSelectionRect = null;
                updateSelectionRects();
            }


            public void layoutChanged()
            {
                origSelectionRect = null;
                updateSelectionRects();
            }
        });

        reportPanel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "SelectionHandler.mouseClicked e = " + e);
                    if (reportElementSelectionModel.getNonDescendentsSelectedElementInfos().size() == 1)
                    {
                        ReportElement reportElement = reportElementSelectionModel.getNonDescendentsSelectedElementInfos().get(0);
                        if (reportElement.isDescendant(reportPanel.getBandToplevelReportElement()))
                        {
                            reportPanel.startInlineEditing(reportElement);
                        }
                    }
                }
            }
        });


        reportPanel.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull final MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if (adjustmentState != AdjustmentState.NONE)
                    {
                        adjustmentState = AdjustmentState.NONE;
                        reportPanel.getBandToplevelReportElement().validate();
                        updateSelectionRects();
                        reportPanel.repaint();
                    }

                    selectedInMousePressed = false;
                    pressedPoint = getModelPoint(e.getPoint(), pressedPoint);

                    if (reportElementSelectionModel.getNonDescendentsSelectedElementInfos().isEmpty() || !selectionContainsMouseEvent(getModelPoint(e.getPoint(), tempPoint)))
                    {
                        Point2D.Double p = getModelPoint(e.getPoint(), tempPoint);
                        ReportElement nextElementInfo = reportElementSelectionModel.getNextElementInfo(reportPanel.getBandToplevelReportElement(), lastClickedElementInfo, p);

                        if (nextElementInfo != null)
                        {
                            int onmask = MouseEvent.CTRL_DOWN_MASK | MouseEvent.BUTTON1_DOWN_MASK;
                            if ((e.getModifiersEx() & onmask) == onmask)
                            {
                                reportElementSelectionModel.addSelectedElement(nextElementInfo);//indirectly updates selection rects and repaints
                            }
                            else
                            {
                                reportElementSelectionModel.setSelection(Arrays.asList(nextElementInfo));//indirectly updates selection rects and repaints
                            }

                            if (selectionRect != null)
                            {
                                origSelectionRect = new Rectangle2D.Double();
                                origSelectionRect.setRect(selectionRect);

                                selectedInMousePressed = true;
                                lastClickedElementInfo = nextElementInfo;
                                adjustmentState = applyAdjustmentStateMouseMoved(getModelPoint(e.getPoint(), tempPoint));
                            }
                        }
                        else
                        {
                            adjustmentState = AdjustmentState.NONE;
                            reportElementSelectionModel.clearSelection();//indirectly updates selection rects and repaints
                            selectedInMousePressed = false;
                        }
                    }
                    else
                    {
                        adjustmentState = applyAdjustmentStateMouseMoved(getModelPoint(e.getPoint(), tempPoint));
                    }
                }


                if (e.isPopupTrigger())
                {
                    reportPanel.requestFocus();
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            CommandManager.createCommandPopupMenu(reportPanel.getReportDialog(), reportPanel.getPlace(), CommandSettings.SIZE_16).showPopupMenu(e);
                        }
                    });
                }
            }


            public void mouseReleased(@NotNull final MouseEvent e)
            {
                BandReportElement insertionBand = SelectionHandler.this.insertionBand;
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    Undo undo = reportPanel.getReportDialog().getUndo();
                    undo.startTransaction(UndoConstants.MOUSE_RELEASED);

                    if (selectionRect != null)
                    {
                        origSelectionRect = new Rectangle2D.Double();
                        origSelectionRect.setRect(selectionRect);
                    }
                    else
                    {
                        origSelectionRect = null;
                    }

                    if (pressedPoint.equals(getModelPoint(e.getPoint(), tempPoint)))
                    {
                        pressedPoint = getModelPoint(e.getPoint(), pressedPoint);

                        if (!selectedInMousePressed)
                        {
                            Point2D.Double p = getModelPoint(e.getPoint(), tempPoint);

                            ReportElement nextElementInfo = reportElementSelectionModel.getNextElementInfo(reportPanel.getBandToplevelReportElement(), lastClickedElementInfo, p);
                            if (nextElementInfo != null)
                            {
                                reportElementSelectionModel.clearSelection();
                                reportElementSelectionModel.addSelectedElement(nextElementInfo);
                                lastClickedElementInfo = nextElementInfo;
                                origSelectionRect = new Rectangle2D.Double();
                                Rectangle2D.Double origSelectionRect = new Rectangle2D.Double();
                                origSelectionRect.setRect(nextElementInfo.getRectangle());
                                ReportElementUtilities.convertRectangle(origSelectionRect, nextElementInfo, null);
                                adjustmentState = applyAdjustmentStateMouseMoved(getModelPoint(e.getPoint(), tempPoint));

                                SelectionHandler.this.origSelectionRect = origSelectionRect;
                            }
                            else
                            {
                                adjustmentState = AdjustmentState.NONE;
                                reportElementSelectionModel.clearSelection();
                                lastClickedElementInfo = null;
                            }
                        }
                    }
                    else if (adjustmentState != AdjustmentState.NONE)
                    {
                        ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getNonDescendentsSelectedElementInfos();
                        ArrayList<Rectangle2D.Double> newRects = new ArrayList<Rectangle2D.Double>();
                        for (ReportElement reportElement : selectedElementInfos)
                        {
                            Rectangle2D.Double rect = new Rectangle2D.Double();//converted
                            rect.setRect(reportElement.getRectangle());
                            ReportElementUtilities.convertRectangle(rect, reportElement, null);

                            double x = Math.max(0, rect.x);
                            double y = Math.max(0, rect.y);
                            double width = Math.min(reportPanel.getReport().getPageDefinition().getInnerPageSize().getWidth(), rect.width);
                            double height = Math.min(reportPanel.getReport().getPageDefinition().getInnerPageSize().getHeight(), rect.height);
                            x = Math.min(x, reportPanel.getReport().getPageDefinition().getInnerPageSize().getWidth() - width);
                            y = Math.min(y, reportPanel.getReport().getPageDefinition().getInnerPageSize().getHeight() - height);
                            newRects.add(new Rectangle2D.Double(x, y, width, height));
                        }

                        if (insertionBand == null)
                        {
                            for (int i = 0; i < selectedElementInfos.size(); i++)
                            {
                                ReportElement reportElement = selectedElementInfos.get(i);
                                Rectangle2D.Double rect = newRects.get(i);

                                if (reportElement.getParent() instanceof BandReportElement)
                                {
                                    reportPanel.getBandToplevelReportElement().addChild(reportElement);
                                }

                                reportElement.setMinimumSize(new DoubleDimension(rect.width, rect.height));
                                reportElement.setPosition(new Point2D.Double(rect.x, rect.y));
                            }
                        }
                        else
                        {
                            for (int i = 0; i < selectedElementInfos.size(); i++)
                            {
                                ReportElement reportElement = selectedElementInfos.get(i);
                                Rectangle2D.Double rect = newRects.get(i);
                                reportElement.setMinimumSize(new DoubleDimension(rect.width, rect.height));

                                Point2D.Double position = new Point2D.Double(rect.x, rect.y);
                                ReportElementUtilities.convertPointToInner(position, insertionBand, null);

                                ReportElement parent = reportElement.getParent();
                                if (parent != null)
                                {
                                    parent.removeChild(reportElement);
                                    reportElement.setPosition(position);
                                    insertionBand.addChild(reportElement);
                                }
                            }
                        }
                    }

                    applyAdjustmentStateMouseMoved(getModelPoint(e.getPoint(), tempPoint));
                    SelectionHandler.this.adjustmentState = AdjustmentState.NONE;

                    updateSubReportElements();

                    undo.endTransaction();
                }

                if (e.isPopupTrigger())
                {
                    reportPanel.requestFocus();
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            CommandManager.createCommandPopupMenu(reportPanel.getReportDialog(), reportPanel.getPlace(), CommandSettings.SIZE_16).showPopupMenu(e);
                        }
                    });
                }

                repaintInsertionRect();
                bandInsertionRect = null;
                SelectionHandler.this.insertionBand = null;
            }


            public void mouseClicked(@NotNull MouseEvent e)
            {
            }


            public void mouseExited(@NotNull MouseEvent e)
            {
            }
        });


        reportPanel.addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseDragged(@NotNull MouseEvent e)
            {
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
                {
                    if (adjustmentState == AdjustmentState.NONE)
                    {
                        return;
                    }
                    else
                    {
                        reportPanel.stopInlineEditing();

                        ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getNonDescendentsSelectedElementInfos();

                        double sf = reportPanel.getScaleFactor();
                        Point2D.Double p = getModelPoint(e.getPoint(), tempPoint);

                        BandReportElement bandReportElement = ReportElementUtilities.getDeepestBandReportElement(p, reportPanel.getBandToplevelReportElement(), selectedElementInfos);
                        if (bandReportElement != null)
                        {
                            bandInsertionRect = bandReportElement.getReportLayoutManager().getDestinationRect(bandReportElement, selectedElementInfos);
                            insertionBand = bandReportElement;
                            repaintInsertionRect();
                        }
                        else
                        {
                            repaintInsertionRect();
                            bandInsertionRect = null;
                            insertionBand = null;
                        }

                        if (adjustmentState == AdjustmentState.MOVE)
                        {
                            checkGuidlinesForTopLine(p, sf, reportPanel);
                            checkGuidlinesForBottomLine(p, sf, reportPanel);
                            checkGuidlinesForLeftLine(p, sf, reportPanel);
                            checkGuidlinesForRightLine(p, sf, reportPanel);

                            checkPageBorderForLeftLine(p);
                            checkPageBorderForRightLine(p);
                            checkPageForTopBorder(p);
                            checkPageForBottomBorder(p);

                            for (ReportElement elementInfo : selectedElementInfos)
                            {
                                Rectangle2D.Double origRectangle = elementInfo.getOrigRectangle();

                                double x = origRectangle.x + (p.getX() - pressedPoint.getX());
                                double y = origRectangle.y + (p.getY() - pressedPoint.getY());
                                double w = origRectangle.width;
                                double h = origRectangle.height;

                                repaintPanels(elementInfo, sf, x, y, w, h, reportElementSelectionModel);
                            }
                        }
                        else if (adjustmentState == AdjustmentState.RESIZE_N)
                        {
                            checkGuidlinesForTopLine(p, sf, reportPanel);

                            checkPageForTopBorder(p);

                            for (ReportElement elementInfo : selectedElementInfos)
                            {
                                Rectangle2D.Double origRectangle = elementInfo.getOrigRectangle();
                                double x = origRectangle.x;
                                double y = Math.min(origRectangle.y + origRectangle.height, origRectangle.y + (p.getY() - pressedPoint.getY()));
                                double w = origRectangle.width;
                                double h = Math.max(0, origRectangle.height - (p.getY() - pressedPoint.getY()));

                                repaintPanels(elementInfo, sf, x, y, w, h, reportElementSelectionModel);
                            }
                        }
                        else if (adjustmentState == AdjustmentState.RESIZE_S)
                        {
                            checkGuidlinesForBottomLine(p, sf, reportPanel);

                            checkPageForBottomBorder(p);

                            for (ReportElement elementInfo : selectedElementInfos)
                            {
                                Rectangle2D.Double origRectangle = elementInfo.getOrigRectangle();
                                double x = origRectangle.x;
                                double y = origRectangle.y;
                                double w = origRectangle.width;
                                double h = Math.max(0, origRectangle.height + (p.getY() - pressedPoint.getY()));

                                repaintPanels(elementInfo, sf, x, y, w, h, reportElementSelectionModel);
                            }
                        }
                        else if (adjustmentState == AdjustmentState.RESIZE_W)
                        {
                            checkGuidlinesForLeftLine(p, sf, reportPanel);

                            checkPageBorderForLeftLine(p);

                            for (ReportElement elementInfo : selectedElementInfos)
                            {
                                Rectangle2D.Double origRectangle = elementInfo.getOrigRectangle();
                                double x = Math.min(origRectangle.x + origRectangle.width, origRectangle.x + (p.getX() - pressedPoint.getX()));
                                double y = origRectangle.y;
                                double w = Math.max(0, origRectangle.width - (p.getX() - pressedPoint.getX()));
                                double h = origRectangle.height;

                                repaintPanels(elementInfo, sf, x, y, w, h, reportElementSelectionModel);
                            }
                        }
                        else if (adjustmentState == AdjustmentState.RESIZE_E)
                        {
                            checkGuidlinesForRightLine(p, sf, reportPanel);

                            checkPageBorderForRightLine(p);

                            for (ReportElement elementInfo : selectedElementInfos)
                            {
                                Rectangle2D.Double origRectangle = elementInfo.getOrigRectangle();
                                double x = origRectangle.x;
                                double y = origRectangle.y;
                                double w = Math.max(0, origRectangle.width + (p.getX() - pressedPoint.getX()));
                                double h = origRectangle.height;

                                repaintPanels(elementInfo, sf, x, y, w, h, reportElementSelectionModel);
                            }
                        }
                        else if (adjustmentState == AdjustmentState.RESIZE_NE)
                        {
                            checkGuidlinesForTopLine(p, sf, reportPanel);
                            checkGuidlinesForRightLine(p, sf, reportPanel);

                            checkPageBorderForRightLine(p);
                            checkPageForTopBorder(p);

                            for (ReportElement elementInfo : selectedElementInfos)
                            {
                                Rectangle2D.Double origRectangle = elementInfo.getOrigRectangle();
                                double x = origRectangle.x;
                                double y = Math.min(origRectangle.y + origRectangle.height, origRectangle.y + (p.getY() - pressedPoint.getY()));
                                double w = Math.max(0, origRectangle.width + (p.getX() - pressedPoint.getX()));
                                double h = Math.max(0, origRectangle.height - (p.getY() - pressedPoint.getY()));

                                repaintPanels(elementInfo, sf, x, y, w, h, reportElementSelectionModel);
                            }
                        }
                        else if (adjustmentState == AdjustmentState.RESIZE_NW)
                        {
                            checkGuidlinesForTopLine(p, sf, reportPanel);
                            checkGuidlinesForLeftLine(p, sf, reportPanel);

                            checkPageBorderForLeftLine(p);
                            checkPageForTopBorder(p);

                            for (ReportElement elementInfo : selectedElementInfos)
                            {
                                Rectangle2D.Double origRectangle = elementInfo.getOrigRectangle();
                                double x = Math.min(origRectangle.x + origRectangle.width, origRectangle.x + (p.getX() - pressedPoint.getX()));
                                double y = Math.min(origRectangle.y + origRectangle.height, origRectangle.y + (p.getY() - pressedPoint.getY()));
                                double w = Math.max(0, origRectangle.width - (p.getX() - pressedPoint.getX()));
                                double h = Math.max(0, origRectangle.height - (p.getY() - pressedPoint.getY()));

                                repaintPanels(elementInfo, sf, x, y, w, h, reportElementSelectionModel);
                            }
                        }
                        else if (adjustmentState == AdjustmentState.RESIZE_SE)
                        {
                            checkGuidlinesForBottomLine(p, sf, reportPanel);
                            checkGuidlinesForRightLine(p, sf, reportPanel);

                            checkPageBorderForRightLine(p);
                            checkPageForBottomBorder(p);

                            for (ReportElement elementInfo : selectedElementInfos)
                            {
                                Rectangle2D.Double origRectangle = elementInfo.getOrigRectangle();
                                double x = origRectangle.x;
                                double y = origRectangle.y;
                                double w = Math.max(0, origRectangle.width + (p.getX() - pressedPoint.getX()));
                                double h = Math.max(0, origRectangle.height + (p.getY() - pressedPoint.getY()));

                                repaintPanels(elementInfo, sf, x, y, w, h, reportElementSelectionModel);
                            }
                        }
                        else if (adjustmentState == AdjustmentState.RESIZE_SW)
                        {
                            checkGuidlinesForBottomLine(p, sf, reportPanel);
                            checkGuidlinesForLeftLine(p, sf, reportPanel);

                            checkPageBorderForLeftLine(p);
                            checkPageForBottomBorder(p);

                            for (ReportElement elementInfo : selectedElementInfos)
                            {
                                Rectangle2D.Double origRectangle = elementInfo.getOrigRectangle();
                                double x = Math.min(origRectangle.x + origRectangle.width, origRectangle.x + (p.getX() - pressedPoint.getX()));
                                double y = origRectangle.y;
                                double w = Math.max(0, origRectangle.width - (p.getX() - pressedPoint.getX()));
                                double h = Math.max(0, origRectangle.height + (p.getY() - pressedPoint.getY()));

                                repaintPanels(elementInfo, sf, x, y, w, h, reportElementSelectionModel);
                            }
                        }
                    }

                    updateSelectionRects();
                }
            }


            public void mouseMoved(@NotNull MouseEvent e)
            {
                applyAdjustmentStateMouseMoved(getModelPoint(e.getPoint(), tempPoint));
            }

        });


    }


    public void updateSubReportElements()
    {
        ArrayList<SubReportElement> subReportElements = new ArrayList<SubReportElement>();
        ArrayList<ReportElement> elementArrayList = reportPanel.getBandToplevelReportElement().getChildren();
        double maxElementY = 0;
        for (ReportElement reportElement : elementArrayList)
        {
            if (reportElement instanceof SubReportElement)
            {
                SubReportElement subReportElement = (SubReportElement) reportElement;
                subReportElements.add(subReportElement);
            }
            else
            {
                maxElementY = Math.max(maxElementY, reportElement.getRectangle().y + reportElement.getRectangle().height);
            }
        }

        int startY = (int) (maxElementY + 1);

        Collections.sort(subReportElements, new Comparator<SubReportElement>()
        {
            public int compare(@NotNull SubReportElement o1, @NotNull SubReportElement o2)
            {
                return o1.getRectangle().y < o2.getRectangle().y ? -1 : o1.getRectangle().y > o2.getRectangle().y ? 1 : 0;
            }
        });

        for (int i = 0; i < subReportElements.size(); i++)
        {
            SubReportElement subReportElement = subReportElements.get(i);
            subReportElement.setPosition(new Point2D.Double(0, startY + i * 20));
            subReportElement.getRectangle().y = startY + i * 20;
        }

        updateSelectionRects();

        //adjust structure according to the visual layout
        BandToplevelReportElement parent = reportPanel.getBandToplevelReportElement();
        for (SubReportElement subReportElement : subReportElements)
        {
            parent.removeChild(subReportElement);
        }
        for (SubReportElement subReportElement : subReportElements)
        {
            parent.addChild(subReportElement);
        }
    }


    private void checkPageForTopBorder(@NotNull Point2D p)
    {
        Rectangle2D.Double rect = origSelectionRect;
        if (rect != null)
        {
            if ((rect.y + (p.getY() - pressedPoint.getY()) < 0))
            {
                p.setLocation(p.getX(), 0 - rect.y + pressedPoint.getY());
            }
        }
    }


    private void checkPageForBottomBorder(@NotNull Point2D p)
    {
        double height = reportPanel.getReport().getPageDefinition().getInnerPageSize().getHeight();
        Rectangle2D.Double rect = origSelectionRect;
        if (rect != null)
        {
            if ((rect.y + rect.height + (p.getY() - pressedPoint.getY()) > height))
            {
                double diff = height - (rect.y + rect.height + (p.getY() - pressedPoint.getY()));
                p.setLocation(p.getX(), p.getY() + diff);
            }
        }
    }


    private void checkPageBorderForLeftLine(@NotNull Point2D p)
    {
        Rectangle2D.Double rect = origSelectionRect;
        if (rect != null)
        {
            if ((rect.x + (p.getX() - pressedPoint.getX()) < 0))
            {
                p.setLocation(0 - rect.x + pressedPoint.getX(), p.getY());
            }
        }
    }


    private void checkPageBorderForRightLine(@NotNull Point2D p)
    {
        double width = reportPanel.getReport().getPageDefinition().getInnerPageSize().getWidth();
        Rectangle2D.Double rect = origSelectionRect;
        if (rect != null)
        {
            if ((rect.x + rect.width + (p.getX() - pressedPoint.getX()) > width))
            {
                double diff = width - (rect.x + rect.width + (p.getX() - pressedPoint.getX()));
                p.setLocation(p.getX() + diff, p.getY());
            }
        }
    }


    private void checkGuidlinesForTopLine(@NotNull Point2D p, double sf, @NotNull ReportPanel reportPanel)
    {
        //check for vertical guidlines
        LinealModel linealModel = reportPanel.getVerticalLinealModel();
        if (linealModel != null)
        {
            LinkedHashSet<GuideLine> verticalGuideLines = linealModel.getGuideLines();
            for (GuideLine guideLine : verticalGuideLines)
            {
                Rectangle2D.Double rect = origSelectionRect;
                if (rect != null)
                {
                    if (guideLine.isActive() &&
                        (rect.y + (p.getY() - pressedPoint.getY())) + SNAP_TO_GUIDLINE_THRESHOLD / sf > guideLine.getPosition() &&
                        (rect.y + (p.getY() - pressedPoint.getY())) - SNAP_TO_GUIDLINE_THRESHOLD / sf < guideLine.getPosition())
                    {
                        double diff = guideLine.getPosition() - (rect.y + (p.getY() - pressedPoint.getY()));
                        p.setLocation(p.getX(), p.getY() + diff);
                        return;
                    }
                }
            }
        }
    }


    private void checkGuidlinesForBottomLine(@NotNull Point2D p, double sf, @NotNull ReportPanel reportPanel)
    {
        //check for vertical guidlines
        LinealModel linealModel = reportPanel.getVerticalLinealModel();
        if (linealModel != null)
        {
            LinkedHashSet<GuideLine> verticalGuideLines = linealModel.getGuideLines();
            for (GuideLine guideLine : verticalGuideLines)
            {
                Rectangle2D.Double rect = origSelectionRect;
                if (rect != null)
                {
                    if (guideLine.isActive() &&
                        (rect.y + rect.height + (p.getY() - pressedPoint.getY())) + SNAP_TO_GUIDLINE_THRESHOLD / sf > guideLine.getPosition() &&
                        (rect.y + rect.height + (p.getY() - pressedPoint.getY())) - SNAP_TO_GUIDLINE_THRESHOLD / sf < guideLine.getPosition())
                    {
                        double diff = guideLine.getPosition() - (rect.y + rect.height + (p.getY() - pressedPoint.getY()));
                        p.setLocation(p.getX(), p.getY() + diff);
                        return;
                    }
                }
            }
        }
    }


    private void checkGuidlinesForLeftLine(@NotNull Point2D p, double sf, @NotNull ReportPanel reportPanel)
    {
        //check for horizontal guidlines
        LinkedHashSet<GuideLine> horizontalGuideLines = reportPanel.getHorizontalLinealModel().getGuideLines();
        for (GuideLine guideLine : horizontalGuideLines)
        {
            Rectangle2D.Double rect = origSelectionRect;
            if (rect != null)
            {
                if (guideLine.isActive() &&
                    (rect.x + (p.getX() - pressedPoint.getX())) + SNAP_TO_GUIDLINE_THRESHOLD / sf > guideLine.getPosition() &&
                    (rect.x + (p.getX() - pressedPoint.getX())) - SNAP_TO_GUIDLINE_THRESHOLD / sf < guideLine.getPosition())
                {
                    double diff = guideLine.getPosition() - (rect.x + (p.getX() - pressedPoint.getX()));
                    p.setLocation(p.getX() + diff, p.getY());
                    return;
                }
            }
        }
    }


    private void checkGuidlinesForRightLine(@NotNull Point2D p, double sf, @NotNull ReportPanel reportPanel)
    {
        //check for horizontal guidlines
        LinkedHashSet<GuideLine> horizontalGuideLines = reportPanel.getHorizontalLinealModel().getGuideLines();
        for (GuideLine guideLine : horizontalGuideLines)
        {
            Rectangle2D.Double rect = origSelectionRect;
            if (rect != null)
            {
                if (guideLine.isActive() &&
                    (rect.x + rect.width + (p.getX() - pressedPoint.getX())) + SNAP_TO_GUIDLINE_THRESHOLD / sf > guideLine.getPosition() &&
                    (rect.x + rect.width + (p.getX() - pressedPoint.getX())) - SNAP_TO_GUIDLINE_THRESHOLD / sf < guideLine.getPosition())
                {
                    double diff = guideLine.getPosition() - (rect.x + rect.width + (p.getX() - pressedPoint.getX()));
                    p.setLocation(p.getX() + diff, p.getY());
                    return;
                }
            }
        }
    }


    private void repaintPanels(@NotNull ReportElement elementInfo, double sf, double x, double y, double w, double h, @NotNull ReportElementSelectionModel reportElementSelectionModel)
    {
        ReportPanel rp = reportElementSelectionModel.getReportPanel(elementInfo);
        if (rp != null)
        {
            repaintElementRectangle(elementInfo, sf, rp);//oldRectangle
            if (rp.getBandToplevelReportElement().getVisualHeight() < y + h)
            {
                rp.getBandToplevelReportElement().setVisualHeight(y + h);
            }
            elementInfo.getRectangle().setRect(x, y, w, h);

            //adjust possible children
            if (elementInfo instanceof BandReportElement)
            {
                BandReportElement bandReportElement = (BandReportElement) elementInfo;
                bandReportElement.getReportLayoutManager().temporarilyLayoutReportElement(bandReportElement);
            }

            repaintElementRectangle(elementInfo, sf, rp);//newRetangle

            rp.getSelectionHandler().updateSelectionRects();
        }
    }


    private void repaintElementRectangle(@NotNull ReportElement elementInfo, double sf, @NotNull ReportPanel reportPanel)
    {
        Rectangle2D.Double rect = new Rectangle2D.Double();
        rect.setRect(elementInfo.getRectangle());

        ReportElementUtilities.convertRectangle(rect, elementInfo, null);
        double bw = Math.max(10, elementInfo.getElementRepaintBorder() * sf);

        int x = (int) ((rect.x) * sf - bw);
        int y = (int) ((rect.y) * sf - bw);
        int width = (int) ((rect.width) * sf + 2 * bw);
        int height = (int) ((rect.height) * sf + 2 * bw);

        reportPanel.repaint(x, y, width, height);
    }


    private void repaintInsertionRect()
    {
        BandReportElement insertionBand = this.insertionBand;
        if (bandInsertionRect != null && insertionBand != null)
        {
            Rectangle2D.Double additionalRect = new Rectangle2D.Double(bandInsertionRect.x - 10, bandInsertionRect.y - 10, bandInsertionRect.width + 20, bandInsertionRect.height + 20);
            ReportElementUtilities.convertRectangleIncludingSource(additionalRect, insertionBand, null);
            reportPanel.repaint(additionalRect.getBounds());
        }
    }


    @NotNull
    private Point2D.Double getModelPoint(@NotNull Point point, @Nullable Point2D.Double dest)
    {
        double sf = reportPanel.getScaleFactor();
        Point2D.Double d = dest;
        if (d == null)
        {
            d = new Point2D.Double();
        }
        d.setLocation(point.x / sf, point.y / sf);
        return d;
    }


    public void updateSelectionRects()
    {
        ArrayList<ReportElement> selected = reportElementSelectionModel.getNonDescendentsSelectedElementInfos();
        ArrayList<ReportElement> selectedElementInfos = new ArrayList<ReportElement>();

        for (ReportElement reportElement : selected)
        {
            if (reportElement.isDescendant(reportPanel.getBandToplevelReportElement()))
            {
                selectedElementInfos.add(reportElement);
            }
        }

        if (!selectedElementInfos.isEmpty())
        {
            selectionRect = null;

            c1 = null;
            c2 = null;
            c3 = null;
            c4 = null;

            e1 = null;
            e2 = null;
            e3 = null;
            e4 = null;

            boolean horizontalLine = false;
            boolean verticalLine = false;
            boolean subreport = false;

            if (selectedElementInfos.size() == 1)
            {
                ReportElement reportElement = selectedElementInfos.iterator().next();
                if (reportElement instanceof LineReportElement)
                {
                    LineReportElement lineReportElement = (LineReportElement) reportElement;
                    if (lineReportElement.getDirection() == LineDirection.HORIZONTAL && lineReportElement.getMinimumSize().getHeight() == 0)
                    {
                        horizontalLine = true;
                    }
                    else if (lineReportElement.getDirection() == LineDirection.VERTICAL && lineReportElement.getMinimumSize().getWidth() == 0)
                    {
                        verticalLine = true;
                    }
                }
                else if (reportElement instanceof SubReportElement)
                {
                    subreport = true;
                }
            }

            for (ReportElement elementInfo : selectedElementInfos)
            {
                if (selectionRect == null)
                {
                    selectionRect = new Rectangle2D.Double();
                    Rectangle2D.Double rect = new Rectangle2D.Double();
                    rect.setRect(elementInfo.getRectangle());
                    ReportElementUtilities.convertRectangle(rect, elementInfo, reportPanel.getBandToplevelReportElement());
                    selectionRect.setRect(rect);
                }
                else
                {
                    Rectangle2D.Double rect = new Rectangle2D.Double();
                    rect.setRect(elementInfo.getRectangle());
                    ReportElementUtilities.convertRectangle(rect, elementInfo, reportPanel.getBandToplevelReportElement());

                    Rectangle2D.union(selectionRect, rect, selectionRect);
                }

                if (elementInfo instanceof SubReportElement)
                {
                    subreport = true;
                }
            }

            if (origSelectionRect == null)
            {
                origSelectionRect = new Rectangle2D.Double();
                origSelectionRect.setRect(selectionRect);
            }

            double sf = reportPanel.getScaleFactor();

            //show resize handles
            //corners
            if (!horizontalLine && !verticalLine && !subreport)
            {
                c1 = new Rectangle2D.Double(selectionRect.x - 2 / sf, selectionRect.y - 2 / sf, 5 / sf, 5 / sf);
                c2 = new Rectangle2D.Double(selectionRect.x + selectionRect.width - 2 / sf, selectionRect.y - 2 / sf, 5 / sf, 5 / sf);
                c3 = new Rectangle2D.Double(selectionRect.x + selectionRect.width - 2 / sf, selectionRect.y + selectionRect.height - 2 / sf, 5 / sf, 5 / sf);
                c4 = new Rectangle2D.Double(selectionRect.x - 2 / sf, selectionRect.y + selectionRect.height - 2 / sf, 5 / sf, 5 / sf);
            }

            //edges
            if (!horizontalLine && !subreport)
            {
                e1 = new Rectangle2D.Double(selectionRect.x + selectionRect.width / 2 - 2 / sf, selectionRect.y - 2 / sf, 5 / sf, 5 / sf);
                e3 = new Rectangle2D.Double(selectionRect.x + selectionRect.width / 2 - 2 / sf, selectionRect.y + selectionRect.height - 2 / sf, 5 / sf, 5 / sf);
            }

            if (!verticalLine && !subreport)
            {
                e2 = new Rectangle2D.Double(selectionRect.x + selectionRect.width - 2 / sf, selectionRect.y + selectionRect.height / 2 - 2 / sf, 5 / sf, 5 / sf);
                e4 = new Rectangle2D.Double(selectionRect.x - 2 / sf, selectionRect.y + selectionRect.height / 2 - 2 / sf, 5 / sf, 5 / sf);
            }
        }
        else
        {
            selectionRect = null;
            origSelectionRect = null;

            c1 = null;
            c2 = null;
            c3 = null;
            c4 = null;

            e1 = null;
            e2 = null;
            e3 = null;
            e4 = null;
        }

    }


    public void paintSelection(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        if (selectionRect != null)
        {
            if (reportPanel.getReportDialog().getDrawSelectionType() == ReportDialog.DrawSelectionType.CLAMP)
            {
                Color origColor = g2d.getColor();
                Stroke origStroke = g2d.getStroke();

                g2d.setColor(Color.LIGHT_GRAY);
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1));

                ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getNonDescendentsSelectedElementInfos();
                for (ReportElement elementInfo : selectedElementInfos)
                {
                    if (elementInfo.isDescendant(reportPanel.getBandToplevelReportElement()))
                    {
                        Rectangle2D.Double rect = new Rectangle2D.Double();
                        rect.setRect(elementInfo.getRectangle());
                        ReportElementUtilities.convertRectangle(rect, elementInfo, reportPanel.getBandToplevelReportElement());

                        drawClampRectangle(g2d, GraphicUtils.getScaledRectangle(graphicsContext, rect));
                    }
                }

                if (selectedElementInfos.size() == 1 && !ReportElementUtilities.isShowInLayoutGUI(selectedElementInfos.get(0)))
                {
                    g2d.setColor(DONT_SHOW_IN_LAYOUT_SELECTION_COLOR);
                    drawClampRectangle(g2d, GraphicUtils.getScaledRectangle(graphicsContext, selectionRect));
                }
                else
                {
                    g2d.setColor(SELECTION_COLOR);
                    drawClampRectangle(g2d, GraphicUtils.getScaledRectangle(graphicsContext, selectionRect));
                }

                g2d.setColor(origColor);
                g2d.setStroke(origStroke);
            }
            else
            {
                Color origColor = g2d.getColor();
                Stroke origStroke = g2d.getStroke();

                g2d.setColor(Color.LIGHT_GRAY);
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, new float[]{2, 2}, 1));

                ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getNonDescendentsSelectedElementInfos();
                for (ReportElement elementInfo : selectedElementInfos)
                {
                    if (elementInfo.isDescendant(reportPanel.getBandToplevelReportElement()))
                    {
                        Rectangle2D.Double rect = new Rectangle2D.Double();
                        rect.setRect(elementInfo.getRectangle());
                        ReportElementUtilities.convertRectangle(rect, elementInfo, reportPanel.getBandToplevelReportElement());

                        drawRectangle(g2d, GraphicUtils.getScaledRectangle(graphicsContext, rect));
                    }
                }

                //draw dotted rectangle
                if (selectedElementInfos.size() == 1 && !ReportElementUtilities.isShowInLayoutGUI(selectedElementInfos.get(0)))
                {
                    g2d.setColor(DONT_SHOW_IN_LAYOUT_SELECTION_COLOR);
                    drawRectangle(g2d, GraphicUtils.getScaledRectangle(graphicsContext, selectionRect));

                    double sf = graphicsContext.getScaleFactor();

                    if (c1 != null) g2d.drawImage(IconLoader.getInstance().getDontShowInLayoutGUISelectionEdge().getImage(), (int) (c1.getX() * sf - 1), (int) (c1.getY() * sf - 1), null);
                    if (c2 != null) g2d.drawImage(IconLoader.getInstance().getDontShowInLayoutGUISelectionEdge().getImage(), (int) (c2.getX() * sf - 1), (int) (c2.getY() * sf - 1), null);
                    if (c3 != null) g2d.drawImage(IconLoader.getInstance().getDontShowInLayoutGUISelectionEdge().getImage(), (int) (c3.getX() * sf - 1), (int) (c3.getY() * sf - 1), null);
                    if (c4 != null) g2d.drawImage(IconLoader.getInstance().getDontShowInLayoutGUISelectionEdge().getImage(), (int) (c4.getX() * sf - 1), (int) (c4.getY() * sf - 1), null);

                    if (e1 != null) g2d.drawImage(IconLoader.getInstance().getDontShowInLayoutGUISelectionEdge().getImage(), (int) (e1.getX() * sf - 1), (int) (e1.getY() * sf - 1), null);
                    if (e2 != null) g2d.drawImage(IconLoader.getInstance().getDontShowInLayoutGUISelectionEdge().getImage(), (int) (e2.getX() * sf - 1), (int) (e2.getY() * sf - 1), null);
                    if (e3 != null) g2d.drawImage(IconLoader.getInstance().getDontShowInLayoutGUISelectionEdge().getImage(), (int) (e3.getX() * sf - 1), (int) (e3.getY() * sf - 1), null);
                    if (e4 != null) g2d.drawImage(IconLoader.getInstance().getDontShowInLayoutGUISelectionEdge().getImage(), (int) (e4.getX() * sf - 1), (int) (e4.getY() * sf - 1), null);
                }
                else
                {
                    g2d.setColor(SELECTION_COLOR);
                    drawRectangle(g2d, GraphicUtils.getScaledRectangle(graphicsContext, selectionRect));

                    double sf = graphicsContext.getScaleFactor();

                    if (c1 != null) g2d.drawImage(IconLoader.getInstance().getSelectionEdge().getImage(), (int) (c1.getX() * sf - 1), (int) (c1.getY() * sf - 1), null);
                    if (c2 != null) g2d.drawImage(IconLoader.getInstance().getSelectionEdge().getImage(), (int) (c2.getX() * sf - 1), (int) (c2.getY() * sf - 1), null);
                    if (c3 != null) g2d.drawImage(IconLoader.getInstance().getSelectionEdge().getImage(), (int) (c3.getX() * sf - 1), (int) (c3.getY() * sf - 1), null);
                    if (c4 != null) g2d.drawImage(IconLoader.getInstance().getSelectionEdge().getImage(), (int) (c4.getX() * sf - 1), (int) (c4.getY() * sf - 1), null);

                    if (e1 != null) g2d.drawImage(IconLoader.getInstance().getSelectionEdge().getImage(), (int) (e1.getX() * sf - 1), (int) (e1.getY() * sf - 1), null);
                    if (e2 != null) g2d.drawImage(IconLoader.getInstance().getSelectionEdge().getImage(), (int) (e2.getX() * sf - 1), (int) (e2.getY() * sf - 1), null);
                    if (e3 != null) g2d.drawImage(IconLoader.getInstance().getSelectionEdge().getImage(), (int) (e3.getX() * sf - 1), (int) (e3.getY() * sf - 1), null);
                    if (e4 != null) g2d.drawImage(IconLoader.getInstance().getSelectionEdge().getImage(), (int) (e4.getX() * sf - 1), (int) (e4.getY() * sf - 1), null);
                }


                g2d.setColor(origColor);
                g2d.setStroke(origStroke);
            }

            if (bandInsertionRect != null)
            {
                g2d.setColor(new Color(100, 150, 100));
                ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getNonDescendentsSelectedElementInfos();
                for (ReportElement elementInfo : selectedElementInfos)
                {
                    BandReportElement insertionBand = this.insertionBand;
                    //noinspection ObjectEquality
                    if (elementInfo != insertionBand && insertionBand != null)
                    {
                        Rectangle2D.Double rect = new Rectangle2D.Double();
                        rect.setRect(bandInsertionRect);

                        ReportElementUtilities.convertRectangleIncludingSource(rect, insertionBand, reportPanel.getBandToplevelReportElement());
                        g2d.draw(GraphicUtils.getScaledRectangle(graphicsContext, rect));
                    }
                }
            }
        }
    }


    private void drawRectangle(@NotNull Graphics2D g2d, @NotNull Rectangle2D.Double rect)
    {
        g2d.drawLine((int) rect.x, (int) rect.y, (int) (rect.x + rect.width), (int) rect.y);
        g2d.drawLine((int) rect.x, (int) (rect.y + rect.height), (int) (rect.x + rect.width), (int) (rect.y + rect.height));
        g2d.drawLine((int) rect.x, (int) rect.y, (int) rect.x, (int) (rect.y + rect.height));
        g2d.drawLine((int) (rect.x + rect.width), (int) rect.y, (int) (rect.x + rect.width), (int) (rect.y + rect.height));
    }


    private void drawClampRectangle(@NotNull Graphics2D g2d, @NotNull Rectangle2D.Double rect)
    {
        //top
        g2d.drawLine((int) rect.x, (int) rect.y, (int) rect.x + 3, (int) rect.y);
        g2d.drawLine((int) rect.x, (int) rect.y, (int) rect.x, (int) rect.y + 3);

        g2d.drawLine((int) (rect.x + rect.width / 2) - 2, (int) rect.y, (int) (rect.x + rect.width / 2) + 2, (int) rect.y);
        g2d.drawLine((int) (rect.x + rect.width / 2), (int) rect.y + 1, (int) (rect.x + rect.width / 2), (int) rect.y + 2);

        g2d.drawLine((int) (rect.x + rect.width - 3), (int) rect.y, (int) (rect.x + rect.width), (int) rect.y);
        g2d.drawLine((int) (rect.x + rect.width), (int) rect.y, (int) (rect.x + rect.width), (int) rect.y + 3);

        //middle
        g2d.drawLine((int) rect.x, (int) (rect.y + rect.height / 2) - 2, (int) rect.x, (int) (rect.y + rect.height / 2) + 2);
        g2d.drawLine((int) rect.x + 1, (int) (rect.y + rect.height / 2), (int) rect.x + 2, (int) (rect.y + rect.height / 2));

        g2d.drawLine((int) (rect.x + rect.width), (int) (rect.y + rect.height / 2) - 2, (int) (rect.x + rect.width), (int) (rect.y + rect.height / 2) + 2);
        g2d.drawLine((int) (rect.x + rect.width - 2), (int) (rect.y + rect.height / 2), (int) (rect.x + rect.width) - 1, (int) (rect.y + rect.height / 2));

        //low
        g2d.drawLine((int) rect.x, (int) (rect.y + rect.height - 3), (int) rect.x, (int) (rect.y + rect.height));
        g2d.drawLine((int) rect.x, (int) (rect.y + rect.height), (int) rect.x + 3, (int) (rect.y + rect.height));

        g2d.drawLine((int) (rect.x + rect.width / 2) - 2, (int) (rect.y + rect.height), (int) (rect.x + rect.width / 2) + 2, (int) (rect.y + rect.height));
        g2d.drawLine((int) (rect.x + rect.width / 2), (int) (rect.y + rect.height) - 2, (int) (rect.x + rect.width / 2), (int) (rect.y + rect.height) - 1);

        g2d.drawLine((int) (rect.x + rect.width - 3), (int) (rect.y + rect.height), (int) (rect.x + rect.width), (int) (rect.y + rect.height));
        g2d.drawLine((int) (rect.x + rect.width), (int) (rect.y + rect.height - 3), (int) (rect.x + rect.width), (int) (rect.y + rect.height));
    }


    private boolean selectionContainsMouseEvent(@NotNull Point2D p)
    {
        if (selectionRect == null)
        {
            return false;
        }

        if (selectionRect.contains(p))
        {
            return true;
        }
        else if (c1 != null && c1.contains(p))
        {
            return true;
        }
        else if (c2 != null && c2.contains(p))
        {
            return true;
        }
        else if (c3 != null && c3.contains(p))
        {
            return true;
        }
        else if (c4 != null && c4.contains(p))
        {
            return true;
        }
        else if (e1 != null && e1.contains(p))
        {
            return true;
        }
        else if (e2 != null && e2.contains(p))
        {
            return true;
        }
        else if (e3 != null && e3.contains(p))
        {
            return true;
        }
        else if (e4 != null && e4.contains(p))
        {
            return true;
        }
        else
        {
            if (reportElementSelectionModel.getNonDescendentsSelectedElementInfos().size() == 1)
            {
                ReportElement reportElement = reportElementSelectionModel.getNonDescendentsSelectedElementInfos().iterator().next();
                if (reportElement instanceof LineReportElement)
                {
                    LineReportElement lineReportElement = (LineReportElement) reportElement;
                    if (lineReportElement.getMinimumSize().getHeight() == 0 || lineReportElement.getMinimumSize().getWidth() == 0)
                    {
                        Rectangle2D.Double rect = new Rectangle2D.Double(lineReportElement.getPosition().x - 3, lineReportElement.getPosition().y - 3, lineReportElement.getMinimumSize().getWidth() + 6, lineReportElement.getMinimumSize().getHeight() + 6);
                        ReportElementUtilities.convertRectangle(rect, lineReportElement, null);
                        if (rect.contains(p))
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "SelectionHandler.selectionContainsMouseEvent rect = " + rect);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    @NotNull
    private AdjustmentState applyAdjustmentStateMouseMoved(@NotNull Point2D p)
    {
        Rectangle2D.Double sr = selectionRect;
        if (sr == null)
        {
            reportPanel.setCursor(Cursor.getDefaultCursor());
            return AdjustmentState.NONE;
        }

        if (c3 != null && c3.contains(p))
        {
            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            return AdjustmentState.RESIZE_SE;
        }
        else if (e2 != null && e2.contains(p))
        {
            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            return AdjustmentState.RESIZE_E;
        }
        else if (c2 != null && c2.contains(p))
        {
            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
            return AdjustmentState.RESIZE_NE;
        }
        else if (e3 != null && e3.contains(p))
        {
            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
            return AdjustmentState.RESIZE_S;
        }
        else if (c4 != null && c4.contains(p))
        {
            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
            return AdjustmentState.RESIZE_SW;
        }
        else if (c1 != null && c1.contains(p))
        {
            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
            return AdjustmentState.RESIZE_NW;
        }
        else if (e1 != null && e1.contains(p))
        {
            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            return AdjustmentState.RESIZE_N;
        }
        else if (e4 != null && e4.contains(p))
        {
            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            return AdjustmentState.RESIZE_W;
        }
        else if (sr.contains(p))
        {
            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            return AdjustmentState.MOVE;
        }
        else
        {
            //special handling for lines
            if (sr.getHeight() == 0 || sr.getWidth() == 0)
            {
                ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getNonDescendentsSelectedElementInfos();
                if (selectedElementInfos.size() == 1)
                {
                    ReportElement reportElement = selectedElementInfos.iterator().next();
                    if (reportElement instanceof LineReportElement)
                    {
                        Rectangle2D.Double widenedSelectionRect = new Rectangle2D.Double(sr.getX() - 3, sr.getY() - 3, sr.getWidth() + 6, sr.getHeight() + 6);
                        if (widenedSelectionRect.contains(p))
                        {
                            reportPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                            return AdjustmentState.MOVE;
                        }
                    }
                }
            }
        }

        reportPanel.setCursor(Cursor.getDefaultCursor());
        return AdjustmentState.NONE;
    }

}
