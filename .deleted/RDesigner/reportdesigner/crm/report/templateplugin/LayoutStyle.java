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
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * User: Martin
 * Date: 28.02.2006
 * Time: 13:52:33
 */
public enum LayoutStyle
{
    @NotNull BLUE(new Color(155, 155, 255), "dialog"),//NON-NLS
    @NotNull UNCOLORED(null, "dialog");//NON-NLS

    @Nullable
    private Color baseColor;
    @NotNull
    private String baseFontName;


    LayoutStyle(@Nullable Color baseColor, @NotNull String baseFontName)
    {
        this.baseColor = baseColor;

        //noinspection ConstantConditions
        if (baseFontName == null)
        {
            throw new IllegalArgumentException("baseFontName must not be null");
        }
        this.baseFontName = baseFontName;
    }


    @Nullable
    public Color getBaseColor()
    {
        return baseColor;
    }


    @NotNull
    public String getBaseFontName()
    {
        return baseFontName;
    }
}
