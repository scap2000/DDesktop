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
 * CleanFlowBoxesStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import java.util.HashMap;

import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.FinishedRenderNode;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.util.InstanceID;
import org.jfree.util.FastStack;

/**
 * Removed finished block-boxes. The boxes have to be marked as 'finished' by the flow output target or nothing will be
 * removed at all. The boxes marked as finished will be replaced by 'FinishedRenderNodes'. This step preserves nodes
 * that have pagebreaks.
 *
 * @author Thomas Morgner
 */
public final class CleanFlowBoxesStep extends IterateStructuralProcessStep
{
  private HashMap finishContexts;
  private InstanceID canvasProcessingId;
  private FastStack blockContexts;
  private LogicalPageBox pageBox;

  public CleanFlowBoxesStep()
  {
    finishContexts = new HashMap();
    blockContexts = new FastStack();
  }

  public void compute(final LogicalPageBox pageBox)
  {
    this.pageBox = pageBox;
    //Log.debug ("START CLEAR");
    finishContexts.clear();
    blockContexts.clear();
    if (startBlockBox(pageBox))
    {
      // not processing the header and footer area: they are 'out-of-context' bands
      processBoxChilds(pageBox);
    }
    finishBlockBox(pageBox);
    finishContexts.clear();
    blockContexts.clear();
    this.pageBox = null;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // we do not process the paragraph lines. This should have been done
    // in the startblock thing and they get re-added anyway as long as the
    // paragraph is active.
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    // it is guaranteed that the finished flag is only set to true, if the box is closed.
    if (box.isFinished() == false || box.isCommited() == false)
    {
      if (box.getParent() != null)
      {
        finishContexts.put(box.getParent().getInstanceId(), Boolean.FALSE);
      }
    }

    if (canvasProcessingId == null)
    {
      canvasProcessingId = box.getInstanceId();
    }

    finishContexts.put(box.getInstanceId(), Boolean.TRUE);
    return true;
  }

  public void finishCanvasBox(final CanvasRenderBox box)
  {
    if (canvasProcessingId == box.getInstanceId())
    {
      canvasProcessingId = null;
    }

    if (box.isFinished() == false || box.isCommited() == false)
    {
      finishContexts.remove(box.getInstanceId());
      return;
    }
    final Boolean finishedFlag = (Boolean) finishContexts.get(box.getInstanceId());
    if (Boolean.FALSE.equals(finishedFlag))
    {
      finishContexts.put(box.getParent().getInstanceId(), Boolean.FALSE);
    }
    else
    {
      // The whole box and all childs are finished. We could now safely remove the box.
      // (We only remove blocklevel boxes to avoid layouting-troubles, but *we could*.
    }
    finishContexts.remove(box.getInstanceId());
  }

  // We cannot clear the box until we have verified that all childs of that box have been cleared.


  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (box.isFinished() == false)
    {
      if (box.getParent() != null)
      {
        finishContexts.put(box.getParent().getInstanceId(), Boolean.FALSE);
      }
      finishContexts.put(box.getInstanceId(), Boolean.FALSE);
    }
    else
    {
      finishContexts.put(box.getInstanceId(), Boolean.TRUE);
    }
    return true;
  }

  protected void finishBlockBox(final BlockRenderBox box)
  {
//    if (box.isFinished() == false)
//    {
//      return;
//    }


    final Boolean finishedFlag = (Boolean) finishContexts.get(box.getInstanceId());
    if (Boolean.FALSE.equals(finishedFlag))
    {
      if (box.getParent() != null)
      {
        finishContexts.put(box.getParent().getInstanceId(), Boolean.FALSE);
      }
      //  Log.debug ("Not removing box " + box + " as this box is not finished.");
      box.setDeepFinished(false);
    }
    else
    {
      box.setDeepFinished(true);
    }
    
    finishContexts.remove(box.getInstanceId());
    if (canvasProcessingId != null)
    {
      return;
    }

    final RenderNode first = box.getFirstChild();
    if (first == null)
    {
      return;
    }
    if (first.isFinished() == false)
    {
      return;
    }
    if (first instanceof RenderBox)
    {
      final RenderBox nextBox = (RenderBox) first;
      if (nextBox.isDeepFinished() == false)
      {
        return;
      }
    }

    RenderNode last = first;
    while (true)
    {
      final RenderNode next = last.getNext();
      if (next == null)
      {
        break;
      }
      if (next instanceof RenderBox)
      {
        final RenderBox nextBox = (RenderBox) next;
        if (next.isFinished() == false &&
            nextBox.isDeepFinished() == false)
        {
          break;
        }
      }
      last = next;
    }

    if (last == first &&
        first instanceof FinishedRenderNode)
    {
      // In this case, we can skip the replace-action below ..
      return;
    }

    // So lets get started. We remove all nodes between (and inclusive)
    // node and last.
    final long nodeY = first.getY();
    final long width = box.getContentAreaX2() - box.getContentAreaX1();
    final long lastY2 = last.getY() + last.getHeight();
    final long height = lastY2 - nodeY;

    // make sure that the finished-box inherits the margins ..
    final long marginsTop = first.getEffectiveMarginTop();
    final long marginsBottom = last.getEffectiveMarginBottom();
    final boolean breakAfter = last.isBreakAfter();
    final FinishedRenderNode replacement =
        new FinishedRenderNode(width, height, marginsTop, marginsBottom, breakAfter);

//    Log.debug (" (" + last.getInstanceId() + ") " +
//        (box.getLastChild() == last) + " " + (box.getFirstChild() == first));
    int counter = 0;
    RenderNode removeNode = first;
    while (removeNode != last)
    {
      final RenderNode next = removeNode.getNext();
      if (removeNode.isOpen())
      {
        throw new IllegalStateException("A node is still open. We should not have come that far.");
      }
      box.remove(removeNode);
      removeNode = next;
      counter += 1;
    }

    if (last.isOpen())
    {
      throw new IllegalStateException("The last node is still open. We should not have come that far.");
    }
    counter += 1;
    box.replaceChild(last, replacement);
    if (replacement.getParent() != box)
    {
      throw new IllegalStateException("The replacement did not work.");
    }
//    Log.debug ("Removed " + counter + " nodes from " + box.getName() + " " + last.getName());
  }
}
