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
 * PageBreakPositionList.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

/**
 * Creation-Date: 15.04.2007, 13:34:13
 *
 * @author Thomas Morgner
 */
public final class PageBreakPositionList
{
  private static final boolean USE_BINSEARCH = true;

  private long[] pageHeaderSizes;
  private long[] masterBreaks;
  private long[] breakPositions;
  private int breakSize;
  private int masterSize;
  private int nextFoundIdx;
  private int prevFoundIdx;
  private int prevFoundMasterIdx;
  private int lastCommonBreak;
  private int lastMasterBreak;

  public PageBreakPositionList(final PageBreakPositionList parentList, final int growth)
  {
    if (growth < 1)
    {
      throw new IndexOutOfBoundsException();
    }

    if (parentList == null)
    {
      // There is always a break at position ZERO. This break is always a master-break.
      breakPositions = new long[growth];
      masterBreaks = new long[growth];
      pageHeaderSizes = new long[growth];
      breakSize = 1;
      masterSize = 1;
      return;
    }

    final long[] parentBreaks = parentList.breakPositions;
    this.breakPositions = new long[parentList.breakSize + growth + 1];
    System.arraycopy(parentBreaks, 0, breakPositions, 0, parentList.breakSize);

    this.masterBreaks = new long[parentList.masterSize + 2];
    System.arraycopy(parentList.masterBreaks, 0, masterBreaks, 0, parentList.masterSize);
    this.pageHeaderSizes = new long[parentList.masterSize + 2];
    System.arraycopy(pageHeaderSizes, 0, pageHeaderSizes, 0, parentList.masterSize);
    this.breakSize = parentList.breakSize;
    this.masterSize = parentList.masterSize;

    this.prevFoundIdx = parentList.prevFoundIdx;
    this.nextFoundIdx = parentList.nextFoundIdx;
    this.lastCommonBreak = parentList.lastCommonBreak;
    this.lastMasterBreak = parentList.lastMasterBreak;
  }

  public void addMinorBreak(final long position)
  {
    // If this results in a IOBEx, then we made something wrong and deserve the exception.
    final long lastPosition = breakPositions[this.breakSize - 1];
    if (position < lastPosition)
    {
      // This usually happens if someone tries to pass a page with negative margins. We do not accept that.
      throw new IllegalArgumentException("Invalid position error: Unsorted Entry or negative page area.");
    }

    if (position > lastPosition)
    {
      breakPositions[breakSize] = position;
      breakSize += 1;
    }
  }

  public void addMajorBreak(final long position, final long pageHeaderSize)
  {
    // If this results in a IOBEx, then we made something wrong and deserve the exception.
    final long lastPosition = breakPositions[this.breakSize - 1];
    if (position < lastPosition)
    {
      // This usually happens if someone tries to pass a page with negative margins. We do not accept that.
      throw new IllegalArgumentException("Invalid position error: Unsorted Entry or negative page area.");
    }

    if (position > lastPosition)
    {
      breakPositions[breakSize] = position;
      breakSize += 1;
    }

    final long lastMaster = masterBreaks[this.masterSize - 1];
    if (position < lastMaster)
    {
      throw new IllegalStateException();
    }

    if (position > lastMaster)
    {
      masterBreaks[masterSize] = position;
      pageHeaderSizes[masterSize] = pageHeaderSize;
      masterSize += 1;
    }
  }


  /**
   * Returns the last break position that is smaller or equal
   *
   * @param position
   * @return
   */
  public long findNextBreakPosition(final long position)
  {
    final int breakIndex = findBreak(position);
    if (breakIndex < 0)
    {
      return breakPositions[0];
    }
    if (breakIndex >= breakSize)
    {
      return breakPositions[breakSize - 1];
    }
    return breakPositions[breakIndex];
  }


