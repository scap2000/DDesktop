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
package org.pentaho.reportdesigner.lib.client.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ComponentEvent;

/**
 * User: Martin
 * Date: 19.11.2004
 * Time: 13:47:58
 */
public class DefaultCommandEvent implements CommandEvent
{
    @Nullable
    private ComponentEvent inputEvent;
    @NotNull
    private DataContext dataContext;
    @NotNull
    private String place;
    @NotNull
    private Presentation presentation;


    public DefaultCommandEvent(@Nullable ComponentEvent inputEvent, @NotNull DataContext dataContext, @NotNull String place, @NotNull Presentation presentation)
    {
        this.inputEvent = inputEvent;
        this.dataContext = dataContext;
        this.place = place;
        this.presentation = presentation;
    }


    @Nullable
    public ComponentEvent getInputEvent()
    {
        return inputEvent;
    }


    @NotNull
    public DataContext getDataContext()
    {
        return dataContext;
    }


    @NotNull
    public String getPlace()
    {
        return place;
    }


    @NotNull
    public Presentation getPresentation()
    {
        return presentation;
    }

}
