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
package org.pentaho.reportdesigner.lib.client.util;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 08:45:30
 */
public class CalendarUtils
{
    private CalendarUtils()
    {
    }


    @NotNull
    public static Date getDate(int day, int month, int year)
    {
        GregorianCalendar g = new GregorianCalendar(year, month - 1, day);
        return g.getTime();
    }


    @NotNull
    public static Date getDateFrom(int day, int month, int year)
    {
        GregorianCalendar g = new GregorianCalendar(year, month - 1, day);
        return g.getTime();
    }


    @NotNull
    public static Date getDateTo(int day, int month, int year)
    {
        GregorianCalendar g = new GregorianCalendar(year, month - 1, day);
        g.add(Calendar.DATE, 1);
        g.add(Calendar.MILLISECOND, -1);
        return g.getTime();
    }


    @NotNull
    public static Date getDateTime(@NotNull Date date, @NotNull Date time)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        GregorianCalendar cal2 = new GregorianCalendar();
        cal2.setTime(time);

        cal.set(Calendar.HOUR, cal2.get(Calendar.HOUR));
        cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal2.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal2.get(Calendar.MILLISECOND));

        return cal.getTime();
    }


    @NotNull
    public static Date getDate(int day, int month, int year, int hour, int minute, int second)
    {
        GregorianCalendar g = new GregorianCalendar(year, month - 1, day, hour, minute, second);
        return g.getTime();
    }


}
