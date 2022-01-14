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
 * ExcelPrinter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls.helper;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;

import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.jfree.io.IOUtils;
import org.jfree.report.Anchor;
import org.jfree.report.ElementAlignment;
import org.jfree.report.ImageContainer;
import org.jfree.report.InvalidReportStateException;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.URLImageContainer;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PhysicalPageBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.RenderUtility;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.TableCellDefinition;
import org.jfree.report.modules.output.table.base.TableContentProducer;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.modules.output.table.xls.ExcelTableModule;
import org.jfree.report.resourceloader.ImageFactory;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.ImageUtils;
import org.jfree.report.util.IntegerCache;
import org.jfree.report.util.MemoryByteArrayOutputStream;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.StringUtils;
import org.jfree.util.WaitingImageObserver;

/**
 * Creation-Date: 09.05.2007, 14:52:05
 *
 * @author Thomas Morgner
 */
public class ExcelPrinter
{
  private InputStream templateInputStream;
  private OutputStream outputStream;
  private HSSFWorkbook workbook;
  private HashMap sheetNamesCount;
  private double scaleFactor;
  private Configuration config;
  private OutputProcessorMetaData metaData;
  private HSSFSheet sheet;
  private HSSFPatriarch patriarch;
  private HSSFCellStyleProducer cellStyleProducer;

  public ExcelPrinter()
  {
    this.sheetNamesCount = new HashMap();
  }

  public void init(final Configuration config,
                   final OutputProcessorMetaData metaData,
                   final OutputStream outputStream)
  {
    this.outputStream = outputStream;
    this.config = config;
    this.metaData = metaData;
    try
    {
      final String scaleFactorText = config.getConfigProperty
          ("org.jfree.report.modules.output.table.xls.CellWidthScaleFactor");
      if (scaleFactorText == null)
      {
        scaleFactor = 50;
      }
      else
      {
        scaleFactor = Double.parseDouble(scaleFactorText);
      }
    }
    catch (Exception e)
    {
      this.scaleFactor = 50;
    }
  }

  public InputStream getTemplateInputStream()
  {
    return templateInputStream;
  }

  public void setTemplateInputStream(final InputStream templateInputStream)
  {
    this.templateInputStream = templateInputStream;
  }

  private String makeUnique(final String name)
  {
    final Integer count = (Integer) sheetNamesCount.get(name);
    if (count == null)
    {
      sheetNamesCount.put(name, IntegerCache.getInteger(1));
      return name;
    }
    else
    {
      final int value = count.intValue() + 1;
      sheetNamesCount.put(name, IntegerCache.getInteger(value));
      return makeUnique(name + ' ' + value);
    }

  }

  private boolean isValidSheetName(final String sheetname)
  {
    if ((sheetname.indexOf('/') > -1)
        || (sheetname.indexOf('\\') > -1)
        || (sheetname.indexOf('?') > -1)
        || (sheetname.indexOf('*') > -1)
        || (sheetname.indexOf(']') > -1)
        || (sheetname.indexOf('[') > -1)
        || (sheetname.indexOf(':') > -1))
    {
      return false;
    }

    return true;
  }

  private HSSFCell getCellAt(final short x, final int y)
  {
    final HSSFRow row = getRowAt(y);
    final HSSFCell cell = row.getCell(x);
    if (cell != null)
    {
      return cell;
    }
    return row.createCell(x);
  }

  private HSSFRow getRowAt(final int y)
  {
    final HSSFRow row = sheet.getRow(y);
    if (row != null)
    {
      return row;
    }
    return sheet.createRow(y);
  }

