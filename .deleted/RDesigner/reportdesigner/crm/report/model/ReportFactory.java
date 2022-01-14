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
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataSetPlugin;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.templateplugin.IndentedLayoutPlugin;
import org.pentaho.reportdesigner.crm.report.templateplugin.LayoutException;
import org.pentaho.reportdesigner.crm.report.templateplugin.LayoutPlugin;
import org.pentaho.reportdesigner.crm.report.templateplugin.LayoutStyle;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import java.util.HashMap;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 15:57:25
 */
@NonNls
public class ReportFactory
{
    private ReportFactory()
    {
    }


    @NotNull
    public static Report createEmptyReport()
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


    @NotNull
    public static SubReport createEmptySubReport()
    {
        SubReport subReport = new SubReport(ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.REPORT_HEADER).createReportElement(),
                                            ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.REPORT_FOOTER).createReportElement(),
                                            ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.PAGE_HEADER).createReportElement(),
                                            ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.PAGE_FOOTER).createReportElement(),
                                            ReportElementInfoFactory.getInstance().getReportGroupsElementInfo().createReportElement(),
                                            ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.ITEM_BAND).createReportElement(),
                                            ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.WATERMARK).createReportElement(),
                                            ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.NO_DATA_BAND).createReportElement(),
                                            new PageDefinition(PageFormatPreset.A4, PageOrientation.PORTRAIT, 20, 20, 20, 20),
                                            ReportElementInfoFactory.getInstance().getReportFunctionsElementInfo().createReportElement());
        return subReport;
    }


    @NotNull
    public static Report createReport(@NotNull HashMap<String, WizardData> wizardDatas)
    {
        Report report = ReportElementInfoFactory.getInstance().getReportReportElementInfo().createReportElement();
        @Nullable
        WizardData wizardData = wizardDatas.get(WizardData.LAYOUT_PLUGIN);
        LayoutPlugin layoutPlugin = null;
        if (wizardData != null && wizardData.getValue() != null && wizardData.getValue() instanceof LayoutPlugin)
        {
            layoutPlugin = (LayoutPlugin) wizardData.getValue();
        }

        if (layoutPlugin == null)
        {
            layoutPlugin = new IndentedLayoutPlugin();
        }

        LayoutStyle layoutStyle = LayoutStyle.BLUE;
        @Nullable
        WizardData wizardDataLayoutStyle = wizardDatas.get(WizardData.LAYOUT_PLUGIN);
        if (wizardDataLayoutStyle != null)
        {
            Object layoutStyleObject = wizardDataLayoutStyle.getValue();
            if (layoutStyleObject instanceof LayoutStyle)
            {
                layoutStyle = (LayoutStyle) layoutStyleObject;
            }
        }

        try
        {
            layoutPlugin.addVisualReportElements(report, wizardDatas, layoutStyle);
        }
        catch (LayoutException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }

        @Nullable
        WizardData wizardDataDataSetPlugin = wizardDatas.get(WizardData.DATA_SET_PLUGIN);
        if (wizardDataDataSetPlugin == null || wizardDataDataSetPlugin.getValue() == null || !(wizardDataDataSetPlugin.getValue() instanceof DataSetPlugin))
        {
            throw new RuntimeException("Could not create report. DataSetPlugin was expected but not available.");
        }

        DataSetPlugin dataSetPlugin = (DataSetPlugin) wizardDataDataSetPlugin.getValue();
        if (dataSetPlugin != null)
        {
            DataSetReportElement dataSet = dataSetPlugin.createDataSet(wizardDatas);
            report.getDataSetsReportElement().addChild(dataSet);
        }

        return report;
    }

}
