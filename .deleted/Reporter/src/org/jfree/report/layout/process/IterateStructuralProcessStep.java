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
 * IterateStructuralProcessStep.java
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

/**
 * Iterates over the document tree using the display-role of the current node
 * as selector. Usually all structural processing steps use this iteration
 * strategy.
 *
 * @author Thomas Morgner
 */
public abstract class IterateStructuralProcessStep
{
  protected IterateStructuralProcessStep()
  {
  }

  protected final void startProcessing (final RenderNode node)
  {
    if (node instanceof CanvasRenderBox)
    {
      final CanvasRenderBox box = (CanvasRenderBox) node;
      if (startCanvasBox(box))
      {
        processBoxChilds(box);
      }
      finishCanvasBox(box);
    }
    else if (node instanceof InlineRenderBox)
    {
      final InlineRenderBox box = (InlineRenderBox) node;
      if (startInlineBox(box))
      {
        processBoxChilds(box);
      }
      finishInlineBox(box);
    }
    else if (node instanceof ParagraphRenderBox)
    {
      final ParagraphRenderBox box = (ParagraphRenderBox) node;
      if (startBlockBox(box))
      {
        processParagraphChilds(box);
      }
      finishBlockBox(box);
    }
    else if (node instanceof LogicalPageBox)
    {
      final LogicalPageBox box = (LogicalPageBox) node;
      if (startBlockBox(box))
      {
        startProcessing(box.getWatermarkArea());
        startProcessing(box.getHeaderArea());
        processBoxChilds(box);
        startProcessing(box.getFooterArea());
      }
      finishBlockBox(box);
    }
    else if (node instanceof BlockRenderBox)
    {
      final BlockRenderBox box = (BlockRenderBox) node;
      if (startBlockBox(box))
      {
        processBoxChilds(box);
      }
      finishBlockBox(box);
    }
    else if (node instanceof RenderBox)
    {
      final RenderBox box = (RenderBox) node;
      if (startOtherBox(box))
      {
        processBoxChilds(box);
      }
      finishOtherBox(box);
    }
    else
    {
      processOtherNode(node);
    }
  }

  protected void finishCanvasBox(final CanvasRenderBox box)
  {

  }

  protected boolean startCanvasBox(final CanvasRenderBox box)
  {
    return true;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box.getPool());
  }

  protected final void processBoxChilds(final RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      startProcessing(node);
      node = node.getNext();
    }
  }

  protected void processOtherNode (final RenderNode node)
  {
  }

  protected boolean startBlockBox (final BlockRenderBox box)
  {
    return true;
  }

  protected void finishBlockBox (final BlockRenderBox box)
  {
  }

  protected boolean startInlineBox (final InlineRenderBox box)
  {
    return true;
  }

  protected void finishInlineBox (final InlineRenderBox box)
  {
  }

  protected boolean startOtherBox (final RenderBox box)
  {
    return true;
  }

  protected void finishOtherBox (final RenderBox box)
  {
  }
}