  public void print(final LogicalPageKey logicalPageKey,
                    final LogicalPageBox logicalPage,
                    final TableContentProducer contentProducer,
                    final boolean incremental)
  {
    if (workbook == null)
    {
      workbook = createWorkbook();

      final boolean hardLimit = "true".equals
          (config.getConfigProperty("org.jfree.report.modules.output.table.xls.HardStyleCountLimit"));
      cellStyleProducer = new HSSFCellStyleProducer(workbook, hardLimit);
    }

    if (sheet == null)
    {
      sheet = openSheet(contentProducer.getSheetName());
      // Start a new page.
      final PhysicalPageBox page = logicalPage.getPageGrid().getPage(0, 0);
      configureSheet(page);

      // Set column widths ..
      final SheetLayout sheetLayout = contentProducer.getSheetLayout();
      final int columnCount = contentProducer.getColumnCount();
      for (short col = 0; col < columnCount; col++)
      {
        final double cellWidth = StrictGeomUtility.toExternalValue(sheetLayout.getCellWidth(col, col + 1));
        final double poiCellWidth = (cellWidth * scaleFactor);
        sheet.setColumnWidth(col, (short) poiCellWidth);
      }

      // ... and row heights ..
      final int rowCount = contentProducer.getRowCount();
      for (int row = 0; row < rowCount; row += 1)
      {
        final HSSFRow hssfRow = getRowAt(row);
        final double lastRowHeight = StrictGeomUtility.toExternalValue(sheetLayout.getRowHeight(row));
        hssfRow.setHeightInPoints((float) (lastRowHeight));
      }
    }

    // and finally the content ..
    final SheetLayout sheetLayout = contentProducer.getSheetLayout();
    final int colCount = sheetLayout.getColumnCount();
    final int startRow = contentProducer.getFinishedRows();
    final int finishRow = contentProducer.getFilledRows();
    //Log.debug ("Excel: Processing: " + startRow + " " + finishRow + " " + incremental);

    for (int row = startRow; row < finishRow; row++)
    {
      for (short col = 0; col < colCount; col++)
      {
        final RenderBox content = contentProducer.getContent(row, col);
        final TableCellDefinition background = sheetLayout.getBackgroundAt(row, col);

        if (content == null && background == null)
        {
          if (row == 0 && col == 0)
          {
            // create a single cell, so that we dont run into nullpointer inside POI..
            getCellAt(col, row);
          }
          // An empty cell .. ignore
          continue;
        }
        if (content == null)
        {
          // A empty cell with a defined background ..
          final HSSFCell cell = getCellAt(col, row);
          final HSSFCellStyle style = cellStyleProducer.createCellStyle(null, background);
          if (style != null)
          {
            cell.setCellStyle(style);
          }
          continue;
        }

        if (content.isCommited() == false)
        {
          throw new InvalidReportStateException("Uncommited content encountered");
        }

        final long contentOffset = contentProducer.getContentOffset(row, col);
        final TableRectangle rectangle = sheetLayout.getTableBounds
            (content.getX(), content.getY() + contentOffset,
                content.getWidth(), content.getHeight(), null);
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
        // export the cell and all content ..

        final HSSFCell cell = getCellAt(col, row);
        final HSSFCellStyle style = cellStyleProducer.createCellStyle(content, realBackground);
        if (style != null)
        {
          cell.setCellStyle(style);
        }

        if (applyCellValue(metaData, content, cell, sheetLayout, rectangle, contentOffset))
        {
          mergeCellRegion(rectangle, row, col, sheetLayout, content);
        }

        content.setFinished(true);
      }

    }

    if (incremental == false)
    {
      // cleanup ..
      patriarch = null;
      sheet = null;
    }
  }

  private void mergeCellRegion(final TableRectangle rectangle,
                               final int row,
                               final short col,
                               final SheetLayout sheetLayout,
                               final RenderBox content)
  {
    final int rowSpan = rectangle.getRowSpan();
    final int columnSpan = rectangle.getColumnSpan();
    if (rowSpan > 1 || columnSpan > 1)
    {
      sheet.addMergedRegion(new Region(row, col, (row + rowSpan - 1), (short) (col + columnSpan - 1)));
      final int rectX = rectangle.getX1();
      final int rectY = rectangle.getY1();

      for (int spannedRow = 0; spannedRow < rowSpan; spannedRow += 1)
      {
        for (int spannedCol = 0; spannedCol < columnSpan; spannedCol += 1)
        {
          final TableCellDefinition bg = sheetLayout.getBackgroundAt(rectY + spannedRow, rectX + spannedCol);
          final HSSFCell regionCell = getCellAt((short) (col + spannedCol), row + spannedRow);
          final HSSFCellStyle spannedStyle = cellStyleProducer.createCellStyle(content, bg);
          if (spannedStyle != null)
          {
            regionCell.setCellStyle(spannedStyle);
          }
        }
      }
    }
  }

