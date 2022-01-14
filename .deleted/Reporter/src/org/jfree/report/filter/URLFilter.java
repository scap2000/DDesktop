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
 * URLFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.filter;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import org.jfree.report.function.ExpressionRuntime;
import org.jfree.util.Log;

/**
 * The URLFilter forms URLs from Strings ,Files and URLs. If an URL is relative, the
 * missing contents can be obtained by a default url, called the baseURL.
 * <p/>
 *
 * @author Thomas Morgner
 */
public class URLFilter implements DataFilter
{
  /**
   * The datasource used to form the urls. This datasource should return strings, files or
   * urls
   */
  private DataSource source;

  /**
   * The base url is used to form the complete url if the given url is relative.
   *
   * @see java.net.URL#URL(java.net.URL, java.lang.String)
   */
  private URL baseURL;

  /**
   * DefaultConstructor.
   */
  public URLFilter ()
  {
  }

  /**
   * Returns the data source for the filter.
   *
   * @return The data source.
   */
  public DataSource getDataSource ()
  {
    return source;
  }

  /**
   * Sets the data source.
   *
   * @param ds The data source.
   */
  public void setDataSource (final DataSource ds)
  {
    if (ds == null)
    {
      throw new NullPointerException();
    }

    source = ds;
  }

  /**
   * Tries to form a url from the object returned from the datasource. This function will
   * return null if the datasource is null or returned null when getValue was called.
   * <p/>
   * Null is also returned if the datasources value is not an url, a String or a file. If
   * the creation of the url failed with an MalformedURLException or the datasource
   * returned a file which is not readable, also null is returned.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return created url or null if something went wrong on url creation.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    if (getDataSource() == null)
    {
      return null;
    }

    final Object o = getDataSource().getValue(runtime);
    if (o == null)
    {
      return null;
    }
    if (o instanceof URL)
    {
      return o;
    }

    try
    {
      if (o instanceof File)
      {
        final File f = (File) o;
        if (f.canRead())
        {
          return f.toURL();
        }
      }
      else if (o instanceof String)
      {
        if (getBaseURL() == null)
        {
          return new URL((String) o);
        }
        else
        {
          return new URL(getBaseURL(), (String) o);
        }
      }
    }
    catch (MalformedURLException mfe)
    {
      Log.info("URLFilter.getValue(): MalformedURLException!");
    }
    return null;

  }

  /**
   * Gets the base url used to make relative URLs absolute.
   *
   * @return the base url used to complete relative urls.
   */
  public URL getBaseURL ()
  {
    return baseURL;
  }

  /**
   * Defines the base url used to complete relative urls.
   *
   * @param baseURL the base URL.
   */
  public void setBaseURL (final URL baseURL)
  {
    this.baseURL = baseURL;
  }

  /**
   * Creates a clone of the URL filter.
   *
   * @return A clone.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final URLFilter f = (URLFilter) super.clone();
    if (source != null)
    {
      f.source = (DataSource) source.clone();
    }
    return f;
  }

}
