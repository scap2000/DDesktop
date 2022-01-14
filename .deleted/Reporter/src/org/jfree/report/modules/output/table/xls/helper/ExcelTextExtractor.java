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
 * ExcelTextExtractor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls.helper;

import java.awt.Color;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;

import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.modules.output.table.base.DefaultTextExtractor;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;

/**
 * Creation-Date: 10.05.2007, 19:49:46
 *
 * @author Thomas Morgner
 */
public class ExcelTextExtractor extends DefaultTextExtractor
{
  private static class RichTextFormat
  {
    private int position;
    private HSSFFontWrapper font;

    protected RichTextFormat(final int position, final HSSFFontWrapper font)
    {
      this.position = position;
      this.font = font;
    }

    public int getPosition()
    {
      return position;
    }

    public HSSFFontWrapper getFont()
    {
      return font;
    }


  }

  private ArrayList buffer;

  public ExcelTextExtractor(final OutputProcessorMetaData metaData)
  {
    super(metaData);
    buffer = new ArrayList();
  }

  public Object compute (final RenderBox paraBox, final ExcelFontFactory fontFactory)
  {
    buffer.clear();
    super.compute(paraBox);

    final String text = getText();
    if (buffer.size() <= 1)
    {
      // A simple result. So there's no need to create a rich-text string.
      final Object rawResult = getRawResult();
      if (rawResult != null)
      {
        return rawResult;
      }
      if (text.length() > 0)
      {
        return text;
      }
      return null;
    }
    else if (text.length() > 0)
    {
      // There's rich text.
      final HSSFRichTextString rtStr = new HSSFRichTextString(text);
      for (int i = 0; i < buffer.size(); i++)
      {
        final RichTextFormat o = (RichTextFormat) buffer.get(i);
        final int position = o.getPosition();
        final HSSFFontWrapper font = o.getFont();
        if (i == (buffer.size() - 1))
        {
          // Last element ..
          rtStr.applyFont(position, text.length() - position, fontFactory.getExcelFont(font));
        }
        else
        {
          final RichTextFormat next = (RichTextFormat) buffer.get(i + 1);
          rtStr.applyFont(position, next.getPosition() - position, fontFactory.getExcelFont(font));
        }
      }
      return rtStr;
    }
    return null;
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    if (box.isBoxVisible(getParagraphBounds()) == false)
    {
      return false;
    }
    
    final StyleSheet styleSheet = box.getStyleSheet();
    final Color textColor = (Color) styleSheet.getStyleProperty(ElementStyleKeys.PAINT);
    final String fontName = (String) styleSheet.getStyleProperty(TextStyleKeys.FONT);
    final short fontSize = (short) styleSheet.getIntStyleProperty(TextStyleKeys.FONTSIZE, 0);
    final boolean bold = styleSheet.getBooleanStyleProperty(TextStyleKeys.BOLD);
    final boolean italic = styleSheet.getBooleanStyleProperty(TextStyleKeys.ITALIC);
    final boolean underline = styleSheet.getBooleanStyleProperty(TextStyleKeys.UNDERLINED);
    final boolean strikethrough = styleSheet.getBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH);
    final HSSFFontWrapper wrapper = new HSSFFontWrapper
            (fontName, fontSize, bold, italic, underline, strikethrough, textColor);
    final RichTextFormat rtf = new RichTextFormat(getTextLength(), wrapper);

    // Check the style.
    if (buffer.isEmpty())
    {
      buffer.add(rtf);
    }
    else
    {
      final RichTextFormat lastRtf = (RichTextFormat) buffer.get(buffer.size() - 1);
      if (lastRtf.getFont().equals(rtf.getFont()) == false)
      {
        buffer.add(rtf);
      }
    }

    return true;
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    return false;
  }

}
