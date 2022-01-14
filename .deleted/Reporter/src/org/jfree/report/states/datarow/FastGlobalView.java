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
 * FastGlobalView.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.states.datarow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.jfree.report.DataRow;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.util.IntegerCache;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 10.08.2007, 14:07:32
 *
 * @author Thomas Morgner
 */
public final class FastGlobalView implements DataRow
{
  private HashSet invalidColumns;
  private boolean modifiableNameCache;
  private HashMap nameCache;
  private String[] columnNames;
  private Boolean[] columnChanged;
  private Object[] columnValue;
  private Object[] columnOldValue;
  private int[] columnPrev;
  private int length;
  private static final boolean DEBUG = false;
  private boolean warnInvalidColumns;

  public FastGlobalView(final FastGlobalView parent)
  {
    if (parent.modifiableNameCache)
    {
      this.nameCache = (HashMap) parent.nameCache.clone();
      this.modifiableNameCache = false;
      this.columnNames = (String[]) parent.columnNames.clone();
    }
    else
    {
      this.nameCache = parent.nameCache;
      this.columnNames = parent.columnNames;
      this.modifiableNameCache = false;
    }
    this.columnChanged = (Boolean[]) parent.columnChanged.clone();
    this.columnValue = (Object[]) parent.columnValue.clone();
    this.columnOldValue = (Object[]) parent.columnOldValue.clone();
    this.columnPrev = (int[]) parent.columnPrev.clone();
    this.length = parent.length;
    this.warnInvalidColumns = parent.warnInvalidColumns;
    this.invalidColumns = parent.invalidColumns;
  }

  public FastGlobalView()
  {
    this.warnInvalidColumns = "true".equals
        (JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty("org.jfree.report.WarnInvalidColumns"));
    if (warnInvalidColumns)
    {
      this.invalidColumns = new HashSet();
    }
    this.nameCache = new HashMap();
    this.modifiableNameCache = true;
    this.columnNames = new String[20];
    this.columnChanged = new Boolean[20];
    this.columnValue = new Object[20];
    this.columnOldValue = new Object[20];
    this.columnPrev = new int[20];
  }

  public Object get(final int col)
  {
    if (col < 0 || col >= length)
    {
      throw new IndexOutOfBoundsException("Column-Index " + col + " is invalid.");
    }
    return columnValue[col];
  }

  public Object get(final String col) throws IllegalStateException
  {
    final int idx = findColumn(col);
    if (idx < 0)
    {
      if (warnInvalidColumns)
      {
        if (invalidColumns.add(col))
        {
          Log.warn("Warning: Data-Set does not contain a column with name '" + col + "'");
        }
      }
      return null;
    }
    return get(idx);
  }

  public String getColumnName(final int col)
  {
    if (col < 0 || col >= length)
    {
      throw new IndexOutOfBoundsException("Column-Index " + col + " is invalid.");
    }
    return columnNames[col];
  }

  public int findColumn(final String name)
  {
    final Integer o = (Integer) nameCache.get(name);
    if (o == null)
    {
      return -1;
    }
    return o.intValue();
  }

  public int getColumnCount()
  {
    return length;
  }

  public boolean isChanged(final String name)
  {
    final int idx = findColumn(name);
    if (idx < 0)
    {
      if (warnInvalidColumns)
      {
        if (invalidColumns.add(name))
        {
          Log.warn("Warning: Data-Set does not contain a column with name '" + name + "'");
        }
      }
      return false;
    }
    return isChanged(idx);
  }

  public boolean isChanged(final int col)
  {
    if (col < 0 || col >= length)
    {
      throw new IndexOutOfBoundsException("Column-Index " + col + " is invalid.");
    }
    final Boolean val = columnChanged[col];
    if (val != null)
    {
      return val.booleanValue();
    }
    final Boolean computedFlag = computeChangedFlag(col);
    columnChanged[col] = computedFlag;
    return computedFlag.booleanValue();
  }

  private Boolean computeChangedFlag(final int col)
  {
    Log.debug("Computing these values should be no longer necessary.");
    // todo
    return Boolean.FALSE;
  }

  public FastGlobalView derive()
  {
    return new FastGlobalView(this);
  }

  public FastGlobalView advance()
  {
    final FastGlobalView advanced = new FastGlobalView(this);
    Arrays.fill(advanced.columnChanged, null);
    System.arraycopy(advanced.columnValue, 0, advanced.columnOldValue, 0, length);
    return advanced;
  }

