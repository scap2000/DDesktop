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
 * ParagraphLineBreakStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.process.linebreak.EmptyLinebreaker;
import org.jfree.report.layout.process.linebreak.FullLinebreaker;
import org.jfree.report.layout.process.linebreak.ParagraphLinebreaker;
import org.jfree.report.layout.process.linebreak.SimpleLinebreaker;
import org.jfree.util.FastStack;

/**
 * This static computation step performs manual linebreaks on all paragraphs.
 * This transforms the pool-collection into the lines-collection.
 * <p/>
 * For now, we follow a very simple path: A paragraph cannot be validated, if it
 * is not yet closed. The linebreaking, be it the static one here or the dynamic
 * one later, must be redone when the paragraph changes.
 * <p/>
 * Splitting for linebreaks happens only between inline-boxes. BlockBoxes that
 * are contained in inline-boxes (like 'inline-block' elements or
 * 'inline-tables') are considered unbreakable according to the CSS specs.
 * Linebreaking can be suspended in these cases.
 * <p/>
 * As paragraphs itself are block elements, the linebreaks can be done
 * iterative, using a simple stack to store the context of possibly nested
 * paragraphs. The paragraph's pool contains the elements that should be
 * processed, and the line-container will receive the pool's content (contained
 * in an artificial inline element, as the linecontainer is a block-level
 * element).
 * <p/>
 * Change-tracking should take place on the paragraph's pool element instead of
 * the paragraph itself. This way, only structural changes are taken into
 * account.
 *
 * @author Thomas Morgner
 */
public final class ParagraphLineBreakStep extends IterateStructuralProcessStep
{
  private static final EmptyLinebreaker LEAF_BREAK_STATE = new EmptyLinebreaker();

  private FastStack paragraphNesting;
  private ParagraphLinebreaker breakState;

  public ParagraphLineBreakStep()
  {
    paragraphNesting = new FastStack();
  }

  public void compute(final LogicalPageBox root)
  {
    paragraphNesting.clear();
    try
    {
      startProcessing(root);
    }
    finally
    {
      paragraphNesting.clear();
      breakState = null;
    }
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (box instanceof ParagraphRenderBox)
    {
      final ParagraphRenderBox paragraphBox = (ParagraphRenderBox) box;
      final long poolChangeTracker = paragraphBox.getPool().getChangeTracker();
      final boolean unchanged = poolChangeTracker == paragraphBox.getLineBoxAge();

      if (unchanged)
      {
        // If the paragraph is unchanged (no new elements have been added to the pool) then we can take a
        // shortcut. The childs of this paragraph will also be unchanged (as any structural change would have increased
        // the change-tracker).
        paragraphNesting.push(LEAF_BREAK_STATE);
        breakState = LEAF_BREAK_STATE;
        return false;
      }

      // When the paragraph has changed, this can only be caused by someone adding a new node to the paragraph
      // or to one of the childs.

      // Paragraphs can be nested whenever a Inline-Level element declares to be a Block-Layouter. (This is an
      // Inline-Block or Inline-Table case in CSS)

      // It is guaranteed, that if a child is changed, the parent is marked as changed as well.
      // So we have only two cases to deal with: (1) The child is unchanged (2) the child is changed.

      if (breakState == null)
      {
        if (paragraphBox.isComplexParagraph())
        {
          final ParagraphLinebreaker item = new FullLinebreaker(paragraphBox);
          paragraphNesting.push(item);
          breakState = item;
        }
        else
        {
          final ParagraphLinebreaker item = new SimpleLinebreaker(paragraphBox);
          paragraphNesting.push(item);
          breakState = item;
        }
        return true;
      }

      // The breakState indicates that there is a paragraph processing active at the moment. This means, the
      // paragraph-box we are dealing with right now is a nested box.

      if (breakState.isWritable() == false)
      {
        // OK, should not happen, but you never know. I'm good at hiding
       // bugs in the code ..
        throw new IllegalStateException ("A child cannot be dirty, if the parent is clean");
      }

      // The paragraph is somehow nested in an other paragraph.
      // This cannot be handled by the simple implementation, as we will most likely start to deriveForAdvance childs sooner
      // or later
      if (breakState instanceof FullLinebreaker == false)
      {
        // convert it ..
        final FullLinebreaker fullBreaker = breakState.startComplexLayout();
        paragraphNesting.pop();
        paragraphNesting.push(fullBreaker);
        breakState = fullBreaker;
      }

      final ParagraphLinebreaker subFlow = breakState.startParagraphBox(paragraphBox);
      paragraphNesting.push(subFlow);
      breakState = subFlow;
      return true;
    }

    // some other block box ..
    if (breakState == null)
    {
      if (box.getChangeTracker() == box.getCachedAge())
      {
        return false;
      }
      // Not nested in a paragraph, thats easy ..
      return true;
    }

    if (breakState.isWritable() == false)
    {
      throw new IllegalStateException("This cannot be: There is an active break-state, but the box is not writable.");
    }

    breakState.startBlockBox(box);
    return true;
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    if (breakState == null)
    {
      if (box.getChangeTracker() == box.getCachedAge())
      {
        return false;
      }

      return true;
    }

    // some other block box .. suspend.
    if (breakState.isWritable() == false)
    {
      throw new IllegalStateException ("A child cannot be dirty, if the parent is clean");
    }

    breakState.startBlockBox(box);
    return true;
  }

