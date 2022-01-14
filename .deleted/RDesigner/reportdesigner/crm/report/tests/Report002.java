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
import org.jfree.report.TableDataFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.style.FontDefinition;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.tests.reportdata.ReportTableModel002;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 15:55:11
 */
@SuppressWarnings({"ALL"})
public class Report002
{
    public static void main(@NotNull String[] args)
    {
        JFreeReport report = new JFreeReport();

        report.setName("Report002");

        //report.getReportHeader().addElement(LabelElementFactory.createLabelElement("ReportTitle",
        //                                                                           new Rectangle2D.Double(30, 0, 300, 50),
        //                                                                           Color.RED,
        //                                                                           ElementAlignment.LEFT,
        //                                                                           new FontDefinition("dialog", 40),
        //                                                                           "Report Title"));

        report.getItemBand().addElement(TextFieldElementFactory.createStringElement("test",
                                                                                    new Rectangle2D.Double(30, 0, 300, 50),
                                                                                    Color.GREEN,
                                                                                    ElementAlignment.LEFT,
                                                                                    ElementAlignment.TOP,
                                                                                    new FontDefinition("dialog", 40),
                                                                                    "-",
                                                                                    "FIRSTNAME"));

        report.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, new ReportTableModel002()));

        //report.setProperty(JFreeReport.REPORT_DEFINITION_CONTENTBASE, "xxx");

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}
