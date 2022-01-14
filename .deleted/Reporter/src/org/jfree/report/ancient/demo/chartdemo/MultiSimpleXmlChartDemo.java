/*
 * Copyright (c) 2007, Your Corporation. All Rights Reserved.
 */

package org.jfree.report.ancient.demo.chartdemo;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.report.JFreeReport;
import org.jfree.report.TableDataFactory;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.Rotation;

public class MultiSimpleXmlChartDemo extends AbstractXmlDemoHandler
{
  private TableModel data;

  public MultiSimpleXmlChartDemo()
  {
    data = createTableModel();
  }

  private TableModel createTableModel ()
  {
    final Object[][] data = new Object[12][];
    for (int i = 0; i < 12; i++)
    {
      data[i] = new Object[]{ createChart(i + 1995)};
    }

    final String[] colNames = {
      "Chart"
    };
    return new DefaultTableModel(data, colNames);
  }

  public String getDemoName()
  {
    return "Multiple JFreeChart Demo (Simple-XML)";
  }

  /**
   * Creates a sample dataset for the demo.
   *
   * @return A sample dataset.
   */
  private PieDataset createSampleDataset()
  {
    final DefaultPieDataset result = new DefaultPieDataset();
    // cheating: java has a higher chance to be the best language :)
    result.setValue("Java", new Integer((int) (Math.random() * 200)));
    result.setValue("Visual Basic", new Integer((int) (Math.random() * 50)));
    result.setValue("C/C++", new Integer((int) (Math.random() * 100)));
    result.setValue("PHP", new Integer((int) (Math.random() * 50)));
    result.setValue("Perl", new Integer((int) (Math.random() * 100)));
    return result;

  }

  /**
   * Creates a sample chart.
   *
   * @return A chart.
   */
  private JFreeChart createChart(final int year)
  {

    final JFreeChart chart = ChartFactory.createPieChart3D(
        "Programming Language of the Year " + year, // chart title
        createSampleDataset(), // data
        true, // include legend
        true,
        false
    );

    // set the background color for the chart...
    final PiePlot3D plot = (PiePlot3D) chart.getPlot();
    plot.setStartAngle(270);
    plot.setDirection(Rotation.CLOCKWISE);
    plot.setForegroundAlpha(0.5f);
    plot.setNoDataMessage("No data to display");

    return chart;

  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setDataFactory(new TableDataFactory("default", data));
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("multi-chart.html", MultiSimpleXmlChartDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("multi-chart-simple.xml", MultiSimpleXmlChartDemo.class);
  }

}
