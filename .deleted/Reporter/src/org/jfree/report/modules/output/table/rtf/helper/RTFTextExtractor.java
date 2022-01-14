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
 * RTFTextExtractor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.rtf.helper;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.BaseFont;

import java.awt.Color;

import java.io.IOException;

import org.jfree.fonts.itext.BaseFontSupport;
import org.jfree.report.ImageContainer;
import org.jfree.report.InvalidReportStateException;
import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.RenderUtility;
import org.jfree.report.modules.output.table.base.DefaultTextExtractor;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.ui.Drawable;
import org.jfree.util.FastStack;

/**
 * Creation-Date: 10.05.2007, 19:49:46
 *
 * @author Thomas Morgner
 */
public class RTFTextExtractor extends DefaultTextExtractor
{
  private class StyleContext
  {
    private TextElementArray target;
    private BaseFontSupport fontSupport;
    private String fontName;
    private double fontSize;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean strikethrough;
    private Color textColor;
    private Color backgroundColor;

    protected StyleContext(final TextElementArray target,
                           final StyleSheet styleSheet,
                           final BaseFontSupport fontSupport)
    {
      this.target = target;
      this.fontSupport = fontSupport;
      this.fontName = (String) styleSheet.getStyleProperty(TextStyleKeys.FONT);
      this.fontSize = styleSheet.getDoubleStyleProperty(TextStyleKeys.FONTSIZE, 0);
      this.bold = styleSheet.getBooleanStyleProperty(TextStyleKeys.BOLD);
      this.italic = styleSheet.getBooleanStyleProperty(TextStyleKeys.ITALIC);
      this.underline = styleSheet.getBooleanStyleProperty(TextStyleKeys.UNDERLINED);
      this.strikethrough = styleSheet.getBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH);
      this.textColor = (Color) styleSheet.getStyleProperty(ElementStyleKeys.PAINT);
      this.backgroundColor = (Color) styleSheet.getStyleProperty(ElementStyleKeys.BACKGROUND_COLOR);
    }

    public TextElementArray getTarget()
    {
      return target;
    }

    public String getFontName()
    {
      return fontName;
    }

    public double getFontSize()
    {
      return fontSize;
    }

    public boolean isBold()
    {
      return bold;
    }

    public boolean isItalic()
    {
      return italic;
    }

    public boolean isUnderline()
    {
      return underline;
    }

    public boolean isStrikethrough()
    {
      return strikethrough;
    }

    public Color getTextColor()
    {
      return textColor;
    }

    public Color getBackgroundColor()
    {
      return backgroundColor;
    }

    public void add(final Element element)
    {
      target.add(element);
    }

    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final StyleContext that = (StyleContext) o;

      if (bold != that.bold)
      {
        return false;
      }
      if (that.fontSize != fontSize)
      {
        return false;
      }
      if (italic != that.italic)
      {
        return false;
      }
      if (strikethrough != that.strikethrough)
      {
        return false;
      }
      if (underline != that.underline)
      {
        return false;
      }
      if (backgroundColor != null ? !backgroundColor.equals(that.backgroundColor) : that.backgroundColor != null)
      {
        return false;
      }
      if (fontName != null ? !fontName.equals(that.fontName) : that.fontName != null)
      {
        return false;
      }
      if (textColor != null ? !textColor.equals(that.textColor) : that.textColor != null)
      {
        return false;
      }

