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
 * PageDefinition.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;

import java.io.Serializable;

/**
 * A logical page definition for a report.
 * <p/>
 * Page oriented reports use the page definition to distribute the content of one logical page to the physical pages.
 * The order of the physical pages in the page definition defines the content order in the generated documents.
 * <p/>
 * Flow oriented reports will use the page-definitions width as default width for the layouting.
 * <p/>
 * If a logical page is completly empty, it will be removed from the report and a pageCanceled event is fired. If a
 * physical page within a non empty logical page is empty, if depends on the ReportProcessor whether the page is printed
 * as empty page or whether it will be removed from the report.
 * <p/>
 * Areas of the logical page not covered by the physical pages will not cause errors, the content in these areas will be
 * ignored and not printed.
 * <p/>
 * Page definitions must not be changed during the report processing, or funny things will happen.
 *
 * @author Thomas Morgner
 */
public interface PageDefinition extends Cloneable, Serializable
{
  /**
   * Returns the total width of the logical page.
   *
   * @return the width of the page.
   */
  public float getWidth();

  /**
   * Returns the total height of the logical page.
   *
   * @return the height of the page.
   */
  public float getHeight();

  /**
   * Returns the number of physical pages in this logical page definition.
   *
   * @return the number of physical pages in the page definition.
   */
  public int getPageCount();

  /**
   * Returns all page positions as array.
   *
   * @return the collected page positions
   * @see PageDefinition#getPagePosition(int)
   */
  public Rectangle2D[] getPagePositions();

  /**
   * Describes the internal position of the given page within the logical page. The logical page does not include any
   * page margins, the printable area for a page starts at (0,0).
   *
   * @param index the index of the physical pageformat
   * @return the bounds for the page.
   */
  public Rectangle2D getPagePosition(int index);

  /**
   * Returns the page format for the given page number. The page format contains local coordinates - that means that the
   * point (0,0) denotes the upper left corner of this returned page format and not global coordinates.
   *
   * @param pos the position of the pageformat within the page
   * @return the given pageformat.
   */
  public PageFormat getPageFormat(int pos);

  /**
   * Creates a copy of the page definition.
   *
   * @return a copy of the page definition.
   * @throws CloneNotSupportedException if cloning failed for some reason.
   */
  public Object clone()
      throws CloneNotSupportedException;
}
