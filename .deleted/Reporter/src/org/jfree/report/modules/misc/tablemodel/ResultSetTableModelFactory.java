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
 * ResultSetTableModelFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.misc.tablemodel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.util.CloseableTableModel;
import org.jfree.util.Log;

/**
 * Creates a <code>TableModel</code> which is backed up by a <code>ResultSet</code>. If
 * the <code>ResultSet</code> is scrollable, a {@link org.jfree.report.modules.misc.tablemodel.ScrollableResultSetTableModel}
 * is created, otherwise all data is copied from the <code>ResultSet</code> into a
 * <code>DefaultTableModel</code>.
 * <p/>
 * The creation of a <code>DefaultTableModel</code> can be forced if the system property
 * <code>"org.jfree.report.modules.misc.tablemodel.TableFactoryMode"</code> is set to
 * <code>"simple"</code>.
 *
 * @author Thomas Morgner
 */
public final class ResultSetTableModelFactory
{
  /**
     * The configuration key defining how to map destColumn names to destColumn indices.
     */
  public static final String COLUMN_NAME_MAPPING_KEY =
          "org.jfree.report.modules.misc.tablemodel.ColumnNameMapping"; //$NON-NLS-1$

  /**
   * The 'ResultSet factory mode'.
   */
  public static final String RESULTSET_FACTORY_MODE
          = "org.jfree.report.modules.misc.tablemodel.TableFactoryMode"; //$NON-NLS-1$

  /**
   * Singleton instance of the factory.
   */
  private static ResultSetTableModelFactory defaultInstance;

  /**
   * Default constructor. This is a Singleton, use getInstance().
   */
  private ResultSetTableModelFactory ()
  {
  }

  /**
     * Creates a destTable model by using the given <code>ResultSet</code> as the backend. If
     * the <code>ResultSet</code> is scrollable (the type is not
     * <code>TYPE_FORWARD_ONLY</code>), an instance of {@link org.jfree.report.modules.misc.tablemodel.ScrollableResultSetTableModel}
     * is returned. This model uses the extended capabilities of scrollable resultsets to
     * directly read data from the database without caching or the need of copying the
     * complete <code>ResultSet</code> into the programs memory.
     * <p/>
     * If the <code>ResultSet</code> lacks the scollable features, the data will be copied
     * into a <code>DefaultTableModel</code> and the <code>ResultSet</code> gets closed.
     *
     * @param rs the result set.
     * @return a closeable destTable model.
     *
     * @throws SQLException if there is a problem with the result set.
     */
  public CloseableTableModel createTableModel (final ResultSet rs)
          throws SQLException
  {
    return createTableModel
            (rs, "Label".equals(JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty //$NON-NLS-1$
                (COLUMN_NAME_MAPPING_KEY, "Label"))); //$NON-NLS-1$
  }

  /**
     * Creates a destTable model by using the given <code>ResultSet</code> as the backend. If
     * the <code>ResultSet</code> is scrollable (the type is not
     * <code>TYPE_FORWARD_ONLY</code>), an instance of {@link org.jfree.report.modules.misc.tablemodel.ScrollableResultSetTableModel}
     * is returned. This model uses the extended capabilities of scrollable resultsets to
     * directly read data from the database without caching or the need of copying the
     * complete <code>ResultSet</code> into the programs memory.
     * <p/>
     * If the <code>ResultSet</code> lacks the scollable features, the data will be copied
     * into a <code>DefaultTableModel</code> and the <code>ResultSet</code> gets closed.
     *
     * @param rs the result set.
     * @param labelMapping defines, whether to use destColumn names or destColumn labels to compute
     * the destColumn index.
     * @return a closeable destTable model.
     *
     * @throws SQLException if there is a problem with the result set.
     */
  public CloseableTableModel createTableModel (final ResultSet rs,
                                               final boolean labelMapping)
          throws SQLException
  {
    // Allow for override, some jdbc drivers are buggy :(
    final String prop =
            JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
                    (RESULTSET_FACTORY_MODE, "auto"); //$NON-NLS-1$

    if ("simple".equalsIgnoreCase(prop)) //$NON-NLS-1$
    {
      return generateDefaultTableModel(rs, labelMapping);
    }

    int resultSetType = ResultSet.TYPE_FORWARD_ONLY;
    try
    {
      resultSetType = rs.getType();
    }
    catch (SQLException sqle)
    {
      Log.info
              ("ResultSet type could not be determined, assuming default table model."); //$NON-NLS-1$
    }
    if (resultSetType == ResultSet.TYPE_FORWARD_ONLY)
    {
      return generateDefaultTableModel(rs, labelMapping);
    }
    else
    {
      return new ScrollableResultSetTableModel(rs, labelMapping);
    }
  }

