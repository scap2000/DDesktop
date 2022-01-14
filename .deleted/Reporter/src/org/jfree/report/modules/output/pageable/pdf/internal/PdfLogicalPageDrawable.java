package org.jfree.report.modules.output.pageable.pdf.internal;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfTextArray;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.awt.geom.AffineTransform;

import org.jfree.fonts.itext.BaseFontRecord;
import org.jfree.fonts.itext.BaseFontSupport;
import org.jfree.fonts.text.Spacing;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PhysicalPageBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.text.Glyph;
import org.jfree.report.modules.output.pageable.graphics.internal.LogicalPageDrawable;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 17.07.2007, 18:41:46
 *
 * @author Thomas Morgner
 */
public class PdfLogicalPageDrawable extends LogicalPageDrawable
{
  protected static class PdfTextSpec extends TextSpec
  {
    private boolean embedded;
    private String encoding;
    private BaseFontRecord baseFontRecord;
    private BaseFontRecord baseFont;
    private PdfContentByte contentByte;

    public PdfTextSpec(final StyleSheet layoutContext,
                       final OutputProcessorMetaData metaData,
                       final PdfGraphics2D g2,
                       final BaseFontSupport fontSupport,
                       final BaseFontRecord baseFont,
                       final PdfContentByte cb)
    {
      super(layoutContext, metaData, g2);
      this.baseFont = baseFont;
      contentByte = cb;
      encoding = (String) layoutContext.getStyleProperty
          (TextStyleKeys.FONTENCODING, fontSupport.getDefaultEncoding());

      embedded = metaData.isFeatureSupported(OutputProcessorFeature.EMBED_ALL_FONTS) ||
          layoutContext.getBooleanStyleProperty(TextStyleKeys.EMBEDDED_FONT);

      baseFontRecord = fontSupport.createBaseFontRecord(getFontName(), isBold(), isItalics(), encoding, embedded);
    }


    public BaseFontRecord getBaseFont()
    {
      return baseFont;
    }

    public PdfContentByte getContentByte()
    {
      return contentByte;
    }

    public boolean isEmbedded()
    {
      return embedded;
    }

    public String getEncoding()
    {
      return encoding;
    }

    public BaseFontRecord getBaseFontRecord()
    {
      return baseFontRecord;
    }

    public void finishText()
    {
      contentByte.endText();
    }

    public void close()
    {
      //super.close(); // we do not dispose the graphics as we are working on the original object
    }
  }

  private static final float ITALIC_ANGLE = 0.21256f;

  private PdfWriter writer;
  private BaseFontSupport fontSupport;
  private float globalHeight;

  public PdfLogicalPageDrawable(final LogicalPageBox rootBox,
                                final OutputProcessorMetaData metaData,
                                final PdfWriter writer,
                                final PhysicalPageBox page,
                                final BaseFontSupport fontSupport)
  {
    super(rootBox, metaData);
    this.writer = writer;
    this.fontSupport = fontSupport;
    if (page != null)
    {
      this.globalHeight = (float) StrictGeomUtility.toExternalValue(page.getHeight() - page.getImageableY());
    }
    else
    {
      this.globalHeight = rootBox.getPageHeight();
    }
  }

  protected void drawAnchor(final RenderNode content)
  {
    if (content.isNodeVisible(getDrawArea()) == false)
    {
      return;
    }
    final String anchorName = (String) content.getStyleSheet().getStyleProperty(ElementStyleKeys.ANCHOR_NAME);
    if (anchorName == null)
    {
      return;
    }
    final AffineTransform affineTransform = getGraphics().getTransform();
    final float translateX = (float) affineTransform.getTranslateX();

    final float upperY = translateX + (float) (globalHeight - StrictGeomUtility.toExternalValue(content.getY()));
    final float leftX = (float) (StrictGeomUtility.toExternalValue(content.getX()));
    final PdfDestination dest = new PdfDestination(PdfDestination.FIT, leftX, upperY, 0);
    writer.getDirectContent().localDestination(anchorName, dest);
  }

