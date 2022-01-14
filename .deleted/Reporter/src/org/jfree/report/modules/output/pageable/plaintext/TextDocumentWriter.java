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
 * TextDocumentWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.plaintext;

import java.awt.print.Paper;

import java.io.IOException;

import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.CodePointStream;
import org.jfree.fonts.encoding.manual.Utf16LE;
import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PageGrid;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.PhysicalPageBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.PhysicalPageKey;
import org.jfree.report.layout.process.IterateStructuralProcessStep;
import org.jfree.report.layout.text.Glyph;
import org.jfree.report.modules.output.pageable.base.RevalidateTextEllipseProcessStep;
import org.jfree.report.modules.output.pageable.plaintext.driver.PlainTextPage;
import org.jfree.report.modules.output.pageable.plaintext.driver.PrinterDriver;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 13.05.2007, 15:49:13
 *
 * @author Thomas Morgner
 */
public class TextDocumentWriter extends IterateStructuralProcessStep
{
  private PrinterDriver driver;
  private String encoding;
  private PlainTextPage plainTextPage;
  private long characterWidthInMicroPoint;
  private long characterHeightInMicroPoint;
  private StrictBounds drawArea;
  private RevalidateTextEllipseProcessStep revalidateTextEllipseProcessStep;
  private long contentAreaX1;
  private long contentAreaX2;
  private boolean textLineOverflow;
  private CodePointBuffer codePointBuffer;

  public TextDocumentWriter(final OutputProcessorMetaData metaData,
                            final PrinterDriver driver,
                            final String encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    this.driver = driver;
    this.encoding = encoding;
    characterHeightInMicroPoint = StrictGeomUtility.toInternalValue(metaData.getNumericFeatureValue(TextOutputProcessorMetaData.CHAR_HEIGHT));
    characterWidthInMicroPoint = StrictGeomUtility.toInternalValue(metaData.getNumericFeatureValue(TextOutputProcessorMetaData.CHAR_WIDTH));

    if (characterHeightInMicroPoint <= 0 || characterWidthInMicroPoint <= 0)
    {
      throw new IllegalStateException("Invalid character box size. Cannot continue.");
    }
    revalidateTextEllipseProcessStep = new RevalidateTextEllipseProcessStep(metaData);
  }


  public void close()
  {
  }

  public void open()
  {

  }

  public void processPhysicalPage(final PageGrid pageGrid,
                                  final LogicalPageBox logicalPage,
                                  final int row,
                                  final int col,
                                  final PhysicalPageKey pageKey) throws IOException
  {
    final PhysicalPageBox page = pageGrid.getPage(row, col);
    final Paper paper = new Paper();
    paper.setSize(StrictGeomUtility.toExternalValue(page.getWidth()),
        StrictGeomUtility.toExternalValue(page.getHeight()));
    paper.setImageableArea
        (StrictGeomUtility.toExternalValue(page.getImageableX()),
            StrictGeomUtility.toExternalValue(page.getImageableY()),
            StrictGeomUtility.toExternalValue(page.getImageableWidth()),
            StrictGeomUtility.toExternalValue(page.getImageableHeight()));
    drawArea = new StrictBounds(page.getGlobalX(), page.getGlobalY(),
        page.getWidth(), page.getHeight());
    plainTextPage = new PlainTextPage(paper, driver, encoding);
    startProcessing(logicalPage);
    plainTextPage.writePage();
  }

  public void processLogicalPage(final LogicalPageKey key, final LogicalPageBox logicalPage) throws IOException
  {
    final Paper paper = new Paper();
    paper.setSize(StrictGeomUtility.toExternalValue(logicalPage.getPageWidth()),
        StrictGeomUtility.toExternalValue(logicalPage.getPageHeight()));
    paper.setImageableArea(0, 0,
        StrictGeomUtility.toExternalValue(logicalPage.getPageWidth()),
        StrictGeomUtility.toExternalValue(logicalPage.getPageHeight()));
    paper.setSize(logicalPage.getPageWidth(), logicalPage.getPageHeight());
    paper.setImageableArea(0, 0, logicalPage.getPageWidth(), logicalPage.getPageHeight());

    drawArea = new StrictBounds(0, 0, logicalPage.getWidth(), logicalPage.getHeight());
    plainTextPage = new PlainTextPage(paper, driver, encoding);
    startProcessing(logicalPage);
    plainTextPage.writePage();
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (box.isBoxVisible(drawArea) == false)
    {
      return false;
    }
    return true;
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    if (box.isBoxVisible(drawArea) == false)
    {
      return false;
    }
    return true;
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    if (box.isBoxVisible(drawArea) == false)
    {
      return false;
    }
    return true;
  }

  protected void drawText(final RenderableText renderableText)
  {
    drawText(renderableText, renderableText.getX() + renderableText.getWidth());
  }

