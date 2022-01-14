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
 * FunctionsReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.base.common;

import java.util.ArrayList;

import org.jfree.report.AbstractReportDefinition;
import org.jfree.report.function.Expression;
import org.jfree.report.modules.parser.base.PropertyAttributes;
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
 * FunctionsReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class FunctionsReadHandler extends AbstractPropertyXmlReadHandler
{
  private AbstractReportDefinition report;
  private ArrayList expressionHandlers;
  private ArrayList propertyRefs;

  public FunctionsReadHandler(final AbstractReportDefinition report)
  {
    this.report = report;
    this.expressionHandlers = new ArrayList();
    this.propertyRefs = new ArrayList();
  }


  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param attrs    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final PropertyAttributes attrs)
      throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if ("expression".equals(tagName) || "function".equals(tagName))
    {
      final ExpressionReadHandler readHandler = new ExpressionReadHandler();
      expressionHandlers.add(readHandler);
      return readHandler;

    }
    else if ("property-ref".equals(tagName))
    {
      final PropertyReferenceReadHandler readHandler = new PropertyReferenceReadHandler();
      propertyRefs.add(readHandler);
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
    for (int i = 0; i < expressionHandlers.size(); i++)
    {
      final ExpressionReadHandler readHandler =
          (ExpressionReadHandler) expressionHandlers.get(i);
      report.addExpression((Expression) readHandler.getObject());
    }

    for (int i = 0; i < propertyRefs.size(); i++)
    {
      final PropertyReferenceReadHandler readHandler =
          (PropertyReferenceReadHandler) propertyRefs.get(i);
      final Object object = readHandler.getObject();
      if (object != null)
      {
        if (object instanceof String)
        {
          final String text = (String) object;
          if (text.length() == 0)
          {
            continue;
          }
        }
        report.setProperty(readHandler.getPropertyName(), object);
      }
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
    return null;
  }
}
