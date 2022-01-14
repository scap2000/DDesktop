/*
 * Copyright (c) 2007, Your Corporation. All Rights Reserved.
 */

package org.jfree.report.ancient.demo.chartdemo;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.util.CompoundDemoFrame;
import org.jfree.report.demo.util.DefaultDemoSelector;
import org.jfree.report.demo.util.DemoSelector;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 28.08.2005, 21:57:24
 *
 * @author: Thomas Morgner
 */
public class ChartDemos extends CompoundDemoFrame
{
  public ChartDemos(final DemoSelector demoSelector)
  {
    super(demoSelector);
    init();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final DefaultDemoSelector demoSelector = ChartDemos.createDemoInfo();

    final ChartDemos frame = new ChartDemos(demoSelector);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }

  public static DefaultDemoSelector createDemoInfo() {
    final DefaultDemoSelector demoSelector =
            new DefaultDemoSelector("Chart Demos");
    demoSelector.addDemo(new BasicSimpleXmlChartDemo());
    demoSelector.addDemo(new BasicExtXmlChartDemo());
    demoSelector.addDemo(new MultiSimpleXmlChartDemo());
    demoSelector.addDemo(new MultiExtXmlChartDemo());
    demoSelector.addDemo(new MultiAPIChartDemo());
    demoSelector.addDemo(new TableJFreeChartDemo());
    return demoSelector;
  }
}
