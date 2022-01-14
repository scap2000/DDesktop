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
import org.jfree.report.Band;
import org.jfree.report.ElementAlignment;
import org.jfree.report.ImageElement;
import org.jfree.report.JFreeReport;
import org.jfree.report.TableDataFactory;
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.ImageFieldElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 15:54:54
 */
@SuppressWarnings({"ALL"})
public class BugTest
{
    public static void main(@NotNull String[] args)
    {
        ReportTableModel reportTableModel = new ReportTableModel();

        JFreeReport report = new JFreeReport();

        report.setName("A Very Simple Report");

        ImageElement imageDataRowElement = ImageFieldElementFactory.createImageDataRowElement("I1", new Rectangle2D.Double(0.0, 0.0, 80, 80), "mediumImage", true, true);
        imageDataRowElement.setDynamicContent(false);

        report.getItemBand().addElement(imageDataRowElement);

        TextElement t2 = TextFieldElementFactory.createStringElement(
                "T2",
                new Rectangle2D.Double(0, 0.0, -100, 30.0),
                Color.black,
                ElementAlignment.LEFT,
                ElementAlignment.TOP,
                null, // font
                "-", // null string
                "title"
        );
        t2.setFontSize(30);
        t2.setDynamicContent(true);

        TextElement t3 = TextFieldElementFactory.createStringElement(
                "T3",
                new Rectangle2D.Double(0, 0, -100, 50),
                Color.black,
                ElementAlignment.LEFT,
                ElementAlignment.TOP,
                null, // font
                "-", // null string
                "creators"
        );
        t3.setDynamicContent(true);
        report.getItemBand().setDynamicContent(true);

        Band band = new Band();
        //TODO fix 0.8.9
        //band.setLayout(new StackedLayoutManager());
        band.addElement(t2);
        band.addElement(t3);
        band.setDynamicContent(true);

        band.getStyle().setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, new Point2D.Double(90, 0));

        report.getItemBand().addElement(band);

        report.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, reportTableModel));

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }


    @NonNls
    private static class ReportTableModel extends AbstractTableModel
    {
        public ReportTableModel()
        {
        }


        public int getRowCount()
        {
            return 2;
        }


        public int getColumnCount()
        {
            return 4;
        }


        public String getColumnName(int columnIndex)
        {
            switch (columnIndex)
            {
                case 0:
                    return "itemId";
                case 1:
                    return "mediumImage";
                case 2:
                    return "title";
                case 3:
                    return "creators";
            }

            return null;
        }


        public Class<?> getColumnClass(int columnIndex)
        {

            return String.class;
        }


        public Object getValueAt(int rowIndex, int columnIndex)
        {
            switch (columnIndex)
            {
                case 0:
                    return "a";
                case 1:
                    return "b";
                case 2:
                    return "Bend It Like Beckham UK IMPORT";
                case 3:
                    return "test";
            }

            return null;
        }
    }
}
