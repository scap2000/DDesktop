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
package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * User: Martin
 * Date: 03.06.2006
 * Time: 20:17:28
 */
public class ColorIcon implements Icon
{
    @NotNull
    private Shape shape;
    @NotNull
    private Color outline;
    @NotNull
    private Color fill;
    private int w;
    private int h;
    private boolean fillBGWhite;

    @NotNull
    private BasicStroke stroke;


    public ColorIcon(@NotNull Color outline, @NotNull Color fill, int w, int h, boolean fillBGWhite)
    {
        this.outline = outline;
        this.fill = fill;

        this.w = w;
        this.h = h;
        this.fillBGWhite = fillBGWhite;

        GeneralPath generalPath = new GeneralPath();
        float r = (float) (2.);
        float o = 0.0f;
        generalPath.moveTo(r + o, 0 + o);
        generalPath.lineTo(w - r + o, 0 + o);
        generalPath.lineTo(w + o, r + o);
        generalPath.lineTo(w + o, h - r + o);
        generalPath.lineTo(w - r + o, h + o);

        generalPath.lineTo(r + o, h + o);
        generalPath.lineTo(0 + o, h - r + o);
        generalPath.lineTo(0 + o, r + o);
        generalPath.closePath();

        shape = generalPath;

        stroke = new BasicStroke(1.0f);
    }


    public void paintIcon(@NotNull Component c, @NotNull Graphics g, int x, int y)
    {
        Graphics2D g2d = (Graphics2D) g;
        Object hint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke s = g2d.getStroke();
        g2d.setStroke(stroke);
        g2d.translate(x, y);

        Color col = g2d.getColor();
        if (fillBGWhite)
        {
            g2d.setColor(Color.WHITE);
            g2d.fill(shape);
        }

        g2d.setColor(fill);
        g2d.fill(shape);
        g2d.setColor(outline);
        g2d.draw(shape);
        g2d.setColor(col);

        g2d.translate(-x, -y);
        g2d.setStroke(s);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hint);
    }


    public int getIconWidth()
    {
        return w + 1;
    }


    public int getIconHeight()
    {
        return h + 1;
    }
}
