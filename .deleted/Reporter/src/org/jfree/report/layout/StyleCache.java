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
 * StyleCache.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import java.util.HashMap;

import org.jfree.report.layout.style.NonPaddingWrapperStyleSheet;
import org.jfree.report.layout.style.SimpleStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;

/**
 * Creation-Date: 26.04.2007, 20:47:23
 *
 * @author Thomas Morgner
 */
public class StyleCache
{
  private static class CacheCarrier
  {
    private long changeTracker;
    private SimpleStyleSheet styleSheet;

    protected CacheCarrier(final long changeTracker, final SimpleStyleSheet styleSheet)
    {
      this.changeTracker = changeTracker;
      this.styleSheet = styleSheet;
    }

    public long getChangeTracker()
    {
      return changeTracker;
    }

    public SimpleStyleSheet getStyleSheet()
    {
      return styleSheet;
    }
  }

  private HashMap cache;
  private StyleKey[] definedStyleKeys;
  private boolean omitPadding;
  private NonPaddingWrapperStyleSheet nonPaddingWrapperStyleSheet;

  public StyleCache(final StyleKey[] definedStyleKeys, final boolean omitPadding)
  {
    this.definedStyleKeys = definedStyleKeys;
    this.omitPadding = omitPadding;
    this.cache = new HashMap();
    this.nonPaddingWrapperStyleSheet = new NonPaddingWrapperStyleSheet();
  }

  public SimpleStyleSheet getStyleSheet(final StyleSheet parent)
  {
    if (omitPadding)
    {
      // this only works, because we know that the created SimpleStyleSheet will not hold any references
      // to this wrapper-stylesheet.
      synchronized (this)
      {
        nonPaddingWrapperStyleSheet.setParent(parent);

        try
        {
          final InstanceID id = nonPaddingWrapperStyleSheet.getId();
          final CacheCarrier cc = (CacheCarrier) cache.get(id);
          if (cc == null)
          {
            final SimpleStyleSheet styleSheet = new SimpleStyleSheet(nonPaddingWrapperStyleSheet, definedStyleKeys);
            cache.put(id, new CacheCarrier(nonPaddingWrapperStyleSheet.getChangeTracker(), styleSheet));
            return styleSheet;
          }

          if (cc.getChangeTracker() != nonPaddingWrapperStyleSheet.getChangeTracker())
          {
            final SimpleStyleSheet styleSheet = new SimpleStyleSheet(nonPaddingWrapperStyleSheet, definedStyleKeys);
            cache.put(id, new CacheCarrier(nonPaddingWrapperStyleSheet.getChangeTracker(), styleSheet));
            return styleSheet;
          }

          return cc.getStyleSheet();
        }
        finally
        {
          nonPaddingWrapperStyleSheet.setParent(null);
        }
      }
    }

    final InstanceID id = parent.getId();
    final CacheCarrier cc = (CacheCarrier) cache.get(id);
    if (cc == null)
    {
      final SimpleStyleSheet styleSheet = new SimpleStyleSheet(parent, definedStyleKeys);
      cache.put(id, new CacheCarrier(parent.getChangeTracker(), styleSheet));
      return styleSheet;
    }

    if (cc.getChangeTracker() != parent.getChangeTracker())
    {
      final SimpleStyleSheet styleSheet = new SimpleStyleSheet(parent, definedStyleKeys);
      cache.put(id, new CacheCarrier(parent.getChangeTracker(), styleSheet));
      return styleSheet;
    }

    return cc.getStyleSheet();
  }
}
