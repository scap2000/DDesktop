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

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Member;
import mondrian.olap.Position;
import mondrian.olap.Query;
import mondrian.olap.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.AbstractTableModel;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Martin
 * Date: 22.07.2006
 * Time: 14:47:16
 */
public class MondrianTableModel extends AbstractTableModel
{

    private static final int AXIS_COLUMN = 0;
    private static final int AXIS_ROW = 1;

    @NotNull
    private ArrayList<String> columnNames;
    @NotNull
    private Object[][] data;
    private int rowCount;
    private int columnCount;


    public MondrianTableModel(@NotNull JNDISource jndiSource, @NotNull URL mondrianDefinition, @NotNull String mdxQuery, int maxRowsToProcess)
    {
        //noinspection ConstantConditions
        if (jndiSource == null)
        {
            throw new IllegalArgumentException("jndiSource must not be null");
        }
        //noinspection ConstantConditions
        if (mondrianDefinition == null)
        {
            throw new IllegalArgumentException("mondrianDefinition must not be null");
        }
        //noinspection ConstantConditions
        if (mdxQuery == null)
        {
            throw new IllegalArgumentException("mdxQuery must not be null");
        }

        //String qs = "with member [Measures].[Variance Percent] as '([Measures].[Variance] / [Measures].[Budget])', format_string = IIf(((([Measures].[Variance] / [Measures].[Budget]) * 100.0) > 2.0), \"|#.00%|style='green'\", IIf(((([Measures].[Variance] / [Measures].[Budget]) * 100.0) < 0.0), \"|#.00%|style='red'\", \"#.00%\"))\n" +
        //            "select Crossjoin({[Region].[All Regions].[Central], [Region].[All Regions].[Eastern], [Region].[All Regions].[Southern], [Region].[All Regions].[Western]}, {[Measures].[Actual], [Measures].[Budget]}) ON columns,\n" +
        //            "  Hierarchize(Union({[Department].[All Departments]}, [Department].[All Departments].Children)) ON rows\n" +
        //            "from [Quadrant Analysis]";

        StringBuilder connectionStr = new StringBuilder(100).append("provider=mondrian; JdbcDrivers=").//NON-NLS
                append(jndiSource.getDriverClass()).
                append("; Jdbc=").//NON-NLS
                append(jndiSource.getConnectionString()).
                append("; Catalog=").//NON-NLS
                append(mondrianDefinition.toExternalForm()).
                append("; user=").//NON-NLS
                append(jndiSource.getUsername()).
                append("; userid=").//NON-NLS
                append(jndiSource.getUsername()).
                append("; JdbcUser=").//NON-NLS
                append(jndiSource.getUsername()).
                append("; password=").//NON-NLS
                append(jndiSource.getPassword()).
                append("; JdbcPassword=").//NON-NLS
                append(jndiSource.getPassword());

        Connection connection = DriverManager.getConnection(connectionStr.toString(), null, false);//NON-NLS
        Query query = connection.parseQuery(mdxQuery);

        Result resultSet = connection.execute(query);
        if (resultSet == null)
        {
            throw new IllegalArgumentException("query returned no resultset");
        }

        // Flatten out the column headers into one column-name
        Object[][] columnHeaders = createColumnHeaders(resultSet);
        Object[][] rowHeaders = createRowHeaders(resultSet);

        columnCount = 0;
        if (rowHeaders.length > 0)
        {
            columnCount = rowHeaders[0].length + resultSet.getAxes()[AXIS_COLUMN].getPositions().size();
        }
        else
        {
            columnCount = resultSet.getAxes()[AXIS_COLUMN].getPositions().size();
        }

        columnNames = new ArrayList<String>();
        for (int i = 0; i < columnCount; i++)
        {
            columnNames.add(calcColumnName(i, columnHeaders, rowHeaders, resultSet));
        }

        rowCount = Math.min(rowHeaders.length, maxRowsToProcess);

        data = new Object[columnCount][rowCount];

        for (int c = 0; c < columnCount; c++)
        {
            for (int r = 0; r < rowCount; r++)
            {
                data[c][r] = calcValueAt(resultSet, rowHeaders, r, c);
            }
        }
    }


