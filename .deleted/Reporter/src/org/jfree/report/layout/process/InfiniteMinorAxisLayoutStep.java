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
 * InfiniteMinorAxisLayoutStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.model.FinishedRenderNode;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PageGrid;
import org.jfree.report.layout.model.ParagraphPoolBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.model.SpacerRenderNode;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;
import org.jfree.report.layout.process.alignment.CenterAlignmentProcessor;
import org.jfree.report.layout.process.alignment.LeftAlignmentProcessor;
import org.jfree.report.layout.process.alignment.RightAlignmentProcessor;
import org.jfree.report.layout.process.alignment.TextAlignmentProcessor;
import org.jfree.report.layout.process.layoutrules.EndSequenceElement;
import org.jfree.report.layout.process.layoutrules.InlineBoxSequenceElement;
import org.jfree.report.layout.process.layoutrules.InlineNodeSequenceElement;
import org.jfree.report.layout.process.layoutrules.ReplacedContentSequenceElement;
import org.jfree.report.layout.process.layoutrules.SequenceList;
import org.jfree.report.layout.process.layoutrules.SpacerSequenceElement;
import org.jfree.report.layout.process.layoutrules.StartSequenceElement;
import org.jfree.report.layout.process.layoutrules.TextSequenceElement;
import org.jfree.util.Log;

/**
 * This process-step computes the effective layout, but it does not take horizontal pagebreaks into account. (It has to
 * deal with vertical breaks, as they affect the text layout.)
 * <p/>
 * This processing step does not ajust anything on the vertical axis. Vertical alignment is handled in a second step.
 * <p/>
 * Please note: This layout model (unlike the default CSS model) uses the BOX-WIDTH as computed with. This means, the
 * defined width specifies the sum of all borders, paddings and the content area width.
 *
 * @author Thomas Morgner
 */
public final class InfiniteMinorAxisLayoutStep extends IterateVisualProcessStep
{

  private MinorAxisParagraphBreakState breakState;
  private PageGrid pageGrid;
  private RenderBox continuedElement;

  private TextAlignmentProcessor centerProcessor;
  private TextAlignmentProcessor rightProcessor;
  private TextAlignmentProcessor leftProcessor;

  public InfiniteMinorAxisLayoutStep()
  {
    breakState = new MinorAxisParagraphBreakState();
  }

  public void compute(final LogicalPageBox root)
  {
    try
    {
      continuedElement = null;
      pageGrid = root.getPageGrid();
      startProcessing(root);
    }
    finally
    {
      continuedElement = null;
      pageGrid = null;
      breakState.deinit();
    }
  }

  /**
     * Continues processing. The renderbox must have a valid minX-layout (that is: X, content-X1, content-X2 and Width)
     *
     * @param pageGrid
     * @param box
     */
  public void continueComputation(final PageGrid pageGrid,
                                  final RenderBox box)
  {
    if (box.getContentAreaX2() == 0 || box.getCachedWidth() == 0)
    {
      throw new IllegalStateException("Box must be layouted a bit ..");
    }

    try
    {
      this.pageGrid = pageGrid;
      this.breakState.deinit();
      this.continuedElement = box;
      startProcessing(box);
    }
    finally
    {
      this.continuedElement = null;
      this.pageGrid = null;
      this.breakState.deinit();
    }
  }

  /**
     * The whole computation is only done for exactly one nesting level of paragraphs. If we encounter an inline-block or
     * inline-destTable, we handle them as a single element.
     *
     * @param box
     * @return
     */
  protected boolean startBlockLevelBox(final RenderBox box)
  {
    // first, compute the position. The position is global, not relative to a
    // parent or so. Therefore a child has no connection to the parent's
    // effective position, when it is painted.

    if (breakState.isActive() == false)
    {
      if (box.isCacheValid())
      {
        return false;
      }

      computeContentArea(box);

      if (box instanceof ParagraphRenderBox)
      {

        final ParagraphRenderBox paragraphBox = (ParagraphRenderBox) box;

        if (continuedElement == null)
        {
          final long lineBoxChangeTracker;
          if (paragraphBox.isComplexParagraph())
          {
            lineBoxChangeTracker = paragraphBox.getLineboxContainer().getChangeTracker();
          }
          else
          {
            lineBoxChangeTracker = paragraphBox.getPool().getChangeTracker();
          }

          final boolean unchanged = lineBoxChangeTracker == paragraphBox.getMinorLayoutAge();
          if (unchanged)
          {
            return false;
          }
        }

        paragraphBox.clearLayout();
        breakState.init(paragraphBox);
      }
      return true;
    }


    if (breakState.isSuspended() == false)
    {
      // The break-state exists only while we are inside of an paragraph
      // and suspend can only happen on inline elements.
      // A block-element inside a paragraph cannot be (and if it does, it is
      // a bug)
      throw new IllegalStateException("This cannot be.");
    }

    // this way or another - we are suspended now. So there is no need to look
    // at the children anymore ..
    return false;
  }

