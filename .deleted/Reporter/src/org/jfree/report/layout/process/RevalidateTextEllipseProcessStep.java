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
 * RevalidateTextEllipseProcessStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.manual.Utf16LE;
import org.jfree.report.layout.TextCache;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.text.DefaultRenderableTextFactory;
import org.jfree.report.layout.text.RenderableTextFactory;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;

/**
 * Creation-Date: 31.07.2007, 19:09:52
 *
 * @author Thomas Morgner
 */
public class RevalidateTextEllipseProcessStep extends IterateStructuralProcessStep
{
  private long contentAreaX1;
  private long contentAreaX2;
  private TextCache textCache;
  private RenderableTextFactory textFactory;
  private CodePointBuffer buffer;

  public RevalidateTextEllipseProcessStep(final OutputProcessorMetaData metaData)
  {
    this.textCache = new TextCache();
    this.textFactory = new DefaultRenderableTextFactory(metaData);
  }

  public void compute (final RenderBox box,
                       final long contentAreaX1,
                       final long contentAreaX2)
  {
    this.contentAreaX1 = contentAreaX1;
    this.contentAreaX2 = contentAreaX2;
    startProcessing(box);
  }

  public long getContentAreaX1()
  {
    return contentAreaX1;
  }

  public long getContentAreaX2()
  {
    return contentAreaX2;
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    // compute the box's text-ellipse if necessary.
    if (box.getTextEllipseBox() != null)
    {
      return false;
    }

    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    final BoxDefinition bdef = box.getBoxDefinition();
    final long boxContentX2 = (box.getX() + box.getWidth() - bdef.getPaddingRight() - sblp.getBorderRight());
    if (boxContentX2 > getContentAreaX2())
    {
      // This is an overflow. Compute the text-ellipse ..
      final RenderBox textEllipseBox = processTextEllipse(box, getContentAreaX2());
      box.setTextEllipseBox(textEllipseBox);
    }
    return true;
  }

  private RenderBox processTextEllipse (final RenderBox box, final long x2)
  {
    final StyleSheet style = box.getStyleSheet();
    final String reslit = (String) style.getStyleProperty(TextStyleKeys.RESERVED_LITERAL);
    if (reslit == null || "".equals(reslit))
    {
      // oh, no ellipse. Thats nice.
      return null;
    }

    final RenderBox textEllipse = (RenderBox) box.derive(false);
    final TextCache.Result result = textCache.get(style.getId(), style.getChangeTracker(), reslit);
    if (result != null)
    {
      textEllipse.addGeneratedChilds(result.getText());
      textEllipse.addGeneratedChilds(result.getFinish());
      performTextEllipseLayout(textEllipse, x2);
      return textEllipse;
    }
    if (buffer != null)
    {
      buffer.setCursor(0);
    }

    buffer = Utf16LE.getInstance().decodeString(reslit, buffer);
    final int[] data = buffer.getBuffer();

    textFactory.startText();
    final RenderNode[] renderNodes = textFactory.createText(data, 0, buffer.getLength(), style);
    final RenderNode[] finishNodes = textFactory.finishText();

    textEllipse.addGeneratedChilds(renderNodes);
    textEllipse.addGeneratedChilds(finishNodes);
    textCache.store(style.getId(), style.getChangeTracker(), reslit, style, renderNodes, finishNodes);
    performTextEllipseLayout(textEllipse, x2);
    return textEllipse;
  }

  private void performTextEllipseLayout (final RenderBox box, final long x2)
  {
    // we do assume that the text-ellipse box is a simple box with no sub-boxes.
    if (box == null)
    {
      return;
    }
    long x = x2;
    RenderNode node = box.getLastChild();
    while (node != null)
    {
      final long nodeWidth = node.getMaximumBoxWidth();
      node.setX(x - nodeWidth);
      node.setWidth(node.getMaximumBoxWidth());
      node.setY(box.getY());
      node.setHeight(box.getHeight());

      node = node.getNext();
      x -= nodeWidth;
    }
    box.setX(x);
    box.setWidth(x2 - x);
    box.setContentAreaX1(x);
    box.setContentAreaX2(x2 - x);
  }

}
