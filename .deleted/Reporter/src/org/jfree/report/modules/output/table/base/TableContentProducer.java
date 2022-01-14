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
 * TableContentProducer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.base;

import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.process.IterateStructuralProcessStep;
import org.jfree.report.layout.process.ProcessUtility;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * After the pagination was able to deriveForAdvance the destTable-structure (all destColumn and row-breaks are now known), this
 * second step flattens the layout-tree into a two-dimensional destTable structure.
 *
 * @author Thomas Morgner
 */
public class TableContentProducer extends IterateStructuralProcessStep
{
  private SheetLayout sheetLayout;
  private GenericObjectTable contentBackend;

  private long maximumHeight;
  private long maximumWidth;

  private TableRectangle lookupRectangle;
  private long pageOffset;
  private long pageEnd;
  private String sheetName;
  private boolean iterativeUpdate;
  //  private boolean performOutput;
  private int finishedRows;
  private int filledRows;
  private long contentOffset;
  private long effectiveOffset;
  private boolean unalignedPagebands;
  private boolean headerProcessed;
  private boolean ellipseAsBackground;
  private boolean shapesAsContent;

  private boolean verboseCellMarkers;
  private boolean debugReportLayout;
  private boolean reportCellConflicts;

  public TableContentProducer(final SheetLayout sheetLayout,
                              final OutputProcessorMetaData metaData)
  {
    this.unalignedPagebands = metaData.isFeatureSupported(OutputProcessorFeature.UNALIGNED_PAGEBANDS);
    this.shapesAsContent = metaData.isFeatureSupported(AbstractTableOutputProcessor.SHAPES_CONTENT);
    this.ellipseAsBackground = metaData.isFeatureSupported(AbstractTableOutputProcessor.TREAT_ELLIPSE_AS_RECTANGLE);
    this.sheetLayout = sheetLayout;
    this.maximumHeight = sheetLayout.getMaxHeight();
    this.maximumWidth = sheetLayout.getMaxWidth();
    this.contentBackend = new GenericObjectTable();
    this.contentBackend.ensureCapacity(sheetLayout.getRowCount(), sheetLayout.getColumnCount());

    final Configuration config = metaData.getConfiguration();
    this.debugReportLayout = "true".equals(config.getConfigProperty
        ("org.jfree.report.modules.output.table.base.DebugReportLayout"));
    this.verboseCellMarkers = "true".equals(config.getConfigProperty
        ("org.jfree.report.modules.output.table.base.VerboseCellMarkers"));
    this.reportCellConflicts = "true".equals(config.getConfigProperty
        ("org.jfree.report.modules.output.table.base.ReportCellConflicts"));
  }

  public String getSheetName()
  {
    return sheetName;
  }

