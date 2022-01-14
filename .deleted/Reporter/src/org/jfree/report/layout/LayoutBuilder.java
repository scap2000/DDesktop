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
 * LayoutBuilder.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import java.awt.Shape;

import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.manual.Utf16LE;
import org.jfree.report.Anchor;
import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.ImageContainer;
import org.jfree.report.RootLevelBand;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.RawDataSource;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.function.ProcessingContext;
import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderLength;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.BoxDefinitionFactory;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.style.AnchorStyleSheet;
import org.jfree.report.layout.style.DynamicHeightWrapperStyleSheet;
import org.jfree.report.layout.style.DynamicReplacedContentStyleSheet;
import org.jfree.report.layout.style.NonDynamicHeightWrapperStyleSheet;
import org.jfree.report.layout.style.NonDynamicReplacedContentStyleSheet;
import org.jfree.report.layout.style.ParagraphPoolboxStyleSheet;
import org.jfree.report.layout.style.SectionKeepTogetherStyleSheet;
import org.jfree.report.layout.style.SimpleStyleSheet;
import org.jfree.report.layout.text.DefaultRenderableTextFactory;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.ReportDrawable;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.ui.Drawable;

/**
 * Creation-Date: 03.04.2007, 14:59:41
 *
 * @author Thomas Morgner
 */
public class LayoutBuilder
{
  private OutputProcessorMetaData metaData;
  private CodePointBuffer buffer;
  private DefaultRenderableTextFactory textFactory;
  private TextCache textCache;
  private StyleCache bandCache;
  private StyleCache styleCache;
  private StyleCache textStyleCache;
  private BoxDefinitionFactory boxDefinitionFactory;
  private SimpleStyleSheet bandWithKeepTogetherStyle;

  public LayoutBuilder(final OutputProcessorMetaData metaData)
  {
    this.metaData = metaData;
    this.textFactory = new DefaultRenderableTextFactory(metaData);
    this.textCache = new TextCache();

    final StyleKey[] definedStyleKeys = StyleKey.getDefinedStyleKeys();
    final boolean paddingsDisabled = metaData.isFeatureSupported(OutputProcessorFeature.DISABLE_PADDING);
    this.bandCache = new StyleCache(definedStyleKeys, paddingsDisabled);
    this.styleCache = new StyleCache(definedStyleKeys, paddingsDisabled);
    this.textStyleCache = new StyleCache(definedStyleKeys, paddingsDisabled);
    this.boxDefinitionFactory = new BoxDefinitionFactory();
    this.bandWithKeepTogetherStyle = new SimpleStyleSheet
        (new SectionKeepTogetherStyleSheet(true), definedStyleKeys);
  }

  private String getStyleFromLayoutManager(final Band band)
  {
    final Object layoutManager = band.getStyle().getStyleProperty(BandLayoutManager.LAYOUTMANAGER);
    if (layoutManager == null)
    {
      return "canvas";
    }
    if (layoutManager instanceof StackedLayoutManager)
    {
      return "block";
    }
    return "canvas";
  }

