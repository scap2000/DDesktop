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

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;

import java.io.IOException;
import java.io.Writer;
import java.util.Random;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * User: Martin
 * Date: 01.08.2006
 * Time: 15:30:55
 */
public class BufferedHandler extends Handler
{

    public static void main(@NotNull String[] args) throws IOException
    {
        //noinspection IOResourceOpenedButNotSafelyClosed
        LimitedStringWriter sw = new LimitedStringWriter();
        Random random = new Random();
        for (int i = 0; i < 10000000; i++)
        {
            sw.append(getRandomLine(random, random.nextInt(1000)));

            if (i % 100 == 0)
            {
                int length = sw.getString().length();
                if (length > LimitedStringWriter.MAX_SIZE)
                {
                    throw new RuntimeException("string too long");
                }
            }
        }
    }


    @NotNull
    private static String getRandomLine(@NotNull Random random, int length)
    {
        String chars = "abcdefghijklmnopqrstuvwxyz1234567890";//NON-NLS
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
        {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        sb.append("\n");
        return sb.toString();
    }


    @NotNull
    private static final BufferedHandler bufferedHandler = new BufferedHandler();


    @NotNull
    public static BufferedHandler getBufferedHandler()
    {
        return bufferedHandler;
    }


    @NotNull
    private LimitedStringWriter writer;


    private BufferedHandler()
    {
        //noinspection IOResourceOpenedButNotSafelyClosed
        writer = new LimitedStringWriter();
    }


    @NotNull
    public synchronized String getBufferedText()
    {
        return writer.getString();
    }


    public synchronized void close() throws SecurityException
    {
        IOUtil.closeStream(writer);
    }


    public void flush()
    {
    }


    public synchronized void publish(@NotNull LogRecord record)
    {
        if (!isLoggable(record))
        {
            return;
        }
        String msg;
        try
        {
            msg = getFormatter().format(record);
        }
        catch (Exception ex)
        {
            reportError(null, ex, ErrorManager.FORMAT_FAILURE);
            return;
        }

        try
        {
            writer.write(msg);
        }
        catch (Exception ex)
        {
            reportError(null, ex, ErrorManager.WRITE_FAILURE);
        }
    }


    private static class LimitedStringWriter extends Writer
    {
        private static final int MIN_SIZE = 100000;
        private static final int MAX_SIZE = 150000;

        @NotNull
        @SuppressWarnings({"StringBufferField"})
        private StringBuilder sb;


        private LimitedStringWriter()
        {
            sb = new StringBuilder(1000);
        }


        public void write(@NotNull char[] cbuf, int off, int len) throws IOException
        {
            sb.append(cbuf, off, len);

            if (sb.length() > MAX_SIZE)
            {
                int deleteEnd = sb.length() - MIN_SIZE;
                //search for a maximum of 1000 characters for a newline, otherwise cuts the oldest line
                int maxSearchEnd = Math.min(deleteEnd + 1000, sb.length());
                for (int i = deleteEnd; i < maxSearchEnd; i++)
                {
                    if (sb.charAt(i) == '\n')
                    {
                        deleteEnd = i + 1;
                        break;
                    }
                }
                sb.delete(0, deleteEnd);
            }
        }


        public void flush() throws IOException
        {
        }


        public void close() throws IOException
        {
        }


        @NotNull
        public String getString()
        {
            return sb.toString();
        }
    }

}
