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
 * LineReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;

import org.jfree.report.Element;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportElementReadHandler;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.StyleExpressionHandler;
import org.jfree.report.util.ShapeTransform;
import org.jfree.util.Log;
import org.jfree.xmlns.common.ParserUtil;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.XmlReadHandler;

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
 * LineReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class LineReadHandler extends AbstractPropertyXmlReadHandler
  implements ReportElementReadHandler
{
  private Element element;
  private static final String NAME_ATT = "name";
  private static final String COLOR_ATT = "color";
  private ArrayList styleExpressionHandlers;

  public LineReadHandler ()
  {
    styleExpressionHandlers = new ArrayList();
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
    final float x1 = ReportParserUtil.parseRelativeFloat
        (atts.getValue(getUri(), "x1"), "Element x1 not specified", getLocator());
    final float y1 = ReportParserUtil.parseRelativeFloat
        (atts.getValue(getUri(), "y1"), "Element y1 not specified", getLocator());
    final float x2 = ReportParserUtil.parseRelativeFloat
        (atts.getValue(getUri(), "x2"), "Element x2 not specified", getLocator());
    final float y2 = ReportParserUtil.parseRelativeFloat
        (atts.getValue(getUri(), "y2"), "Element y2 not specified", getLocator());

    if (x1 == x2 && y1 == y2)
    {
      Log.warn("creating a horizontal line with 'x1 == x2 && y1 == y2' is deprecated. " +
               "Use relative coordinates instead.");
      final Stroke stroke = readStroke(atts);
      final String name = atts.getValue(getUri(), NAME_ATT);
      final Color c = ReportParserUtil.parseColor(atts.getValue(getUri(), COLOR_ATT));
      element = StaticShapeElementFactory.createHorizontalLine(name, c, stroke, y2);
      return;
    }


    final boolean relativeSizes = (x1 < 0) || (y1 < 0) || (x2 < 0) || (y2 < 0);


    if (relativeSizes == false)
    {
      createSimpleLine(atts, x1, y1, x2, y2);
    }
    else
    {
      createRelativeLine(atts, x1, y1, x2, y2);
    }
  }

  private void createRelativeLine (final PropertyAttributes atts,
                                   final float x1,
                                   final float y1,
                                   final float x2,
                                   final float y2)
          throws SAXException
  {
    final Stroke stroke = readStroke(atts);
    final String name = atts.getValue(getUri(), NAME_ATT);
    final Color c = ReportParserUtil.parseColor
        (atts.getValue(getUri(), COLOR_ATT), null);

    final String widthValue = atts.getValue(getUri(), "width");
    final float width;
    if (widthValue != null)
    {
      width = ReportParserUtil.parseRelativeFloat
          (widthValue, "Width is invalid", getLocator());
    }
    else
    {
      width = computeDimension(name, x1, x2);
    }

    final String heightValue = atts.getValue(getUri(), "height");
    final float height;
    if (heightValue != null)
    {
      height = ReportParserUtil.parseRelativeFloat
          (heightValue, "Height is invalid", getLocator());
    }
    else
    {
      height = computeDimension(name, y1, y2);
    }

    // create the bounds as specified by the user
    final Rectangle2D bounds =
            new Rectangle2D.Float(computePosition(name, x1,x2),
                    computePosition(name, y1, y2), width, height);

    // here comes the magic - we transform the line into the absolute space;
    // this should preserve the general appearance. Heck, and if not, then
    // it is part of the users reponsibility to resolve that. Magic does not
    // solve all problems, you know.
    final Line2D line = new Line2D.Float
            (Math.abs(x1), Math.abs(y1), Math.abs(x2), Math.abs(y2));
    final Rectangle2D shapeBounds = line.getBounds2D();
    final Shape transformedShape =
            ShapeTransform.translateShape(line, -shapeBounds.getX(), -shapeBounds.getY());
    // and use that shape with the user's bounds to create the element.
    element = StaticShapeElementFactory.createShapeElement
            (name, bounds, c, stroke, transformedShape, true, false, true, false);
  }

  private float computePosition (final String name, final float x1, final float x2)
  {
    final boolean x1Relative;
    final boolean x2Relative;

    if (x1 == 0)
    {
      x1Relative = x2 < 0;
      x2Relative = x1Relative;
    }
    else if (x2 == 0)
    {
      x1Relative = x1 < 0;
      x2Relative = x1Relative;
    }
    else
    {
      x1Relative = x1 < 0;
      x2Relative = x2 < 0;
    }

    if (x1Relative && x2Relative)
    {
      // relative sizes are given as negative numbers.
      return Math.max(x2,x1);
    }
    else if (x1Relative == false && x2Relative == false)
    {
      // absolute sizes are given as positive numbers.
      return Math.min(x2,x1);
    }
    else
    {
      Log.warn ("Mixing relative and absolute positions in '" + name + "'. " +
              "The definition is ambigous. (" + x1 + ", " +x2 + ')');

      // return the absolute element as computed position.
      if (x1Relative)
      {
        return x2;
      }
      else
      {
        return x1;
      }
    }
  }

  private float computeDimension(final String name, final float x1, final float x2)
  {
    final boolean x1Relative;
    final boolean x2Relative;

    if (x1 == 0)
    {
      x1Relative = x2 < 0;
      x2Relative = x1Relative;
    }
    else if (x2 == 0)
    {
      x1Relative = x1 < 0;
      x2Relative = x1Relative;
    }
    else
    {
      x1Relative = x1 < 0;
      x2Relative = x2 < 0;
    }

    if (x1Relative && x2Relative)
    {
      // relative sizes are given as negative numbers.
      return Math.min(x2,x1) - Math.max(x2,x1);
    }
    else if (x1Relative == false && x2Relative == false)
    {
      // absolute sizes are given as positive numbers.
      return Math.max(x2,x1) - Math.min(x2,x1);
    }
    else
    {
      // use the relative element as width ..
      Log.warn ("Mixing relative and absolute sizes in '" + name + "'. " +
                "The definition is ambigous. (" + x1 + ", " +x2 + ')');
      if (x1Relative)
      {
        return x1;
      }
      else
      {
        return x2;
      }
    }
  }

  private void createSimpleLine(final PropertyAttributes atts,
                                final float x1,
                                final float y1,
                                final float x2,
                                final float y2)
          throws SAXException
  {
    final Stroke stroke = readStroke(atts);
    final String name = atts.getValue(getUri(), NAME_ATT);
    final Color c = ReportParserUtil.parseColor(atts.getValue(getUri(), COLOR_ATT), null);
    final String widthValue = atts.getValue(getUri(), "width");
    final float width;
    if (widthValue != null)
    {
      width = ReportParserUtil.parseRelativeFloat
          (widthValue, "Width is invalid", getLocator());
    }
    else
    {
      width = Math.max(x2,x1) - Math.min(x2,x1);
    }

    final String heightValue = atts.getValue(getUri(), "height");
    final float height;
    if (heightValue != null)
    {
      height = ReportParserUtil.parseRelativeFloat
          (heightValue, "Height is invalid", getLocator());
    }
    else
    {
      height = Math.max(y2,y1) - Math.min(y2,y1);
    }

    // create the bounds as specified by the user
    final Rectangle2D bounds =
            new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), width, height);
    // first version of the line (not originating @(0,0))
    final Line2D line = new Line2D.Float(x1, y1, x2, y2);
    // the bounds of that line
    final Rectangle2D shapeBounds = line.getBounds2D();
    // translate so that the shape starts at position (0,0)
    final Shape transformedShape =
            ShapeTransform.translateShape(line, -shapeBounds.getX(), -shapeBounds.getY());
    // and use that shape with the user's bounds to create the element.
    element = StaticShapeElementFactory.createShapeElement
            (name, bounds, c, stroke, transformedShape, true, false, true, false);
  }


  private Stroke readStroke (final PropertyAttributes atts)
      throws ParseException
  {
    final String strokeStyle = atts.getValue(getUri(), "stroke-style");
    final String weightAttr = atts.getValue(getUri(), "weight");
    float weight = 1;
    if (weightAttr != null)
    {
      weight = ParserUtil.parseFloat
          (weightAttr, "Weight is given, but no number.", getLocator());
    }

    // "dashed | solid | dotted | dot-dot-dash | dot-dash"
    return ReportParserUtil.parseStroke(strokeStyle, weight);
  }

  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final PropertyAttributes attrs)
      throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if ("style-expression".equals(tagName))
    {
      final StyleExpressionHandler handler = new StyleExpressionHandler();
      styleExpressionHandlers.add(handler);
      return handler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    for (int i = 0; i < styleExpressionHandlers.size(); i++)
    {
      final StyleExpressionHandler handler =
          (StyleExpressionHandler) styleExpressionHandlers.get(i);
      element.setStyleExpression(handler.getKey(), handler.getExpression());
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return element;
  }

}
