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

import org.jetbrains.annotations.Nullable;
import org.jfree.report.function.ColumnAggregationExpression;

import java.math.BigDecimal;

/**
 * User: Martin
 * Date: 26.06.2006
 * Time: 18:50:02
 */
public class ColumnDifferenceExpression extends ColumnAggregationExpression
{

    public ColumnDifferenceExpression()
    {
    }


    /**
     * Return the current expression value. <P> The value depends (obviously) on
     * the expression implementation.
     *
     * @return the value of the function.
     */
    @Nullable
    public Object getValue()
    {
        Object[] values = getFieldValues();
        BigDecimal computedResult = null;
        for (final Object value : values)
        {
            if (value instanceof Number)
            {
                Number n = (Number) value;
                if (computedResult == null)
                {
                    //noinspection ObjectToString
                    computedResult = new BigDecimal(n.toString());
                }
                else
                {
                    //noinspection ObjectToString
                    computedResult = computedResult.subtract(new BigDecimal(n.toString()));
                }
            }
        }

        if (computedResult != null)
        {
            return computedResult.stripTrailingZeros();
        }
        else
        {
            return null;
        }
    }
}
