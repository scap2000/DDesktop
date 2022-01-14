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
package org.pentaho.reportdesigner.crm.report.datasetplugin.composer;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.components.ProgressListener;
import org.pentaho.reportdesigner.crm.report.connection.SQLUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 08:10:23
 */
public class JDBCAnalyzer
{
    private JDBCAnalyzer()
    {
    }


    @NotNull
    public static JDBCGraph buildGraph(@NotNull String catalog, @NotNull String schema, @NotNull Connection connection, @NotNull ProgressListener progressListener) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();

        ArrayList<JDBCTableInfo> tableInfos = getTableInfos(metaData, catalog, schema, progressListener);
        ArrayList<JDBCRelationInfo> allImportedKeys = new ArrayList<JDBCRelationInfo>();
        for (JDBCTableInfo jdbcTableInfo : tableInfos)
        {
            ArrayList<JDBCRelationInfo> importedKeys = getImportedKeys(metaData, catalog, schema, jdbcTableInfo.getTableName());
            allImportedKeys.addAll(importedKeys);
        }

        JDBCGraph jdbcGraph = new JDBCGraph(tableInfos, allImportedKeys);//vertices
        return jdbcGraph;
    }


    @NotNull
    private static ArrayList<JDBCTableInfo> getTableInfos(@NotNull DatabaseMetaData metaData, @NotNull String catalog, @NotNull String schema, @NotNull ProgressListener progressListener) throws SQLException
    {
        ResultSet tables = null;
        try
        {
            tables = metaData.getTables(catalog, schema, "%", new String[]{"TABLE", "VIEW"});//NON-NLS

            ArrayList<JDBCTableInfo> tableInfos = new ArrayList<JDBCTableInfo>();
            int cc = tables.getMetaData().getColumnCount();
            while (tables.next())
            {
                JDBCTableInfo jdbcTableInfo = new JDBCTableInfo();

                if (1 <= cc)
                    jdbcTableInfo.setCatalog(tables.getString(1));
                if (2 <= cc)
                    jdbcTableInfo.setSchema(tables.getString(2));
                if (3 <= cc)
                    jdbcTableInfo.setTableName(tables.getString(3));
                if (4 <= cc)
                    jdbcTableInfo.setType(tables.getString(4));
                if (5 <= cc)
                    jdbcTableInfo.setRemarks(tables.getString(5));
                if (6 <= cc)
                    jdbcTableInfo.setTypesCatalog(tables.getString(6));
                if (7 <= cc)
                    jdbcTableInfo.setTypesSchema(tables.getString(7));
                if (8 <= cc)
                    jdbcTableInfo.setTypeName(tables.getString(8));
                if (9 <= cc)
                    jdbcTableInfo.setSelfReferencingColumnName(tables.getString(9));
                if (10 <= cc)
                    jdbcTableInfo.setRefGeneration(tables.getString(10));

                progressListener.taskStarted(jdbcTableInfo.getTableName());

                jdbcTableInfo.setColumnInfos(getColumnInfos(metaData, catalog, schema, jdbcTableInfo.getTableName()));

                tableInfos.add(jdbcTableInfo);
            }

            return tableInfos;
        }
        finally
        {
            SQLUtil.closeResultSet(tables);
        }
    }


    @NotNull
    private static ArrayList<JDBCColumnInfo> getColumnInfos(@NotNull DatabaseMetaData metaData, @NotNull String catalog, @NotNull String schema, @NotNull String tableName) throws SQLException
    {
        ResultSet tables = null;

        ArrayList<JDBCColumnInfo> columnInfos = new ArrayList<JDBCColumnInfo>();
        try
        {
            tables = metaData.getColumns(catalog, schema, tableName, "%");
            int cc = tables.getMetaData().getColumnCount();
            while (tables.next())
            {
                JDBCColumnInfo jdbcColumnInfo = new JDBCColumnInfo();

                if (1 <= cc)
                    jdbcColumnInfo.setCatalog(tables.getString(1));
                if (2 <= cc)
                    jdbcColumnInfo.setSchema(tables.getString(2));
                if (3 <= cc)
                    jdbcColumnInfo.setTableName(tables.getString(3));
                if (4 <= cc)
                    jdbcColumnInfo.setColumnName(tables.getString(4));
                if (5 <= cc)
                    jdbcColumnInfo.setDataType(tables.getString(5));
                if (6 <= cc)
                    jdbcColumnInfo.setTypeName(tables.getString(6));
                if (7 <= cc)
                    jdbcColumnInfo.setColumnSize(tables.getInt(7));
                //void bufferLength;
                if (9 <= cc)
                    jdbcColumnInfo.setDecimalDigits(tables.getInt(9));
                if (10 <= cc)
                    jdbcColumnInfo.setNumRecRadix(tables.getInt(10));
                if (11 <= cc)
                    jdbcColumnInfo.setNullable(tables.getInt(11));
                if (12 <= cc)
                    jdbcColumnInfo.setRemarks(tables.getString(12));
                if (13 <= cc)
                    jdbcColumnInfo.setDefaultValue(tables.getString(13));
                //int sqlDataType;
                //int sqlDateTimeSub;
                if (16 <= cc)
                    jdbcColumnInfo.setCharOctetLength(tables.getInt(16));
                if (17 <= cc)
                    jdbcColumnInfo.setOrdinalPosition(tables.getInt(17));
                if (18 <= cc)
                    jdbcColumnInfo.setNullable(tables.getString(18));
                if (19 <= cc)
                    jdbcColumnInfo.setScopeCatalog(tables.getString(19));
                if (20 <= cc)
                    jdbcColumnInfo.setScopeSchema(tables.getString(20));
                if (21 <= cc)
                    jdbcColumnInfo.setSourceDataType(tables.getString(21));

                columnInfos.add(jdbcColumnInfo);
            }
        }
        finally
        {
            SQLUtil.closeResultSet(tables);
        }

        return columnInfos;
    }


    @NotNull
    private static ArrayList<JDBCRelationInfo> getImportedKeys(@NotNull DatabaseMetaData metaData, @NotNull String catalog, @NotNull String schema, @NotNull String table) throws SQLException
    {
        ResultSet importedKeys = null;
        ArrayList<JDBCRelationInfo> relationInfos = new ArrayList<JDBCRelationInfo>();

        try
        {
            importedKeys = metaData.getImportedKeys(catalog, schema, table);

            while (importedKeys.next())
            {
                String primaryKeyTableCatalog = importedKeys.getString(1);
                String primaryKeyTableScheam = importedKeys.getString(2);
                String primaryKeyColumnTable = importedKeys.getString(3);
                String primaryKeyColumnName = importedKeys.getString(4);
                String foreignKeyColumnCatalog = importedKeys.getString(5);
                String foreignKeyColumnSchema = importedKeys.getString(6);
                String foreignKeyColumnTable = importedKeys.getString(7);
                String foreignKeyColumnName = importedKeys.getString(8);

                int sequenceNumber = importedKeys.getInt(9);
                int updateRule = importedKeys.getInt(10);
                int deleteRule = importedKeys.getInt(11);

                String foreignKeyName = importedKeys.getString(12);
                String primaryKeyName = importedKeys.getString(13);
                int deferrability = importedKeys.getInt(14);

                relationInfos.add(new JDBCRelationInfo(primaryKeyTableCatalog, primaryKeyTableScheam, primaryKeyColumnTable, primaryKeyColumnName, foreignKeyColumnCatalog, foreignKeyColumnSchema, foreignKeyColumnTable, foreignKeyColumnName, sequenceNumber, updateRule, deleteRule, foreignKeyName, primaryKeyName, deferrability));
            }
        }
        finally
        {
            SQLUtil.closeResultSet(importedKeys);
        }

        return relationInfos;
    }
}
