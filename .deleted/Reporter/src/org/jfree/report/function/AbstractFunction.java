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
 * AbstractFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import org.jfree.report.event.ReportEvent;

/**
 * Base class for implementing new report functions.  Provides empty implementations of
 * all the methods in the Function interface.
 * <p/>
 * The function is initialized when it gets added to the report. The method
 * <code>initialize</code> gets called to perform the required initializations. At this
 * point, all function properties must have been set to a valid state and the function
 * must be named. If the initialisation fails, a FunctionInitializeException is thrown and
 * the function get not added to the report.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractFunction extends AbstractExpression
        implements Function
{
  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  protected AbstractFunction ()
  {
  }

  /**
   * Creates an named function.
   *
   * @param name the name of the function.
   */
  protected AbstractFunction (final String name)
  {
    setName(name);
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The
   * event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized (final ReportEvent event)
  {
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportStarted (final ReportEvent event)
  {
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event the event.
   */
  public void reportFinished (final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event the event.
   */
  public void groupFinished (final ReportEvent event)
  {
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group of item bands is about to be processed. <P> The
   * next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted (final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group of item bands has been completed. <P> The itemBand
   * is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished (final ReportEvent event)
  {
  }
  
  /**
   * Receives notification that report generation has completed, the report footer was
   * printed, no more output is done. This is a helper event to shut down the output
   * service.
   *
   * @param event The event.
   */
  public void reportDone (final ReportEvent event)
  {
    // does nothing...
  }
}
