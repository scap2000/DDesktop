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
import org.pentaho.reportdesigner.crm.report.inspections.Inspection;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.inspections.LocationInfo;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElement;
import org.pentaho.reportdesigner.crm.report.util.ReportElementUtilities;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 19:24:51
 */
public class OverlappingTextElementsInspection implements Inspection
{
    @NotNull
    public ArrayList<InspectionResult> inspect(@NotNull Report report)
    {
        ArrayList<InspectionResult> inspectionResults = new ArrayList<InspectionResult>();

        ArrayList<BandToplevelReportElement> bandToplevelReportElements = new ArrayList<BandToplevelReportElement>();
        collectBandsToplevel(bandToplevelReportElements, report);

        for (BandToplevelReportElement bandToplevelReportElement : bandToplevelReportElements)
        {
            ArrayList<TextReportElement> textReportElements = new ArrayList<TextReportElement>();
            collect(textReportElements, bandToplevelReportElement);

            for (int i = 0; i < textReportElements.size(); i++)
            {
                TextReportElement textReportElement1 = textReportElements.get(i);
                for (int j = 0; j < textReportElements.size(); j++)
                {
                    TextReportElement textReportElement2 = textReportElements.get(j);
                    if (i != j)
                    {
                        Rectangle2D.Double rect1 = new Rectangle2D.Double();
                        rect1.setRect(textReportElement1.getRectangle());
                        ReportElementUtilities.convertRectangle(rect1, textReportElement1, null);

                        Rectangle2D.Double rect2 = new Rectangle2D.Double();
                        rect2.setRect(textReportElement2.getRectangle());
                        ReportElementUtilities.convertRectangle(rect2, textReportElement2, null);

                        if (MathUtils.intersectRectangles(rect1, rect2))
                        {
                            HashSet<LocationInfo> locationInfos = new HashSet<LocationInfo>();
                            locationInfos.add(new LocationInfo(textReportElement1, null));
                            locationInfos.add(new LocationInfo(textReportElement2, null));
                            inspectionResults.add(new InspectionResult(InspectionResult.Severity.WARNING,
                                                                       TranslationManager.getInstance().getTranslation("R", "OverlappingTextElementsInspection.Summary"),
                                                                       TranslationManager.getInstance().getTranslation("R", "OverlappingTextElementsInspection.Description"),
                                                                       null, locationInfos));
                        }
                    }
                }
            }
        }

        return inspectionResults;
    }


    private void collectBandsToplevel(@NotNull ArrayList<BandToplevelReportElement> bandToplevelReportElements, @NotNull ReportElement reportElement)
    {
        if (reportElement instanceof BandToplevelReportElement)
        {
            BandToplevelReportElement bandToplevelReportElement = (BandToplevelReportElement) reportElement;
            bandToplevelReportElements.add(bandToplevelReportElement);
        }
        else
        {
            ArrayList<ReportElement> children = reportElement.getChildren();
            for (ReportElement element : children)
            {
                collectBandsToplevel(bandToplevelReportElements, element);
            }
        }
    }


    private void collect(@NotNull ArrayList<TextReportElement> textReportElements, @NotNull ReportElement reportElement)
    {
        if (reportElement instanceof TextReportElement && !(reportElement instanceof SubReportElement))
        {
            textReportElements.add((TextReportElement) reportElement);
        }

        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement child : children)
        {
            collect(textReportElements, child);
        }
    }
}
