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
 * FlowPaginationStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.layout.ModelPrinter;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PageBreakPositionList;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;

/**
 * The flow-pagination is a pagination step, where the page-boundaries cannot be determined beforehand. It only works
 * for infinite size pages and ignores all page-header and footers.
 * <p/>
 * The page-break list is updated on the fly while the report is paginated. A new break will only be added if the old
 * list did not contain the new break. It is guaranteed that only one break is added on every run.
 * <p/>
 * If complex compound layouts are required, this pagination step must be followed by a classical pagination step so
 * that boxes that overlap a break-position get shifted accordingly.
 *
 * @author Thomas Morgner
 */
public final class FlowPaginationStep extends IterateVisualProcessStep
{
  private boolean breakPending;
  private PageBreakPositionList breakUtility;
  private boolean breakAdded;
  private Object finalVisibleState;
  private long pageEnd;

  public FlowPaginationStep()
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
        ModelPrinter.print(pageBox);
        throw new IllegalStateException
            ("Assertation failed: Block layouting did not proceed: " + lastChildY2 + " < " + pageBox.getHeight());
      }
    }

    this.breakPending = false;
    this.breakAdded = false;
    this.finalVisibleState = null;
    this.pageEnd = pageBox.getHeight();

    try
    {
      final PageBreakPositionList allPreviousBreak = pageBox.getAllVerticalBreaks();

      // Note: For now, we limit both the header and footer to a single physical
      // page. This safes me a lot of trouble for now.
      final PageBreakPositionList breaks = new PageBreakPositionList(allPreviousBreak, 1);
      breakUtility = breaks;
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
      final boolean nextPageContainsContent = (pageBox.getHeight() > masterBreak);
      return new PaginationResult(breaks, breakAdded, nextPageContainsContent, finalVisibleState);
    }
    finally
    {
      breakUtility = null;
    }
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    final int breakIndicator = box.getManualBreakIndicator();

    // First check the simple cases:
    // If the box wants to break, then there's no point in waiting: Shift the box and continue.
    if (breakIndicator == RenderBox.DIRECT_MANUAL_BREAK || breakPending)
    {
      // find the next major break and shift the box to this position.
      // update the 'shift' to reflect this new change. Process the contents of this box as well, as the box may
      // have additional breaks inside (or may overflow, or whatever ..).
      final long boxY = box.getY();
      if (breakAdded == false &&
          boxY != pageEnd &&
          boxY > breakUtility.getLastMasterBreak())
      {
        // This box will cause a new break. Add it.
        breakUtility.addMajorBreak(box.getY(), 0);
        breakAdded = true;
      }
      breakPending = false;
      return true;
    }

    if (breakIndicator == RenderBox.NO_MANUAL_BREAK)
    {
      // As neither this box nor any of the children will cause a pagebreak, skip the processing of the childs.
      if (breakAdded == false)
      {
        updateStateKeyDeep(box);
      }
      return false;
    }

    // One of the children of this box will cause a manual pagebreak. We have to dive deeper into this child.
    // for now, we will only apply the ordinary shift.
    if (breakAdded == false)
    {
      updateStateKey(box);
    }
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
        this.finalVisibleState = stateKey;
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
        this.finalVisibleState = stateKey;
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
  
  protected void finishBlockLevelBox(final RenderBox box)
  {
    if (breakPending == false && box.isBreakAfter())
    {
      breakPending = true;
    }
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    return false;
  }

  // At a later point, we have to do some real page-breaking here. We should check, whether the box fits, and should
  // shift the box if it doesnt.
  protected boolean startCanvasLevelBox(final RenderBox box)
  {
    return false;
  }
}
