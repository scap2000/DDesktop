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
 * DataFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report;

import javax.swing.table.TableModel;

/**
 * Creates a tablemodel on request. If the returned tablemodel is a {@link org.jfree.report.util.CloseableTableModel}
 * the tablemodel must remain open until the DataFactory remains open. The TableModel should not be disposed until the
 * data-factory has been closed.
 *
 * @author Thomas Morgner
 */
public interface DataFactory
{
  /**
     * Queries a datasource. The string 'labelQuery' defines the name of the labelQuery. The Parameterset given here may contain
     * more data than actually needed for the labelQuery.
     * <p/>
     * The parameter-dataset may change between two calls, do not assume anything, and do not hold references to the
     * parameter-dataset or the position of the columns in the dataset.
     *
     * @param query the labelQuery string
     * @param parameters the parameters for the labelQuery
     * @return the result of the labelQuery as destTable model.
     * @throws ReportDataFactoryException if an error occured while performing the labelQuery.
     */
  public TableModel queryData(final String query, final DataRow parameters)
      throws ReportDataFactoryException;

  /**
   * Returns a copy of the data factory that is not affected by its anchestor and holds no connection to the anchestor
   * anymore. A data-factory will be derived at the beginning of the report processing. 
   *
   * @return a copy of the data factory.
   * @throws ReportDataFactoryException if an error occured.
   */
  public DataFactory derive()
      throws ReportDataFactoryException;

  /**
   * Opens the data factory. This initializes everything. Performing queries on data factories which have not yet been
   * opened will result in exceptions.
   */
  public void open();

  /**
   * Closes the data factory and frees all resources held by this instance.
   */
  public void close();
}
