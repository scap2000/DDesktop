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
 * ShowElementIfDataAvailableExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import javax.swing.table.TableModel;

import org.jfree.report.Band;
import org.jfree.report.Element;

/**
 * This functions checks the tablemodel and hides the named elements, if there
 * is no data available.
 *
 * @author Thomas Morgner
 */
public class ShowElementIfDataAvailableExpression
        extends AbstractElementFormatFunction
{
  /**
   * Default Constructor.
   */
  public ShowElementIfDataAvailableExpression()
  {
  }

  /**
   * Applies the computed visiblity to all elements with the name defined in this expression.
   * @param b the band.
   */
  protected void processRootBand(final Band b)
  {
    final boolean visible = isDataAvailable();
    final Element[] elements = FunctionUtilities.findAllElements(b, getElement());
    for (int i = 0; i < elements.length; i++)
    {
      final Element element = elements[i];
      element.setVisible(visible);
    }
  }

  /**
   * Computes the visibility state.
   *
   * @return true, if there is data available, false otherwise.
   */
  private boolean isDataAvailable()
  {
    final ExpressionRuntime runtime = getRuntime();
    if (runtime == null)
    {
      return false;
    }
    final TableModel data = runtime.getData();
    if (data == null)
    {
      return false;
    }
    if (data.getRowCount() == 0)
    {
      return false;
    }
    return true;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    if (isDataAvailable())
    {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
}
