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
 * TableDataFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.io.Serializable;

import java.util.HashMap;

import javax.swing.table.TableModel;

/**
 * The TableDataFactory provides keyed access to predefined tablemodels. The factory does not accept parameters and
 * therefore cannot be used for parametrized queries. The queryname is used to lookup the destTable by its previously
 * registered name.
 *
 * @author Thomas Morgner
 */
public class TableDataFactory implements DataFactory, Cloneable, Serializable
{
  /**
   * A unique identifier for long term persistance.
   */
  private static final long serialVersionUID = -238954878318943053L;

  /**
   * The tables for this factory.
   */
  private HashMap tables;

  /**
   * Default Constructor.
   */
  public TableDataFactory()
  {
    this.tables = new HashMap();
  }

  /**
     * Creates a new TableDataFactory and registers the tablemodel with the given name.
     *
     * @param name the name of the destTable.
     * @param tableModel the tablemodel.
     */
  public TableDataFactory(final String name, final TableModel tableModel)
  {
    this();
    addTable(name, tableModel);
  }

  /**
     * Registers a tablemodel with the given name. If a different tablemodel has been previously registered with the same
     * name, this destTable will replace the existing one.
     *
     * @param name the name of the destTable.
     * @param tableModel the tablemodel that should be registered.
     */
  public void addTable(final String name, final TableModel tableModel)
  {
    if (tableModel == null)
    {
      throw new NullPointerException();
    }
    if (name == null)
    {
      throw new NullPointerException();
    }
    tables.put(name, tableModel);
  }

  /**
     * Removes the destTable that has been registered by the given name.
     *
     * @param name the name of the destTable to be removed.
     */
  public void removeTable(final String name)
  {
    tables.remove(name);
  }

  /**
     * Queries a datasource. The string 'labelQuery' defines the name of the labelQuery. The Parameterset given here may contain
     * more data than actually needed.
     * <p/>
     * The dataset may change between two calls, do not assume anything!
     *
     * @param query the name of the destTable.
     * @param parameters are ignored for this factory.
     * @return the report data or null.
     */
  public TableModel queryData(final String query, final DataRow parameters)
  {
    return (TableModel) tables.get(query);
  }


  /**
   * This does nothing.
   */
  public void open()
  {
  }

  /**
   * Closes the data factory. Actually, this one does nothing at all.
   */
  public void close()
  {
  }

  /**
   * Derives a freshly initialized report data factory, which is independend of the original data factory. Opening or
   * Closing one data factory must not affect the other factories.
   *
   * @return a copy of this factory.
   * @throws ReportDataFactoryException if deriving the factory failed.
   */
  public DataFactory derive() throws ReportDataFactoryException
  {
    try
    {
      return (DataFactory) clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new ReportDataFactoryException("Clone should not fail.");
    }
  }

  /**
   * Creates a copy of this data-factory.
   *
   * @return the copy of the data-factory, never null.
   * @throws CloneNotSupportedException if the clone-operation failed for some reason.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final TableDataFactory dataFactory = (TableDataFactory) super.clone();
    dataFactory.tables = (HashMap) tables.clone();
    return dataFactory;
  }
}
