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
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 19:24:51
 */
public class FontHigherThanElementInspection extends AbstractElementTestInspection
{
    protected void inspectElement(@NotNull ReportElement reportElement, @NotNull ArrayList<InspectionResult> inspectionResults)
    {
        if (reportElement instanceof TextReportElement && !(reportElement instanceof SubReportElement))
        {
            TextReportElement textReportElement = (TextReportElement) reportElement;
            if (isFontHigherThanElement(textReportElement))
            {
                HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                locationInfos.add(new LocationInfo(reportElement, PropertyKeys.MINIMUM_SIZE));
                inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                           TranslationManager.getInstance().getTranslation("R", "FontHigherThanElementInspection.Summary"),
                                                           TranslationManager.getInstance().getTranslation("R", "FontHigherThanElementInspection.Description"),
                                                           null, locationInfos));
            }
        }
    }


    private boolean isFontHigherThanElement(@NotNull TextReportElement textReportElement)
    {
        Report report = textReportElement.getReport();
        if (report != null && report.isUseMaxCharBounds())
        {
            Rectangle2D maxCharBounds = textReportElement.getFont().getMaxCharBounds(new FontRenderContext(null, true, true));
            return Math.max(maxCharBounds.getHeight(), textReportElement.getLineHeight()) > textReportElement.getRectangle().getHeight() && !textReportElement.isDynamicContent();
        }
        else
        {
            return Math.max(textReportElement.getFont().getSize(), textReportElement.getLineHeight()) > textReportElement.getRectangle().getHeight() && !textReportElement.isDynamicContent();
        }
    }

}
