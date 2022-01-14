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
 * ImageFieldElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.elementfactory;

import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ImageElement;
import org.jfree.report.filter.templates.ImageFieldTemplate;

/**
 * A factory to define ImageFieldElements. These elements expect an java.awt.Image or an ImageReference instance as
 * content and will display the content in the report.
 *
 * @author Thomas Morgner
 */
public class ImageFieldElementFactory extends ImageElementFactory
{
  /**
   * The fieldname of the datarow from where to read the content.
   */
  private String fieldname;
  /**
   * The value-formula for this field-element.
   */
  private String formula;

  /**
   * DefaultConstructor.
   */
  public ImageFieldElementFactory()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public String getFieldname()
  {
    return fieldname;
  }

  /**
   * Defines the field name from where to read the content of the element. The field name is the name of a datarow
   * column.
   *
   * @param fieldname the field name.
   */
  public void setFieldname(final String fieldname)
  {
    this.fieldname = fieldname;
  }

  /**
   * Returns the formula that should be used to compute the value of the field. The formula must be valid according to
   * the OpenFormula specifications.
   *
   * @return the formula as string.
   */
  public String getFormula()
  {
    return formula;
  }

  /**
   * Assigns a formula to the element to compute the value for this element. If a formula is defined, it will override
   * the 'field' property.
   *
   * @param formula the formula as a string.
   */
  public void setFormula(final String formula)
  {
    this.formula = formula;
  }

  /**
   * Creates the image element based on the defined properties.
   *
   * @return the created image element.
   * @throws IllegalStateException if the fieldname is not set.
   * @see ElementFactory#createElement()
   */
  public Element createElement()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final ImageFieldTemplate fieldTemplate = new ImageFieldTemplate();
    if (getFormula() != null)
    {
      fieldTemplate.setFormula(getFormula());
    }
    else
    {
      fieldTemplate.setField(getFieldname());
    }
    final ImageElement element = new ImageElement();
    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(fieldTemplate);

    return element;
  }


  /**
     * Creates a new ImageElement.
     *
     * @param name the name of the new element
     * @param bounds the bounds of the new element
     * @param field the name of the destColumn/function/expression that returns the URL for the image.
     * @return a report element for displaying an image.
     * @throws NullPointerException if bounds, name or source are null
     * @throws IllegalArgumentException if the given alignment is invalid
     */
  public static ImageElement createImageDataRowElement(final String name,
                                                       final Rectangle2D bounds,
                                                       final String field)
  {
    return createImageDataRowElement(name, bounds, field, true);
  }

  /**
     * Creates a new ImageElement.
     *
     * @param name the name of the new element.
     * @param bounds the bounds of the new element.
     * @param field the name of the destColumn/function/expression that returns the URL for the image.
     * @param scale scale the image?
     * @return a report element for displaying an image.
     * @throws NullPointerException if bounds, name or source are null
     * @throws IllegalArgumentException if the given alignment is invalid
     */
  public static ImageElement createImageDataRowElement(final String name,
                                                       final Rectangle2D bounds,
                                                       final String field,
                                                       final boolean scale)
  {
    return createImageDataRowElement(name, bounds, field, scale, false);
  }

  /**
     * Creates a new ImageElement.
     *
     * @param name the name of the new element.
     * @param bounds the bounds of the new element.
     * @param field the name of the destColumn/function/expression that returns the URL for the image.
     * @param scale scale the image?
     * @param keepAspectRatio preserve the aspect ratio?
     * @return a report element for displaying an image.
     * @throws NullPointerException if bounds, name or source are null
     * @throws IllegalArgumentException if the given alignment is invalid
     */
  public static ImageElement createImageDataRowElement(final String name,
                                                       final Rectangle2D bounds,
                                                       final String field,
                                                       final boolean scale,
                                                       final boolean keepAspectRatio)
  {
    final ImageFieldElementFactory factory = new ImageFieldElementFactory();
    factory.setName(name);
    factory.setX(new Float(bounds.getX()));
    factory.setY(new Float(bounds.getY()));
    factory.setMinimumWidth(new Float(bounds.getWidth()));
    factory.setMinimumHeight(new Float(bounds.getHeight()));
    factory.setScale(ElementFactory.getBooleanValue(scale));
    factory.setKeepAspectRatio(ElementFactory.getBooleanValue(keepAspectRatio));
    factory.setFieldname(field);
    return (ImageElement) factory.createElement();
  }
}
