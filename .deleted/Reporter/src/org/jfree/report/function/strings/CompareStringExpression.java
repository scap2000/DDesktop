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
 * CompareStringExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.strings;

import org.jfree.report.function.AbstractCompareExpression;

/**
 * Compares a given static string with a string read from a field.
 *
 * @author Thomas Morgner
 * @deprecated This can be done a lot easier using a simple formula.
 */
public class CompareStringExpression extends AbstractCompareExpression
{
  /** The static text. */
  private String text;

  /**
   * Default Constructor.
   */
  public CompareStringExpression()
  {
  }

  /**
   * Returns the static text to which the field's value should be compared to.
   *
   * @return the text.
   */
  public String getText()
  {
    return text;
  }

  /**
   * Defines the static text to which the field's value should be compared to.
   *
   * @param text the text.
   */
  public void setText(final String text)
  {
    this.text = text;
  }

  /**
   * Returns the text.
   * @return the text.
   */
  protected Comparable getComparable()
  {
    return text;
  }
}
