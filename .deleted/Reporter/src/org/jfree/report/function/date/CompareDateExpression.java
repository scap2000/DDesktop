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
 * CompareDateExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.date;

import java.util.Date;

import org.jfree.report.function.AbstractCompareExpression;

/**
 * This expression compares a static date with the value read from a field.
 *
 * @author Thomas Morgner
 * @deprecated This can be solved easier using the Inline-Expression language.
 */
public class CompareDateExpression extends AbstractCompareExpression
{
  /**
   * The static date that is used in the comparison.
   */
  private Date date;

  /**
   * Default Constructor.
   */
  public CompareDateExpression()
  {
  }

  /**
   * Returns the static date that is used for the comparison.
   *
   * @return the date.
   */
  public Date getDate()
  {
    return date;
  }

  /**
   * Defines the static date for the comparison. If no date is defined, this expression will always evaluate to false.
   *
   * @param date the static date for the comparison.
   */
  public void setDate(final Date date)
  {
    this.date = date;
  }

  /**
   * Returns the static comparable.
   *
   * @return the static comparable.
   */
  protected Comparable getComparable()
  {
    return date;
  }
}
