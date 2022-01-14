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
 * ReportDefinitionWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportInfo;
import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * A report definition writer.
 *
 * @author Thomas Morgner.
 */
public class ReportDefinitionWriter extends AbstractXMLDefinitionWriter
{
  /**
   * Creates a new writer.
   *
   * @param reportWriter the writer context holding the global configuration for this write-operation.
   * @param xmlWriter the report writer.
   */
  public ReportDefinitionWriter(final ReportWriterContext reportWriter,
                                final XmlWriter xmlWriter)
  {
    super(reportWriter, xmlWriter);
  }

  /**
   * Writes a report definition to a character stream writer.  After the
   * standard XML header and the opening tag is written, this class delegates
   * work to:
   * <p/>
   * <ul> <li>{@link ParserConfigWriter}
   * to write the parser configuration;</li> <li>{@link ReportConfigWriter}
   * to write the report configuration;</li> <li>{@link StylesWriter}
   * to write the templates;</li> <li>{@link ReportDescriptionWriter}
   * to write the report description;</li> <li>{@link FunctionsWriter}
   * to write the function definitions;</li> </ul>
   *
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write()
      throws IOException, ReportWriterException
  {
    final JFreeReport report = (JFreeReport) getReport();
    final String reportName = report.getName();
    final String query = report.getQuery();
    final XmlWriter xmlWriter = getXmlWriter();

    final AttributeList attList = new AttributeList();
    attList.addNamespaceDeclaration("", ExtParserModule.NAMESPACE);
    if (reportName != null)
    {
      attList.setAttribute(ExtParserModule.NAMESPACE, "name", reportName);
    }
    if (query != null)
    {
      attList.setAttribute(ExtParserModule.NAMESPACE, "query", query);
    }
    attList.setAttribute(ExtParserModule.NAMESPACE,
        "engine-version", JFreeReportInfo.getInstance().getVersion());

    xmlWriter.writeTag(ExtParserModule.NAMESPACE,
        REPORT_DEFINITION_TAG, attList, XmlWriter.OPEN);

    final ParserConfigWriter parserConfigWriter =
        new ParserConfigWriter(getReportWriter(), xmlWriter);
    parserConfigWriter.write();

    final ReportConfigWriter reportConfigWriter =
        new ReportConfigWriter(getReportWriter(), xmlWriter);
    reportConfigWriter.write();

    final StylesWriter stylesWriter =
        new StylesWriter(getReportWriter(), xmlWriter);
    stylesWriter.write();

    final ReportDescriptionWriter reportDescriptionWriter
        = new ReportDescriptionWriter(getReportWriter(), xmlWriter);
    reportDescriptionWriter.write();

    final FunctionsWriter functionsWriter =
        new FunctionsWriter(getReportWriter(), xmlWriter);
    functionsWriter.write();
    xmlWriter.writeCloseTag();
  }

}
