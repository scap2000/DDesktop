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
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.inspections.LocationInfo;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 19:24:51
 */
public class MultipleFieldDefinitionInspection extends AbstractElementTestInspection
{
    @NotNull
    private HashMap<String, LinkedList<ReportElement>> definedFields = new HashMap<String, LinkedList<ReportElement>>();


    public void prepare()
    {
        definedFields.clear();
    }


    public void finish(@NotNull ArrayList<InspectionResult> inspectionResults)
    {
        Set<String> fields = definedFields.keySet();
        for (String fieldName : fields)
        {
            LinkedList<ReportElement> reportElements = definedFields.get(fieldName);
            if (reportElements.size() > 1)
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                for (ReportElement reportElement : reportElements)
                {
                    if (reportElement instanceof ReportFunctionElement)
                    {
                        locationInfos.add(new LocationInfo(reportElement, PropertyKeys.NAME));
                    }
                    else
                    {
                        locationInfos.add(new LocationInfo(reportElement, null));
                    }
                }
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "MultipleFieldDefinitionInspection.Summary", fieldName),
                                                           TranslationManager.getInstance().getTranslation("R", "MultipleFieldDefinitionInspection.Description"),
                                                           null, locationInfos));
            }
        }

        //cleanup
        definedFields.clear();
    }


    protected void inspectElement(@NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (reportElement instanceof DataSetReportElement)
        {
            DataSetReportElement dataSetReportElement = (DataSetReportElement) reportElement;
            HashSet<String> definedFields = dataSetReportElement.getDefinedFields();
            for (String fieldName : definedFields)
            {
                addDefinedField(fieldName, dataSetReportElement);
            }
        }
        else if (reportElement instanceof ReportFunctionElement)
        {
            ReportFunctionElement functionElement = (ReportFunctionElement) reportElement;
            if (functionElement.getName().length() > 0)
            {
                addDefinedField(functionElement.getName(), functionElement);
            }
        }
    }


    private void addDefinedField(@NotNull String fieldName, @NotNull ReportElement definingReportElement)
    {
        @Nullable
        LinkedList<ReportElement> reportElements = definedFields.get(fieldName);
        if (reportElements != null)
        {
            reportElements.add(definingReportElement);
        }
        else
        {
            reportElements = new LinkedList<ReportElement>();
            reportElements.add(definingReportElement);
            definedFields.put(fieldName, reportElements);
        }
    }
}
