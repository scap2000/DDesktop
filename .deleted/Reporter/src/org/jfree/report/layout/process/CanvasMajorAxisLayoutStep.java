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
 * CanvasMajorAxisLayoutStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.FinishedRenderNode;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderLength;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.WatermarkAreaBox;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * This processes the second step of the vertical-layouting.
 * <p/>
 * At this point, the static height of all elements is known (that is the height of all elements that do not use
 * percentages in either the minY or height properties).
 * <p/>
 * That height is then used as base-value to resolve all relative heights and minY positions and the layouting is redone.
 *
 * @author Thomas Morgner
 */
public final class CanvasMajorAxisLayoutStep extends IterateVisualProcessStep
{
  // Set the maximum height to an incredibly high value. This is now 2^43 micropoints or more than
  // 3000 kilometers. Please call me directly at any time if you need more space for printing.
  private static final long MAX_AUTO = StrictGeomUtility.toInternalValue(0x80000000000L);

  private RenderBox continuedElement;

  public CanvasMajorAxisLayoutStep()
  {
  }

  public void compute(final LogicalPageBox pageBox)
  {
    this.continuedElement = null;
    try
    {
      startProcessing(pageBox);
    }
    finally
    {
      this.continuedElement = null;
    }
  }

  private long resolveParentHeight(final RenderNode node)
  {
    final RenderBox parent = node.getParent();
    if (parent == null)
    {
      return 0;
    }
    return parent.getCachedHeight();
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

    this.continuedElement = box;
    startProcessing(box);
    this.continuedElement = null;
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

    final long oldPosition = box.getCachedY();
    
    // Compute the block-position of the box. The box is positioned relative to the previous silbling or
    // relative to the parent.
    box.setCachedY(computeVerticalBlockPosition(box));
    if (box instanceof BlockRenderBox)
    {
      box.setCachedHeight(computeBlockHeightAndAlign(box, resolveParentHeight(box)));
    }
    else
    {

      box.setCachedHeight(computeCanvasHeight(box));
    }

    if (box instanceof ParagraphRenderBox)
    {
      if (box.getCachedY() != oldPosition)
      {
        CacheBoxShifter.shiftBoxChilds(box, box.getCachedY() - oldPosition);
      }
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
      box.setCachedHeight(computeBlockHeightAndAlign(box, resolveParentHeight(box)));
    }
    else
    {
      box.setCachedHeight(computeCanvasHeight(box));
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

    final long rprefH = preferredHeight.resolve(resolveSize, usedHeight);
    final long rminH = minimumHeight.resolve(resolveSize, 0);
    final long rmaxH = maximumHeight.resolve(resolveSize, MAX_AUTO);

    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final long insetBottom = blp.getBorderBottom() + boxDefinition.getPaddingBottom();
    final long insetTop = blp.getBorderTop() + boxDefinition.getPaddingTop();

    final long specifiedHeight = InfiniteMajorAxisLayoutStep.computeHeight(rminH, rmaxH, rprefH);
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
    
    final long oldPosition = box.getCachedY();
    box.setCachedY(computeVerticalCanvasPosition(box));
    if (box instanceof BlockRenderBox)
    {
      // make sure that we resolve against zero.
      box.setCachedHeight(computeBlockHeightAndAlign(box, resolveParentHeight(box)));
    }
    else
    {

      box.setCachedHeight(computeCanvasHeight(box));
    }
    if (box instanceof ParagraphRenderBox)
    {
      if (box.getCachedY() != oldPosition)
      {
        CacheBoxShifter.shiftBoxChilds(box, box.getCachedY() - oldPosition);
      }
    }

    return true;
  }

  protected void processCanvasLevelNode(final RenderNode node)
  {
    node.setCachedY(computeVerticalCanvasPosition(node));

    // docmark
    if (node instanceof RenderableReplacedContent)
    {
      final RenderableReplacedContent rpc = (RenderableReplacedContent) node;
      final long computedHeight = rpc.computeHeight(resolveParentHeight(node), node.getComputedWidth());
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
      box.setCachedHeight(computeBlockHeightAndAlign(box, resolveParentHeight(box)));
    }
    else
    {

      box.setCachedHeight(computeCanvasHeight(box));
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
      return (parentPosition + RenderLength.resolveLength(resolveParentHeight(node), posY));
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
      return (parentPosition + RenderLength.resolveLength(resolveParentHeight(node), posY) - insetsTop);
    }
  }

  private long resolveUseableParentHeight(final RenderNode node)
  {
    final RenderBox parent = node.getParent();
    if (parent == null)
    {
      return 0;
    }
    final long height = parent.getCachedHeight();
    final BoxDefinition bdef = parent.getBoxDefinition();
    final StaticBoxLayoutProperties blp = parent.getStaticBoxLayoutProperties();
    final long insetsTop = (blp.getBorderTop() + bdef.getPaddingTop());
    final long insetsBottom = blp.getBorderBottom() + bdef.getPaddingBottom();
    return (parent.getCachedY() + height - insetsTop - insetsBottom) - node.getCachedY();
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

    final long parentHeight = Math.max (resolveParentHeight(box), box.getCachedHeight());
    // find the maximum of the used height (for all childs) and the specified min-height.
    final long usableParentHeight = resolveUseableParentHeight(box);
    long consumedHeight = Math.max(box.getCachedHeight(),
        Math.min (minHeight.resolve(parentHeight), usableParentHeight));

    // The consumed height computed above specifies the size at the border-edge.
    // However, depending on the box-sizing property, we may have to resolve them against the
    // content-edge instead.

    if (box.isSizeSpecifiesBorderBox())
    {
      final long minHeightResolved = minHeight.resolve(parentHeight);
      final long maxHeightResolved = maxHeight.resolve(parentHeight);
      final long prefHeightResolved;
      if (preferredHeight == RenderLength.AUTO)
      {
        prefHeightResolved = consumedHeight;
      }
      else
      {
        prefHeightResolved = preferredHeight.resolve(parentHeight);
      }

      final long height = InfiniteMajorAxisLayoutStep.computeHeight(minHeightResolved, maxHeightResolved, prefHeightResolved);
      return Math.min (height, usableParentHeight);
    }
    else
    {
      consumedHeight = Math.max(0, consumedHeight - insets);
      final long minHeightResolved = minHeight.resolve(parentHeight);
      final long maxHeightResolved = maxHeight.resolve(parentHeight);
      final long prefHeightResolved;
      if (preferredHeight == RenderLength.AUTO)
      {
        prefHeightResolved = consumedHeight;
      }
      else
      {
        prefHeightResolved = preferredHeight.resolve(parentHeight);
      }

      final long height = InfiniteMajorAxisLayoutStep.computeHeight(minHeightResolved, maxHeightResolved, prefHeightResolved);
      return Math.min (height + insets, usableParentHeight);
    }
  }

}
