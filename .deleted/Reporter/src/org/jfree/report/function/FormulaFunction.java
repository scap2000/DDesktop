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
 * FormulaFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import org.jfree.formula.Formula;
import org.jfree.formula.FormulaContext;
import org.jfree.report.event.ReportEvent;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * The formula function is a stateful version of the FormulaExpression and is used to evaluate a LibFormula/OpenFormula
 * expression. This function can be used if a stateful evaluation is needed or if the formula value should be
 * initialized to a certain value before the real evaluation starts.
 *
 * @author Thomas Morgner
 */
public final class FormulaFunction extends AbstractFunction
{
  /**
   * A cached version of the compiled formula.
   */
  private transient Formula compiledFormula;
  /**
   * The formula namespace as defined by OpenFormula.
   */
  private String formulaNamespace;
  /**
   * The formula itself.
   */
  private String formulaExpression;
  /**
   * The formula as specified by the user. This is the formula and the namespace.
   */
  private String formula;

  /**
   * The formula namespace of the initial formula as defined by OpenFormula.
   */
  private String initialNamespace;
  /**
   * The initial formula itself.
   */
  private String initialExpression;
  /**
   * The initial formula as specified by the user. This is the formula and the namespace.
   */
  private String initial;
  /**
   * A flag indicating whether the initial-formula has been evaluated.
   */
  private boolean initialized;
  /**
   * A flag indicating that the formula cannot be parsed.
   */
  private boolean formulaError;

  /**
   * Default Constructor.
   */
  public FormulaFunction()
  {
  }

  /**
   * Returns the defined formula context from the report processing context.
   *
   * @return the formula context.
   */
  private FormulaContext getFormulaContext()
  {
    final ProcessingContext globalContext = getRuntime().getProcessingContext();
    return globalContext.getFormulaContext();
  }

  /**
   * Returns the initial formula (incuding the optional namespace) as defined by the OpenFormula standard.
   *
   * @return the formula as text.
   */
  public String getInitial()
  {
    return initial;
  }

  /**
   * Returns the initial formula expression. The initial formula is used at the first call only.
   *
   * @return the initial formula expression.
   */
  public String getInitialExpression()
  {
    return initialExpression;
  }

  /**
   * Returns the formula namespace if the initial formula. If the formula specified by the user starts with "=", then
   * the namespace "report" is assumed.
   *
   * @return the namespace of the formula.
   */
  public String getInitialNamespace()
  {
    return initialNamespace;
  }

  /**
   * Defines the initial formula (incuding the optional namespace) as defined by the OpenFormula standard. The initial
   * formula is used at the first call only.
   *
   * @param initial the initial formula as text.
   */
  public void setInitial(final String initial)
  {
    this.initial = initial;
    if (initial == null)
    {
      initialNamespace = null;
      initialExpression = null;
    }
    else
    {
      final int separator = initial.indexOf(':');
      if (separator <= 0 || ((separator + 1) == initial.length()))
      {
        if (formula.startsWith("="))
        {
          initialNamespace = "report";
          initialExpression = initial.substring(1);
        }
        else
        {
          // error: invalid formula.
          initialNamespace = null;
          initialExpression = null;
        }
      }
      else
      {
        initialNamespace = initial.substring(0, separator);
        initialExpression = initial.substring(separator + 1);
      }
    }
  }

  /**
   * Resets the function state.
   *
   * @param event the report event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    initialized = false;
  }

  /**
   * Returns the formula (incuding the optional namespace) as defined by the OpenFormula standard.
   *
   * @return the formula as text.
   */
  public String getFormula()
  {
    return formula;
  }

  /**
   * Returns the formula namespace. If the formula specified by the user starts with "=", then the namespace "report" is
   * assumed.
   *
   * @return the namespace of the formula.
   */
  public String getFormulaNamespace()
  {
    return formulaNamespace;
  }

