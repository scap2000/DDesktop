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
package org.pentaho.reportdesigner.lib.client.components.favoritespanel;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * A Panel used to display the title of a GradientBorderPanel.<p/>
 * <p/>
 * It uses a gradient with user defineable colors as background.
 *
 * @author schmm7
 */
public class GradientPanel extends JPanel
{

    public enum Direction
    {
        @NotNull DIRECTION_UP,
        @NotNull DIRECTION_DOWN,
        @NotNull DIRECTION_LEFT,
    }


    @NotNull
    private Color[] gradientColors;
    @NotNull
    private Direction direction;
    private boolean gradientSetByUser;


    /**
     * Creates a new GradientTitlePanel with a gradient from DARK_GREY to LIGHT_GREY.
     * The default title string is set to a one char width whitespace. This results
     * in a correctly calculated preferred size of the title label.
     */
    public GradientPanel()
    {
        setLayout(new BorderLayout());
        setOpaque(false);

        gradientColors = new Color[]{getBackground().darker(), getBackground()};
        direction = Direction.DIRECTION_DOWN;
        gradientSetByUser = false;
    }


    /**
     * Sets the colors used as gradient.<p/>
     * <p/>
     * The first color in the array is used as the leftmost color in the gradient.
     *
     * @param gradientColors the colors to use as gradient
     */
    public void setGradientColors(@NotNull Color[] gradientColors)
    {
        //noinspection ConstantConditions
        if (gradientColors == null || gradientColors.length == 0)
        {
            throw new IllegalArgumentException("gradientColors must be array with more than zero elements");
        }
        gradientSetByUser = true;
        if (gradientColors.length == 1)
        {
            this.gradientColors = new Color[]{gradientColors[0], gradientColors[0]};
        }

        this.gradientColors = new Color[gradientColors.length];
        System.arraycopy(gradientColors, 0, this.gradientColors, 0, gradientColors.length);

    }


    @NotNull
    public Color[] getGradientColors()
    {
        return gradientColors;
    }


    @NotNull
    public Direction getDirection()
    {
        return direction;
    }


    public void setDirection(@NotNull Direction direction)
    {
        this.direction = direction;
    }


    public void setBackground(@NotNull Color bg)
    {
        super.setBackground(bg);
        if (!gradientSetByUser)
        {
            gradientColors = new Color[]{getBackground().darker(), getBackground()};
        }
    }


    /**
     * First paints the gradient, and delegates the call to its children. <p/>
     * Usually you should use components that do not draw their background on their own.
     *
     * @param g the GraphicsContext to paint on
     * @see JComponent#setOpaque(boolean)
     */
    protected void paintComponent(@NotNull Graphics g)
    {
        Graphics2D graphics2D = (Graphics2D) g;

        if (direction == Direction.DIRECTION_DOWN)
        {
            int heigthIncrement = getHeight() / (gradientColors.length - 1);

            for (int i = 0; i < gradientColors.length - 1; i++)
            {
                GradientPaint gradientPaint = new GradientPaint(new Point2D.Double(0, heigthIncrement * i), gradientColors[i], new Point2D.Double(0, (i + 1) * heigthIncrement), gradientColors[i + 1]);
                graphics2D.setPaint(gradientPaint);
                graphics2D.fillRect(0, i * heigthIncrement, getWidth(), heigthIncrement);
            }
        }
        else if (direction == Direction.DIRECTION_UP)
        {
            int heigthIncrement = getHeight() / (gradientColors.length - 1);

            for (int i = 0; i < gradientColors.length - 1; i++)
            {
                Color gc1 = gradientColors[gradientColors.length - i - 1];
                Color gc2 = gradientColors[gradientColors.length - i - 2];
                GradientPaint gradientPaint = new GradientPaint(new Point2D.Double(0, heigthIncrement * i), gc1, new Point2D.Double(0, (i + 1) * heigthIncrement), gc2);
                graphics2D.setPaint(gradientPaint);
                graphics2D.fillRect(0, i * heigthIncrement, getWidth(), heigthIncrement);
            }
        }
        else if (direction == Direction.DIRECTION_LEFT)
        {
            int widthIncrement = getWidth() / (gradientColors.length - 1);

            for (int i = 0; i < gradientColors.length - 1; i++)
            {
                GradientPaint gradientPaint = new GradientPaint(new Point2D.Double(widthIncrement * i, 0), gradientColors[i], new Point2D.Double((i + 1) * widthIncrement, 0), gradientColors[i + 1]);
                graphics2D.setPaint(gradientPaint);
                graphics2D.fillRect(i * widthIncrement, 0, widthIncrement, getHeight());
            }
        }

        super.paintComponent(g);
    }


}

