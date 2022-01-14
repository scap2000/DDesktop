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
 * LogicalPageDrawable.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.graphics.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import java.net.URL;

import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.CodePointStream;
import org.jfree.fonts.encoding.manual.Utf16LE;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.ShapeElement;
import org.jfree.report.URLImageContainer;
import org.jfree.report.layout.ModelPrinter;
import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphPoolBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.RenderUtility;
import org.jfree.report.layout.process.IterateStructuralProcessStep;
import org.jfree.report.layout.text.ExtendedBaselineInfo;
import org.jfree.report.layout.text.Glyph;
import org.jfree.report.modules.output.pageable.base.RevalidateTextEllipseProcessStep;
import org.jfree.report.modules.output.pageable.graphics.PageDrawable;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.util.ShapeTransform;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;
import org.jfree.util.Log;
import org.jfree.util.WaitingImageObserver;

/**
 * The page drawable is the content provider for the Graphics2DOutputTarget. This component is responsible for rendering
 * the current page to a Graphics2D object.
 *
 * @author Thomas Morgner
 */
public class LogicalPageDrawable extends IterateStructuralProcessStep implements PageDrawable
{
  protected static class TextSpec
  {
    private boolean bold;
    private boolean italics;
    private String fontName;
    private float fontSize;
    private OutputProcessorMetaData metaData;
    private Graphics2D graphics;

    public TextSpec(final StyleSheet layoutContext,
                    final OutputProcessorMetaData metaData,
                    final Graphics2D graphics)
    {
      this.graphics = graphics;
      this.metaData = metaData;
      fontName = metaData.getNormalizedFontFamilyName
          ((String) layoutContext.getStyleProperty(TextStyleKeys.FONT));
      fontSize = (float) layoutContext.getDoubleStyleProperty(TextStyleKeys.FONTSIZE, 10);
      bold = layoutContext.getBooleanStyleProperty(TextStyleKeys.BOLD);
      italics = layoutContext.getBooleanStyleProperty(TextStyleKeys.ITALIC);
    }

    public boolean isSame(final StyleSheet layoutContext)
    {
      if (this.fontName.equals(metaData.getNormalizedFontFamilyName
          ((String) layoutContext.getStyleProperty(TextStyleKeys.FONT))) == false)
      {
        return false;
      }
      if (this.fontSize != (float) layoutContext.getDoubleStyleProperty(TextStyleKeys.FONTSIZE, 10))
      {
        return false;
      }
      if (bold != layoutContext.getBooleanStyleProperty(TextStyleKeys.BOLD))
      {
        return false;
      }
      if (italics = layoutContext.getBooleanStyleProperty(TextStyleKeys.ITALIC))
      {
        return false;
      }
      return true;
    }

    public boolean isBold()
    {
      return bold;
    }

    public boolean isItalics()
    {
      return italics;
    }

    public String getFontName()
    {
      return fontName;
    }

    public float getFontSize()
    {
      return fontSize;
    }

    public Graphics2D getGraphics()
    {
      return graphics;
    }

    public void close()
    {
      graphics.dispose();
      graphics = null;
    }
  }

  private static class FontDecorationSpec
  {
    private double end;
    private double start;
    private double verticalPosition;
    private double lineWidth;
    private Color textColor;

    protected FontDecorationSpec()
    {
      start = -1;
      end = -1;
    }

    public Color getTextColor()
    {
      return textColor;
    }

    public void setTextColor(final Color textColor)
    {
      this.textColor = textColor;
    }

    public void updateStart(final double start)
    {
      if (this.start < 0)
      {
        this.start = start;
      }
      else if (start < this.start)
      {
        this.start = start;
      }
    }

    public double getEnd()
    {
      return end;
    }

    public void updateEnd(final double end)
    {
      if (this.end < 0)
      {
        this.end = end;
      }
      else if (end > this.end)
      {
        this.end = end;
      }
    }

    public double getStart()
    {
      return start;
    }

    public double getLineWidth()
    {
      return lineWidth;
    }

    public void updateLineWidth(final double lineWidth)
    {
      if (lineWidth > this.lineWidth)
      {
        this.lineWidth = lineWidth;
      }
    }

    public void updateVerticalPosition(final double verticalPosition)
    {
      if (verticalPosition > this.verticalPosition)
      {
        this.verticalPosition = verticalPosition;
      }
    }

    public double getVerticalPosition()
    {
      return verticalPosition;
    }
  }