  /**
   * Returns the formula expression.
   *
   * @return the formula expression.
   */
  public String getFormulaExpression()
  {
    return formulaExpression;
  }

  /**
   * Defines the formula (incuding the optional namespace) as defined by the OpenFormula standard.
   *
   * @param formula the formula as text.
   */
  public void setFormula(final String formula)
  {
    this.formula = formula;
    if (formula == null)
    {
      formulaNamespace = null;
      formulaExpression = null;
    }
    else
    {
      final int separator = formula.indexOf(':');
      if (separator <= 0 || ((separator + 1) == formula.length()))
      {
        if (formula.startsWith("="))
        {
          formulaNamespace = "report";
          formulaExpression = formula.substring(1);
        }
        else
        {
          // error: invalid formula.
          formulaNamespace = null;
          formulaExpression = null;
        }
      }
      else
      {
        formulaNamespace = formula.substring(0, separator);
        formulaExpression = formula.substring(separator + 1);
      }
    }
    this.compiledFormula = null;
    this.formulaError = false;
  }


  /**
   * Computes the value of the formula by evaluating the initial formula against the current data-row.
   *
   * @return the computed value or null, if an error occured.
   */
  private Object computeInitialValue()
  {
    try
    {
      if (initial != null)
      {
        final Formula initFormula = new Formula(initialExpression);
        final ReportFormulaContext context =
            new ReportFormulaContext(getFormulaContext(), getDataRow(), getRuntime().getExportDescriptor());
        try
        {
          initFormula.initialize(context);
          return initFormula.evaluate();
        }
        finally
        {
          context.setDataRow(null);
        }
      }

      // if the code above did not trigger, compute a regular thing ..
      return computeRegularValue();
    }
    catch (Exception e)
    {
      if (Log.isDebugEnabled())
      {
        final Configuration config = getReportConfiguration();
        if ("true".equals(config.getConfigProperty("org.jfree.report.function.LogFormulaFailureCause")))
        {
          Log.debug("Failed to compute the initial value [" + formulaExpression + ']', e);
        }
        else
        {
          Log.debug("Failed to compute the initial value [" + formulaExpression + ']');
        }
      }
      return null;
    }
  }

  /**
   * Computes the value of the formula by evaluating the formula against the current data-row.
   *
   * @return the computed value or null, if an error occured.
   */
  private Object computeRegularValue()
  {
    if (formulaError)
    {
      return null;
    }

    try
    {
      if (compiledFormula == null)
      {
        compiledFormula = new Formula(formulaExpression);
      }

      final ReportFormulaContext context =
          new ReportFormulaContext(getFormulaContext(), getDataRow(), getRuntime().getExportDescriptor());
      try
      {
        compiledFormula.initialize(context);
        return compiledFormula.evaluate();
      }
      finally
      {
        context.setDataRow(null);
      }
    }
    catch (Exception e)
    {
      formulaError = true;
      if (Log.isDebugEnabled())
      {
        final Configuration config = getReportConfiguration();
        if ("true".equals(config.getConfigProperty("org.jfree.report.function.LogFormulaFailureCause")))
        {
          Log.debug("Failed to compute the regular value [" + formulaExpression + ']', e);
        }
        else
        {
          Log.debug("Failed to compute the regular value [" + formulaExpression + ']');
        }
      }
      return null;
    }
  }


  /**
   * Return the computed value of the formula. The first call will return the initial-value instead.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    try
    {
      if (initialized == false)
      {
        initialized = true;
        return computeInitialValue();
      }
      return computeRegularValue();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * Clones the expression, expression should be reinitialized after the cloning. <P> Expression maintain no state,
   * cloning is done at the beginning of the report processing to disconnect the used expression from any other object
   * space.
   *
   * @return A clone of this expression.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final FormulaFunction o = (FormulaFunction) super.clone();
    if (compiledFormula != null)
    {
      o.compiledFormula = (Formula) compiledFormula.clone();
    }
    return o;
  }
}
