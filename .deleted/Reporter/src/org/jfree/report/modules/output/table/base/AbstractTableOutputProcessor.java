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
 * AbstractTableOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.base;

import java.util.ArrayList;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.output.AbstractOutputProcessor;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.FlowSelector;
import org.jfree.report.layout.output.IterativeOutputProcessor;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorFeature;

/**
 * The Table-Output processor uses the pagination stage to build a list of destTable-layouts.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractTableOutputProcessor extends AbstractOutputProcessor
    implements IterativeOutputProcessor
{
  public static final OutputProcessorFeature.BooleanOutputProcessorFeature STRICT_LAYOUT =
      new OutputProcessorFeature.BooleanOutputProcessorFeature("strict-layout");
  public static final OutputProcessorFeature.BooleanOutputProcessorFeature TREAT_ELLIPSE_AS_RECTANGLE =
      new OutputProcessorFeature.BooleanOutputProcessorFeature("treat-ellipse-as-rectangle");
  public static final OutputProcessorFeature.BooleanOutputProcessorFeature SHAPES_CONTENT =
      new OutputProcessorFeature.BooleanOutputProcessorFeature("shape-content");

  private ArrayList sheetLayouts;
  private TableLayoutProducer currentLayout;
  private TableContentProducer currentContent;

  protected AbstractTableOutputProcessor()
  {
    this.sheetLayouts = new ArrayList();
  }

  public boolean isNeedAlignedPage()
  {
    return getMetaData().isFeatureSupported(OutputProcessorFeature.ITERATIVE_RENDERING) == false;
  }

  protected final void processPaginationContent(final LogicalPageKey logicalPageKey, final LogicalPageBox logicalPage)
  {
    if (currentLayout == null)
    {
      currentLayout = new TableLayoutProducer(getMetaData());
    }
    currentLayout.update(logicalPage, true, false);
    currentLayout.pageCompleted();

    //ModelPrinter.print(logicalPage);

    sheetLayouts.add(currentLayout);
    currentLayout = null;
  }

  protected final void processPageContent(final LogicalPageKey logicalPageKey, final LogicalPageBox logicalPage)
      throws ContentProcessingException
  {
    // this one is only called after the pagination is complete. In that case we have a valid table.
    final FlowSelector tableInterceptor = getFlowSelector();
    if (tableInterceptor == null)
    {
      return;
    }

    if (tableInterceptor.isLogicalPageAccepted(logicalPageKey) == false)
    {
      return;
    }

    if (currentContent == null)
    {
      final int pageCursor = getPageCursor();
      final TableLayoutProducer sheetLayout = (TableLayoutProducer) sheetLayouts.get(pageCursor);
      currentContent = new TableContentProducer(sheetLayout.getLayout(), getMetaData());
    }

    currentContent.compute(logicalPage, false, true);
//    ModelPrinter.print(logicalPage);
    processTableContent(logicalPageKey, logicalPage, currentContent);
    currentContent.clearFinishedBoxes();
    currentContent = null;
  }

  protected abstract void processTableContent(final LogicalPageKey logicalPageKey,
                                              final LogicalPageBox logicalPage,
                                              final TableContentProducer contentProducer)
      throws ContentProcessingException;


  public final void processIterativeContent(final LogicalPageBox logicalPageBox,
                                      final boolean performOutput) throws ContentProcessingException
  {
    if (isContentGeneratable() == false)
    {
      // In pagination mode.
      if (currentLayout == null)
      {
        currentLayout = new TableLayoutProducer(getMetaData());
      }
      currentLayout.update(logicalPageBox, true, true);
    }
    else
    {
      // In content generation mode.
      final int pageCursor = getPageCursor();
      final LogicalPageKey logicalPageKey = getLogicalPage(pageCursor);
      final FlowSelector tableInterceptor = getFlowSelector();
      if (tableInterceptor == null)
      {
        return;
      }

      if (tableInterceptor.isLogicalPageAccepted(logicalPageKey) == false)
      {
        return;
      }

      if (currentContent == null)
      {
        final TableLayoutProducer sheetLayout = (TableLayoutProducer) sheetLayouts.get(pageCursor);
        currentContent = new TableContentProducer(sheetLayout.getLayout(), getMetaData());
      }

//      Log.debug ("Incremental output: " + logicalPageBox.getPageHeight() + " " + logicalPageBox.getPageOffset());
      currentContent.compute(logicalPageBox, true, true);
      updateTableContent(logicalPageKey, logicalPageBox, currentContent, performOutput);
      currentContent.clearFinishedBoxes();
    }
  }

  protected void updateTableContent(final LogicalPageKey logicalPageKey,
                                    final LogicalPageBox logicalPageBox,
                                    final TableContentProducer tableContentProducer,
                                    final boolean performOutput) throws ContentProcessingException
  {
    throw new UnsupportedOperationException
        ("This output processor does not implement the iterative content processing protocol.");
  }

  protected abstract FlowSelector getFlowSelector();
}
