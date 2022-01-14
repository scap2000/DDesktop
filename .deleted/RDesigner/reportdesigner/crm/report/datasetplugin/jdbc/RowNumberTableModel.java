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

import javax.swing.table.AbstractTableModel;

/**
 * User: Martin
 * Date: 07.03.2006
 * Time: 11:14:24
 */
public class RowNumberTableModel extends AbstractTableModel
{
    private int rowCount;


    public RowNumberTableModel(int rowCount)
    {
        this.rowCount = rowCount;
    }


    public int getRowCount()
    {
        return rowCount;
    }


    public int getColumnCount()
    {
        return 1;
    }


    @NotNull
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return Integer.valueOf(rowIndex + 1);
    }
}
