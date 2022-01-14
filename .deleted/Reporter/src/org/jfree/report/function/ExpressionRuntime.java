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
 * ExpressionRuntime.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import javax.swing.table.TableModel;

import org.jfree.report.DataRow;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.util.Configuration;

/**
 * The expression runtime encapsulates all properties of the current report processing run that might be needed to
 * successfully evaluate an expression. The runtime grants access to the DataRow, the TableModel of the current report
 * and the ProcessingContext.
 *
 * @author Thomas Morgner
 */
public interface ExpressionRuntime
{
  /**
   * Returns the current data-row. The datarow can be used to access the computed values of all expressions and
   * functions and the current row in the tablemodel.
   *
   * @return the data-row.
   */
  public DataRow getDataRow();

  /**
   * Returns the report configuration that was used to initiate this processing run.
   *
   * @return the report configuration.
   */
  public Configuration getConfiguration();

  /**
   * Returns the resource-bundle factory of current processing context.
   *
   * @return the current resource-bundle factory.
   */
  public ResourceBundleFactory getResourceBundleFactory();

  /**
   * Grants access to the tablemodel was granted using report properties, now direct.
   *
   * @return the current tablemodel used in the report.
   */
  public TableModel getData();

  /**
   * Returns the number of the row in the tablemodel that is currently being processed.
   *
   * @return the current row number.
   */
  public int getCurrentRow();

  /**
     * Returns the current export descriptor as returned by the OutputProcessorMetaData object. The output descriptor is a
     * simple string collections consisting of the following components: exportclass/type/subtype
     * <p/>
     * For example, the PDF export would be: pageable/pdf and the StreamHTML export would return destTable/html/stream
     *
     * @return the export descriptor.
     */
  public String getExportDescriptor();

  /**
   * Returns the current processing context.
   *
   * @return the processing context.
   */
  public ProcessingContext getProcessingContext();
}