    @Nullable
    private Object calcValueAt(@Nullable Result resultSet, @Nullable Object[][] rowHeaders, int rowIndex, int columnIndex)
    {
        if (resultSet != null)
        {
            if (rowHeaders != null)
            {
                if (columnIndex < rowHeaders[0].length)
                {
                    return rowHeaders[rowIndex][columnIndex];
                }
                else
                {
                    columnIndex -= rowHeaders[0].length;
                }
            }

            int[] key = new int[2];
            key[0] = columnIndex;
            key[1] = rowIndex;
            return resultSet.getCell(key).getValue();
        }
        return null;
    }


    @NotNull
    private String calcColumnName(int columnNumber, @NotNull Object[][] columnHeaders, @Nullable Object[][] rowHeaders, @Nullable Result resultSet)
    {
        if (rowHeaders != null)
        {
            if (columnNumber < rowHeaders[0].length)
            {
                return calcColumnName(columnNumber, resultSet);
            }
            else
            {
                columnNumber -= rowHeaders[0].length;
            }
        }
        StringBuilder buf = new StringBuilder(32);
        for (int i = 0; i < columnHeaders.length; i++)
        {
            if (i > 0)
            {
                buf.append("/");
            }
            buf.append(columnHeaders[i][columnNumber].toString());
        }
        return buf.toString();
    }


    @NotNull
    private String calcColumnName(int columnNumber, @Nullable Result nativeResultSet)
    {
        if (nativeResultSet != null)
        {
            // Flatten out the column headers into one column-name
            List<Position> positions = nativeResultSet.getAxes()[AXIS_ROW].getPositions();
            if (columnNumber < positions.get(0).size())
            {
                Member member = positions.get(0).get(columnNumber);
                return member.getHierarchy().getCaption();
            }
            else
            {
//              return positions[0].getMembers()[positions[0].getMembers().length - 1].getHierarchy().getName() + "{" + columnNumber + "}";
                return positions.get(0).get(positions.get(0).size()-1).getHierarchy().getName() + "{" + columnNumber + "}";
            }
        }
        return "";
    }


    @NotNull
    private Object[][] createColumnHeaders(@NotNull Result resultSet)
    {
        List<Position> positions = resultSet.getAxes()[AXIS_COLUMN].getPositions();
        int rowCount = positions.get(0).size() + 1;
        int colCount = positions.size();
        Object[][] result = new Object[rowCount][colCount];
        for (int c = 0; c < colCount; c++)
        {
            List<Member> members = positions.get(c);
            Member member = null;
            for (int r = 0; r < rowCount - 1; r++)
            {
                member = members.get(r);
                result[r][c] = member.getCaption();
            }

            if (member != null)
            {
                result[rowCount - 1][c] = member.getHierarchy().getCaption();
            }
        }
        return result;
    }


    @NotNull
    private Object[][] createRowHeaders(@NotNull Result resultSet)
    {
        List<Position> positions = resultSet.getAxes()[AXIS_ROW].getPositions();
        int rowCount = positions.size();
        int colCount = positions.get(0).size() + 1;
        Object[][] result = new Object[rowCount][colCount];
        for (int r = 0; r < rowCount; r++)
        {
          List<Member> members = positions.get(r);
            Member member = null;
            for (int c = 0; c < colCount - 1; c++)
            {
                member = members.get(c);
                result[r][c] = member.getCaption();
            }
            if (member != null)
            {
                result[r][colCount - 1] = member.getHierarchy().getCaption();
            }
        }
        return result;
    }


    @NotNull
    public String getColumnName(int columnNumber)
    {
        return columnNames.get(columnNumber);
    }


    public int getRowCount()
    {
        return rowCount;
    }


    public int getColumnCount()
    {
        return columnCount;
    }


    @NotNull
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data[columnIndex][rowIndex];
    }
}
