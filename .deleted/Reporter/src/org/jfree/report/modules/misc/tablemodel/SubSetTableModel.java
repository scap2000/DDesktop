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
 * SubSetTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.misc.tablemodel;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * A TableModel that proxies an other tablemodel and cuts rows from the start and/or the
 * end of the other tablemodel.
 *
 * @author Thomas Morgner
 */
public class SubSetTableModel implements TableModel
{
  /**
     * A helper class, that translates tableevents received from the wrapped destTable model and
     * forwards them with changed indices to the registered listeners.
     */
  private final class TableEventTranslator implements TableModelListener
  {
    /**
     * the registered listeners.
     */
    private final ArrayList listeners;

    /**
     * Default Constructor.
     */
    private TableEventTranslator ()
    {
      listeners = new ArrayList();
    }

    /**
     * This fine grain notification tells listeners the exact range of cells, rows, or
     * columns that changed. The received rows are translated to fit the external
     * tablemodel size.
     *
     * @param e the event, that should be translated.
     */
    public void tableChanged (final TableModelEvent e)
    {
      int firstRow = e.getFirstRow();
      if (e.getFirstRow() > 0)
      {
        firstRow -= getStart();
      }

      int lastRow = e.getLastRow();
      if (lastRow > 0)
      {
        lastRow -= getStart();
        lastRow -= (getEnclosedModel().getRowCount() - getEnd());
      }
      final int type = e.getType();
      final int column = e.getColumn();

      final TableModelEvent event =
              new TableModelEvent(SubSetTableModel.this, firstRow, lastRow, column, type);

      for (int i = 0; i < listeners.size(); i++)
      {
        final TableModelListener l = (TableModelListener) listeners.get(i);
        l.tableChanged(event);
      }
    }

    /**
     * Adds the TableModelListener to this Translator.
     *
     * @param l the tablemodel listener
     */
    protected void addTableModelListener (final TableModelListener l)
    {
      listeners.add(l);
    }

    /**
     * Removes the TableModelListener from this Translator.
     *
     * @param l the tablemodel listener
     */
    protected void removeTableModelListener (final TableModelListener l)
    {
      listeners.remove(l);
    }
  }

  /**
   * the row that should be the first row.
   */
  private int start;

  /**
   * the row that should be the last row.
   */
  private int end;

  /**
   * the model.
   */
  private TableModel model;

  /**
   * the event translator.
   */
  private TableEventTranslator eventHandler;

  /**
   * Creates a new SubSetTableModel, the start and the end parameters define the new
   * tablemodel row count. The parameter <code>start</code> must be a positive integer and
   * denotes the number or rows removed from the start of the tablemodel. <code>end</code>
   * is the number of the last translated row. Any row after <code>end</code> is ignored.
   * End must be greater or equal the given start row.
   *
   * @param start the number of rows that should be removed.
   * @param end   the last row.
   * @param model the wrapped model
   * @throws NullPointerException     if the given model is null
   * @throws IllegalArgumentException if start or end are invalid.
   */
  public SubSetTableModel (final int start, final int end, final TableModel model)
  {
    if (start < 0)
    {
      throw new IllegalArgumentException("Start < 0"); //$NON-NLS-1$
    }
    if (end <= start)
    {
      throw new IllegalArgumentException("end < start"); //$NON-NLS-1$
    }
    if (model == null)
    {
      throw new NullPointerException();
    }
    if (end >= model.getRowCount())
    {
      throw new IllegalArgumentException("End >= Model.RowCount"); //$NON-NLS-1$
    }

    this.start = start;
    this.end = end;
    this.model = model;
    this.eventHandler = new TableEventTranslator();
  }

  /**
   * Translates the given row to fit for the wrapped tablemodel.
   *
   * @param rowIndex the original row index.
   * @return the translated row index.
   */
  private int getClientRowIndex (final int rowIndex)
  {
    return rowIndex + start;
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
    final int rowCount = model.getRowCount();
    return rowCount - start - (rowCount - end);
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
    return model.getColumnCount();
  }

  /**
     * Returns the name of the destColumn at <code>columnIndex</code>.  This is used to
     * initialize the destTable's destColumn header name.  Note: this name does not need to be
     * unique; two columns in a destTable can have the same name.
     *
     * @param columnIndex the index of the destColumn
     * @return the name of the destColumn
     */
  public String getColumnName (final int columnIndex)
  {
    return model.getColumnName(columnIndex);
  }

  /**
     * Returns the most specific superclass for all the cell values in the destColumn.  This is
     * used by the <code>JTable</code> to set up a default renderer and editor for the
     * column.
     *
     * @param columnIndex the index of the destColumn
     * @return the base ancestor class of the object values in the model.
     */
  public Class getColumnClass (final int columnIndex)
  {
    return model.getColumnClass(columnIndex);
  }

  /**
     * Returns true if the cell at <code>rowIndex</code> and <code>columnIndex</code> is
     * editable.  Otherwise, <code>setValueAt</code> on the cell will not change the value
     * of that cell.
     *
     * @param rowIndex the row whose value to be queried
     * @param columnIndex the destColumn whose value to be queried
     * @return true if the cell is editable
     *
     * @see #setValueAt
     */
  public boolean isCellEditable (final int rowIndex, final int columnIndex)
  {
    return model.isCellEditable(getClientRowIndex(rowIndex), columnIndex);
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
    return model.getValueAt(getClientRowIndex(rowIndex), columnIndex);
  }

  /**
     * Sets the value in the cell at <code>columnIndex</code> and <code>rowIndex</code> to
     * <code>aValue</code>.
     *
     * @param aValue the new value
     * @param rowIndex the row whose value is to be changed
     * @param columnIndex the destColumn whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
  public void setValueAt (final Object aValue, final int rowIndex, final int columnIndex)
  {
    model.setValueAt(aValue, getClientRowIndex(rowIndex), columnIndex);
  }

  /**
   * Adds a listener to the list that is notified each time a change to the data model
   * occurs.
   *
   * @param l the TableModelListener
   */
  public void addTableModelListener (final TableModelListener l)
  {
    eventHandler.addTableModelListener(l);
  }

  /**
   * Removes a listener from the list that is notified each time a change to the data
   * model occurs.
   *
   * @param l the TableModelListener
   */
  public void removeTableModelListener (final TableModelListener l)
  {
    eventHandler.removeTableModelListener(l);
  }

  /**
     * Returns the enclosed tablemodel, which is wrapped by this subset destTable model.
     *
     * @return the enclosed destTable model, never null.
     */
  protected TableModel getEnclosedModel ()
  {
    return model;
  }

  /**
   * Returns the start row that should be mapped to row 0 of this model.
   *
   * @return the first row that should be visible.
   */
  protected int getStart ()
  {
    return start;
  }

  /**
   * Returns the last row that should be visible.
   *
   * @return the number of the last row.
   */
  protected int getEnd ()
  {
    return end;
  }
}
