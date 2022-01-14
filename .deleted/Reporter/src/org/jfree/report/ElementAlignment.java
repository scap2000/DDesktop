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
 * ElementAlignment.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.jfree.report.util.ObjectStreamResolveException;

/**
 * Represents the alignment of an element.
 *
 * @author Thomas Morgner
 */
public final class ElementAlignment implements Serializable
{
  /**
   * A constant for left alignment.
   */
  public static final ElementAlignment LEFT = new ElementAlignment("LEFT");

  /**
   * A constant for center alignment (horizontal).
   */
  public static final ElementAlignment CENTER = new ElementAlignment("CENTER");

  /**
   * A constant for right alignment.
   */
  public static final ElementAlignment RIGHT = new ElementAlignment("RIGHT");

  /**
   * A constant for top alignment.
   */
  public static final ElementAlignment TOP = new ElementAlignment("TOP");

  /**
   * A constant for middle alignment (vertical).
   */
  public static final ElementAlignment MIDDLE = new ElementAlignment("MIDDLE");

  /**
   * A constant for bottom alignment.
   */
  public static final ElementAlignment BOTTOM = new ElementAlignment("BOTTOM");

  /**
   * The alignment name.
   */
  private final String myName; // for debug only
  /**
   * A cached hashcode.
   */
  private final int hashCode;

  /**
   * Creates a new alignment object.  Since this constructor is private, you cannot create
   * new alignment objects, you can only use the predefined constants.
   *
   * @param name the alignment name.
   */
  private ElementAlignment (final String name)
  {
    myName = name;
    hashCode = myName.hashCode();
  }

  /**
   * Returns the alignment name.
   *
   * @return the alignment name.
   */
  public String toString ()
  {
    return myName;
  }

  /**
   * Returns <code>true</code> if this object is equal to the specified object, and
   * <code>false</code> otherwise.
   *
   * @param o the other object.
   * @return A boolean.
   */
  public boolean equals (final Object o)
  {
    return (this == o);
  }

  /**
   * Returns a hash code for the alignment object.
   *
   * @return The code.
   */
  public int hashCode ()
  {
    return hashCode;
  }

  /**
   * Replaces the automatically generated instance with one of the enumeration instances.
   *
   * @return the resolved element
   *
   * @throws ObjectStreamException if the element could not be resolved.
   */
  private Object readResolve ()
          throws ObjectStreamException
  {
    if (this.myName.equals(ElementAlignment.LEFT.myName))
    {
      return ElementAlignment.LEFT;
    }
    if (this.myName.equals(ElementAlignment.RIGHT.myName))
    {
      return ElementAlignment.RIGHT;
    }
    if (this.myName.equals(ElementAlignment.CENTER.myName))
    {
      return ElementAlignment.CENTER;
    }
    if (this.myName.equals(ElementAlignment.TOP.myName))
    {
      return ElementAlignment.TOP;
    }
    if (this.myName.equals(ElementAlignment.BOTTOM.myName))
    {
      return ElementAlignment.BOTTOM;
    }
    if (this.myName.equals(ElementAlignment.MIDDLE.myName))
    {
      return ElementAlignment.MIDDLE;
    }
    // unknown element alignment...
    throw new ObjectStreamResolveException();
  }
}
