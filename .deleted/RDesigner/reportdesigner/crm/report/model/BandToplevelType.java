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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * User: Martin
 * Date: 09.01.2006
 * Time: 13:47:09
 */
@NonNls
public enum BandToplevelType
{
    @NotNull REPORT_HEADER("BandToplevelType.ReportHeader"),
    @NotNull REPORT_FOOTER("BandToplevelType.ReportFooter"),
    @NotNull PAGE_HEADER("BandToplevelType.PageHeader"),
    @NotNull PAGE_FOOTER("BandToplevelType.PageFooter"),
    @NotNull ITEM_BAND("BandToplevelType.ItemBand"),
    @NotNull GROUP_HEADER("BandToplevelType.GroupHeader"),
    @NotNull GROUP_FOOTER("BandToplevelType.GroupFooter"),
    @NotNull WATERMARK("BandToplevelType.Watermark"),
    @NotNull NO_DATA_BAND("BandToplevelType.NoDataBand");

    @NotNull
    private String name;


    BandToplevelType(@NotNull String name)
    {
        this.name = name;
    }


    @NotNull
    public String getName()
    {
        return name;
    }
}
