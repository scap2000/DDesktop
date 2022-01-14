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
 * AverageExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An expression that takes values from one or more fields and returns the average of
 * them.
 *
 * @deprecated this has been replaced by the ColumnAverageExpression.
 * @author Thomas Morgner
 */
public class AverageExpression extends AbstractExpression
{
  /**
   * An ordered list containing the fieldnames used in the expression.
   */
  private ArrayList fieldList;
  /**
   * The scale-property defines the precission of the divide-operation.
   */
  private int scale;
  /**
   * The rounding-property defines the precission of the divide-operation.
   */
  private int roundingMode;

  /**
   * Creates a new expression.  The fields used by the expression are defined using
   * properties named '0', '1', ... 'N'.  These fields should contain {@link Number}
   * instances.
   */
  public AverageExpression ()
  {
    this.fieldList = new ArrayList();
    scale = 14;
    roundingMode = BigDecimal.ROUND_HALF_UP;
  }

  /**
   * Returns the defined rounding mode. This influences the precision of the divide-operation.
   *
   * @return the rounding mode.
   * @see java.math.BigDecimal#divide(java.math.BigDecimal,int)
   */
  public int getRoundingMode()
  {
    return roundingMode;
  }

  /**
   * Defines the rounding mode. This influences the precision of the divide-operation.
   *
   * @param roundingMode the rounding mode.
   * @see java.math.BigDecimal#divide(java.math.BigDecimal,int)
   */
  public void setRoundingMode(final int roundingMode)
  {
    this.roundingMode = roundingMode;
  }

  /**
   * Returns the scale for the divide-operation. The scale influences the precision of the division.
   *
   * @return the scale.
   */
  public int getScale()
  {
    return scale;
  }

  /**
   * Defines the scale for the divide-operation. The scale influences the precision of the division.
   *
   * @param scale the scale.
   */
  public void setScale(final int scale)
  {
    this.scale = scale;
  }


  /**
   * Returns the average of the values.
   *
   * @return a BigDecimal instance.
   */
  public Object getValue ()
  {
    final Number[] values = collectValues();
    BigDecimal total = new BigDecimal(0.0);
    int count = 0;
    for (int i = 0; i < values.length; i++)
    {
      final Number n = values[i];
      if (n != null)
      {
        total = total.add(new BigDecimal(n.toString()));
        count++;
      }
    }
    if (count > 0)
    {
      return total.divide(new BigDecimal(count), scale, roundingMode);
    }
    return new BigDecimal(0.0);
  }

  /**
   * collects the values of all fields defined in the fieldList.
   *
   * @return an Objectarray containing all defined values from the datarow
   */
  private Number[] collectValues ()
  {
    final Number[] retval = new Number[this.fieldList.size()];
    for (int i = 0; i < this.fieldList.size(); i++)
    {
      final String field = (String) this.fieldList.get(i);
      retval[i] = (Number) getDataRow().get(field);
    }
    return retval;
  }

  /**
   * Clones the expression.
   *
   * @return A copy of this expression.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final AverageExpression ae = (AverageExpression) super.clone();
    ae.fieldList = (ArrayList) fieldList.clone();
    return ae;
  }

  /**
   * Returns the defined fields as array.
   *
   * @return the fields
   */
  public String[] getField ()
  {
    return (String[]) fieldList.toArray(new String[fieldList.size()]);
  }

  /**
   * Defines all fields as array. This completely replaces any previously defined fields.
   *
   * @param fields the new list of fields.
   */
  public void setField (final String[] fields)
  {
    this.fieldList.clear();
    this.fieldList.addAll(Arrays.asList(fields));
  }

  /**
   * Returns the defined field at the given index-position.
   *
   * @param index the position of the field name that should be queried.
   * @return the field name at the given position.
   */
  public String getField (final int index)
  {
    return (String) this.fieldList.get(index);
  }

  /**
   * Defines the field in the field-list at the given index.
   *
   * @param index the position in the list, where the field should be defined.
   * @param field the name of the field.
   */
  public void setField (final int index, final String field)
  {
    if (this.fieldList.size() == index)
    {
      this.fieldList.add(field);
    }
    else
    {
      this.fieldList.set(index, field);
    }
  }

  /**
   * Returns the number of fields defined in this expression.
   * @return the number of fields.
   */
  public int getFieldCount()
  {
    return fieldList.size();
  }
}
