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
 * ColumnAverageExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import java.math.BigDecimal;

/**
 * Computes the horizontal average over all columns specified in the field-list. The average will be computed over all
 * fields of the current data-row, it will not be computed for all rows in the group. For that use the {@link
 * org.jfree.report.function.ItemAvgFunction} instead.
 * <p/>
 * Non numeric and null-columns will be treated as zero for the task of summing up all members. Whether these fields are
 * used counted as valid fields can be controlled with the 'onlyValidFields' flag.
 *
 * @author Thomas Morgner
 */
public class ColumnAverageExpression extends ColumnAggregationExpression
{
  /**
   * A flag defining whether non-numeric and null-values should be ignored.
   */
  private boolean onlyValidFields;

  /**
   * A flag defining whether the expression  should return infinity if there are no valid fields. If set to false, this
   * expression returns null instead.
   */
  private boolean returnInfinity;
  /**
   * The scale-property defines the precission of the divide-operation.
   */
  private int scale;
  /**
   * The rounding-property defines the precission of the divide-operation.
   */
  private int roundingMode;

  /**
   * Default Constructor.
   */
  public ColumnAverageExpression()
  {
    this.returnInfinity = true; // for backward compatiblity.
    scale = 14;
    roundingMode = BigDecimal.ROUND_HALF_UP;
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
   * Returns, whether the expression returns infinity if there are no valid fields. If set to false, this expression
   * returns null instead.
   *
   * @return true, if infinity is returned, false otherwise.
   */
  public boolean isReturnInfinity()
  {
    return returnInfinity;
  }

  /**
   * Defines, whether the expression returns infinity if there are no valid fields. If set to false, this expression
   * returns null instead.
   *
   * @param returnInfinity true, if infinity is returned, false otherwise.
   */
  public void setReturnInfinity(final boolean returnInfinity)
  {
    this.returnInfinity = returnInfinity;
  }

  /**
   * Returns, whether non-numeric and null-values are ignored for the average-computation.
   *
   * @return true, if the invalid fields will be ignored, false if they count as valid zero-value fields.
   */
  public boolean isOnlyValidFields()
  {
    return onlyValidFields;
  }

  /**
   * Defines, whether non-numeric and null-values are ignored for the average-computation.
   *
   * @param onlyValidFields true, if the invalid fields will be ignored, false if they count as valid zero-value
   *                        fields.
   */
  public void setOnlyValidFields(final boolean onlyValidFields)
  {
    this.onlyValidFields = onlyValidFields;
  }

  /**
   * Computes the horizontal average of all field in the field-list. The average will be computed over all fields of the
   * current data-row, it will not be computed for all rows in the group. For that use the {@link
   * org.jfree.report.function.ItemAvgFunction} instead.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    final Object[] values = getFieldValues();
    BigDecimal computedResult = new BigDecimal(0);
    int count = 0;
    for (int i = 0; i < values.length; i++)
    {
      final Object value = values[i];
      if (value instanceof Number == false)
      {
        continue;
      }

      final Number n = (Number) value;
      final BigDecimal nval = new BigDecimal(n.toString());
      computedResult = computedResult.add(nval);
      count += 1;
    }

    if (onlyValidFields)
    {
      if (count == 0)
      {
        if (returnInfinity == false)
        {
          return null;
        }
        if (computedResult.signum() == -1)
        {
          return new Double(Double.NEGATIVE_INFINITY);
        }
        else
        {
          return new Double(Double.POSITIVE_INFINITY);
        }
      }
      return computedResult.divide(new BigDecimal(count), scale, roundingMode);
    }

    if (values.length == 0)
    {
      if (returnInfinity == false)
      {
        return null;
      }
      if (computedResult.signum() == -1)
      {
        return new Double(Double.NEGATIVE_INFINITY);
      }
      else
      {
        return new Double(Double.POSITIVE_INFINITY);
      }
    }
    return computedResult.divide(new BigDecimal(values.length), scale, roundingMode);
  }
}
