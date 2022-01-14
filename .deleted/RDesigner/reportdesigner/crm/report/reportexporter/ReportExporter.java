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
package org.pentaho.reportdesigner.crm.report.reportexporter;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceLabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceMessageReportElement;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;

import java.util.List;

/**
 * User: Martin
 * Date: 28.10.2005
 * Time: 08:38:04
 */
public abstract class ReportExporter
{
    public abstract void exportReport(boolean isSubReport, @NotNull Report report) throws Exception;


    public void getImageElements(@NotNull ReportElement reportElement, @NotNull List<StaticImageReportElement> imageElements)
    {
        //noinspection ConstantConditions
        if (reportElement == null)
        {
            throw new IllegalArgumentException("reportElement must not be null");
        }
        //noinspection ConstantConditions
        if (imageElements == null)
        {
            throw new IllegalArgumentException("imageElements must not be null");
        }

        List children = reportElement.getChildren();
        for (Object aChildren : children)
        {
            if (aChildren instanceof ReportElement)
            {
                ReportElement child = (ReportElement) aChildren;
                getImageElements(child, imageElements);
                if (child instanceof StaticImageReportElement)
                {
                    imageElements.add((StaticImageReportElement) child);
                }
            }
        }
    }


    public void getSubReportElements(@NotNull ReportElement reportElement, @NotNull List<SubReportElement> subReportElements)
    {
        //noinspection ConstantConditions
        if (reportElement == null)
        {
            throw new IllegalArgumentException("reportElement must not be null");
        }
        //noinspection ConstantConditions
        if (subReportElements == null)
        {
            throw new IllegalArgumentException("subReportElements must not be null");
        }

        List children = reportElement.getChildren();
        for (Object aChildren : children)
        {
            if (aChildren instanceof ReportElement)
            {
                ReportElement child = (ReportElement) aChildren;
                getSubReportElements(child, subReportElements);
                if (child instanceof SubReportElement)
                {
                    subReportElements.add((SubReportElement) child);
                }
            }
        }
    }


    public void getResourceElements(@NotNull ReportElement reportElement, @NotNull List<ReportElement> reportElements)
    {
        //noinspection ConstantConditions
        if (reportElement == null)
        {
            throw new IllegalArgumentException("reportElement must not be null");
        }
        //noinspection ConstantConditions
        if (reportElements == null)
        {
            throw new IllegalArgumentException("reportElements must not be null");
        }

        List children = reportElement.getChildren();
        for (Object aChildren : children)
        {
            if (aChildren instanceof ReportElement)
            {
                ReportElement child = (ReportElement) aChildren;
                getResourceElements(child, reportElements);
                if (child instanceof ResourceFieldReportElement)
                {
                    reportElements.add(child);
                }
                else if (child instanceof ResourceMessageReportElement)
                {
                    reportElements.add(child);
                }
                else if (child instanceof ResourceLabelReportElement)
                {
                    reportElements.add(child);
                }
            }
        }
    }
}
