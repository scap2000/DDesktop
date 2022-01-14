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
 * HtmlTextExtractor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html;

import java.io.IOException;

import java.text.NumberFormat;

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
import org.jfree.report.layout.model.SpacerRenderNode;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.RenderUtility;
import org.jfree.report.modules.output.table.base.DefaultTextExtractor;
import org.jfree.report.modules.output.table.html.helper.HtmlOutputProcessingException;
import org.jfree.report.modules.output.table.html.helper.StyleBuilder;
import org.jfree.report.modules.output.table.html.helper.StyleManager;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.repository.ContentIOException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.ui.Drawable;
import org.jfree.util.StackableRuntimeException;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;
import org.jfree.xmlns.writer.XmlWriterSupport;

/**
 * Creation-Date: 02.11.2007, 15:58:29
*
* @author Thomas Morgner
*/
public class HtmlTextExtractor extends DefaultTextExtractor
{
  private OutputProcessorMetaData metaData;
  private XmlWriter xmlWriter;
  private StyleManager styleManager;
  private StyleBuilder styleBuilder;
  private HtmlContentGenerator contentGenerator;

  public HtmlTextExtractor(final OutputProcessorMetaData metaData,
                           final XmlWriter xmlWriter,
                           final StyleManager styleManager,
                           final HtmlContentGenerator contentGenerator)
  {
    super(metaData);
    this.contentGenerator = contentGenerator;

    this.metaData = metaData;
    this.xmlWriter = xmlWriter;
    this.styleManager = styleManager;
    this.styleBuilder = new StyleBuilder();
  }

