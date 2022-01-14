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
 * FunctionStorage.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.states;

import java.util.HashMap;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.function.Expression;

/**
 * Creation-Date: Dec 15, 2006, 2:24:30 PM
 *
 * @author Thomas Morgner
 */
public class FunctionStorage
{
  private HashMap storage;

  public FunctionStorage()
  {
    storage = new HashMap();
  }

  /**
   * Stores expressions at the end of a run.
   *
   * @param key
   * @param expressions
   */
  public void store (final FunctionStorageKey key,
                     final Expression[] expressions,
                     final int length)
      throws ReportProcessingException
  {
//    Log.debug ("Store: " + key);
    try
    {
      final Expression[] copy = new Expression[length];
      for (int i = 0; i < length; i++)
      {
          copy[i] = (Expression) expressions[i].clone();
      }
      storage.put (key, copy);
    }
    catch (CloneNotSupportedException e)
    {
      throw new ReportProcessingException ("Storing expressions failed.");
    }
  }

  public Expression[] restore (final FunctionStorageKey key)
      throws ReportProcessingException
  {
    try
    {
      final Expression[] expressions = (Expression[]) storage.get(key);
      if (expressions == null)
      {
//        Log.debug ("Failed to Restore: " + key);
        return null;
      }

//      Log.debug ("Success Restore: " + key);
      final Expression[] copy = (Expression[]) expressions.clone();
      for (int i = 0; i < expressions.length; i++)
      {
          copy[i] = (Expression) expressions[i].clone();
      }
      return copy;
    }
    catch (CloneNotSupportedException e)
    {
      throw new ReportProcessingException ("Restoring expressions failed.");
    }
  }
}
