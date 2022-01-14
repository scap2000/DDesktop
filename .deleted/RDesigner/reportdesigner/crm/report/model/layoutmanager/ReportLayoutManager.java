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

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 21.02.2006
 * Time: 09:27:40
 */
public abstract class ReportLayoutManager
{
    public enum Type
    {
        @NotNull STACKED,
        @NotNull NULL
    }


    public void layoutReportElement(@NotNull ReportElement reportElement)
    {
    }


    public void temporarilyLayoutReportElement(@NotNull ReportElement reportElement)
    {
    }


    /**
     * @param band
     * @return the insertion rect of possibleNewChild in the bands coordinate space
     */
    @NotNull
    public Rectangle2D.Double getDestinationRect(@NotNull ReportElement band, @NotNull ArrayList<ReportElement> elementsToInsert)
    {
        return new Rectangle2D.Double(0, 0, band.getMinimumSize().getWidth(), 10);
    }
}
