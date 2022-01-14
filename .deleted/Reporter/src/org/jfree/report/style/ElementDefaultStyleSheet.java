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
 * ElementDefaultStyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import org.jfree.report.ElementAlignment;
import org.jfree.report.util.geom.StrictBounds;

/**
 * The default element style sheet. This style sheet defines default attribute values for
 * all elements.
 * <p/>
 * The default valignment was modified to top.
 *
 * @author Thomas Morgner
 */
public class ElementDefaultStyleSheet extends ElementStyleSheet
{
  /**
   * The default paint.
   */
  public static final Color DEFAULT_PAINT = Color.black;
  public static final Stroke DEFAULT_STROKE = new BasicStroke();

  /**
   * The default font.
   * @deprecated 
   */
  public static final FontDefinition DEFAULT_FONT_DEFINITION = new FontDefinition("Serif", 10);

  /**
   * A shared default style-sheet.
   */
  private static ElementDefaultStyleSheet defaultStyle;
  /**
   * a flag indicating the read-only state of this style sheet.
   */
  private boolean locked;

  /**
   * Creates a new style sheet.
   */
  protected ElementDefaultStyleSheet (final String name)
  {
    super(name);
    setStyleProperty(ElementStyleKeys.BOX_SIZING, BoxSizing.BORDER_BOX);
    setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(0));
    setStyleProperty(ElementStyleKeys.MIN_HEIGHT, new Float(0));
    setStyleProperty(ElementStyleKeys.MAX_WIDTH, new Float(Short.MAX_VALUE));
    setStyleProperty(ElementStyleKeys.MAX_HEIGHT, new Float(Short.MAX_VALUE));

    setStyleProperty(TextStyleKeys.LINEHEIGHT, new Float(0));
    setStyleProperty(TextStyleKeys.RESERVED_LITERAL, "..");
    setStyleProperty(TextStyleKeys.BOLD, Boolean.FALSE);
    setStyleProperty(TextStyleKeys.ITALIC, Boolean.FALSE);
    setStyleProperty(TextStyleKeys.UNDERLINED, Boolean.FALSE);
    setStyleProperty(TextStyleKeys.STRIKETHROUGH, Boolean.FALSE);
    setStyleProperty(TextStyleKeys.EMBEDDED_FONT, Boolean.FALSE);
    setStyleProperty(TextStyleKeys.FONT_SMOOTH, FontSmooth.AUTO);
    setStyleProperty(TextStyleKeys.FONT, "Serif");
    setStyleProperty(TextStyleKeys.FONTSIZE, new Integer(10));
    setStyleProperty(TextStyleKeys.WHITE_SPACE_COLLAPSE, WhitespaceCollapse.PRESERVE);
    setStyleProperty(TextStyleKeys.TRIM_TEXT_CONTENT, Boolean.FALSE);
    setStyleProperty(TextStyleKeys.TEXT_WRAP, TextWrap.WRAP);

    setStyleProperty(BOUNDS, new StrictBounds().getLockedInstance());
    setStyleProperty(ElementStyleKeys.PAINT, DEFAULT_PAINT);
    setStyleProperty(ElementStyleKeys.STROKE, DEFAULT_STROKE);
    setStyleProperty(ElementStyleKeys.VALIGNMENT, ElementAlignment.TOP);
    setStyleProperty(ElementStyleKeys.ALIGNMENT, ElementAlignment.LEFT);
    setStyleProperty(TextStyleKeys.VERTICAL_TEXT_ALIGNMENT, VerticalTextAlign.BASELINE);
    setStyleProperty(ElementStyleKeys.VISIBLE, Boolean.TRUE);

    setStyleProperty(ElementStyleKeys.ELEMENT_LAYOUT_CACHEABLE, Boolean.TRUE);
    setStyleProperty(ElementStyleKeys.OVERFLOW_X, Boolean.FALSE);
    setStyleProperty(ElementStyleKeys.OVERFLOW_Y, Boolean.FALSE);

    setStyleProperty(ElementStyleKeys.BORDER_TOP_WIDTH, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_LEFT_WIDTH, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_WIDTH, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_RIGHT_WIDTH, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_BREAK_WIDTH, new Float(0));

