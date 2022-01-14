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
 * CacheStyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.text;

import java.util.Arrays;

import org.jfree.report.style.AbstractStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;

/**
 * This stylesheet collects all style-properties that may be used during the text-creation.
 *
 * @author Thomas Morgner
 */
public class CacheStyleSheet extends AbstractStyleSheet
{
  public static final Object UNDEFINED = new Object();
  private Object[] cache;
  private StyleSheet parent;
  private boolean locked;

  public CacheStyleSheet(final StyleSheet parent, final int styleKeyCount)
  {
    this.parent = parent;
    this.cache = new Object[styleKeyCount];
    Arrays.fill(cache, UNDEFINED);
  }

  public Object getStyleProperty(final StyleKey key, final Object defaultValue)
  {
    final Object value = parent.getStyleProperty(key, defaultValue);
    if (!locked)
    {
      cache[key.getIdentifier()] = value;
    }
    return value;
  }

  public StyleSheet getParent()
  {
    return parent;
  }
//
//  public void setParent(final StyleSheet parent)
//  {
//    this.cache.clear();
//    this.parent = parent;
//  }

  public Object[] getEntries()
  {
    return this.cache;
  }

  public void lock()
  {
    this.locked = true;
    this.cache = null;
  }
}
