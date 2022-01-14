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
import org.pentaho.reportdesigner.crm.report.lineal.HorizontalLinealComponent;
import org.pentaho.reportdesigner.crm.report.lineal.LinealModel;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.zoom.ZoomModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 26.01.2006
 * Time: 08:00:56
 */
public class SideLinealComponent extends JComponent
{
    @NotNull
    private BandsComponent bandsComponent;
    @NotNull
    private JScrollPane scrollPane;

    @NotNull
    private HorizontalLinealComponent horizontalLineal;
    @NotNull
    private JLabel percentageLabel;
    @NotNull
    private ReportDialog reportDialog;

    private int lastScaleFactor;


    public SideLinealComponent(@NotNull ReportDialog reportDialog, @NotNull LinealModel horizontalLinealModel)
    {
        this.reportDialog = reportDialog;
        setLayout(new LM());

        horizontalLineal = new HorizontalLinealComponent(reportDialog, horizontalLinealModel);

        bandsComponent = new BandsComponent();
        scrollPane = new JScrollPane();

        percentageLabel = new JLabel(" ");
    }


    @NotNull
    public BandsComponent getBandsComponent()
    {
        return bandsComponent;
    }


    public void updateReportBorderPanels(@NotNull ArrayList<ReportBorderPanel> reportBorderPanels)
    {
        removeAll();

        bandsComponent.updateReportBorderPanels(reportBorderPanels);

        scrollPane.setViewportView(bandsComponent);

        scrollPane.setColumnHeaderView(horizontalLineal);
        scrollPane.setRowHeaderView(bandsComponent.getBandsComponentHelper());

        scrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener()
        {
            public void adjustmentValueChanged(@NotNull AdjustmentEvent e)
            {
                horizontalLineal.revalidate();
                horizontalLineal.repaint();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        percentageLabel = new JLabel(" ");
        percentageLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(percentageLabel, BorderLayout.CENTER);

        percentageLabel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    ZoomModel zoomModel = reportDialog.getZoomModel();
                    if (zoomModel != null)
                    {
                        zoomModel.setZoomFactor(1000);
                    }
                }
            }
        });

        final int[] x = new int[1];
        percentageLabel.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull MouseEvent e)
            {
                x[0] = e.getX();
            }
        });

        percentageLabel.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(@NotNull MouseEvent e)
            {
                ZoomModel zoomModel = reportDialog.getZoomModel();
                if (zoomModel != null)
                {
                    int diff = x[0] - e.getX();
                    int zoomFactor = zoomModel.getZoomFactor();
                    zoomModel.setZoomFactor(Math.min(5000, Math.max(100, zoomFactor - diff * 10)));
                    x[0] = e.getX();
                }
            }
        });


        percentageLabel.addMouseWheelListener(new MouseWheelListener()
        {
            public void mouseWheelMoved(@NotNull MouseWheelEvent e)
            {
                ZoomModel zoomModel = reportDialog.getZoomModel();
                if (zoomModel != null)
                {
                    int diff = e.getWheelRotation() * 100;
                    int zoomFactor = zoomModel.getZoomFactor();
                    int newZoomFactor = Math.min(5000, Math.max(100, zoomFactor + diff));
                    newZoomFactor = newZoomFactor / 100 * 100;
                    zoomModel.setZoomFactor(newZoomFactor);
                }
            }
        });

        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, panel);

        add(scrollPane);

        setScaleFactor(lastScaleFactor);

        revalidate();
        repaint();
    }


    public void setScaleFactor(int sf)
    {
        lastScaleFactor = sf;

        percentageLabel.setText(sf / 10 + "%");
        horizontalLineal.setScaleFactor(sf / 1000.);
        bandsComponent.setScaleFactor(sf / 1000.);
    }


    @NotNull
    public HorizontalLinealComponent getHorizontalLineal()
    {
        return horizontalLineal;
    }


    public void adjustToPerfectSize()
    {
        ArrayList<ReportBorderPanel> reportBorderPanels = bandsComponent.getReportBorderPanels();
        for (ReportBorderPanel reportBorderPanel : reportBorderPanels)
        {
            double height = getOptimalHeight(reportBorderPanel.getReportPanel().getBandToplevelReportElement());
            reportBorderPanel.getReportPanel().getBandToplevelReportElement().setVisualHeight(Math.max(height, reportBorderPanel.isShowTopBorder() ? 0 : 5));
        }

        revalidate();
        repaint();
    }


    private double getOptimalHeight(@NotNull ReportElement reportElement)
    {
        double m = reportElement.getMinimumSize().getHeight() + reportElement.getPosition().y;
        double b = reportElement.getPreferredSize().getHeight() + reportElement.getPosition().y;
        m = Math.max(m, b);

        for (ReportElement element : reportElement.getChildren())
        {
            m = Math.max(m, getOptimalHeight(element));
        }

        if (reportElement.getMaximumSize().getHeight() > 0)
        {
            m = Math.min(m, reportElement.getMaximumSize().getHeight());
        }

        return m;
    }


    private class LM implements LayoutManager2
    {
        public void addLayoutComponent(@NotNull Component comp, @Nullable Object constraints)
        {
        }


        @NotNull
        public Dimension maximumLayoutSize(@NotNull Container target)
        {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }


        public float getLayoutAlignmentX(@NotNull Container target)
        {
            return 0;
        }


        public float getLayoutAlignmentY(@NotNull Container target)
        {
            return 0;
        }


        public void invalidateLayout(@NotNull Container target)
        {
        }


        public void addLayoutComponent(@NotNull String name, @NotNull Component comp)
        {
        }


        public void removeLayoutComponent(@NotNull Component comp)
        {
        }


        @NotNull
        public Dimension preferredLayoutSize(@NotNull Container parent)
        {
            return new Dimension(100, bandsComponent.getPreferredSize().height + 8 + 20);
        }


        @NotNull
        public Dimension minimumLayoutSize(@NotNull Container parent)
        {
            return new Dimension(100, 100);
        }


        public void layoutContainer(@NotNull Container parent)
        {
            int topLinealHeight = 0;
            int leftLinealWidth = 0;
            int grabberWidth = 0;

            scrollPane.setBounds(grabberWidth + leftLinealWidth, topLinealHeight, getWidth() - (leftLinealWidth + grabberWidth), getHeight() - topLinealHeight);
        }
    }
}
