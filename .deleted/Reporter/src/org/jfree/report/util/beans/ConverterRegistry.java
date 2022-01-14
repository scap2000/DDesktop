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
 * ConverterRegistry.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.util.beans;

import java.awt.Color;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.jfree.report.ElementAlignment;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.BoxSizing;
import org.jfree.report.style.FontSmooth;
import org.jfree.report.style.VerticalTextAlign;
import org.jfree.report.style.WhitespaceCollapse;

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
 * ConverterRegistry.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public final class ConverterRegistry
{
  private static ConverterRegistry instance;
  private HashMap registeredClasses;

  public static synchronized ConverterRegistry getInstance ()
  {
    if (instance == null)
    {
      instance = new ConverterRegistry();
    }
    return instance;
  }

  private ConverterRegistry ()
  {
    registeredClasses = new HashMap();
    registeredClasses.put(BigDecimal.class, new BigDecimalValueConverter());
    registeredClasses.put(BigInteger.class, new BigIntegerValueConverter());
    registeredClasses.put(Boolean.class, new BooleanValueConverter());
    registeredClasses.put(Boolean.TYPE, new BooleanValueConverter());
    registeredClasses.put(Byte.class, new ByteValueConverter());
    registeredClasses.put(Byte.TYPE, new ByteValueConverter());
    registeredClasses.put(Character.class, new CharacterValueConverter());
    registeredClasses.put(Character.TYPE, new CharacterValueConverter());
    registeredClasses.put(Color.class, new ColorValueConverter());
    registeredClasses.put(Double.class, new DoubleValueConverter());
    registeredClasses.put(Double.TYPE, new DoubleValueConverter());
    registeredClasses.put(Float.class, new FloatValueConverter());
    registeredClasses.put(Float.TYPE, new FloatValueConverter());
    registeredClasses.put(Integer.class, new IntegerValueConverter());
    registeredClasses.put(Integer.TYPE, new IntegerValueConverter());
    registeredClasses.put(Long.class, new LongValueConverter());
    registeredClasses.put(Long.TYPE, new LongValueConverter());
    registeredClasses.put(Short.class, new ShortValueConverter());
    registeredClasses.put(Short.TYPE, new ShortValueConverter());
    registeredClasses.put(String.class, new StringValueConverter());
    registeredClasses.put(Number.class, new BigDecimalValueConverter());
    registeredClasses.put(Class.class, new ClassValueConverter());
    registeredClasses.put(Locale.class, new LocaleValueConverter());
    registeredClasses.put(TimeZone.class, new TimeZoneValueConverter());
    registeredClasses.put(Date.class, new DateValueConverter());

    registeredClasses.put(ElementAlignment.class, new ElementAlignmentValueConverter());
    registeredClasses.put(WhitespaceCollapse.class, new WhitespaceCollapseValueConverter());
    registeredClasses.put(VerticalTextAlign.class, new VerticalTextAlignValueConverter());
    registeredClasses.put(BoxSizing.class, new BoxSizingValueConverter());
    registeredClasses.put(BorderStyle.class, new BorderStyleValueConverter());
    registeredClasses.put(FontSmooth.class, new FontSmoothValueConverter());
  }

  public ValueConverter getValueConverter (final Class c)
  {
    final ValueConverter plain = (ValueConverter) registeredClasses.get(c);
    if (plain != null)
    {
      return plain;
    }
    if (c.isArray())
    {
      final Class componentType = c.getComponentType();
      final ValueConverter componentConverter = getValueConverter(componentType);
      if (componentConverter != null)
      {
        return new ArrayValueConverter(componentType, componentConverter);
      }
    }
    return null;
  }

  /**
   * Converts an object to an attribute value.
   *
   * @param o the object.
   * @return the attribute value.
   * @throws BeanException if there was an error during the conversion.
   */
  public static String toAttributeValue (final Object o) throws BeanException
  {
    if (o == null)
    {
      return null;
    }
    final ValueConverter vc =
            ConverterRegistry.getInstance().getValueConverter(o.getClass());
    if (vc == null)
    {
      return null;
    }
    return vc.toAttributeValue(o);
  }

  /**
   * Converts a string to a property value.
   *
   * @param s the string.
   * @return a property value.
   * @throws BeanException if there was an error during the conversion.
   */
  public static Object toPropertyValue (final String s, final Class c) throws BeanException
  {
    if (s == null)
    {
      return null;
    }
    final ValueConverter vc =
            ConverterRegistry.getInstance().getValueConverter(c);
    if (vc == null)
    {
      return null;
    }
    return vc.toPropertyValue(s);
  }

}
