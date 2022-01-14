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
public class NullReportLayoutManager extends ReportLayoutManager
{
    private boolean layoutInProgress;


    public void layoutReportElement(@NotNull ReportElement reportElement)
    {
        if (!layoutInProgress)
        {
            layoutInProgress = true;

            ArrayList<ReportElement> children = reportElement.getChildren();
            for (ReportElement element : children)
            {
                element.getOrigRectangle().setRect(element.getPosition().getX(), element.getPosition().getY(), element.getMinimumSize().getWidth(), element.getMinimumSize().getHeight());
                element.getRectangle().setRect(element.getPosition().getX(), element.getPosition().getY(), element.getMinimumSize().getWidth(), element.getMinimumSize().getHeight());

                //cut off width/height
                Rectangle2D bounds2D = new Rectangle2D.Double(0, 0, reportElement.getRectangle().width, reportElement.getRectangle().height);
                Rectangle2D rect = bounds2D.createIntersection(element.getRectangle());

                DoubleDimension ms = new DoubleDimension(rect.getWidth(), rect.getHeight());
                element.setMinimumSize(ms);

                if (element.getRectangle().x < 0)
                {
                    element.setPosition(new Point2D.Double(0, element.getPosition().getY()));
                }
                if (element.getRectangle().y < 0)
                {
                    element.setPosition(new Point2D.Double(element.getPosition().getX(), 0));
                }
                if (element.getRectangle().x + element.getRectangle().width > reportElement.getRectangle().width)
                {
                    element.setPosition(new Point2D.Double(reportElement.getRectangle().width - (element.getRectangle().x + element.getRectangle().width), element.getPosition().getY()));
                }
                if (element.getRectangle().y + element.getRectangle().height > reportElement.getRectangle().height)
                {
                    element.setPosition(new Point2D.Double(element.getPosition().getX(), reportElement.getRectangle().height - (element.getRectangle().y + element.getRectangle().height)));
                }
            }

            layoutInProgress = false;
        }
    }


    @NotNull
    public Rectangle2D.Double getDestinationRect(@NotNull ReportElement band, @NotNull ArrayList<ReportElement> elementsToInsert)
    {
        Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, band.getRectangle().width, band.getRectangle().height);
        return rect;
    }
}
