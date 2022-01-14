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
 * BorderFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model.context;

import java.awt.Color;

import java.util.HashMap;

import org.jfree.report.layout.model.Border;
import org.jfree.report.layout.model.BorderCorner;
import org.jfree.report.layout.model.BorderEdge;
import org.jfree.report.layout.model.RenderLength;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.BoxSizing;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 27.04.2007, 22:09:05
 *
 * @author Thomas Morgner
 */
public class BoxDefinitionFactory
{
  private static class CacheCarrier
  {
    private long changeTracker;
    private BoxDefinition boxDefinition;

    protected CacheCarrier(final long changeTracker, final BoxDefinition border)
    {
      this.changeTracker = changeTracker;
      this.boxDefinition = border;
    }

    public long getChangeTracker()
    {
      return changeTracker;
    }

    public BoxDefinition getBoxDefinition()
    {
      return boxDefinition;
    }
  }

  private HashMap cache;

  public BoxDefinitionFactory()
  {
    this.cache = new HashMap();
  }

  public BoxDefinition getBoxDefinition (final StyleSheet es)
  {
    final InstanceID id = es.getId();
    final CacheCarrier cc = (CacheCarrier) cache.get(id);
    if (cc == null)
    {
      final BoxDefinition boxDefinition = createBoxDefinition(es);
      cache.put (id, new CacheCarrier(es.getChangeTracker(), boxDefinition));
      return boxDefinition;
    }

    if (cc.getChangeTracker() != es.getChangeTracker())
    {
      final BoxDefinition boxDefinition = createBoxDefinition(es);
      cache.put (id, new CacheCarrier(es.getChangeTracker(), boxDefinition));
      return boxDefinition;
    }

    return cc.getBoxDefinition();
  }


