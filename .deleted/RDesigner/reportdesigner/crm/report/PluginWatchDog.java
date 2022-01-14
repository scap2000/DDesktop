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
package org.pentaho.reportdesigner.crm.report;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.plugin.PluginWatcher;

import java.io.File;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 28.02.2006
 * Time: 11:40:41
 */
public class PluginWatchDog
{
    @NotNull
    private static final PluginWatchDog instance = new PluginWatchDog();


    @NotNull
    public static PluginWatchDog getInstance()
    {
        return instance;
    }


    @NotNull
    private ArrayList<PluginWatcher> pluginWatchers;


    private PluginWatchDog()
    {
        pluginWatchers = new ArrayList<PluginWatcher>();
    }


    public void setWatchFolder(@NotNull Object rootComponent, @NotNull File file, long millis)
    {
        PluginWatcher pluginWatcher = new PluginWatcher(rootComponent, file, millis);
        pluginWatchers.add(pluginWatcher);
    }


    @NotNull
    public ArrayList<PluginWatcher> getPluginWatchers()
    {
        return pluginWatchers;
    }
}
