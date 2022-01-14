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
 * OpenSourceDemoApplet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.opensource;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.modules.gui.base.PreviewApplet;

/**
 * This demo application replicates the report generated by OpenSourceXMLDemoHandler.java, but
 * creates the report in code rather than using an XML report template. The whole report
 * is displayed in an applet.
 * <p/>
 * To use or adopt that applet to your own needs, just change the getReport() method and
 * your applet should work perfectly ...
 *
 * @author Thomas Morgner
 */
public class OpenSourceDemoApplet extends PreviewApplet
{
  /**
   * The report (created in code).
   */
  private JFreeReport report;

  /**
   * Constructs the demo applet.
   */
  public OpenSourceDemoApplet ()
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();
  }

  public void init()
  {
    super.init();
    setReportJob(createReport());
  }

  /**
   * Creates a report definition in code.
   * <p/>
   * It is more base to read the definition from an XML report template file, but
   * sometimes you might need to create a report dynamically.
   *
   * @return a report.
   */
  public JFreeReport createReport ()
  {

    if (this.report != null)
    {
      return this.report;
    }

    // uses the report from the OpenSourceXMLDemoHandler 2 ...
    this.report = OpenSourceAPIDemoHandler.createStaticReport();
    report.setDataFactory(new TableDataFactory("default", new OpenSourceProjects()));
    return report;
  }

}
