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
 * OpenSourceXMLDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.opensource;

import java.awt.Image;
import java.awt.Toolkit;

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
import org.jfree.report.modules.output.table.html.HtmlReportUtil;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.WaitingImageObserver;

/**
 * A simple JFreeReport demonstration.  The generated report lists some free and open
 * source software projects for the Java programming language.
 *
 * @author David Gilbert
 */
public class OpenSourceXMLDemoHandler extends AbstractXmlDemoHandler
{
  /**
   * The data for the report.
   */
  private TableModel data;

  /**
   * Constructs the demo application.
   *
   *
   */
  public OpenSourceXMLDemoHandler ()
  {
    this.data = new OpenSourceProjects();
  }

  public String getDemoName()
  {
    return "Open Source Demo (XML)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setDataFactory(new TableDataFactory
        ("default", data));

    final URL imageURL = ObjectUtilities.getResourceRelative
            ("gorilla.jpg", OpenSourceXMLDemoHandler.class);
    final Image image = Toolkit.getDefaultToolkit().createImage(imageURL);
    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();
    report.setProperty("logo", image);
    return report;
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("opensource-xml.html", OpenSourceXMLDemoHandler.class);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("opensource.xml", OpenSourceXMLDemoHandler.class);
  }

  public static void main(final String[] args)
      throws ReportDefinitionException, ReportProcessingException, IOException
  {
    JFreeReportBoot.getInstance().start();
    final OpenSourceXMLDemoHandler handler = new OpenSourceXMLDemoHandler();
//    PdfReportUtil.createPDF(handler.createReport(), "/tmp/report.pdf");

    HtmlReportUtil.createDirectoryHTML(handler.createReport(), "/tmp/report.html");


  }
}
