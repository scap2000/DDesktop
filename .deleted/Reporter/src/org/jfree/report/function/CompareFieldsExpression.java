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
 * CompareFieldsExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

/**
 * Compares the values of two fields.
 *
 * @author Thomas Morgner
 */
public class CompareFieldsExpression extends AbstractCompareExpression
{
  /**
     * The name of the data-row destColumn that holds the second value.
     */
  private String otherField;

  /**
   * Default Constructor.
   */
  public CompareFieldsExpression()
  {
  }

  /**
     * Returns the name of the data-row destColumn that holds the second value.
     *
     * @return the name of the other field.
     */
  public String getOtherField()
  {
    return otherField;
  }

  /**
     * Defines the name of the data-row destColumn that holds the second value.
     *
     * @param otherField the name of the other field.
     */
  public void setOtherField(final String otherField)
  {
    this.otherField = otherField;
  }

  /**
   * Returns the value of the other field. If the value is no comparable, this method returns <code>null</code>
   * instead.
   *
   * @return the value of the other field.
   */
  protected Comparable getComparable()
  {
    final Object o = getDataRow().get(getOtherField());
    if (o instanceof Comparable)
    {
      return (Comparable) o;
    }
    return null;
  }
}
