package org.jfree.report.layout.process;

import org.jfree.fonts.registry.FontMetrics;
import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.model.BlockRenderBox;
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
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.process.alignment.CenterAlignmentProcessor;
import org.jfree.report.layout.process.alignment.LastLineTextAlignmentProcessor;
import org.jfree.report.layout.process.alignment.LeftAlignmentProcessor;
import org.jfree.report.layout.process.alignment.RightAlignmentProcessor;
import org.jfree.report.layout.process.layoutrules.EndSequenceElement;
import org.jfree.report.layout.process.layoutrules.InlineBoxSequenceElement;
import org.jfree.report.layout.process.layoutrules.InlineNodeSequenceElement;
import org.jfree.report.layout.process.layoutrules.ReplacedContentSequenceElement;
import org.jfree.report.layout.process.layoutrules.SequenceList;
import org.jfree.report.layout.process.layoutrules.SpacerSequenceElement;
import org.jfree.report.layout.process.layoutrules.StartSequenceElement;
import org.jfree.report.layout.process.layoutrules.TextSequenceElement;
import org.jfree.report.layout.process.valign.BoxAlignContext;
import org.jfree.report.layout.process.valign.InlineBlockAlignContext;
import org.jfree.report.layout.process.valign.NodeAlignContext;
import org.jfree.report.layout.process.valign.ReplacedContentAlignContext;
import org.jfree.report.layout.process.valign.TextElementAlignContext;
import org.jfree.report.layout.process.valign.VerticalAlignmentProcessor;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.FastStack;
import org.jfree.util.Log;

/**
 * This final processing step revalidates the text-layouting and the vertical alignment of block-level elements.
 *
 * At this point, the layout is almost finished, but non-dynamic text elements may contain more content on the
 * last line than actually needed. This step recomputes the vertical alignment and merges all extra lines into the
 * last line. 
 *
 * @author Thomas Morgner
 */
public final class RevalidateAllAxisLayoutStep extends IterateVisualProcessStep
{
  private static class MergeContext
  {
    private RenderBox readContext;
    private RenderBox writeContext;

    protected MergeContext(final RenderBox writeContext, final RenderBox readContext)
    {
      this.readContext = readContext;
      this.writeContext = writeContext;
    }

    public RenderBox getReadContext()
    {
      return readContext;
    }

    public RenderBox getWriteContext()
    {
      return writeContext;
    }
  }

  // Set the maximum height to an incredibly high value. This is now 2^43 micropoints or more than
  // 3000 kilometers. Please call me directly at any time if you need more space for printing.
  private static final long MAX_AUTO = StrictGeomUtility.toInternalValue(0x80000000000L);

  private RenderBox continuedElement;
  private LastLineTextAlignmentProcessor centerProcessor;
  private LastLineTextAlignmentProcessor leftProcessor;
  private LastLineTextAlignmentProcessor rightProcessor;
  private PageGrid pageGrid;
  private OutputProcessorMetaData metaData;
  private VerticalAlignmentProcessor verticalAlignmentProcessor;

  public RevalidateAllAxisLayoutStep()
  {
    verticalAlignmentProcessor = new VerticalAlignmentProcessor();
  }

  public void compute(final LogicalPageBox pageBox, final OutputProcessorMetaData metaData)
  {
    this.metaData = metaData;
    this.continuedElement = null;
    this.pageGrid = pageBox.getPageGrid();
    try
    {
      startProcessing(pageBox);
    }
    finally
    {
      this.continuedElement = null;
      this.pageGrid = null;
      this.metaData = null;
    }
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
    return true;
  }


  protected boolean startCanvasLevelBox(final RenderBox box)
  {
    if (box.isIgnorableForRendering())
    {
      return false;
    }

    if (box.isCacheValid())
    {
      return false;
    }
    return true;
  }