  public void add(final RenderBox parent,
                  final Band band,
                  final ExpressionRuntime runtime,
                  final Object stateKey)
  {
    final boolean parentIsInlineContainer =
        (parent instanceof InlineRenderBox || parent instanceof ParagraphRenderBox);
    if (isEmpty(band))
    {
      if (isControlBand(band))
      {
        final RenderBox box = produceBox(band, stateKey, parentIsInlineContainer);
        parent.addChild(box);
        box.close();
      }
      else if (band instanceof RootLevelBand)
      {
        final BlockRenderBox markerBox = new BlockRenderBox(bandWithKeepTogetherStyle, BoxDefinition.EMPTY, stateKey);
        parent.addChild(markerBox);
        markerBox.close();
      }
      return;
    }

    final RenderBox box = produceBox(band, stateKey, parentIsInlineContainer);
    ParagraphRenderBox paragraphBox = null;
    if (box instanceof InlineRenderBox && parentIsInlineContainer == false)
    {
      // Normalize the rendering-model. Inline-Boxes must always be contained in Paragraph-Boxes ..
      final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(band.getStyle());
      paragraphBox = new ParagraphRenderBox(box.getStyleSheet(), boxDefinition, stateKey);
      paragraphBox.setName(band.getName());
      paragraphBox.getBoxDefinition().setPreferredWidth(RenderLength.AUTO);
      paragraphBox.addChild(box);

      parent.addChild(paragraphBox);
    }
    else
    {
      parent.addChild(box);
    }

    final Element[] elementArray = band.getElementArray();
    for (int i = 0; i < elementArray.length; i++)
    {
      final Element element = elementArray[i];
      if (element instanceof Band)
      {
        final Band childBand = (Band) element;
        add(box, childBand, runtime, stateKey);
      }
      else
      {
        if (element.isVisible() == false)
        {
          continue;
        }

        final Object value = element.getValue(runtime);
        if (metaData.isContentSupported(value))
        {
          if (value instanceof ReportDrawable)
          {
            // A report drawable element receives some context information as well.
            final ReportDrawable reportDrawable = (ReportDrawable) value;
            final ProcessingContext processingContext = runtime.getProcessingContext();
            reportDrawable.setLayoutSupport(processingContext.getLayoutSupport());
            reportDrawable.setConfiguration(processingContext.getConfiguration());
            reportDrawable.setResourceBundleFactory(processingContext.getResourceBundleFactory());
            processReportDrawableContent(reportDrawable, box, element, stateKey);
          }
          else if (value instanceof Anchor)
          {
            processAnchor((Anchor) value, box, element, stateKey);
          }
          else if (value != null)
          {
            final DataSource dataSource = element.getDataSource();
            final Object rawValue;
            if (dataSource instanceof RawDataSource)
            {
              final RawDataSource rds = (RawDataSource) dataSource;
              rawValue = rds.getRawValue(runtime);
            }
            else
            {
              rawValue = null;
            }

            if (value instanceof ImageContainer ||
                value instanceof Shape ||
                value instanceof Drawable)
            {
              processReplacedContent(value, rawValue, box, element, stateKey);
            }
            else
            {
              processText(value, rawValue, box, element, stateKey);
            }
          }
          // ignore null values ..
        }
      }
    }

    if (paragraphBox != null)
    {
      paragraphBox.close();
    }

    box.close();
  }

  private boolean isControlBand(final Band band)
  {
    final ElementStyleSheet style = band.getStyle();
    if (style.getStyleProperty(BandStyleKeys.COMPUTED_SHEETNAME) != null)
    {
      return true;
    }
    if (style.getStyleProperty(BandStyleKeys.BOOKMARK) != null)
    {
      return true;
    }
    if ("inline".equals(style.getStyleProperty(BandStyleKeys.LAYOUT)) == false)
    {
      if (Boolean.TRUE.equals(style.getStyleProperty(BandStyleKeys.PAGEBREAK_AFTER)))
      {
        return true;
      }
      if (Boolean.TRUE.equals(style.getStyleProperty(BandStyleKeys.PAGEBREAK_BEFORE)))
      {
        return true;
      }
    }
    return false;
  }

  private boolean isEmpty(final Band band)
  {
    if (band.isVisible() == false)
    {
      return true;
    }

    if (band.getElementCount() > 0)
    {
      return false;
    }

    // A band is empty, if it has a defined minimum or preferred height
    final ElementStyleSheet style = band.getStyle();
    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.HEIGHT, null)))
    {
      return false;
    }
    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.WIDTH, null)))
    {
      return false;
    }
    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.POS_Y, null)))
    {
      return false;
    }
    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.POS_X, null)))
    {
      return false;
    }
    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.MIN_HEIGHT, null)))
    {
      return false;
    }
