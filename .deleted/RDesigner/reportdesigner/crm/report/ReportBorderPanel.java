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
import org.pentaho.reportdesigner.crm.report.lineal.GuideLine;
import org.pentaho.reportdesigner.crm.report.lineal.LinealModelListener;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 13.11.2005
 * Time: 10:48:03
 */
public class ReportBorderPanel extends JComponent implements Scrollable
{

    @NotNull
    private ReportPanel reportPanel;
    private boolean showTopBorder;
    private boolean showBottomBorder;
    @NotNull
    public static final Color BORDER_COLOR = new Color(240, 240, 240);

    private boolean showLeftBorder = true;


    public ReportBorderPanel(@NotNull final ReportPanel reportPanel, boolean showTopBorder, boolean showBottomBorder)
    {
        this.reportPanel = reportPanel;
        this.showTopBorder = showTopBorder;
        this.showBottomBorder = showBottomBorder;

        add(reportPanel);
        setLayout(new ReportBorderLayoutManager());

        LinealModelListener linealModelListener = new LinealModelListener()
        {
            public void guidLineAdded(@NotNull GuideLine guideLine)
            {
                repaint();
            }


            public void guidLineRemoved(@NotNull GuideLine guideLine)
            {
                repaint();
            }


            public void activationChanged(@NotNull GuideLine guideLine)
            {
                repaint();
            }


            public void positionChanged(@NotNull GuideLine guideLine, double oldPosition)
            {
                repaint();
            }
        };

        reportPanel.getVerticalLinealModel().addLinealModelListener(linealModelListener);
        reportPanel.getHorizontalLinealModel().addLinealModelListener(linealModelListener);

        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull MouseEvent e)
            {
                reportPanel.getBandElementModel().setSelection(Arrays.asList(reportPanel.getBandToplevelReportElement()));
                reportPanel.requestFocusInWindow();
            }
        });

    }


    @NotNull
    public ReportPanel getReportPanel()
    {
        return reportPanel;
    }


    public boolean isShowTopBorder()
    {
        return showTopBorder;
    }


    public boolean isShowBottomBorder()
    {
        return showBottomBorder;
    }


    protected void paintComponent(@NotNull Graphics g)
    {
        g.setColor(BORDER_COLOR);

        PageDefinition pageDefinition = reportPanel.getReport().getPageDefinition();
        double sf = reportPanel.getScaleFactor();

        int width = (int) ((pageDefinition.getLeftBorder() + pageDefinition.getRightBorder() + pageDefinition.getInnerPageSize().getWidth()) * sf) + 1;
        int height = (int) (((showTopBorder ? pageDefinition.getTopBorder() : 0) + (showBottomBorder ? pageDefinition.getBottomBorder() : 0) + pageDefinition.getInnerPageSize().getHeight()) * sf) + 1;

        g.setColor(BORDER_COLOR);
        Insets insets = getInsets();
        g.fillRect(insets.left, insets.top, width, height);
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, 0, getWidth(), 0);
        g.drawRect(-1 + insets.left, -1 + insets.top, width - 1 + 1 /*- (insets.left + insets.right + 1)*/, height /*- (insets.top + insets.bottom + 1)*/);

        g.setColor(new Color(240, 170, 170));
        double start = 0;
        if (showTopBorder)
        {
            start = pageDefinition.getTopBorder();
        }
        LinkedHashSet<GuideLine> verticalGuideLines = reportPanel.getVerticalLinealModel().getGuideLines();
        for (GuideLine guideLine : verticalGuideLines)
        {
            if (guideLine.isActive())
            {
                int ts = (int) (start * reportPanel.getScaleFactor());
                int y = (int) (guideLine.getPosition() * reportPanel.getScaleFactor()) + ts;
                g.drawLine(0, y, getWidth(), y);
            }
        }

        double leftStart = 0;
        if (showLeftBorder)
        {
            leftStart = pageDefinition.getLeftBorder();
        }
        LinkedHashSet<GuideLine> horizontalGuideLines = reportPanel.getHorizontalLinealModel().getGuideLines();
        for (GuideLine guideLine : horizontalGuideLines)
        {
            if (guideLine.isActive())
            {
                int ls = (int) (leftStart * reportPanel.getScaleFactor());
                int x = (int) (guideLine.getPosition() * reportPanel.getScaleFactor()) + ls;
                g.drawLine(x, 0, x, getHeight());
            }
        }

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
        return 10;
    }


    public boolean getScrollableTracksViewportWidth()
    {
        return false;
    }


    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }


    private class ReportBorderLayoutManager implements LayoutManager
    {

        public void addLayoutComponent(@NotNull String name, @NotNull Component comp)
        {
        }


        public void removeLayoutComponent(@NotNull Component comp)
        {
        }


        @NotNull
        public Dimension preferredLayoutSize(@NotNull Container parent)
        {
            return calculateSize(parent);

        }


        @NotNull
        public Dimension minimumLayoutSize(@NotNull Container parent)
        {
            return calculateSize(parent);
        }


        @NotNull
        private Dimension calculateSize(@NotNull Container parent)
        {
            ReportPanel reportPanel = (ReportPanel) parent.getComponent(0);
            PageDefinition pageDefinition = reportPanel.getReport().getPageDefinition();
            double sf = reportPanel.getScaleFactor();

            Insets insets = getInsets();
            return new Dimension((int) ((pageDefinition.getLeftBorder() + pageDefinition.getRightBorder() + pageDefinition.getInnerPageSize().getWidth()) * sf) + 1 + insets.left + insets.right,
                                 (int) (((showTopBorder ? pageDefinition.getTopBorder() : 0) + (showBottomBorder ? pageDefinition.getBottomBorder() : 0) + pageDefinition.getInnerPageSize().getHeight()) * sf) + 1 + insets.top + insets.bottom);
        }


        public void layoutContainer(@NotNull Container parent)
        {
            ReportPanel reportPanel = (ReportPanel) parent.getComponent(0);
            PageDefinition pageDefinition = reportPanel.getReport().getPageDefinition();
            double sf = reportPanel.getScaleFactor();

            Insets insets = getInsets();


            reportPanel.setBounds((int) (pageDefinition.getLeftBorder() * sf) + insets.left,
                                  (int) ((showTopBorder ? pageDefinition.getTopBorder() : 0) * sf) + insets.top,
                                  (int) ((pageDefinition.getInnerPageSize().getWidth()) * sf + 1),
                                  (int) ((pageDefinition.getInnerPageSize().getHeight()) * sf + 1));
        }
    }
}