  protected void drawText(final RenderableText text, final long contentX2)
  {
    if (text.isNodeVisible(drawArea) == false)
    {
      return;
    }
    if (text.getLength() == 0)
    {
      // This text is empty.
      return;
    }

    final Glyph[] gs = text.getGlyphs();
    final int maxLength = computeMaximumTextSize(text, contentX2);
    final String rawText = textToString(gs, text.getOffset(), maxLength);

    final int x = PlainTextPage.correctedDivisionFloor
        ((float) (text.getX() - drawArea.getX()), characterWidthInMicroPoint);
    final int y = PlainTextPage.correctedDivisionFloor
        ((float) (text.getY() - drawArea.getY()), characterHeightInMicroPoint);
    int w = Math.min (maxLength,
        PlainTextPage.correctedDivisionFloor((float) text.getWidth(), characterWidthInMicroPoint));

    // filter out results that do not belong to the current physical page
    if (x + w > plainTextPage.getWidth())
    {
      w = Math.max(0, plainTextPage.getWidth() - x);
    }
    if (w == 0)
    {
      return;
    }
    if (y < 0)
    {
      return;
    }
    if (y >= plainTextPage.getHeight())
    {
      return;
    }
    plainTextPage.addTextChunk(x, y, w, rawText, text.getStyleSheet());
  }

  protected int computeMaximumTextSize(final RenderableText node, final long contentX2)
  {
    final int length = node.getLength();
    final long x = node.getX();
    if (contentX2 >= (x + node.getWidth()))
    {
      return length;
    }

    final Glyph[] gs = node.getGlyphs();
    long runningPos = x;
    final int offset = node.getOffset();
    final int maxPos = offset + length;

    for (int i = offset; i < maxPos; i++)
    {
      final Glyph g = gs[i];
      runningPos += g.getWidth();
      if (runningPos > contentX2)
      {
        return Math.max(0, i - offset);
      }
    }
    return length;
  }

  protected String textToString(final Glyph[] gs, final int offset, final int length)
  {
    if (length == 0)
    {
      return "";
    }

    if (codePointBuffer == null)
    {
      codePointBuffer = new CodePointBuffer(length);
    }
    else
    {
      codePointBuffer.setCursor(0);
    }
    //noinspection SynchronizeOnNonFinalField
    synchronized (codePointBuffer)
    {
      final CodePointStream cps = new CodePointStream(codePointBuffer, length);
      final int maxPos = offset + length;
      for (int i = offset; i < maxPos; i++)
      {
        final Glyph g = gs[i];
        cps.put(g.getCodepoint());
        final int[] extraChars = g.getExtraChars();
        if (extraChars.length > 0)
        {
          cps.put(extraChars);
        }
      }
      cps.close();
      return Utf16LE.getInstance().encodeString(codePointBuffer);
    }
  }

  protected void processOtherNode(final RenderNode node)
  {
    if (node instanceof RenderableText)
    {
      if (isTextLineOverflow())
      {
        final long ellipseSize = extractEllipseSize(node);
        final long x1 = node.getX();
        final long x2 = x1 + node.getWidth();
        final long effectiveAreaX2 = (contentAreaX2 - ellipseSize);
        if (x2 <= effectiveAreaX2)
        {
          // the text will be fully visible.
          drawText((RenderableText) node);
        }
        else if (x1 >= contentAreaX2)
        {
          // Skip, the node will not be visible.
        }
        else
        {
          // The text node that is printed will overlap with the ellipse we need to print.
          drawText((RenderableText) node, effectiveAreaX2);
          final RenderBox parent = node.getParent();
          if (parent != null)
          {
            final RenderBox textEllipseBox = parent.getTextEllipseBox();
            if (textEllipseBox != null)
            {
              processBoxChilds(textEllipseBox);
            }
          }
        }

      }
      else
      {
        drawText((RenderableText) node);
      }
    }
  }

  private long extractEllipseSize(final RenderNode node)
  {
    if (node == null)
    {
      return 0;
    }
    final RenderBox parent = node.getParent();
    if (parent == null)
    {
      return 0;
    }
    final RenderBox textEllipseBox = parent.getTextEllipseBox();
    if (textEllipseBox == null)
    {
      return 0;
    }
    return textEllipseBox.getWidth();
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    this.contentAreaX1 = box.getContentAreaX1();
    this.contentAreaX2 = box.getContentAreaX2();

    RenderBox lineBox = (RenderBox) box.getFirstChild();
    while (lineBox != null)
    {
      processTextLine(lineBox, contentAreaX1, contentAreaX2);
      lineBox = (RenderBox) lineBox.getNext();
    }
  }

  protected void processTextLine(final RenderBox lineBox,
                                 final long contentAreaX1,
                                 final long contentAreaX2)
  {
    this.textLineOverflow =
        ((lineBox.getX() + lineBox.getWidth()) > contentAreaX2) &&
            lineBox.getStyleSheet().getBooleanStyleProperty(ElementStyleKeys.OVERFLOW_X, false) == false;

    if (textLineOverflow)
    {
      revalidateTextEllipseProcessStep.compute(lineBox, contentAreaX1, contentAreaX2);
    }

    startProcessing(lineBox);
  }

  public long getContentAreaX2()
  {
    return contentAreaX2;
  }

  public void setContentAreaX2(final long contentAreaX2)
  {
    this.contentAreaX2 = contentAreaX2;
  }

  public long getContentAreaX1()
  {
    return contentAreaX1;
  }

  public void setContentAreaX1(final long contentAreaX1)
  {
    this.contentAreaX1 = contentAreaX1;
  }

  public boolean isTextLineOverflow()
  {
    return textLineOverflow;
  }

  public void setTextLineOverflow(final boolean textLineOverflow)
  {
    this.textLineOverflow = textLineOverflow;
  }
}
