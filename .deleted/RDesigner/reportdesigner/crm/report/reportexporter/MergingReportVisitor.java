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
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertiesDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertyInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.staticfactory.StaticFactoryDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.AnchorFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelItemReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelPageReportElement;
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
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
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

import java.util.ArrayList;
import java.util.TreeSet;


/**
 * User: Martin
 * Date: 24.02.2006
 * Time: 07:55:20
 */
public class MergingReportVisitor extends ReportVisitor
{
    @NotNull
    private Report oldReport;


    public MergingReportVisitor(@NotNull Report oldReport)
    {
        //noinspection ConstantConditions
        if (oldReport == null)
        {
            throw new IllegalArgumentException("oldReport must not be null");
        }

        this.oldReport = oldReport;
    }


    @Nullable
    public Object getCurrentContext()
    {
        return null;
    }


    public void setCurrentContext(@Nullable Object context)
    {
    }


    @NotNull
    public Object visit(@Nullable Object parent, @NotNull Report report)
    {
        return oldReport;
    }


    @Nullable
    @SuppressWarnings({"ObjectEquality"})
    public Object visit(@Nullable Object parent, @NotNull BandToplevelReportElement bandToplevelReportElement) throws ReportCreationException
    {
        if (bandToplevelReportElement.getParent() instanceof ReportGroup)
        {
            return null;//1. already added by visit(ReportGroups) or 2. can be ignored
        }
        else
        {
            Report mergingReport = (Report) bandToplevelReportElement.getParent();

            if (mergingReport != null)
            {
                if (mergingReport.getReportHeaderBand() == bandToplevelReportElement)
                {
                    BandToplevelReportElement reportHeader = oldReport.getReportHeaderBand();
                    reportHeader.setPageBreakBefore(bandToplevelReportElement.isPageBreakBefore());
                    reportHeader.setPageBreakAfter(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(reportHeader, bandToplevelReportElement);
                    return reportHeader;
                }
                else if (mergingReport.getReportFooterBand() == bandToplevelReportElement)
                {
                    BandToplevelReportElement reportFooter = oldReport.getReportFooterBand();
                    reportFooter.setPageBreakBefore(bandToplevelReportElement.isPageBreakBefore());
                    reportFooter.setPageBreakAfter(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(reportFooter, bandToplevelReportElement);
                    return reportFooter;
                }
                else if (mergingReport.getPageHeaderBand() == bandToplevelReportElement)
                {
                    BandToplevelPageReportElement pageHeader = oldReport.getPageHeaderBand();
                    BandToplevelPageReportElement bandToplevelPageReportElement = (BandToplevelPageReportElement) bandToplevelReportElement;
                    pageHeader.setPageBreakBefore(bandToplevelReportElement.isPageBreakBefore());
                    pageHeader.setPageBreakAfter(bandToplevelReportElement.isPageBreakAfter());
                    pageHeader.setDisplayOnFirstPage(bandToplevelPageReportElement.isDisplayOnFirstPage());
                    pageHeader.setDisplayOnLastPage(bandToplevelPageReportElement.isDisplayOnLastPage());
                    applySizes(pageHeader, bandToplevelReportElement);
                    return pageHeader;
                }
                else if (mergingReport.getPageFooterBand() == bandToplevelReportElement)
                {
                    BandToplevelPageReportElement pageFooter = oldReport.getPageFooterBand();
                    BandToplevelPageReportElement bandToplevelPageReportElement = (BandToplevelPageReportElement) bandToplevelReportElement;
                    pageFooter.setPageBreakBefore(bandToplevelPageReportElement.isPageBreakBefore());
                    pageFooter.setPageBreakAfter(bandToplevelPageReportElement.isPageBreakAfter());
                    pageFooter.setDisplayOnFirstPage(bandToplevelPageReportElement.isDisplayOnFirstPage());
                    pageFooter.setDisplayOnLastPage(bandToplevelPageReportElement.isDisplayOnLastPage());
                    applySizes(pageFooter, bandToplevelReportElement);
                    return pageFooter;
                }
                else if (mergingReport.getItemBand() == bandToplevelReportElement)
                {
                    BandToplevelItemReportElement itemBand = oldReport.getItemBand();
                    itemBand.setPageBreakBefore(bandToplevelReportElement.isPageBreakBefore());
                    itemBand.setPageBreakAfter(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(itemBand, bandToplevelReportElement);
                    if (mergingReport.getItemBand().getRowBandingDefinition().isEnabled())
                    {
                        itemBand.setRowBandingDefinition(mergingReport.getItemBand().getRowBandingDefinition());
                    }
                    return itemBand;
                }
                else if (mergingReport.getWatermarkBand() == bandToplevelReportElement)
                {
                    BandToplevelReportElement watermark = oldReport.getWatermarkBand();
                    watermark.setPageBreakBefore(bandToplevelReportElement.isPageBreakBefore());
                    watermark.setPageBreakAfter(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(watermark, bandToplevelReportElement);
                    return watermark;
                }
                else if (mergingReport.getNoDataBand() == bandToplevelReportElement)
                {
                    BandToplevelReportElement noDataBand = oldReport.getNoDataBand();
                    noDataBand.setPageBreakBefore(bandToplevelReportElement.isPageBreakBefore());
                    noDataBand.setPageBreakAfter(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(noDataBand, bandToplevelReportElement);
                    return noDataBand;
                }
            }
        }
        throw new RuntimeException("eeek!");
    }


    private void applySizes(@NotNull ReportElement targetReportElement, @NotNull ReportElement sourceReportElement)
    {
        targetReportElement.setMinimumSize(sourceReportElement.getMinimumSize());
        targetReportElement.setPreferredSize(sourceReportElement.getPreferredSize());
        targetReportElement.setMaximumSize(sourceReportElement.getMaximumSize());
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull BandReportElement bandReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(bandReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull DateFieldReportElement dateFieldReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(dateFieldReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull LabelReportElement labelReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(labelReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull MessageFieldReportElement messageFieldReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(messageFieldReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull NumberFieldReportElement numberFieldReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(numberFieldReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ReportGroup reportGroup) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(reportGroup);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ResourceFieldReportElement resourceFieldReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(resourceFieldReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ResourceLabelReportElement resourceLabelReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(resourceLabelReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ChartReportElement chartReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(chartReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ResourceMessageReportElement resourceMessageReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(resourceMessageReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull TextFieldReportElement textFieldReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(textFieldReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ReportFunctionsElement reportFunctionsElement) throws ReportCreationException
    {
        if (parent instanceof Report)
        {
            return oldReport.getReportFunctions();
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ReportFunctionElement reportFunctionElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(reportFunctionElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ReportGroups reportGroups) throws ReportCreationException
    {
        if (parent instanceof Report)
        {
            if (oldReport.getReportGroups().getChildren().isEmpty())
            {
                oldReport.addChild(reportGroups);
            }
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull LineReportElement lineReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(lineReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull StaticImageReportElement staticImageReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(staticImageReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull DataSetsReportElement dataSetsReportElement) throws ReportCreationException
    {
        if (parent instanceof Report)
        {
            return oldReport.getDataSetsReportElement();
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull JDBCDataSetReportElement jdbcDataSetReportElement)
    {
        if (parent instanceof DataSetsReportElement)
        {
            DataSetsReportElement dataSetsReportElement = (DataSetsReportElement) parent;
            dataSetsReportElement.addChild(jdbcDataSetReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull StaticFactoryDataSetReportElement staticFactoryDataSetReportElement) throws ReportCreationException
    {
        if (parent instanceof DataSetsReportElement)
        {
            DataSetsReportElement dataSetsReportElement = (DataSetsReportElement) parent;
            dataSetsReportElement.addChild(staticFactoryDataSetReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull MultiDataSetReportElement multiDataSetReportElement) throws ReportCreationException
    {
        if (parent instanceof DataSetsReportElement)
        {
            DataSetsReportElement dataSetsReportElement = (DataSetsReportElement) parent;
            dataSetsReportElement.addChild(multiDataSetReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull PropertiesDataSetReportElement propertiesDataSetReportElement) throws ReportCreationException
    {
        if (parent instanceof DataSetsReportElement)
        {
            DataSetsReportElement dataSetsReportElement = (DataSetsReportElement) parent;
            boolean mergedWithExisting = false;
            ArrayList<ReportElement> children = dataSetsReportElement.getChildren();
            for (ReportElement reportElement : children)
            {
                if (reportElement instanceof PropertiesDataSetReportElement)
                {
                    PropertiesDataSetReportElement dataSetReportElement = (PropertiesDataSetReportElement) reportElement;

                    TreeSet<PropertyInfo> properties = new TreeSet<PropertyInfo>();
                    properties.addAll(dataSetReportElement.getProperties());
                    properties.addAll(propertiesDataSetReportElement.getProperties());

                    dataSetReportElement.setProperties(properties);
                    mergedWithExisting = true;
                }
            }

            if (!mergedWithExisting)
            {
                dataSetsReportElement.addChild(propertiesDataSetReportElement);
            }
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull RectangleReportElement rectangleReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(rectangleReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull EllipseReportElement ellipseReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(ellipseReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ImageFieldReportElement imageFieldReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(imageFieldReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ImageURLFieldReportElement imageURLFieldReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(imageURLFieldReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull AnchorFieldReportElement anchorFieldReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(anchorFieldReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull DrawableFieldReportElement drawableFieldReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(drawableFieldReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull SubReportElement subReportElement) throws ReportCreationException
    {
        if (parent instanceof ReportElement)
        {
            ReportElement parentReportElement = (ReportElement) parent;
            parentReportElement.addChild(subReportElement);
        }
        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull SubReportDataElement subReportDataElement) throws ReportCreationException
    {
        return null;
    }
}