  private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1);


  private FontDecorationSpec strikeThrough;
  private FontDecorationSpec underline;
  private boolean outlineMode = false;
  private LogicalPageBox rootBox;
  private OutputProcessorMetaData metaData;
  private PageFormat pageFormat;
  private double width;
  private double height;
  private CodePointBuffer codePointBuffer;
  private Graphics2D graphics;
  private boolean textLineOverflow;
  private long contentAreaX1;
  private long contentAreaX2;
  private RevalidateTextEllipseProcessStep revalidateTextEllipseProcessStep;
  private StrictBounds drawArea;
  private Rectangle2D.Double boxArea;
  private TextSpec textSpec;

  public LogicalPageDrawable(final LogicalPageBox rootBox,
                             final OutputProcessorMetaData metaData)
  {
    this.boxArea = new Rectangle2D.Double();
    this.rootBox = rootBox;
    this.metaData = metaData;
    this.width = rootBox.getPageWidth() / 1000.0f;
    this.height = rootBox.getPageHeight() / 1000.0f;

    final Paper paper = new Paper();
    paper.setImageableArea(0, 0, width, height);

    this.pageFormat = new PageFormat();
    this.pageFormat.setPaper(paper);

    this.outlineMode = JFreeReportBoot.getInstance().getExtendedConfig().getBoolProperty
        ("org.jfree.report.modules.output.pageable.graphics.debug.OutlineMode");
    if (JFreeReportBoot.getInstance().getExtendedConfig().getBoolProperty
        ("org.jfree.report.modules.output.pageable.graphics.debug.PrintPageContents"))
    {
      ModelPrinter.print(rootBox);
    }

    revalidateTextEllipseProcessStep = new RevalidateTextEllipseProcessStep(metaData);
  }

  public boolean isOutlineMode()
  {
    return outlineMode;
  }


  protected StrictBounds getDrawArea()
  {
    return drawArea;
  }

  public PageFormat getPageFormat()
  {
    return (PageFormat) pageFormat.clone();
  }

  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio aware, these bounds should be used to
   * compute the preferred aspect ratio for this drawable.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize()
  {
    return new Dimension((int) width, (int) height);
  }

  public double getHeight()
  {
    return height;
  }

  public double getWidth()
  {
    return width;
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio()
  {
    return true;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(final Graphics2D g2, final Rectangle2D area)
  {
    g2.setPaint(Color.white);
    g2.fill(area);
    g2.translate(-area.getX(), -area.getY());
    //g2.clip(area);
    try
    {
      final StrictBounds pageBounds = StrictGeomUtility.createBounds(area.getX(), area.getY(), area.getWidth(), area.getHeight());
      this.drawArea = pageBounds;
      this.graphics = g2;
      if (rootBox.isNodeVisible(drawArea) == false)
      {
        return;
      }

      if (startBlockBox(rootBox))
      {
        startProcessing(rootBox.getWatermarkArea());

        final BlockRenderBox headerArea = rootBox.getHeaderArea();
        final BlockRenderBox footerArea = rootBox.getFooterArea();
        final StrictBounds headerBounds = new StrictBounds(headerArea.getX(), headerArea.getY(), headerArea.getWidth(), headerArea.getHeight());
        final StrictBounds footerBounds = new StrictBounds(footerArea.getX(), footerArea.getY(), footerArea.getWidth(), footerArea.getHeight());
        final StrictBounds contentBounds = new StrictBounds
            (rootBox.getX(), headerArea.getY() + headerArea.getHeight(),
             rootBox.getWidth(), footerArea.getY() - headerArea.getHeight());
        this.drawArea = headerBounds;
        startProcessing(headerArea);
        this.drawArea = contentBounds;
        processBoxChilds(rootBox);
        this.drawArea = footerBounds;
        startProcessing(footerArea);
        this.drawArea = pageBounds;
      }
      finishBlockBox(rootBox);
    }
    finally
    {
      this.graphics = null;
      this.drawArea = null;
    }
  }
  

  private void drawDebugBox(final Graphics2D g2, final RenderBox box)
  {
//    if (box instanceof TableCellRenderBox)
//    {
//      g2.setPaint(Color.yellow);
//    }
//    else if (box instanceof TableRowRenderBox)
//    {
//      g2.setPaint(Color.green);
//    }
//    else if (box instanceof TableSectionRenderBox)
//    {
//      g2.setPaint(Color.red);
//    }
//    else if (box instanceof TableRenderBox)
//    {
//      g2.setPaint(Color.blue);
//    }
//    else
    if (box instanceof ParagraphRenderBox)
    {
      g2.setPaint(Color.magenta);
    }
    else if (box instanceof ParagraphPoolBox)
    {
      g2.setPaint(Color.orange);
    }
    else
    {
      g2.setPaint(Color.lightGray);
    }
    final int x = (int) (box.getX() / 1000);
    final int y = (int) (box.getY() / 1000);
    final int w = (int) (box.getWidth() / 1000);
    final int h = (int) (box.getHeight() / 1000);
    g2.drawRect(x, y, w, h);
  }

  private void processLinksAndAnchors(final RenderNode box)
  {
    final StyleSheet styleSheet = box.getStyleSheet();
    final String target = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_TARGET);
    if (target != null)
    {
      final String window = (String) styleSheet.getStyleProperty(ElementStyleKeys.HREF_WINDOW);
      drawHyperlink(box, target, window);
    }

    final String anchor = (String) styleSheet.getStyleProperty(ElementStyleKeys.ANCHOR_NAME);
    if (anchor != null)
    {
      drawAnchor(box);
    }

    final String bookmark = (String) styleSheet.getStyleProperty(BandStyleKeys.BOOKMARK);
    if (bookmark != null)
    {
      drawBookmark(box, bookmark);
    }
  }

  protected void drawBookmark(final RenderNode box, final String bookmark)
  {
  }

  protected void drawHyperlink(final RenderNode box, final String target, final String window)
  {
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    if (box.isBoxVisible(drawArea) == false)
    {
      return false;
    }

    final Graphics2D g2 = getGraphics();
    if (outlineMode)
    {
      drawDebugBox(g2, box);
    }
    else if (box.getBoxDefinition().getBorder().isEmpty() == false)
    {
      final BorderRenderer borderRenderer = new BorderRenderer(box);
      borderRenderer.paintBackroundAndBorder(g2);
    }
    else
    {
      final Color backgroundColor = (Color) box.getStyleSheet().getStyleProperty(ElementStyleKeys.BACKGROUND_COLOR);
      if (backgroundColor != null)
      {
        final double x = StrictGeomUtility.toExternalValue(box.getX());
        final double y = StrictGeomUtility.toExternalValue(box.getY());
        final double w = StrictGeomUtility.toExternalValue(box.getWidth());
        final double h = StrictGeomUtility.toExternalValue(box.getHeight());
        boxArea.setFrame(x,y,w,h);
        g2.setColor(backgroundColor);
        g2.fill(boxArea);
      }
    }

    processLinksAndAnchors(box);
    return true;
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (box.isBoxVisible(drawArea) == false)
    {
      return false;
    }

    final Graphics2D g2 = getGraphics();
    if (outlineMode)
    {
      drawDebugBox(g2, box);
    }
    else if (box.getBoxDefinition().getBorder().isEmpty() == false)
    {
      final BorderRenderer borderRenderer = new BorderRenderer(box);
      borderRenderer.paintBackroundAndBorder(g2);
    }
    else
    {
      final Color backgroundColor = (Color) box.getStyleSheet().getStyleProperty(ElementStyleKeys.BACKGROUND_COLOR);
      if (backgroundColor != null)
      {
        final double x = StrictGeomUtility.toExternalValue(box.getX());
        final double y = StrictGeomUtility.toExternalValue(box.getY());
        final double w = StrictGeomUtility.toExternalValue(box.getWidth());
        final double h = StrictGeomUtility.toExternalValue(box.getHeight());
        boxArea.setFrame(x,y,w,h);
        g2.setColor(backgroundColor);
        g2.fill(boxArea);
      }
    }

    processLinksAndAnchors(box);
    return true;
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    if (box.isBoxVisible(drawArea) == false)
    {
      return false;
    }

    final Graphics2D g2 = getGraphics();
    if (outlineMode)
    {
      drawDebugBox(g2, box);
    }
    else if (box.getBoxDefinition().getBorder().isEmpty() == false)
    {
      final BorderRenderer borderRenderer = new BorderRenderer(box);
      borderRenderer.paintBackroundAndBorder(g2);
    }
    else
    {
      final Color backgroundColor = (Color) box.getStyleSheet().getStyleProperty(ElementStyleKeys.BACKGROUND_COLOR);
      if (backgroundColor != null)
      {
        final double x = StrictGeomUtility.toExternalValue(box.getX());
        final double y = StrictGeomUtility.toExternalValue(box.getY());
        final double w = StrictGeomUtility.toExternalValue(box.getWidth());
        final double h = StrictGeomUtility.toExternalValue(box.getHeight());
        boxArea.setFrame(x,y,w,h);
        g2.setColor(backgroundColor);
        g2.fill(boxArea);
      }
    }

    if (textSpec != null)
    {
      textSpec.close();
      textSpec = null;
    }

    final FontDecorationSpec newUnderlineSpec = computeUnderline(box, underline);
    if (underline != null && newUnderlineSpec == null)
    {
      drawTextDecoration(underline);
      underline = null;
    }
    else
    {
      underline = newUnderlineSpec;
    }

    final FontDecorationSpec newStrikeThroughSpec = computeStrikeThrough(box, strikeThrough);
    if (strikeThrough != null && newStrikeThroughSpec == null)
    {
      drawTextDecoration(strikeThrough);
      strikeThrough = null;
    }
    else
    {
      strikeThrough = newStrikeThroughSpec;
    }

    processLinksAndAnchors(box);
    return true;
  }


  protected TextSpec getTextSpec()
  {
    return textSpec;
  }

  protected void setTextSpec(final TextSpec textSpec)
  {
    this.textSpec = textSpec;
  }

  private FontDecorationSpec computeUnderline(final RenderBox box, FontDecorationSpec oldSpec)
  {
    final StyleSheet styleSheet = box.getStyleSheet();
    if (styleSheet.getBooleanStyleProperty(TextStyleKeys.UNDERLINED) == false)
    {
      return null;
    }
    if (oldSpec == null)
    {
      oldSpec = new FontDecorationSpec();
    }
    final double size = box.getStyleSheet().getDoubleStyleProperty(TextStyleKeys.FONTSIZE, 0);
    final double lineWidth = Math.max (1, size / 20.0);
    oldSpec.updateLineWidth(lineWidth);
    oldSpec.setTextColor((Color) box.getStyleSheet().getStyleProperty(ElementStyleKeys.PAINT));
    return oldSpec;
  }

  private FontDecorationSpec computeStrikeThrough(final RenderBox box, FontDecorationSpec oldSpec)
  {
    final StyleSheet styleSheet = box.getStyleSheet();
    if (styleSheet.getBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH) == false)
    {
      return null;
    }
    if (oldSpec == null)
    {
      oldSpec = new FontDecorationSpec();
    }

    final double size = box.getStyleSheet().getDoubleStyleProperty(TextStyleKeys.FONTSIZE, 0);
    final double lineWidth = Math.max (1, size / 20.0);
    oldSpec.updateLineWidth(lineWidth);
    oldSpec.setTextColor((Color) box.getStyleSheet().getStyleProperty(ElementStyleKeys.PAINT));
    return oldSpec;
  }

  private boolean isStyleActive(final StyleKey key, final RenderBox parent)
  {
    if (parent instanceof InlineRenderBox == false)
    {
      return false;
    }
    return parent.getStyleSheet().getBooleanStyleProperty(key);
  }

  protected void finishInlineBox(final InlineRenderBox box)
  {
    final RenderBox parent = box.getParent();
    if (underline != null)
    {
      if (isStyleActive(TextStyleKeys.UNDERLINED, parent) == false)
      {
        // The parent has no underline style, but this box has. So finish up the underline.
        drawTextDecoration(underline);
        underline = null;
      }
    }
    else
    {
      // maybe this inlinebox has no underline, but the parent has ...
      underline = computeUnderline(box, null);
    }

    if (strikeThrough != null)
    {
      if (isStyleActive(TextStyleKeys.STRIKETHROUGH, parent) == false)
      {
        // The parent has no underline style, but this box has. So finish up the underline.
        drawTextDecoration(strikeThrough);
        strikeThrough = null;
      }
    }
    else
    {
      underline = computeUnderline(box, null);
    }

    if (textSpec != null)
    {
      textSpec.close();
      textSpec = null;
    }
  }

  private void drawTextDecoration(final FontDecorationSpec decorationSpec)
  {
    final Graphics2D graphics = (Graphics2D) this.graphics.create();
    graphics.setColor(decorationSpec.getTextColor());
    graphics.setStroke(new BasicStroke((float) decorationSpec.getLineWidth()));
    graphics.draw(new Line2D.Double(decorationSpec.getStart(), decorationSpec.getVerticalPosition(),
        decorationSpec.getEnd(), decorationSpec.getVerticalPosition()));
    graphics.dispose();
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
    if (lineBox.isNodeVisible(drawArea) == false)
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

    underline = null;
    strikeThrough = null;

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


  protected void processOtherNode(final RenderNode node)
  {
    if (node.isNodeVisible(drawArea) == false)
    {
      return;
    }

    if (node instanceof RenderableReplacedContent)
    {
      drawReplacedContent((RenderableReplacedContent) node);
    }
    else if (node instanceof RenderableText)
    {
      final RenderableText text = (RenderableText) node;
      if (underline != null)
      {
        final ExtendedBaselineInfo baselineInfo = text.getBaselineInfo();
        final long underlinePos = text.getY() + baselineInfo.getUnderlinePosition();
        underline.updateVerticalPosition(StrictGeomUtility.toExternalValue(underlinePos));
        underline.updateStart(StrictGeomUtility.toExternalValue(text.getX()));
        underline.updateEnd(StrictGeomUtility.toExternalValue(text.getX() + text.getWidth()));
      }

      if (strikeThrough != null)
      {
        final ExtendedBaselineInfo baselineInfo = text.getBaselineInfo();
        final long strikethroughPos = text.getY() + baselineInfo.getStrikethroughPosition();
        strikeThrough.updateVerticalPosition(StrictGeomUtility.toExternalValue(strikethroughPos));
        strikeThrough.updateStart(StrictGeomUtility.toExternalValue(text.getX()));
        strikeThrough.updateEnd(StrictGeomUtility.toExternalValue(text.getX() + text.getWidth()));
      }

      if (isTextLineOverflow())
      {
        final long ellipseSize = extractEllipseSize(node);
        final long x1 = text.getX();
        final long x2 = x1 + text.getWidth();
        final long effectiveAreaX2 = (contentAreaX2 - ellipseSize);
        if (x2 <= effectiveAreaX2)
        {
          // the text will be fully visible.
          drawText(text);
        }
        else if (x1 >= contentAreaX2)
        {
          // Skip, the node will not be visible.
        }
        else
        {
          // The text node that is printed will overlap with the ellipse we need to print.
          drawText(text, effectiveAreaX2);
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
        drawText(text);
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

  private void drawReplacedContent(final RenderableReplacedContent content)
  {
    final Graphics2D g2 = getGraphics();
    processLinksAndAnchors(content);

    final Object o = content.getRawObject();
    if (o instanceof Image)
    {
      drawImage(content, (Image) o);
    }
    else if (o instanceof Drawable)
    {
      final Drawable d = (Drawable) o;
      drawDrawable(content, g2, d);
    }
    else if (o instanceof Shape)
    {
      final Shape unscaledShape = (Shape) o;
      drawShape(content, unscaledShape, g2);
    }
    else if (o instanceof LocalImageContainer)
    {
      final LocalImageContainer imageContainer = (LocalImageContainer) o;
      final Image image = imageContainer.getImage();
      drawImage(content, image);
    }
    else if (o instanceof URLImageContainer)
    {
      final URLImageContainer imageContainer = (URLImageContainer) o;
      if (imageContainer.isLoadable() == false)
      {
        Log.info("URL-image cannot be rendered, as it was declared to be not loadable.");
        return;
      }

      final URL sourceURL = imageContainer.getSourceURL();
      if (sourceURL == null)
      {
        Log.info("URL-image cannot be rendered, as it did not return a valid URL.");
      }
    }
    else
    {
      Log.debug("Unable to handle " + o);
    }
  }

  /**
   * To be overriden in the PDF drawable.
   *
   * @param content the render-node that defines the anchor.
   */
  protected void drawAnchor(final RenderNode content)
  {

  }

  /**
   *
   * @param content
   * @param image
   */
  private void drawImage(final RenderableReplacedContent content, Image image)
  {

    final StyleSheet layoutContext = content.getStyleSheet();
    final boolean shouldScale = layoutContext.getBooleanStyleProperty(ElementStyleKeys.SCALE);

    final int x = (int) StrictGeomUtility.toExternalValue(content.getX());
    final int y = (int) StrictGeomUtility.toExternalValue(content.getY());
    final int width = (int) StrictGeomUtility.toExternalValue(content.getWidth());
    final int height = (int) StrictGeomUtility.toExternalValue(content.getHeight());

    if (width == 0 || height == 0)
    {
      Log.debug("Error: Image area is empty: " + content);
      return;
    }

    WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();
    final int imageWidth = image.getWidth(obs);
    final int imageHeight = image.getHeight(obs);
    if (imageWidth < 1 || imageHeight < 1)
    {
      return;
    }

    final double devResolution = metaData.getNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION);
    final double scale;
    if (metaData.isFeatureSupported(OutputProcessorFeature.IMAGE_RESOLUTION_MAPPING) &&
        devResolution > 0)
    {
      scale = devResolution / 72.0;
    }
    else
    {
      scale = 1;
    }

    final Rectangle2D.Double drawAreaBounds = new Rectangle2D.Double
        (StrictGeomUtility.toExternalValue(drawArea.getX()), StrictGeomUtility.toExternalValue(drawArea.getY()),
         StrictGeomUtility.toExternalValue(drawArea.getWidth()), StrictGeomUtility.toExternalValue(drawArea.getHeight()));

    final Graphics2D g2;
    if (shouldScale == false && scale == 1)
    {
      final int clipWidth = Math.min(width, imageWidth);
      final int clipHeight = Math.min(height, imageHeight);
      final ElementAlignment horizontalAlignment =
          (ElementAlignment) layoutContext.getStyleProperty(ElementStyleKeys.ALIGNMENT);
      final ElementAlignment verticalAlignment =
          (ElementAlignment) layoutContext.getStyleProperty(ElementStyleKeys.VALIGNMENT);
      final int alignmentX = (int) RenderUtility.computeHorizontalAlignment(horizontalAlignment, width, clipWidth);
      final int alignmentY = (int) RenderUtility.computeVerticalAlignment(verticalAlignment, height, clipHeight);

      g2 = (Graphics2D) getGraphics().create();
      g2.clip(drawAreaBounds);
      g2.translate(x, y);
      g2.translate(alignmentX, alignmentY);
      g2.clip(new Rectangle2D.Float(0, 0, clipWidth, clipHeight));
    }
    else
    {
      g2 = (Graphics2D) getGraphics().create();
      g2.clip(drawAreaBounds);
      g2.translate(x, y);
      g2.clip(new Rectangle2D.Float(0, 0, width, height));

      if (devResolution != 72.0 && devResolution > 0)
      {
        // Need to scale the device to its native resolution before attempting to draw the image..
        final double deviceScaleFactor = (72.0/devResolution);
        g2.scale(deviceScaleFactor, deviceScaleFactor);
      }

      final double scaleX;
      final double scaleY;
      if (shouldScale)
      {
        final boolean keepAspectRatio = layoutContext.getBooleanStyleProperty(ElementStyleKeys.KEEP_ASPECT_RATIO);
        if (keepAspectRatio)
        {
          scaleX = scale * Math.min(width / (double) imageWidth, height / (double) imageHeight);
          //noinspection SuspiciousNameCombination
          scaleY = scaleX;
        }
        else
        {
          scaleX = scale * width / (double) imageWidth;
          scaleY = scale * height / (double) imageHeight;
        }
      }
      else
      {
        scaleX = scale;
        scaleY = scale;
      }

      final int clipWidth = (int) (scaleX * imageWidth);
      final int clipHeight = (int) (scaleY * imageHeight);

      final ElementAlignment horizontalAlignment =
          (ElementAlignment) layoutContext.getStyleProperty(ElementStyleKeys.ALIGNMENT);
      final ElementAlignment verticalAlignment =
          (ElementAlignment) layoutContext.getStyleProperty(ElementStyleKeys.VALIGNMENT);
      final int alignmentX = (int) RenderUtility.computeHorizontalAlignment(horizontalAlignment, width, clipWidth);
      final int alignmentY = (int) RenderUtility.computeVerticalAlignment(verticalAlignment, height, clipHeight);

      g2.translate(alignmentX, alignmentY);
      // g2.scale(scaleX, scaleY);

      final Object contentCached = content.getContentCached();
      if (contentCached instanceof Image)
      {
        image = (Image) contentCached;
      }
      else
      {
        image = RenderUtility.scaleImage(image, clipWidth, clipHeight, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
        content.setContentCached(image);
        obs = new WaitingImageObserver(image);
        obs.waitImageLoaded();
      }
    }

    while (g2.drawImage(image, null, obs) == false)
    {
      obs.waitImageLoaded();
      if (obs.isError())
      {
        Log.warn("Error while loading the image during the rendering.");
        break;
      }
    }

    g2.dispose();
  }


  private void drawDrawable(final RenderableReplacedContent content, final Graphics2D g2, final Drawable d)
  {
    final double x = StrictGeomUtility.toExternalValue(content.getX());
    final double y = StrictGeomUtility.toExternalValue(content.getY());
    final double width = StrictGeomUtility.toExternalValue(content.getWidth());
    final double height = StrictGeomUtility.toExternalValue(content.getHeight());

    if (width <= 0 || height <= 0)
    {
      return;
    }

    final Graphics2D clone = (Graphics2D) g2.create();
    final Rectangle2D.Double drawAreaBounds = new Rectangle2D.Double
        (StrictGeomUtility.toExternalValue(drawArea.getX()), StrictGeomUtility.toExternalValue(drawArea.getY()),
         StrictGeomUtility.toExternalValue(drawArea.getWidth()), StrictGeomUtility.toExternalValue(drawArea.getHeight()));

    clone.clip(drawAreaBounds);
    clone.translate(x, y);
    final Rectangle2D.Double bounds = new Rectangle2D.Double(0, 0, width, height);
    clone.clip(bounds);

    final StyleSheet layoutContext = content.getStyleSheet();
    configureGraphics(layoutContext, clone);
    configureStroke(layoutContext, clone);
    d.draw(clone, bounds);
    clone.dispose();
  }

  private void drawShape(final RenderableReplacedContent content, final Shape unscaledShape, final Graphics2D g2)
  {
    final StyleSheet layoutContext = content.getStyleSheet();
    final boolean shouldDraw = layoutContext.getBooleanStyleProperty(ShapeElement.DRAW_SHAPE);
    final boolean shouldFill = layoutContext.getBooleanStyleProperty(ShapeElement.FILL_SHAPE);
    if (shouldFill == false && shouldDraw == false)
    {
      return;
    }
    final boolean scale = layoutContext.getBooleanStyleProperty(ElementStyleKeys.SCALE);
    final boolean keepAspectRatio = layoutContext.getBooleanStyleProperty(ElementStyleKeys.KEEP_ASPECT_RATIO);
    final double x = StrictGeomUtility.toExternalValue(content.getX());
    final double y = StrictGeomUtility.toExternalValue(content.getY());
    final double width = StrictGeomUtility.toExternalValue(content.getWidth());
    final double height = StrictGeomUtility.toExternalValue(content.getHeight());

    final Shape scaledShape = ShapeTransform.transformShape(unscaledShape, scale, keepAspectRatio, width, height);
    final Graphics2D clone = (Graphics2D) g2.create();
    clone.translate(x, y);
    configureGraphics(layoutContext, clone);
    configureStroke(layoutContext, clone);
    if (shouldDraw)
    {
      clone.draw(scaledShape);
    }
    if (shouldFill)
    {
      clone.fill(scaledShape);
    }
    clone.dispose();
  }

  protected void drawText(final RenderableText renderableText)
  {
    drawText(renderableText, renderableText.getX() + renderableText.getWidth());
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
    if (renderableText.isNodeVisible(drawArea) == false)
    {
      return;
    }
    final long posX = renderableText.getX();
    final long posY = renderableText.getY();

    final Graphics2D g2;
    if (textSpec == null)
    {
      g2 = (Graphics2D) getGraphics().create();
      final StyleSheet layoutContext = renderableText.getStyleSheet();
      configureGraphics(layoutContext, g2);
      g2.setStroke(DEFAULT_STROKE);

      if (RenderUtility.isFontSmooth(layoutContext, metaData))
      {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      }
      else
      {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      }
    }
    else
    {
      g2 = textSpec.getGraphics();
    }

    // This shifting is necessary to make sure that all text is rendered like in the previous versions.
    // In the earlier versions, we did not really obey to the baselines of the text, we just hoped and prayed.
    // Therefore, all text was printed at the bottom of the text elements. With the introduction of the full
    // font metrics setting, this situation got a little bit better, for the price that text-elements became
    // nearly unpredictable ..
    //
    // The code below may be weird, but at least it is predictable weird.
    final ExtendedBaselineInfo baselineInfo = renderableText.getBaselineInfo();
    final long baseline = baselineInfo.getBaseline(baselineInfo.getDominantBaseline());

    final FontMetrics fm = g2.getFontMetrics();
    final Rectangle2D rect = fm.getMaxCharBounds(g2);
    final long awtBaseLine = StrictGeomUtility.toInternalValue(-rect.getY());

    final Glyph[] gs = renderableText.getGlyphs();
    if (metaData.isFeatureSupported(OutputProcessorFeature.FAST_FONTRENDERING) &&
        isNormalTextSpacing(renderableText))
    {
      final int maxLength = computeMaximumTextSize(renderableText, contentX2);
      final String text = textToString(gs, renderableText.getOffset(), maxLength);
      final float y = (posY + awtBaseLine) / 1000.0f;
      g2.drawString(text, posX / 1000.0f, y);
    }
    else
    {
      final int maxPos = renderableText.getOffset() + computeMaximumTextSize(renderableText, contentX2);
      long runningPos = posX;
      final long baselineDelta = awtBaseLine - baseline;
      for (int i = renderableText.getOffset(); i < maxPos; i++)
      {
        final Glyph g = gs[i];
        final float y = (posY + g.getBaseLine() + baselineDelta) / 1000.0f;
        g2.drawString(glpyhToString(g), runningPos / 1000.0f, y);
        runningPos += g.getWidth() + g.getSpacing().getMinimum();
      }
    }
    g2.dispose();
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

  protected boolean isNormalTextSpacing(final RenderableText text)
  {
    return text.isNormalTextSpacing();
//    final Glyph[] glyphs = text.getGlyphs();
//    for (int i = 0; i < glyphs.length; i++)
//    {
//      final Glyph glyph = glyphs[i];
//      if (Spacing.EMPTY_SPACING.equals(glyph.getSpacing()) == false)
//      {
//        return false;
//      }
//    }
//    return true;
  }

  protected void configureStroke(final StyleSheet layoutContext, final Graphics2D g2)
  {
    final Stroke styleProperty = (Stroke) layoutContext.getStyleProperty(ElementStyleKeys.STROKE);
    if (styleProperty != null)
    {
      g2.setStroke(styleProperty);
    }
    else
    {
      // Apply a default one ..
      g2.setStroke(DEFAULT_STROKE);
    }
  }

  protected void configureGraphics(final StyleSheet layoutContext, final Graphics2D g2)
  {
    final boolean bold = layoutContext.getBooleanStyleProperty(TextStyleKeys.BOLD);
    final boolean italics = layoutContext.getBooleanStyleProperty(TextStyleKeys.ITALIC);

    int style = Font.PLAIN;
    if (bold)
    {
      style |= Font.BOLD;
    }
    if (italics)
    {
      style |= Font.ITALIC;
    }

    // todo: What about the 'EXT_PAINT' property? Do we need it?
    final Color cssColor = (Color) layoutContext.getStyleProperty(ElementStyleKeys.PAINT);
    g2.setColor(cssColor);

    final int fontSize = layoutContext.getIntStyleProperty(TextStyleKeys.FONTSIZE,
        (int) metaData.getNumericFeatureValue(OutputProcessorFeature.DEFAULT_FONT_SIZE));

    final String fontName = metaData.getNormalizedFontFamilyName
        ((String) layoutContext.getStyleProperty(TextStyleKeys.FONT));
    g2.setFont(new Font(fontName, style, fontSize));
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

  /**
   * This is a rather dirty hack to convert a code-point sequence into a char sequence. We should probably use the
   * LibFonts CharBuffer later ..
   *
   * @param g the glyph
   * @return the string for this glyph
   */
  public String glpyhToString(final Glyph g)
  {
    if (codePointBuffer == null)
    {
      final int[] extraChars = g.getExtraChars();
      final int bufferSize = 1 + extraChars.length;
      codePointBuffer = new CodePointBuffer(bufferSize);
      final int[] data = codePointBuffer.getData();
      data[0] = g.getCodepoint();
      if (bufferSize > 1)
      {
        System.arraycopy(extraChars, 0, data, 1, extraChars.length);
      }
      codePointBuffer.setData(data, bufferSize, 0);
      return Utf16LE.getInstance().encodeString(codePointBuffer);
    }

    codePointBuffer.setCursor(0);

    //noinspection SynchronizeOnNonFinalField
    synchronized (codePointBuffer)
    {
      final int[] extraChars = g.getExtraChars();
      final int bufferSize = 1 + extraChars.length;
      codePointBuffer.ensureSize(bufferSize);
      final int[] data = codePointBuffer.getData();
      data[0] = g.getCodepoint();
      if (bufferSize > 1)
      {
        System.arraycopy(extraChars, 0, data, 1, extraChars.length);
      }
      codePointBuffer.setData(data, bufferSize, 0);
      return Utf16LE.getInstance().encodeString(codePointBuffer);
    }
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public Graphics2D getGraphics()
  {
    return graphics;
  }

}
