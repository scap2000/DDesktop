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
 * DemoFrontend.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo;

import java.net.URL;

import javax.swing.JComponent;

import org.digitall.lib.components.Login;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.ancient.demo.bookstore.BookstoreDemo;
import org.jfree.report.ancient.demo.cards.CardDemo;
import org.jfree.report.ancient.demo.chartdemo.ChartDemos;
import org.jfree.report.ancient.demo.conditionalgroup.ConditionalGroupDemo;
import org.jfree.report.ancient.demo.fonts.FontDemo;
import org.jfree.report.ancient.demo.form.SimplePatientFormDemo;
import org.jfree.report.ancient.demo.functions.FunctionsDemo;
import org.jfree.report.ancient.demo.groups.GroupsDemo;
import org.jfree.report.ancient.demo.groups.LogEventDemo;
import org.jfree.report.ancient.demo.groups.RowbandingDemo;
import org.jfree.report.ancient.demo.groups.TrafficLightingDemo;
import org.jfree.report.ancient.demo.huge.VeryLargeReportDemo;
import org.jfree.report.ancient.demo.internationalisation.I18nDemo;
import org.jfree.report.ancient.demo.invoice.InvoiceDemo;
import org.jfree.report.ancient.demo.largetext.LGPLTextDemo;
import org.jfree.report.ancient.demo.layouts.LayoutDemo;
import org.jfree.report.ancient.demo.multireport.MultiReportDemoCollection;
import org.jfree.report.ancient.demo.onetomany.PeopleReportDemo;
import org.jfree.report.ancient.demo.opensource.OpenSourceDemo;
import org.jfree.report.ancient.demo.sportscouncil.SportsCouncilDemo;
import org.jfree.report.ancient.demo.stylesheets.StyleSheetDemoHandler;
import org.jfree.report.ancient.demo.subreport.SubReportDemoCollection;
import org.jfree.report.ancient.demo.surveyscale.SurveyScaleDemo;
import org.jfree.report.ancient.demo.swingicons.SwingIconsDemo;
import org.jfree.report.ancient.demo.world.WorldDemo;
import org.jfree.report.demo.JFreeReportDemoBoot;
import org.jfree.report.demo.features.datasource.SQLDataSourceDemo;
import org.jfree.report.demo.features.subreport.SQLSubReportDemo;
import org.jfree.report.demo.util.CompoundDemoFrame;
import org.jfree.report.demo.util.DefaultDemoSelector;
import org.jfree.report.demo.util.DemoSelector;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

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
 * DemoFrontend.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class DemoFrontend extends CompoundDemoFrame
{
  private JComponent infoPane;

  public DemoFrontend(final DemoSelector demoSelector)
  {
    super(demoSelector);
    setIgnoreEmbeddedConfig(true);
    final ModifiableConfiguration editableConfig =
            JFreeReportDemoBoot.getInstance().getEditableConfig();
    editableConfig.setConfigProperty(EMBEDDED_KEY, "true");
    init();
  }
  public static DemoSelector createDemoInfo ()
  {
    final DefaultDemoSelector rootSelector = new DefaultDemoSelector
            ("All JFreeReport Demos");

    // the most important demos first: the ones that get you started
    rootSelector.addDemo(new HelloWorld());
    rootSelector.addDemo(new GroupsDemo());
    rootSelector.addDemo(new LogEventDemo());
    rootSelector.addDemo(new SwingIconsDemo());
    rootSelector.addDemo(new RowbandingDemo());
    rootSelector.addDemo(new TrafficLightingDemo());
    //
    rootSelector.addChild(OpenSourceDemo.createDemoInfo());
    rootSelector.addChild(WorldDemo.createDemoInfo());
    rootSelector.addChild(InvoiceDemo.createDemoInfo());
    rootSelector.addChild(PeopleReportDemo.createDemoInfo());
    rootSelector.addChild(SurveyScaleDemo.createDemoInfo());
    rootSelector.addChild(FunctionsDemo.createDemoInfo());
    rootSelector.addChild(LayoutDemo.createDemoInfo());
    rootSelector.addChild(CardDemo.createDemoInfo());
    rootSelector.addChild(MultiReportDemoCollection.createDemoInfo());
    rootSelector.addChild(SubReportDemoCollection.createDemoInfo());
    rootSelector.addChild(ChartDemos.createDemoInfo());

    rootSelector.addDemo(new ConditionalGroupDemo());
    rootSelector.addDemo(new SimplePatientFormDemo());
    rootSelector.addDemo(new SportsCouncilDemo());
    rootSelector.addDemo(new LGPLTextDemo());
    rootSelector.addDemo(new I18nDemo());
    rootSelector.addDemo(new VeryLargeReportDemo());
    rootSelector.addDemo(new BookstoreDemo());
    rootSelector.addDemo(new FontDemo());
    rootSelector.addDemo(new StyleSheetDemoHandler());
    //rootSelector.addDemo(new CSVReaderDemo());

    rootSelector.addDemo(new SQLSubReportDemo());
    rootSelector.addDemo(new SQLDataSourceDemo());
    return rootSelector;
  }

  protected JComponent getNoHandlerInfoPane()
  {
    if (infoPane == null)
    {
      final URL url = ObjectUtilities.getResource
            ("org/jfree/report/demo/demo-introduction.html", CompoundDemoFrame.class);

      infoPane = createDescriptionTextPane(url);
    }
    return infoPane;
  }

  public static void main (final String[] args)
  {
  
    Login log = new Login("","",true,false);
    log.setModal(true);
    log.show();
  
    JFreeReportDemoBoot.getInstance().start();

    final DemoFrontend frontend = new DemoFrontend(createDemoInfo());
    frontend.pack();
    RefineryUtilities.centerFrameOnScreen(frontend);
    frontend.setVisible(true);
  }
}
