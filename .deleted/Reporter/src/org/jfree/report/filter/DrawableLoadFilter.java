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
 * DrawableLoadFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.filter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.URL;

import java.sql.Blob;
import java.sql.SQLException;

import java.util.HashSet;

import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.resourceloader.DrawableFactory;
import org.jfree.report.util.KeyedQueue;
import org.jfree.util.Log;

/**
 * The DrawableLoadFilter is used to load drawable image files (like WMF's) during the
 * report generation process. This filter expects its datasource to return a java.net.URL.
 * If the datasource does not return an URL, <code>null</code> is returned as result of
 * calling "getValue()".
 * <p/>
 * This filter is mostly used in conjunction with the URLFilter, which creates URLs from
 * Strings and files if nessesary.
 * <p/>
 * The url is used to create a new Drawable object which is returned to the caller. The
 * loaded/created Drawable is also stored in an internal cache.
 * <p/>
 * This filter can be used to dynamically change images of a report, a very nice feature
 * for photo albums and catalogs for instance.
 * <p/>
 * This filter will return null, if something else than an URL was retrieved from the
 * assigned datasource
 *
 * @author Thomas Morgner
 */
public class DrawableLoadFilter implements DataFilter
{
  /**
   * the cache for previously loaded images. If the maximum size of the cache reached,
   */
  private transient KeyedQueue imageCache;
  private transient HashSet failureCache;
  /**
   * The datasource from where to read the urls.
   */
  private DataSource source;

  /**
   * creates a new ImageLoadFilter with a cache size of 10.
   */
  public DrawableLoadFilter ()
  {
    this(10);
  }

  /**
   * Creates a new ImageLoadFilter with the defined cache size.
   *
   * @param cacheSize the cache size.
   */
  public DrawableLoadFilter (final int cacheSize)
  {
    imageCache = new KeyedQueue(cacheSize);
    failureCache = new HashSet();
  }

  /**
   * Reads this filter's datasource and if the source returned an URL, tries to form a
   * imagereference. If the image is loaded in a previous run and is still in the cache,
   * no new reference is created and the previously loaded reference is returned.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return the current value for this filter.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    final DataSource ds = getDataSource();
    if (ds == null)
    {
      return null;
    }
    final Object o = ds.getValue(runtime);
    if (o == null)
    {
      return null;
    }

    if (o instanceof URL)
    {

      // a valid url is found, lookup the url in the cache, maybe the image is loaded and
      // still there.
      final URL url = (URL) o;
      final String urlString = String.valueOf(url);
      Object retval = imageCache.get(urlString);
      if (retval == null)
      {
        if (failureCache.contains(urlString))
        {
          return null;
        }
        try
        {
          retval = DrawableFactory.getInstance().createDrawable(url);
        }
        catch (IOException ioe)
        {
          if (Log.isWarningEnabled())
          {
            Log.warn ("Error while loading the drawable from " + url);
          }
          else if (Log.isDebugEnabled())
          {
            Log.debug("Error while loading the drawable from " + url, ioe);
          }
          failureCache.add(urlString);
          return null;
        }
        if (retval == null)
        {
          return null;
        }
      }
      // update the cache and put the image at the top of the list
      imageCache.put(urlString, retval);
      return retval;
    }
    else if (o instanceof byte[])
    {
      try
      {
        final byte[] data = (byte[]) o;
        return DrawableFactory.getInstance().createDrawable(data, null, null);
       }
      catch (IOException e)
      {
        Log.warn("Error while loading the image from an byte-array", e);
      }
      return null;

    }
    else if (o instanceof Blob)
    {
      try
      {
        final Blob b = (Blob) o;
        final byte[] data = b.getBytes(0,(int) b.length());
        return DrawableFactory.getInstance().createDrawable(data, null, null);
      }
      catch (SQLException e)
      {
        Log.warn("Error while loading the image from an blob", e);
      }
      catch (IOException e)
      {
        Log.warn("Error while loading the image from an blob", e);
      }
      return null;
    }
    else
    {
      return null;
    }
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
   * Clones the filter.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final DrawableLoadFilter il = (DrawableLoadFilter) super.clone();
    il.imageCache = (KeyedQueue) imageCache.clone();
    il.failureCache = (HashSet) failureCache.clone();
    if (source != null)
    {
      il.source = (DataSource) source.clone();
    }
    return il;
  }


  /**
   * A helper method that is called during the serialization process.
   *
   * @param out the serialization output stream.
   * @throws IOException if an IO error occured.
   */
  private void writeObject(final ObjectOutputStream out)
     throws IOException
  {
    out.defaultWriteObject();
    out.writeInt(imageCache.getLimit());
  }

  /**
   * A helper method that is called during the de-serialization process.
   *
   * @param in the serialization input stream.
   * @throws IOException if an IOError occurs.
   * @throws ClassNotFoundException if a dependent class cannot be found.
   */
  private void readObject(final ObjectInputStream in)
     throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    final int limit = in.readInt();
    imageCache = new KeyedQueue(limit);
    failureCache = new HashSet();
  }

}
