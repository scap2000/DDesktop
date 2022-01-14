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
 * ResourceBundleLookupExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.strings;

import java.util.ResourceBundle;

import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionUtilities;

/**
 * Performs a resource-bundle lookup using the value read from the defined field as key in the resource-bundle. This
 * expression behaves like the Resource-field.
 *
 * @author Thomas Morgner
 */
public class ResourceBundleLookupExpression extends AbstractExpression
{
  /**
   * The field from where to read the key value.
   */
  private String field;

  /**
   * The name of the used resource bundle. If null, the default resource-bundle will be used instead.
   */
  private String resourceIdentifier;

  /**
   * Default Constructor.
   */
  public ResourceBundleLookupExpression()
  {
  }

  /**
     * Returns the name of the datarow-destColumn from where to read the resourcebundle key value.
     *
     * @return the field.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Defines the name of the datarow-destColumn from where to read the resourcebundle key value.
     *
     * @param field the field.
     */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Returns the name of the resource-bundle. If none is defined here, the default resource-bundle is used instead.
   *
   * @return the resource-bundle identifier.
   */
  public String getResourceIdentifier()
  {
    return resourceIdentifier;
  }

  /**
   * Defines name of the resource-bundle. If none is defined here, the default resource-bundle is used instead.
   *
   * @param resourceIdentifier the resource-bundle identifier.
   */
  public void setResourceIdentifier(final String resourceIdentifier)
  {
    this.resourceIdentifier = resourceIdentifier;
  }


  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    final Object key = getDataRow().get(getField());
    if (key == null)
    {
      return null;
    }

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
    return bundle.getObject(String.valueOf(key));
  }
}
