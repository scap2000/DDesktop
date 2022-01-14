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
 * DefaultTextExtractor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.base;

import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.CodePointStream;
import org.jfree.fonts.encoding.manual.Utf16LE;
import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.model.SpacerRenderNode;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.process.IterateStructuralProcessStep;
import org.jfree.report.layout.process.RevalidateTextEllipseProcessStep;
import org.jfree.report.layout.text.Glyph;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.util.geom.StrictBounds;

/**
 * Creation-Date: 02.11.2007, 14:14:23
 *
 * @author Thomas Morgner
 */
public class DefaultTextExtractor extends IterateStructuralProcessStep
{
  private StringBuffer text;
  private Object rawResult;
  private RenderNode rawSource;
  private StrictBounds paragraphBounds;
  private boolean textLineOverflow;
  private RevalidateTextEllipseProcessStep revalidateTextEllipseProcessStep;
  private CodePointBuffer codePointBuffer;
  private boolean manualBreak;
  private long contentAreaX1;
  private long contentAreaX2;

  public DefaultTextExtractor(final OutputProcessorMetaData metaData)
  {
    text = new StringBuffer(400);
    paragraphBounds = new StrictBounds();
    revalidateTextEllipseProcessStep = new RevalidateTextEllipseProcessStep(metaData);
  }

  public Object compute(final RenderBox box)
  {
    rawResult = null;
    rawSource = null;
    // initialize it once. It may be overriden later, if there is a real paragraph
    paragraphBounds.setRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
    clearText();
    startProcessing(box);

    // A simple result. So there's no need to create a rich-text string.
    if (rawResult != null)
    {
      return rawResult;
    }
    return text.toString();
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

  protected void processOtherNode(final RenderNode node)
  {
    if (node.isNodeVisible(paragraphBounds) == false)
    {
      return;
    }

    if (node instanceof RenderableText)
    {
      final RenderableText textNode = (RenderableText) node;
      if (isTextLineOverflow())
      {
        final long ellipseSize = extractEllipseSize(node);
        final long x1 = node.getX();
        final long x2 = x1 + node.getWidth();
        final long effectiveAreaX2 = (contentAreaX2 - ellipseSize);
        if (x2 <= effectiveAreaX2)
        {
          // the text will be fully visible.
          drawText(textNode, x2);
        }
        else if (x1 >= contentAreaX2)
        {
          // Skip, the node will not be visible.
        }
        else
        {
          // The text node that is printed will overlap with the ellipse we need to print.
          drawText(textNode, effectiveAreaX2);
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
        drawText(textNode, textNode.getX() + textNode.getWidth());
      }
      if (textNode.isForceLinebreak())
      {
        manualBreak = true;
      }
    }
    else if (node instanceof SpacerRenderNode)
    {
      this.text.append(' ');
    }
  }

  /**
   * Renders the glyphs stored in the text node.
   *
   * @param renderableText the text node that should be rendered.
   * @param contentX2
   */
  protected void drawText(final RenderableText renderableText, final long contentX2)
  {
    if (renderableText.getLength() == 0)
    {
      // This text is empty.
      return;
    }
    if (renderableText.isNodeVisible(paragraphBounds) == false)
    {
      return;
    }

    if (contentX2 >= (renderableText.getX() + renderableText.getWidth()))
    {
      this.text.append(renderableText.getRawText());
    }
    else
    {
      final Glyph[] gs = renderableText.getGlyphs();
      final int maxLength = computeMaximumTextSize(renderableText, contentX2);
      this.text.append(textToString(gs, renderableText.getOffset(), maxLength));
    }
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

  protected boolean startOtherBox(final RenderBox box)
  {
    return false;
  }

  protected boolean isContentField(final RenderBox box)
  {
    final RenderNode firstChild = box.getFirstChild();
    return (firstChild instanceof RenderableReplacedContent && firstChild == box.getLastChild());
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    if (isContentField(box))
    {
      final RenderableReplacedContent rpc = (RenderableReplacedContent) box.getFirstChild();
      this.rawResult = rpc.getRawObject();
      this.rawSource = rpc;
      return false;
    }
    return false;
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (isContentField(box))
    {
      final RenderableReplacedContent rpc = (RenderableReplacedContent) box.getFirstChild();
      this.rawResult = rpc.getRawObject();
      this.rawSource = rpc;
      return false;
    }
    return true;
  }

  public RenderNode getRawSource()
  {
    return rawSource;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    rawResult = box.getRawValue();
    paragraphBounds.setRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
    //final long y2 = box.getY() + box.getHeight();
    contentAreaX1 = box.getContentAreaX1();
    contentAreaX2 = box.getContentAreaX2();

    RenderBox lineBox = (RenderBox) box.getFirstChild();
    while (lineBox != null)
    {
      manualBreak = false;
      processTextLine(lineBox, contentAreaX1, contentAreaX2);
      if (manualBreak)
      {
        addLinebreak();
      }
      else if (lineBox.getStaticBoxLayoutProperties().isPreserveSpace() == false &&
          lineBox.getNext() != null)
      {
        addSoftBreak();
      }
      lineBox = (RenderBox) lineBox.getNext();
    }
  }

  protected void addSoftBreak()
  {
    text.append(" ");
  }

  protected void addLinebreak()
  {
    text.append("\n");
  }


  protected void processTextLine(final RenderBox lineBox,
                                 final long contentAreaX1,
                                 final long contentAreaX2)
  {
    if (lineBox.isNodeVisible(paragraphBounds) == false)
    {
      return;
    }


    this.textLineOverflow =
        ((lineBox.getX() + lineBox.getWidth()) > contentAreaX2) &&
            lineBox.getStyleSheet().getBooleanStyleProperty(ElementStyleKeys.OVERFLOW_X, false) == false;

    if (textLineOverflow)
    {
      revalidateTextEllipseProcessStep.compute(lineBox, contentAreaX1, contentAreaX2);
    }

    startProcessing(lineBox);
  }


  public Object getRawResult()
  {
    return rawResult;
  }

  protected void setRawResult(final Object rawResult)
  {
    this.rawResult = rawResult;
  }

  public String getText()
  {
    return text.toString();
  }

  public int getTextLength()
  {
    return text.length();
  }

  protected void clearText()
  {
    text.delete(0, text.length());
  }


  protected StrictBounds getParagraphBounds()
  {
    return paragraphBounds;
  }


  public boolean isTextLineOverflow()
  {
    return textLineOverflow;
  }
}