  /**
   * Applies the cell value and determines whether the cell should be merged. Merging will only take place if the cell
   * has a row or colspan greater than one. Images will never be merged, as image content is rendered into an anchored
   * frame on top of the cells.
   *
   * @param content
   * @param cell
   * @param sheetLayout
   * @param rectangle
   * @return true, if the cell may to be put into a merged region, false otherwise.
   */
  private boolean applyCellValue(final OutputProcessorMetaData metaData,
                                 final RenderBox content,
                                 final HSSFCell cell,
                                 final SheetLayout sheetLayout,
                                 final TableRectangle rectangle,
                                 final long contentOffset)
  {
    final ExcelTextExtractor etx = new ExcelTextExtractor(metaData);
    final Object value = etx.compute(content, cellStyleProducer.getFontFactory());

    if (value instanceof ImageContainer)
    {
      final ImageContainer imageContainer = (ImageContainer) value;
      // todo: this is wrong ..
      final RenderNode rawSource = etx.getRawSource();
      final StrictBounds contentBounds =
          new StrictBounds(content.getX(), content.getY() + contentOffset, content.getWidth(), content.getHeight());
      createImageCell(rawSource, imageContainer, sheetLayout, rectangle, contentBounds);
      return false;
    }
    else if (value instanceof Drawable)
    {
      final Drawable drawable = (Drawable) value;
        final RenderNode rawSource = etx.getRawSource();
      final StrictBounds contentBounds = new StrictBounds
          (rawSource.getX(), rawSource.getY() + contentOffset, rawSource.getWidth(), rawSource.getHeight());
      final ImageContainer imageFromDrawable =
          RenderUtility.createImageFromDrawable(drawable, contentBounds, content, metaData);
      createImageCell(rawSource, imageFromDrawable, sheetLayout, rectangle, contentBounds);
      return false;
    }
    else if (value instanceof Shape)
    {
      // We *could* do this as well ... but for now we dont.
      return false;
    }


    final String linkTarget = (String) content.getStyleSheet().getStyleProperty(ElementStyleKeys.HREF_TARGET);
    if (linkTarget != null)
    {
      // this may be wrong if we have quotes inside. We should escape them ..
      cell.setCellFormula("HYPERLINK(\"" + linkTarget + "\",\"" + etx.getText() + "\")");
    }
    else if (value instanceof HSSFRichTextString)
    {
      cell.setCellValue((HSSFRichTextString) value);
    }
    else if (value instanceof Date)
    {
      cell.setCellValue((Date) value);
    }
    else if (value instanceof Number)
    {
      final Number number = (Number) value;
      cell.setCellValue(number.doubleValue());
    }
    else if (value instanceof Boolean)
    {
      cell.setCellValue(Boolean.TRUE.equals(value));
    }
    else if (value instanceof Anchor)
    {
      // Anchors are not printable and therefore ignored.
    }
    else // Something we can't handle.
    {
      if (value == null)
      {
        cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
      }
      else
      {
        cell.setCellValue(new HSSFRichTextString(String.valueOf(value)));
      }
    }
    return true;
  }

  private void configureSheet(final PhysicalPageBox page)
  {
    // make sure a new patriarch is created if needed.
    patriarch = null;

    final String paper = config.getConfigProperty(ExcelTableModule.CONFIGURATION_PREFIX + ".Paper");
    final String orientation = config.getConfigProperty(ExcelTableModule.CONFIGURATION_PREFIX + ".PaperOrientation");

    final HSSFPrintSetup printSetup = sheet.getPrintSetup();
    ExcelPrintSetupFactory.performPageSetup(printSetup, page, paper, orientation);

    final boolean displayGridLines = "true".equals(config.getConfigProperty
        (ExcelTableModule.CONFIGURATION_PREFIX + ".GridLinesDisplayed"));
    final boolean printGridLines = "true".equals(config.getConfigProperty
        (ExcelTableModule.CONFIGURATION_PREFIX + ".GridLinesPrinted"));
    sheet.setDisplayGridlines(displayGridLines);
    sheet.setPrintGridlines(printGridLines);
  }

  public void close()
  {
    if (workbook != null)
    {
      try
      {
        workbook.write(outputStream);
        // cleanup..
        patriarch = null;
        sheet = null;
        outputStream.flush();
      }
      catch (IOException e)
      {
        Log.warn("could not write xls data. Message:", e);
      }
      finally
      {
        workbook = null;
      }
    }

  }

