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
import org.jetbrains.annotations.Nullable;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 10:00:32
 */
public class WizardData
{
    @NotNull
    public static final WizardData[] EMPTY_ARRAY = new WizardData[0];

    //general
    @NotNull
    public static final String LAYOUT_PLUGIN = "LayoutPlugin";
    @NotNull
    public static final String LAYOUT_STYLE = "LayoutStyle";
    @NotNull
    public static final String DATA_SET_PLUGIN = "DataSetPlugin";
    @NotNull
    public static final String VISIBLE_COLUMN_INFOS = "VisibleColumnInfos";
    @NotNull
    public static final String COLUMN_GROUPS = "ColumnGroups";
    @NotNull
    public static final String COLUMN_INFOS = "CollumnInfos";
    @NotNull
    public static final String PAGE_DEFINITION = "PageDefinition";
    @NotNull
    public static final String COLUMN_EXPRESSIONS = "ColumnExpressions";
    @NotNull
    public static final String AVAILABLE_COLUMN_INFOS = "AvailableColumnInfos";
    @NotNull
    public static final String TEMPLATE = "Template";

    //SQL stuff
    @NotNull
    public static final String CONNECTION_STRING = "ConnectionString";
    @NotNull
    public static final String JARS = "Jars";
    @NotNull
    public static final String DRIVER_CLASS = "DriverClass";
    @NotNull
    public static final String USERNAME = "Username";
    @NotNull
    public static final String PASSWORD = "Password";
    @NotNull
    public static final String SQL_QUERY = "SQLQuery";
    @NotNull
    public static final String MAX_PREVIEW_ROWS = "MaxPreviewRows";


    @NotNull
    private String key;
    @Nullable
    private Object value;


    public WizardData(@NotNull String key, @Nullable Object value)
    {
        this.key = key;
        this.value = value;
    }


    @NotNull
    public String getKey()
    {
        return key;
    }


    @Nullable
    public Object getValue()
    {
        return value;
    }


    @NotNull
    public String toString()
    {
        return "WizardData{" +
               "key='" + key + "'" +
               ", value=" + value +
               "}";
    }
}
