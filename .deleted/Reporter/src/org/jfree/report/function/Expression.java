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
 * Expression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import org.jfree.report.DataRow;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.util.Configuration;

/**
 * An expression is a lightweight function that does not maintain a state. Expressions are used to calculate values
 * within a single row of a report. Expressions can use a dataRow to access other fields, expressions or functions
 * within the current row in the report.
 *
 * @author Thomas Morgner
 */
public interface Expression extends Cloneable
{
  /**
     * Returns the name of the expression.
     * <p/>
     * Every expression, function and destColumn in the datamodel within a report is required to have a unique name.
     *
     * @return the function name.
     */
  public String getName();

  /**
   * Sets the name of the expression.
   * <p/>
   * The name must not be null and must be unique within the expression group.
   *
   * @param name the name.
   */
  public void setName(String name);

  /**
   * Return the current expression value.
   * <p/>
   * The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue();

  /**
   * Returns true if this expression contains autoactive content and should be called by the system, regardless whether
   * this expression is referenced in the datarow.
   *
   * @return true, if the expression is activated automaticly, false otherwise.
   * @deprecated The Active-Flag is no longer evaluated. We always assume it to be true.
   */
  public boolean isActive();

  /**
   * Returns the DataRow used in this expression. The dataRow is set when the report processing starts and can be used
   * to access the values of functions, expressions and the reports datasource.
   *
   * @return the assigned DataRow for this report processing.
   */
  public DataRow getDataRow();

  /**
   * Clones the expression, expression should be reinitialized after the cloning.
   * <p/>
   * Expression maintain no state, cloning is done at the beginning of the report processing to disconnect the used
   * expression from any other object space.
   *
   * @return A clone of this expression.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone()
      throws CloneNotSupportedException;

  /**
   * The dependency level defines the level of execution for this function. Higher dependency functions are executed
   * before lower dependency functions. For ordinary functions and expressions, the range for dependencies is defined to
   * start from 0 (lowest dependency possible) to 2^31 (upper limit of int).
   * <p/>
   * Levels below 0 are reserved for system-functionality (printing and layouting).
   * <p/>
   * The level must not change during the report processing, or the result is invalid.
   *
   * @return the level.
   */
  public int getDependencyLevel();

  /**
   * Sets the dependency level for the expression.
   *
   * @param level the level.
   */
  public void setDependencyLevel(int level);

  /**
   * Return a new instance of this expression. The copy is initialized and uses the same parameters as the original, but
   * does not share any objects.
   *
   * @return a copy of this function.
   */
  public Expression getInstance();

  /**
   * Returns the resource-bundle factory of the report. This factory encapsulates the Locale and allows to create a
   * resource-bundle in a implementation-independent way.
   *
   * @return the resource-bundle factory.
   */
  public ResourceBundleFactory getResourceBundleFactory();

  /**
   * Returns the report's current configuration.
   *
   * @return the configuration of the report.
   */
  public Configuration getReportConfiguration();

  /**
   * Defines the DataRow used in this expression. The dataRow is set when the report processing starts and can be used
   * to access the values of functions, expressions and the reports datasource.
   *
   * @param runtime the runtime information for the expression
   */
  public void setRuntime(ExpressionRuntime runtime);

  /**
   * Retrieves the runtime instance.
   *
   * @return the runtime, can be null, if the expression is abused outside of the report processing.
   */
  public ExpressionRuntime getRuntime();

  /**
   * Checks, whether this expression is a deep-traversing expression. Deep-traversing expressions receive events from
   * all sub-reports.
   *
   * @return the deep-traversing flag.
   */
  public boolean isDeepTraversing();

  /**
   * Checks, whether this expression's last value is preserved after the expression goes out of scope.
   *
   * @return true, if the expression's last value is preserved, false otherwise.
   */
  public boolean isPreserve();
}
