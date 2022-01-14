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
 * DefaultRenderableTextFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.text;

import java.util.ArrayList;

import org.jfree.fonts.registry.BaselineInfo;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.text.ClassificationProducer;
import org.jfree.fonts.text.DefaultLanguageClassifier;
import org.jfree.fonts.text.GraphemeClusterProducer;
import org.jfree.fonts.text.LanguageClassifier;
import org.jfree.fonts.text.Spacing;
import org.jfree.fonts.text.SpacingProducer;
import org.jfree.fonts.text.StaticSpacingProducer;
import org.jfree.fonts.text.breaks.BreakOpportunityProducer;
import org.jfree.fonts.text.breaks.LineBreakProducer;
import org.jfree.fonts.text.breaks.WordBreakProducer;
import org.jfree.fonts.text.classifier.GlyphClassificationProducer;
import org.jfree.fonts.text.classifier.WhitespaceClassificationProducer;
import org.jfree.fonts.text.font.FontSizeProducer;
import org.jfree.fonts.text.font.GlyphMetrics;
import org.jfree.fonts.text.font.KerningProducer;
import org.jfree.fonts.text.font.NoKerningProducer;
import org.jfree.fonts.text.font.VariableFontSizeProducer;
import org.jfree.fonts.text.whitespace.CollapseWhiteSpaceFilter;
import org.jfree.fonts.text.whitespace.DiscardWhiteSpaceFilter;
import org.jfree.fonts.text.whitespace.PreserveBreaksWhiteSpaceFilter;
import org.jfree.fonts.text.whitespace.PreserveWhiteSpaceFilter;
import org.jfree.fonts.text.whitespace.WhiteSpaceFilter;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.model.SpacerRenderNode;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.style.TextWrap;
import org.jfree.report.style.WhitespaceCollapse;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 03.04.2007, 16:43:48
 *
 * @author Thomas Morgner
 */
public class DefaultRenderableTextFactory implements RenderableTextFactory
{
  private static final int[] EMPTY_EXTRA_CHARS = new int[0];
  private static final RenderNode[] EMPTY_RENDER_NODE = new RenderNode[0];
  private static final RenderableText[] EMPTY_TEXT = new RenderableText[0];
  private static final Glyph[] EMPTY_GLYPHS = new Glyph[0];
  private static final int[] END_OF_TEXT = new int[]{ClassificationProducer.END_OF_TEXT};

  private GraphemeClusterProducer clusterProducer;
  private boolean startText;
  private boolean produced;
  private FontSizeProducer fontSizeProducer;
  private KerningProducer kerningProducer;
  private SpacingProducer spacingProducer;
  private BreakOpportunityProducer breakOpportunityProducer;
  private WhiteSpaceFilter whitespaceFilter;
  private GlyphClassificationProducer classificationProducer;
  private StyleSheet layoutContext;
  private LanguageClassifier languageClassifier;

  private transient GlyphMetrics dims;

  private ArrayList words;
  private ArrayList glyphList;
  private long leadingMargin;
  private int lastLanguage;

  // todo: This is part of a cheap hack.
  private transient FontMetrics fontMetrics;
  private OutputProcessorMetaData metaData;
  private BaselineInfo baselineInfo;

  // cached instance ..
  private NoKerningProducer noKerningProducer;

  private WhitespaceCollapse whitespaceFilterValue;
  private WhitespaceCollapse whitespaceCollapseValue;
  private TextWrap breakOpportunityValue;
  private long wordSpacing;

  public DefaultRenderableTextFactory(final OutputProcessorMetaData metaData)
  {
    this.metaData = metaData;
    this.clusterProducer = new GraphemeClusterProducer();
    this.languageClassifier = new DefaultLanguageClassifier();
    this.startText = true;
    this.words = new ArrayList();
    this.glyphList = new ArrayList();
    this.dims = new GlyphMetrics();
    this.baselineInfo = new BaselineInfo();
    this.noKerningProducer = new NoKerningProducer();
  }

