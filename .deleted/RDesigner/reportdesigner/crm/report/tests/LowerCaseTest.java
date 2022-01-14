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
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.function.strings.ToLowerCaseStringExpression;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.style.FontDefinition;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 08.03.2006
 * Time: 21:25:16
 */
@SuppressWarnings({"ALL"})
public class LowerCaseTest
{
    public static void main(@NotNull String[] args)
    {
        Object[] columnNames = new Object[]{"ALIASES.NAME"};
        DefaultTableModel reportTableModel = new DefaultTableModel(new Object[][]{{"UPPERCASESTRING"}, {"MixedCaseString"}, {"lowercasestring"}}, columnNames);

        JFreeReport report = new JFreeReport();

        TextElement t2 = TextFieldElementFactory.createStringElement(
                "ANAME",
                new Rectangle2D.Double(0, 0, -50, 10),
                Color.black,
                ElementAlignment.LEFT,
                ElementAlignment.TOP,
                new FontDefinition(new Font("Arial", Font.PLAIN, 8)), // font
                "-", // null string
                "toLowerCase"
        );

        report.getItemBand().addElement(t2);

        ToLowerCaseStringExpression function = new ToLowerCaseStringExpression();
        function.setField("ALIASES.NAME");
        function.setName("toLowerCase");

        report.getExpressions().add(function);

        report.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, reportTableModel));

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}
