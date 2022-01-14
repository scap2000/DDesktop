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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class IconCreator
{
    public enum FirstIconAlignment
    {
        @NotNull TOP,
        @NotNull BOTTOM,
        @NotNull LEFT,
        @NotNull RIGHT
    }


    private IconCreator()
    {
    }


    @NotNull
    public static ImageIcon createOverlayImageIcon(@NotNull ImageIcon... imageIcons)
    {
        BufferedImage bi = new BufferedImage(imageIcons[0].getIconWidth(), imageIcons[0].getIconWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bi.getGraphics();
        for (ImageIcon imageIcon : imageIcons)
        {
            graphics.drawImage(imageIcon.getImage(), 0, 0, null);
        }
        return new ImageIcon(bi);
    }


    @NotNull
    public static ImageIcon converIconToImageIcon(@NotNull Icon icon)
    {
        BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bi.getGraphics();
        Component c = new Component()
        {
        };

        icon.paintIcon(c, graphics, 0, 0);
        return new ImageIcon(bi);
    }


    @NotNull
    public static ImageIcon createRotatedTextIcon(@NotNull Color foreground, int rotate, @NotNull Font font, @NotNull String text)
    {
        FontRenderContext fontRenderContext = new FontRenderContext(null, false, false);
        GlyphVector glyphs = font.createGlyphVector(fontRenderContext, text);
        int width = (int) glyphs.getLogicalBounds().getWidth() + 4;
        //height = (int)glyphs.getLogicalBounds().getHeight();

        LineMetrics lineMetrics = font.getLineMetrics(text, fontRenderContext);
        float ascent = lineMetrics.getAscent();
        int height = (int) Math.ceil(lineMetrics.getHeight());

        int x = 0;
        int y = 0;

        int w = rotate == RotateTextIcon.CW || rotate == RotateTextIcon.CCW ? height : width;
        int h = rotate == RotateTextIcon.CW || rotate == RotateTextIcon.CCW ? width : height;

        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

        g2d.setFont(font);
        AffineTransform oldTransform = g2d.getTransform();

        g2d.setColor(foreground);

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

        return new ImageIcon(bufferedImage);
    }


    public static void drawRotatedText(@NotNull Graphics2D g2d, int x, int y, @NotNull Color foreground, int rotate, @NotNull Font font, @NotNull String text)
    {
        FontRenderContext fontRenderContext = new FontRenderContext(null, false, false);
        //GlyphVector glyphs = font.createGlyphVector(fontRenderContext, text);
        //int width = (int) glyphs.getLogicalBounds().getWidth() + 4;

        Rectangle2D sb = font.getStringBounds(text, fontRenderContext);
        int width = (int) sb.getWidth() + 4;

        LineMetrics lineMetrics = font.getLineMetrics(text, fontRenderContext);
        float ascent = lineMetrics.getAscent();
        int height = (int) Math.ceil(lineMetrics.getHeight());

        g2d.setFont(font);
        AffineTransform oldTransform = g2d.getTransform();

        g2d.setColor(foreground);

        if (rotate == RotateTextIcon.NONE)
        {
            //g2d.drawGlyphVector(glyphs, x + 2, y + ascent);
            g2d.drawString(text, x + 2, y + ascent);
        }
        else if (rotate == RotateTextIcon.CW)
        {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(oldTransform);
            trans.translate(x, y + 2);
            trans.rotate(Math.PI / 2, height / 2, width / 2);
            g2d.setTransform(trans);
            //g2d.drawGlyphVector(glyphs, (height - width) / 2, (width - height) / 2 + ascent);
            g2d.drawString(text, (height - width) / 2, (width - height) / 2 + ascent);
        }
        else if (rotate == RotateTextIcon.CCW)
        {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(oldTransform);
            trans.translate(x, y - 2);
            trans.rotate(Math.PI * 3 / 2, height / 2, width / 2);
            g2d.setTransform(trans);
            //g2d.drawGlyphVector(glyphs, (height - width) / 2, (width - height) / 2 + ascent);
            g2d.drawString(text, (height - width) / 2, (width - height) / 2 + ascent);
        }

        g2d.setTransform(oldTransform);
    }


    @NotNull
    public static ImageIcon createComposedImageIcon(@NotNull ImageIcon firstIcon, @NotNull ImageIcon secondIcon, @NotNull FirstIconAlignment firstIconAlignment)
    {
        switch (firstIconAlignment)
        {
            case TOP:
            {
                int width = Math.max(firstIcon.getIconWidth(), secondIcon.getIconWidth());
                int height = firstIcon.getIconHeight() + secondIcon.getIconHeight();

                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = bi.getGraphics();

                graphics.drawImage(firstIcon.getImage(), (width - firstIcon.getIconWidth()) / 2, 0, null);
                graphics.drawImage(secondIcon.getImage(), (width - secondIcon.getIconWidth()) / 2, firstIcon.getIconHeight(), null);

                return new ImageIcon(bi);
            }
            case BOTTOM:
            {
                int width = Math.max(firstIcon.getIconWidth(), secondIcon.getIconWidth());
                int height = firstIcon.getIconHeight() + secondIcon.getIconHeight();

                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = bi.getGraphics();

                graphics.drawImage(firstIcon.getImage(), (width - firstIcon.getIconWidth()) / 2, height - firstIcon.getIconHeight(), null);
                graphics.drawImage(secondIcon.getImage(), (width - secondIcon.getIconWidth()) / 2, 0, null);

                return new ImageIcon(bi);
            }
            case LEFT:
            {
                int width = firstIcon.getIconWidth() + secondIcon.getIconWidth();
                int height = Math.max(firstIcon.getIconHeight(), secondIcon.getIconHeight());

                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = bi.getGraphics();

                graphics.drawImage(firstIcon.getImage(), 0, (height - firstIcon.getIconHeight()) / 2, null);
                graphics.drawImage(secondIcon.getImage(), firstIcon.getIconWidth(), (height - secondIcon.getIconHeight()) / 2, null);

                return new ImageIcon(bi);
            }
            case RIGHT:
            {
                int width = firstIcon.getIconWidth() + secondIcon.getIconWidth();
                int height = Math.max(firstIcon.getIconHeight(), secondIcon.getIconHeight());

                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = bi.getGraphics();

                graphics.drawImage(firstIcon.getImage(), secondIcon.getIconWidth(), (height - firstIcon.getIconHeight()) / 2, null);
                graphics.drawImage(secondIcon.getImage(), 0, (height - secondIcon.getIconHeight()) / 2, null);

                return new ImageIcon(bi);
            }
        }

        return new ImageIcon();
    }

}
