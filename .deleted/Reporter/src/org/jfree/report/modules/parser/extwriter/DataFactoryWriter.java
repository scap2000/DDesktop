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
 * DataFactoryWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;

import org.jfree.report.AbstractReportDefinition;
import org.jfree.report.DataFactory;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * Creation-Date: Jan 18, 2007, 6:39:15 PM
 *
 * @author Thomas Morgner
 */
public class DataFactoryWriter extends AbstractXMLDefinitionWriter
{
  private static final String PREFIX =
      "org.jfree.report.modules.parser.extwriter.handler.datafactories.";

  public DataFactoryWriter(final ReportWriterContext reportWriter, final XmlWriter xmlWriter)
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
    final AbstractReportDefinition reportDef = getReport();
    if (reportDef instanceof JFreeReport == false)
    {
      // subreports have no data-factory at all.
      return;
    }

    // first, try to find a suitable writer implementation.
    final JFreeReport report = (JFreeReport) getReport();
    final DataFactory dataFactory = report.getDataFactory();

    final String configKey = PREFIX + dataFactory.getClass().getName();
    final Configuration globalConfig = JFreeReportBoot.getInstance().getGlobalConfig();
    final String value = globalConfig.getConfigProperty(configKey);
    if (value != null)
    {
      final DataFactoryWriteHandler handler =
          (DataFactoryWriteHandler) ObjectUtilities.loadAndInstantiate
          (value, DataFactoryWriter.class, DataFactoryWriteHandler.class);
      if (handler != null)
      {
        handler.write(getReportWriter(), getXmlWriter());
        return;
      }
    }

    // then fall back to the default ..
    writeDefaultDataFactory();
  }


  private void writeDefaultDataFactory() throws IOException
  {
    final JFreeReport report = (JFreeReport) getReport();
    final DataFactory dataFactory = report.getDataFactory();

    String dataFactoryClass = null;
    if (dataFactory != null)
    {
      if (hasPublicDefaultConstructor(dataFactory.getClass()))
      {
        dataFactoryClass = dataFactory.getClass().getName();
      }
    }

    if (dataFactoryClass == null)
    {
      return;
    }

    final AttributeList attr = new AttributeList();
    if (dataFactoryClass != null)
    {
      attr.setAttribute(ExtParserModule.NAMESPACE, "type", dataFactoryClass);
    }
    // todo: Fork into a suitable writer for the data-factory ..
    final XmlWriter xmlWriter = getXmlWriter();
    xmlWriter.writeTag(ExtParserModule.NAMESPACE, "data-factory",
        attr, XmlWriter.CLOSE);
  }

}
