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
 * CacheBoxShifter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;

/**
 * By keeping the shifting in a separate class, we can optimize it later without having to touch the other code.
 * Remember: Recursive calls can be evil in complex documents..
 *
 * @author Thomas Morgner
 */
public final class CacheBoxShifter
{
  private CacheBoxShifter()
  {
  }


  public static void shiftBox(final RenderNode box, final long amount)
  {
    if (amount == 0)
    {
      return;
    }
    if (amount < 0)
    {
      throw new IllegalArgumentException("Cannot shift upwards: " + amount);
    }

    box.shiftCached(amount);
    if (box instanceof RenderBox)
    {
      CacheBoxShifter.shiftBoxInternal((RenderBox) box, amount);
    }
  }

  public static void shiftBoxUnchecked(final RenderNode box, final long amount)
  {
    if (amount == 0)
    {
      return;
    }

    box.shiftCached(amount);
    if (box instanceof RenderBox)
    {
      CacheBoxShifter.shiftBoxInternal((RenderBox) box, amount);
    }
  }

  public static void shiftBoxChilds(final RenderBox box, final long amount)
  {
    if (amount == 0)
    {
      return;
    }
    CacheBoxShifter.shiftBoxInternal(box, amount);
  }

  private static void shiftBoxInternal(final RenderBox box, final long amount)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      node.shiftCached(amount);
      if (node instanceof RenderBox)
      {
        CacheBoxShifter.shiftBoxInternal((RenderBox) node, amount);
      }
      node = node.getNext();
    }
  }


  public static void extendHeight(final RenderNode node, final long amount)
  {
    if (amount < 0)
    {
      throw new IllegalArgumentException("Cannot shrink elements.");
    }
    if (node == null || amount == 0)
    {
      return;
    }

    node.setCachedHeight(node.getCachedHeight() + amount);

    RenderBox parent = node.getParent();
    while (parent != null)
    {
      parent.setCachedHeight(parent.getCachedHeight() + amount);
      parent = parent.getParent();
    }
  }
}
