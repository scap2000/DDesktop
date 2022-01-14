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
 * TotalGroupCountFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.HashMap;

import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.states.ReportStateKey;
import org.jfree.report.util.IntegerCache;

/**
 * A report function that counts the total of groups in a report. If a null-groupname is given, all groups are counted.
 * <p/>
 * A group can be defined using the property "group". If the group property is not set, all group starts get counted.
 *
 * @author Thomas Morgner
 */
public class TotalGroupCountFunction extends GroupCountFunction
{
  /**
   * A map of results, keyed by the process-key.
   */
  private transient HashMap results;
  /**
   * The currently computed result.
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
  public TotalGroupCountFunction()
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
    super.reportInitialized(event);
    globalStateKey = event.getState().getProcessKey();
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      results.clear();
      result = IntegerCache.getInteger(getCount());
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
    super.groupStarted(event);

    final Group group = FunctionUtilities.getCurrentGroup(event);

    if (group.getName().equals(getParentGroup()))
    {
      groupStateKey = event.getState().getProcessKey();
      if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
      {
        result = IntegerCache.getInteger(getCount());
        results.put(globalStateKey, result);
        results.put(groupStateKey, result);
        return;
      }
      else
      {
        // Activate the current group, which was filled in the prepare run.
        result = (Integer) results.get(groupStateKey);
      }
    }

    final String definedGroupName = getGroup();
    if (definedGroupName == null || group.getName().equals(definedGroupName))
    {
      // count all groups...
      if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
      {
        result = IntegerCache.getInteger(getCount());
        results.put(globalStateKey, result);
        results.put(groupStateKey, result);
      }
    }
  }

  /**
   * Returns the computed value.
   *
   * @return the computed value.
   */
  public Object getValue()
  {
    return result;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any changeable objects with the
   * original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final TotalGroupCountFunction fn =
        (TotalGroupCountFunction) super.getInstance();
    fn.results = new HashMap();
    return fn;
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
  }

}
