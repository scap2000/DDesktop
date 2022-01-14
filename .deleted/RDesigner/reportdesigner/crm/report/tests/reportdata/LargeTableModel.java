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
package org.pentaho.reportdesigner.crm.report.tests.reportdata;

import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;

/**
 * User: Martin
 * Date: 12.01.2006
 * Time: 09:41:17
 */
@SuppressWarnings({"ALL"})
public class LargeTableModel extends AbstractTableModel
{
    @NotNull
    private String[] fields = new String[]{"FIRSTNAME", "LASTNAME", "INCOME"};
    @NotNull
    private Class[] fieldTypes = new Class[]{String.class, String.class, Double.class};


    public LargeTableModel()
    {
    }


    public int getRowCount()
    {
        return 10000;
    }


    public int getColumnCount()
    {
        return fields.length;
    }


    public String getColumnName(int columnIndex)
    {
        return fields[columnIndex];
    }


    public Class<?> getColumnClass(int columnIndex)
    {
        return fieldTypes[columnIndex];
    }


    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
        {
            return "Hugo" + rowIndex;
        }
        if (columnIndex == 1)
        {
            return "Habicht" + rowIndex;
        }
        if (columnIndex == 2)
        {
            return new Double(rowIndex / 123.);
        }
        return null;
    }
}