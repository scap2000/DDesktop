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
 * MessageFormatExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function.strings;

import java.io.UnsupportedEncodingException;

import java.util.Date;

import org.jfree.report.DataRow;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.filter.MessageFormatSupport;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.Expression;
import org.jfree.report.util.UTFEncodingUtil;
import org.jfree.util.Log;

/**
 * Formats a message read from a resource-bundle using named parameters. The parameters are resolved against the current
 * data-row.
 * <p/>
 * This performs the same task as the ResourceMessageFormatFilter does inside a text-element.
 *
 * @author Thomas Morgner
 */
public class MessageFormatExpression extends AbstractExpression
{
  /**
   * A internal data-row wrapper that URL-encodes all values returned by the data-row. 
   */
  private static class EncodeDataRow implements DataRow
  {
    /** The wrappedDataRow datarow. */
    private DataRow wrappedDataRow;
    /** The character encoding used for the URL-encoding. */
    private String encoding;

    /**
     * Default Constructor.
     */
    protected EncodeDataRow()
    {
    }

    /**
     * Returns the wrapped data-row.
     * @return the wrapped data-row.
     */
    public DataRow getWrappedDataRow()
    {
      return wrappedDataRow;
    }

    /**
     * Defines the wrapped data-row.
     *
     * @param wrappedDataRow the wrapped datarow.
     */
    public void setWrappedDataRow(final DataRow wrappedDataRow)
    {
      this.wrappedDataRow = wrappedDataRow;
    }

    /**
     * Returns the String-encoding used for the URL encoding.
     * @return the string-encoding.
     */
    public String getEncoding()
    {
      return encoding;
    }

    /**
     * Defines the String-encoding used for the URL encoding.
     * @param encoding the string-encoding.
     */
    public void setEncoding(final String encoding)
    {
      this.encoding = encoding;
    }

    /**
     * Encodes the given value. The encoding process is skipped, if the value is null, is a number or is a date.
     *
     * @param fieldValue the value that should be encoded.
     * @return the encoded value.
     */
    private Object encode(final Object fieldValue)
    {
      if (fieldValue == null)
      {
        return null;
      }
      if (fieldValue instanceof Date)
      {
        return fieldValue;
      }
      else if (fieldValue instanceof Number)
      {
        return fieldValue;
      }
      try
      {
        return UTFEncodingUtil.encode(String.valueOf(fieldValue), encoding);
      }
      catch (UnsupportedEncodingException e)
      {
        Log.debug("Unsupported Encoding: " + encoding);
        return null;
      }
    }

    /**
	 * Returns the value of the expression or destColumn in the data-row using the given
	 * column number as index.
	 *
	 * @param col the item index.
	 * @return the value.
	 * @throws IllegalStateException if the datarow detected a deadlock.
	 */
    public Object get(final int col)
    {
      return encode(wrappedDataRow.get(col));
    }

    /**
	 * Returns the value of the function, expression or destColumn using its specific name. The
	 * given name is translated into a valid destColumn number and the the destColumn is queried.
	 * For functions and expressions, the <code>getValue()</code> method is called and for
	 * columns from the tablemodel the tablemodel method <code>getValueAt(row,
	 * column)</code> gets called.
	 *
	 * @param col the item index.
	 * @return the value.
	 *
	 * @throws IllegalStateException if the datarow detected a deadlock.
	 */
    public Object get(final String col) throws IllegalStateException
    {
      return encode(wrappedDataRow.get(col));
    }

    /**
	 * Returns the name of the destColumn, expression or function. For columns from the
	 * tablemodel, the tablemodels <code>getColumnName</code> method is called. For
	 * functions, expressions and report properties the assigned name is returned.
	 *
	 * @param col the item index.
	 * @return the name.
	 */
    public String getColumnName(final int col)
    {
      return wrappedDataRow.getColumnName(col);
    }

    /**
	 * Returns the destColumn position of the destColumn, expression or function with the given name
	 * or -1 if the given name does not exist in this DataRow.
	 *
	 * @param name the item name.
	 * @return the item index.
	 */
    public int findColumn(final String name)
    {
      return wrappedDataRow.findColumn(name);
    }

    /**
     * Returns the number of columns, expressions and functions and marked ReportProperties
     * in the report.
     *
     * @return the item count.
     */
    public int getColumnCount()
    {
      return wrappedDataRow.getColumnCount();
    }

