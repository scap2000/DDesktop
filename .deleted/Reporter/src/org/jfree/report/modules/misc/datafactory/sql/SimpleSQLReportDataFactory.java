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
 * SimpleSQLReportDataFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.misc.datafactory.sql;

import java.io.Serializable;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.HashMap;

import javax.swing.table.TableModel;

import org.digitall.lib.sql.LibSQL;

import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.modules.misc.tablemodel.ResultSetTableModelFactory;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 19.02.2006, 17:37:33
 *
 * @author Thomas Morgner
 */
public class SimpleSQLReportDataFactory
    implements DataFactory, Cloneable, Serializable
{
  private static class PreparedStatementCarrier
  {
    private PreparedStatement preparedStatement;
    private String[] parameters;

    protected PreparedStatementCarrier(final PreparedStatement preparedStatement,
                                    final String[] parameters)
    {
      this.preparedStatement = preparedStatement;
      this.parameters = parameters;
    }

    public PreparedStatement getPreparedStatement()
    {
      return preparedStatement;
    }

    public String[] getParameters()
    {
      return parameters;
    }
  }

  private transient HashMap preparedStatements;
  private transient Connection connection;
  private ConnectionProvider connectionProvider;

  private boolean labelMapping;
  private static final String COLUMN_NAME_MAPPING_KEY =
      "org.jfree.report.modules.data.sql.ColumnNameMapping"; //$NON-NLS-1$


  public SimpleSQLReportDataFactory(final Connection connection)
  {
    this(new StaticConnectionProvider(connection));
  }

  public SimpleSQLReportDataFactory(final ConnectionProvider connectionProvider)
  {
    if (connectionProvider == null)
    {
      throw new NullPointerException();
    }
    this.connectionProvider = connectionProvider;
    final Configuration globalConfig =
        JFreeReportBoot.getInstance().getGlobalConfig();
    this.labelMapping = "Label".equals(globalConfig.getConfigProperty //$NON-NLS-1$
        (SimpleSQLReportDataFactory.COLUMN_NAME_MAPPING_KEY, "Label")); //$NON-NLS-1$
  }

  public boolean isLabelMapping()
  {
    return labelMapping;
  }

  public void setLabelMapping(final boolean labelMapping)
  {
    this.labelMapping = labelMapping;
  }

  private synchronized Connection getConnection() throws SQLException
  {
    if (connection == null)
    {
      connection = connectionProvider.getConnection();
    }
    return connection;
  }

  private int getBestResultSetType() throws SQLException
  {
    final Connection connection = getConnection();
    final boolean supportsScrollInsensitive = connection.getMetaData().supportsResultSetType
        (ResultSet.TYPE_SCROLL_INSENSITIVE);
    final boolean supportsScrollSensitive = connection.getMetaData().supportsResultSetType
        (ResultSet.TYPE_SCROLL_SENSITIVE);

    if (supportsScrollInsensitive)
    {
      return ResultSet.TYPE_SCROLL_INSENSITIVE;
    }
    if (supportsScrollSensitive)
    {
      return ResultSet.TYPE_SCROLL_SENSITIVE;
    }
    return ResultSet.TYPE_FORWARD_ONLY;
  }

  /**
     * Queries a datasource. The string 'labelQuery' defines the name of the labelQuery. The Parameterset given here may contain
     * more data than actually needed.
     * <p/>
     * The dataset may change between two calls, do not assume anything!
     *
     * @param query
     * @param parameters
     * @return
     */
  public synchronized TableModel queryData(final String query, final DataRow parameters)
      throws ReportDataFactoryException
  {
    try
    {
      if (preparedStatements == null)
      {
        preparedStatements = new HashMap();
      }

      PreparedStatementCarrier pstmtCarrier = (PreparedStatementCarrier) preparedStatements.get(query);
      final boolean callableStatementQuery = isCallableStatementQuery(query);
      if (pstmtCarrier == null)
      {
        final SQLParameterLookupParser parser = new SQLParameterLookupParser();
        final String translatedQuery = parser.translateAndLookup(query);
        final PreparedStatement pstmt;
        if (callableStatementQuery || isCallableStatement(query))
        {
          pstmt = getConnection().prepareCall
              (translatedQuery, getBestResultSetType(), ResultSet.CONCUR_READ_ONLY);
        }
        else
        {
          pstmt = getConnection().prepareStatement
              (translatedQuery, getBestResultSetType(), ResultSet.CONCUR_READ_ONLY);
        }
        pstmtCarrier = new PreparedStatementCarrier(pstmt, parser.getFields());
        preparedStatements.put(query, pstmtCarrier);
      }

      final PreparedStatement pstmt = pstmtCarrier.getPreparedStatement();
      pstmt.clearParameters();
      if (callableStatementQuery)
      {
        final CallableStatement callableStatement = (CallableStatement) pstmt;
        callableStatement.registerOutParameter(0, Types.OTHER);
      }
      final String[] params = pstmtCarrier.getParameters();
      for (int i = 0; i < params.length; i++)
      {
        final String param = params[i];
        final Object pvalue = parameters.get(param);
        if (pvalue == null)
        {
          // this should work, but some driver are known to die here.
          // they should be fed with setNull(..) instead; something
          // we cant do as JDK1.2's JDBC does not define it.
          pstmt.setObject(i + 1, null);
        }
        else
        {
          pstmt.setObject(i + 1, pvalue);
        }
      }
      final ResultSet res = LibSQL.exFunction(query, "");
      //final ResultSet res = pstmt.executeQuery();
      return ResultSetTableModelFactory.getInstance().createTableModel(res);
    }
    catch (Exception e)
    {
      throw new ReportDataFactoryException("Failed at query: " + query, e); //$NON-NLS-1$
    }
  }

  private boolean isCallableStatement(final String query)
  {
    int state = 0;
    final char[] chars = query.toCharArray();
    final int length = query.length();
    for (int i = 0; i < length; i++)
    {
      final char c = chars[i];
      if (Character.isWhitespace(c))
      {
        if (state == 5)
        {
          return true;
        }
      }
      else if ('{' == c && state == 0)
      {
        state = 1;
      }
      else if (('c' == c || 'C' == c) && state == 1)
      {
        state = 2;
      }
      else if (('a' == c || 'A' == c) && state == 2)
      {
        state = 3;
      }
      else if (('l' == c || 'L' == c) && state == 4)
      {
        state = 4;
      }
      else if (('l' == c || 'L' == c) && state == 5)
      {
        state = 5;
      }
      else
      {
        if (state == 5)
        {
          return true;
        }
        return false;
      }
    }
    return false;
  }

  private boolean isCallableStatementQuery(final String query)
  {
    int state = 0;
    final char[] chars = query.toCharArray();
    final int length = query.length();
    for (int i = 0; i < length; i++)
    {
      final char c = chars[i];
      if (Character.isWhitespace(c))
      {
        if (state == 7)
        {
          return true;
        }
      }
      else if ('{' == c && state == 0)
      {
        state = 1;
      }
      else if ('?' == c && state == 1)
      {
        state = 2;
      }
      else if ('=' == c && state == 2)
      {
        state = 3;
      }
      else if (('c' == c || 'C' == c) && state == 3)
      {
        state = 4;
      }
      else if (('a' == c || 'A' == c) && state == 4)
      {
        state = 5;
      }
      else if (('l' == c || 'L' == c) && state == 5)
      {
        state = 6;
      }
      else if (('l' == c || 'L' == c) && state == 6)
      {
        state = 7;
      }
      else
      {
        if (state == 7)
        {
          return true;
        }
        return false;
      }
    }
    return false;
  }

  public void open()
  {

  }

  public synchronized void close()
  {
    if (connection == null)
    {
      return;
    }

    try
    {
      connection.close();
    }
    catch (SQLException e)
    {
      // we tried our very best ..
    }
    connection = null;
  }

  /**
   * Derives a freshly initialized report data factory, which is independend of the original data factory. Opening or
   * Closing one data factory must not affect the other factories.
   *
   * @return
   */
  public DataFactory derive()
  {
    try
    {
      return (DataFactory) clone();
    }
    catch (CloneNotSupportedException e)
    {
      // this should not happen ..
      throw new IllegalStateException("Clone failed?"); //$NON-NLS-1$
    }
  }

  public Object clone() throws CloneNotSupportedException
  {
    final SimpleSQLReportDataFactory dataFactory = (SimpleSQLReportDataFactory) super.clone();
    dataFactory.connection = null;
    dataFactory.preparedStatements = null;
    return dataFactory;
  }

  public ConnectionProvider getConnectionProvider()
  {
    return connectionProvider;
  }

}
