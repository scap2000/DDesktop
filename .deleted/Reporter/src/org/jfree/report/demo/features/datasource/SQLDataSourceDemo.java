package org.jfree.report.demo.features.datasource;

import java.io.IOException;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.modules.output.pageable.pdf.PdfReportUtil;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: Feb 21, 2007, 4:01:57 PM
 *
 * @author Thomas Morgner
 */
public class SQLDataSourceDemo extends AbstractXmlDemoHandler
{
  public SQLDataSourceDemo()
  {
  }

  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName()
  {
    return "SQL-DataSource Demo (External DataSource definition)";
  }

  /**
   * Creates the report. For XML reports, this will most likely call the
   * ReportGenerator, while API reports may use this function to build and
   * return a new, fully initialized report object.
   *
   * @return the fully initialized JFreeReport object.
   * @throws org.jfree.report.demo.util.ReportDefinitionException
   *          if an error occured preventing the report definition.
   */
  public JFreeReport createReport() throws ReportDefinitionException
  {
    return parseReport();
  }

  /**
   * Returns the URL of the XML definition for this report.
   *
   * @return the URL of the report definition.
   */
  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
        ("sql-datasource.report", SQLDataSourceDemo.class);
  }

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
        ("sql-datasource.html", SQLDataSourceDemo.class);
  }

  /**
   * Returns the presentation component for this demo. This component is shown
   * before the real report generation is started. Ususally it contains a JTable
   * with the demo data and/or input components, which allow to configure the
   * report.
   *
   * @return the presentation component, never null.
   */
  public JComponent getPresentationComponent()
  {
    return new JPanel();
  }

  public static void main(String[] args) throws ReportProcessingException, IOException, ReportDefinitionException
  {
    JFreeReportBoot.getInstance().start();

    final SQLDataSourceDemo handler = new SQLDataSourceDemo();
//    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
//    frame.init();
//    frame.pack();
//    RefineryUtilities.centerFrameOnScreen(frame);
//    frame.setVisible(true);
//    ExcelReportUtil.createXLS(handler.createReport(), "/tmp/report.xls");
    PdfReportUtil.createPDF(handler.createReport(), "/tmp/report.pdf");

  }
}
