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
 * SimpleAdvertisingDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.invoice;

import java.net.URL;

import java.util.Date;

import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.TableDataFactory;
import org.jfree.report.ancient.demo.invoice.model.Advertising;
import org.jfree.report.ancient.demo.invoice.model.Article;
import org.jfree.report.ancient.demo.invoice.model.Customer;
import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
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
 * SimpleAdvertisingDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class SimpleAdvertisingDemoHandler extends AbstractXmlDemoHandler
{
  private AdvertisingTableModel data;

  public SimpleAdvertisingDemoHandler ()
  {
    data = createDataModel();
  }

  public String getDemoName()
  {
    return "Simple Advertising Demo";
  }

  private AdvertisingTableModel createDataModel ()
  {
    final Customer customer =
            new Customer("Will", "Snowman", "Mr.", "12 Federal Plaza",
                    "12346", "AnOtherTown", "Lilliput");
    final Advertising ad = new Advertising(customer, new Date(), "A-000-0123");

    final Article mainboard = new Article("MB.A02", "ZUSE Z0001 Mainboard", 1299.50f);
    final Article hardDisk = new Article
            ("HD.201", "Sillicium Core HDD", 99.50f,
                    "Even the babylonians used stone for long term document archiving, so why shouldn't you?");
    final Article memory = new Article("MEM.30", "ferrit core memory", 119.99f);
    final Article operatingSystem = new Article
            ("OS.36", "Windows XP", 259.99f, "Experience the world of tomorrow by spreading trojans today.");
    ad.addArticle(mainboard, 999.99d);
    ad.addArticle(hardDisk, 79.50);
    ad.addArticle(memory, 99.99f);
    ad.addArticle(operatingSystem, 199.99);

    final AdvertisingTableModel data = new AdvertisingTableModel();
    data.addAdvertising(ad);
    return data;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("advertising.html", SimpleAdvertisingDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("advertising.xml", SimpleAdvertisingDemoHandler.class);
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setDataFactory(new TableDataFactory
        ("default", data));
    return report;
  }
}
