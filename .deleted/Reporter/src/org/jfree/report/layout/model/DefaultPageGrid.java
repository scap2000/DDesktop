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
 * DefaultPageGrid.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;

import java.util.Iterator;
import java.util.TreeSet;

import org.jfree.report.PageDefinition;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 05.04.2007, 16:15:32
 *
 * @author Thomas Morgner
 */
public class DefaultPageGrid implements PageGrid
{
  private long[] horizontalBreaks;
  private long[] verticalBreaks;
  private long[] horizontalBreaksFull;
  private long[] verticalBreaksFull;
  private PageFormat[][] pageMapping;

  public DefaultPageGrid(final PageDefinition pageDefinition)
  {
    final Rectangle2D[] pagePositions = pageDefinition.getPagePositions();

    final TreeSet horizontalPositions = new TreeSet();
    final TreeSet verticalPositions = new TreeSet();

    for (int i = 0; i < pagePositions.length; i++)
    {
      final Rectangle2D pagePosition = pagePositions[i];

      final double minX = pagePosition.getMinX();
      final double maxX = pagePosition.getMaxX();
      final double minY = pagePosition.getMinY();
      final double maxY = pagePosition.getMaxY();

      if (minX == maxX || maxY == minY)
      {
        throw new IllegalArgumentException("This page format is invalid, it has no imageable area.");
      }
      horizontalPositions.add(new Double(minX));
      horizontalPositions.add(new Double(maxX));
      verticalPositions.add(new Double(minY));
      verticalPositions.add(new Double(maxY));
    }

    horizontalBreaksFull = new long[horizontalPositions.size()];
    int pos = 0;
    for (Iterator iterator = horizontalPositions.iterator(); iterator.hasNext();)
    {
      final Double value = (Double) iterator.next();
      horizontalBreaksFull[pos] = StrictGeomUtility.toInternalValue(value.doubleValue());
      pos += 1;
    }

    verticalBreaksFull = new long[verticalPositions.size()];
    pos = 0;
    for (Iterator iterator = verticalPositions.iterator(); iterator.hasNext();)
    {
      final Double value = (Double) iterator.next();
      verticalBreaksFull[pos] = StrictGeomUtility.toInternalValue(value.doubleValue());
      pos += 1;
    }

    horizontalPositions.remove(new Double(0));
    verticalPositions.remove(new Double(0));

    horizontalBreaks = new long[horizontalPositions.size()];
    pos = 0;
    for (Iterator iterator = horizontalPositions.iterator(); iterator.hasNext();)
    {
      final Double value = (Double) iterator.next();
      horizontalBreaks[pos] = StrictGeomUtility.toInternalValue(value.doubleValue());
      pos += 1;
    }

    verticalBreaks = new long[verticalPositions.size()];
    pos = 0;
    for (Iterator iterator = verticalPositions.iterator(); iterator.hasNext();)
    {
      final Double value = (Double) iterator.next();
      verticalBreaks[pos] = StrictGeomUtility.toInternalValue(value.doubleValue());
      pos += 1;
    }

    pageMapping = new PageFormat[verticalBreaksFull.length - 1][horizontalBreaksFull.length - 1];
    for (int col = 0; col < horizontalBreaksFull.length; col++)
    {
      final long xPosition = horizontalBreaksFull[col];
      for (int row = 0; row < verticalBreaksFull.length; row++)
      {
        final long yPosition = verticalBreaksFull[row];
        final int idx = findPageFormat(pagePositions, xPosition, yPosition);
        if (idx >= 0)
        {
          pageMapping[row][col] = pageDefinition.getPageFormat(idx);
        }
      }
    }
  }

  private int findPageFormat(final Rectangle2D[] positions, final long xPosition, final long yPosition)
  {
    for (int i = 0; i < positions.length; i++)
    {
      final Rectangle2D rect = positions[i];
      if (StrictGeomUtility.toInternalValue(rect.getMinY()) == yPosition &&
          StrictGeomUtility.toInternalValue(rect.getMinX()) == xPosition)
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * In case of overlapping pageboxes, this method may return null.
   *
   * @param row
   * @param col
   * @return
   */
  public PhysicalPageBox getPage(final int row, final int col)
  {
    final long offsetX = horizontalBreaksFull[col];
    final long offsetY = verticalBreaksFull[row];

    final PageFormat format = pageMapping[row][col];
    return new PhysicalPageBox(format, offsetX, offsetY);
  }

  public long[] getHorizontalBreaks()
  {
    return (long[]) horizontalBreaks.clone();
  }

  public long[] getVerticalBreaks()
  {
    return (long[]) verticalBreaks.clone();
  }

  public int getRowCount()
  {
    return verticalBreaks.length;
  }

  public int getColumnCount()
  {
    return horizontalBreaks.length;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  public long getMaximumPageWidth()
  {
    return horizontalBreaks[horizontalBreaks.length - 1];
  }

  public long getMaximumPageHeight()
  {
    return verticalBreaks[verticalBreaks.length - 1];
  }
}
