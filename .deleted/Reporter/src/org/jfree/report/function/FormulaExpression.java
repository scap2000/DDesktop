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
 * FormulaExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import org.jfree.formula.Formula;
import org.jfree.formula.FormulaContext;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * The formula expression is used to evaluate a LibFormula/OpenFormula expression. There is generally no need to
 * reference this class directly, as this expression is used automatically if a formula is specified in a element,
 * style-expression or common expression.
 *
 * @author Thomas Morgner
 */
public final class FormulaExpression extends AbstractExpression
{
  /** A cached version of the compiled formula. */
  private transient Formula compiledFormula;
  /** The formula namespace as defined by OpenFormula. */
  private String formulaNamespace;
  /** The formula itself. */
  private String formulaExpression;
  /** The formula as specified by the user. This is the formula and the namespace.*/ 
  private String formula;
  /** A flag indicating that the formula cannot be parsed. */
  private boolean formulaError;

  /**
   * Default Constructor.
   */
  public FormulaExpression()
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
   * Returns the formula (incuding the optional namespace) as defined by the OpenFormula standard.  
   *
   * @return the formula as text.
   */
  public String getFormula()
  {
    return formula;
  }

  /**
   * Returns the formula namespace. If the formula specified by the user starts with "=", then the namespace
   * "report" is assumed.
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
    else if (formula.startsWith("="))
    {
      formulaNamespace = "report";
      formulaExpression = formula.substring(1);
    }
    else
    {
      final int separator = formula.indexOf(':');
      if (separator <= 0 || ((separator + 1) == formula.length()))
      {
        // error: invalid formula.
        formulaNamespace = null;
        formulaExpression = null;
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

    if (formulaExpression == null)
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
          new ReportFormulaContext(getFormulaContext(), getDataRow(),
              getRuntime().getExportDescriptor());
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
      final Configuration config = getReportConfiguration();
      if (Log.isDebugEnabled())
      {
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
   * Return the computed value of the formula.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    try
    {
      return computeRegularValue();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * Clones the expression.
   *
   * @return A clone of this expression.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final FormulaExpression o = (FormulaExpression) super.clone();
    if (compiledFormula != null)
    {
      o.compiledFormula = (Formula) compiledFormula.clone();
    }
    return o;
  }
}
