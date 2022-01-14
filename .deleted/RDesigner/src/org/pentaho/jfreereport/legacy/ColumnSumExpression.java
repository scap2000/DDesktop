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
package org.pentaho.jfreereport.legacy;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.function.ColumnAggregationExpression;

import java.math.BigDecimal;

/**
 * User: Martin
 * Date: 26.06.2006
 * Time: 18:50:02
 */
public class ColumnSumExpression extends ColumnAggregationExpression
{

    public ColumnSumExpression()
    {
    }


    /**
     * Return the current expression value. <P> The value depends (obviously) on
     * the expression implementation.
     *
     * @return the value of the function.
     */
    @NotNull
    public Object getValue()
    {
        Object[] values = getFieldValues();
        BigDecimal computedResult = new BigDecimal("0");
        for (final Object value : values)
        {
            if (value instanceof Number)
            {
                Number n = (Number) value;
                //noinspection ObjectToString
                computedResult = computedResult.add(new BigDecimal(n.toString()));
            }
        }

        return computedResult.stripTrailingZeros();
    }
}
