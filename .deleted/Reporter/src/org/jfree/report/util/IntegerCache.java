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
 * IntegerCache.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.util;

/**
 * A class that caches commonly used Integer-objects. This reduces the number of objects created when processing the
 * report. JDK 1.5 provides a similiar facility, but this cheap functionality is not worth upgrading to that JDK.
 *
 * @author Thomas Morgner
 */
public class IntegerCache
{
  /**
   * A cache holding the first 1000 integers.
   */
  private static Integer[] cachedNumbers;

  static
  {
    cachedNumbers = new Integer[1000];
    for (int i = 0; i < cachedNumbers.length; i++)
    {
      cachedNumbers[i] = new Integer(i);
    }
  }

  /**
   * Default constructor.
   */
  private IntegerCache()
  {
  }

  /**
   * Returns the integer-object for the given primitive integer.
   *
   * @param i the primitive integer value.
   * @return the constructed integer object.
   */
  public static Integer getInteger(final int i)
  {
    if (i < 0)
    {
      return new Integer(i);
    }
    if (i > 999)
    {
      return new Integer(i);
    }
    return cachedNumbers[i];
  }
}
