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
 * BorderCorner.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

/**
 * Creation-Date: 03.04.2007, 13:57:52
 *
 * @author Thomas Morgner
 */
public class BorderCorner
{
  public static final BorderCorner EMPTY = new BorderCorner(0,0);

  private long width;
  private long height;

  public BorderCorner(final long width, final long height)
  {
    this.width = width;
    this.height = height;
  }

  public long getWidth()
  {
    return width;
  }

  public long getHeight()
  {
    return height;
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

    final BorderCorner that = (BorderCorner) o;

    if (height != that.height)
    {
      return false;
    }
    if (width != that.width)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = (int) (width ^ (width >>> 32));
    result = 29 * result + (int) (height ^ (height >>> 32));
    return result;
  }

  public String toString()
  {
    return "BorderCorner{" +
        "width=" + width +
        ", height=" + height +
        '}';
  }
}