  public RenderNode[] createText(final int[] text,
                                 final int offset,
                                 final int length,
                                 final StyleSheet layoutContext)
  {
    if (layoutContext == null)
    {
      throw new NullPointerException();
    }

    fontMetrics = metaData.getFontMetrics(layoutContext);

    kerningProducer = createKerningProducer(layoutContext);
    fontSizeProducer = createFontSizeProducer(layoutContext);
    spacingProducer = createSpacingProducer(layoutContext);
    breakOpportunityProducer = createBreakProducer(layoutContext);
    whitespaceFilter = createWhitespaceFilter(layoutContext);
    classificationProducer = createGlyphClassifier(layoutContext);
    this.layoutContext = layoutContext;
    
    if (metaData.isFeatureSupported(OutputProcessorFeature.SPACING_SUPPORTED))
    {
      this.wordSpacing = StrictGeomUtility.toInternalValue
          (layoutContext.getDoubleStyleProperty(TextStyleKeys.WORD_SPACING, 0));
    }
    else
    {
      this.wordSpacing = 0;
    }
    
    if (startText)
    {
      whitespaceFilter.filter(ClassificationProducer.START_OF_TEXT);
      breakOpportunityProducer.createBreakOpportunity(ClassificationProducer.START_OF_TEXT);
      kerningProducer.getKerning(ClassificationProducer.START_OF_TEXT);
      startText = false;
      produced = false;
    }

    return processText(text, offset, length);
  }

  protected RenderNode[] processText(final int[] text,
                                     final int offset,
                                     final int length)
  {
    int clusterStartIdx = -1;
    final int maxLen = Math.min(length + offset, text.length);
    for (int i = offset; i < maxLen; i++)
    {
      final int codePoint = text[i];
      final boolean clusterStarted =
          this.clusterProducer.createGraphemeCluster(codePoint);
      // ignore the first cluster start; we need to see the whole cluster.
      if (clusterStarted)
      {
        if (i > offset)
        {
          final int extraCharLength = i - clusterStartIdx - 1;
          if (extraCharLength > 0)
          {
            final int[] extraChars = new int[extraCharLength];
            System.arraycopy(text, clusterStartIdx + 1, extraChars, 0, extraChars.length);
            addGlyph(text[clusterStartIdx], extraChars);
          }
          else
          {
            addGlyph(text[clusterStartIdx], EMPTY_EXTRA_CHARS);
          }
        }

        clusterStartIdx = i;
      }
    }

    // Process the last cluster ...
    if (clusterStartIdx >= offset)
    {
      final int extraCharLength = maxLen - clusterStartIdx - 1;
      if (extraCharLength > 0)
      {
        final int[] extraChars = new int[extraCharLength];
        System.arraycopy(text, clusterStartIdx + 1, extraChars, 0, extraChars.length);
        addGlyph(text[clusterStartIdx], extraChars);
      }
      else
      {
        addGlyph(text[clusterStartIdx], EMPTY_EXTRA_CHARS);
      }
    }

    if (words.isEmpty() == false)
    {
      final RenderNode[] renderableTexts = (RenderNode[]) words.toArray(new RenderNode[words.size()]);
      words.clear();
      produced = true;
      return renderableTexts;
    }
    else
    {
      // we did not produce any text.
      return EMPTY_RENDER_NODE;
    }
  }

