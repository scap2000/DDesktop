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
 * ReportWriterContext.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.extwriter;

import org.jfree.report.AbstractReportDefinition;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;

/**
 * Creation-Date: Jan 22, 2007, 3:06:53 PM
 *
 * @author Thomas Morgner
 */
public class ReportWriterContext
{
  private ReportWriterContext parent;
  private AbstractReportDefinition reportDefinition;

  protected ReportWriterContext(final AbstractReportDefinition reportDefinition)
  {
    if (reportDefinition == null)
    {
      throw new NullPointerException("Report is null");
    }
    this.reportDefinition = reportDefinition;
  }

  public ReportWriterContext(final AbstractReportDefinition reportDefinition,
                             final ReportWriterContext parent)
  {
    if (reportDefinition == null)
    {
      throw new NullPointerException("Report is null");
    }
    if (parent == null)
    {
      throw new NullPointerException("Parent is null");
    }
    this.reportDefinition = reportDefinition;
    this.parent = parent;
  }

  public AbstractReportDefinition getReport()
  {
    return reportDefinition;
  }

  public ClassFactoryCollector getClassFactoryCollector()
  {
    return parent.getClassFactoryCollector();
  }

  public ElementFactoryCollector getElementFactoryCollector()
  {
    return parent.getElementFactoryCollector();
  }

  public StyleKeyFactoryCollector getStyleKeyFactoryCollector()
  {
    return parent.getStyleKeyFactoryCollector();
  }

  public TemplateCollector getTemplateCollector()
  {
    return parent.getTemplateCollector();
  }

  public DataSourceCollector getDataSourceCollector()
  {
    return parent.getDataSourceCollector();
  }

  public boolean hasParent()
  {
    return parent != null;
  }
}
