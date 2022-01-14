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
 * AbstractRootLevelBand.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.util.ArrayList;

/**
 * The root-level band is the container that is processed by a report-state. The root-level band processing
 * is atomic - so either the full band is processed or not processed at all. 
 *
 * @author Thomas Morgner
 */
public abstract class AbstractRootLevelBand extends Band implements RootLevelBand
{
  /**
   * A empty array. (For performance reasons.)
   */
  private static final SubReport[] EMPTY_SUBREPORTS = new SubReport[0];
  /**
   * The list of follow-up root-level sub-reports.
   */
  private ArrayList subReports;

  /**
   * Constructs a new band (initially empty).
   */
  protected AbstractRootLevelBand()
  {
  }

  /**
   * Constructs a new band with the given pagebreak attributes. Pagebreak
   * attributes have no effect on subbands.
   *
   * @param pagebreakAfter  defines, whether a pagebreak should be done after
   *                        that band was printed.
   * @param pagebreakBefore defines, whether a pagebreak should be done before
   *                        that band gets printed.
   */
  protected AbstractRootLevelBand(final boolean pagebreakBefore,
                                  final boolean pagebreakAfter)
  {
    super(pagebreakBefore, pagebreakAfter);
  }

  /**
   * Assigns the report definition. Don't play with that function, unless you
   * know what you are doing. You might get burned.
   *
   * @param reportDefinition the report definition.
   */
  public void setReportDefinition(final ReportDefinition reportDefinition)
  {
    super.setReportDefinition(reportDefinition);
  }

  /**
   * Returns the number of subreports attached to this root level band.
   *
   * @return the number of subreports.
   */
  public int getSubReportCount()
  {
    if (subReports == null)
    {
      return 0;
    }
    return subReports.size();
  }

  /**
   * Clones this band and all elements contained in this band. After the cloning
   * the band is no longer connected to a report definition.
   *
   * @return the clone of this band.
   *
   * @throws CloneNotSupportedException if this band or an element contained in
   *                                    this band does not support cloning.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    final AbstractRootLevelBand rootLevelBand = (AbstractRootLevelBand) super.clone();
    if (rootLevelBand.subReports != null)
    {
      rootLevelBand.subReports.clone();
    }
    return rootLevelBand;
  }

  /**
   * Returns the subreport at the given index-position.
   *
   * @param index the index
   * @return the subreport stored at the given index.
   *
   * @throws IndexOutOfBoundsException if there is no such subreport.
   */
  public SubReport getSubReport(final int index)
  {
    if (subReports == null)
    {
      throw new IndexOutOfBoundsException();
    }
    return (SubReport) subReports.get(index);
  }

  /**
   * Attaches a new subreport at the end of the list.
   *
   * @param report the subreport, never null.
   */
  public void addSubReport(final SubReport report)
  {
    if (report == null)
    {
      throw new NullPointerException("Parameter 'report' must not be null");
    }

    if (subReports == null)
    {
      subReports = new ArrayList();
    }
    subReports.add(report);
  }

  /**
   * Removes the given subreport from the list of attached sub-reports.
   *
   * @param report the subreport to be removed.
   */
  public void removeSubreport(final SubReport report)
  {
    if (report == null)
    {
      throw new NullPointerException("Parameter 'report' must not be null");
    }
    if (subReports == null)
    {
      return;
    }
    subReports.remove(report);
  }

  /**
   * Returns all sub-reports as array.
   *
   * @return the sub-reports as array.
   */
  public SubReport[] getSubReports()
  {
    if (subReports == null)
    {
      return EMPTY_SUBREPORTS;
    }
    return (SubReport[]) subReports.toArray(new SubReport[subReports.size()]);
  }
}
