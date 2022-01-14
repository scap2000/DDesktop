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
 * CenterAlignmentProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.alignment;

import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.process.layoutrules.EndSequenceElement;
import org.jfree.report.layout.process.layoutrules.InlineBoxSequenceElement;
import org.jfree.report.layout.process.layoutrules.InlineSequenceElement;
import org.jfree.report.layout.process.layoutrules.StartSequenceElement;
import org.jfree.report.layout.process.layoutrules.TextSequenceElement;

/**
 * Right alignment strategy. Not working yet, as this is unimplemented right now.
 *
 * @author Thomas Morgner
 */
public class CenterAlignmentProcessor extends AbstractAlignmentProcessor
{
  public CenterAlignmentProcessor()
  {
  }

  protected int handleElement(final int start, final int count)
  {
    final InlineSequenceElement[] sequenceElements = getSequenceElements();
    final RenderNode[] nodes = getNodes();
    final long[] elementDimensions = getElementDimensions();
    final long[] elementPositions = getElementPositions();

    // if we reached that method, then this means, that the elements may fit
    // into the available space. (Assuming that there is no inner pagebreak;
    // a thing we do not handle yet)
    final int endIndex = start + count;
    long usedWidth = 0;
    long usedWidthToStart = 0;
    int contentIndex = start;
    InlineSequenceElement contentElement = null;
    for (int i = 0; i < endIndex; i++)
    {
      final InlineSequenceElement element = sequenceElements[i];
      final RenderNode node = nodes[i];
      usedWidth += element.getMaximumWidth(node);
      if (i < start)
      {
        usedWidthToStart += element.getMaximumWidth(node);
      }
      if (element instanceof StartSequenceElement ||
          element instanceof EndSequenceElement)
      {
        continue;
      }
      contentElement = element;
      contentIndex = i;
    }

    final long nextPosition = (getStartOfLine() + usedWidth);
    final long lastPageBreak = getPageBreak(getPagebreakCount() - 1);
    if (nextPosition > lastPageBreak)
    {
      // The contents we processed so far will not fit on the current line. That's dangerous.
      // We have to center align the content up to the start position.
      performCenterAlignment(start, usedWidthToStart,
        sequenceElements, nodes, elementDimensions, elementPositions);

      // we cross a pagebreak. Stop working on it - we bail out here.

      if (contentElement instanceof TextSequenceElement)
      {
        // the element may be splittable. Test, and if so, give a hint to the
        // outside world ..
        setSkipIndex(endIndex);
        setBreakableIndex(contentIndex);
        return (start);
      }

      // This is the first element and it still does not fit. How evil.
      if (start == 0)
      {
        if (contentElement instanceof InlineBoxSequenceElement)
        {
          final RenderNode node = nodes[contentIndex];
          if (node instanceof RenderBox)
          {
            // OK, limit the size of the box to the maximum line width and
            // revalidate it.
            final long contentPosition = elementPositions[contentIndex];
            final RenderBox box = (RenderBox) node;
            final long maxWidth = (getEndOfLine() - contentPosition);
            computeInlineBlock(box, contentPosition, maxWidth);

            elementDimensions[endIndex - 1] = node.getCachedWidth();
          }
        }
        setSkipIndex(endIndex);
      }
      return (start);
    }

    // if we reached that method, then this means, that the elements may fit
    // into the available space. (Assuming that there is no inner pagebreak;
    // a thing we do not handle yet)

    if (performCenterAlignment(endIndex, usedWidth,
        sequenceElements, nodes, elementDimensions, elementPositions))
    {
      return endIndex;
    }
    return start;
  }

  private boolean performCenterAlignment(final int endIndex,
                                     final long usedWidth,
                                     final InlineSequenceElement[] sequenceElements,
                                     final RenderNode[] nodes,
                                     final long[] elementDimensions,
                                     final long[] elementPositions)
  {
    final long startOfLine = getStartOfLine();
    final long totalWidth = getEndOfLine() - startOfLine;
    final long emptySpace = Math.max (0, (totalWidth - usedWidth));
    long position = startOfLine + emptySpace / 2;
    for (int i = 0; i < endIndex; i++)
    {
      final RenderNode node = nodes[i];
      final long elementWidth = sequenceElements[i].getMaximumWidth(node);
      elementDimensions[i] = elementWidth;
      elementPositions[i] = position;

      position += elementWidth;
    }

    // Now search the element at the center-point.
    if (getPagebreakCount() == 1)
    {
      return true;
    }

    // Find the center-point of the element and the center point (and center element) of the elements.
    final long centerPoint = startOfLine + totalWidth / 2;
    final int centerElement = findElementForPosition(centerPoint);
    final long centerElementPosition = elementPositions[centerElement];
    final long centerElementEnd = centerElementPosition + elementDimensions[centerElement];

    // The distance between start of the element and the center point is greater
    // than the distance between the center point and the end of the element, then shift the center element
    // to the left. Also shift it to the left, if the element is the only element that should be centered.
    if ((centerPoint - centerElementPosition) > (centerElementEnd - centerPoint))
    {
      // the Center element will be shifted to the left.
      if (performShiftLeft(centerElement + 1, centerPoint) &&
          performShiftRight(centerElement + 1, endIndex, centerPoint))
      {
        return true;
      }
      return false;
    }

    // The center-element will be shifted to the right.
    if (performShiftLeft(centerElement, centerPoint) &&
        performShiftRight(centerElement, endIndex, centerPoint))
    {
      return true;
    }
    return false;
  }


