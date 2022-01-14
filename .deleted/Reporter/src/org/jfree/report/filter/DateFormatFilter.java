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
 * DateFormatFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.filter;

import java.text.DateFormat;
import java.text.Format;

import java.util.Date;

import org.jfree.report.function.ExpressionRuntime;

/**
 * A filter that creates string from dates. This filter will format java.util. Date
 * objects using a java.text.DateFormat to create the string representation for the date
 * obtained from the datasource.
 * <p/>
 * If the object read from the datasource is no date, the NullValue defined by
 * setNullValue(Object) is returned.
 *
 * @author Thomas Morgner
 * @see java.text.DateFormat
 */
public class DateFormatFilter extends FormatFilter
{
  /**
   * Default constructor.  Creates a new filter using the default date format for the
   * current locale.
   */
  public DateFormatFilter ()
  {
    setFormatter(DateFormat.getInstance());
  }

  /**
   * Returns the date format object.
   *
   * @return The date format object.
   */
  public DateFormat getDateFormat ()
  {
    return (DateFormat) getFormatter();
  }

  /**
   * Sets the date format for the filter.
   *
   * @param format The format.
   * @throws NullPointerException if the format given is null
   */
  public void setDateFormat (final DateFormat format)
  {
    super.setFormatter(format);
  }

  /**
   * Sets the formatter.
   *
   * @param format The format.
   * @throws ClassCastException   if the format given is no DateFormat
   * @throws NullPointerException if the format given is null
   */
  public void setFormatter (final Format format)
  {
    final DateFormat dfmt = (DateFormat) format;
    super.setFormatter(dfmt);
  }

  public Object getRawValue(final ExpressionRuntime runtime)
  {
    final Object value = super.getRawValue(runtime);
    if (value instanceof Number)
    {
      // Automagically fix numbers into dates
      final Number number = (Number) value;
      return new Date(number.longValue());
    }
    return value;
  }


  public FormatSpecification getFormatString(final ExpressionRuntime runtime, FormatSpecification formatSpecification)
  {
    final DataSource source = getDataSource();
    if (source instanceof RawDataSource)
    {
      final RawDataSource rds = (RawDataSource) source;
      return rds.getFormatString(runtime, formatSpecification);
    }
    if (formatSpecification == null)
    {
      formatSpecification = new FormatSpecification();
    }
    formatSpecification.redefine(FormatSpecification.TYPE_UNDEFINED, null);
    return formatSpecification;
  }
  
}
