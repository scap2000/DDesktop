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
 * EventMonitorFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.util.Log;

/**
 * A function that logs each event that it receives.  This function can be used for debugging purposes.
 *
 * @author Thomas Morgner
 */
public class EventMonitorFunction extends AbstractFunction implements PageEventListener
{
  /**
   * Counts the number of times the reportStarted(...) method is called.
   */
  private transient int reportStartCount = 0;
  /**
   * A flag indicating whether this expression will receive events from subreports.
   */
  private boolean deepTraversing;

  /**
   * Creates a new function.
   */
  public EventMonitorFunction()
  {
  }

  /**
   * Creates a new function.
   *
   * @param name the name of the function
   */
  public EventMonitorFunction(final String name)
  {
    setName(name);
  }


  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportStarted(final ReportEvent event)
  {
    Log.info("Report Started: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
    reportStartCount++;
    Log.info("Report Started Count: " + reportStartCount);
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event the event.
   */
  public void reportFinished(final ReportEvent event)
  {
    Log.info("Report Finished: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
  }

  /**
   * Receives notification that report generation has completed, the report footer was printed, no more output is done.
   * This is a helper event to shut down the output service.
   *
   * @param event The event.
   */
  public void reportDone(final ReportEvent event)
  {
    Log.info("Report Done: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));

  }

  /**
   * Receives notification that a page has started.
   *
   * @param event the event.
   */
  public void pageStarted(final ReportEvent event)
  {
    Log.info("Page Started: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
    Log.info("Page Started: " + event.getState().getCurrentPage());
  }

  /**
   * Receives notification that a page has ended.
   *
   * @param event the event.
   */
  public void pageFinished(final ReportEvent event)
  {
    Log.info("Page Finished: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
    Log.info("Page Finished: " + event.getState().getCurrentPage());
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    Log.info("Group Started: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
    Log.info("Group Started: " + event.getState().getCurrentGroupIndex());
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event the event.
   */
  public void groupFinished(final ReportEvent event)
  {
    Log.info("Group Finished: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
    Log.info("Group Finished: " + event.getState().getCurrentGroupIndex());
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    Log.info("Items Advanced: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   *
   * @param event the event.
   */
  public void itemsStarted(final ReportEvent event)
  {
    Log.info("Items Started: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
  }

  /**
   * Receives notification that a group of item bands has been completed.
   *
   * @param event the event.
   */
  public void itemsFinished(final ReportEvent event)
  {
    Log.info("Items Finished: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The event carries a
   * ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    Log.info("Report Initialized: Level = " + event.getState().getLevel()
        + " ItemCount: " + event.getState().getCurrentDataItem()
        + " Prepare Run: " + event.getState().isPrepareRun()
        + " Deep-Event " + ((event.getType() & ReportEvent.DEEP_TRAVERSING_EVENT) != 0));
  }

  /**
   * Returns <code>null</code> since this function is for generating log messages only.
   *
   * @return the value of the function (<code>null</code>).
   */
  public Object getValue()
  {
    return null;
  }

  /**
   * Returns whether this expression will receive events from subreports.
   *
   * @return true, if the function is deep-traversing, false otherwise.
   */
  public boolean isDeepTraversing()
  {
    return deepTraversing;
  }

  /**
   * Defines, whether this expression will receive events from subreports.
   *
   * @param deepTraversing true, if the function is deep-traversing, false otherwise.
   */
  public void setDeepTraversing(final boolean deepTraversing)
  {
    this.deepTraversing = deepTraversing;
  }
}
