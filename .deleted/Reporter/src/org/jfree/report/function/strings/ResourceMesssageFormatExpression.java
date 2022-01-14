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
 * ResourceMesssageFormatExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.strings;

import java.util.ResourceBundle;

import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.filter.MessageFormatSupport;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionUtilities;

/**
 * Formats a message read from a resource-bundle using named parameters. The parameters are resolved against the current
 * data-row.
 * <p/>
 * This performs the same task as the ResourceMessageFormatFilter does inside a text-element.
 *
 * @author Thomas Morgner
 */
public class ResourceMesssageFormatExpression extends AbstractExpression
{
  /**
   * The key that gets used to lookup the message format string from the resource bundle.
   */
  private String formatKey;

  /**
   * The name of the resource bundle used to lookup the message.
   */
  private String resourceIdentifier;

  /**
   * The message format support translates raw message strings into useable MessageFormat parameters and read the
   * necessary input data from the datarow.
   */
  private MessageFormatSupport messageFormatSupport;

  /**
   * Default constructor.
   */
  public ResourceMesssageFormatExpression()
  {
    messageFormatSupport = new MessageFormatSupport();
  }

  /**
   * Returns the name of the used resource bundle.
   *
   * @return the name of the resourcebundle
   * @see org.jfree.report.ResourceBundleFactory#getResourceBundle(String)
   */
  public String getResourceIdentifier()
  {
    return resourceIdentifier;
  }

  /**
   * Defines the name of the used resource bundle. If undefined, all calls to {@link
   * ResourceMesssageFormatExpression#getValue()} will result in <code>null</code> values.
   *
   * @param resourceIdentifier the resource bundle name
   */
  public void setResourceIdentifier(final String resourceIdentifier)
  {
    this.resourceIdentifier = resourceIdentifier;
  }

  /**
   * Defines the key that is used to lookup the format string used in the message format in the resource bundle.
   *
   * @param format a resourcebundle key for the message format lookup.
   */
  public void setFormatKey(final String format)
  {
    this.formatKey = format;
  }

  /**
   * Returns the key that is used to lookup the format string used in the message format in the resource bundle.
   *
   * @return the resource bundle key.
   */
  public String getFormatKey()
  {
    return formatKey;
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
    final ResourceBundle bundle;
    if (resourceIdentifier == null)
    {
      bundle = ExpressionUtilities.getDefaultResourceBundle(this);
    }
    else
    {
      bundle = resourceBundleFactory.getResourceBundle(resourceIdentifier);
    }

    final String newFormatString = bundle.getString(formatKey);
    messageFormatSupport.setFormatString(newFormatString);
    messageFormatSupport.setLocale(resourceBundleFactory.getLocale());
    return messageFormatSupport.performFormat(getDataRow());
  }
}
