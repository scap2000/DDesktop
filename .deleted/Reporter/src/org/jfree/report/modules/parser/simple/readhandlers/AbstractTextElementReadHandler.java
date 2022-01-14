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
 * AbstractTextElementReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.style.FontSmooth;
import org.jfree.report.style.TextWrap;
import org.jfree.report.style.VerticalTextAlign;
import org.jfree.report.style.WhitespaceCollapse;
import org.jfree.xmlns.common.ParserUtil;

import org.xml.sax.SAXException;

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
 * AbstractTextElementReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public abstract class AbstractTextElementReadHandler extends AbstractElementReadHandler
{
  /**
   * A constant defining the name of the reserved-literal attribute.
   */
  public static final String RESERVED_LITERAL_ATT = "reserved-literal";
  /**
   * A constant defining the name of the trim-text-content attribute.
   */
  public static final String TRIM_TEXT_CONTENT_ATT = "trim-text-content";
  public static final String EXCEL_WRAP_TEXT = "excel-wrap-text";

  protected AbstractTextElementReadHandler ()
  {
  }

  protected abstract TextElementFactory getTextElementFactory ();

  protected final ElementFactory getElementFactory ()
  {
    return getTextElementFactory();
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException
  {
    super.startParsing(atts);
    final TextElementFactory factory = getTextElementFactory();
    factory.setVerticalTextAlignment(ReportParserUtil.parseVerticalTextElementAlignment
        (atts.getValue(getUri(), "vertical-text-alignment"), getLocator()));
    factory.setBold(ParserUtil.parseBoolean
        (atts.getValue(getUri(), FS_BOLD), getLocator()));
    factory.setEmbedFont(ParserUtil.parseBoolean
        (atts.getValue(getUri(), FS_EMBEDDED), getLocator()));
    factory.setWrapText(ParserUtil.parseBoolean
        (atts.getValue(getUri(), EXCEL_WRAP_TEXT), getLocator()));
    factory.setEncoding(atts.getValue(getUri(), FS_ENCODING));
    factory.setFontName(atts.getValue(getUri(), FONT_NAME_ATT));
    factory.setFontSize(ReportParserUtil.parseInteger
        (atts.getValue(getUri(), FONT_SIZE_ATT), getLocator()));
    factory.setItalic(ParserUtil.parseBoolean
        (atts.getValue(getUri(), FS_ITALIC), getLocator()));
    factory.setLineHeight(ReportParserUtil.parseFloat
        (atts.getValue(getUri(), LINEHEIGHT), getLocator()));
    factory.setStrikethrough(ParserUtil.parseBoolean
        (atts.getValue(getUri(), FS_STRIKETHR), getLocator()));
    factory.setUnderline(ParserUtil.parseBoolean
        (atts.getValue(getUri(), FS_UNDERLINE), getLocator()));
    factory.setReservedLiteral(atts.getValue(getUri(), RESERVED_LITERAL_ATT));
    factory.setTrimTextContent(ParserUtil.parseBoolean
        (atts.getValue(getUri(), TRIM_TEXT_CONTENT_ATT), getLocator()));
    parseSimpleFontStyle(atts.getValue(getUri(), FONT_STYLE_ATT), factory);

    factory.setWhitespaceCollapse(parseWhitespaceCollapse(atts.getValue(getUri(), "whitespace-collapse")));
    factory.setVerticalTextAlignment(parseVerticalTextAlign(atts.getValue(getUri(), "vertical-text-alignment")));
    factory.setWrap(parseTextWrap(atts.getValue(getUri(), "wrap")));
    factory.setFontSmooth(parseFontSmooth(atts.getValue(getUri(), "fontsmooth")));

    final String minLetterSpacing = atts.getValue(getUri(), "minimum-letter-spacing");
    if (minLetterSpacing != null)
    {
      factory.setMinimumLetterSpacing(ReportParserUtil.parseFloat(minLetterSpacing,  getLocator()));
    }

    final String maxLetterSpacing = atts.getValue(getUri(), "maximum-letter-spacing");
    if (maxLetterSpacing != null)
    {
      factory.setMaximumLetterSpacing(ReportParserUtil.parseFloat(maxLetterSpacing,  getLocator()));
    }

    final String optLetterSpacing = atts.getValue(getUri(), "optimum-letter-spacing");
    if (optLetterSpacing != null)
    {
      factory.setOptimumLetterSpacing(ReportParserUtil.parseFloat(optLetterSpacing,  getLocator()));
    }
  }

  private WhitespaceCollapse parseWhitespaceCollapse (final String attr)
  {
    if ("discard".equalsIgnoreCase(attr))
    {
      return WhitespaceCollapse.DISCARD;
    }
    if ("collapse".equalsIgnoreCase(attr))
    {
      return WhitespaceCollapse.COLLAPSE;
    }
    if ("preserve".equalsIgnoreCase(attr))
    {
      return WhitespaceCollapse.PRESERVE;
    }
    if ("preserve-breaks".equalsIgnoreCase(attr))
    {
      return WhitespaceCollapse.PRESERVE_BREAKS;
    }
    return null;
  }

  private VerticalTextAlign parseVerticalTextAlign (final String attr)
  {
    if ("use-script".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.USE_SCRIPT;
    }
    if ("text-bottom".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.TEXT_BOTTOM;
    }
    if ("bottom".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.BOTTOM;
    }
    if ("text-top".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.TEXT_TOP;
    }
    if ("top".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.TOP;
    }
    if ("central".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.CENTRAL;
    }
    if ("middle".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.MIDDLE;
    }

    if ("sub".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.SUB;
    }
    if ("super".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.SUPER;
    }
    if ("baseline".equalsIgnoreCase(attr))
    {
      return VerticalTextAlign.BASELINE;
    }
    return null;
  }

  private FontSmooth parseFontSmooth (final String attr)
  {
    if ("always".equalsIgnoreCase(attr))
    {
      return FontSmooth.ALWAYS;
    }
    if ("never".equalsIgnoreCase(attr))
    {
      return FontSmooth.NEVER;
    }
    if ("auto".equalsIgnoreCase(attr))
    {
      return FontSmooth.AUTO;
    }
    return null;
  }

  private TextWrap parseTextWrap (final String attr)
  {
    if ("wrap".equalsIgnoreCase(attr))
    {
      return TextWrap.WRAP;
    }
    if ("none".equalsIgnoreCase(attr))
    {
      return TextWrap.NONE;
    }
    return null;
  }

  /**
   * Parses a simple font style for text elements. These styles contain "bold", "italic"
   * and "bold-italic". The style constants are included for compatibility with older
   * releases and should no longer be used. Use the boolean flags instead.
   *
   * @param fontStyle the font style string.
   * @param target    the text element factory that should receive the parsed values.
   */
  private void parseSimpleFontStyle
          (final String fontStyle, final TextElementFactory target)
  {
    if (fontStyle != null)
    {
      if ("bold".equals(fontStyle))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.FALSE);
      }
      else if ("italic".equals(fontStyle))
      {
        target.setBold(Boolean.FALSE);
        target.setItalic(Boolean.TRUE);
      }
      else if ("bold-italic".equals(fontStyle))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.TRUE);
      }
      else if ("plain".equals(fontStyle))
      {
        target.setBold(Boolean.FALSE);
        target.setItalic(Boolean.FALSE);
      }
    }
  }

}
