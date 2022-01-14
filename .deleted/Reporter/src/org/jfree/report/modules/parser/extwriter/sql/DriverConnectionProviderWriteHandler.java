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
 * DriverConnectionProviderWriteHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.extwriter.sql;

import java.io.IOException;

import org.jfree.report.modules.misc.datafactory.sql.ConnectionProvider;
import org.jfree.report.modules.misc.datafactory.sql.DriverConnectionProvider;
import org.jfree.report.modules.parser.extwriter.ReportWriterContext;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * Creation-Date: Jan 19, 2007, 5:03:22 PM
 *
 * @author Thomas Morgner
 */
public class DriverConnectionProviderWriteHandler
    implements ConnectionProviderWriteHandler
{
  public DriverConnectionProviderWriteHandler()
  {
  }

  public void write(final ReportWriterContext reportWriter,
                    final XmlWriter xmlWriter,
                    final ConnectionProvider connectionProvider)
      throws IOException, ReportWriterException
  {
    final DriverConnectionProvider driverProvider =
        (DriverConnectionProvider) connectionProvider;
    xmlWriter.writeTag
        (SQLDataFactoryWriteHandler.NAMESPACE, "connection", XmlWriter.OPEN);

    xmlWriter.writeTag
        (SQLDataFactoryWriteHandler.NAMESPACE, "driver", XmlWriter.OPEN);
    xmlWriter.writeText(XmlWriter.normalize(driverProvider.getDriver(), false));
    xmlWriter.writeCloseTag();

    xmlWriter.writeTag
        (SQLDataFactoryWriteHandler.NAMESPACE, "url", XmlWriter.OPEN);
    xmlWriter.writeText(XmlWriter.normalize(driverProvider.getUrl(), false));
    xmlWriter.writeCloseTag();

    xmlWriter.writeTag
        (SQLDataFactoryWriteHandler.NAMESPACE, "properties", XmlWriter.OPEN);
    final String[] propertyNames = driverProvider.getPropertyNames();
    for (int i = 0; i < propertyNames.length; i++)
    {
      final String name = propertyNames[i];
      final String value = driverProvider.getProperty(name);
      xmlWriter.writeTag(SQLDataFactoryWriteHandler.NAMESPACE,
          "property", "name", name, XmlWriter.OPEN);
      xmlWriter.writeText(XmlWriter.normalize(value, false));
      xmlWriter.writeCloseTag();
    }
    xmlWriter.writeCloseTag();

    xmlWriter.writeCloseTag();
  }
}
