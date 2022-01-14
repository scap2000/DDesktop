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
 * PageTotalFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.util.HashMap;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.states.ReportStateKey;

/**
 * Prints the total number of pages of an report. If a group is specified, this function expects the group to have the
 * manual pagebreak enabled.
 * <p/>
 * This function will only work as expected in group mode if the named group has pagebreak-before set to true on the
 * header for pagebreak-after set to true on the group's footer.
 *
 * @author Thomas Morgner
 */
public class PageTotalFunction extends PageFunction
{
  /**
   * A map of results, keyed by the process-key.
   */
  private HashMap groupPages;

  /**
   * The state key is used to store the result for the report.
   */
  private transient ReportStateKey currentStateKey;
  private boolean firstGroupSeen;

  /**
   * Creates a new page total function.
   */
  public PageTotalFunction()
  {
    this.groupPages = new HashMap();
    setDependencyLevel(0);
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    super.reportInitialized(event);
    if (event.isDeepTraversing())
    {
      return;
    }

    firstGroupSeen = false;
    currentStateKey = event.getState().getProcessKey();
  }

  public void groupStarted(final ReportEvent event)
  {
    super.groupStarted(event);
    if (event.isDeepTraversing())
    {
      return;
    }

    if (getGroup() == null)
    {
      return;
    }

    if (FunctionUtilities.isDefinedGroup(getGroup(), event))
    {
      if (firstGroupSeen == false)
      {
        firstGroupSeen = true;
        return;
      }
      
      currentStateKey = event.getState().getProcessKey();
    }
  }


  public void groupFinished(final ReportEvent event)
  {
    super.groupFinished(event);
    if (event.isDeepTraversing())
    {
      return;
    }

    if (getGroup() == null)
    {
      return;
    }

    if (FunctionUtilities.isDefinedGroup(getGroup(), event))
    {
      if (event.getState().isPrepareRun())
      {
        groupPages.put(currentStateKey, super.getValue());
      }
    }
  }


  public void pageFinished(final ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      groupPages.put(currentStateKey, super.getValue());
    }
  }

  public void reportDone(final ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      groupPages.put(currentStateKey, super.getValue());
    }
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any changeable objects with the
   * original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final PageTotalFunction function = (PageTotalFunction) super.getInstance();
    function.groupPages = new HashMap();
    return function;
  }

  public Object getValue()
  {
//    Log.debug ("Returning " + currentStateKey);
    return groupPages.get(currentStateKey);
  }

}
