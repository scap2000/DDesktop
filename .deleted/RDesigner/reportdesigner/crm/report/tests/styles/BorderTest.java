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
package org.pentaho.reportdesigner.crm.report.tests.styles;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.ReportConfiguration;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 15:55:11
 */
@SuppressWarnings({"ALL"})
public class BorderTest
{
    public static void main(@NotNull String[] args) throws IOException, ReportWriterException
    {
        Object[] columnNames = new Object[]{"Customer", "City", "Number"};

        DefaultTableModel reportTableModel = new DefaultTableModel(
                new Object[][]{{"Customer_ASDFSDFSDFSDFSaasdasdasdasweruzweurzwiezrwieuzriweuzriweu", "Bern", "123"},
                               {"Hugo", "Zürich", "2234"},},
                columnNames);

        JFreeReport report = new JFreeReport();

        report.setName("BorderTest");


        report.getItemBand().addElement(LabelElementFactory.createLabelElement("CustomerLabel",
                                                                               new Rectangle2D.Double(0, 0, 200, 100),
                                                                               Color.RED,
                                                                               ElementAlignment.LEFT,
                                                                               new FontDefinition("Arial", 12),
                                                                               "CustomerLabel"));

        TextElement element = TextFieldElementFactory.createStringElement(
                "CustomerField",
                new Rectangle2D.Double(210, 0, 150, 1030),
                Color.black,
                ElementAlignment.LEFT,
                ElementAlignment.TOP,
                null, // font
                "-", // null string
                "Customer"
        );


        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_COLOR, Color.RED);
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_WIDTH, new Float(5));
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_STYLE, BorderStyle.SOLID);

        //TODO Was für Werte muss ich wählen, damit die Linien nicht schräg über den Report laufen. Ich glaube hier ist etwas noch nicht richtig.
        //element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_LEFT_RADIUS, new Dimension(5, 5));

        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_LEFT_COLOR, Color.GREEN);
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_LEFT_WIDTH, new Float(5));
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_LEFT_STYLE, BorderStyle.SOLID);

        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_RIGHT_COLOR, Color.YELLOW);
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_RIGHT_WIDTH, new Float(5));
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_RIGHT_STYLE, BorderStyle.SOLID);

        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BREAK_COLOR, Color.CYAN);
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BREAK_WIDTH, new Float(1));
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BREAK_STYLE, BorderStyle.DOTTED);

        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_COLOR, Color.CYAN);
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_WIDTH, new Float(5));
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_STYLE, BorderStyle.SOLID);

        //TODO Zum Testen mit halbtransparenter Farbe.
        //TODO Ein Rechteck wird hinter dem Text mit der originalgrösse des Textes gezeichnet, und nicht mit der Elementgrösse, auch wenn OVEFRFLOW_X nicht auf true gesetzt ist.
        //TODO Wird Transparenz bei Farben unterstützt?
        element.getStyle().setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, new Color(255, 127, 127, 120));

        //TODO padding-left wird auch als padding-top verwendet. Vermutlich verursacht durch den Fehler im ElementStyleKeys (siehe Mail)
        element.getStyle().setStyleProperty(ElementStyleKeys.PADDING_LEFT, new Float(5));
        //element.getStyle().setStyleProperty(ElementStyleKeys.PADDING_TOP, new Float(5));

        //TODO sollte das Padding des Hintergrundes berücksichtigt werden, oder sollte der Hintergrund im Overflow-Bereich nicht erscheinen?
        //element.getStyle().setStyleProperty(ElementStyleKeys.OVERFLOW_X, Boolean.TRUE);

        report.getItemBand().addElement(element);

        report.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, reportTableModel));

        final ReportConfiguration config = new ReportConfiguration(report.getReportConfiguration());

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setSize(600, 400);
        preview.setVisible(true);
    }
}