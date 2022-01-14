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
 * FontFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple;

import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.xmlns.common.ParserUtil;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Parses the font specifications for bands and text elements.
 *
 * @author Thomas Morgner
 */
public final class FontFactory
{

  /**
   * Literal text for an XML attribute.
   */
  public static final String FONT_NAME_ATT = "fontname";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FONT_STYLE_ATT = "fontstyle";

  /**
   * Literal text for an XML attribute.
   */
  public static final String FONT_SIZE_ATT = "fontsize";


  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_BOLD = "fsbold";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_ITALIC = "fsitalic";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_UNDERLINE = "fsunderline";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_STRIKETHR = "fsstrikethr";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_EMBEDDED = "font-embedded";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String FS_ENCODING = "font-encoding";

  /**
   * Literal text for an XML attribute value.
   */
  public static final String LINEHEIGHT = "line-height";

  /**
   * The FontInformation class is used to store the font definition, until it can be
   * applied to a stylesheet. Parameters that are not defined, are null.
   */
  public static class FontInformation
  {
    /**
     * the font name.
     */
    private String fontname;

    /**
     * the font size.
     */
    private Integer fontSize;

    /**
     * the bold flag for the font.
     */
    private Boolean isBold;

    /**
     * the italic flag for the font.
     */
    private Boolean isItalic;

    /**
     * the strikeThrough flag for the font.
     */
    private Boolean isStrikeThrough;

    /**
     * the underlined flag for the font.
     */
    private Boolean isUnderlined;

    /**
     * the embedded flag for the font.
     */
    private Boolean isEmbedded;

    /**
     * the font encoding for the font.
     */
    private String fontencoding;

    /**
     * the line height for the font.
     */
    private Float lineHeight;

    /**
     * Creates a new FontInformation.
     */
    public FontInformation ()
    {
    }

    /**
     * Gets the font name or null, if no font name is defined.
     *
     * @return the font name or null.
     */
    public String getFontname ()
    {
      return fontname;
    }

    /**
     * Defines the font name or set to null, to indicate that no font name is defined.
     *
     * @param fontname the defined font name or null.
     */
    public void setFontname (final String fontname)
    {
      this.fontname = fontname;
    }

    /**
     * Gets the font size or null, if no font size is defined.
     *
     * @return the font size or null.
     */
    public Integer getFontSize ()
    {
      return fontSize;
    }

    /**
     * Defines the font size or set to null, to indicate that no font size is defined.
     *
     * @param fontSize the defined font size or null.
     */
    public void setFontSize (final Integer fontSize)
    {
      this.fontSize = fontSize;
    }

    /**
     * Gets the bold flag or null, if this flag is undefined.
     *
     * @return the bold flag or null.
     */
    public Boolean getBold ()
    {
      return isBold;
    }

    /**
     * Defines the bold flag or set to null, to indicate that this flag is undefined.
     *
     * @param bold the defined bold flag or null.
     */
    public void setBold (final Boolean bold)
    {
      isBold = bold;
    }

    /**
     * Gets the italic flag or null, if this flag is undefined.
     *
     * @return the italic flag or null.
     */
    public Boolean getItalic ()
    {
      return isItalic;
    }

    /**
     * Defines the italic flag or set to null, to indicate that this flag is undefined.
     *
     * @param italic the defined italic flag or null.
     */
    public void setItalic (final Boolean italic)
    {
      isItalic = italic;
    }

    /**
     * Gets the strikeThrough flag or null, if this flag is undefined.
     *
     * @return the strikeThrough flag or null.
     */
    public Boolean getStrikeThrough ()
    {
      return isStrikeThrough;
    }

    /**
     * Defines the strikeThrough flag or set to null, to indicate that this flag is
     * undefined.
     *
     * @param strikeThrough the defined strikeThrough flag or null.
     */
    public void setStrikeThrough (final Boolean strikeThrough)
    {
      isStrikeThrough = strikeThrough;
    }

    /**
     * Gets the underlined flag or null, if this flag is undefined.
     *
     * @return the underlined flag or null.
     */
    public Boolean getUnderlined ()
    {
      return isUnderlined;
    }

    /**
     * Defines the underlined flag or set to null, to indicate that this flag is
     * undefined.
     *
     * @param underlined the defined underlined flag or null.
     */
    public void setUnderlined (final Boolean underlined)
    {
      isUnderlined = underlined;
    }

    /**
     * Gets the underlined flag or null, if this flag is undefined.
     *
     * @return the underlined flag or null.
     */
    public Boolean getEmbedded ()
    {
      return isEmbedded;
    }

    /**
     * Defines the embedded flag or set to null, to indicate that this flag is undefined.
     *
     * @param embedded the defined embedded flag or null.
     */
    public void setEmbedded (final Boolean embedded)
    {
      isEmbedded = embedded;
    }

    /**
     * Returns the defined character encoding for this font, or null, if no encoding is
     * defined.
     *
     * @return the defined character encoding or null.
     */
    public String getFontencoding ()
    {
      return fontencoding;
    }

    /**
     * Defines the character encoding for this font, or null, if no encoding is defined.
     *
     * @param fontencoding the character encoding or null.
     */
    public void setFontencoding (final String fontencoding)
    {
      this.fontencoding = fontencoding;
    }

    /**
     * Returns the line height for this font, or null, if the line height is undefined.
     *
     * @return the defined line height or null.
     */
    public Float getLineHeight ()
    {
      return lineHeight;
    }

    /**
     * Defines the line height for this font, or null, if the line height is undefined.
     *
     * @param lineHeight the defined line height or null.
     */
    public void setLineHeight (final Float lineHeight)
    {
      this.lineHeight = lineHeight;
    }
  }

