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
import org.pentaho.reportdesigner.crm.report.model.BandToplevelPageReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelType;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 15:51:58
 */
public class BandToplevelPageReportElementInfo extends BandToplevelReportElementInfo
{

    public BandToplevelPageReportElementInfo(@NotNull BandToplevelType bandToplevelType)
    {
        super(bandToplevelType);
        if (bandToplevelType != BandToplevelType.PAGE_HEADER && bandToplevelType != BandToplevelType.PAGE_FOOTER)
        {
            throw new IllegalArgumentException("wrong type " + bandToplevelType);
        }
    }


    @NotNull
    public BandToplevelPageReportElement createReportElement()
    {
        BandToplevelPageReportElement bandToplevelReportElement = new BandToplevelPageReportElement();
        bandToplevelReportElement.setBandToplevelType(getBandToplevelType());

        bandToplevelReportElement.setDisplayOnFirstPage(true);
        bandToplevelReportElement.setDisplayOnLastPage(true);

        return bandToplevelReportElement;
    }

}
