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
 * CSVProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.csv;

import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.DataFactory;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportEventException;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.event.ReportProgressEvent;
import org.jfree.report.event.ReportProgressListener;
import org.jfree.report.function.OutputFunction;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.output.DefaultProcessingContext;
import org.jfree.report.states.CollectingReportErrorHandler;
import org.jfree.report.states.IgnoreEverythingReportErrorHandler;
import org.jfree.report.states.LayoutProcess;
import org.jfree.report.states.ReportProcessingErrorHandler;
import org.jfree.report.states.StateUtilities;
import org.jfree.report.states.process.ProcessState;
import org.jfree.report.util.NullOutputStream;
import org.jfree.report.util.ReportConfigurationUtil;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * The <code>CSVProcessor</code> coordinates the writing process for the raw CSV
 * output.
 * <p/>
 * A {@link CSVWriter} is added to the private copy of the report to handle the
 * output process.
 *
 * @author Thomas Morgner
 */
public class CSVProcessor
{
  protected static final int MAX_EVENTS_PER_RUN = 200;
  protected static final int MIN_ROWS_PER_EVENT = 200;

  /**
   * A key for accessing the separator string in the {@link
   * org.jfree.report.util.ReportConfiguration}.
   */
  public static final String CSV_SEPARATOR =
      "org.jfree.report.modules.output.csv.Separator";

  /**
   * A key for accessing the 'print data row names' flag in the {@link
   * org.jfree.report.util.ReportConfiguration}.
   */
  public static final String CSV_DATAROWNAME
      = "org.jfree.report.modules.output.csv.WriteDatarowNames";

  public static final String CSV_WRITE_STATECOLUMNS
      = "org.jfree.report.modules.output.csv.WriteStateColumns";
  public static final String CSV_ENABLE_REPORTHEADER
      = "org.jfree.report.modules.output.csv.EnableReportHeader";
  public static final String CSV_ENABLE_REPORTFOOTER
      = "org.jfree.report.modules.output.csv.EnableReportFooter";
  public static final String CSV_ENABLE_GROUPHEADERS
      = "org.jfree.report.modules.output.csv.EnableGroupHeaders";
  public static final String CSV_ENABLE_GROUPFOOTERS
      = "org.jfree.report.modules.output.csv.EnableGroupFooters";
  public static final String CSV_ENABLE_ITEMBANDS
      = "org.jfree.report.modules.output.csv.EnableItembands";

//  /**
//   * The default name for the csv writer function used by this processor.
//   */
//  private static final String CSV_WRITER =
//      "org.jfree.report.modules.output.csv.csv-writer";

  /**
   * The character stream writer to be used by the {@link CSVWriter} function.
   */
  private Writer writer;

  /**
   * The report to be processed.
   */
  private JFreeReport report;

  /**
   * Defines, whether this processor should check the thread for an interrupt
   * request.
   */
  private boolean handleInterruptedState;
  private static final String EXPORT_DESCRIPTOR = "data/csv";
  private String separator;
  private boolean writeDataRowNames;
  /** Storage for listener references. */
  private ArrayList listeners;
  private transient Object[] listenersCache;
  private transient DataFactory activeDataFactory;

  /**
     * Creates a new <code>CSVProcessor</code>. The processor will use a comma
     * (",") to separate the destColumn values, unless defined otherwise in the report
     * configuration. The processor creates a private copy of the clone, so that
     * no change to the original report will influence the report processing.
     * DataRow names are not written.
     *
     * @param report the report to be processed.
     * @throws ReportProcessingException if the report initialisation failed.
     */
  public CSVProcessor(final JFreeReport report)
      throws ReportProcessingException
  {
    this(report, report.getReportConfiguration().getConfigProperty(CSV_SEPARATOR, ","));
  }

  /**
     * Creates a new CSVProcessor. The processor will use the specified separator,
     * the report configuration is not queried for a separator. The processor
     * creates a private copy of the clone, so that no change to the original
     * report will influence the report processing. DataRowNames are not written.
     *
     * @param report the report to be processed.
     * @param separator the separator string to mark destColumn boundaries.
     * @throws ReportProcessingException if the report initialisation failed.
     */
  public CSVProcessor(final JFreeReport report, final String separator)
      throws ReportProcessingException
  {
    this(report, separator,
        queryBoolConfig(report.getReportConfiguration(), CSV_DATAROWNAME));
  }

