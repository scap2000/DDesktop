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
 * Function.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import org.jfree.report.event.ReportListener;

/**
 * The interface for report functions.  A report function separates the business logic
 * from presentation of the result.  The function is called whenever JFreeReport changes
 * its state while generating the report. The working model for the functions is based on
 * cloning the state of the function on certain checkpoints to support the ReportState
 * implementation of JFreeReport.
 * <p/>
 * Although functions support the ReportListener interface, they are not directly added to
 * a report. A report FunctionCollection is used to control the functions. Functions are
 * required to be cloneable.
 * <p/>
 *
 * @author Thomas Morgner
 */
public interface Function extends ReportListener, Expression
{
}
