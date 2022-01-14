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
package org.pentaho.reportdesigner.crm.report.datasetplugin;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 27.02.2006
 * Time: 09:43:01
 */
public class DataSetPluginRegistry
{
    @NotNull
    private static final DataSetPluginRegistry instance = new DataSetPluginRegistry();


    @NotNull
    public static DataSetPluginRegistry getInstance()
    {
        return instance;
    }


    @NotNull
    private LinkedHashSet<DataSetPlugin> dataSetPlugins;
    @NotNull
    private LinkedHashSet<DataSetPluginRegistryListener> dataSetPluginRegistryListeners;


    private DataSetPluginRegistry()
    {
        dataSetPlugins = new LinkedHashSet<DataSetPlugin>();
        dataSetPluginRegistryListeners = new LinkedHashSet<DataSetPluginRegistryListener>();
    }


    public void registerDataSetPlugin(@NotNull DataSetPlugin dataSetPlugin)
    {
        if (dataSetPlugins.add(dataSetPlugin))
        {
            firePluginAdded(dataSetPlugin);
        }
    }


    public void deregisterDataSetPlugin(@NotNull DataSetPlugin dataSetPlugin)
    {
        if (dataSetPlugins.remove(dataSetPlugin))
        {
            firePluginRemoved(dataSetPlugin);
        }
    }


    @NotNull
    public LinkedHashSet<DataSetPlugin> getDataSetPlugins()
    {
        return new LinkedHashSet<DataSetPlugin>(dataSetPlugins);
    }


    public void addDataSetPluginRegistryListener(@NotNull DataSetPluginRegistryListener listener)
    {
        dataSetPluginRegistryListeners.add(listener);
    }


    public void removeDataSetPluginRegistryListener(@NotNull DataSetPluginRegistryListener listener)
    {
        dataSetPluginRegistryListeners.remove(listener);
    }


    private void firePluginAdded(@NotNull DataSetPlugin dataSetPlugin)
    {
        LinkedHashSet<DataSetPluginRegistryListener> listeners = new LinkedHashSet<DataSetPluginRegistryListener>(dataSetPluginRegistryListeners);
        for (DataSetPluginRegistryListener dataSetPluginRegistryListener : listeners)
        {
            dataSetPluginRegistryListener.pluginAdded(dataSetPlugin);
        }
    }


    private void firePluginRemoved(@NotNull DataSetPlugin dataSetPlugin)
    {
        LinkedHashSet<DataSetPluginRegistryListener> listeners = new LinkedHashSet<DataSetPluginRegistryListener>(dataSetPluginRegistryListeners);
        for (DataSetPluginRegistryListener dataSetPluginRegistryListener : listeners)
        {
            dataSetPluginRegistryListener.pluginRemoved(dataSetPlugin);
        }
    }

}
