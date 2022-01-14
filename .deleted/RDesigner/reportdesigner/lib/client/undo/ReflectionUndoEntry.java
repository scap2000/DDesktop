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
package org.pentaho.reportdesigner.lib.client.undo;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 26.07.2006
 * Time: 16:17:40
 */
public class ReflectionUndoEntry extends UndoEntry
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(ReflectionUndoEntry.class.getName());

    @NotNull
    private Object obj;
    @NotNull
    private String methodName;
    @NotNull
    private Class argType;
    @Nullable
    private Object oldValue;
    @Nullable
    private Object newValue;


    public ReflectionUndoEntry(@NotNull Object obj, @NotNull String methodName, @NotNull Class argType, @Nullable Object oldValue, @Nullable Object newValue)
    {
        this.obj = obj;
        this.methodName = methodName;
        this.argType = argType;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }


    public void undo()
    {
        try
        {
            obj.getClass().getMethod(methodName, argType).invoke(obj, oldValue);
        }
        catch (IllegalAccessException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReflectionUndoEntry.undo ", e);
        }
        catch (InvocationTargetException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReflectionUndoEntry.undo ", e);
        }
        catch (NoSuchMethodException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReflectionUndoEntry.undo ", e);
        }
    }


    public void redo()
    {
        try
        {
            obj.getClass().getMethod(methodName, argType).invoke(obj, newValue);
        }
        catch (IllegalAccessException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReflectionUndoEntry.redo ", e);
        }
        catch (InvocationTargetException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReflectionUndoEntry.redo ", e);
        }
        catch (NoSuchMethodException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReflectionUndoEntry.redo ", e);
        }
    }
}
