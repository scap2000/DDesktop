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
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 19.01.2006
 * Time: 08:37:37
 */
public class ReportDataTableModel extends AbstractTableModel
{

    @NotNull
    private ColumnInfo[] columnInfos;
    @NotNull
    private ArrayList<ArrayList<Object>> data;
    @NotNull
    private Class<?>[] columnClasses;


    public ReportDataTableModel(@NotNull ColumnInfo[] columnInfos, @NotNull ArrayList<ArrayList<Object>> data)
    {
        this.columnInfos = columnInfos;
        this.data = data;

        columnClasses = new Class<?>[columnInfos.length];
        for (int i = 0; i < columnInfos.length; i++)
        {
            columnClasses[i] = columnInfos[i].getColumnClass();
        }
    }


    @NotNull
    public String getColumnName(int column)
    {
        return columnInfos[column].getColumnName();
    }


    @NotNull
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClasses[columnIndex];
    }


    public int getRowCount()
    {
        return data.size();
    }


    public int getColumnCount()
    {
        return columnInfos.length;
    }


    @NotNull
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data.get(rowIndex).get(columnIndex);
    }
}
