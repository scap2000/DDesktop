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
 * MessageFormatFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter;

import java.util.Locale;

import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.util.ObjectUtilities;

/**
 * A filter that formats values from a data source to a string representation.
 * <p/>
 * This filter will format objects using a {@link java.text.MessageFormat} to create the string representation for the
 * number obtained from the datasource.
 *
 * @author Joerg Schaible
 * @author Thomas Morgner
 * @see java.text.MessageFormat
 */
public class MessageFormatFilter implements DataSource
{

  /**
   * The message format support translates raw message strings into useable MessageFormat parameters and read the
   * necessary input data from the datarow.
   */
  private MessageFormatSupport messageFormatSupport;
  /**
   * The current locale.
   */
  private transient Locale locale;

  /**
   * Default constructor. <P> Uses a general number format for the current locale.
   */
  public MessageFormatFilter()
  {
    messageFormatSupport = new MessageFormatSupport();
  }

  /**
   * Defines the format string for the {@link java.text.MessageFormat} object used in this implementation.
   *
   * @param format the message format.
   */
  public void setFormatString(final String format)
  {
    messageFormatSupport.setFormatString(format);
  }

  /**
   * Returns the format string used in the message format.
   *
   * @return the format string.
   */
  public String getFormatString()
  {
    return messageFormatSupport.getFormatString();
  }

  /**
   * Returns the formatted string. The value is read using the data source given and formated using the formatter of
   * this object. The formating is guaranteed to completly form the object to an string or to return the defined
   * NullValue.
   * <p/>
   * If format, datasource or object are null, the NullValue is returned.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return The formatted value.
   */
  public Object getValue(final ExpressionRuntime runtime)
  {
    if (runtime == null)
    {
      return null;
    }
    final ResourceBundleFactory resourceBundleFactory =
        runtime.getResourceBundleFactory();
    final Locale newLocale = resourceBundleFactory.getLocale();
    if (ObjectUtilities.equal(newLocale, locale) == false)
    {
      messageFormatSupport.setLocale(resourceBundleFactory.getLocale());
      locale = newLocale;
    }
    return messageFormatSupport.performFormat(runtime.getDataRow());
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    final MessageFormatFilter mf = (MessageFormatFilter) super.clone();
    mf.messageFormatSupport = (MessageFormatSupport) messageFormatSupport.clone();
    return mf;
  }

  /**
   * Returns the replacement text if one of the referenced fields in the message is null.
   *
   * @return the replacement string for null-values.
   */
  public String getNullString()
  {
    return messageFormatSupport.getNullString();
  }

  /**
   * Defines the replacement text that is used, if one of the referenced fields in the message is null.
   *
   * @param nullString the replacement string for null-values.
   */
  public void setNullString(final String nullString)
  {
    this.messageFormatSupport.setNullString(nullString);
  }
}
