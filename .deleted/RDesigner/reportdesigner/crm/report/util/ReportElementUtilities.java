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
package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.FilePath;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.TableModelDataSetReportElement;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: Martin
 * Date: 21.02.2006
 * Time: 10:15:59
 */
public class ReportElementUtilities
{
    private ReportElementUtilities()
    {
    }


    public static boolean canPreviewReport(@Nullable Report report)
    {
        if (report != null)
        {
            ArrayList<ReportElement> children = report.getDataSetsReportElement().getChildren();
            for (ReportElement reportElement : children)
            {
                if (reportElement instanceof TableModelDataSetReportElement)
                {
                    TableModelDataSetReportElement tableModelDataSetReportElement = (TableModelDataSetReportElement) reportElement;
                    if (tableModelDataSetReportElement.canFetchPreviewDataTableModel())
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public static boolean canCreateReport(@Nullable Report report)
    {
        if (report != null)
        {
            ArrayList<ReportElement> children = report.getDataSetsReportElement().getChildren();
            for (ReportElement reportElement : children)
            {
                if (reportElement instanceof TableModelDataSetReportElement)
                {
                    TableModelDataSetReportElement tableModelDataSetReportElement = (TableModelDataSetReportElement) reportElement;
                    if (tableModelDataSetReportElement.canFetchRealDataTableModel())
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    @NotNull
    public static ReportElement getDeepestReportElementAt(@NotNull Point2D.Double point, @NotNull BandToplevelReportElement bandToplevelReportElement, @NotNull Collection<ReportElement> excludedElements)
    {
        ArrayList<ReportElement> children = bandToplevelReportElement.getChildren();
        for (ReportElement reportElement : children)
        {
            if (!excludedElements.contains(reportElement) && reportElement.getRectangle().contains(point))
            {
                return getDeepestReportElementAt(point, reportElement, excludedElements, reportElement.getPosition().getX(), reportElement.getPosition().getY());//bandTopLevel always has a position of (0,0)
            }
        }

        return bandToplevelReportElement;
    }


    @NotNull
    private static ReportElement getDeepestReportElementAt(@NotNull Point2D.Double point, @NotNull ReportElement element, @NotNull Collection<ReportElement> excludedElements, double xParent, double yParent)
    {
        ArrayList<ReportElement> children = element.getChildren();
        for (ReportElement reportElement : children)
        {
            Rectangle2D.Double rectangle = new Rectangle2D.Double(xParent + reportElement.getRectangle().getX(), yParent + reportElement.getRectangle().getY(), reportElement.getRectangle().getWidth(), reportElement.getRectangle().getHeight());
            if (!excludedElements.contains(reportElement) && rectangle.contains(point))
            {
                return getDeepestReportElementAt(point, reportElement, excludedElements, xParent + reportElement.getRectangle().getX(), yParent + reportElement.getRectangle().getY());
            }
        }
        return element;
    }


    @Nullable
    public static BandReportElement getDeepestBandReportElement(@NotNull Point2D.Double point, @NotNull BandToplevelReportElement bandToplevelReportElement, @NotNull Collection<ReportElement> excludedElements)
    {
        ReportElement reportElement = getDeepestReportElementAt(point, bandToplevelReportElement, excludedElements);

        do
        {
            if (reportElement instanceof BandReportElement)
            {
                BandReportElement bandReportElement = (BandReportElement) reportElement;
                if (isShowInLayoutGUI(bandReportElement))
                {
                    return bandReportElement;
                }
            }
        } while ((reportElement = reportElement.getParent()) != null);

        return null;
    }


    public static void convertRectangle(@NotNull Rectangle2D.Double rect, @NotNull ReportElement source, @Nullable ReportElement target)
    {
        ReportElement temp = source.getParent();
        //noinspection ObjectEquality
        while (temp != target && temp != null)
        {
            rect.x += temp.getPosition().getX();
            rect.y += temp.getPosition().getY();

            temp = temp.getParent();
        }
    }


    public static void convertRectangleIncludingSource(@NotNull Rectangle2D.Double rect, @NotNull ReportElement source, @Nullable ReportElement target)
    {
        ReportElement temp = source;
        //noinspection ObjectEquality
        while (temp != target && temp != null)
        {
            rect.x += temp.getPosition().getX();
            rect.y += temp.getPosition().getY();

            temp = temp.getParent();
        }
    }


    public static void convertPointToInner(@NotNull Point2D.Double point, @NotNull ReportElement source, @Nullable ReportElement target)
    {
        ReportElement temp = source;
        //noinspection ObjectEquality
        while (temp != target && temp != null)
        {
            point.x -= temp.getPosition().getX();
            point.y -= temp.getPosition().getY();

            temp = temp.getParent();
        }
    }


    public static boolean isShowInLayoutGUI(@NotNull ReportElement reportElement)
    {
        ReportElement testElement = reportElement;
        do
        {
            if (testElement instanceof BandReportElement)
            {
                BandReportElement bandReportElement = (BandReportElement) testElement;
                if (!bandReportElement.isShowInLayoutGUI())
                {
                    return false;
                }
            }
        } while ((testElement = testElement.getParent()) != null);

        return true;
    }


    @Nullable
    public static SubReportElement findSubReportElement(@NotNull ReportElement reportElement, @NotNull FilePath filePath)
    {
        if (reportElement instanceof SubReportElement)
        {
            SubReportElement subReportElement = (SubReportElement) reportElement;
            if (subReportElement.getFilePath().equals(filePath))
            {
                return subReportElement;
            }
        }

        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement element : children)
        {
            SubReportElement subReportElement = findSubReportElement(element, filePath);
            if (subReportElement != null)
            {
                return subReportElement;
            }
        }
        return null;
    }
}
