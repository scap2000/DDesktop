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
 * AbstractAlignmentProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.alignment;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.PageGrid;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;
import org.jfree.report.layout.process.InfiniteMinorAxisLayoutStep;
import org.jfree.report.layout.process.layoutrules.EndSequenceElement;
import org.jfree.report.layout.process.layoutrules.InlineSequenceElement;
import org.jfree.report.layout.process.layoutrules.SequenceList;
import org.jfree.report.layout.process.layoutrules.StartSequenceElement;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.util.LongList;
import org.jfree.util.FastStack;

/**
 * Todo: The whole horizontal alignment is not suitable for spanned page breaks.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractAlignmentProcessor implements TextAlignmentProcessor, LastLineTextAlignmentProcessor
{
  private static final InlineSequenceElement[] EMPTY_ELEMENTS = new InlineSequenceElement[0];
  private static final RenderNode[] EMPTY_NODES = new RenderNode[0];

  private static final int START = 0;
  private static final int CONTENT = 1;
  private static final int END = 2;

  private long startOfLine;
  private long endOfLine;
  private long[] pagebreaks;
  private int pagebreakCount;
  private PageGrid pageGrid;

  private InlineSequenceElement[] sequenceElements = EMPTY_ELEMENTS;
  private RenderNode[] nodes = EMPTY_NODES;
  private int sequenceFill;

  /**
   * A layouter hint, that indicates a possibly breakable element
   */
  private int breakableIndex;
  /**
   * A layouter hint, that indicates where to continue on unbreakable elements.
   */
  private int skipIndex;

  private long[] elementPositions;
  private long[] elementDimensions;
  private FastStack contexts;
  private ArrayList pendingElements;
  private static final long[] EMPTY = new long[0];
  private boolean lastLineAlignment;
  private LeftAlignmentProcessor leftAlignProcessor;


  protected AbstractAlignmentProcessor()
  {
    this.contexts = new FastStack();
    this.pendingElements = new ArrayList();
    this.elementDimensions = EMPTY;
    this.elementPositions = EMPTY;
  }

  protected long getStartOfLine()
  {
    return startOfLine;
  }

  protected PageGrid getPageGrid()
  {
    return pageGrid;
  }

  protected InlineSequenceElement[] getSequenceElements()
  {
    return sequenceElements;
  }

  protected RenderNode[] getNodes()
  {
    return nodes;
  }

  protected long[] getElementPositions()
  {
    return elementPositions;
  }

  protected long[] getElementDimensions()
  {
    return elementDimensions;
  }

  protected long getEndOfLine()
  {
    return endOfLine;
  }

  public int getPagebreakCount()
  {
    return pagebreakCount;
  }

  protected long getPageBreak(final int pageIndex)
  {
    if (pageIndex < 0 || pageIndex >= pagebreakCount)
    {
      throw new IndexOutOfBoundsException();
    }
    return pagebreaks[pageIndex];
  }

  protected long[] getPageBreaks()
  {
    return pagebreaks;
  }

  protected int getBreakableIndex()
  {
    return breakableIndex;
  }

  protected void setBreakableIndex(final int breakableIndex)
  {
    this.breakableIndex = breakableIndex;
  }

  protected int getSkipIndex()
  {
    return skipIndex;
  }

  protected void setSkipIndex(final int skipIndex)
  {
    this.skipIndex = skipIndex;
  }

  /**
   * Processes the text and calls the layouting methods. This method returns the index of the last element that fits on
   * the current line.
   *
   * @param elements
   * @param maxPos
   * @return
   */
  protected int iterate(final InlineSequenceElement[] elements, final int maxPos)
  {
    breakableIndex = -1;
    skipIndex = -1;
    // The state transitions are as follows:
    // ......From....START...CONTENT...END
    // to...START....-.......X.........X
    // ...CONTENT....-.......X.........X
    // .......END....-.......-.........-
    //
    // Dash signals, that there is no break opportunity,
    // while X means, that it is possible to break the inline flow at that
    // position.

    if (maxPos == 0)
    {
      // nothing to do ..
      return 0;
    }


    int lastElementType = classifyInput(elements[0]);
    int startIndex = 0;
    for (int i = 1; i < maxPos; i++)
    {
      final InlineSequenceElement element = elements[i];
      final int elementType = classifyInput(element);
      if (elementType == END)
      {
        lastElementType = elementType;
        continue;
      }

      if (lastElementType == START)
      {
        lastElementType = elementType;
        continue;
      }

      final int newIndex = handleElement(startIndex, i - startIndex);
      if (newIndex <= startIndex)
      {
        return startIndex;
      }

      startIndex = i;
      lastElementType = elementType;
    }

    return handleElement(startIndex, maxPos - startIndex);
  }

  /**
   * Initializes the alignment process. The start and end parameters specify the line boundaries, and have been
   * precomputed.
   *
   * @param sequence
   * @param start
   * @param end
   * @param breaks
   */
  public void initialize(final SequenceList sequence,
                         final long start,
                         final long end,
                         final PageGrid breaks)
  {
    if (end < start)
    {
      // This is most certainly an error, treat it as such ..
      throw new IllegalArgumentException("Start is <= end; which is stupid!: " + end + ' ' + start);
    }

    this.sequenceElements = sequence.getSequenceElements(this.sequenceElements);
    this.nodes = sequence.getNodes(this.nodes);
    this.sequenceFill = sequence.size();
    this.pageGrid = breaks;
    if (elementPositions.length <= sequenceFill)
    {
      this.elementPositions = new long[sequenceFill];
    }
    else
    {
      Arrays.fill(this.elementPositions, 0);
    }

    if (elementDimensions.length <= sequenceFill)
    {
      this.elementDimensions = new long[sequenceFill];
    }
    else
    {
      Arrays.fill(this.elementDimensions, 0);
    }
    // to be computed by the pagegrid ..
    if (startOfLine != start || endOfLine != end || pagebreaks == null)
    {
      this.startOfLine = start;
      this.endOfLine = end;
      updateBreaks();
    }
  }

  public void deinitialize()
  {
    this.pageGrid = null;
    this.pendingElements.clear();
    this.contexts.clear();
    this.sequenceElements = EMPTY_ELEMENTS;
  }

  private void updateBreaks()
  {
    final long[] horizontalBreaks = pageGrid.getHorizontalBreaks();
    final LongList pageLongList = new LongList(horizontalBreaks.length);
    for (int i = 0; i < horizontalBreaks.length; i++)
    {
      final long pos = horizontalBreaks[i];
      if (pos <= startOfLine)
      {
        // skip ..
        continue;
      }
      if (pos >= endOfLine)
      {
        break;
      }
      pageLongList.add(pos);
    }
    pageLongList.add(endOfLine);

    this.pagebreaks = pageLongList.toArray(this.pagebreaks);
    this.pagebreakCount = pageLongList.size();
  }

  public boolean hasNext()
  {
    return sequenceFill > 0;
  }

  public RenderNode next()
  {
    Arrays.fill(elementDimensions, 0);
    Arrays.fill(elementPositions, 0);

    int lastPosition = iterate(sequenceElements, sequenceFill);
    if (lastPosition == 0)
    {
      // This could evolve into an infinite loop. Thats evil.
      // We have two choices to prevent that:
      // (1) Try to break the element.
//      if (getBreakableIndex() >= 0)
//      {
//        // Todo: Breaking is not yet implemented ..
//      }
      if (getSkipIndex() >= 0)
      {
        // This causes an overflow ..
        performSkipAlignment(getSkipIndex());
        lastPosition = getSkipIndex();
      }
      else
      {
        // Skip the complete line. Oh, thats not good, really!
        lastPosition = sequenceFill;
      }
    }

    // now, build the line and update the array ..
    pendingElements.clear();
    contexts.clear();
    RenderBox firstBox = null;
    RenderBox box = null;
    for (int i = 0; i < lastPosition; i++)
    {
      final RenderNode node = nodes[i];
      final InlineSequenceElement element = sequenceElements[i];
      if (element instanceof EndSequenceElement)
      {
        contexts.pop();
        final long boxX2 = (elementPositions[i] + elementDimensions[i]);
        box.setCachedWidth(boxX2 - box.getCachedX());

        if (contexts.isEmpty())
        {
          box = null;
        }
        else
        {
          final RenderNode tmpnode = box;
          box = (RenderBox) contexts.peek();
          box.addGeneratedChild(tmpnode);
        }
        continue;
      }

      if (element instanceof StartSequenceElement)
      {
        box = (RenderBox) node.derive(false);
        box.setCachedX(elementPositions[i]);
        contexts.push(box);
        if (firstBox == null)
        {
          firstBox = box;
        }
        continue;
      }

      if (box == null)
      {
        throw new IllegalStateException("Invalid sequence: " +
            "Cannot have elements before we open the box context.");
      }

      // Content element: Perform a deep-deriveForAdvance, so that we preserve the
      // possibly existing sub-nodes.
      final RenderNode child = node.derive(true);
      child.setCachedX(elementPositions[i]);
      child.setCachedWidth(elementDimensions[i]);
      if (box.getStaticBoxLayoutProperties().isPreserveSpace() &&
          box.getStyleSheet().getBooleanStyleProperty(TextStyleKeys.TRIM_TEXT_CONTENT) == false)
      {
        box.addGeneratedChild(child);
        continue;
      }

      if (child.isIgnorableForRendering())
      {
        pendingElements.add(child);
      }
      else
      {
        for (int j = 0; j < pendingElements.size(); j++)
        {
          final RenderNode pendingNode = (RenderNode) pendingElements.get(j);
          box.addGeneratedChild(pendingNode);
        }
        pendingElements.clear();
        box.addGeneratedChild(child);
      }
    }

    // Remove all spacers and other non printable content that might
    // look ugly at the beginning of a new line ..
    for (; lastPosition < sequenceFill; lastPosition++)
    {
      final RenderNode node = nodes[lastPosition];
      if (node.isIgnorableForRendering() == false)
      {
        break;
      }
    }

    // If there are open contexts, then add the split-result to the new line
    // and update the width of the current line
    RenderBox previousContext = null;
    final int openContexts = contexts.size();
    for (int i = 0; i < openContexts; i++)
    {
      final RenderBox renderBox = (RenderBox) contexts.get(i);
      renderBox.setCachedWidth(getEndOfLine() - box.getCachedX());

      final InlineRenderBox rightBox = (InlineRenderBox) renderBox.split(RenderNode.HORIZONTAL_AXIS);
      sequenceElements[i] = StartSequenceElement.INSTANCE;
      nodes[i] = rightBox;
      if (previousContext != null)
      {
        previousContext.addGeneratedChild(renderBox);
      }
      previousContext = renderBox;
    }

    final int length = sequenceFill - lastPosition;
    System.arraycopy(sequenceElements, lastPosition, sequenceElements, openContexts, length);
    System.arraycopy(nodes, lastPosition, nodes, openContexts, length);
    sequenceFill = openContexts + length;
    Arrays.fill(sequenceElements, sequenceFill, sequenceElements.length, null);
    Arrays.fill(nodes, sequenceFill, nodes.length, null);

    return firstBox;
  }

  /**
   * Handle the next input chunk.
   *
   * @param start the start index
   * @param count the number of elements in the sequence
   * @return the processing position. Linebreaks will be inserted, if the returned value is equal or less the start
   *         index.
   */
  protected abstract int handleElement(final int start, final int count);

  private int classifyInput(final InlineSequenceElement element)
  {
    if (element instanceof StartSequenceElement)
    {
      return START;
    }
    else if (element instanceof EndSequenceElement)
    {
      return END;
    }
    else
    {
      return CONTENT;
    }
  }


  protected void computeInlineBlock(final RenderBox box,
                                    final long position,
                                    final long itemElementWidth)
  {
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    box.setCachedX(position + blp.getMarginLeft());
    final long width = itemElementWidth - blp.getMarginLeft() - blp.getMarginRight();
    if (width == 0)
    {
      //ModelPrinter.printParents(box);

      throw new IllegalStateException("A box without any width? " +
          Integer.toHexString(System.identityHashCode(box)) + ' ' + box.getClass());
    }
    box.setCachedWidth(width);

    final BoxDefinition bdef = box.getBoxDefinition();
    final long leftInsets = bdef.getPaddingLeft() + blp.getBorderLeft();
    final long rightInsets = bdef.getPaddingRight() + blp.getBorderRight();
    box.setContentAreaX1(box.getCachedX() + leftInsets);
    box.setContentAreaX2(box.getCachedX() + box.getCachedWidth() - rightInsets);

    final InfiniteMinorAxisLayoutStep layoutStep = new InfiniteMinorAxisLayoutStep();
    layoutStep.continueComputation(getPageGrid(), box);
  }

  protected int getSequenceFill()
  {
    return sequenceFill;
  }

  public void performLastLineAlignment()
  {
    Arrays.fill(elementDimensions, 0);
    Arrays.fill(elementPositions, 0);

    int lastPosition = iterate(sequenceElements, sequenceFill);
    if (lastPosition == 0)
    {
      // This could evolve into an infinite loop. Thats evil.
      // We have two choices to prevent that:
      // (1) Try to break the element.
//      if (getBreakableIndex() >= 0)
//      {
//        // Todo: Breaking is not yet implemented ..
//      }
      if (getSkipIndex() >= 0)
      {
        // This causes an overflow ..
        lastPosition = getSkipIndex();
      }
      else
      {
        // Skip the complete line. Oh, thats not good, really!
        lastPosition = sequenceFill;
      }
    }

    // the elements up to the 'lastPosition' are now aligned according to the alignment rules.
    // now, update the element's positions and dimensions ..

    if (lastPosition == sequenceFill || lastLineAlignment)
    {
      // First, the simple case: The line's content did fully fit into the linebox. No linebreaks were necessary.
      RenderBox firstBox = null;
      for (int i = 0; i < lastPosition; i++)
      {
        final RenderNode node = nodes[i];
        final InlineSequenceElement element = sequenceElements[i];
        if (element instanceof EndSequenceElement)
        {
          final long boxX2 = (elementPositions[i] + elementDimensions[i]);
          final RenderBox box = (RenderBox) node;
          box.setCachedWidth(boxX2 - box.getCachedX());
          continue;
        }

        if (element instanceof StartSequenceElement)
        {
          final RenderBox box = (RenderBox) node;
          box.setCachedX(elementPositions[i]);
          if (firstBox == null)
          {
            firstBox = box;
          }
          continue;
        }

        // Content element: Perform a deep-deriveForAdvance, so that we preserve the
        // possibly existing sub-nodes.
        node.setCachedX(elementPositions[i]);
        node.setCachedWidth(elementDimensions[i]);
      }

      return;
    }

    // The second case is more complicated. The text did not fit fully into the text-element.

    // Left align all elements after the layouted content ..
    if (leftAlignProcessor == null)
    {
      leftAlignProcessor = new LeftAlignmentProcessor();
    }
    leftAlignProcessor.initializeForLastLineAlignment(this);
    leftAlignProcessor.performLastLineAlignment();
    leftAlignProcessor.deinitialize();
  }

  public void performSkipAlignment (final int endIndex)
  {
    // Left align all elements after the layouted content ..
    if (leftAlignProcessor == null)
    {
      leftAlignProcessor = new LeftAlignmentProcessor();
    }
    leftAlignProcessor.initializeForSkipAlignment(this, endIndex);
    leftAlignProcessor.performLastLineAlignment();
    leftAlignProcessor.deinitialize();
  }

  protected void initializeForSkipAlignment(final AbstractAlignmentProcessor proc, final int endIndex)
  {
    this.lastLineAlignment = true;
    this.sequenceElements = proc.sequenceElements;
    this.nodes = proc.nodes;
    this.sequenceFill = endIndex;
    this.pageGrid = proc.pageGrid;
    this.elementDimensions = proc.elementDimensions;
    this.elementPositions = proc.elementPositions;
    Arrays.fill(this.elementPositions, 0);
    Arrays.fill(this.elementDimensions, 0);

    this.startOfLine = proc.startOfLine;
    this.endOfLine = proc.endOfLine;
    this.pagebreaks = proc.pagebreaks;
    this.pagebreakCount = proc.pagebreakCount;
  }

  protected void initializeForLastLineAlignment(final AbstractAlignmentProcessor proc)
  {
    this.lastLineAlignment = true;
    this.sequenceElements = proc.sequenceElements;
    this.nodes = proc.nodes;
    this.sequenceFill = proc.sequenceFill;
    this.pageGrid = proc.pageGrid;
    this.elementDimensions = proc.elementDimensions;
    this.elementPositions = proc.elementPositions;
    Arrays.fill(this.elementPositions, 0);
    Arrays.fill(this.elementDimensions, 0);

    this.startOfLine = proc.startOfLine;
    this.endOfLine = proc.endOfLine;

    updateBreaksForLastLineAlignment();
  }

  private void updateBreaksForLastLineAlignment()
  {
    final long[] horizontalBreaks = pageGrid.getHorizontalBreaks();
    final LongList pageLongList = new LongList(horizontalBreaks.length);
    for (int i = 0; i < horizontalBreaks.length; i++)
    {
      final long pos = horizontalBreaks[i];
      if (pos <= startOfLine)
      {
        // skip ..
        continue;
      }
      if (pos >= endOfLine)
      {
        break;
      }
      pageLongList.add(pos);
    }
    //pageLongList.add(endOfLine);
    pageLongList.add(Long.MAX_VALUE);

    this.pagebreaks = pageLongList.toArray(this.pagebreaks);
    this.pagebreakCount = pageLongList.size();
  }
}