  /**
   * Default constructor.
   */
  private FontFactory ()
  {
  }

  /**
   * Applies the font information to the ElementStyleSheet.
   *
   * @param es the element style sheet that should receive the font definition.
   * @param fi the previously parsed font information.
   */
  public static void applyFontInformation (final ElementStyleSheet es,
                                           final FontInformation fi)
  {
    if (fi.getFontname() != null)
    {
      es.setStyleProperty(TextStyleKeys.FONT, fi.getFontname());
    }
    if (fi.getFontSize() != null)
    {
      es.setStyleProperty(TextStyleKeys.FONTSIZE, fi.getFontSize());
    }
    if (fi.getItalic() != null)
    {
      es.setStyleProperty(TextStyleKeys.ITALIC, fi.getItalic());
    }
    if (fi.getBold() != null)
    {
      es.setStyleProperty(TextStyleKeys.BOLD, fi.getBold());
    }
    if (fi.getUnderlined() != null)
    {
      es.setStyleProperty(TextStyleKeys.UNDERLINED, fi.getUnderlined());
    }
    if (fi.getStrikeThrough() != null)
    {
      es.setStyleProperty(TextStyleKeys.STRIKETHROUGH, fi.getStrikeThrough());
    }
    if (fi.getEmbedded() != null)
    {
      es.setStyleProperty(TextStyleKeys.EMBEDDED_FONT, fi.getEmbedded());
    }
    if (fi.getFontencoding() != null)
    {
      es.setStyleProperty(TextStyleKeys.FONTENCODING, fi.getFontencoding());
    }
    if (fi.getLineHeight() != null)
    {
      es.setStyleProperty(TextStyleKeys.LINEHEIGHT, fi.getLineHeight());
    }
  }


  /**
   * Reads the fontstyle for an attribute set. The font style is appended to the given
   * font style definition and the font information is also returned to the caller.
   *
   * @param attr   the element attributes.
   * @param target the font information, that should be used to store the defined values.
   * @return the read font information.
   */
  private static FontInformation readSimpleFontStyle (final String uri,
                                                      final Attributes attr,
                                                      FontInformation target,
                                                      final Locator locator)
      throws SAXException
  {
    if (target == null)
    {
      target = new FontInformation();
    }
    final String fontStyle = attr.getValue(uri, FONT_STYLE_ATT);

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

    target.setBold(ParserUtil.parseBoolean(attr.getValue(uri, FS_BOLD), locator));
    target.setItalic(ParserUtil.parseBoolean(attr.getValue(uri, FS_ITALIC), locator));
    target.setStrikeThrough(ParserUtil.parseBoolean(attr.getValue(uri, FS_STRIKETHR), locator));
    target.setUnderlined(ParserUtil.parseBoolean(attr.getValue(uri, FS_UNDERLINE), locator));
    target.setEmbedded(ParserUtil.parseBoolean(attr.getValue(uri, FS_EMBEDDED), locator));
    target.setFontencoding(attr.getValue(uri, FS_ENCODING));
    target.setLineHeight(ReportParserUtil.parseFloat(attr.getValue(uri, LINEHEIGHT), locator));
    return target;
  }

  /**
   * Parses an element font. Missing attributes are replaces with the default font's
   * attributes.
   *
   * @param attr   the element attributes.
   * @param target the target element style sheet, that should receive the created font
   *               definition.
   */
  public static void createFont (final String uri,
                                 final Attributes attr,
                                 final ElementStyleSheet target,
                                 final Locator locator) throws SAXException
  {
    // get the font name...
    final String elementFontName = attr.getValue(uri, FONT_NAME_ATT);
    if (elementFontName != null)
    {
      target.setStyleProperty(TextStyleKeys.FONT, elementFontName);
    }

    final FontInformation fi = new FontInformation();
    // get the font style...
    applyFontInformation(target, readSimpleFontStyle(uri, attr, fi, locator));

    // get the font size...
    final Integer elementFontSize = parseInteger(attr.getValue(uri, FONT_SIZE_ATT));
    if (elementFontSize != null)
    {
      target.setStyleProperty(TextStyleKeys.FONTSIZE, elementFontSize);
    }
  }

  /**
   * Reads an attribute as int and returns <code>def</code> if that fails.
   *
   * @param val the attribute value.
   * @return the int value.
   */
  private static Integer parseInteger (final String val)
  {
    if (val == null)
    {
      return null;
    }
    try
    {
      return new Integer(val);
    }
    catch (NumberFormatException e)
    {
      // swallow the exception, the default value will be returned
    }
    return null;
  }

  /**
   * Parses an element font. Missing attributes are replaces with the default font's
   * attributes.
   *
   * @param attr the element attributes.
   * @return the created font information.
   */
  public static FontInformation createFont(final String uri,
                                           final Attributes attr,
                                           final Locator locator)
      throws SAXException
  {
    // get the font name...
    final FontInformation fi = new FontInformation();

    final String elementFontName = attr.getValue(uri, FONT_NAME_ATT);
    if (elementFontName != null)
    {
      fi.setFontname(elementFontName);
    }

    // get the font style...
    readSimpleFontStyle(uri, attr, fi, locator);

    // get the font size...
    final Integer elementFontSize = parseInteger(attr.getValue(uri, FONT_SIZE_ATT));
    if (elementFontSize != null)
    {
      fi.setFontSize(elementFontSize);
    }
    return fi;
  }
//
//  /**
//   * Returns the correct Boolean object for the given primitive boolean variable.
//   *
//   * @param bool the primitive boolean.
//   * @return the Boolean object.
//   */
//  private static Boolean getBoolean (final boolean bool)
//  {
//    if (bool == true)
//    {
//      return Boolean.TRUE;
//    }
//    return Boolean.FALSE;
//  }
}
