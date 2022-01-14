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
 * StreamingRenderer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.IterativeOutputProcessor;
import org.jfree.report.layout.output.LayoutPagebreakHandler;
import org.jfree.report.layout.output.OutputProcessor;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.process.ApplyAutoCommitPageHeaderStep;
import org.jfree.report.layout.process.CleanFlowBoxesStep;

/**
 * The streaming renderer streams all generated (and layouted) elements to the output processor. The output processor
 * should mark the processed elements by setting the 'dirty' flag to false. Pagebreaks will be ignored, all content ends
 * up in a single stream of data.
 *
 * @author Thomas Morgner
 */
public class StreamingRenderer extends AbstractRenderer
{
  private CleanFlowBoxesStep cleanBoxesStep;
  private ApplyAutoCommitPageHeaderStep applyAutoCommitPageHeaderStep;

  public StreamingRenderer(final OutputProcessor outputProcessor)
  {
    super(outputProcessor);
    this.cleanBoxesStep = new CleanFlowBoxesStep();
    this.applyAutoCommitPageHeaderStep = new ApplyAutoCommitPageHeaderStep();
  }

  protected boolean isPageFinished()
  {
    if (getPageBox().isOpen())
    {
      return false;
    }
    return true;
  }


  public void processIncrementalUpdate(final boolean performOutput) throws ContentProcessingException
  {

    if (isDirty() == false)
    {
//      Log.debug ("Not dirty, no update needed.");
      return;
    }
    clearDirty();

    final OutputProcessor outputProcessor = getOutputProcessor();
    if (outputProcessor instanceof IterativeOutputProcessor == false ||
        outputProcessor.getMetaData().isFeatureSupported(OutputProcessorFeature.ITERATIVE_RENDERING) == false)
    {
//      Log.debug ("No incremental system.");
      return;
    }

//    Log.debug ("Computing Incremental update.");

    final LogicalPageBox pageBox = getPageBox();
    pageBox.setPageOffset(0);
    pageBox.setPageEnd(pageBox.getHeight());
    // shiftBox(pageBox, true);

    if (pageBox.isOpen())
    {
      final IterativeOutputProcessor io = (IterativeOutputProcessor) outputProcessor;
      if (applyAutoCommitPageHeaderStep.compute(pageBox))
      {
        io.processIterativeContent(pageBox, performOutput);
        cleanBoxesStep.compute(pageBox);
      }
    }
  }

  protected boolean performPagination(final LayoutPagebreakHandler handler,
                                      final boolean performOutput) throws ContentProcessingException
  {
    if (performOutput == false)
    {
      return false;
    }

    final OutputProcessor outputProcessor = getOutputProcessor();
    final LogicalPageBox pageBox = getPageBox();

    // This is fixed: The streaming renderers always use the whole page area ..
    pageBox.setPageOffset(0);
    pageBox.setPageEnd(pageBox.getHeight());

    if (pageBox.isOpen())
    {
      // Not finished and the output target is non-iterative, so we have to wait until everything is done..
      return false;
    }

    // the reporting finally came to an end. Lets process the content.
    // shiftBox(pageBox, false);
    outputProcessor.processContent(pageBox);
    cleanBoxesStep.compute(pageBox);
    debugPrint(pageBox);
    outputProcessor.processingFinished();

    setPagebreaks(1);
    return false;
  }


  protected void debugPrint(final LogicalPageBox pageBox)
  {
//    Log.debug("**** Start Printing Page: " + 1);
//    ModelPrinter.print(pageBox);
//    Log.debug("**** Done  Printing Page: " + 1);
  }

  public void createRollbackInformation()
  {
    throw new UnsupportedOperationException("Streaming-Renderer do not implement the createRollbackInformation-method.");
  }

  public void applyRollbackInformation()
  {
    throw new UnsupportedOperationException("Streaming-Renderer do not implement the applyRollbackInformation method.");
  }

  public void rollback()
  {
    throw new UnsupportedOperationException("Streaming-Renderer do not implement the rollback method.");
  }
}
