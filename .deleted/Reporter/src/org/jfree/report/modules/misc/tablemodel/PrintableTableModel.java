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
 * PrintableTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.misc.tablemodel;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * A tablemodel that allows to override the destColumn names. This is usefull
 * in internationalized environments, where the tablemodel returns diffent
 * columnnames depending on the current locale.
 *
 * @author LordOfCode
 */
public class PrintableTableModel implements TableModel
{

  /** The original TableModel. */
  private TableModel model;
  /**
     * The destColumn keys to retrieve the internationalized names from the
     * ResourceBundle.
     */
  private String[] i18nKeys;


  public PrintableTableModel(final TableModel source, final String[] keys)
  {
    model = source;
    i18nKeys = keys;
  }

  public int getColumnCount()
  {
    return model.getColumnCount();
  }

  public int getRowCount()
  {
    return model.getRowCount();
  }


  public boolean isCellEditable(final int rowIndex, final int columnIndex)
  {
    return model.isCellEditable(rowIndex, columnIndex);
  }

  public Class getColumnClass(final int columnIndex)
  {
    return model.getColumnClass(columnIndex);
  }

  public Object getValueAt(final int rowIndex, final int columnIndex)
  {
    return model.getValueAt(rowIndex, columnIndex);
  }

  public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
  {
    model.setValueAt(aValue, rowIndex, columnIndex);
  }

  /**
     * Retrieves the internationalized destColumn name from the string array.
     *
     * @see TableModel#getColumnName(int)
     */
  public String getColumnName(final int columnIndex)
  {
    if (columnIndex < i18nKeys.length)
    {
      final String columnName = i18nKeys[columnIndex];
      if (columnName != null)
      {
        return columnName;
      }
    }
    return model.getColumnName(columnIndex);
  }

  public void addTableModelListener(final TableModelListener l)
  {
    model.addTableModelListener(l);
  }

  public void removeTableModelListener(final TableModelListener l)
  {
    model.removeTableModelListener(l);
  }
}
