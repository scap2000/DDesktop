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
 * InfiniteMajorAxisLayoutStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.FinishedRenderNode;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphPoolBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderLength;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.model.WatermarkAreaBox;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;
import org.jfree.report.layout.process.valign.BoxAlignContext;
import org.jfree.report.layout.process.valign.InlineBlockAlignContext;
import org.jfree.report.layout.process.valign.NodeAlignContext;
import org.jfree.report.layout.process.valign.ReplacedContentAlignContext;
import org.jfree.report.layout.process.valign.TextElementAlignContext;
import org.jfree.report.layout.process.valign.VerticalAlignmentProcessor;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Computes the absolute layout. The computed height and minY positions of all abolutely positioned elements will be stored
 * in the 'canvasY' and 'canvasHeight' properties of RenderNode. Percentages will be resolved to zero.
 *
 * @author Thomas Morgner
 */
public final class InfiniteMajorAxisLayoutStep extends IterateVisualProcessStep
{
  // Set the maximum height to an incredibly high value. This is now 2^43 micropoints or more than
  // 3000 kilometers. Please call me directly at any time if you need more space for printing.
  private static final long MAX_AUTO = StrictGeomUtility.toInternalValue(0x80000000000L);

  private MajorAxisParagraphBreakState breakState;
  private RenderBox continuedElement;
  private VerticalAlignmentProcessor processor;

  public InfiniteMajorAxisLayoutStep()
  {
    this.breakState = new MajorAxisParagraphBreakState();
    this.processor = new VerticalAlignmentProcessor();
  }

  public void compute(final LogicalPageBox pageBox)
  {
    this.breakState.deinit();
    this.continuedElement = null;
    try
    {
      startProcessing(pageBox);
    }
    finally
    {
      this.continuedElement = null;
      this.breakState.deinit();
    }
  }

