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
 * TotalGroupSumQuotientFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.math.BigDecimal;

import org.jfree.report.event.ReportEvent;

/**
 * A report function that calculates the quotient of two summed fields (columns) from the report's data row. This
 * function produces a global total. The total sum of the group is known when the group processing starts and the report
 * is not performing a prepare-run. The sum is calculated in the prepare run and recalled in the printing run.
 * <p/>
 * The function can be used in two ways: <ul> <li>to calculate a quotient for the entire report;</li> <li>to calculate a
 * quotient within a particular group;</li> </ul> This function expects its input values to be either java.lang.Number
 * instances or Strings that can be parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p/>
 * The function undestands tree parameters. The <code>dividend</code> parameter is required and denotes the name of an
 * ItemBand-field which gets summed up as dividend. The <code>divisor</code> parameter is required and denotes the name
 * of an ItemBand-field which gets summed up as divisor.
 * <p/>
 * The parameter <code>group</code> denotes the name of a group. When this group is started, the counter gets reseted to
 * null. This parameter is optional.
 *
 * @author Thomas Morgner
 */
public class TotalGroupSumQuotientFunction extends AbstractFunction
{
  /**
   * Internal function to compute the dividend.
   */
  private TotalGroupSumFunction dividendFunction;
  /**
   * Internal function to compute the divisor.
   */
  private TotalGroupSumFunction divisorFunction;
  /**
   * The scale-property defines the precission of the divide-operation.
   */
  private int scale;
  /**
   * The rounding-property defines the precission of the divide-operation.
   */
  private int roundingMode;

  /**
   * Constructs a new function.
   * <p/>
   * Initially the function has no name...be sure to assign one before using the function.
   */
  public TotalGroupSumQuotientFunction()
  {
    scale = 14;
    roundingMode = BigDecimal.ROUND_HALF_UP;
    dividendFunction = new TotalGroupSumFunction();
    divisorFunction = new TotalGroupSumFunction();
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    dividendFunction.reportInitialized(event);
    divisorFunction.reportInitialized(event);
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    dividendFunction.groupStarted(event);
    divisorFunction.groupStarted(event);
  }


  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    dividendFunction.itemsAdvanced(event);
    divisorFunction.itemsAdvanced(event);
  }

  /**
   * Returns the name of the group to be totalled.
   *
   * @return the group name.
   */
  public String getGroup()
  {
    return divisorFunction.getGroup();
  }

  /**
   * Defines the name of the group to be totalled. If the name is null, all groups are totalled.
   *
   * @param group the group name.
   */
  public void setGroup(final String group)
  {
    this.divisorFunction.setGroup(group);
    this.dividendFunction.setGroup(group);
  }

  /**
   * Return the current function value. <P> The value depends (obviously) on the function implementation.   For example,
   * a page counting function will return the current page number.
   *
   * @return The value of the function.
   */
  public Object getValue()
  {
    final BigDecimal dividend = (BigDecimal) dividendFunction.getValue();
    final BigDecimal divisor = (BigDecimal) divisorFunction.getValue();
    if (divisor == null || dividend == null || divisor.doubleValue() == 0)
    {
      return null;
    }
    return dividend.divide(divisor, scale, roundingMode);
  }

  /**
     * Returns the field used as dividend by the function. <P> The field name corresponds to a destColumn name in the report's
     * data-row.
     *
     * @return The field name.
     */
  public String getDividend()
  {
    return this.dividendFunction.getField();
  }

  /**
     * Returns the field used as divisor by the function. <P> The field name corresponds to a destColumn name in the report's
     * data row.
     *
     * @return The field name.
     */
  public String getDivisor()
  {
    return this.divisorFunction.getField();
  }

  /**
     * Sets the field name to be used as dividend for the function. <P> The field name corresponds to a destColumn name in the
     * report's data-row.
     *
     * @param dividend the field name (null not permitted).
     */
  public void setDividend(final String dividend)
  {
    this.dividendFunction.setField(dividend);
  }

  /**
     * Sets the field name to be used as divisor for the function. <P> The field name corresponds to a destColumn name in the
     * report's data-row.
     *
     * @param divisor the field name (null not permitted).
     */
  public void setDivisor(final String divisor)
  {
    this.divisorFunction.setField(divisor);
  }

  /**
   * Returns the defined rounding mode. This influences the precision of the divide-operation.
   *
   * @return the rounding mode.
   * @see java.math.BigDecimal#divide(java.math.BigDecimal,int)
   */
  public int getRoundingMode()
  {
    return roundingMode;
  }

  /**
   * Defines the rounding mode. This influences the precision of the divide-operation.
   *
   * @param roundingMode the rounding mode.
   * @see java.math.BigDecimal#divide(java.math.BigDecimal,int)
   */
  public void setRoundingMode(final int roundingMode)
  {
    this.roundingMode = roundingMode;
  }

  /**
   * Returns the scale for the divide-operation. The scale influences the precision of the division.
   *
   * @return the scale.
   */
  public int getScale()
  {
    return scale;
  }

  /**
   * Defines the scale for the divide-operation. The scale influences the precision of the division.
   *
   * @param scale the scale.
   */
  public void setScale(final int scale)
  {
    this.scale = scale;
  }

  /**
   * Defines the function's dependency level. This method forwards all calls to the interal functions.
   *
   * @param level the dependency level.
   * @see Expression#getDependencyLevel()
   */
  public void setDependencyLevel(final int level)
  {
    super.setDependencyLevel(level);
    dividendFunction.setDependencyLevel(level);
    divisorFunction.setDependencyLevel(level);
  }

  /**
   * Defines the ExpressionRune used in this expression. The ExpressionRuntime is set before the expression receives
   * events or gets evaluated and is unset afterwards. Do not hold references on the runtime or you will create
   * memory-leaks.
   *
   * @param runtime the runtime information for the expression
   */
  public void setRuntime(final ExpressionRuntime runtime)
  {
    super.setRuntime(runtime);
    dividendFunction.setRuntime(runtime);
    divisorFunction.setRuntime(runtime);
  }

}
