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
 * NamedStaticDataFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.misc.datafactory;

import java.util.HashMap;

import javax.swing.table.TableModel;

import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.ReportDataFactoryException;

/**
 * A NamedStaticDataFactory provides an labelQuery-aliasing facility to decouple
 * the report definitions from the underlying datasource implentation. The
 * reports no longer need to specify the raw-labelQuery (which is in fact just an
 * implementation detail) and can use a symbolic name in the report definition
 * instead.
 *
 * @author Thomas Morgner
 */
public class NamedStaticDataFactory extends StaticDataFactory
{
  private HashMap querymappings;

  /**
   * Defaultconstructor.
   */
  public NamedStaticDataFactory()
  {
    querymappings = new HashMap();
  }

  /**
     * Adds an labelQuery-alias to this factory.
     *
     * @param alias the alias
     * @param queryString the real labelQuery string that should be used when the
     * alias is specified as labelQuery.
     */
  public void setQuery(final String alias, final String queryString)
  {
    if (queryString == null)
    {
      querymappings.remove(alias);
    }
    else
    {
      querymappings.put(alias, queryString);
    }
  }

  /**
   * Derives the factory. The derived factory does no longer share properties
   * with its parent and changes to either factory will not be reflected in the
   * other factory.
   *
   * @return the derived factory.
   * @throws ReportDataFactoryException in case something goes wrong.
   */
  public DataFactory derive() throws ReportDataFactoryException
  {
    try
    {
      return (DataFactory) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new ReportDataFactoryException("Failed to deriveForAdvance the factory", e); //$NON-NLS-1$
    }
  }

  /**
   * Returns a clone of the factory.
   *
   * @return the clone.
   * @throws CloneNotSupportedException if cloning failed.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final NamedStaticDataFactory nds = (NamedStaticDataFactory) super.clone();
    nds.querymappings = (HashMap) querymappings.clone();
    return nds;
  }

  /**
     * Queries a datasource. The string 'labelQuery' defines the name of the labelQuery. The
     * Parameterset given here may contain more data than actually needed.
     * <p/>
     * The dataset may change between two calls, do not assume anything!
     *
     * @param query the alias-name of the labelQuery.
     * @param parameters the set of parameters.
     * @return the tablemodel.
     */
  public TableModel queryData(final String query, final DataRow parameters)
      throws ReportDataFactoryException
  {
    if (query == null)
    {
      throw new NullPointerException("Query is null."); //$NON-NLS-1$
    }
    final String realQuery = getQuery(query);
    if (realQuery == null)
    {
      throw new ReportDataFactoryException("Query '" + query + "' is not recognized."); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return super.queryData(realQuery, parameters);
  }

  /**
     * Returns the labelQuery for the given alias-name or null, if there is no such alias
     * defined.
     *
     * @param name the alias name.
     * @return the real labelQuery or null.
     */
  public String getQuery(final String name)
  {
    return (String) querymappings.get(name);
  }

  /**
   * Returns all known alias-names.
   *
   * @return all alias-names as string-array.
   */
  public String[] getQueryNames()
  {
    return (String[]) querymappings.keySet().toArray
        (new String[querymappings.size()]);
  }
}
