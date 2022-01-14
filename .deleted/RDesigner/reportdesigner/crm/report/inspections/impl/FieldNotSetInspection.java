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
import org.pentaho.reportdesigner.crm.report.model.AnchorFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.DrawableFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageURLFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import java.lang.reflect.InvocationTargetException;
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
public class FieldNotSetInspection extends AbstractElementTestInspection
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(FieldNotSetInspection.class.getName());


    protected void inspectElement(@NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (reportElement instanceof TextFieldReportElement)
        {
            TextFieldReportElement textFieldReportElement = (TextFieldReportElement) reportElement;
            String fieldName = textFieldReportElement.getFieldName();
            if (fieldName.length() == 0 && textFieldReportElement.getFormula().getText().length() == 0)
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.FIELD_NAME));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Description"), null, locationInfos));
            }
        }
        else if (reportElement instanceof MessageFieldReportElement)
        {
            MessageFieldReportElement messageFieldReportElement = (MessageFieldReportElement) reportElement;
            if (!hasField(messageFieldReportElement.getFormatString()))
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.FORMAT_STRING));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Description"),
                                                           null, locationInfos));
            }
        }
        else if (reportElement instanceof ReportGroup)
        {
            ReportGroup group = (ReportGroup) reportElement;
            String[] groupFields = group.getGroupFields();
            if (groupFields.length == 0 && !"default".equals(group.getName()))//NON-NLS
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.GROUP_FIELDS));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Description"),
                                                           null, locationInfos));
            }
        }
        else if (reportElement instanceof ImageFieldReportElement)
        {
            ImageFieldReportElement imageFieldReportElement = (ImageFieldReportElement) reportElement;
            String fieldName = imageFieldReportElement.getFieldName();
            if (fieldName.length() == 0 && imageFieldReportElement.getFormula().getText().length() == 0)
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.FIELD_NAME));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Description"),
                                                           null, locationInfos));
            }
        }
        else if (reportElement instanceof ImageURLFieldReportElement)
        {
            ImageURLFieldReportElement imageURLFieldReportElement = (ImageURLFieldReportElement) reportElement;
            String fieldName = imageURLFieldReportElement.getFieldName();
            if (fieldName.length() == 0 && imageURLFieldReportElement.getFormula().getText().length() == 0)
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.FIELD_NAME));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Description"),
                                                           null, locationInfos));
            }
        }
        else if (reportElement instanceof AnchorFieldReportElement)
        {
            AnchorFieldReportElement anchorFieldReportElement = (AnchorFieldReportElement) reportElement;
            String fieldName = anchorFieldReportElement.getFieldName();
            if (fieldName.length() == 0)
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.FIELD_NAME));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Description"),
                                                           null, locationInfos));
            }
        }
        else if (reportElement instanceof DrawableFieldReportElement)
        {
            DrawableFieldReportElement drawableFieldReportElement = (DrawableFieldReportElement) reportElement;
            String fieldName = drawableFieldReportElement.getFieldName();
            if (fieldName.length() == 0 && drawableFieldReportElement.getFormula().getText().length() == 0)
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.FIELD_NAME));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Description"),
                                                           null, locationInfos));
            }
        }
        else if (reportElement instanceof ReportFunctionElement)
        {
            ReportFunctionElement functionElement = (ReportFunctionElement) reportElement;
            try
            {
                Method method = functionElement.getClass().getMethod("getField");//NON-NLS
                Object value = method.invoke(functionElement);
                if (value == null || String.valueOf(value).length() == 0)
                {
                    HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                    locationInfos.add(new LocationInfo(reportElement, PropertyKeys.FIELD));
                    inspectionResults.add(new InspectionResult(InspectionResult.Severity.ERROR,
                                                               TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Summary"),
                                                               TranslationManager.getInstance().getTranslation("R", "FieldNotSetInspection.Description"),
                                                               null, locationInfos));
                }
            }
            catch (NoSuchMethodException e)
            {
                //this one is extremely annoying
                //if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FieldNotSetInspection.inspectElement ", e);
            }
            catch (IllegalAccessException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FieldNotSetInspection.inspectElement ", e);
            }
            catch (InvocationTargetException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FieldNotSetInspection.inspectElement ", e);
            }
        }
    }


    private boolean hasField(@NotNull String pattern)
    {
        int start = -1;
        while ((start = pattern.indexOf("$(", start + 1)) != -1)
        {
            int end = pattern.indexOf(')', start + 1);
            if (end != -1)
            {
                //String s = pattern.substring(start + 2, end);//field, but not used here
                return true;
            }
        }

        return false;
    }
}
