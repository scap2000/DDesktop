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
 * RTFPrinter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.rtf.helper;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.table.RtfBorder;
import com.lowagie.text.rtf.table.RtfBorderGroup;
import com.lowagie.text.rtf.table.RtfCell;

import java.awt.Color;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jfree.fonts.itext.BaseFontSupport;
import org.jfree.report.InvalidReportStateException;
import org.jfree.report.JFreeReportInfo;
import org.jfree.report.layout.model.BorderEdge;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PhysicalPageBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.TableCellDefinition;
import org.jfree.report.modules.output.table.base.TableContentProducer;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.util.NoCloseOutputStream;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Creation-Date: 09.05.2007, 14:52:05
 *
 * @author Thomas Morgner
 */
public class RTFPrinter
{
  private static final String CREATOR =
      JFreeReportInfo.getInstance().getName() + " version " +
          JFreeReportInfo.getInstance().getVersion();

  private InputStream templateInputStream;
  private OutputStream outputStream;
  private Configuration config;
  private RTFImageCache imageCache;
  private Document document;
  private BaseFontSupport baseFontSupport;
  private Table table;

  public RTFPrinter()
  {
  }

  public void init(final Configuration config,
                   final OutputStream outputStream)
  {
    this.outputStream = outputStream;
    this.config = config;
  }

  public InputStream getTemplateInputStream()
  {
    return templateInputStream;
  }

  public void setTemplateInputStream(final InputStream templateInputStream)
  {
    this.templateInputStream = templateInputStream;
  }

  /** @noinspection IOResourceOpenedButNotSafelyClosed*/
  public void print(final LogicalPageKey logicalPageKey,
                    final LogicalPageBox logicalPage,
                    final TableContentProducer contentProducer,
                    final OutputProcessorMetaData metaData,
                    final boolean incremental)
      throws ContentProcessingException
  {
    if (document == null)
    {
      final RTFOutputProcessorMetaData rtfMetaData = (RTFOutputProcessorMetaData) metaData;
      baseFontSupport = rtfMetaData.getITextFontStorage().getBaseFontSupport();

      final PhysicalPageBox pageFormat = logicalPage.getPageGrid().getPage(0, 0);
      final float urx = (float) StrictGeomUtility.toExternalValue(pageFormat.getWidth());
      final float ury = (float) StrictGeomUtility.toExternalValue(pageFormat.getHeight());

      final float marginLeft = (float) StrictGeomUtility.toExternalValue(pageFormat.getImageableX());
      final float marginRight =
          (float) StrictGeomUtility.toExternalValue(pageFormat.getWidth()
              - pageFormat.getImageableWidth() - pageFormat.getImageableX());
      final float marginTop = (float) StrictGeomUtility.toExternalValue(pageFormat.getImageableY());
      final float marginBottom =
          (float) StrictGeomUtility.toExternalValue(pageFormat.getHeight()
              - pageFormat.getImageableHeight() - pageFormat.getImageableY());
      final Rectangle pageSize = new Rectangle(urx, ury);

      document = new Document(pageSize, marginLeft, marginRight, marginTop, marginBottom);
      imageCache = new RTFImageCache();

      // rtf does not support PageFormats or other meta data...
      final RtfWriter2 instance = RtfWriter2.getInstance(document, new NoCloseOutputStream(outputStream));
      instance.getDocumentSettings().setAlwaysUseUnicode(true);

      final String author = config.getConfigProperty
          ("org.jfree.report.modules.output.table.rtf.Author");
      if (author != null)
      {
        document.addAuthor(author);
      }

      final String title = config.getConfigProperty("org.jfree.report.modules.output.table.rtf.Title");
      if (title != null)
      {
        document.addTitle(title);
      }

      document.addProducer();
      document.addCreator(CREATOR);

      try
      {
        document.addCreationDate();
      }
      catch (Exception e)
      {
        Log.debug("Unable to add creation date. It will have to work without it.", e);
      }

      document.open();
    }

    // Start a new page.
    try
    {
      final SheetLayout sheetLayout = contentProducer.getSheetLayout();
      final int columnCount = contentProducer.getColumnCount();
      if (table == null)
      {
        final int rowCount = contentProducer.getRowCount();
        table = new Table(columnCount, rowCount);
        table.setAutoFillEmptyCells(false);
        table.setWidth(100); // span the full page..
        // and finally the content ..

        final float[] cellWidths = new float[columnCount];
        for (int i = 0; i < columnCount; i++)
        {
          cellWidths[i] = (float) StrictGeomUtility.toExternalValue(sheetLayout.getCellWidth(i, i + 1));
        }
        table.setWidths(cellWidths);
      }

      final int startRow = contentProducer.getFinishedRows();
      final int finishRow = contentProducer.getFilledRows();
      //Log.debug ("Processing: " + startRow + " " + finishRow + " " + incremental);

      for (int row = startRow; row < finishRow; row++)
      {
        for (short col = 0; col < columnCount; col++)
        {
          final RenderBox content = contentProducer.getContent(row, col);
          final TableCellDefinition background = sheetLayout.getBackgroundAt(row, col);

          if (content == null && background == null)
          {
            // An empty cell .. ignore
            final RtfCell cell = new RtfCell();
            cell.setBorderWidth(0);
            table.addCell(cell, row, col);
            continue;
          }
          if (content == null)
          {
            // A empty cell with a defined background ..
            final RtfCell cell = new RtfCell();
            cell.setBorderWidth(0);
            updateCellStyle(cell, background);
            table.addCell(cell, row, col);
            continue;
          }

          if (content.isCommited() == false)
          {
            throw new InvalidReportStateException("Uncommited content encountered");
          }

          
          final TableRectangle rectangle = sheetLayout.getTableBounds
              (content.getX(), content.getY(), content.getWidth(), content.getHeight(), null);
          if (rectangle.isOrigin(col, row) == false)
          {
            // A spanned cell ..
            continue;
          }

          final TableCellDefinition realBackground;
          if (background == null || (rectangle.getColumnSpan() == 1 && rectangle.getRowSpan() == 1))
          {
            realBackground = background;
          }
          else
          {
            realBackground = sheetLayout.getBackgroundAt
                (rectangle.getX1(), rectangle.getY1(), rectangle.getColumnSpan(), rectangle.getRowSpan());
          }

          final RtfCell cell = new RtfCell();
          cell.setBorderWidth(0);
          if (background != null)
          {
            updateCellStyle(cell, realBackground);
          }
          cell.setRowspan(rectangle.getRowSpan());
          cell.setColspan(rectangle.getColumnSpan());

          // export the cell and all content ..
          final RTFTextExtractor etx = new RTFTextExtractor(metaData);
          etx.compute(content, cell, imageCache, metaData, baseFontSupport);

          table.addCell(cell, row, col);
          content.setFinished(true);
        }

      }

      if (incremental == false)
      {
        document.add(table);
        table = null;
      }
    }
    catch (DocumentException e)
    {
      throw new ContentProcessingException("Failed to generate RTF-Document", e);
    }
  }

