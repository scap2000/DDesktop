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
 * ElementReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.ArrayList;

import org.jfree.report.Element;
import org.jfree.report.filter.DataSource;
import org.jfree.report.function.Expression;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.StyleExpressionHandler;
import org.jfree.report.style.StyleKey;
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
 * ElementReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class ElementReadHandler extends AbstractPropertyXmlReadHandler
{
  private ArrayList styleExpressionHandlers;
  private XmlReadHandler dataSourceHandler;
  private Element element;

  public ElementReadHandler (final Element element)
  {
    if (element == null)
    {
      throw new NullPointerException("Element given must not be null.");
    }
    this.element = element;
    this.styleExpressionHandlers = new ArrayList();
  }

  protected Element getElement ()
  {
    return element;
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
    final String name = attrs.getValue(getUri(), "name");
    if (name != null)
    {
      element.setName(name);
    }

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

    if ("style".equals(tagName))
    {
      return new StyleReadHandler(element.getStyle());
    }
    if ("style-expression".equals(tagName))
    {
      final StyleExpressionHandler styleExpressionHandler = new StyleExpressionHandler();
      styleExpressionHandlers.add(styleExpressionHandler);
      return styleExpressionHandler;
    }
    else if ("datasource".equals(tagName))
    {
      dataSourceHandler = new DataSourceReadHandler();
      return dataSourceHandler;
    }
    else if ("template".equals(tagName))
    {
      dataSourceHandler = new TemplateReadHandler(false);
      return dataSourceHandler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing ()
          throws SAXException
  {
    if (dataSourceHandler != null)
    {
      element.setDataSource((DataSource) dataSourceHandler.getObject());
    }

    for (int i = 0; i < styleExpressionHandlers.size(); i++)
    {
      final StyleExpressionHandler handler =
          (StyleExpressionHandler) styleExpressionHandlers.get(i);
      final StyleKey key = handler.getKey();
      final Expression expression = handler.getExpression();
      element.setStyleExpression(key, expression);
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
    return element;
  }
}
