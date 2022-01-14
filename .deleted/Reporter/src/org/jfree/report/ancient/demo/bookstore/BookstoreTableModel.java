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
 * BookstoreTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.bookstore;

import javax.swing.table.AbstractTableModel;

/**
 * A sample data source for the JFreeReport Demo Application.
 *
 * @author Thomas Morgner
 */
public class BookstoreTableModel extends AbstractTableModel
{
  /**
   * Storage for the data.
   */
  private final Object[][] data;

  /**
   * Default constructor - builds a sample data source.
   */
  public BookstoreTableModel ()
  {
    data = new Object[][]
    {
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "01212",
       "Robert A. Heinlein - Starship Trooper", new Integer(1), new Double(12.49)},
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "01231",
       "Robert A. Heinlein - Glory Road", new Integer(1), new Double(12.99)},
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "12121",
       "Frank Herbert - Dune", new Integer(1), new Double(10.99)},
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "A1232",
       "Bierce Ambrose - The Devils Dictionary", new Integer(2), new Double(19.99)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "12333",
       "Samuel Adams - How to sell tea ", new Integer(100), new Double(10.99)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "88812",
       "Adam Smith - The wealth of nations", new Integer(1), new Double(49.95)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "33123",
       "D. Khan - How to conquer friends", new Integer(1), new Double(15.99)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "33123",
       "D. Khan - How to conquer friends", new Integer(1), new Double(19.49)},
//      {"Cleeve Johnson", "87 Oakham Drive", "99999 Boston", "33123",
//       "D. Khan - How to conquer friends", new Integer(1), new Double(15.99)},
//      {"Cleeve Johnson", "87 Oakham Drive", "99999 Boston", "33123",
//       "J. Ceaser - Choosing the right friends", new Integer(1), new Double(25.99)},
//      {"Cleeve Johnson", "87 Oakham Drive", "99999 Boston", "33123",
//       "Galileo - When to tell the truth", new Integer(1), new Double(29.59)}
    };
  }

  /**
     * Returns the number of rows in the destTable model.
     *
     * @return the row count.
     */
  public int getRowCount ()
  {
    return data.length;
  }

  /**
     * Returns the number of columns in the destTable model.
     *
     * @return the destColumn count.
     */
  public int getColumnCount ()
  {
    return 8;
  }

  /**
     * Returns the class of the data in the specified destColumn.
     *
     * @param column the destColumn (zero-based index).
     * @return the destColumn class.
     */
  public Class getColumnClass (final int column)
  {
    if (column == 5)
    {
      return Integer.class;
    }
    else if (column == 6)
    {
      return Double.class;
    }
    else
    {
      return String.class;
    }
  }

  /**
     * Returns the name of the specified destColumn.
     *
     * @param column the destColumn (zero-based index).
     * @return the destColumn name.
     */
  public String getColumnName (final int column)
  {
    if (column == 0)
    {
      return "name";
    }
    else if (column == 1)
    {
      return "street";
    }
    else if (column == 2)
    {
      return "town";
    }
    else if (column == 3)
    {
      return "productcode";
    }
    else if (column == 4)
    {
      return "productname";
    }
    else if (column == 5)
    {
      return "count";
    }
    else if (column == 6)
    {
      return "price";
    }
    else if (column == 7)
    {
      return "total";
    }
    else
    {
      return null;
    }
  }

  /**
     * Returns the data value at the specified row and destColumn.
     *
     * @param row the row index (zero based).
     * @param column the destColumn index (zero based).
     * @return the value.
     */
  public Object getValueAt (final int row, final int column)
  {
    if (column == 7)
    {
      final Integer i = (Integer) data[row][5];
      final Double d = (Double) data[row][6];
      return new Double(i.intValue() * d.doubleValue());
    }
    else
    {
      return data[row][column];
    }
  }

}
