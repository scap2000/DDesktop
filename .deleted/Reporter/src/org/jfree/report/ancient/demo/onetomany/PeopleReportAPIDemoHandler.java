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
 * PeopleReportAPIDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.onetomany;

import java.net.URL;

import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.TableDataFactory;
import org.jfree.report.demo.util.AbstractDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.util.ObjectUtilities;

/**
 * A demo handler that shows how to define the one-to-many reports using the
 * API.
 *
 * @author Thomas Morgner
 */
public class PeopleReportAPIDemoHandler extends AbstractDemoHandler
{
  private PeopleReportTableModel tableModel;

  public PeopleReportAPIDemoHandler()
  {
    tableModel = new PeopleReportTableModel();
  }

  public String getDemoName()
  {
    return "One-To-Many-Elements Reports Demo (API-Version)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final PeopleReportDefinition reportCreator = new PeopleReportDefinition();
    final JFreeReport report = reportCreator.getReport();
    report.setDataFactory(new TableDataFactory
        ("default", tableModel));
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("people-api.html", PeopleReportAPIDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(tableModel);
  }

}
