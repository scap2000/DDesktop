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
package org.pentaho.reportdesigner.crm.report.templateplugin;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 27.02.2006
 * Time: 09:43:01
 */
public class LayoutPluginRegistry
{
    @NotNull
    private static final LayoutPluginRegistry instance = new LayoutPluginRegistry();


    @NotNull
    public static LayoutPluginRegistry getInstance()
    {
        return instance;
    }


    @NotNull
    private LinkedHashSet<LayoutPlugin> layoutPlugins;


    private LayoutPluginRegistry()
    {
        layoutPlugins = new LinkedHashSet<LayoutPlugin>();
    }


    public void registerLayoutPlugin(@NotNull LayoutPlugin layoutPlugin)
    {
        layoutPlugins.add(layoutPlugin);
    }


    @NotNull
    public LinkedHashSet<LayoutPlugin> getLayoutPlugins()
    {
        return new LinkedHashSet<LayoutPlugin>(layoutPlugins);
    }

}
