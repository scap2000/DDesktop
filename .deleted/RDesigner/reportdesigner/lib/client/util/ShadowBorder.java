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
package org.pentaho.reportdesigner.lib.client.util;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * User: Martin
 * Date: 03.02.2006
 * Time: 08:13:47
 */
public class ShadowBorder extends AbstractBorder
{
    public static void main(@NotNull String[] args)
    {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setBorder(new ShadowBorder(new Color(160, 160, 160), new Color(210, 210, 210)));
        frame.getContentPane().add(new JLabel("   "), BorderLayout.NORTH);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(new JLabel("   "), BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private static final int OFFSET_1 = 20;
    private static final int OFFSET_2 = 30;
    private static final int OFFSET_3 = 40;

    @NotNull
    private Color innerColor;
    @NotNull
    private Color outerColor;


    public ShadowBorder()
    {
    }


    /**
     * Creates a new ShadowBorder.
     */
    public ShadowBorder(@NotNull Color innerColor, @NotNull Color outerColor)
    {
        this.innerColor = innerColor;
        this.outerColor = outerColor;
    }


    /**
     * Returns the insets used by this border.<p/>
     * This border uses one pixel on the top and left side, and 4 pixels on
     * the bottom and right side.
     *
     * @param c the component for which this border insets value applies
     * @return the insets object initialized to (1,1,4,4)
     */
    @NotNull
    public Insets getBorderInsets(@NotNull Component c)
    {
        return new Insets(1, 1, 4, 4);
    }


    public boolean isBorderOpaque()
    {
        return false;
    }


    /**
     * Paints this border with a one pixel wide line on the top and left side,
     * and a fading shadow on the bottom and right side.
     *
     * @param c      the component for which this border is being painted
     * @param g      the paint graphics
     * @param x      the x position of the painted border
     * @param y      the y position of the painted border
     * @param width  the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(@NotNull Component c, @NotNull Graphics g, int x, int y, int width, int height)
    {
        Color col1;
        Color col2;
        Color col3;

        if (innerColor != null && outerColor != null)
        {
            col1 = innerColor;
            int middleR = (innerColor.getRed() + outerColor.getRed()) / 2;
            int middleG = (innerColor.getGreen() + outerColor.getGreen()) / 2;
            int middleB = (innerColor.getBlue() + outerColor.getBlue()) / 2;
            col2 = new Color(middleR, middleG, middleB);
            col3 = outerColor;
        }
        else
        {
            Color base = c.getBackground();
            if (base == null)
            {
                base = UIConstants.DEFAULT_BACKGROUND_COLOR;
            }
            col3 = new Color(base.getRed() - OFFSET_1, base.getGreen() - OFFSET_1, base.getBlue() - OFFSET_1);
            col2 = new Color(base.getRed() - OFFSET_2, base.getGreen() - OFFSET_2, base.getBlue() - OFFSET_2);
            col1 = new Color(base.getRed() - OFFSET_3, base.getGreen() - OFFSET_3, base.getBlue() - OFFSET_3);

        }

        g.setColor(col3);
        g.drawLine(x + width - 4, y, x + width - 3, y);
        g.drawLine(x, y + height - 4, x, y + height - 3);
        g.drawLine(x + 1, y + height - 2, x + width - 3, y + height - 2);
        g.drawLine(x + width - 3, y + height - 3, x + width - 3, y + height - 3);
        g.drawLine(x + width - 2, y + 1, x + width - 2, y + height - 3);

        g.setColor(col2);
        g.drawLine(x + width - 5, y, x + width - 4, y);
        g.drawLine(x, y + height - 5, x, y + height - 4);
        g.drawLine(x + 1, y + height - 3, x + width - 4, y + height - 3);
        g.drawLine(x + width - 3, y + 1, x + width - 3, y + height - 4);

        g.setColor(col1);
        g.drawLine(x, y, x + width - 5, y);
        g.drawLine(x, y, x, y + height - 5);
        g.drawLine(x + 1, y + height - 4, x + width - 4, y + height - 4);
        g.drawLine(x + width - 4, y + 1, x + width - 4, y + height - 4);
    }
}
