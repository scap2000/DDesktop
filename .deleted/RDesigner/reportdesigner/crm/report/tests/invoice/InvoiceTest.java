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
package org.pentaho.reportdesigner.crm.report.tests.invoice;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.ReportGenerator;
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
import org.jfree.report.util.ReportConfiguration;
import org.jfree.resourceloader.ResourceException;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.Parser;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Martin
 * Date: 10.10.2005
 * Time: 09:28:23
 */
@SuppressWarnings({"ALL"})
public class InvoiceTest
{
    public static void main(@NotNull String[] args) throws IOException, ElementDefinitionException, ReportWriterException, ResourceException
    {
        JFreeReport report = ReportGenerator.getInstance().parseReport("C:\\Daten\\02_Software\\java\\report\\jfreereport-0.8.6\\jfreereport-0.8.6\\source\\org\\jfree\\report\\demo\\invoice\\invoiceModified.xml");
        //ReportWriter reportConfigWriter = new ReportWriter(report, XMLConstants.ENCODING, ReportWriter.createDefaultConfiguration(report));
        //FileWriter fileWriter = new FileWriter("C:\\temp\\report\\InvoiceTest.xml");
        //reportConfigWriter.write(fileWriter);
        //fileWriter.close();

        //report.setProperty(JFreeReport.REPORT_DEFINITION_CONTENTBASE, "xxx");

        //ReportWriter reportConfigWriter = new ReportWriter(report, XMLConstants.ENCODING, ReportWriter.createDefaultConfiguration(report));

        final ReportConfiguration config = new ReportConfiguration(report.getReportConfiguration());
        config.setConfigProperty(Parser.CONTENTBASE_KEY, new File("C:\\Daten\\07_Projekte\\Report\\org.pentaho.reportdesigner.crm.report\\xml\\InvoiceModified.xml").toURI().toURL().toExternalForm());
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

        FileWriter fileWriter = new FileWriter("C:\\Daten\\07_Projekte\\Report\\org.pentaho.reportdesigner.crm.report\\xml\\InvoiceModified.xml");
        writer.write(fileWriter);
        fileWriter.close();

    }
}
