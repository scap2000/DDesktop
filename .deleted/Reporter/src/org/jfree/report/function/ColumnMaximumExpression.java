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
 * ColumnMaximumExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

/**
 * Computes the maximum of all data-row columns defined in the field-list. This computes the horizontal
 * maximum, to compute the maximum value in a group, use the ItemMaxFunction instead.  
 *
 * @author Thomas Morgner
 */
public class ColumnMaximumExpression extends ColumnAggregationExpression
{
  /**
   * Default Constructor.
   */
  public ColumnMaximumExpression()
  {
  }

  /**
   * Returns the maximum value. Non-comparable values are ignored.
   *
   * @return the maximum value computed by the function.
   */
  public Object getValue()
  {
    final Object[] values = getFieldValues();
    Comparable computedResult = null;
    for (int i = 0; i < values.length; i++)
    {
      final Object value = values[i];
      if (value instanceof Comparable == false)
      {
        continue;
      }

      final Comparable n = (Comparable) value;
      if (computedResult == null)
      {
        computedResult = n;
      }
      else if (computedResult.compareTo(n) > 0)
      {
        computedResult = n;
      }
    }
    return computedResult;
  }
}
