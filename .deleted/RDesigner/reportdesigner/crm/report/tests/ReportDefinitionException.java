/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2001-2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 * ReportDefinitionException.java
 * ------------
 * (C) Copyright 2001-2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.pentaho.reportdesigner.crm.report.tests;

import org.jfree.util.StackableException;

/**
 * An exception that is thrown, if a report could not be defined. This
 * encapsulates parse errors as well as runtime exceptions caused by invalid
 * setup code.
 *
 * @author: Thomas Morgner
 */
@SuppressWarnings({"ALL"})
public class ReportDefinitionException extends StackableException
{
    public ReportDefinitionException()
    {
    }


    public ReportDefinitionException(final String message, final Exception ex)
    {
        super(message, ex);
    }


    public ReportDefinitionException(final String message)
    {
        super(message);
    }
}
