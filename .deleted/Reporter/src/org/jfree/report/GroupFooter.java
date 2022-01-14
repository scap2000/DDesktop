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
 * GroupFooter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import org.jfree.report.style.BandStyleKeys;

/**
 * A band that appears at the end of each instance of a group. A group-footer can be marked as repeating footer causing
 * the footer to be printed at the bottom of each page as long as the group is active. If the footer is marked sticky,
 * the footer will even be printed for all sub-report pages.
 * <p/>
 * Subreports defined for a repeating group footer will be ignored for all repeating instances.
 *
 * @author David Gilbert
 */
public class GroupFooter extends AbstractRootLevelBand
{
  /**
   * Constructs a group footer band, containing no elements.
   */
  public GroupFooter()
  {
  }

  /**
   * Checks, whether this group header should be repeated on new pages.
   *
   * @return true, if the header will be repeated, false otherwise
   */
  public boolean isRepeat()
  {
    return getStyle().getBooleanStyleProperty(BandStyleKeys.REPEAT_HEADER);
  }

  /**
   * Defines, whether this group header should be repeated on new pages.
   *
   * @param repeat true, if the header will be repeated, false otherwise
   */
  public void setRepeat(final boolean repeat)
  {
    getStyle().setBooleanStyleProperty(BandStyleKeys.REPEAT_HEADER, repeat);
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

}