  public void close()
  {
    if (document != null)
    {
      // cleanup..
      document.close();
      try
      {
        outputStream.flush();
      }
      catch (IOException e)
      {
        Log.info("Failed to flush the RTF-Output stream.");
      }
      finally
      {
        document = null;
      }
    }
  }

  private int translateBorderStyle(final BorderStyle borderStyle)
  {
    if (BorderStyle.DASHED.equals(borderStyle))
    {
      return RtfBorder.BORDER_DASHED;
    }
    if (BorderStyle.DOT_DASH.equals(borderStyle))
    {
      return RtfBorder.BORDER_DOT_DASH;
    }
    if (BorderStyle.DOT_DOT_DASH.equals(borderStyle))
    {
      return RtfBorder.BORDER_DOT_DOT_DASH;
    }
    if (BorderStyle.DOTTED.equals(borderStyle))
    {
      return RtfBorder.BORDER_DOTTED;
    }
    if (BorderStyle.DOUBLE.equals(borderStyle))
    {
      return RtfBorder.BORDER_DOUBLE;
    }
    if (BorderStyle.HIDDEN.equals(borderStyle))
    {
      return RtfBorder.BORDER_NONE;
    }
    if (BorderStyle.NONE.equals(borderStyle))
    {
      return RtfBorder.BORDER_NONE;
    }
    if (BorderStyle.GROOVE.equals(borderStyle))
    {
      return RtfBorder.BORDER_ENGRAVE;
    }
    if (BorderStyle.RIDGE.equals(borderStyle))
    {
      return RtfBorder.BORDER_EMBOSS;
    }
    if (BorderStyle.INSET.equals(borderStyle))
    {
      return RtfBorder.BORDER_SINGLE;
    }
    if (BorderStyle.OUTSET.equals(borderStyle))
    {
      return RtfBorder.BORDER_SINGLE;
    }
    if (BorderStyle.SOLID.equals(borderStyle))
    {
      return RtfBorder.BORDER_SINGLE;
    }
    if (BorderStyle.WAVE.equals(borderStyle))
    {
      return RtfBorder.BORDER_WAVY;
    }
    return RtfBorder.BORDER_NONE;
  }

  private void updateCellStyle(final RtfCell cell, final TableCellDefinition background)
  {

    final Color backgroundColor = background.getBackgroundColor();
    if (backgroundColor != null)
    {
      cell.setBackgroundColor(backgroundColor);
    }
    final RtfBorderGroup borderGroup = new RtfBorderGroup();
    final BorderEdge top = background.getTop();
    if (BorderEdge.EMPTY.equals(top) == false)
    {
      borderGroup.addBorder(Rectangle.TOP, translateBorderStyle(top.getBorderStyle()),
          (float) StrictGeomUtility.toExternalValue(top.getWidth()), top.getColor());
    }

    final BorderEdge left = background.getLeft();
    if (BorderEdge.EMPTY.equals(left) == false)
    {
      borderGroup.addBorder(Rectangle.LEFT, translateBorderStyle(left.getBorderStyle()),
          (float) StrictGeomUtility.toExternalValue(left.getWidth()), left.getColor());
    }

    final BorderEdge bottom = background.getBottom();
    if (BorderEdge.EMPTY.equals(bottom) == false)
    {
      borderGroup.addBorder(Rectangle.BOTTOM, translateBorderStyle(bottom.getBorderStyle()),
          (float) StrictGeomUtility.toExternalValue(bottom.getWidth()), bottom.getColor());
    }

    final BorderEdge right = background.getRight();
    if (BorderEdge.EMPTY.equals(right) == false)
    {
      borderGroup.addBorder(Rectangle.RIGHT, translateBorderStyle(right.getBorderStyle()),
          (float) StrictGeomUtility.toExternalValue(right.getWidth()), right.getColor());
    }
    cell.setBorders(borderGroup);
  }
}
