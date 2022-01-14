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
 * RootLevelBand.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report;

/**
 * A RootLevelBand is directly connected with a report definition or a group.
 * RootLevelBands are used as entry points for the content creation.
 *
 * @author Thomas Morgner
 */
public interface RootLevelBand
{
  /**
   * Assigns the report definition. Don't play with that function, unless you
   * know what you are doing. You might get burned.
   *
   * @param reportDefinition the report definition.
   */
  public void setReportDefinition(ReportDefinition reportDefinition);

  /**
   * Returns the assigned report definition (or null).
   *
   * @return the report definition.
   */
  public ReportDefinition getReportDefinition();

  /**
   * Returns the number of subreports attached to this root level band.
   *
   * @return the number of subreports.
   */
  public int getSubReportCount();

  /**
   * Returns the subreport at the given index-position.
   *
   * @param index the index
   * @return the subreport stored at the given index.
   *
   * @throws IndexOutOfBoundsException if there is no such subreport.
   */
  public SubReport getSubReport(int index);

  /**
   * Returns all sub-reports as array.
   *
   * @return the sub-reports as array.
   */
  public SubReport[] getSubReports();
}
