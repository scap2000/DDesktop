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

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 09.02.2006
 * Time: 13:21:46
 */
public class ThrowableHandler implements Thread.UncaughtExceptionHandler
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(ThrowableHandler.class.getName());

    @NotNull
    private static final String SUN_AWT_EXCEPTION_HANDLER = "sun.awt.exception.handler";


    public static void main(@NotNull String[] args)
    {
        System.setProperty(SUN_AWT_EXCEPTION_HANDLER, ThrowableHandler.class.getName());
    }


    @NotNull
    private static final ThrowableHandler instance = new ThrowableHandler();


    @NotNull
    public static ThrowableHandler getInstance()
    {
        return instance;
    }


    public ThrowableHandler()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ThrowableHandler.ThrowableHandler ");
    }


    public void handle(@NotNull Throwable throwable)
    {
        UncaughtExcpetionsModel.getInstance().addException(throwable);
    }


    public void uncaughtException(@NotNull Thread t, @NotNull final Throwable e)
    {
        if (!EventQueue.isDispatchThread())
        {
            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    handle(e);
                }
            });
        }
        else
        {
            handle(e);
        }
    }
}

