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
 * ColumnAggregationExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.report.DataRow;

/**
 * The base-class for all expressions that aggregate values from multiple columns.
 *
 * @author Thomas Morgner
 */
public abstract class ColumnAggregationExpression extends AbstractExpression
{
  /**
   * An ordered list containing the fieldnames used in the expression.
   */
  private ArrayList fields;
  /**
   * A temporary array to reduce the number of object creations.
   */
  private transient Object[] fieldValues;

  /**
   * Default Constructor.
   */
  protected ColumnAggregationExpression()
  {
    fields = new ArrayList();
  }

  /**
   * Collects the values of all fields defined in the fieldList.
   *
   * @return an Object-array containing all defined values from the datarow
   */
  protected Object[] getFieldValues ()
  {
    final int size = fields.size();
    if (fieldValues == null || fieldValues.length != size)
    {
      fieldValues = new Object[size];
    }

    final DataRow dataRow = getDataRow();
    for (int i = 0; i < size; i++)
    {
      final String field = (String) fields.get(i);
      if (field != null)
      {
        fieldValues[i] = dataRow.get(field);
      }
    }
    return fieldValues;
  }

  /**
   * Defines the field in the field-list at the given index.
   *
   * @param index the position in the list, where the field should be defined.
   * @param field the name of the field.
   */
  public void setField (final int index, final String field)
  {
    if (fields.size() == index)
    {
      fields.add(field);
    }
    else
    {
      fields.set(index, field);
    }
    this.fieldValues = null;
  }

  /**
   * Returns the defined field at the given index-position.
   *
   * @param index the position of the field name that should be queried.
   * @return the field name at the given position.
   */
  public String getField (final int index)
  {
    return (String) fields.get(index);
  }

  /**
   * Returns the number of fields defined in this expression.
   * @return the number of fields.
   */
  public int getFieldCount ()
  {
    return fields.size();
  }

  /**
   * Returns all defined fields as array of strings.
   *
   * @return all the fields.
   */
  public String[] getField ()
  {
    return (String[]) fields.toArray(new String[fields.size()]);
  }

  /**
   * Defines all fields as array. This completely replaces any previously defined fields.
   *
   * @param fields the new list of fields.
   */
  public void setField (final String[] fields)
  {
    this.fields.clear();
    this.fields.addAll(Arrays.asList(fields));
    this.fieldValues = null;
  }

  /**
   * Creates a copy of this expression.
   *
   * @return the clone.
   * @throws CloneNotSupportedException if cloning failed for some reason.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final ColumnAggregationExpression co = (ColumnAggregationExpression) super.clone();
    co.fields = (ArrayList) fields.clone();
    return co;
  }
}
