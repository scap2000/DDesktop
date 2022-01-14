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
package org.pentaho.reportdesigner.crm.report.wizard;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;

import javax.swing.table.TableModel;

/**
 * User: Martin
 * Date: 18.01.2006
 * Time: 16:57:07
 */
public class ReportData
{
    @NotNull
    private ColumnInfo[] columnInfos;
    @NotNull
    private TableModel dataTableModel;


    public ReportData(@NotNull ColumnInfo[] columnInfos, @NotNull TableModel dataTableModel)
    {
        this.columnInfos = columnInfos;
        this.dataTableModel = dataTableModel;
    }


    @NotNull
    public ColumnInfo[] getColumnInfos()
    {
        return columnInfos;
    }


    @NotNull
    public TableModel getDataTableModel()
    {
        return dataTableModel;
    }
}
