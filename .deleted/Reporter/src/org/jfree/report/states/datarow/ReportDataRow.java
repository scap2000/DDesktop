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
 * ReportDataRow.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.states.datarow;

import javax.swing.table.TableModel;

import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.ReportDataFactoryException;

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
 * ReportDataRow.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public final class ReportDataRow
{
  //private Map nameCache;
  private String[] names;
  private final TableModel reportData;
  private int cursor;

  private ReportDataRow(final TableModel reportData)
  {
    if (reportData == null)
    {
      throw new NullPointerException();
    }
    this.reportData = reportData;
    this.cursor = 0;

    final int columnCount = reportData.getColumnCount();
    this.names = new String[columnCount];

    for (int i = 0; i < columnCount; i++)
    {
      this.names[i] = reportData.getColumnName(i);
    }
  }

  private ReportDataRow(final TableModel reportData,
                        final ReportDataRow reportDataRow)
  {
    if (reportData == null)
    {
      throw new NullPointerException();
    }

    if (reportDataRow == null)
    {
      throw new NullPointerException();
    }

    this.reportData = reportData;
    this.cursor = 0;

    this.cursor = reportDataRow.cursor + 1;
    this.names = reportDataRow.names;
  }

  public static ReportDataRow createDataRow(final DataFactory dataFactory,
                                            final String query,
                                            final DataRow parameters)
      throws ReportDataFactoryException
  {
    final TableModel reportData = dataFactory.queryData(query, parameters);
    return new ReportDataRow(reportData);
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
    return reportData.getValueAt(cursor, col);
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
    return names.length;
  }

  /**
   * Advances to the next row and attaches the given master row to the objects
   * contained in that client data row.
   *
   * @param master
   * @return
   */
  public ReportDataRow advance()
  {
    return new ReportDataRow(reportData, this);
  }

  public boolean isAdvanceable()
  {
    return cursor < (reportData.getRowCount() - 1);
  }

  public boolean isReadable ()
  {
    return cursor >= 0 && cursor < reportData.getRowCount();
  }

  public TableModel getReportData()
  {
    return reportData;
  }

  public int getCursor()
  {
    return cursor;
  }
}
