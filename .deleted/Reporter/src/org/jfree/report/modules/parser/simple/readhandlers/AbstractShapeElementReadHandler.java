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
 * AbstractShapeElementReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.Stroke;

import org.jfree.report.elementfactory.ShapeElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.xmlns.common.ParserUtil;
import org.jfree.xmlns.parser.ParseException;

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
 * AbstractShapeElementReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public abstract class AbstractShapeElementReadHandler extends AbstractElementReadHandler
{
  private static final String SCALE_ATT = "scale";
  private static final String KEEP_ASPECT_RATIO_ATT = "keepAspectRatio";
  private static final String FILL_ATT = "fill";
  private static final String DRAW_ATT = "draw";

  protected AbstractShapeElementReadHandler()
  {
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing(final PropertyAttributes atts)
      throws SAXException
  {
    super.startParsing(atts);
    handleScale(atts);
    handleKeepAspectRatio(atts);
    handleFill(atts);
    handleDraw(atts);
    handleStroke(atts);
  }

  private void handleStroke(final PropertyAttributes atts)
      throws ParseException
  {
    final String strokeStyle = atts.getValue
        (getUri(), "stroke-style");

    final String weightAttr = atts.getValue(getUri(), "weight");
    float weight = 1;
    if (weightAttr != null)
    {
      weight = ParserUtil.parseFloat
          (weightAttr, "Weight is given, but no number.", getLocator());
    }

    final Stroke stroke = ReportParserUtil.parseStroke(strokeStyle, weight);

    if (stroke != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setStroke(stroke);
    }
  }

  protected void handleScale(final PropertyAttributes atts) throws ParseException
  {
    final String booleanValue = atts.getValue(getUri(), SCALE_ATT);
    final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
    final Boolean scale = ParserUtil.parseBoolean(booleanValue, getLocator());
    if (scale != null)
    {
      elementFactory.setScale(scale);
    }
  }

  protected void handleKeepAspectRatio(final PropertyAttributes atts)
      throws ParseException
  {
    final String booleanValue = atts.getValue(getUri(), KEEP_ASPECT_RATIO_ATT);
    final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
    final Boolean keepAspectRatio = ParserUtil.parseBoolean(booleanValue, getLocator());
    if (keepAspectRatio != null)
    {
      elementFactory.setKeepAspectRatio(keepAspectRatio);
    }
  }

  protected void handleFill(final PropertyAttributes atts) throws ParseException
  {
    final String booleanValue = atts.getValue(getUri(), FILL_ATT);
    final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
    final Boolean fill = ParserUtil.parseBoolean(booleanValue, getLocator());
    if (fill != null)
    {
      elementFactory.setShouldFill(fill);
    }
  }

  protected void handleDraw(final PropertyAttributes atts) throws ParseException
  {
    final String booleanValue = atts.getValue(getUri(), DRAW_ATT);
    final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
    final Boolean draw = ParserUtil.parseBoolean(booleanValue, getLocator());
    if (draw != null)
    {
      elementFactory.setShouldDraw(draw);
    }
  }

}
