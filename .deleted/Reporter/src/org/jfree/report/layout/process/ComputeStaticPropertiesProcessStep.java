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
 * ComputeHorizontalsProcessStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.fonts.registry.BaselineInfo;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.Border;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.FinishedRenderNode;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderLength;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.text.ExtendedBaselineInfo;
import org.jfree.report.layout.text.TextUtility;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.style.WhitespaceCollapse;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Computes the width for all elements. This uses the CSS alogorithm, percentages are resolved against the parent's
 * already known width.
 *
 * @author Thomas Morgner
 */
public final class ComputeStaticPropertiesProcessStep extends IterateVisualProcessStep
{
  // Set the maximum height to an incredibly high value. This is now 2^43 micropoints or more than
  // 3000 kilometers. Please call me directly at any time if you need more space for printing.
  private static final long MAX_AUTO = StrictGeomUtility.toInternalValue(0x80000000000L);

  private static final boolean ENABLE_ASSERTATION = false;
  private LogicalPageBox logicalPage;
  private OutputProcessorMetaData metaData;
  private BaselineInfo baselineInfo;

  public ComputeStaticPropertiesProcessStep()
  {
    this.baselineInfo = new BaselineInfo();
  }

  public void compute(final LogicalPageBox root,
                      final OutputProcessorMetaData metaData)
  {
    try
    {
      this.logicalPage = root;
      this.metaData = metaData;
      startProcessing(root);
    }
    finally
    {
      this.logicalPage = null;
      this.metaData = null;
    }
//    Log.debug ("Performance: " + cacheHit + ":"  + cacheMiss + ":" + cacheFill);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
//    startProcessing(box.getLineboxContainer());
    startProcessing(box.getPool());
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    final long age = box.getStaticBoxPropertiesAge();
    final long changeTracker = box.getChangeTracker();

    if (age == changeTracker)
    {
      // the node has been computed in the past, and there have been no structural changes later.
      assertChildValid(box);
      return false;
    }
    if (age > -1)
    {
      // the node has been computed in the past, but new nodes have been added.
      assertBoxValid(box);
      return true;
    }

    computeBreakIndicator(box);

    final long parentContextWidth = updateStaticProperties(box);
    final BoxDefinition boxDefinition = box.getBoxDefinition();

    final RenderLength preferredWidth = boxDefinition.getPreferredWidth();
    final RenderLength minimumWidth = boxDefinition.getMinimumWidth();
    final RenderLength maximumWidth = boxDefinition.getMaximumWidth();

    final long rprefW = preferredWidth.resolve(parentContextWidth, parentContextWidth);
    final long rminW = minimumWidth.resolve(parentContextWidth, 0);
    final long rmaxW = maximumWidth.resolve(parentContextWidth, MAX_AUTO);

    final long computedWidth = computeWidth(rminW, rmaxW, rprefW);
    box.setComputedX(computeParentContentBoxX(box));

    if (boxDefinition.isSizeSpecifiesBorderBox() == false)
    {
      final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
      final long horizontalInsets = boxDefinition.getPaddingLeft() + boxDefinition.getPaddingRight() +
          sblp.getBorderLeft() + sblp.getBorderRight();
      box.setComputedWidth(Math.max(0, computedWidth + horizontalInsets));
    }
    else
    {
      box.setComputedWidth(Math.max(0, computedWidth));
    }
    return true;
  }

  private void assertBoxValid(final RenderBox box)
  {
    if (ENABLE_ASSERTATION && box.getStaticBoxLayoutProperties().getNominalBaselineInfo() == null)
    {
      throw new NullPointerException();
    }
  }

  private void assertChildValid(final RenderBox box)
  {
    if (ENABLE_ASSERTATION && box.getFirstChild() != null)
    {
      final RenderNode node = box.getFirstChild();
      if (node instanceof RenderBox)
      {
        final RenderBox cbox = (RenderBox) node;
        assertBoxValid(cbox);
      }
    }
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    final long age = box.getStaticBoxPropertiesAge();
    final long changeTracker = box.getChangeTracker();
    if (age == changeTracker)
    {
      // the node has been computed in the past, and there were no structural changes later.
//      cacheHit += 1;
      assertChildValid(box);
      return false;
    }
    if (age > -1)
    {
      // the node has been computed in the past, but new nodes have been added.
//      cacheMiss += 1;
      assertBoxValid(box);
      return true;
    }

//    cacheFill += 1;

    updateStaticProperties(box);
    box.setComputedWidth(0); // A inline level box can never have a computed width.
    box.setComputedX(0);     // The position of an inline box is not known yet and cannot be computed here
    return true;
  }

