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
 * StringFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter;

import org.jfree.report.function.ExpressionRuntime;

/**
 * A filter that returns the value from a data source as a String. The value is converted
 * to an String using String.valueOf () which uses Object.toString() to convert the object
 * into the string.
 * <p/>
 * You can specify a default string to return when the value from the data source is
 * <code>null</code>.  Initially the string 'null' is used.
 *
 * @author Thomas Morgner
 */
public class StringFilter implements DataFilter, RawDataSource
{
  /**
   * The data source for this filter.
   */
  private DataSource source;

  /**
   * The string used to represent a null value.
   */
  private String nullvalue;

  /**
   * Default constructor.
   */
  public StringFilter ()
  {
  }

  /**
   * Sets the string used to represent a null value.
   *
   * @param nullvalue the null value.
   */
  public void setNullValue (final String nullvalue)
  {
    this.nullvalue = nullvalue;
  }

  /**
   * Returns the string used to represent a null value.
   *
   * @return the string.
   */
  public String getNullValue ()
  {
    return nullvalue;
  }

  /**
   * Returns the value obtained from the data source. <P> The filter ensures that the
   * returned value is a String, even though the return type is Object (as required by the
   * DataSource interface).
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return the string.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    final DataSource ds = getDataSource();
    if (ds == null)
    {
      return getNullValue();
    }
    final Object o = ds.getValue(runtime);
    if (o == null)
    {
      return getNullValue();
    }
    if (o instanceof String)
    {
      return o;
    }
    return String.valueOf(o);
  }

  /**
   * Returns the data source for this filter.
   *
   * @return the data source.
   */
  public DataSource getDataSource ()
  {
    return source;
  }

  /**
   * Sets the data source for this filter.
   *
   * @param ds the data source.
   */
  public void setDataSource (final DataSource ds)
  {
    if (ds == null)
    {
      throw new NullPointerException();
    }
    source = ds;
  }

  /**
   * Clones the filter.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final StringFilter f = (StringFilter) super.clone();
    if (source != null)
    {
      f.source = (DataSource) source.clone();
    }
    return f;
  }

  public Object getRawValue (final ExpressionRuntime runtime)
  {
    if (source instanceof RawDataSource)
    {
      final RawDataSource rawDataSource = (RawDataSource) source;
      return rawDataSource.getRawValue(runtime);
    }
    return source.getValue(runtime);
  }

  public FormatSpecification getFormatString(final ExpressionRuntime runtime, FormatSpecification formatSpecification)
  {
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
