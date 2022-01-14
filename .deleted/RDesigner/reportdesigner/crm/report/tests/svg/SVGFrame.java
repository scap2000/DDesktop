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

import com.kitfox.svg.app.beans.SVGIcon;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * User: Martin
 * Date: 06.08.2006
 * Time: 11:08:05
 */
@SuppressWarnings({"ALL"})
public class SVGFrame
{
    private static int counter = 0;


    public static void main(@NotNull String[] args)
            throws URISyntaxException
    {
        JFrame frame = new JFrame();
        final SVGIcon svgIcon = new SVGIcon();
        svgIcon.setAntiAlias(true);
        //svgIcon.setScaleToFit(true);
        //svgIcon.setPreferredSize(new Dimension(200, 200));
        svgIcon.setSvgURI(new URI("file:/C:/Daten/07_Projekte/PentahoReportDesigner/src/res/icons/SaveIcon.svg"));

        JLabel comp = new JLabel(svgIcon)
        {
            protected void paintComponent(@NotNull Graphics g)
            {
                counter++;
                int width = svgIcon.getIconWidth();
                int height = svgIcon.getIconHeight();
                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                super.paintComponent(g);
                svgIcon.paintIcon(null, bi.createGraphics(), 0, 0);
                try
                {
                    ImageIO.write(bi, "png", new File("C:\\temp\\images\\test" + counter + ".png"));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        frame.getContentPane().add(comp, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 200, 200);
        frame.setVisible(true);
    }
}
