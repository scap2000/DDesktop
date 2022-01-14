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
 * IsExportTypeFunctionDescription.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function.formula;

import org.jfree.formula.function.AbstractFunctionDescription;
import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.function.information.InformationFunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.LogicalType;
import org.jfree.formula.typing.coretypes.TextType;

/**
 * The function-description class for the IsExportTypeFunction. This class holds meta-data for the formula function.
 *
 * @author Thomas Morgner
 */
public class IsExportTypeFunctionDescription extends AbstractFunctionDescription
{
  /**
   * Default Constructor.
   */
  public IsExportTypeFunctionDescription()
  {
    super("org.jfree.report.function.formula.IsExportType-Function");
  }

  /**
   * Returns the expected value type. This function returns a LogicalType.
   * @return LogicalType.TYPE
   */
  public Type getValueType()
  {
    return LogicalType.TYPE;
  }

  /**
   * Returns the number of parameters expected by the function.
   * @return 1.
   */
  public int getParameterCount()
  {
    return 1;
  }

  /**
   * Returns the parameter type of the function parameters.
   *
   * @param position the parameter index.
   * @return always TextType.TYPE.
   */
  public Type getParameterType(final int position)
  {
    return TextType.TYPE;
  }

  /**
   * Defines, whether the parameter at the given position is mandatory. A
   * mandatory parameter must be filled in, while optional parameters need not
   * to be filled in.
   *
   * @param position the position of the parameter.
   * @return true, as all parameters are mandatory.
   */
  public boolean isParameterMandatory(final int position)
  {
    return true;
  }

  /**
   * Returns the function category. The function category groups functions by their expected use.
   *
   * @return InformationFunctionCategory.CATEGORY.
   */
  public FunctionCategory getCategory()
  {
    return InformationFunctionCategory.CATEGORY;
  }
}
