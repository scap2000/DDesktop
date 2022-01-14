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
 * PropertyUpdatePreviewHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.layouts;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.util.AbstractDemoFrame;
import org.jfree.report.demo.util.InternalDemoHandler;
import org.jfree.report.demo.util.PreviewHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;

/**
 * A helper class to make this demo accessible from the DemoFrontend.
 */
public class PropertyUpdatePreviewHandler implements PreviewHandler
{
  private InternalDemoHandler handler;

  public PropertyUpdatePreviewHandler(final InternalDemoHandler handler)
  {
    this.handler = handler;
  }

  public void attemptPreview()
  {
    try
    {
      final JFreeReport report = handler.createReport();

      final PreviewDialog frame = new PreviewDialog(report);
      frame.setToolbarFloatable(true);
      frame.setReportController(new DemoReportController());
      frame.pack();
      RefineryUtilities.positionFrameRandomly(frame);
      frame.setVisible(true);
      frame.requestFocus();
    }
    catch (ReportDefinitionException e)
    {
      Log.error("Unable to create the report; report definition contained errors.", e);
      AbstractDemoFrame.showExceptionDialog("report.definitionfailure", e);
    }
  }
}
