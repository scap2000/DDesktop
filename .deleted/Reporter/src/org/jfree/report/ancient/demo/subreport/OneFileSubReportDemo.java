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
 * OneFileSubReportDemo.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.subreport;

import java.io.IOException;
import java.io.OutputStreamWriter;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.modules.misc.tablemodel.JoiningTableModel;
import org.jfree.report.modules.parser.ext.factory.base.ArrayClassFactory;
import org.jfree.report.modules.parser.ext.factory.base.ExtraShapesClassFactory;
import org.jfree.report.modules.parser.ext.factory.base.URLClassFactory;
import org.jfree.report.modules.parser.ext.factory.datasource.DefaultDataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.DefaultElementFactory;
import org.jfree.report.modules.parser.ext.factory.objects.BandLayoutClassFactory;
import org.jfree.report.modules.parser.ext.factory.objects.DefaultClassFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.DefaultStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.templates.DefaultTemplateCollection;
import org.jfree.report.modules.parser.extwriter.ReportWriter;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.util.ObjectUtilities;

/**
 * The MultiReportDemo combines data from multiple destTable models
 * into one single report.
 * <p>
 * For a detailed explaination of the demo have a look at the
 * file <a href="multireport.html">file</a>'.
 *
 * @author Thomas Morgner
 */
public class OneFileSubReportDemo extends AbstractXmlDemoHandler
{
  /**
   * The data for the report.
   */
  private final TableModel data;

  public OneFileSubReportDemo()
  {
    this.data = createJoinedTableModel();
  }

  public String getDemoName()
  {
    return "Multi-Report Demo (Sub-Report-Version; One file)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    return parseReport();
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("multireport.html", OneFileSubReportDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("one-file-subreport.xml", OneFileSubReportDemo.class);
  }

  public static TableModel createFruitTableModel ()
  {
    final String[] names = new String[]{"Id Number", "Cat", "Fruit"};
    final Object[][] data = new Object[][]{
      { "I1", "A", "Apple"},
      { "I2", "A", "Orange"},
      { "I3", "B", "Water melon"},
      { "I4", "B", "Strawberry"},
    };
    return new DefaultTableModel(data, names);
  }

  public static TableModel createColorTableModel ()
  {
    final String[] names = new String[]{"Number", "Group", "Color"};
    final Object[][] data = new Object[][]{
      { new Integer(1), "X", "Red"},
      { new Integer(2), "X", "Green"},
      { new Integer(3), "Y", "Yellow"},
      { new Integer(4), "Y", "Blue"},
      { new Integer(5), "Z", "Orange"},
      { new Integer(6), "Z", "White"},
    };
    return new DefaultTableModel(data, names);
  }

  private TableModel createJoinedTableModel ()
  {
    final JoiningTableModel jtm = new JoiningTableModel();
    jtm.addTableModel("Color", OneFileSubReportDemo.createColorTableModel());
    jtm.addTableModel("Fruit", OneFileSubReportDemo.createFruitTableModel());
    return jtm;
  }

  public static void main (final String[] args)
      throws ReportDefinitionException, IOException, ReportWriterException
  {
    JFreeReportBoot.getInstance().start();

    final OneFileSubReportDemo demoHandler = new OneFileSubReportDemo();
//    final SimpleDemoFrame frame = new SimpleDemoFrame(demoHandler);
//    frame.init();
//    frame.pack();
//    RefineryUtilities.centerFrameOnScreen(frame);
//    frame.setVisible(true);
    final JFreeReport report = demoHandler.createReport();
    final ReportWriter writer = new ReportWriter(report, "ISO-8859-1",
        ReportWriter.createDefaultConfiguration(report));
    writer.addClassFactoryFactory(new URLClassFactory());
    writer.addClassFactoryFactory(new DefaultClassFactory());
    writer.addClassFactoryFactory(new BandLayoutClassFactory());
    writer.addClassFactoryFactory(new ArrayClassFactory());
    writer.addClassFactoryFactory(new ExtraShapesClassFactory());
    writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
    writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
    writer.addTemplateCollection(new DefaultTemplateCollection());
    writer.addElementFactory(new DefaultElementFactory());
    writer.addDataSourceFactory(new DefaultDataSourceFactory());
    final OutputStreamWriter w = new OutputStreamWriter(System.out);
    writer.write(w);
    w.close();
  }
}
