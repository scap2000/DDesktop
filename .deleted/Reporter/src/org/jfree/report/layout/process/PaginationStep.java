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
 * PaginationSet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.BreakMarkerRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PageBreakPositionList;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderLength;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;

/**
 * Creation-Date: 11.04.2007, 14:23:34
 *
 * @author Thomas Morgner
 */
public final class PaginationStep extends IterateVisualProcessStep
{
  private long shift;
  private boolean breakPending;
  private PageBreakPositionList breakUtility;
  private long pageHeight;
  private long pageEnd;
  private Object visualState;
  private boolean breakIndicatorEncountered;

  public PaginationStep()
  {
  }

  public PaginationResult performPagebreak(final LogicalPageBox pageBox)
  {
    final RenderNode lastChild = pageBox.getLastChild();
    if (lastChild != null)
    {
      final long lastChildY2 = lastChild.getY() + lastChild.getHeight();
      if (lastChildY2 < pageBox.getHeight())
      {
        //ModelPrinter.print(pageBox);
        throw new IllegalStateException
            ("Assertation failed: Block layouting did not proceed: " + lastChildY2 + " < " + pageBox.getHeight());
      }
    }

    this.breakPending = false;
    this.shift = 0;
    this.pageHeight = pageBox.getPageHeight();
    this.breakIndicatorEncountered = false;

    try
    {
      final PageBreakPositionList allPreviousBreak = pageBox.getAllVerticalBreaks();
      final long[] allCurrentBreaks = pageBox.getPhysicalBreaks(RenderNode.VERTICAL_AXIS);
      final long pageOffset = pageBox.getPageOffset();

      if (allCurrentBreaks.length == 0)
      {
        // No maximum height.
        throw new IllegalStateException("No page given. This is really bad.");
      }

      // Note: For now, we limit both the header and footer to a single physical
      // page. This safes me a lot of trouble for now.

      final BlockRenderBox headerArea = pageBox.getHeaderArea();
      final long headerHeight = Math.min(headerArea.getHeight(), allCurrentBreaks[0]);
      headerArea.setHeight(headerHeight);

      final long lastBreakLocal = allCurrentBreaks[allCurrentBreaks.length - 1];
      final BlockRenderBox footerArea = pageBox.getFooterArea();
      long footerHeight = footerArea.getHeight();
      if (allCurrentBreaks.length > 1)
      {
        final long lastPageHeight = lastBreakLocal - allCurrentBreaks[allCurrentBreaks.length - 2];
        footerHeight = Math.min(footerHeight, lastPageHeight);
        footerArea.setHeight(footerHeight);
      }

      // Assertation: Make sure that we do not run into a infinite loop..
      if (headerHeight + footerHeight >= lastBreakLocal)
      {
        // This is also bad. There will be no space left to print a single element.
        throw new IllegalStateException("Header and footer consume the whole page. No space left for normal-flow.");
      }

      final PageBreakPositionList breaks = new PageBreakPositionList(allPreviousBreak, allCurrentBreaks.length + 1);

      // Then add all new breaks (but take the header and footer-size into account) ..
      if (allCurrentBreaks.length == 1)
      {
        breaks.addMajorBreak(pageOffset, headerHeight);
        breaks.addMajorBreak((lastBreakLocal - footerHeight - headerHeight) + pageOffset, headerHeight);
      }
      else // more than one physical page; therefore header and footer are each on a separate canvas ..
      {
        breaks.addMajorBreak(pageOffset, headerHeight);
        for (int i = 1; i < allCurrentBreaks.length - 1; i++)
        {
          final long aBreak = allCurrentBreaks[i];
          breaks.addMinorBreak(pageOffset + (aBreak - headerHeight));
        }
        breaks.addMajorBreak(pageOffset + (lastBreakLocal - headerHeight - footerHeight), headerHeight);
      }

      pageEnd = breaks.getLastMasterBreak();
      breakUtility = breaks;
      visualState = null;
      // now process all the other content (excluding the header and footer area)
      if (startBlockLevelBox(pageBox))
      {
        processBoxChilds(pageBox);
      }
      finishBlockLevelBox(pageBox);

      if (lastChild != null)
      {
        final long lastChildY2 = lastChild.getY() + lastChild.getHeight();
        if (lastChildY2 < pageBox.getHeight())
        {
          throw new IllegalStateException
              ("Assertation failed: Pagination violated block-constraints: " + lastChildY2 + " < " + pageBox.getHeight());
        }
      }

      final long masterBreak = breaks.getLastMasterBreak();
      final boolean overflow =  breakIndicatorEncountered ||
          pageBox.getHeight() > masterBreak;
      final boolean nextPageContainsContent = (pageBox.getHeight() > masterBreak);
      return new PaginationResult (breaks, overflow, nextPageContainsContent, visualState);
    }
    finally
    {
      breakUtility = null;
      visualState = null;
    }
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }


