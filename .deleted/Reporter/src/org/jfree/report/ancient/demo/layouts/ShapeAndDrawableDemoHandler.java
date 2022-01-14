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
 * ShapeAndDrawableDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.layouts;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.demo.util.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * A simple report where destColumn 3 displays (destColumn 1 / destColumn 2) as a percentage.
 *
 * @author David Gilbert
 */
public class ShapeAndDrawableDemoHandler extends AbstractXmlDemoHandler
{

  /**
   * Constructs the demo application.
   */
  public ShapeAndDrawableDemoHandler ()
  {
  }

  public String getDemoName()
  {
    return "Component Drawing Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("shape-and-drawable.html", ShapeAndDrawableDemoHandler.class);
  }



  public JComponent getPresentationComponent()
  {
    return new JPanel();
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("shape-and-drawable.xml", ShapeAndDrawableDemoHandler.class);
  }


  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final ShapeAndDrawableDemoHandler demoHandler = new ShapeAndDrawableDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(demoHandler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
