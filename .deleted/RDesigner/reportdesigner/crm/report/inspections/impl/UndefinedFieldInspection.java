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
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.inspections.LocationInfo;
import org.pentaho.reportdesigner.crm.report.model.AnchorFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.DrawableFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageURLFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.SubReportDataElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 19:24:51
 */
public class UndefinedFieldInspection extends AbstractElementTestInspection
{
    @NotNull
    private static final String GET_FIELD = "getField";
    @NotNull
    private static final String GET_FIELD1 = "getField1";
    @NotNull
    private static final String GET_FIELD2 = "getField2";

    @NotNull
    private HashSet<String> definedFields = new HashSet<String>();
    @NotNull
    private HashMap<String, ReportElement> referencedFields = new HashMap<String, ReportElement>();


    public void prepare()
    {
        definedFields.clear();
        referencedFields.clear();

        definedFields.add("report.date");//NON-NLS
    }


    public void finish(@NotNull ArrayList<InspectionResult> inspectionResults)
    {
        Set<String> strings = referencedFields.keySet();
        strings.removeAll(definedFields);
        for (String field : referencedFields.keySet())
        {
            ReportElement reportElement = referencedFields.get(field);
            HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
            if (reportElement instanceof TextFieldReportElement)
            {
                TextFieldReportElement textFieldReportElement = (TextFieldReportElement) reportElement;
                locationInfos.add(new LocationInfo(textFieldReportElement, PropertyKeys.FIELD_NAME));
            }
            else if (reportElement instanceof MessageFieldReportElement)
            {
                MessageFieldReportElement messageFieldReportElement = (MessageFieldReportElement) reportElement;
                locationInfos.add(new LocationInfo(messageFieldReportElement, PropertyKeys.FORMAT_STRING));
            }
            else if (reportElement instanceof ImageFieldReportElement)
            {
                ImageFieldReportElement imageFieldReportElement = (ImageFieldReportElement) reportElement;
                locationInfos.add(new LocationInfo(imageFieldReportElement, PropertyKeys.FIELD_NAME));
            }
            else if (reportElement instanceof ImageURLFieldReportElement)
            {
                ImageURLFieldReportElement imageURLFieldReportElement = (ImageURLFieldReportElement) reportElement;
                locationInfos.add(new LocationInfo(imageURLFieldReportElement, PropertyKeys.FIELD_NAME));
            }
            else if (reportElement instanceof AnchorFieldReportElement)
            {
                AnchorFieldReportElement anchorFieldReportElement = (AnchorFieldReportElement) reportElement;
                locationInfos.add(new LocationInfo(anchorFieldReportElement, PropertyKeys.FIELD_NAME));
            }
            else if (reportElement instanceof DrawableFieldReportElement)
            {
                DrawableFieldReportElement drawableFieldReportElement = (DrawableFieldReportElement) reportElement;
                locationInfos.add(new LocationInfo(drawableFieldReportElement, PropertyKeys.FIELD_NAME));
            }
            else if (reportElement instanceof ReportFunctionElement)
            {
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.FIELD));
            }
            else if (reportElement instanceof ReportGroup)
            {
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.GROUP_FIELDS));
            }
            else
            {
                locationInfos.add(new LocationInfo(reportElement, null));
            }
            inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                       TranslationManager.getInstance().getTranslation("R", "UndefinedFieldInspection.Summary", field),
                                                       TranslationManager.getInstance().getTranslation("R", "UndefinedFieldInspection.Description"),
                                                       null, locationInfos));
        }

        //cleanup
        definedFields.clear();
        referencedFields.clear();
    }


    protected void inspectElement(@NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (reportElement instanceof DataSetReportElement)
        {
            DataSetReportElement dataSetReportElement = (DataSetReportElement) reportElement;
            definedFields.addAll(dataSetReportElement.getDefinedFields());
        }
        else if (reportElement instanceof SubReportDataElement)
        {
            SubReportDataElement dataSetReportElement = (SubReportDataElement) reportElement;
            definedFields.addAll(dataSetReportElement.getDefinedFields());
        }
        else if (reportElement instanceof ReportFunctionElement)
        {
            ReportFunctionElement functionElement = (ReportFunctionElement) reportElement;
            if (functionElement.getName().length() > 0)
            {
                definedFields.add(functionElement.getName());
            }

            checkFunction(functionElement);
        }
        else if (reportElement instanceof TextFieldReportElement)
        {
            TextFieldReportElement textFieldReportElement = (TextFieldReportElement) reportElement;
            String fieldName = textFieldReportElement.getFieldName();
            if (fieldName.length() > 0)
            {
                referencedFields.put(fieldName, textFieldReportElement);
            }
        }
        else if (reportElement instanceof ImageFieldReportElement)
        {
            ImageFieldReportElement imageFieldReportElement = (ImageFieldReportElement) reportElement;
            String fieldName = imageFieldReportElement.getFieldName();
            if (fieldName.length() > 0)
            {
                referencedFields.put(fieldName, imageFieldReportElement);
            }
        }
        else if (reportElement instanceof ImageURLFieldReportElement)
        {
            ImageURLFieldReportElement imageURLFieldReportElement = (ImageURLFieldReportElement) reportElement;
            String fieldName = imageURLFieldReportElement.getFieldName();
            if (fieldName.length() > 0)
            {
                referencedFields.put(fieldName, imageURLFieldReportElement);
            }
        }
        else if (reportElement instanceof AnchorFieldReportElement)
        {
            AnchorFieldReportElement anchorFieldReportElement = (AnchorFieldReportElement) reportElement;
            String fieldName = anchorFieldReportElement.getFieldName();
            if (fieldName.length() > 0)
            {
                referencedFields.put(fieldName, anchorFieldReportElement);
            }
        }
        else if (reportElement instanceof DrawableFieldReportElement)
        {
            DrawableFieldReportElement textFieldReportElement = (DrawableFieldReportElement) reportElement;
            String fieldName = textFieldReportElement.getFieldName();
            if (fieldName.length() > 0)
            {
                referencedFields.put(fieldName, textFieldReportElement);
            }
        }
        else if (reportElement instanceof MessageFieldReportElement)
        {
            MessageFieldReportElement messageFieldReportElement = (MessageFieldReportElement) reportElement;
            analyzeMessagePattern(messageFieldReportElement, messageFieldReportElement.getFormatString());
        }
        else if (reportElement instanceof ReportGroup)
        {
            ReportGroup group = (ReportGroup) reportElement;
            String[] groupFields = group.getGroupFields();
            if (groupFields.length != 0)
            {
                for (String groupField : groupFields)
                {
                    referencedFields.put(groupField, group);
                }
            }
        }
    }


    private void checkFunction(@NotNull ReportFunctionElement functionElement)
    {
        readField(functionElement, GET_FIELD);
        readField(functionElement, GET_FIELD1);
        readField(functionElement, GET_FIELD2);
    }


    private void readField(@NotNull ReportFunctionElement functionElement, @NotNull String methodName)
    {
        try
        {
            Method method = functionElement.getClass().getMethod(methodName);
            Object obj = method.invoke(functionElement);
            if (obj instanceof String[])
            {
                String[] strings = (String[]) obj;
                for (String field : strings)
                {
                    referencedFields.put(field, functionElement);
                }
            }
            else if (obj instanceof String)
            {
                String field = (String) obj;
                if (field.length() > 0)
                {
                    referencedFields.put(field, functionElement);
                }
            }
        }
        catch (Exception e)
        {
            //ok
        }
    }


    private void analyzeMessagePattern(@NotNull MessageFieldReportElement messageFieldReportElement, @NotNull String pattern)
    {
        int start = -1;
        while ((start = pattern.indexOf("$(", start + 1)) != -1)
        {
            int endComma = pattern.indexOf(',', start + 1);
            int endParan = pattern.indexOf(')', start + 1);
            if (endComma == -1)
            {
                endComma = Integer.MAX_VALUE;
            }
            int end = endParan;
            if (endComma < endParan)
            {
                end = endComma;
            }
            if (end != -1)
            {
                String s = pattern.substring(start + 2, end);
                referencedFields.put(s, messageFieldReportElement);
            }
        }
    }
}
