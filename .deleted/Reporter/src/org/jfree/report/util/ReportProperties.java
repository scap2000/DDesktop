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
 * ReportProperties.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * The report properties is a hashtable with string keys. ReportProperties are bound to a
 * report as a general purpose storage. ReportProperties bound to a JFreeReport object are
 * visible to all generated report-state chains. A ReportState will inherit all
 * ReportProperties bound to the JFreeReport-object when the ReportState.StartState object
 * is created.  Properties bound to the report definition after the report state is
 * created are not visible to the ReportState and its children.
 * <p/>
 * ReportProperties bound to a ReportState are not visible to the report definition (the
 * JFreeReport object), but are visible to all ReportStates of that ReportState-chain. So
 * when you add a property at the end of a report run to a ReportState, the value of this
 * property will be visible to all ReportStates when the report is restarted at a certain
 * point.
 * <p/>
 * ReportProperties can be seen as a stateless shared report internal storage area. All
 * functions have access to the properties by using the ReportState.getProperty() and
 * ReportState.setProperty() functions.
 * <p/>
 * For a list of defined default properties, have a look at the
 * {@link org.jfree.report.JFreeReport} class.
 *
 * @author Thomas Morgner
 */
public class ReportProperties implements Serializable, Cloneable
{
  /**
   * Storage for the properties.
   */
  private HashMap properties;

  /**
   * The fall-back property-collection.
   */
  private ReportProperties masterProperties;

  /**
   * Copy constructor.
   *
   * @param props an existing ReportProperties instance.
   */
  public ReportProperties (final ReportProperties props)
  {
    this.properties = new HashMap(props.properties);
  }

  /**
   * Default constructor.
   */
  public ReportProperties ()
  {
    this.properties = new HashMap();
  }

  /**
   * Adds a property to this properties collection. If a property with the given name
   * exist, the property will be replaced with the new value. If the value is null, the
   * property will be removed.
   *
   * @param key   the property key.
   * @param value the property value.
   */
  public void put (final String key, final Object value)
  {
    if (key == null)
    {
      throw new NullPointerException
              ("ReportProperties.put (..): Parameter 'key' must not be null");
    }
    if (value == null)
    {
      this.properties.remove(key);
    }
    else
    {
      this.properties.put(key, value);
    }
  }

  /**
   * Retrieves the value stored for a key in this properties collection.
   *
   * @param key the property key.
   * @return The stored value, or <code>null</code> if the key does not exist in this
   *         collection.
   */
  public Object get (final String key)
  {
    if (key == null)
    {
      throw new NullPointerException
              ("ReportProperties.get (..): Parameter 'key' must not be null");
    }
    return get(key, null);
  }

  /**
   * Retrieves the value stored for a key in this properties collection, and returning the
   * default value if the key was not stored in this properties collection.
   *
   * @param key          the property key.
   * @param defaultValue the default value to be returned when the key is not stored in
   *                     this properties collection.
   * @return The stored value, or the default value if the key does not exist in this
   *         collection.
   */
  public Object get (final String key, final Object defaultValue)
  {
    if (key == null)
    {
      throw new NullPointerException
              ("ReportProperties.get (..): Parameter 'key' must not be null");
    }
    final Object o = this.properties.get(key);
    if (masterProperties != null)
    {
      return masterProperties.get(key, defaultValue);
    }

    if (o == null)
    {
      return defaultValue;
    }
    return o;
  }

  /**
   * Returns all property keys as enumeration.
   *
   * @return an enumeration of the property keys.
   */
  public Iterator keys ()
  {
    final TreeSet list = new TreeSet();
    list.addAll(this.properties.keySet());

    return list.iterator();
  }

  /**
   * Removes all properties stored in this collection.
   */
  public void clear ()
  {
    this.properties.clear();
  }

  /**
   * Checks whether the given key is stored in this collection of ReportProperties.
   *
   * @param key the property key.
   * @return true, if the given key is known.
   */
  public boolean containsKey (final String key)
  {
    if (key == null)
    {
      throw new NullPointerException
              ("ReportProperties.containsKey (..): Parameter key must not be null");
    }
    return this.properties.containsKey(key);
  }

  /**
   * Clones the properties.
   *
   * @return a copy of this ReportProperties object.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final ReportProperties p = (ReportProperties) super.clone();
    p.properties = (HashMap) this.properties.clone();
    p.masterProperties = null;
    return p;
  }

  /**
   * Marks a property.
   *
   * @param property the property key.
   * @param marked   boolean.
   * @deprecated marking is no longer necessary.
   */
  public void setMarked (final String property, final boolean marked)
  {
  }

  /**
   * Returns true if the specified property is marked, and false otherwise.
   *
   * @param property the property key.
   * @return true for marked properties, false otherwise.
   * @deprecated marking is no longer necessary.
   */
  public boolean isMarked (final String property)
  {
    return true;
  }

  /**
   * Returns true, if there is at least one marked property.
   *
   * @return true, if there are some properties marked, false otherwise.
   */
  public boolean containsMarkedProperties ()
  {
    return true;
  }

  /**
   * Returns the fall-back property-collection. If defined, this collection will be used if a queried property is not
   * defined in this collection.
   *
   * @return the fall-back collection.
   */
  public ReportProperties getMasterProperties()
  {
    return masterProperties;
  }

  /**
   * Defines the fall-back property-collection. If defined, this collection will be used if a queried property is not
   * defined in this collection.
   *
   * @param masterProperties the fall-back collection.
   */
  public void setMasterProperties(final ReportProperties masterProperties)
  {
    this.masterProperties = masterProperties;
  }

  /**
   * Marks a property. Marking was a historical process that made the property available in the report's datarow. Since
   * version 0.8.9, all properties are part of the data-row by default. 
   *
   * @deprecated marking is no longer necessary.
   * @return all keys
   */
  public Iterator markedKeys()
  {
    return keys();
  }

  /**
   * Returns all defined keys as string-array.
   *
   * @return the keys as array.
   */
  public String[] keyArray()
  {
    return (String[]) properties.keySet().toArray(new String[properties.size()]);
  }

  /**
   * Returns the number of entries in this collection.
   *
   * @return the number of properties defined here.
   */
  public int size()
  {
    return properties.size();
  }
}
