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
 * AdvertisingTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.invoice;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import org.jfree.report.ancient.demo.invoice.model.Advertising;
import org.jfree.report.ancient.demo.invoice.model.Article;

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
 * AdvertisingTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class AdvertisingTableModel extends AbstractTableModel
{
  private static final String[] COLUMN_NAMES =
          {
            "advertise",
            "customer.firstName", "customer.lastName", "customer.street",
            "customer.town", "customer.postalCode", "customer.country",
            "customer.salutation", "ad.date", "ad.number",
            "article.name", "article.number", "article.details",
            "article.price", "article.reducedPrice"
          };

  private static Class[] COLUMN_TYPES =
          {
            Advertising.class,
            String.class, String.class, String.class,
            String.class, String.class, String.class,
            String.class, Date.class, String.class,
            String.class, String.class, String.class,
            Float.class, Double.class
          };


  public AdvertisingTableModel ()
  {
    advertisings = new ArrayList();
  }

  private transient Advertising[] adPerRow;
  private transient Article[] articlesPerRow;
  private transient double[] reducedPricePerRow;

  private ArrayList advertisings;
  private int totalSize;

  public void addAdvertising (final Advertising advertising)
  {
    advertisings.add(advertising);
    invalidateCaches();
    fireTableDataChanged();
  }

  public void removeAdvertising (final Advertising advertising)
  {
    advertisings.remove(advertising);
    invalidateCaches();
    fireTableDataChanged();
  }

  public Advertising getAdvertising (final int ad)
  {
    return (Advertising) advertisings.get(ad);
  }

  public void invalidateCaches ()
  {
    int size = 0;
    for (int i = 0; i < advertisings.size(); i++)
    {
      final Advertising inv = getAdvertising(i);
      size += inv.getArticleCount();
    }
    this.totalSize = size;
    this.adPerRow = null;
    this.articlesPerRow = null;
    this.reducedPricePerRow = null;
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
    return COLUMN_NAMES.length;
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
    return totalSize;
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
    return COLUMN_NAMES[column];
  }

  /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the destColumn being queried
     * @return the Object.class
     */
  public Class getColumnClass (final int columnIndex)
  {
    return COLUMN_TYPES[columnIndex];
  }

  private void fillCache ()
  {
    if (adPerRow != null && articlesPerRow != null)
    {
      // nothing to do...
      return;
    }
    // ensure that we have enough space ...
    this.adPerRow = new Advertising[totalSize];
    this.articlesPerRow = new Article[totalSize];
    this.reducedPricePerRow = new double[totalSize];

    int currentRow = 0;
    final int invoiceSize = advertisings.size();
    for (int i = 0; i < invoiceSize; i++)
    {
      final Advertising inv = (Advertising) advertisings.get(i);
      final int articleCount = inv.getArticleCount();
      for (int ac = 0; ac < articleCount; ac++)
      {
        adPerRow[currentRow] = inv;
        articlesPerRow[currentRow] = inv.getArticle(ac);
        reducedPricePerRow[currentRow] = inv.getArticleReducedPrice(ac);
        currentRow += 1;
      }
    }
  }

  /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the destColumn whose value is to be queried
     * @return the value Object at the specified cell
     */
  public Object getValueAt (final int rowIndex, final int columnIndex)
  {
    // just make sure we can access the advertisings by the array
    fillCache();
    final Advertising inv = adPerRow[rowIndex];
    final Article art = articlesPerRow[rowIndex];

    switch (columnIndex)
    {
      case 0:
        return inv;
      case 1:
        return inv.getCustomer().getFirstName();
      case 2:
        return inv.getCustomer().getLastName();
      case 3:
        return inv.getCustomer().getStreet();
      case 4:
        return inv.getCustomer().getTown();
      case 5:
        return inv.getCustomer().getPostalCode();
      case 6:
        return inv.getCustomer().getCountry();
      case 7:
        return inv.getCustomer().getSalutation();
      case 8:
        return inv.getDate();
      case 9:
        return inv.getAdNumber();
      case 10:
        return art.getName();
      case 11:
        return art.getArticleNumber();
      case 12:
        return art.getArticleDetails();
      case 13:
        return new Float(art.getPrice());
      case 14:
        return new Double(reducedPricePerRow[rowIndex]);
    }
    throw new IndexOutOfBoundsException("ColumnIndex");
  }
}
