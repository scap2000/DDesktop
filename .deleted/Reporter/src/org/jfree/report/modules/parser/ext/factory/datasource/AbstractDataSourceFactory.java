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
 * AbstractDataSourceFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.datasource;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryImpl;
import org.jfree.report.modules.parser.ext.factory.base.ObjectDescription;

/**
 * A base class for implementing the {@link DataSourceFactory} interface.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractDataSourceFactory
        extends ClassFactoryImpl implements DataSourceFactory
{
  /**
   * Storage for the data sources.
   */
  private final HashMap dataSources;

  /**
   * Creates a new factory.
   */
  protected AbstractDataSourceFactory ()
  {
    dataSources = new HashMap();
  }

  /**
   * Registers a data source.
   *
   * @param name the name.
   * @param o    the object description.
   */
  public void registerDataSources (final String name, final ObjectDescription o)
  {
    dataSources.put(name, o);
    registerClass(o.getObjectClass(), o);
  }

  /**
   * Returns a data source description.
   *
   * @param name the data source name.
   * @return The object description.
   */
  public ObjectDescription getDataSourceDescription (final String name)
  {
    final ObjectDescription od = (ObjectDescription) dataSources.get(name);
    if (od != null)
    {
      return od.getInstance();
    }
    return null;
  }

  /**
   * Returns a data source name given a description.
   *
   * @param od the object description.
   * @return The name.
   */
  public String getDataSourceName (final ObjectDescription od)
  {
    final Iterator keys = dataSources.keySet().iterator();
    while (keys.hasNext())
    {
      final String key = (String) keys.next();
      final ObjectDescription ds = (ObjectDescription) dataSources.get(key);
      if (ds.getObjectClass().equals(od.getObjectClass()))
      {
        return key;
      }
    }
    return null;
  }

  /**
   * Returns the names of all registered datasources as iterator.
   *
   * @return the registered names.
   */
  public Iterator getRegisteredNames ()
  {
    return dataSources.keySet().iterator();
  }
}
