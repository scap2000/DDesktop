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
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.inspections.Inspection;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;

import java.util.ArrayList;

/**
 * User: Martin
 * Date: 02.02.2006
 * Time: 10:31:38
 */
public abstract class AbstractElementTestInspection implements Inspection
{
    @Nullable
    private Report report;


    @Nullable
    public Report getReport()
    {
        return report;
    }


    public void prepare()
    {
    }


    @NotNull
    public ArrayList<InspectionResult> inspect(@NotNull Report report)
    {
        this.report = report;
        prepare();

        ArrayList<InspectionResult> inspectionResults = new ArrayList<InspectionResult>();
        visit(inspectionResults, report);

        finish(inspectionResults);

        this.report = null;//dont keep references around (GC)

        return inspectionResults;
    }


    public void finish(@NotNull ArrayList<InspectionResult> inspectionResults)
    {
    }


    private void visit(@NotNull ArrayList<InspectionResult> inspectionResults, @NotNull ReportElement reportElement)
    {
        inspectElement(reportElement, inspectionResults);

        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement child : children)
        {
            visit(inspectionResults, child);
        }
    }


    protected abstract void inspectElement(@NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults);
}
