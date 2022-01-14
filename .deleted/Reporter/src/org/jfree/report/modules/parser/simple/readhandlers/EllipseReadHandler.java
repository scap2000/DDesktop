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
 * EllipseReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;

import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;

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
 * EllipseReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class EllipseReadHandler extends AbstractShapeElementReadHandler
{
  private StaticShapeElementFactory elementFactory;

  public EllipseReadHandler ()
  {
    elementFactory = new StaticShapeElementFactory();
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException
  {
    super.startParsing(atts);
    if (elementFactory.getShouldDraw() == null)
    {
      elementFactory.setShouldDraw(Boolean.TRUE);
    }
    if (elementFactory.getShouldFill() == null)
    {
      elementFactory.setShouldFill(Boolean.TRUE);
    }
    elementFactory.setScale(Boolean.TRUE);
    elementFactory.setDynamicHeight(Boolean.FALSE);
    elementFactory.setKeepAspectRatio(Boolean.FALSE);

    final Dimension2D size = elementFactory.getMinimumSize();
    if (size == null)
    {
      elementFactory.setShape(new Ellipse2D.Float(0, 0, 100, 100));
    }
    else
    {
      // this helps to keep the rounded corners in shape. This will not help
      // for elements which are defined with percentages, but at least it
      // stops some of the weird stuff.
      //
      // Later, such rounded background should be done by a suitable border definition.
      elementFactory.setShape(new Ellipse2D.Double
              (0, 0, Math.abs(size.getWidth()), Math.abs(size.getHeight())));
    }
  }

  protected ElementFactory getElementFactory ()
  {
    return elementFactory;
  }
}