  public void performOutput (final RenderBox content) throws IOException
  {
    styleBuilder.clear();
    clearText();
    setRawResult(null);
    
    if (content instanceof ParagraphRenderBox)
    {
      processParagraphCell((ParagraphRenderBox) content);
    }
    else
    {
      processBoxChilds(content);
    }
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    // We shall never see an other box here ..
    return false;
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    try
    {
      final StyleSheet styleSheet = box.getStyleSheet();
      final String target = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TARGET);
      if (target != null)
      {
        final String window = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_WINDOW);
        final AttributeList linkAttr = new AttributeList();
        linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "href", target);
        if (window != null)
        {
          linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "target", window);
        }
        final String title = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TITLE);
        if (title != null)
        {
          linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "title", HtmlPrinter.normalize(title, xmlWriter));
        }
        xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", linkAttr, XmlWriterSupport.OPEN);
      }
      return true;
    }
    catch (IOException e)
    {
      throw new HtmlOutputProcessingException("Failed to perform IO", e);
    }
  }

  public void finishCanvasBox(final CanvasRenderBox box)
  {
    try
    {
      final StyleSheet styleSheet = box.getStyleSheet();
      final String target = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TARGET);
      if (target != null)
      {
        xmlWriter.writeCloseTag();
      }
    }
    catch (IOException e)
    {
      throw new HtmlOutputProcessingException("Failed to perform IO", e);
    }
  }

  protected void processParagraphCell(final ParagraphRenderBox box) throws IOException
  {
    final StyleSheet styleSheet = box.getStyleSheet();
    final String target = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TARGET);
    if (target != null)
    {
      final String window = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_WINDOW);
      final AttributeList linkAttr = new AttributeList();
      linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "href", HtmlPrinter.normalize(target, xmlWriter));
      if (window != null)
      {
        linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "target", HtmlPrinter.normalize(window, xmlWriter));
      }
      final String title = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TITLE);
      if (title != null)
      {
        linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "title", HtmlPrinter.normalize(title, xmlWriter));
      }
      xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", linkAttr, XmlWriterSupport.OPEN);

      final AttributeList attrList = new AttributeList();
      styleManager.updateStyle(HtmlPrinter.produceTextStyle(styleBuilder, box, false), attrList);
      xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "span", attrList, XmlWriterSupport.OPEN);
    }

    processParagraphChilds(box);

    if (target != null)
    {
      xmlWriter.writeCloseTag(); // closing the span ..
      xmlWriter.writeCloseTag();
    }

  }

  protected void addLinebreak()
  {
    try
    {
      xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "br", XmlWriter.CLOSE);
    }
    catch (IOException e)
    {
      throw new HtmlOutputProcessingException("Failed to perform IO", e);
    }
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    try
    {
      final AttributeList attrList = new AttributeList();
      styleManager.updateStyle(HtmlPrinter.produceTextStyle(styleBuilder, box, true), attrList);

      xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "div", attrList, XmlWriterSupport.OPEN);
      final StyleSheet styleSheet = box.getStyleSheet();
      final String anchor = (String) styleSheet.getStyleProperty(ElementStyleKeys.ANCHOR_NAME);
      if (anchor != null)
      {
        xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", "name", anchor, XmlWriterSupport.CLOSE);
      }

      final String target = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TARGET);
      if (target != null)
      {
        final String window = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_WINDOW);
        final AttributeList linkAttr = new AttributeList();
        linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "href", HtmlPrinter.normalize(target, xmlWriter));
        if (window != null)
        {
          linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "target", HtmlPrinter.normalize(window, xmlWriter));
        }
        final String title = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TITLE);
        if (title != null)
        {
          linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "title", HtmlPrinter.normalize(title, xmlWriter));
        }
        xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", linkAttr, XmlWriterSupport.OPEN);
      }

      return true;
    }
    catch (IOException e)
    {
      throw new HtmlOutputProcessingException("Failed to perform IO", e);
    }
  }

  protected void finishBlockBox(final BlockRenderBox box)
  {
    try
    {
      final StyleSheet styleSheet = box.getStyleSheet();
      final String target = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TARGET);
      if (target != null)
      {
        // close anchor tag
        xmlWriter.writeCloseTag();
      }

      xmlWriter.writeCloseTag();
    }
    catch (IOException e)
    {
      throw new HtmlOutputProcessingException("Failed to perform IO", e);
    }
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    try
    {
      final AttributeList attrList = new AttributeList();
      styleManager.updateStyle(HtmlPrinter.produceTextStyle(styleBuilder, box, true), attrList);
      xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "span", attrList, XmlWriterSupport.OPEN);
      final StyleSheet styleSheet = box.getStyleSheet();
      final String anchor = (String) styleSheet.getStyleProperty(ElementStyleKeys.ANCHOR_NAME);
      if (anchor != null)
      {
        xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", "name", HtmlPrinter.normalize(anchor, xmlWriter), XmlWriterSupport.CLOSE);
      }

      final String target = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TARGET);
      if (target != null)
      {
        final String window = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_WINDOW);
        final AttributeList linkAttr = new AttributeList();
        linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "href", HtmlPrinter.normalize(target, xmlWriter));
        if (window != null)
        {
          linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "target", HtmlPrinter.normalize(window, xmlWriter));
        }
        final String title = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TITLE);
        if (title != null)
        {
          linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "title", HtmlPrinter.normalize(title, xmlWriter));
        }
        xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", linkAttr, XmlWriterSupport.OPEN);
      }
      return true;
    }
    catch (IOException e)
    {
      throw new HtmlOutputProcessingException("Failed to perform IO", e);
    }
  }

  protected void finishInlineBox(final InlineRenderBox box)
  {
    try
    {
      final StyleSheet styleSheet = box.getStyleSheet();
      final String target = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TARGET);
      if (target != null)
      {
        // close anchor tag
        xmlWriter.writeCloseTag();
      }

      xmlWriter.writeCloseTag();
    }
    catch (IOException e)
    {
      throw new HtmlOutputProcessingException("Failed to perform IO", e);
    }
  }


  protected void processOtherNode(final RenderNode node)
  {
    try
    {
      if (node instanceof RenderableText)
      {
        super.processOtherNode(node);
      }
      else if (node instanceof SpacerRenderNode)
      {
        xmlWriter.writeText(" ");
      }
      else if (node instanceof RenderableReplacedContent)
      {
        final StyleSheet styleSheet = node.getStyleSheet();
        final String anchor = (String) styleSheet.getStyleProperty(ElementStyleKeys.ANCHOR_NAME);
        if (anchor != null)
        {
          xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", "name", HtmlPrinter.normalize(anchor, xmlWriter), XmlWriterSupport.CLOSE);
        }

        final String target = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TARGET);
        if (target != null)
        {
          final String window = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_WINDOW);
          final AttributeList linkAttr = new AttributeList();
          linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "href", HtmlPrinter.normalize(target, xmlWriter));
          if (window != null)
          {
            linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "target", HtmlPrinter.normalize(window, xmlWriter));
          }
          final String title = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TITLE);
          if (title != null)
          {
            linkAttr.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "title", HtmlPrinter.normalize(title, xmlWriter));
          }
          xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", linkAttr, XmlWriterSupport.OPEN);
        }

        final RenderableReplacedContent rc = (RenderableReplacedContent) node;
        final ResourceKey source = rc.getSource();
        // We have to do three things here. First, w have to check what kind
        // of content we deal with.
        if (source != null)
        {
          // Cool, we have access to the raw-data. Thats always nice as we
          // dont have to recode the whole thing.
          if (contentGenerator.isRegistered(source))
          {
            // Write image reference; return the name of the reference ..
            final String name = contentGenerator.writeRaw(source);
            if (name != null)
            {
              // Write image reference ..
              final AttributeList attrList = new AttributeList();
              attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "src", name);
              // width and height and scaling and so on ..
              styleManager.updateStyle(produceImageStyle(rc), attrList);
              xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "img", attrList, XmlWriter.CLOSE);

              contentGenerator.registerContent(source, name);
              if (target != null)
              {
                xmlWriter.writeCloseTag();
              }
              return;
            }
            else
            {
              contentGenerator.registerFailure(source);
            }
          }
          else
          {
            final String cachedName = contentGenerator.getRegisteredName(source);

            if (cachedName != null)
            {
              final AttributeList attrList = new AttributeList();
              attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "src", cachedName);
              // width and height and scaling and so on ..
              styleManager.updateStyle(produceImageStyle(rc), attrList);
              xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "img", attrList, XmlWriter.CLOSE);

              if (target != null)
              {
                xmlWriter.writeCloseTag();
              }
              return;
            }
          }
        }

        // Fallback: (At the moment, we only support drawables and images.)
        final Object rawObject = rc.getRawObject();
        if (rawObject instanceof ImageContainer)
        {
          // Make it a PNG file ..
          //xmlWriter.writeComment("Image content source:" + source);
          final String name = contentGenerator.writeImage((ImageContainer) rawObject);
          if (name != null)
          {
            // Write image reference ..
            final AttributeList attrList = new AttributeList();
            attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "src", name);
            // width and height and scaling and so on ..
            styleManager.updateStyle(produceImageStyle(rc), attrList);
            xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "img", attrList, XmlWriter.CLOSE);
          }
        }
        else if (rawObject instanceof Drawable)
        {
          // render it into an Buffered image and make it a PNG file.
          final StrictBounds cb = new StrictBounds
              (node.getX(), node.getY(), node.getWidth(), node.getHeight());
          final ImageContainer image =
              RenderUtility.createImageFromDrawable((Drawable) rawObject, cb, node, metaData);
          if (image == null)
          {
            // todo: DOes not yet handle drawables ..
            xmlWriter.writeComment("Drawable content [No image generated]:" + source);
            if (target != null)
            {
              xmlWriter.writeCloseTag();
            }
            return;
          }

          final String name = contentGenerator.writeImage(image);
          if (name == null)
          {
            xmlWriter.writeComment("Drawable content [No image written]:" + source);
            if (target != null)
            {
              xmlWriter.writeCloseTag();
            }
            return;
          }

          xmlWriter.writeComment("Drawable content:" + source);
          // Write image reference ..
          final AttributeList attrList = new AttributeList();
          attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "src", name);
          // width and height and scaling and so on ..
          styleManager.updateStyle(produceImageStyle(rc), attrList);
          xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "img", attrList, XmlWriter.CLOSE);
        }

        if (target != null)
        {
          xmlWriter.writeCloseTag();
        }

      }
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
    catch (ContentIOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
//    catch (URLRewriteException e)
//    {
//      Log.warn("Rewriting the URL failed.", e);
//      throw new StackableRuntimeException("Failed", e);
//    }
  }


  /**
   * Populates the style builder with the style information for the image based on the RenderableReplacedContent
   */
  private StyleBuilder produceImageStyle(final RenderableReplacedContent rc)
  {
    styleBuilder.clear(); // cuts down on object creation
    final NumberFormat pointConverter = styleBuilder.getPointConverter();
    styleBuilder.append("height", pointConverter.format(StrictGeomUtility.toExternalValue(rc.getHeight())), "pt");
    styleBuilder.append("width", pointConverter.format(StrictGeomUtility.toExternalValue(rc.getWidth())), "pt");
    return styleBuilder;
  }


  protected void drawText(final RenderableText renderableText, final long contentX2)
  {
    try
    {
      super.drawText(renderableText, contentX2);
      if (getTextLength() > 0)
      {
        xmlWriter.writeText(xmlWriter.normalizeLocal(getText(), false));
        clearText();
      }
//      if (renderableText.isForceLinebreak())
//      {
//        xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "br", XmlWriter.CLOSE);
//      }
    }
    catch(IOException ioe)
    {
      throw new InvalidReportStateException ("Failed to write text", ioe);
    }

  }
}
