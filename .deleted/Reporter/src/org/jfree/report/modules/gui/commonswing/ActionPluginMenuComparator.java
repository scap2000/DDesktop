/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id: ActionPluginMenuComparator.java 3574 2007-10-23 11:35:47Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.commonswing;

import java.io.Serializable;

import java.util.Comparator;

/**
 * Creation-Date: 16.11.2006, 16:50:39
 *
 * @author Thomas Morgner
 */
public class ActionPluginMenuComparator implements Comparator, Serializable
{
  public ActionPluginMenuComparator()
  {
  }

  /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or
     * a positive integer as the first argument is less than, equal to, or greater
     * than the second.<p>
     * <p/>
     * The implementor must ensure that <tt>sgn(compare(minX, minY)) == -sgn(compare(minY,
     * x))</tt> for all <tt>minX</tt> and <tt>minY</tt>.  (This implies that
     * <tt>compare(minX, minY)</tt> must throw an exception if and only if
     * <tt>compare(minY, minX)</tt> throws an exception.)<p>
     * <p/>
     * The implementor must also ensure that the relation is transitive:
     * <tt>((compare(minX, minY)&gt;0) &amp;&amp; (compare(minY, z)&gt;0))</tt> implies
     * <tt>compare(minX, z)&gt;0</tt>.<p>
     * <p/>
     * Finally, the implementer must ensure that <tt>compare(minX, minY)==0</tt> implies
     * that <tt>sgn(compare(minX, z))==sgn(compare(minY, z))</tt> for all
     * <tt>z</tt>.<p>
     * <p/>
     * It is generally the case, but <i>not</i> strictly required that
     * <tt>(compare(minX, minY)==0) == (minX.equals(minY))</tt>.  Generally speaking, any
     * comparator that violates this condition should clearly indicate this fact.
     * The recommended language is "Note: this comparator imposes orderings that
     * are inconsistent with equals."
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     * @throws ClassCastException if the arguments' types prevent them from being
     * compared by this Comparator.
     */
  public int compare(final Object o1, final Object o2)
  {
    final ActionPlugin a1 = (ActionPlugin) o1;
    final ActionPlugin a2 = (ActionPlugin) o2;

    final int toolbarOrder = a1.getMenuOrder();
    final int toolbarOrder2 = a2.getMenuOrder();
    if (toolbarOrder < toolbarOrder2)
    {
      return -1;
    }
    if (toolbarOrder > toolbarOrder2)
    {
      return 1;
    }
    return 0;
  }
}
