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
 * ReportConfigReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.AbstractReportDefinition;
import org.jfree.report.DataFactory;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.DataFactoryReadHandler;
import org.jfree.report.modules.parser.base.DataFactoryReadHandlerFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.ConfigurationReadHandler;
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
 * ReportConfigReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class ReportConfigReadHandler extends AbstractPropertyXmlReadHandler
{
  private DataFactoryReadHandler dataFactoryReadHandler;

  public ReportConfigReadHandler ()
  {
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
    final DataFactoryReadHandlerFactory factory = DataFactoryReadHandlerFactory.getInstance();
    final DataFactoryReadHandler handler = (DataFactoryReadHandler) factory.getHandler(uri, tagName);
    if (handler != null)
    {
      dataFactoryReadHandler = handler;
      return handler;
    }

    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    final AbstractReportDefinition report = (AbstractReportDefinition)
            getRootHandler().getHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME);
    if (report instanceof JFreeReport)
    {
      final JFreeReport masterReport = (JFreeReport) report;
      if ("page-definition".equals(tagName))
      {
        return new PageDefinitionReadHandler();
      }
      else if ("simple-page-definition".equals(tagName))
      {
        return new SimplePageDefinitionReadHandler();
      }
      else if ("configuration".equals(tagName))
      {
        return new ConfigurationReadHandler(masterReport.getReportConfiguration());
      }
    }

    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    if (dataFactoryReadHandler == null)
    {
      return;
    }

    final Object maybeReport =
            getRootHandler().getHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME);
    if (maybeReport instanceof JFreeReport == false)
    {
      return;
    }

    final JFreeReport report = (JFreeReport) maybeReport;
    final DataFactory dataFactory = dataFactoryReadHandler.getDataFactory();
    if (dataFactory != null)
    {
      report.setDataFactory(dataFactory);
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return null;
  }
}