  public void compute(final LogicalPageBox logicalPage,
                      final boolean iterativeUpdate,
                      final boolean performOutput)
  {
    this.iterativeUpdate = iterativeUpdate;

//    this.performOutput = performOutput;
    this.sheetName = null;
    if (unalignedPagebands == false)
    {
      // The page-header and footer area are aligned/shifted within the logical pagebox so that all areas
      // share a common coordinate system. This also implies, that the whole logical page is aligned content.
      pageOffset = 0;
      pageEnd = logicalPage.getPageEnd() - logicalPage.getPageOffset();
      effectiveOffset = 0;
      //Log.debug ("Content Processing " + pageOffset + " -> " + pageEnd);
      if (startBlockBox(logicalPage))
      {
        if (headerProcessed == false)
        {
          startProcessing(logicalPage.getWatermarkArea());
          final BlockRenderBox headerArea = logicalPage.getHeaderArea();
          startProcessing(headerArea);
          headerProcessed = true;
        }

        processBoxChilds(logicalPage);
        if (iterativeUpdate == false)
        {
          startProcessing(logicalPage.getFooterArea());
        }
      }
      finishBlockBox(logicalPage);
      //ModelPrinter.print(logicalPage);
    }
    else
    {
      // The page-header and footer area are not aligned/shifted within the logical pagebox.
      // All areas have their own coordinate system starting at (0,0). We apply a manual shift here
      // so that we dont have to modify the nodes (which invalidates the cache, and therefore is ugly)

      //Log.debug ("Content Processing " + pageOffset + " -> " + pageEnd);
      effectiveOffset = 0;
      pageOffset = 0;
      pageEnd = logicalPage.getPageEnd();
      if (startBlockBox(logicalPage))
      {
        if (headerProcessed == false)
        {
          contentOffset = 0;
          effectiveOffset = 0;

          final BlockRenderBox watermarkArea = logicalPage.getWatermarkArea();
          pageEnd = watermarkArea.getHeight();
          startProcessing(watermarkArea);

          final BlockRenderBox headerArea = logicalPage.getHeaderArea();
          pageEnd = headerArea.getHeight();
          startProcessing(headerArea);
          contentOffset = headerArea.getHeight();
          headerProcessed = true;
        }

        pageOffset = logicalPage.getPageOffset();
        pageEnd = logicalPage.getPageEnd();
        effectiveOffset = contentOffset;
        processBoxChilds(logicalPage);

        if (iterativeUpdate == false)
        {
          pageOffset = 0;
          final BlockRenderBox footerArea = logicalPage.getFooterArea();
          final long footerOffset = contentOffset + (logicalPage.getPageEnd() - logicalPage.getPageOffset());
          pageEnd = footerOffset + footerArea.getHeight();
          effectiveOffset = footerOffset;
          startProcessing(footerArea);
        }
      }
      finishBlockBox(logicalPage);
      //ModelPrinter.print(logicalPage);
    }

    if (iterativeUpdate)
    {
//      Log.debug("iterative: Computing commited rows: " + sheetLayout.getRowCount() + " vs. " + contentBackend.getRowCount());
      updateFilledRows();
    }
    else
    {
//      Log.debug("Non-iterative: Assuming all rows are commited: " + sheetLayout.getRowCount() + " vs. " + contentBackend.getRowCount());
//      updateFilledRows();
      filledRows = getRowCount();
    }

    if (iterativeUpdate == false)
    {
      headerProcessed = false;
    }

//    if (contentBackend.getRowCount() > 10000)
//    {
    int counter = 0;
    //final int rowCount = contentBackend.getRowCount();
    final int columnCount = contentBackend.getColumnCount();
    for (int r = 0; r < finishedRows; r++)
    {
      for (int c = 0; c < columnCount; c++)
      {
        final Object o = contentBackend.getObject(r, c);
        if (o instanceof ContentMarker)
        {
          counter += 1;
        }
      }
    }
    if (counter > 0)
    {
      Log.debug("Counter: " + finishedRows + " -> " + counter);
    }
//    }
  }

  public TableCellDefinition getBackground(final int row, final int column)
  {
    return sheetLayout.getBackgroundAt(row, column);
  }

  public RenderBox getContent(final int row, final int column)
  {
    final CellMarker marker = (CellMarker) contentBackend.getObject(row, column);
    if (marker == null)
    {
      return null;
    }
    return marker.getContent();
  }

  public long getContentOffset(final int row, final int column)
  {
    final CellMarker marker = (CellMarker) contentBackend.getObject(row, column);
    if (marker == null)
    {
      return 0;
    }
    return marker.getContentOffset();
  }

  public int getRowCount()
  {
    return Math.max(contentBackend.getRowCount(), sheetLayout.getRowCount());
  }

  public int getColumnCount()
  {
    return Math.max(contentBackend.getColumnCount(), sheetLayout.getColumnCount());
  }

