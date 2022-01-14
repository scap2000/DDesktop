/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 * LogEventDemo.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.groups;

import java.net.URL;

import java.util.GregorianCalendar;

import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.demo.util.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 * LogEventDemo.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class LogEventDemo extends AbstractXmlDemoHandler
{
  private LogEventTableModel tableModel;

  public LogEventDemo ()
  {
    tableModel = new LogEventTableModel();
    GregorianCalendar gc = new GregorianCalendar();

    gc.set(2000, 10, 24, 19, 24, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Arrival", "Aliens arrived in New York"));

    gc.set(2000, 10, 24, 20, 14, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Meeting", "The UFO was stolen in the bronx."));

    gc.set(2000, 10, 24, 22, 14, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Meeting", "The aliens have been robbed."));

    gc.set(2000, 10, 24, 23, 23, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Event", "Hungry and with only little money, the alien entered a restaurant of an famous fast-food chain."));

    gc.set(2000, 10, 24, 23, 23, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Event",
            "The aliens died from drinking strange looking mineral water."));

    gc.set(2000, 10, 25, 9, 10, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Arrival", "The mother of the alien arrives."));

    gc.set(2000, 10, 25, 9, 14, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Meeting", "The UFO was stolen in the lower east side."));

    gc.set(2000, 10, 25, 10, 19, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Meeting", "The alien mother calls their growd."));

    gc.set(2000, 11, 25, 20, 59, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Diplomacy", "A vogon construction fleet appeared over all major cities."));

    gc.set(2000, 11, 25, 21, 10, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Diplomacy", "The United Nations fire a cloud of deadly lawyers against the vogons."));

    gc.set(2000, 11, 25, 21, 15, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Diplomacy", "No vogon survived. Earth is declared uninhabitable to intelligent life."));

    gc.set(2000, 12, 10, 10, 0, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Meeting", "The lawyers, pushed by their sucess, took over control over earth."));

    gc.set(2000, 11, 26, 18, 0, 0);
    tableModel.addEvent(new LogEvent(gc.getTime(), "Doomsday", "Life has gone."));
  }

  /**
   * Creates the report. For XML reports, this will most likely call the ReportGenerator,
   * while API reports may use this function to build and return a new, fully initialized
   * report object.
   *
   * @return the fully initialized JFreeReport object.
   *
   * @throws org.jfree.report.demo.helper.ReportDefinitionException
   *          if an error occured preventing the report definition.
   */
  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setDataFactory(new TableDataFactory
        ("default", tableModel));
    return report;
  }

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource ()
  {
    return ObjectUtilities.getResourceRelative("log-events.html", GroupsDemo.class);
  }

  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName ()
  {
    return "Log-Event demo";
  }

  /**
   * Returns the presentation component for this demo. This component is shown before the
   * real report generation is started. Ususally it contains a JTable with the demo data
   * and/or input components, which allow to configure the report.
   *
   * @return the presentation component, never null.
   */
  public JComponent getPresentationComponent ()
  {
    return createDefaultTable(tableModel);
  }

  /**
   * Returns the URL of the XML definition for this report.
   *
   * @return the URL of the report definition.
   */
  public URL getReportDefinitionSource ()
  {
    return ObjectUtilities.getResourceRelative("log-events.xml", GroupsDemo.class);
  }

  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final LogEventDemo handler = new LogEventDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
