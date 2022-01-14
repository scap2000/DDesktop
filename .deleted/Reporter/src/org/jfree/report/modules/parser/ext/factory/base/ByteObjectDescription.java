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
 * ByteObjectDescription.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.base;

/**
 * An object-description for a <code>Byte</code> object.
 *
 * @author Thomas Morgner
 */
public class ByteObjectDescription extends AbstractObjectDescription {

    /**
     * Creates a new object description.
     */
    public ByteObjectDescription() {
        super(Byte.class);
        setParameterDefinition("value", String.class);
    }

    /**
     * Creates a new object (<code>Byte</code>) based on this description object.
     *
     * @return The <code>Byte</code> object.
     */
    public Object createObject() {
        final String o = (String) getParameter("value");
        return Byte.valueOf(o.trim());
    }

    /**
     * Sets the parameters of this description object to match the supplied object.
     *
     * @param o  the object (should be an instance of <code>Byte</code>.
     * @throws ObjectFactoryException if there is a problem
     * while reading the properties of the given object.
     */
    public void setParameterFromObject(final Object o) throws ObjectFactoryException {
        if (!(o instanceof Byte)) {
            throw new ObjectFactoryException("The given object is no java.lang.Byte.");
        }
        setParameter("value", String.valueOf(o));
    }

    /**
     * Tests for equality.
     *
     * @param o  the object to test.
     *
     * @return A boolean.
     */
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractObjectDescription)) {
            return false;
        }

        final AbstractObjectDescription abstractObjectDescription = (AbstractObjectDescription) o;

        if (Byte.TYPE.equals(abstractObjectDescription.getObjectClass())) {
            return true;
        }
        if (Byte.class.equals(abstractObjectDescription.getObjectClass())) {
            return true;
        }
        return false;
    }

    /**
     * Returns a hash code for the object.
     *
     * @return The hash code.
     */
    public int hashCode() {
        return getObjectClass().hashCode();
    }

}
