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

import org.jetbrains.annotations.NotNull;
import org.jfree.report.util.geom.StrictGeomUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

/**
 * User: Martin
 * Date: 15.02.2006
 * Time: 20:23:05
 */
@SuppressWarnings({"ALL"})
public class LineProblemTest
{
    public static void main(@NotNull String[] args)
    {
        double v = 19500 / 1000f;
        System.out.println(Double.toHexString(v));

        double v2 = 19500 / 1000d - 0.00000000000001;
        System.out.println(Double.toHexString(v2));

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(0, 1));
        //frame.getContentPane().add(new JPanel()
        //{
        //    protected void paintComponent(@NotNull Graphics g)
        //    {
        //        Graphics2D g2 = (Graphics2D) g;
        //        Line2D.Double line = new Line2D.Double(19.5, 1.5, 19.5, 31.5);
        //        g2.draw(line);
        //    }
        //});

        frame.getContentPane().add(new JPanel()
        {
            protected void paintComponent(@NotNull Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g;
                //Line2D.Double line1 = new Line2D.Double(17.5, 1.5, 17.5, 31.5);
                //g2.draw(line1);
                //
                AffineTransform origAT = g2.getTransform();
                ////g2.translate(23.5, 43.20000076293945);
                //g2.transform(AffineTransform.getTranslateInstance(StrictGeomUtility.toExternalValue(19500),
                //                                                  StrictGeomUtility.toExternalValue(1500)));
                g2.setColor(Color.GRAY);
                Line2D.Double line1 = new Line2D.Double(20 + 20.0 - 1, 20 + 1.5, 20 + 20.0 - 1, 20 + 11.5);
                g2.draw(line1);
                //g2.setTransform(origAT);

                g2.transform(AffineTransform.getScaleInstance(1.0, 1.0));
                g.setColor(Color.BLACK);
                g2.translate(-0, -0);
                g2.transform(AffineTransform.getTranslateInstance(20.0, 20.0));
                g2.transform(AffineTransform.getTranslateInstance(0f - StrictGeomUtility.toExternalValue(0L), 0f - StrictGeomUtility.toExternalValue(0L)));
                g2.transform(AffineTransform.getTranslateInstance(StrictGeomUtility.toExternalValue(19500L), StrictGeomUtility.toExternalValue(1500L)));
                Line2D.Double line = new Line2D.Double(0.5, 0.0, 0.5, 10);
                g2.draw(line);
                g2.transform(AffineTransform.getTranslateInstance(0 - 20.0 + 0.0, 0 - 20.0 + 0.0));
                g2.setTransform(AffineTransform.getTranslateInstance(20, 20));
                g2.setTransform(origAT);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 300, 100);
        frame.setVisible(true);


    }
}
