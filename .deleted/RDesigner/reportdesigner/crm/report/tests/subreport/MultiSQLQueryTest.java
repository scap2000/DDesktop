package org.pentaho.reportdesigner.crm.report.tests.subreport;

import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.SubReport;
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.misc.datafactory.sql.SQLReportDataFactory;
import org.jfree.report.style.FontDefinition;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCClassLoader;
import org.pentaho.reportdesigner.crm.report.datasetplugin.sampledb.SampleDB;

import java.awt.*;
import java.sql.Connection;

/**
 * User: Martin
 * Date: 24.02.2007
 * Time: 11:21:18
 */
@SuppressWarnings({"ALL"})
public class MultiSQLQueryTest
{
    public static void main(String[] args) throws Exception
    {
        JFreeReportBoot.getInstance().start();

        JFreeReport report = new JFreeReport();

        SampleDB.initSampleDB();

        Connection connection = JDBCClassLoader.getConnection("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:sample", "sa", "");
        SQLReportDataFactory sqlReportDataFactory = new SQLReportDataFactory(connection);
        sqlReportDataFactory.setQuery("query1", "select * from customers, orders where customers.CUSTOMER_ID=orders.CUSTOMER_ID");
        sqlReportDataFactory.setQuery("subQuery1", "select * from order_items where order_id = ${ORDER_ID}");
        report.setDataFactory(sqlReportDataFactory);

        report.setQuery("query1");

        TextElement textElement = TextFieldElementFactory.createStringElement("reportField1", new Rectangle(0, 0, 100, 20), Color.BLACK, ElementAlignment.LEFT, ElementAlignment.TOP, new FontDefinition("Arial", 12), "-", "FIRST_NAME");
        report.getItemBand().addElement(textElement);
        TextElement textElement2 = TextFieldElementFactory.createStringElement("reportField2", new Rectangle(100, 0, 100, 20), Color.BLACK, ElementAlignment.LEFT, ElementAlignment.TOP, new FontDefinition("Arial", 12), "-", "CUSTOMER_ID");
        report.getItemBand().addElement(textElement2);

        SubReport subReport = new SubReport();
        subReport.addInputParameter("ORDER_ID", "ORDER_ID");

        subReport.setQuery("subQuery1");
        TextElement subReportTextElement = TextFieldElementFactory.createStringElement("subreportField1", new Rectangle(200, 0, 300, 20), Color.RED, ElementAlignment.LEFT, ElementAlignment.TOP, new FontDefinition("Arial", 12), "-", "PRODUCT_ID");
        subReport.getItemBand().addElement(subReportTextElement);
        report.getItemBand().addSubReport(subReport);

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}
