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
 * StyleExpressionsEvaluator.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function.sys;

import java.util.Iterator;
import java.util.Map;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.function.AbstractElementFormatFunction;
import org.jfree.report.function.Expression;
import org.jfree.report.function.StructureFunction;
import org.jfree.report.states.LayoutProcess;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.util.beans.ConverterRegistry;

/**
 * Evaluates style-expressions and updates the stylesheet.
 * This is an internal helper function. It is not meant to be used by end-users and manually adding this function to a
 * report will cause funny side-effects.
 *
 * @author Thomas Morgner
 */
public class StyleExpressionsEvaluator extends AbstractElementFormatFunction
    implements StructureFunction
{
  /**
   * Default Constructor.
   */
  public StyleExpressionsEvaluator()
  {
  }

  /**
   * Evaluates all style expressions from all elements and updates the style-sheet if needed.
   *
   * @param b the band.
   */
  protected void processRootBand(final Band b)
  {
    evaluateElement(b);
    final Element[] elementArray = b.getElementArray();
    for (int i = 0; i < elementArray.length; i++)
    {
      final Element element = elementArray[i];
      if (element instanceof Band)
      {
        processRootBand((Band) element);
      }
      else
      {
        evaluateElement(element);
      }
    }
  }

  /**
   * Evaluates all defined style-expressions of the given element.
   *
   * @param e the element that should be updated.
   */
  protected void evaluateElement (final Element e)
  {
    final Map styleExpressions = e.getStyleExpressions();
    if (styleExpressions.isEmpty())
    {
      return;
    }

    final ElementStyleSheet style = e.getStyle();
    final Iterator entries = styleExpressions.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final StyleKey key = (StyleKey) entry.getKey();
      final Expression ex = (Expression) entry.getValue();
      if (ex == null)
      {
        continue;
      }

      ex.setRuntime(getRuntime());
      try
      {
        final Object value = ex.getValue();
        //Log.debug ("Setting style " + key + " " + value);
        if (key.getValueType().isInstance(value))
        {
          style.setStyleProperty(key, value);
        }
        else if (value != null)
        {
          // try to convert it ..
          final Object o = ConverterRegistry.toPropertyValue
              (String.valueOf(value), key.getValueType());
          style.setStyleProperty(key, o);
        }
      }
      catch(Exception exception)
      {
        // ignored ..
      }
      finally
      {
        ex.setRuntime(null);
      }
    }
  }

  /**
   * Returns the dependency level for the expression (controls evaluation order
   * for expressions and functions).
   *
   * @return the level.
   */
  public int getDependencyLevel()
  {
    return LayoutProcess.LEVEL_PAGINATE;
  }
}