  protected void drawBookmark(final RenderNode box, final String bookmark)
  {
    if (box.isNodeVisible(getDrawArea()) == false)
    {
      return;
    }
    final PdfOutline root = writer.getDirectContent().getRootOutline();

    final AffineTransform affineTransform = getGraphics().getTransform();
    final float translateX = (float) affineTransform.getTranslateX();

    final float upperY = translateX + (float) (globalHeight - StrictGeomUtility.toExternalValue(box.getY()));
    final float leftX = (float) (StrictGeomUtility.toExternalValue(box.getX()));
    final PdfDestination dest = new PdfDestination(PdfDestination.FIT, leftX, upperY, 0);
    new PdfOutline(root, dest, bookmark);
    // destination will always point to the 'current' page
    // todo: Make this a hierarchy ..
  }

  protected void drawHyperlink(final RenderNode box, final String target, final String window)
  {
    if (box.isNodeVisible(getDrawArea()) == false)
    {
      return;
    }

    final PdfAction action = new PdfAction();
    action.put(PdfName.S, PdfName.URI);
    action.put(PdfName.URI, new PdfString(target));

    final AffineTransform affineTransform = getGraphics().getTransform();
    final float translateX = (float) affineTransform.getTranslateX();

    final float leftX = translateX + (float) (StrictGeomUtility.toExternalValue(box.getX()));
    final float rightX = translateX + (float) (StrictGeomUtility.toExternalValue(box.getX() + box.getWidth()));
    final float lowerY = (float) (globalHeight - StrictGeomUtility.toExternalValue(box.getY() + box.getHeight()));
    final float upperY = (float) (globalHeight - StrictGeomUtility.toExternalValue(box.getY()));
    final PdfContentByte cb = this.writer.getDirectContent();

    cb.setAction(action, leftX, lowerY, rightX, upperY);
//    // Title support requires us to add a annotation and to selectively show or hide it
//    // This will be a 1.0 feature, there is not enough time to do it properly now.
//    final PdfAction hideAction = new PdfAction();
//    action.put (PdfName.S, PdfName.HIDE);
//    action.put (PdfName.T, <dictionary-reference>);
//    cb.setAction(hideAction, leftX, lowerY, rightX, upperY);

  }