  private boolean startBox(final RenderBox box)
  {
    if (box.isFinished())
    {
      return true;
    }

//    if (box.isOpen())
//    {
//      Log.debug("Received open box: " + box);
//    }

    final long y = effectiveOffset + box.getY() - pageOffset;
    final long height = box.getHeight();

    final long pageHeight = effectiveOffset + (pageEnd - pageOffset);

//    Log.debug ("Processing Box " + effectiveOffset + " " + pageHeight + " -> " + y + " " + height);
//    Log.debug ("Processing Box " + box);
//

    if (height > 0)
    {
      if ((y + height) <= effectiveOffset)
      {
        return false;
      }
      if (y >= pageHeight)
      {
        return false;
      }
    }
    else
    {
      // zero height boxes are always a bit tricky ..
      if ((y + height) < effectiveOffset)
      {
        return false;
      }
      if (y > pageHeight)
      {
        return false;
      }
    }

    // Always process everything ..
    final long y1 = Math.max(0, y);
    final long boxX = box.getX();
    final long x1 = Math.max(0, boxX);
    final long y2 = Math.min(y + box.getHeight(), maximumHeight);
    final long x2 = Math.min(boxX + box.getWidth(), maximumWidth);
    lookupRectangle = sheetLayout.getTableBounds(x1, y1, x2 - x1, y2 - y1, lookupRectangle);

    if (ProcessUtility.isContent(box, false, ellipseAsBackground, shapesAsContent) == false)
    {

      final String sheetName = (String) box.getStyleSheet().getStyleProperty(BandStyleKeys.COMPUTED_SHEETNAME);
      if (sheetName != null)
      {
//        if (sheetName.equals(this.sheetName) == false)
//        {
//          Log.debug ("Received new sheetname: " + sheetName);
//          Log.debug ("                      : " + box);
//        }
        this.sheetName = sheetName;
      }

      if (box.isCommited())
      {
        box.setFinished(true);
      }

      final int rectX2 = lookupRectangle.getX2();
      final int rectY2 = lookupRectangle.getY2();
      contentBackend.ensureCapacity(rectY2, rectX2);

      if (box.isFinished())
      {
        if (box.isCommited() == false)
        {
          throw new IllegalStateException();
        }
        //Log.debug("Processing box-cell with bounds (" + x1 + ", " + y1 + ")(" + x2 + ", " + y2 + ")");
        final BandMarker bandMarker;
        if (verboseCellMarkers)
        {
          bandMarker = new BandMarker(box.toString());
        }
        else
        {
          bandMarker = BandMarker.INSTANCE;
        }
        for (int r = lookupRectangle.getY1(); r < rectY2; r++)
        {
          for (int c = lookupRectangle.getX1(); c < rectX2; c++)
          {
            final Object o = contentBackend.getObject(r, c);
            if (o == null)
            {
              contentBackend.setObject(r, c, bandMarker);
            }
          }
        }
      }
      return true;
    }
    if (box.isCommited() == false)
    {
      // content-box is not finished yet.
//      if (iterativeUpdate == false)
//      {
//        Log.debug("Still Skipping content-cell with bounds (" + x1 + ", " + y1 + ")(" + x2 + ", " + y2 + ")");
//      }
      return false;
    }

    //Log.debug("Processing content-cell with bounds (" + x1 + ", " + y1 + ")(" + x2 + ", " + y2 + ")");
    final String sheetName = (String) box.getStyleSheet().getStyleProperty(BandStyleKeys.COMPUTED_SHEETNAME);
    if (sheetName != null)
    {
//      if (sheetName.equals(this.sheetName) == false)
//      {
//        Log.debug ("Received new sheetname: " + sheetName);
//        Log.debug ("                      : " + box);
//      }
      this.sheetName = sheetName;
    }

    if (isCellSpaceOccupied(lookupRectangle) == false)
    {
      final int rectX2 = lookupRectangle.getX2();
      final int rectY2 = lookupRectangle.getY2();
      contentBackend.ensureCapacity(rectY2, rectX2);
      final ContentMarker contentMarker = new ContentMarker(box, effectiveOffset - pageOffset);
      for (int r = lookupRectangle.getY1(); r < rectY2; r++)
      {
        for (int c = lookupRectangle.getX1(); c < rectX2; c++)
        {
          contentBackend.setObject(r, c, contentMarker);
        }
      }

      // Setting this content-box to finished has to be done in the actual content-generator.
    }
    else
    {
      if (reportCellConflicts)
      {
        Log.debug("LayoutShift: Offending Content: " + box);
        Log.debug("LayoutShift: Offending Content: " + box.isFinished());
      }
      box.setFinished(true);
    }
    return true;
  }


  private boolean isCellSpaceOccupied(final TableRectangle rect)
  {
    final int x2 = rect.getX2();
    final int y2 = rect.getY2();

    for (int r = rect.getY1(); r < y2; r++)
    {
      for (int c = rect.getX1(); c < x2; c++)
      {
        final Object object = contentBackend.getObject(r, c);
        if (object != null && object instanceof BandMarker == false)
        {
          if (reportCellConflicts)
          {
            Log.debug("Cell (" + c + ", " + r + ") already filled: Content in cell: " + object);
          }
          return true;
        }
      }
    }
    return false;
  }


  public int getFinishedRows()
  {
    return finishedRows;
  }

