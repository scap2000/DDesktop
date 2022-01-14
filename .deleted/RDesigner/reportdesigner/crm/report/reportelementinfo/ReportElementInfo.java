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
package org.pentaho.reportdesigner.crm.report.reportelementinfo;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;

import javax.swing.*;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 11:56:50
 */
public abstract class ReportElementInfo
{
    @NotNull
    private Icon icon;
    @NotNull
    private String title;


    protected ReportElementInfo(@NotNull Icon icon, @NotNull @NonNls String title)
    {
        this.icon = icon;
        this.title = title;
    }


    @NotNull
    public abstract ReportElement createReportElement();


    @NotNull
    public Icon getIcon()
    {
        return icon;
    }


    @NotNull
    public String getTitle()
    {
        return title;
    }


    protected void setIcon(@NotNull Icon icon)
    {
        this.icon = icon;
    }


    protected void setTitle(@NotNull String title)
    {
        this.title = title;
    }
}
