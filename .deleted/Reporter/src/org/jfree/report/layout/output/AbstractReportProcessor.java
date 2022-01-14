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
 * AbstractOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.base.log.MemoryUsageMessage;
import org.jfree.report.DataFactory;
import org.jfree.report.EmptyReportException;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportEventException;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportProgressEvent;
import org.jfree.report.event.ReportProgressListener;
import org.jfree.report.function.OutputFunction;
import org.jfree.report.layout.AbstractRenderer;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.Renderer;
import org.jfree.report.states.CollectingReportErrorHandler;
import org.jfree.report.states.IgnoreEverythingReportErrorHandler;
import org.jfree.report.states.LayoutProcess;
import org.jfree.report.states.ReportProcessingErrorHandler;
import org.jfree.report.states.ReportState;
import org.jfree.report.states.ReportStateKey;
import org.jfree.report.states.StateUtilities;
import org.jfree.report.states.process.ProcessState;
import org.jfree.report.util.IntList;
import org.jfree.report.util.ReportConfigurationUtil;
import org.jfree.report.util.StringUtil;
import org.jfree.util.Log;

/**
 * Creation-Date: 08.04.2007, 14:52:52
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReportProcessor implements ReportProcessor
{
  private static final boolean SHOW_ROLLBACKS = false;
  
  protected static final int MAX_EVENTS_PER_RUN = 400;
  protected static final int MIN_ROWS_PER_EVENT = 200;
  protected static final int PAGE_EVENT_RATE = 20;

  /**
   * A flag defining whether to check for Thread-Interrupts.
   */
  private boolean handleInterruptedState;

  /**
   * Storage for listener references.
   */
  private ArrayList listeners;

  /**
   * The listeners as object array for faster access.
   */
  private transient Object[] listenersCache;

  private JFreeReport report;

  private OutputProcessor outputProcessor;

  private PageStateList stateList;
  private transient DataFactory activeDataFactory;

  private IntList physicalMapping;
  private IntList logicalMapping;
  private boolean pagebreaksSupported;
  private boolean paranoidChecks;

  protected AbstractReportProcessor(final JFreeReport report,
                                    final OutputProcessor outputProcessor)
      throws ReportProcessingException
  {
    if (report == null)
    {
      throw new NullPointerException("Report cannot be null.");
    }

    if (outputProcessor == null)
    {
      throw new NullPointerException("OutputProcessor cannot be null");
    }

    try
    {
      // first cloning ... protect the page layouter function ...
      // and any changes we may do to the report instance.

      // a second cloning is done in the start state, to protect the
      // processed data.
      this.report = (JFreeReport) report.clone();
    }
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("Initial Clone of Report failed");
    }

    this.outputProcessor = outputProcessor;
    this.paranoidChecks = "true".equals(outputProcessor.getMetaData().getConfiguration().getConfigProperty
        ("org.jfree.report.layout.ParanoidChecks"));
    this.pagebreaksSupported = outputProcessor.getMetaData().isFeatureSupported(OutputProcessorFeature.PAGEBREAKS);
    final ModifiableConfiguration configuration = report.getReportConfiguration();
    final String yieldRateText = configuration.getConfigProperty("org.jfree.report.YieldRate");
    final int yieldRate = StringUtil.parseInt(yieldRateText, 0);
    if (yieldRate > 0)
    {
      addReportProgressListener(new YieldReportListener(yieldRate));
    }
  }

  protected JFreeReport getReport()
  {
    return report;
  }

  public OutputProcessor getOutputProcessor()
  {
    return outputProcessor;
  }

  protected OutputProcessorMetaData getOutputProcessorMetaData()
  {
    return outputProcessor.getMetaData();
  }


  /**
   * Adds a repagination listener. This listener will be informed of pagination events.
   *
   * @param l the listener.
   */
  public synchronized void addReportProgressListener(final ReportProgressListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      listeners = new ArrayList(5);
    }
    listenersCache = null;
    listeners.add(l);
  }

  /**
   * Removes a repagination listener.
   *
   * @param l the listener.
   */
  public synchronized void removeReportProgressListener(final ReportProgressListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      return;
    }
    listenersCache = null;
    listeners.remove(l);
  }


  /**
   * Sends a repagination update to all registered listeners.
   *
   * @param state the state.
   */
  protected synchronized void fireStateUpdate(final ReportProgressEvent state)
  {
    if (listeners == null)
    {
      return;
    }
    if (listenersCache == null)
    {
      listenersCache = listeners.toArray();
    }
    for (int i = 0; i < listenersCache.length; i++)
    {
      final ReportProgressListener l = (ReportProgressListener) listenersCache[i];
      l.reportProcessingUpdate(state);
    }
  }

  /**
   * Returns whether the processor should check the threads interrupted state. If this is set to true and the thread was
   * interrupted, then the report processing is aborted.
   *
   * @return true, if the processor should check the current thread state, false otherwise.
   */
  public boolean isHandleInterruptedState()
  {
    return handleInterruptedState;
  }

  /**
   * Defines, whether the processor should check the threads interrupted state. If this is set to true and the thread
   * was interrupted, then the report processing is aborted.
   *
   * @param handleInterruptedState true, if the processor should check the current thread state, false otherwise.
   */
  public void setHandleInterruptedState(final boolean handleInterruptedState)
  {
    this.handleInterruptedState = handleInterruptedState;
  }

  /**
   * Checks, whether the current thread is interrupted.
   *
   * @throws org.jfree.report.ReportInterruptedException
   *          if the thread is interrupted to abort the report processing.
   */
  protected final void checkInterrupted()
      throws ReportInterruptedException
  {
    if (isHandleInterruptedState() && Thread.interrupted())
    {
      throw new ReportInterruptedException("Current thread is interrupted. Returning.");
    }
  }

  public synchronized void close()
  {
    if (activeDataFactory != null)
    {
      this.activeDataFactory.close();
      this.activeDataFactory = null;
      this.stateList = null;
      this.physicalMapping = null;
      this.logicalMapping = null;
    }
  }


  protected DefaultProcessingContext createProcessingContext()
  {
    final OutputProcessorMetaData metaData = getOutputProcessorMetaData();
    final boolean maxLineHeightUsed =
        metaData.isFeatureSupported(OutputProcessorFeature.LEGACY_LINEHEIGHT_CALC) == false;
    final boolean imageResolutionMapping =
        metaData.isFeatureSupported(OutputProcessorFeature.IMAGE_RESOLUTION_MAPPING) == false;
    final JFreeReport report = getReport();
    return new DefaultProcessingContext
        (metaData.getExportDescriptor(), new DefaultLayoutSupport(maxLineHeightUsed, imageResolutionMapping),
            report.getResourceBundleFactory(), report.getConfiguration(),
            report.getResourceManager(),
            report.getContentBase());
  }

  /**
     * Processes all prepare levels to compute the function values.
     *
     * @param state the state state with which we beginn the processing.
     * @param level the current processing level.
     * @param maxRows the number of rows in the destTable model.
     * @return the finish state for the current level.
     * @throws ReportProcessingException if processing failed or if there are exceptions during the function execution.
     */
  protected ProcessState processPrepareLevels(ProcessState state, final int level, final int maxRows)
      throws ReportProcessingException
  {
    final boolean failOnError = ReportConfigurationUtil.isStrictErrorHandling(getReport().getReportConfiguration());
    final ReportProcessingErrorHandler errorHandler = new CollectingReportErrorHandler();
    state.setErrorHandler(errorHandler);

    int lastRow = -1;
    int eventCount = 0;
    final int eventTrigger = Math.min(maxRows / MAX_EVENTS_PER_RUN, MIN_ROWS_PER_EVENT);
    final ReportProgressEvent repaginationState = new ReportProgressEvent(this);
    // Function processing does not use the PageLayouter, so we don't need
    // the expensive cloning ...
    while (!state.isFinish())
    {
      checkInterrupted();
      if (lastRow != state.getCurrentDataItem())
      {
        lastRow = state.getCurrentDataItem();
        if (eventCount == 0)
        {
          repaginationState.reuse(level, state.getCurrentDataItem(), state.getNumberOfRows(),
              state.getCurrentPage(), state.getProgressLevel(), state.getProgressLevelCount());
          fireStateUpdate(repaginationState);
          eventCount += 1;
        }
        else
        {
          if (eventCount == eventTrigger)
          {
            eventCount = 0;
          }
          else
          {
            eventCount += 1;
          }
        }
      }

      //progress = state.createStateProgress(progress);
      final ProcessState nextState = state.advance();
      state.setErrorHandler(IgnoreEverythingReportErrorHandler.INSTANCE);
      state = nextState.commit();

      if (errorHandler.isErrorOccured() == true)
      {
        final List childExceptions = Arrays.asList(errorHandler.getErrors());
        errorHandler.clearErrors();
        if (failOnError)
        {
          throw new ReportEventException("Failed to dispatch an event.", childExceptions);
        }
        else
        {
          final ReportEventException exception =
              new ReportEventException("Failed to dispatch an event.", childExceptions);
          Log.error("Failed to dispatch an event.", exception);
        }
      }
    }
    return state;
  }

  protected abstract DefaultOutputFunction createLayoutManager();


  protected void prepareReportProcessing() throws ReportProcessingException
  {
    if (stateList != null)
    {
      // is already paginated.
      return;
    }

    final long start = System.currentTimeMillis();

    try
    {
      // every report processing starts with an StartState.
      final DefaultProcessingContext processingContext = createProcessingContext();
      final JFreeReport report = getReport();
      report.setProperty(JFreeReport.REPORT_DATE_PROPERTY, new Date());
      report.setProperty(JFreeReport.REPORT_LAYOUT_SUPPORT, processingContext.getLayoutSupport());

      final OutputFunction lm = createLayoutManager();

      final LayoutProcess lp = new LayoutProcess
          ((OutputFunction) lm.getInstance(), report.getStructureFunctions());

      final ProcessState startState = new ProcessState(report, processingContext, lp);
      activeDataFactory = startState.getDataFactory();
      ProcessState state = startState;
      final int maxRows = startState.getNumberOfRows();

      report.getDataFactory().open();

      // the report processing can be splitted into 2 separate processes.
      // The first is the ReportPreparation; all function values are resolved and
      // a dummy run is done to calculate the final layout. This dummy run is
      // also necessary to resolve functions which use or depend on the PageCount.

      // the second process is the printing of the report, this is done in the
      // processReport() method.
      processingContext.setPrepareRun(true);

      // now process all function levels.
      // there is at least one level defined, as we added the PageLayouter
      // to the report.
      // the levels are defined from +inf to 0
      // we don't draw and we do not collect states in a StateList yet
      final int[] levels = StateUtilities.computeLevels(report, lp);
      if (levels.length == 0)
      {
        throw new IllegalStateException("Assertation Failed: No functions defined, invalid implementation.");
      }
      processingContext.setProgressLevelCount(levels.length);
      int index = 0;
      int level = levels[index];
      // outer loop: process all function levels
      boolean hasNext;
      do
      {
        processingContext.setProcessingLevel(level);
        processingContext.setProgressLevel(index);

        // if the current level is the output-level, then save the report state.
        // The state is used later to restart the report processing.
        if (level == LayoutProcess.LEVEL_PAGINATE)
        {
          stateList = new PageStateList(this);
          physicalMapping = new IntList(40);
          logicalMapping = new IntList(20);
          Log.debug("Pagination started ..");
          state = processPaginationLevel(state, stateList, maxRows);
        }
        else
        {
          state = processPrepareLevels(state, level, maxRows);
        }

        // if there is an other level to process, then use the finish state to
        // create a new start state, which will continue the report processing on
        // the next higher level.
        hasNext = (index < (levels.length - 1));
        if (hasNext)
        {
          index += 1;
          level = levels[index];
          processingContext.setProcessingLevel(level);
          processingContext.setProgressLevel(index);
          if (state.isFinish())
          {
            state = state.restart();
            // this is a paranoid check ...
            if (state.getCurrentPage() != ReportState.BEFORE_FIRST_PAGE)
            {
              throw new IllegalStateException("State was not set up properly");
            }
          }
          else
          {
            throw new IllegalStateException(
                "Repaginate did not produce an finish state");
          }
        }
      }
      while (hasNext == true);

      // finally return the saved page states.
      processingContext.setPrepareRun(false);
    }
    catch (ReportDataFactoryException e)
    {
      throw new ReportProcessingException("Unable to initialize the report", e);
    }

    final long end = System.currentTimeMillis();
    Log.debug("Pagination-Time: " + (end - start));

  }


  /**
   * Processes the print level for the current report. This function will fill the report state list while performing
   * the repagination.
   *
   * @param startState the start state for the print level.
   * @param pageStates the list of report states that should receive the created page states.
   * @param maxRows    the number of rows in the report (used to estaminate the current progress).
   * @return the finish state for the report.
   * @throws ReportProcessingException if there was a problem processing the report.
   */
  private ProcessState processPaginationLevel(final ProcessState startState,
                                              final PageStateList pageStates,
                                              final int maxRows)
      throws ReportProcessingException
  {
    try
    {
      final boolean failOnError = ReportConfigurationUtil.isStrictErrorHandling(getReport().getReportConfiguration());
      final ReportProcessingErrorHandler errorHandler = new CollectingReportErrorHandler();
      final DefaultLayoutPagebreakHandler pagebreakHandler = new DefaultLayoutPagebreakHandler();

      final ProcessState initialReportState = startState.deriveForStorage();
      final PageState initialPageState = new PageState(initialReportState, outputProcessor.getPageCursor());
      pageStates.add(initialPageState);

      final ReportProgressEvent repaginationState = new ReportProgressEvent(this);

      // inner loop: process the complete report, calculate the function values
      // for the current level. Higher level functions are not available in the
      // dataRow.
      final int eventTrigger = Math.min(maxRows / MAX_EVENTS_PER_RUN, MIN_ROWS_PER_EVENT);

      ProcessState state = startState.deriveForStorage();
      state.setErrorHandler(errorHandler);
      validate(state);

      int pageEventCount = 0;
      // First and last derive of a page must be a storage derivate - this clones everything and does
      // not rely on the more complicated transactional layouting ..
      ProcessState fallBackState = startState.deriveForPagebreak();
//      ProcessState debugState = startState.deriveForStorage();
//      ProcessState lastCommitedState = null;

      Object rollbackPageState = null;
      int rollBackCount = 0;
      boolean isInRollBackMode = false;

      int eventCount = 0;
      int lastRow = -1;
      while (!state.isFinish())
      {
        int logPageCount = outputProcessor.getLogicalPageCount();
        int physPageCount = outputProcessor.getPhysicalPageCount();

        checkInterrupted();
        if (lastRow != state.getCurrentDataItem())
        {
          lastRow = state.getCurrentDataItem();
          if (eventCount == 0)
          {
            repaginationState.reuse(ReportProgressEvent.PAGINATING,
                state.getCurrentDataItem(), state.getNumberOfRows(), state.getCurrentPage(),
                state.getProgressLevel(), state.getProgressLevelCount());
            fireStateUpdate(repaginationState);
            eventCount += 1;
          }
          else
          {
            if (eventCount == eventTrigger)
            {
              eventCount = 0;
            }
            else
            {
              eventCount += 1;
            }
          }
        }

        final ProcessState restoreState = fallBackState;
        if (isPagebreaksSupported())
        {
          if (isInRollBackMode == false)
          {
            if (pageEventCount == PAGE_EVENT_RATE)
            {

//              lastCommitedState = fallBackState.deriveForStorage();
//
              final DefaultOutputFunction commitableOutputFunction =
                  (DefaultOutputFunction) state.getLayoutProcess().getOutputFunction();
              final Renderer commitableRenderer = commitableOutputFunction.getRenderer();
              commitableRenderer.createRollbackInformation();

//              Log.debug ("Deriving " + fallBackState.getProcessKey());
              fallBackState = state.deriveForPagebreak();
//              debugState = state.deriveForStorage();
              validate(state);
              pageEventCount = 0;
            }
            else
            {
              pageEventCount += 1;
            }
          }
          else
          {
            // on rollback, increase the count, but never create new fallback states.
            rollBackCount += 1;
          }
        }

        final ProcessState nextState = state.advance();
        state.setErrorHandler(IgnoreEverythingReportErrorHandler.INSTANCE);
        state = nextState;
        validate(state);

        final ReportStateKey nextStateKey = state.getProcessKey();

        if (errorHandler.isErrorOccured() == true)
        {
          final List childExceptions = Arrays.asList(errorHandler.getErrors());
          errorHandler.clearErrors();
          if (failOnError)
          {
            throw new ReportEventException("Failed to dispatch an event.", childExceptions);
          }
          else
          {
            final ReportEventException exception =
                new ReportEventException("Failed to dispatch an event.", childExceptions);
            Log.error("Failed to dispatch an event.", exception);
          }
        }

        final DefaultOutputFunction lm = (DefaultOutputFunction) state.getLayoutProcess().getOutputFunction();
        final Renderer renderer = lm.getRenderer();
        pagebreakHandler.setReportState(state);

        if (isInRollBackMode)
        {
          if (rollBackCount > PAGE_EVENT_RATE)
          {
            // we missed the state that caused the pagebreak. This means, that the report behaved
            // non-deterministic - which is bad.
            //throw new ReportProcessingException ("Failed to rollback during the page processing: " + rollBackCount);
          }

          // todo: Could be that we have to use the other key here..
          // was: state.getProcessKey()
          if (nextStateKey.equals(rollbackPageState))
          {
            // reached the border case. We have to insert a manual pagebreak here or at least
            // we have to force the renderer to end the page right now.
            //Log.debug ("HERE: Found real pagebreak position. This might be the last state we process.");
            renderer.addPagebreak(state.getProcessKey());
          }
        }

        final boolean pagebreakEncountered = renderer.validatePages();
        if (pagebreakEncountered)
        {
          final Object lastVisibleStateKey = renderer.getLastStateKey();
          if (isPagebreaksSupported() &&
              isInRollBackMode == false &&
              lastVisibleStateKey != null &&
              renderer.isOpen())
          {
            if (lastVisibleStateKey.equals(nextStateKey) == false)
            {
              // Roll back to the last known to be good position and process the states up to, but not
              // including the current state. This way, we can fire the page-events *before* this band
              // gets printed.
              rollbackPageState = lastVisibleStateKey;
              rollBackCount = 0;
              state = restoreState.deriveForPagebreak();

              final DefaultOutputFunction rollbackOutputFunction = (DefaultOutputFunction) state.getLayoutProcess().getOutputFunction();
              final Renderer rollbackRenderer = rollbackOutputFunction.getRenderer();
              rollbackRenderer.rollback();
              if (SHOW_ROLLBACKS)
              {
                Log.debug("HERE: Encountered bad break, need to roll-back: " + rollbackPageState);
                Log.debug("HERE:                                         : " + state.getProcessKey());
                Log.debug("HERE:                                         : " + restoreState.getProcessKey());
              }

              validate(state);

              isInRollBackMode = true;
              fallBackState = null; // there is no way we can fall-back inside a roll-back ..
              continue;
            }
            else
            {
              // The current state printed content partially on the now finished page and there is more
              // content on the currently open page. This is a in-between pagebreak, we invoke a pagebreak
              // after this state has been processed.
              if (SHOW_ROLLBACKS)
              {
                Log.debug("HERE: Encountered on-going break " + lastVisibleStateKey);
              }
            }
          }
          else
          {
            if (SHOW_ROLLBACKS)
            {
              Log.debug("HERE: Encountered a good break or a roll-back break: " + isInRollBackMode);
              Log.debug("HERE:                                              : " + state.getProcessKey());
            }
            isInRollBackMode = false;
            rollbackPageState = null;
            rollBackCount = 0;
          }

          if (isPagebreaksSupported() == false)
          {
            // The commit causes all closed-nodes to become finishable. This allows the process-page
            // and the incremental-update methods to remove the nodes. For non-streaming targets (where
            // pagebreaks are possible) the commit state is managed manually
            renderer.applyAutoCommit();
          }
          
          if (renderer.processPage(pagebreakHandler, state.getProcessKey(), true) == false)
          {
            throw new IllegalStateException
                ("This cannot be. If the validation said we get a new page, how can we now get lost here");
          }

          state = state.commit();

          // can continue safely ..
          final int newLogPageCount = outputProcessor.getLogicalPageCount();
          final int newPhysPageCount = outputProcessor.getPhysicalPageCount();

          final int result = stateList.size() - 1;
          for (; physPageCount < newPhysPageCount; physPageCount++)
          {
            physicalMapping.add(result);
          }

          for (; logPageCount < newLogPageCount; logPageCount++)
          {
            logicalMapping.add(result);
          }

          if (state.isFinish() == false)
          {
            // A pagebreak has occured ...
            // We add all but the last state ..
            final PageState pageState = new PageState(state.deriveForStorage(), outputProcessor.getPageCursor());
            stateList.add(pageState);
          }

          if (isPagebreaksSupported())
          {
            fallBackState = state.deriveForPagebreak();
            pageEventCount = 0;
            eventCount = 0;
          }
        }
        else
        {
          if (isPagebreaksSupported() == false)
          {
            renderer.applyAutoCommit();
          }

          // PageEventCount is zero on streaming exports and zero after a new rollback event is created.
          if (pageEventCount == 0 && isInRollBackMode == false)
          {
            renderer.processIncrementalUpdate(true);
          }
          state = state.commit();

          // Expected a pagebreak now, but did not encounter one.
          // todo: Could be that we have to use the other key here ..
//          if (nextStateKey.equals(rollbackPageState))
//          {
//            // reached the border case. We have to insert a manual pagebreak here or at least
//            // we have to force the renderer to end the page right now.
//            // todo
//            Log.debug("HERE: Ups, Found real pagebreak position. but where is my break?");
//            renderer.addPagebreak(state.getProcessKey());
//          }

          if (fallBackState != restoreState)
          {
            final DefaultOutputFunction commitableOutputFunction =
                (DefaultOutputFunction) state.getLayoutProcess().getOutputFunction();
            final Renderer commitableRenderer = commitableOutputFunction.getRenderer();
            commitableRenderer.applyRollbackInformation();
          }
        }

      }
      return initialReportState;
    }
    catch (ContentProcessingException e)
    {
      throw new ReportProcessingException("Content-Processing failed.", e);
    }
  }

  private void validate(final ProcessState state)
  {
    if (paranoidChecks)
    {
      final DefaultOutputFunction of = (DefaultOutputFunction) state.getLayoutProcess().getOutputFunction();
      final AbstractRenderer r = (AbstractRenderer) of.getRenderer();
      r.performParanoidModelCheck();
    }
  }

  public boolean isPaginated()
  {
    return stateList != null;
  }

  protected PageState getLogicalPageState(final int page)
  {
    final int index = logicalMapping.get(page);
    final PageState pageState = stateList.get(index);
    if (pageState == null)
    {
      throw new IndexOutOfBoundsException("The logical mapping between page " + page + " and index " + index + " is invalid.");
    }
    return pageState;
  }

  protected PageState getPhysicalPageState(final int page)
  {
    final int index = physicalMapping.get(page);
    final PageState pageState = stateList.get(index);
    if (pageState == null)
    {
      throw new IndexOutOfBoundsException("The physical mapping between page " + page + " and index " + index + " is invalid.");
    }
    return pageState;
  }


  public PageState processPage(final PageState pageState,
                               final boolean performOutput)
      throws ReportProcessingException
  {
    if (pageState == null)
    {
      throw new NullPointerException("PageState must not be null.");
    }
    final boolean failOnError = ReportConfigurationUtil.isStrictErrorHandling(getReport().getReportConfiguration());
    final ReportProcessingErrorHandler errorHandler = new CollectingReportErrorHandler();
    //todo

    try
    {
      final ProcessState startState = pageState.getReportState();
      outputProcessor.setPageCursor(pageState.getPageCursor());
      final int maxRows = startState.getNumberOfRows();
      final ReportProgressEvent repaginationState = new ReportProgressEvent(this);
      final DefaultLayoutPagebreakHandler pagebreakHandler = new DefaultLayoutPagebreakHandler();
      // inner loop: process the complete report, calculate the function values
      // for the current level. Higher level functions are not available in the
      // dataRow.
      final int eventTrigger = Math.min(maxRows / MAX_EVENTS_PER_RUN, MIN_ROWS_PER_EVENT);

      Object rollbackPageState = null;

      ProcessState state = startState.deriveForStorage();
      ProcessState fallBackState = state.deriveForPagebreak();
      state.setErrorHandler(errorHandler);

      int rollBackCount = 0;
      boolean isInRollBackMode = false;
      int lastRow = -1;
      int eventCount = 0;
      int pageEventCount = 0;
      while (!state.isFinish())
      {
        checkInterrupted();
        if (lastRow != state.getCurrentDataItem())
        {
          lastRow = state.getCurrentDataItem();
          if (eventCount == 0)
          {
            repaginationState.reuse(ReportProgressEvent.GENERATING_CONTENT,
                state.getCurrentDataItem(), maxRows, state.getCurrentPage(),
                state.getProgressLevel(), state.getProgressLevelCount());
            fireStateUpdate(repaginationState);
            eventCount += 1;
          }
          else
          {
            if (eventCount == eventTrigger)
            {
              eventCount = 0;
            }
            else
            {
              eventCount += 1;
            }
          }
        }


        final ProcessState restoreState = fallBackState;
        if (isPagebreaksSupported())
        {
          if (isInRollBackMode == false)
          {
            if (pageEventCount == PAGE_EVENT_RATE)
            {
              final DefaultOutputFunction commitableOutputFunction =
                  (DefaultOutputFunction) state.getLayoutProcess().getOutputFunction();
              final Renderer commitableRenderer = commitableOutputFunction.getRenderer();
              commitableRenderer.createRollbackInformation();

              fallBackState = state.deriveForPagebreak();
              pageEventCount = 0;
            }
            else
            {
              pageEventCount += 1;
            }
          }
          else
          {
            // on rollback, increase the count, but never create new fallback states.
            rollBackCount += 1;
          }
        }

        final ProcessState nextState = state.advance();
        state.setErrorHandler(IgnoreEverythingReportErrorHandler.INSTANCE);
        state = nextState;

        final ReportStateKey nextStateKey = state.getProcessKey();

        if (errorHandler.isErrorOccured() == true)
        {
          final List childExceptions = Arrays.asList(errorHandler.getErrors());
          errorHandler.clearErrors();
          if (failOnError)
          {
            throw new ReportEventException("Failed to dispatch an event.", childExceptions);
          }
          else
          {
            final ReportEventException exception =
                new ReportEventException("Failed to dispatch an event.", childExceptions);
            Log.error("Failed to dispatch an event.", exception);
          }
        }

        final DefaultOutputFunction lm = (DefaultOutputFunction) state.getLayoutProcess().getOutputFunction();
        final Renderer renderer = lm.getRenderer();
        pagebreakHandler.setReportState(state);

        if (isInRollBackMode)
        {
          if (rollBackCount > PAGE_EVENT_RATE)
          {
            // we missed the state that caused the pagebreak. This means, that the report behaved
            // non-deterministic - which is bad.
            //throw new ReportProcessingException ("Failed to rollback during the page processing: " + rollBackCount);
          }

          if (nextStateKey.equals(rollbackPageState))
          {
            // reached the border case. We have to insert a manual pagebreak here or at least
            // we have to force the renderer to end the page right now.
//            Log.debug ("HERE: Found real pagebreak position. This might be the last state we process.");
            renderer.addPagebreak(state.getProcessKey());
          }
        }

        final boolean pagebreakEncountered = (renderer.validatePages());
        if (pagebreakEncountered)
        {
          final Object lastStateKey = renderer.getLastStateKey();
          if (isPagebreaksSupported() &&
              isInRollBackMode == false &&
              renderer.isOpen() &&
              lastStateKey != null)
          {
            if (lastStateKey.equals(nextStateKey) == false)
            {
//              Log.debug ("HERE: Encountered bad break, need to roll-back");

              // Roll back to the last known to be good position and process the states up to, but not
              // including the current state. This way, we can fire the page-events *before* this band
              // gets printed.
              rollbackPageState = lastStateKey;
              rollBackCount = 0;
              state = restoreState.deriveForPagebreak();

              final DefaultOutputFunction rollbackOutputFunction = (DefaultOutputFunction) state.getLayoutProcess().getOutputFunction();
              final Renderer rollbackRenderer = rollbackOutputFunction.getRenderer();
              rollbackRenderer.rollback();
              if (SHOW_ROLLBACKS)
              {
                Log.debug("HERE: Encountered bad break, need to roll-back: " + rollbackPageState);
                Log.debug("HERE:                                         : " + state.getProcessKey());
                Log.debug("HERE:                                         : " + restoreState.getProcessKey());
              }

              validate(state);

              isInRollBackMode = true;
              continue;
            }
//            else
//            {
//              // The current state printed content partially on the now finished page and there is more
//              // content on the currently open page. This is a in-between pagebreak, we invoke a pagebreak
//              // after this state has been processed.
//              Log.debug ("HERE: Encountered on-going break " + lastStateKey);
//            }
          }
//          else
//          {
//            Log.debug ("HERE: Encountered a good break or a roll-back break: " + isInRollBackMode);
//            Log.debug ("HERE:                                              : " + state.getProcessKey());
//          }
          if (isPagebreaksSupported() == false)
          {
            // The commit causes all closed-nodes to become finishable. This allows the process-page
            // and the incremental-update methods to remove the nodes. For non-streaming targets (where
            // pagebreaks are possible) the commit state is managed manually
            renderer.applyAutoCommit();
          }

          if (renderer.processPage(pagebreakHandler, state.getProcessKey(), performOutput) == false)
          {
            throw new IllegalStateException("This must not be.");
          }

          state = state.commit();
          if (renderer.isOpen())
          {
            // No need to create a copy here. It is part of the contract that the resulting page state must be
            // cloned before it can be used again. The only place where it is used is this method, so we can
            // be pretty sure that this contract is valid.
            return new PageState(state, outputProcessor.getPageCursor());
          }
        }
        else
        {
          if (isPagebreaksSupported() == false)
          {
            // The commit causes all closed-nodes to become finishable. This allows the process-page
            // and the incremental-update methods to remove the nodes. For non-streaming targets (where
            // pagebreaks are possible) the commit state is managed manually
            renderer.applyAutoCommit();
          }
          
          if (pageEventCount == 0 && isInRollBackMode == false)
          {
            renderer.processIncrementalUpdate(performOutput);
          }
          state = state.commit();

          // Expected a pagebreak now, but did not encounter one.
//          if (nextStateKey.equals(rollbackPageState))
//          {
//            // reached the border case. We have to insert a manual pagebreak here or at least
//            // we have to force the renderer to end the page right now.
//            Log.debug ("HERE: Ups, Found real pagebreak position. but where is my break?");
//            //renderer.addPagebreak(state.getProcessKey());
//          }
          if (fallBackState != restoreState)
          {
            final DefaultOutputFunction commitableOutputFunction =
                (DefaultOutputFunction) state.getLayoutProcess().getOutputFunction();
            final Renderer commitableRenderer = commitableOutputFunction.getRenderer();
            commitableRenderer.applyRollbackInformation();
          }
        }
      }

      // We should never reach this point, if this function has been called by the PageStateList.
      return null;
    }
//    catch (CloneNotSupportedException e)
//    {
//      throw new ReportProcessingException("Clone failed. This cannot be.");
//    }
    catch (ContentProcessingException e)
    {
      throw new ReportProcessingException("Content-Processing failed.", e);
    }
  }

  public void processReport() throws ReportProcessingException
  {
    if (Log.isDebugEnabled())
    {
      Log.debug(new MemoryUsageMessage(System.identityHashCode(Thread.currentThread()) + ": Report processing time: Starting: "));
    }
    try
    {
      final long startTime = System.currentTimeMillis();
      if (isPaginated() == false)
      {
        // Processes the whole report ..
        prepareReportProcessing();
      }

      final long paginateTime = System.currentTimeMillis();
      if (Log.isDebugEnabled())
      {
        Log.debug(new MemoryUsageMessage
            (System.identityHashCode(Thread.currentThread()) +
                ": Report processing time: Pagination time: " + ((paginateTime - startTime) / 1000.0)));
      }
      if (getLogicalPageCount() == 0)
      {
        throw new EmptyReportException("Report did not generate any content.");
      }

      // Start from scratch ...
      PageState state = getLogicalPageState(0);
      while (state != null)
      {
        state = processPage(state, true);
      }
      final long endTime = System.currentTimeMillis();
      if (Log.isDebugEnabled())
      {
        Log.debug(new MemoryUsageMessage
            (System.identityHashCode(Thread.currentThread()) +
                ": Report processing time: " + ((endTime - startTime) / 1000.0)));
      }
    }
    catch(EmptyReportException re)
    {
      throw re;
    }
    catch (ReportProcessingException re)
    {
      Log.error(System.identityHashCode(Thread.currentThread()) + ": Report processing failed.");
      throw re;
    }
    catch (Exception e)
    {
      Log.error(System.identityHashCode(Thread.currentThread()) + ": Report processing failed.");
      throw new ReportProcessingException("Failed to process the report", e);
    }
    if (Log.isDebugEnabled())
    {
      Log.debug(System.identityHashCode(Thread.currentThread()) + ": Report processing finished.");
    }
  }

  public int getLogicalPageCount()
  {
    return logicalMapping.size();
  }

  /**
   * Checks, whether the output mode may generate pagebreaks. If we have to deal with pagebreaks, we may have to perform
   * roll-backs and commits to keep the pagebreaks in sync with the state-processing. This is ugly, expensive and you
   * better dont try this at home.
   * <p/>
   * The roll-back is done for paginated and flow-report outputs, but if we have no autmoatic and manual pagebreaks,
   * there is no need to even consider to roll-back to a state before the pagebreak (which will never occur).
   *
   * @return a flag indicating whether the output target supports pagebreaks.
   */
  protected boolean isPagebreaksSupported()
  {
    return pagebreaksSupported;
  }
}
