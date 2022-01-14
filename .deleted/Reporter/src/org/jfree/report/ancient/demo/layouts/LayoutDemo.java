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
 * LayoutDemo.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.layouts;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ancient.demo.layouts.internalframe.InternalFrameDrawingDemoHandler;
import org.jfree.report.demo.util.CompoundDemoFrame;
import org.jfree.report.demo.util.DefaultDemoSelector;
import org.jfree.report.demo.util.DemoSelector;
import org.jfree.ui.RefineryUtilities;

/**
 * The LayoutDemo is the frontend class for all layouting related demos.
 *
 * @author Thomas Morgner
 */
public class LayoutDemo extends CompoundDemoFrame
{
  public LayoutDemo(final DemoSelector demoSelector)
  {
    super(demoSelector);
    init();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final DefaultDemoSelector demoSelector = createDemoInfo();

    final LayoutDemo frame = new LayoutDemo(demoSelector);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }

  public static DefaultDemoSelector createDemoInfo() {
    final DefaultDemoSelector demoSelector =
            new DefaultDemoSelector("Layout demos");
    demoSelector.addDemo(new BandInBandStackingDemoHandler());
    demoSelector.addDemo(new StackedLayoutXMLDemoHandler());
    demoSelector.addDemo(new StackedLayoutAPIDemoHandler());
    demoSelector.addDemo(new ComponentDrawingDemoHandler());
    demoSelector.addDemo(new InternalFrameDrawingDemoHandler());
    return demoSelector;
  }
}
