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
 * CachingDataFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.states;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.util.CloseableTableModel;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 19.11.2006, 13:35:45
 *
 * @author Thomas Morgner
 */
public class CachingDataFactory implements DataFactory
{
  private static class Parameters implements DataRow
  {
    private Object[] dataStore;
    private String[] nameStore;
    private Integer hashCode;

    public Parameters(final DataRow dataSet)
    {
      final int columnCount = dataSet.getColumnCount();
      dataStore = new Object[columnCount];
      nameStore = new String[columnCount];

      for (int i = 0; i < columnCount; i++)
      {
        nameStore[i] = dataSet.getColumnName(i);
        dataStore[i] = dataSet.get(i);
      }
    }

    public boolean isChanged(final String name)
    {
      return true;
    }

    public boolean isChanged(final int index)
    {
      return true;
    }

    public int getColumnCount()
    {
      return dataStore.length;
    }

    public String getColumnName(final int column)
    {
      return nameStore[column];
    }

    public Object get(final int column)
    {
      return dataStore[column];
    }

    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final Parameters that = (Parameters) o;

      if (!Arrays.equals(dataStore, that.dataStore))
      {
        return false;
      }
      if (!Arrays.equals(nameStore, that.nameStore))
      {
        return false;
      }

      return true;
    }

    public synchronized int hashCode()
    {
      if (hashCode != null)
      {
        return hashCode.intValue();
      }
      int hashCode = 0;
      for (int i = 0; i < dataStore.length; i++)
      {
        final Object o = dataStore[i];
        if (o != null)
        {
          hashCode = hashCode * 23 + o.hashCode();
        }
        else
        {
          hashCode = hashCode * 23;
        }
      }
      for (int i = 0; i < nameStore.length; i++)
      {
        final Object o = nameStore[i];
        if (o != null)
        {
          hashCode = hashCode * 23 + o.hashCode();
        }
        else
        {
          hashCode = hashCode * 23;
        }
      }
      this.hashCode = new Integer(hashCode);
      return hashCode;
    }

    /**
	 * Returns the value of the function, expression or destColumn using its specific name. The given name is translated
	 * into a valid destColumn number and the the destColumn is queried. For functions and expressions, the
	 * <code>getValue()</code> method is called and for columns from the tablemodel the tablemodel method
	 * <code>getValueAt(row, destColumn)</code> gets called.
	 *
	 * @param col the item index.
	 * @return the value.
	 * @throws IllegalStateException if the datarow detected a deadlock.
	 */
    public Object get(final String col) throws IllegalStateException
    {
      final int idx = findColumn(col);
      if (idx == -1)
      {
        return null;
      }
      return get(idx);
    }

