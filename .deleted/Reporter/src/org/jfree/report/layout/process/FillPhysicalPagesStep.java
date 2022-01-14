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
 * FillPhysicalPagesStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;

/**
 * This Step copies all content from the logical page into the page-grid. When done, it clears the content and replaces
 * the elements with dummy-nodes. These nodes have a fixed-size (the last known layouted size), and will not be
 * recomputed later.
 * <p/>
 * Adjoining dummy-nodes get unified into a single node, thus simplifying and pruning the document tree.
 *
 * @author Thomas Morgner
 */
public final class FillPhysicalPagesStep extends IterateVisualProcessStep
{
  private long contentEnd;
  private long contentStart;

  public FillPhysicalPagesStep()
  {
  }

  public LogicalPageBox compute(final LogicalPageBox pagebox,
                                final long pageStart,
                                final long pageEnd)
  {
    this.contentStart = pagebox.getHeaderArea().getHeight();
    this.contentEnd = (pageEnd - pageStart) + contentStart;

    // This is a simple strategy.
    // Copy and relocate, then prune. (I whished we could prune first, but
    // this does not work.)
    //
    // For the sake of efficiency, we do *not* create private copies for each
    // physical page. This would be an total overkill.
    final LogicalPageBox derived = (LogicalPageBox) pagebox.derive(true);

    // first, shift the normal-flow content downwards.
    // The start of the logical pagebox might be in the negative range now
    // The header-size has already been taken into account by the pagination
    // step.
    BoxShifter.shiftBoxUnchecked(derived, -pageStart + contentStart);

    // now remove all the content that will not be visible at all ..
    // not processing the header and footer area: they are 'out-of-context' bands
    processBoxChilds(derived);

    // Then add the header at the top - it starts at (0,0) and thus it is
    // ok to leave it unshifted.

    // finally, move the footer at the bottom (to the page's bottom, please!)
    final RenderBox footerArea = derived.getFooterArea();
    final long footerPosition = pagebox.getPageHeight() -
        (footerArea.getY() + footerArea.getHeight());
    final long footerShift = footerPosition - footerArea.getY();
    BoxShifter.shiftBoxUnchecked(footerArea, footerShift);

    // the renderer is responsible for painting the page-header and footer ..

    derived.setPageOffset(0);
    derived.setPageEnd(contentEnd);
    return derived;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }

  /**
   * Invisible nodes may need special treatment here.
   *
   * @param box
   * @return
   */
  protected boolean startBlockLevelBox(final RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering())
      {
        node = node.getNext();
        continue;
      }

      if ((node.getY() + node.getHeight()) <= contentStart)
      {
        final RenderNode next = node.getNext();
        box.remove(node);
        node = next;
      }
      else if (node.getY() >= contentEnd)
      {
        final RenderNode next = node.getNext();
        box.remove(node);
        node = next;
      }
      else
      {
        node = node.getNext();
      }
    }
    return true;
  }

  protected boolean startCanvasLevelBox(final RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering())
      {
        node = node.getNext();
        continue;
      }
      if ((node.getY() + node.getHeight()) <= contentStart)
      {
        final RenderNode next = node.getNext();
        box.remove(node);
        node = next;
      }
      else if (node.getY() >= contentEnd)
      {
        final RenderNode next = node.getNext();
        box.remove(node);
        node = next;
      }
      else
      {
        node = node.getNext();
      }
    }
    return true;
  }
}
