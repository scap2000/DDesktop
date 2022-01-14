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
 * PhysicalPageDrawable.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.graphics.internal;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import org.jfree.report.layout.model.PhysicalPageBox;
import org.jfree.report.modules.output.pageable.graphics.PageDrawable;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.Log;

/**
 * Creation-Date: 17.11.2006, 18:00:46
 *
 * @author Thomas Morgner
 */
public class PhysicalPageDrawable implements PageDrawable
{
  private LogicalPageDrawable pageDrawable;
  private PageFormat pageFormat;
  private long globalX;
  private long globalY;

  /** @noinspection SuspiciousNameCombination*/
  public PhysicalPageDrawable(final LogicalPageDrawable pageDrawable,
                              final PhysicalPageBox page)
  {
    this.pageDrawable = pageDrawable;

    this.globalX = page.getGlobalX();
    this.globalY = page.getGlobalY();

    final Paper p = new Paper();

    final float marginLeft = (float) StrictGeomUtility.toExternalValue(page.getImageableX());
    final float marginRight = (float) StrictGeomUtility.toExternalValue
                (page.getWidth() - page.getImageableWidth() - page.getImageableX());
    final float marginTop = (float) StrictGeomUtility.toExternalValue (page.getImageableY());
    final float marginBottom = (float) StrictGeomUtility.toExternalValue
                (page.getHeight() - page.getImageableHeight() - page.getImageableY());
    switch (page.getOrientation())
    {
      case PageFormat.PORTRAIT:
        p.setSize(StrictGeomUtility.toExternalValue(page.getWidth()),
            StrictGeomUtility.toExternalValue(page.getHeight()));
        PageFormatFactory.getInstance().setBorders(p, marginTop, marginLeft,
                marginBottom, marginRight);
        break;
      case PageFormat.LANDSCAPE:
        // right, top, left, bottom
        p.setSize(StrictGeomUtility.toExternalValue(page.getHeight()),
            StrictGeomUtility.toExternalValue(page.getWidth()));
        PageFormatFactory.getInstance().setBorders(p, marginRight, marginTop,
                marginLeft, marginBottom);
        break;
      case PageFormat.REVERSE_LANDSCAPE:
        p.setSize(StrictGeomUtility.toExternalValue(page.getHeight()),
            StrictGeomUtility.toExternalValue(page.getWidth()));
        PageFormatFactory.getInstance().setBorders(p, marginLeft, marginBottom,
                marginRight, marginTop);
        break;
      default:
        // will not happen..
        Log.debug("Unexpected paper orientation.");
    }

    this.pageFormat = new PageFormat();
    this.pageFormat.setPaper(p);
    this.pageFormat.setOrientation(page.getOrientation());
  }

  public PageFormat getPageFormat()
  {
    return pageFormat;
  }

  public Dimension getPreferredSize()
  {
    return new Dimension ((int) pageFormat.getWidth(), (int) pageFormat.getHeight());
  }

  public boolean isPreserveAspectRatio()
  {
    return true;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(final Graphics2D g2, final Rectangle2D area)
  {
    g2.clip(new Rectangle2D.Double(0, 0, pageFormat.getWidth(), pageFormat.getHeight()));
    g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

    pageDrawable.draw(g2, new Rectangle2D.Double
        (StrictGeomUtility.toExternalValue(globalX), StrictGeomUtility.toExternalValue(globalY), 
            pageFormat.getImageableWidth(), pageFormat.getImageableHeight()));
  }
}
