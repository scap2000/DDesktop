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
import org.pentaho.reportdesigner.crm.report.components.InternalProgressDialog;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 09.02.2006
 * Time: 15:55:13
 */
public class ExceptionUtils
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(ExceptionUtils.class.getName());


    private ExceptionUtils()
    {
    }


    public static void disposeDialogInEDT(@NotNull final JDialog dialog)
    {
        if (SwingUtilities.isEventDispatchThread())
        {
            try
            {
                dialog.dispose();
            }
            catch (Throwable e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExceptionUtils.disposeDialogInEDT ", e);
                //ok, just to be paranoid
            }
        }
        else
        {
            try
            {
                EventQueue.invokeAndWait(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            dialog.dispose();
                        }
                        catch (Throwable e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExceptionUtils.disposeDialogInEDT ", e);
                            //ok, just to be paranoid
                        }
                    }
                });
            }
            catch (InterruptedException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExceptionUtils.disposeDialogInEDT ", e);
                //ok, just to be paranoid
            }
            catch (InvocationTargetException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExceptionUtils.disposeDialogInEDT ", e);
                //ok, just to be paranoid
            }
        }

    }


    public static void disposeDialogInEDT(@NotNull final InternalProgressDialog progressDialog)
    {
        if (SwingUtilities.isEventDispatchThread())
        {
            try
            {
                progressDialog.dispose();
            }
            catch (Throwable e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExceptionUtils.disposeDialogInEDT ", e);
                //ok, just to be paranoid
            }
        }
        else
        {
            try
            {
                EventQueue.invokeAndWait(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            progressDialog.dispose();
                        }
                        catch (Throwable e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExceptionUtils.disposeDialogInEDT ", e);
                            //ok, just to be paranoid
                        }
                    }
                });
            }
            catch (InterruptedException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExceptionUtils.disposeDialogInEDT ", e);
                //ok, just to be paranoid
            }
            catch (InvocationTargetException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExceptionUtils.disposeDialogInEDT ", e);
                //ok, just to be paranoid
            }
        }
    }
}
