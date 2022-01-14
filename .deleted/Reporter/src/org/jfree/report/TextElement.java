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
 * TextElement.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import org.jfree.report.filter.StringFilter;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.style.TextStyleKeys;

/**
 * The base class for all elements that display text in a report band.
 * <p/>
 * All content is converted to String using the String.valueOf () method. To convert
 * values in a more sophisicated way, add filters to this element. Known filters are for
 * instance the <code>NumberFormatFilter</code> or the <code>SimpleDateFormatFilter</code>.
 * <p/>
 * For more information on filters have a look at the filter package {@link
 * org.jfree.report.filter}
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class TextElement extends Element
{
  /**
   * The content type string.
   * @deprecated No longer used.
   */
  public static final String CONTENT_TYPE = "text/plain";

  /**
   * The filter used to convert values (Objects) to strings.
   */
  private StringFilter stringfilter;

  /**
   * Creates a new empty text element.
   */
  public TextElement ()
  {
    stringfilter = new StringFilter();
    setNullString(null);
  }

  /**
   * Return the null-value representation for this element. This will never return null,
   * although you may feed a null value into the set method of this property.
   *
   * @return the null value representation for this element.
   *
   * @see #setNullString(String)
   */
  public String getNullString ()
  {
    return stringfilter.getNullValue();
  }

  /**
   * Defines the null value representation for this element. If null is given, the value
   * is set to a reasonable value (this implementation sets the value to the string "-").
   *
   * @param s the null string.
   */
  public void setNullString (final String s)
  {
    final String nstring = (s == null) ? "-" : s;
    stringfilter.setNullValue(nstring);
  }

  /**
   * Returns the value for this text element.
   * <p/>
   * Internally, a <code>StringFilter</code> is used to ensure that the final result is an
   * instance of String (even though it is returned as an Object.
   *
   * @param runtime the expression runtime for evaluating inline expression.
   * @return the value for the element.
   */
  public final Object getValue (final ExpressionRuntime runtime)
  {
    stringfilter.setDataSource(getDataSource());
    return stringfilter.getValue(runtime);
  }

  /**
   * Returns a string representation of this element, useful for debugging purposes.
   *
   * @return a string.
   */
  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append(this.getClass().getName());
    b.append("={ name=");
    b.append(getName());
    b.append(", font=");
    b.append(getStyle().getFontDefinitionProperty());
    b.append('}');
    return b.toString();
  }

  /**
   * Clones this element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final TextElement te = (TextElement) super.clone();
    te.stringfilter = (StringFilter) stringfilter.clone();
    return te;
  }

  /**
   * Returns the content type, in this case '<code>text/plain</code>'.
   *
   * @return the content type.
   * @deprecated No longer used.
   */
  public String getContentType ()
  {
    return TextElement.CONTENT_TYPE;
  }

  /**
   * Returns the name of the current font.
   *
   * @return the font name
   */
  public String getFontName ()
  {
    return (String) getStyle().getStyleProperty(ElementStyleSheet.FONT);
  }

  /**
   * Defines the font name of the current font.
   *
   * @param fontName the font name
   */
  public void setFontName (final String fontName)
  {
    getStyle().setStyleProperty(ElementStyleSheet.FONT, fontName);
  }

  /**
   * Returns the font size in points.
   *
   * @return the font size.
   */
  public int getFontSize ()
  {
    final Integer i = (Integer) getStyle().getStyleProperty
            (TextStyleKeys.FONTSIZE);
    // fontsize is never null.
    return i.intValue();
  }

  /**
   * Defines the height of the font in points.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param fontSize the font size in points.
   */
  public void setFontSize (final int fontSize)
  {
    getStyle().setStyleProperty
            (TextStyleKeys.FONTSIZE, new Integer(fontSize));
  }

  /**
   * Checks, whether the font should be displayed in bold style.
   *
   * @return true, if the font should be bold, false otherwise.
   */
  public boolean isBold ()
  {
    return getStyle().getBooleanStyleProperty(TextStyleKeys.BOLD);
  }

  /**
   * Defines, whether the font should be displayed in bold, false otherwise.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param bold true, if the font should be displayed in bold, false otherwise
   */
  public void setBold (final boolean bold)
  {
    getStyle().setBooleanStyleProperty(TextStyleKeys.BOLD, bold);
  }

  /**
   * Checks, whether the font should be displayed in italic style.
   *
   * @return true, if the font should be italic, false otherwise.
   */
  public boolean isItalic ()
  {
    return getStyle().getBooleanStyleProperty(TextStyleKeys.ITALIC);
  }

  /**
   * Defines, whether the font should be displayed in italics.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param italic true, if the font should be in italic style, false otherwise.
   */
  public void setItalic (final boolean italic)
  {
    getStyle().setBooleanStyleProperty(TextStyleKeys.ITALIC, italic);
  }

  /**
   * Returns whether the text should be displayed underlined.
   *
   * @return true, if the fond should be underlined, false otherwise.
   */
  public boolean isUnderline ()
  {
    return getStyle().getBooleanStyleProperty(TextStyleKeys.UNDERLINED);
  }

  /**
   * Defines, whether the text should be displayed with the underline style applied.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param underline true, if the text should be displayed underlined, false otherwise.
   */
  public void setUnderline (final boolean underline)
  {
    getStyle().setBooleanStyleProperty(TextStyleKeys.UNDERLINED, underline);
  }

  /**
   * Returns whether the text should have the strikethough style applied.
   *
   * @return true, if the font should be striked through, false otherwise.
   */
  public boolean isStrikethrough ()
  {
    return getStyle().getBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH);
  }

  /**
   * Defines, whether the text should be displayed striked through.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param strikethrough whether to display the text with strikethrough style applied
   */
  public void setStrikethrough (final boolean strikethrough)
  {
    getStyle().setBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH, strikethrough);
  }

  /**
   * Returns the font definition object assigned with this element. Never null.
   *
   * @return the font definition for this element.
   */
  public FontDefinition getFont ()
  {
    return getStyle().getFontDefinitionProperty();
  }

  /**
   * Defines all font properties by applying the values from the given font definition
   * object.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param font the font definition for this element.
   */
  public void setFont (final FontDefinition font)
  {
    getStyle().setFontDefinitionProperty(font);

  }

  /**
   * Returns the lineheight for the element. The lineheight can be used to extend the
   * space between two text lines, the effective lineheight will be the maximum of this
   * property and the font height.
   *
   * @return the defined line height.
   */
  public float getLineHeight ()
  {
    final Float i = (Float) getStyle().getStyleProperty(TextStyleKeys.LINEHEIGHT);
    if (i == null)
    {
      return 0;
    }
    return i.floatValue();
  }

  /**
   * Defines the lineheight for the element. The lineheight can be used to extend the
   * space between two text lines, the effective lineheight will be the maximum of this
   * property and the font height.
   * <p/>
   * Calling this function with any parameter will override any previously defined value
   * for the layoutcachable attribute. The value can no longer be inherited from parent
   * stylesheets.
   *
   * @param lineHeight the defined line height.
   */
  public void setLineHeight (final float lineHeight)
  {
    getStyle().setStyleProperty(TextStyleKeys.LINEHEIGHT, new Float(lineHeight));
  }

  /**
   * Returns the reserved literal for this text element. This literal is appended,
   * whenever the text from tne content does not fully fit into the element's bounds.
   *
   * @return the reserved literal.
   */
  public String getReservedLiteral ()
  {
    return (String) getStyle().getStyleProperty(TextStyleKeys.RESERVED_LITERAL);
  }

  /**
   * Defines the reserved literal for this text element. This literal is appended,
   * whenever the text from tne content does not fully fit into the element's bounds.
   *
   * @param reservedLiteral the reserved literal.
   */
  public void setReservedLiteral (final String reservedLiteral)
  {
    getStyle().setStyleProperty(TextStyleKeys.RESERVED_LITERAL, reservedLiteral);
  }
}
