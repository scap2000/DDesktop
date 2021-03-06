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
 * ClassFactoryImpl.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.base;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.util.ClassComparator;
import org.jfree.util.Configuration;

/**
 * An abstract class that implements the {@link ClassFactory} interface.
 *
 * @author Thomas Morgner.
 */
public abstract class ClassFactoryImpl implements ClassFactory {

    /** Storage for the classes. */
    private HashMap classes;
    /** A class comparator for searching the super class */
    private ClassComparator comparator;
    /** The parser/report configuration */
    private Configuration config;

    /**
     * Creates a new class factory.
     */
    protected ClassFactoryImpl() {
        this.classes = new HashMap();
        this.comparator = new ClassComparator();
    }

    /**
     * Returns the class comparator used to sort the super classes of an object.
     *
     * @return the class comparator.
     */
    public ClassComparator getComparator() {
        return this.comparator;
    }

    /**
     * Returns an object-description for a class.
     *
     * @param c  the class.
     *
     * @return An object description.
     */
    public ObjectDescription getDescriptionForClass(final Class c) {
        final ObjectDescription od = (ObjectDescription) this.classes.get(c);
        if (od == null) {
            return null;
        }
        return od.getInstance();
    }

    /**
     * Returns the most concrete object-description for the super class of a class.
     *
     * @param d  the class.
     * @param knownSuperClass a known supported superclass or null, if no superclass
     * is known yet.
     *
     * @return The object description.
     */
    public ObjectDescription getSuperClassObjectDescription
        (final Class d, ObjectDescription knownSuperClass) {

        if (d == null) {
            throw new NullPointerException("Description class must not be null.");
        }
        final Iterator iterator = this.classes.keySet().iterator();
        while (iterator.hasNext()) {
            final Class keyClass = (Class) iterator.next();
            if (keyClass.isAssignableFrom(d)) {
                final ObjectDescription od = (ObjectDescription) this.classes.get(keyClass);
                if (knownSuperClass == null) {
                    knownSuperClass = od;
                }
                else {
                    if (this.comparator.isComparable
                        (knownSuperClass.getObjectClass(), od.getObjectClass())) {
                        if (this.comparator.compare
                            (knownSuperClass.getObjectClass(), od.getObjectClass()) < 0) {
                            knownSuperClass = od;
                        }
                    }
                }
            }
        }
        if (knownSuperClass == null) {
            return null;
        }
        return knownSuperClass.getInstance();
    }

    /**
     * Registers an object description with the factory.
     *
     * @param key  the key.
     * @param od  the object description.
     */
    protected void registerClass(final Class key, final ObjectDescription od) {
        this.classes.put(key, od);
        if (this.config != null) {
            od.configure(this.config);
        }
    }

    /**
     * Returns an iterator that provides access to the registered object definitions.
     *
     * @return The iterator.
     */
    public Iterator getRegisteredClasses() {
        return this.classes.keySet().iterator();
    }


    /**
     * Configures this factory. The configuration contains several keys and
     * their defined values. The given reference to the configuration object
     * will remain valid until the report parsing or writing ends.
     * <p>
     * The configuration contents may change during the reporting.
     *
     * @param config the configuration, never null
     */
    public void configure(final Configuration config) {
        if (config == null) {
            throw new NullPointerException("The given configuration is null");
        }
        if (this.config != null) {
            // already configured ... ignored
            return;
        }

        this.config = config;
        final Iterator it = this.classes.values().iterator();
        while (it.hasNext()) {
            final ObjectDescription od = (ObjectDescription) it.next();
            od.configure(config);
        }
    }

    /**
     * Returns the currently set configuration or null, if none was set.
     *
     * @return the configuration.
     */
    public Configuration getConfig() {
        return this.config;
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
        if (!(o instanceof ClassFactoryImpl)) {
            return false;
        }

        final ClassFactoryImpl classFactory = (ClassFactoryImpl) o;

        if (!this.classes.equals(classFactory.classes)) {
            return false;
        }

        return true;
    }

    /**
     * Returns a hash code.
     *
     * @return A hash code.
     */
    public int hashCode() {
        return this.classes.hashCode();
    }
}
