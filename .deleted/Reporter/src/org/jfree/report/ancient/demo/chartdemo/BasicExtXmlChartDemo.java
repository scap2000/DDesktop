/*
 * Copyright (c) 2007, Your Corporation. All Rights Reserved.
 */

package org.jfree.report.ancient.demo.chartdemo;

import java.awt.Color;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.Rotation;

public class BasicExtXmlChartDemo extends AbstractXmlDemoHandler
{
  public BasicExtXmlChartDemo()
  {
  }

  public String getDemoName()
  {
    return "Basic JFreeChart Demo (Ext-XML)";
  }

  /**
   * Creates a sample dataset for the demo.
   *
   * @return A sample dataset.
   */
  private PieDataset createSampleDataset()
  {

    final DefaultPieDataset result = new DefaultPieDataset();
    result.setValue("Java", new Double(43.2));
    result.setValue("Visual Basic", new Double(10.0));
    result.setValue("C/C++", new Double(17.5));
    result.setValue("PHP", new Double(32.5));
    result.setValue("Perl", new Double(0.0));
    return result;

  }

  /**
   * Creates a sample chart.
   *
   * @param dataset  the dataset.
   *
   * @return A chart.
   */
  private JFreeChart createChart(final PieDataset dataset)
  {

    final JFreeChart chart = ChartFactory.createPieChart3D(
        "Pie Chart 3D Demo 1", // chart title
        dataset, // data
        true, // include legend
        true,
        false
    );

    // set the background color for the chart...
    chart.setBackgroundPaint(Color.yellow);
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
    // create a dataset...
    final PieDataset dataset = createSampleDataset();
    // create the chart...
    final JFreeChart chart = createChart(dataset);
    report.setProperty("Chart", chart);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("basic-chart.html", BasicExtXmlChartDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return new JPanel();
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("basic-chart-ext.xml", BasicExtXmlChartDemo.class);
  }

}
