package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.resourceloader.ResourceException;
import org.jfree.xml.ElementDefinitionException;

import java.io.IOException;

/**
 * User: Martin
 * Date: 10.10.2005
 * Time: 09:28:23
 */
@SuppressWarnings({"ALL"})
public class ItemHideTest
{
    public static void main(@NotNull String[] args) throws IOException, ElementDefinitionException, ReportWriterException, ResourceException
    {
        JFreeReportBoot.getInstance().start();
        JFreeReport report = ReportGenerator.getInstance().parseReport("C:\\Daten\\02_Software\\java\\jfreereport\\jfreereport-0.8.8\\jfreereport-0.8.8\\source\\org\\jfree\\report\\demo\\functions\\item-hiding.xml");

        System.out.println("report = " + report);
    }
}
