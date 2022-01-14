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
 * DrawableFieldElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.elementfactory;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.filter.templates.DrawableFieldTemplate;

/**
 * The drawable field element factory can be used to create elements that display
 * <code>Drawable</code> elements.
 * <p/>
 * A drawable field expects the named datasource to contain Drawable objects.
 * <p/>
 * Once the desired properties are set, the factory can be reused to create similiar
 * elements.
 *
 * @author Thomas Morgner
 * @see org.jfree.ui.Drawable
 */
public class DrawableFieldElementFactory extends ElementFactory
{
  /**
   * The fieldname of the datarow from where to read the element data.
   */
  private String fieldname;
  /**
   * The value-formula that computes the value for this element.
   */
  private String formula;

  /**
   * DefaultConstructor.
   */
  public DrawableFieldElementFactory ()
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
   * Creates a new drawable field element based on the defined properties.
   *
   * @return the generated elements
   *
   * @throws IllegalStateException if the field name is not set.
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final DrawableElement element = new DrawableElement();
    applyElementName(element);
    applyStyle(element.getStyle());

    final DrawableFieldTemplate fieldTemplate = new DrawableFieldTemplate();
    if (getFormula() != null)
    {
      fieldTemplate.setFormula(getFormula());
    }
    else
    {
      fieldTemplate.setField(getFieldname());
    }
    element.setDataSource(fieldTemplate);

    return element;
  }
}
