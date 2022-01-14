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
 * ExcelColorSupport.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls.helper;

import java.awt.Color;

import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;

import org.jfree.util.Log;

/**
 * POI Excel utility methods.
 *
 * @author Heiko Evermann
 */
public final class ExcelColorSupport
{
  /**
   * DefaultConstructor.
   */
  private ExcelColorSupport()
  {
  }

  /**
   * the pre-defined excel color triplets.
   */
  private static Map triplets;
  private static Map indexes;

  /**
   * Find a suitable color for the cell.
   * <p/>
   * The algorithm searches all available triplets, weighted by tripletvalue and
   * tripletdifference to the other triplets. The color wins, which has the
   * smallest triplet difference and where all triplets are nearest to the
   * requested color. Damn, why couldn't these guys from microsoft implement a
   * real color system.
   *
   * @param awtColor the awt color that should be transformed into an Excel
   *                 color.
   * @return the excel color index that is nearest to the supplied color.
   */
  public static synchronized short getNearestColor(final Color awtColor)
  {
    if (triplets == null)
    {
      triplets = HSSFColor.getTripletHash();
    }

    if (triplets == null || triplets.isEmpty())
    {
      Log.warn("Unable to get triplet hashtable");
      return HSSFColor.BLACK.index;
    }

    short color = HSSFColor.BLACK.index;
    double minDiff = Double.MAX_VALUE;

    // get the color without the alpha chanel
    final float[] hsb = Color.RGBtoHSB
            (awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), null);

    float[] excelHsb = null;
    final Iterator elements = triplets.values().iterator();
    while (elements.hasNext())
    {
      final HSSFColor crtColor = (HSSFColor) elements.next();
      final short[] rgb = crtColor.getTriplet();
      excelHsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], excelHsb);

      final double weight =
          3.0d * Math.abs(excelHsb[0] - hsb[0]) +
                      Math.abs(excelHsb[1] - hsb[1]) +
                      Math.abs(excelHsb[2] - hsb[2]);

      if (weight < minDiff)
      {
        minDiff = weight;
        if (minDiff == 0)
        {
          // we found the color ...
          return crtColor.getIndex();
        }
        color = crtColor.getIndex();
      }
    }
    return color;
  }

  public static synchronized HSSFColor getColor(final short index)
  {
    if (indexes == null)
    {
      indexes = HSSFColor.getIndexHash();
    }

    final Short s = new Short(index);
    final HSSFColor color = (HSSFColor) indexes.get(s);
    if (color == null)
    {
      throw new IllegalStateException("No such color.");
    }
    return color;
  }
}