  private void performVerticalBlockAlignment(final ParagraphRenderBox box)
  {

    final RenderNode lastChildNode = box.getLastChild();

    if (lastChildNode == null)
    {
      return;
    }

    final BoxDefinition boxDefinition = box.getBoxDefinition();
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final long insetBottom = blp.getBorderBottom() + boxDefinition.getPaddingBottom();
    final long insetTop = blp.getBorderTop() + boxDefinition.getPaddingTop();

    final long childY2 = lastChildNode.getCachedY() + lastChildNode.getCachedHeight() +
        lastChildNode.getEffectiveMarginBottom();
    final long childY1 = box.getFirstChild().getCachedY();
    final long usedHeight = (childY2 - childY1);

    final long computedHeight = box.getCachedHeight();
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
    }
  }

  protected void processParagraphChilds(final ParagraphRenderBox paragraph)
  {
    if (paragraph.getStyleSheet().getBooleanStyleProperty(ElementStyleKeys.OVERFLOW_Y) == true)
    {
      return;
    }

    // Process the direct childs of the paragraph
    // Each direct child represents a line ..
    final long paragraphBottom = paragraph.getCachedY() + paragraph.getCachedHeight();

    final RenderNode lastLine = paragraph.getVisibleLast();
    if (lastLine == null)
    {
      // Empty paragraph, no need to do anything ...
      return;
    }

    if ((lastLine.getCachedY() + lastLine.getCachedHeight()) <= paragraphBottom)
    {
      // Already perfectly aligned. 
      return;
    }

    RenderNode node = paragraph.getVisibleFirst();
    ParagraphPoolBox prev = null;
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
      // Process the current line.
      final long y = inlineRenderBox.getCachedY();
      final long height = inlineRenderBox.getCachedHeight();
      if (y + height <= paragraphBottom)
      {

        // Node will fit, so we can allow it ..
        prev = inlineRenderBox;
        node = node.getVisibleNext();
        continue;
      }

      // Encountered a paragraph that will not fully fit into the paragraph.
      // Merge it with the previous line-paragraph.
      final ParagraphPoolBox mergedLine = rebuildLastLine(prev, inlineRenderBox);

      // now remove all pending lineboxes (they should be empty anyway).
      while (node != null)
      {
        final RenderNode oldNode = node;
        node = node.getVisibleNext();
        paragraph.remove(oldNode);
      }

      if (mergedLine == null)
      {
        return;
      }

      final ElementAlignment textAlignment = paragraph.getLastLineAlignment();
      final LastLineTextAlignmentProcessor proc = create(textAlignment);

      // Now Build the sequence list that holds all nodes for the horizontal alignment computation.
      // The last line will get a special "last-line" horizontal alignment. This is quite usefull if
      // we are working with justified text and want the last line to be left-aligned.
      final SequenceList sequenceList = createHorizontalSequenceList(mergedLine);
      final long lineStart = paragraph.getContentAreaX1();
      final long lineEnd = paragraph.getContentAreaX2();
      if (lineEnd - lineStart <= 0)
      {
        final long minimumChunkWidth = paragraph.getMinimumChunkWidth();
        proc.initialize(sequenceList, lineStart, lineStart + minimumChunkWidth, pageGrid);
        Log.warn("Auto-Corrected zero-width linebox.");
      }
      else
      {
        proc.initialize(sequenceList, lineStart, lineEnd, pageGrid);
      }
      proc.performLastLineAlignment();
      proc.deinitialize();

      // Now Perform the vertical layout for the last line of the paragraph.
      final BoxAlignContext valignContext = createVerticalAlignContext(mergedLine);
      final StaticBoxLayoutProperties blp = mergedLine.getStaticBoxLayoutProperties();
      final BoxDefinition bdef = mergedLine.getBoxDefinition();
      final long insetTop = (blp.getBorderTop() + bdef.getPaddingTop());

      final long contentAreaY1 = mergedLine.getCachedY() + insetTop;
      final long lineHeight = mergedLine.getLineHeight();
      verticalAlignmentProcessor.align(valignContext, contentAreaY1, lineHeight);

      // And finally make sure that the paragraph box itself obeys to the defined vertical box alignment.
      performVerticalBlockAlignment(paragraph);
      return;
    }
  }


  private BoxAlignContext createVerticalAlignContext(final InlineRenderBox box)
  {
    BoxAlignContext alignContext = new BoxAlignContext(box);
    final FastStack contextStack = new FastStack();
    final FastStack alignContextStack = new FastStack();
    RenderNode next = box.getFirstChild();
    RenderBox context = box;

    while (next != null)
    {
      // process next
      if (next instanceof InlineRenderBox)
      {
        final RenderBox nBox = (RenderBox) next;
        final RenderNode firstChild = nBox.getFirstChild();
        if (firstChild != null)
        {
          // Open a non-empty box context
          contextStack.push(context);
          alignContextStack.push(alignContext);

          next = firstChild;

          final BoxAlignContext childBoxContext = new BoxAlignContext(nBox);
          alignContext.addChild(childBoxContext);
          context = nBox;
          alignContext = childBoxContext;
        }
        else
        {
          // Process an empty box.
          final BoxAlignContext childBoxContext = new BoxAlignContext(nBox);
          alignContext.addChild(childBoxContext);
          next = nBox.getNext();
        }
      }
      else
      {
        // Process an ordinary node.
        if (next instanceof RenderableText)
        {
          alignContext.addChild(new TextElementAlignContext((RenderableText) next));
        }
        else if (next instanceof RenderableReplacedContent)
        {
          alignContext.addChild(new ReplacedContentAlignContext((RenderableReplacedContent) next));
        }
        else if (next instanceof BlockRenderBox)
        {
          alignContext.addChild(new InlineBlockAlignContext((RenderBox) next));
        }
        else
        {
          alignContext.addChild(new NodeAlignContext(next));
        }
        next = next.getNext();
      }

      while (next == null && contextStack.isEmpty() == false)
      {
        // Finish the current box context, if needed
        next = context.getNext();
        context = (RenderBox) contextStack.pop();
        alignContext = (BoxAlignContext) alignContextStack.pop();
      }
    }
    return alignContext;
  }


  private SequenceList createHorizontalSequenceList(final InlineRenderBox box)
  {
    final SequenceList sequenceList = new SequenceList();
    sequenceList.add(StartSequenceElement.INSTANCE, box);

    RenderNode next = box.getFirstChild();
    RenderBox context = box;

    final FastStack contextStack = new FastStack();
    boolean containsContent = false;

    while (next != null)
    {
      // process next
      if (next instanceof InlineRenderBox)
      {
        final RenderBox nBox = (RenderBox) next;
        final RenderNode firstChild = nBox.getFirstChild();
        if (firstChild != null)
        {
          // Open a non-empty box context
          contextStack.push(context);
          next = firstChild;

          sequenceList.add(StartSequenceElement.INSTANCE, nBox);
          context = nBox;
        }
        else
        {
          // Process an empty box.
          sequenceList.add(StartSequenceElement.INSTANCE, nBox);
          sequenceList.add(EndSequenceElement.INSTANCE, nBox);
          next = nBox.getNext();
        }
      }
      else
      {
        // Process an ordinary node.
        if (next instanceof RenderableText)
        {
          sequenceList.add(TextSequenceElement.INSTANCE, next);
          containsContent = true;
        }
        else if (next instanceof RenderableReplacedContent)
        {
          sequenceList.add(ReplacedContentSequenceElement.INSTANCE, next);
          containsContent = true;
        }
        else if (next instanceof SpacerRenderNode)
        {
          if (containsContent)
          {
            sequenceList.add(SpacerSequenceElement.INSTANCE, next);
          }
        }
        else if (next instanceof BlockRenderBox)
        {
          containsContent = true;
          sequenceList.add(InlineBoxSequenceElement.INSTANCE, next);
        }
        else
        {
          containsContent = true;
          sequenceList.add(InlineNodeSequenceElement.INSTANCE, next);
        }
        next = next.getNext();
      }

      while (next == null && contextStack.isEmpty() == false)
      {
        // Finish the current box context, if needed
        sequenceList.add(EndSequenceElement.INSTANCE, context);
        next = context.getNext();
        context = (RenderBox) contextStack.pop();
      }
    }

    sequenceList.add(EndSequenceElement.INSTANCE, box);
    return sequenceList;
  }

  /**
   * Reuse the processors ..
   *
   * @param alignment
   * @return
   */
  private LastLineTextAlignmentProcessor create(final ElementAlignment alignment)
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

  private ParagraphPoolBox rebuildLastLine(final ParagraphPoolBox lineBox, final ParagraphPoolBox nextBox)
  {
    if (lineBox == null)
    {
      if (nextBox == null)
      {
        throw new NullPointerException("Both Line- and Next-Line are null.");
      }

      return rebuildLastLine(nextBox, (ParagraphPoolBox) nextBox.getVisibleNext());
    }

    if (nextBox == null)
    {
      // Linebox is finished, no need to do any merging anymore..
      return lineBox;
    }

    boolean needToAddSpacing = true;

    // do the merging ..
    final FastStack contextStack = new FastStack();
    RenderNode next = nextBox.getFirstChild();
    MergeContext context = new MergeContext(lineBox, nextBox);
    while (next != null)
    {
      // process next
      final RenderBox writeContext = context.getWriteContext();
      final StaticBoxLayoutProperties staticBoxLayoutProperties = writeContext.getStaticBoxLayoutProperties();
      long spaceWidth = staticBoxLayoutProperties.getSpaceWidth();
      if (spaceWidth == 0)
      {
        // Space has not been computed yet.
        final FontMetrics fontMetrics = metaData.getFontMetrics(writeContext.getStyleSheet());
        spaceWidth = fontMetrics.getCharWidth(' ');
        staticBoxLayoutProperties.setSpaceWidth(spaceWidth);
      }

      if (next instanceof RenderBox)
      {
        final RenderBox nBox = (RenderBox) next;
        final RenderNode firstChild = nBox.getFirstChild();
        if (firstChild != null)
        {
          contextStack.push(context);
          next = firstChild;

          final RenderNode writeContextLastChild = writeContext.getLastChild();
          if (writeContextLastChild instanceof RenderBox)
          {
            if (writeContextLastChild.getInstanceId() == nBox.getInstanceId())
            {
              context = new MergeContext((RenderBox) writeContextLastChild, nBox);
            }
            else
            {
              if (needToAddSpacing)
              {
                if (spaceWidth > 0)
                {
                  // docmark: Used zero as new height
                  writeContext.addGeneratedChild(new SpacerRenderNode(spaceWidth, 0, false));
                }
                needToAddSpacing = false;
              }
              final RenderBox newWriter = (RenderBox) nBox.derive(false);
              writeContext.addGeneratedChild(newWriter);
              context = new MergeContext(newWriter, nBox);
            }
          }
          else
          {
            if (needToAddSpacing)
            {
              if (spaceWidth > 0)
              {
                // docmark: Used zero as new height
                writeContext.addGeneratedChild(new SpacerRenderNode(spaceWidth, 0, false));
              }
              needToAddSpacing = false;
            }

            final RenderBox newWriter = (RenderBox) nBox.derive(false);
            writeContext.addGeneratedChild(newWriter);
            context = new MergeContext(newWriter, nBox);
          }
        }
        else
        {
          if (needToAddSpacing)
          {
            if (spaceWidth > 0)
            {
              // docmark: Used zero as new height
              writeContext.addGeneratedChild(new SpacerRenderNode(spaceWidth, 0, false));
            }
            needToAddSpacing = false;
          }

          writeContext.addGeneratedChild(nBox.derive(true));
          next = nBox.getNext();
        }
      }
      else
      {
        if (needToAddSpacing)
        {
          final RenderNode lastChild = writeContext.getLastChild();
          if (spaceWidth > 0 && (lastChild != null && lastChild instanceof SpacerRenderNode == false))
          {
            // docmark: Used zero as new height
            writeContext.addGeneratedChild(new SpacerRenderNode(spaceWidth, 0, false));
          }
          needToAddSpacing = false;
        }

        writeContext.addGeneratedChild(next.derive(true));
        next = next.getNext();
      }

      while (next == null && contextStack.isEmpty() == false)
      {
//        Log.debug ("FINISH " + context.getReadContext());
        next = context.getReadContext().getNext();
        context = (MergeContext) contextStack.pop();
      }
    }

    return rebuildLastLine(lineBox, (ParagraphPoolBox) nextBox.getVisibleNext());
  }
}