  public void removeColumn(final String name)
  {
    final Integer o = (Integer) nameCache.get(name);
    if (o == null)
    {
      return;
    }
    final int idx = o.intValue();
    if (DEBUG)
    {
      Log.debug(
          "Removing column " + name + " (Length: " + length + " NameCache: " +
              nameCache.size() + ", Idx: " + idx);
    }

    if (modifiableNameCache == false)
    {
      this.columnNames = (String[]) columnNames.clone();
      this.nameCache = (HashMap) nameCache.clone();
      this.modifiableNameCache = true;
    }

    if (idx == (length - 1))
    {
      columnChanged[idx] = null;
      columnNames[idx] = null;
      columnValue[idx] = null;
      if (columnPrev[idx] == -1)
      {
        nameCache.remove(name);
      }
      else
      {
        nameCache.put(name, IntegerCache.getInteger(columnPrev[idx]));
      }
      // thats the easy case ..
      length -= 1;
      return;
    }

    Log.warn("Out of order removeal of a column: " + name);

    if (columnPrev[idx] == -1)
    {
      nameCache.remove(name);
    }
    else
    {
      nameCache.put(name, IntegerCache.getInteger(columnPrev[idx]));
    }

    final int moveStartIdx = idx + 1;
    final int moveLength = length - moveStartIdx;
    System.arraycopy(columnNames, moveStartIdx, columnNames, idx, moveLength);
    System.arraycopy(columnChanged, moveStartIdx, columnChanged, idx, moveLength);
    System.arraycopy(columnOldValue, moveStartIdx, columnOldValue, idx, moveLength);
    System.arraycopy(columnValue, moveStartIdx, columnValue, idx, moveLength);
    System.arraycopy(columnPrev, moveStartIdx, columnPrev, idx, moveLength);
    columnNames[length - 1] = null;
    columnChanged[length - 1] = null;
    columnOldValue[length - 1] = null;
    columnPrev[length - 1] = 0;

    // Now it gets expensive: Rebuild the namecache ..
    final int newLength = moveLength + idx;
    nameCache.clear();
    for (int i = 0; i < newLength; i++)
    {
      final String columnName = columnNames[i];
      final Integer oldVal = (Integer) nameCache.get(columnName);
      nameCache.put(columnName, IntegerCache.getInteger(i));
      if (oldVal != null)
      {
        columnPrev[i] = oldVal.intValue();
      }
      else
      {
        columnPrev[i] = -1;
      }
    }
    length -= 1;
    Log.debug("New Namecache: " + nameCache);
  }

  public void putField(final String name,
                       final Object value,
                       final boolean update)
  {
    if (DEBUG)
    {
      if (update)
      {
        Log.debug("  +   : " + name);
      }
      else
      {
        Log.debug("Adding: " + name);
      }

    }

    final Integer o = (Integer) nameCache.get(name);

    if (update == false)
    {
      if (modifiableNameCache == false)
      {
        this.columnNames = (String[]) columnNames.clone();
        this.nameCache = (HashMap) nameCache.clone();
        this.modifiableNameCache = true;
      }

      // A new column ...
      ensureCapacity(length + 1);
      columnNames[length] = name;
      columnValue[length] = value;
      if (o == null)
      {
        columnPrev[length] = -1;
      }
      else
      {
        columnPrev[length] = o.intValue();
      }

      columnOldValue[length] = null;
      columnChanged[length] = Boolean.TRUE;
      nameCache.put(name, IntegerCache.getInteger(length));
      length += 1;
    }
    else
    {
      // Updating an existing column ...
      if (o == null)
      {
        throw new IllegalStateException("Update to a non-existing column: " + name);
      }

      final int idx = o.intValue();
      columnNames[idx] = name;
      columnOldValue[idx] = columnValue[idx];
      columnValue[idx] = value;
      if (ObjectUtilities.equal(columnOldValue[idx], columnValue[idx]))
      {
        columnChanged[idx] = Boolean.FALSE;
      }
      else
      {
        columnChanged[idx] = Boolean.TRUE;
      }
    }
  }

  private void ensureCapacity(final int requestedSize)
  {
    final int capacity = this.columnNames.length;
    if (capacity > requestedSize)
    {
      return;
    }
    final int newSize = Math.max(capacity << 1, requestedSize + 10);

    final String[] newColumnNames = new String[newSize];
    System.arraycopy(columnNames, 0, newColumnNames, 0, length);
    this.columnNames = newColumnNames;

    final Boolean[] newColumnChanged = new Boolean[newSize];
    System.arraycopy(columnChanged, 0, newColumnChanged, 0, length);
    this.columnChanged = newColumnChanged;

    final int[] newColumnPrev = new int[newSize];
    System.arraycopy(columnPrev, 0, newColumnPrev, 0, length);
    this.columnPrev = newColumnPrev;

    final Object[] newColumnValue = new Object[newSize];
    System.arraycopy(columnValue, 0, newColumnValue, 0, length);
    this.columnValue = newColumnValue;

    final Object[] newOldColumnValue = new Object[newSize];
    System.arraycopy(columnOldValue, 0, newOldColumnValue, 0, length);
    this.columnOldValue = newOldColumnValue;
  }
}
