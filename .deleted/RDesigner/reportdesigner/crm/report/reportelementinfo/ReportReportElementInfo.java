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
import org.pentaho.reportdesigner.crm.report.model.BandToplevelType;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
import org.pentaho.reportdesigner.crm.report.model.PageFormatPreset;
import org.pentaho.reportdesigner.crm.report.model.PageOrientation;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

/**
 * User: Martin
 * Date: 09.01.2006
 * Time: 08:06:52
 */
public class ReportReportElementInfo extends ReportElementInfo
{
    public ReportReportElementInfo()
    {
        super(IconLoader.getInstance().getReportReportElementIcon(), TranslationManager.getInstance().getTranslation("R", "ReportElementInfo.Report"));
    }


    @NotNull
    public Report createReportElement()
    {
        Report report = new Report(ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.REPORT_HEADER).createReportElement(),
                                   ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.REPORT_FOOTER).createReportElement(),
                                   ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.PAGE_HEADER).createReportElement(),
                                   ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.PAGE_FOOTER).createReportElement(),
                                   ReportElementInfoFactory.getInstance().getReportGroupsElementInfo().createReportElement(),
                                   ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.ITEM_BAND).createReportElement(),
                                   ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.WATERMARK).createReportElement(),
                                   ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.NO_DATA_BAND).createReportElement(),
                                   new PageDefinition(PageFormatPreset.A4, PageOrientation.PORTRAIT, 20, 20, 20, 20),
                                   ReportElementInfoFactory.getInstance().getReportFunctionsElementInfo().createReportElement());
        return report;
    }
}
