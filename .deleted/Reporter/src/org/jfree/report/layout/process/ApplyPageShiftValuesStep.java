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
 * ApplyPageShiftValuesStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.util.InstanceID;

/**
 * This processing step applies the cache shift to all nodes.
 *
 * Paginated and processed boxes are based on shifted nodes that have been created/shifted by the PaginationStep.
 * The cached values of the nodes that come after these finished nodes are now invalid, as the cache-positions
 * have been stored *before* the pagination shift was applied.
 *
 * Instead of simply invalidating the caches (which would be expensive), we patch the caches of these nodes and
 * shift them downwards so that the cache and the recomputation results will be in sync.
 *
 * Without this correction (or the equivalent cache-invalidation), we would encounter lost content, as the
 * finished node and the cached content would overlap.
 *
 * @author Thomas Morgner
 */
public final class ApplyPageShiftValuesStep extends IterateStructuralProcessStep
{
  private long shift;
  private InstanceID triggerId;
  private boolean found;

  public ApplyPageShiftValuesStep()
  {
  }

  public void compute (final LogicalPageBox logicalPageBox, final long shift, final InstanceID triggerId)
  {
    this.shift = shift;
    this.triggerId = triggerId;
    this.found = false;
    startProcessing(logicalPageBox);
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    if (found == false && box.getInstanceId() == triggerId)
    {
      found = true;
      CacheBoxShifter.extendHeight(box.getParent(),  shift);
    }
    if (found)
    {
      box.shiftCached(shift);
    }
    return true;
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (found == false && box.getInstanceId() == triggerId)
    {
      found = true;
      CacheBoxShifter.extendHeight(box.getParent(),  shift);
    }
    if (found)
    {
      box.shiftCached(shift);
    }
    return true;
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    if (found == false && box.getInstanceId() == triggerId)
    {
      found = true;
      CacheBoxShifter.extendHeight(box.getParent(),  shift);
    }
    if (found)
    {
      box.shiftCached(shift);
    }
    return true;
  }

  protected void processOtherNode(final RenderNode node)
  {
    if (found == false && node.getInstanceId() == triggerId)
    {
      found = true;
      CacheBoxShifter.extendHeight(node.getParent(),  shift);
    }
    if (found)
    {
      node.shiftCached(shift);
    }
    // do something
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    if (found == false && box.getInstanceId() == triggerId)
    {
      found = true;
      CacheBoxShifter.extendHeight(box.getParent(),  shift);
    }
    if (found)
    {
      box.shiftCached(shift);
    }
    return true;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }
}
