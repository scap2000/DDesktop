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

import java.awt.*;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 18:32:17
 */
public class BandsComponentLayoutManager implements LayoutManager2
{
    @NotNull
    private BandsComponent bandsComponent;


    public BandsComponentLayoutManager(@NotNull BandsComponent bandsComponent)
    {
        //noinspection ConstantConditions
        if (bandsComponent == null)
        {
            throw new IllegalArgumentException("bandsComponent must not be null");
        }
        this.bandsComponent = bandsComponent;
    }


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
        return calculateSize();
    }


    @NotNull
    public Dimension minimumLayoutSize(@NotNull Container parent)
    {
        return calculateSize();
    }


    @NotNull
    private Dimension calculateSize()
    {
        Dimension dim = new Dimension(100, 0);
        ArrayList<ReportBorderPanel> panelInfos = bandsComponent.getReportBorderPanels();
        for (ReportBorderPanel reportBorderPanel : panelInfos)
        {
            dim.width = reportBorderPanel.getPreferredSize().width;
            double topBorder = reportBorderPanel.isShowTopBorder() ? reportBorderPanel.getReportPanel().getReport().getPageDefinition().getTopBorder() : 0;
            dim.height += (int) ((reportBorderPanel.getReportPanel().getBandToplevelReportElement().getVisualHeight() + topBorder) * reportBorderPanel.getReportPanel().getScaleFactor());
        }

        dim.height += 10;

        return dim;
    }


    public void layoutContainer(@NotNull Container parent)
    {
        int grabberWidth = 0;
        int width = parent.getWidth() - grabberWidth;

        int currentY = 0;

        ArrayList<ReportBorderPanel> panelInfos = bandsComponent.getReportBorderPanels();
        for (int i = 0; i < panelInfos.size(); i++)
        {
            ReportBorderPanel reportBorderPanel = panelInfos.get(i);
            double topBorder = reportBorderPanel.isShowTopBorder() ? reportBorderPanel.getReportPanel().getReport().getPageDefinition().getTopBorder() : 0;
            reportBorderPanel.setBounds(grabberWidth, currentY, width, (int) ((reportBorderPanel.getReportPanel().getBandToplevelReportElement().getVisualHeight() + topBorder) * reportBorderPanel.getReportPanel().getScaleFactor()));
            currentY += (int) ((reportBorderPanel.getReportPanel().getBandToplevelReportElement().getVisualHeight() + topBorder) * reportBorderPanel.getReportPanel().getScaleFactor());

            GrabberComponent grabberComponent = bandsComponent.getGrabberComponents().get(i);
            grabberComponent.setBounds(0, currentY - 3, grabberWidth, 6);
        }

        bandsComponent.getBottomHelperPanel().setBounds(0, currentY, width, parent.getHeight() - currentY);
    }
}
