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
 * TokenizeStringExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.strings;

import java.util.StringTokenizer;

import org.jfree.report.function.AbstractExpression;

/**
 * Tokenizes a string and replaces all occurences of the delimeter with the value given in replacement. An optional
 * prefix and suffix can be appended to the string.
 *
 * @author Thomas Morgner
 */
public class TokenizeStringExpression extends AbstractExpression
{
  /**
   * The field from where to read the original value.
   */
  private String field;
  /**
   * The delimeter value.
   */
  private String delimeter;
  /**
   * The replacement value.
   */
  private String replacement;
  /**
   * The (optional) prefix value.
   */
  private String prefix;
  /**
   * The (optional) suffix value.
   */
  private String suffix;

  /**
   * Default Constructor.
   */
  public TokenizeStringExpression()
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
   * Returns the delimeter string.
   *
   * @return the delimeter.
   */
  public String getDelimeter()
  {
    return delimeter;
  }

  /**
   * Defines the delimeter string.
   *
   * @param delimeter the delimeter.
   */
  public void setDelimeter(final String delimeter)
  {
    this.delimeter = delimeter;
  }

  /**
   * Returns the replacement for the delimter.
   *
   * @return the replacement text.
   */
  public String getReplacement()
  {
    return replacement;
  }

  /**
   * Defines the replacement for the delimter.
   *
   * @param replacement the replacement text.
   */
  public void setReplacement(final String replacement)
  {
    this.replacement = replacement;
  }

  /**
   * Returns the prefix text.
   *
   * @return the prefix text.
   */
  public String getPrefix()
  {
    return prefix;
  }

  /**
   * Defines the prefix text.
   *
   * @param prefix the prefix text.
   */
  public void setPrefix(final String prefix)
  {
    this.prefix = prefix;
  }

  /**
   * Returns the suffix text.
   *
   * @return the suffix text.
   */
  public String getSuffix()
  {
    return suffix;
  }

  /**
   * Defines the suffix text.
   *
   * @param suffix the suffix text.
   */
  public void setSuffix(final String suffix)
  {
    this.suffix = suffix;
  }

  /**
   * Computes the tokenized string. Replaces 
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

    final StringBuffer buffer = new StringBuffer();
    if (prefix != null)
    {
      buffer.append(prefix);
    }

    if (delimeter != null)
    {
      final StringTokenizer strtok = new StringTokenizer(text, delimeter, false);
      while (strtok.hasMoreTokens())
      {
        final String o = strtok.nextToken();
        buffer.append(o);
        if (replacement != null && strtok.hasMoreTokens())
        {
          buffer.append(replacement);
        }
      }
    }

    if (suffix != null)
    {
      buffer.append(suffix);
    }
    return buffer.toString();
  }
}
