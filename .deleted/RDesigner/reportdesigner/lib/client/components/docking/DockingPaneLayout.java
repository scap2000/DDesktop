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
package org.pentaho.reportdesigner.lib.client.components.docking;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 05.11.2004
 * Time: 13:14:08
 */
public class DockingPaneLayout implements LayoutManager
{
    private int maxX;
    private int maxY;


    public DockingPaneLayout()
    {
        maxX = 100;
        maxY = 100;
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
        return new Dimension(maxX, maxY);
    }


    @NotNull
    public Dimension minimumLayoutSize(@NotNull Container parent)
    {
        return new Dimension(maxX, maxY);
    }


    public void layoutContainer(@NotNull Container parent)
    {
        synchronized (parent.getTreeLock())
        {
            DockingPane dockingPane = (DockingPane) parent;

            JPanel[] buttonBorders = dockingPane.getButtonBorders();

            BorderComponent[] borderComponents = dockingPane.getDockedComponents();

            int w = dockingPane.getWidth();
            int h = dockingPane.getHeight();


            int tBH = buttonBorders[0].getPreferredSize().height;
            //int tBW = buttonBorders[0].getPreferredSize().width;

            int bBH = buttonBorders[1].getPreferredSize().height;
            //int bBW = buttonBorders[1].getPreferredSize().width;

            //int lBH = buttonBorders[2].getPreferredSize().height;
            int lBW = buttonBorders[2].getPreferredSize().width;

            //int rBH = buttonBorders[3].getPreferredSize().height;
            int rBW = buttonBorders[3].getPreferredSize().width;

            //int tCW = borderComponents[0].getPreferredSize().width;
            int tCH = borderComponents[0].getPreferredSize().height;

            //int bCW = borderComponents[1].getPreferredSize().width;
            int bCH = borderComponents[1].getPreferredSize().height;

            int lCW = borderComponents[2].getPreferredSize().width;
            //int lCH = borderComponents[2].getPreferredSize().height;

            int rCW = borderComponents[3].getPreferredSize().width;
            //int rCH = borderComponents[3].getPreferredSize().height;

            if (tBH + tCH + bBH + bCH > h)
            {
                double divideRatio = bCH / (double) (tCH + bCH);

                int innerHeight = h - (tBH + bBH);

                int bAllowedH = (int) (innerHeight * divideRatio);//h without border divided proportionally
                if (bAllowedH < borderComponents[1].getDragPanel().getHeight())
                {
                    bAllowedH = borderComponents[1].getDragPanel().getHeight();
                }
                int tAllowedH = innerHeight - bAllowedH;//buttom gets rest of available space
                if (tAllowedH < borderComponents[0].getDragPanel().getHeight())
                {
                    tAllowedH = borderComponents[0].getDragPanel().getHeight();
                }
                tCH = tAllowedH;
                bCH = bAllowedH;

                borderComponents[0].setToolWindowHeight(tCH);
                borderComponents[1].setToolWindowHeight(bCH);
            }

            if (lBW + lCW + rBW + rCW > w)
            {
                double divideRatio = rCW / (double) (lCW + rCW);

                int innerWidth = w - (lBW + rBW);

                int bAllowedW = (int) (innerWidth * divideRatio);//h without border divided proportionally
                if (bAllowedW < borderComponents[3].getDragPanel().getWidth())
                {
                    bAllowedW = borderComponents[3].getDragPanel().getWidth();
                }
                int tAllowedW = innerWidth - bAllowedW;//buttom gets rest of available space
                if (tAllowedW < borderComponents[2].getDragPanel().getWidth())
                {
                    tAllowedW = borderComponents[2].getDragPanel().getWidth();
                }
                lCW = tAllowedW;
                rCW = bAllowedW;

                borderComponents[2].setToolWindowWidth(lCW);
                borderComponents[3].setToolWindowWidth(rCW);
            }


            buttonBorders[0].setBounds(lBW, 0, w - (lBW + rBW), tBH);//top
            buttonBorders[1].setBounds(lBW, h - bBH, w - (lBW + rBW), bBH);//bottom

            buttonBorders[2].setBounds(0, tBH + tCH, lBW, h - (tBH + bBH + tCH + bCH));
            buttonBorders[3].setBounds(w - rBW, tBH + tCH, rBW, h - (tBH + bBH + tCH + bCH));


            borderComponents[0].setBounds(lBW, tBH, w - (lBW + rBW), tCH);
            borderComponents[1].setBounds(lBW, h - (bBH + bCH), w - (lBW + rBW), bCH);

            borderComponents[2].setBounds(lBW, tBH + tCH, lCW, h - (tBH + bBH + tCH + bCH));
            borderComponents[3].setBounds(w - (rBW + rCW), tBH + tCH, rCW, h - (tBH + bBH + tCH + bCH));

            dockingPane.getCenterComponent().setBounds(
                    lBW + lCW,
                    tBH + tCH,
                    w - (lBW + lCW + rBW + rCW),
                    h - (tBH + tCH + bBH + bCH));
        }
    }

}
