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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 24.02.2006
 * Time: 09:36:43
 */
public class UncaughtExcpetionsModel
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(UncaughtExcpetionsModel.class.getName());

    private static final int MAXIMUM_SIZE = 100;

    @NotNull
    private static final UncaughtExcpetionsModel instance = new UncaughtExcpetionsModel();


    @NotNull
    public static UncaughtExcpetionsModel getInstance()
    {
        return instance;
    }


    @NotNull
    private LinkedHashSet<UncaughtExceptionModelListener> uncaughtExceptionModelListeners;
    @NotNull
    private LinkedList<ThrowableInfo> throwables;


    public UncaughtExcpetionsModel()
    {
        uncaughtExceptionModelListeners = new LinkedHashSet<UncaughtExceptionModelListener>();
        throwables = new LinkedList<ThrowableInfo>();
    }


    public void addException(@NotNull Throwable throwable)
    {
        if (LOG.isLoggable(Level.SEVERE)) LOG.log(Level.SEVERE, "UncaughtExcpetionsModel.addException ", throwable);

        throwables.add(new ThrowableInfo(System.currentTimeMillis(), throwable));
        if (throwables.size() > MAXIMUM_SIZE)
        {
            throwables.removeFirst();
        }

        LinkedHashSet<UncaughtExceptionModelListener> uncaughtExceptionModelListeners = new LinkedHashSet<UncaughtExceptionModelListener>(this.uncaughtExceptionModelListeners);
        for (UncaughtExceptionModelListener uncaughtExceptionModelListener : uncaughtExceptionModelListeners)
        {
            uncaughtExceptionModelListener.exceptionCaught(throwable);
        }
    }


    @NotNull
    public LinkedList<ThrowableInfo> getThrowables()
    {
        return throwables;
    }


    public void addUncaughtExceptionModelListener(@NotNull UncaughtExceptionModelListener uncaughtExceptionModelListener)
    {
        uncaughtExceptionModelListeners.add(uncaughtExceptionModelListener);
    }


    public void removeUncaughtExceptionModelListener(@NotNull UncaughtExceptionModelListener uncaughtExceptionModelListener)
    {
        uncaughtExceptionModelListeners.remove(uncaughtExceptionModelListener);
    }


    public void clearExceptions()
    {
        throwables.clear();
    }


    public static class ThrowableInfo
    {
        private boolean submitted;
        private long millis;
        @NotNull
        private Throwable throwable;


        public ThrowableInfo(long millis, @NotNull Throwable throwable)
        {
            //noinspection ConstantConditions
            if (throwable == null)
            {
                throw new IllegalArgumentException("throwable must not be null");
            }

            submitted = false;
            this.millis = millis;
            this.throwable = throwable;
        }


        public long getMillis()
        {
            return millis;
        }


        @NotNull
        public Throwable getThrowable()
        {
            return throwable;
        }


        public boolean isSubmitted()
        {
            return submitted;
        }


        public void setSubmitted(boolean submitted)
        {
            this.submitted = submitted;
        }


        @NotNull
        public String toString()
        {
            return (submitted ? "" : "* ") + new Date(millis) + " " + throwable;
        }
    }
}
