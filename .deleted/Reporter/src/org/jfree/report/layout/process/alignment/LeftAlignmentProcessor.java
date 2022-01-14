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
 * LeftAlignmentProcessor.java
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
 * Performs the left-alignment computations.
 * <p/>
 * The inf-min-step creates the initial sequence of elements. The alignment processor now iterates over the sequence and
 * produces the layouted line.
 * <p/>
 * Elements can be split, splitting is a local operation and does not copy the children. Text splitting may produce a
 * totally different text (see: TeX hyphenation system).
 * <p/>
 * The process is iterative and continues unless all elements have been consumed.
 *
 * @author Thomas Morgner
 */
public class LeftAlignmentProcessor extends AbstractAlignmentProcessor
{
  private long position;
  private int pageSegment;

  public LeftAlignmentProcessor()
  {
  }

  public int getPageSegment()
  {
    return pageSegment;
  }

  public void setPageSegment(final int pageSegment)
  {
    this.pageSegment = pageSegment;
  }

  private long getPosition()
  {
    return position;
  }

  private void setPosition(final long position)
  {
    this.position = position;
  }

  private void addPosition(final long width)
  {
    this.position += width;
  }

  public RenderNode next()
  {
    position = getStartOfLine();
    pageSegment = 0;

    final RenderNode retval = super.next();

    position = 0;
    pageSegment = 0;

    return retval;
  }

  public void performLastLineAlignment()
  {
    position = getStartOfLine();
    pageSegment = 0;

    super.performLastLineAlignment();

    position = 0;
    pageSegment = 0;
  }

  /**
   * Handle the next input chunk.
   *
   * @param start the start index
   * @param count the number of elements in the sequence
   * @return the index of the last element that will fit on the current line.
   */
  protected int handleElement(final int start, final int count)
  {
    final InlineSequenceElement[] sequenceElements = getSequenceElements();
    final RenderNode[] nodes = getNodes();
    final long[] elementDimensions = getElementDimensions();
    final long[] elementPositions = getElementPositions();

    long width = 0;
    final int endIndex = start + count;

    // In the given range, there should be only one content element.
    InlineSequenceElement contentElement = null;
    int contentIndex = start;
    for (int i = start; i < endIndex; i++)
    {
      final InlineSequenceElement element = sequenceElements[i];
      final RenderNode node = nodes[i];
      if (element instanceof StartSequenceElement ||
          element instanceof EndSequenceElement)
      {
        width += element.getMaximumWidth(node);
        continue;
      }

      width += element.getMaximumWidth(node);
      contentElement = element;
      contentIndex = i;
    }

    final long nextPosition = getPosition() + width;
    final long lastPageBreak = getPageBreak(getPagebreakCount() - 1);
    // Do we cross a page boundary?
    if (nextPosition > lastPageBreak)
    {
      // On outer break: Stop processing

      // Dont write through to the stored position; but prepare if
      // we have to fallback ..
      long position = getPosition();
      for (int i = start; i < endIndex; i++)
      {
        final InlineSequenceElement element = sequenceElements[i];
        final RenderNode node = nodes[i];
        elementPositions[i] = position;
        final long elementWidth = element.getMaximumWidth(node);
        elementDimensions[i] = elementWidth;
        position += elementWidth;
      }

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

    final long innerPagebreak = getPageBreak(getPageSegment());
    if (nextPosition > innerPagebreak)
    {
      // It is an inner pagebreak and the current element would not fit into the remaining space.
      // Move the element to the next page segment (but only if the start is not on
      setPosition(innerPagebreak);
      setPageSegment(getPageSegment() + 1);
    }


    // No, it is an ordinary advance ..
    // Check, whether we hit an item-sequence element
    if (contentElement instanceof InlineBoxSequenceElement == false)
    {
      for (int i = start; i < endIndex; i++)
      {
        final RenderNode node = nodes[i];
        final InlineSequenceElement element = sequenceElements[i];
        elementPositions[i] = getPosition();
        final long elementWidth = element.getMaximumWidth(node);
        elementDimensions[i] = elementWidth;
        addPosition(elementWidth);
      }
      return endIndex;
    }

    // Handle the ItemSequence element.

    // This is a bit more complicated. So we encountered an inline-block
    // element here. That means, the element will try to occuppy its
    // maximum-content-width.
//    Log.debug("Advance block at index " + contentIndex);
//    final long ceWidth = contentElement.getMinimumWidth();
//    final long extraSpace = contentElement.getMaximumWidth();
//    Log.debug("Advance block: Min " + ceWidth);
//    Log.debug("Advance block: Max " + extraSpace);

    final RenderNode contentNode = nodes[contentIndex];
    final long itemElementWidth = contentElement.getMaximumWidth(contentNode);

    if (contentNode instanceof RenderBox)
    {
      final RenderBox box = (RenderBox) contentNode;
      computeInlineBlock(box, getPosition(), itemElementWidth);
    }
    else
    {
      contentNode.setCachedX(getPosition());
      contentNode.setCachedWidth(itemElementWidth);
    }

    final long preferredEndingPos = getPosition() + itemElementWidth;
    if (preferredEndingPos > getEndOfLine())
    {
      // We would eat the whole space up to the end of the line and more
      // So lets move that element to the next line instead...

      // But: We could easily end in an endless loop here. So check whether
      // the element is the first in the line
      if (start == 0)
      {
        // As it is guaranteed, that each chunk contains at least one item,
        // checking for start == 0 is safe enough ..
        return endIndex;
      }

      return start;
    }

    for (int i = start; i < contentIndex; i++)
    {
      final InlineSequenceElement element = sequenceElements[i];
      final RenderNode node = nodes[contentIndex];
      final long elementWidth = element.getMaximumWidth(node);
      elementPositions[i] = getPosition();
      elementDimensions[i] = elementWidth;
      addPosition(elementWidth);
    }

    elementPositions[contentIndex] = getPosition();
    elementDimensions[contentIndex] = itemElementWidth;
    setPosition(preferredEndingPos);

    for (int i = contentIndex + 1; i < endIndex; i++)
    {
      final InlineSequenceElement element = sequenceElements[i];
      final RenderNode node = nodes[contentIndex];
      final long elementWidth = element.getMaximumWidth(node);
      elementPositions[i] = getPosition();
      elementDimensions[i] = elementWidth;
      addPosition(elementWidth);
    }

    return endIndex;
  }

  public void performSkipAlignment(final int endIndex)
  {
    // this is a NO-OP method, as the skip-alignment is simply a left-alignment ...
  }
}
