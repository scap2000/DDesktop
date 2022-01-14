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
 * LocaleValueConverter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.util.beans;

import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Creation-Date: 24.01.2006, 19:19:03
 *
 * @author Thomas Morgner
 */
public class LocaleValueConverter implements ValueConverter
{
  public LocaleValueConverter()
  {
  }

  /**
   * Converts an object to an attribute value.
   *
   * @param o the object.
   * @return the attribute value.
   * @throws BeanException if there was an error during the conversion.
   */
  public String toAttributeValue(final Object o) throws BeanException
  {
    final Locale l = (Locale) o;
    if ("".equals(l.getCountry()))
    {
      return l.getLanguage();
    }
    else if ("".equals(l.getVariant()))
    {
      return l.getLanguage() + '_' + l.getCountry();
    }
    else
    {
      return l.getLanguage() + '_' + l.getCountry() + '_' + l.getVariant();
    }
  }

  /**
   * Converts a string to a property value.
   *
   * @param s the string.
   * @return a property value.
   * @throws BeanException if there was an error during the conversion.
   */
  public Object toPropertyValue(final String s) throws BeanException
  {
    final StringTokenizer strtok = new StringTokenizer(s.trim(), "_");
    if (strtok.hasMoreElements() == false)
    {
      throw new BeanException("This is no valid locale specification.");
    }
    final String language = strtok.nextToken();
    String country = "";
    if (strtok.hasMoreTokens())
    {
      country = strtok.nextToken();
    }
    String variant = "";
    if (strtok.hasMoreTokens())
    {
      variant = strtok.nextToken();
    }
    return new Locale(language, country, variant);
  }
}