    setStyleProperty(ElementStyleKeys.BORDER_TOP_COLOR, Color.black);
    setStyleProperty(ElementStyleKeys.BORDER_LEFT_COLOR, Color.black);
    setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_COLOR, Color.black);
    setStyleProperty(ElementStyleKeys.BORDER_RIGHT_COLOR, Color.black);
    setStyleProperty(ElementStyleKeys.BORDER_BREAK_COLOR, Color.black);

    setStyleProperty(ElementStyleKeys.BORDER_TOP_STYLE, BorderStyle.NONE);
    setStyleProperty(ElementStyleKeys.BORDER_LEFT_STYLE, BorderStyle.NONE);
    setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_STYLE, BorderStyle.NONE);
    setStyleProperty(ElementStyleKeys.BORDER_RIGHT_STYLE, BorderStyle.NONE);
    setStyleProperty(ElementStyleKeys.BORDER_BREAK_STYLE, BorderStyle.NONE);

    setStyleProperty(ElementStyleKeys.BORDER_TOP_LEFT_RADIUS_WIDTH, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_TOP_LEFT_RADIUS_HEIGHT, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS_WIDTH, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS_HEIGHT, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS_WIDTH, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS_HEIGHT, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS_WIDTH, new Float(0));
    setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS_HEIGHT, new Float(0));

    setStyleProperty(ElementStyleKeys.PADDING_TOP, new Float(0));
    setStyleProperty(ElementStyleKeys.PADDING_LEFT, new Float(0));
    setStyleProperty(ElementStyleKeys.PADDING_RIGHT, new Float(0));
    setStyleProperty(ElementStyleKeys.PADDING_BOTTOM, new Float(0));
    setStyleProperty(ElementStyleKeys.AVOID_PAGEBREAK_INSIDE, Boolean.TRUE);

    setLocked(true);
  }

  /**
   * Gets the locked state of this stylesheet. After the first initialization the
   * stylesheet gets locked, so that it could not be changed anymore.
   *
   * @return true, if this stylesheet is readonly.
   */
  protected boolean isLocked ()
  {
    return locked;
  }

  /**
   * Defines the locked-state for this stylesheet.
   *
   * @param locked true, if the stylesheet is locked and read-only, false otherwise.
   */
  protected void setLocked (final boolean locked)
  {
    this.locked = locked;
  }

  /**
   * Returns the default element style sheet.
   *
   * @return the style-sheet.
   */
  public static ElementDefaultStyleSheet getDefaultStyle ()
  {
    if (defaultStyle == null)
    {
      defaultStyle = new ElementDefaultStyleSheet("GlobalElement");
    }
    return defaultStyle;
  }

  /**
   * Sets a style property (or removes the style if the value is <code>null</code>).
   *
   * @param key   the style key (<code>null</code> not permitted).
   * @param value the value.
   * @throws NullPointerException          if the given key is null.
   * @throws ClassCastException            if the value cannot be assigned with the given
   *                                       key.
   * @throws UnsupportedOperationException as this style sheet is read only.
   */
  public void setStyleProperty (final StyleKey key, final Object value)
  {
    if (isLocked())
    {
      throw new UnsupportedOperationException("This stylesheet is readonly");
    }
    else
    {
      super.setStyleProperty(key, value);
    }
  }

  /**
   * Clones the style-sheet. The assigned parent style sheets are not cloned. The
   * stylesheets are not assigned to the contained stylesheet collection, you have to
   * reassign them manually ...
   *
   * @return the clone.
   */
  public ElementStyleSheet getCopy ()
  {
    return this;
  }

  /**
   * Returns true, if this stylesheet is one of the global default stylesheets. Global
   * default stylesheets are unmodifiable and shared among all element stylesheets.
   *
   * @return always true.
   */
  public boolean isGlobalDefault ()
  {
    return true;
  }

  protected StyleSheetCarrier createCarrier (final ElementStyleSheet styleSheet)
  {
    throw new UnsupportedOperationException
            ("Cannot add other stylesheets as parent to the global default stylesheet");
  }
}
