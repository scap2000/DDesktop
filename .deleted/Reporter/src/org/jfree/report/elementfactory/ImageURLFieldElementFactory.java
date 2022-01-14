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
 * ImageURLFieldElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.elementfactory;

import java.awt.geom.Rectangle2D;

import java.net.URL;

import org.jfree.report.Element;
import org.jfree.report.ImageElement;
import org.jfree.report.filter.templates.ImageURLFieldTemplate;

/**
 * A factory to define ImageURLFieldElements. These elements expect an java.net.URL or an
 * String as content and will display the image content of that URL in the report.
 *
 * @author Thomas Morgner
 */
public class ImageURLFieldElementFactory extends ImageElementFactory
{
  /**
   * The fieldname of the datarow from where to read the content.
   */
  private String fieldname;
  /**
   * The value-formula that computes the value for this element.
   */
  private String formula;

  /** The base URL is used to resolve relative URLs. */
  private URL baseURL;

  /**
   * DefaultConstructor.
   */
  public ImageURLFieldElementFactory ()
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
   * Returns the base url. The BaseURL is used to resolve relative URLs found in the
   * datasource.
   *
   * @return the base url.
   */
  public URL getBaseURL ()
  {
    return baseURL;
  }

  /**
   * Defines a BaseURL for the new element. The BaseURL is used to resolve relative URLs found in the
   * datasource.
   *
   * @param baseURL the base URL.
   */
  public void setBaseURL (final URL baseURL)
  {
    this.baseURL = baseURL;
  }

  /**
   * Creates the image URL field element based on the defined properties.
   *
   * @return the created element.
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final ImageURLFieldTemplate fieldTemplate = new ImageURLFieldTemplate();
    if (getFormula() != null)
    {
      fieldTemplate.setFormula(getFormula());
    }
    else
    {
      fieldTemplate.setField(getFieldname());
    }
    fieldTemplate.setBaseURL(getBaseURL());
    final ImageElement element = new ImageElement();
    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(fieldTemplate);


    return element;
  }


  /**
     * Creates a new ImageElement, which is fed from an URL stored in the datasource.
     *
     * @param name the name of the new element
     * @param bounds the bounds of the new element
     * @param field the name of the destColumn/function/expression that returns the URL for the
     * image.
     * @return a report element for displaying an image based on a URL.
     *
     * @throws NullPointerException if bounds, name or source are null
     * @throws IllegalArgumentException if the given alignment is invalid
     */
  public static ImageElement createImageURLElement (final String name,
                                                    final Rectangle2D bounds,
                                                    final String field)
  {
    return createImageURLElement(name, bounds, field, true);
  }

  /**
     * Creates a new ImageElement, which is fed from an URL stored in the datasource.
     *
     * @param name the name of the new element.
     * @param bounds the bounds of the new element.
     * @param field the name of the destColumn/function/expression that returns the URL for the
     * image.
     * @param scale scale the image?
     * @return a report element for displaying an image based on a URL.
     *
     * @throws NullPointerException if bounds, name or source are null
     * @throws IllegalArgumentException if the given alignment is invalid
     */
  public static ImageElement createImageURLElement (final String name,
                                                    final Rectangle2D bounds,
                                                    final String field,
                                                    final boolean scale)
  {
    return createImageURLElement(name, bounds, field, scale, false);
  }

  /**
     * Creates a new ImageElement, which is fed from an URL stored in the datasource.
     *
     * @param name the name of the new element
     * @param bounds the bounds of the new element
     * @param field the name of the destColumn/function/expression that returns the
     * URL for the image.
     * @param scale true if the content should be scaled to fit.
     * @param keepAspectRatio preserve the aspect ratio.
     * @return a report element for displaying an image based on a URL.
     *
     * @throws NullPointerException if bounds, name or source are null
     * @throws IllegalArgumentException if the given alignment is invalid
     */
  public static ImageElement createImageURLElement (final String name,
                                                    final Rectangle2D bounds,
                                                    final String field,
                                                    final boolean scale,
                                                    final boolean keepAspectRatio)
  {
    final ImageURLFieldElementFactory factory = new ImageURLFieldElementFactory();
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
