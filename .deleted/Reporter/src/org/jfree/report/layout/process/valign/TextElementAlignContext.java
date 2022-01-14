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
 * TextAlignmentContext.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.valign;

import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.layout.text.ExtendedBaselineInfo;

/**
 * Creation-Date: 13.10.2006, 22:26:50
 *
 * @author Thomas Morgner
 */
public class TextElementAlignContext extends AlignContext
{
  private long[] baselines;
  private long baselineShift;

  public TextElementAlignContext(final RenderableText text)
  {
    super(text);
    final ExtendedBaselineInfo baselineInfo = text.getBaselineInfo();
    this.baselines = baselineInfo.getBaselines();
    setDominantBaseline(baselineInfo.getDominantBaseline());
  }

  public long getBaselineDistance(final int baseline)
  {
    return (baselines[baseline] - baselines[getDominantBaseline()]) + baselineShift;
  }

  public void shift(final long delta)
  {
    baselineShift += delta;
  }

  public long getAfterEdge()
  {
    return this.baselines[ExtendedBaselineInfo.AFTER_EDGE] + baselineShift;
  }

  public long getBeforeEdge()
  {
    return this.baselines[ExtendedBaselineInfo.BEFORE_EDGE] + baselineShift;
  }
}
