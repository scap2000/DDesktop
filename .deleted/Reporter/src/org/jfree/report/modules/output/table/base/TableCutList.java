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
 * TableCutList.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.base;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creation-Date: 26.08.2007, 15:06:45
 *
 * @author Thomas Morgner
 */
public class TableCutList
{
  /**
   * Encapsulates X- or Y-Cuts. An auxilary CutObject can be removed on non-strict sets.
   */
  public static class CutEntry
  {
    private long coordinate;
    private int position;
    private boolean auxilary;

    protected CutEntry(final long coordinate,
                       final int position,
                       final boolean auxilary)
    {
      this.coordinate = coordinate;
      this.position = position;
      this.auxilary = auxilary;
    }

    public long getCoordinate()
    {
      return coordinate;
    }

    public boolean isAuxilary()
    {
      return auxilary;
    }

    public int getPosition()
    {
      return position;
    }


    public String toString()
    {
      return "org.jfree.report.modules.output.table.base.SheetLayout.BoundsCut{" +
          "auxilary=" + auxilary +
          ", position=" + position +
          '}';
    }

    public void makePermanent()
    {
      auxilary = false;
    }
  }

  private static final CutEntry[] EMPTY_ENTRIES = new CutEntry[0];
  private static final long[] EMPTY_KEYS = new long[0];

  private CutEntry[] entries;
  private long[] keys;
  private int size;
  private int increment;
  private int foundIdx;


  public TableCutList(final int increment)
  {
    if (increment < 1)
    {
      throw new IllegalArgumentException();
    }
    this.increment = increment;
    entries = EMPTY_ENTRIES;
    keys = EMPTY_KEYS;
  }

  public TableCutList()
  {
    this(100);
  }

  public int size()
  {
    return size;
  }

  /**
   * Ensures, that the list backend can store at least <code>c</code> elements. This method does nothing, if the new
   * capacity is less than the current capacity.
   *
   * @param c the new capacity of the list.
   */
  private void ensureCapacity(final int c)
  {
    if (keys.length <= c)
    {
      final long[] newKeys = new long[Math.max(keys.length + increment, c + 1)];
      System.arraycopy(keys, 0, newKeys, 0, size);
      keys = newKeys;

      final CutEntry[] newCuts = new CutEntry[Math.max(entries.length + increment, c + 1)];
      System.arraycopy(entries, 0, newCuts, 0, size);
      entries = newCuts;
    }
  }

  public boolean put(final long key, final CutEntry entry)
  {
    if (size > 0)
    {
      // try a short-cut, which is usefull for y-coordinates (which are almost always sorted).
      if (key > keys[size - 1])
      {
        ensureCapacity(size + 1);
        keys[size] = key;
        entries[size] = entry;
        size += 1;
        return true;
      }
    }

    // ok, check, whether this is a new key ..
    final int position = binarySearch(keys, key, 0, size);
    if (position >= 0)
    {
      return false;
    }

    ensureCapacity(size + 1);

    final int insertPoint = -(position + 1);
    if (insertPoint < size)
    {
      // shift the contents ..
      System.arraycopy(keys, insertPoint, keys, insertPoint + 1, size - insertPoint);
      System.arraycopy(entries, insertPoint, entries, insertPoint + 1, size - insertPoint);
    }
    keys[insertPoint] = key;
    entries[insertPoint] = entry;
    size += 1;
    return true;
  }


  /**
   * Performs a binary-search, but includes some optimizations in case we search for the same key all the time.
   *
   * @param pos the starting position of the box.
   * @return the position as positive integer or a negative integer indicating the insert-point.
   */
  private int findKeyInternal(final long pos)
  {
//    int foundIndex = this.foundIdx;
//    final int retval = _findKeyInternal(pos);
//    Log.debug ("FoundIndex: " + foundIndex + " -> " + foundIdx);
//    return retval;
//  }
//  private int _findKeyInternal(final long pos)
//  {
    int start = 0;
    int end = size;
    if (foundIdx >= 0 && foundIdx < size)
    {
      final long foundPos = keys[foundIdx];
      if (foundPos == pos)
      {
        return foundIdx;
      }

      if (pos < foundPos)
      {
        end = foundIdx;
      }
      if (pos > foundPos)
      {
        start = foundIdx + 1;
      }
    }

    final int i = binarySearch(keys, pos, start, end);
    if (i > -1)
    {
      foundIdx = (i - 1);
      return i;
    }
    if (i == -1)
    {
      foundIdx = -1;
      return -1;
    }

    foundIdx = -(i + 2);
    return i;

  }


  public boolean remove(final long key)
  {
    final int position = findKeyInternal(key);
    //binarySearch(keys, key, 0, size);
    if (position < 0)
    {
      return false;
    }

    final int shiftElements = size - position - 1;
    if (shiftElements == 0)
    {
      keys[position] = 0;
      entries[position] = null;
      size -= 1;
      return true;
    }

    size -= 1;
    System.arraycopy(keys, position + 1, keys, position, shiftElements);
    System.arraycopy(entries, position + 1, entries, position, shiftElements);

    keys[size] = 0;
    entries[size] = null;
    return true;
  }

  public CutEntry get(final long key)
  {
    if (size == 0)
    {
      return null;
    }

    if (key > keys[size - 1])
    {
      return null;
    }

    final int position = findKeyInternal(key);
    //binarySearch(keys, key, 0, size);
    if (position < 0)
    {
      return null;
    }
    return entries[position];
  }

