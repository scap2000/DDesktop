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
 * ReportFormulaContext.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import org.jfree.formula.ContextEvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.FunctionRegistry;
import org.jfree.formula.operators.OperatorFactory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.report.DataRow;
import org.jfree.util.Configuration;

/**
 * The report formula context is a FormulaContext implementation that connects the formula evaluator with the current
 * data-row of the report process.
 * <p/>
 * This is an internal class used by the FormulaExpression and FormulaFunction. It has no sensible usages outside of
 * that scope.
 *
 * @author Thomas Morgner
 */
public class ReportFormulaContext implements FormulaContext
{
  /**
   * The formula context provided from the LibFormula implementation.
   */
  private FormulaContext backend;
  /**
   * The data-row from the report.
   */
  private DataRow dataRow;
  /**
   * The export-type as returned by the output-processor.
   */
  private String exportType;

  /**
   * Creates a new ReportFormulaContext using the given FormulaContext as backend. All data is read from
   * the data-row.
   *
   * @param backend the formula-context backend.
   * @param dataRow the current data-row.
   * @param exportType the export type string as reported by the OutputProcessorMetaData.
   */
  public ReportFormulaContext(final FormulaContext backend,
                              final DataRow dataRow,
                              final String exportType)
  {
    if (exportType == null)
    {
      throw new NullPointerException("ExportType is null.");
    }
    if (backend == null)
    {
      throw new NullPointerException("Backend-FormulaContext is null");
    }
    if (dataRow == null)
    {
      throw new NullPointerException("Data-Row is null");
    }
    this.exportType = exportType;
    this.backend = backend;
    this.dataRow = dataRow;
  }

  /**
     * Returns the localization context of this formula. The localization context can be used to labelQuery locale specific
     * configuration settings.
     *
     * @return the localization context.
     */
  public LocalizationContext getLocalizationContext()
  {
    return backend.getLocalizationContext();
  }

  /**
   * Returns the local configuration of the formula.
   *
   * @return the local configuration.
   */
  public Configuration getConfiguration()
  {
    return backend.getConfiguration();
  }

  /**
   * Returns the function registry. The function registry grants access to all formula-function implementations.
   *
   * @return the function registry.
   */
  public FunctionRegistry getFunctionRegistry()
  {
    return backend.getFunctionRegistry();
  }

  /**
   * Returns the type registry. The type registry contains all type information and allows to convert values between
   * different types.
   *
   * @return the function registry.
   */
  public TypeRegistry getTypeRegistry()
  {
    return backend.getTypeRegistry();
  }

  /**
   * Returns the operator registry. The Operator-registry contains all operator-implementations.
   *
   * @return the operator registry.
   */
  public OperatorFactory getOperatorFactory()
  {
    return backend.getOperatorFactory();
  }

  /**
   * Checks, whether the external object referenced by <code>name</code> has changed. This forwards the call
   * to the data-row and checks, whether the value has changed since the last call to advance().
   *
   * @param name the name that identifies the reference.
   * @return true, if the reference has changed, false otherwise.
   * @throws ContextEvaluationException if an error occurs.
   */
  public boolean isReferenceDirty(final Object name) throws ContextEvaluationException
  {
    return dataRow.isChanged((String) name);
  }

  /**
   * Resolves the given reference. How the name is interpreted by the outside system is an implementation detail.
   * This method always returns AnyType, as we do not interpret the values returned from the data-row.
   *
   * @param name the name that identifies the reference.
   * @return the resolved object.
   */
  public Type resolveReferenceType(final Object name)
  {
    return AnyType.TYPE;
  }

  /**
   * Queries the type of the given reference. How the name is interpreted by the outside system is an implementation
   * detail. This return a LibFormula type object matching the type of the object that would be returned by
   * resolveReference.
   *
   * @param name the name that identifies the reference.
   * @return the type of the resolved object.
   * @throws ContextEvaluationException if an error occurs.
   */
  public Object resolveReference(final Object name) throws ContextEvaluationException
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    return dataRow.get(String.valueOf(name));
  }

  /**
   * Returns the current data-row.
   *
   * @return the current datarow.
   */
  public DataRow getDataRow()
  {
    return dataRow;
  }

  /**
   * (Re-)Defines the current data-row.
   *
   * @param dataRow the current datarow.
   */
  public void setDataRow(final DataRow dataRow)
  {
    this.dataRow = dataRow;
  }

  /**
   * Return the export type of the current report processing run.
   *
   * @return the current export type.
   */
  public String getExportType()
  {
    return exportType;
  }
}
