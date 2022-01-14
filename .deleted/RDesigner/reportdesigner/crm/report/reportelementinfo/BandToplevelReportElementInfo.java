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

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelType;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 15:51:58
 */
public class BandToplevelReportElementInfo extends ReportElementInfo
{
    @NotNull
    private BandToplevelType bandToplevelType;


    public BandToplevelReportElementInfo(@NotNull BandToplevelType bandToplevelType)
    {
        super(IconLoader.getInstance().getBandReportElementIcon(), TranslationManager.getInstance().getTranslation("R", bandToplevelType.getName()));
        if (bandToplevelType == BandToplevelType.GROUP_HEADER || bandToplevelType == BandToplevelType.PAGE_HEADER || bandToplevelType == BandToplevelType.REPORT_HEADER)
        {
            setIcon(IconLoader.getInstance().getBandHeaderReportElementIcon());
        }
        else if (bandToplevelType == BandToplevelType.GROUP_FOOTER || bandToplevelType == BandToplevelType.PAGE_FOOTER || bandToplevelType == BandToplevelType.REPORT_FOOTER)
        {
            setIcon(IconLoader.getInstance().getBandFooterReportElementIcon());
        }
        else if (bandToplevelType == BandToplevelType.ITEM_BAND)
        {
            setIcon(IconLoader.getInstance().getBandReportElementIcon());
        }
        else if (bandToplevelType == BandToplevelType.WATERMARK)
        {
            setIcon(IconLoader.getInstance().getBandWatermarkReportElementIcon());
        }
        else if (bandToplevelType == BandToplevelType.NO_DATA_BAND)
        {
            setIcon(IconLoader.getInstance().getBandReportElementIcon());
        }
        this.bandToplevelType = bandToplevelType;
    }


    @NotNull
    public BandToplevelReportElement createReportElement()
    {
        BandToplevelReportElement bandToplevelReportElement = new BandToplevelReportElement();
        bandToplevelReportElement.setBandToplevelType(getBandToplevelType());

        if (bandToplevelType == BandToplevelType.WATERMARK)
        {
            bandToplevelReportElement.setShowInLayoutGUI(false);
        }
        else if (bandToplevelType == BandToplevelType.NO_DATA_BAND)
        {
            bandToplevelReportElement.setShowInLayoutGUI(false);
        }
        return bandToplevelReportElement;
    }


    @NotNull
    public BandToplevelType getBandToplevelType()
    {
        return bandToplevelType;
    }


    @NotNull
    public String toString()
    {
        return "BandToplevelReportElementInfo{" +
               "bandToplevelType=" + bandToplevelType +
               "}";
    }
}
