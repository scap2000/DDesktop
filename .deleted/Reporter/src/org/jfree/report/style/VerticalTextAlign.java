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
 * VerticalAlign.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.style;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.jfree.report.util.ObjectStreamResolveException;

/**
 * Creation-Date: 24.11.2005, 17:08:01
 *
 * @author Thomas Morgner
 */
public class VerticalTextAlign implements Serializable
{
  public static final VerticalTextAlign USE_SCRIPT =
          new VerticalTextAlign("use-script");
  public static final VerticalTextAlign BASELINE =
          new VerticalTextAlign("baseline");
  public static final VerticalTextAlign SUB =
          new VerticalTextAlign("sub");
  public static final VerticalTextAlign SUPER =
          new VerticalTextAlign("super");

  public static final VerticalTextAlign TOP =
          new VerticalTextAlign("top");
  public static final VerticalTextAlign TEXT_TOP =
          new VerticalTextAlign("text-top");
  public static final VerticalTextAlign CENTRAL =
          new VerticalTextAlign("central");
  public static final VerticalTextAlign MIDDLE =
          new VerticalTextAlign("middle");
  public static final VerticalTextAlign BOTTOM =
          new VerticalTextAlign("bottom");
  public static final VerticalTextAlign TEXT_BOTTOM =
          new VerticalTextAlign("text-bottom");
  private String id;

  private VerticalTextAlign (final String id)
  {
    this.id = id;
  }

  /**
   * Replaces the automatically generated instance with one of the enumeration instances.
   *
   * @return the resolved element
   *
   * @throws java.io.ObjectStreamException if the element could not be resolved.
   */
  private Object readResolve ()
          throws ObjectStreamException
  {
    if (this.id.equals(VerticalTextAlign.USE_SCRIPT.id))
    {
      return VerticalTextAlign.USE_SCRIPT;
    }
    if (this.id.equals(VerticalTextAlign.BASELINE.id))
    {
      return VerticalTextAlign.BASELINE;
    }
    if (this.id.equals(VerticalTextAlign.SUPER.id))
    {
      return VerticalTextAlign.SUPER;
    }
    if (this.id.equals(VerticalTextAlign.SUB.id))
    {
      return VerticalTextAlign.SUB;
    }
    if (this.id.equals(VerticalTextAlign.TOP.id))
    {
      return VerticalTextAlign.TOP;
    }
    if (this.id.equals(VerticalTextAlign.TEXT_TOP.id))
    {
      return VerticalTextAlign.TEXT_TOP;
    }
    if (this.id.equals(VerticalTextAlign.BOTTOM.id))
    {
      return VerticalTextAlign.BOTTOM;
    }
    if (this.id.equals(VerticalTextAlign.TEXT_BOTTOM.id))
    {
      return VerticalTextAlign.TEXT_BOTTOM;
    }
    if (this.id.equals(VerticalTextAlign.CENTRAL.id))
    {
      return VerticalTextAlign.CENTRAL;
    }
    if (this.id.equals(VerticalTextAlign.MIDDLE.id))
    {
      return VerticalTextAlign.MIDDLE;
    }
    // unknown element alignment...
    throw new ObjectStreamResolveException();
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final VerticalTextAlign that = (VerticalTextAlign) o;

    if (!id.equals(that.id))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return id.hashCode();
  }
}