  /**
   * Returns the page-segment, where a box would be located that ends at the given position. If the position is located
   * before or on the page start, then this method returns -1 to indicate that this box would not be displayed at all.
   * If the given position is direcly located on a page-boundary, the segment number of the previous page is returned.
   *
   * @param pos the starting position of the box.
   * @return -1 or a positive integer denoting the page segment where the box would be displayed.
   */
  private int findNextBreak(final long pos)
  {
    int start = 0;
    int retval = -1;
    if (nextFoundIdx > 0)
    {
      final long foundPos = breakPositions[nextFoundIdx];
      final long prevPos = breakPositions[nextFoundIdx - 1];
      if (foundPos >= pos && prevPos < pos)
      {
        if (prevPos < pos)
        {
          return nextFoundIdx;
        }

        start = nextFoundIdx + 1;
        retval = nextFoundIdx;
      }
    }

    if (USE_BINSEARCH == false)
    {
      for (int i = start; i < breakSize; i++)
      {
        final long breakPosition = breakPositions[i];
        if (breakPosition >= pos)
        {
          break;
        }
        retval = i;
      }
      nextFoundIdx = retval;
      return retval;
    }

    final int i = binarySearch(breakPositions, pos, start, breakSize);
    if (i > -1)
    {
      nextFoundIdx = (i - 1);
      return nextFoundIdx;
    }
    if (i == -1)
    {
      nextFoundIdx = -1;
      return -1;
    }

    final int insertPos = Math.min(-(i + 2), breakSize - 1);
    // if greater than last break, return the last break ..
    nextFoundIdx = insertPos;
    return insertPos;

  }


  /**
   * Returns the page-segment, where a box would be located that starts at the given position. If the position is
   * located before the page start, then this method returns -1 to indicate that this box would not be displayed at
   * all.
   *
   * @param pos the starting position of the box.
   * @return -1 or a positive integer denoting the page segment where the box would be displayed.
   */
  private int findPreviousBreak(final long pos)
  {
    int start = 0;
    int retval = -1;

    if (prevFoundIdx >= 0)
    {
      final long prevFoundPos = breakPositions[prevFoundIdx];
      if (prevFoundPos == pos)
      {
        return prevFoundIdx;
      }

      if (prevFoundPos < pos)
      {
        if (prevFoundIdx >= (breakSize - 1))
        {
          // This is behind or directly at the end of the known breaks ...
          return prevFoundIdx;
        }

        // Check, whether the next one would be smaller too
        final long nextBreak = breakPositions[prevFoundIdx + 1];
        if (nextBreak > pos)
        {
          return prevFoundIdx;
        }
        // No need to start from the beginning, the result must be after the previous search result ...
        start = prevFoundIdx + 1;
        retval = prevFoundIdx;
      }
    }

    if (USE_BINSEARCH == false)
    {
      for (int i = start; i < breakSize; i++)
      {
        final long breakPosition = breakPositions[i];
        if (breakPosition > pos)
        {
          prevFoundIdx = retval;
          return retval;
        }
        retval = i;
      }

      prevFoundIdx = retval;
      return retval;
    }

    final int i = binarySearch(breakPositions, pos, start, breakSize);
    if (i > -1)
    {
      prevFoundIdx = i;
      return prevFoundIdx;
    }

    if (i == -1)
    {
      prevFoundIdx = -1;
      return -1;
    }

    final int insertPos = Math.min(-(i + 1), breakSize) - 1;
    // if greater than last break, return the last break ..
    prevFoundIdx = insertPos;
    return insertPos;


  }


  /**
   * Returns the page-segment, where a box would be located that starts at the given position. If the position is
   * located before the page start, then this method returns -1 to indicate that this box would not be displayed at
   * all.
   *
   * @param pos the starting position of the box.
   * @return -1 or a positive integer denoting the page segment where the box would be displayed.
   */
  private int findPreviousMajorBreak(final long pos)
  {
    int start = 0;
    int retval = -1;

    if (prevFoundMasterIdx >= 0)
    {
      final long prevFoundPos = masterBreaks[prevFoundIdx];
      if (prevFoundPos == pos)
      {
        return prevFoundMasterIdx;
      }

      if (prevFoundPos < pos)
      {
        if (prevFoundMasterIdx >= (masterSize - 1))
        {
          // This is behind or directly at the end of the known breaks ...
          return prevFoundMasterIdx;
        }

        // Check, whether the next one would be smaller too
        final long nextBreak = masterBreaks[prevFoundMasterIdx + 1];
        if (nextBreak > pos)
        {
          return prevFoundMasterIdx;
        }
        // No need to start from the beginning, the result must be after the previous search result ...
        start = prevFoundMasterIdx + 1;
        retval = prevFoundMasterIdx;
      }
    }

    if (USE_BINSEARCH == false)
    {
      for (int i = start; i < masterSize; i++)
      {
        final long breakPosition = masterBreaks[i];
        if (breakPosition > pos)
        {
          prevFoundMasterIdx = retval;
          return retval;
        }
        retval = i;
      }

      prevFoundMasterIdx = retval;
      return retval;
    }

    final int i = binarySearch(masterBreaks, pos, start, masterSize);
    if (i > -1)
    {
      prevFoundMasterIdx = i;
      return prevFoundMasterIdx;
    }

    if (i == -1)
    {
      prevFoundMasterIdx = -1;
      return -1;
    }

    final int insertPos = Math.min(-(i + 1), masterSize) - 1;
    // if greater than last break, return the last break ..
    prevFoundMasterIdx = insertPos;
    return insertPos;


  }

