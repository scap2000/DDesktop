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
package org.pentaho.reportdesigner.crm.report.reportelementinfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

/**
 * User: Martin
 * Date: 21.03.2006
 * Time: 08:50:12
 */
public class NullReportElementInfo extends ReportElementInfo
{
    public NullReportElementInfo()
    {
        super(IconLoader.getInstance().getNullReportElementIcon(), TranslationManager.getInstance().getTranslation("R", "NullElementInfo.Label"));
    }


    @NotNull
    public ReportElement createReportElement()
    {
        return new ReportElement()
        {
            public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor)
            {
            }
        };
    }
}
