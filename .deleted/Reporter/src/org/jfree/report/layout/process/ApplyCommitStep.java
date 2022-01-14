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
 * ApplyCommitStep.java
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

/**
 * Applies the createRollbackInformation-marker to all closed boxes and applies the pending marker to all currently open boxes.
 * During a roll-back, we can use these markers to identify boxes that have been added since the last createRollbackInformation
 * to remove them from the model.
 *
 * @author Thomas Morgner
 */
public class ApplyCommitStep extends IterateStructuralProcessStep
{
  public ApplyCommitStep()
  {
  }

  public void compute (final LogicalPageBox pageBox)
  {
    startProcessing(pageBox);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }

  private boolean processBox(final RenderBox box)
  {
    if (box.isCommited())
    {
      return false;
    }
    
    box.commitApplyMark();
    return true;
  }

  protected boolean startCanvasBox(final CanvasRenderBox box)
  {
    return processBox(box);
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    return processBox(box);
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    return processBox(box);
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    return processBox(box);
  }
}
