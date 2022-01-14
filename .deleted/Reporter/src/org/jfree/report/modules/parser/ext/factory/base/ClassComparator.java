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
 * ClassComparator.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.factory.base;

import java.io.Serializable;

import java.util.Comparator;

/**
 * The class comparator can be used to compare and sort classes and their
 * superclasses. The comparator is not able to compare classes which have
 * no relation...
 *
 * @author Thomas Morgner
 * @deprecated Moved to org.jfree.util
 */
public class ClassComparator implements Comparator, Serializable {

    /**
     * Defaultconstructor.
     */
    public ClassComparator() {
        super();
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <P>
     * Note: throws ClassCastException if the arguments' types prevent them from
     * being compared by this Comparator.
     * And IllegalArgumentException if the classes share no relation.
     *
     * The implementor must ensure that <tt>sgn(compare(minX, minY)) ==
     * -sgn(compare(minY, minX))</tt> for all <tt>minX</tt> and <tt>minY</tt>.  (This
     * implies that <tt>compare(minX, minY)</tt> must throw an exception if and only
     * if <tt>compare(minY, minX)</tt> throws an exception.)<p>
     *
     * The implementor must also ensure that the relation is transitive:
     * <tt>((compare(minX, minY)&gt;0) &amp;&amp; (compare(minY, z)&gt;0))</tt> implies
     * <tt>compare(minX, z)&gt;0</tt>.<p>
     *
     * Finally, the implementer must ensure that <tt>compare(minX, minY)==0</tt>
     * implies that <tt>sgn(compare(minX, z))==sgn(compare(minY, z))</tt> for all
     * <tt>z</tt>.<p>
     *
     * It is generally the case, but <i>not</i> strictly required that
     * <tt>(compare(minX, minY)==0) == (minX.equals(minY))</tt>.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     */
    public int compare(final Object o1, final Object o2) {
        final Class c1 = (Class) o1;
        final Class c2 = (Class) o2;
        if (c1.equals(o2)) {
            return 0;
        }
        if (c1.isAssignableFrom(c2)) {
            return -1;
        }
        else {
            if (!c2.isAssignableFrom(c2)) {
                throw new IllegalArgumentException("The classes share no relation");
            }
            return 1;
        }
    }

    /**
     * Checks, whether the given classes are comparable. This method will
     * return true, if one of the classes is assignable from the other class.
     *
     * @param c1 the first class to compare
     * @param c2 the second class to compare
     * @return true, if the classes share a direct relation, false otherwise.
     */
    public boolean isComparable(final Class c1, final Class c2) {
        return (c1.isAssignableFrom(c2) || c2.isAssignableFrom(c1));
    }
}
