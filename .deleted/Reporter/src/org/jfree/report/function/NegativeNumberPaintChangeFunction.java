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
 * NegativeNumberPaintChangeFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import java.awt.Color;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleKeys;

/**
 * This function changes the color of the named elements according to the current value of a numeric field. If the value
 * of the field is not numeric (or null), the positive color is set.
 *
 * @author Thomas Morgner
 * @deprecated The same thing can be achieved using a simple StyleExpression on the element's PAINT stylekey.
 */
public class NegativeNumberPaintChangeFunction extends AbstractElementFormatFunction
{
  /**
     * The name of the data-row destColumn from where to read the numeric value.
     */
  private String field;
  /**
   * The color that is used for positive values.
   */
  private Color positiveColor;
  /**
   * The color that is used for negative values.
   */
  private Color negativeColor;
  /**
   * The color that is used for zero values.
   */
  private Color zeroColor;

  /**
   * Default Constructor.
   */
  public NegativeNumberPaintChangeFunction()
  {
  }

  /**
   * Applies the computed color to all elements with the defined name.
   *
   * @param b the band to which the color is applied.
   */
  protected void processRootBand(final Band b)
  {
    final Element[] elements = FunctionUtilities.findAllElements(b, getElement());
    if (elements.length == 0)
    {
      return;
    }

    final Color color = computeColor();
    for (int i = 0; i < elements.length; i++)
    {
      elements[i].getStyle().setStyleProperty(ElementStyleKeys.PAINT, color);
    }
  }

  /**
   * Computes the color that is applied to the elements.
   *
   * @return the computed color.
   */
  protected Color computeColor()
  {
    final Object o = getDataRow().get(getField());
    if (o instanceof Number == false)
    {
      return getPositiveColor();
    }
    final Number n = (Number) o;
    final double d = n.doubleValue();
    if (d < 0)
    {
      return getNegativeColor();
    }
    if (d > 0)
    {
      return getPositiveColor();
    }
    final Color zeroColor = getZeroColor();
    if (zeroColor == null)
    {
      return getPositiveColor();
    }
    return zeroColor;
  }

  /**
   * Returns the color that is used if the number read from the field is positive.
   *
   * @return the color for positive values.
   */
  public Color getPositiveColor()
  {
    return positiveColor;
  }

  /**
   * Defines the color that is used if the number read from the field is positive.
   *
   * @param positiveColor the color for positive values.
   */
  public void setPositiveColor(final Color positiveColor)
  {
    this.positiveColor = positiveColor;
  }

  /**
   * Returns the color that is used if the number read from the field is negative.
   *
   * @return the color for negative values.
   */
  public Color getNegativeColor()
  {
    return negativeColor;
  }

  /**
   * Defines the color that is used if the number read from the field is negative.
   *
   * @param negativeColor the color for negative values.
   */
  public void setNegativeColor(final Color negativeColor)
  {
    this.negativeColor = negativeColor;
  }

  /**
   * Returns the color that is used if the number read from the field is zero.
   *
   * @return the color for zero values.
   */
  public Color getZeroColor()
  {
    return zeroColor;
  }

  /**
   * Defines the color that is used if the number read from the field is zero.
   *
   * @param zeroColor the color for zero values.
   */
  public void setZeroColor(final Color zeroColor)
  {
    this.zeroColor = zeroColor;
  }

  /**
     * Returns the name of the data-row destColumn from where to read the numeric value.
     *
     * @return the field name.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Defines the name of the data-row destColumn from where to read the numeric value.
     *
     * @param field the field name.
     */
  public void setField(final String field)
  {
    this.field = field;
  }
}
