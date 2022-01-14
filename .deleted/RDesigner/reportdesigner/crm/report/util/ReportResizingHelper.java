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
import org.pentaho.reportdesigner.crm.report.lineal.GuideLine;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 08.02.2006
 * Time: 19:03:38
 */
public class ReportResizingHelper
{
    private ReportResizingHelper()
    {
    }


    public enum Alignment
    {
        @NotNull LEFT,
        @NotNull CENTER,
        @NotNull RIGHT
    }


    public static void increaseWidthAlign(double oldWidth, double newWidth, @NotNull Report report, @NotNull Alignment alignment)
    {
        //noinspection ConstantConditions
        if (report == null)
        {
            throw new IllegalArgumentException("report must not be null");
        }
        //noinspection ConstantConditions
        if (alignment == null)
        {
            throw new IllegalArgumentException("alignment must not be null");
        }

        if (oldWidth > newWidth)
        {
            throw new IllegalArgumentException("oldWidth (" + oldWidth + ") must be smaller than newWidth (" + newWidth + ")");
        }

        if (alignment == Alignment.LEFT)
        {
            return;
        }

        double additionalSpace = newWidth - oldWidth;
        increaseWidth(additionalSpace, report, alignment);

        LinkedHashSet<GuideLine> guideLines = report.getHorizontalLinealModel().getGuideLines();
        for (GuideLine guideLine : guideLines)
        {
            if (alignment == Alignment.CENTER)
            {
                report.getHorizontalLinealModel().setPosition(guideLine, guideLine.getPosition() + additionalSpace / 2);
            }
            else if (alignment == Alignment.RIGHT)
            {
                report.getHorizontalLinealModel().setPosition(guideLine, guideLine.getPosition() + additionalSpace);
            }
        }
    }


    private static void increaseWidth(double additionalSpace, @NotNull ReportElement reportElement, @NotNull Alignment alignment)
    {
        double toAdd = 0;
        if (alignment == Alignment.CENTER)
        {
            toAdd = additionalSpace / 2;
        }
        else if (alignment == Alignment.RIGHT)
        {
            toAdd = additionalSpace;
        }

        reportElement.setPosition(new Point2D.Double(reportElement.getPosition().getX() + toAdd, reportElement.getPosition().getY()));

        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement child : children)
        {
            increaseWidth(additionalSpace, child, alignment);
        }
    }


    public static void adjustWidthProportional(double oldWidth, double newWidth, @NotNull Report report)
    {
        //noinspection ConstantConditions
        if (report == null)
        {
            throw new IllegalArgumentException("report must not be null");
        }

        double sf = newWidth / oldWidth;
        adjustWidth(sf, report);

        LinkedHashSet<GuideLine> guideLines = report.getHorizontalLinealModel().getGuideLines();
        for (GuideLine guideLine : guideLines)
        {
            report.getHorizontalLinealModel().setPosition(guideLine, guideLine.getPosition() * sf);
        }
    }


    private static void adjustWidth(double sf, @NotNull ReportElement reportElement)
    {
        reportElement.setPosition(new Point2D.Double(reportElement.getPosition().getX() * sf, reportElement.getPosition().getY()/* * sf*/));
        reportElement.setMinimumSize(new DoubleDimension(reportElement.getMinimumSize().getWidth() * sf, reportElement.getMinimumSize().getHeight()/* * sf*/));
        reportElement.setPreferredSize(new DoubleDimension(reportElement.getPreferredSize().getWidth() * sf, reportElement.getPreferredSize().getHeight()/* * sf*/));
        reportElement.setMaximumSize(new DoubleDimension(reportElement.getMaximumSize().getWidth() * sf, reportElement.getMaximumSize().getHeight()/* * sf*/));

        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement child : children)
        {
            adjustWidth(sf, child);
        }
    }

}
