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
 * SimpleDateFormatObjectDescription.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.base;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * An object-description for a <code>SimpleDateFormat</code> object.
 *
 * @author Thomas Morgner
 */
public class SimpleDateFormatObjectDescription extends BeanObjectDescription {

    /**
     * Creates a new object description.
     */
    public SimpleDateFormatObjectDescription() {
        this(SimpleDateFormat.class);
    }

    /**
     * Creates a new object description.
     *
     * @param className  the class.
     */
    public SimpleDateFormatObjectDescription(final Class className) {
        this(className, true);
    }

    /**
     * Creates a new object description.
     *
     * @param className  the class.
     * @param init  initialise?
     */
    public SimpleDateFormatObjectDescription(final Class className, final boolean init) {
        super(className, false);
        setParameterDefinition("2DigitYearStart", Date.class);
        setParameterDefinition("calendar", Calendar.class);
        setParameterDefinition("dateFormatSymbols", DateFormatSymbols.class);
        setParameterDefinition("lenient", Boolean.TYPE);
        setParameterDefinition("numberFormat", NumberFormat.class);
//        setParameterDefinition("timeZone", TimeZone.class);
        setParameterDefinition("localizedPattern", String.class);
        setParameterDefinition("pattern", String.class);
        ignoreParameter("localizedPattern");
        ignoreParameter("pattern");
    }

    /**
     * Sets the parameters of this description object to match the supplied object.
     *
     * @param o  the object.
     *
     * @throws ObjectFactoryException if there is a problem while reading the
     * properties of the given object.
     */
    public void setParameterFromObject(final Object o)
        throws ObjectFactoryException {
        super.setParameterFromObject(o);
        final SimpleDateFormat format = (SimpleDateFormat) o;
        setParameter("pattern", format.toPattern());
    }

    /**
     * Creates an object based on this description.
     *
     * @return The object.
     */
    public Object createObject() {
        final SimpleDateFormat format = (SimpleDateFormat) super.createObject();
        if (getParameter("pattern") != null) {
            format.applyPattern((String) getParameter("pattern"));
        }
        return format;
    }

}
