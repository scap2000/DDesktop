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
package org.pentaho.reportdesigner.lib.common.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import sun.security.action.GetPropertyAction;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AccessController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 06:51:24
 */
public class OneLineFormatter extends Formatter
{
    @NotNull
    private Date dat = new Date();
    //private final static String format = "{0,date} {0,time}";
    //private MessageFormat formatter;

    @NotNull
    @NonNls
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    // Line separator string.  This is the value of the line.separator
    // property at the moment that the SimpleFormatter was created.
    @NotNull
    @SuppressWarnings({"unchecked"})
    @NonNls
    private String lineSeparator = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));


    /**
     * Format the given LogRecord.
     *
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    @NotNull
    public synchronized String format(@NotNull LogRecord record)
    {
        StringBuilder sb = new StringBuilder(128);
        // Minimize memory allocations here.
        dat.setTime(record.getMillis());
        sb.append(dateFormat.format(dat));
        sb.append(" ");
        sb.append(record.getLevel().getName());
        sb.append(" ");

        sb.append(record.getLoggerName());
        sb.append(": ");

        String message = formatMessage(record);
        sb.append(message);
        sb.append(lineSeparator);

        if (record.getThrown() != null)
        {
            //noinspection EmptyCatchBlock,UnusedCatchParameter
            try
            {
                StringWriter sw = new StringWriter();
                //noinspection IOResourceOpenedButNotSafelyClosed
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            }
            catch (Exception ex)
            {
            }
        }
        return sb.toString();
    }
}
