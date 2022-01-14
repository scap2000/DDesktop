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
 * InternalFrameDrawingDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.layouts.internalframe;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.util.DemoHandler;
import org.jfree.report.demo.util.PreviewHandler;

/**
 * Creation-Date: 11.12.2005, 12:33:51
 *
 * @author Thomas Morgner
 */
public class InternalFrameDrawingDemoHandler implements DemoHandler
{
  public InternalFrameDrawingDemoHandler()
  {
  }

  /**
   * A helper class to make this demo accessible from the DemoFrontend.
   */
  private class InternalFrameDrawingPreviewHandler implements PreviewHandler
  {
    public InternalFrameDrawingPreviewHandler()
    {
    }

    public void attemptPreview()
    {
      final InternalFrameDemoFrame internalFrameDemoFrame = new InternalFrameDemoFrame();
      internalFrameDemoFrame.updateFrameSize(20);
      internalFrameDemoFrame.setVisible(true);
    }
  }

  public String getDemoName()
  {
    return "InternalFrame drawing";
  }

  public PreviewHandler getPreviewHandler()
  {
    return new InternalFrameDrawingPreviewHandler();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();
    new InternalFrameDrawingDemoHandler().getPreviewHandler().attemptPreview();
  }
}
