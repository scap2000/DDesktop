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
 * FlowRenderer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import org.jfree.report.PageDefinition;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.IterativeOutputProcessor;
import org.jfree.report.layout.output.LayoutPagebreakHandler;
import org.jfree.report.layout.output.OutputProcessor;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.process.ApplyAutoCommitPageHeaderStep;
import org.jfree.report.layout.process.ApplyPageShiftValuesStep;
import org.jfree.report.layout.process.CleanFlowBoxesStep;
import org.jfree.report.layout.process.CleanPaginatedBoxesStep;
import org.jfree.report.layout.process.FillFlowPagesStep;
import org.jfree.report.layout.process.FlowPaginationStep;
import org.jfree.report.layout.process.PaginationResult;
import org.jfree.report.util.InstanceID;

/**
 * A flow renderer is a light-weight paginating renderer. It does not care about the page-size but searches for manual
 * breaks. Once a manual break is encountered, the flow shifts and creates a page-event. (This is the behavior of the
 * old destTable-exporters.)
 * <p/>
 * This implementation is a mix of a paginated and streaming renderer.
 *
 * @author Thomas Morgner
 */
public class FlowRenderer extends AbstractRenderer
{
  private FlowPaginationStep paginationStep;
  private FillFlowPagesStep fillPhysicalPagesStep;
  private CleanPaginatedBoxesStep cleanPaginatedBoxesStep;
  private CleanFlowBoxesStep cleanFlowBoxesStep;
  private ApplyPageShiftValuesStep applyPageShiftValuesStep;
  private ApplyAutoCommitPageHeaderStep applyAutoCommitPageHeaderStep;
  private int flowCount;
  private long lastPageAge;
  private boolean pageStartPending;

  public FlowRenderer(final OutputProcessor outputProcessor)
  {
    super(outputProcessor);
    this.paginationStep = new FlowPaginationStep();
    this.fillPhysicalPagesStep = new FillFlowPagesStep();
    this.cleanPaginatedBoxesStep = new CleanPaginatedBoxesStep();
    this.cleanFlowBoxesStep = new CleanFlowBoxesStep();
    this.applyPageShiftValuesStep = new ApplyPageShiftValuesStep();
    this.applyAutoCommitPageHeaderStep = new ApplyAutoCommitPageHeaderStep();
  }

  public void startReport(final PageDefinition pageDefinition)
  {
    super.startReport(pageDefinition);
    lastPageAge = System.currentTimeMillis();
  }

  protected boolean isPageFinished()
  {
    final LogicalPageBox pageBox = getPageBox();
//    final long sizeBeforePagination = pageBox.getHeight();
    //final LogicalPageBox clone = (LogicalPageBox) pageBox.deriveForAdvance(true);
    final PaginationResult pageBreak = paginationStep.performPagebreak(pageBox);
    if (pageBreak.isOverflow() || pageBox.isOpen() == false)
    {
      setLastStateKey(pageBreak.getLastVisibleState());
      return true;
    }
    return false;
  }

  protected void debugPrint(final LogicalPageBox pageBox)
  {
//    ModelPrinter.print(pageBox);
  }


  public void processIncrementalUpdate(final boolean performOutput) throws ContentProcessingException
  {
    if (isDirty() == false)
    {
//      Log.debug ("Not dirty, no update needed.");
      return;
    }
    clearDirty();

    final OutputProcessor outputProcessor = getOutputProcessor();
    if (outputProcessor instanceof IterativeOutputProcessor == false ||
        outputProcessor.getMetaData().isFeatureSupported(OutputProcessorFeature.ITERATIVE_RENDERING) == false)
    {
//      Log.debug ("No incremental system.");
      return;
    }


    final LogicalPageBox pageBox = getPageBox();
    pageBox.setPageEnd(pageBox.getHeight());
//    Log.debug ("Computing Incremental update: " + pageBox.getPageOffset() + " " + pageBox.getPageEnd());
    // shiftBox(pageBox, true);

    if (pageBox.isOpen())
    {
      final IterativeOutputProcessor io = (IterativeOutputProcessor) outputProcessor;
      if (applyAutoCommitPageHeaderStep.compute(pageBox))
      {
        io.processIterativeContent(pageBox, performOutput);
        cleanFlowBoxesStep.compute(pageBox);
      }
    }
  }