      return true;
    }

    public int hashCode()
    {
      int result = (fontName != null ? fontName.hashCode() : 0);
      final long temp = fontSize != +0.0d ? Double.doubleToLongBits(fontSize) : 0L;
      result = 29 * result + (int) (temp ^ (temp >>> 32));
      result = 29 * result + (bold ? 1 : 0);
      result = 29 * result + (italic ? 1 : 0);
      result = 29 * result + (underline ? 1 : 0);
      result = 29 * result + (strikethrough ? 1 : 0);
      result = 29 * result + (textColor != null ? textColor.hashCode() : 0);
      result = 29 * result + (backgroundColor != null ? backgroundColor.hashCode() : 0);
      return result;
    }

    public void add(final String text)
    {
      int style = Font.NORMAL;
      if (bold)
      {
        style |= Font.BOLD;
      }
      if (italic)
      {
        style |= Font.ITALIC;
      }
      if (strikethrough)
      {
        style |= Font.STRIKETHRU;
      }
      if (underline)
      {
        style |= Font.UNDERLINE;
      }

      final BaseFont baseFont = fontSupport.createBaseFont(fontName, bold, italic, "utf-8", false);
      final Font font = new Font(baseFont, (float) fontSize, style, textColor);
      final Chunk c = new Chunk(text, font);
      if (backgroundColor != null)
      {
        c.setBackground(backgroundColor);
      }
      target.add(c);
    }
  }

  private RTFImageCache imageCache;
  private FastStack context;
  private OutputProcessorMetaData metaData;
  private BaseFontSupport fontSupport;
  private boolean handleImages;

  public RTFTextExtractor(final OutputProcessorMetaData metaData)
  {
    super(metaData);
    this.handleImages = metaData.isFeatureSupported(RTFOutputProcessorMetaData.IMAGES_ENABLED);
    context = new FastStack();
  }


  private StyleContext getCurrentContext()
  {
    return (StyleContext) context.peek();
  }

  public void compute(final RenderBox box,
                      final TextElementArray cell,
                      final RTFImageCache imageCache,
                      final OutputProcessorMetaData metaData,
                      final BaseFontSupport fontSupport)
  {
    this.metaData = metaData;
    this.fontSupport = fontSupport;
    this.context.clear();
    this.context.push(new StyleContext(cell, box.getStyleSheet(), fontSupport));
    this.imageCache = imageCache;
    getParagraphBounds().setRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());

    startProcessing(box);
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    // Compare the text style ..
    final StyleContext currentContext = getCurrentContext();
    final StyleContext boxContext = new StyleContext(currentContext.getTarget(), box.getStyleSheet(), fontSupport);
    if (currentContext.equals(boxContext) == false)
    {
      if (getTextLength() > 0)
      {
        final String text = getText();
        currentContext.add(text);
        clearText();
      }
      this.context.pop();
      this.context.push(boxContext);
    }
    return true;
  }

  protected void finishInlineBox(final InlineRenderBox box)
  {
    final StyleContext currentContext = getCurrentContext();
    if (getTextLength() > 0)
    {
      final String text = getText();
      currentContext.add(text);
      clearText();
    }
  }

  protected void processOtherNode(final RenderNode node)
  {
    final StrictBounds paragraphBounds = getParagraphBounds();
    if (node.isNodeVisible(paragraphBounds) == false)
    {
      return;
    }

    try
    {
      super.processOtherNode(node);
      if (node instanceof RenderableText)
      {
        if ((node.getX() + node.getWidth()) > (paragraphBounds.getX() + paragraphBounds.getWidth()))
        {
          // This node will only be partially visible. The end-of-line marker will not apply.
          return;
        }
        final RenderableText text = (RenderableText) node;
        if (text.isForceLinebreak())
        {
          final StyleContext currentContext = getCurrentContext();
          if (getTextLength() > 0)
          {
            currentContext.add(getText());
            clearText();
          }
          context.pop();
          final StyleContext cellContext = getCurrentContext();
          cellContext.add(currentContext.getTarget());

          context.push(new StyleContext(new Paragraph(), text.getStyleSheet(), fontSupport));
        }
      }
      else if (node instanceof RenderableReplacedContent && handleImages)
      {
        final RenderableReplacedContent rpc = (RenderableReplacedContent) node;
        processReplacedContent(rpc, node);
      }
    }
    catch (DocumentException ioe)
    {
      throw new InvalidReportStateException("Failed to extract text", ioe);
    }
    catch (IOException e)
    {
      // double ignore ..
      throw new InvalidReportStateException("Failed to extract text", e);
    }
  }

  private void processReplacedContent(final RenderableReplacedContent rpc, final RenderNode node)
      throws DocumentException, IOException
  {
    final Object rawObject = rpc.getRawObject();
    if (rawObject instanceof ImageContainer)
    {
      final Image image = imageCache.getImage((ImageContainer) rawObject);
      final StyleContext currentContext = getCurrentContext();
      if (getTextLength() > 0)
      {
        currentContext.add(getText());
        clearText();
      }
      currentContext.add(image);
    }
    else if (rawObject instanceof Drawable)
    {
      final StrictBounds rect = new StrictBounds
          (node.getX(), node.getY(), node.getWidth(), node.getHeight());
      final ImageContainer ic =
          RenderUtility.createImageFromDrawable((Drawable) rawObject, rect, node, metaData);
      final Image image = imageCache.getImage(ic);
      final StyleContext currentContext = getCurrentContext();
      if (getTextLength() > 0)
      {
        currentContext.add(getText());
        clearText();
      }
      currentContext.add(image);
    }
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    return false;
  }


  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    return true;
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    return true;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    context.push(new StyleContext(new Paragraph(), box.getStyleSheet(), fontSupport));
    clearText();

    super.processParagraphChilds(box);

    final StyleContext currentContext = getCurrentContext();
    if (getTextLength() > 0)
    {
      currentContext.add(getText());
      clearText();
    }
    context.pop();
    getCurrentContext().add(currentContext.getTarget());
  }

}
