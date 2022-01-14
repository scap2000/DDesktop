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
 * ExtSubReportReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.ArrayList;

import org.jfree.report.SubReport;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.SubReportReadHandler;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.FunctionsReadHandler;
import org.jfree.report.modules.parser.base.common.IncludeReadHandler;
import org.jfree.report.modules.parser.base.common.ParameterMappingReadHandler;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.xmlns.parser.RootXmlReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.SAXException;

/**
 * Creation-Date: Dec 18, 2006, 1:20:26 PM
 *
 * @author Thomas Morgner
 */
public class ExtSubReportReadHandler extends AbstractPropertyXmlReadHandler
    implements SubReportReadHandler
{
  public static final String ELEMENT_FACTORY_KEY = "::element-factory";
  public static final String STYLE_FACTORY_KEY = "::stylekey-factory";
  public static final String CLASS_FACTORY_KEY = "::class-factory";
  public static final String DATASOURCE_FACTORY_KEY = "::datasource-factory";
  public static final String TEMPLATE_FACTORY_KEY = "::template-factory";

  private ArrayList importParameters;
  private ArrayList exportParameters;
  private boolean disableRootTagWarning;

  public ExtSubReportReadHandler()
  {
    importParameters = new ArrayList();
    exportParameters = new ArrayList();
  }

  public void setDisableRootTagWarning(final boolean disableWarning)
  {
    disableRootTagWarning = disableWarning;
  }


  public boolean isDisableRootTagWarning()
  {
    return disableRootTagWarning;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing(final PropertyAttributes attrs)
      throws SAXException
  {
    final RootXmlReadHandler rootHandler = getRootHandler();
    final Object maybeReport = rootHandler.getHelperObject
        (ReportParserUtil.HELPER_OBJ_REPORT_NAME);
    final SubReport report;
    if (maybeReport instanceof SubReport == false)
    {
      // replace it ..
      report = new SubReport();
    }
    else
    {
      report = (SubReport) maybeReport;
    }

    if (ReportParserUtil.isIncluded(rootHandler) == false)
    {
      final String query = attrs.getValue(getUri(), "query");
      if (query != null)
      {
        report.setQuery(query);
      }
    }

    if (rootHandler.getHelperObject(ELEMENT_FACTORY_KEY) == null)
    {
      final ElementFactoryCollector elementFactory = new ElementFactoryCollector();
      rootHandler.setHelperObject(ELEMENT_FACTORY_KEY, elementFactory);
    }
    if (rootHandler.getHelperObject(STYLE_FACTORY_KEY) == null)
    {
      final StyleKeyFactoryCollector styleKeyFactory = new StyleKeyFactoryCollector();
      rootHandler.setHelperObject(STYLE_FACTORY_KEY, styleKeyFactory);
    }
    if (rootHandler.getHelperObject(CLASS_FACTORY_KEY) == null)
    {
      final ClassFactoryCollector classFactory = new ClassFactoryCollector();
      classFactory.configure(rootHandler.getParserConfiguration());
      rootHandler.setHelperObject(CLASS_FACTORY_KEY, classFactory);
    }
    if (rootHandler.getHelperObject(DATASOURCE_FACTORY_KEY) == null)
    {
      final DataSourceCollector dataSourceFactory = new DataSourceCollector();
      dataSourceFactory.configure(rootHandler.getParserConfiguration());
      rootHandler.setHelperObject(DATASOURCE_FACTORY_KEY, dataSourceFactory);
    }
    if (rootHandler.getHelperObject(TEMPLATE_FACTORY_KEY) == null)
    {
      final TemplateCollector templateFactory = new TemplateCollector();
      templateFactory.configure(rootHandler.getParserConfiguration());
      rootHandler.setHelperObject(TEMPLATE_FACTORY_KEY, templateFactory);
    }

    rootHandler.setHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME, report);
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
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
      return new FunctionsReadHandler(getSubReport());
    }
    else if ("include".equals(tagName))
    {
      return new IncludeReadHandler();
    }
    else if ("import-parameter".equals(tagName))
    {
      final ParameterMappingReadHandler handler = new ParameterMappingReadHandler();
      importParameters.add(handler);
      return handler;
    }
    else if ("export-parameter".equals(tagName))
    {
      final ParameterMappingReadHandler handler = new ParameterMappingReadHandler();
      exportParameters.add(handler);
      return handler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    super.doneParsing();
    final SubReport report = getSubReport();
    for (int i = 0; i < importParameters.size(); i++)
    {
      final ParameterMappingReadHandler handler =
          (ParameterMappingReadHandler) importParameters.get(i);
      report.addInputParameter(handler.getName(), handler.getAlias());
    }
    for (int i = 0; i < exportParameters.size(); i++)
    {
      final ParameterMappingReadHandler handler =
          (ParameterMappingReadHandler) exportParameters.get(i);
      report.addExportParameter(handler.getAlias(), handler.getName());
    }
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   */
  public Object getObject()
  {
    return getRootHandler().getHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME);
  }

  public SubReport getSubReport()
  {
    return (SubReport) getObject();
  }
}
