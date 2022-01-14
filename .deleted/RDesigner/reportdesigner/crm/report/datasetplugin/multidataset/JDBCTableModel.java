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
package org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.connection.SQLUtil;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCClassLoader;

import javax.swing.table.AbstractTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * User: Martin Date: 27.07.2006 Time: 16:39:51
 */
public class JDBCTableModel extends AbstractTableModel
{
    @NotNull
    private ArrayList<Object[]> data;
    @NotNull
    private ArrayList<String> columnNames;
    @NotNull
    private ArrayList<Class> columnTypes;


    public JDBCTableModel(@NotNull JNDISource jndiSource, @NotNull String sqlQuery, int maxRowsToProcess) throws Exception
    {
        // noinspection ConstantConditions
        if (jndiSource == null)
        {
            throw new IllegalArgumentException("jndiSource must not be null");
        }
        // noinspection ConstantConditions
        if (sqlQuery == null)
        {
            throw new IllegalArgumentException("sqlQuery must not be null");
        }

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            // noinspection JDBCResourceOpenedButNotSafelyClosed
            connection = JDBCClassLoader.getConnection(jndiSource.getDriverClass(), jndiSource.getConnectionString(), jndiSource.getUsername(), jndiSource.getPassword());
            // connection = DriverManager.getConnection(jndiSource.getConnectionString(), jndiSource.getUsername(), jndiSource.getPassword());
            statement = connection.createStatement();
            try
            {
                if (maxRowsToProcess > 0)
                {
                    statement.setMaxRows(maxRowsToProcess);
                }
            }
            catch (Exception e)
            {
                // if this fails, we need not blow out, we just want to try to set this here
                // since it is will do a significantly better job at limited the data
            }
            resultSet = statement.executeQuery(sqlQuery);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            columnNames = new ArrayList<String>();
            columnTypes = new ArrayList<Class>();

            for (int i = 1; i <= columnCount; i++)
            {
                String columnName = metaData.getColumnName(i);
                Class type = Class.forName(metaData.getColumnClassName(i));
                columnNames.add(columnName);
                columnTypes.add(type);
            }

            data = new ArrayList<Object[]>();

            while (resultSet.next())
            {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++)
                {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                data.add(rowData);
                if (data.size() >= maxRowsToProcess)
                {
                    break;
                }
            }
        }
        finally
        {
            SQLUtil.closeResultSet(resultSet);
            SQLUtil.closeStatement(statement);
            SQLUtil.closeConnection(connection);
        }
    }


    @NotNull
    public String getColumnName(int column)
    {
        return columnNames.get(column);
    }


    @NotNull
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnTypes.get(columnIndex);
    }


    public int getColumnCount()
    {
        return columnNames.size();
    }


    public int getRowCount()
    {
        return data.size();
    }


    @Nullable
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data.get(rowIndex)[columnIndex];
    }
}
