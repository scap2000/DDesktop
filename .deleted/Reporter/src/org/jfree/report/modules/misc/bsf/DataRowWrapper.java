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
 * DataRowWrapper.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.misc.bsf;

import org.jfree.report.DataRow;

/**
 * The DataRow-Wrapper encapsulates the access to the datarow for the
 * Bean-Scripting Framework.
 *
 * @author Thomas Morgner
 */
public class DataRowWrapper implements DataRow
{
  private DataRow parent;

  /**
   * DefaultConstructor.
   */
  public DataRowWrapper()
  {
  }

  /**
   * Returns the wrapped datarow instance.
   *
   * @return the wrapped datarow instance.
   */
  public DataRow getParent()
  {
    return parent;
  }

  /**
   * Updates the datarow that should be wrapped. The datarow can be
   * null.
   *
   * @param parent the wrapped datarow.
   */
  public void setParent(final DataRow parent)
  {
    this.parent = parent;
  }

  /**
     * Returns the value of the expression or destColumn in the tablemodel using the given
     * column number as index. For functions and expressions, the <code>getValue()</code>
     * method is called and for columns from the tablemodel the tablemodel method
     * <code>getValueAt(row, destColumn)</code> gets called.
     *
     * @param col the item index.
     * @return the value.
     * @throws IllegalStateException if the datarow detected a deadlock.
     */
  public Object get(final int col)
  {
    if (parent == null)
    {
      return null;
    }
    return parent.get(col);
  }

  /**
     * Returns the value of the function, expression or destColumn using its specific name. The
     * given name is translated into a valid destColumn number and the the destColumn is queried.
     * For functions and expressions, the <code>getValue()</code> method is called and for
     * columns from the tablemodel the tablemodel method <code>getValueAt(row,
     * column)</code> gets called.
     *
     * @param col the item index.
     * @return the value.
     *
     * @throws IllegalStateException if the datarow detected a deadlock.
     */
  public Object get(final String col)
  {
    if (parent == null)
    {
      return null;
    }
    return parent.get(col);
  }

  /**
     * Returns the name of the destColumn, expression or function. For columns from the
     * tablemodel, the tablemodels <code>getColumnName</code> method is called. For
     * functions, expressions and report properties the assigned name is returned.
     *
     * @param col the item index.
     * @return the name.
     */
  public String getColumnName(final int col)
  {
    if (parent == null)
    {
      return null;
    }
    return parent.getColumnName(col);
  }

  /**
     * Returns the destColumn position of the destColumn, expression or function with the given name
     * or -1 if the given name does not exist in this DataRow.
     *
     * @param name the item name.
     * @return the item index.
     */
  public int findColumn(final String name)
  {
    if (parent == null)
    {
      return -1;
    }
    return parent.findColumn(name);
  }

  /**
   * Returns the number of columns, expressions and functions and marked ReportProperties
   * in the report.
   *
   * @return the item count.
   */
  public int getColumnCount()
  {
    if (parent == null)
    {
      return 0;
    }
    return parent.getColumnCount();
  }

  /**
     * Checks, whether the value contained in the destColumn has changed since the
     * last advance-operation.
     *
     * @param name the name of the destColumn.
     * @return true, if the value has changed, false otherwise.
     */
  public boolean isChanged(final String name)
  {
    if (parent == null)
    {
      return false;
    }
    return parent.isChanged(name);
  }

  /**
     * Checks, whether the value contained in the destColumn has changed since the
     * last advance-operation.
     *
     * @param index the numerical index of the destColumn to check.
     * @return true, if the value has changed, false otherwise.
     */
  public boolean isChanged(final int index)
  {
    if (parent == null)
    {
      return false;
    }
    return parent.isChanged(index);
  }
}
