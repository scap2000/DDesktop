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
package org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.JFreeReport;
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
import org.jfree.report.util.ReportConfiguration;
import org.jfree.xml.Parser;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportExporter;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;

import java.io.File;
import java.io.StringWriter;

/**
 * User: Martin
 * Date: 28.10.2005
 * Time: 08:37:52
 */
public class JFreeReportExporter extends ReportExporter
{

    @NotNull
    private String result = "";


    public JFreeReportExporter()
    {
    }


    @NotNull
    public String getExtendedReportDefinition()
    {
        return result;
    }


    public void exportReport(boolean isSubReport, @NotNull Report report) throws Exception
    {
        //noinspection ConstantConditions
        if (report == null)
        {
            throw new IllegalArgumentException("report must not be null");
        }

        JFreeReportVisitor reportVisitor = new JFreeReportVisitor();

        report.accept(null, reportVisitor);

        JFreeReport jFreeReport = reportVisitor.getJFreeReport();

        final ReportConfiguration config = new ReportConfiguration(jFreeReport.getReportConfiguration());
        config.setConfigProperty(Parser.CONTENTBASE_KEY, new File(".").toURI().toURL().toExternalForm());
        jFreeReport.getReportConfiguration().setConfigProperty("org.jfree.report.NoPrinterAvailable", Boolean.TRUE.toString());
        if (report.isUseMaxCharBounds())
        {
            config.setConfigProperty("org.jfree.report.layout.fontrenderer.UseMaxCharBounds", Boolean.TRUE.toString());//NON-NLS
        }

        final ReportWriter writer = new ReportWriter(jFreeReport, XMLConstants.ENCODING, config);
        writer.addClassFactoryFactory(new URLClassFactory());
        writer.addClassFactoryFactory(new DefaultClassFactory());
        writer.addClassFactoryFactory(new BandLayoutClassFactory());
        writer.addClassFactoryFactory(new ArrayClassFactory());

        writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
        writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
        writer.addTemplateCollection(new DefaultTemplateCollection());
        writer.addElementFactory(new DefaultElementFactory());
        writer.addDataSourceFactory(new DefaultDataSourceFactory());

        StringWriter stringWriter = new StringWriter();
        writer.write(stringWriter);

        result = stringWriter.toString();
    }


}
