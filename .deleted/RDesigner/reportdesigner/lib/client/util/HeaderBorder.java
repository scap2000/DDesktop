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
 * Time: 08:12:20
 */
public class HeaderBorder extends AbstractBorder
{
    public static void main(@NotNull String[] args)
    {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setBorder(new HeaderBorder());
        frame.getContentPane().add(new JLabel("   "), BorderLayout.NORTH);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(new JLabel("   "), BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    @NotNull
    private Color innerColor;
    @NotNull
    private Color outerColor;


    public HeaderBorder()
    {
    }


    /**
     * Creates a new ShadowBorder.
     */
    public HeaderBorder(@NotNull Color innerColor, @NotNull Color outerColor)
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
        return new Insets(1, 1, 1, 0);
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

        if (innerColor != null && outerColor != null)
        {
            col1 = innerColor;
            col2 = outerColor;
        }
        else
        {
            Color base = c.getBackground();
            if (base == null)
            {
                base = UIConstants.DEFAULT_BACKGROUND_COLOR;
            }
            col1 = new Color(Math.max(0, base.getRed() - 60), Math.max(0, base.getGreen() - 60), Math.max(0, base.getBlue() - 60));
            col2 = new Color(Math.min(255, base.getRed() + 60), Math.min(255, base.getGreen() + 60), Math.min(255, base.getBlue() + 60));
        }

        g.setColor(col1);
        g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);

        g.setColor(col2);
        g.drawLine(x, y, x + width - 1, y);
        g.drawLine(x, y, x, y + height - 2);


    }
}