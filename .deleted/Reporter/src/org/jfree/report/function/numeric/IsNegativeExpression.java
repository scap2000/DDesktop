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
 * IsNegativeExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.numeric;

import org.jfree.report.function.AbstractExpression;

/**
 * Checks, whether the destColumn specified by the field name contains a negative number.
 *
 * @author Thomas Morgner
 */
public class IsNegativeExpression extends AbstractExpression
{
  /**
   * The field name.
   */
  private String field;

  /**
   * Default constructor.
   */
  public IsNegativeExpression()
  {
  }

  /**
     * Checks, whether the destColumn specified by the field name contains a negative number. Non-numeric values are not
     * compared and result in Boolean.FALSE.
     *
     * @return the value of the function.
     */
  public Object getValue()
  {
    final Object o = getDataRow().get(getField());
    if (o instanceof Number == false)
    {
      return Boolean.FALSE;
    }
    final Number n = (Number) o;
    if (n.doubleValue() < 0)
    {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  /**
   * Returns the name of the field from where to read the number.
   *
   * @return the field.
   */
  public String getField()
  {
    return field;
  }

  /**
   * Defines the name of the field from where to read the number.
   *
   * @param field the field.
   */
  public void setField(final String field)
  {
    this.field = field;
  }
}
