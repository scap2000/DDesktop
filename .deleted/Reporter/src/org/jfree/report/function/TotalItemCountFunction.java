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
 * TotalItemCountFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.HashMap;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.states.ReportStateKey;
import org.jfree.report.util.IntegerCache;

/**
 * A report function that counts the total number of items contained in groups in a
 * report. If no groupname is given, all items of the report are counted.
 * <p/>
 * Like all Total-Functions, this function produces a precomputed grand total. The function's result
 * is precomputed once and will not change later. Printing the result of this function in a group header
 * returns the same value as printed in the group-footer.
 * <p/>
 * The ItemCount can be used to produce a running row-count for a group or report.
 * <p/>
 * To count the number of groups in a report, use the TotalGroupCountFunction.
 *
 * @author Thomas Morgner
 */
public class TotalItemCountFunction extends AbstractFunction
{
  /**
   * A map of results, keyed by the process-key.
   */
  private transient HashMap results;

  /**
   * The name of the group on which to reset.
   */
  private String group;

  /**
   * The current row-count.
   */
  private transient Integer result;
  /**
   * The global state key is used to store the result for the whole report.
   */
  private transient ReportStateKey globalStateKey;
  /**
   * The current group key is used to store the result for the current group.
   */
  private transient ReportStateKey groupStateKey;

  /**
   * Default constructor.
   */
  public TotalItemCountFunction ()
  {
    results = new HashMap();
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    globalStateKey = event.getState().getProcessKey();
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      results.clear();
      result = IntegerCache.getInteger(0);
      results.put(globalStateKey, result);
    }
    else
    {
      result = (Integer) results.get(globalStateKey);
    }
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getGroup(), event) == false)
    {
      // wrong group ...
      return;
    }

    groupStateKey = event.getState().getProcessKey();
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      result = IntegerCache.getInteger(0);
      results.put(globalStateKey, result);
      results.put(groupStateKey, result);
    }
    else
    {
      // Activate the current group, which was filled in the prepare run.
      result = (Integer) results.get(groupStateKey);
    }
  }


  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event) == false)
    {
      return;
    }

    result = IntegerCache.getInteger(result.intValue() + 1);
    results.put(globalStateKey, result);
    if (groupStateKey != null)
    {
      results.put(groupStateKey, result);
    }
  }
  /**
   * Returns the name of the group to be totalled.
   *
   * @return the group name.
   */
  public String getGroup ()
  {
    return group;
  }

  /**
   * Defines the name of the group to be totalled. If the name is null, all groups are
   * totalled.
   *
   * @param group the group name.
   */
  public void setGroup (final String group)
  {
    this.group = group;
  }

  /**
   * Returns the number of items counted (so far) by the function.  This is either the
   * number of items in the report, or the group (if a group has been defined for the
   * function).
   *
   * @return The item count.
   */
  public Object getValue ()
  {
    return result;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final TotalItemCountFunction function = (TotalItemCountFunction) super.getInstance();
    function.results = new HashMap();
    return function;
  }

  /**
   * Helper function for the serialization.
   *
   * @param in the input stream.
   * @throws IOException if an IO error occured.
   * @throws ClassNotFoundException if a required class could not be found.
   */
  private void readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    results = new HashMap();
  }


}
