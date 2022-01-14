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
package org.pentaho.reportdesigner.crm.report.inspections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 21:14:11
 */
public class LocationInfo
{
    @NotNull
    private ReportElement reportElement;
    @Nullable
    private String propertyName;


    public LocationInfo(@NotNull ReportElement reportElement)
    {
        this(reportElement, null);
    }


    public LocationInfo(@NotNull ReportElement reportElement, @Nullable String propertyName)
    {
        this.reportElement = reportElement;
        this.propertyName = propertyName;
    }


    @NotNull
    public ReportElement getReportElement()
    {
        return reportElement;
    }


    @Nullable
    public String getPropertyName()
    {
        return propertyName;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final LocationInfo that = (LocationInfo) o;

        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) return false;
        return !(!reportElement.equals(that.reportElement));

    }


    public int hashCode()
    {
        int result;
        result = (reportElement.hashCode());
        result = 29 * result + (propertyName != null ? propertyName.hashCode() : 0);
        return result;
    }


    @NotNull
    public String toString()
    {
        return "LocationInfo{" +
               "reportElement=" + reportElement +
               ", propertyName='" + propertyName + "'" +
               "}";
    }
}
