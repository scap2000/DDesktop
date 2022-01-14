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
 * ElementStyleKeys.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.style;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import org.jfree.report.ElementAlignment;
import org.jfree.report.util.geom.StrictBounds;

/**
 * Creation-Date: 05.04.2007, 15:18:00
 *
 * @author Thomas Morgner
 */
public class ElementStyleKeys
{
  public static final StyleKey BORDER_TOP_WIDTH = StyleKey.getStyleKey("border-top-width", Float.class, false, false);
  public static final StyleKey BORDER_TOP_STYLE = StyleKey.getStyleKey("border-top-style", BorderStyle.class, false, false);
  public static final StyleKey BORDER_TOP_COLOR = StyleKey.getStyleKey("border-top-color", Color.class, false, false);

  public static final StyleKey BORDER_LEFT_WIDTH = StyleKey.getStyleKey("border-left-width", Float.class, false, false);
  public static final StyleKey BORDER_LEFT_STYLE = StyleKey.getStyleKey("border-left-style", BorderStyle.class, false, false);
  public static final StyleKey BORDER_LEFT_COLOR = StyleKey.getStyleKey("border-left-color", Color.class, false, false);

  public static final StyleKey BORDER_BOTTOM_WIDTH = StyleKey.getStyleKey("border-bottom-width", Float.class, false, false);
  public static final StyleKey BORDER_BOTTOM_STYLE = StyleKey.getStyleKey("border-bottom-style", BorderStyle.class, false, false);
  public static final StyleKey BORDER_BOTTOM_COLOR = StyleKey.getStyleKey("border-bottom-color", Color.class, false, false);

  public static final StyleKey BORDER_RIGHT_WIDTH = StyleKey.getStyleKey("border-right-width", Float.class, false, false);
  public static final StyleKey BORDER_RIGHT_STYLE = StyleKey.getStyleKey("border-right-style", BorderStyle.class, false, false);
  public static final StyleKey BORDER_RIGHT_COLOR = StyleKey.getStyleKey("border-right-color", Color.class, false, false);

  public static final StyleKey BORDER_BREAK_WIDTH = StyleKey.getStyleKey("border-break-width", Float.class, false, false);
  public static final StyleKey BORDER_BREAK_STYLE = StyleKey.getStyleKey("border-break-style", BorderStyle.class, false, false);
  public static final StyleKey BORDER_BREAK_COLOR = StyleKey.getStyleKey("border-break-color", Color.class, false, false);

  public static final StyleKey BORDER_TOP_RIGHT_RADIUS = StyleKey.getStyleKey("border-top-right-radius", Dimension2D.class, true, false);
  public static final StyleKey BORDER_TOP_LEFT_RADIUS = StyleKey.getStyleKey("border-top-left-radius", Dimension2D.class, true, false);
  public static final StyleKey BORDER_BOTTOM_RIGHT_RADIUS = StyleKey.getStyleKey("border-bottom-right-radius", Dimension2D.class, true, false);
  public static final StyleKey BORDER_BOTTOM_LEFT_RADIUS = StyleKey.getStyleKey("border-bottom-left-radius", Dimension2D.class, true, false);

  public static final StyleKey BORDER_TOP_RIGHT_RADIUS_WIDTH = StyleKey.getStyleKey("border-top-right-radius-width", Float.class, false, false);
  public static final StyleKey BORDER_TOP_LEFT_RADIUS_WIDTH = StyleKey.getStyleKey("border-top-left-radius-width", Float.class, false, false);
  public static final StyleKey BORDER_BOTTOM_RIGHT_RADIUS_WIDTH = StyleKey.getStyleKey("border-bottom-right-radius-width", Float.class, false, false);
  public static final StyleKey BORDER_BOTTOM_LEFT_RADIUS_WIDTH = StyleKey.getStyleKey("border-bottom-left-radius-width", Float.class, false, false);
  public static final StyleKey BORDER_TOP_RIGHT_RADIUS_HEIGHT = StyleKey.getStyleKey("border-top-right-radius-height", Float.class, false, false);
  public static final StyleKey BORDER_TOP_LEFT_RADIUS_HEIGHT = StyleKey.getStyleKey("border-top-left-radius-height", Float.class, false, false);
  public static final StyleKey BORDER_BOTTOM_RIGHT_RADIUS_HEIGHT = StyleKey.getStyleKey("border-bottom-right-radius-height", Float.class, false, false);
  public static final StyleKey BORDER_BOTTOM_LEFT_RADIUS_HEIGHT = StyleKey.getStyleKey("border-bottom-left-radius-height", Float.class, false, false);