    /**
	 * Checks, whether the value contained in the destColumn has changed since the
	 * last advance-operation.
	 *
	 * @param name the name of the destColumn.
	 * @return true, if the value has changed, false otherwise.
	 */
    public boolean isChanged(final String name)
    {
      return wrappedDataRow.isChanged(name);
    }

    /**
	 * Checks, whether the value contained in the destColumn has changed since the
	 * last advance-operation.
	 *
	 * @param index the numerical index of the destColumn to check.
	 * @return true, if the value has changed, false otherwise.
	 */
    public boolean isChanged(final int index)
    {
      return wrappedDataRow.isChanged(index);
    }
  }

  /**
   * The message-format pattern used to compute the result.
   */
  private String pattern;

  /**
   * The message format support translates raw message strings into useable MessageFormat parameters and read the
   * necessary input data from the datarow.
   */
  private MessageFormatSupport messageFormatSupport;
  /**
   * A flag indicating whether the data read from the fields should be URL encoded.
   */
  private boolean urlEncodeData;
  /**
   * A flag indicating whether the whole result string should be URL encoded.
   */
  private boolean urlEncodeResult;
  /**
   * The byte-encoding used for the URL encoding.
   */
  private String encoding;

  /**
   * Default constructor.
   */
  public MessageFormatExpression()
  {
    messageFormatSupport = new MessageFormatSupport();
    encoding = "ISO-8859-1";
  }


  /**
   * Returns the format string used in the message format.
   *
   * @return the format string.
   */
  public String getPattern()
  {
    return pattern;
  }

  /**
   * Defines the format string for the {@link java.text.MessageFormat} object used in this implementation.
   *
   * @param pattern the message format.
   */
  public void setPattern(final String pattern)
  {
    this.pattern = pattern;
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
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    this.encoding = encoding;
  }

  /**
   * Defines, whether the values read from the data-row should be URL encoded. Dates and Number objects are never
   * encoded.
   *
   * @param urlEncode true, if the values from the data-row should be URL encoded before they are passed to the
   *                  MessageFormat, false otherwise.
   */
  public void setUrlEncodeValues(final boolean urlEncode)
  {
    this.urlEncodeData = urlEncode;
  }

  /**
   * Queries, whether the values read from the data-row should be URL encoded.
   *
   * @return true, if the values are encoded, false otherwise.
   */
  public boolean isUrlEncodeValues()
  {
    return urlEncodeData;
  }

  /**
   * Queries, whether the formatted result-string will be URL encoded.
   *
   * @return true, if the formatted result will be encoded, false otherwise.
   */
  public boolean isUrlEncodeResult()
  {
    return urlEncodeResult;
  }

  /**
   * Defines, whether the formatted result-string will be URL encoded.
   *
   * @param urlEncodeResult true, if the formatted result will be encoded, false otherwise.
   */
  public void setUrlEncodeResult(final boolean urlEncodeResult)
  {
    this.urlEncodeResult = urlEncodeResult;
  }

  /**
   * Returns the replacement text that is used if one of the referenced message parameters is null.
   *
   * @return the replacement text for null-values.
   */
  public String getNullString()
  {
    return messageFormatSupport.getNullString();
  }

  /**
   * Defines the replacement text that is used if one of the referenced message parameters is null.
   *
   * @param nullString the replacement text for null-values.
   */
  public void setNullString(final String nullString)
  {
    this.messageFormatSupport.setNullString(nullString);
  }

  /**
   * Returns the formatted message.
   *
   * @return the formatted message.
   */
  public Object getValue()
  {
    final ResourceBundleFactory resourceBundleFactory = getResourceBundleFactory();
    messageFormatSupport.setFormatString(pattern);
    messageFormatSupport.setLocale(resourceBundleFactory.getLocale());

    final String result;
    if (isUrlEncodeValues())
    {
      final EncodeDataRow dataRow = new EncodeDataRow();
      dataRow.setEncoding(encoding);
      dataRow.setWrappedDataRow(getDataRow());
      result = messageFormatSupport.performFormat(dataRow);
    }
    else
    {
      result = messageFormatSupport.performFormat(getDataRow());
    }

    if (isUrlEncodeResult())
    {
      try
      {
        return UTFEncodingUtil.encode(result, getEncoding());
      }
      catch (UnsupportedEncodingException e)
      {
        Log.debug("Unsupported Encoding: " + encoding);
        return null;
      }
    }
    else
    {
      return result;
    }
  }


  public Expression getInstance()
  {
    final MessageFormatExpression ex = (MessageFormatExpression) super.getInstance();
    ex.messageFormatSupport = new MessageFormatSupport();
    return ex;
  }
}
