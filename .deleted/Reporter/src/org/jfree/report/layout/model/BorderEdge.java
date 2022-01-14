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
 * BorderEdge.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import java.awt.Color;

import org.jfree.report.style.BorderStyle;

/**
 * Creation-Date: 03.04.2007, 13:54:41
 *
 * @author Thomas Morgner
 */

/**
 * Creation-Date: 23.06.2006, 16:54:04
 *
 * @author Thomas Morgner
 */
public class BorderEdge
{
  public static final BorderEdge EMPTY = new BorderEdge(BorderStyle.NONE, Color.black, 0);

  /**
   * One of the styles defined in org.jfree.layouting.input.style.keys.border.BorderStyle.
   */
  private BorderStyle borderStyle;
  private Color color;
  private long width;

  public BorderEdge(final BorderStyle borderStyle, final Color color, final long width)
  {
    if (borderStyle == null)
    {
      throw new NullPointerException();
    }
    if (color == null)
    {
      throw new NullPointerException();
    }

    this.borderStyle = borderStyle;
    this.color = color;
    this.width = width;

    if (BorderStyle.NONE.equals(borderStyle))
    {
      this.width = 0;
    }
  }

  public BorderStyle getBorderStyle()
  {
    return borderStyle;
  }

  public Color getColor()
  {
    return color;
  }

  public long getWidth()
  {
    return width;
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

    final BorderEdge that = (BorderEdge) o;

    if (width != that.width)
    {
      return false;
    }
    if (!borderStyle.equals(that.borderStyle))
    {
      return false;
    }
    if (!color.equals(that.color))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = borderStyle.hashCode();
    result = 29 * result + color.hashCode();
    result = 29 * result + (int) (width ^ (width >>> 32));
    return result;
  }

  public String toString()
  {
    return "BorderEdge{" +
        "borderStyle='" + borderStyle + '\'' +
        ", color=" + color +
        ", width=" + width +
        '}';
  }
}
