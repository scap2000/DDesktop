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
 * IsExportTypeExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.sys;

import org.jfree.report.function.AbstractExpression;

/**
 * Tests, whether a certain export type is currently used. This matches the given export type with the export type
 * that is specified by the output-target. The given export type can be a partial pattern, in which case this expression
 * tests, whether the given export type is a sub-type of the output-target's type.
 *
 * To test whether a destTable-export is used, specifiy the export type as "destTable" and it will match all destTable exports.
 *
 * @author Thomas Morgner
 */
public class IsExportTypeExpression extends AbstractExpression
{
  /**
   * The export type for which to test for.
   */
  private String exportType;

  /**
   * Default constructor.
   */
  public IsExportTypeExpression()
  {
  }

  /**
   * Returns the export type string.
   *
   * @return the export type string.
   */
  public String getExportType()
  {
    return exportType;
  }

  /**
   * Defines the export type.
   *
   * @param exportType the export type.
   */
  public void setExportType(final String exportType)
  {
    this.exportType = exportType;
  }

  /**
   * Return Boolean.TRUE, if the specified export type matches the used export type, Boolean.FALSE otherwise.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    if (exportType == null)
    {
      return Boolean.FALSE;
    }
    if (getRuntime().getExportDescriptor().startsWith(exportType))
    {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
}
