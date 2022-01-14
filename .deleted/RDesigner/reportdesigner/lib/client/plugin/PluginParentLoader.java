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
package org.pentaho.reportdesigner.lib.client.plugin;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 17.06.2005
 * Time: 10:25:44
 */
public class PluginParentLoader extends ClassLoader
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(PluginParentLoader.class.getName());

    @NotNull
    private HashMap<String, PluginInfo> pluginInfoMap;
    @NotNull
    private HashMap<String, PluginInfo> libraryInfoMap;


    public PluginParentLoader(@NotNull ClassLoader parent, @NotNull HashMap<String, PluginInfo> pluginInfoMap, @NotNull HashMap<String, PluginInfo> libraryInfoMap)
    {
        super(parent);

        this.pluginInfoMap = pluginInfoMap;
        this.libraryInfoMap = libraryInfoMap;
    }


    @NotNull
    public Class<?> loadClass(@NotNull String name) throws ClassNotFoundException
    {
        try
        {
            return super.loadClass(name);
        }
        catch (ClassNotFoundException e)
        {
            //try to find it in libraries
            Collection<PluginInfo> libraryInfos = libraryInfoMap.values();
            for (PluginInfo pluginInfo : libraryInfos)
            {
                try
                {
                    return pluginInfo.pluginLoader.loadClass(name, 1);
                }
                catch (ClassNotFoundException e1)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginParentLoader.loadClass ", e1);
                }
            }

            Collection<PluginInfo> pluginInfos = pluginInfoMap.values();
            for (PluginInfo pluginInfo : pluginInfos)
            {
                try
                {
                    return pluginInfo.pluginLoader.loadClass(name, 1);
                }
                catch (ClassNotFoundException e1)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginParentLoader.loadClass ", e1);
                }
            }
        }

        throw new ClassNotFoundException(name);
    }
}
