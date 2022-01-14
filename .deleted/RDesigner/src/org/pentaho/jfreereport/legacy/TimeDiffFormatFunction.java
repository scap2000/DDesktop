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

public class TimeDiffFormatFunction extends AbstractFunction
{
    @Nullable
    private String field;
    @Nullable
    private String formatted;


    public TimeDiffFormatFunction()
    {
    }


    public TimeDiffFormatFunction(@NotNull final String name)
    {
        this();
        setName(name);
    }


    @Nullable
    public String getField()
    {
        return field;
    }


    public void setField(@Nullable String field)
    {
        this.field = field;
    }


    public void itemsAdvanced(@NotNull final ReportEvent event)
    {
        formatted = null;

        final Object fieldValue1 = getDataRow().get(getField());
        if (!(fieldValue1 instanceof Number))
        {
            return;
        }
        long value1 = ((Number) fieldValue1).longValue();
        format(value1);
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
        final TimeDiffFormatFunction function = (TimeDiffFormatFunction) super.getInstance();
        return function;
    }

}
