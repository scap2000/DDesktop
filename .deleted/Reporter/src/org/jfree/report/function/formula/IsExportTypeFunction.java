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
 * IsExportTypeFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function.formula;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.coretypes.LogicalType;
import org.jfree.report.function.ReportFormulaContext;

/**
 * Tests, whether a certain export type is currently used. This matches the given export type with the export type
 * that is specified by the output-target. The given export type can be a partial pattern, in which case this expression
 * tests, whether the given export type is a sub-type of the output-target's type.
 *
 * To test whether a destTable-export is used, specifiy the export type as "destTable" and it will match all destTable exports.
 *
 * @author Thomas Morgner
 */
public class IsExportTypeFunction implements Function
{
  /**
   * Default Constructor. Does nothing.
   */
  public IsExportTypeFunction()
  {
  }

  /**
   * Returns the Canonical Name of the function.
   *
   * @return the constant string "ISEXPORTTYPE"
   */
  public String getCanonicalName()
  {
    return "ISEXPORTTYPE";
  }

  /**
   * Return Boolean.TRUE, if the specified export type matches the used export type, Boolean.FALSE otherwise.
   *
   * @param context the formula context, which allows access to the runtime.
   * @param parameters the parameter callback is used to retrieve parameter values.
   * @return the computed result wrapped in a TypeValuePair.
   * @throws EvaluationException if an error occurs.
   */
  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters) throws EvaluationException
  {
    final ReportFormulaContext rfc = (ReportFormulaContext) context;
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    final Object value = parameters.getValue(0);
    if (value != null && rfc.getExportType().startsWith(String.valueOf(value)))
    {
      return new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
    }

    return new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);
  }
}
