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
 * LegacyFontMetrics.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.text;

import org.jfree.fonts.registry.BaselineInfo;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.tools.StrictGeomUtility;

/**
 * Creation-Date: 15.04.2007, 14:40:35
 *
 * @author Thomas Morgner
 */
public class LegacyFontMetrics implements FontMetrics
{
  private FontMetrics parent;
  private long fontHeight;

  public LegacyFontMetrics(final FontMetrics parent, final double fontHeight)
  {
    this.parent = parent;
    this.fontHeight = StrictGeomUtility.toInternalValue(fontHeight);
  }

  public long getAscent()
  {
    return parent.getAscent();
  }

  public long getDescent()
  {
    return parent.getDescent();
  }

  public long getLeading()
  {
    return parent.getLeading();
  }

  public long getXHeight()
  {
    return parent.getXHeight();
  }

  public long getOverlinePosition()
  {
    return parent.getOverlinePosition();
  }

  public long getUnderlinePosition()
  {
    return parent.getUnderlinePosition();
  }

  public long getStrikeThroughPosition()
  {
    return parent.getStrikeThroughPosition();
  }

  public long getMaxAscent()
  {
    return parent.getMaxAscent();
  }

  public long getMaxDescent()
  {
    return parent.getMaxDescent();
  }

  public long getMaxHeight()
  {
    return fontHeight;
  }

  public long getMaxCharAdvance()
  {
    return parent.getMaxCharAdvance();
  }

  public long getCharWidth(final int codePoint)
  {
    return parent.getCharWidth(codePoint);
  }

  public long getKerning(final int previous, final int codePoint)
  {
    return parent.getKerning(previous, codePoint);
  }

  public BaselineInfo getBaselines(final int codePoint, final BaselineInfo info)
  {
    return parent.getBaselines(codePoint, info);
  }

  public long getItalicAngle()
  {
    return parent.getItalicAngle();
  }
}
