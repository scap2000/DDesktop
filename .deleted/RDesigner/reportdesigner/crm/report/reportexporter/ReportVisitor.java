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
package org.pentaho.reportdesigner.crm.report.reportexporter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertiesDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.staticfactory.StaticFactoryDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.AnchorFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.DateFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.DrawableFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.EllipseReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageURLFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.LabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.LineReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.NumberFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.RectangleReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionsElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.ReportGroups;
import org.pentaho.reportdesigner.crm.report.model.ResourceFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceLabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceMessageReportElement;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportDataElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;

/**
 * User: Martin
 * Date: 28.10.2005
 * Time: 08:52:06
 */
public abstract class ReportVisitor
{
    @Nullable
    public abstract Object getCurrentContext();


    public abstract void setCurrentContext(@Nullable Object context);


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull Report report) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull BandToplevelReportElement bandToplevelReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull BandReportElement bandReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull DateFieldReportElement dateFieldReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull LabelReportElement labelReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull MessageFieldReportElement messageFieldReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull NumberFieldReportElement numberFieldReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ReportGroup reportGroup) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ResourceFieldReportElement resourceFieldReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull TextFieldReportElement textFieldReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ReportFunctionsElement reportFunctionsElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ReportFunctionElement reportFunctionElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ReportGroups reportGroups) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull LineReportElement lineReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull StaticImageReportElement staticImageReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull DataSetsReportElement dataSetsReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull PropertiesDataSetReportElement propertiesDataSetReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull StaticFactoryDataSetReportElement staticFactoryDataSetReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull MultiDataSetReportElement multiDataSetReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull RectangleReportElement rectangleReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull EllipseReportElement ellipseReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ImageFieldReportElement imageFieldReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ImageURLFieldReportElement imageURLFieldReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull DrawableFieldReportElement drawableFieldReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull AnchorFieldReportElement anchorFieldReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ResourceMessageReportElement resourceMessageReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ResourceLabelReportElement resourceLabelReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull SubReportElement subReportElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull SubReportDataElement subReportDataElement) throws ReportCreationException;


    @Nullable
    public abstract Object visit(@Nullable Object parent, @NotNull ChartReportElement chartReportElement) throws ReportCreationException;
}