  protected void addGlyph(final int rawCodePoint, final int[] extraChars)
  {
    //  Log.debug ("Processing " + rawCodePoint);

    if (rawCodePoint == ClassificationProducer.END_OF_TEXT)
    {
      whitespaceFilter.filter(rawCodePoint);
      classificationProducer.getClassification(rawCodePoint);
      kerningProducer.getKerning(rawCodePoint);
      breakOpportunityProducer.createBreakOpportunity(rawCodePoint);
      spacingProducer.createSpacing(rawCodePoint);
      fontSizeProducer.getCharacterSize(rawCodePoint, dims);

      if (leadingMargin != 0 || glyphList.isEmpty() == false)
      {
        addWord(false);
      }
      else
      {
        // finish up ..
        glyphList.clear();
        leadingMargin = 0;
      }
      return;
    }

    int codePoint = whitespaceFilter.filter(rawCodePoint);
    final boolean stripWhitespaces;

    // No matter whether we will ignore the result, we have to keep our
    // factories up and running. These beasts need to see all data, no
    // matter what get printed later.
    if (codePoint == WhiteSpaceFilter.STRIP_WHITESPACE)
    {
      // if we dont have extra-chars, ignore the thing ..
      if (extraChars.length == 0)
      {
        stripWhitespaces = true;
      }
      else
      {
        // convert it into a space. This might be invalid, but will work for now.
        codePoint = ' ';
        stripWhitespaces = false;
      }
    }
    else
    {
      stripWhitespaces = false;
    }

    int glyphClassification = classificationProducer.getClassification(codePoint);
    final long kerning = kerningProducer.getKerning(codePoint);
    int breakweight = breakOpportunityProducer.createBreakOpportunity(codePoint);
    final Spacing spacing = spacingProducer.createSpacing(codePoint);
    dims = fontSizeProducer.getCharacterSize(codePoint, dims);
    int width = dims.getWidth();
    int height = dims.getHeight();
    lastLanguage = languageClassifier.getScript(codePoint);

    for (int i = 0; i < extraChars.length; i++)
    {
      final int extraChar = extraChars[i];
      dims = fontSizeProducer.getCharacterSize(extraChar, dims);
      width = Math.max(width, (dims.getWidth() & 0x7FFFFFFF));
      height = Math.max(height, (dims.getHeight() & 0x7FFFFFFF));
      breakweight = breakOpportunityProducer.createBreakOpportunity(extraChar);
      glyphClassification = classificationProducer.getClassification(extraChar);
    }

    if (stripWhitespaces)
    {
      //  Log.debug ("Stripping whitespaces");
      return;
    }

    if (Glyph.SPACE_CHAR == glyphClassification && isWordBreak(breakweight))
    {

      // Finish the current word ...
      final boolean forceLinebreak = breakweight == BreakOpportunityProducer.BREAK_LINE;
      if (glyphList.isEmpty() == false || forceLinebreak)
      {
        addWord(forceLinebreak);
      }

      // This character can be stripped. We increase the leading margin of the
      // next word by the character's width.
      leadingMargin += width + wordSpacing;
      //   Log.debug ("Increasing Margin");
      return;
    }

    final Glyph glyph = new Glyph(codePoint, breakweight, glyphClassification, spacing, width, height,
        dims.getBaselinePosition(), (int) kerning, extraChars);
    glyphList.add(glyph);
    // Log.debug ("Adding Glyph");

    // does this finish a word? Check it!
    if (isWordBreak(breakweight))
    {
      final boolean forceLinebreak = breakweight == BreakOpportunityProducer.BREAK_LINE;
      addWord(forceLinebreak);
    }
  }

  protected void addWord(final boolean forceLinebreak)
  {
    if (glyphList.isEmpty())
    {
      // create an trailing margin element. This way, it can collapse with
      // the next element.
      if (forceLinebreak)
      {
        final ExtendedBaselineInfo info = TextUtility.createBaselineInfo('\n', fontMetrics, baselineInfo);
        final RenderableText text = new RenderableText(layoutContext,
            info, EMPTY_GLYPHS, 0, 0, lastLanguage, true);
        words.add(text);
      }
      else if (produced == true)
      {
        final SpacerRenderNode spacer = new SpacerRenderNode(leadingMargin, 0, true);
        words.add(spacer);
      }
    }
    else
    {
      // ok, it does.
      final Glyph[] glyphs = (Glyph[]) glyphList.toArray(new Glyph[glyphList.size()]);
      if (leadingMargin > 0)// && words.isEmpty() == false)
      {
        final SpacerRenderNode spacer = new SpacerRenderNode(leadingMargin, 0, true);
        words.add(spacer);
      }

      // todo: this is cheating ..
      final int codePoint = glyphs[0].getCodepoint();

      final ExtendedBaselineInfo baselineInfo = TextUtility.createBaselineInfo(codePoint, fontMetrics, this.baselineInfo);
      final RenderableText text = new RenderableText(layoutContext,
          baselineInfo, glyphs, 0, glyphs.length, lastLanguage, forceLinebreak);
      words.add(text);
      glyphList.clear();
    }
    leadingMargin = 0;
  }

  private boolean isWordBreak(final int breakOp)
  {
    if (BreakOpportunityProducer.BREAK_WORD == breakOp ||
        BreakOpportunityProducer.BREAK_LINE == breakOp)
    {
      return true;
    }
    return false;
  }

