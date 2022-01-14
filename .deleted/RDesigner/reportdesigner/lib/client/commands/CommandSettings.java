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
/**
 * User: Martin
 * Date: 24.04.2006
 * Time: 13:34:45
 */
package org.pentaho.reportdesigner.lib.client.commands;

import org.jetbrains.annotations.NotNull;

public class CommandSettings
{
    @NotNull
    private static final CommandSettings instance = new CommandSettings();


    @NotNull
    public static CommandSettings getInstance()
    {
        return instance;
    }


    @NotNull
    public static final String SIZE_16 = "16*16";
    @NotNull
    public static final String SIZE_32 = "32*32";
    @NotNull
    public static final String SIZE_48 = "48*48";
    @NotNull
    public static final String SIZE_64 = "48*48";

    @NotNull
    private String toolbarIconKey;
    @NotNull
    private String menuIconKey;
    @NotNull
    private String popupmenuIconKey;
    @NotNull
    private String categoriesIconKey;


    private CommandSettings()
    {
        toolbarIconKey = SIZE_16;
        menuIconKey = SIZE_16;
        popupmenuIconKey = SIZE_16;
        categoriesIconKey = SIZE_16;
    }


    @NotNull
    public String getToolbarIconKey()
    {
        return toolbarIconKey;
    }


    public void setToolbarIconKey(@NotNull String toolbarIconKey)
    {
        this.toolbarIconKey = toolbarIconKey;
    }


    @NotNull
    public String getMenuIconKey()
    {
        return menuIconKey;
    }


    public void setMenuIconKey(@NotNull String menuIconKey)
    {
        this.menuIconKey = menuIconKey;
    }


    @NotNull
    public String getPopupmenuIconKey()
    {
        return popupmenuIconKey;
    }


    public void setPopupmenuIconKey(@NotNull String popupmenuIconKey)
    {
        this.popupmenuIconKey = popupmenuIconKey;
    }


    @NotNull
    public String getCategoriesIconKey()
    {
        return categoriesIconKey;
    }


    public void setCategoriesIconKey(@NotNull String categoriesIconKey)
    {
        this.categoriesIconKey = categoriesIconKey;
    }

}
