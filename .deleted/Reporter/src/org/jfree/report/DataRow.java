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
 * DataRow.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

/**
 * The datarow is used to access the current row in the <code>TableModel</code>, <code>Expression</code>s and
 * <code>Function</code>s using a generic interface.
 * <p/>
 * The Engine assumes, that the tablemodels given for reporting are immutable and do not change during the report
 * processing.
 *
 * @author Thomas Morgner
 * @see org.jfree.report.function.Expression
 * @see org.jfree.report.function.Function
 * @see javax.swing.table.TableModel
 */
public interface DataRow
{
  /**
     * Returns the value of the expression or destColumn in the tablemodel using the given destColumn number as index. For
     * functions and expressions, the <code>getValue()</code> method is called and for columns from the tablemodel the
     * tablemodel method <code>getValueAt(row, destColumn)</code> gets called.
     *
     * @param col the item index.
     * @return the value.
     * @deprecated Positional parameter access will be removed in the next version.
     */
  public Object get(int col);

  /**
     * Returns the value of the function, expression or destColumn using its specific name. The given name is translated into
     * a valid destColumn number and the the destColumn is queried. For functions and expressions, the <code>getValue()</code>
     * method is called and for columns from the tablemodel the tablemodel method <code>getValueAt(row, destColumn)</code>
     * gets called.
     *
     * @param col the item index.
     * @return the value.
     */
  public Object get(String col);

  /**
     * Returns the name of the destColumn, expression or function. For columns from the tablemodel, the tablemodels
     * <code>getColumnName</code> method is called. For functions, expressions and report properties the assigned name is
     * returned.
     *
     * @param col the item index.
     * @return the name.
     * @deprecated Positional parameter access will be removed in the next version.
     */
  public String getColumnName(int col);

  /**
     * Returns the destColumn position of the destColumn, expression or function with the given name or -1 if the given name does
     * not exist in this DataRow.
     *
     * @param name the item name.
     * @return the item index.
     * @deprecated Positional parameter access will be removed in the next version.
     */
  public int findColumn(String name);

  /**
   * Returns the number of columns, expressions and functions and marked ReportProperties in the report.
   *
   * @return the item count.
   */
  public int getColumnCount();

  /**
     * Checks, whether the value contained in the destColumn has changed since the last advance-operation.
     *
     * @param name the name of the destColumn.
     * @return true, if the value has changed, false otherwise.
     */
  public boolean isChanged(String name);

  /**
     * Checks, whether the value contained in the destColumn has changed since the last advance-operation.
     *
     * @param index the numerical index of the destColumn to check.
     * @return true, if the value has changed, false otherwise.
     * @deprecated Positional parameter access will be removed in the next version.
     */
  public boolean isChanged(int index);
}
