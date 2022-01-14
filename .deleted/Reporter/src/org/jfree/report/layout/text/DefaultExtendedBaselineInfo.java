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
 * DefaultExtendedBaselineInfo.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.text;

/**
 * Creation-Date: 24.07.2006, 17:35:25
 *
 * @author Thomas Morgner
 */
public final class DefaultExtendedBaselineInfo implements ExtendedBaselineInfo
{
  private long[] baselines;
  private int dominantBaseline;
  private long underlinePosition;
  private long strikethroughPosition;

  public DefaultExtendedBaselineInfo(final int dominantBaseline,
                                     final long[] baselines,
                                     final long underlinePosition,
                                     final long strikethroughPosition)
  {
    this.strikethroughPosition = strikethroughPosition;
    this.underlinePosition = underlinePosition;
    this.baselines = baselines;
    this.dominantBaseline = dominantBaseline;
  }

  public long getUnderlinePosition()
  {
    return underlinePosition;
  }

  public long getStrikethroughPosition()
  {
    return strikethroughPosition;
  }

  public int getDominantBaseline()
  {
    return dominantBaseline;
  }

  public long[] getBaselines()
  {
    return baselines;
  }

//  public void setBaselines(final long[] baselines)
//  {
//    if (baselines.length != ExtendedBaselineInfo.BASELINE_COUNT)
//    {
//      throw new IllegalArgumentException();
//    }
//    System.arraycopy(baselines, 0, this.baselines, 0, ExtendedBaselineInfo.BASELINE_COUNT);
//  }

  public long getBaseline(final int baseline)
  {
    return baselines[baseline];
  }

  public String toString()
  {
    final StringBuffer b = new StringBuffer();
    b.append("DefaultExtendedBaselineInfo{");
    for (int i = 0; i < baselines.length; i++)
    {
      if (i > 0)
      {
        b.append(", ");
      }
      b.append("baselines[");
      b.append(String.valueOf(i));
      b.append("]=");
      b.append(baselines[i]);

    }
    b.append(", dominantBaseline=");
    b.append(dominantBaseline);
    b.append('}');
    return b.toString();
  }
}
