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
 * NumberFieldTemplate.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter.templates;

import java.text.DecimalFormat;

import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DecimalFormatFilter;
import org.jfree.report.filter.FormatSpecification;
import org.jfree.report.filter.RawDataSource;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.function.ExpressionRuntime;

/**
 * A number field template.
 *
 * @author Thomas Morgner
 */
public class NumberFieldTemplate extends AbstractTemplate
        implements RawDataSource
{
  /**
   * A decimal format filter.
   */
  private DecimalFormatFilter decimalFormatFilter;

  /**
   * A data-row accessor.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * Creates a new number field template.
   */
  public NumberFieldTemplate ()
  {
    dataRowDataSource = new DataRowDataSource();
    decimalFormatFilter = new DecimalFormatFilter();
    decimalFormatFilter.setDataSource(dataRowDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(decimalFormatFilter);
  }

  /**
   * Returns the number formatter.
   *
   * @return The number formatter.
   */
  public DecimalFormat getDecimalFormat ()
  {
    return (DecimalFormat) decimalFormatFilter.getFormatter();
  }

  /**
   * Sets the number formatter.
   *
   * @param decimalFormat the number formatter.
   */
  public void setDecimalFormat (final DecimalFormat decimalFormat)
  {
    decimalFormatFilter.setFormatter(decimalFormat);
  }

  /**
   * Returns the format string.
   *
   * @return The format string.
   */
  public String getFormat ()
  {
    return decimalFormatFilter.getFormatString();
  }

  /**
   * Sets the format string.
   *
   * @param format the format string.
   */
  public void setFormat (final String format)
  {
    decimalFormatFilter.setFormatString(format);
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field the field name.
   */
  public void setField (final String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
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
   * Returns the string that represents a <code>null</code> value.
   *
   * @return A string.
   */
  public String getNullValue ()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the string that represents a <code>null</code> value.
   *
   * @param nullValue the string that represents a <code>null</code> value.
   */
  public void setNullValue (final String nullValue)
  {
    stringFilter.setNullValue(nullValue);
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
    return stringFilter.getValue(runtime);
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final NumberFieldTemplate template = (NumberFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.decimalFormatFilter = (DecimalFormatFilter) template.stringFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.decimalFormatFilter.getDataSource();
    return template;
  }

  /**
   * Returns the datarow data source used in this template.
   *
   * @return the datarow data source.
   */
  protected DataRowDataSource getDataRowDataSource ()
  {
    return dataRowDataSource;
  }

  public Object getRawValue (final ExpressionRuntime runtime)
  {
    return decimalFormatFilter.getRawValue(runtime);
  }

  public FormatSpecification getFormatString(final ExpressionRuntime runtime, final FormatSpecification formatSpecification)
  {
    return decimalFormatFilter.getFormatString(runtime, formatSpecification);
  }
}
