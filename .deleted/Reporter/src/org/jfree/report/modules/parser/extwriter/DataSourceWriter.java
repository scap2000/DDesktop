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
 * DataSourceWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;

import org.jfree.report.filter.DataSource;
import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.report.modules.parser.ext.factory.base.ObjectDescription;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * A data-source writer. Writes datasources and templates.
 *
 * @author Thomas Morgner.
 */
public class DataSourceWriter extends ObjectWriter
{
  /**
   * The data-source.
   */
  private DataSourceCollector dataSourceCollector;

  /**
   * Creates a new writer.
   *
   * @param reportWriter      the report writer.
   * @param baseObject        the base object.
   * @param objectDescription the object description.
   * @param indent            the current indention level.
   * @param commentHintPath   the path on where to search for ext-parser comments in the
   *                          report builder hints.
   * @throws ReportWriterException    if an error occured.
   * @throws IllegalArgumentException if the object description does not describe a
   *                                  datasource.
   */
  public DataSourceWriter (final ReportWriterContext reportWriter,
                           final DataSource baseObject,
                           final ObjectDescription objectDescription,
                           final XmlWriter indent)
          throws ReportWriterException
  {
    super(reportWriter, baseObject, objectDescription, indent);
    if (DataSource.class.isAssignableFrom(objectDescription.getObjectClass()) == false)
    {
      throw new IllegalArgumentException("Expect a datasource description, but got "
              + objectDescription.getObjectClass());
    }
    dataSourceCollector = getReportWriter().getDataSourceCollector();
  }

  /**
   * Writes a parameter.
   *
   * @param writer the writer.
   * @param name   the name.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if the report definition could not be written.
   */
  protected void writeParameter (final String name)
          throws IOException, ReportWriterException
  {
    if ("dataSource".equals(name) == false)
    {
      super.writeParameter(name);
      return;
    }

    final DataSource ds = (DataSource) getObjectDescription().getParameter(name);
    final ObjectDescription dsDesc = getParameterDescription(name);
    final String dsname = dataSourceCollector.getDataSourceName(dsDesc);

    if (dsname == null)
    {
      throw new ReportWriterException("The datasource type is not registered: "
              + ds.getClass());
    }

    final XmlWriter writer = getXmlWriter();
    writer.writeTag(ExtParserModule.NAMESPACE, DATASOURCE_TAG,
        "type", dsname, XmlWriter.OPEN);

    final DataSourceWriter dsWriter =
            new DataSourceWriter(getReportWriter(), ds, dsDesc, writer);
    dsWriter.write();
    writer.writeCloseTag();
  }
}
