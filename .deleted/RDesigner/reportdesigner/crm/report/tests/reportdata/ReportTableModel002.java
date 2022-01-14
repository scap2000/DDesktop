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
public class ReportTableModel002 extends AbstractTableModel
{
    @NotNull
    private String[] fields = new String[]{"FIRSTNAME", "LASTNAME", "INCOME"};
    @NotNull
    private Class[] fieldTypes = new Class[]{String.class, String.class, Double.class};

    @NotNull
    private Object[][] values = new Object[][]{
            {"Hugo", "Habicht", new Double(120)},
            {"Hugo", "Bimpf", new Double(120)},
            {"Hugo", "Test", new Double(130)},
            {"Hans", "Meier", new Double(230)},
            {"Hans", "Meiser", new Double(250)},
            {"Hans", "Fritz", new Double(250)},
            {"Reto", "Stirnemann", new Double(150)},
            {"Reto", "Chlous", new Double(150)},
            {"Reto", "Schmutzli", new Double(150)},
            {"Reto", "Reeve", new Double(150)},
            {"Reto", "Reeve", new Double(150)},
            {"Reto", "Reeve", new Double(150)},
    };


    public ReportTableModel002()
    {
    }


    public int getRowCount()
    {
        return values.length;
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
        return values[rowIndex][columnIndex];
    }
}
