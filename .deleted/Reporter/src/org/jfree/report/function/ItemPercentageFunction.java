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
 * ItemPercentageFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.math.BigDecimal;

import org.jfree.report.event.ReportEvent;
import org.jfree.util.Log;

/**
 * Calculates the percentage value of a numeric field. The total sum is taken and divided by the number of items
 * counted.
 *
 * @author Thomas Morgner
 */
public class ItemPercentageFunction extends AbstractFunction
{
  /**
   * A constant for the value 100.
   */
  private static final BigDecimal HUNDRED = new BigDecimal(100);

  /**
   * A total group sum function.
   */
  private TotalGroupSumFunction totalSumFunction;

  /**
   * The current value.
   */
  private transient BigDecimal currentValue;

  /**
   * A useful constant representing zero.
   */
  private static final BigDecimal ZERO = new BigDecimal(0.0);

  /**
   * The name of the group on which to reset the count. This can be set to null to compute the percentage for the whole
   * report.
   */
  private String group;
  /**
   * The name of the field from where to read the values.
   */
  private String field;
  /**
   * The scale-property defines the precission of the divide-operation.
   */
  private int scale;
  /**
   * The rounding-property defines the precission of the divide-operation.
   */
  private int roundingMode;
  /**
   * A flag defining whether the returned value should be scaled to 100.
   */
  private boolean scaleToHundred;

  /**
   * Creates a new ItemPercentageFunction.
   */
  public ItemPercentageFunction()
  {
    totalSumFunction = new TotalGroupSumFunction();
    totalSumFunction.setName("total");
    scale = 14;
    roundingMode = BigDecimal.ROUND_HALF_UP;
    scaleToHundred = true;
  }

  /**
   * Returns whether the returned value should be scaled to 100.
   *
   * @return true, if the value should be scaled to 100, false otherwise.
   */
  public boolean isScaleToHundred()
  {
    return scaleToHundred;
  }

  /**
   * Defines whether the returned value should be scaled to 100.
   *
   * @param scaleToHundred true, if the value should be scaled to 100, false otherwise.
   */
  public void setScaleToHundred(final boolean scaleToHundred)
  {
    this.scaleToHundred = scaleToHundred;
  }

  /**
   * Returns the defined rounding mode. This influences the precision of the divide-operation.
   *
   * @return the rounding mode.
   * @see java.math.BigDecimal#divide(java.math.BigDecimal, int)
   */
  public int getRoundingMode()
  {
    return roundingMode;
  }

  /**
   * Defines the rounding mode. This influences the precision of the divide-operation.
   *
   * @param roundingMode the rounding mode.
   * @see java.math.BigDecimal#divide(java.math.BigDecimal, int)
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
   * Returns the group name.
   *
   * @return The group name.
   */
  public String getGroup()
  {
    return group;
  }

  /**
   * Sets the group name. <P> If a group is defined, the minimum value is reset to zero at the start of every instance
   * of this group.
   *
   * @param name the group name (null permitted).
   */
  public void setGroup(final String name)
  {
    this.group = name;
    this.totalSumFunction.setGroup(group);
  }

  /**
     * Returns the field used by the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @return The field name.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Sets the field name for the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @param field the field name.
     */
  public void setField(final String field)
  {
    this.field = field;
    this.totalSumFunction.setField(field);
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    totalSumFunction.groupStarted(event);

    final Object fieldValue = event.getDataRow().get(getField());
    if (fieldValue == null)
    {
      // No add, field is null
      return;
    }
    try
    {
      final Number n = (Number) fieldValue;
      currentValue = new BigDecimal(n.toString());
    }
    catch (Exception e)
    {
      Log.error("ItemSumFunction.advanceItems(): problem adding number.");
    }
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    totalSumFunction.itemsAdvanced(event);

    final Object fieldValue = event.getDataRow().get(getField());
    if (fieldValue == null)
    {
      // No add, field is null
      return;
    }
    try
    {
      final Number n = (Number) fieldValue;
      currentValue = new BigDecimal(n.toString());
    }
    catch (Exception e)
    {
      Log.error("ItemSumFunction.advanceItems(): problem adding number.");
    }

  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    totalSumFunction.reportInitialized(event);
    currentValue = ZERO;
  }

  /**
   * Return the current function value. <P> Don not count on the correctness of this function until the preparerun has
   * finished.
   *
   * @return The value of the function.
   */
  public Object getValue()
  {
    final BigDecimal total = (BigDecimal) totalSumFunction.getValue();

    if (total == null || total.longValue() == 0)
    {
      return null;
    }
    if (scaleToHundred)
    {
      return currentValue.multiply(HUNDRED).divide(total, scale, roundingMode);
    }
    else
    {
      return currentValue.divide(total, scale, roundingMode);
    }
  }

  /**
   * Returns a clone of the function. <P> Be aware, this does not create a deep copy. If you have complex strucures
   * contained in objects, you have to overwrite this function.
   *
   * @return A clone of the function.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    final ItemPercentageFunction clone = (ItemPercentageFunction) super.clone();
    clone.totalSumFunction = (TotalGroupSumFunction) totalSumFunction.clone();
    return clone;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any changeable objects with the
   * original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final ItemPercentageFunction function = (ItemPercentageFunction) super.getInstance();
    function.totalSumFunction = (TotalGroupSumFunction) totalSumFunction.getInstance();
    function.currentValue = ZERO;
    return function;
  }

  public void setDependencyLevel(final int level)
  {
    super.setDependencyLevel(level);
    totalSumFunction.setDependencyLevel(level);
  }

  /**
   * Defines the ExpressionRune used in this expression. The ExpressionRuntime is set before the expression receives
   * events or gets evaluated and is unset afterwards. Do not hold references on the runtime or you will create
   * memory-leaks.
   * <p/>
   * This updates the internal TotalItemSumFunction.
   *
   * @param runtime the runtime information for the expression
   */
  public void setRuntime(final ExpressionRuntime runtime)
  {
    super.setRuntime(runtime);
    totalSumFunction.setRuntime(runtime);
  }
}
