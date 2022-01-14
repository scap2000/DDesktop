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
 * AndExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.bool;

import org.jfree.report.function.ColumnAggregationExpression;

/**
 * Computes the logical AND of all fields given. Non-boolean values are ignored and have no
 * influence on the result.
 *
 * @author Thomas Morgner
 */
public class AndExpression extends ColumnAggregationExpression
{
  /**
   * Default Constructor.
   */
  public AndExpression()
  {
  }

  /**
   * Computes the logical AND of all fields given.
   *
   * @return Boolean.TRUE or Boolean.FALSE
   */
  public Object getValue()
  {
    final Object[] values = getFieldValues();
    if (values.length == 0)
    {
      return Boolean.FALSE;
    }

    for (int i = 0; i < values.length; i++)
    {
      final Object value = values[i];
      if (value instanceof Boolean == false)
      {
        continue;
      }

      final Boolean n = (Boolean) value;
      if (n.equals(Boolean.FALSE))
      {
        return Boolean.FALSE;
      }
    }

    return Boolean.TRUE;
  }
}