  public boolean isCrossingPagebreak(final RenderBox box,
                                     final long shift)
  {
    // A box does not cross the break, if both Y1 and Y2 are on the same page.
    final long height = box.getHeight();
    if (height == 0)
    {
      // A box without a height can appear on either side of the pagebreak.
      // But under no circumstances it can cross it.
      return false;
    }

    // Simple case: No fixed position at all ..
    final long shiftedStartPos = box.getY() + shift;
    final int y1 = findPreviousBreak(shiftedStartPos);
    final int y2 = findNextBreak(shiftedStartPos + height);

    // System.out.println("Result: (" + y1 + ", " + shiftedStartPos + ") (" + y2 + ", " + (shiftedStartPos + height) + ")");
    return y1 != y2;
  }

  public boolean isCrossingPagebreakWithFixedPosition(final long shiftedBoxPosition,
                                                      final long boxHeight,
                                                      final long fixedPositionResolved)
  {
    // Only allow positive values.
    // Make sure that we do not cover the page header area. If so, then correct the value to be
    // directly below the header-area.

    // Compute, the distance between the fixed-positioned box and the bottom edge of the page-header.
    final long shiftedSpaceOnPage = Math.max(0, (fixedPositionResolved - getPageHeaderHeight(shiftedBoxPosition)));
    // Compute the page-start on the normal flow.
    final int pageIndex = findPreviousMajorBreak(shiftedBoxPosition);
    final long fixedPositionInFlow;
    if (pageIndex < 0)
    {
      fixedPositionInFlow = masterBreaks[0] + shiftedSpaceOnPage;
    }
    else
    {
      fixedPositionInFlow = masterBreaks[pageIndex] + shiftedSpaceOnPage;
    }

    // A box does not cross the break, if both Y1 and Y2 are on the same page.
    if (boxHeight == 0)
    {
      // A box without a height can appear on either side of the pagebreak. But under no circumstances it may cross it.
      return false;
    }

    final int y1 = findPreviousBreak(fixedPositionInFlow);
    final int y2 = findNextBreak(fixedPositionInFlow + boxHeight);
    return y1 != y2;
  }

  /**
   * Computes the box's position in the normal-flow that will fullfill the 'fixed-position' constraint.
   *
   * @param shiftedBoxPosition
   * @param fixedPositionResolved
   * @return
   */
  public long computeFixedPositionInFlow(final long shiftedBoxPosition,
                                         final long fixedPositionResolved)
  {
    // Compute, the distance between the fixed-positioned box and the bottom edge of the page-header.
    final long shiftedSpaceOnPage = Math.max(0, (fixedPositionResolved - getPageHeaderHeight(shiftedBoxPosition)));
    // Compute the page-start on the normal flow.
    final int pageIndex = findPreviousMajorBreak(shiftedBoxPosition);
    if (pageIndex < 0)
    {
      return masterBreaks[0] + shiftedSpaceOnPage;
    }
    return masterBreaks[pageIndex] + shiftedSpaceOnPage;
  }

  private long getPageHeaderHeight(final long position)
  {
    return pageHeaderSizes[findNextMajorBreak(position)];
  }


  /**
   * Returns the first break position that is greater than the given position.
   *
   * @param pos
   * @return -1 or a positive integer.
   */
  private int findBreak(final long pos)
  {
    int start = 0;
    if (lastCommonBreak >= breakSize)
    {
      // the last known pagebreak.
      final long lastBreakPos = breakPositions[breakSize - 1];
      if (lastBreakPos > pos)
      {
        // the position behind the last break, so return it
        return breakSize;
      }
      if (lastBreakPos == pos)
      {
        lastCommonBreak = breakSize - 1;
        return lastCommonBreak;
      }
      // else the position we search is lower. Search from the beginning ..
    }
    else if (lastCommonBreak == 0)
    {
      final long lastBreakPos = breakPositions[lastCommonBreak];
      if (lastBreakPos >= pos)
      {
        return lastCommonBreak;
      }
    }
    else
    {
      final long lastBreakPos = breakPositions[lastCommonBreak];
      if (lastBreakPos >= pos)
      {
        final long prevBreakPos = breakPositions[lastCommonBreak - 1];
        if (prevBreakPos < pos)
        {
          return lastCommonBreak;
        }
      }
      else
      {
        start = lastCommonBreak + 1;
      }
    }

    if (USE_BINSEARCH == false)
    {
      for (int i = start; i < breakSize; i++)
      {
        final long breakPosition = breakPositions[i];
        if (breakPosition >= pos)
        {
          lastCommonBreak = i;
          return i;
        }
      }
      lastCommonBreak = breakSize;
      return breakSize;
    }

    final int i = binarySearch(breakPositions, pos, start, breakSize);
    if (i > -1)
    {
      lastCommonBreak = i;
      return i;
    }
    if (i == -1)
    {
      // not found ..
      lastCommonBreak = 0;
      return 0;
    }

    final int insertPos = -(i + 1);
    lastCommonBreak = insertPos;
    return insertPos;

  }


