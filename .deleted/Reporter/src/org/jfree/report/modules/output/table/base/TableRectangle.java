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
 * TableRectangle.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.base;

/**
 * The TableRectangle contains GridCoordinates for the tables. The rectangle contains minX- and minY-cuts, so as long as the
 * cell is not empty (width==0, height==0), x1 and x2 will not have the same value.
 */
public class TableRectangle
{
  private int x1;
  private int y1;
  private int x2;
  private int y2;

  public TableRectangle ()
  {
  }

  public TableRectangle (final int x1, final int x2, final int y1, final int y2)
  {
    setRect(x1, y1, x2, y2);
  }

  public int getX1 ()
  {
    return x1;
  }

  public int getX2 ()
  {
    return x2;
  }

  public int getY1 ()
  {
    return y1;
  }

  public int getY2 ()
  {
    return y2;
  }

  public void setRect (final int x1, final int y1, final int x2, final int y2)
  {
    if (x1 > x2)
    {
      throw new IllegalArgumentException("x1 is greater than x2 - the rectangle would have negative content.");
    }
    if (y1 > y2)
    {
      throw new IllegalArgumentException("y1 is greater than y2 - the rectangle would have negative content.");
    }
    this.x1 = x1;
    this.x2 = x2;
    this.y1 = y1;
    this.y2 = y2;
  }

  public boolean isOrigin (final int x, final int y)
  {
    return (x == x1 && y == y1);
  }

  public int getRowSpan ()
  {
    return y2 - y1;
  }

  public int getColumnSpan ()
  {
    return x2 - x1;
  }


  public String toString ()
  {
    return "org.jfree.report.modules.output.table.base.TableRectangle{" +
            "x1=" + x1 +
            ", y1=" + y1 +
            ", x2=" + x2 +
            ", y2=" + y2 +
        '}';
  }
}
