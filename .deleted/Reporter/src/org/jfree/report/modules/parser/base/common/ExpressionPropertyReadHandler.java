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
 * ExpressionPropertyReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.base.common;

import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.util.beans.BeanException;
import org.jfree.report.util.beans.BeanUtility;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.writer.CharacterEntityParser;

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
 * ExpressionPropertyReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class ExpressionPropertyReadHandler extends PropertyStringReadHandler
{
  public static final String NAME_ATT = "name";
  public static final String CLASS_ATT = "class";

  private BeanUtility beanUtility;
  private CharacterEntityParser entityParser;
  private String propertyName;
  private String propertyType;
  private String expressionName;

  public ExpressionPropertyReadHandler (final BeanUtility expression,
                                        final String expressionName,
                                        final CharacterEntityParser entityParser)
  {
    super();
    this.expressionName = expressionName;
    this.beanUtility = expression;
    this.entityParser = entityParser;
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
    super.startParsing(attrs);
    propertyType = attrs.getValue(getUri(), CLASS_ATT);
    propertyName = attrs.getValue(getUri(), NAME_ATT);
    if (propertyName == null)
    {
      throw new SAXException("Required attribute 'name' is null.");
    }
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException
  {
    super.doneParsing();
    final String result = getResult();
    if (beanUtility == null)
    {
      throw new SAXException("No current beanUtility");
    }
    try
    {
      if (propertyType != null)
      {
        final ClassLoader cl = ObjectUtilities.getClassLoader
                (ExpressionPropertyReadHandler.class);
        final Class c = cl.loadClass(propertyType);
        beanUtility.setPropertyAsString
              (propertyName, c, entityParser.decodeEntities(result));
      }
      else
      {
        beanUtility.setPropertyAsString
              (propertyName, entityParser.decodeEntities(result));
      }
    }
    catch (BeanException e)
    {
      throw new SAXException("Unable to assign property '" + propertyName
              + "' to expression '" + expressionName + '\'', e);
    }
    catch (ClassNotFoundException e)
    {
      throw new SAXException("Unable to assign property '" + propertyName
              + "' to expression '" + expressionName + '\'', e);
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
