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
 * TotalGroupSumFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.math.BigDecimal;

import java.util.HashMap;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.states.ReportStateKey;
import org.jfree.util.Log;

/**
 * A report function that calculates the sum of one field (destColumn) from the Data-Row. This function produces a global
 * total. The total sum of the group is known when the group processing starts and the report is not performing a
 * prepare-run. The sum is calculated in the prepare run and recalled in the printing run.
 * <p/>
 * The function can be used in two ways: <ul> <li>to calculate a sum for the entire report;</li> <li>to calculate a sum
 * within a particular group;</li> </ul> This function expects its input values to be either java.lang.Number instances
 * or Strings that can be parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p/>
 * The function undestands two parameters, the <code>field</code> parameter is required and denotes the name of an
 * ItemBand-field which gets summed up.
 * <p/>
 * The parameter <code>group</code> denotes the name of a group. When this group is started, the counter gets reseted to
 * null. This parameter is optional.
 *
 * @author Thomas Morgner
 */
public class TotalGroupSumFunction extends AbstractFunction
{
  /**
   * A map of results, keyed by the process-key.
   */
  private transient HashMap results;

  /**
   * The field that should be evaluated.
   */
  private String field;
  /**
   * The name of the group on which to reset.
   */
  private String group;

  /**
   * The currently computed result.
   */
  private transient BigDecimal result;
  /**
   * The global state key is used to store the result for the whole report.
   */
  private transient ReportStateKey globalStateKey;
  /**
   * The current group key is used to store the result for the current group.
   */
  private transient ReportStateKey groupStateKey;

  /**
   * Constructs a new function. <P> Initially the function has no name...be sure to assign one before using the
   * function.
   */
  public TotalGroupSumFunction()
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
      result = new BigDecimal(0);
      results.put(globalStateKey, result);
    }
    else
    {
      result = (BigDecimal) results.get(globalStateKey);
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
      result = new BigDecimal(0);
      results.put(globalStateKey, result);
      results.put(groupStateKey, result);
    }
    else
    {
      // Activate the current group, which was filled in the prepare run.
      result = (BigDecimal) results.get(groupStateKey);
    }
  }


  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    if (field == null)
    {
      return;
    }

    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event) == false)
    {
      return;
    }

    final Object fieldValue = event.getDataRow().get(getField());
    try
    {
      final Number n = (Number) fieldValue;
      if (n instanceof BigDecimal)
      {
        final BigDecimal bd = (BigDecimal) n;
        result = result.add(bd);
        results.put(globalStateKey, result);
        if (groupStateKey != null)
        {
          results.put(groupStateKey, result);
        }
      }
      else if (n instanceof Number)
      {
        result = result.add(new BigDecimal(String.valueOf(n)));
        results.put(globalStateKey, result);
        if (groupStateKey != null)
        {
          results.put(groupStateKey, result);
        }
      }
    }
    catch (Exception e)
    {
      Log.error("TotalGroupSumFunction.itemsAdvanced(): problem adding number." + fieldValue, e);
    }
  }

  /**
   * Returns the name of the group to be totalled.
   *
   * @return the group name.
   */
  public String getGroup()
  {
    return group;
  }

  /**
   * Defines the name of the group to be totalled. If the name is null, all groups are totalled.
   *
   * @param group the group name.
   */
  public void setGroup(final String group)
  {
    this.group = group;
  }

  /**
   * Return the current function value. <P> The value depends (obviously) on the function implementation.   For example,
   * a page counting function will return the current page number.
   *
   * @return The value of the function.
   */
  public Object getValue()
  {
    return result;
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
   * Return a completly separated copy of this function. The copy does no longer share any changeable objects with the
   * original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final TotalGroupSumFunction function = (TotalGroupSumFunction) super.getInstance();
    function.result = null;
    function.results = new HashMap();
    return function;
  }

  /**
   * Helper function for the serialization.
   *
   * @param in the input stream.
   * @throws IOException            if an IO error occured.
   * @throws ClassNotFoundException if a required class could not be found.
   */
  private void readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    this.results = new HashMap();
    this.result = null;
  }
}
