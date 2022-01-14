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
 * ElementFactoryCollector.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.elements;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.Element;

/**
 * An element factory that encapsulates multiple element factories.
 *
 * @author Thomas Morgner
 */
public class ElementFactoryCollector implements ElementFactory
{
  /**
   * Storage for the element factories.
   */
  private final ArrayList factories;

  /**
   * Creates a new element factory.
   */
  public ElementFactoryCollector ()
  {
    factories = new ArrayList();
  }

  /**
   * Adds an element factory.
   *
   * @param factory the factory.
   */
  public void addFactory (final ElementFactory factory)
  {
    factories.add(factory);
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
   * Returns an element for the given type.
   *
   * @param type the content type.
   * @return The element.
   */
  public Element getElementForType (final String type)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      final ElementFactory fact = (ElementFactory) factories.get(i);
      final Element element = fact.getElementForType(type);
      if (element != null)
      {
        return element;
      }
    }
    return null;
  }

  /**
   * Indicated whether an other object is equal to this one.
   *
   * @param o the other object.
   * @return true, if the object is equal, false otherwise.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof ElementFactoryCollector))
    {
      return false;
    }

    final ElementFactoryCollector elementFactoryCollector = (ElementFactoryCollector) o;

    if (!factories.equals(elementFactoryCollector.factories))
    {
      return false;
    }

    return true;
  }

  /**
   * Computes an hashcode for this factory.
   *
   * @return the hashcode.
   *
   * @see java.lang.Object#hashCode()
   */
  public int hashCode ()
  {
    return factories.hashCode();
  }
}