    /**
	 * Returns the destColumn position of the destColumn, expression or function with the given name or -1 if the given name
	 * does not exist in this DataRow.
	 *
	 * @param name the item name.
	 * @return the item index.
	 */
    public int findColumn(final String name)
    {
      for (int i = 0; i < nameStore.length; i++)
      {
        final String storedName = nameStore[i];
        if (ObjectUtilities.equal(storedName, name))
        {
          return i;
        }
      }
      return -1;
    }
  }

  private HashMap queryCache;
  private DataFactory backend;
  private boolean closed;
  private boolean debugDataSources;
  private boolean profileDataSources;

  public CachingDataFactory(final DataFactory backend)
  {
    if (backend == null)
    {
      throw new NullPointerException();
    }
    this.backend = backend;
    this.queryCache = new HashMap();
    this.debugDataSources = "true".equals(JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
                              ("org.jfree.report.DebugDataSources"));
    this.profileDataSources = "true".equals(JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
                              ("org.jfree.report.ProfileDataSources"));
  }

  public void open()
  {
    backend.open();
    closed = false;
  }

  /**
     * Queries a datasource. The string 'labelQuery' defines the name of the labelQuery. The Parameterset given here may contain
     * more data than actually needed.
     * <p/>
     * The dataset may change between two calls, do not assume anything!
     *
     * @param query
     * @param parameters
     * @return
     */
  public TableModel queryData(final String query, final DataRow parameters)
      throws ReportDataFactoryException
  {
    if (profileDataSources && Log.isDebugEnabled())
    {
      Log.debug(System.identityHashCode(Thread.currentThread()) + ": Query processing time: Starting");
    }
    final long startTime = System.currentTimeMillis();
    try
    {
      final HashMap parameterCache = (HashMap) queryCache.get(query);
      if (parameterCache == null)
      {
        // totally new query here.
        final HashMap newParams = new HashMap();
        queryCache.put(query, newParams);

        final Parameters params = new Parameters(parameters);
        final TableModel newData = backend.queryData(query, params);
        if (newData == null)
        {
          final DefaultTableModel value = new DefaultTableModel();
          if (debugDataSources && Log.isDebugEnabled())
          {
            Log.debug ("Query failed for query '" + query + '\'');
          }
          newParams.put(params, value);
          return value;
        }
        else
        {
          if (debugDataSources && Log.isDebugEnabled())
          {
            printTableModelContents(newData);
          }
          newParams.put(params, newData);
          return newData;
        }
      }
      else
      {
        // Lookup the parameters ...
        final Parameters params = new Parameters(parameters);
        final TableModel data = (TableModel) parameterCache.get(params);
        if (data != null)
        {
          return data;
        }

        final TableModel newData = backend.queryData(query, params);
        if (newData == null)
        {
          final DefaultTableModel value = new DefaultTableModel();
          if (debugDataSources && Log.isDebugEnabled())
          {
            Log.debug ("Query failed for query '" + query + '\'');
          }
          parameterCache.put(params, value);
          return value;
        }
        else
        {
          if (debugDataSources && Log.isDebugEnabled())
          {
            printTableModelContents(newData);
          }
          parameterCache.put(params, newData);
          return newData;
        }
      }
    }
    finally
    {
      final long queryTime = System.currentTimeMillis();
      if (profileDataSources && Log.isDebugEnabled())
      {
        Log.debug(System.identityHashCode(Thread.currentThread()) + ": Query processing time: " + ((queryTime - startTime) / 1000.0));
      }
    }
  }

  /**
   * Closes the report data factory and all report data instances that have been returned by this instance.
   */
  public synchronized void close()
  {
    if (closed == false)
    {
      final Iterator queries = queryCache.values().iterator();
      while (queries.hasNext())
      {
        final HashMap map = (HashMap) queries.next();
        final Iterator dataSets = map.values().iterator();
        while (dataSets.hasNext())
        {
          final TableModel data = (TableModel) dataSets.next();
          if (data instanceof CloseableTableModel)
          {
            final CloseableTableModel ct = (CloseableTableModel) data;
            ct.close();
          }
        }
      }
      backend.close();
      closed = true;
    }
  }

  /**
   * Derives a freshly initialized report data factory, which is independend of the original data factory. Opening or
   * Closing one data factory must not affect the other factories.
   *
   * @return
   */
  public DataFactory derive()
  {
    // If you see that exception, then you've probably tried to use that
    // datafactory from outside of the report processing. You deserve the
    // exception in that case ..
    throw new UnsupportedOperationException
        ("The CachingReportDataFactory cannot be derived.");
  }


  /**
     * Prints a destTable model to standard output.
     *
     * @param mod the model.
     */
  public static void printTableModelContents (final TableModel mod)
  {
    Log.debug ("Tablemodel contains " + mod.getRowCount() + " rows."); //$NON-NLS-1$ //$NON-NLS-2$
    for (int i = 0; i < mod.getColumnCount(); i++)
    {
      Log.debug ("Column: " + i + " Name = " + mod.getColumnName(i) + "; DataType = " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
              + mod.getColumnClass(i));
    }

    Log.debug ("Checking the data inside"); //$NON-NLS-1$
    for (int rows = 0; rows < mod.getRowCount(); rows++)
    {
      for (int i = 0; i < mod.getColumnCount(); i++)
      {
        final Object value = mod.getValueAt(rows, i);
        Log.debug ("ValueAt (" + rows + ", " + i + ") is " + value); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
    }
  }

}
