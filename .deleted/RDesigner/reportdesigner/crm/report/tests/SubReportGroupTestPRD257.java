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
import org.jfree.report.SubReport;
import org.jfree.report.TableDataFactory;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
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
public class SubReportGroupTestPRD257
{
    public static void main(String[] args)
    {
        JFreeReport report = new JFreeReport();

        report.setName("SubReportGroupTest");

        report.getReportHeader().addElement(LabelElementFactory.createLabelElement("l1",
                                                                                   new Rectangle2D.Double(0, 0, 100, 20),
                                                                                   Color.BLUE,
                                                                                   ElementAlignment.LEFT,
                                                                                   new FontDefinition("dialog", 12),
                                                                                   "header"));

        report.addGroup(new Group());
        report.getGroup(0).addField("onr");
        report.getGroup(0).getHeader().setPagebreakBeforePrint(true);
        report.getGroup(0).getHeader().addElement(TextFieldElementFactory.createStringElement("tf1",
                                                                                              new Rectangle2D.Double(0, 0, 100, 20),
                                                                                              Color.BLUE,
                                                                                              ElementAlignment.LEFT,
                                                                                              new FontDefinition("dialog", 12),
                                                                                              "-",
                                                                                              "onr"));

        DefaultTableModel tm1 = new DefaultTableModel(new Object[][]{{1, "A"}, {1, "AA"}, {2, "B"}, {2, "BB"}, {3, "C"}, {3, "CC"}}, new Object[]{"onr", "text"});
        DefaultTableModel tm2 = new DefaultTableModel(new Object[][]{{"A"}, {"AA"}, {"B"}, {"BB"}, {"C"}, {"CC"}}, new Object[]{"text"});


        TableDataFactory dataFactory = new TableDataFactory("default", tm1);
        dataFactory.addTable("Test Query", tm2);

        SubReport subReport = new SubReport();
        subReport.setQuery("Test Query");

        subReport.getItemBand().addElement(TextFieldElementFactory.createStringElement("stf2",
                                                                                       new Rectangle2D.Double(80, 0, 100, 20),
                                                                                       Color.PINK,
                                                                                       ElementAlignment.LEFT,
                                                                                       new FontDefinition("dialog", 12),
                                                                                       "-",
                                                                                       "text"));

        report.getItemBand().addSubReport(subReport);

        report.setDataFactory(dataFactory);

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}