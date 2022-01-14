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
 * CSVTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.misc.tablemodel;

import javax.swing.table.AbstractTableModel;

/**
 * <code>TableModel</code> used by the <code>CSVTableModelProducer</code> class. It has a
 * feature which generates the destColumn name if it is not know.
 *
 * @author Mimil
 * @see this.getColumnName ()
 */
public class CSVTableModel extends AbstractTableModel
{

  private String[] columnNames = null;
  private int rowCount = 0;
  private int maxColumnCount = 0;
  private Object[][] data;

  public CSVTableModel ()
  {
  }

  public Object[][] getData ()
  {
    return data;
  }

  public void setData (final Object[][] data)
  {
    this.data = data;
  }

  public String[] getColumnNames ()
  {
    return columnNames;
  }

  public void setColumnNames (final String[] columnNames)
  {
    this.columnNames = columnNames;
  }

  /**
     * Counts columns of this <code>TableModel</code>.
     *
     * @return the destColumn count
     */
  public int getColumnCount ()
  {
    if (this.columnNames != null)
    {
      return columnNames.length;
    }

    return this.maxColumnCount;
  }

  /**
   * Counts rows of this <code>TableModel</code>.
   *
   * @return the row count
   */
  public int getRowCount ()
  {
    return this.rowCount;
  }

  /**
     * Gets the Object at specified row and destColumn positions.
     *
     * @param rowIndex row index
     * @param columnIndex column index
     * @return The requested Object
     */
  public Object getValueAt (final int rowIndex, final int columnIndex)
  {
    final Object[] line = this.data[rowIndex];

    if (line.length < columnIndex)
    {
      return null;
    }
    else
    {
      return line[columnIndex];
    }
  }

  /**
     * Sets the maximum destColumn count if it is bigger than the current one.
     *
     * @param maxColumnCount
     */
  public void setMaxColumnCount (final int maxColumnCount)
  {
    if (this.maxColumnCount < maxColumnCount)
    {
      this.maxColumnCount = maxColumnCount;
    }
  }

  public int getMaxColumnCount()
  {
    return maxColumnCount;
  }

  /**
     * Return the destColumn name at a specified position.
     *
     * @param column column index
     * @return the destColumn name
     */
  public String getColumnName (final int column)
  {
    if (this.columnNames != null)
    {
      return this.columnNames[column];
    }
    else
    {
      if (column >= this.maxColumnCount)
      {
        throw new IllegalArgumentException("Column (" + column + ") does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
      }
      else
      {
        return "COLUMN_" + column; //$NON-NLS-1$
      }
    }
  }
}
