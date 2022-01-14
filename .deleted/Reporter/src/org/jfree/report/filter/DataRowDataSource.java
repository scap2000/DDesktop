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
 * DataRowDataSource.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter;

import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.function.FormulaExpression;
import org.jfree.util.Log;

/**
 * A DataSource that can access values from the 'data-row'. The data-row contains all values from the current row of the
 * report's <code>TableModel</code>, plus the current values of the defined expressions and functions for the report.
 * <p/>
 * The DataRowDataSource can either labelQuery the data-row directly using the specified field name or it can evaluate a
 * given formula (which must be compatible to the OpenFormula specifications) to compute the value.
 * <p/>
 * Fields and formulas are mutually exclusive; defining a field name autmatically undefines the formula and vice versa.
 *
 * @author Thomas Morgner
 * @see org.jfree.report.DataRow
 */
public class DataRowDataSource implements DataSource
{
  /**
   * The field name that should be queried.
   */
  private String field;

  /**
   * The formula-expression that computes the result value, if no field is given.
   */
  private FormulaExpression valueExpression;

  /**
   * Default constructor.
   * <p/>
   * The expression name is empty ("", not null), the value initially null.
   */
  public DataRowDataSource()
  {
    this(null);
  }

  /**
   * Constructs a new data source.
   *
   * @param column the name of the field, function or expression in the data-row.
   */
  public DataRowDataSource(final String column)
  {
    this.field = column;
  }

  /**
     * Returns the data source destColumn name.
     *
     * @return the destColumn name.
     */
  public String getDataSourceColumnName()
  {
    return field;
  }

  /**
     * Defines the name of the destColumn in the datarow to be queried.
     *
     * @param dataSourceColumnName the name of the destColumn in the datarow to be queried.
     * @throws NullPointerException if the name is <code>null</code>.
     * @see org.jfree.report.DataRow#get
     */
  public void setDataSourceColumnName(final String dataSourceColumnName)
  {
    if (dataSourceColumnName == null)
    {
      throw new NullPointerException();
    }
    this.field = dataSourceColumnName;
    if (valueExpression != null)
    {
      this.valueExpression.setFormula(null);
    }
  }

  /**
   * Returns the formula used to compute the value of the data source.
   *
   * @return the formula.
   */
  public String getFormula()
  {
    if (valueExpression == null)
    {
      return null;
    }
    return valueExpression.getFormula();
  }

  /**
   * Defines the formula used to compute the value of this data source.
   *
   * @param formula the formula for the data source.
   */
  public void setFormula(final String formula)
  {
    if (formula == null)
    {
      throw new NullPointerException();
    }

    this.field = null;
    if (valueExpression == null)
    {
      valueExpression = new FormulaExpression();
    }
    this.valueExpression.setFormula(formula);
    if ("field".equals(valueExpression.getFormulaNamespace()))
    {
      Log.warn("Encountered formula with 'field' prefix. Direct access to field-data should not be done using a formula. Auto-Fixing.");
      this.field = valueExpression.getFormulaExpression();
      this.valueExpression.setFormula(null);
    }
  }

  /**
     * Returns the current value of the data source, obtained from a particular destColumn in the data-row.
     *
     * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
     * this filter.
     * @return the value.
     */
  public Object getValue(final ExpressionRuntime runtime)
  {
    if (runtime == null)
    {
      return null;
    }

    if (field != null)
    {
      return runtime.getDataRow().get(field);
    }
    if (valueExpression == null)
    {
      return null;
    }

    valueExpression.setRuntime(runtime);
    try
    {
      return valueExpression.getValue();
    }
    catch (Exception e)
    {
      // ignore ..
      return null;
    }
    finally
    {
      valueExpression.setRuntime(null);
    }
  }

  /**
   * Clones the data source. A previously registered report definition is not inherited to the clone.
   *
   * @return a clone.
   * @throws CloneNotSupportedException if the cloning is not supported.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    final DataRowDataSource drs = (DataRowDataSource) super.clone();
    if (valueExpression != null)
    {
      drs.valueExpression = (FormulaExpression) valueExpression.clone();
    }
    return drs;
  }
}
