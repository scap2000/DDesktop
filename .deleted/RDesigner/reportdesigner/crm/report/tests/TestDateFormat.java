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
package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * User: Martin
 * Date: 16.03.2006
 * Time: 09:57:40
 */
@SuppressWarnings({"ALL"})
public class TestDateFormat
{
    public static void main(@NotNull String[] args)
    {
        GregorianCalendar gc = new GregorianCalendar();
        int week = gc.get(Calendar.WEEK_OF_YEAR);
        System.out.println("week = " + week);

        SimpleDateFormat sdf = new SimpleDateFormat("w, yyyy");
        System.out.println(sdf.format(gc.getTime()));
    }
}
