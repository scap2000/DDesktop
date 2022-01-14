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
 * WayBillDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.functions;

import java.net.URL;

import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.demo.util.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 01.10.2005, 11:47:14
 *
 * @author Thomas Morgner
 */
public class WayBillDemoHandler extends AbstractXmlDemoHandler
{
  private WayBillTableModel tableModel;

  public WayBillDemoHandler()
  {
    tableModel = new WayBillTableModel();
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container A", "Glass Pearls", "Fragile", 5000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container A", "Chinese Silk", "Keep Dry", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container A", "Incense", "", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container B", "Palladium", "", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container B", "Tungsten", "", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container B", "Grain", "Keep Dry", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Container B", "Scottish Whiskey", "Stay Dry!", 1000));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Notes", "Note", "This freight is dutyable.", 0));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Notes", "Note", "Customs paid on 2005-08-12 12:00.", 0));
    tableModel.addItem (new WayBillTableModel.CategoryItem
            ("Notes", "Note", "Customs bill id: NY-A32ZY48473", 0));
  }

  public String getDemoName()
  {
    return "Way-Bill Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setDataFactory(new TableDataFactory
        ("default", tableModel));
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("waybill.html", WayBillDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(tableModel);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("waybill.xml", WayBillDemoHandler.class);
  }


  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final WayBillDemoHandler demoHandler = new WayBillDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(demoHandler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