  protected boolean startCanvasLevelBox(final RenderBox box)
  {
    final long age = box.getStaticBoxPropertiesAge();
    final long changeTracker = box.getChangeTracker();
    if (age == changeTracker)
    {
      // the node has been computed in the past, and there were no structural changes later.
//      cacheHit += 1;
      return false;
    }
    if (age > -1)
    {
      // the node has been computed in the past, but new nodes have been added.
//      cacheMiss += 1;
      return true;
    }

//    cacheFill += 1;

    computeBreakIndicator(box);
    
    // For now, we silently assume that percentages in an canvas-element get resolved against the parent's width
    // This should still result in valid behavior most of the time, while it greatly simplifies the layouting model.
    final long contextWidth = updateStaticProperties(box);

    final BoxDefinition boxDefinition = box.getBoxDefinition();
    final RenderLength preferredWidth = boxDefinition.getPreferredWidth();
    final RenderLength minimumWidth = boxDefinition.getMinimumWidth();
    final RenderLength maximumWidth = boxDefinition.getMaximumWidth();

    // docmark: Changed the auto-mapping to zero. Bands should not expand infinitely ..
    final long autoWidth = computeAutoWidth (box);
    final long rprefW = preferredWidth.resolve(contextWidth, autoWidth);
    final long rminW = minimumWidth.resolve(contextWidth, 0);
    final long rmaxW = maximumWidth.resolve(contextWidth, MAX_AUTO);
    final long computedWidth = Math.max(0, computeWidth(rminW, rmaxW, rprefW));

    final StyleSheet styleSheet = box.getStyleSheet();
    final double posX = styleSheet.getDoubleStyleProperty(ElementStyleKeys.POS_X, 0);
    final long rx = RenderLength.resolveLength(contextWidth, posX);

    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    final long insetsLeft = boxDefinition.getPaddingLeft() + sblp.getBorderLeft();
    final long insets = insetsLeft + boxDefinition.getPaddingRight() + sblp.getBorderRight();

    if (boxDefinition.isSizeSpecifiesBorderBox())
    {
      box.setComputedX(rx + computeParentContentBoxX(box));
      if (rx > contextWidth)
      {
        // This element will not be visible, it is outside of the page-area ..
        box.setComputedWidth(0);
      }
      else if ((rx + computedWidth) > contextWidth)
      {
        // The element will be partially out of scope. It consumes as much as it can...
        box.setComputedWidth(Math.max(0, contextWidth - rx));
      }
      else
      {
        box.setComputedWidth(Math.max(0, computedWidth));
      }
    }
    else
    {
      // all sizes specify the content-box. Therefore, to compute the real x in the border-box we have to
      // subtract the border and padding sizes. (The computed X can be negative now.)
      final long realX = rx - insetsLeft;
      box.setComputedX(realX + computeParentContentBoxX(box));
      if (realX > contextWidth)
      {
        // This element will not be visible, it is outside of the page-area ..
        box.setComputedWidth(0);
      }
      else if ((realX + computedWidth + insets) > contextWidth)
      {
        // The element will be partially out of scope.
        box.setComputedWidth(contextWidth - realX);
      }
      else
      {
        // the element fully fits into the parent.
        box.setComputedWidth(computedWidth + insets);
      }
    }
    return true;

  }

