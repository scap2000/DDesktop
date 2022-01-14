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
 * IncludeReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.base.common;

import java.util.HashMap;

import org.jfree.report.JFreeReport;
import org.jfree.report.SubReport;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.resourceloader.DependencyCollector;
import org.jfree.resourceloader.FactoryParameterKey;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.RootXmlReadHandler;

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
 * IncludeReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class IncludeReadHandler extends AbstractPropertyXmlReadHandler
{
  public IncludeReadHandler()
  {
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
    final String file = attrs.getValue(getUri(), "src");
    if (file == null)
    {
      throw new ParseException("Required attribute 'src' is missing.", getRootHandler().getDocumentLocator());
    }

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
        final FactoryParameterKey key = new FactoryParameterKey(name);
        map.put(key, rootHandler.getHelperObject(name));
      }
      map.put(ReportParserUtil.INCLUDE_PARSING_KEY, ReportParserUtil.INCLUDE_PARSING_VALUE);

      final ResourceKey target = resourceManager.deriveKey(source, file, map);
      final DependencyCollector dc = rootHandler.getDependencyCollector();

      final Object maybeReport = getRootHandler().getHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME);
      if (maybeReport == null)
      {
        throw new ParseException("Illegal State: No valid report", getRootHandler().getDocumentLocator());
      }

      final Class targetType;
      if (maybeReport instanceof SubReport)
      {
        targetType = SubReport.class;
      }
      else if (maybeReport instanceof JFreeReport)
      {
        targetType = JFreeReport.class;
      }
      else
      {
        throw new ParseException("Illegal State: No valid report", getRootHandler().getDocumentLocator());
      }

      final Resource resource = resourceManager.create (target, rootHandler.getContext(), targetType);
      dc.add(resource);

    }
    catch (ResourceKeyCreationException e)
    {
      throw new ParseException("Failure while building the resource-key.", e, getLocator());
    }
    catch (ResourceLoadingException e)
    {
      throw new ParseException("Failure while loading data.", e, getLocator());
    }
    catch (ResourceCreationException e)
    {
      throw new ParseException("Failure while loading data.", e, getLocator());
    }
  }

  /**
   * Returns the object for this element (if any).
   *
   * @return the object.
   */
  public Object getObject()
  {
    return null;
  }
}
