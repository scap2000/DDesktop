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
 * StyleSheetDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.stylesheets;

import java.awt.Color;

import java.io.IOException;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.ancient.demo.world.CountryDataTableModel;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.demo.util.SimpleDemoFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.resourceloader.ResourceException;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 11.10.2005, 12:54:29
 *
 * @author Thomas Morgner
 */
public class StyleSheetDemoHandler extends AbstractXmlDemoHandler
{
  private TableModel data;

  public StyleSheetDemoHandler()
  {
    data = new CountryDataTableModel();
  }

  public String getDemoName()
  {
    return "WorldDemo using StyleSheets and Macros";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    // create a private instance of the parser so that we can safely modify
    // the configuration..
    final ReportGenerator generator = ReportGenerator.createInstance();
    generator.setObject("outer-header-color", "red");

    final URL in = getReportDefinitionSource();
    if (in == null)
    {
      throw new ReportDefinitionException("URL is invalid");
    }
    try
    {
      JFreeReport report = generator.parseReport(in);
      final StyleSheetCollection styleCollection =
          report.getStyleSheetCollection();
      final ElementStyleSheet styleSheet =
          styleCollection.createStyleSheet("my-style");
      styleSheet.setStyleProperty(ElementStyleSheet.FONT, "SansSerif");
      styleSheet.setStyleProperty(ElementStyleSheet.PAINT, Color.blue);

      report.setDataFactory(new TableDataFactory("default", data));
      return report;
    }
    catch (IOException e)
    {
      throw new ReportDefinitionException("IOError", e);
    }
    catch (ResourceException e)
    {
      throw new ReportDefinitionException("ResourceError", e);
    }
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("stylesheets.html", StyleSheetDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("stylesheets.xml", StyleSheetDemoHandler.class);
  }

  public static void main(String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final StyleSheetDemoHandler handler = new StyleSheetDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
