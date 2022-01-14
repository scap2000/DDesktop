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
package org.pentaho.reportdesigner.crm.report.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * User: Martin
 * Date: 02.06.2006
 * Time: 10:19:10
 */
//perhaps inherit the other way or let borderdefinition use 4 linedefinitions or something like that.
public class LineDefinition extends BorderDefinition
{
    public LineDefinition()
    {
    }


    public LineDefinition(double borderWidth)
    {
        super(borderWidth);
    }


    public LineDefinition(@NotNull BasicStroke basicStroke)
    {
        super(basicStroke);
    }


    public LineDefinition(@NotNull Color borderColor, @NotNull BasicStroke basicStroke)
    {
        super(borderColor, basicStroke);
    }


    public LineDefinition(@NotNull Color borderColor, double borderWidth, int join, int cap, double miterlimit, @Nullable double[] dash, double dashPhase)
    {
        super(borderColor, borderWidth, join, cap, miterlimit, dash, dashPhase);
    }


    @NotNull
    public LineDefinition derive(double width)
    {
        return new LineDefinition(getColor(), width, getJoin(), getCap(), getMiterlimit(), getDash(), getDashPhase());
    }


    @NotNull
    public LineDefinition derive(@NotNull Color color)
    {
        return new LineDefinition(color, getWidth(), getJoin(), getCap(), getMiterlimit(), getDash(), getDashPhase());
    }

}
