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
 * DateFormatParser.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.filter;

import java.text.DateFormat;
import java.text.Format;

import java.util.Date;

/**
 * Parses a String into a java.util.Date. The string is read from the given datasource and
 * then parsed by the dateformat contained in this FormatParser.
 *
 * @author Thomas Morgner
 */
public class DateFormatParser extends FormatParser
{
  /**
   * Creates a new 'date format parser'.
   */
  public DateFormatParser ()
  {
    setDateFormat(DateFormat.getInstance());
  }

  /**
   * Returns the format for this filter. The format object is returned as DateFormat.
   *
   * @return the formatter.
   *
   * @throws NullPointerException if the given format is null
   */
  public DateFormat getDateFormat ()
  {
    return (DateFormat) getFormatter();
  }

  /**
   * Sets the format for the filter.
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   */
  public void setDateFormat (final DateFormat format)
  {
    super.setFormatter(format);
  }

  /**
   * Sets the format for the filter. The formater is required to be of type DateFormat.
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   * @throws ClassCastException   if an invalid formater is set.
   */
  public void setFormatter (final Format format)
  {
    final DateFormat dfmt = (DateFormat) format;
    super.setFormatter(dfmt);
  }

  /**
   * Sets the value that will be displayed if the data source supplies a null value. The
   * nullValue itself can be null to cover the case when no reasonable default value can
   * be defined.
   * <p/>
   * The null value for date format parsers is required to be either null or a
   * java.util.Date.
   *
   * @param nullvalue the nullvalue returned when parsing failed.
   * @throws ClassCastException if the value is no date or not null.
   */
  public void setNullValue (final Object nullvalue)
  {
    final Date dt = (Date) nullvalue;
    super.setNullValue(dt);
  }

  /**
   * Checks whether the given value is already a valid result. IF the datasource already
   * returned a valid value, and no parsing is required, a parser can skip the parsing
   * process by returning true in this function.
   *
   * @param o the value.
   * @return true, if the given value is already an instance of date.
   */
  protected boolean isValidOutput (final Object o)
  {
    return o instanceof Date;
  }
}
