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
 * AbstractPageableOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PageGrid;
import org.jfree.report.layout.output.AbstractOutputProcessor;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.PhysicalPageKey;

/**
 * Creation-Date: 09.04.2007, 10:59:35
 *
 * @author Thomas Morgner
 */
public abstract class AbstractPageableOutputProcessor extends AbstractOutputProcessor implements PageableOutputProcessor
{
  private List physicalPages;

  protected AbstractPageableOutputProcessor()
  {
    this.physicalPages = new ArrayList();
  }

  protected void processingPagesFinished()
  {
    super.processingPagesFinished();
    physicalPages = Collections.unmodifiableList(physicalPages);
  }

  public int getPhysicalPageCount()
  {
    return physicalPages.size();
  }

  public PhysicalPageKey getPhysicalPage(final int page)
  {
    if (isPaginationFinished() == false)
    {
      throw new IllegalStateException();
    }

    return (PhysicalPageKey) physicalPages.get(page);
  }

  protected LogicalPageKey createLogicalPage(final int width,
                                             final int height)
  {
    final LogicalPageKey key = super.createLogicalPage(width, height);
    for (int h = 0; h < key.getHeight(); h++)
    {
      for (int w = 0; w < key.getWidth(); w++)
      {
        physicalPages.add(key.getPage(w, h));
      }
    }
    return key;
  }

  protected void processPageContent(final LogicalPageKey logicalPageKey,
                                    final LogicalPageBox logicalPage)
      throws ContentProcessingException
  {
    final PageGrid pageGrid = logicalPage.getPageGrid();
    final int rowCount = pageGrid.getRowCount();
    final int colCount = pageGrid.getColumnCount();

    final PageFlowSelector selector = getFlowSelector();
    if (selector != null)
    {
      if (selector.isLogicalPageAccepted(logicalPageKey))
      {
        processLogicalPage(logicalPageKey, logicalPage);
      }

      for (int row = 0; row < rowCount; row++)
      {
        for (int col = 0; col < colCount; col++)
        {
          final PhysicalPageKey pageKey = logicalPageKey.getPage(col, row);
          if (selector.isPhysicalPageAccepted(pageKey))
          {
            processPhysicalPage(pageGrid, logicalPage, row, col, pageKey);
          }
        }
      }
    }
  }

  protected abstract PageFlowSelector getFlowSelector();


  protected abstract void processPhysicalPage(final PageGrid pageGrid,
                                              final LogicalPageBox logicalPage,
                                              final int row,
                                              final int col,
                                              final PhysicalPageKey pageKey)
      throws ContentProcessingException;

  protected abstract void processLogicalPage(final LogicalPageKey key,
                                             final LogicalPageBox logicalPage)
      throws ContentProcessingException;


}
