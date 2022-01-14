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
 * OperationResultTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.converter.components;

import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import org.jfree.report.modules.gui.converter.ConverterGUIModule;
import org.jfree.report.modules.gui.converter.parser.OperationResult;
import org.jfree.report.util.i18n.Messages;

/**
 * The operation result tablemodel is used to display the parser and converter results to
 * the user.
 *
 * @author Thomas Morgner
 */
public class OperationResultTableModel extends AbstractTableModel
{
  /**
   * Provides access to externalized strings
   */
  private static final Messages messages = new Messages(ConverterGUIModule.BUNDLE_NAME);
  
  /**
     * An internal destColumn mapping.
     */
  private static final int COLUMN_SEVERITY = 0;
  /**
     * An internal destColumn mapping.
     */
  private static final int COLUMN_MESSAGE = 1;
  /**
     * An internal destColumn mapping.
     */
  private static final int COLUMN_LINE = 2;
  /**
     * An internal destColumn mapping.
     */
  private static final int COLUMN_COLUMN = 3;

  /**
   * The operation results are read from the parser.
   */
  private OperationResult[] data;
  /**
     * The resource bundle used to translate the destColumn names.
     */
  private final ResourceBundle resources;

  /**
     * The destColumn name keys for the resource bundle.
     */
  private static final String[] COLUMN_NAMES =
          {
            "ResultTableModel.Severity", //$NON-NLS-1$
            "ResultTableModel.Message", //$NON-NLS-1$
            "ResultTableModel.Line", //$NON-NLS-1$
            "ResultTableModel.Column" //$NON-NLS-1$
          };
  private static final OperationResult[] EMPTY_DATA = new OperationResult[0];

  /**
     * Creates a new and initially empty operation result destTable model.
     */
  public OperationResultTableModel ()
  {
    this(EMPTY_DATA);
  }

  /**
     * Creates a new operation result destTable model which will be filled with the given data.
     *
     * @param data the operation result objects from the parser or writer.
     */
  public OperationResultTableModel (final OperationResult[] data)
  {
    this.resources = ResourceBundle.getBundle(ConverterGUIModule.BUNDLE_NAME);
    setData(data);
  }

  /**
   * Sets the data for the tablemodel.
   *
   * @param data the data.
   */
  public void setData (final OperationResult[] data)
  {
    if (data == null)
    {
      throw new NullPointerException();
    }
    if (data.length == 0)
    {
      this.data = EMPTY_DATA;
    }
    else
    {
      this.data = new OperationResult[data.length];
      System.arraycopy(data, 0, this.data, 0, data.length);
      fireTableDataChanged();
    }
  }

  /**
   * Returns the operation result data as large huge array.
   *
   * @return the data stored in this tablemodel.
   */
  public OperationResult[] getData()
  {
    return (OperationResult[]) data.clone();
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
    return data.length;
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
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the destColumn whose value is to be queried
     * @return the value Object at the specified cell
     */
  public Object getValueAt (final int rowIndex, final int columnIndex)
  {
    switch (columnIndex)
    {
      case COLUMN_SEVERITY:
        return data[rowIndex].getSeverity();
      case COLUMN_MESSAGE:
        return (data[rowIndex].getMessage());
      case COLUMN_LINE:
        return new Integer(data[rowIndex].getLine());
      case COLUMN_COLUMN:
        return new Integer(data[rowIndex].getColumn());
      default:
        throw new IndexOutOfBoundsException(messages.getErrorString("OperationResultTableModel.ERROR_0001_INVALID_INDEX")); //$NON-NLS-1$
    }
  }

  /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the destColumn being queried
     * @return the Object.class
     */
  public Class getColumnClass (final int columnIndex)
  {
    switch (columnIndex)
    {
      case COLUMN_SEVERITY:
        return Object.class;
      case COLUMN_MESSAGE:
        return String.class;
      case COLUMN_LINE:
        return Integer.class;
      case COLUMN_COLUMN:
        return Integer.class;
      default:
        throw new IndexOutOfBoundsException(messages.getErrorString("OperationResultTableModel.ERROR_0002_INVALID_INDEX")); //$NON-NLS-1$
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
    return resources.getString(COLUMN_NAMES[column]);
  }
}
