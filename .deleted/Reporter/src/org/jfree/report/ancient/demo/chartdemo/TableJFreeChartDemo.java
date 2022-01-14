/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * MultipleJFreeChartDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableJFreeChartDemo.java 3102 2007-08-01 20:19:29Z tmorgner $
 *
 * Changes
 * -------
 *
 *
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

/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * MultipleJFreeChartDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableJFreeChartDemo.java 3102 2007-08-01 20:19:29Z tmorgner $
 *
 * Changes
 * -------
 *
 *
 */
public class TableJFreeChartDemo extends AbstractXmlDemoHandler
{
  private TableModel data;


  /**
   * Creates a new demo.
   */
  public TableJFreeChartDemo()
  {
    data = createTableModel();
  }

  public String getDemoName()
  {
    return "Table And JFreeChart Demo (Simple-XML)";
  }

  /**
   * Creates a sample dataset for the demo.
   *
   * @return A sample dataset.
   */
  private PieDataset createSampleDataset(final int[] votes)
  {
    final DefaultPieDataset result = new DefaultPieDataset();
    // cheating: java has a higher chance to be the best language :)
    result.setValue("Java", new Integer(votes[0]));
    result.setValue("Visual Basic", new Integer(votes[1]));
    result.setValue("C/C++", new Integer(votes[2]));
    result.setValue("PHP", new Integer(votes[3]));
    result.setValue("Perl", new Integer(votes[4]));
    return result;
  }

  /**
   * Creates a sample chart.
   *
   * @return A chart.
   */
  private JFreeChart createChart(final int year, final int[] votes)
  {

    final JFreeChart chart = ChartFactory.createPieChart3D(
        "Programming Language of the Year " + (year), // chart title
        createSampleDataset(votes), // data
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

  private TableModel createTableModel ()
  {
    final Object[][] data = new Object[12 * 5][];
    final int[] votes = new int[5];
    for (int i = 0; i < 12; i++)
    {
      final Integer year = new Integer (1995 + i);
      votes[0] = (int) (Math.random() * 200);
      votes[1] = (int) (Math.random() * 50);
      votes[2] = (int) (Math.random() * 100);
      votes[3] = (int) (Math.random() * 50);
      votes[4] = (int) (Math.random() * 100);

      final JFreeChart chart = createChart(year.intValue(), votes);

      data[i * 5] = new Object[] {
        year, "Java", new Integer(votes[0]), chart
      };
      data[i * 5 + 1] = new Object[] {
        year, "Visual Basic", new Integer(votes[1]), chart
      };
      data[i * 5 + 2] = new Object[] {
        year, "C/C++", new Integer(votes[2]), chart
      };
      data[i * 5 + 3] = new Object[] {
        year, "PHP", new Integer(votes[3]), chart
      };
      data[i * 5 + 4] = new Object[] {
        year, "Perl", new Integer(votes[4]), chart
      };

    }

    final String[] colNames = {
      "year", "language", "votes", "chart"
    };

    return new DefaultTableModel(data, colNames);
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setDataFactory(new TableDataFactory("default", data));
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("table-chart.html", MultiSimpleXmlChartDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("table-chart-simple.xml", MultiSimpleXmlChartDemo.class);
  }
}