  private BoxDefinition createBoxDefinition(final StyleSheet style)
  {
    final BoxDefinition box = new BoxDefinition();
    box.setPreferredWidth(produceFromStyle (style, ElementStyleKeys.WIDTH, RenderLength.AUTO));
    box.setPreferredHeight(produceFromStyle(style, ElementStyleKeys.HEIGHT, RenderLength.AUTO));
    box.setMinimumWidth(produceFromStyle(style, ElementStyleKeys.MIN_WIDTH, RenderLength.EMPTY));
    box.setMinimumHeight(produceFromStyle(style, ElementStyleKeys.MIN_HEIGHT, RenderLength.EMPTY));
    box.setSizeSpecifiesBorderBox(BoxSizing.BORDER_BOX.equals(style.getStyleProperty(ElementStyleKeys.BOX_SIZING)));

    box.setMaximumWidth(produceFromStyle(style, ElementStyleKeys.MAX_WIDTH, RenderLength.AUTO));
    box.setMaximumHeight(produceFromStyle(style, ElementStyleKeys.MAX_HEIGHT, RenderLength.AUTO));
    box.setFixedPosition(produceFromStyle(style, BandStyleKeys.FIXED_POSITION, RenderLength.AUTO));

    box.setPaddingTop(Math.max(0, StrictGeomUtility.toInternalValue
        (style.getDoubleStyleProperty(ElementStyleKeys.PADDING_TOP, 0))));
    box.setPaddingLeft(Math.max(0, StrictGeomUtility.toInternalValue
        (style.getDoubleStyleProperty(ElementStyleKeys.PADDING_LEFT, 0))));
    box.setPaddingBottom(Math.max(0, StrictGeomUtility.toInternalValue
        (style.getDoubleStyleProperty(ElementStyleKeys.PADDING_BOTTOM, 0))));
    box.setPaddingRight(Math.max(0, StrictGeomUtility.toInternalValue
        (style.getDoubleStyleProperty(ElementStyleKeys.PADDING_RIGHT, 0))));

    final BorderEdge edgeTop = createEdge(style, ElementStyleKeys.BORDER_TOP_STYLE,
        ElementStyleKeys.BORDER_TOP_COLOR, ElementStyleKeys.BORDER_TOP_WIDTH);
    final BorderEdge edgeLeft = createEdge(style, ElementStyleKeys.BORDER_LEFT_STYLE,
        ElementStyleKeys.BORDER_LEFT_COLOR, ElementStyleKeys.BORDER_LEFT_WIDTH);
    final BorderEdge edgeBottom = createEdge(style, ElementStyleKeys.BORDER_BOTTOM_STYLE,
        ElementStyleKeys.BORDER_BOTTOM_COLOR, ElementStyleKeys.BORDER_BOTTOM_WIDTH);
    final BorderEdge edgeRight = createEdge(style, ElementStyleKeys.BORDER_RIGHT_STYLE,
        ElementStyleKeys.BORDER_RIGHT_COLOR, ElementStyleKeys.BORDER_RIGHT_WIDTH);
    final BorderEdge edgeBreak = createEdge(style, ElementStyleKeys.BORDER_BREAK_STYLE,
        ElementStyleKeys.BORDER_BREAK_COLOR, ElementStyleKeys.BORDER_BREAK_WIDTH);

    if (edgeBottom == BorderEdge.EMPTY &&
        edgeLeft == BorderEdge.EMPTY &&
        edgeBreak == BorderEdge.EMPTY &&
        edgeRight == BorderEdge.EMPTY &&
        edgeTop == BorderEdge.EMPTY)
    {
      box.setBorder(Border.EMPTY_BORDER);
    }
    else
    {
      final BorderCorner topLeftCorner = createCorner
          (style, ElementStyleKeys.BORDER_TOP_LEFT_RADIUS_WIDTH, ElementStyleKeys.BORDER_TOP_LEFT_RADIUS_HEIGHT);
      final BorderCorner topRightCorner = createCorner
          (style, ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS_WIDTH, ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS_HEIGHT);
      final BorderCorner bottmLeftCorner = createCorner
          (style, ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS_WIDTH, ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS_HEIGHT);
      final BorderCorner bottomRightCorner = createCorner
          (style, ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS_WIDTH, ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS_HEIGHT);
      box.setBorder(new Border(edgeTop, edgeLeft, edgeBottom, edgeRight, edgeBreak,
          topLeftCorner, topRightCorner, bottmLeftCorner, bottomRightCorner));
    }
    return box;
  }

  private BorderCorner createCorner(final StyleSheet style, final StyleKey radiusKeyX, final StyleKey radiusKeyY)
  {
    final float dimX = (float) style.getDoubleStyleProperty(radiusKeyX, 0);
    final float dimY = (float) style.getDoubleStyleProperty(radiusKeyY, 0);
    if (dimX <= 0 || dimY <= 0)
    {
      return BorderCorner.EMPTY;
    }
    return new BorderCorner(StrictGeomUtility.toInternalValue(dimX), StrictGeomUtility.toInternalValue(dimY));
  }

  private BorderEdge createEdge(final StyleSheet style, final StyleKey borderStyleKey,
                                final StyleKey borderColorKey, final StyleKey borderWidthKey)
  {
    final BorderStyle styleRight = (BorderStyle) style.getStyleProperty(borderStyleKey);
    if (styleRight == null || BorderStyle.NONE.equals(styleRight))
    {
      return BorderEdge.EMPTY;
    }

    final Color color = (Color) style.getStyleProperty(borderColorKey);
    final double width = style.getDoubleStyleProperty(borderWidthKey, 0);
    if (color == null || width <= 0)
    {
      return BorderEdge.EMPTY;
    }

    return new BorderEdge(styleRight, color, StrictGeomUtility.toInternalValue(width));
  }

  private RenderLength produceFromStyle(final StyleSheet styleSheet, final StyleKey key, final RenderLength retval)
  {
    final Float value = (Float) styleSheet.getStyleProperty(key);
    if (value == null)
    {
      return retval;
    }
    return RenderLength.createFromRaw(value.doubleValue());
  }

}
