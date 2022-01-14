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
 * PageFooter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import org.jfree.report.style.BandStyleKeys;

/**
 * A report band that appears at the bottom of every page. The page-footer is the last band that is printed on a page.
 * There is an option to suppress the page footer on the first page, and another option does the same for the last page.
 * If the footer is marked sticky, the footer will even be printed for all sub-report pages.
 * <p/>
 * A page header or footer cannot have subreports.
 *
 * @author David Gilbert
 */
public class PageFooter extends Band implements RootLevelBand
{
  /**
   * A empty array defined here for performance reasons.
   */
  private static final SubReport[] EMPTY_REPORTS = new SubReport[0];

  /**
   * Constructs a page footer containing no elements.
   */
  public PageFooter()
  {
  }

  /**
   * Constructs a page footer containing no elements.
   *
   * @param onFirstPage defines, whether the page header will be printed on the first page
   * @param onLastPage  defines, whether the page footer will be printed on the last page.
   */
  public PageFooter(final boolean onFirstPage, final boolean onLastPage)
  {
    super();
    setDisplayOnFirstPage(onFirstPage);
    setDisplayOnLastPage(onLastPage);
  }

  /**
   * Returns true if the footer should be shown on page 1, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isDisplayOnFirstPage()
  {
    return getStyle().getBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_FIRSTPAGE, false);
  }

  /**
   * Defines whether the footer should be shown on the first page.
   *
   * @param b a flag indicating whether or not the footer is shown on the first page.
   */
  public void setDisplayOnFirstPage(final boolean b)
  {
    getStyle().setBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_FIRSTPAGE, b);
  }

  /**
   * Returns true if the footer should be shown on the last page, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isDisplayOnLastPage()
  {
    return getStyle().getBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_LASTPAGE, false);
  }

  /**
   * Defines whether the footer should be shown on the last page.
   *
   * @param b a flag indicating whether or not the footer is shown on the first page.
   */
  public void setDisplayOnLastPage(final boolean b)
  {
    getStyle().setBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_LASTPAGE, b);
  }


  /**
   * Assigns the report definition. Don't play with that function, unless you know what you are doing. You might get
   * burned.
   *
   * @param reportDefinition the report definition.
   */
  public void setReportDefinition(final ReportDefinition reportDefinition)
  {
    super.setReportDefinition(reportDefinition);
  }

  /**
   * Returns the number of subreports on this band. This returns zero, as page-bands cannot have subreports.
   *
   * @return the subreport count.
   */
  public final int getSubReportCount()
  {
    return 0;
  }

  /**
   * Throws an IndexOutOfBoundsException as page-footer cannot have sub-reports.
   *
   * @param index the index.
   * @return nothing, as an exception is thrown instead.
   */
  public final SubReport getSubReport(final int index)
  {
    throw new IndexOutOfBoundsException("PageFooter cannot have subreports.");
  }

  /**
   * Returns true if the footer should be shown on all subreports.
   *
   * @return true or false.
   */
  public boolean isSticky()
  {
    return getStyle().getBooleanStyleProperty(BandStyleKeys.STICKY, false);
  }

  /**
   * Defines whether the footer should be shown on all subreports.
   *
   * @param b a flag indicating whether or not the footer is shown on the first page.
   */
  public void setSticky(final boolean b)
  {
    getStyle().setBooleanStyleProperty(BandStyleKeys.STICKY, b);
  }

  /**
   * Returns an empty array, as page-footer cannot have subreports.
   *
   * @return the sub-reports as array.
   */
  public SubReport[] getSubReports()
  {
    return EMPTY_REPORTS;
  }
}