  private HSSFWorkbook createWorkbook()
  {
    // Not opened yet. Lets do this now.
    if (templateInputStream != null)
    {
      // do some preprocessing ..
      try
      {
        final POIFSFileSystem fs = new POIFSFileSystem(templateInputStream);
        final HSSFWorkbook workbook = new HSSFWorkbook(fs);

        // OK, we have a workbook, but we can't stop here..
        final int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++)
        {
          final String sheetName = workbook.getSheetName(i);
          // make sure that that name is marked as used ..
          makeUnique(sheetName);
        }

        // todo: Read in the existing styles, maybe we can reuse some of them ..
        return workbook;
      }
      catch (IOException e)
      {
        Log.warn("Unable to read predefined xls-data.", e);
      }
    }
    return new HSSFWorkbook();
  }

  private HSSFSheet openSheet(final String sheetName)
  {
    if (sheetName == null)
    {
      return workbook.createSheet();
    }
    else
    {
      final String uniqueSheetname = makeUnique(sheetName);
      if (uniqueSheetname.length() == 0 || uniqueSheetname.length() > 31)
      {
        Log.warn("A sheet name must not be empty and greater than 31 characters");
        return workbook.createSheet();
      }
      else if (isValidSheetName(uniqueSheetname) == false)
      {
        Log.warn("A sheet name must not contain any of ':/\\*?[]'");
        // OpenOffice is even more restrictive and only allows Letters,
        // Digits, Spaces and the Underscore
        return workbook.createSheet();
      }
      else
      {
        return workbook.createSheet(uniqueSheetname);
      }
    }
  }


  /**
   * @noinspection SuspiciousNameCombination
   */
  private void createImageCell(final RenderNode contentNode,
                               final ImageContainer image,
                               final SheetLayout currentLayout,
                               TableRectangle rectangle,
                               final StrictBounds cellBounds)
  {
    try
    {
      if (rectangle == null)
      {
        // there was an error while computing the grid-position for this
        // element. Evil me...
        Log.debug("Invalid reference: I was not able to compute " +
            "the rectangle for the content.");
        return;
      }

      final StyleSheet layoutContext = contentNode.getStyleSheet();
      final boolean shouldScale = layoutContext.getBooleanStyleProperty(ElementStyleKeys.SCALE);

      final int imageWidth = image.getImageWidth();
      final int imageHeight = image.getImageHeight();
      if (imageWidth < 1 || imageHeight < 1)
      {
        return;
      }


      final ElementAlignment horizontalAlignment =
          (ElementAlignment) layoutContext.getStyleProperty(ElementStyleKeys.ALIGNMENT);
      final ElementAlignment verticalAlignment =
          (ElementAlignment) layoutContext.getStyleProperty(ElementStyleKeys.VALIGNMENT);
      final long internalImageWidth = StrictGeomUtility.toInternalValue(imageWidth);
      final long internalImageHeight = StrictGeomUtility.toInternalValue(imageHeight);
      final long cellWidth = cellBounds.getWidth();
      final long cellHeight = cellBounds.getHeight();

      final StrictBounds cb;
      final int pictureId;
      if (shouldScale)
      {
        final double scaleX;
        final double scaleY;

        final boolean keepAspectRatio = layoutContext.getBooleanStyleProperty(ElementStyleKeys.KEEP_ASPECT_RATIO);
        if (keepAspectRatio)
        {
          scaleX = Math.min(cellWidth / (double) internalImageWidth, cellHeight / (double) internalImageHeight);
          scaleY = scaleX;
        }
        else
        {
          scaleX = cellWidth / (double) internalImageWidth;
          scaleY = cellHeight / (double) internalImageHeight;
        }

        final long clipWidth = (long) (scaleX * internalImageWidth);
        final long clipHeight = (long) (scaleY * internalImageHeight);

        final long alignmentX = RenderUtility.computeHorizontalAlignment(horizontalAlignment, cellWidth, clipWidth);
        final long alignmentY = RenderUtility.computeVerticalAlignment(verticalAlignment, cellHeight, clipHeight);

        cb = new StrictBounds(cellBounds.getX() + alignmentX,
            cellBounds.getY() + alignmentY,
            Math.min(clipWidth, cellWidth),
            Math.min(clipHeight, cellHeight));

        // Recompute the cells that this image will cover (now that it has been resized)
        rectangle = currentLayout.getTableBounds(cb, rectangle);

        pictureId = loadImage(workbook, image);
        if (pictureId <= 0)
        {
          return;
        }
      }
      else
      {
        // unscaled ..
        if (internalImageWidth <= cellWidth && internalImageHeight <= cellHeight)
        {
          // No clipping needed.
          final long alignmentX = RenderUtility.computeHorizontalAlignment
              (horizontalAlignment, cellBounds.getWidth(), internalImageWidth);
          final long alignmentY = RenderUtility.computeVerticalAlignment
              (verticalAlignment, cellBounds.getHeight(), internalImageHeight);

          cb = new StrictBounds(cellBounds.getX() + alignmentX,
              cellBounds.getY() + alignmentY,
              internalImageWidth,
              internalImageHeight);

          // Recompute the cells that this image will cover (now that it has been resized)
          rectangle = currentLayout.getTableBounds(cb, rectangle);


          pictureId = loadImage(workbook, image);
          if (pictureId <= 0)
          {
            return;
          }
        }
        else
        {
          // at least somewhere there is clipping needed.
          final long clipWidth = Math.min(cellWidth, internalImageWidth);
          final long clipHeight = Math.min(cellHeight, internalImageHeight);
          final long alignmentX = RenderUtility.computeHorizontalAlignment
              (horizontalAlignment, cellBounds.getWidth(), clipWidth);
          final long alignmentY = RenderUtility.computeVerticalAlignment
              (verticalAlignment, cellBounds.getHeight(), clipHeight);
          cb = new StrictBounds(cellBounds.getX() + alignmentX,
              cellBounds.getY() + alignmentY,
              clipWidth,
              clipHeight);

          // Recompute the cells that this image will cover (now that it has been resized)
          rectangle = currentLayout.getTableBounds(cb, rectangle);


          pictureId = loadImageWithClipping(workbook, image, clipWidth, clipHeight);
          if (pictureId <= 0)
          {
            return;
          }
        }
      }


      final int cell1x = rectangle.getX1();
      final int cell1y = rectangle.getY1();
      final int cell2x = Math.max(cell1x, rectangle.getX2() - 1);
      final int cell2y = Math.max(cell1y, rectangle.getY2() - 1);

      final long cell1width = currentLayout.getCellWidth(cell1x);
      final long cell1height = currentLayout.getRowHeight(cell1y);
      final long cell2width = currentLayout.getCellWidth(cell2x);
      final long cell2height = currentLayout.getRowHeight(cell2y);

      final long cell1xPos = currentLayout.getXPosition(cell1x);
      final long cell1yPos = currentLayout.getYPosition(cell1y);
      final long cell2xPos = currentLayout.getXPosition(cell2x);
      final long cell2yPos = currentLayout.getYPosition(cell2y);

      final int dx1 = (int) (1023 * ((cb.getX() - cell1xPos) / (double) cell1width));
      final int dy1 = (int) (255 * ((cb.getY() - cell1yPos) / (double) cell1height));
      final int dx2 = (int) (1023 * ((cb.getX() + cb.getWidth() - cell2xPos) / (double) cell2width));
      final int dy2 = (int) (255 * ((cb.getY() + cb.getHeight() - cell2yPos) / (double) cell2height));

      final HSSFClientAnchor anchor = new HSSFClientAnchor(dx1, dy1, dx2, dy2, (short) cell1x, cell1y, (short) cell2x, cell2y);
      anchor.setAnchorType(2); // Move, but don't size
      if (patriarch == null)
      {
        patriarch = sheet.createDrawingPatriarch();
      }
      patriarch.createPicture(anchor, pictureId);
    }
    catch (IOException e)
    {
      Log.warn("Failed to add image. Ignoring.");
    }
  }

  private int getImageFormat(final URL sourceURL)
  {
    final String file = sourceURL.getFile();
    if (StringUtils.endsWithIgnoreCase(file, ".png"))
    {
      return HSSFWorkbook.PICTURE_TYPE_PNG;
    }
    if (StringUtils.endsWithIgnoreCase(file, ".jpg") ||
        StringUtils.endsWithIgnoreCase(file, ".jpeg"))
    {
      return HSSFWorkbook.PICTURE_TYPE_JPEG;
    }
    if (StringUtils.endsWithIgnoreCase(file, ".bmp") ||
        StringUtils.endsWithIgnoreCase(file, ".ico"))
    {
      return HSSFWorkbook.PICTURE_TYPE_DIB;
    }
    return -1;
  }

  private int loadImageWithClipping(final HSSFWorkbook workbook,
                                    final ImageContainer reference,
                                    final long clipWidth,
                                    final long clipHeight)
      throws IOException
  {

    Image image = null;
    // The image has an assigned URL ...
    if (reference instanceof URLImageContainer)
    {
      final URLImageContainer urlImage = (URLImageContainer) reference;
      final URL url = urlImage.getSourceURL();
      // if we have an source to load the image data from ..
      if (url != null && urlImage.isLoadable())
      {
        if (reference instanceof LocalImageContainer)
        {
          final LocalImageContainer li = (LocalImageContainer) reference;
          image = li.getImage();
        }
        if (image == null)
        {
          image = ImageFactory.getInstance().createImage(url);
        }
      }
    }

    if (reference instanceof LocalImageContainer)
    {
      // Check, whether the imagereference contains an AWT image.
      // if so, then we can use that image instance for the recoding
      final LocalImageContainer li = (LocalImageContainer) reference;
      if (image == null)
      {
        image = li.getImage();
      }
    }

    if (image != null)
    {
      // now encode the image. We don't need to create digest data
      // for the image contents, as the image is perfectly identifyable
      // by its URL
      return clipAndEncodeImage(workbook, image, clipWidth, clipHeight);
    }
    return -1;
  }

  private int clipAndEncodeImage(final HSSFWorkbook workbook,
                                 final Image image,
                                 final long width,
                                 final long height)
  {
    final int imageWidth = (int) StrictGeomUtility.toExternalValue(width);
    final int imageHeight = (int) StrictGeomUtility.toExternalValue(height);
    // first clip.
    final BufferedImage bi = ImageUtils.createTransparentImage(imageWidth, imageHeight);
    final Graphics2D graphics = (Graphics2D) bi.getGraphics();


    if (image instanceof BufferedImage)
    {
      final int imgW = image.getWidth(null);
      final int imgH = image.getHeight(null);
      graphics.translate((width - imgW) >> 1, (height - imgH) >> 1);
      if (graphics.drawImage(image, null, null) == false)
      {
        Log.debug("Failed to render the image. This should not happen for BufferedImages");
      }
    }
    else
    {
      final WaitingImageObserver obs = new WaitingImageObserver(image);
      obs.waitImageLoaded();
      final int imgW = image.getWidth(obs);
      final int imgH = image.getHeight(obs);
      graphics.translate((width - imgW) >> 1, (height - imgH) >> 1);

      while (graphics.drawImage(image, null, obs) == false)
      {
        obs.waitImageLoaded();
        if (obs.isError())
        {
          Log.warn("Error while loading the image during the rendering.");
          break;
        }
      }
    }

    graphics.dispose();
    final byte[] data = RenderUtility.encodeImage(image);
    return workbook.addPicture(data, HSSFWorkbook.PICTURE_TYPE_PNG);

  }


  private int loadImage(final HSSFWorkbook workbook, final ImageContainer reference)
      throws IOException
  {
    Image image = null;
    // The image has an assigned URL ...
    if (reference instanceof URLImageContainer)
    {
      final URLImageContainer urlImage = (URLImageContainer) reference;
      final URL url = urlImage.getSourceURL();
      // if we have an source to load the image data from ..
      if (url != null && urlImage.isLoadable())
      {
        // and the image is one of the supported image formats ...
        // we we can embedd it directly ...
        final int format = getImageFormat(urlImage.getSourceURL());
        if (format == -1)
        {
          // This is a unsupported image format.
          if (reference instanceof LocalImageContainer)
          {
            final LocalImageContainer li = (LocalImageContainer) reference;
            image = li.getImage();
          }
          if (image == null)
          {
            image = ImageFactory.getInstance().createImage(url);
          }
        }
        else
        {
          final MemoryByteArrayOutputStream bout = new MemoryByteArrayOutputStream();
          final InputStream urlIn = new BufferedInputStream(urlImage.getSourceURL().openStream());
          try
          {
            IOUtils.getInstance().copyStreams(urlIn, bout);
          }
          finally
          {
            bout.close();
            urlIn.close();
          }

          final byte[] data = bout.toByteArray();
          // create the image
          return workbook.addPicture(data, format);
        }
      }
    }

    if (reference instanceof LocalImageContainer)
    {
      // Check, whether the imagereference contains an AWT image.
      // if so, then we can use that image instance for the recoding
      final LocalImageContainer li = (LocalImageContainer) reference;
      if (image == null)
      {
        image = li.getImage();
      }
    }

    if (image != null)
    {
      // now encode the image. We don't need to create digest data
      // for the image contents, as the image is perfectly identifyable
      // by its URL
      final byte[] data = RenderUtility.encodeImage(image);
      return workbook.addPicture(data, HSSFWorkbook.PICTURE_TYPE_PNG);
    }
    return -1;
  }

}
