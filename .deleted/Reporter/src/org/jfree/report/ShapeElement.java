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
 * ShapeElement.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.awt.BasicStroke;
import java.awt.Stroke;

import org.jfree.report.style.ElementDefaultStyleSheet;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleKey;

/**
 * Used to draw shapes (typically lines and boxes) on a report band. The drawing style of
 * the shapes contained in that element can be controled by using the StyleKeys FILL_SHAPE
 * and DRAW_SHAPE.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class ShapeElement extends Element
{
  /**
   * The default stroke.
   */
  public static final BasicStroke DEFAULT_STROKE = new BasicStroke(0.5f);

  /**
   * A key for the 'fill-shape' style.
   */
  public static final StyleKey FILL_SHAPE = ElementStyleKeys.FILL_SHAPE;

  /**
   * A key for the 'draw-shape' style.
   */
  public static final StyleKey DRAW_SHAPE = ElementStyleKeys.DRAW_SHAPE;

  /**
   * A default style sheet for shape elements. This defined a default stroke for all
   * shapes.
   */
  private static class ShapeElementDefaultStyleSheet extends ElementDefaultStyleSheet
  {
    /**
     * Creates a new style-sheet. The stylesheet is not modifiable
     */
    protected ShapeElementDefaultStyleSheet ()
    {
      super("GlobalShapeElementDefault");
      // unlock the write protection
      setLocked(false);
      setStyleProperty(ElementStyleKeys.STROKE, ShapeElement.DEFAULT_STROKE);
      setBooleanStyleProperty(ElementStyleKeys.FILL_SHAPE, false);
      setBooleanStyleProperty(ElementStyleKeys.DRAW_SHAPE, false);
      // and lock the stylesheet again...
      setLocked(true);
    }
  }

  /**
   * A shared default style sheet for shape elements.
   */
  private static ElementDefaultStyleSheet defaultShapeStyle;

  /**
   * Returns the default style-sheet for shape elements.
   *
   * @return a default style sheet that can be shared among shape elements.
   */
  public static synchronized ElementDefaultStyleSheet getDefaultStyle ()
  {
    if (defaultShapeStyle == null)
    {
      defaultShapeStyle = new ShapeElementDefaultStyleSheet();
    }
    return defaultShapeStyle;
  }

  /**
   * Constructs a shape element.
   */
  public ShapeElement ()
  {
  }

  /**
   * Returns the ShapeElement specific global stylesheet.
   *
   * @return the global stylesheet.
   */
  protected ElementDefaultStyleSheet createGlobalDefaultStyle ()
  {
    return ShapeElement.getDefaultStyle();
  }

  /**
   * Returns a string describing the element.  Useful for debugging.
   *
   * @return the string.
   */
  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("Shape={ name=");
    b.append(getName());
    b.append('}');

    return b.toString();
  }

  /**
   * Returns true if the element outline should be drawn, and false otherwise.
   * <p/>
   * This is determined by the element's style-sheet.
   *
   * @return true or false.
   */
  public boolean isShouldDraw ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleKeys.DRAW_SHAPE);
  }

  /**
   * Returns true of the element should be filled, and false otherwise.
   * <p/>
   * This is determined by the element's style-sheet.
   *
   * @return true or false.
   */
  public boolean isShouldFill ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleKeys.FILL_SHAPE);
  }

  /**
   * Sets a flag that controls whether or not the outline of the shape is drawn.
   *
   * @param shouldDraw the flag.
   */
  public void setShouldDraw (final boolean shouldDraw)
  {
    getStyle().setStyleProperty(ElementStyleKeys.DRAW_SHAPE, shouldDraw ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * Sets a flag that controls whether or not the area of the shape is filled.
   *
   * @param shouldFill the flag.
   */
  public void setShouldFill (final boolean shouldFill)
  {
    getStyle().setStyleProperty(ElementStyleKeys.FILL_SHAPE, shouldFill ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * Returns true if the shape should be scaled, and false otherwise.
   * <p/>
   * This is determined by the element's style-sheet.
   *
   * @return true or false.
   */
  public boolean isScale ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleKeys.SCALE);
  }

  /**
   * Sets a flag that controls whether the shape should be scaled to fit the element
   * bounds.
   *
   * @param scale the flag.
   */
  public void setScale (final boolean scale)
  {
    getStyle().setStyleProperty(ElementStyleKeys.SCALE, scale ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * Returns true if the shape's aspect ratio should be preserved, and false otherwise.
   * <p/>
   * This is determined by the element's style-sheet.
   *
   * @return true or false.
   */
  public boolean isKeepAspectRatio ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleKeys.KEEP_ASPECT_RATIO);
  }

  /**
   * Sets a flag that controls whether the shape should be scaled to fit the element
   * bounds.
   *
   * @param kar the flag.
   */
  public void setKeepAspectRatio (final boolean kar)
  {
    getStyle().setStyleProperty(ElementStyleKeys.KEEP_ASPECT_RATIO,
            kar ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * A string for the content type.
   */
  public static final String CONTENT_TYPE = "shape/generic";

  /**
   * Returns the content type, in this case 'shape/generic'.
   *
   * @return the content type.
   */
  public String getContentType ()
  {
    return ShapeElement.CONTENT_TYPE;
  }

  /**
   * Returns the stroke.
   *
   * @return the stroke.
   */
  public Stroke getStroke ()
  {
    return (Stroke) getStyle().getStyleProperty(ElementStyleKeys.STROKE);
  }

  /**
   * Sets the stroke.
   *
   * @param stroke the stroke.
   */
  public void setStroke (final Stroke stroke)
  {
    getStyle().setStyleProperty(ElementStyleKeys.STROKE, stroke);
  }
}