  public void clearFinishedBoxes()
  {
    final int rowCount = getFilledRows();
    final int columnCount = getColumnCount();
    if (debugReportLayout)
    {
      Log.debug("Request: Clearing rows from " + finishedRows + " to " + rowCount);
    }

    int lastRowCleared = finishedRows - 1;
    for (int row = finishedRows; row < rowCount; row++)
    {
      boolean rowHasContent = false;
      for (int column = 0; column < columnCount; column++)
      {
        final CellMarker o = (CellMarker) contentBackend.getObject(row, column);
        if (o == null)
        {
          if (debugReportLayout)
          {
            Log.debug("Cannot clear row: Cell (" + column + ", " + row + ") is undefined.");
          }
          return;
        }
        final boolean b = o.isFinished();
        if (b == false)
        {
          if (debugReportLayout)
          {
            Log.debug("Cannot clear row: Cell (" + column + ", " + row + ") is not finished: " + o);
          }
          return;
        }
        else
        {
          if (rowHasContent == false && o.getContent() != null)
          {
            rowHasContent = true;
          }
        }
      }

      if (rowHasContent)
      {
        finishedRows = row + 1;
//        if (debugReportLayout)
//        {
//          Log.debug("Clearing rows from " + (lastRowCleared + 1)+ " to " + finishedRows);
//        }
        for (int clearRowNr = lastRowCleared + 1; clearRowNr < finishedRows; clearRowNr++)
        {
          if (debugReportLayout)
          {
            Log.debug("#Cleared row: " + row + '/' + clearRowNr);
          }
          for (int column = 0; column < columnCount; column++)
          {
            final Object o = contentBackend.getObject(clearRowNr, column);
            final FinishedMarker finishedMarker;
            if (verboseCellMarkers)
            {
              finishedMarker = new FinishedMarker(String.valueOf(o));
            }
            else
            {
              finishedMarker = FinishedMarker.INSTANCE;
            }
            contentBackend.setObject(clearRowNr, column, finishedMarker);
          }
        }
        lastRowCleared = row;
      }
      else if (debugReportLayout)
      {
        lastRowCleared = row;
        Log.debug("-Cleared row: " + row + '.');
      }
    }

    if (debugReportLayout)
    {
        Log.debug("Need to clear  row: " + (lastRowCleared + 1) + " - " + filledRows);
    }
    finishedRows = filledRows;
    for (int clearRowNr = lastRowCleared + 1; clearRowNr < finishedRows; clearRowNr++)
    {
      if (debugReportLayout)
      {
        Log.debug("*Cleared row: " + clearRowNr + '.');
      }
      for (int column = 0; column < columnCount; column++)
      {
        final Object o = contentBackend.getObject(clearRowNr, column);
        final FinishedMarker finishedMarker;
        if (verboseCellMarkers)
        {
          finishedMarker = new FinishedMarker(String.valueOf(o));
        }
        else
        {
          finishedMarker = FinishedMarker.INSTANCE;
        }
        contentBackend.setObject(clearRowNr, column, finishedMarker);
      }
    }

  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    return startBox(box);
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    // we should not have come that far ..
    return false;
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    return startBox(box);
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    return startBox(box);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // not needed.
  }

  public SheetLayout getSheetLayout()
  {
    return sheetLayout;
  }

  public int getFilledRows()
  {
    return filledRows;
  }

  private void updateFilledRows()
  {
    final int rowCount = contentBackend.getRowCount();
    final int columnCount = getColumnCount();
    filledRows = finishedRows;
    for (int row = finishedRows; row < rowCount; row++)
    {
      for (int column = 0; column < columnCount; column++)
      {
        final CellMarker o = (CellMarker) contentBackend.getObject(row, column);
        if (o == null)
        {
          if (debugReportLayout)
          {
            Log.debug("Row: Cell (" + column + ", " + row + ") is undefined.");
          }
          return;
        }
        if (o.isCommited() == false)
        {
          if (debugReportLayout)
          {
            Log.debug("Row: Cell (" + column + ", " + row + ") is not commited.");
          }
          return;
        }
      }

//      Log.debug("Processable Row: " + filledRows + ".");
      filledRows = row + 1;
    }

    if (debugReportLayout)
    {
      Log.debug("Processable Rows: " + finishedRows + ' ' + filledRows + '.');
    }
  }

}
