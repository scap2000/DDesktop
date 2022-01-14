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
 * ParserConfigWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.util.Log;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * A parser configuration writer.
 *
 * @author Thomas Morgner
 */
public class ParserConfigWriter extends AbstractXMLDefinitionWriter
{

  /**
   * The 'stylekey-factory' tag name.
   */
  public static final String STYLEKEY_FACTORY_TAG = "stylekey-factory";

  /**
   * The 'template-factory' tag name.
   */
  public static final String TEMPLATE_FACTORY_TAG = "template-factory";

  /**
   * The 'object-factory' tag name.
   */
  public static final String OBJECT_FACTORY_TAG = "object-factory";

  /**
   * The 'datadefinition-factory' tag name.
   */
  public static final String DATADEFINITION_FACTORY_TAG = "datadefinition-factory";

  /**
   * The 'datasource-factory' tag name.
   */
  public static final String DATASOURCE_FACTORY_TAG = "datasource-factory";

  /**
   * The 'element-factory' tag name.
   */
  public static final String ELEMENT_FACTORY_TAG = "element-factory";

  /**
   * Creates a new writer.
   *
   * @param reportWriter the report writer.
   * @param indentLevel  the current indention level.
   */
  public ParserConfigWriter (final ReportWriterContext reportWriter,
                             final XmlWriter xmlWriter)
  {
    super(reportWriter, xmlWriter);
  }

  /**
   * Writes the XML.
   *
   * @param writer the writer.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void write ()
          throws IOException
  {
    final XmlWriter xmlWriter = getXmlWriter();
    xmlWriter.writeTag(ExtParserModule.NAMESPACE, PARSER_CONFIG_TAG, XmlWriter.OPEN);

    writeFactory(OBJECT_FACTORY_TAG,
            filterFactories(getReportWriter().getClassFactoryCollector().getFactories()));
    writeFactory(ELEMENT_FACTORY_TAG,
            filterFactories(getReportWriter().getElementFactoryCollector().getFactories()));
    writeFactory(STYLEKEY_FACTORY_TAG,
            filterFactories(getReportWriter().getStyleKeyFactoryCollector().getFactories()));
    writeFactory(TEMPLATE_FACTORY_TAG,
            filterFactories(getReportWriter().getTemplateCollector().getFactories()));
    writeFactory(DATASOURCE_FACTORY_TAG,
            filterFactories(getReportWriter().getDataSourceCollector().getFactories()));

    xmlWriter.writeCloseTag();
  }

  /**
   * Filters the given factories iterator and removes all duplicate entries.
   *
   * @param it the unfiltered factories iterator.
   * @return a cleaned version of the iterator.
   */
  private Iterator filterFactories (final Iterator it)
  {
    final ReportWriterContext writer = getReportWriter();
    final ArrayList factories = new ArrayList();
    while (it.hasNext())
    {
      final Object o = it.next();
      if (o.equals(writer.getClassFactoryCollector()))
      {
        continue;
      }
      if (o.equals(writer.getDataSourceCollector()))
      {
        continue;
      }
      if (o.equals(writer.getElementFactoryCollector()))
      {
        continue;
      }
      if (o.equals(writer.getStyleKeyFactoryCollector()))
      {
        continue;
      }
      if (o.equals(writer.getTemplateCollector()))
      {
        continue;
      }
      if (factories.contains(o) == false)
      {
        factories.add(o);
      }
    }
    // sort them ?
    return factories.iterator();
  }

  /**
   * Writes a factory element.
   *
   * @param w       the writer.
   * @param tagName the tag name.
   * @param it      an iterator over a collection of factories, which should be defined
   *                for the target report.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeFactory (final String tagName, final Iterator it)
          throws IOException
  {
    while (it.hasNext())
    {
      final Object itObject = it.next();
      final Class itClass = itObject.getClass();
      if (hasPublicDefaultConstructor(itClass) == false)
      {
        final StringBuffer message = new StringBuffer();
        message.append("FactoryClass ");
        message.append(itObject.getClass());
        message.append(" has no default constructor. This class will be ignored");
        Log.warn(message.toString());
        continue;
      }

      final String className = itObject.getClass().getName();
      getXmlWriter().writeTag(ExtParserModule.NAMESPACE, tagName,
          CLASS_ATTRIBUTE, className, XmlWriter.CLOSE);
    }
  }

}
