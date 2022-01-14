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
package org.pentaho.reportdesigner.crm.report.wizard;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 15.02.2006
 * Time: 11:08:54
 */
public enum Template
{
    @NotNull COLORED(IconLoader.getInstance().getTemplateColoredSampleIcon(), false, new Color(155, 155, 255), false),
    @NotNull GRID(IconLoader.getInstance().getTemplateGridSampleIcon(), true, null, false);

    @NotNull
    private ImageIcon sampleImageIcon;
    private boolean grid;
    @NotNull
    private Color bgColor;
    private boolean indent;


    Template(@NotNull ImageIcon sampleImageIcon, boolean grid, @NotNull Color bgColor, boolean indent)
    {
        this.sampleImageIcon = sampleImageIcon;
        this.grid = grid;
        this.bgColor = bgColor;
        this.indent = indent;
    }


    @NotNull
    public ImageIcon getSampleImageIcon()
    {
        return sampleImageIcon;
    }


    public boolean isGrid()
    {
        return grid;
    }


    @NotNull
    public Color getBgColor()
    {
        return bgColor;
    }


    public boolean isIndent()
    {
        return indent;
    }
}
