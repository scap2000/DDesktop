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
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ShapeElement;
import org.jfree.report.TableDataFactory;
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.style.FontDefinition;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 12.01.2006
 * Time: 11:14:25
 */
@SuppressWarnings({"ALL"})
public class ReportTextLayout001
{
    public static void main(@NotNull String[] args)
    {
        JFreeReport report = new JFreeReport();
        report.setName("ReportTextLayout001");

        ShapeElement rectangleElement = StaticShapeElementFactory.createRectangleShapeElement("Rectangle",
                                                                                              Color.GREEN,
                                                                                              new BasicStroke(1),
                                                                                              new Rectangle2D.Double(0, 10, 235, 104),
                                                                                              true,
                                                                                              true);

        TextElement labelElement = LabelElementFactory.createLabelElement("Label1",
                                                                          new Rectangle2D.Double(0, 10, 235, 104),
                                                                          Color.BLACK,
                                                                          ElementAlignment.LEFT,
                                                                          new FontDefinition("Arial", 40),
                                                                          "ppp Title ÊÉ ÊÉ");
        labelElement.setUnderline(true);
        labelElement.setStrikethrough(true);


        report.getReportHeader().addElement(rectangleElement);
        report.getReportHeader().addElement(labelElement);

        report.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, new DefaultTableModel()));

        JFreeReportBoot.getInstance().start();
        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}
