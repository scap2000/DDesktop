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
 * ActionPluginComparator.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.base.internal;

import java.io.Serializable;

import java.util.Comparator;

import org.jfree.report.modules.gui.commonswing.ActionPlugin;

/**
 * Creation-Date: 17.05.2007, 20:14:23
 *
 * @author Thomas Morgner
 */
public class ActionPluginComparator implements Comparator, Serializable
{
  public ActionPluginComparator()
  {
  }

  /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.<p>
     * <p/>
     * The implementor must ensure that <tt>sgn(compare(minX, minY)) == -sgn(compare(minY, minX))</tt> for all <tt>minX</tt> and
     * <tt>minY</tt>.  (This implies that <tt>compare(minX, minY)</tt> must throw an exception if and only if <tt>compare(minY,
     * x)</tt> throws an exception.)<p>
     * <p/>
     * The implementor must also ensure that the relation is transitive: <tt>((compare(minX, minY)&gt;0) &amp;&amp; (compare(minY,
     * z)&gt;0))</tt> implies <tt>compare(minX, z)&gt;0</tt>.<p>
     * <p/>
     * Finally, the implementer must ensure that <tt>compare(minX, minY)==0</tt> implies that <tt>sgn(compare(minX,
     * z))==sgn(compare(minY, z))</tt> for all <tt>z</tt>.<p>
     * <p/>
     * It is generally the case, but <i>not</i> strictly required that <tt>(compare(minX, minY)==0) == (minX.equals(minY))</tt>.
     * Generally speaking, any comparator that violates this condition should clearly indicate this fact.  The recommended
     * language is "Note: this comparator imposes orderings that are inconsistent with equals."
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     * than the second.
     * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
     */
  public int compare(final Object o1, final Object o2)
  {
    final ActionPlugin ap1 = (ActionPlugin) o1;
    final ActionPlugin ap2 = (ActionPlugin) o2;
    final int to1 = ap1.getToolbarOrder();
    final int to2 = ap2.getToolbarOrder();

    if (to1 < to2)
    {
      return -1;
    }
    else if (to1 > to2)
    {
      return +1;
    }
    return 0;
  }
}
