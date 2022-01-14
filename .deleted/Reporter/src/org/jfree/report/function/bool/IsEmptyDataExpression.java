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
 * IsEmptyDataExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.bool;

import javax.swing.table.TableModel;

import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionRuntime;

/**
 * An expression that checks, whether the current report has a non-empty datasource. 
 *
 * @author Thomas Morgner
 */
public class IsEmptyDataExpression extends AbstractExpression
{
  /**
   * Default Constructor.
   */
  public IsEmptyDataExpression()
  {
  }

  /**
   * Checks, whether the report has a data-source and wether the datasource is empty. A datasource is considered
   * empty, if it contains no rows. The number of columns is not evaluated.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {

    final ExpressionRuntime runtime = getRuntime();
    if (runtime == null)
    {
      return null;
    }
    final TableModel data = runtime.getData();
    if (data == null)
    {
      return null;
    }
    if (data.getRowCount() == 0)
    {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
}
