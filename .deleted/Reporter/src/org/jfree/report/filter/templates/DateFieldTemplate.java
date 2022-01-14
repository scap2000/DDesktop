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
 * DateFieldTemplate.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter.templates;

import java.text.SimpleDateFormat;

import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.FormatSpecification;
import org.jfree.report.filter.RawDataSource;
import org.jfree.report.filter.SimpleDateFormatFilter;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.function.ExpressionRuntime;

/**
 * A date field template.
 *
 * @author Thomas Morgner
 */
public class DateFieldTemplate extends AbstractTemplate
        implements RawDataSource
{
  /**
   * The date format filter.
   */
  private SimpleDateFormatFilter dateFilter;

  /**
   * The data-row datasource.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * Creates a new date field template.
   */
  public DateFieldTemplate ()
  {
    dataRowDataSource = new DataRowDataSource();
    dateFilter = new SimpleDateFormatFilter();
    dateFilter.setDataSource(dataRowDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(dateFilter);
  }

  /**
   * Returns the date format string.
   *
   * @return The date format string.
   */
  public String getFormat ()
  {
    return getDateFilter().getFormatString();
  }

  /**
   * Sets the date format string.
   *
   * @param format the format string.
   */
  public void setFormat (final String format)
  {
    getDateFilter().setFormatString(format);
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return getDataRowDataSource().getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field the field name.
   */
  public void setField (final String field)
  {
    getDataRowDataSource().setDataSourceColumnName(field);
  }

  /**
   * Returns the formula used to compute the value of the data source.
   *
   * @return the formula.
   */
  public String getFormula()
  {
    return dataRowDataSource.getFormula();
  }

  /**
   * Defines the formula used to compute the value of this data source.
   *
   * @param formula the formula for the data source.
   */
  public void setFormula(final String formula)
  {
    dataRowDataSource.setFormula(formula);
  }

  /**
   * Returns the string that represents <code>null</code> values.
   *
   * @return A string.
   */
  public String getNullValue ()
  {
    return getStringFilter().getNullValue();
  }

  /**
   * Sets the string that represents <code>null</code> values.
   *
   * @param nullValue the string that represents <code>null</code> values.
   */
  public void setNullValue (final String nullValue)
  {
    getStringFilter().setNullValue(nullValue);
  }

  /**
   * Returns the date formatter.
   *
   * @return The date formatter.
   */
  public SimpleDateFormat getDateFormat ()
  {
    return (SimpleDateFormat) getDateFilter().getFormatter();
  }

  /**
   * Sets the date formatter.
   *
   * @param dateFormat the date formatter.
   */
  public void setDateFormat (final SimpleDateFormat dateFormat)
  {
    getDateFilter().setFormatter(dateFormat);
  }

  /**
   * Returns the current value for the data source.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return the value.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    return getStringFilter().getValue(runtime);
  }

  /**
   * Clones this template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final DateFieldTemplate template = (DateFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.dateFilter = (SimpleDateFormatFilter) template.stringFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.dateFilter.getDataSource();
    return template;
  }

  /**
   * Returns the date filter.
   *
   * @return The date filter.
   */
  protected SimpleDateFormatFilter getDateFilter ()
  {
    return dateFilter;
  }

  /**
   * Returns the data-row datasource.
   *
   * @return The data-row datasource.
   */
  protected DataRowDataSource getDataRowDataSource ()
  {
    return dataRowDataSource;
  }

  /**
   * Returns the string filter.
   *
   * @return The string filter.
   */
  protected StringFilter getStringFilter ()
  {
    return stringFilter;
  }

  public Object getRawValue (final ExpressionRuntime runtime)
  {
    return dateFilter.getRawValue(runtime);
  }

  public FormatSpecification getFormatString(final ExpressionRuntime runtime, final FormatSpecification formatSpecification)
  {
    return dateFilter.getFormatString(runtime, formatSpecification);
  }
}
