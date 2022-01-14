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
 * ApplyAutoCommitPageHeaderStep.java
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
 * Marks the Page-Header as commited and as seen. Page-headers are generated on the fly and are not part of the
 * common report-processing system. Therefore they must be printed as soon as there is some commitable content.
 *
 * @author Thomas Morgner
 */
public class ApplyAutoCommitPageHeaderStep extends IterateStructuralProcessStep
{
  private ApplyAutoCommitStep autoCommitStep;
  private boolean hasCommitableContent;

  public ApplyAutoCommitPageHeaderStep()
  {
    autoCommitStep = new ApplyAutoCommitStep();
  }

  public boolean compute (final LogicalPageBox pageBox)
  {
    hasCommitableContent = false;
    processBoxChilds(pageBox);
    if (hasCommitableContent)
    {
      autoCommitStep.compute(pageBox.getWatermarkArea());
      autoCommitStep.compute(pageBox.getHeaderArea());
      return true;
    }
    else
    {
      return false;
    }
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }

  private boolean processBox(final RenderBox box)
  {
    if (hasCommitableContent == true || box.isCommited())
    {
      hasCommitableContent = true;
      return false;
    }

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
