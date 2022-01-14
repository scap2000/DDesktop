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
 * PropertyReferenceReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.base.common;

import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.util.beans.BeanException;
import org.jfree.report.util.beans.ConverterRegistry;
import org.jfree.report.util.beans.StringValueConverter;
import org.jfree.report.util.beans.ValueConverter;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.parser.ParseException;

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
 * PropertyReferenceReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class PropertyReferenceReadHandler extends PropertyStringReadHandler
{
  public static final String CLASS_ATT = "class";
  public static final String NAME_ATT = "name";

  private String propertyName;
  private Object value;
  private ValueConverter valueType;

  public PropertyReferenceReadHandler ()
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
    super.startParsing(attrs);
    propertyName = attrs.getValue(getUri(), NAME_ATT);
    if (propertyName == null)
    {
      throw new SAXException("Required attribute 'name' is null.");
    }

    final String className = attrs.getValue(getUri(), CLASS_ATT);
    if (className == null)
    {
      valueType = new StringValueConverter();
    }
    else
    {
      try
      {
        final Class c = ObjectUtilities.getClassLoader(getClass()).loadClass(className);
        valueType = ConverterRegistry.getInstance().getValueConverter(c);
        if (valueType == null)
        {
          Log.warn ("Unable to find a suitable value-converter for " + c);
          valueType = new StringValueConverter();
        }
      }
      catch (Exception e)
      {
        throw new SAXException("Attribute 'class' is invalid.");
      }
    }
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing ()
          throws SAXException
  {
    super.doneParsing();
    try
    {
      value = valueType.toPropertyValue(getResult());
    }
    catch (BeanException e)
    {
      throw new ParseException("Failed to parse property value.", e);
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
    return value;
  }

  public String getPropertyName ()
  {
    return propertyName;
  }
}
