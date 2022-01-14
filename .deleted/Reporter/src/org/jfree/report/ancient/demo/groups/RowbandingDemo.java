package org.jfree.report.ancient.demo.groups;

import java.io.IOException;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.TableDataFactory;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.demo.util.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * This demo shows how to define nested groups.
 *
 * @author Thomas Morgner
 */
public class RowbandingDemo extends AbstractXmlDemoHandler
{
  private TableModel data;

  public RowbandingDemo()
  {
    data = new ColorAndLetterTableModel();
  }

  public String getDemoName()
  {
    return "Row-Banding Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setDataFactory(new TableDataFactory
        ("default", data));
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("rowbanding.html", RowbandingDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("rowbanding.xml", RowbandingDemo.class);
  }


  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
          throws ReportProcessingException,
          IOException, ReportDefinitionException
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final RowbandingDemo handler = new RowbandingDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
    //HtmlReportUtil.createStreamHTML(handler.createReport(), "/tmp/groups.html");
//    ExcelReportUtil.createXLS(handler.createReport(), "/tmp/groups.xls");
  }
}
