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
 * ElementVisibilityFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import org.jfree.report.Band;
import org.jfree.report.Element;

/**
 * Triggers the visiblity of an element based on the boolean value read from the defined field.
 *
 * @author Thomas Morgner
 * @deprecated add a style-expression for the visible style-key instead.
 */
public class ElementVisibilityFunction extends AbstractElementFormatFunction
{
  /**
     * The field name of the data-row destColumn from where to read the boolean value.
     */
  private String field;

  /**
   * Default Constructor.
   */
  public ElementVisibilityFunction()
  {
  }

  /**
     * Returns the field name of the data-row destColumn from where to read the boolean value.
     *
     * @return the field name.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Defines field name of the data-row destColumn from where to read the boolean value.
     *
     * @param field the name of the field.
     */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Returns whether the element will be visible or not.
   *
   * @return Boolean.TRUE or Boolean.FALSE.
   */
  public Object getValue()
  {
    if (isVisible())
    {
      return Boolean.TRUE;
    }
    else
    {
      return Boolean.FALSE;
    }
  }

  /**
   * Applies the visibility to all elements of the band with the given name.
   *
   * @param b the root band.
   */
  protected void processRootBand(final Band b)
  {
    final boolean visible = isVisible();
    final Element[] elements = FunctionUtilities.findAllElements(b, getElement());
    for (int i = 0; i < elements.length; i++)
    {
      final Element element = elements[i];
      element.setVisible(visible);
    }

  }

  /**
   * Computes the visiblity of the element.
   *
   * @return true, if the field contains the Boolean.TRUE object, false otherwise.
   */
  protected boolean isVisible()
  {
    return Boolean.TRUE.equals(getDataRow().get(getField()));
  }
}
