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
package org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 04.03.2006
 * Time: 16:11:30
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr"})
public class ResultSetPrinter
{
    private ResultSetPrinter()
    {
    }


    public static void printResultSet(@NotNull ResultSet resultSet) throws SQLException
    {
        ResultSetMetaData metaData = resultSet.getMetaData();
        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();

        int cc = metaData.getColumnCount();
        for (int i = 1; i <= cc; i++)
        {
            String columnName = metaData.getColumnName(i);
            columnNames.add(columnName);
            columnInfos.add(new ColumnInfo());
            if (columnName != null)
            {
                columnInfos.get(i - 1).width = columnName.length();
            }
        }

        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        while (resultSet.next())
        {
            ArrayList<String> row = new ArrayList<String>();
            for (int i = 1; i <= cc; i++)
            {
                String value = resultSet.getString(i);
                row.add(value);
                if (value != null)
                {
                    columnInfos.get(i - 1).width = Math.max(columnInfos.get(i - 1).width, value.length());
                }
            }
            data.add(row);
        }

        //output
        StringBuilder fillerBuilder = new StringBuilder(1000);
        for (int i = 0; i < 100; i++)
        {
            fillerBuilder.append("          ");//10
        }
        String filler = fillerBuilder.toString();

        printRow(columnInfos, columnNames, filler);
        for (ColumnInfo columnInfo : columnInfos)
        {
            for (int n = 0; n < columnInfo.width; n++)
            {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();

        for (ArrayList<String> row : data)
        {
            printRow(columnInfos, row, filler);
        }

        System.out.println();
    }


    private static void printRow(@NotNull ArrayList<ColumnInfo> columnInfos, @NotNull ArrayList<String> row, @NotNull String filler)
    {
        for (int i = 0; i < columnInfos.size(); i++)
        {
            ColumnInfo columnInfo = columnInfos.get(i);
            String v = (row.get(i) + filler).substring(0, columnInfo.width);
            System.out.print(v);
            System.out.print("|");
        }
        System.out.println();
    }


    private static class ColumnInfo
    {
        public int width;
    }
}