  public CutEntry getPrevious(final long key)
  {
    if (size == 0)
    {
      return null;
    }
    if (key > keys[size - 1])
    {
      return entries[size - 1];
    }

    final int position = findKeyInternal(key);
    //binarySearch(keys, key, 0, size);
    if (position == 0)
    {
      return null;
    }
    if (position > 0)
    {
      return entries[position - 1];
    }

    final int insertPoint = -(position + 2);
    return entries[insertPoint];
  }

  public boolean containsKey(final long key)
  {
    if (size > 0)
    {
      // try a short-cut, which is usefull for y-coordinates (which are almost always sorted).
      if (key > keys[size - 1])
      {
        return false;
      }
    }
    return findKeyInternal(key) >= 0;
  }

  private static int binarySearch(final long[] array, final long key, final int start, final int end)
  {
    int low = start;
    int high = end - 1;

    while (low <= high)
    {
      final int mid = (low + high) >> 1;
      final long midVal = array[mid];

      if (midVal < key)
      {
        low = mid + 1;
      }
      else if (midVal > key)
      {
        high = mid - 1;
      }
      else
      {
        return mid; // key found
      }
    }
    return -(low + 1);  // key not found.
  }

  public CutEntry[] getRawEntries()
  {
    return entries;
  }

  /**
   * Copys the list contents into a new array.
   *
   * @return the list contents as array.
   */
  public long[] getKeys()
  {
    if (size == 0)
    {
      return EMPTY_KEYS;
    }

    if (size == keys.length)
    {
      return (long[]) keys.clone();
    }

    final long[] retval = new long[size];
    System.arraycopy(keys, 0, retval, 0, size);
    return retval;
  }

  public long[] getKeys(long[] retval)
  {
    if (retval == null || retval.length < size)
    {
      retval = new long[size];
    }
    System.arraycopy(keys, 0, retval, 0, size);
    return retval;
  }

  /**
   * Tries to locate the key that matches the given key-parameter as closely as possible. If greater is set to true,
   * then - if the coordinate is not contained in the list - the next coordinate is given, else the previous one is
   * returned.
   *
   * @param coordinate
   * @param greater
   * @return
   */
  public int findKeyPosition(final long coordinate, final boolean greater)
  {
    final int pos = findKeyInternal(coordinate);
    //binarySearch(keys, coordinate, 0, size);
    if (pos == size)
    {
      //return xMaxBounds;
      // warning: This might be stupid
      return size - 1;
    }
    if (pos >= 0)
    {
      return pos;
    }

    // the coordinate is greater than the largest key in this list ..
    if (pos == -(size + 1))
    {
      return size - 1;
    }

    // the coordinate is not a key, but smaller than the largest key in this list..
    if (greater)
    {
      return (-pos - 1);
    }
    else
    {
      return (-pos - 2);
    }
  }

  public long getKeyAt(final int indexPosition)
  {
    if (indexPosition >= size || indexPosition < 0)
    {
      throw new IndexOutOfBoundsException();
    }
    return keys[indexPosition];
  }

  public CutEntry getValueAt(final int indexPosition)
  {
    if (indexPosition >= size || indexPosition < 0)
    {
      throw new IndexOutOfBoundsException();
    }
    return entries[indexPosition];
  }

  public long findKey(final long key, final boolean upperBounds)
  {
    final int pos = findKeyPosition(key, upperBounds);
    return keys[pos];
  }

  /**
   * Expects a sorted (ascending) list of cut-entries that should be removed. You will run into troubles if the list is
   * not sorted.
   *
   * @param cuts
   */
  public void removeAll(final ArrayList cuts)
  {
    if (cuts.isEmpty())
    {
      return;
    }

    final CutEntry[] cutArray = (CutEntry[]) cuts.toArray(new CutEntry[cuts.size()]);
    int cutIndex = 0;
    long currentCut = cutArray[0].getCoordinate();

    int targetPosition = 0;
    int sourcePosition = 0;
    for (; sourcePosition < keys.length; sourcePosition++)
    {
      final long key = keys[sourcePosition];
      if (key == currentCut)
      {
        // do nothing ..
        cutIndex += 1;
        if (cutIndex == cutArray.length)
        {
          System.arraycopy(keys, sourcePosition + 1, keys, targetPosition, keys.length - sourcePosition - 1);
          System.arraycopy(entries, sourcePosition + 1, entries, targetPosition, entries.length - sourcePosition - 1);
          targetPosition = size - cutIndex;
          break;
        }
        currentCut = cutArray[cutIndex].getCoordinate();
      }
      else
      {
        keys[targetPosition] = key;
        entries[targetPosition] = entries[sourcePosition];
        targetPosition += 1;
      }
    }

    Arrays.fill(keys, targetPosition, size, 0);
    Arrays.fill(entries, targetPosition, size, null);
    size = targetPosition;
  }

  public static void main(String[] args)
  {
    TableCutList tcl = new TableCutList(7);
    tcl.put(1000, new CutEntry(1000, 0, true));
    tcl.put(2000, new CutEntry(2000, 0, true));
    tcl.put(5000, new CutEntry(5000, 0, true));
    tcl.put(6000, new CutEntry(6000, 0, true));
    tcl.put(7000, new CutEntry(7000, 0, true));
    tcl.put(9000, new CutEntry(9000, 0, true));
    tcl.put(10000, new CutEntry(10000, 0, true));

    final ArrayList list = new ArrayList();
    list.add (new CutEntry(1000, 0, true));
    list.add (new CutEntry(5000, 0, true));
//    list.add (new CutEntry(10000, 0, true));

    tcl.removeAll(list);
    
    System.out.println();
  }

}
