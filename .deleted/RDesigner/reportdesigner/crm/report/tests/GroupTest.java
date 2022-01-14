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
import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.style.FontDefinition;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 15:55:11
 */
@SuppressWarnings({"ALL"})
public class GroupTest
{
    public static void main(String[] args)
    {
        JFreeReport report = new JFreeReport();

        report.setName("GroupTest");

        report.addGroup(new Group());
        report.getGroup(0).addField("column");
        report.getGroup(0).getHeader().addElement(TextFieldElementFactory.createStringElement("tf1",
                                                                                              new Rectangle2D.Double(0, 0, 100, 30),
                                                                                              Color.BLUE,
                                                                                              ElementAlignment.LEFT,
                                                                                              new FontDefinition("dialog", 12),
                                                                                              "-",
                                                                                              "column"));

        report.getItemBand().addElement(TextFieldElementFactory.createStringElement("tf2",
                                                                                    new Rectangle2D.Double(0, 0, 100, 30),
                                                                                    Color.RED,
                                                                                    ElementAlignment.LEFT,
                                                                                    new FontDefinition("dialog", 12),
                                                                                    "-",
                                                                                    "column"));

        report.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, new DefaultTableModel(new Object[][]{{"1"}, {"1"}, {"1"}, {"2"}, {"2"}, {"2"}, {"3"}, {"3"}, {"3"}}, new Object[]{"column"})));

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}