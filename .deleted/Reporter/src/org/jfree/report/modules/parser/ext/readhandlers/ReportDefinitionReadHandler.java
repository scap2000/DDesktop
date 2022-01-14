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
 * ReportDefinitionReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.FunctionsReadHandler;
import org.jfree.report.modules.parser.base.common.IncludeReadHandler;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.xmlns.parser.RootXmlReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.SAXException;

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
 * ReportDefinitionReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class ReportDefinitionReadHandler extends AbstractPropertyXmlReadHandler
{
  public static final String ELEMENT_FACTORY_KEY = "::element-factory";
  public static final String STYLE_FACTORY_KEY = "::stylekey-factory";
  public static final String CLASS_FACTORY_KEY = "::class-factory";
  public static final String DATASOURCE_FACTORY_KEY = "::datasource-factory";
  public static final String TEMPLATE_FACTORY_KEY = "::template-factory";

  private JFreeReport report;

  public ReportDefinitionReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    final Object maybeReport = getRootHandler().getHelperObject
        (ReportParserUtil.HELPER_OBJ_REPORT_NAME);
    final JFreeReport report;
    if (maybeReport instanceof JFreeReport == false)
    {
      // replace it ..
      report = new JFreeReport();
    }
    else
    {
      report = (JFreeReport) maybeReport;
    }

    final RootXmlReadHandler parser = getRootHandler();
    if (ReportParserUtil.isIncluded(parser) == false)
    {
      final String query = attrs.getValue(getUri(), "query");
      if (query != null)
      {
        report.setQuery(query);
      }

      final String value = attrs.getValue(getUri(), "name");
      if (value != null)
      {
        report.setName(value);
      }
    }
    final ElementFactoryCollector elementFactory = new ElementFactoryCollector();
    final StyleKeyFactoryCollector styleKeyFactory = new StyleKeyFactoryCollector();
    final ClassFactoryCollector classFactory = new ClassFactoryCollector();
    final DataSourceCollector dataSourceFactory = new DataSourceCollector();
    final TemplateCollector templateFactory = new TemplateCollector();

    classFactory.configure(getRootHandler().getParserConfiguration());
    dataSourceFactory.configure(getRootHandler().getParserConfiguration());
    templateFactory.configure(getRootHandler().getParserConfiguration());

    getRootHandler().setHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME, report);
    getRootHandler().setHelperObject(ELEMENT_FACTORY_KEY, elementFactory);
    getRootHandler().setHelperObject(STYLE_FACTORY_KEY, styleKeyFactory);
    getRootHandler().setHelperObject(CLASS_FACTORY_KEY, classFactory);
    getRootHandler().setHelperObject(DATASOURCE_FACTORY_KEY, dataSourceFactory);
    getRootHandler().setHelperObject(TEMPLATE_FACTORY_KEY, templateFactory);

    this.report = report;
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild (final String uri,
                                               final String tagName,
                                               final PropertyAttributes atts)
          throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if ("parser-config".equals(tagName))
    {
      return new ParserConfigReadHandler();
    }
    else if ("report-config".equals(tagName))
    {
      return new ReportConfigReadHandler();
    }
    else if ("styles".equals(tagName))
    {
      return new StylesReadHandler();
    }
    else if ("templates".equals(tagName))
    {
      return new TemplatesReadHandler();
    }
    else if ("report-description".equals(tagName))
    {
      return new ReportDescriptionReadHandler();
    }
    else if ("functions".equals(tagName))
    {
      return new FunctionsReadHandler(report);
    }
    else if ("include".equals(tagName))
    {
      return new IncludeReadHandler();
    }
    return null;
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return getRootHandler().getHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME);
  }
}
