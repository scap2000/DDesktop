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
 * StyleChangeSupport.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.style;

import java.lang.ref.WeakReference;

import java.util.ArrayList;

/**
 * A utility class for managing a collection of {@link StyleChangeListener} objects.
 *
 * @author Thomas Morgner.
 */
public class StyleChangeSupport
{
  /**
   * Storage for the listeners.
   */
  private ArrayList listeners;

  /**
   * The source.
   */
  private final ElementStyleSheet source;

  /**
   * Creates a new support object.
   *
   * @param source the source of change events.
   */
  public StyleChangeSupport (final ElementStyleSheet source)
  {
    this.source = source;
  }

  /**
   * Adds a listener.
   *
   * @param l the listener.
   */
  public void addListener (final StyleChangeListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      listeners = new ArrayList(5);
    }
    listeners.add(new WeakReference(l));
  }

  /**
   * Removes a listener.
   *
   * @param l the listener.
   */
  public void removeListener (final StyleChangeListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      return;
    }
    listeners.remove(l);
  }

  /**
   * Notifies all listeners that a style has changed.
   *
   * @param key   the style key.
   * @param value the new style value.
   */
  public void fireStyleChanged (final StyleKey key, final Object value)
  {
    if (listeners == null)
    {
      return;
    }
    ArrayList removeList = null;

    for (int i = 0; i < listeners.size(); i++)
    {
      final WeakReference ref = (WeakReference) listeners.get(i);
      final StyleChangeListener l = (StyleChangeListener) ref.get();
      if (l != null)
      {
        l.styleChanged(source, key, value);
      }
      else
      {
        if (removeList == null)
        {
          removeList = new ArrayList(5);
        }
        removeList.add(ref);
      }
    }
    if (removeList != null)
    {
      listeners.removeAll(removeList);
    }
  }

  /**
   * Notifies all listeners that a style has been removed.
   *
   * @param key the style key.
   */
  public void fireStyleRemoved (final StyleKey key)
  {
    if (listeners == null)
    {
      return;
    }
    ArrayList removeList = null;

    for (int i = 0; i < listeners.size(); i++)
    {
      final WeakReference ref = (WeakReference) listeners.get(i);
      final StyleChangeListener l = (StyleChangeListener) ref.get();
      if (l != null)
      {
        l.styleRemoved(source, key);
      }
      else
      {
        if (removeList == null)
        {
          removeList = new ArrayList(5);
        }
        removeList.add(ref);
      }
    }
    if (removeList != null)
    {
      listeners.removeAll(removeList);
    }
  }
}
