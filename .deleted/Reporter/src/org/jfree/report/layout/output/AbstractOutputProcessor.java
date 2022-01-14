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
 * AbstractOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PageGrid;

/**
 * Creation-Date: 09.04.2007, 10:51:30
 *
 * @author Thomas Morgner
 */
public abstract class AbstractOutputProcessor implements OutputProcessor
{
  protected static final int PROCESSING_PAGES = 0;
  protected static final int PROCESSING_CONTENT = 2;
  private int processingState;

  private List logicalPages;
  private int pageCursor;

  protected AbstractOutputProcessor()
  {
    logicalPages = new ArrayList();
  }

  public final int getLogicalPageCount()
  {
    return logicalPages.size();
  }

  public final LogicalPageKey getLogicalPage(final int page)
  {
    if (isPaginationFinished() == false)
    {
      throw new IllegalStateException();
    }

    return (LogicalPageKey) logicalPages.get(page);
  }


  /**
   * Checks, whether the 'processingFinished' event had been received at least once.
   *
   * @return
   */
  public final boolean isPaginationFinished()
  {
    return processingState == PROCESSING_CONTENT;
  }

  /**
   * Notifies the output processor, that the processing has been finished and that the input-feed received the last
   * event.
   */
  public final void processingFinished()
  {
    pageCursor = 0;
    if (processingState == PROCESSING_PAGES)
    {
      // the pagination is complete. So, now we can produce real content.
      processingPagesFinished();
      processingState = PROCESSING_CONTENT;
    }
    else
    {
      processingContentFinished();
    }
  }

  protected void processingContentFinished()
  {

  }

  protected void processingPagesFinished()
  {
    logicalPages = Collections.unmodifiableList(logicalPages);
  }

  protected LogicalPageKey createLogicalPage(final int width,
                                             final int height)
  {
    return new LogicalPageKey(logicalPages.size(), width, height);
  }

  public final int getPageCursor()
  {
    return pageCursor;
  }

  public final void setPageCursor(final int pageCursor)
  {
    this.pageCursor = pageCursor;
  }

  /**
   * This flag indicates, whether the output processor has collected enough information to start the content
   * generation.
   *
   * @return
   */
  protected boolean isContentGeneratable()
  {
    return processingState == PROCESSING_CONTENT;
  }

  public void processRecomputedContent(final LogicalPageBox pageBox) throws ContentProcessingException
  {
    setPageCursor(pageCursor + 1);
  }

  public final void processContent(final LogicalPageBox logicalPage)
       throws ContentProcessingException
  {
    if (isContentGeneratable() == false)
    {
      // This is the pagination stage ..

      // This is just an assertation ...
      // Only if pagination is active ..
      final PageGrid pageGrid = logicalPage.getPageGrid();
      final int rowCount = pageGrid.getRowCount();
      final int colCount = pageGrid.getColumnCount();

      final LogicalPageKey key = createLogicalPage(colCount, rowCount);
      logicalPages.add(key);
      final int pageCursor = getPageCursor();
      if (key.getPosition() != pageCursor)
      {
        throw new IllegalStateException("Expected position " + pageCursor + " is not the key's position " + key.getPosition());
      }

      processPaginationContent(key, logicalPage);
      setPageCursor(pageCursor + 1);

    }
    else // if (isContentGeneratable())
    {
      // This is the content generation stage ..

      final int pageCursor = getPageCursor();
      final LogicalPageKey logicalPageKey = getLogicalPage(pageCursor);
      processPageContent(logicalPageKey, logicalPage);
      setPageCursor(pageCursor + 1);
    }
  }

  public boolean isNeedAlignedPage()
  {
    return isContentGeneratable();
  }

  protected void processPaginationContent(final LogicalPageKey logicalPageKey,
                                          final LogicalPageBox logicalPage)
       throws ContentProcessingException
  {
  }

  protected abstract void processPageContent(final LogicalPageKey logicalPageKey,
                                             final LogicalPageBox logicalPage)
       throws ContentProcessingException;

  public int getPhysicalPageCount()
  {
    // By default, we assume a one-to-one mapping between logical and physical pages. Only the pageable target
    // will implement a different mapping ..
    return getLogicalPageCount();
  }
}
