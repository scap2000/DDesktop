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
 * Anchor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report;

import java.io.Serializable;

/**
 * An anchor is a possible target for external hyperlinks.
 * <p/>
 * In HTML anchors would be produced by using &lt;a name=&quot;anchorname&quot;&gt;. This
 * class is immutable. 
 *
 * @author Thomas Morgner
 * @see AnchorElement
 * @deprecated Ancors should not be created this way. Add a Anchor-Style-Expression instead.
 */
public class Anchor implements Serializable
{
  /** Unique identifier for long-term persistence. */
  private static final long serialVersionUID = 8495721791372012478L;

  /**
   * The name of the anchor. Should be unique within the report.
   */
  private String name;

  /**
   * Creates a new anchor with the given name.
   *
   * @param name the name of the anchor.
   * @throws NullPointerException if the given name is null.
   */
  public Anchor (final String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.name = name;
  }

  /**
   * Returns the name of the anchor.
   *
   * @return the name
   */
  public String getName ()
  {
    return name;
  }

  /**
   * Checks, whether the given object is an anchor with the same name as this one.
   *
   * @param obj the other object.
   * @return true, if the object is equal to this one, false otherwise.
   */
  public boolean equals (final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (!(obj instanceof Anchor))
    {
      return false;
    }

    final Anchor anchor = (Anchor) obj;

    if (!name.equals(anchor.name))
    {
      return false;
    }

    return true;
  }

  /**
   * Computes a hashcode for this anchor.
   *
   * @return the hashcode.
   */
  public int hashCode ()
  {
    return name.hashCode();
  }
}