  /**
     * Continues processing. The renderbox must have a valid minX-layout (that is: X, content-X1, content-X2 and Width)
     *
     * @param box
     */
  public void continueComputation(final RenderBox box)
  {
    // This is most-likely wrong, but as we do not support inline-block elements yet, we can ignore this for now.
    if (box.getContentAreaX2() == 0 || box.getCachedWidth() == 0)
    {
      throw new IllegalStateException("Box must be layouted a bit ..");
    }

    this.breakState.deinit();
    this.continuedElement = box;
    startProcessing(box);
    this.continuedElement = null;
    this.breakState.deinit();
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    if (box.isIgnorableForRendering())
    {
      return false;
    }

    if (box.isCacheValid())
    {
      return false;
    }

    // Compute the block-position of the box. The box is positioned relative to the previous silbling or
    // relative to the parent.
    box.setCachedY(computeVerticalBlockPosition(box));

    if (breakState.isActive())
    {
      // No breakstate and not being suspended? Why this?
      if (breakState.isSuspended() == false)
      {
        throw new IllegalStateException("This cannot be.");
      }

      // this way or another - we are suspended now. So there is no need to look
      // at the children anymore ..

      // This code is only executed for inline-block elements. Inline-block elements are not part of
      // the 0.8.9 or 1.0 engine layouting.
      return false;
    }

    if (box instanceof ParagraphRenderBox)
    {
      final ParagraphRenderBox paragraphBox = (ParagraphRenderBox) box;
      // We cant cache that ... the shift operations later would misbehave
      // One way around would be to at least store the layouted offsets
      // (which should be immutable as long as the line did not change its
      // contents) and to reapply them on each run. This is cheaper than
      // having to compute the whole v-align for the whole line.
      breakState.init(paragraphBox);
    }

    return true;
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    // This could be anything, text, or an image.
    node.setCachedY(computeVerticalBlockPosition(node));

    if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode fnode = (FinishedRenderNode) node;
      node.setCachedHeight(fnode.getLayoutedHeight());
    }
    else if (node instanceof InlineRenderBox)
    {
      throw new IllegalStateException("A Inline-Box must be contained in a paragraph.");
    }
    else if (node instanceof RenderableReplacedContent)
    {
      final RenderableReplacedContent rpc = (RenderableReplacedContent) node;
      node.setCachedHeight(rpc.computeHeight(computeBlockContextWidth(node), node.getComputedWidth()));
    }
  }

  protected void finishBlockLevelBox(final RenderBox box)
  {
    if (box instanceof BlockRenderBox)
    {
      // make sure that we resolve against zero.
      box.setCachedHeight(computeBlockHeightAndAlign(box, 0));
    }
    else
    {
      box.setCachedHeight(computeCanvasHeight(box));
    }

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
        breakState.deinit();
      }
    }
  }

  private long computeVerticalBlockPosition(final RenderNode node)
  {
    // we have no margins yet ..
    final long marginTop = 0;

    // The y-position of a box depends on the parent.
    final RenderBox parent = node.getParent();

    // A table row is something special. Although it is a block box,
    // it layouts its children from left to right
    if (parent instanceof BlockRenderBox)
    {
      final RenderNode prev = node.getVisiblePrev();
      if (prev != null)
      {
        // we have a silbling. Position yourself directly below your silbling ..
        return (marginTop + prev.getCachedY() + prev.getCachedHeight());
      }
      else
      {
        final StaticBoxLayoutProperties blp = parent.getStaticBoxLayoutProperties();
        final BoxDefinition bdef = parent.getBoxDefinition();
        final long insetTop = (blp.getBorderTop() + bdef.getPaddingTop());

        return (marginTop + insetTop + parent.getCachedY());
      }
    }
    else
    {
      // there's no parent ..
      return (marginTop);
    }
  }

  private long computeBlockHeightAndAlign(final RenderBox box, final long resolveSize)
  {
    // For the water-mark area, this computation is different. The Watermark-area uses the known height of
    // the parent (=the page size)
    if (box instanceof WatermarkAreaBox)
    {
      final WatermarkAreaBox watermarkAreaBox = (WatermarkAreaBox) box;
      final LogicalPageBox lpb = watermarkAreaBox.getLogicalPage();
      // set the page-height as watermark size.
      return lpb.getPageHeight();
    }

    // Check the height. Set the height.
    final BoxDefinition boxDefinition = box.getBoxDefinition();
    final RenderLength preferredHeight = boxDefinition.getPreferredHeight();
    final RenderLength minimumHeight = boxDefinition.getMinimumHeight();
    final RenderLength maximumHeight = boxDefinition.getMaximumHeight();

    final long usedHeight;
    final long childY2;
    final long childY1;
    final RenderNode lastChildNode = box.getLastChild();
    if (lastChildNode != null)
    {
      childY1 = box.getFirstChild().getCachedY();
      childY2 = lastChildNode.getCachedY() + lastChildNode.getCachedHeight() + lastChildNode.getEffectiveMarginBottom();
      usedHeight = (childY2 - childY1);
    }
    else
    {
      usedHeight = 0;
      childY2 = 0;
      childY1 = 0;
    }

    //final long blockContextWidth = box.getStaticBoxLayoutProperties().getBlockContextWidth();
    final long rprefH = preferredHeight.resolve(resolveSize, usedHeight);
    final long rminH = minimumHeight.resolve(resolveSize, 0);
    final long rmaxH = maximumHeight.resolve(resolveSize, MAX_AUTO);

    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final long insetBottom = blp.getBorderBottom() + boxDefinition.getPaddingBottom();
    final long insetTop = blp.getBorderTop() + boxDefinition.getPaddingTop();

    final long specifiedHeight = computeHeight(rminH, rmaxH, rprefH);
    final long computedHeight;
    if (boxDefinition.isSizeSpecifiesBorderBox())
    {
      computedHeight = specifiedHeight - insetTop - insetBottom;
    }
    else
    {
      computedHeight = specifiedHeight;
    }

    if (lastChildNode != null)
    {
      // grab the node's y2
      if (computedHeight > usedHeight)
      {
        // we have extra space to distribute. So lets shift some boxes.
        final ElementAlignment valign = box.getNodeLayoutProperties().getVerticalAlignment();
        if (ElementAlignment.BOTTOM.equals(valign))
        {
          final long boxBottom = (box.getCachedY() + box.getCachedHeight() - insetBottom);
          final long delta = boxBottom - childY2;
          CacheBoxShifter.shiftBoxChilds(box, delta);
        }
        else if (ElementAlignment.MIDDLE.equals(valign))
        {
          final long extraHeight = computedHeight - usedHeight;
          final long boxTop = box.getCachedY() + insetTop + (extraHeight / 2);
          final long delta = boxTop - childY1;
          CacheBoxShifter.shiftBoxChilds(box, delta);
        }
        return Math.max(0, computedHeight + insetTop + insetBottom);
      }
      return Math.max(0, computedHeight + insetTop + insetBottom);
    }
    else
    {
      return Math.max(0, computedHeight + insetTop + insetBottom);
    }
  }

  private long computeBlockContextWidth(final RenderNode box)
  {
    final RenderBox parentBlockContext = box.getParent();
    if (parentBlockContext == null)
    {
      final LogicalPageBox logicalPage = box.getLogicalPage();
      if (logicalPage == null)
      {
        return 0;
      }
      return logicalPage.getPageWidth();
    }
    return parentBlockContext.getStaticBoxLayoutProperties().getBlockContextWidth();
  }


  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // Process the direct childs of the paragraph
    // Each direct child represents a line ..

    RenderNode node = box.getVisibleFirst();
    while (node != null)
    {
      // all childs of the linebox container must be inline boxes. They
      // represent the lines in the paragraph. Any other element here is
      // a error that must be reported
      if (node instanceof ParagraphPoolBox == false)
      {
        throw new IllegalStateException("Encountered " + node.getClass());
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

  private boolean startLine(final ParagraphPoolBox box)
  {
    box.setCachedY(computeVerticalBlockPosition(box));

    if (breakState.isActive() == false)
    {
      return false;
    }

    if (breakState.isSuspended())
    {
      return false;
    }

    breakState.openContext(new BoxAlignContext(box));
    return true;
  }

  private void finishLine(final ParagraphPoolBox inlineRenderBox)
  {
    if (breakState.isActive() == false || breakState.isSuspended())
    {
      return;
    }

    final BoxAlignContext boxAlignContext = breakState.closeContext();

    // This aligns all direct childs. Once that is finished, we have to
    // check, whether possibly existing inner-paragraphs are still valid
    // or whether moving them violated any of the inner-pagebreak constraints.

    final StaticBoxLayoutProperties blp = inlineRenderBox.getStaticBoxLayoutProperties();
    final BoxDefinition bdef = inlineRenderBox.getBoxDefinition();
    final long insetTop = (blp.getBorderTop() + bdef.getPaddingTop());

    final long contentAreaY1 = inlineRenderBox.getCachedY() + insetTop;
    final long lineHeight = inlineRenderBox.getLineHeight();
    processor.align(boxAlignContext, contentAreaY1, lineHeight);
  }


  protected boolean startInlineLevelBox(final RenderBox box)
  {
    if (box.isCacheValid())
    {
      return false;
    }

    box.setCachedY(computeVerticalInlinePosition(box));
    computeBaselineInfo(box);

    if (breakState == null)
    {
      // ignore .. should not happen anyway ..
      return true;
    }

    if (breakState.isSuspended())
    {
      return false;
    }

    if (box instanceof InlineRenderBox)
    {
      breakState.openContext(new BoxAlignContext(box));
      return true;
    }

    breakState.getCurrentLine().addChild(new InlineBlockAlignContext(box));
    breakState.setSuspendItem(box.getInstanceId());
    return false;
  }

  private void computeBaselineInfo(final RenderBox box)
  {
    if (box.getBaselineInfo() == null)
    {
      return;
    }

    RenderNode node = box.getVisibleFirst();
    while (node != null)
    {
      if (node instanceof RenderableText)
      {
        // grab the baseline info from there ...
        final RenderableText text = (RenderableText) node;
        box.setBaselineInfo(text.getBaselineInfo());
        break;
      }

      node = node.getVisibleNext();
    }

    // If we have no baseline info here, ask the parent. If that one has none
    // either, then we cant do anything about it.
    if (box.getBaselineInfo() == null)
    {
      box.setBaselineInfo(box.getStaticBoxLayoutProperties().getNominalBaselineInfo());
    }
  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    // compute the intial position.
    node.setCachedY(computeVerticalInlinePosition(node));
    // the height and the real position will be computed during the vertical-alignment computation. 

    if (breakState.isActive() == false || breakState.isSuspended())
    {
      return;
    }

    if (node instanceof RenderableText)
    {
      breakState.getCurrentLine().addChild(new TextElementAlignContext((RenderableText) node));
    }
    else if (node instanceof RenderableReplacedContent)
    {
      breakState.getCurrentLine().addChild(new ReplacedContentAlignContext((RenderableReplacedContent) node));
    }
    else
    {
      breakState.getCurrentLine().addChild(new NodeAlignContext(node));
    }
  }


  protected void finishInlineLevelBox(final RenderBox box)
  {
    // The height of an inline-level box will be computed when the vertical-alignemnt is done.

    if (breakState.isActive() == false)
    {
      return;
    }

    if (box instanceof InlineRenderBox)
    {
      breakState.closeContext();
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

  private long computeVerticalInlinePosition(final RenderNode node)
  {
    final RenderBox parent = node.getParent();

    if (parent != null)
    {
      // the computed position of an inline-element must be the same as the position of the parent element.
      // A inline-box always has an other inline-box as parent (the paragraph-pool-box is the only exception;
      // and this one is handled elsewhere).

      // Top and bottom margins are not applied to inline-elements.
      final StaticBoxLayoutProperties blp = parent.getStaticBoxLayoutProperties();
      final BoxDefinition bdef = parent.getBoxDefinition();
      final long insetTop = (blp.getBorderTop() + bdef.getPaddingTop());

      return (insetTop + parent.getCachedY());
    }
    else
    {
      // there's no parent .. Should not happen, shouldn't it?
      return (0);
    }
  }


  protected boolean startCanvasLevelBox(final RenderBox box)
  {
    if (box.isCacheValid())
    {
      return false;
    }

    if (box.isIgnorableForRendering())
    {
      return false;
    }

    box.setCachedY(computeVerticalCanvasPosition(box));

    if (breakState.isActive() == false)
    {
      if (box instanceof ParagraphRenderBox)
      {
        final ParagraphRenderBox paragraphBox = (ParagraphRenderBox) box;
        // We cant cache that ... the shift operations later would misbehave
        // One way around would be to at least store the layouted offsets
        // (which should be immutable as long as the line did not change its
        // contents) and to reapply them on each run. This is cheaper than
        // having to compute the whole v-align for the whole line.
        breakState.init(paragraphBox);
      }

      return true;
    }

    // No breakstate and not being suspended? Why this?
    if (breakState.isSuspended() == false)
    {
      throw new IllegalStateException("This cannot be.");
    }

    // this way or another - we are suspended now. So there is no need to look
    // at the children anymore ..
    return false;
  }

  protected void processCanvasLevelNode(final RenderNode node)
  {
    node.setCachedY(computeVerticalCanvasPosition(node));
    
    // docmark
    if (node instanceof RenderableReplacedContent)
    {
      final RenderableReplacedContent rpc = (RenderableReplacedContent) node;
      final long computedHeight = rpc.computeHeight(0, node.getComputedWidth());
      node.setCachedHeight(computedHeight);
    }
    else if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode fnode = (FinishedRenderNode) node;
      node.setCachedHeight(fnode.getLayoutedHeight());
    }
    else
    {
      node.setCachedHeight(0);
    }
  }

  /**
     * Finishes up a canvas level box. This updates/affects the height of the parent, as the canvas model defines that the
     * parent always fully encloses all of its childs.
     * <p/>
     * When no preferred height is defined, the height of an element is the maximum of its minimum-height and the absolute
     * height of all of its direct children.
     * <p/>
     * To resolve the value of percentages, the system uses the maximum of the parent's height and the maximum of all (minY +
     * height) of all children.)
     *
     * @param box
     */
  protected void finishCanvasLevelBox(final RenderBox box)
  {
    if (box instanceof BlockRenderBox)
    {
      // make sure that we resolve against zero.
      box.setCachedHeight(computeBlockHeightAndAlign(box, 0));
    }
    else
    {

      box.setCachedHeight(computeCanvasHeight(box));
    }

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
        breakState.deinit();
      }
    }
  }

  private long computeVerticalCanvasPosition (final RenderNode node)
  {
    final RenderBox parent = node.getParent();
    final long parentPosition;
    if (parent == null)
    {
      parentPosition = 0;
    }
    else
    {
      parentPosition = parent.getCachedY();
    }

    final double posY = node.getStyleSheet().getDoubleStyleProperty(ElementStyleKeys.POS_Y, 0);
    if (node.isSizeSpecifiesBorderBox())
    {
      return (parentPosition + RenderLength.resolveLength(0, posY));
    }
    else
    {
      final long insetsTop;
      if (node instanceof RenderBox)
      {
        final RenderBox box = (RenderBox) node;
        final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
        final BoxDefinition bdef = box.getBoxDefinition();
        insetsTop = (blp.getBorderTop() + bdef.getPaddingTop());
      }
      else
      {
        insetsTop = 0;
      }
      return (parentPosition + RenderLength.resolveLength(0, posY) - insetsTop);
    }
  }

  private long computeCanvasHeight(final RenderBox box)
  {
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final BoxDefinition bdef = box.getBoxDefinition();

    final BoxDefinition boxDefinition = box.getBoxDefinition();
    final RenderLength minHeight = boxDefinition.getMinimumHeight();
    final RenderLength preferredHeight = boxDefinition.getPreferredHeight();
    final RenderLength maxHeight = boxDefinition.getMaximumHeight();

    final long insetsTop = (blp.getBorderTop() + bdef.getPaddingTop());
    final long insetsBottom = blp.getBorderBottom() + bdef.getPaddingBottom();
    final long insets = insetsTop + insetsBottom;

    // find the maximum of the used height (for all childs) and the specified min-height.
    long consumedHeight = minHeight.resolve(0);

    if (box.isSizeSpecifiesBorderBox())
    {
      consumedHeight = Math.max(0, consumedHeight - insetsBottom);
    }
    else
    {
      consumedHeight = Math.max(0, consumedHeight + insetsTop);
    }

    final long boxY = box.getCachedY();

    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      final long childY2 = (insetsTop + (node.getCachedY() + node.getCachedHeight()));
      final long childLocalY2 = childY2 - boxY;
      if (childLocalY2 > consumedHeight)
      {
        consumedHeight = childLocalY2;
      }
      node = node.getNext();
    }

    consumedHeight += insetsBottom;

    // The consumed height computed above specifies the size at the border-edge.
    // However, depending on the box-sizing property, we may have to resolve them against the
    // content-edge instead.

    if (box.isSizeSpecifiesBorderBox())
    {
      final long minHeightResolved = minHeight.resolve(0);
      final long maxHeightResolved = maxHeight.resolve(0);
      final long prefHeightResolved;
      if (preferredHeight == RenderLength.AUTO)
      {
        prefHeightResolved = consumedHeight;
      }
      else
      {
        prefHeightResolved = preferredHeight.resolve(0);
      }

      final long height = computeHeight(minHeightResolved, maxHeightResolved, prefHeightResolved);
      return (height);
    }
    else
    {
      consumedHeight = Math.max(0, consumedHeight - insets);
      final long minHeightResolved = minHeight.resolve(0);
      final long maxHeightResolved = maxHeight.resolve(0);
      final long prefHeightResolved;
      if (preferredHeight == RenderLength.AUTO)
      {
        prefHeightResolved = consumedHeight;
      }
      else
      {
        prefHeightResolved = preferredHeight.resolve(0);
      }

      final long height = computeHeight(minHeightResolved, maxHeightResolved, prefHeightResolved);
      return (height + insets);
    }
  }

  public static long computeHeight(final long min, final long max, final long pref)
  {
    if (pref > max)
    {
      if (max < min)
      {
        return min;
      }
      return max;
    }

    if (pref < min)
    {
      if (max < min)
      {
        return max;
      }
      return min;
    }

    if (max < pref)
    {
      return max;
    }
    return pref;
  }
}