  protected void finishBlockLevelBox(final RenderBox box)
  {
    // Todo: maybe it would be very wise if we dont extend the box later on.
    // Heck, lets see whether this causes great pain ...
    verifyContentWidth(box);

    if (breakState.isActive())
    {
      final Object suspender = breakState.getSuspendItem();
      if (box.getInstanceId() == suspender)
      {
        breakState.setSuspendItem(null);
        return;
      }
      if (suspender != null)
      {
        return;
      }

      if (box instanceof ParagraphRenderBox)
      {
        // finally update the change tracker ..
        final ParagraphRenderBox paraBox = (ParagraphRenderBox) box;
        if (paraBox.isComplexParagraph())
        {
          paraBox.setMinorLayoutAge(paraBox.getLineboxContainer().getChangeTracker());
        }
        else
        {
          paraBox.setMinorLayoutAge(paraBox.getPool().getChangeTracker());
        }

        breakState.deinit();
      }
    }

  }

  /**
   * Computes the effective content area. The content area is the space that can be used by any of the childs of the
   * given box.
   * <p/>
   * InlineBoxes get computed in the alignment processor.
   *
   * @param box the block render box for which we compute the content area
   */
  private void computeContentArea(final RenderBox box)
  {
    if (box == continuedElement)
    {
      return;
    }

    final BoxDefinition bdef = box.getBoxDefinition();
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final long x = box.getComputedX();
    box.setCachedX(x);
    // next, compute the width ...

    final long leftPadding = blp.getBorderLeft() + bdef.getPaddingLeft();
    final long rightPadding = blp.getBorderRight() + bdef.getPaddingRight();
    box.setContentAreaX1(x + leftPadding);
    box.setContentAreaX2(x + box.getComputedWidth() - rightPadding);
  }

  /**
   * Verifies the content width and produces the effective box width. (is called from finishBLock and finishCanvas)
   *
   * @param box
   */
  private void verifyContentWidth(final RenderBox box)
  {
    final long x = box.getCachedX();
    final long contentEnd = box.getContentAreaX2();
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final BoxDefinition bdef = box.getBoxDefinition();
    final long boxEnd = contentEnd + blp.getBorderRight() + bdef.getPaddingRight();
    box.setCachedWidth(boxEnd - x);
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    if (breakState.isActive() == false)
    {
      // ignore .. should not happen anyway ..
      if (box.isCacheValid())
      {
        return false;
      }
      return true;
    }

    if (breakState.isSuspended())
    {
      return false;
    }

    if (box instanceof InlineRenderBox)
    {
      breakState.add(StartSequenceElement.INSTANCE, box);
      return true;
    }

    computeContentArea(box);

    breakState.add(InlineBoxSequenceElement.INSTANCE, box);
    breakState.setSuspendItem(box.getInstanceId());
    return false;
  }

  protected void finishInlineLevelBox(final RenderBox box)
  {
    if (breakState.isActive() == false)
    {
      return;
    }
    if (breakState.getSuspendItem() == box.getInstanceId())
    {
      // stop being suspended.
      breakState.setSuspendItem(null);
      return;
    }

    if (box instanceof InlineRenderBox)
    {
      breakState.add(EndSequenceElement.INSTANCE, box);
      return;
    }

    final Object suspender = breakState.getSuspendItem();
    if (box.getInstanceId() == suspender)
    {
      breakState.setSuspendItem(null);
      return;
    }

    if (suspender != null)
    {
      return;
    }

    if (box instanceof ParagraphRenderBox)
    {
      throw new IllegalStateException("This cannot be.");
    }

  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    if (breakState.isActive() == false || breakState.isSuspended())
    {
      return;
    }

    if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode finNode = (FinishedRenderNode) node;
      node.setCachedWidth(finNode.getLayoutedWidth());
    }

