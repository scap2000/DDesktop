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
package org.pentaho.reportdesigner.crm.report.tests.simple1;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.ext.factory.base.ArrayClassFactory;
import org.jfree.report.modules.parser.ext.factory.base.URLClassFactory;
import org.jfree.report.modules.parser.ext.factory.datasource.DefaultDataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.DefaultElementFactory;
import org.jfree.report.modules.parser.ext.factory.objects.BandLayoutClassFactory;
import org.jfree.report.modules.parser.ext.factory.objects.DefaultClassFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.DefaultStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.templates.DefaultTemplateCollection;
import org.jfree.report.modules.parser.extwriter.ReportWriter;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.xml.Parser;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 15:55:11
 */
@SuppressWarnings({"ALL"})
public class SimpleAbsolutePositions
{
    public static void main(@NotNull String[] args) throws IOException, ReportWriterException
    {
        Object[] columnNames = new Object[]{"Customer", "City", "Number"};

        DefaultTableModel reportTableModel = new DefaultTableModel(
                new Object[][]{{"Customer_ASDFSDFSDFSDFSaasdasdasdasweruzweurzwiezrwieuzriweuzriweu", "Bern", "123"},
                               {"Hugo", "Zürich", "2234"},},
                columnNames);

        JFreeReport report = new JFreeReport();

        report.setName("SimpleAbsolutePositions");

        report.getItemBand().addElement(StaticShapeElementFactory.createRectangleShapeElement("CustomerRect",
                                                                                              Color.GREEN,
                                                                                              new BasicStroke(1, 1, 1),
                                                                                              new Rectangle2D.Double(0, 0, 100, 100),
                                                                                              true,
                                                                                              false));

        report.getItemBand().addElement(LabelElementFactory.createLabelElement("CustomerLabel",
                                                                               new Rectangle2D.Double(0, 0, 100, 100),
                                                                               Color.RED,
                                                                               ElementAlignment.LEFT,
                                                                               new FontDefinition("Arial", 30),
                                                                               "CustomerLabel"));

        TextElement t2 = TextFieldElementFactory.createStringElement(
                "CustomerField",
                new Rectangle2D.Double(110, 0, 150, 20),
                Color.black,
                ElementAlignment.LEFT,
                ElementAlignment.TOP,
                null, // font
                "-", // null string
                "Customer"
        );
        //t2.setFontSize(30);

        TextElement t3 = TextFieldElementFactory.createStringElement(
                "CityField",
                new Rectangle2D.Double(270, 0, 80, 20),
                Color.black,
                ElementAlignment.LEFT,
                ElementAlignment.TOP,
                null, // font
                "-", // null string
                "City"
        );

        TextElement t4 = TextFieldElementFactory.createStringElement(
                "NumberField",
                new Rectangle2D.Double(350, 0, 80, 20),
                Color.black,
                ElementAlignment.LEFT,
                ElementAlignment.TOP,
                null, // font
                "-", // null string
                "Number"
        );


        report.getItemBand().addElement(t2);
        report.getItemBand().addElement(t3);
        report.getItemBand().addElement(t4);

        report.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, reportTableModel));

        //report.setProperty(JFreeReport.REPORT_DEFINITION_CONTENTBASE, "xxx");

        //ReportWriter reportConfigWriter = new ReportWriter(report, XMLConstants.ENCODING, ReportWriter.createDefaultConfiguration(report));

        final ReportConfiguration config = new ReportConfiguration(report.getReportConfiguration());
        config.setConfigProperty(Parser.CONTENTBASE_KEY, new File("C:\\Daten\\07_Projekte\\Report\\org.pentaho.reportdesigner.crm.report\\xml\\SimpleAbsolutePositions.xml").toURI().toURL().toExternalForm());
        config.setConfigProperty("org.jfree.report.NoPrinterAvailable", Boolean.TRUE.toString());

        final ReportWriter writer = new ReportWriter(report, XMLConstants.ENCODING, config);
        writer.addClassFactoryFactory(new URLClassFactory());
        writer.addClassFactoryFactory(new DefaultClassFactory());
        writer.addClassFactoryFactory(new BandLayoutClassFactory());
        writer.addClassFactoryFactory(new ArrayClassFactory());

        writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
        writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
        writer.addTemplateCollection(new DefaultTemplateCollection());
        writer.addElementFactory(new DefaultElementFactory());
        writer.addDataSourceFactory(new DefaultDataSourceFactory());

        FileWriter fileWriter = new FileWriter("C:\\Daten\\07_Projekte\\Report\\org.pentaho.reportdesigner.crm.report\\xml\\SimpleAbsolutePositions.xml");
        writer.write(fileWriter);
        fileWriter.close();


        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}
