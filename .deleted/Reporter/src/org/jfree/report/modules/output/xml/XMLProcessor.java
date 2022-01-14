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
 * XMLProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.output.xml;

import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jfree.report.DataFactory;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportEventException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;
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
import org.jfree.util.Log;

/**
 * The XMLProcessor coordinates the report processing for the XML-Output. This
 * class is responsible to initialize and maintain the XMLWriter, which performs
 * the output process.
 * <p/>
 * The XMLProcessor is not intended to produce complex output, it is an
 * educational example. If you want valid xml data enriched with layouting
 * information, then have a look at the HTML-OutputTarget, this target is also
 * able to write XHTMl code.
 *
 * @author Thomas Morgner
 */
public class XMLProcessor
{
  /**
   * the target writer.
   */
  private Writer writer;

  /**
   * the source report.
   */
  private JFreeReport report;
  private static final String EXPORT_DESCRIPTOR = "document/xml";
  private DataFactory activeDataFactory;

  /**
   * Creates a new XMLProcessor. The processor will output the report as simple
   * xml stream.
   *
   * @param report the report that should be processed
   * @throws ReportProcessingException if the report could not be initialized
   */
  public XMLProcessor(final JFreeReport report)
      throws ReportProcessingException
  {
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

  /**
   * Returns the XMLProcessors local report instance. This instance has the
   * XMLWriter attached and should be used outside of this class.
   *
   * @return the processors report instance.
   */
  protected JFreeReport getReport()
  {
    return report;
  }

  /**
   * Returns the writer, which will receive the generated output.
   *
   * @return the writer
   */
  public Writer getWriter()
  {
    return writer;
  }

  /**
   * Sets the writer, which will receive the generated output. The writer should
   * have the proper encoding set.
   *
   * @param writer that should receive the generated output.
   */
  public void setWriter(final Writer writer)
  {
    this.writer = writer;
  }

  /**
   * Processes the entire report and records the state at the end of the report
   * preparation.
   *
   * @return the final ReportState
   * @throws ReportProcessingException  if there was a problem processing the
   *                                    report.
   * @throws CloneNotSupportedException if there is a cloning problem.
   */
  private ProcessState repaginate()
      throws
      ReportProcessingException, CloneNotSupportedException
  {
    try
    {
      report.getDataFactory().open();

      final DefaultLayoutSupport layoutSupport = new DefaultLayoutSupport(true, true);
      final DefaultProcessingContext processingContext =
          new DefaultProcessingContext(EXPORT_DESCRIPTOR,
              layoutSupport,
              report.getResourceBundleFactory(),
              report.getReportConfiguration(),
              report.getResourceManager(),
              report.getContentBase());

      report.setProperty(JFreeReport.REPORT_DATE_PROPERTY, new Date());
      report.setProperty(JFreeReport.REPORT_LAYOUT_SUPPORT, layoutSupport);

      final XMLWriter lm = new XMLWriter();

      final LayoutProcess lp = new LayoutProcess
          ((OutputFunction) lm.getInstance(), report.getStructureFunctions());

      final ProcessState pstate = new ProcessState(getReport(), processingContext, lp);
      activeDataFactory = pstate.getDataFactory();
      ProcessState state = pstate;

      // the report processing can be splitted into 2 separate processes.
      // The first is the ReportPreparation; all function values are resolved and
      // a dummy run is done to calculate the final layout. This dummy run is
      // also necessary to resolve functions which use or depend on the PageCount.

      // the second process is the printing of the report, this is done in the
      // processReport() method.
      processingContext.setPrepareRun(true);

      // now change the writer function to be a dummy writer. We don't want any
      // output in the prepare runs.
      final XMLWriter w = (XMLWriter) lp.getOutputFunction();
      w.setWriter(new OutputStreamWriter(new NullOutputStream()));

      // now process all function levels.
      // there is at least one level defined, as we added the CSVWriter
      // to the report.
      final int[] levels = StateUtilities.computeLevels(report, lp);
      if (levels.length == 0)
      {
        throw new IllegalStateException("No functions defined, invalid implementation.");
      }

      processingContext.setProgressLevelCount(levels.length);
      boolean hasNext;
      int index = 0;
      int level = levels[index];
      // outer loop: process all function levels
      ProcessState retval = null;
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
        final boolean failOnError =
            ReportConfigurationUtil.isStrictErrorHandling(getReport().getReportConfiguration());
        final ReportProcessingErrorHandler errorHandler = new CollectingReportErrorHandler();
        state.setErrorHandler(errorHandler);
        while (!state.isFinish())
        {
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
            throw new IllegalStateException("Repaginate did not produce an finish state");
          }
        }
      }
      while (hasNext == true);


      processingContext.setPrepareRun(false);
      // finally prepeare the returned start state.
      if (retval == null)
      {
        throw new IllegalStateException("There was no valid pagination done.");
      }
      // reset the state, so that the datarow points to the first row of the tablemodel.
      if (retval.getEventCode() != ReportEvent.REPORT_INITIALIZED)
      {
        throw new IllegalStateException("There was no valid pagination done.");
      }
      return retval;
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
  public void processReport()
      throws ReportProcessingException
  {
    if (writer == null)
    {
      throw new IllegalStateException("No writer defined");
    }
    try
    {
      ProcessState state = repaginate();

      final XMLWriter w = (XMLWriter) state.getLayoutProcess().getOutputFunction();
      w.setWriter(getWriter());

      final boolean failOnError = ReportConfigurationUtil.isStrictErrorHandling
          (getReport().getReportConfiguration());
      final ReportProcessingErrorHandler errorHandler = new CollectingReportErrorHandler();
      state.setErrorHandler(errorHandler);
      while (!state.isFinish())
      {
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
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("StateCopy was not supported");
    }
    finally
    {
      activeDataFactory.close();
      activeDataFactory = null;
    }
  }

}
