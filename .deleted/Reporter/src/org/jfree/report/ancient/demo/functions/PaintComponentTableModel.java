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
 * PaintComponentTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.functions;

import java.awt.Component;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

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
 * PaintComponentTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class PaintComponentTableModel
        extends AbstractTableModel
{
  private ArrayList rows;

  public PaintComponentTableModel ()
  {
    rows = new ArrayList();
  }

  public void addComponent (final Component component)
  {
    if (component == null)
    {
      throw new NullPointerException("Component must not be null.");
    }
    rows.add(component);
  }

  /**
   * Returns the number of columns in the model. A <code>JTable</code> uses this method to
   * determine how many columns it should create and display by default.
   *
   * @return the number of columns in the model
   *
   * @see #getRowCount
   */
  public int getColumnCount ()
  {
    return 2;
  }

  /**
   * Returns the number of rows in the model. A <code>JTable</code> uses this method to
   * determine how many rows it should display.  This method should be quick, as it is
   * called frequently during rendering.
   *
   * @return the number of rows in the model
   *
   * @see #getColumnCount
   */
  public int getRowCount ()
  {
    return rows.size();
  }

  /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the destColumn whose value is to be queried
     * @return the value Object at the specified cell
     */
  public Object getValueAt (final int rowIndex,
                            final int columnIndex)
  {
    final Object c = rows.get(rowIndex);
    if (columnIndex == 0)
    {
      return c;
    }
    return c.getClass().getName();
  }

  /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the destColumn being queried
     * @return the Object.class
     */
  public Class getColumnClass (final int columnIndex)
  {
    if (columnIndex == 0)
    {
      return Component.class;
    }
    else
    {
      return String.class;
    }
  }

  /**
     * Returns a default name for the destColumn using spreadsheet conventions: A, B, C, ... Z,
     * AA, AB, etc.  If <code>destColumn</code> cannot be found, returns an empty string.
     *
     * @param column the destColumn being queried
     * @return a string containing the default name of <code>destColumn</code>
     */
  public String getColumnName (final int column)
  {
    if (column == 0)
    {
      return "Component";
    }
    else
    {
      return "Class";
    }
  }
}