  protected boolean startBlockLevelBox(final RenderBox box)
  {
    if (box instanceof BreakMarkerRenderBox)
    {
      breakIndicatorEncountered = true;
    }
    else
    {
      breakIndicatorEncountered = false;
    }

    if (box.isFinished() == false)
    {
      if (box.isCommited())
      {
        box.setFinished(true);
      }
      else
      {
        final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
        if (sblp.isAvoidPagebreakInside() || sblp.getWidows() > 0 || sblp.getOrphans() > 0)
        {
          // Check, whether this box sits on a break-position. In that case, we can call that box finished as well.
          final long boxY = box.getY();
          final long nextMinorBreak = breakUtility.findNextBreakPosition(boxY + shift);
          final long spaceAvailable = nextMinorBreak - (boxY + shift);

          // This box sits directly on a pagebreak. No matter how much content we fill in the box, it will not move.
          // This makes this box a finished box.
          if (spaceAvailable == 0)
          {
            box.setFinished(true);
          }
        }
        else
        {
          // This box defines no constraints that would cause a shift of it later in the process. We can treat it as
          // if it is finished already ..
          box.setFinished(true);
        }
      }
    }

    final int breakIndicator = box.getManualBreakIndicator();

    // First check the simple cases:
    // If the box wants to break, then there's no point in waiting: Shift the box and continue.
    final RenderLength fixedPosition = box.getBoxDefinition().getFixedPosition();

    final long fixedPositionResolved = fixedPosition.resolve(pageHeight, 0);
    if (breakIndicator == RenderBox.DIRECT_MANUAL_BREAK || breakPending)
    {
      // find the next major break and shift the box to this position.
      // update the 'shift' to reflect this new change. Process the contents of this box as well, as the box may
      // have additional breaks inside (or may overflow, or whatever ..).
      final long boxY = box.getY();
      final long shiftedBoxY = boxY + shift;
      final long nextMajorBreak = breakUtility.findNextMajorBreakPosition(shiftedBoxY) + fixedPositionResolved;
      if (nextMajorBreak < shiftedBoxY)
      {
        // This band will be outside the last pagebreak. We can only shift it normally, but there is no way
        // that we could shift it to the final position yet.
        box.setY(boxY + shift);
      }
      else
      {
        final long nextShift = nextMajorBreak - boxY;
        final long shiftDelta = nextShift - shift;
        box.setY(boxY + nextShift);
        BoxShifter.extendHeight(box.getParent(), shiftDelta);
        shift = nextShift;
      }
      updateStateKey(box);
      breakPending = false;
      return true;
    }


    // If this box does not cross any (major or minor) break, it may need no additional shifting at all.
    if (RenderLength.AUTO.equals(fixedPosition))
    {
      if (breakUtility.isCrossingPagebreak(box, shift) == false)
      {
        // The whole box fits on the current page. No need to do anything fancy.
        if (breakIndicator == RenderBox.NO_MANUAL_BREAK)
        {
          // As neither this box nor any of the children will cause a pagebreak, we can shift them and skip the processing
          // from here.
          BoxShifter.shiftBox(box, shift);
          updateStateKeyDeep(box);
          return false;
        }
        if (breakIndicator == RenderBox.INDIRECT_MANUAL_BREAK)
        {
          // One of the children of this box will cause a manual pagebreak. We have to dive deeper into this child.
          // for now, we will only apply the ordinary shift.
          final long boxY = box.getY();
          box.setY(boxY + shift);
          updateStateKey(box);
          return true;
        }
        throw new IllegalStateException("The box contains an invalid BreakIndicator.");
      }


      // At this point we know, that the box may cause some shifting. It crosses at least one minor or major pagebreak.
      // Right now, we are just evaluating the next break. In a future version, we could search all possible break
      // positions up to the next major break.
      final long boxY = box.getY();

      final long nextMinorBreak = breakUtility.findNextBreakPosition(boxY + shift);
      final long spaceAvailable = nextMinorBreak - (boxY + shift);

      // This box sits directly on a pagebreak. This means, the page is empty, and there is no need for additional
      // shifting.
      if (spaceAvailable == 0)
      {
        box.setY(boxY + shift);
        updateStateKey(box);
        return true;
      }

      final long spaceConsumed = computeUsedBoxHeight(box);
      if (spaceAvailable < spaceConsumed)
      {
        // So we have not enough space to fullfill the layout-constraints. Be it so. Lets shift the box to the next
        // break.
        final long nextShift = nextMinorBreak - boxY;
        final long shiftDelta = nextShift - shift;
        box.setY(boxY + nextShift);
        BoxShifter.extendHeight(box.getParent(), shiftDelta);
        shift = nextShift;
        updateStateKey(box);
        return true;
      }

      // OK, there *is* enough space available. Start the normal processing
      box.setY(boxY + shift);
      updateStateKey(box);
      return true;
    }

    // If you've come this far, this means, that your box has a fixed position defined.

    final long boxY = box.getY();
    final long shiftedBoxPosition = boxY + shift;
    final long fixedPositionInFlow = breakUtility.computeFixedPositionInFlow(shiftedBoxPosition, fixedPositionResolved);
    final long fixedPositionDelta = fixedPositionInFlow - shiftedBoxPosition;
    if (breakUtility.isCrossingPagebreakWithFixedPosition
        (shiftedBoxPosition, box.getHeight(), fixedPositionResolved) == false)
    {
      // The whole box fits on the current page. However, we have to apply the shifting to move the box
      // to its defined fixed-position.
      if (breakIndicator == RenderBox.NO_MANUAL_BREAK)
      {
        // As neither this box nor any of the children will cause a pagebreak, we can shift them and skip the processing
        // from here.
        BoxShifter.shiftBox(box, fixedPositionDelta);
        BoxShifter.extendHeight(box.getParent(), fixedPositionDelta);
        updateStateKeyDeep(box);
        return false;
      }
      if (breakIndicator == RenderBox.INDIRECT_MANUAL_BREAK)
      {
        // One of the children of this box will cause a manual pagebreak. We have to dive deeper into this child.
        // for now, we will only apply the ordinary shift.
        box.setY(fixedPositionInFlow);
        shift += fixedPositionDelta;
        BoxShifter.extendHeight(box.getParent(), fixedPositionDelta);
        updateStateKey(box);
        return true;
      }
      throw new IllegalStateException("The box contains an invalid BreakIndicator.");
    }

    // A box with a fixed position will always be printed at this position, even if it does not seem
    // to fit there. If we move the box, we would break the explict layout constraint 'fixed-position' in
    // favour of an implict one ('page-break: avoid').

    final long nextMinorBreak = breakUtility.findNextBreakPosition(fixedPositionInFlow);
    final long spaceAvailable = nextMinorBreak - fixedPositionInFlow;

    // This box sits directly on a pagebreak. This means, the current page is empty, and there is no need for additional
    // shifting.
    if (spaceAvailable == 0)
    {
      shift += fixedPositionDelta;
      box.setY(fixedPositionInFlow);
      BoxShifter.extendHeight(box.getParent(), fixedPositionDelta);
      updateStateKey(box);
      return true;
    }

    final long spaceConsumed = computeUsedBoxHeight(box);
    if (spaceAvailable < spaceConsumed)
    {
      // So we have not enough space to fullfill the layout-constraints. Be it so. Lets shift the box to the next
      // break.
      final long nextShift = nextMinorBreak - boxY;
      final long shiftDelta = nextShift - shift;
      box.setY(boxY + nextShift);
      BoxShifter.extendHeight(box.getParent(), shiftDelta);
      shift = nextShift;
      updateStateKey(box);
      return true;
    }

    // OK, there *is* enough space available. Start the normal processing
    shift += fixedPositionDelta;
    box.setY(fixedPositionInFlow);
    BoxShifter.extendHeight(box.getParent(), fixedPositionDelta);
    updateStateKey(box);
    return true;
  }

