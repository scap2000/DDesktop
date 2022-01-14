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
 * Border.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

/**
 * Creation-Date: 03.04.2007, 13:54:17
 *
 * @author Thomas Morgner
 */
public class Border implements Cloneable
{
  public static final Border EMPTY_BORDER =
      new Border(BorderEdge.EMPTY, BorderEdge.EMPTY, BorderEdge.EMPTY, BorderEdge.EMPTY, BorderEdge.EMPTY,
          BorderCorner.EMPTY, BorderCorner.EMPTY, BorderCorner.EMPTY, BorderCorner.EMPTY);


  private BorderEdge top;
  private BorderEdge left;
  private BorderEdge bottom;
  private BorderEdge right;
  private BorderEdge splittingEdge;

  private BorderCorner topLeft;
  private BorderCorner topRight;
  private BorderCorner bottomLeft;
  private BorderCorner bottomRight;
  private Boolean empty;

  public Border(final BorderEdge top,
                final BorderEdge left,
                final BorderEdge bottom,
                final BorderEdge right,
                final BorderEdge splittingEdge,
                final BorderCorner topLeft,
                final BorderCorner topRight,
                final BorderCorner bottomLeft,
                final BorderCorner bottomRight)
  {
    this.top = top;
    this.left = left;
    this.bottom = bottom;
    this.right = right;
    this.splittingEdge = splittingEdge;
    this.topLeft = topLeft;
    this.topRight = topRight;
    this.bottomLeft = bottomLeft;
    this.bottomRight = bottomRight;
  }

  public BorderEdge getTop()
  {
    return top;
  }

  public BorderEdge getLeft()
  {
    return left;
  }

  public BorderEdge getBottom()
  {
    return bottom;
  }

  public BorderEdge getRight()
  {
    return right;
  }

  public BorderEdge getSplittingEdge()
  {
    return splittingEdge;
  }

  public BorderCorner getTopLeft()
  {
    return topLeft;
  }

  public BorderCorner getTopRight()
  {
    return topRight;
  }

  public BorderCorner getBottomLeft()
  {
    return bottomLeft;
  }

  public BorderCorner getBottomRight()
  {
    return bottomRight;
  }

  public Border[] splitVertically(Border[] borders)
  {
    if (borders == null || borders.length < 2)
    {
      borders = new Border[2];
    }

    borders[0] = (Border) clone();
    borders[0].empty = null;
    borders[0].right = borders[0].splittingEdge;
    borders[0].topRight = BorderCorner.EMPTY;
    borders[0].bottomRight = BorderCorner.EMPTY;

    borders[1] = (Border) clone();
    borders[1].empty = null;
    borders[1].left = borders[1].splittingEdge;
    borders[1].topLeft= BorderCorner.EMPTY;
    borders[1].bottomLeft = BorderCorner.EMPTY;
    return borders;
  }

  public Border[] splitHorizontally(Border[] borders)
  {
    if (borders == null || borders.length < 2)
    {
      borders = new Border[2];
    }

    borders[0] = (Border) clone();
    borders[0].empty = null;
    borders[0].bottom = borders[0].splittingEdge;
    borders[0].bottomLeft = BorderCorner.EMPTY;
    borders[0].bottomRight = BorderCorner.EMPTY;

    borders[1] = (Border) clone();
    borders[1].empty = null;
    borders[1].top = borders[1].splittingEdge;
    borders[1].topLeft= BorderCorner.EMPTY;
    borders[1].topRight = BorderCorner.EMPTY;
    return borders;
  }

  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Borders not supporting clone is evil!");
    }
  }

  public boolean isEmpty()
  {
    if (empty != null)
    {
      return empty.booleanValue();
    }

    if (top.getWidth() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (left.getWidth() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (bottom.getWidth() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (right.getWidth() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }

    empty = Boolean.TRUE;
    return true;
  }


  public String toString()
  {
    return "Border{" +
        "top=" + top +
        ", left=" + left +
        ", bottom=" + bottom +
        ", right=" + right +
        ", splittingEdge=" + splittingEdge +
        ", topLeft=" + topLeft +
        ", topRight=" + topRight +
        ", bottomLeft=" + bottomLeft +
        ", bottomRight=" + bottomRight +
        ", empty=" + empty +
        '}';
  }
}
