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
package org.pentaho.reportdesigner.crm.report.model.layoutmanager;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 21.02.2006
 * Time: 09:28:04
 */
public class StackedReportLayoutManager extends ReportLayoutManager
{

    public void layoutReportElement(@NotNull ReportElement reportElement)
    {
        ArrayList<ReportElement> children = reportElement.getChildren();
        double width = reportElement.getMinimumSize().getWidth();
        double height = reportElement.getMinimumSize().getHeight();

        double v = (height / children.size());

        for (int i = 0; i < children.size(); i++)
        {
            ReportElement child = children.get(i);
            child.setPosition(new Point2D.Double(0, 0 + v * i));
            child.setMinimumSize(new DoubleDimension(width, v));
        }
    }


    public void temporarilyLayoutReportElement(@NotNull ReportElement reportElement)
    {
        ArrayList<ReportElement> children = reportElement.getChildren();
        double width = reportElement.getRectangle().getWidth();
        double height = reportElement.getRectangle().getHeight();

        double v = (height / children.size());

        for (int i = 0; i < children.size(); i++)
        {
            ReportElement child = children.get(i);
            child.getRectangle().setRect(0, 0 + v * i, width, v);
        }
    }


    @NotNull
    public Rectangle2D.Double getDestinationRect(@NotNull ReportElement band, @NotNull ArrayList<ReportElement> elementsToInsert)
    {
        int size = 1;
        if (band.getChildren().containsAll(elementsToInsert))
        {
            size = 0;
        }
        double height = band.getRectangle().getHeight() / (band.getChildren().size() + size);
        return new Rectangle2D.Double(0, band.getRectangle().getHeight() - height, band.getRectangle().getWidth(), height);
    }
}
