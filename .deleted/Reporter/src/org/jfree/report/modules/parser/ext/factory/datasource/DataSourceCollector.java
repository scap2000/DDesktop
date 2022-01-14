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
 * DataSourceCollector.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.datasource;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.modules.parser.ext.factory.base.ClassFactory;
import org.jfree.report.modules.parser.ext.factory.base.ObjectDescription;
import org.jfree.util.ClassComparator;
import org.jfree.util.Configuration;

/**
 * A {@link DataSourceFactory} created from a number of other factories.
 *
 * @author Thomas Morgner
 */
public class DataSourceCollector implements DataSourceFactory
{
  /**
   * Storage for the factories.
   */
  private final ArrayList factories;
  /**
   * The comparator used to compare class instances.
   */
  private final ClassComparator comparator;
  /**
   * The parser/report configuration.
   */
  private Configuration config;

  /**
   * Creates a new factory.
   */
  public DataSourceCollector ()
  {
    factories = new ArrayList();
    comparator = new ClassComparator();
  }

  /**
   * Adds a factory to the collection.
   *
   * @param factory the factory.
   */
  public void addFactory (final DataSourceFactory factory)
  {
    factories.add(factory);
    if (getConfig() != null)
    {
      factory.configure(getConfig());
    }
  }

  /**
   * Returns an iterator that provides access to the factories.
   *
   * @return The iterator.
   */
  public Iterator getFactories ()
  {
    return factories.iterator();
  }

  /**
   * Returns a data source description.
   *
   * @param name the data source name.
   * @return The description.
   */
  public ObjectDescription getDataSourceDescription (final String name)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      final DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      final ObjectDescription o = fact.getDataSourceDescription(name);
      if (o != null)
      {
        return o.getInstance();
      }
    }
    return null;
  }

  /**
   * Returns a data source name.
   *
   * @param od the object description.
   * @return The name.
   */
  public String getDataSourceName (final ObjectDescription od)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      final DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      final String o = fact.getDataSourceName(od);
      if (o != null)
      {
        return o;
      }
    }
    return null;
  }

  /**
   * Returns a description for the class.
   *
   * @param c the class.
   * @return The description.
   */
  public ObjectDescription getDescriptionForClass (final Class c)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      final DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      final ObjectDescription o = fact.getDescriptionForClass(c);
      if (o != null)
      {
        return o.getInstance();
      }
    }
    return null;
  }

  /**
   * Returns a description for the super class.
   *
   * @param d               the class.
   * @param knownSuperClass the last known super class for the given class or null if none
   *                        was found yet.
   * @return The object description suitable to create instances of the given class d.
   */
  public ObjectDescription getSuperClassObjectDescription
          (final Class d, ObjectDescription knownSuperClass)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      final DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      final ObjectDescription od = fact.getSuperClassObjectDescription(d, knownSuperClass);
      if (od == null)
      {
        continue;
      }
      if (knownSuperClass == null)
      {
        knownSuperClass = od;
      }
      else
      {
        if (comparator.isComparable(knownSuperClass.getObjectClass(), od.getObjectClass())
                && (comparator.compare(knownSuperClass.getObjectClass(), od.getObjectClass()) < 0))
        {
          knownSuperClass = od;
        }
      }
    }
    if (knownSuperClass != null)
    {
      return knownSuperClass.getInstance();
    }
    return null;
  }

  /**
   * Returns an iterator that provides access to the registered classes.
   *
   * @return The iterator.
   */
  public Iterator getRegisteredClasses ()
  {
    final ArrayList list = new ArrayList();
    for (int i = 0; i < factories.size(); i++)
    {
      final ClassFactory f = (ClassFactory) factories.get(i);
      final Iterator keys = f.getRegisteredClasses();
      while (keys.hasNext())
      {
        list.add(keys.next());
      }
    }
    return list.iterator();
  }

  /**
   * Configures this factory. The configuration contains several keys and their defined
   * values. The given reference to the configuration object will remain valid until the
   * report parsing or writing ends.
   * <p/>
   * The configuration contents may change during the reporting.
   *
   * @param config the configuration, never null
   */
  public void configure (final Configuration config)
  {
    if (config == null)
    {
      throw new NullPointerException("The given configuration is null");
    }
    if (this.config != null)
    {
      // already configured ... ignored
      return;
    }

    this.config = config;
    final Iterator it = factories.iterator();
    while (it.hasNext())
    {
      final DataSourceFactory od = (DataSourceFactory) it.next();
      od.configure(config);
    }

  }

  /**
   * Returns the currently set configuration or null, if none was set.
   *
   * @return the configuration.
   */
  public Configuration getConfig ()
  {
    return config;
  }

  /**
   * Returns the names of all registered datasources as iterator.
   *
   * @return the registered names.
   */
  public Iterator getRegisteredNames ()
  {
    return new ArrayList().iterator();
  }
}
