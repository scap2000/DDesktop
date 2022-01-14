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
import org.jfree.report.function.HideNullValuesFunction;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.style.FontDefinition;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

/**
 * User: Martin
 * Date: 08.03.2006
 * Time: 21:25:16
 */
@SuppressWarnings({"ALL"})
public class HideNullValuesTest
{
    public static void main(@NotNull String[] args)
    {
        Vector columnNames = new Vector();
        columnNames.add("NEVER_NULL");
        columnNames.add("ALWAYS_NULL");
        columnNames.add("SOMETIMES_NULL");
        DefaultTableModel reportTableModel = new DefaultTableModel(columnNames, 5)
        {
            public Class<?> getColumnClass(int columnIndex)
            {
                return String.class;
            }


            public Object getValueAt(int row, int column)
            {
                if (column == 0)
                {
                    return "row " + row;
                }
                else if (column == 1)
                {
                    return null;
                }
                else
                {
                    return (row % 2 == 0) ? null : "row " + row;
                }
            }
        };


        JFreeReport report = new JFreeReport();

        TextElement field1 = TextFieldElementFactory.createStringElement("field1",
                                                                         new Rectangle(0, 0, 100, 30),
                                                                         Color.BLACK,
                                                                         ElementAlignment.LEFT,
                                                                         ElementAlignment.TOP,
                                                                         new FontDefinition(new Font("dialog", Font.PLAIN, 12)),
                                                                         "null String",
                                                                         "NEVER_NULL");

        TextElement field2 = TextFieldElementFactory.createStringElement("field2",
                                                                         new Rectangle(100, 0, 100, 30),
                                                                         Color.BLACK,
                                                                         ElementAlignment.LEFT,
                                                                         ElementAlignment.TOP,
                                                                         new FontDefinition(new Font("dialog", Font.PLAIN, 12)),
                                                                         "null String",
                                                                         "ALWAYS_NULL");

        TextElement field3 = TextFieldElementFactory.createStringElement("field3",
                                                                         new Rectangle(200, 0, 100, 30),
                                                                         Color.BLACK,
                                                                         ElementAlignment.LEFT,
                                                                         ElementAlignment.TOP,
                                                                         new FontDefinition(new Font("dialog", Font.PLAIN, 12)),
                                                                         "null String",
                                                                         "SOMETIMES_NULL");

        report.getItemBand().addElement(field1);
        report.getItemBand().addElement(field2);
        report.getItemBand().addElement(field3);

        report.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, reportTableModel));

        HideNullValuesFunction hnvf1 = new HideNullValuesFunction();
        hnvf1.setName("hideFunction1");
        hnvf1.setElement("field1");
        hnvf1.setField("NEVER_NULL");

        HideNullValuesFunction hnvf2 = new HideNullValuesFunction();
        hnvf2.setName("hideFunction2");
        hnvf2.setElement("field2");
        hnvf2.setField("ALWAYS_NULL");

        HideNullValuesFunction hnvf3 = new HideNullValuesFunction();
        hnvf3.setName("hideFunction3");
        hnvf3.setElement("field3");
        hnvf3.setField("SOMETIMES_NULL");

        report.addExpression(hnvf1);
        report.addExpression(hnvf2);
        report.addExpression(hnvf3);

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}
