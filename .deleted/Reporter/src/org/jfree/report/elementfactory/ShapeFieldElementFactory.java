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
 * ShapeFieldElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ShapeElement;
import org.jfree.report.filter.templates.ShapeFieldTemplate;

/**
 * A factory to define Shape field elements.
 *
 * @author Thomas Morgner
 */
public class ShapeFieldElementFactory extends ShapeElementFactory
{
  /**
   * The fieldname of the datarow from where to read the content.
   */
  private String fieldname;
  /**
   * The value-formula that computes the value for this element.
   */
  private String formula;

  /**
   * DefaultConstructor.
   */
  public ShapeFieldElementFactory ()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public String getFieldname ()
  {
    return fieldname;
  }

  /**
     * Defines the field name from where to read the content of the element. The field name
     * is the name of a datarow destColumn.
     *
     * @param fieldname the field name.
     */
  public void setFieldname (final String fieldname)
  {
    this.fieldname = fieldname;
  }

  /**
   * Returns the formula that should be used to compute the value of the field.
   * The formula must be valid according to the OpenFormula
   * specifications.
   *
   * @return the formula as string.
   */
  public String getFormula()
  {
    return formula;
  }

  /**
   * Assigns a formula to the element to compute the value for this element.
   * If a formula is defined, it will override the 'field' property.
   *
   * @param formula the formula as a string.
   */
  public void setFormula(final String formula)
  {
    this.formula = formula;
  }

  /**
   * Creates the shape field element.
   *
   * @return the generated shape field element
   *
   * @throws IllegalStateException if the fieldname is not defined.
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }
    final ShapeElement e = new ShapeElement();
    applyStyle(e.getStyle());
    applyElementName(e);

    final ShapeFieldTemplate template = new ShapeFieldTemplate();
    if (getFormula() != null)
    {
      template.setFormula(getFormula());
    }
    else
    {
      template.setField(getFieldname());
    }
    e.setDataSource(template);
    return e;
  }

  /**
   * Creates a new ShapeElement.
   *
   * @param name            the name of the new element.
   * @param bounds          the bounds.
   * @param paint           the line color of this element.
   * @param stroke          the stroke of this shape. For pdf use, restrict to
   *                        BasicStrokes.
   * @param fieldname       the fieldname from where to get the shape.
   * @param shouldDraw      draw the shape?
   * @param shouldFill      fill the shape?
   * @param shouldScale     scale the shape?
   * @param keepAspectRatio preserve the aspect ratio?
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException     if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement (final String name,
                                                 final Rectangle2D bounds,
                                                 final Color paint,
                                                 final Stroke stroke,
                                                 final String fieldname,
                                                 final boolean shouldDraw,
                                                 final boolean shouldFill,
                                                 final boolean shouldScale,
                                                 final boolean keepAspectRatio)
  {
    final ShapeFieldElementFactory factory = new ShapeFieldElementFactory();
    factory.setName(name);
    factory.setX(new Float(bounds.getX()));
    factory.setY(new Float(bounds.getY()));
    factory.setMinimumWidth(new Float(bounds.getWidth()));
    factory.setMinimumHeight(new Float(bounds.getHeight()));
    factory.setColor(paint);
    factory.setKeepAspectRatio(ElementFactory.getBooleanValue(keepAspectRatio));
    factory.setFieldname(fieldname);
    factory.setStroke(stroke);
    factory.setShouldDraw(ElementFactory.getBooleanValue(shouldDraw));
    factory.setShouldFill(ElementFactory.getBooleanValue(shouldFill));
    factory.setScale(ElementFactory.getBooleanValue(shouldScale));
    return (ShapeElement) factory.createElement();
  }

}
