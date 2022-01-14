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
package org.pentaho.reportdesigner.crm.report.commands;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Martin
 * Date: 08.02.2006
 * Time: 09:01:10
 */
public class ReportElementList implements Cloneable
{
    @NotNull
    private ArrayList<ReportElement> reportElements;


    public ReportElementList(@NotNull ReportElement[] res)
    {
        reportElements = new ArrayList<ReportElement>(Arrays.asList(res));
    }


    @NotNull
    public ArrayList<ReportElement> getReportElements()
    {
        return reportElements;
    }


    @NotNull
    public ReportElementList clone() throws CloneNotSupportedException
    {
        ReportElementList reportElementList = (ReportElementList) super.clone();
        reportElementList.reportElements = new ArrayList<ReportElement>();
        for (int i = 0; i < reportElements.size(); i++)
        {
            ReportElement reportElement = reportElements.get(i);
            reportElementList.reportElements.add(reportElement.clone());
        }
        return reportElementList;
    }
}
