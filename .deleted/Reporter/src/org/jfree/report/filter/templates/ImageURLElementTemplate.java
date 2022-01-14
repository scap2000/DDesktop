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
 * ImageURLElementTemplate.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter.templates;

import java.net.URL;

import org.jfree.report.filter.ImageLoadFilter;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.filter.URLFilter;
import org.jfree.report.function.ExpressionRuntime;

/**
 * An image URL element template, which reads the image from a static URL.
 *
 * @author Thomas Morgner
 */
public class ImageURLElementTemplate extends AbstractTemplate
{
  /**
   * The image load filter.
   */
  private ImageLoadFilter imageLoadFilter;

  /**
   * A static datasource.
   */
  private StaticDataSource staticDataSource;

  /**
   * A URL filter.
   */
  private URLFilter urlFilter;

  /**
   * Creates a new template.
   */
  public ImageURLElementTemplate ()
  {
    staticDataSource = new StaticDataSource();
    urlFilter = new URLFilter();
    urlFilter.setDataSource(staticDataSource);
    imageLoadFilter = new ImageLoadFilter();
    imageLoadFilter.setDataSource(urlFilter);
  }

  /**
   * Sets the URL for the template.
   *
   * @param content the URL.
   */
  public void setContent (final String content)
  {
    staticDataSource.setValue(content);
  }

  /**
   * Returns the URL text for the template.
   *
   * @return The URL text.
   */
  public String getContent ()
  {
    return (String) (staticDataSource.getValue(null));
  }

  /**
   * Returns the base URL.
   *
   * @return The URL.
   */
  public URL getBaseURL ()
  {
    return urlFilter.getBaseURL();
  }

  /**
   * Sets the base URL.
   *
   * @param baseURL the URL.
   */
  public void setBaseURL (final URL baseURL)
  {
    urlFilter.setBaseURL(baseURL);
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
    return imageLoadFilter.getValue(runtime);
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
    final ImageURLElementTemplate template = (ImageURLElementTemplate) super.clone();
    template.imageLoadFilter = (ImageLoadFilter) imageLoadFilter.clone();
    template.urlFilter = (URLFilter) template.imageLoadFilter.getDataSource();
    template.staticDataSource = (StaticDataSource) template.urlFilter.getDataSource();
    return template;
  }

}
