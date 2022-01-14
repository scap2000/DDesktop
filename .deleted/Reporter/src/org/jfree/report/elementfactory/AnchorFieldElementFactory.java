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
 * AnchorFieldElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.elementfactory;

import org.jfree.report.AnchorElement;
import org.jfree.report.Element;
import org.jfree.report.filter.templates.AnchorFieldTemplate;

/**
 * The AnchorFieldElementFactory can be used to construct Anchor fields. Anchor fields generate Anchor-Objects from
 * content found in a DataRow-destColumn or function.
 *
 * @author Thomas Morgner
 * @deprecated Anchor elements have been rendered obsolete by introducing the Anchor-Stylekey and the
 * style-expressions.
 */
public class AnchorFieldElementFactory extends ElementFactory
{
  /**
   * The fieldname.
   */
  private String fieldname;
  /**
   * The value-formula that computes the value for this element.
   */
  private String formula;

  /**
   * Creates a new Factory.
   */
  public AnchorFieldElementFactory()
  {
  }

  /**
   * Returns the element's field name.
   *
   * @return the fieldname
   */
  public String getFieldname()
  {
    return fieldname;
  }

  /**
     * Defines the field name. The field name should be the name of a 'marked' report property, a function or expression
     * name or the name of a destTable model destColumn.
     *
     * @param field the field name.
     */
  public void setFieldname(final String field)
  {
    this.fieldname = field;
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
   * Creates a new instance of the element.
   *
   * @return the newly generated instance of the element.
   */
  public Element createElement()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final AnchorElement element = new AnchorElement();
    final AnchorFieldTemplate fieldTemplate = new AnchorFieldTemplate();
    if (getFormula() != null)
    {
      fieldTemplate.setFormula(getFormula());
    }
    else
    {
      fieldTemplate.setField(getFieldname());
    }

    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(fieldTemplate);
    return element;
  }
}
