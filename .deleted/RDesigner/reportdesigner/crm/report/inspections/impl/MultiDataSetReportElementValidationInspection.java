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

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.inspections.LocationInfo;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 03.02.2006
 * Time: 12:18:18
 */
public class MultiDataSetReportElementValidationInspection extends AbstractElementTestInspection
{
    protected void inspectElement(@NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (reportElement instanceof MultiDataSetReportElement)
        {
            MultiDataSetReportElement dataSetReportElement = (MultiDataSetReportElement) reportElement;
            if (dataSetReportElement.getConnectionType() == MultiDataSetReportElement.ConnectionType.JNDI)
            {
                if (dataSetReportElement.getSelectedJNDIDataSource() == null)
                {
                    HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                    locationInfos.add(new LocationInfo(dataSetReportElement));
                    inspectionResults.add(new InspectionResult(InspectionResult.Severity.ERROR,
                                                               TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementValidationInspection.SourceNotSelectedSummary"),
                                                               TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementValidationInspection.SourceNotSelectedDescription"),
                                                               null, locationInfos));
                }
            }
            else if (dataSetReportElement.getConnectionType() == MultiDataSetReportElement.ConnectionType.XQuery)
            {
                if (dataSetReportElement.getXQueryDataFile() == null)
                {
                    HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                    locationInfos.add(new LocationInfo(dataSetReportElement));
                    inspectionResults.add(new InspectionResult(InspectionResult.Severity.ERROR,
                                                               TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementValidationInspection.XQueryDataFileNotSelectedSummary"),
                                                               TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElementValidationInspection.XQueryDataFileNotSelectedDescription"),
                                                               null, locationInfos));
                }
            }
        }
    }
}