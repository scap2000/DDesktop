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
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;

/**
 * User: Martin
 * Date: 01.04.2005
 * Time: 09:52:21
 */
public class RotateTextIcon implements Icon
{
    public static final int NONE = 0;
    public static final int CW = 1;
    public static final int CCW = 2;

    private int rotate;
    @NotNull
    private Font font;
    @NotNull
    private GlyphVector glyphs;
    private float width;
    private float height;
    private float ascent;


    public RotateTextIcon(int rotate, @NotNull Font font, @NotNull String text)
    {
        this.rotate = rotate;
        this.font = font;

        FontRenderContext fontRenderContext = new FontRenderContext(null, false, false);
        glyphs = font.createGlyphVector(fontRenderContext, text);
        width = (int) glyphs.getLogicalBounds().getWidth() + 4;
        //height = (int)glyphs.getLogicalBounds().getHeight();

        LineMetrics lineMetrics = font.getLineMetrics(text, fontRenderContext);
        ascent = lineMetrics.getAscent();
        height = (int) lineMetrics.getHeight();
    }


    public int getIconWidth()
    {
        return (int) (rotate == RotateTextIcon.CW || rotate == RotateTextIcon.CCW ? height : width);
    }


    public int getIconHeight()
    {
        return (int) (rotate == RotateTextIcon.CW || rotate == RotateTextIcon.CCW ? width : height);
    }


    public void paintIcon(@NotNull Component c, @NotNull Graphics g, int x, int y)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(font);
        AffineTransform oldTransform = g2d.getTransform();
        RenderingHints oldHints = g2d.getRenderingHints();

        g2d.setColor(c.getForeground());

        if (rotate == RotateTextIcon.NONE)
        {
            g2d.drawGlyphVector(glyphs, x + 2, y + ascent);
        }
        else if (rotate == RotateTextIcon.CW)
        {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(oldTransform);
            trans.translate(x, y + 2);
            trans.rotate(Math.PI / 2, height / 2, width / 2);
            g2d.setTransform(trans);
            g2d.drawGlyphVector(glyphs, (height - width) / 2, (width - height) / 2 + ascent);
        }
        else if (rotate == RotateTextIcon.CCW)
        {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(oldTransform);
            trans.translate(x, y - 2);
            trans.rotate(Math.PI * 3 / 2, height / 2, width / 2);
            g2d.setTransform(trans);
            g2d.drawGlyphVector(glyphs, (height - width) / 2, (width - height) / 2 + ascent);
        }

        g2d.setTransform(oldTransform);
        g2d.setRenderingHints(oldHints);
    }


}