  /**
   * This is a hack. In this basic layout model we normally do not resolve the AUTO-length against the intrinsic
   * length of the paragraphs and replaced content. We should, but in the canvas model, we can run cheaper without
   * it.
   *  
   * @param box
   * @return
   */
  private long computeAutoWidth(final RenderBox box)
  {
    final RenderNode child = box.getFirstChild();
    if (child instanceof RenderableReplacedContent)
    {
      final RenderableReplacedContent rpc = (RenderableReplacedContent) child;
      return rpc.getContentWidth();
    }
    return 0;
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode fnode = (FinishedRenderNode) node;
      node.setComputedWidth(fnode.getLayoutedWidth());
    }
    else if (node instanceof RenderableReplacedContent)
    {
      final long bcw = computeBlockContextWidth(node);
      final RenderableReplacedContent prc = (RenderableReplacedContent) node;
      node.setComputedWidth(prc.computeWidth(bcw));
    }
    else
    {
      final long bcw = computeBlockContextWidth(node);
      node.setComputedWidth(bcw);
    }
    node.setComputedX(computeParentContentBoxX(node));
  }

  private long computeParentContentBoxX(final RenderNode node)
  {
    final RenderBox parent = node.getParent();
    if (parent == null)
    {
      return 0;
    }
    final BoxDefinition boxDefinition = parent.getBoxDefinition();
    final StaticBoxLayoutProperties sblp = parent.getStaticBoxLayoutProperties();
    final long insets = boxDefinition.getPaddingLeft() + sblp.getBorderLeft();
    return parent.getComputedX() + insets;
  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    // they have no computable size/position or something else ..
    node.setComputedWidth(0);
    node.setComputedX(0);
  }

  protected void processCanvasLevelNode(final RenderNode node)
  {
    if (node instanceof RenderableText)
    {
      // If this happens, we really have a problem ..
      throw new IllegalStateException("Encountered RenderableText outside of an inline-context.");
    }

    if (node instanceof RenderableReplacedContent)
    {
      final long contextWidth = computeBlockContextWidth(node);
      final RenderableReplacedContent content = (RenderableReplacedContent) node;
      final StyleSheet styleSheet = node.getStyleSheet();
      final long computedWidth = content.computeWidth(contextWidth);

      final double posX = styleSheet.getDoubleStyleProperty(ElementStyleKeys.POS_X, 0);
      // Here, we have no need to differentiate between border-box and content-box, as the
      // replaced content never has a direct border
      final long rx = RenderLength.resolveLength(contextWidth, posX);
      if (rx > contextWidth)
      {
        node.setComputedWidth(0);
        node.setComputedX(rx + computeParentContentBoxX(node));
      }
      else if ((rx + computedWidth) > contextWidth)
      {
        node.setComputedWidth(contextWidth - rx);
        node.setComputedX(rx + computeParentContentBoxX(node));
      }
      else
      {
        node.setComputedWidth(computedWidth);
        node.setComputedX(rx + computeParentContentBoxX(node));
      }
    }
  }

  /**
   * Returns the computed block-context width. This width is a content-size width - so it excludes paddings
   * and borders. (See CSS3-BOX 4.2; http://www.w3.org/TR/css3-box/#containing)
   *
   * @param box the box for which the block-context width should be computed.
   * @return the block context width.
   */
  private long computeBlockContextWidth(final RenderNode box)
  {
    final RenderBox parentBlockContext = box.getParent();
    if (parentBlockContext == null)
    {
      // page cannot have borders ...
      return logicalPage.getPageWidth();
    }
    if (parentBlockContext instanceof BlockRenderBox ||
        parentBlockContext instanceof CanvasRenderBox)
    {
      final BoxDefinition boxDefinition = parentBlockContext.getBoxDefinition();
      final StaticBoxLayoutProperties sblp = parentBlockContext.getStaticBoxLayoutProperties();
      final long insets = sblp.getBorderLeft() + sblp.getBorderRight() +
          boxDefinition.getPaddingLeft() + boxDefinition.getPaddingRight();
      return Math.max (0, parentBlockContext.getComputedWidth() - insets);
    }
    // The parent's computed width is used as block context ..
    return parentBlockContext.getStaticBoxLayoutProperties().getBlockContextWidth();
  }


  private void computeBreakIndicator(final RenderBox box)
  {
    final StyleSheet styleSheet = box.getStyleSheet();
    final boolean breakBefore = styleSheet.getBooleanStyleProperty(BandStyleKeys.PAGEBREAK_BEFORE);
    final boolean breakAfter = box.isBreakAfter();
    final RenderBox parent = box.getParent();
    final boolean fixedPosition = RenderLength.AUTO.equals
        (styleSheet.getStyleProperty(BandStyleKeys.FIXED_POSITION, RenderLength.AUTO)) == false;
    if ((breakBefore) && (parent instanceof ParagraphRenderBox == false))
    {
      box.setManualBreakIndicator(RenderBox.DIRECT_MANUAL_BREAK);
      applyIndirectManualBreakIndicator(parent);
      return;
    }
    if (breakAfter && (parent instanceof ParagraphRenderBox == false))
    {
      if (parent != null)
      {
        applyIndirectManualBreakIndicator(parent);
      }
    }
    if (fixedPosition)
    {
      applyIndirectManualBreakIndicator(box);
    }
    else
    {
      box.setManualBreakIndicator(RenderBox.NO_MANUAL_BREAK);
    }
  }

  private void applyIndirectManualBreakIndicator(RenderBox node)
  {
    while (node != null)
    {
      if (node.getManualBreakIndicator() != RenderBox.NO_MANUAL_BREAK)
      {
        return;
      }
      node.setManualBreakIndicator(RenderBox.INDIRECT_MANUAL_BREAK);
      node = node.getParent();
    }
  }

  /**
   * Collects and possibly computes the static properties according to the CSS layouting model. The classic JFreeReport
   * layout model does not know anything about margins or borders, so in that case resolving against the CSS model is
   * ok.
   *
   * @param box
   * @return the computed width of the containing block (the so-called block-context width).
   */
  private long updateStaticProperties(final RenderBox box)
  {
    final BoxDefinition boxDefinition = box.getBoxDefinition();
    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    if (sblp.getNominalBaselineInfo() == null)
    {
      final long parentWidth = computeBlockContextWidth(box);
      sblp.setMarginTop(boxDefinition.getMarginTop().resolve(parentWidth));
      sblp.setMarginLeft(boxDefinition.getMarginLeft().resolve(parentWidth));
      sblp.setMarginBottom(boxDefinition.getMarginBottom().resolve(parentWidth));
      sblp.setMarginRight(boxDefinition.getMarginRight().resolve(parentWidth));

      final Border border = boxDefinition.getBorder();
      sblp.setBorderTop(border.getTop().getWidth());
      sblp.setBorderLeft(border.getLeft().getWidth());
      sblp.setBorderBottom(border.getBottom().getWidth());
      sblp.setBorderRight(border.getRight().getWidth());

      final StyleSheet style = box.getStyleSheet();
      sblp.setAvoidPagebreakInside(style.getBooleanStyleProperty(ElementStyleKeys.AVOID_PAGEBREAK_INSIDE, false));
      sblp.setDominantBaseline(-1);
      sblp.setOrphans(style.getIntStyleProperty(ElementStyleKeys.ORPHANS, 0));
      sblp.setWidows(style.getIntStyleProperty(ElementStyleKeys.WIDOWS, 0));

      final FontMetrics fontMetrics = metaData.getFontMetrics(style);
      final ExtendedBaselineInfo baselineInfo = TextUtility.createBaselineInfo('x', fontMetrics, this.baselineInfo);
      if (baselineInfo == null)
      {
        throw new IllegalStateException();
      }
      sblp.setNominalBaselineInfo(baselineInfo);
      sblp.setFontFamily(metaData.getNormalizedFontFamilyName((String) style.getStyleProperty(TextStyleKeys.FONT)));
      final Object collapse = style.getStyleProperty(TextStyleKeys.WHITE_SPACE_COLLAPSE);
      sblp.setPreserveSpace(WhitespaceCollapse.PRESERVE.equals(collapse));

      sblp.setBlockContextWidth(parentWidth);
      return parentWidth;
    }
    else
    {
//      Log.debug ("Compute box: " + box + " is not needed.");
//      throw new IllegalStateException("Tried to compute the nominal baseline info twice.");
      return sblp.getBlockContextWidth();
    }
  }

  private long computeWidth(final long min, final long max, final long pref)
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
