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
package org.pentaho.reportdesigner.crm.report.datasetplugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 29.07.2006
 * Time: 16:02:55
 */
public class ColumnInfoTableModel extends AbstractTableModel
{
    @NotNull
    private ArrayList<ColumnInfo> columnInfos;


    public ColumnInfoTableModel(@NotNull ArrayList<ColumnInfo> columnInfos)
    {
        this.columnInfos = columnInfos;
    }


    public int getRowCount()
    {
        return columnInfos.size();
    }


    public int getColumnCount()
    {
        return 2;
    }


    @NotNull
    public String getColumnName(int column)
    {
        if (column == 0)
        {
            return TranslationManager.getInstance().getTranslation("R", "JDBCDataSetReportElement.ColumnName");
        }
        else
        {
            return TranslationManager.getInstance().getTranslation("R", "JDBCDataSetReportElement.ColumnType");
        }
    }


    @Nullable
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
        {
            return columnInfos.get(rowIndex).getColumnName();
        }
        else if (columnIndex == 1)
        {
            return columnInfos.get(rowIndex).getColumnClass().getSimpleName();
        }
        return null;
    }
}