  public void finishCanvasBox(final CanvasRenderBox box)
  {
    if (breakState != null)
    {
      if (breakState.isWritable() == false)
      {
        throw new IllegalStateException ("A child cannot be dirty, if the parent is clean");
      }

      breakState.finishBlockBox(box);
    }
  }

  protected void finishBlockBox(final BlockRenderBox box)
  {
    if (box instanceof ParagraphRenderBox)
    {
      // do the linebreak jiggle ...
      // This is the first test case whether it is possible to avoid
      // composition-recursion on such computations. I'd prefer to have
      // an iterator pattern here ...

      // finally update the change tracker ..
      breakState.finish();
      paragraphNesting.pop();
      if (paragraphNesting.isEmpty())
      {
        breakState = null;
      }
      else
      {
        breakState = (ParagraphLinebreaker) paragraphNesting.peek();
        breakState.finishParagraphBox((ParagraphRenderBox) box);
      }
      return;
    }

    if (breakState == null)
    {
      return;
    }

    if (breakState.isWritable() == false)
    {
      throw new IllegalStateException ("A child cannot be dirty, if the parent is clean");
    }

    breakState.finishBlockBox(box);
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    if (breakState == null || breakState.isWritable() == false)
    {
      if (box.getChangeTracker() == box.getCachedAge())
      {
        return false;
      }
      return true;
    }

    breakState.startInlineBox(box);
    return true;
  }

  protected void finishInlineBox(final InlineRenderBox box)
  {
    if (breakState == null || breakState.isWritable() == false)
    {
      return;
    }

    breakState.finishInlineBox(box);
    if (breakState.isBreakRequested() && box.getNext() != null)
    {
      performBreak();
    }
  }

  protected void processOtherNode(final RenderNode node)
  {
    if (breakState == null || breakState.isWritable() == false)
    {
      return;
    }
    if (breakState.isSuspended() || node instanceof RenderableText == false)
    {
      breakState.addNode(node);
      return;
    }

    final RenderableText text = (RenderableText) node;
    breakState.addNode(text);
    if (text.isForceLinebreak() == false)
    {
      return;
    }

    // OK, someone requested a manual linebreak.
    // Fill a stack with the current context ..
    // Check if we are at the end of the line
    if (node.getNext() == null)
    {
      // OK, if we are at the end of the line (for all contexts), so we
      // dont have to perform a break. The text will end anyway ..
      if (isEndOfLine(node))
      {
        return;
      }

      // as soon as we are no longer the last element - break!
      // According to the flow rules, that will happen in one of the next
      // finishInlineBox events ..
      breakState.setBreakRequested(true);
      return;
    }

    performBreak();
  }

  private boolean isEndOfLine(final RenderNode node)
  {
    boolean endOfLine = true;
    RenderBox parent = node.getParent();
    while (parent != null)
    {
      if (parent instanceof InlineRenderBox == false)
      {
        break;
      }
      if (parent.getNext() != null)
      {
        endOfLine = false;
        break;
      }
      parent = parent.getParent();
    }
    return endOfLine;
  }

  private void performBreak()
  {
    if (breakState instanceof FullLinebreaker == false)
    {
      final FullLinebreaker fullBreaker = breakState.startComplexLayout();
      paragraphNesting.pop();
      paragraphNesting.push(fullBreaker);
      breakState = fullBreaker;

      fullBreaker.performBreak();
    }
    else
    {
      final FullLinebreaker fullBreaker = (FullLinebreaker) breakState;
      fullBreaker.performBreak();
    }
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    if (breakState == null)
    {
      return false;
    }

    if (breakState.isWritable() == false)
    {
      return false;
    }

    breakState.startBlockBox(box);
    return true;
  }

  protected void finishOtherBox(final RenderBox box)
  {
    if (breakState != null && breakState.isWritable())
    {
      breakState.finishBlockBox(box);
    }
  }
}
