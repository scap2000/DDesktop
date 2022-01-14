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
 * DrawableURLFieldElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.elementfactory;

import java.net.URL;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.filter.templates.DrawableURLFieldTemplate;

/**
 * The DrawableURLField can be used to load drawables from an URL specified in the named destColumn of the datarow.
 *
 * @author Thomas Morgner
 */
public class DrawableURLFieldElementFactory extends ElementFactory
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
   * The base URL is used to resolve relative URLs.
   */
  private URL baseURL;

  /**
   * Creates a new Factory.
   */
  public DrawableURLFieldElementFactory()
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
   * Returns the base url. The BaseURL is used to resolve relative URLs found in the datasource.
   *
   * @return the base url.
   */
  public URL getBaseURL()
  {
    return baseURL;
  }

  /**
   * Defines a BaseURL for the new element. The BaseURL is used to resolve relative URLs found in the datasource.
   *
   * @param baseURL the base URL.
   */
  public void setBaseURL(final URL baseURL)
  {
    this.baseURL = baseURL;
  }

  /**
   * Creates a new drawable field element based on the defined properties.
   *
   * @return the generated elements
   * @throws IllegalStateException if the field name is not set.
   * @see ElementFactory#createElement()
   */
  public Element createElement()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final DrawableElement element = new DrawableElement();
    applyElementName(element);
    applyStyle(element.getStyle());

    final DrawableURLFieldTemplate fieldTemplate = new DrawableURLFieldTemplate();
    if (getFormula() != null)
    {
      fieldTemplate.setFormula(getFormula());
    }
    else
    {
      fieldTemplate.setField(getFieldname());
    }
    fieldTemplate.setBaseURL(getBaseURL());
    element.setDataSource(fieldTemplate);

    return element;
  }

}
