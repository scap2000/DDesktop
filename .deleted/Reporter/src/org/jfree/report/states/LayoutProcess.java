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
 * LayoutProcess.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.states;

import javax.swing.table.TableModel;

import org.jfree.report.DataRow;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.PrepareEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.function.OutputFunction;
import org.jfree.report.function.ProcessingContext;
import org.jfree.report.function.StructureFunction;
import org.jfree.report.states.datarow.DefaultFlowController;
import org.jfree.report.states.datarow.GlobalMasterRow;
import org.jfree.report.states.datarow.ReportDataRow;
import org.jfree.util.Configuration;

/**
 * Creation-Date: Dec 14, 2006, 5:05:39 PM
 *
 * @author Thomas Morgner
 */
public class LayoutProcess implements Cloneable
{
  private static class DataRowRuntime implements ExpressionRuntime
  {
    private ReportState state;

    protected DataRowRuntime()
    {
    }

    public ReportState getState()
    {
      return state;
    }

    public void setState(final ReportState state)
    {
      this.state = state;
    }

    public DataRow getDataRow()
    {
      return state.getDataRow();
    }

    public Configuration getConfiguration()
    {
      return getProcessingContext().getConfiguration();
    }

    public ResourceBundleFactory getResourceBundleFactory()
    {
      return getProcessingContext().getResourceBundleFactory();
    }

    /**
     * Access to the tablemodel was granted using report properties, now direct.
     */
    public TableModel getData()
    {
      final DefaultFlowController flowController = state.getFlowController();
      final GlobalMasterRow masterRow = flowController.getMasterRow();
      final ReportDataRow reportDataRow = masterRow.getReportDataRow();
      if (reportDataRow != null)
      {
        return reportDataRow.getReportData();
      }
      return null;
    }

    /**
     * Where are we in the current processing.
     */
    public int getCurrentRow()
    {
      return state.getCurrentDataItem();
    }

    /**
	 * The output descriptor is a simple string collections consisting of the following components:
	 * exportclass/type/subtype
	 * <p/>
	 * For example, the PDF export would be: pageable/pdf The StreamHTML export would return destTable/html/stream
	 *
	 * @return the export descriptor.
	 */
    public String getExportDescriptor()
    {
      return getProcessingContext().getExportDescriptor();
    }

    public ProcessingContext getProcessingContext()
    {
      return state.getFlowController().getReportContext();
    }
  }

  public static final int LEVEL_PAGINATE = -2;
  public static final int LEVEL_COLLECT = -1;

  private OutputFunction outputFunction;
  private StructureFunction[] collectionFunctions;
  private boolean outputFunctionIsPageListener;
  private boolean outputFunctionIsPrepareListener;
  private boolean[] collectionFunctionIsPageListener;
  private boolean[] collectionFunctionIsPrepareListener;
  private boolean hasPrepareListener;
  private boolean hasPageListener;

