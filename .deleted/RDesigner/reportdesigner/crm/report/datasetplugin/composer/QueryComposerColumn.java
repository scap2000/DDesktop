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

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 09:44:14
 */
public class QueryComposerColumn
{
    @NotNull
    private JDBCColumnInfo columnInfo;
    private boolean visible;
    @Nullable
    private OrderDirection orderDirection;
    @Nullable
    private String aggregateFunction;
    @Nullable
    private String scalarFunction;
    @Nullable
    private String filter;


    public QueryComposerColumn(@NotNull JDBCColumnInfo columnInfo, boolean visible, @Nullable OrderDirection orderDirection, @Nullable String aggregateFunction, @Nullable String scalarFunction, @Nullable String filter)
    {
        //noinspection ConstantConditions
        if (columnInfo == null)
        {
            throw new IllegalArgumentException("columnInfo must not be null");
        }

        this.columnInfo = columnInfo;
        this.visible = visible;
        this.orderDirection = orderDirection;
        this.aggregateFunction = aggregateFunction;
        this.scalarFunction = scalarFunction;
        this.filter = filter;
    }


    public boolean isVisible()
    {
        return visible;
    }


    @NotNull
    public JDBCColumnInfo getColumnInfo()
    {
        return columnInfo;
    }


    @Nullable
    public OrderDirection getOrderDirection()
    {
        return orderDirection;
    }


    @Nullable
    public String getAggregateFunction()
    {
        return aggregateFunction;
    }


    @Nullable
    public String getScalarFunction()
    {
        return scalarFunction;
    }


    @Nullable
    public String getFilter()
    {
        return filter;
    }


    @NotNull
    public String toString()
    {
        return "QueryComposerColumn{" +
               "columnInfo=" + columnInfo +
               ", visible=" + visible +
               ", orderDirection=" + orderDirection +
               ", aggregateFunction='" + aggregateFunction + "'" +
               ", scalarFunction='" + scalarFunction + "'" +
               ", filter='" + filter + "'" +
               "}";
    }
}
