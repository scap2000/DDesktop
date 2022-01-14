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
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.common.graph.Vertex;

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 09:08:19
 */
public class JDBCVertex extends Vertex
{
    @NotNull
    private JDBCTableInfo tableInfo;


    public JDBCVertex(@NotNull JDBCTableInfo tableInfo)
    {
        //noinspection ConstantConditions
        if (tableInfo == null)
        {
            throw new IllegalArgumentException("tableInfo must not be null");
        }

        this.tableInfo = tableInfo;
    }


    @NotNull
    public JDBCTableInfo getTableInfo()
    {
        return tableInfo;
    }


    @NotNull
    public String toString()
    {
        return tableInfo.getTableName();
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JDBCVertex that = (JDBCVertex) o;

        return !(tableInfo != null ? !tableInfo.equals(that.tableInfo) : that.tableInfo != null);

    }


    public int hashCode()
    {
        return (tableInfo != null ? tableInfo.hashCode() : 0);
    }
}
