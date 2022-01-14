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
package org.pentaho.reportdesigner.crm.report.inspections.impl;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.inspections.LocationInfo;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 19:24:51
 */
public class ChartSeriesNamesAndValuesColumnDiffererentElementInspection extends AbstractElementTestInspection
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ChartSeriesNamesAndValuesColumnDiffererentElementInspection.class.getName());


    protected void inspectElement(@NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (reportElement instanceof ChartReportElement)
        {
            ChartReportElement chartReportElement = (ChartReportElement) reportElement;
            if (isSeriesNotSameSizeAsValues(chartReportElement))
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.MINIMUM_SIZE));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "ChartSeriesNamesAndValuesColumnDiffererentElementInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "ChartSeriesNamesAndValuesColumnDiffererentElementInspection.Description"),
                                                           null, locationInfos));
            }
        }
    }


    private boolean isSeriesNotSameSizeAsValues(@NotNull ChartReportElement chartReportElement)
    {
        ReportFunctionElement dataCollectorFunction = chartReportElement.getDataCollectorFunction();
        Class<? extends ReportFunctionElement> aClass = dataCollectorFunction.getClass();
        if (aClass.getName().contains("CategorySetCollectorFunction"))//NON-NLS
        {
            try
            {
                Method m1 = aClass.getMethod("getSeriesName");//NON-NLS
                Method m2 = aClass.getMethod("getValueColumn");//NON-NLS
                Object o1 = m1.invoke(dataCollectorFunction);
                Object o2 = m2.invoke(dataCollectorFunction);

                if (o1 instanceof Object[] && o2 instanceof Object[])
                {
                    Object[] a1 = (Object[]) o1;
                    Object[] a2 = (Object[]) o2;
                    if (a1.length != a2.length)
                    {
                        return true;
                    }
                }
            }
            catch (Exception e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ChartSeriesNamesAndValuesColumnDiffererentElementInspection.isSeriesNotSameSizeAsValues ", e);
            }
        }
        return false;
    }

}