  protected WhiteSpaceFilter createWhitespaceFilter(final StyleSheet layoutContext)
  {
    final WhitespaceCollapse wsColl = (WhitespaceCollapse) layoutContext.getStyleProperty(TextStyleKeys.WHITE_SPACE_COLLAPSE);

    if (whitespaceFilter != null)
    {
      if (ObjectUtilities.equal(whitespaceFilterValue, wsColl))
      {
        whitespaceFilter.reset();
        return whitespaceFilter;
      }
    }

    whitespaceFilterValue = wsColl;

    if (WhitespaceCollapse.DISCARD.equals(wsColl))
    {
      return new DiscardWhiteSpaceFilter();
    }
    if (WhitespaceCollapse.PRESERVE.equals(wsColl))
    {
      return new PreserveWhiteSpaceFilter();
    }
    if (WhitespaceCollapse.PRESERVE_BREAKS.equals(wsColl))
    {
      return new PreserveBreaksWhiteSpaceFilter();
    }
    return new CollapseWhiteSpaceFilter();
  }

  protected GlyphClassificationProducer createGlyphClassifier(final StyleSheet layoutContext)
  {
    final WhitespaceCollapse wsColl = (WhitespaceCollapse) layoutContext.getStyleProperty(TextStyleKeys.WHITE_SPACE_COLLAPSE);
    if (classificationProducer != null)
    {
      if (ObjectUtilities.equal(whitespaceCollapseValue, wsColl))
      {
        classificationProducer.reset();
        return classificationProducer;
      }
    }

    whitespaceCollapseValue = wsColl;

//    if (WhitespaceCollapse.PRESERVE_BREAKS.equals(wsColl))
//    {
//      return new LinebreakClassificationProducer();
//    }
    classificationProducer = new WhitespaceClassificationProducer();
    return classificationProducer;
  }

  protected BreakOpportunityProducer createBreakProducer
      (final StyleSheet layoutContext)
  {
    final TextWrap wordBreak = (TextWrap) layoutContext.getStyleProperty(TextStyleKeys.TEXT_WRAP);
    if (breakOpportunityProducer != null)
    {
      if (ObjectUtilities.equal(breakOpportunityValue, wordBreak))
      {
        breakOpportunityProducer.reset();
        return breakOpportunityProducer;
      }
    }

    breakOpportunityValue = wordBreak;

    if (TextWrap.NONE.equals(wordBreak))
    {
      // surpress all but the linebreaks. This equals the 'pre' mode of HTML
      breakOpportunityProducer = new LineBreakProducer();
    }
    else
    {
      // allow other breaks as well. The wordbreak producer does not perform
      // advanced break-detection (like syllable based breaks).
      breakOpportunityProducer = new WordBreakProducer();
    }
    return breakOpportunityProducer;
  }

  protected SpacingProducer createSpacingProducer
      (final StyleSheet layoutContext)
  {
    if (metaData.isFeatureSupported(OutputProcessorFeature.SPACING_SUPPORTED))
    {
      final double minValue = layoutContext.getDoubleStyleProperty(TextStyleKeys.X_MIN_LETTER_SPACING, 0);
      final double optValue = layoutContext.getDoubleStyleProperty(TextStyleKeys.X_OPTIMUM_LETTER_SPACING, 0);
      final double maxValue = layoutContext.getDoubleStyleProperty(TextStyleKeys.X_MAX_LETTER_SPACING, 0);

      final int minIntVal = (int) StrictGeomUtility.toInternalValue(minValue);
      final int optIntVal = (int) StrictGeomUtility.toInternalValue(optValue);
      final int maxIntVal = (int) StrictGeomUtility.toInternalValue(maxValue);

      final Spacing spacing = new Spacing(minIntVal, optIntVal, maxIntVal);
      return new StaticSpacingProducer(spacing);
    }
    return new StaticSpacingProducer(Spacing.EMPTY_SPACING);
  }

  protected FontSizeProducer createFontSizeProducer(final StyleSheet layoutContext)
  {
    return new VariableFontSizeProducer(fontMetrics);
  }

  protected KerningProducer createKerningProducer(final StyleSheet layoutContext)
  {
    // for now, do nothing ..
    return noKerningProducer;
  }

  public RenderNode[] finishText()
  {
    if (layoutContext == null)
    {
      return EMPTY_TEXT;
    }

    final RenderNode[] text = processText(END_OF_TEXT, 0, 1);
    layoutContext = null;
    fontSizeProducer = null;
    spacingProducer = null;

    return text;
  }

  public void startText()
  {
    startText = true;
  }
}