//    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.MIN_WIDTH, null)))
//    {
//      return false;
//    }
    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.PADDING_TOP, null)))
    {
      return false;
    }
    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.PADDING_LEFT, null)))
    {
      return false;
    }
    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.PADDING_BOTTOM, null)))
    {
      return false;
    }
    if (isLengthDefined(style.getStyleProperty(ElementStyleKeys.PADDING_RIGHT, null)))
    {
      return false;
    }
    if (BorderStyle.NONE.equals(style.getStyleProperty(ElementStyleKeys.BORDER_BOTTOM_STYLE, BorderStyle.NONE)) == false)
    {
      return false;
    }
    if (BorderStyle.NONE.equals(style.getStyleProperty(ElementStyleKeys.BORDER_TOP_STYLE, BorderStyle.NONE)) == false)
    {
      return false;
    }
    if (BorderStyle.NONE.equals(style.getStyleProperty(ElementStyleKeys.BORDER_LEFT_STYLE, BorderStyle.NONE)) == false)
    {
      return false;
    }
    if (BorderStyle.NONE.equals(style.getStyleProperty(ElementStyleKeys.BORDER_RIGHT_STYLE, BorderStyle.NONE)) == false)
    {
      return false;
    }
    return true;
  }

  private boolean isLengthDefined(final Object o)
  {
    if (o == null)
    {
      return false;
    }
    if (o instanceof Number == false)
    {
      return false;
    }
    final Number n = (Number) o;
    return n.doubleValue() != 0;
  }

  private void processText(final Object value,
                           final Object rawValue,
                           final RenderBox box,
                           final Element element,
                           final Object stateKey)
  {
    final String text = String.valueOf(value);
    final ElementStyleSheet style = element.getStyle();
    final TextCache.Result result = textCache.get(style.getId(), style.getChangeTracker(), text);
    if (result != null)
    {
      addTextNodes(element.getName(), rawValue, result.getText(), result.getFinish(),
          box, result.getStyleSheet(), stateKey);
      return;
    }

    final SimpleStyleSheet elementStyle;
    if (box instanceof CanvasRenderBox)
    {
      if (element.isDynamicContent() == false)
      {
        elementStyle = textStyleCache.getStyleSheet(new NonDynamicHeightWrapperStyleSheet(style));
      }
      else
      {
        elementStyle = styleCache.getStyleSheet(new DynamicHeightWrapperStyleSheet(style));
      }
    }
    else
    {
      elementStyle = styleCache.getStyleSheet(style);
    }

    if (buffer != null)
    {
      buffer.setCursor(0);
    }

    buffer = Utf16LE.getInstance().decodeString(text, buffer);
    final int[] data = buffer.getBuffer();

    if (box instanceof InlineRenderBox == false)
    {
      textFactory.startText();
    }

    final RenderNode[] renderNodes = textFactory.createText(data, 0, buffer.getLength(), elementStyle);
    final RenderNode[] finishNodes = textFactory.finishText();

    addTextNodes(element.getName(), rawValue, renderNodes, finishNodes, box, elementStyle, stateKey);
    textCache.store(style.getId(), style.getChangeTracker(), text, elementStyle, renderNodes, finishNodes);
  }

  private void processReportDrawableContent(final ReportDrawable value,
                                            final RenderBox box,
                                            final Element element,
                                            final Object stateKey)
  {
    final SimpleStyleSheet elementStyle;

    if (element.isDynamicContent() == false)
    {
      elementStyle = textStyleCache.getStyleSheet(new NonDynamicReplacedContentStyleSheet(element.getStyle()));
    }
    else
    {
      elementStyle = styleCache.getStyleSheet(new DynamicReplacedContentStyleSheet(element.getStyle()));
    }
    final RenderableReplacedContent child = new RenderableReplacedContent(elementStyle, value, null, metaData);
    value.setStyleSheet(elementStyle);

    if (box instanceof InlineRenderBox)
    {
      box.addChild(child);
    }
    else // add the replaced content into a ordinary block box. There's no need to create a full paragraph for it
    {
      final SimpleStyleSheet styleSheet;
      if (element.isDynamicContent() == false)
      {
        styleSheet = bandCache.getStyleSheet(new NonDynamicHeightWrapperStyleSheet(element.getStyle()));
      }
      else
      {
        styleSheet = bandCache.getStyleSheet(element.getStyle());
      }
      final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(styleSheet);
      final RenderBox autoParagraphBox = new CanvasRenderBox(styleSheet, boxDefinition, stateKey);
      autoParagraphBox.setName(element.getName());
      autoParagraphBox.getBoxDefinition().setPreferredWidth(RenderLength.AUTO);
      autoParagraphBox.addChild(child);
      autoParagraphBox.close();
      box.addChild(autoParagraphBox);
    }
  }

  private void processAnchor(final Anchor anchor,
                             final RenderBox box,
                             final Element element,
                             final Object stateKey)
  {
    final String anchorName = anchor.getName();

    if (box instanceof InlineRenderBox)
    {
      final SimpleStyleSheet styleSheet = bandCache.getStyleSheet
          (new NonDynamicHeightWrapperStyleSheet(new AnchorStyleSheet(anchorName, element.getStyle())));
      final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(styleSheet);
      final RenderBox autoParagraphBox = new InlineRenderBox(styleSheet, boxDefinition, stateKey);
      autoParagraphBox.setName(element.getName());
      autoParagraphBox.getBoxDefinition().setPreferredWidth(RenderLength.AUTO);
      autoParagraphBox.close();
      box.addChild(autoParagraphBox);
    }
    else // add the replaced content into a ordinary block box. There's no need to create a full paragraph for it
    {
      final SimpleStyleSheet styleSheet = bandCache.getStyleSheet
          (new NonDynamicHeightWrapperStyleSheet(new AnchorStyleSheet(anchorName, element.getStyle())));
      final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(styleSheet);
      final RenderBox autoParagraphBox = new CanvasRenderBox(styleSheet, boxDefinition, stateKey);
      autoParagraphBox.setName(element.getName());
      autoParagraphBox.getBoxDefinition().setPreferredWidth(RenderLength.AUTO);
      autoParagraphBox.close();
      box.addChild(autoParagraphBox);
    }
  }

  private void processReplacedContent(final Object value,
                                      final Object rawValue,
                                      final RenderBox box,
                                      final Element element,
                                      final Object stateKey)
  {
    final SimpleStyleSheet elementStyle;
    if (box instanceof CanvasRenderBox)
    {
      if (element.isDynamicContent() == false)
      {
        elementStyle = textStyleCache.getStyleSheet (new NonDynamicReplacedContentStyleSheet(element.getStyle()));
      }
      else
      {
        elementStyle = styleCache.getStyleSheet (new DynamicReplacedContentStyleSheet(element.getStyle()));
      }
    }
    else
    {
      elementStyle = styleCache.getStyleSheet (element.getStyle());
    }

    final ResourceKey rawKey;
    if (rawValue instanceof ResourceKey)
    {
      rawKey = (ResourceKey) rawValue;
    }
    else
    {
      rawKey = null;
    }

    final RenderableReplacedContent child = new RenderableReplacedContent(elementStyle, value, rawKey, metaData);

    if (box instanceof InlineRenderBox)
    {
      box.addChild(child);
    }
    else // add the replaced content into a ordinary block box. There's no need to create a full paragraph for it
    {
      final SimpleStyleSheet styleSheet;
      if (element.isDynamicContent() == false && box instanceof CanvasRenderBox)
      {
        styleSheet = bandCache.getStyleSheet(new NonDynamicHeightWrapperStyleSheet(element.getStyle()));
      }
      else
      {
        styleSheet = bandCache.getStyleSheet(element.getStyle());
      }
      final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(styleSheet);
      final RenderBox autoParagraphBox = new CanvasRenderBox(styleSheet, boxDefinition, stateKey);
      autoParagraphBox.setName(element.getName());
      autoParagraphBox.getBoxDefinition().setPreferredWidth(RenderLength.AUTO);
      autoParagraphBox.addChild(child);
      autoParagraphBox.close();
      box.addChild(autoParagraphBox);
    }
  }

  private void addTextNodes(final String name,
                            final Object rawValue,
                            final RenderNode[] renderNodes,
                            final RenderNode[] finishNodes,
                            final RenderBox box,
                            final StyleSheet elementStyle,
                            final Object stateKey)
  {
    if (box instanceof InlineRenderBox)
    {
      final StyleSheet styleSheet = bandCache.getStyleSheet(elementStyle);
      final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(styleSheet);
      final InlineRenderBox autoParagraphBox = new InlineRenderBox(styleSheet, boxDefinition, stateKey);
      autoParagraphBox.setName(name);
      autoParagraphBox.getBoxDefinition().setPreferredWidth(RenderLength.AUTO);
      autoParagraphBox.addChilds(renderNodes);
      autoParagraphBox.addChilds(finishNodes);
      autoParagraphBox.close();
      box.addChild(autoParagraphBox);
    }
    else
    {
      final StyleSheet styleSheet = bandCache.getStyleSheet(elementStyle);
      final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(styleSheet);
      final ParagraphRenderBox autoParagraphBox = new ParagraphRenderBox(styleSheet, boxDefinition, stateKey);
      autoParagraphBox.setRawValue(rawValue);
      autoParagraphBox.setName(name);
      autoParagraphBox.getBoxDefinition().setPreferredWidth(RenderLength.AUTO);
      autoParagraphBox.addChilds(renderNodes);
      autoParagraphBox.addChilds(finishNodes);
      autoParagraphBox.close();
      box.addChild(autoParagraphBox);
    }
  }

  private RenderBox produceBox(final Band band,
                               final Object stateKey,
                               final boolean parentIsInlineBox)
  {
    Object layoutType = band.getStyle().getStyleProperty(BandStyleKeys.LAYOUT, null);
    if (layoutType == null)
    {
      layoutType = getStyleFromLayoutManager(band);
    }
    if (parentIsInlineBox)
    {
      layoutType = "inline";
    }

    // todo: Check for cachability ..
    final RenderBox box;
    if ("block".equals(layoutType))
    {
      final SimpleStyleSheet styleSheet = bandCache.getStyleSheet(band.getStyle());
      final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(band.getStyle());
      box = new BlockRenderBox(styleSheet, boxDefinition, stateKey);
    }
    else if ("inline".equals(layoutType))
    {
      if (parentIsInlineBox)
      {
        final SimpleStyleSheet styleSheet = bandCache.getStyleSheet(band.getStyle());
        final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(band.getStyle());
        box = new InlineRenderBox(styleSheet, boxDefinition, stateKey);
      }
      else
      {
        final SimpleStyleSheet styleSheet =
            bandCache.getStyleSheet(new ParagraphPoolboxStyleSheet(band.getStyle()));
        final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(styleSheet);
        box = new InlineRenderBox(styleSheet, boxDefinition, stateKey);
      }
    }
    else // assume 'Canvas' by default ..
    {
      final SimpleStyleSheet styleSheet = bandCache.getStyleSheet(band.getStyle());
      final BoxDefinition boxDefinition = boxDefinitionFactory.getBoxDefinition(band.getStyle());
      box = new CanvasRenderBox(styleSheet, boxDefinition, stateKey);
    }

    // for the sake of debugging ..
    final String name = band.getName();
    if (name != null && name.startsWith(Band.ANONYMOUS_BAND_PREFIX) == false)
    {
      box.setName(name);
    }
    return box;
  }
}