  /** @noinspection InstanceofIncompatibleInterface*/
  public LayoutProcess(final OutputFunction outputFunction,
                       final StructureFunction[] collectionFunctions)
  {
    this.outputFunction = outputFunction;
    this.collectionFunctions = collectionFunctions;

    this.outputFunctionIsPageListener = (outputFunction instanceof PageEventListener);
    this.outputFunctionIsPrepareListener = (outputFunction instanceof PrepareEventListener);
    this.collectionFunctionIsPageListener = new boolean[collectionFunctions.length];
    this.collectionFunctionIsPrepareListener = new boolean[collectionFunctions.length];
    this.hasPageListener = outputFunctionIsPageListener;
    this.hasPrepareListener = outputFunctionIsPrepareListener;
    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction fn = collectionFunctions[i];
      if (fn instanceof PageEventListener)
      {
        this.collectionFunctionIsPageListener[i] = true;
        this.hasPageListener = true;
      }
      if (fn instanceof PrepareEventListener)
      {
        this.collectionFunctionIsPrepareListener[i] = true;
        this.hasPrepareListener = true;
      }


    }
  }

  public boolean isPrepareListener()
  {
    return hasPrepareListener;
  }

  public boolean isPageListener()
  {
    return hasPageListener;
  }

  public OutputFunction getOutputFunction()
  {
    return outputFunction;
  }

  public StructureFunction[] getCollectionFunctions()
  {
    return (StructureFunction[]) collectionFunctions.clone();
  }

  public LayoutProcess deriveForStorage ()
  {
    try
    {
      final LayoutProcess lp = (LayoutProcess) clone();
      lp.outputFunction = outputFunction.deriveForStorage();
      lp.collectionFunctions = (StructureFunction[]) collectionFunctions.clone();
      for (int i = 0; i < collectionFunctions.length; i++)
      {
        collectionFunctions[i] = (StructureFunction) collectionFunctions[i].clone();
      }
      return lp;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException();
    }
  }

  public LayoutProcess deriveForPagebreak()
  {
    try
    {
      final LayoutProcess lp = (LayoutProcess) clone();
      lp.outputFunction = outputFunction.deriveForPagebreak();
      lp.collectionFunctions = (StructureFunction[]) collectionFunctions.clone();
      for (int i = 0; i < collectionFunctions.length; i++)
      {
        collectionFunctions[i] = (StructureFunction) collectionFunctions[i].clone();
      }
      return lp;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException();
    }
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  public void fireReportEvent(ReportEvent event)
  {
    if (event.getState().isDeepTraversing())
    {
      // this is a deep traversing state. The meaning of deep-traversing is different for the layout-process. 
      event = new ReportEvent(event.getState(), event.getType() | ReportEvent.DEEP_TRAVERSING_EVENT);
    }

    final int level = event.getLevel();
    if (level >= 0)
    {
      return;
    }

    final ExpressionRuntime oldRuntime = outputFunction.getRuntime();
    final DataRowRuntime runtime = new DataRowRuntime();
    runtime.setState(event.getState());
    outputFunction.setRuntime(runtime);
    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.setRuntime(runtime);
    }

    try
    {
      // first check the flagged events: Prepare, Page-Start, -end, cancel and rollback
      // before heading for the unflagged events ..

      if (event.isPrepareEvent())
      {
        firePrepareEvent(event);
      }
      else if ((event.getType() & ReportEvent.PAGE_STARTED) ==
          ReportEvent.PAGE_STARTED)
      {
        firePageStartedEvent(event);
      }
      else if ((event.getType() & ReportEvent.PAGE_FINISHED) ==
          ReportEvent.PAGE_FINISHED)
      {
        firePageFinishedEvent(event);
      }
      else if ((event.getType() & ReportEvent.ITEMS_ADVANCED) ==
          ReportEvent.ITEMS_ADVANCED)
      {
        fireItemsAdvancedEvent(event);
      }
      else if ((event.getType() & ReportEvent.ITEMS_FINISHED) ==
          ReportEvent.ITEMS_FINISHED)
      {
        fireItemsFinishedEvent(event);
      }
      else if ((event.getType() & ReportEvent.ITEMS_STARTED) ==
          ReportEvent.ITEMS_STARTED)
      {
        fireItemsStartedEvent(event);
      }
      else if ((event.getType() & ReportEvent.GROUP_FINISHED) ==
          ReportEvent.GROUP_FINISHED)
      {
        fireGroupFinishedEvent(event);
      }
      else if ((event.getType() & ReportEvent.GROUP_STARTED) ==
          ReportEvent.GROUP_STARTED)
      {
        fireGroupStartedEvent(event);
      }
      else if ((event.getType() & ReportEvent.REPORT_INITIALIZED) ==
          ReportEvent.REPORT_INITIALIZED)
      {
        fireReportInitializedEvent(event);
      }
      else if ((event.getType() & ReportEvent.REPORT_DONE) ==
          ReportEvent.REPORT_DONE)
      {
        fireReportDoneEvent(event);
      }
      else if ((event.getType() & ReportEvent.REPORT_FINISHED) ==
          ReportEvent.REPORT_FINISHED)
      {
        fireReportFinishedEvent(event);
      }
      else if ((event.getType() & ReportEvent.REPORT_STARTED) ==
          ReportEvent.REPORT_STARTED)
      {
        fireReportStartedEvent(event);
      }
      else
      {
        throw new IllegalArgumentException();
      }
    }
    finally
    {
      outputFunction.setRuntime(oldRuntime);
      for (int i = 0; i < collectionFunctions.length; i++)
      {
        final StructureFunction function = collectionFunctions[i];
        function.setRuntime(oldRuntime);
      }
    }
  }

  private void fireItemsAdvancedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.itemsAdvanced(event);
    }

    if (activeLevel == LEVEL_PAGINATE)
    {
      outputFunction.itemsAdvanced(event);
    }
  }

  private void fireItemsStartedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.itemsStarted(event);
    }

    if (activeLevel == LEVEL_PAGINATE)
    {
      outputFunction.itemsStarted(event);
    }

  }

  private void fireItemsFinishedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.itemsFinished(event);
    }

    if (activeLevel == LEVEL_PAGINATE)
    {
      outputFunction.itemsFinished(event);
    }
  }

  private void fireGroupStartedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.groupStarted(event);
    }

    if (activeLevel == LEVEL_PAGINATE)
    {
      outputFunction.groupStarted(event);
    }
  }

  private void fireGroupFinishedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.groupFinished(event);
    }

    if (activeLevel == LEVEL_PAGINATE)
    {
      outputFunction.groupFinished(event);
    }

  }

  private void fireReportStartedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.reportStarted(event);
    }

    if (activeLevel == LEVEL_PAGINATE)
    {
      outputFunction.reportStarted(event);
    }

  }

  private void fireReportDoneEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.reportDone(event);
    }

    if (activeLevel == LEVEL_PAGINATE)
    {
      outputFunction.reportDone(event);
    }

  }

  private void fireReportFinishedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.reportFinished(event);
    }

    if (activeLevel == LEVEL_PAGINATE)
    {
      outputFunction.reportFinished(event);
    }

  }

  private void fireReportInitializedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      function.reportInitialized(event);
    }

    if (activeLevel == LEVEL_PAGINATE)
    {
      outputFunction.reportInitialized(event);
    }

  }

  private void firePageStartedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      if (collectionFunctionIsPageListener[i])
      {
        final PageEventListener pel = (PageEventListener) function;
        pel.pageStarted(event);
      }
    }

    if (activeLevel == LEVEL_PAGINATE && outputFunctionIsPageListener)
    {
      final PageEventListener pel = (PageEventListener) outputFunction;
      pel.pageStarted(event);
    }
  }

  private void firePageFinishedEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      if (collectionFunctionIsPageListener[i])
      {
        final PageEventListener pel = (PageEventListener) function;
        pel.pageFinished(event);
      }
    }

    if (activeLevel == LEVEL_PAGINATE && outputFunctionIsPageListener)
    {
      final PageEventListener pel = (PageEventListener) outputFunction;
      pel.pageFinished(event);
    }

  }

  /** @noinspection CastToIncompatibleInterface*/
  private void firePrepareEvent(final ReportEvent event)
  {
    final int activeLevel = event.getState().getLevel();

    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      if (collectionFunctionIsPrepareListener[i])
      {
        final PrepareEventListener pel = (PrepareEventListener) function;
        pel.prepareEvent(event);
      }
    }

    if (activeLevel == LEVEL_PAGINATE && outputFunctionIsPrepareListener)
    {
      final PrepareEventListener pel = (PrepareEventListener) outputFunction;
      pel.prepareEvent(event);
    }

  }
}
