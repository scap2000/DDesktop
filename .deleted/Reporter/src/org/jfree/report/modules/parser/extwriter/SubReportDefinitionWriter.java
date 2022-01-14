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
 * SubReportDefinitionWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;

import org.jfree.report.ParameterMapping;
import org.jfree.report.SubReport;
import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * Creation-Date: Jan 22, 2007, 3:05:02 PM
 *
 * @author Thomas Morgner
 */
public class SubReportDefinitionWriter extends AbstractXMLDefinitionWriter
{
  public SubReportDefinitionWriter(final ReportWriterContext reportWriter,
                                   final XmlWriter xmlWriter)
  {
    super(reportWriter, xmlWriter);
  }

  /**
   * Writes the report definition portion. Every DefinitionWriter handles one or
   * more elements of the JFreeReport object tree, DefinitionWriter traverse the
   * object tree and write the known objects or forward objects to other
   * definition writers.
   *
   * @throws java.io.IOException if there is an I/O problem.
   * @throws org.jfree.report.modules.parser.extwriter.ReportWriterException
   *                             if the report serialisation failed.
   */
  public void write() throws IOException, ReportWriterException
  {
    final SubReport report = (SubReport) getReport();
    final String query = report.getQuery();
    final XmlWriter xmlWriter = getXmlWriter();

    final AttributeList attList = new AttributeList();
    if (getReportWriter().hasParent() == false)
    {
      attList.addNamespaceDeclaration("", ExtParserModule.NAMESPACE);
    }
    if (query != null)
    {
      attList.setAttribute(ExtParserModule.NAMESPACE, "query", query);
    }
    xmlWriter.writeTag(ExtParserModule.NAMESPACE,
        "sub-report", attList, XmlWriter.OPEN);

    writeParameterDeclaration();

    // no need to write the parser config, if this subreport is inlined.
    if (getReportWriter().hasParent() == false)
    {
      final ParserConfigWriter parserConfigWriter =
          new ParserConfigWriter(getReportWriter(), xmlWriter);
      parserConfigWriter.write();
    }

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

  private void writeParameterDeclaration()
       throws IOException
  {
    final SubReport report = (SubReport) getReport();
    final ParameterMapping[] exportMappings = report.getExportMappings();
    for (int i = 0; i < exportMappings.length; i++)
    {
      final ParameterMapping mapping = exportMappings[i];
      final AttributeList attList = new AttributeList();
      attList.setAttribute(ExtParserModule.NAMESPACE, "name", mapping.getName());
      if (mapping.getAlias().equals(mapping.getName()) == false)
      {
        attList.setAttribute(ExtParserModule.NAMESPACE, "alias", mapping.getAlias());
      }

      getXmlWriter().writeTag(ExtParserModule.NAMESPACE,
          "export-parameter", attList, XmlWriter.CLOSE);
    }

    final ParameterMapping[] importMappings = report.getInputMappings();
    for (int i = 0; i < importMappings.length; i++)
    {
      final ParameterMapping mapping = importMappings[i];
      final AttributeList attList = new AttributeList();
      attList.setAttribute(ExtParserModule.NAMESPACE, "name", mapping.getName());
      if (mapping.getAlias().equals(mapping.getName()) == false)
      {
        attList.setAttribute(ExtParserModule.NAMESPACE, "alias", mapping.getAlias());
      }

      getXmlWriter().writeTag(ExtParserModule.NAMESPACE,
          "import-parameter", attList, XmlWriter.CLOSE);
    }
  }
}
