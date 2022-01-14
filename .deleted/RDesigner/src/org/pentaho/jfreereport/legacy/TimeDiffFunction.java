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
import org.jetbrains.annotations.Nullable;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.Expression;

import java.util.Date;

public class TimeDiffFunction extends AbstractFunction
{
    private long diff;
    private boolean valid;

    @Nullable
    private String field1;
    @Nullable
    private String field2;


    public TimeDiffFunction()
    {
        diff = 0;
    }


    public TimeDiffFunction(@NotNull final String name)
    {
        this();
        setName(name);
    }


    @Nullable
    public String getField1()
    {
        return field1;
    }


    public void setField1(@Nullable String field1)
    {
        this.field1 = field1;
    }


    @Nullable
    public String getField2()
    {
        return field2;
    }


    public void setField2(@Nullable String field2)
    {
        this.field2 = field2;
    }


    public void itemsAdvanced(@NotNull final ReportEvent event)
    {
        valid = false;

        final Object fieldValue1 = getDataRow().get(getField1());
        final Object fieldValue2 = getDataRow().get(getField2());
        long value1;
        if (fieldValue1 instanceof Number)
        {
            Number number = (Number) fieldValue1;
            value1 = number.longValue();
        }
        else if (fieldValue1 instanceof Date)
        {
            Date date = (Date) fieldValue1;
            value1 = date.getTime();
        }
        else
        {
            return;
        }

        long value2;
        if (fieldValue2 instanceof Number)
        {
            Number number = (Number) fieldValue2;
            value2 = number.longValue();
        }
        else if (fieldValue2 instanceof Date)
        {
            Date date = (Date) fieldValue2;
            value2 = date.getTime();
        }
        else
        {
            return;
        }

        diff = value1 - value2;
        valid = true;
    }


    @Nullable
    public Object getValue()
    {
        if (valid)
        {
            return new Long(diff);
        }
        return null;
    }


    @NotNull
    public Expression getInstance()
    {
        final TimeDiffFunction function = (TimeDiffFunction) super.getInstance();
        return function;
    }

}
