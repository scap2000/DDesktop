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
 * MessageFieldTemplate.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.filter.templates;

import org.jfree.report.filter.MessageFormatFilter;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.function.ExpressionRuntime;

/**
 * The message field template simplifies the on-the-fly creation of strings.
 *
 * @author Thomas Morgner
 * @see org.jfree.report.filter.MessageFormatSupport
 */
public class MessageFieldTemplate extends AbstractTemplate
{
  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * The message format filter inlines data from other sources into a string.
   */
  private MessageFormatFilter messageFormatFilter;

  /**
   * Creates a new string field template.
   */
  public MessageFieldTemplate ()
  {
    messageFormatFilter = new MessageFormatFilter();
    stringFilter = new StringFilter();
    stringFilter.setDataSource(messageFormatFilter);
  }

  /**
     * Returns the format string used in the message format filter.
     * This is a raw value which contains untranslated references to destColumn names.
     * It cannot be used directly in java.text.MessageFormat objects.
     *
     * @return the format string.
     */
  public String getFormat ()
  {
    return messageFormatFilter.getFormatString();
  }

  /**
     * Redefines the format string for the message format. The assigned message
     * format string must be given as raw value, where destColumn references are given
     * in the format $(COLNAME).
     *
     * @param format the new format string.
     */
  public void setFormat (final String format)
  {
    this.messageFormatFilter.setFormatString(format);
  }

  /**
   * Returns the value displayed by the field when the data source value is
   * <code>null</code>.
   *
   * @return A value to represent <code>null</code>.
   */
  public String getNullValue ()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the value displayed by the field when the data source value is
   * <code>null</code>.
   *
   * @param nullValue the value that represents <code>null</code>.
   */
  public void setNullValue (final String nullValue)
  {
    messageFormatFilter.setNullString(nullValue);
    stringFilter.setNullValue(nullValue);
  }

  /**
   * Returns the current value for the data source.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return the value.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    return stringFilter.getValue(runtime);
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final MessageFieldTemplate template = (MessageFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.messageFormatFilter = (MessageFormatFilter) template.stringFilter.getDataSource();
    return template;
  }

}
