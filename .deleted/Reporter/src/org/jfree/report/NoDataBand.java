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
 * NoDataBand.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report;

/**
 * The No-Data-Band is printed if the current report has no data in its main data-destTable. It replaces the itemband for
 * such reports.
 *
 * @author Thomas Morgner
 */
public class NoDataBand extends AbstractRootLevelBand
{
  /**
   * Constructs a new band.
   */
  public NoDataBand()
  {
  }

  /**
   * Constructs a new band with the given pagebreak attributes. Pagebreak attributes have no effect on subbands.
   *
   * @param pagebreakAfter  defines, whether a pagebreak should be done after that band was printed.
   * @param pagebreakBefore defines, whether a pagebreak should be done before that band gets printed.
   */
  public NoDataBand(final boolean pagebreakBefore, final boolean pagebreakAfter)
  {
    super(pagebreakBefore, pagebreakAfter);
  }
}
