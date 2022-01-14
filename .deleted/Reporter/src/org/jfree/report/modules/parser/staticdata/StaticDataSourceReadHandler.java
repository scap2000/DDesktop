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
 * StaticDataSourceReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.staticdata;

import java.util.ArrayList;

import org.jfree.report.DataFactory;
import org.jfree.report.modules.misc.datafactory.NamedStaticDataFactory;
import org.jfree.report.modules.parser.base.DataFactoryReadHandler;
import org.jfree.xmlns.parser.AbstractXmlReadHandler;
import org.jfree.xmlns.parser.PropertyReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 07.04.2006, 17:47:53
 *
 * @author Thomas Morgner
 */
public class StaticDataSourceReadHandler extends AbstractXmlReadHandler
  implements DataFactoryReadHandler
{
  private ArrayList queries;
  private DataFactory dataFactory;

  public StaticDataSourceReadHandler()
  {
    queries = new ArrayList();
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws org.xml.sax.SAXException       if there is a parsing error.
   * @throws SAXException if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
          throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if ("query".equals(tagName))
    {
      final XmlReadHandler queryReadHandler = new PropertyReadHandler();
      queries.add(queryReadHandler);
      return queryReadHandler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException       if there is a parsing error.
   * @throws SAXException if there is a reader error.
   */
  protected void doneParsing() throws SAXException
  {
    final NamedStaticDataFactory srdf = new NamedStaticDataFactory();
    for (int i = 0; i < queries.size(); i++)
    {
      final PropertyReadHandler handler = (PropertyReadHandler) queries.get(i);
      srdf.setQuery(handler.getName(), handler.getResult());
    }

    dataFactory = srdf;
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   * @throws SAXException if there is a parsing error.
   */
  public Object getObject() throws SAXException
  {
    return dataFactory;
  }

  public DataFactory getDataFactory()
  {
    return dataFactory;
  }
}
