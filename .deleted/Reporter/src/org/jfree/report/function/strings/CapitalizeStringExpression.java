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
 * CapitalizeStringExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.strings;

import org.jfree.report.function.AbstractExpression;

/**
 * A expression that transforms all first letters of a given string into upper-case letters.
 *
 * @author Thomas Morgner
 */
public class CapitalizeStringExpression extends AbstractExpression
{
  /** The field name from where to read the string that should be capitalized. */
  private String field;
  /** A flag indicating that only the first word should be capitalized. */
  private boolean firstWordOnly;

  /**
   * Default constructor.
   */
  public CapitalizeStringExpression()
  {
  }

  /**
   * Returns, whether only the first word should be capitalized.
   *
   * @return true, if the first word should be capitalized, false if all words should be capitalized.
   */
  public boolean isFirstWordOnly()
  {
    return firstWordOnly;
  }

  /**
   * Defines, whether only the first word should be capitalized.
   *
   * @param firstWordOnly true, if the first word should be capitalized, false if all words should be capitalized.
   */
  public void setFirstWordOnly(final boolean firstWordOnly)
  {
    this.firstWordOnly = firstWordOnly;
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
   * Capitalizes the string that has been read from the defined field.
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
    final char[] textArray = text.toCharArray();

    boolean startOfWord = true;

    for (int i = 0; i < textArray.length; i++)
    {
      final char c = textArray[i];
      // we ignore the punctutation chars or any other possible extra chars
      // for now. Words start at whitespaces ...
      if (Character.isWhitespace(c))
      {
        startOfWord = true;
      }
      else
      {
        if (startOfWord == true)
        {
          textArray[i] = Character.toTitleCase(c);
        }
        if (firstWordOnly)
        {
          break;
        }
        startOfWord = false;
      }
    }
    return new String (textArray);
  }
}
