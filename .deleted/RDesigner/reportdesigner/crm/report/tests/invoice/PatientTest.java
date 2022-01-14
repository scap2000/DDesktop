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
import org.jfree.report.modules.parser.extwriter.ReportWriter;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.resourceloader.ResourceException;
import org.jfree.xml.ElementDefinitionException;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;

import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Martin
 * Date: 10.10.2005
 * Time: 09:28:23
 */
@SuppressWarnings({"ALL"})
public class PatientTest
{
    public static void main(@NotNull String[] args) throws IOException, ElementDefinitionException, ReportWriterException, ResourceException
    {
        JFreeReport report = ReportGenerator.getInstance().parseReport("C:\\Daten\\02_Software\\java\\report\\jfreereport-0.8.6\\jfreereport-0.8.6\\source\\org\\jfree\\report\\demo\\form\\patient.xml");
        ReportWriter reportConfigWriter = new ReportWriter(report, XMLConstants.ENCODING, ReportWriter.createDefaultConfiguration(report));

        FileWriter fileWriter = new FileWriter("C:\\temp\\report\\PatientTest.xml");
        reportConfigWriter.write(fileWriter);
        fileWriter.close();

    }
}
