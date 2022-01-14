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
 * CompareNumberExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.numeric;

import org.jfree.report.function.AbstractCompareExpression;

/**
 * A function that compares a static number with a number read from a field.
 *
 * @author Thomas Morgner
 * @deprecated like all compare functions, using the formula support is easier.
 */
public class CompareNumberExpression extends AbstractCompareExpression
{
  /** The number to which the field's value get compared. */
  private Double number;

  /**
   * Default constructor.
   */
  public CompareNumberExpression()
  {
    number = new Double(0);
  }

  /**
   * Returns the static value to which the field's value is compared.
   *
   * @return the static value.
   */
  protected Comparable getComparable()
  {
    return number;
  }

  /**
   * Returns the static number value.
   * @return the static number.
   */
  public double getNumber()
  {
    return number.doubleValue();
  }

  /**
   * Sets the static number value.
   * @param number the static number.
   */
  public void setNumber(final double number)
  {
    this.number = new Double(number);
  }
}