  /**
     * Returns the page number that would contain this position. Mapping the page-number into a pagebreak position returns
     * the page boundary (minY + height). This method returns -1 if the given position is *before* the first page-boundary.
     *
     * @param pos
     * @return -1 or a positive integer.
     */
  private int findNextMajorBreak(final long pos)
  {
    int start = 0;
    if (lastMasterBreak >= masterSize)
    {
      // the last known pagebreak.
      final long lastBreakPos = masterBreaks[breakSize - 1];
      if (lastBreakPos > pos)
      {
        // the position behind the last break, so return it
        return masterSize;
      }
      if (lastBreakPos == pos)
      {
        lastMasterBreak = masterSize - 1;
        return lastMasterBreak;
      }
      // else the position we search is lower. Search from the beginning ..
    }
    else if (lastMasterBreak == 0)
    {
      final long lastBreakPos = masterBreaks[lastMasterBreak];
      if (lastBreakPos >= pos)
      {
        return lastMasterBreak;
      }
    }
    else
    {
      final long lastBreakPos = masterBreaks[lastMasterBreak];
      if (lastBreakPos >= pos)
      {
        final long prevBreakPos = masterBreaks[lastMasterBreak - 1];
        if (prevBreakPos < pos)
        {
          return lastMasterBreak;
        }
      }
      else
      {
        start = lastMasterBreak + 1;
      }
    }

    if (USE_BINSEARCH == false)
    {
      for (int i = start; i < masterSize; i++)
      {
        final long breakPosition = masterBreaks[i];
        if (breakPosition >= pos)
        {
          lastMasterBreak = i;
          return i;
        }
      }
      lastMasterBreak = masterSize;
      return masterSize;
    }


    final int i = binarySearch(masterBreaks, pos, start, masterSize);
    if (i > -1)
    {
      lastMasterBreak = i;
      return i;
    }
    if (i == -1)
    {
      // not found ..
      lastMasterBreak = 0;
      return 0;
    }

    final int insertPos = -(i + 1);
    lastMasterBreak = insertPos;
    return insertPos;
  }

  private static int binarySearch(final long[] array, final long key, final int start, final int end)
  {
    int low = start;
    int high = end - 1;

    while (low <= high)
    {
      final int mid = (low + high) >> 1;
      final long midVal = array[mid];

      if (midVal < key)
      {
        low = mid + 1;
      }
      else if (midVal > key)
      {
        high = mid - 1;
      }
      else
      {
        return mid; // key found
      }
    }
    return -(low + 1);  // key not found.
  }

  public long findNextMajorBreakPosition(final long pos)
  {
    final int majorBreakIndex = findNextMajorBreak(pos);
    if (majorBreakIndex < 0)
    {
      return masterBreaks[0];
    }
    if (majorBreakIndex >= masterSize)
    {
      return masterBreaks[masterSize - 1];
    }
    return masterBreaks[majorBreakIndex];
  }

  public long getLastMasterBreak()
  {
    return masterBreaks[masterSize - 1];
  }


  public String toString()
  {
    final StringBuffer retval = new StringBuffer(100);
    retval.append("PageBreakPositionList{breakSize=");
    retval.append(breakSize);
    retval.append(", masterSize=");
    retval.append(masterSize);
    retval.append(", prevFoundIdx=");
    retval.append(prevFoundIdx);
    retval.append(", masterBreaks={");
    for (int i = 0; i < masterBreaks.length; i++)
    {
      if (i > 0)
      {
        retval.append(", ");
      }
      final long aBreak = masterBreaks[i];
      retval.append(String.valueOf(aBreak));
    }
    retval.append("}, breakPositions={");

    for (int i = 0; i < breakPositions.length; i++)
    {
      if (i > 0)
      {
        retval.append(", ");
      }
      final long position = breakPositions[i];
      retval.append(String.valueOf(position));

    }
    retval.append("}}");
    return retval.toString();
  }
}
