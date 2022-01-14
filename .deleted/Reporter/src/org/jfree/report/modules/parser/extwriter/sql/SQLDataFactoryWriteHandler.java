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
 * SQLDataFactoryWriteHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.extwriter.sql;

import java.io.IOException;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.misc.datafactory.sql.ConnectionProvider;
import org.jfree.report.modules.misc.datafactory.sql.SQLReportDataFactory;
import org.jfree.report.modules.parser.extwriter.DataFactoryWriteHandler;
import org.jfree.report.modules.parser.extwriter.ReportWriterContext;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * Creation-Date: Jan 19, 2007, 4:44:05 PM
 *
 * @author Thomas Morgner
 */
public class SQLDataFactoryWriteHandler implements DataFactoryWriteHandler
{
  public static final String PREFIX =
      "org.jfree.report.modules.parser.extwriter.handler.sql-connection-provider.";
  public static final String NAMESPACE =
      "http://jfreereport.sourceforge.net/namespaces/datasources/sql";

  public SQLDataFactoryWriteHandler()
  {
  }

  public void write(final ReportWriterContext reportWriter,
                    final XmlWriter xmlWriter)
      throws IOException, ReportWriterException
  {
    final JFreeReport report = (JFreeReport) reportWriter.getReport();
    final SQLReportDataFactory dataFactory =
        (SQLReportDataFactory) report.getDataFactory();

    final AttributeList rootAttrs = new AttributeList();
    rootAttrs.addNamespaceDeclaration("data", NAMESPACE);
    xmlWriter.writeTag(NAMESPACE, "sql-datasource", rootAttrs, XmlWriter.OPEN);
    xmlWriter.writeTag(NAMESPACE, "config", "label-mapping",
        String.valueOf(dataFactory.isLabelMapping()), XmlWriter.CLOSE);

    writeConnectionInfo(reportWriter, xmlWriter, dataFactory.getConnectionProvider());

    final String[] queryNames = dataFactory.getQueryNames();
    for (int i = 0; i < queryNames.length; i++)
    {
      final String queryName = queryNames[i];
      final String query = dataFactory.getQuery(queryName);
      xmlWriter.writeTag(NAMESPACE, "query", "name", queryName, XmlWriter.OPEN);
      xmlWriter.writeText(XmlWriter.normalize(query, false));
      xmlWriter.writeCloseTag();
    }
    xmlWriter.writeCloseTag();
  }

  private void writeConnectionInfo(final ReportWriterContext reportWriter,
                                   final XmlWriter xmlWriter,
                                   final ConnectionProvider connectionProvider)
      throws IOException, ReportWriterException
  {
    final String configKey = PREFIX + connectionProvider.getClass().getName();
    final Configuration globalConfig = JFreeReportBoot.getInstance().getGlobalConfig();
    final String value = globalConfig.getConfigProperty(configKey);
    if (value != null)
    {
      final ConnectionProviderWriteHandler handler =
          (ConnectionProviderWriteHandler) ObjectUtilities.loadAndInstantiate
              (value, SQLReportDataFactory.class, ConnectionProviderWriteHandler.class);
      if (handler != null)
      {
        handler.write(reportWriter, xmlWriter, connectionProvider);
      }
    }

  }
}
