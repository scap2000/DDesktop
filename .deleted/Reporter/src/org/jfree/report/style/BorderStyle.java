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
 * BorderStyle.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.style;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.jfree.report.util.ObjectStreamResolveException;

/**
 * Creation-Date: 30.10.2005, 19:37:35
 *
 * @author Thomas Morgner
 */
public class BorderStyle implements Serializable
{
  public static final BorderStyle NONE = new BorderStyle("none");
  public static final BorderStyle HIDDEN = new BorderStyle("hidden");
  public static final BorderStyle DOTTED = new BorderStyle("dotted");
  public static final BorderStyle DASHED = new BorderStyle("dashed");
  public static final BorderStyle SOLID = new BorderStyle("solid");
  public static final BorderStyle DOUBLE = new BorderStyle("double");
  public static final BorderStyle DOT_DASH = new BorderStyle("dot-dash");
  public static final BorderStyle DOT_DOT_DASH = new BorderStyle("dot-dot-dash");
  public static final BorderStyle WAVE = new BorderStyle("wave");
  public static final BorderStyle GROOVE = new BorderStyle("groove");
  public static final BorderStyle RIDGE = new BorderStyle("ridge");
  public static final BorderStyle INSET = new BorderStyle("inset");
  public static final BorderStyle OUTSET = new BorderStyle("outset");
  private String type;

  private BorderStyle(final String type)
  {
    this.type = type;
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

    final BorderStyle that = (BorderStyle) o;

    if (type != null ? !type.equals(that.type) : that.type != null)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return (type != null ? type.hashCode() : 0);
  }

  public String toString()
  {
    return type;
  }

  /**
   * Replaces the automatically generated instance with one of the enumeration instances.
   *
   * @return the resolved element
   *
   * @throws java.io.ObjectStreamException if the element could not be resolved.
   * @noinspection UNUSED_SYMBOL
   */
  private Object readResolve ()
          throws ObjectStreamException
  {
    if (this.type.equals(BorderStyle.NONE.type))
    {
      return BorderStyle.NONE;
    }
    if (this.type.equals(BorderStyle.DASHED.type))
    {
      return BorderStyle.DASHED;
    }
    if (this.type.equals(BorderStyle.DOT_DASH.type))
    {
      return BorderStyle.DOT_DASH;
    }
    if (this.type.equals(BorderStyle.DOT_DOT_DASH.type))
    {
      return BorderStyle.DOT_DOT_DASH;
    }
    if (this.type.equals(BorderStyle.DOTTED.type))
    {
      return BorderStyle.DOTTED;
    }
    if (this.type.equals(BorderStyle.DOUBLE.type))
    {
      return BorderStyle.DOUBLE;
    }
    if (this.type.equals(BorderStyle.GROOVE.type))
    {
      return BorderStyle.GROOVE;
    }
    if (this.type.equals(BorderStyle.HIDDEN.type))
    {
      return BorderStyle.HIDDEN;
    }
    if (this.type.equals(BorderStyle.INSET.type))
    {
      return BorderStyle.INSET;
    }
    if (this.type.equals(BorderStyle.NONE.type))
    {
      return BorderStyle.NONE;
    }
    if (this.type.equals(BorderStyle.OUTSET.type))
    {
      return BorderStyle.OUTSET;
    }
    if (this.type.equals(BorderStyle.RIDGE.type))
    {
      return BorderStyle.RIDGE;
    }
    if (this.type.equals(BorderStyle.SOLID.type))
    {
      return BorderStyle.SOLID;
    }
    if (this.type.equals(BorderStyle.WAVE.type))
    {
      return BorderStyle.WAVE;
    }
    // unknown element alignment...
    throw new ObjectStreamResolveException();
  }

}
