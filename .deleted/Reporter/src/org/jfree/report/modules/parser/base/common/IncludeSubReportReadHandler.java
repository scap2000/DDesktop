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
 * IncludeSubReportReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.base.common;

import java.util.HashMap;

import org.jfree.report.SubReport;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.SubReportReadHandler;
import org.jfree.report.modules.parser.base.SubReportReadHandlerFactory;
import org.jfree.resourceloader.DependencyCollector;
import org.jfree.resourceloader.FactoryParameterKey;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.RootXmlReadHandler;

import org.xml.sax.SAXException;

/**
 * Creation-Date: Dec 18, 2006, 2:34:05 PM
 *
 * @author Thomas Morgner
 */
public class IncludeSubReportReadHandler extends AbstractPropertyXmlReadHandler
{
  private SubReport report;
  private Object oldReport;
  private SubReportReadHandler subReportReadHandler;

  public IncludeSubReportReadHandler()
  {
  }


  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing(final PropertyAttributes attrs) throws SAXException
  {
    final String file = attrs.getValue(getUri(), "href");
    if (file != null)
    {
      report = parseReport(file);
    }
    else
    {
      report = new SubReport();
    }

    final String query = attrs.getValue(getUri(), "query");
    if (query != null)
    {
      report.setQuery(query);
    }


    final SubReportReadHandlerFactory factory = SubReportReadHandlerFactory.getInstance();
    final SubReportReadHandler handler = (SubReportReadHandler) factory.getHandler(getUri(), getTagName());
    if (handler != null)
    {
      handler.setDisableRootTagWarning (true);
      oldReport = getRootHandler().getHelperObject
          (ReportParserUtil.HELPER_OBJ_REPORT_NAME);

      getRootHandler().setHelperObject
          (ReportParserUtil.HELPER_OBJ_REPORT_NAME, report);

      getRootHandler().delegate(handler, getUri(), getTagName(), attrs);
      subReportReadHandler = handler;
    }
  }

  private SubReport parseReport (final String file) throws ParseException
  {
    try
    {
      final RootXmlReadHandler rootHandler = getRootHandler();
      final ResourceManager resourceManager = rootHandler.getResourceManager();
      final ResourceKey source = rootHandler.getSource();

      final HashMap map = new HashMap();
      final String[] names = rootHandler.getHelperObjectNames();
      for (int i = 0; i < names.length; i++)
      {
        final String name = names[i];
        if (ReportParserUtil.HELPER_OBJ_REPORT_NAME.equals(name))
        {
          continue;
        }

        final FactoryParameterKey key = new FactoryParameterKey(name);
        map.put(key, rootHandler.getHelperObject(name));
      }
      map.put (ReportParserUtil.INCLUDE_PARSING_KEY,
          ReportParserUtil.INCLUDE_PARSING_VALUE);

      final ResourceKey target = resourceManager.deriveKey(source, file, map);
      final DependencyCollector dc = rootHandler.getDependencyCollector();

      final Resource resource = resourceManager.create
          (target, rootHandler.getContext(), SubReport.class);
      dc.add(resource);
      return (SubReport) resource.getResource();
    }
    catch (ResourceException e)
    {
      throw new ParseException("Failure while loading data.", e);
    }
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    if (subReportReadHandler != null)
    {
      report = subReportReadHandler.getSubReport();
    }

    if (oldReport != null)
    {
      getRootHandler().setHelperObject
          (ReportParserUtil.HELPER_OBJ_REPORT_NAME, oldReport);
      oldReport = null;
    }
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   */
  public Object getObject() throws SAXException
  {
    return report;
  }
}
