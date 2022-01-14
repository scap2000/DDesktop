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
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 15:51:58
 */
public class ReportFunctionElementInfo extends ReportElementInfo
{
    @NotNull
    private String className;


    public ReportFunctionElementInfo(@NotNull String className)
    {
        super(IconLoader.getInstance().getFunctionsIcon(), TranslationManager.getInstance().getTranslation("R", "ReportElementInfo.ReportFunction"));
        this.className = className;
    }


    @NotNull
    public ReportFunctionElement createReportElement()
    {

        ReportFunctionElement reportFunctionElement = ExpressionRegistry.getInstance().createWrapperInstance(className);
        if (reportFunctionElement == null)
        {
            throw new RuntimeException("Element for class '" + className + "' can not be created");
        }
        return reportFunctionElement;

    }
}
