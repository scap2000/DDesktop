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
 * FormatFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter;

import java.text.Format;

import org.jfree.report.function.ExpressionRuntime;
import org.jfree.util.ObjectUtilities;

/**
 * The base class for filters that format data.  Data is received from a DataSource and
 * formatted. The data source might be a field in the TableModel or a report function, or
 * even another format filter (since filters implement the DataSource interface).
 * <p/>
 * Formating is done by a java.text.Format object. This filter will always return a String
 * object on getValue().
 * <p/>
 * If the formater does not understand the object returned by the defined datasource, the
 * defined null value is returned.
 * <p/>
 * The nullValue is set to "-" by default.
 *
 * @author Thomas Morgner
 */
public class FormatFilter implements DataFilter, RawDataSource
{
  /**
   * The format used to create the string representation of the data.
   */
  private Format format;

  /**
   * The datasource from where the data is obtained.
   */
  private DataSource datasource;

  /**
   * The string used to represent null.
   */
  private String nullvalue;

  private transient String cachedResult;
  private transient Object cachedValue;
  private transient Format cachedFormat;

  /**
   * Default constructor.
   */
  public FormatFilter ()
  {
    nullvalue = null;
  }

  protected void invalidateCache()
  {
    cachedFormat = null;
    cachedValue = null;
    cachedResult = null;
  }

  /**
   * Sets the format for the filter.
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   */
  public void setFormatter (final Format format)
  {
    if (format == null)
    {
      throw new NullPointerException();
    }
    this.format = format;
  }

  /**
   * Returns the format for the filter.
   *
   * @return The format.
   */
  public Format getFormatter ()
  {
    return this.format;
  }

  /**
   * Returns the formatted string. The value is read using the data source given and
   * formated using the formatter of this object. The formating is guaranteed to completly
   * form the object to an string or to return the defined NullValue.
   * <p/>
   * If format, datasource or object are null, the NullValue is returned.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return The formatted value.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    final Format f = getFormatter();
    if (f == null)
    {
      return getNullValue();
    }

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

    if (cachedResult != null &&
        (cachedFormat != f) &&
        ObjectUtilities.equal(cachedValue, o))
    {
      return cachedResult;
    }

    try
    {
      cachedResult = f.format(o);
    }
    catch (IllegalArgumentException e)
    {
      cachedResult = getNullValue();
    }

    cachedFormat = f;
    cachedValue = o;
    return cachedResult;
  }

  /**
   * Sets the value that will be displayed if the data source supplies a null value.
   *
   * @param nullvalue The string.
   */
  public void setNullValue (final String nullvalue)
  {
    if (nullvalue == null)
    {
      throw new NullPointerException();
    }
    this.nullvalue = nullvalue;
  }

  /**
   * Returns the string representing a null value from the data source.
   *
   * @return The string.
   */
  public String getNullValue ()
  {
    return nullvalue;
  }

  /**
   * Returns the data source for the filter.
   *
   * @return The data source.
   */
  public DataSource getDataSource ()
  {
    return datasource;
  }

  /**
   * Sets the data source.
   *
   * @param ds The data source.
   */
  public void setDataSource (final DataSource ds)
  {
    if (ds == null)
    {
      throw new NullPointerException();
    }
    this.datasource = ds;
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
    final FormatFilter f = (FormatFilter) super.clone();
    if (datasource != null)
    {
      f.datasource = (DataSource) datasource.clone();
    }
    if (format != null)
    {
      f.format = (Format) format.clone();
      if (cachedFormat == format)
      {
        f.cachedFormat = f.format;
      }
    }
    return f;
  }

  public Object getRawValue (final ExpressionRuntime runtime)
  {
    return datasource.getValue(runtime);
  }


  public FormatSpecification getFormatString(final ExpressionRuntime runtime, FormatSpecification formatSpecification)
  {
    if (datasource instanceof RawDataSource)
    {
      final RawDataSource rds = (RawDataSource) datasource;
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

