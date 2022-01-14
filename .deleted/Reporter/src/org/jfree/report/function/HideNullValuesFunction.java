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
 * HideNullValuesFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import org.jfree.report.Band;
import org.jfree.report.Element;

/**
 * Hides the specified elements if the given field contains empty strings or
 * zero numbers.
 *
 * @author Thomas Morgner
 */
public class HideNullValuesFunction extends AbstractElementFormatFunction
{
  /**
     * The name of the data-row destColumn that is checked for null-values.
     */
  private String field;

  /**
   * Default Constructor.
   */
  public HideNullValuesFunction()
  {
  }

  /**
     * Returns the field used by the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @return The field name.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Sets the field name for the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @param field the field name.
     */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Applies the computed visiblity to all child elements of the given band.
   *
   * @param b the visibility.
   */
  protected void processRootBand(final Band b)
  {
    final Element[] elements =
            FunctionUtilities.findAllElements(b, getElement());
    final boolean visible = computeVisibility();

    for (int i = 0; i < elements.length; i++)
    {
      final Element element = elements[i];
      element.setVisible(visible);
    }
  }

  /**
   * Computes the visiblity of the specified element.
   *
   * @return true, if the element should be visible, false otherwise.
   */
  protected boolean computeVisibility()
  {
    final Object value = getDataRow().get(getField());
    if (value == null)
    {
      return false;
    }
    if (value instanceof String)
    {
      final String strValue = (String) value;
      return strValue.trim().length() > 0;
    }
    if (value instanceof Number)
    {
      final Number numValue = (Number) value;
      return numValue.doubleValue() != 0;
    }
    return true;
  }
}
