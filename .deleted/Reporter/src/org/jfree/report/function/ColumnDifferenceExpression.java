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
 * ColumnDifferenceExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import java.math.BigDecimal;

/**
 * Computes the difference between the given destColumn values. This is almost the same as the formula
 * <code>[field1] - [field2] - field[3] - .. - [fieldn]</code>. Values that are non-numeric or null
 * are ignored.
 *
 * @author Thomas Morgner
 */
public class ColumnDifferenceExpression extends ColumnAggregationExpression
{
  /**
   * Default Constructor.
   */
  public ColumnDifferenceExpression()
  {
  }

  /**
   * Return the current expression value.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    final Object[] values = getFieldValues();
    BigDecimal computedResult = null;
    for (int i = 0; i < values.length; i++)
    {
      final Object value = values[i];
      if (value instanceof Number == false)
      {
        continue;
      }

      final Number n = (Number) value;
      final BigDecimal nval = new BigDecimal(n.toString());
      if (computedResult == null)
      {
        computedResult = nval;
      }
      else
      {
        computedResult = computedResult.subtract(nval);
      }
    }

    return computedResult;
  }
}