  public static final StyleKey PADDING_LEFT = StyleKey.getStyleKey("padding-left", Float.class, false, false);
  public static final StyleKey PADDING_TOP = StyleKey.getStyleKey("padding-top", Float.class, false, false);
  public static final StyleKey PADDING_BOTTOM = StyleKey.getStyleKey("padding-bottom", Float.class, false, false);
  public static final StyleKey PADDING_RIGHT = StyleKey.getStyleKey("padding-right", Float.class, false, false);

  public static final StyleKey OVERFLOW_X = StyleKey.getStyleKey("overflow-x", Boolean.class, false, false);
  public static final StyleKey OVERFLOW_Y = StyleKey.getStyleKey("overflow-y", Boolean.class, false, false);

  public static final StyleKey BOX_SIZING = StyleKey.getStyleKey("box-sizing", BoxSizing.class, false, false);

  /**
   * A key for the absolute position of an element.
   * @deprecated use the POS_X and POS_Y keys instead.
   */
  public static final StyleKey ABSOLUTE_POS = StyleKey.getStyleKey("absolute_pos", Point2D.class, true, false);

  public static final StyleKey POS_X = StyleKey.getStyleKey("x", Float.class, false, false);
  public static final StyleKey POS_Y = StyleKey.getStyleKey("y", Float.class, false, false);

  /**
   * If this flag is set to true, the engine will try to avoid pagebreaks in this box. However, if after one break
   * the box still doesnot fit into the page, we will start breaking the box.
   */
  public static final StyleKey AVOID_PAGEBREAK_INSIDE = StyleKey.getStyleKey("avoid-page-break", Boolean.class);

  /**
   * A key for the 'minimum size' of an element.
   * This style property is not inherited from the parent band.
   * @deprecated use the minimum-width and minimum-height style-keys instead.
   */
  public static final StyleKey MINIMUMSIZE =
          StyleKey.getStyleKey("min-size", Dimension2D.class, true, false);

  public static final StyleKey MIN_WIDTH = StyleKey.getStyleKey("min-width", Float.class, false, false);
  public static final StyleKey MIN_HEIGHT = StyleKey.getStyleKey("min-height", Float.class, false, false);

  /**
   * A key for the 'maximum size' of an element.
   * This style property is not inherited from the parent band.
   * @deprecated use the max-width and max-height style-keys instead.
   */
  public static final StyleKey MAXIMUMSIZE =
          StyleKey.getStyleKey("max-size", Dimension2D.class, true, false);

  public static final StyleKey MAX_WIDTH = StyleKey.getStyleKey("max-width", Float.class, false, false);
  public static final StyleKey MAX_HEIGHT = StyleKey.getStyleKey("max-height", Float.class, false, false);

  /**
   * A key for the 'preferred size' of an element.
   * This style property is not inherited from the parent band.
   * @deprecated use the width and height style-keys instead.
   */
  public static final StyleKey PREFERREDSIZE =
          StyleKey.getStyleKey("preferred-size", Dimension2D.class, true, false);

  public static final StyleKey WIDTH = StyleKey.getStyleKey("width", Float.class, false, false);
  public static final StyleKey HEIGHT = StyleKey.getStyleKey("height", Float.class, false, false);

  /**
   * A key for the 'bounds' of an element.
   * This style property is not inherited from the parent band.
   * This style property is an internal state property and is therefore
   * not written to the XML.
   * @deprecated no longer used.
   */
  public static final StyleKey BOUNDS = StyleKey.getStyleKey("bounds", StrictBounds.class, true, false);

  /**
   * A key for an element's 'visible' flag.
   */
  public static final StyleKey VISIBLE = StyleKey.getStyleKey("visible", Boolean.class, false, false);

  /**
   * A key for the 'paint' used to color an element. For historical reasons, this key
   * requires a color value.
   */
  public static final StyleKey PAINT = StyleKey.getStyleKey("paint", Color.class);

