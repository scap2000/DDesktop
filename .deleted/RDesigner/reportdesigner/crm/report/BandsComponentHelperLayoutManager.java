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
import org.pentaho.reportdesigner.crm.report.lineal.VerticalLabel;

import java.awt.*;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 18:32:17
 */
public class BandsComponentHelperLayoutManager implements LayoutManager2
{
    @NotNull
    private BandsComponentHelper bandsComponentHelper;


    public BandsComponentHelperLayoutManager(@NotNull BandsComponentHelper bandsComponent)
    {
        //noinspection ConstantConditions
        if (bandsComponent == null)
        {
            throw new IllegalArgumentException("bandsComponent must not be null");
        }
        this.bandsComponentHelper = bandsComponent;
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
        Dimension dim = new Dimension(35, 0);
        ArrayList<ReportBorderPanel> panelInfos = bandsComponentHelper.getBandsComponent().getReportBorderPanels();
        for (ReportBorderPanel panelInfo : panelInfos)
        {
            double topBorder = panelInfo.isShowTopBorder() ? panelInfo.getReportPanel().getReport().getPageDefinition().getTopBorder() : 0;
            dim.height += (int) ((panelInfo.getReportPanel().getBandToplevelReportElement().getVisualHeight() + topBorder) * panelInfo.getReportPanel().getScaleFactor());
        }

        dim.height += 10;

        return dim;
    }


    public void layoutContainer(@NotNull Container parent)
    {
        int grabberWidth = 20;
        int linealWidth = 15;

        int currentY = 0;

        ArrayList<ReportBorderPanel> panelInfos = bandsComponentHelper.getBandsComponent().getReportBorderPanels();
        for (int i = 0; i < panelInfos.size(); i++)
        {
            ReportBorderPanel panelInfo = panelInfos.get(i);
            double topBorder = panelInfo.isShowTopBorder() ? panelInfo.getReportPanel().getReport().getPageDefinition().getTopBorder() : 0;
            int height = (int) ((panelInfo.getReportPanel().getBandToplevelReportElement().getVisualHeight() + topBorder) * panelInfo.getReportPanel().getScaleFactor());
            bandsComponentHelper.getBandsComponent().getVerticalLineals().get(i).setBounds(grabberWidth, currentY, linealWidth, height);
            VerticalLabel verticalLabel = bandsComponentHelper.getBandsComponent().getVerticalLabels().get(i);
            verticalLabel.setBounds(0, i == 0 ? 0 : currentY + 3, grabberWidth, height - 3);
            currentY += height;
            GrabberComponent grabberComponent = bandsComponentHelper.getBandsComponent().getGrabberComponents().get(i);
            grabberComponent.setBounds(0, currentY - 3, grabberWidth, 6);
        }
    }
}
