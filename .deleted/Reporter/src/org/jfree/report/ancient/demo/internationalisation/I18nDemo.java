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
 * I18nDemo.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.internationalisation;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.PreviewHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.demo.util.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * A simple report where destColumn 3 displays (destColumn 1 / destColumn 2) as a percentage.
 *
 * @author David Gilbert
 */
public class I18nDemo extends AbstractXmlDemoHandler
{

  /**
   * The data for the report.
   */
  private TableModel data;

  /**
   * Constructs the demo application.
   */
  public I18nDemo ()
  {
    this.data = createData();
  }


  public String getDemoName()
  {
    return "Internationalisation Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setDataFactory(new TableDataFactory
        ("default", data));
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("i18n.html", I18nDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("i18n.xml", I18nDemo.class);
  }

  /**
   * Creates a sample dataset. <!-- (Used in JUnitTest) -->
   *
   * @return A <code>TableModel</code>.
   */
  public static TableModel createData ()
  {
    final DefaultTableModel data = new DefaultTableModel();
    data.addColumn("Data");
    data.addColumn("A");
    data.addColumn("B");
    data.addColumn("C");
    data.addRow(new Object[]{"data.firstElement", new Double(43.0), new Double(127.5), new Double(10001.999)});
    data.addRow(new Object[]{"data.secondElement", new Double(57.0), new Double(108.5), new Double(-10001.999)});
    data.addRow(new Object[]{"data.thirdElement", new Double(35.0), new Double(164.8), new Double(-999.9999)});
    data.addRow(new Object[]{"data.fourthElement", new Double(86.0), new Double(164.0), new Double(999.9999)});
    data.addRow(new Object[]{"data.lastElement", new Double(12.0), new Double(103.2), new Double(0.999)});
    return data;
  }

  public PreviewHandler getPreviewHandler()
  {
    return new LocaleUpdatePreviewHandler(this);
  }


  public static void main(String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final I18nDemo handler = new I18nDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
