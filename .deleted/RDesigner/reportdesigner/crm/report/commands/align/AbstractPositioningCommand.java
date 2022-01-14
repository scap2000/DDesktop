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
package org.pentaho.reportdesigner.crm.report.commands.align;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.commands.CommandKeys;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionsElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroups;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;

/**
 * User: Martin
 * Date: 11.07.2006
 * Time: 20:51:18
 */
public abstract class AbstractPositioningCommand extends AbstractCommand
{
    public AbstractPositioningCommand(@NotNull @NonNls String name)
    {
        super(name);
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null && reportElements.length > 1)
        {
            boolean ok = true;
            for (ReportElement reportElement : reportElements)
            {
                if (reportElement instanceof BandToplevelReportElement ||
                    reportElement instanceof Report ||
                    reportElement instanceof ReportGroups ||
                    reportElement instanceof ReportFunctionsElement ||
                    reportElement instanceof DataSetsReportElement)
                {
                    ok = false;
                }
            }
            event.getPresentation().setEnabled(ok);
        }
        else
        {
            event.getPresentation().setEnabled(false);
        }
    }
}
