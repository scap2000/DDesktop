/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.connection;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCClassLoader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 18.01.2006
 * Time: 15:24:39
 */
public class JDBCConnection implements MetaDataService
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JDBCConnection.class.getName());

    @SuppressWarnings({"StaticNonFinalField"})
    private static int openCount;

    @NotNull
    private static final String[] TYPES_TO_FETCH = new String[]{"TABLE", "VIEW"};

    @NotNull
    private Connection db;


    public JDBCConnection(@NotNull String[] jars, @NotNull String classname, @NotNull String connectionString, @NotNull String username, @NotNull String password) throws Exception
    {
        db = JDBCClassLoader.getConnection(jars, classname, connectionString, username, password);
        openCount++;

        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCConnection.JDBCConnection openCount = " + openCount);
    }


    @NotNull
    public Connection getConnection()
    {
        return db;
    }


    @NotNull
    public String getCatalog()
    {
        try
        {
            return db.getCatalog();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    public void dispose()
    {
        SQLUtil.closeConnection(db);
        openCount--;
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCConnection.dispose openCount = " + openCount);
    }


    @NotNull
    public String[] getSchemaNames()
    {
        ResultSet resultSet = null;
        try
        {
            ArrayList<String> values = new ArrayList<String>();
            resultSet = db.getMetaData().getSchemas();
            while (resultSet.next())
            {
                String string = resultSet.getString(1);
                values.add(string);
            }

            return values.toArray(new String[values.size()]);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            SQLUtil.closeResultSet(resultSet);
        }
    }


    @NotNull
    public String[] getTableNames(@NotNull String schemaName)
    {
        ResultSet resultSet = null;
        try
        {
            ArrayList<String> values = new ArrayList<String>();
            resultSet = db.getMetaData().getTables(null, null, "%", TYPES_TO_FETCH);
            while (resultSet.next())
            {
                String string = resultSet.getString(3);
                values.add(string);
            }

            return values.toArray(new String[values.size()]);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            SQLUtil.closeResultSet(resultSet);
        }
    }


    @NotNull
    public ColumnInfo[] getColumnInfos(@NotNull String schemaName, @NotNull String tableName)
    {
        ResultSet resultSet = null;
        try
        {
            ArrayList<ColumnInfo> values = new ArrayList<ColumnInfo>();
            resultSet = db.getMetaData().getColumns(null, schemaName, tableName, "%");
            while (resultSet.next())
            {
                String string = resultSet.getString(4);
                int sqlType = resultSet.getInt(5);
                values.add(new ColumnInfo(tableName, string, ColumnInfo.getJavaTypeFomrSQLType(sqlType)));
            }

            return values.toArray(new ColumnInfo[values.size()]);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            SQLUtil.closeResultSet(resultSet);
        }
    }


    @NotNull
    public ColumnInfo[] getQueryMetaData(@NotNull String query)
    {
        ResultSet resultSet = null;
        Statement statement = null;

        try
        {
            ArrayList<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();

            statement = db.createStatement();
            // updated by MDD 06/29/2006
            // The fetchSize is causing problems with some database vendors not supporting
            // this call.  If the purpose of locking this down to 1 is to limit the amount of
            // data coming back from the server, we'll be fine with our maxRows set to 1.  The
            // fetchSize would be more useful if we had a huge amount of rows and we wanted
            // smaller fetchSizes so we could start possibly queueing data.
            // I'm removing the setFetchSize() call.  Several forum posts with errors.
            //statement.setFetchSize(1);
            statement.setMaxRows(1);
            resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int cc = metaData.getColumnCount();
            for (int i = 1; i <= cc; i++)
            {
                String tableName = metaData.getTableName(i);
                String columnName = metaData.getColumnName(i);
                String columnClassName = metaData.getColumnClassName(i);
                Class columnClass;
                try
                {
                    columnClass = Class.forName(columnClassName);
                }
                catch (ClassNotFoundException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCConnection.getQueryMetaData ", e);
                    columnClass = Object.class;
                }

                columnInfos.add(new ColumnInfo(tableName, columnName, columnClass));
            }

            return columnInfos.toArray(new ColumnInfo[columnInfos.size()]);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            SQLUtil.closeResultSet(resultSet);
            SQLUtil.closeStatement(statement);
        }
    }


    @NotNull
    public ArrayList<ArrayList<Object>> getQueryData(int maxRows, @NotNull String query)
    {
        ArrayList<ArrayList<Object>> arrayLists = new ArrayList<ArrayList<Object>>();

        ResultSet resultSet = null;
        Statement statement = null;
        try
        {
            statement = db.createStatement();
            if (maxRows < Integer.MAX_VALUE)
            {
                statement.setFetchSize(maxRows);
                statement.setMaxRows(maxRows);
            }
            resultSet = statement.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int cc = resultSetMetaData.getColumnCount();
            int currentRow = 0;
            while (resultSet.next())
            {
                if (currentRow >= maxRows)
                {
                    return arrayLists;
                }

                ArrayList<Object> row = new ArrayList<Object>();
                for (int i = 1; i <= cc; i++)
                {
                    Object obj = resultSet.getObject(i);
                    row.add(obj);
                }

                arrayLists.add(row);

                currentRow++;
            }

            return arrayLists;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            SQLUtil.closeResultSet(resultSet);
            SQLUtil.closeStatement(statement);
        }
    }


}
