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
 * BorderRenderer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.graphics.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.layout.model.Border;
import org.jfree.report.layout.model.BorderCorner;
import org.jfree.report.layout.model.BorderEdge;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 28.10.2007, 15:52:19
 *
 * @author Thomas Morgner
 */
public class BorderRenderer
{
  private static final AffineTransform scaleInstance = AffineTransform.getScaleInstance(1.0 / 1000.0, 1.0 / 1000.0);
  private RenderBox box;
  private boolean sameForAllSides;
  private Color backgroundColor;

  private Shape borderShape;
  private Shape borderShapeTop;
  private Shape borderShapeLeft;
  private Shape borderShapeBottom;
  private Shape borderShapeRight;

  public BorderRenderer(final RenderBox box)
  {
    this.box = box;
    this.sameForAllSides = computeSameForAllSides();
    this.backgroundColor = (Color) box.getStyleSheet().getStyleProperty(ElementStyleKeys.BACKGROUND_COLOR);
  }

  private BasicStroke createStroke(final BorderEdge edge, final long internalWidth)
  {
    final float effectiveWidth = (float) StrictGeomUtility.toExternalValue(internalWidth);

    if (BorderStyle.DASHED.equals(edge.getBorderStyle()))
    {
      return new BasicStroke(effectiveWidth, BasicStroke.CAP_SQUARE,
          BasicStroke.JOIN_MITER,
          10.0f, new float[]
          {6 * effectiveWidth, 6 * effectiveWidth}, 0.0f);
    }
    if (BorderStyle.DOTTED.equals(edge.getBorderStyle()))
    {
      return new BasicStroke(effectiveWidth, BasicStroke.CAP_SQUARE,
          BasicStroke.JOIN_MITER,
          5.0f, new float[]{0.0f, 2 * effectiveWidth}, 0.0f);
    }
    if (BorderStyle.DOT_DASH.equals(edge.getBorderStyle()))
    {
      return new BasicStroke(effectiveWidth, BasicStroke.CAP_SQUARE,
          BasicStroke.JOIN_MITER,
          10.0f, new float[]
          {0, 2 * effectiveWidth, 6 * effectiveWidth, 2 * effectiveWidth}, 0.0f);
    }
    if (BorderStyle.DOT_DOT_DASH.equals(edge.getBorderStyle()))
    {
      return new BasicStroke(effectiveWidth, BasicStroke.CAP_SQUARE,
          BasicStroke.JOIN_MITER,
          10.0f, new float[]{0, 2 * effectiveWidth,
          0, 2 * effectiveWidth,
          6 * effectiveWidth, 2 * effectiveWidth}, 0.0f);
    }
    return new BasicStroke(effectiveWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
  }

  public void paintBackroundAndBorder(final Graphics2D g2d)
  {
    final Color oldColor = g2d.getColor();
    final Stroke oldStroke = g2d.getStroke();
    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();

    if (isSameForAllSides())
    {
      final Shape borderShape = getBorderShape();

      if (backgroundColor != null)
      {
        g2d.setColor(backgroundColor);
        g2d.fill(borderShape);
      }

      if (sblp.getBorderTop() > 0)
      {
        final Border border = box.getBoxDefinition().getBorder();
        final BorderEdge borderEdge = border.getTop();

        g2d.setColor(borderEdge.getColor());
        g2d.setStroke(createStroke(borderEdge, sblp.getBorderTop()));
        g2d.draw(borderShape);
      }
      g2d.setColor(oldColor);
      g2d.setStroke(oldStroke);
      return;
    }


    if (backgroundColor != null)
    {
      final Shape borderShape = getBorderShape();
      g2d.setColor(backgroundColor);
      g2d.fill(borderShape);
    }

    final Border border = box.getBoxDefinition().getBorder();
    if (sblp.getBorderTop() > 0)
    {
      final BorderEdge borderEdge = border.getTop();
      g2d.setColor(borderEdge.getColor());
      g2d.setStroke(createStroke(borderEdge, sblp.getBorderTop()));
      g2d.draw(getBorderTopShape());
    }

    if (sblp.getBorderRight() > 0)
    {
      final BorderEdge borderEdge = border.getRight();
      g2d.setColor(borderEdge.getColor());
      g2d.setStroke(createStroke(borderEdge, sblp.getBorderRight()));
      g2d.draw(getBorderRightShape());
    }

    if (sblp.getBorderBottom() > 0)
    {
      final BorderEdge borderEdge = border.getBottom();

      g2d.setColor(borderEdge.getColor());
      g2d.setStroke(createStroke(borderEdge, sblp.getBorderBottom()));
      g2d.draw(getBorderBottomShape());
    }

    if (sblp.getBorderLeft() > 0)
    {
      final BorderEdge borderEdge = border.getLeft();

      g2d.setColor(borderEdge.getColor());
      g2d.setStroke(createStroke(borderEdge, sblp.getBorderLeft()));
      g2d.draw(getBorderLeftShape());
    }

    g2d.setColor(oldColor);
    g2d.setStroke(oldStroke);
  }

  private boolean computeSameForAllSides()
  {
    final Border border = box.getBoxDefinition().getBorder();
    final BorderEdge borderEdge = border.getTop();
    if (borderEdge.equals(border.getBottom()) == false)
    {
      return false;
    }
    if (borderEdge.equals(border.getLeft()) == false)
    {
      return false;
    }
    if (borderEdge.equals(border.getRight()) == false)
    {
      return false;
    }
    final BorderCorner corner = border.getTopLeft();
    if (corner.equals(border.getTopRight()) == false)
    {
      return false;
    }
    if (corner.equals(border.getBottomLeft()) == false)
    {
      return false;
    }
    if (corner.equals(border.getBottomRight()) == false)
    {
      return false;
    }
    return true;
  }

  private boolean isSameForAllSides()
  {
    return sameForAllSides;
  }

  public Shape getBorderShape()
  {
    if (borderShape != null)
    {
      return borderShape;
    }

    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    final long x = box.getX() + (sblp.getBorderLeft() / 2);
    final long y = box.getY() + (sblp.getBorderTop() / 2);
    final long w = box.getWidth() - ((sblp.getBorderLeft() + sblp.getBorderRight()) / 2);
    final long h = box.getHeight() - ((sblp.getBorderTop() + sblp.getBorderBottom()) / 2);

    final Border border = box.getBoxDefinition().getBorder();
    final long topLeftWidth = border.getTopLeft().getWidth();
    final long topLeftHeight = border.getTopLeft().getHeight();
    final long topRightWidth;
    final long topRightHeight;
    final long bottomLeftWidth;
    final long bottomLeftHeight;
    final long bottomRightWidth;
    final long bottomRightHeight;
    if (isSameForAllSides())
    {
      topRightWidth = topLeftWidth;
      topRightHeight = topLeftHeight;
      bottomLeftWidth = topLeftWidth;
      bottomLeftHeight = topLeftHeight;
      bottomRightWidth = topLeftWidth;
      bottomRightHeight = topLeftHeight;
    }
    else
    {
      topRightWidth = border.getTopRight().getWidth();
      topRightHeight = border.getTopRight().getHeight();
      bottomLeftWidth = border.getBottomLeft().getWidth();
      bottomLeftHeight = border.getBottomLeft().getHeight();
      bottomRightWidth = border.getBottomRight().getWidth();
      bottomRightHeight = border.getBottomRight().getHeight();
    }

    if (topLeftHeight == 0 && topRightHeight == 0 && topLeftWidth == 0 && topRightWidth == 0 &&
        bottomLeftHeight == 0 && bottomRightHeight == 0 && bottomLeftWidth == 0 && bottomRightWidth == 0)
    {
      borderShape = new Rectangle2D.Double(StrictGeomUtility.toExternalValue(x), StrictGeomUtility.toExternalValue(y),
          StrictGeomUtility.toExternalValue(w), StrictGeomUtility.toExternalValue(h));
      return borderShape;
    }


    final GeneralPath generalPath = new GeneralPath();
    generalPath.append(new Arc2D.Double(x, y, 2 * topLeftWidth, 2 * topLeftHeight, -225, -45, Arc2D.OPEN), true);
    generalPath.lineTo((float) (x + w - topRightWidth), (float) y);//2
    generalPath.append(new Arc2D.Double(x + w - 2 * topRightWidth, y, 2 * topRightWidth, 2 * topRightHeight, 90, -45, Arc2D.OPEN), true);

    generalPath.append(new Arc2D.Double(x + w - 2 * topRightWidth, y, 2 * topRightWidth, 2 * topRightHeight, 45, -45, Arc2D.OPEN), true);
    generalPath.lineTo((float) (x + w), (float) (y + h - bottomRightHeight));//4
    generalPath.append(new Arc2D.Double(x + w - 2 * bottomRightWidth, y + h - 2 * bottomRightHeight, 2 * bottomRightWidth, 2 * bottomRightHeight, 0, -45, Arc2D.OPEN), true);

    generalPath.append(new Arc2D.Double(x + w - 2 * bottomRightWidth, y + h - 2 * bottomRightHeight, 2 * bottomRightWidth, 2 * bottomRightHeight, -45, -45, Arc2D.OPEN), true);
    generalPath.lineTo((float) (x + bottomLeftWidth), (float) (y + h));//6
    generalPath.append(new Arc2D.Double(x, y + h - 2 * bottomLeftHeight, 2 * bottomLeftWidth, 2 * bottomLeftHeight, -90, -45, Arc2D.OPEN), true);

    generalPath.append(new Arc2D.Double(x, y + h - 2 * bottomLeftHeight, 2 * bottomLeftWidth, 2 * bottomLeftHeight, -135, -45, Arc2D.OPEN), true);
    generalPath.lineTo((float) x, (float) (y + topLeftHeight));//8
    generalPath.append(new Arc2D.Double(x, y, 2 * topLeftWidth, 2 * topLeftHeight, -180, -45, Arc2D.OPEN), true);

    generalPath.closePath();
    generalPath.transform(scaleInstance);
    borderShape = generalPath;
    return generalPath;
  }

  public Shape getBorderTopShape()
  {
    if (borderShapeTop != null)
    {
      return borderShapeTop;
    }

    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    final long x = box.getX() + (sblp.getBorderLeft() / 2);
    final long y = box.getY() + (sblp.getBorderTop() / 2);
    final long w = box.getWidth() - ((sblp.getBorderLeft() + sblp.getBorderRight()) / 2);

    final Border border = box.getBoxDefinition().getBorder();
    final long topLeftWidth = border.getTopLeft().getWidth();
    final long topLeftHeight = border.getTopLeft().getHeight();
    final long topRightWidth = border.getTopRight().getWidth();
    final long topRightHeight = border.getTopRight().getHeight();

    if (topLeftWidth == 0 && topRightWidth == 0 && topLeftHeight == 0 && topRightHeight == 0)
    {
      final long borderWidth = sblp.getBorderTop() / 2;
      borderShapeTop = new Line2D.Double
          (StrictGeomUtility.toExternalValue(x + borderWidth), StrictGeomUtility.toExternalValue(y),
           StrictGeomUtility.toExternalValue(x + w - borderWidth), StrictGeomUtility.toExternalValue(y));
      return borderShapeTop;
    }

    final GeneralPath generalPath = new GeneralPath();
    generalPath.append(new Arc2D.Double(x, y, 2 * topLeftWidth, 2 * topLeftHeight, -225, -45, Arc2D.OPEN), true);
    generalPath.lineTo((float) (x + w - topRightWidth), (float) y);//2
    generalPath.append(new Arc2D.Double(x + w - 2 * topRightWidth, y, 2 * topRightWidth, 2 * topRightHeight, 90, -45, Arc2D.OPEN), true);
    generalPath.transform(scaleInstance);
    borderShapeTop = generalPath;
    return generalPath;
  }

  public Shape getBorderBottomShape()
  {
    if (borderShapeBottom != null)
    {
      return borderShapeBottom;
    }

    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    final long x = box.getX() + (sblp.getBorderLeft() / 2);
    final long y = box.getY() + (sblp.getBorderTop() / 2);
    final long w = box.getWidth() - ((sblp.getBorderLeft() + sblp.getBorderRight()) / 2);
    final long h = box.getHeight() - ((sblp.getBorderTop() + sblp.getBorderBottom()) / 2);

    final Border border = box.getBoxDefinition().getBorder();
    final long bottomLeftWidth = border.getBottomLeft().getWidth();
    final long bottomLeftHeight = border.getBottomLeft().getHeight();
    final long bottomRightWidth = border.getBottomRight().getWidth();
    final long bottomRightHeight = border.getBottomRight().getHeight();

    if (bottomLeftWidth == 0 && bottomRightWidth == 0 && bottomLeftHeight == 0 && bottomRightHeight == 0)
    {
      final long borderWidth = sblp.getBorderBottom() / 2;
      borderShapeBottom = new Line2D.Double
          (StrictGeomUtility.toExternalValue(x + borderWidth), StrictGeomUtility.toExternalValue(y + h),
           StrictGeomUtility.toExternalValue(x + w - borderWidth), StrictGeomUtility.toExternalValue(y + h));
      return borderShapeBottom;
    }

    final GeneralPath generalPath = new GeneralPath();
    generalPath.append(new Arc2D.Double(x + w - 2 * bottomRightWidth, y + h - 2 * bottomRightHeight, 2 * bottomRightWidth, 2 * bottomRightHeight, -45, -45, Arc2D.OPEN), true);
    generalPath.lineTo((float) (x + bottomLeftWidth), (float) (y + h));//6
    generalPath.append(new Arc2D.Double(x, y + h - 2 * bottomLeftHeight, 2 * bottomLeftWidth, 2 * bottomLeftHeight, -90, -45, Arc2D.OPEN), true);
    generalPath.transform(scaleInstance);
    borderShapeBottom = generalPath;
    return generalPath;
  }


  public Shape getBorderLeftShape()
  {
    if (borderShapeLeft != null)
    {
      return borderShapeLeft;
    }

    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    final long x = box.getX() + (sblp.getBorderLeft() / 2);
    final long y = box.getY() + (sblp.getBorderTop() / 2);
    final long h = box.getHeight() - ((sblp.getBorderTop() + sblp.getBorderBottom()) / 2);

    final Border border = box.getBoxDefinition().getBorder();
    final long topLeftWidth = border.getTopLeft().getWidth();
    final long topLeftHeight = border.getTopLeft().getHeight();
    final long bottomLeftWidth = border.getBottomLeft().getWidth();
    final long bottomLeftHeight = border.getBottomLeft().getHeight();

    if (bottomLeftWidth == 0 && topLeftWidth == 0 && bottomLeftHeight == 0 && topLeftHeight == 0)
    {
      final long borderWidth = sblp.getBorderLeft() / 2;
      borderShapeLeft = new Line2D.Double
          (StrictGeomUtility.toExternalValue(x), StrictGeomUtility.toExternalValue(y + borderWidth),
           StrictGeomUtility.toExternalValue(x), StrictGeomUtility.toExternalValue(y + h - borderWidth));
      return borderShapeLeft;
    }

    final GeneralPath generalPath = new GeneralPath();
    generalPath.append(new Arc2D.Double(x, y + h - 2 * bottomLeftHeight, 2 * bottomLeftWidth, 2 * bottomLeftHeight, -135, -45, Arc2D.OPEN), true);
    generalPath.lineTo((float) x, (float) (y + topLeftHeight));//8
    generalPath.append(new Arc2D.Double(x, y, 2 * topLeftWidth, 2 * topLeftHeight, -180, -45, Arc2D.OPEN), true);
    generalPath.transform(scaleInstance);
    borderShapeLeft = generalPath;
    return generalPath;
  }

  public Shape getBorderRightShape()
  {
    if (borderShapeRight != null)
    {
      return borderShapeRight;
    }

    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    final long x = box.getX() + (sblp.getBorderLeft() / 2);
    final long y = box.getY() + (sblp.getBorderTop() / 2);
    final long w = box.getWidth() - ((sblp.getBorderLeft() + sblp.getBorderRight()) / 2);
    final long h = box.getHeight() - ((sblp.getBorderTop() + sblp.getBorderBottom()) / 2);

    final Border border = box.getBoxDefinition().getBorder();
    final long topRightWidth = border.getTopRight().getWidth();
    final long topRightHeight = border.getTopRight().getHeight();
    final long bottomRightWidth = border.getBottomRight().getWidth();
    final long bottomRightHeight = border.getBottomRight().getHeight();

    if (topRightWidth == 0 && bottomRightWidth == 0 && topRightHeight == 0 && bottomRightHeight == 0)
    {
      final long borderWidth = sblp.getBorderRight() / 2;
      borderShapeRight = new Line2D.Double
          (StrictGeomUtility.toExternalValue(x + w), StrictGeomUtility.toExternalValue(y - borderWidth),
           StrictGeomUtility.toExternalValue(x + w), StrictGeomUtility.toExternalValue(y + h - borderWidth));
      return borderShapeRight;
    }

    final GeneralPath generalPath = new GeneralPath();
    generalPath.append(new Arc2D.Double(x + w - 2 * topRightWidth, y, 2 * topRightWidth, 2 * topRightHeight, 45, -45, Arc2D.OPEN), true);
    generalPath.lineTo((float) (x + w), (float) (y + h - bottomRightHeight));//4
    generalPath.append(new Arc2D.Double(x + w - 2 * bottomRightWidth, y + h - 2 * bottomRightHeight, 2 * bottomRightWidth, 2 * bottomRightHeight, 0, -45, Arc2D.OPEN), true);
    generalPath.transform(scaleInstance);
    borderShapeRight = generalPath;
    return generalPath;
  }
}
