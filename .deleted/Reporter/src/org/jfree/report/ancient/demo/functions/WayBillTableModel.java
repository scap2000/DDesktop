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
 * WayBillTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.functions;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Creation-Date: 01.10.2005, 11:36:13
 *
 * @author Thomas Morgner
 */
public class WayBillTableModel extends AbstractTableModel
{
  public static class CategoryItem
  {
    private String container;
    private String item;
    private String notes;
    private Double weight;

    public CategoryItem(final String category,
                        final String item,
                        final String notes,
                        final double weight)
    {
      this.container = category;
      this.item = item;
      this.notes = notes;
      this.weight = new Double(weight);
    }

    public Double getWeight()
    {
      return weight;
    }

    public String getContainer()
    {
      return container;
    }

    public String getItem()
    {
      return item;
    }

    public String getNotes()
    {
      return notes;
    }
  }

  private String[] COLNAMES = {"Container", "Item", "Notes", "Weight"};

  private ArrayList rows;

  public WayBillTableModel()
  {
    rows = new ArrayList();
  }

  public void addItem(CategoryItem item)
  {
    rows.add(item);
  }

  public int getRowCount()
  {
    return rows.size();
  }

  public int getColumnCount()
  {
    return COLNAMES.length;
  }

  public Class getColumnClass(int columnIndex)
  {
    if (columnIndex == 3)
    {
      return Double.class;
    }
    return String.class;
  }

  public String getColumnName(int column)
  {
    return COLNAMES[column];
  }

  public Object getValueAt(int rowIndex, int columnIndex)
  {
    CategoryItem item = (CategoryItem) rows.get(rowIndex);
    switch (columnIndex)
    {
      case 0:
        return item.getContainer();
      case 1:
        return item.getItem();
      case 2:
        return item.getNotes();
      case 3:
        return item.getWeight();
    }
    return null;
  }
}
