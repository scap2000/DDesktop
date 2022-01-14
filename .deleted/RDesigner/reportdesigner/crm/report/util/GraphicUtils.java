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
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.GraphicsContext;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 07:58:51
 */
public class GraphicUtils
{
    private GraphicUtils()
    {
    }


    @NotNull
    public static Rectangle2D.Double getScaledRectangle(@NotNull GraphicsContext graphicsContext, @NotNull Rectangle2D.Double orig)
    {
        double sf = graphicsContext.getScaleFactor();
        Rectangle2D.Double rect = new Rectangle2D.Double(orig.x * sf, orig.y * sf, orig.width * sf, orig.height * sf);
        return rect;
    }


    @Nullable
    public static float[] getArrayCopy(@Nullable double[] doubles)
    {
        if (doubles == null)
        {
            return null;
        }

        float[] floats = new float[doubles.length];
        for (int i = 0; i < floats.length; i++)
        {
            floats[i] = (float) doubles[i];
        }
        return floats;
    }


    @NotNull
    public static ImageIcon createColorImageIcon(@NotNull Color outline, @NotNull Color fill, int w, int h, boolean fillBGWhite)
    {
        BufferedImage bi = new BufferedImage(w + 1, h + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();

        Object hint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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


        Color col = g2d.getColor();
        if (fillBGWhite)
        {
            g2d.setColor(Color.WHITE);
            g2d.fill(generalPath);
        }

        g2d.setColor(fill);
        g2d.fill(generalPath);
        g2d.setColor(outline);
        g2d.draw(generalPath);
        g2d.setColor(col);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hint);

        g2d.dispose();

        return new ImageIcon(bi);
    }
}
