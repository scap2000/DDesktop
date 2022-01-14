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
 * ParameterDataRow.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import org.jfree.report.states.datarow.StaticDataRow;
import org.jfree.report.util.ReportProperties;

/**
 * A static data row that reads its values from a report properties collection. Changes to the report property
 * collection do not affect the parameter-data-row.
 *
 * @author Thomas Morgner
 */
public class ParameterDataRow extends StaticDataRow
{
  /**
   * Create a parameter row from a master report's report properties collection.
   *
   * @param parameters the report parameter set.
   */
  public ParameterDataRow(final ReportProperties parameters)
  {
    final String[] names = parameters.keyArray();
    final Object[] values = new Object[names.length];

    for (int i = 0; i < names.length; i++)
    {
      final String key = names[i];
      values[i] = parameters.get(key);
    }
    setData(names, values);
  }

  /**
   * Create a parameter data row from a master report's data row and a set
   * of parameter mappings. The incoming parameters can be aliased through
   * the parameter mapping definitions.
   *
   * @param parameters the parameter mappings
   * @param dataRow the data row.
   */
  public ParameterDataRow(final ParameterMapping[] parameters, final DataRow dataRow)
  {
    final String[] innerNames = new String[parameters.length];
    final Object[] values = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++)
    {
      final ParameterMapping parameter = parameters[i];
      final String name = parameter.getName();
      innerNames[i] = parameter.getAlias();
      values[i] = dataRow.get(name);
    }
    setData(innerNames, values);
  }

  /**
   * Create a parameter data row from a master report's data row and a set
   * of parameter mappings.
   *
   * @param dataRow the data row.
   */
  public ParameterDataRow(final DataRow dataRow)
  {
    final int columnCount = dataRow.getColumnCount();
    final String[] innerNames = new String[columnCount];
    int nameCount = 0;
    for (int i = 0; i < innerNames.length; i++)
    {
      final String innerName = dataRow.getColumnName(i);
      if (innerName == null)
      {
        continue;
      }
      if (contains(innerName, innerNames, nameCount - 1) == false)
      {
        innerNames[nameCount] = innerName;
        nameCount += 1;
      }
    }

    final Object[] values = new Object[nameCount];
    for (int i = 0; i < nameCount; i++)
    {
      values[i] = dataRow.get(innerNames[i]);
    }
    setData(innerNames, values);
  }

  /**
   * A helper function that searches the given name if the provided array.
   *
   * @param name the name that is searched.
   * @param array the array containing all known names.
   * @param length the maximum number of elements in the given array that are valid.
   * @return true, if the name has been found, false otherwise.
   */
  private boolean contains (final String name, final String[] array, final int length)
  {
    for (int i = 0; i < length; i++)
    {
      if (name.equals(array[i]))
      {
        return true;
      }
    }
    return false;
  }
}
