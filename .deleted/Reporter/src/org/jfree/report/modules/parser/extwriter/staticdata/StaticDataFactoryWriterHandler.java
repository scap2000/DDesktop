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
 * StaticDataFactoryWriterHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.extwriter.staticdata;

import java.io.IOException;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.misc.datafactory.NamedStaticDataFactory;
import org.jfree.report.modules.parser.extwriter.DataFactoryWriteHandler;
import org.jfree.report.modules.parser.extwriter.ReportWriterContext;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * Creation-Date: Jan 18, 2007, 6:41:57 PM
 *
 * @author Thomas Morgner
 */
public class StaticDataFactoryWriterHandler implements DataFactoryWriteHandler
{
  public static final String NAMESPACE =
          "http://jfreereport.sourceforge.net/namespaces/datasources/static";

  public StaticDataFactoryWriterHandler()
  {
  }

  public void write(final ReportWriterContext reportWriter,
                    final XmlWriter xmlWriter)
      throws IOException, ReportWriterException
  {
    final JFreeReport report = (JFreeReport) reportWriter.getReport();
    final NamedStaticDataFactory dataFactory =
        (NamedStaticDataFactory) report.getDataFactory();

    final AttributeList rootAttrs = new AttributeList();
    rootAttrs.addNamespaceDeclaration("data", NAMESPACE);
    xmlWriter.writeTag(NAMESPACE, "static-datasource", rootAttrs, XmlWriter.OPEN);

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
}