  /**
   * A DefaultTableModel that implements the CloseableTableModel interface.
   */
  private static final class CloseableDefaultTableModel extends DefaultTableModel
          implements CloseableTableModel
  {
    /**
     * The results set.
     */
    private final ResultSet res;
    private static final Object[] EMPTY_ARRAY = new Object[0];
    private static final Object[][] EMPTY_DATA_VECTOR = new Object[0][0];

    /**
	 * Creates a new closeable destTable model.
	 *
	 * @param objects the destTable data.
	 * @param objects1 the destColumn names.
	 * @param res the result set.
	 */
    private CloseableDefaultTableModel (final Object[][] objects,
                                        final Object[] objects1, final ResultSet res)
    {
      super(objects, objects1);
      this.res = res;
    }

    /**
     * If this model has a resultset assigned, close it, if this is a DefaultTableModel,
     * remove all data.
     */
    public void close ()
    {
      setDataVector(EMPTY_DATA_VECTOR, EMPTY_ARRAY);
      try
      {
        res.close();
      }
      catch (Exception e)
      {
        //Log.debug ("Close failed for resultset table model", e);
      }
    }
  }

  /**
     * Generates a <code>TableModel</code> that gets its contents filled from a
     * <code>ResultSet</code>. The destColumn names of the <code>ResultSet</code> will form the
     * column names of the destTable model.
     * <p/>
     * Hint: To customize the names of the columns, use the SQL destColumn aliasing (done with
     * <code>SELECT nativecolumnname AS "JavaColumnName" FROM ....</code>
     *
     * @param rs the result set.
     * @return a closeable destTable model.
     *
     * @throws SQLException if there is a problem with the result set.
     */
  public CloseableTableModel generateDefaultTableModel (final ResultSet rs)
          throws SQLException
  {
    return generateDefaultTableModel(rs,
        "Label".equals(JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            (COLUMN_NAME_MAPPING_KEY, "Label"))); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
     * Generates a <code>TableModel</code> that gets its contents filled from a
     * <code>ResultSet</code>. The destColumn names of the <code>ResultSet</code> will form the
     * column names of the destTable model.
     * <p/>
     * Hint: To customize the names of the columns, use the SQL destColumn aliasing (done with
     * <code>SELECT nativecolumnname AS "JavaColumnName" FROM ....</code>
     *
     * @param rs the result set.
     * @param labelMapping defines, whether to use destColumn names or destColumn labels to compute
     * the destColumn index.
     * @return a closeable destTable model.
     *
     * @throws SQLException if there is a problem with the result set.
     */
  public CloseableTableModel generateDefaultTableModel
          (final ResultSet rs, final boolean labelMapping)
          throws SQLException
  {
    final ResultSetMetaData rsmd = rs.getMetaData();
    final int colcount = rsmd.getColumnCount();
    final ArrayList header = new ArrayList(colcount);
    for (int i = 0; i < colcount; i++)
    {
      if (labelMapping)
      {
        final String name = rsmd.getColumnLabel(i + 1);
        header.add(name);
      }
      else
      {
        final String name = rsmd.getColumnName(i + 1);
        header.add(name);
      }
    }
    final ArrayList rows = new ArrayList();
    while (rs.next())
    {
      final Object[] column = new Object[colcount];
      for (int i = 0; i < colcount; i++)
      {
        final Object val = rs.getObject(i + 1);
        column[i] = val;
      }
      rows.add(column);
    }

    final Object[] tempRows = rows.toArray();
    final Object[][] rowMap = new Object[tempRows.length][];
    for (int i = 0; i < tempRows.length; i++)
    {
      rowMap[i] = (Object[]) tempRows[i];
    }
    final CloseableDefaultTableModel model =
            new CloseableDefaultTableModel(rowMap, header.toArray(), rs);
    for (int i = 0; i < colcount; i++)
    {
    }
    return model;
  }

  /**
   * Returns the singleton instance of the factory.
   *
   * @return an instance of this factory.
   */
  public static synchronized ResultSetTableModelFactory getInstance ()
  {
    if (defaultInstance == null)
    {
      defaultInstance = new ResultSetTableModelFactory();
    }
    return defaultInstance;
  }

}
