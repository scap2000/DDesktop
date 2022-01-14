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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 14.01.2005
 * Time: 14:13:40
 */
public class PluginWatcher
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(PluginWatcher.class.getName());


    @NotNull
    private File folder;
    @NotNull
    private Thread watcherThread;

    @NotNull
    private HashMap<String, PluginInfo> pluginInfoMap;
    @NotNull
    private HashMap<String, PluginInfo> libraryInfoMap;
    @NotNull
    private Object rootComponent;

    @NotNull
    private PluginParentLoader pluginParentLoader;
    @NotNull
    private static final String JAR = ".jar";
    @NotNull
    private static final String UNKNOWN = "unknown";
    @NotNull
    private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
    @NotNull
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];


    public PluginWatcher(@NotNull Object rootComponent, @NotNull File folder, final long waitMillis)
    {
        this.rootComponent = rootComponent;
        this.folder = folder;
        pluginInfoMap = new HashMap<String, PluginInfo>();
        libraryInfoMap = new HashMap<String, PluginInfo>();

        pluginParentLoader = new PluginParentLoader(getClass().getClassLoader(), pluginInfoMap, libraryInfoMap);

        watcherThread = new Thread()
        {
            public void run()
            {
                //noinspection InfiniteLoopStatement
                while (true)
                {
                    try
                    {
                        updatePlugins();
                    }
                    catch (RuntimeException e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.run ", e);
                    }

                    try
                    {
                        Thread.sleep(waitMillis);
                    }
                    catch (InterruptedException e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.run ", e);
                    }

                }
            }
        };

        watcherThread.setPriority(Thread.MIN_PRIORITY);
        watcherThread.setDaemon(true);

        watcherThread.start();
    }


    @NotNull
    public HashMap<String, PluginInfo> getPluginInfoMap()
    {
        return pluginInfoMap;
    }


    @NotNull
    public PluginInfo getPlugin(@NotNull String classname)
    {
        return pluginInfoMap.get(classname);
    }


    private void updatePlugins()
    {
        HashMap<String, PluginInfo> oldPluginInfoMap = new HashMap<String, PluginInfo>(pluginInfoMap);
        HashMap<String, PluginInfo> oldLibraryInfoMap = new HashMap<String, PluginInfo>(libraryInfoMap);
        ArrayList<Plugin> pluginsToLoad = new ArrayList<Plugin>();

        File[] files = folder.listFiles();
        for (File file : files)
        {
            if (file.getName().endsWith(JAR))
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins file = " + file);

                try
                {
                    PluginLoader pluginLoader = new PluginLoader(pluginParentLoader, file);
                    String classname = pluginLoader.getMainPluginClassname();
                    int version = pluginLoader.getPluginVersion();
                    String[] depends = pluginLoader.getDepends();

                    if (classname != null)
                    {
                        PluginInfo pluginInfo = pluginInfoMap.get(classname);
                        if (pluginInfo == null)
                        {
                            Constructor constructor = pluginLoader.loadClass(classname).getConstructor(EMPTY_CLASS_ARRAY);
                            Plugin plugin = (Plugin) constructor.newInstance(EMPTY_OBJECT_ARRAY);

                            pluginInfo = new PluginInfo(pluginLoader, plugin, version, depends);
                            pluginInfoMap.put(classname, pluginInfo);
                            pluginsToLoad.add(plugin);
                        }
                        else
                        {
                            oldPluginInfoMap.remove(classname);

                            if (pluginInfo.version < version)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins found newer version of plugin: " + classname + "(" + pluginInfo.version + "->" + version + ")");
                                pluginInfo.plugin.unload();

                                Constructor constructor = pluginLoader.loadClass(classname).getConstructor(EMPTY_CLASS_ARRAY);
                                Plugin plugin = (Plugin) constructor.newInstance(EMPTY_OBJECT_ARRAY);

                                pluginInfo.pluginLoader = pluginLoader;
                                pluginInfo.plugin = plugin;
                                pluginInfo.version = version;
                                pluginInfo.depends = depends;

                                pluginsToLoad.add(plugin);
                            }
                        }
                    }
                    else
                    {
                        //it's a library
                        PluginInfo pluginInfo = libraryInfoMap.get(file.getName());
                        if (pluginInfo == null)
                        {
                            pluginInfo = new PluginInfo(pluginLoader, null, version, depends);
                            try
                            {
                                /*Constructor constructor = */
                                pluginLoader.loadClass(UNKNOWN).getConstructor();
                            }
                            catch (Exception e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins ", e);
                            }

                            libraryInfoMap.put(file.getName(), pluginInfo);
                        }
                        else
                        {
                            oldLibraryInfoMap.remove(classname);

                            if (pluginInfo.version < version)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins found newer version of plugin: " + classname + "(" + pluginInfo.version + "->" + version + ")");
                                pluginInfo.plugin.unload();

                                try
                                {
                                    /*Constructor constructor = */
                                    pluginLoader.loadClass(UNKNOWN).getConstructor();//just start preloading
                                }
                                catch (Exception e)
                                {
                                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins ", e);
                                }

                                pluginInfo.pluginLoader = pluginLoader;
                                pluginInfo.plugin = null;
                                pluginInfo.version = version;
                                pluginInfo.depends = depends;
                            }
                        }
                    }
                }
                catch (InstantiationException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins ", e);
                }
                catch (IllegalAccessException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins ", e);
                }
                catch (ClassNotFoundException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins ", e);
                }
                catch (NoSuchMethodException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins ", e);
                }
                catch (InvocationTargetException e)
                {
                    if (e.getCause() != null && e.getCause() instanceof NoClassDefFoundError)
                    {
                        throw new RuntimeException("Class could not be loaded. Verify the manifest file of the plugin and add a Depends entry. Missing class: " + e.getCause().getMessage());
                    }
                    else
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins ", e);
                    }
                }
                catch (IOException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins ", e);
                }
            }
        }

        for (Plugin plugin : pluginsToLoad)
        {
            plugin.load(rootComponent);
        }

        Collection collection = oldPluginInfoMap.values();
        for (Object o : collection)
        {
            PluginInfo pluginInfo = (PluginInfo) o;
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginWatcher.updatePlugins remove unused plugin: " + pluginInfo.plugin.getClass().getName() + " (" + pluginInfo.version + ")");
            pluginInfo.plugin.unload();
            pluginInfoMap.remove(pluginInfo.plugin.getClass().getName());
        }
    }

}
