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
 * ReplacedContentAlignContext.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.valign;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.text.ExtendedBaselineInfo;
import org.jfree.util.Log;

/**
 * A generic align context for images and other nodes. (Renderable-Content
 * should have been aligned by the parent.
 *
 * @author Thomas Morgner
 */
public class ReplacedContentAlignContext extends AlignContext
{
  private long shift;
  private long height;

  public ReplacedContentAlignContext(final RenderableReplacedContent node)
  {
    super(node);
    // todo
//    final StrictDimension contentSize = node.getContentSize();
//    // we better compute the height of the content or we will run into
//    // trouble ..
//
    this.height = node.computeHeight(computeBlockContextWidth(node), node.getComputedWidth());

    Log.debug ("HEIGHT" + height);
  }

  private long computeBlockContextWidth(final RenderNode box)
  {
    final RenderBox parentBlockContext = box.getParent();
    if (parentBlockContext == null)
    {
      final LogicalPageBox logicalPage = box.getLogicalPage();
      if (logicalPage == null)
      {
        return 0;
      }
      return logicalPage.getPageWidth();
    }
    return parentBlockContext.getStaticBoxLayoutProperties().getBlockContextWidth();
  }

  public long getBaselineDistance(final int baseline)
  {
    if (baseline == ExtendedBaselineInfo.BEFORE_EDGE)
    {
      return 0;
    }
    if (baseline == ExtendedBaselineInfo.TEXT_BEFORE_EDGE)
    {
      return 0;
    }
    // oh that's soooo primitive ..
    return height;
  }

  public void shift(final long delta)
  {
    this.shift += delta;
  }

  public long getAfterEdge()
  {
    return shift + height;
  }

  public long getBeforeEdge()
  {
    return shift;
  }
}
