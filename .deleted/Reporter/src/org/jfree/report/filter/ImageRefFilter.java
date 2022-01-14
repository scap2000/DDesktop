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
 * ImageRefFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter;

import java.awt.Image;

import java.io.IOException;

import org.jfree.report.DefaultImageReference;
import org.jfree.report.ImageContainer;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.util.Log;

/**
 * A filter that converts an Image to an ImageReference. The DataSource is expected to
 * contain an java.awt.Image, the image is then wrapped into an ImageReference and this
 * ImageReference is returned to the caller.
 *
 * @author Thomas Morgner
 */
public class ImageRefFilter implements DataFilter
{
  /**
   * Default constructor.
   */
  public ImageRefFilter ()
  {
  }

  /**
   * The data source.
   */
  private DataSource dataSource;

  /**
   * Returns the data source for the filter.
   *
   * @return The data source.
   */
  public DataSource getDataSource ()
  {
    return dataSource;
  }

  /**
   * Sets the data source for the filter.
   *
   * @param dataSource The data source.
   */
  public void setDataSource (final DataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  /**
   * Returns the current value for the data source. <P> The returned object, unless it is
   * null, will be an instance of ImageReference.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return The value.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    final DataSource ds = getDataSource();
    if (ds == null)
    {
      return null;
    }
    final Object o = ds.getValue(runtime);
    if (o instanceof ImageContainer)
    {
      return o;
    }
    if (o == null || (o instanceof Image) == false)
    {
      return null;
    }

    try
    {
      return new DefaultImageReference((Image) o);
    }
    catch (IOException e)
    {
      Log.warn ("Unable to fully load a given image.", e);
      return null;
    }
  }

  /**
   * Clones the filter.
   *
   * @return A clone of this filter.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final ImageRefFilter r = (ImageRefFilter) super.clone();
    if (dataSource != null)
    {
      r.dataSource = (DataSource) dataSource.clone();
    }
    return r;
  }

}
