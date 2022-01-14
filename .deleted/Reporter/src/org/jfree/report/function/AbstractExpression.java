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
 * AbstractExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import java.io.Serializable;

import org.jfree.report.DataRow;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.util.Configuration;

/**
 * An abstract base class for implementing new report expressions.
 * <p/>
 * Expressions are stateless functions which have access to the report's {@link DataRow}. All expressions are named and
 * the defined names have to be unique within the report's expressions, functions and fields of the datasource.
 * Expressions are configured using properties.
 * <p/>
 *
 * @author Thomas Morgner
 */
public abstract class AbstractExpression implements Expression, Serializable
{
  /**
   * The expression name.
   */
  private String name;

  /**
   * The dependency level.
   */
  private int dependency;

  /**
   * The data row.
   */
  private transient ExpressionRuntime runtime;

  /**
   * A flag indicating whether the expression's result should be preserved in the datarow even when the expression
   * itself goes out of scope.
   */
  private boolean preserve;

  /**
   * Creates an unnamed expression. Make sure the name of the expression is set using {@link #setName} before the
   * expression is added to the report's expression collection.
   */
  protected AbstractExpression()
  {
    name = super.toString();
  }

  /**
   * Returns the name of the expression.
   *
   * @return the name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name of the expression. <P> The name should be unique among: <ul> <li>the functions and expressions for
   * the report; <li>the columns in the report's <code>TableModel</code>; </ul> This allows the expression to be
   * referenced by name from any report element.
   * <p/>
   * You should not change the name of an expression once it has been added to the report's expression collection.
   *
   * @param name the name (<code>null</code> not permitted).
   */
  public void setName(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException(
          "AbstractExpression.setName(...) : name is null.");
    }
    this.name = name;
  }

  /**
   * Returns <code>true</code> if this expression contains "auto-active" content and should be called by the system
   * regardless of whether this expression is referenced in the {@link DataRow}.
   *
   * @return true as all expressions are always evaluated now.
   * @deprecated The Active-Flag is no longer evaluated. We always assume it to be true.
   */
  public final boolean isActive()
  {
    return true;
  }

  /**
   * Defines, whether the expression is an active expression. This method has no effect anymore.
   *
   * @param active a flag.
   * @deprecated All expressions are always active. This method has no effect anymore.
   */
  public final void setActive(final boolean active)
  {
  }

  /**
   * Returns the dependency level for the expression (controls evaluation order for expressions and functions).
   *
   * @return the level.
   */
  public int getDependencyLevel()
  {
    return dependency;
  }

  /**
   * Sets the dependency level for the expression.
   * <p/>
   * The dependency level controls the order of evaluation for expressions and functions. Higher level expressions are
   * evaluated before lower level expressions.  Any level in the range 0 to Integer.MAX_VALUE is allowed. Negative
   * values are reserved for system functions (printing and layouting).
   *
   * @param level the level (must be greater than or equal to 0).
   */
  public void setDependencyLevel(final int level)
  {
    if (level < 0)
    {
      throw new IllegalArgumentException(
          "AbstractExpression.setDependencyLevel(...) : negative "
              + "dependency not allowed for user-defined expressions.");
    }
    this.dependency = level;
  }

  /**
   * Returns the current {@link DataRow}.
   *
   * @return the data row.
   */
  public DataRow getDataRow()
  {
    if (runtime == null)
    {
      throw new IllegalStateException("Runtime is null");
    }
    return runtime.getDataRow();
  }

  /**
   * Clones the expression.  The expression should be reinitialized after the cloning. <P> Expressions maintain no
   * state, cloning is done at the beginning of the report processing to disconnect the expression from any other object
   * space.
   *
   * @return a clone of this expression.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any changeable objects with the
   * original function. Only the datarow may be shared.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    try
    {
      final AbstractExpression abstractExpression = (AbstractExpression) clone();
      abstractExpression.setRuntime(null);
      return abstractExpression;
    }
    catch (CloneNotSupportedException cne)
    {
      throw new IllegalStateException("Clone must always be supported.");
    }
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    if (runtime == null)
    {
      throw new IllegalStateException("Runtime is null");
    }
    return runtime.getResourceBundleFactory();
  }

  public Configuration getReportConfiguration()
  {
    if (runtime == null)
    {
      throw new IllegalStateException("Runtime is null");
    }
    return runtime.getConfiguration();
  }

  /**
   * Defines the ExpressionRune used in this expression. The ExpressionRuntime is set before the expression
   * receives events or gets evaluated and is unset afterwards. Do not hold references on the runtime or you
   * will create memory-leaks.
   *
   * @param runtime the runtime information for the expression
   */
  public void setRuntime(final ExpressionRuntime runtime)
  {
    this.runtime = runtime;
  }

  /**
   * Returns the ExpressionRune used in this expression. The ExpressionRuntime is set before the expression
   * receives events or gets evaluated and is unset afterwards. Do not hold references on the runtime or you
   * will create memory-leaks.
   *
   * @return the runtime information for the expression
   */
  public ExpressionRuntime getRuntime()
  {
    return runtime;
  }

  /**
   * Checks, whether this expression is a deep-traversing expression. Deep-traversing expressions receive
   * events from all sub-reports. This returns false by default, as ordinary expressions have no need to be
   * deep-traversing.
   *
   * @return false.
   */
  public boolean isDeepTraversing()
  {
    return false;
  }

  /**
   * Checks, whether this expression's last value is preserved after the expression goes out of scope.
   *
   * @return true, if the expression's last value is preserved, false otherwise.
   */
  public boolean isPreserve()
  {
    return preserve;
  }

  /**
   * Defines, whether this expression's last value is preserved after the expression goes out of scope.
   *
   * @param preserve true, if the expression's last value is preserved, false otherwise.
   */
  public void setPreserve(final boolean preserve)
  {
    this.preserve = preserve;
  }
}
