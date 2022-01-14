package org.pentaho.reportdesigner.crm.report.tests.subreport;

import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ParameterMapping;
import org.jfree.report.SubReport;
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.misc.datafactory.StaticDataFactory;
import org.jfree.report.style.FontDefinition;

import java.awt.*;

/**
 * User: Martin
 * Date: 09.01.2007
 * Time: 15:25:34
 */
@SuppressWarnings({"ALL"})
public class SimpleTest
{
    public static void main(String[] args) throws Exception
    {
        JFreeReportBoot.getInstance().start();

        JFreeReport report = new JFreeReport();
        StaticDataFactory staticDataFactory = new StaticDataFactory();
        report.setDataFactory(staticDataFactory);
        report.setQuery("org.pentaho.reportdesigner.crm.report.tests.subreport.TestDataFactory#createMainTableModel()");

        TextElement textElement = TextFieldElementFactory.createStringElement("reportField1", new Rectangle(0, 0, 100, 20), Color.BLACK, ElementAlignment.LEFT, ElementAlignment.TOP, new FontDefinition("Arial", 12), "-", "c1");
        report.getItemBand().addElement(textElement);

        SubReport subReport = new SubReport();
        subReport.addInputParameter("c1", "c1");
        //subReport.addInputParameter("*", "*");
        //subReport.addExportParameter("t1", "t2");

        subReport.setQuery("org.pentaho.reportdesigner.crm.report.tests.subreport.TestDataFactory#createSubReportTableModel(c1)");
        TextElement subReportTextElement = TextFieldElementFactory.createStringElement("subreportField1", new Rectangle(20, 0, 100, 20), Color.RED, ElementAlignment.LEFT, ElementAlignment.TOP, new FontDefinition("Arial", 12), "-", "t1");
        subReport.getItemBand().addElement(subReportTextElement);
        report.getItemBand().addSubReport(subReport);

        TextElement textElementT1 = TextFieldElementFactory.createStringElement("reportFieldT1", new Rectangle(0, 20, 100, 20), Color.BLACK, ElementAlignment.LEFT, ElementAlignment.TOP, new FontDefinition("Arial", 12), "-", "t2");
        report.getItemBand().addElement(textElementT1);

        ParameterMapping[] parameterMappings = subReport.getExportMappings();
        for (ParameterMapping parameterMapping : parameterMappings)
        {
            System.out.println("parameterMapping.getAlias() = " + parameterMapping.getAlias());
            System.out.println("parameterMapping.getName() = " + parameterMapping.getName());
        }

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}