  protected void drawText(final RenderableText renderableText, final long contentX2)
  {
    if (renderableText.getLength() == 0)
    {
      return;
    }
    if (renderableText.isNodeVisible(getDrawArea()) == false)
    {
      return;
    }

    final long posX = renderableText.getX();
    final long posY = renderableText.getY();
    final float x1 = (float) (StrictGeomUtility.toExternalValue(posX));

    PdfContentByte cb;
    PdfTextSpec textSpec = (PdfTextSpec) getTextSpec();
    if (textSpec == null)
    {
      final StyleSheet layoutContext = renderableText.getStyleSheet();

      // The code below may be weird, but at least it is predictable weird.
      final String fontName = getMetaData().getNormalizedFontFamilyName
          ((String) layoutContext.getStyleProperty(TextStyleKeys.FONT));
      final String encoding = (String) layoutContext.getStyleProperty
          (TextStyleKeys.FONTENCODING, fontSupport.getDefaultEncoding());
      final float fontSize = (float) layoutContext.getDoubleStyleProperty(TextStyleKeys.FONTSIZE, 10);

      final boolean embed =
          getMetaData().isFeatureSupported(OutputProcessorFeature.EMBED_ALL_FONTS) ||
              layoutContext.getBooleanStyleProperty(TextStyleKeys.EMBEDDED_FONT);
      final boolean bold = layoutContext.getBooleanStyleProperty(TextStyleKeys.BOLD);
      final boolean italics = layoutContext.getBooleanStyleProperty(TextStyleKeys.ITALIC);

      final BaseFontRecord baseFontRecord =
          fontSupport.createBaseFontRecord(fontName, bold, italics, encoding, embed);

      final PdfGraphics2D g2 = (PdfGraphics2D) getGraphics();
      final Color cssColor = (Color) layoutContext.getStyleProperty(ElementStyleKeys.PAINT);
      g2.setPaint(cssColor);
      g2.setFillPaint();
      g2.setStrokePaint();
      //final float translateY = (float) affineTransform.getTranslateY();

      cb = g2.getRawContentByte();

      textSpec = new PdfTextSpec(layoutContext, getMetaData(), g2, fontSupport, baseFontRecord, cb);
      setTextSpec(textSpec);

      cb.beginText();
      cb.setFontAndSize(baseFontRecord.getBaseFont(), fontSize);
    }
    else
    {
      cb = textSpec.getContentByte();
    }

    final BaseFontRecord baseFontRecord = textSpec.getBaseFont();
    final BaseFont baseFont = baseFontRecord.getBaseFont();
    final float ascent = baseFont.getFontDescriptor(BaseFont.BBOXURY, textSpec.getFontSize());
    final float y2 = (float) (StrictGeomUtility.toExternalValue(posY) + ascent);
    final float y = globalHeight - y2;


    final AffineTransform affineTransform = textSpec.getGraphics().getTransform();
    final float translateX = (float) affineTransform.getTranslateX();

    if (baseFontRecord.isTrueTypeFont() && textSpec.isBold())
    {
      final float strokeWidth = textSpec.getFontSize() / 30.0f; // right from iText ...
      if (strokeWidth == 1)
      {
        cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
      }
      else
      {
        cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
        cb.setLineWidth(strokeWidth);
      }
    }
    else
    {
      cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
    }

    // if the font does not declare to be italics already, emulate it ..
    if (baseFontRecord.isTrueTypeFont() && textSpec.isItalics())
    {
      final float italicAngle =
          baseFont.getFontDescriptor(BaseFont.ITALICANGLE, textSpec.getFontSize());
      if (italicAngle == 0)
      {
        // italics requested, but the font itself does not supply italics gylphs.
        cb.setTextMatrix(1, 0, ITALIC_ANGLE, 1, x1 + translateX, y);
      }
      else
      {
        cb.setTextMatrix(x1 + translateX, y);
      }
    }
    else
    {
      cb.setTextMatrix(x1 + translateX, y);
    }

    final OutputProcessorMetaData metaData = getMetaData();
    final Glyph[] gs = renderableText.getGlyphs();
    final int offset = renderableText.getOffset();

    if (metaData.isFeatureSupported(OutputProcessorFeature.FAST_FONTRENDERING) &&
        isNormalTextSpacing(renderableText))
    {
      final int maxLength = computeMaximumTextSize(renderableText, contentX2);
      final String text = textToString(gs, renderableText.getOffset(), maxLength);

      cb.showText(text);
    }
    else
    {
      final PdfTextArray textArray = new PdfTextArray();
      final StringBuffer buffer = new StringBuffer();
      final int maxPos = offset + computeMaximumTextSize(renderableText, contentX2);

      for (int i = offset; i < maxPos; i++)
      {
        final Glyph g = gs[i];
        final Spacing spacing = g.getSpacing();
        if (i != offset)
        {
          final int optimum = spacing.getOptimum();
          if (optimum != 0)
          {
            textArray.add(buffer.toString());
            textArray.add(-optimum / textSpec.getFontSize());
            buffer.setLength(0);
          }
        }

        final String text = glpyhToString(g);
        buffer.append(text);
      }
      if (buffer.length() > 0)
      {
        textArray.add(buffer.toString());
      }
      cb.showText(textArray);
    }
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    final PdfTextSpec textSpec = (PdfTextSpec) getTextSpec();
    if (textSpec != null)
    {
      textSpec.finishText();
    }
    return super.startInlineBox(box);
  }

  protected void finishInlineBox(final InlineRenderBox box)
  {
    final PdfTextSpec textSpec = (PdfTextSpec) getTextSpec();
    if (textSpec != null)
    {
      textSpec.finishText();
    }
    super.finishInlineBox(box);
  }
}