  private void updateStateKey(final RenderBox box)
  {
    final long y = box.getY();
    if (y < (pageEnd))
    {
      final Object stateKey = box.getStateKey();
      if (stateKey != null)
      {
//        Log.debug ("Updating state key: " + stateKey);
        this.visualState = stateKey;
      }
//      else
//      {
//        Log.debug ("No key: " + y + " <= " + (pageOffset + pageHeight));
//      }
    }
//    else
//    {
//      Log.debug ("Not in Range: " + y + " <= " + (pageOffset + pageHeight));
//    }
  }

  private boolean updateStateKeyDeep(final RenderBox box)
  {
    final long y = box.getY();
    if (y < (pageEnd))
    {
      final Object stateKey = box.getStateKey();
      if (stateKey != null)
      {
//        Log.debug ("Deep: Updating state key: " + stateKey);
        this.visualState = stateKey;
        return true;
      }
      else
      {
        RenderNode lastChild = box.getLastChild();
        while (lastChild != null)
        {
          if (lastChild instanceof RenderBox == false)
          {
            lastChild = lastChild.getPrev();
            continue;
          }
          final RenderBox lastBox = (RenderBox) lastChild;
          if (updateStateKeyDeep(lastBox))
          {
            return true;
          }
          lastChild = lastBox.getPrev();
        }
        return false;
      }
    }
    else
    {
//      Log.debug ("Deep: Not in Range: " + y + " <= " + (pageOffset + pageHeight));
      return false;
    }
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    if (node instanceof RenderableReplacedContent == false)
    {
      node.setY(node.getY() + shift);
      if (breakPending == false && node.isBreakAfter())
      {
        breakPending = true;
      }
      return;
    }

    // Check, whether the replaced content will fit. If it doesnt fit, we may have to break it. As this is
    // replaced content, we can't come up with a sane break-rule and have to assume that the content can break
    // anyway.
    final RenderableReplacedContent rpc = (RenderableReplacedContent) node;
    if (rpc.isAvoidPagebreaksInside() == false)
    {
      node.setY(node.getY() + shift);
      return;
    }

    // So we have not enough space to fullfill the layout-constraints. Be it so. Lets shift the box to the next
    // break.
    final long boxY = node.getY();
    final long nextMinorBreak = breakUtility.findNextBreakPosition(boxY + shift);
    final long nextShift = nextMinorBreak - boxY;
    final long shiftDelta = nextShift - shift;
    node.setY(boxY + nextShift);
    BoxShifter.extendHeight(node.getParent(), shiftDelta);
    shift = nextShift;
  }


