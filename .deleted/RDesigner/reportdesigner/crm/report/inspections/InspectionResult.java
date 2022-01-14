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

import java.util.HashSet;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 18:43:37
 */
public class InspectionResult
{
    public enum Severity
    {
        @NotNull HINT,
        @NotNull WARNING,
        @NotNull ERROR
    }

    @NotNull
    private Severity severity;

    @NotNull
    private String summary;
    @NotNull
    private String description;
    @Nullable
    private QuickFix quickFix;
    @NotNull
    private HashSet<LocationInfo> locationInfos;


    public InspectionResult(@NotNull Severity severity, @NotNull String summary, @NotNull String description, @Nullable QuickFix quickFix, @NotNull HashSet<LocationInfo> locationInfos)
    {
        //noinspection ConstantConditions
        if (severity == null)
        {
            throw new IllegalArgumentException("severity must not be null");
        }
        //noinspection ConstantConditions
        if (summary == null)
        {
            throw new IllegalArgumentException("summary must not be null");
        }
        //noinspection ConstantConditions
        if (description == null)
        {
            throw new IllegalArgumentException("description must not be null");
        }
        //noinspection ConstantConditions
        if (locationInfos == null)
        {
            throw new IllegalArgumentException("locationInfos must not be null");
        }
        this.severity = severity;
        this.summary = summary;
        this.description = description;
        this.quickFix = quickFix;
        this.locationInfos = locationInfos;
    }


    @NotNull
    public Severity getSeverity()
    {
        return severity;
    }


    @NotNull
    public String getSummary()
    {
        return summary;
    }


    @NotNull
    public String getDescription()
    {
        return description;
    }


    @Nullable
    public QuickFix getQuickFix()
    {
        return quickFix;
    }


    @NotNull
    public HashSet<LocationInfo> getLocationInfos()
    {
        return locationInfos;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final InspectionResult that = (InspectionResult) o;

        if (!description.equals(that.description)) return false;
        if (!locationInfos.equals(that.locationInfos)) return false;
        if (severity != that.severity) return false;
        return summary.equals(that.summary);

    }


    public int hashCode()
    {
        int result;
        result = severity.hashCode();
        result = 29 * result + summary.hashCode();
        result = 29 * result + description.hashCode();
        result = 29 * result + locationInfos.hashCode();
        return result;
    }
}
