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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * User: Martin
 * Date: 03.02.2006
 * Time: 09:18:36
 */
public class PaletteLayoutManager implements LayoutManager2
{
    public void addLayoutComponent(@NotNull Component comp, @Nullable Object constraints)
    {
    }


    @Nullable
    public Dimension maximumLayoutSize(@NotNull Container target)
    {
        return null;
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
        return calculateSize(parent);
    }


    @NotNull
    public Dimension minimumLayoutSize(@NotNull Container parent)
    {
        int height = 10;
        int width = 0;
        int cc = parent.getComponentCount();
        for (int i = 0; i < cc; i++)
        {
            height = Math.max(height, parent.getComponent(i).getPreferredSize().height);
            width = Math.max(width, parent.getComponent(i).getPreferredSize().width);
        }
        return new Dimension(width + parent.getInsets().left + parent.getInsets().right, height + parent.getInsets().top + parent.getInsets().bottom);
    }


    @NotNull
    private Dimension calculateSize(@NotNull Container parent)
    {
        int width = parent.getWidth() - (parent.getInsets().left + parent.getInsets().right);

        int gridWidth = 1;
        int gridHeight = 1;

        //search widest component
        int cc = parent.getComponentCount();
        for (int i = 0; i < cc; i++)
        {
            gridWidth = Math.max(gridWidth, parent.getComponent(i).getPreferredSize().width);
            gridHeight = Math.max(gridHeight, parent.getComponent(i).getPreferredSize().height);
        }

        int columnCount = Math.max(1, width / gridWidth);
        int compsPerColumn = cc / columnCount + 1;

        return new Dimension(columnCount * gridWidth + parent.getInsets().left + parent.getInsets().right,
                             compsPerColumn * gridHeight + parent.getInsets().top + parent.getInsets().bottom);
    }


    public void layoutContainer(@NotNull Container parent)
    {
        int width = parent.getWidth() - (parent.getInsets().left + parent.getInsets().right);

        int gridWidth = 1;
        int gridHeight = 1;

        //search widest component
        int cc = parent.getComponentCount();
        for (int i = 0; i < cc; i++)
        {
            gridWidth = Math.max(gridWidth, parent.getComponent(i).getPreferredSize().width);
            gridHeight = Math.max(gridHeight, parent.getComponent(i).getPreferredSize().height);
        }

        int columnCount = Math.max(1, width / gridWidth);

        int currentColumn = 0;
        int currentRow = 0;
        for (int i = 0; i < cc; i++)
        {
            parent.getComponent(i).setBounds(currentColumn * gridWidth + parent.getInsets().left,
                                             currentRow * gridHeight + parent.getInsets().top,
                                             gridWidth,
                                             gridHeight);

            currentColumn++;
            if (currentColumn >= columnCount)
            {
                currentRow++;
                currentColumn = 0;
            }
        }
    }
}