  protected boolean performPagination(final LayoutPagebreakHandler layoutPagebreakHandler,
                                      final boolean performOutput)
      throws ContentProcessingException
  {
    final OutputProcessor outputProcessor = getOutputProcessor();
    // next: perform pagination.
    final LogicalPageBox pageBox = getPageBox();
//    final long sizeBeforePagination = pageBox.getHeight();
    //final LogicalPageBox clone = (LogicalPageBox) pageBox.deriveForAdvance(true);
    final PaginationResult pageBreak = paginationStep.performPagebreak(pageBox);
    if (pageBreak.isOverflow() || pageBox.isOpen() == false)
    {
      setLastStateKey(pageBreak.getLastVisibleState());
//      final long sizeAfterPagination = pageBox.getHeight();
      setPagebreaks(getPagebreaks() + 1);
      pageBox.setAllVerticalBreaks(pageBreak.getAllBreaks());

      flowCount += 1;
      debugPrint(pageBox);

      // A new page has been started. Recover the page-grid, then restart
      // everything from scratch. (We have to recompute, as the pages may
      // be different now, due to changed margins or page definitions)
      final long nextOffset = pageBox.computePageEnd();
      pageBox.setPageEnd(nextOffset);
      final long pageOffset = pageBox.getPageOffset();

      if (performOutput)
      {
        if (outputProcessor.isNeedAlignedPage())
        {
          final LogicalPageBox box = fillPhysicalPagesStep.compute(pageBox, pageOffset, nextOffset);
//          Log.debug("Processing contents for Page " + flowCount + " Page-Offset: " + pageOffset + " -> " + nextOffset);

          outputProcessor.processContent(box);
        }
        else
        {
//          Log.debug("Processing fast contents for Page " + flowCount + " Page-Offset: " + pageOffset + " -> " + nextOffset);
          outputProcessor.processContent(pageBox);
        }
      }
      else
      {
//        Log.debug("Recomputing contents for Page " + flowCount + " Page-Offset: " + pageOffset + " -> " + nextOffset);
        outputProcessor.processRecomputedContent(pageBox);
      }

      // Now fire the pagebreak. This goes through all layers and informs all
      // components, that a pagebreak has been encountered and possibly a
      // new page has been set. It does not save the state or perform other
      // expensive operations. However, it updates the 'isPagebreakEncountered'
      // flag, which will be active until the input-feed received a new event.
      final long currentPageAge = System.currentTimeMillis();
//      Log.debug("PageTime " + (currentPageAge - lastPageAge));
      lastPageAge = currentPageAge;

      final boolean repeat = pageBox.isOpen() || pageBreak.isOverflow();
      if (repeat)
      {
        // pageBox.setAllVerticalBreaks(pageBreak.getAllBreaks());
        // First clean all boxes that have been marked as finished. This reduces the overall complexity of the
        // pagebox and improves performance on huge reports.
        cleanFlowBoxesStep.compute(pageBox);

        final long l = cleanPaginatedBoxesStep.compute(pageBox);
        if (l > 0)
        {
//          Log.debug ("Apply shift afterwards " + l);
          final InstanceID shiftNode = cleanPaginatedBoxesStep.getShiftNode();
          applyPageShiftValuesStep.compute(pageBox, l, shiftNode);
          debugPrint(pageBox);
        }

        pageBox.setPageOffset(nextOffset);
        if (pageBreak.isNextPageContainsContent())
        {
          if (layoutPagebreakHandler != null)
          {
            layoutPagebreakHandler.pageStarted();
          }
          return true;
        }
        // No need to try again, we know that the result will not change, as the next page is
        // empty. (We already tested it.)
        pageStartPending = true;
        return false;
      }
      else
      {
        outputProcessor.processingFinished();
        pageBox.setPageOffset(nextOffset);
        return false;
      }
    }
    else if (outputProcessor instanceof IterativeOutputProcessor &&
             outputProcessor.getMetaData().isFeatureSupported(OutputProcessorFeature.ITERATIVE_RENDERING))
    {
      processIncrementalUpdate(performOutput);
//      final IterativeOutputProcessor io = (IterativeOutputProcessor) outputProcessor;
//      io.processIterativeContent(pageBox, performOutput);
//      cleanFlowBoxesStep.compute(pageBox);
    }
    return false;
  }

  public int getFlowCount()
  {
    return flowCount;
  }

  public boolean clearPendingPageStart(final LayoutPagebreakHandler layoutPagebreakHandler)
  {
    if (pageStartPending == false)
    {
      return false;
    }

    if (layoutPagebreakHandler != null)
    {
      layoutPagebreakHandler.pageStarted();
    }
    pageStartPending = false;
    return true;
  }

  public boolean isPageStartPending()
  {
    return pageStartPending;
  }
}
