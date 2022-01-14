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
 * TextStyleKeys.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.style;

/**
 * Creation-Date: 03.04.2007, 16:46:39
 *
 * @author Thomas Morgner
 */
public class TextStyleKeys
{
  private TextStyleKeys()
  {
  }

  /**
   * 'preserve', 'discard', 'preserve-breaks' or 'none'
   */
  public static final StyleKey WHITE_SPACE_COLLAPSE =
      StyleKey.getStyleKey("whitespace-collapse", WhitespaceCollapse.class, false, true);
  /**
   * 'none' or 'wrap'
   */
  public static final StyleKey TEXT_WRAP =
      StyleKey.getStyleKey("text-wrap", TextWrap.class, false, true);

  public static final StyleKey X_MIN_LETTER_SPACING =
      StyleKey.getStyleKey("min-letter-spacing", Float.class, false, true);
  public static final StyleKey X_OPTIMUM_LETTER_SPACING =
      StyleKey.getStyleKey("optimum-letter-spacing", Float.class, false, true);
  public static final StyleKey X_MAX_LETTER_SPACING =
      StyleKey.getStyleKey("max-letter-spacing", Float.class, false, true);
  public static final StyleKey WORD_SPACING =
      StyleKey.getStyleKey("word-spacing", Float.class, false, true);

  public static final StyleKey FONT_SMOOTH =
      StyleKey.getStyleKey("font-smooth", FontSmooth.class, false, true);
  public static final StyleKey VERTICAL_TEXT_ALIGNMENT =
      StyleKey.getStyleKey("vertical-text-alignment", VerticalTextAlign.class);


  /**
   * A key for the 'font family' used to draw element text.
   */
  public static final StyleKey FONT = StyleKey.getStyleKey("font", String.class);

  /**
   * A key for the 'font size' used to draw element text.
   */
  public static final StyleKey FONTSIZE = StyleKey.getStyleKey("font-size", Integer.class);

  /**
   * A key for the 'font size' used to draw element text.
   */
  public static final StyleKey LINEHEIGHT = StyleKey.getStyleKey("line-height", Float.class);

  /**
   * A key for an element's 'bold' flag.
   */
  public static final StyleKey BOLD = StyleKey.getStyleKey("font-bold", Boolean.class);

  /**
   * A key for an element's 'italic' flag.
   */
  public static final StyleKey ITALIC = StyleKey.getStyleKey("font-italic", Boolean.class);

  /**
   * A key for an element's 'underlined' flag.
   */
  public static final StyleKey UNDERLINED = StyleKey.getStyleKey("font-underline", Boolean.class);

  /**
   * A key for an element's 'strikethrough' flag.
   */
  public static final StyleKey STRIKETHROUGH = StyleKey.getStyleKey("font-strikethrough", Boolean.class);

  /**
   * A key for an element's 'embedd' flag.
   */
  public static final StyleKey EMBEDDED_FONT = StyleKey.getStyleKey("font-embedded", Boolean.class);

  /**
   * A key for an element's 'embedd' flag.
   */
  public static final StyleKey FONTENCODING = StyleKey.getStyleKey("font-encoding", String.class);

  /**
   * The string that is used to end a text if not all text fits into the element.
   */
  public static final StyleKey RESERVED_LITERAL = StyleKey.getStyleKey ("reserved-literal", String.class);

  /**
   * The Layout Cacheable stylekey. Set this stylekey to false, to define that the element
   * is not cachable. This key defaults to true.
   */
  public static final StyleKey TRIM_TEXT_CONTENT = StyleKey.getStyleKey("trim-text-content", Boolean.class);

}
