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
 * ApplyCachedValuesStep.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process;

import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.util.Log;

/**
 * Creation-Date: 19.04.2007, 20:47:38
 *
 * @author Thomas Morgner
 */
public final class ApplyCachedValuesStep extends IterateStructuralProcessStep
{
  private RenderBox uncleanBox;

  public ApplyCachedValuesStep()
  {
  }

  public void compute(final RenderBox box)
  {
    try
    {
      startProcessing(box);
    }
    finally
    {
      uncleanBox = null;
    }
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    if (uncleanBox == null)
    {
      final int state = box.getCacheState();
      if (state == RenderNode.CACHE_CLEAN)
      {
        return false;
      }
      if (state == RenderNode.CACHE_DEEP_DIRTY)
      {
        uncleanBox = box;
      }
    }

    box.apply();
    box.setStaticBoxPropertiesAge(box.getChangeTracker());
    return true;
  }

  protected void processOtherNode(final RenderNode node)
  {
    node.apply();
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (uncleanBox == null)
    {
      final int state = box.getCacheState();
      if (state == RenderNode.CACHE_CLEAN)
      {
        if (box.getStaticBoxPropertiesAge() == box.getChangeTracker())
        {
          return false;
        }
        else
        {
          Log.debug ("State clean but changetracker not clean: " + box);
        }
      }
      if (state == RenderNode.CACHE_DEEP_DIRTY)
      {
        uncleanBox = box;
      }
    }

    box.apply();
    box.setStaticBoxPropertiesAge(box.getChangeTracker());
    return true;
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    if (uncleanBox == null)
    {
      final int state = box.getCacheState();
      if (state == RenderNode.CACHE_CLEAN)
      {
        return false;
      }
      if (state == RenderNode.CACHE_DEEP_DIRTY)
      {
        uncleanBox = box;
      }
    }

    box.apply();
    box.setStaticBoxPropertiesAge(box.getChangeTracker());
    return true;
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    if (uncleanBox == null)
    {
      final int state = box.getCacheState();
      if (state == RenderNode.CACHE_CLEAN)
      {
        return false;
      }
      if (state == RenderNode.CACHE_DEEP_DIRTY)
      {
        uncleanBox = box;
      }
    }

    box.apply();
    box.setStaticBoxPropertiesAge(box.getChangeTracker());
    return true;
  }


  public void finishCanvasBox(final CanvasRenderBox box)
  {
    if (box == uncleanBox)
    {
      uncleanBox = null;
    }
  }

  protected void finishBlockBox(final BlockRenderBox box)
  {
    if (box == uncleanBox)
    {
      uncleanBox = null;
    }
  }

  protected void finishInlineBox(final InlineRenderBox box)
  {
    if (box == uncleanBox)
    {
      uncleanBox = null;
    }
  }

  protected void finishOtherBox(final RenderBox box)
  {
    if (box == uncleanBox)
    {
      uncleanBox = null;
    }
  }
}
