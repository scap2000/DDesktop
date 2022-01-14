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
package org.pentaho.reportdesigner.crm.report.palette;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfo;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 11:47:10
 */
public class ReportElementPalette extends JPanel implements Scrollable
{
    @Nullable
    private ReportElementInfo reportElementInfo;
    @NotNull
    private ArrayList<ReportElementInfoPanel> reportElementInfoPanels;

    @NotNull
    private Border selectionBorder;
    @NotNull
    private Border noSelectionBorder;
    @NotNull
    private Color selectionBackground;
    @NotNull
    private Color selectionForeground;
    @NotNull
    private Color normalForeground;
    @NotNull
    private Color normalBackground;


    public ReportElementPalette()
    {
        reportElementInfoPanels = new ArrayList<ReportElementInfoPanel>();

        //setOpaque(true);
        //setBackground(Color.WHITE);

        JList list = new JList();//just to get colors
        selectionBackground = list.getSelectionBackground();
        selectionForeground = list.getSelectionForeground();
        normalForeground = list.getForeground();
        normalBackground = getBackground();

        Border selectionBorder = UIManager.getBorder("List.focusSelectedCellHighlightBorder");//NON-NLS
        if (selectionBorder == null)
        {
            UIManager.getBorder("List.focusCellHighlightBorder");//NON-NLS
        }
        if (selectionBorder == null)
        {
            selectionBorder = BorderFactory.createLineBorder(Color.BLACK);
        }
        this.selectionBorder = selectionBorder;
        noSelectionBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new PaletteLayoutManager());

        addReportElement(ReportElementInfoFactory.getInstance().getLabelReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getResourceLabelReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getResourceMessageReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getAnchorFieldReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getChartReportElementInfo());  
        
        addReportElement(ReportElementInfoFactory.getInstance().getStaticImageReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo());

        addReportElement(ReportElementInfoFactory.getInstance().getRectangleReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getEllipseReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getLineReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo());

        addReportElement(ReportElementInfoFactory.getInstance().getBandReportElementInfo());
        addReportElement(ReportElementInfoFactory.getInstance().getSubReportElementInfo());

        initDragAndDrop();
    }


    private void addReportElement(@NotNull final ReportElementInfo reportElementInfo)
    {
        ReportElementInfoPanel reportElementInfoPanel = new ReportElementInfoPanel(reportElementInfo, selectionForeground, selectionBackground, normalForeground, normalBackground, selectionBorder, noSelectionBorder);

        reportElementInfoPanel.addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseDragged(@NotNull MouseEvent e)
            {
                ReportElementPalette.this.reportElementInfo = reportElementInfo;

                TransferHandler handler = ReportElementPalette.this.getTransferHandler();
                handler.exportAsDrag(ReportElementPalette.this, e, TransferHandler.COPY);
            }


            public void mouseMoved(@NotNull MouseEvent e)
            {
            }
        });

        reportElementInfoPanels.add(reportElementInfoPanel);

        add(reportElementInfoPanel);
        revalidate();
        repaint();
    }


    private void initDragAndDrop()
    {
        @NonNls
        final DataFlavor dataFlavorLibraryItems = new DataFlavor("application/x-icore-reportelement;class=" + ReportElement.class.getName(), "ReportElement");


        setTransferHandler(new TransferHandler()
        {
            @NotNull
            protected Transferable createTransferable(@NotNull JComponent c)
            {
                final ReportElement[] reportElement = new ReportElement[1];
                if (reportElementInfo != null)
                {
                    reportElement[0] = reportElementInfo.createReportElement();
                }

                return new Transferable()
                {
                    @NotNull
                    public DataFlavor[] getTransferDataFlavors()
                    {
                        return new DataFlavor[]{dataFlavorLibraryItems};
                    }


                    public boolean isDataFlavorSupported(@NotNull DataFlavor flavor)
                    {
                        return dataFlavorLibraryItems.equals(flavor);
                    }


                    @NotNull
                    public Object getTransferData(@NotNull DataFlavor flavor) throws UnsupportedFlavorException
                    {
                        if (dataFlavorLibraryItems.equals(flavor))
                        {
                            return reportElement[0];
                        }
                        else
                        {
                            throw new UnsupportedFlavorException(flavor);
                        }
                    }
                };
            }


            public int getSourceActions(@NotNull JComponent c)
            {
                if (reportElementInfo != null)
                {
                    return DnDConstants.ACTION_COPY;
                }
                return TransferHandler.NONE;
            }
        });


    }


    @NotNull
    public Dimension getPreferredScrollableViewportSize()
    {
        return getPreferredSize();
    }


    public int getScrollableUnitIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
    {
        return 10;
    }


    public int getScrollableBlockIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
    {
        return 50;
    }


    public boolean getScrollableTracksViewportWidth()
    {
        if (getParent() instanceof JViewport)
        {
            return (getParent().getWidth() > getMinimumSize().width);
        }
        return false;
    }


    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }


    public void dropCompleted()
    {
        for (ReportElementInfoPanel reportElementInfoPanel : reportElementInfoPanels)
        {
            reportElementInfoPanel.dropCompleted();
        }
    }
}
