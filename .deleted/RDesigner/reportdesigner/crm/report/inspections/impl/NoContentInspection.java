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
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.inspections.LocationInfo;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 19:24:51
 */
public class NoContentInspection extends AbstractElementTestInspection
{
    private boolean contentFound;


    public void prepare()
    {
        contentFound = false;
    }


    public void finish(@NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (!contentFound)
        {
            inspectionResults.add(new InspectionResult(InspectionResult.Severity.HINT,
                                                       TranslationManager.getInstance().getTranslation("R", "NoContentInspection.Summary"),
                                                       TranslationManager.getInstance().getTranslation("R", "NoContentInspection.Description"),
                                                       null, new HashSet<LocationInfo>()));
        }
    }


    protected void inspectElement(@NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (contentFound)
        {
            return;
        }

        if (reportElement instanceof BandToplevelReportElement)
        {
            if (!reportElement.getChildren().isEmpty())
            {
                contentFound = true;
            }
        }
    }
}
