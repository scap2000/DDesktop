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
 * ToLowerCaseStringExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.strings;

import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.function.AbstractExpression;

/**
 * A expression that transforms all letters of a given string into lower-case letters.
 *
 * @author Thomas Morgner
 */
public class ToLowerCaseStringExpression extends AbstractExpression
{
  /**
   * The field name from where to read the string that should be converted to lower case.
   */
  private String field;

  /**
   * Default Constructor.
   */
  public ToLowerCaseStringExpression()
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
   * Transforms the string that has been read from the defined field.
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
    final ResourceBundleFactory rf = getResourceBundleFactory();
    if (rf == null)
    {
      return text.toLowerCase();
    }
    else
    {
      return text.toLowerCase(rf.getLocale());
    }
  }
}
