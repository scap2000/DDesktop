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

import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ShapeElement;
import org.jfree.report.TableDataFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.function.ElementVisibilitySwitchFunction;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.style.FontDefinition;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 15:55:11
 */
@SuppressWarnings({"ALL"})
public class RowBandingTest
{
    public static void main(String[] args)
    {
        JFreeReport report = new JFreeReport();
        report.setName("RowBandingTest");
        report.getItemBand().addElement(TextFieldElementFactory.createStringElement("tf1",
                                                                                    new Rectangle2D.Double(20, 0, 100, 18),
                                                                                    Color.BLACK,
                                                                                    ElementAlignment.LEFT,
                                                                                    ElementAlignment.MIDDLE,//change to ElementAlignment.TOP
                                                                                    new FontDefinition("dialog", 12),
                                                                                    "-",
                                                                                    "column"));

        ShapeElement shapeElement = StaticShapeElementFactory.createRectangleShapeElement("rect", new Color(255, 0, 0, 128), new BasicStroke(1), new Rectangle2D.Double(0, 0, -100, -100), false, true);

        ElementVisibilitySwitchFunction switchFunction = new ElementVisibilitySwitchFunction();
        switchFunction.setElement("rect");
        switchFunction.setInitialState(true);
        switchFunction.setNumberOfElements(1);
        report.getExpressions().add(switchFunction);

        report.getItemBand().addElement(shapeElement);

        report.getReportConfiguration().setConfigProperty("org.jfree.report.layout.fontrenderer.UseMaxCharBounds", Boolean.TRUE.toString());

        DefaultTableModel tm1 = new DefaultTableModel(new Object[][]{{"1"}, {"2"}, {"3"}, {"4"}, {"5"}, {"6"}, {"7"}, {"8"}, {"9"}}, new Object[]{"column"});

        TableDataFactory dataFactory = new TableDataFactory("default", tm1);
        report.setDataFactory(dataFactory);

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}