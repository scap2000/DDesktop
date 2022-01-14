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
 * TableLayoutProducer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.base;

import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.process.IterateStructuralProcessStep;

/**
 * Creation-Date: 02.05.2007, 18:31:37
 *
 * @author Thomas Morgner
 */
public class TableLayoutProducer extends IterateStructuralProcessStep
{
  private SheetLayout layout;

  private long pageOffset;
  private long pageEnd;
  private boolean headerProcessed;
//  private boolean iterativeUpdate;
  private long contentOffset;
  private long effectiveOffset;
  private boolean unalignedPagebands;

  public TableLayoutProducer(final OutputProcessorMetaData metaData)
  {
    this.unalignedPagebands = metaData.isFeatureSupported(OutputProcessorFeature.UNALIGNED_PAGEBANDS);

    final boolean verboseCellMarker = "true".equals
        (metaData.getConfiguration().getConfigProperty("org.jfree.report.modules.output.table.base.VerboseCellMarkers"));
    this.layout = new SheetLayout
        (metaData.isFeatureSupported(AbstractTableOutputProcessor.STRICT_LAYOUT),
            metaData.isFeatureSupported(AbstractTableOutputProcessor.TREAT_ELLIPSE_AS_RECTANGLE), verboseCellMarker);
  }

  public SheetLayout getLayout()
  {
    return layout;
  }

  public void update(final LogicalPageBox logicalPage,
                     final boolean withPageFooter,
                     final boolean iterativeUpdate)
  {
//    this.iterativeUpdate = iterativeUpdate;

    if (unalignedPagebands == false)
    {
      // The page-header and footer area are aligned/shifted within the logical pagebox so that all areas
      // share a common coordinate system. This also implies, that the whole logical page is aligned content.
      pageOffset = 0;
      pageEnd = logicalPage.getPageEnd() - logicalPage.getPageOffset();
      effectiveOffset = 0;
      //Log.debug ("Content Processing " + pageOffset + " -> " + pageEnd);
      if (startBlockBox(logicalPage))
      {
        if (headerProcessed == false)
        {
          startProcessing(logicalPage.getWatermarkArea());
          final BlockRenderBox headerArea = logicalPage.getHeaderArea();
          startProcessing(headerArea);
          headerProcessed = true;
        }

        processBoxChilds(logicalPage);
        if (iterativeUpdate == false)
        {
          startProcessing(logicalPage.getFooterArea());
        }
      }
      finishBlockBox(logicalPage);
    }
    else
    {
      // The page-header and footer area are not aligned/shifted within the logical pagebox.
      // All areas have their own coordinate system starting at (0,0). We apply a manual shift here
      // so that we dont have to modify the nodes (which invalidates the cache, and therefore is ugly)
      effectiveOffset = 0;
      pageOffset = 0;
      pageEnd = logicalPage.getPageEnd();
      if (startBlockBox(logicalPage))
      {
        if (headerProcessed == false)
        {
          contentOffset = 0;
          effectiveOffset = 0;

          final BlockRenderBox watermarkArea = logicalPage.getWatermarkArea();
          pageEnd = watermarkArea.getHeight();
          startProcessing(watermarkArea);

          final BlockRenderBox headerArea = logicalPage.getHeaderArea();
          pageEnd = headerArea.getHeight();
          startProcessing(headerArea);
          contentOffset = headerArea.getHeight();
          headerProcessed = true;
        }

        pageOffset = logicalPage.getPageOffset();
        pageEnd = logicalPage.getPageEnd();
        effectiveOffset = contentOffset;
        processBoxChilds(logicalPage);

        if (iterativeUpdate == false)
        {
          pageOffset = 0;
          final BlockRenderBox footerArea = logicalPage.getFooterArea();
          final long footerOffset = contentOffset + (logicalPage.getPageEnd() - logicalPage.getPageOffset());
          pageEnd = footerOffset + footerArea.getHeight();
          effectiveOffset = footerOffset;
          startProcessing(footerArea);
        }
      }
      finishBlockBox(logicalPage);
    }
  }


  private boolean startBox (final RenderBox box)
  {
    final long y = effectiveOffset + box.getY() - pageOffset;
    final long height = box.getHeight();

//    Log.debug ("Processing Box " + effectiveOffset + " " + y + " " + height);
//    Log.debug ("Processing Box " + box);

    final long pageHeight = effectiveOffset + (pageEnd - pageOffset);
    if (height > 0)
    {
      if ((y + height) <= effectiveOffset)
      {
        return false;
      }
      if (y >= pageHeight)
      {
        return false;
      }
    }
    else
    {
      // zero height boxes are always a bit tricky ..
      if ((y + height) < effectiveOffset)
      {
        return false;
      }
      if (y > pageHeight)
      {
        return false;
      }
    }

    // This changes the rendering order; the computed background are most likely not 100% correct.
    // todo: Insert the box multiple times and replace the background on the whole area where not already merged.
    // todo: If open, do not produce a bottom-border.
    // OK, for now, we dont have to worry, as non-rootlevel boxes have neither border or backgrounds. But before
    // we push this code into 0.9 we should add some handling here ..
//    if (box.isOpen())
//    {
//      layout.add(box, -pageOffset, true);
//    }

    if (box.isOpen() == false &&
        box.isFinished() == false &&
        box.isCommited())
    {
      layout.add(box, -pageOffset + effectiveOffset);
      box.setFinished(true);
      return true;
    }

    return true;
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    return startBox(box);
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    // we should not have come that far ..
    return false;
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    return startBox(box);
  }

  public boolean startCanvasBox(final CanvasRenderBox box)
  {
    return startBox(box);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // not needed. Keep this method empty so that the paragraph childs are *not* processed.
  }

  public void pageCompleted()
  {
    layout.pageCompleted();
    headerProcessed = false;
  }
}