  /**
   * A key for the 'ext-paint' used to fill or draw an element. If the specified paint is
   * not supported by the output target, the color given with the 'paint' key is used
   * instead.
   */
  public static final StyleKey EXTPAINT = StyleKey.getStyleKey("ext-paint", Paint.class);

  public static final StyleKey BACKGROUND_COLOR = StyleKey.getStyleKey("background-color", Color.class, false, false);

  /**
   * A key for the 'stroke' used to draw an element. (This now only applies to shape and drawable-elements.)
   */
  public static final StyleKey STROKE = StyleKey.getStyleKey("stroke", Stroke.class);
//
//  public static final StyleKey STROKE_STYLE = StyleKey.getStyleKey("stroke-style", String.class);
//  public static final StyleKey STROKE_WIDTH = StyleKey.getStyleKey("stroke-width", Float.class);

  /**
   * A key for the horizontal alignment of an element.
   */
  public static final StyleKey ALIGNMENT = StyleKey.getStyleKey("alignment",
          ElementAlignment.class);

  /**
   * A key for the vertical alignment of an element. This is the content alignment for all elements. However, if a
   * text-element defines a vertical-text-alignment, then this one is used instead.
   */
  public static final StyleKey VALIGNMENT = StyleKey.getStyleKey("valignment", ElementAlignment.class);

  /**
   * A key for an element's 'scale' flag.
   */
  public static final StyleKey SCALE = StyleKey.getStyleKey("scale", Boolean.class);

  /**
   * A key for an element's 'keep aspect ratio' flag.
   */
  public static final StyleKey KEEP_ASPECT_RATIO = StyleKey.getStyleKey("keepAspectRatio", Boolean.class);

  /**
   * A key for the dynamic height flag for an element.
   */
  public static final StyleKey DYNAMIC_HEIGHT = StyleKey.getStyleKey("dynamic_height", Boolean.class);

  /**
   * The Layout Cacheable stylekey. Set this stylekey to false, to define that the element
   * is not cachable. This key defaults to true.
   *
   * @deprecated
   */
  public static final StyleKey ELEMENT_LAYOUT_CACHEABLE = StyleKey.getStyleKey("layout-cacheable", Boolean.class);

  public static final StyleKey ANCHOR_NAME = StyleKey.getStyleKey("anchor-name", String.class, false, false);

  public static final StyleKey HREF_TARGET = StyleKey.getStyleKey("href-target", String.class, false, false);
  public static final StyleKey HREF_TITLE = StyleKey.getStyleKey("href-title", String.class, false, false);

  /**
   * Specifies the anchor tag's target window for opening the link.
   */
  public static final StyleKey HREF_WINDOW = StyleKey.getStyleKey ("href-html-window", String.class);

  /**
   * An internal flag style indicating whether the current HRef is inherited from
   * a child.
   *
   * This style property is an internal state property and is therefore
   * not written to the XML.
   *
   * @deprecated the HREF is no longer inheritable and therefore this key is not used anymore.
   */
  public static final StyleKey HREF_INHERITED = StyleKey.getStyleKey("href-inherited", Boolean.class, true, false);

  /**
   * The StyleKey for the user defined cell data format.
   */
  public static final StyleKey EXCEL_WRAP_TEXT = StyleKey.getStyleKey("Excel.WrapText", Boolean.class);

  /**
   * The StyleKey for the user defined cell data format.
   */
  public static final StyleKey EXCEL_DATA_FORMAT_STRING = StyleKey.getStyleKey("Excel.CellDataFormat", String.class, false, false);


  /**
   * A key for the 'fill-shape' style.
   */
  public static final StyleKey FILL_SHAPE = StyleKey.getStyleKey("fill-shape", Boolean.class);

  /**
   * A key for the 'draw-shape' style.
   */
  public static final StyleKey DRAW_SHAPE = StyleKey.getStyleKey("draw-shape", Boolean.class);

  public static final StyleKey ORPHANS = StyleKey.getStyleKey("orphans", Integer.class, false, false);
  public static final StyleKey WIDOWS = StyleKey.getStyleKey("widows", Integer.class, false, false);

  private ElementStyleKeys()
  {
  }
}
