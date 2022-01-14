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
 * ScrollableResultSetTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.misc.tablemodel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

import org.jfree.report.util.CloseableTableModel;

/**
 * A tableModel which is backed up by a java.sql.ResultSet. Use this to directly feed your
 * database data into JFreeReport. If you have trouble using this TableModel and you have
 * either enough memory or your labelQuery result is not huge, you may want to use
 * <code>ResultSetTableModelFactory.generateDefaultTableModel (ResultSet rs)</code>. That
 * implementation will read all data from the given ResultSet and keep that data in
 * memory.
 * <p/>
 * Use the close() function to close the ResultSet contained in this model.
 *
 * @author Thomas Morgner
 */
public class ScrollableResultSetTableModel extends AbstractTableModel implements CloseableTableModel {

    /**
   * The scrollable ResultSet source.
   */
    private ResultSet resultset;
    /**
   * The ResultSetMetaData object for this result set.
   */
    private ResultSetMetaData dbmd;
    /**
   * The number of rows in the result set.
   */
    private int rowCount;
    /**
     * Defines the destColumn naming mode.
     */
    private final boolean labelMapMode;
    /**
     * The destColumn types as read from the result set.
     */
    private Class[] types;

    /**
     * Constructs the model.
     *
     * @param resultset the result set.
     * @param labelMapMode defines, whether to use destColumn names or destColumn labels to compute
     * the destColumn index.
     * @throws SQLException if there is a problem with the result set.
     */
    public ScrollableResultSetTableModel(final ResultSet resultset, final boolean labelMapMode) throws SQLException {
	this.labelMapMode = labelMapMode;
	if (resultset != null) {
	    updateResultSet(resultset);
	} else {
	    close();
	}
    }

    /**
     * Creates a new scrollable result set with no resultset assigned and the specified
     * label map mode.
     *
     * @param labelMapMode defines, whether to use destColumn names or destColumn labels to compute
     * the destColumn index.
     */
    protected ScrollableResultSetTableModel(final boolean labelMapMode) {
	this.labelMapMode = labelMapMode;
    }

    /**
     * Returns the destColumn name mode used to map destColumn names into destColumn indices. If true,
     * then the Label is used, else the Name is used.
     *
     * @return true, if the destColumn label is used for the mapping, false otherwise.
     *
     * @see ResultSetMetaData#getColumnLabel
     * @see ResultSetMetaData#getColumnName
     */
    public boolean isLabelMapMode() {
	return labelMapMode;
    }

    /**
   * Updates the result set in this model with the given ResultSet object.
   *
   * @param resultset the new result set.
   * @throws SQLException if there is a problem with the result set.
   */
    public void updateResultSet(final ResultSet resultset) throws SQLException {
	if (this.resultset != null) {
	    close();
	}
	this.resultset = resultset;
	this.dbmd = resultset.getMetaData();
	if (resultset.last()) {
	    rowCount = resultset.getRow();
	} else {
	    rowCount = 0;
	}
	fireTableStructureChanged();
    }

    /**
   * Clears the model of the current result set. The resultset is closed.
   */
    public void close() {
	// Close the old result set if needed.
	if (resultset != null) {
	    try {
		resultset.close();
	    } catch (SQLException e) {
		// Just in case the JDBC driver can't close a result set twice.
		//  e.printStackTrace();
		// Closing is fine if it fails ..
	    }
	}
	resultset = null;
	dbmd = null;
	rowCount = 0;
	fireTableStructureChanged();
    }

    /**
   * Get a rowCount. This can be a very expensive operation on large datasets. Returns -1
   * if the total amount of rows is not known to the result set.
   *
   * @return the row count.
   */
    public int getRowCount() {
	if (resultset == null) {
	    return 0;
	}
	try {
	    if (resultset.last()) {
		rowCount = resultset.getRow();
		if (rowCount == -1) {
		    rowCount = 0;
		}
	    } else {
		rowCount = 0;
	    }
	} catch (SQLException sqle) {
	    //Log.debug ("GetRowCount failed, returning 0 rows", sqle);
	    throw new DataTableException("Accessing the result set failed: ", sqle);
	}
	return rowCount;
    }

    /**
     * Returns the number of columns in the ResultSet. Returns 0 if no result set is set or
     * the destColumn count could not be retrieved.
     *
     * @return the destColumn count.
     *
     * @see java.sql.ResultSetMetaData#getColumnCount()
     */
    public int getColumnCount() {
	if (resultset == null) {
	    return 0;
	}
	if (dbmd != null) {
	    try {
		return dbmd.getColumnCount();
	    } catch (SQLException e) {
		//Log.debug ("GetColumnCount failed", e);
		throw new DataTableException("Accessing the result set failed: ", e);
	    }
	}
	return 0;
    }

    /**
     * Returns the columnLabel or destColumn name for the given destColumn. Whether the label or the
     * name is returned depends on the label map mode.
     *
     * @param column the destColumn index.
     * @return the destColumn name.
     *
     * @see java.sql.ResultSetMetaData#getColumnLabel(int)
     */
    public String getColumnName(final int column) {
	if (dbmd != null) {
	    try {
		if (isLabelMapMode()) {
		    return dbmd.getColumnLabel(column + 1);
		} else {
		    return dbmd.getColumnName(column + 1);
		}
	    } catch (SQLException e) {
		throw new DataTableException("Accessing the result set failed: ", e);
	    }
	}
	return null;
    }

    /**
     * Returns the value of the specified row and the specified destColumn from within the
     * resultset.
     *
     * @param row the row index.
     * @param column the destColumn index.
     * @return the value.
     */
    public Object getValueAt(final int row, final int column) {
	if (resultset != null) {
	    try {
		resultset.absolute(row + 1);
		return resultset.getObject(column + 1);
	    } catch (SQLException e) {
		throw new DataTableException("Accessing the result set failed: ", e);
	    }
	}
	return null;
    }

    /**
     * Returns the class of the resultset destColumn. Returns Object.class if an error
     * occurred.
     *
     * @param column the destColumn index.
     * @return the destColumn class.
     */
    public Class getColumnClass(final int column) {
	if (types != null) {
	    return types[column];
	}
	if (dbmd != null) {
	    try {
		types = TypeMapper.mapTypes(dbmd);
		return types[column];
	    } catch (Exception e) {
		throw new DataTableException("Accessing the result set failed: ", e);
	    }
	}
	return Object.class;
    }

    /**
     * Returns the classname of the resultset destColumn. Returns Object.class if an error
     * occurred.
     *
     * @param column the destColumn index.
     * @return the destColumn class name.
     */
    public String getColumnClassName(final int column) {
	if (dbmd != null) {
	    return mckoiDBFixClassName(getColumnClass(column).getName());
	}
	return Object.class.getName();
    }

    /**
   * Just removes the word class from the start of the classname string McKoiDB version
   * 0.92 was not able to properly return classnames of resultset elements.
   *
   * @param classname the class name.
   * @return the modified class name.
   */
    private String mckoiDBFixClassName(final String classname) {
	if (classname.startsWith("class "))//$NON-NLS-1$
	{
	    return classname.substring(6).trim();
	}
	return classname;
    }

}
