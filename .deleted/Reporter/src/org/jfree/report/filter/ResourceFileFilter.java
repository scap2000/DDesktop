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
 * ResourceFileFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.filter;

import java.util.ResourceBundle;

import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.util.Log;

/**
 * Lookup a key from a datasource using a ResourceBundle.
 * <p/>
 * Filters a given datasource and uses the datasource value as key for a ResourceBundle.
 *
 * @author Thomas Morgner
 */
public class ResourceFileFilter implements DataFilter
{
  /**
   * the used resource bundle.
   */
  private String resourceIdentifier;

  /**
   * the filtered data source.
   */
  private DataSource dataSource;

  /**
   * Creates a new ResourceFileFilter.
   */
  public ResourceFileFilter()
  {
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
   * org.jfree.report.filter.ResourceFileFilter#getValue(ExpressionRuntime)} will result in <code>null</code> values.
   *
   * @param resourceIdentifier the resource bundle name
   */
  public void setResourceIdentifier(final String resourceIdentifier)
  {
    this.resourceIdentifier = resourceIdentifier;
  }

  /**
   * Returns the current value for the data source. The method will return null, if no datasource or no resource bundle
   * is defined or if the datasource's value is null.
   * <p/>
   * The value read from the dataSource is looked up in the given resourcebundle using the
   * <code>ResourceBundle.getObject()</code> method. If the lookup fails, null is returned.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return the value or null, if the value could not be looked up.
   */
  public Object getValue(final ExpressionRuntime runtime)
  {
    if (dataSource == null)
    {
      return null;
    }
    if (runtime == null)
    {
      return null;
    }
    final Object value = dataSource.getValue(runtime);
    if (value == null)
    {
      return null;
    }
    final String svalue = String.valueOf(value);

    try
    {
      final String resourceId;
      if (resourceIdentifier != null)
      {
        resourceId = resourceIdentifier;
      }
      else
      {
        resourceId = runtime.getConfiguration().getConfigProperty
            (ResourceBundleFactory.DEFAULT_RESOURCE_BUNDLE_CONFIG_KEY);
      }

      if (resourceId == null)
      {
        return null;
      }

      final ResourceBundleFactory resourceBundleFactory =
          runtime.getResourceBundleFactory();
      final ResourceBundle bundle =
          resourceBundleFactory.getResourceBundle(resourceId);
      if (bundle != null)
      {
        return bundle.getObject(svalue);
      }
    }
    catch (Exception e)
    {
      // on errors return null.
      Log.info(new Log.SimpleMessage("Failed to retrive the value for key ", svalue));
    }
    return null;
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
    final ResourceFileFilter filter = (ResourceFileFilter) super.clone();
    filter.dataSource = (DataSource) dataSource.clone();
    return filter;
  }

  /**
   * Returns the assigned DataSource for this Target.
   *
   * @return The datasource.
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Assigns a DataSource for this Target.
   *
   * @param ds The data source.
   */
  public void setDataSource(final DataSource ds)
  {
    this.dataSource = ds;
  }

}
