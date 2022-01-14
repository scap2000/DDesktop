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
 * SubStringExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.strings;

import org.jfree.report.function.AbstractExpression;

/**
 * Creates a substring of the given text. If the expression has an ellipsis defined, and the given start
 * position and length are greater than the string's length, the ellipsis-text will be appended to indicate
 * that this substring result is only a partial match.
 *
 * A similiar functionality can be achived using the built-in formula support.
 *
 * @author Thomas Morgner
 */
public class SubStringExpression extends AbstractExpression
{
  /** The field from where to read the string. */
  private String field;
  /** The ellipse-text that marks partial results. */
  private String ellipsis;
  /** The start-index position. */
  private int start;
  /** The length of the substring. */
  private int length;

  /**
   * Default Constructor.
   */
  public SubStringExpression()
  {
  }

  /**
     * Returns the name of the datarow-destColumn from where to read the string value.
     *
     * @return the field.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Defines the name of the datarow-destColumn from where to read the string value.
     *
     * @param field the field.
     */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Returns the ellipsis-text that indicates partial values.
   * @return the ellipsis.
   */
  public String getEllipsis()
  {
    return ellipsis;
  }

  /**
   * Defines the ellipsis-text that indicates partial values.
   * @param ellipsis the ellipsis.
   */
  public void setEllipsis(final String ellipsis)
  {
    this.ellipsis = ellipsis;
  }

  /**
   * Returns the sub-string's start position.
   * @return the start position.
   */
  public int getStart()
  {
    return start;
  }

  /**
   * Defines the sub-string's start position. The start position cannot be negative.
   * @param start the start position.
   */
  public void setStart(final int start)
  {
    if (length < 0)
    {
      throw new IndexOutOfBoundsException ("String start position cannot be negative.");
    }
    this.start = start;
  }

  /**
   * Returns the sub-string's length.
   * @return the length.
   */
  public int getLength()
  {
    return length;
  }

  /**
   * Defines the sub-string's length. The length cannot be negative.
   * @param length the length.
   */
  public void setLength(final int length)
  {
    if (length < 0)
    {
      throw new IndexOutOfBoundsException("String length cannot be negative.");
    }
    this.length = length;
  }

  /**
   * Computes the sub-string.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    final Object raw = getDataRow().get(getField());
    if (raw == null)
    {
      return null;
    }
    final String text = String.valueOf(raw);

    // the text is not large enough to fit the start-bounds. Return nothing,
    // but indicate that there would have been some more content ...
    if (start >= text.length())
    {
      return appendEllipsis(null);
    }

    // the text fully fits into the given range. No clipping needed ...
    if ((start + length) > text.length())
    {
      return appendEllipsis(text.substring(start));
    }

    return text.substring(start, start + length);
  }

  /**
   * Appends the ellipsis, if one is defined.
   *
   * @param value the computed value (without the ellipsis).
   * @return the computed value with the ellipsis.
   */
  private String appendEllipsis(final String value)
  {
    if (ellipsis == null)
    {
      return value;
    }
    if (value == null)
    {
      return ellipsis;
    }
    return value + ellipsis;
  }
}
