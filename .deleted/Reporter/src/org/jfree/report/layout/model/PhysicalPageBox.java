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
 * PhysicalPageBox.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import java.awt.print.PageFormat;

import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Defines the properties of a single physical page. In a later version, this
 * box may receive physical page header and footer or may even support the full
 * CSS-pagebox modell.
 */
public class PhysicalPageBox implements Cloneable
{
  private long width;
  private long height;
  private long imageableX;
  private long imageableY;
  private long imageableWidth;
  private long imageableHeight;
  private long globalX;
  private long globalY;
  private int orientation;

  public PhysicalPageBox(final PageFormat pageFormat,
                         final long globalX,
                         final long globalY)
  {
    this.width = StrictGeomUtility.toInternalValue(pageFormat.getWidth());
    this.height = StrictGeomUtility.toInternalValue(pageFormat.getHeight());
    this.imageableX = StrictGeomUtility.toInternalValue(pageFormat.getImageableX());
    this.imageableY = StrictGeomUtility.toInternalValue(pageFormat.getImageableY());
    this.imageableWidth = StrictGeomUtility.toInternalValue(pageFormat.getImageableWidth());
    this.imageableHeight = StrictGeomUtility.toInternalValue(pageFormat.getImageableHeight());
    this.globalX = globalX;
    this.globalY = globalY;
    this.orientation = pageFormat.getOrientation();
  }

//
//  public PhysicalPageBox(final long width,
//                         final long height,
//                         final long imageableX,
//                         final long imageableY,
//                         final long imageableWidth,
//                         final long imageableHeight,
//                         final long globalX,
//                         final long globalY)
//  {
//    this.width = width;
//    this.height = height;
//    this.imageableX = imageableX;
//    this.imageableY = imageableY;
//    this.imageableWidth = imageableWidth;
//    this.imageableHeight = imageableHeight;
//    this.globalX = globalX;
//    this.globalY = globalY;
//  }


  public int getOrientation()
  {
    return orientation;
  }

  public long getImageableX()
  {
    return imageableX;
  }

  public long getImageableY()
  {
    return imageableY;
  }

  public long getImageableWidth()
  {
    return imageableWidth;
  }

  public long getImageableHeight()
  {
    return imageableHeight;
  }

  public long getGlobalX()
  {
    return globalX;
  }

  public void setGlobalX(final long globalX)
  {
    this.globalX = globalX;
  }

  public long getGlobalY()
  {
    return globalY;
  }

  public void setGlobalY(final long globalY)
  {
    this.globalY = globalY;
  }

  public long getWidth()
  {
    return width;
  }

  public long getHeight()
  {
    return height;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
