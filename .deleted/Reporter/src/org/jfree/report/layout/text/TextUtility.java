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
 * TextUtility.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.text;

import org.jfree.fonts.registry.BaselineInfo;
import org.jfree.fonts.registry.FontMetrics;

/**
 * Creation-Date: 04.04.2007, 14:46:14
 *
 * @author Thomas Morgner
 */
public class TextUtility
{
  private TextUtility()
  {
  }

  private static int translateBaselines(final int baseline)
  {
    switch (baseline)
    {
      case BaselineInfo.HANGING:
        return ExtendedBaselineInfo.HANGING;
      case BaselineInfo.ALPHABETIC:
        return ExtendedBaselineInfo.ALPHABETHIC;
      case BaselineInfo.CENTRAL:
        return ExtendedBaselineInfo.CENTRAL;
      case BaselineInfo.IDEOGRAPHIC:
        return ExtendedBaselineInfo.IDEOGRAPHIC;
      case BaselineInfo.MATHEMATICAL:
        return ExtendedBaselineInfo.MATHEMATICAL;
      case BaselineInfo.MIDDLE:
        return ExtendedBaselineInfo.MIDDLE;
      default:
        throw new IllegalArgumentException("Invalid baseline");
    }
  }

  public static ExtendedBaselineInfo createBaselineInfo(final int codepoint,
                                                        final FontMetrics fontMetrics,
                                                        final BaselineInfo reusableBaselineInfo)
  {
    if (fontMetrics == null)
    {
      throw new NullPointerException("FontMetrics cannot be null");
    }

    final BaselineInfo baselineInfo = fontMetrics.getBaselines(codepoint, reusableBaselineInfo);
    final int dominantBaseline = TextUtility.translateBaselines(baselineInfo.getDominantBaseline());

    final long[] baselines = new long[ExtendedBaselineInfo.BASELINE_COUNT];
    baselines[ExtendedBaselineInfo.ALPHABETHIC] = baselineInfo.getBaseline(BaselineInfo.ALPHABETIC);
    baselines[ExtendedBaselineInfo.CENTRAL] = baselineInfo.getBaseline(BaselineInfo.CENTRAL);
    baselines[ExtendedBaselineInfo.HANGING] = baselineInfo.getBaseline(BaselineInfo.HANGING);
    baselines[ExtendedBaselineInfo.IDEOGRAPHIC] = baselineInfo.getBaseline(BaselineInfo.IDEOGRAPHIC);
    baselines[ExtendedBaselineInfo.MATHEMATICAL] = baselineInfo.getBaseline(BaselineInfo.MATHEMATICAL);
    baselines[ExtendedBaselineInfo.MIDDLE] = baselineInfo.getBaseline(BaselineInfo.MIDDLE);
    baselines[ExtendedBaselineInfo.BEFORE_EDGE] = 0;
    baselines[ExtendedBaselineInfo.TEXT_BEFORE_EDGE] = 0;
    baselines[ExtendedBaselineInfo.TEXT_AFTER_EDGE] = fontMetrics.getMaxHeight();
    baselines[ExtendedBaselineInfo.AFTER_EDGE] = baselines[ExtendedBaselineInfo.TEXT_AFTER_EDGE];

    final long underlinePosition = fontMetrics.getUnderlinePosition();
    final long strikeThroughPosition = fontMetrics.getStrikeThroughPosition();
    return new DefaultExtendedBaselineInfo(dominantBaseline, baselines, underlinePosition, strikeThroughPosition);
  }

}
