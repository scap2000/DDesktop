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
 * ReportHeader.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;


/**
 * A report band that is printed once only at the beginning of the report.
 * <p/>
 * A flag can be set forcing the report generator to start a new page after printing the
 * report header.
 * <p/>
 * Note that if there is a page header on the first page of your report, it will be
 * printed above the report header, the logic being that the page header *always* appears
 * at the top of the page.  In many cases, it makes better sense to suppress the page
 * header on the first page of the report (leaving just the report header on page 1).
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class ReportHeader extends AbstractRootLevelBand
{
  /**
   * Constructs a report header, initially containing no elements.
   */
  public ReportHeader ()
  {
  }
}
