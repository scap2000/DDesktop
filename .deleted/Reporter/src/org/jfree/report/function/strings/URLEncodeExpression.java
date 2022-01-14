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
 * URLEncodeExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function.strings;

import java.io.UnsupportedEncodingException;

import org.jfree.report.function.AbstractExpression;
import org.jfree.report.util.UTFEncodingUtil;

/**
 * Performs an URL encoding on the value read from the given field. As the URL-encoding schema is a binary
 * encoding, a real character encoding must be given as well. If not defined otherwise, ISO-8859-1 is used.
 *
 * @author Thomas Morgner
 */
public class URLEncodeExpression extends AbstractExpression
{
  /**
   * The field name from where to read the string that should be URL-encoded.
   */
  private String field;
  /**
   * The character-encoding that should be used for the URL-encoding of the value.
   */
  private String encoding;

  /**
   * Default Constructor.
   */
  public URLEncodeExpression()
  {
    encoding = "ISO-8859-1";
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
   * Returns the defined character encoding that is used to transform the Java-Unicode strings into bytes.
   *
   * @return the encoding.
   */
  public String getEncoding()
  {
    return encoding;
  }

  /**
   * Defines the character encoding that is used to transform the Java-Unicode strings into bytes.
   *
   * @param encoding the encoding.
   */
  public void setEncoding(final String encoding)
  {
    this.encoding = encoding;
  }

  /**
   * Encodes the value read from the defined field. The value is converted to a string using the "toString" method.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    final Object value = getDataRow().get(getField());
    if (value == null)
    {
      return null;
    }
    try
    {
      return UTFEncodingUtil.encode(String.valueOf(value), encoding);
    }
    catch (UnsupportedEncodingException e)
    {
      return null;
    }
  }


}
