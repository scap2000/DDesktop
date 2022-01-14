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
 * BandReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.ArrayList;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.function.Expression;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.common.StyleExpressionHandler;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.style.StyleKey;
import org.jfree.util.Log;
import org.jfree.xmlns.parser.ParseException;
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
 * BandReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class BandReadHandler extends ElementReadHandler
{
  private ArrayList elementHandlers;
  private ArrayList styleExpressionHandlers;

  public BandReadHandler(final Band element)
  {
    super(element);
    elementHandlers = new ArrayList();
    this.styleExpressionHandlers = new ArrayList();
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

    if ("style".equals(tagName))
    {
      return new StyleReadHandler(getElement().getStyle());
    }
    else if ("default-style".equals(tagName))
    {
      Log.warn("Tag <default-style> is deprecated. All definitions " +
          "have been mapped into the bands primary style sheet.");
      return new StyleReadHandler(getElement().getStyle());
    }
    if ("style-expression".equals(tagName))
    {
      final StyleExpressionHandler styleExpressionHandler = new StyleExpressionHandler();
      styleExpressionHandlers.add(styleExpressionHandler);
      return styleExpressionHandler;
    }
    else if ("element".equals(tagName))
    {
      final String type = atts.getValue(getUri(), "type");
      if (type == null)
      {
        throw new ParseException("The element's 'type' attribute is missing",
            getRootHandler().getDocumentLocator());
      }

      final ElementFactoryCollector fc = (ElementFactoryCollector)
          getRootHandler().getHelperObject
              (ReportDefinitionReadHandler.ELEMENT_FACTORY_KEY);
      final Element element = fc.getElementForType(type);
      if (element == null)
      {
        throw new ParseException("There is no factory for elements of type '" +
            type + '\'', getLocator());
      }

      final XmlReadHandler readHandler = new ElementReadHandler(element);
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if ("band".equals(tagName))
    {
      final XmlReadHandler readHandler = new BandReadHandler(new Band());
      elementHandlers.add(readHandler);
      return readHandler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing()
      throws SAXException
  {
    super.doneParsing();
    final Band band = (Band) getElement();
    for (int i = 0; i < elementHandlers.size(); i++)
    {
      final ElementReadHandler readHandler = (ElementReadHandler) elementHandlers.get(i);
      band.addElement(readHandler.getElement());
    }
    for (int i = 0; i < styleExpressionHandlers.size(); i++)
    {
      final StyleExpressionHandler handler =
          (StyleExpressionHandler) styleExpressionHandlers.get(i);
      final StyleKey key = handler.getKey();
      final Expression expression = handler.getExpression();
      band.setStyleExpression(key, expression);
    }
  }
}
