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
 * PageableRenderer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.base;

import org.jfree.report.PageDefinition;
import org.jfree.report.layout.AbstractRenderer;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.LayoutPagebreakHandler;
import org.jfree.report.layout.output.OutputProcessor;
import org.jfree.report.layout.process.ApplyPageShiftValuesStep;
import org.jfree.report.layout.process.CleanPaginatedBoxesStep;
import org.jfree.report.layout.process.FillPhysicalPagesStep;
import org.jfree.report.layout.process.PaginationResult;
import org.jfree.report.layout.process.PaginationStep;
import org.jfree.report.util.InstanceID;
import org.jfree.util.Log;

/**
 * Creation-Date: 08.04.2007, 15:08:48
 *
 * @author Thomas Morgner
 */
public class PageableRenderer extends AbstractRenderer
{
  private PaginationStep paginationStep;
  private FillPhysicalPagesStep fillPhysicalPagesStep;
  private CleanPaginatedBoxesStep cleanPaginatedBoxesStep;
  private ApplyPageShiftValuesStep applyPageShiftValuesStep;
  private int pageCount;
  private long lastPageAge;
  private boolean pageStartPending;

  public PageableRenderer(final OutputProcessor outputProcessor)
  {
    super(outputProcessor);
    this.paginationStep = new PaginationStep();
    this.fillPhysicalPagesStep = new FillPhysicalPagesStep();
    this.cleanPaginatedBoxesStep = new CleanPaginatedBoxesStep();
    this.applyPageShiftValuesStep = new ApplyPageShiftValuesStep();
    this.lastPageAge = System.currentTimeMillis();
  }

  public void startReport(final PageDefinition pageDefinition)
  {
    final long prePageAge = System.currentTimeMillis();
    Log.debug("Time to pagination " + (prePageAge - lastPageAge));
    this.lastPageAge = prePageAge;

    super.startReport(pageDefinition);
    pageCount = 0;
  }

//
//  public Renderer deriveForStorage()
//  {
//    if (pageCount == 3)
//    {
//      ModelPrinter.print(getPageBox());
//    }
//    else
//    {
//      Log.debug ("PageCount " + pageCount);
//    }
//    return super.deriveForStorage();
//  }

  protected void debugPrint(final LogicalPageBox pageBox)
  {
//    if (pageCount != 53)
//    {
//      return;
//    }
//
//    Log.debug("**** Start Printing Page: " + pageCount);
//    FileModelPrinter.print(pageBox);
//    Log.debug("**** Done  Printing Page: " + pageCount);
  }

  protected boolean isPageFinished()
  {
    final LogicalPageBox pageBox = getPageBox();
//    final long sizeBeforePagination = pageBox.getHeight();
//    final LogicalPageBox clone = (LogicalPageBox) pageBox.derive(true);
    final PaginationResult pageBreak = paginationStep.performPagebreak(pageBox);
    if (pageBreak.isOverflow() || pageBox.isOpen() == false)
    {
      setLastStateKey(pageBreak.getLastVisibleState());
      return true;
    }
    return false;
  }

  protected boolean performPagination(final LayoutPagebreakHandler layoutPagebreakHandler,
                                      final boolean performOutput)
      throws ContentProcessingException
  {
    // next: perform pagination.
    final LogicalPageBox pageBox = getPageBox();

    //    final long sizeBeforePagination = pageBox.getHeight();
    //    final LogicalPageBox clone = (LogicalPageBox) pageBox.derive(true);
    final PaginationResult pageBreak = paginationStep.performPagebreak(pageBox);
    if (pageBreak.isOverflow() || pageBox.isOpen() == false)
    {
//      final long sizeAfterPagination = pageBox.getHeight();
      setLastStateKey(pageBreak.getLastVisibleState());
      setPagebreaks(getPagebreaks() + 1);
      pageBox.setAllVerticalBreaks(pageBreak.getAllBreaks());

      pageCount += 1;
      debugPrint(pageBox);

      // A new page has been started. Recover the page-grid, then restart
      // everything from scratch. (We have to recompute, as the pages may
      // be different now, due to changed margins or page definitions)
      final OutputProcessor outputProcessor = getOutputProcessor();
      final long nextOffset = pageBreak.getLastPosition();
      final long pageOffset = pageBox.getPageOffset();

      if (performOutput)
      {
        if (outputProcessor.isNeedAlignedPage())
        {
          final LogicalPageBox box = fillPhysicalPagesStep.compute(pageBox, pageOffset, nextOffset);
          outputProcessor.processContent(box);
//          Log.debug("Processing contents for Page " + pageCount + " Page-Offset: " + pageOffset + " -> " + nextOffset);
        }
        else
        {
//          Log.debug("Processing fast contents for Page " + pageCount + " Page-Offset: " + pageOffset + " -> " + nextOffset);
          outputProcessor.processContent(pageBox);
        }
      }
      else
      {
        // todo: When recomputing the contents, we have to update the page cursor or the whole
        // excercise is next to useless ..
//        Log.debug("Recomputing contents for Page " + pageCount + " Page-Offset: " + pageOffset + " -> " + nextOffset);
        outputProcessor.processRecomputedContent(pageBox);
      }

      // Now fire the pagebreak. This goes through all layers and informs all
      // components, that a pagebreak has been encountered and possibly a
      // new page has been set. It does not save the state or perform other
      // expensive operations. However, it updates the 'isPagebreakEncountered'
      // flag, which will be active until the input-feed received a new event.
      final long currentPageAge = System.currentTimeMillis();
//      Log.debug ("PageTime " + (currentPageAge - lastPageAge));
      lastPageAge = currentPageAge;

      final boolean repeat = pageBox.isOpen() || (pageBox.getHeight() > nextOffset);
      if (repeat)
      {
//        Log.debug(new MemoryUsageMessage("PAGEBREAK ENCOUNTERED"));
        //Log.debug("Page-Offset: " + pageOffset + " -> " + nextOffset);

        pageBox.setPageOffset(nextOffset);

        final long l = cleanPaginatedBoxesStep.compute(pageBox);
        if (l > 0)
        {
          final InstanceID shiftNode = cleanPaginatedBoxesStep.getShiftNode();
          applyPageShiftValuesStep.compute(pageBox, l, shiftNode);
          debugPrint(pageBox);
        }
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
        pageBox.setPageOffset(nextOffset);
        outputProcessor.processingFinished();
        return false;
      }
    }
    return false;
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


  public int getPageCount()
  {
    return pageCount;
  }

  public boolean isPageStartPending()
  {
    return pageStartPending;
  }
}