    if (node instanceof RenderableText)
    {
      breakState.add(TextSequenceElement.INSTANCE, node);
    }
    else if (node instanceof RenderableReplacedContent)
    {
      breakState.add(ReplacedContentSequenceElement.INSTANCE, node);
    }
    else if (node instanceof SpacerRenderNode)
    {
      if (breakState.isContainsContent())
      {
        breakState.add(SpacerSequenceElement.INSTANCE, node);
      }
    }
    else
    {
      breakState.add(InlineNodeSequenceElement.INSTANCE, node);
    }
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    // This could be anything, text, or an image.
    node.setCachedX(node.getComputedX());
    if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode finNode = (FinishedRenderNode) node;
      node.setCachedWidth(finNode.getLayoutedWidth());
    }
    else
    {
      node.setCachedWidth(node.getComputedWidth());
    }
  }

  protected void processCanvasLevelNode(final RenderNode node)
  {
    // next, compute the width ...
    node.setCachedX(node.getComputedX());
    if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode finNode = (FinishedRenderNode) node;
      node.setCachedWidth(finNode.getLayoutedWidth());
    }
    else
    {
      node.setCachedWidth(node.getComputedWidth());
    }
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    if (box.isComplexParagraph())
    {
      final RenderBox lineboxContainer = box.getLineboxContainer();
      RenderNode node = lineboxContainer.getVisibleFirst();
      while (node != null)
      {
        // all childs of the linebox container must be inline boxes. They
        // represent the lines in the paragraph. Any other element here is
        // a error that must be reported
        if (node instanceof ParagraphPoolBox == false)
        {
          throw new IllegalStateException("Expected ParagraphPoolBox elements.");
        }

        final ParagraphPoolBox inlineRenderBox = (ParagraphPoolBox) node;
        if (startLine(inlineRenderBox))
        {
          processBoxChilds(inlineRenderBox);
          finishLine(inlineRenderBox);
        }

        node = node.getVisibleNext();
      }
    }
    else
    {
      final ParagraphPoolBox node = box.getPool();
      // all childs of the linebox container must be inline boxes. They
      // represent the lines in the paragraph. Any other element here is
      // a error that must be reported
      if (startLine(node))
      {
        processBoxChilds(node);
        finishLine(node);
      }
    }
  }

  private boolean startLine(final ParagraphPoolBox inlineRenderBox)
  {
    if (breakState.isActive() == false)
    {
      return false;
    }

    if (breakState.isSuspended())
    {
      return false;
    }

    breakState.clear();
    breakState.add(StartSequenceElement.INSTANCE, inlineRenderBox);
    return true;
  }

  private void finishLine(final ParagraphPoolBox inlineRenderBox)
  {
    if (breakState.isActive() == false || breakState.isSuspended())
    {
      throw new IllegalStateException("No active breakstate, finish-line cannot continue.");
    }

    breakState.add(EndSequenceElement.INSTANCE, inlineRenderBox);

    final ParagraphRenderBox paragraph = breakState.getParagraph();

    final ElementAlignment textAlignment = paragraph.getTextAlignment();

    // This aligns all direct childs. Once that is finished, we have to
    // check, whether possibly existing inner-paragraphs are still valid
    // or whether moving them violated any of the inner-pagebreak constraints.
    final TextAlignmentProcessor processor = create(textAlignment);

    final SequenceList sequence = breakState.getSequence();

    final long lineStart = paragraph.getContentAreaX1();
    final long lineEnd = paragraph.getContentAreaX2();
    if (lineEnd - lineStart <= 0)
    {
      final long minimumChunkWidth = paragraph.getMinimumChunkWidth();
      processor.initialize(sequence, lineStart, lineStart + minimumChunkWidth, pageGrid);
      Log.warn("Auto-Corrected zero-width linebox on " + paragraph.getName());
    }
    else
    {
      processor.initialize(sequence, lineStart, lineEnd, pageGrid);
    }

    while (processor.hasNext())
    {
      final RenderNode linebox = processor.next();
      if (linebox instanceof ParagraphPoolBox == false)
      {
        throw new NullPointerException("Line must not be null");
      }

      paragraph.addGeneratedChild(linebox);
    }

    processor.deinitialize();
  }

  /**
   * Reuse the processors ..
   *
   * @param alignment
   * @return
   */
  private TextAlignmentProcessor create(final ElementAlignment alignment)
  {
    if (ElementAlignment.CENTER.equals(alignment))
    {
      if (centerProcessor == null)
      {
        centerProcessor = new CenterAlignmentProcessor();
      }
      return centerProcessor;
    }
    else if (ElementAlignment.RIGHT.equals(alignment))
    {
      if (rightProcessor == null)
      {
        rightProcessor = new RightAlignmentProcessor();
      }
      return rightProcessor;
    }

    if (leftProcessor == null)
    {
      leftProcessor = new LeftAlignmentProcessor();
    }
    return leftProcessor;
  }

  protected boolean startCanvasLevelBox(final RenderBox box)
  {
    // first, compute the position. The position is global, not relative to a
    // parent or so. Therefore a child has no connection to the parent's
    // effective position, when it is painted.

    if (breakState.isActive() == false)
    {
      if (box.isCacheValid())
      {
        return false;
      }

      if (box != continuedElement)
      {
        final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
        final long x = box.getComputedX();//  + blp.getPositionX(); //todo
        box.setCachedX(x);
        // next, compute the width ...

        final BoxDefinition bdef = box.getBoxDefinition();
        final long leftPadding = blp.getBorderLeft() + bdef.getPaddingLeft();
        final long rightPadding = blp.getBorderRight() + bdef.getPaddingRight();
        box.setContentAreaX1(x + leftPadding);
        box.setContentAreaX2(x + box.getComputedWidth() - rightPadding);
      }

      if (box instanceof ParagraphRenderBox)
      {
        final ParagraphRenderBox paragraphBox = (ParagraphRenderBox) box;
        if (continuedElement == null)
        {
          final long lineBoxChangeTracker;
          if (paragraphBox.isComplexParagraph())
          {
            lineBoxChangeTracker = paragraphBox.getLineboxContainer().getChangeTracker();
          }
          else
          {
            lineBoxChangeTracker = paragraphBox.getPool().getChangeTracker();
          }

          final boolean unchanged = lineBoxChangeTracker == paragraphBox.getMinorLayoutAge();
          if (unchanged)
          {
            return false;
          }
        }

        paragraphBox.clearLayout();
        breakState.init(paragraphBox);
      }
      return true;
    }

    if (breakState.isSuspended() == false)
    {
      // The break-state exists only while we are inside of an paragraph
      // and suspend can only happen on inline elements.
      // A block-element inside a paragraph cannot be (and if it does, it is
      // a bug)
      throw new IllegalStateException("This cannot be.");
    }

    // this way or another - we are suspended now. So there is no need to look
    // at the children anymore ..
    return false;
  }

  protected void finishCanvasLevelBox(final RenderBox box)
  {
    // Todo: maybe it would be very wise if we dont extend the box later on.
    // Heck, lets see whether this causes great pain ...
    verifyContentWidth(box);

    if (breakState.isActive())
    {
      final Object suspender = breakState.getSuspendItem();
      if (box.getInstanceId() == suspender)
      {
        breakState.setSuspendItem(null);
        return;
      }
      if (suspender != null)
      {
        return;
      }

      if (box instanceof ParagraphRenderBox)
      {
        // finally update the change tracker ..
        final ParagraphRenderBox paraBox = (ParagraphRenderBox) box;
        if (paraBox.isComplexParagraph())
        {
          paraBox.setMinorLayoutAge(paraBox.getLineboxContainer().getChangeTracker());
        }
        else
        {
          paraBox.setMinorLayoutAge(paraBox.getPool().getChangeTracker());
        }

        breakState.deinit();
      }
    }
  }
}
