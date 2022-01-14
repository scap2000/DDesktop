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
 * TotalItemMaxFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.HashMap;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.states.ReportStateKey;
import org.jfree.util.Log;

/**
 * A report function that pre-computes the largest item in a group. The Items must be mutually comparable
 * among each other or the function will fail. Comparing dates with strings will not work.
 * <p/>
 * Like all Total-Functions, this function produces a precomputed grand total. The function's result
 * is precomputed once and will not change later. Printing the result of this function in a group header
 * returns the same value as printed in the group-footer.
 * <p/>
 * A group can be defined using the property "group". If the group property is not set, the function will process the
 * whole report.
 *
 * @author Thomas Morgner
 */
public class TotalItemMaxFunction extends AbstractFunction
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
   * The field that should be evaluated.
   */
  private String field;
  /**
   * The currently computed maximum value.
   */
  private transient Comparable max;
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
  public TotalItemMaxFunction ()
  {
    results = new HashMap();
  }
  /**
     * Returns the field used by the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @return The field name.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Sets the field name for the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @param field the field name.
     */
  public void setField(final String field)
  {
    this.field = field;
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
      this.max = null;
      results.put(globalStateKey, null);
    }
    else
    {
      this.max = (Comparable) results.get(globalStateKey);
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
      this.max = null;
      results.put(globalStateKey, max);
      results.put(groupStateKey, max);
    }
    else
    {
      // Activate the current group, which was filled in the prepare run.
      max = (Integer) results.get(groupStateKey);
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

    final Object fieldValue = event.getDataRow().get(getField());
    if (fieldValue instanceof Comparable == false)
    {
      return;
    }
    try
    {
      final Comparable compare = (Comparable) fieldValue;
      if (max == null)
      {
        max = compare;
      }
      else if (max.compareTo(compare) < 0)
      {
        max = compare;
      }
    }
    catch (Exception e)
    {
      Log.error("TotalItemMaxFunction.advanceItems(): problem comparing values.");
    }

    results.put(globalStateKey, max);
    if (groupStateKey != null)
    {
      results.put(groupStateKey, max);
    }
  }

  /**
   * Returns the name of the group for which the minimum should be computed.
   *
   * @return the group name.
   */
  public String getGroup ()
  {
    return group;
  }

  /**
   * Defines the name of the group to be totalled. If the name is null, the minimum for the whole report is computed.
   *
   * @param group the group name.
   */
  public void setGroup (final String group)
  {
    this.group = group;
  }

  /**
   * Returns the computed maximum value.
   *
   * @return The computed maximum value.
   */
  public Object getValue ()
  {
    return max;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final TotalItemMaxFunction function = (TotalItemMaxFunction) super.getInstance();
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