  private boolean performShiftRight (final int firstElementIndex, final int lastElementIndex, final long centerPoint)
  {
    if (firstElementIndex >= lastElementIndex)
    {
      // nothing to do here ..
      return true;
    }

    final long[] elementDimensions = getElementDimensions();
    final long[] elementPositions = (long[]) getElementPositions().clone();
    final long endOfLine = getEndOfLine();

    int segment = findPagebreakForPosition(centerPoint);
    long segmentEnd = getPageBreak(segment);
    long segmentStart = centerPoint;
    final int pagebreakCount = getPagebreakCount();

    for (int i = firstElementIndex; i < lastElementIndex; i++)
    {
      final long elementWidth = elementDimensions[i];
      long elementEnd = segmentStart + elementWidth;
      if (elementEnd > endOfLine)
      {
        // this element will not fit ..
        return false;
      }
      while (segment < pagebreakCount && elementEnd > segmentEnd)
      {
        // try the next segment ..
        segment += 1;
        segmentStart = segmentEnd;
        segmentEnd = getPageBreak(segment);
        elementEnd = segmentStart + elementWidth;
      }

      if (elementEnd > endOfLine)
      {
        // the element will not fit into any of the remaining segments. So skip it.
        return false;
      }

      elementPositions[i] = segmentStart;
      segmentStart = elementEnd;
    }

    System.arraycopy(elementPositions, firstElementIndex, getElementPositions(),
        firstElementIndex, lastElementIndex - firstElementIndex);
    return true;
  }

  private boolean performShiftLeft (final int lastElementIndex, final long centerPoint)
  {
    if (lastElementIndex == 0)
    {
      // there is nothing to shift here ..
      return true;
    }

    // we will work on a clone, so that the undo is easier ..
    final long[] elementDimensions = getElementDimensions();
    final long[] elementPositions = (long[]) getElementPositions().clone();
    final int elementIdx = lastElementIndex - 1;

    // iterate backwards; start from the center element and right align all previous elements ..
    final long startOfLine = getStartOfLine();
    // the current segment.
    int segment = findPagebreakForPosition(centerPoint);
    long segmentEnd = getPageBreak(segment);
    long segmentStart = getStartOfSegment(segment);

    for (int i = elementIdx; i >= 0; i--)
    {
      final long elementWidth = elementDimensions[i];
      long elementStart = segmentEnd - elementWidth;
      if (elementStart < startOfLine)
      {
        // this element will not fit. Skip it.
        return false;
      }

      while (segment > 0 && elementStart < segmentStart)
      {
        // the element will not fit into the current segment. Move it to the next segment.
        elementStart = segmentStart - elementWidth;
        segment -= 1;
        segmentStart = getStartOfSegment(segment);
      }

      if (elementStart < segmentStart)
      {
        // the element will not fit into any of the remaining segments. So skip it.
        return false;
      }

      elementPositions[i] = elementStart;
      elementDimensions[i] = elementWidth;
      segmentEnd = elementStart;
    }

    // Commit the changes ..
    System.arraycopy(elementPositions, 0, getElementPositions(), 0, lastElementIndex);
    return true;
  }

  private long getStartOfSegment(final int segment)
  {
    if (segment <= 0)
    {
      return getStartOfLine();
    }

    return getPageBreak(segment -1);
  }

  private int findPagebreakForPosition (final long position)
  {
    final long[] breaks = getPageBreaks();
    final int elementSize = getPagebreakCount();
    final int i = binarySearch(breaks, position, elementSize);
    if (i > -1)
    {
      return i;
    }
    if (i == -1)
    {
      return 0;
    }

    return Math.min (-(i + 2), elementSize - 1);
  }

  private int findElementForPosition (final long position)
  {
    final long[] elementPositions = getElementPositions();
    final int elementSize = getSequenceFill();
    final int i = binarySearch(elementPositions, position, elementSize);
    if (i > -1)
    {
      return i;
    }
    if (i == -1)
    {
      return 0;
    }

    // if greater than last break, return the last break ..
    return Math.min(-(i + 2), elementSize - 1);
  }

  private static int binarySearch(final long[] array, final long key, final int end)
  {
    int low = 0;
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

}
