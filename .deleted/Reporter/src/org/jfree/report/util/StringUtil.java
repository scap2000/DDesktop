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
 * StringUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util;

import org.jfree.util.StringUtils;

/**
 * String utility functions. Provides functions to parse floats, ints and boolean values.
 *
 * @author Thomas Morgner
 */
public final class StringUtil
{
  /**
   * Default Constructor.
   */
  private StringUtil ()
  {
  }

  /**
     * Helper functions to labelQuery a strings start portion. The comparison is case
     * insensitive.
     *
     * @param base the base string.
     * @param start the starting text.
     * @return true, if the string starts with the given starting text.
     * @deprecated use JCommon's StringUtils instead.
     */
  public static boolean startsWithIgnoreCase (final String base, final String start)
  {
    return StringUtils.startsWithIgnoreCase(base, start);
  }

  /**
     * Helper functions to labelQuery a strings end portion. The comparison is case insensitive.
     *
     * @param base the base string.
     * @param end the ending text.
     * @return true, if the string ends with the given ending text.
     * @deprecated use JCommon's StringUtils instead.
     */
  public static boolean endsWithIgnoreCase (final String base, final String end)
  {
    return StringUtils.endsWithIgnoreCase(base, end);
  }

  /**
   * Parses the given string and returns the parsed integer value or the given default if
   * the parsing failed.
   *
   * @param value        the to be parsed string
   * @param defaultValue the default value
   * @return the parsed string.
   */
  public static int parseInt (final String value, final int defaultValue)
  {
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return Integer.parseInt(value);
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }


  /**
   * Parses the given string and returns the parsed integer value or the given default if
   * the parsing failed.
   *
   * @param value        the to be parsed string
   * @param defaultValue the default value
   * @return the parsed string.
   */
  public static float parseFloat (final String value, final float defaultValue)
  {
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return Float.parseFloat(value);
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }
  /**
   * Parses the given string into a boolean value. This returns true, if the string's
   * value is "true".
   *
   * @param attribute    the string that should be parsed.
   * @param defaultValue the default value, in case the string is null.
   * @return the parsed value.
   */
  public static boolean parseBoolean (final String attribute, final boolean defaultValue)
  {
    if (attribute == null)
    {
      return defaultValue;
    }
    if ("true".equals(attribute))
    {
      return true;
    }
    return false;
  }


}
