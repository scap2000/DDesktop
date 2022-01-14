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
 * StaticDataRow.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.states.datarow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jfree.report.DataRow;
import org.jfree.report.util.IntegerCache;

/**
 * This is a static datarow holding a value for each name in the datarow. This
 * datarow does not hold dataflags and thus does not track the changes done to
 * the data inside.
 * <p/>
 * The StaticDataRow is a derived view and is used to provide a safe collection
 * of the values of the previous datarow.
 *
 * @author Thomas Morgner
 */
public class StaticDataRow implements DataRow
{
  private String[] names;
  private Object[] values;
  private Map nameCache;

  protected StaticDataRow()
  {
  }

  protected StaticDataRow(final StaticDataRow dataRow)
  {
    if (dataRow == null)
    {
      throw new NullPointerException();
    }

    this.nameCache = dataRow.nameCache;
    this.names = dataRow.names;
    this.values = dataRow.values;
  }

  public StaticDataRow(final DataRow dataRow)
  {
    if (dataRow == null)
    {
      throw new NullPointerException();
    }

    final HashMap nameCache = new HashMap();
    synchronized (dataRow)
    {
      final int columnCount = dataRow.getColumnCount();
      this.names = new String[columnCount];
      this.values = new Object[dataRow.getColumnCount()];
      for (int i = 0; i < columnCount; i++)
      {
        names[i] = dataRow.getColumnName(i);
        values[i] = dataRow.get(i);
        if (names[i] != null)
        {
          nameCache.put(names[i], IntegerCache.getInteger(i));
        }
      }
    }
    this.nameCache = Collections.unmodifiableMap(nameCache);
  }

  public StaticDataRow(final String[] names, final Object[] values)
  {
    setData(names, values);
  }

  protected void setData(final String[] names, final Object[] values)
  {
    if (names == null)
    {
      throw new NullPointerException();
    }
    if (values == null)
    {
      throw new NullPointerException();
    }
    final int length;
    if (names.length == values.length)
    {
      length = names.length;
      this.names = (String[]) names.clone();
      this.values = (Object[]) values.clone();
    }
    else
    {
      length = Math.min(names.length, values.length);
      this.names = new String[length];
      this.values = new Object[length];
      System.arraycopy(names, 0, this.names, 0, length);
      System.arraycopy(values, 0, this.values, 0, length);
    }

    final HashMap nameCache = new HashMap();
    for (int i = 0; i < length; i++)
    {
      final String name = names[i];
      if (name != null)
      {
        nameCache.put(name, IntegerCache.getInteger(i));
      }
    }
    this.nameCache = Collections.unmodifiableMap(nameCache);
  }

  protected void updateData(final Object[] values)
  {
    if (values.length != this.values.length)
    {
      throw new IllegalArgumentException("You should preserve the number of columns.");
    }

    this.values = (Object[]) values.clone();
  }

  /**
     * Returns the value of the expression or destColumn in the tablemodel using the
     * given destColumn number as index. For functions and expressions, the
     * <code>getValue()</code> method is called and for columns from the
     * tablemodel the tablemodel method <code>getValueAt(row, destColumn)</code> gets
     * called.
     *
     * @param col the item index.
     * @return the value.
     * @throws IllegalStateException if the datarow detected a deadlock.
     */
  public Object get(final int col)
  {
    return values[col];
  }

  /**
     * Returns the value of the function, expression or destColumn using its specific
     * name. The given name is translated into a valid destColumn number and the the
     * column is queried. For functions and expressions, the
     * <code>getValue()</code> method is called and for columns from the
     * tablemodel the tablemodel method <code>getValueAt(row, destColumn)</code> gets
     * called.
     *
     * @param col the item index.
     * @return the value.
     * @throws IllegalStateException if the datarow detected a deadlock.
     */
  public Object get(final String col)
  {
    final Integer idx = (Integer) nameCache.get(col);
    if (idx == null)
    {
      return null;
    }
    return values[idx.intValue()];
  }

  /**
     * Returns the name of the destColumn, expression or function. For columns from
     * the tablemodel, the tablemodels <code>getColumnName</code> method is
     * called. For functions, expressions and report properties the assigned name
     * is returned.
     *
     * @param col the item index.
     * @return the name.
     */
  public String getColumnName(final int col)
  {
    return names[col];
  }

  /**
   * Returns the number of columns, expressions and functions and marked
   * ReportProperties in the report.
   *
   * @return the item count.
   */
  public int getColumnCount()
  {
    return values.length;
  }

  /**
     * Returns the destColumn position of the destColumn, expression or function with the
     * given name or -1 if the given name does not exist in this DataRow.
     *
     * @param name the item name.
     * @return the item index.
     */
  public int findColumn(final String name)
  {
    final Integer o = (Integer) nameCache.get(name);
    if (o != null)
    {
      return o.intValue();
    }
    return -1;
  }

  public boolean isChanged(final String name)
  {
    return false;
  }

  public boolean isChanged(final int index)
  {
    return false;
  }
}
