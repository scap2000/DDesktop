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

public class TimeDiffAndFormatFunction extends AbstractFunction
{
    @Nullable
    private String formatted;

    @Nullable
    private String field1;
    @Nullable
    private String field2;


    public TimeDiffAndFormatFunction()
    {
    }


    public TimeDiffAndFormatFunction(@NotNull final String name)
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
        formatted = null;

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

        long diff = value1 - value2;
        format(diff);
    }


    private void format(long value1)
    {
        long hours = value1 / (60 * 60 * 1000);
        long rest = value1 - (hours * 60 * 60 * 1000);
        long minutes = rest / (60 * 1000);
        rest -= minutes * 60 * 1000;
        long seconds = rest / 1000;

        String min = String.valueOf(minutes);
        String sec = String.valueOf(seconds);
        if (min.length() < 2)
        {
            min = "0" + min;
        }
        if (sec.length() < 2)
        {
            sec = "0" + sec;
        }
        formatted = hours + ":" + min + ":" + sec;
    }


    @Nullable
    public Object getValue()
    {
        return formatted;
    }


    @NotNull
    public Expression getInstance()
    {
        final TimeDiffAndFormatFunction function = (TimeDiffAndFormatFunction) super.getInstance();
        return function;
    }

}
