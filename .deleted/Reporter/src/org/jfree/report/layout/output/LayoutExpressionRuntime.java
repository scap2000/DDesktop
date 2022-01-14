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
 * LayoutExpressionRuntime.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import javax.swing.table.TableModel;

import org.jfree.report.DataRow;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.function.ProcessingContext;
import org.jfree.util.Configuration;

/**
 * Creation-Date: Jan 22, 2007, 12:09:32 PM
 *
 * @author Thomas Morgner
 */
public class LayoutExpressionRuntime implements ExpressionRuntime
{
  private DataRow dataRow;
  private int currentRow;
  private TableModel data;
  private ProcessingContext processingContext;

  public LayoutExpressionRuntime(final DataRow dataRow,
                                 final int currentRow,
                                 final TableModel data,
                                 final ProcessingContext processingContext)
  {
    this.processingContext = processingContext;
    this.dataRow = dataRow;
    this.currentRow = currentRow;
    this.data = data;
  }

  public ProcessingContext getProcessingContext()
  {
    return processingContext;
  }

  public Configuration getConfiguration()
  {
    return getProcessingContext().getConfiguration();
  }

  public DataRow getDataRow()
  {
    return dataRow;
  }

  /**
   * Access to the tablemodel was granted using report properties, now direct.
   */
  public TableModel getData()
  {
    return data;
  }

  /**
   * Where are we in the current processing.
   */
  public int getCurrentRow()
  {
    return currentRow;
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    return getProcessingContext().getResourceBundleFactory();
  }

  /**
     * The output descriptor is a simple string collections consisting of the
     * following components: exportclass/type/subtype
     * <p/>
     * For example, the PDF export would be: pageable/pdf The StreamHTML export
     * would return destTable/html/stream
     *
     * @return the export descriptor.
     */
  public String getExportDescriptor()
  {
    return getProcessingContext().getExportDescriptor();
  }
}
