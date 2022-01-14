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
 * CleanPaginatedBoxesStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.FinishedRenderNode;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.util.InstanceID;

/**
 * This step must not remove boxes that have a manual break attached.
 *
 * @author Thomas Morgner
 */
public final class CleanPaginatedBoxesStep extends IterateStructuralProcessStep
{
  private long pageOffset;
  private long shiftOffset;
  private InstanceID shiftNode;

  public CleanPaginatedBoxesStep()
  {
  }

  public long compute(final LogicalPageBox pageBox)
  {
    shiftOffset = 0;
    pageOffset = pageBox.getPageOffset();
    if (startBlockBox(pageBox))
    {
      // not processing the header and footer area: they are 'out-of-context' bands
      processBoxChilds(pageBox);
    }
    finishBlockBox(pageBox);
    //Log.debug ("ShiftOffset after clean: " + shiftOffset);
    return shiftOffset;
  }

  public InstanceID getShiftNode()
  {
    return shiftNode;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // we do not process the paragraph lines. This should have been done
    // in the startblock thing and they get re-added anyway as long as the
    // paragraph is active.
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    return false;
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (box instanceof ParagraphRenderBox)
    {
      return false;
    }

    if (box.isFinished() == false)
    {
      return true;
    }

    final RenderNode firstNode = box.getVisibleFirst();
    if (firstNode == null)
    {
      // The cell is empty ..
      return false;
    }

    final long nodeY = firstNode.getY();
    if (nodeY > pageOffset)
    {
      // This box will be visible or will be processed in the future.
      return false;
    }

    if (firstNode.isOpen())
    {
      return true;
    }

    if ((firstNode.getY() + firstNode.getHeight()) > pageOffset)
    {
      // this box will span to the next page and cannot be removed ...
      return true;
    }

    // Next, search the last node that is fully invisible. We collapse all
    // invisible node into one big box for efficiency reasons. They wont be
    // visible anyway and thus the result will be the same as if they were
    // still alive ..
    RenderNode last = firstNode;
    while (true)
    {
      final RenderNode next = last.getVisibleNext();
      if (next == null)
      {
        break;
      }
      if (next.isOpen())
      {
        // as long as a box is open, it can grow and therefore it cannot be
        // removed ..
        break;
      }

      if ((next.getY() + next.getHeight()) > pageOffset)
      {
        // we cant handle that. This node will be visible. So the current last
        // node is the one we can shrink ..
        break;
      }
      last = next;
    }

    if (last == firstNode)
    {
      if (last instanceof FinishedRenderNode)
      {
        // In this case, we can skip the replace-action below ..
        return true;
      }
    }

    // So lets get started. We remove all nodes between (and inclusive)
    // node and last.
    final long width = box.getContentAreaX2() - box.getContentAreaX1();
    final long lastY2 = last.getY() + last.getHeight();
    final long height = lastY2 - nodeY;

    // make sure that the finished-box inherits the margins ..
    final long marginsTop = firstNode.getEffectiveMarginTop();
    final long marginsBottom = last.getEffectiveMarginBottom();
    final boolean breakAfter = isBreakAfter(last);
    final FinishedRenderNode replacement = new FinishedRenderNode(width, height, marginsTop, marginsBottom, breakAfter);

    RenderNode removeNode = firstNode;
    while (removeNode != last)
    {
      final RenderNode next = removeNode.getNext();
      if (removeNode.isOpen())
      {
        throw new IllegalStateException("A node is still open. We should not have come that far.");
      }
      box.remove(removeNode);
      removeNode = next;
    }

    if (last.isOpen())
    {
      throw new IllegalStateException("The last node is still open. We should not have come that far.");
    }
    box.replaceChild(last, replacement);
    if (replacement.getParent() != box)
    {
//      return true;
      throw new IllegalStateException("The replacement did not work.");
    }

    final long cachedY2 = last.getCachedY() + last.getCachedHeight();
    final long newShift = lastY2 - cachedY2;
    if (newShift > shiftOffset)
    {
      shiftOffset = newShift;
      shiftNode = replacement.getInstanceId();
    }
    return (box.getLastChild() != replacement);
  }


  private boolean isBreakAfter(final RenderNode node)
  {
    if (node.isBreakAfter())
    {
      return true;
    }

    if (node instanceof BlockRenderBox)
    {
      final RenderBox box = (RenderBox) node;
      final RenderNode lastChild = box.getLastChild();
      if (lastChild != null)
      {
        return isBreakAfter(lastChild);
      }
    }
    return false;
  }

}
