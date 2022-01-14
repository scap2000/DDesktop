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
import org.jfree.report.ImageElement;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.elementfactory.ImageFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * User: Martin
 * Date: 08.03.2006
 * Time: 21:25:16
 */
@SuppressWarnings({"ALL"})
public class ImageFieldTest
{
    public static void main(@NotNull String[] args)
    {
        Vector columnNames = new Vector();
        columnNames.add("DYNAMIC_IMAGE");
        DefaultTableModel reportTableModel = new DefaultTableModel(columnNames, 5)
        {
            public Class<?> getColumnClass(int columnIndex)
            {
                return Image.class;
            }


            public Object getValueAt(int row, int column)
            {
                BufferedImage bi = new BufferedImage(50, 30, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = bi.getGraphics();
                graphics.setColor(Color.GREEN);
                graphics.fillRect(0, 0, 50, 30);
                graphics.setColor(Color.BLACK);
                graphics.drawString("(" + row + ", " + column + ")", 10, 15);
                return bi;
            }
        };


        JFreeReport report = new JFreeReport();

        ImageElement imageElement = ImageFieldElementFactory.createImageDataRowElement("imageField",
                                                                                       new Rectangle(0, 0, 50, 30),
                                                                                       "DYNAMIC_IMAGE",
                                                                                       true,
                                                                                       false);

        report.getItemBand().addElement(imageElement);
        report.getItemBand().setMinimumSize(new Dimension(0, 50));

        report.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, reportTableModel));

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}
