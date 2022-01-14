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
package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.components.docking.IconCreator;
import org.pentaho.reportdesigner.lib.client.components.docking.RotateTextIcon;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 27.01.2006
 * Time: 13:14:26
 */
@SuppressWarnings({"ALL"})
public class DrawRotatedTextTester
{

    public static void main(@NotNull String[] args)
    {
        JFrame frame = new JFrame();
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(null);
        Label label = new Label();
        frame.getContentPane().add(label);
        label.setBounds(10, 10, 101, 101);
        frame.setBounds(100, 100, 200, 200);
        frame.setVisible(true);
    }


    @NonNls
    private static class Label extends JComponent
    {
        @NotNull
        private static final String DEFAULT_FONT_FACE = "dialog";

        @NotNull
        private Font font;


        public Label()
        {
            font = new Font(DEFAULT_FONT_FACE, Font.BOLD, 14);
        }


        public Dimension getPreferredSize()
        {
            return new Dimension(100, 100);
        }


        protected void paintComponent(@NotNull Graphics g)
        {
            g.drawRect(0, 0, 100, 100);
            g.setColor(Color.WHITE);
            g.fillRect(1, 1, 98, 98);

            g.setColor(Color.BLACK);

            //Graphics2D g2d = (Graphics2D) g;
            //AffineTransform transform = g2d.getTransform();
            //AffineTransform at = new AffineTransform();
            //at.setToRotation(-Math.PI / 2.0);
            //g.setFont(font);
            //Rectangle2D stringBounds = g.getFont().getStringBounds("aString", new FontRenderContext(null, false, false));
            //System.out.println("stringBounds = " + stringBounds);
            //g2d.setTransform(at);
            //g2d.drawString("aString", (int) -stringBounds.getWidth() - 10, (int) (stringBounds.getHeight() + 0.5));
            //g2d.setTransform(transform);
            long l1 = System.nanoTime();
            IconCreator.drawRotatedText((Graphics2D) g, 0, 0, Color.BLACK, RotateTextIcon.CCW, g.getFont(), "123");
            long l2 = System.nanoTime();
            System.out.println("l2-l1 = " + (l2 - l1) / 1000.);
        }
    }

}
