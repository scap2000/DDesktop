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

import java.util.HashMap;

/**
 * User: Martin
 * Date: 23.11.2004
 * Time: 19:34:44
 */
public class ApplicationCommandRegistry
{
    @NotNull
    private Long applicationId;

    @NotNull
    private HashMap<String, DefaultCommandGroup> placesCommandGroups;


    public ApplicationCommandRegistry(@NotNull Long applicationId)
    {
        this.applicationId = applicationId;

        placesCommandGroups = new HashMap<String, DefaultCommandGroup>();
    }


    @NotNull
    public DefaultCommandGroup getDefaultCommandGroup(@NotNull String place)
    {
        DefaultCommandGroup defaultCommandGroup = placesCommandGroups.get(place);
        if (defaultCommandGroup == null)
        {
            defaultCommandGroup = new DefaultCommandGroup("DefaultCommandGroup");//NON-NLS
            placesCommandGroups.put(place, defaultCommandGroup);
        }

        return defaultCommandGroup;
    }


    @NotNull
    public Long getApplicationId()
    {
        return applicationId;
    }


    public void registerDefaultCommandGroup(@NotNull String place, @NotNull DefaultCommandGroup defaultCommandGroup)
    {
        placesCommandGroups.put(place, defaultCommandGroup);
    }
}
