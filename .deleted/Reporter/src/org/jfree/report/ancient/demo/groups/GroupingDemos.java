/*
 * Copyright (c) 2007, Your Corporation. All Rights Reserved.
 */

package org.jfree.report.ancient.demo.groups;

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
public class GroupingDemos extends CompoundDemoFrame
{
  public GroupingDemos(final DemoSelector demoSelector)
  {
    super(demoSelector);
    init();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final DefaultDemoSelector demoSelector = GroupingDemos.createDemoInfo();

    final GroupingDemos frame = new GroupingDemos(demoSelector);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }

  public static DefaultDemoSelector createDemoInfo() {
    final DefaultDemoSelector demoSelector =
            new DefaultDemoSelector("Grouping Demos");
    demoSelector.addDemo(new GroupsDemo());
    demoSelector.addDemo(new LogEventDemo());
    demoSelector.addDemo(new RowbandingDemo());
    demoSelector.addDemo(new TrafficLightingDemo());
    return demoSelector;
  }
}
