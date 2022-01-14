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
 * ReportInitialisationException.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import org.jfree.util.StackableException;

/**
 * An exception that is thrown when a report fails to initialise.
 *
 * @author Thomas Morgner.
 * @deprecated No longer used. Will be removed in the next version.
 */
public class ReportInitialisationException extends StackableException
{
  /**
   * Constructs an <code>Exception</code> with no specified detail message.
   */
  public ReportInitialisationException ()
  {
  }

  /**
   * Constructs an <code>Exception</code> with the specified detail message.
   *
   * @param s the detail message.
   */
  public ReportInitialisationException (final String s)
  {
    super(s);
  }

  /**
   * Constructs an <code>Exception</code> with the specified detail message.
   *
   * @param s the detail message.
   * @param e the parent exception.
   */
  public ReportInitialisationException (final String s, final Exception e)
  {
    super(s, e);
  }
}
