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
 * SwingIconsDemo.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.swingicons;

import java.io.IOException;

import java.net.URL;

import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.TableDataFactory;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.modules.output.pageable.pdf.PdfReportUtil;
import org.jfree.util.ObjectUtilities;

/**
 * A demonstration application. <P> This demo is written up in the JFreeReport PDF
 * Documentation.  Please notify David Gilbert (david.gilbert@object-refinery.com) if you
 * need to make changes to this file. <P> To run this demo, you need to have the Java Look
 * and Feel Icons jar file on your classpath.
 *
 * @author David Gilbert
 */
public class SwingIconsDemo extends AbstractXmlDemoHandler
{
  private SwingIconsDemoPanel demoPanel;

  /**
   * Constructs the demo application.
   */
  public SwingIconsDemo ()
  {
    demoPanel = new SwingIconsDemoPanel();
  }

  public JComponent getPresentationComponent()
  {
    return demoPanel;
  }

  public String getDemoName()
  {
    return "Swing Icons Report";
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("swing-icons.html", SwingIconsDemo.class);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("swing-icons.xml", SwingIconsDemo.class);
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setDataFactory(new TableDataFactory
        ("default", demoPanel.getData()));
    return report;
  }

  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args) throws ReportProcessingException, IOException, ReportDefinitionException
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final SwingIconsDemo handler = new SwingIconsDemo();

//    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
//    frame.init();
//    frame.pack();
//    RefineryUtilities.centerFrameOnScreen(frame);
//    frame.setVisible(true);
    //HtmlReportUtil.createZIPHTML(handler.createReport(), "/tmp/report.zip");
    PdfReportUtil.createPDF(handler.createReport(), "/tmp/report.pdf");
    //ExcelReportUtil.createXLS(handler.createReport(), "/tmp/report.xls");
  }

}
