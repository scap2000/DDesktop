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
 * StateUtilities.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.states;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.RootLevelBand;
import org.jfree.report.SubReport;
import org.jfree.report.function.Expression;
import org.jfree.report.function.StructureFunction;
import org.jfree.report.util.IntegerCache;

/**
 * Creation-Date: Dec 14, 2006, 7:59:39 PM
 *
 * @author Thomas Morgner
 */
public class StateUtilities
{
  /**
   * A comparator for levels in descending order.
   */
  private static final class DescendingComparator implements Comparator
  {
    /**
     * Default constructor.
     */
    private DescendingComparator ()
    {
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a
     * positive integer as the first argument is less than, equal to, or greater than the
     * second.<p>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is
     *         less than, equal to, or greater than the second.
     *
     * @throws ClassCastException if the arguments' types prevent them from being compared
     *                            by this Comparator.
     */
    public int compare (final Object o1, final Object o2)
    {
      if ((o1 instanceof Comparable) == false)
      {
        throw new ClassCastException("Need comparable Elements");
      }
      if ((o2 instanceof Comparable) == false)
      {
        throw new ClassCastException("Need comparable Elements");
      }
      final Comparable c1 = (Comparable) o1;
      final Comparable c2 = (Comparable) o2;
      return -1 * c1.compareTo(c2);
    }
  }

  private StateUtilities()
  {
  }

  public static int[] computeLevels(final JFreeReport report, final LayoutProcess lp)
  {
    final HashSet levels = new HashSet();
    // levels.add(IntegerCache.getInteger(0)); // A report without expressions needs no function evaluation run ..

    final StructureFunction[] collectionFunctions = lp.getCollectionFunctions();
    for (int i = 0; i < collectionFunctions.length; i++)
    {
      final StructureFunction function = collectionFunctions[i];
      levels.add(IntegerCache.getInteger(function.getDependencyLevel()));
    }
    levels.add(IntegerCache.getInteger(LayoutProcess.LEVEL_PAGINATE));

    collectLevels(report, levels);
    final Integer[] levelList = (Integer[]) levels.toArray(new Integer[levels.size()]);
    Arrays.sort(levelList, new DescendingComparator());
    final int[] retval = new int[levelList.length];
    for (int i = 0; i < retval.length; i++)
    {
      retval[i] = levelList[i].intValue();
    }
    return retval;
  }

  private static void collectLevels (final JFreeReport report, final HashSet levels)
  {
    final Expression[] expressions = report.getExpressions().getExpressions();

    for (int i = 0; i < expressions.length; i++)
    {
      final Expression expression = expressions[i];
      levels.add(IntegerCache.getInteger(expression.getDependencyLevel()));
    }

    collectLevels(report.getItemBand(), levels);
    collectLevels(report.getReportFooter(), levels);
    collectLevels(report.getReportHeader(), levels);

    final int groupCount = report.getGroupCount();
    for (int i = 0; i < groupCount; i++)
    {
      final Group g = report.getGroup(i);
      collectLevels(g.getFooter(), levels);
      collectLevels(g.getHeader(), levels);
    }
  }

  private static void collectLevels (final SubReport report, final HashSet levels)
  {
    final Expression[] expressions = report.getExpressions().getExpressions();

    for (int i = 0; i < expressions.length; i++)
    {
      final Expression expression = expressions[i];
      levels.add(IntegerCache.getInteger(expression.getDependencyLevel()));
    }

    collectLevels(report.getItemBand(), levels);
    collectLevels(report.getReportFooter(), levels);
    collectLevels(report.getReportHeader(), levels);

    final int groupCount = report.getGroupCount();
    for (int i = 0; i < groupCount; i++)
    {
      final Group g = report.getGroup(i);
      collectLevels(g.getFooter(), levels);
      collectLevels(g.getHeader(), levels);
    }
  }

  private static void collectLevels (final RootLevelBand rootLevelBand, final HashSet levels)
  {
    final int sc = rootLevelBand.getSubReportCount();
    for (int i = 0; i < sc; i++)
    {
      final SubReport sr = rootLevelBand.getSubReport(i);
      collectLevels(sr, levels);
    }
  }
}
