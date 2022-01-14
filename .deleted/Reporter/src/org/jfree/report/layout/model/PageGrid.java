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
 * PageGrid.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

/**
 * The page grid describes the logical page. That grid consists of PageGridAreas,
 * which correspond to the usable content-PageArea. PageGridAreas are
 * synchronized against each other - the smallest width or height defines the
 * available destColumn space.
 *
 * Modifications to PageAreas are only valid, if they are not locked.
 *
 * @author Thomas Morgner
 */
public interface PageGrid extends Cloneable
{
  public PhysicalPageBox getPage(int row, int col);
  public int getRowCount ();
  public int getColumnCount ();
  public long[] getHorizontalBreaks();

  public Object clone() throws CloneNotSupportedException;
}