  /**
     * Creates a new CSVProcessor. The processor will use the specified separator,
     * the report configuration is not queried for a separator. The processor
     * creates a private copy of the clone, so that no change to the original
     * report will influence the report processing. The first row will contain the
     * datarow names.
     *
     * @param report the report to be processed.
     * @param separator the separator string to mark destColumn boundaries.
     * @param writeDataRowNames controls whether or not the data row names are
     * output.
     * @throws ReportProcessingException if the report initialisation failed.
     */
  public CSVProcessor(final JFreeReport report,
                      final String separator,
                      final boolean writeDataRowNames)
      throws ReportProcessingException
  {
    this.separator = separator;
    this.writeDataRowNames = writeDataRowNames;

    if (report == null)
    {
      throw new NullPointerException();
    }
    try
    {
      this.report = (JFreeReport) report.clone();
    }
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("Initial Clone of Report failed");
    }
  }

  private static boolean queryBoolConfig(final Configuration config,
                                         final String name)
  {
    return "true".equals(config.getConfigProperty(name, "false"));
  }

  /**
   * Gets the local copy of the report. This report is initialized to handle the
   * report writing, changes to the report can have funny results, so be
   * carefull, when using the report object.
   *
   * @return the local copy of the report.
   */
  protected JFreeReport getReport()
  {
    return report;
  }

  /**
   * Returns the writer used in this Processor.
   *
   * @return the writer
   */
  public Writer getWriter()
  {
    return writer;
  }

  /**
   * Defines the writer which should be used to write the contents of the
   * report.
   *
   * @param writer the writer.
   */
  public void setWriter(final Writer writer)
  {
    this.writer = writer;
  }

  /**
   * Adds a repagination listener. This listener will be informed of pagination
   * events.
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
  protected void fireStateUpdate(final ReportProgressEvent state)
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
   * Processes the entire report and records the state at the end of every
   * page.
   *
   * @return a list of report states (one for the beginning of each page in the
   *         report).
   * @throws ReportProcessingException  if there was a problem processing the
   *                                    report.
   * @throws CloneNotSupportedException if there is a problem cloning.
   */
  private ProcessState repaginate()
      throws ReportProcessingException
  {
    getReport().getDataFactory().open();

    // every report processing starts with an StartState.
    try
    {
      final DefaultLayoutSupport layoutSupport = new DefaultLayoutSupport(true, true);
      final DefaultProcessingContext processingContext =
          new DefaultProcessingContext(EXPORT_DESCRIPTOR, layoutSupport,
              report.getResourceBundleFactory(),
              report.getReportConfiguration(),
              report.getResourceManager(),
              report.getContentBase());

      report.setProperty(JFreeReport.REPORT_DATE_PROPERTY, new Date());
      report.setProperty(JFreeReport.REPORT_LAYOUT_SUPPORT, layoutSupport);

      final CSVWriter lm = new CSVWriter();
      lm.setSeparator(separator);
      lm.setWriteDataRowNames(writeDataRowNames);

      final ModifiableConfiguration config = report.getReportConfiguration();
      lm.setWriteStateColumns(queryBoolConfig(config, CSV_WRITE_STATECOLUMNS));
      lm.setEnableReportHeader(queryBoolConfig(config, CSV_ENABLE_REPORTHEADER));
      lm.setEnableReportFooter(queryBoolConfig(config, CSV_ENABLE_REPORTFOOTER));
      lm.setEnableGroupHeader(queryBoolConfig(config, CSV_ENABLE_GROUPHEADERS));
      lm.setEnableGroupFooter(queryBoolConfig(config, CSV_ENABLE_GROUPFOOTERS));
      lm.setEnableItemband(queryBoolConfig(config, CSV_ENABLE_ITEMBANDS));

      final LayoutProcess lp = new LayoutProcess
          ((OutputFunction) lm.getInstance(), report.getStructureFunctions());

      final ProcessState startState = new ProcessState(getReport(), processingContext, lp);
      activeDataFactory = startState.getDataFactory();
      final int maxRows = startState.getNumberOfRows();
      ProcessState state = startState;

      // the report processing can be splitted into 2 separate processes.
      // The first is the ReportPreparation; all function values are resolved and
      // a dummy run is done to calculate the final layout. This dummy run is
      // also necessary to resolve functions which use or depend on the PageCount.

      // the second process is the printing of the report, this is done in the
      // processReport() method.

      // during a prepare run the REPORT_PREPARERUN_PROPERTY is set to true.
      processingContext.setPrepareRun(true);

      // now change the writer function to be a dummy writer. We don't want any
      // output in the prepare runs.
      final CSVWriter w = (CSVWriter) lp.getOutputFunction();
      w.setWriter(new OutputStreamWriter(new NullOutputStream()));

      // now process all function levels.
      // there is at least one level defined, as we added the CSVWriter
      // to the report.
      final int[] levels = StateUtilities.computeLevels(report, lp);
      if (levels.length == 0)
      {
        throw new IllegalStateException("No functions defined, invalid implementation.");
      }
      processingContext.setProgressLevelCount(levels.length + 1);

      ProcessState retval = null;
      boolean hasNext;
      int index = 0;
      int level = levels[index];
      // outer loop: process all function levels
      do
      {
        processingContext.setProcessingLevel(level);
        processingContext.setProgressLevel(index);

        // if the current level is the output-level, then save the report state.
        // The state is used later to restart the report processing.
        if (level == LayoutProcess.LEVEL_PAGINATE)
        {
          retval = (ProcessState) state.clone();
        }

        // inner loop: process the complete report, calculate the function values
        // for the current level. Higher level functions are not available in the
        // dataRow.
        final boolean failOnError = (level == LayoutProcess.LEVEL_PAGINATE) &&
            ReportConfigurationUtil.isStrictErrorHandling
                (getReport().getReportConfiguration());

        final int eventTrigger = Math.min(maxRows / MAX_EVENTS_PER_RUN, MIN_ROWS_PER_EVENT);
        final ReportProgressEvent repaginationState = new ReportProgressEvent(this);
        final ReportProcessingErrorHandler errorHandler = new CollectingReportErrorHandler();
        state.setErrorHandler(errorHandler);

        int eventCount = 0;
        int lastRow = -1;
        while (!state.isFinish())
        {
          checkInterrupted();
          if (lastRow != state.getCurrentDataItem())
          {
            lastRow = state.getCurrentDataItem();
            if (eventCount == 0)
            {
              repaginationState.reuse(level,
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
//          if (!state.isFinish())
//          {
//            // if the report processing is stalled, throw an exception; an infinite
//            // loop would be caused.
//            if (!state.isProceeding(progress))
//            {
//              throw new ReportProcessingException("State did not proceed, bailing out!");
//            }
//          }
        }

        // if there is an other level to process, then use the finish state to
        // create a new start state, which will continue the report processing on
        // the next higher level.
        hasNext = (index < (levels.length - 1));
        if (hasNext)
        {
          index += 1;
          level = levels[index];
          processingContext.setProgressLevel(index);
          processingContext.setProcessingLevel(level);
          if (state.isFinish())
          {
            state = state.restart();
          }
          else
          {
            throw new IllegalStateException("The processing did not produce an finish state");
          }
        }
      }
      while (hasNext == true);

      // root of evilness here ... pagecount should not be handled specially ...
      // The pagecount should not be added as report property, there are functions to
      // do this.

      // finally prepeare the returned start state.
      if (retval == null)
      {
        throw new IllegalStateException("There was no valid pagination done.");
      }
      if (retval.getEventCode() != ReportEvent.REPORT_INITIALIZED)
      {
        throw new IllegalStateException("There was no valid pagination done.");
      }

      processingContext.setPrepareRun(false);
      return retval;
    }
    catch (CloneNotSupportedException e)
    {
      throw new ReportProcessingException("Unable to clone the start state.", e);
    }
    catch (ReportDataFactoryException e)
    {
      throw new ReportProcessingException("Unable to initialize the report", e);
    }

  }

  /**
   * Processes the report. The generated output is written using the defined
   * writer, the report is repaginated before the final writing.
   *
   * @throws ReportProcessingException if the report processing failed.
   * @throws IllegalStateException     if there is no writer defined.
   */
  public synchronized void processReport()
      throws ReportProcessingException
  {
    if (writer == null)
    {
      throw new IllegalStateException("No writer defined");
    }

    try
    {
      ProcessState state = repaginate();

      final int maxRows = state.getNumberOfRows();
      final int eventTrigger = Math.min(maxRows / MAX_EVENTS_PER_RUN, MIN_ROWS_PER_EVENT);

      final CSVWriter w = (CSVWriter) state.getLayoutProcess().getOutputFunction();
      w.setWriter(getWriter());

      final boolean failOnError = ReportConfigurationUtil.isStrictErrorHandling
          (getReport().getReportConfiguration());
      final ReportProcessingErrorHandler errorHandler = new CollectingReportErrorHandler();
      state.setErrorHandler(errorHandler);

      final ReportProgressEvent repaginationState = new ReportProgressEvent(this);
      int lastRow = -1;
      int eventCount = 0;
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
//        if (!state.isFinish())
//        {
//          if (!state.isProceeding(progress))
//          {
//            throw new ReportProcessingException("State did not proceed, bailing out!");
//          }
//        }
      }
    }
    finally
    {
      activeDataFactory.close();
      activeDataFactory = null;
    }
  }

  /**
   * Returns whether the processor should check the threads interrupted state.
   * If this is set to true and the thread was interrupted, then the report
   * processing is aborted.
   *
   * @return true, if the processor should check the current thread state, false
   *         otherwise.
   */
  public boolean isHandleInterruptedState()
  {
    return handleInterruptedState;
  }

  /**
   * Defines, whether the processor should check the threads interrupted state.
   * If this is set to true and the thread was interrupted, then the report
   * processing is aborted.
   *
   * @param handleInterruptedState true, if the processor should check the
   *                               current thread state, false otherwise.
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
  protected void checkInterrupted()
      throws ReportInterruptedException
  {
    if (isHandleInterruptedState() && Thread.interrupted())
    {
      throw new ReportInterruptedException("Current thread is interrupted. Returning.");
    }
  }
}
