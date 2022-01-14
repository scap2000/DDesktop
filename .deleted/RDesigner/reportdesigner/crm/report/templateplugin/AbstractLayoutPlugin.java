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
package org.pentaho.reportdesigner.crm.report.templateplugin;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.function.PageFunction;
import org.jfree.report.function.PageTotalFunction;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.DateFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionsElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElementHorizontalAlignment;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * User: Martin
 * Date: 28.02.2006
 * Time: 19:24:01
 */
public abstract class AbstractLayoutPlugin implements LayoutPlugin
{
    protected void composePageFooter(@NotNull ReportFunctionsElement reportFunctions, double pageWidth, @NotNull BandToplevelReportElement pageFooterBand)
    {
        PageFunction pageFunctionJFree = new PageFunction();
        //PageFunction pageFunction = (PageFunction) ReportElementInfoFactory.getInstance().getReportFunctionElementInfo(PageFunction.class.getName()).createReportElement();
        pageFunctionJFree.setPageIncrement(1);
        pageFunctionJFree.setStartPage(1);
        pageFunctionJFree.setName("currentPage");//NON-NLS
        reportFunctions.addChild(ExpressionRegistry.getInstance().createWrapperInstance(pageFunctionJFree));

        PageTotalFunction totalPagesFunctionJFree = new PageTotalFunction();
        //PageTotalFunction totalPagesFunction = (PageTotalFunction) ReportElementInfoFactory.getInstance().getReportFunctionElementInfo(PageTotalFunction.class.getName()).createReportElement();
        totalPagesFunctionJFree.setPageIncrement(1);
        totalPagesFunctionJFree.setStartPage(1);
        totalPagesFunctionJFree.setName("totalPages");//NON-NLS
        reportFunctions.addChild(ExpressionRegistry.getInstance().createWrapperInstance(totalPagesFunctionJFree));

        MessageFieldReportElement pageInfoMessageField = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
        pageInfoMessageField.setFormatString("$(currentPage)/$(totalPages)");//NON-NLS
        pageInfoMessageField.setPosition(new Point2D.Double(0, 0));
        pageInfoMessageField.setMinimumSize(new DoubleDimension(pageWidth, 16));
        pageInfoMessageField.setHorizontalAlignment(TextReportElementHorizontalAlignment.CENTER);
        pageFooterBand.addChild(pageInfoMessageField);
    }


    protected void composePageHeader(@NotNull ReportFunctionsElement reportFunctions, double pageWidth, @NotNull BandToplevelReportElement pageHeaderBand)
    {
        DateFieldReportElement currentDateField = ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo().createReportElement();
        currentDateField.setFieldName("report.date");//NON-NLS
        currentDateField.setPosition(new Point2D.Double(0, 0));
        currentDateField.setMinimumSize(new DoubleDimension(pageWidth, 16));
        currentDateField.setHorizontalAlignment(TextReportElementHorizontalAlignment.RIGHT);
        pageHeaderBand.addChild(currentDateField);
    }


    @NotNull
    protected Color getBackgroundColor(@NotNull Color baseColor, int groupCount, int currentGroup)
    {
        double colorOffset = 100. / (groupCount + 3);
        int r = (int) (baseColor.getRed() + colorOffset * currentGroup);
        int g = (int) (baseColor.getGreen() + colorOffset * currentGroup);
        int b = (int) (baseColor.getBlue() + colorOffset * currentGroup);

        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        Color color = new Color(r, g, b);
        return color;
    }
}