  protected void finishBlockLevelBox(final RenderBox box)
  {
    if (breakPending == false && box.isBreakAfter())
    {
      breakPending = true;
    }
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    breakIndicatorEncountered = false;
    BoxShifter.shiftBox(box, shift);
    return false;
  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    node.setY(node.getY() + shift);
  }

  // At a later point, we have to do some real page-breaking here. We should check, whether the box fits, and should
  // shift the box if it doesnt.
  protected boolean startCanvasLevelBox(final RenderBox box)
  {
    breakIndicatorEncountered = false;
    box.setY(box.getY() + shift);
    return true;
  }

  protected void processCanvasLevelNode(final RenderNode node)
  {
    breakIndicatorEncountered = false;
    node.setY(node.getY() + shift);
  }

  /**
   * Computes the height that will be required on this page to display at least some parts of the box.
   *
   * @param box
   * @return
   */
  private long computeUsedBoxHeight(final RenderBox box)
  {
    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    if (sblp.isAvoidPagebreakInside())
    {
      return box.getHeight();
    }

    if (box instanceof BlockRenderBox == false)
    {
      // Canvas and inline-boxes have no notion of lines, and therefore they cannot have orphans and widows.
      return 0;
    }

    final int orphans = sblp.getOrphans();
    final int widows = sblp.getWidows();
    if (orphans == 0 && widows == 0)
    {
      // Widows and orphans will be ignored if both of them are zero.
      return 0;
    }

    int counter = 0;
    RenderNode child = box.getFirstChild();
    while (child != null && counter < orphans)
    {
      counter += 1;
      child = child.getNext();
    }

    if (child == null)
    {
      return box.getHeight();
    }
    final long orphanHeight = box.getY() - (child.getY() + child.getHeight());

    counter = 0;
    child = box.getLastChild();
    while (child != null && counter < orphans)
    {
      counter += 1;
      child = child.getPrev();
    }

    if (child == null)
    {
      return box.getHeight();
    }
    final long widowHeight = (box.getY() + box.getHeight()) - (child.getY());

    // todo: Compute the height the orphans and widows consume.
    return Math.max (orphanHeight, widowHeight);
  }

}
