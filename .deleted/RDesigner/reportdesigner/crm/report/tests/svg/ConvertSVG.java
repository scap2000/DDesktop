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
package org.pentaho.reportdesigner.crm.report.tests.svg;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;

/**
 * User: Martin
 * Date: 06.08.2006
 * Time: 10:55:50
 */
@SuppressWarnings({"ALL"})
public class ConvertSVG
{
    public static void main(@NotNull String[] args) throws IOException, SVGException
    {
        File root = new File("C:\\Daten\\07_Projekte\\PentahoReportDesigner\\src\\res\\icons");
        File[] files = root.listFiles(new FileFilter()
        {
            public boolean accept(@NotNull File pathname)
            {
                return pathname.getName().endsWith(".svg");
            }
        });

        for (int i = 0; i < files.length; i++)
        {
            try
            {
                convert(files, i);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    private static void convert(@NotNull File[] files, int i) throws SVGException, IOException
    {
        File file = files[i];
        URI uri = SVGCache.getSVGUniverse().loadSVG(file.toURI().toURL());
        System.out.println("uri = " + uri);
        SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(uri);
        //SVGIcon svgIcon = new SVGIcon();
        //svgIcon.setSvgURI(file.toURI());
        //float width = diagram.getWidth();
        //System.out.println("width = " + width);
        //svgIcon.setScaleToFit(true);
        //svgIcon.setPreferredSize(new Dimension(32, 32));

        int width = 128;
        int height = 128;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = bi.createGraphics();

        Rectangle2D.Double rect = new Rectangle2D.Double();
        diagram.getViewRect(rect);

        AffineTransform scaleXform = new AffineTransform();
        scaleXform.setToScale(width / rect.width, height / rect.height);

        graphics.transform(scaleXform);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        diagram.setIgnoringClipHeuristic(true);
        diagram.render(graphics);
        //svgIcon.paintIcon(null, graphics, 0, 0);
        graphics.dispose();

        ImageIO.write(bi, "png", new File("C:\\temp\\images\\" + file.getName() + ".png"));
        //File dest = new File("C:\\temp\\images\\" + file.getName() + ".png");
        //convert(file, dest);
        //convert(file, dest);
    }

    //private static void convert(File source, File dest)
    //{
    //    SVGIcon icon = new SVGIcon();
    //    icon.setSvgURI(source.toURI());
    //    icon.setAntiAlias(true);
    //
    //    int width = icon.getIconWidth();
    //    int height = icon.getIconHeight();
    //    BufferedImage image = new BufferedImage(width, height, 2);
    //
    //    Graphics2D g = image.createGraphics();
    //
    //    icon.paintIcon(null, g, 0, 0);
    //    icon.paintIcon(null, g, 0, 0);
    //    g.dispose();
    //
    //    try
    //    {
    //        ImageIO.write(image, "png", dest);
    //    }
    //
    //    catch (IOException e)
    //    {
    //        e.printStackTrace();
    //    }
    //
    //    SVGCache.getSVGUniverse().clear();
    //}
}
