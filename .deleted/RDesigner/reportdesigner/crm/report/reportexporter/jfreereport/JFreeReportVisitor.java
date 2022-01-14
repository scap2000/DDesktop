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
package org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.AbstractReportDefinition;
import org.jfree.report.AbstractRootLevelBand;
import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.Group;
import org.jfree.report.GroupFooter;
import org.jfree.report.GroupHeader;
import org.jfree.report.ImageElement;
import org.jfree.report.ItemBand;
import org.jfree.report.JFreeReport;
import org.jfree.report.NoDataBand;
import org.jfree.report.PageFooter;
import org.jfree.report.PageHeader;
import org.jfree.report.ReportFooter;
import org.jfree.report.ReportHeader;
import org.jfree.report.ShapeElement;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.Watermark;
import org.jfree.report.elementfactory.AnchorFieldElementFactory;
import org.jfree.report.elementfactory.DateFieldElementFactory;
import org.jfree.report.elementfactory.DrawableFieldElementFactory;
import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.elementfactory.ImageFieldElementFactory;
import org.jfree.report.elementfactory.ImageURLFieldElementFactory;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.MessageFieldElementFactory;
import org.jfree.report.elementfactory.NumberFieldElementFactory;
import org.jfree.report.elementfactory.ResourceFieldElementFactory;
import org.jfree.report.elementfactory.ResourceLabelElementFactory;
import org.jfree.report.elementfactory.ResourceMessageElementFactory;
import org.jfree.report.elementfactory.StaticImageURLElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.function.ElementVisibilitySwitchFunction;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionCollection;
import org.jfree.report.function.FormulaExpression;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.modules.misc.datafactory.StaticDataFactory;
import org.jfree.report.modules.misc.datafactory.sql.ConnectionProvider;
import org.jfree.report.modules.misc.datafactory.sql.SQLReportDataFactory;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleKey;
import org.jfree.ui.FloatDimension;
import org.pentaho.reportdesigner.crm.report.ReportDesignerUtils;
import org.pentaho.reportdesigner.crm.report.datasetplugin.MQLDataFactory;
import org.pentaho.reportdesigner.crm.report.datasetplugin.MondrianDataFactory;
import org.pentaho.reportdesigner.crm.report.datasetplugin.MultiDataFactory;
import org.pentaho.reportdesigner.crm.report.datasetplugin.XPathDataFactory;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCClassLoader;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.JNDISource;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.Query;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertiesDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertyInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.staticfactory.StaticFactoryDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.AnchorFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelGroupReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelItemReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelPageReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.DateFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.DrawableFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ElementBorderDefinition;
import org.pentaho.reportdesigner.crm.report.model.EllipseReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageURLFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.LabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.LineDirection;
import org.pentaho.reportdesigner.crm.report.model.LineReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.NumberFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
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
import org.pentaho.reportdesigner.crm.report.model.RowBandingDefinition;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.StyleExpression;
import org.pentaho.reportdesigner.crm.report.model.StyleExpressions;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.model.SubReportDataElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportParameter;
import org.pentaho.reportdesigner.crm.report.model.SubReportParameters;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElementHorizontalAlignment;
import org.pentaho.reportdesigner.crm.report.model.TextReportElementVerticalAlignment;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.model.layoutmanager.StackedReportLayoutManager;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin Date: 28.10.2005 Time: 08:55:20
 */
@SuppressWarnings({"ObjectEquality"})
public class JFreeReportVisitor extends ReportVisitor
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JFreeReportVisitor.class.getName());

    @NotNull
    private JFreeReport jFreeReport;
    @NotNull
    private org.jfree.report.SubReport jFreeSubReport;

    @Nullable
    private AbstractReportDefinition currentJFreeReport;

    @NotNull
    private Locale defaultLocale;

    @Nullable
    private MultiDataFactory multiDataFactory;


    public JFreeReportVisitor()
    {
        defaultLocale = Locale.getDefault();
        jFreeReport = new JFreeReport();
        jFreeSubReport = new org.jfree.report.SubReport();
    }


    @Nullable
    public Object getCurrentContext()
    {
        return currentJFreeReport;
    }


    public void setCurrentContext(@Nullable Object context)
    {
        if (context instanceof AbstractReportDefinition)
        {
            AbstractReportDefinition abstractReportDefinition = (AbstractReportDefinition) context;
            this.currentJFreeReport = abstractReportDefinition;
        }
    }


    @NotNull
    public JFreeReport getJFreeReport()
    {
        return jFreeReport;
    }


    @NotNull
    public org.jfree.report.SubReport getSubReport()
    {
        return jFreeSubReport;
    }


    @NotNull
    public AbstractReportDefinition visit(@Nullable Object parent, @NotNull Report report) throws ReportCreationException
    {
        try
        {
            if (report instanceof SubReport)
            {
                SubReport subReport = (SubReport) report;
                jFreeSubReport = new org.jfree.report.SubReport();
                jFreeSubReport.setQuery(subReport.getQuery());

                applySubReportParameters(jFreeSubReport, subReport.getParameters());

                defaultLocale = report.getDefaultLocale();

                return jFreeSubReport;
            }
            else
            {
                jFreeReport = new JFreeReport();
                jFreeReport.setDataFactory(new NullDataFactory());
                jFreeReport.setName(report.getName());

                defaultLocale = report.getDefaultLocale();

                // jFreeReport.setProperty(JFreeReport.REPORT_DEFINITION_CONTENTBASE, new File(".").toURI().toURL().toExternalForm());
                // jFreeReport.setPropertyMarked(JFreeReport.REPORT_DATE_PROPERTY, true);

                HashMap<String, String> configProperties = report.getReportConfiguration().getConfigProperties();
                for (String key : configProperties.keySet())
                {
                    jFreeReport.getReportConfiguration().setConfigProperty(key, configProperties.get(key));
                }

                URL url = report.getResourceBundleClasspath();

                if (url != null)
                {
                    jFreeReport.setResourceBundleFactory(new URLResourceBundleFactory(defaultLocale, url));
                }

                Paper paper = new Paper();

                PageDefinition pageDefinition = report.getPageDefinition();
                paper.setSize(pageDefinition.getInnerPageSize().getWidth() + pageDefinition.getLeftBorder() + pageDefinition.getRightBorder(), pageDefinition.getInnerPageSize().getHeight() + pageDefinition.getTopBorder()
                                                                                                                                               + pageDefinition.getBottomBorder());
                paper.setImageableArea(pageDefinition.getLeftBorder(), pageDefinition.getTopBorder(), pageDefinition.getInnerPageSize().getWidth(), pageDefinition.getInnerPageSize().getHeight());

                PageFormat pageFormat = new PageFormat();
                pageFormat.setPaper(paper);
                pageFormat.setOrientation(PageFormat.PORTRAIT);

                SimplePageDefinition simplePageDefinition = new SimplePageDefinition(pageFormat);

                jFreeReport.setPageDefinition(simplePageDefinition);

                return jFreeReport;
            }
        }
        catch (Exception e)
        {
            throw new ReportCreationException("Unknown error occured", report, e);
        }
    }


    private void applySubReportParameters(@NotNull org.jfree.report.SubReport jFreeSubReport, @NotNull SubReportParameters subReportParameters)
    {
        if (subReportParameters.isGlobalImport())
        {
            jFreeSubReport.addInputParameter("*", "*");
        }
        Collection<SubReportParameter> importParameters = subReportParameters.getImportParameters().values();
        for (SubReportParameter importParameter : importParameters)
        {
            jFreeSubReport.addInputParameter(importParameter.getKey(), importParameter.getValue());
        }

        if (subReportParameters.isGlobalExport())
        {
            jFreeSubReport.addExportParameter("*", "*");
        }
        Collection<SubReportParameter> exportParameters = subReportParameters.getExportParameters().values();
        for (SubReportParameter exportParameter : exportParameters)
        {
            jFreeSubReport.addInputParameter(exportParameter.getKey(), exportParameter.getValue());
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull BandToplevelReportElement bandToplevelReportElement) throws ReportCreationException
    {
        try
        {
            if (bandToplevelReportElement.getParent() instanceof ReportGroup)
            {
                ReportGroup reportGroup = (ReportGroup) bandToplevelReportElement.getParent();
                Group group = (Group) freeParent;
                if (bandToplevelReportElement == reportGroup.getGroupHeader())
                {
                    GroupHeader groupHeader = new GroupHeader();
                    BandToplevelGroupReportElement bandToplevelGroupReportElement = (BandToplevelGroupReportElement) bandToplevelReportElement;
                    groupHeader.setRepeat(bandToplevelGroupReportElement.isRepeat());
                    groupHeader.setPagebreakBeforePrint(bandToplevelReportElement.isPageBreakBefore());
                    groupHeader.setPagebreakAfterPrint(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(groupHeader, bandToplevelReportElement);
                    addStyleExpressions(groupHeader, bandToplevelGroupReportElement);
                    group.setHeader(groupHeader);
                    return groupHeader;
                }
                else if (bandToplevelReportElement == reportGroup.getGroupFooter())
                {
                    GroupFooter groupFooter = new GroupFooter();
                    BandToplevelGroupReportElement bandToplevelGroupReportElement = (BandToplevelGroupReportElement) bandToplevelReportElement;
                    groupFooter.setRepeat(bandToplevelGroupReportElement.isRepeat());
                    groupFooter.setPagebreakBeforePrint(bandToplevelReportElement.isPageBreakBefore());
                    groupFooter.setPagebreakAfterPrint(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(groupFooter, bandToplevelReportElement);
                    addStyleExpressions(groupFooter, bandToplevelGroupReportElement);
                    group.setFooter(groupFooter);
                    return groupFooter;
                }
            }
            else
            {
                AbstractReportDefinition freeReport = (AbstractReportDefinition) freeParent;
                Report parent = (Report) bandToplevelReportElement.getParent();

                if (parent.getReportHeaderBand() == bandToplevelReportElement)
                {
                    ReportHeader reportHeader = freeReport.getReportHeader();
                    reportHeader.setPagebreakBeforePrint(bandToplevelReportElement.isPageBreakBefore());
                    reportHeader.setPagebreakAfterPrint(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(reportHeader, bandToplevelReportElement);
                    addStyleExpressions(reportHeader, bandToplevelReportElement);
                    return reportHeader;
                }
                else if (parent.getReportFooterBand() == bandToplevelReportElement)
                {
                    ReportFooter reportFooter = freeReport.getReportFooter();
                    reportFooter.setPagebreakBeforePrint(bandToplevelReportElement.isPageBreakBefore());
                    reportFooter.setPagebreakAfterPrint(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(reportFooter, bandToplevelReportElement);
                    addStyleExpressions(reportFooter, bandToplevelReportElement);
                    return reportFooter;
                }
                else if (parent.getPageHeaderBand() == bandToplevelReportElement)
                {
                    PageHeader pageHeader = freeReport.getPageHeader();
                    BandToplevelPageReportElement bandToplevelPageReportElement = (BandToplevelPageReportElement) bandToplevelReportElement;
                    pageHeader.setDisplayOnFirstPage(bandToplevelPageReportElement.isDisplayOnFirstPage());
                    pageHeader.setDisplayOnLastPage(bandToplevelPageReportElement.isDisplayOnLastPage());
                    applySizes(pageHeader, bandToplevelReportElement);
                    addStyleExpressions(pageHeader, bandToplevelPageReportElement);
                    return pageHeader;
                }
                else if (parent.getPageFooterBand() == bandToplevelReportElement)
                {
                    PageFooter pageFooter = freeReport.getPageFooter();
                    BandToplevelPageReportElement bandToplevelPageReportElement = (BandToplevelPageReportElement) bandToplevelReportElement;
                    pageFooter.setDisplayOnFirstPage(bandToplevelPageReportElement.isDisplayOnFirstPage());
                    pageFooter.setDisplayOnLastPage(bandToplevelPageReportElement.isDisplayOnLastPage());
                    applySizes(pageFooter, bandToplevelReportElement);
                    addStyleExpressions(pageFooter, bandToplevelPageReportElement);
                    return pageFooter;
                }
                else if (parent.getItemBand() == bandToplevelReportElement)
                {
                    ItemBand band = freeReport.getItemBand();
                    band.setPagebreakBeforePrint(bandToplevelReportElement.isPageBreakBefore());
                    band.setPagebreakAfterPrint(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(band, bandToplevelReportElement);
                    addStyleExpressions(band, bandToplevelReportElement);
                    BandToplevelItemReportElement itemBandReportElement = (BandToplevelItemReportElement) bandToplevelReportElement;
                    RowBandingDefinition rowBandingDefinition = itemBandReportElement.getRowBandingDefinition();
                    if (rowBandingDefinition.isEnabled())
                    {
                        ElementVisibilitySwitchFunction switchFunction = new ElementVisibilitySwitchFunction();
                        switchFunction.setElement("RowBandingBackgroundRect_IAmTheRandomSuffix314152654");// NON-NLS
                        switchFunction.setInitialState(rowBandingDefinition.isStartState());
                        switchFunction.setNumberOfElements(rowBandingDefinition.getSwitchItemCount());
                        freeReport.getExpressions().add(switchFunction);

                        ShapeElement shapeElement = StaticShapeElementFactory.createRectangleShapeElement("RowBandingBackgroundRect_IAmTheRandomSuffix314152654", rowBandingDefinition.getColor(), new BasicStroke(1), new Rectangle2D.Double(0, 0, -100, -100), false, true);// NON-NLS
                        band.addElement(shapeElement);
                    }
                    return band;
                }
                else if (parent.getWatermarkBand() == bandToplevelReportElement)
                {
                    Watermark watermark = freeReport.getWatermark();
                    watermark.setPagebreakBeforePrint(bandToplevelReportElement.isPageBreakBefore());
                    watermark.setPagebreakAfterPrint(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(watermark, bandToplevelReportElement);
                    addStyleExpressions(watermark, bandToplevelReportElement);
                    return watermark;
                }
                else if (parent.getNoDataBand() == bandToplevelReportElement)
                {
                    NoDataBand noDataBand = freeReport.getNoDataBand();
                    noDataBand.setPagebreakBeforePrint(bandToplevelReportElement.isPageBreakBefore());
                    noDataBand.setPagebreakAfterPrint(bandToplevelReportElement.isPageBreakAfter());
                    applySizes(noDataBand, bandToplevelReportElement);
                    addStyleExpressions(noDataBand, bandToplevelReportElement);
                    return noDataBand;
                }
            }
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", bandToplevelReportElement, e);
        }
        throw new ReportCreationException("Wrong element type", bandToplevelReportElement, new Throwable());
    }


    private void applySizes(@NotNull Element reportHeader, @NotNull BandToplevelReportElement bandToplevelReportElement) throws ReportCreationException
    {
        try
        {
            DoubleDimension minimumSize = bandToplevelReportElement.getMinimumSize();
            if (minimumSize.getWidth() > 0 || minimumSize.getHeight() > 0)
            {
                reportHeader.setMinimumSize(minimumSize);
            }
            DoubleDimension maximumSize = bandToplevelReportElement.getMaximumSize();
            if (maximumSize.getWidth() > 0 || maximumSize.getHeight() > 0)
            {
                reportHeader.setMaximumSize(maximumSize);
            }
            DoubleDimension preferredSize = bandToplevelReportElement.getPreferredSize();
            if (preferredSize.getWidth() > 0 || preferredSize.getHeight() > 0)
            {
                reportHeader.setPreferredSize(preferredSize);
            }
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", bandToplevelReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object parent, @NotNull BandReportElement bandReportElement) throws ReportCreationException
    {
        try
        {
            Band band = new Band();
            band.setName(bandReportElement.getName());
            band.getStyle().setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, bandReportElement.getPosition());
            band.setMinimumSize(bandReportElement.getMinimumSize());

            if (bandReportElement.getReportLayoutManager() instanceof StackedReportLayoutManager)
            {
                band.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, "block");// NON-NLS
            }

            if (!MathUtils.approxEquals(bandReportElement.getMaximumSize().getHeight(), 0) || !MathUtils.approxEquals(bandReportElement.getMaximumSize().getWidth(), 0))
            {
                band.setMaximumSize(bandReportElement.getMaximumSize());
            }
            else
            {
                band.setMaximumSize(new DoubleDimension(bandReportElement.getMinimumSize().getWidth(), Integer.MAX_VALUE));
            }

            if (!MathUtils.approxEquals(bandReportElement.getPreferredSize().getHeight(), 0) || !MathUtils.approxEquals(bandReportElement.getPreferredSize().getWidth(), 0))
            {
                band.setPreferredSize(bandReportElement.getPreferredSize());
            }

            if (parent instanceof Band)
            {
                Band b = (Band) parent;
                addStyleExpressions(band, bandReportElement);
                b.addElement(band);
            }
            return band;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", bandReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull DateFieldReportElement dateFieldReportElement) throws ReportCreationException
    {
        try
        {
            DateFieldElementFactory dateFieldElementFactory = new DateFieldElementFactory();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFieldReportElement.getFormat().toPattern(), defaultLocale);
            dateFieldElementFactory.setFormat(simpleDateFormat);
            dateFieldElementFactory.setExcelCellFormat(dateFieldReportElement.getExcelDateFormat());
            dateFieldElementFactory.setFieldname(dateFieldReportElement.getFieldName());
            dateFieldElementFactory.setNullString(dateFieldReportElement.getNullString());
            dateFieldElementFactory.setFormula("".equals(dateFieldReportElement.getFormula().getText().trim()) ? null : dateFieldReportElement.getFormula().getText());

            applyTextFieldValues(dateFieldElementFactory, dateFieldReportElement);
            applyElementValues(dateFieldElementFactory, dateFieldReportElement);

            return addTextElementToBand(freeParent, dateFieldReportElement, dateFieldElementFactory);
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", dateFieldReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull LabelReportElement labelReportElement) throws ReportCreationException
    {
        try
        {
            LabelElementFactory labelElementFactory = new LabelElementFactory();
            labelElementFactory.setText(labelReportElement.getText());

            applyTextFieldValues(labelElementFactory, labelReportElement);
            applyElementValues(labelElementFactory, labelReportElement);

            return addTextElementToBand(freeParent, labelReportElement, labelElementFactory);
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", labelReportElement, e);
        }
    }


    @NotNull
    private Element addTextElementToBand(@NotNull Object freeParent, @NotNull TextReportElement labelReportElement, @NotNull ElementFactory labelElementFactory)
    {
        Band e = (Band) freeParent;

        Element element = labelElementFactory.createElement();

        addStyleExpressions(element, labelReportElement);
        e.addElement(element);

        return element;
    }


    private void addStyleExpressions(@NotNull Element element, @NotNull ReportElement reportElement)
    {
        StyleExpressions styleExpressions = reportElement.getStyleExpressions();
        List<StyleExpression> list = styleExpressions.getStyleExpressions();

        for (StyleExpression styleExpression : list)
        {
            if (!"".equals(styleExpression.getExpression()))
            {
                String keyName = styleExpression.getStyleKeyName();
                StyleKey key = StyleKey.getStyleKey(keyName);
                if (key != null)
                {
                    FormulaExpression expression = new FormulaExpression();
                    expression.setFormula(styleExpression.getExpression());
                    element.setStyleExpression(key, expression);
                }
            }
        }

        if (reportElement.getBackground() != null)
        {
            element.getStyle().setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, reportElement.getBackground());
        }

        element.getStyle().setStyleProperty(ElementStyleKeys.PADDING_TOP, Float.valueOf((float) reportElement.getPadding().getTop()));
        element.getStyle().setStyleProperty(ElementStyleKeys.PADDING_BOTTOM, Float.valueOf((float) reportElement.getPadding().getBottom()));
        element.getStyle().setStyleProperty(ElementStyleKeys.PADDING_LEFT, Float.valueOf((float) reportElement.getPadding().getLeft()));
        element.getStyle().setStyleProperty(ElementStyleKeys.PADDING_RIGHT, Float.valueOf((float) reportElement.getPadding().getRight()));

        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_COLOR, reportElement.getElementBorder().getTopSide().getColor());
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_WIDTH, Float.valueOf((float) reportElement.getElementBorder().getTopSide().getWidth()));
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_STYLE, getBorderStyle(reportElement.getElementBorder().getTopSide().getType()));

        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_LEFT_RADIUS, reportElement.getElementBorder().getTopLeftEdge().getRadii());

        if (reportElement.getElementBorder().isSameBorderForAllSides())
        {
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_COLOR, reportElement.getElementBorder().getTopSide().getColor());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_WIDTH, Float.valueOf((float) reportElement.getElementBorder().getTopSide().getWidth()));
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_STYLE, getBorderStyle(reportElement.getElementBorder().getTopSide().getType()));

            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_LEFT_COLOR, reportElement.getElementBorder().getTopSide().getColor());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_LEFT_WIDTH, Float.valueOf((float) reportElement.getElementBorder().getTopSide().getWidth()));
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_LEFT_STYLE, getBorderStyle(reportElement.getElementBorder().getTopSide().getType()));

            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_RIGHT_COLOR, reportElement.getElementBorder().getTopSide().getColor());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_RIGHT_WIDTH, Float.valueOf((float) reportElement.getElementBorder().getTopSide().getWidth()));
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_RIGHT_STYLE, getBorderStyle(reportElement.getElementBorder().getTopSide().getType()));

            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_COLOR, reportElement.getElementBorder().getTopSide().getColor());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_WIDTH, Float.valueOf((float) reportElement.getElementBorder().getTopSide().getWidth()));
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_STYLE, getBorderStyle(reportElement.getElementBorder().getTopSide().getType()));

            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS, reportElement.getElementBorder().getTopLeftEdge().getRadii());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS, reportElement.getElementBorder().getTopLeftEdge().getRadii());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS, reportElement.getElementBorder().getTopLeftEdge().getRadii());
        }
        else
        {
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_LEFT_COLOR, reportElement.getElementBorder().getLeftSide().getColor());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_LEFT_WIDTH, Float.valueOf((float) reportElement.getElementBorder().getLeftSide().getWidth()));
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_LEFT_STYLE, getBorderStyle(reportElement.getElementBorder().getLeftSide().getType()));

            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_RIGHT_COLOR, reportElement.getElementBorder().getRightSide().getColor());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_RIGHT_WIDTH, Float.valueOf((float) reportElement.getElementBorder().getRightSide().getWidth()));
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_RIGHT_STYLE, getBorderStyle(reportElement.getElementBorder().getRightSide().getType()));

            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_COLOR, reportElement.getElementBorder().getBottomSide().getColor());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_WIDTH, Float.valueOf((float) reportElement.getElementBorder().getBottomSide().getWidth()));
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_STYLE, getBorderStyle(reportElement.getElementBorder().getBottomSide().getType()));

            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS, reportElement.getElementBorder().getTopRightEdge().getRadii());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS, reportElement.getElementBorder().getBottomLeftEdge().getRadii());
            element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS, reportElement.getElementBorder().getBottomRightEdge().getRadii());
        }

        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BREAK_COLOR, reportElement.getElementBorder().getBreakSide().getColor());
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BREAK_WIDTH, Float.valueOf((float) reportElement.getElementBorder().getBreakSide().getWidth()));
        element.getStyle().setStyleProperty(ElementStyleKeys.BORDER_BREAK_STYLE, getBorderStyle(reportElement.getElementBorder().getBreakSide().getType()));
    }


    @NotNull
    private BorderStyle getBorderStyle(@NotNull ElementBorderDefinition.BorderType borderType)
    {
        switch (borderType)
        {
            case DASHED:
                return BorderStyle.DASHED;
            case DOT_DASH:
                return BorderStyle.DOT_DASH;
            case DOT_DOT_DASH:
                return BorderStyle.DOT_DOT_DASH;
            case DOTTED:
                return BorderStyle.DOTTED;
            case DOUBLE:
                return BorderStyle.DOUBLE;
            case GROOVE:
                return BorderStyle.GROOVE;
            case HIDDEN:
                return BorderStyle.HIDDEN;
            case INSET:
                return BorderStyle.INSET;
            case NONE:
                return BorderStyle.NONE;
            case OUTSET:
                return BorderStyle.OUTSET;
            case RIDGE:
                return BorderStyle.RIDGE;
            case SOLID:
                return BorderStyle.SOLID;
            case WAVE:
                return BorderStyle.WAVE;
        }
        return BorderStyle.NONE;
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull MessageFieldReportElement messageFieldReportElement) throws ReportCreationException
    {
        try
        {
            MessageFieldElementFactory messageFieldElementFactory = new MessageFieldElementFactory();
            messageFieldElementFactory.setFormatString(messageFieldReportElement.getFormatString());
            messageFieldElementFactory.setNullString(messageFieldReportElement.getNullString());
            // MARKED formula missing in factory?

            applyTextFieldValues(messageFieldElementFactory, messageFieldReportElement);
            applyElementValues(messageFieldElementFactory, messageFieldReportElement);

            return addTextElementToBand(freeParent, messageFieldReportElement, messageFieldElementFactory);
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", messageFieldReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull NumberFieldReportElement numberFieldReportElement) throws ReportCreationException
    {
        try
        {
            NumberFieldElementFactory numberFieldElementFactory = new NumberFieldElementFactory();
            numberFieldElementFactory.setFieldname(numberFieldReportElement.getFieldName());
            numberFieldElementFactory.setNullString(numberFieldReportElement.getNullString());
            numberFieldElementFactory.setFormat(numberFieldReportElement.getFormat());
            numberFieldElementFactory.setExcelCellFormat(numberFieldReportElement.getExcelNumberFormat());
            numberFieldElementFactory.setFormula("".equals(numberFieldReportElement.getFormula().getText().trim()) ? null : numberFieldReportElement.getFormula().getText());

            applyTextFieldValues(numberFieldElementFactory, numberFieldReportElement);
            applyElementValues(numberFieldElementFactory, numberFieldReportElement);

            return addTextElementToBand(freeParent, numberFieldReportElement, numberFieldElementFactory);
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", numberFieldReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull ReportGroups reportGroups) throws ReportCreationException
    {
        try
        {
            return freeParent;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", reportGroups, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull LineReportElement lineReportElement) throws ReportCreationException
    {
        try
        {
            Band band = (Band) freeParent;
            Rectangle2D.Double rect = lineReportElement.getRectangle();

            Line2D line2D;
            Rectangle2D.Double bounds;

            if (lineReportElement.getDirection() == LineDirection.HORIZONTAL)
            {
                double x1 = 0;
                double y1 = rect.getHeight() / 2;
                double x2 = rect.getWidth();
                double y2 = rect.getHeight() / 2;

                bounds = lineReportElement.getRectangle();
                line2D = new Line2D.Double(x1, y1, x2, y2);
            }
            else
            {
                double x1 = rect.getWidth() / 2;
                double y1 = 0;
                double x2 = rect.getWidth() / 2;
                double y2 = rect.getHeight();

                bounds = lineReportElement.getRectangle();
                line2D = new Line2D.Double(x1, y1, x2, y2);
            }
            ShapeElement shapeElement = StaticShapeElementFactory.createShapeElement(lineReportElement.getName(), bounds, lineReportElement.getLineDefinition().getColor(), lineReportElement.getLineDefinition().getBasicStroke(), line2D, true,
                                                                                     false, true);
            shapeElement.setDynamicContent(false);
            addStyleExpressions(shapeElement, lineReportElement);
            band.addElement(shapeElement);

            return shapeElement;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", lineReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull RectangleReportElement rectangleReportElement) throws ReportCreationException
    {
        try
        {
            Band band = (Band) freeParent;

            Band helperBand = new Band();
            helperBand.getStyle().setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, rectangleReportElement.getPosition());
            helperBand.setMinimumSize(rectangleReportElement.getMinimumSize());
            helperBand.setMaximumSize(new DoubleDimension(rectangleReportElement.getMinimumSize().getWidth(), Integer.MAX_VALUE));
            helperBand.setName(rectangleReportElement.getName());

            if (rectangleReportElement.getArcWidth() > 0 && rectangleReportElement.getArcHeight() > 0)
            {
                RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double(0, 0, -100, -100, rectangleReportElement.getArcWidth(), rectangleReportElement.getArcHeight());

                if (rectangleReportElement.isFill())
                {
                    ShapeElement shapeElement = StaticShapeElementFactory.createRoundRectangleShapeElement(rectangleReportElement.getName() + ".fill", rectangleReportElement.getColor(), rectangleReportElement.getBorderDefinition().getBasicStroke(), roundRect, false, rectangleReportElement.isFill());// NON-NLS
                    addStyleExpressions(shapeElement, rectangleReportElement);
                    helperBand.addElement(shapeElement);
                }
                if (rectangleReportElement.isDrawBorder())
                {
                    ShapeElement borderShape = StaticShapeElementFactory.createRoundRectangleShapeElement(rectangleReportElement.getName() + ".border", rectangleReportElement.getBorderDefinition().getColor(), rectangleReportElement.getBorderDefinition().getBasicStroke(), roundRect, true, false);// NON-NLS
                    addStyleExpressions(borderShape, rectangleReportElement);
                    helperBand.addElement(borderShape);
                }
            }
            else
            {
                Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, -100, -100);
                if (rectangleReportElement.isFill())
                {
                    ShapeElement shapeElement = StaticShapeElementFactory.createRectangleShapeElement(rectangleReportElement.getName() + ".fill", rectangleReportElement.getColor(), rectangleReportElement.getBorderDefinition().getBasicStroke(), rect, false, rectangleReportElement.isFill());// NON-NLS
                    addStyleExpressions(shapeElement, rectangleReportElement);
                    helperBand.addElement(shapeElement);
                }
                if (rectangleReportElement.isDrawBorder())
                {
                    ShapeElement borderShape = StaticShapeElementFactory.createRectangleShapeElement(rectangleReportElement.getName() + ".border", rectangleReportElement.getBorderDefinition().getColor(), rectangleReportElement.getBorderDefinition().getBasicStroke(), rect, true, false);// NON-NLS
                    addStyleExpressions(borderShape, rectangleReportElement);
                    helperBand.addElement(borderShape);
                }
            }

            addStyleExpressions(helperBand, rectangleReportElement);
            band.addElement(helperBand);
            return helperBand;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", rectangleReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull EllipseReportElement ellipseReportElement) throws ReportCreationException
    {
        try
        {
            Band band = (Band) freeParent;

            Band helperBand = new Band();

            // if (ellipseReportElement.isKeepAspect())
            // {
            // Rectangle2D.Double rect = new Rectangle2D.Double(ellipseReportElement.getPosition().x, ellipseReportElement.getPosition().y, ellipseReportElement.getMinimumSize().getWidth(), ellipseReportElement.getMinimumSize().getHeight());
            // double side = Math.min(rect.width, rect.height);
            // Point2D.Double pos = new Point2D.Double(rect.x + rect.width / 2 - side / 2, rect.y + rect.height / 2 - side / 2);
            // helperBand.getStyle().setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, pos);
            // helperBand.setMinimumSize(new DoubleDimension(side, side));
            // helperBand.setMaximumSize(new DoubleDimension(side, Integer.MAX_VALUE));
            // helperBand.setName(ellipseReportElement.getName());
            // }
            // else
            {
                helperBand.getStyle().setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, ellipseReportElement.getPosition());
                helperBand.setMinimumSize(ellipseReportElement.getMinimumSize());
                helperBand.setMaximumSize(new DoubleDimension(ellipseReportElement.getMinimumSize().getWidth(), Integer.MAX_VALUE));
                helperBand.setName(ellipseReportElement.getName());
            }

            Ellipse2D.Double ellipse = new Ellipse2D.Double(0, 0, -100, -100);

            ShapeElement shapeElement = StaticShapeElementFactory.createEllipseShapeElement(ellipseReportElement.getName() + ".fill", ellipseReportElement.getColor(), ellipseReportElement.getBorderDefinition().getBasicStroke(), ellipse, false, ellipseReportElement.isFill());// NON-NLS
            addStyleExpressions(shapeElement, ellipseReportElement);
            helperBand.addElement(shapeElement);
            if (ellipseReportElement.isDrawBorder())
            {
                ShapeElement borderShape = StaticShapeElementFactory.createEllipseShapeElement(ellipseReportElement.getName() + ".border", ellipseReportElement.getBorderDefinition().getColor(), ellipseReportElement.getBorderDefinition().getBasicStroke(), ellipse, true, false);// NON-NLS
                addStyleExpressions(borderShape, ellipseReportElement);
                helperBand.addElement(borderShape);
            }

            addStyleExpressions(helperBand, ellipseReportElement);
            band.addElement(helperBand);
            return helperBand;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", ellipseReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull StaticImageReportElement staticImageReportElement) throws ReportCreationException
    {
        try
        {
            Band band = (Band) freeParent;
            ImageElement imageElement = StaticImageURLElementFactory
                    .createImageElement(staticImageReportElement.getName(), staticImageReportElement.getRectangle(), staticImageReportElement.getUrl(), true, staticImageReportElement.isKeepAspect());
            addStyleExpressions(imageElement, staticImageReportElement);
            band.addElement(imageElement);
            return imageElement;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", staticImageReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull ImageFieldReportElement imageFieldReportElement) throws ReportCreationException
    {
        try
        {
            Band band = (Band) freeParent;
            ImageFieldElementFactory imageFieldElementFactory = new ImageFieldElementFactory();
            imageFieldElementFactory.setName(imageFieldReportElement.getName());
            imageFieldElementFactory.setAbsolutePosition(new Point2D.Double(imageFieldReportElement.getRectangle().getX(), imageFieldReportElement.getRectangle().getY()));
            imageFieldElementFactory.setMinimumSize(new FloatDimension((float) imageFieldReportElement.getRectangle().getWidth(), (float) imageFieldReportElement.getRectangle().getHeight()));
            imageFieldElementFactory.setScale(Boolean.TRUE);
            imageFieldElementFactory.setKeepAspectRatio(Boolean.valueOf(imageFieldReportElement.isKeepAspect()));
            imageFieldElementFactory.setFieldname(imageFieldReportElement.getFieldName());
            imageFieldElementFactory.setFormula("".equals(imageFieldReportElement.getFormula().getText().trim()) ? null : imageFieldReportElement.getFormula().getText());

            ImageElement imageElement = (ImageElement) imageFieldElementFactory.createElement();
            addStyleExpressions(imageElement, imageFieldReportElement);
            band.addElement(imageElement);
            return imageElement;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", imageFieldReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull ImageURLFieldReportElement imageURLFieldReportElement) throws ReportCreationException
    {
        try
        {
            Band band = (Band) freeParent;
            ImageURLFieldElementFactory imageFieldElementFactory = new ImageURLFieldElementFactory();
            imageFieldElementFactory.setName(imageURLFieldReportElement.getName());
            imageFieldElementFactory.setAbsolutePosition(new Point2D.Double(imageURLFieldReportElement.getRectangle().getX(), imageURLFieldReportElement.getRectangle().getY()));
            imageFieldElementFactory.setMinimumSize(new FloatDimension((float) imageURLFieldReportElement.getRectangle().getWidth(), (float) imageURLFieldReportElement.getRectangle().getHeight()));
            imageFieldElementFactory.setScale(Boolean.TRUE);
            imageFieldElementFactory.setKeepAspectRatio(Boolean.valueOf(imageURLFieldReportElement.isKeepAspect()));
            imageFieldElementFactory.setFieldname(imageURLFieldReportElement.getFieldName());
            imageFieldElementFactory.setFormula("".equals(imageURLFieldReportElement.getFormula().getText().trim()) ? null : imageURLFieldReportElement.getFormula().getText());

            ImageElement imageElement = (ImageElement) imageFieldElementFactory.createElement();
            addStyleExpressions(imageElement, imageURLFieldReportElement);
            band.addElement(imageElement);
            return imageElement;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", imageURLFieldReportElement, e);
        }
    }


    @Nullable
    public Object visit(@Nullable Object freeParent, @NotNull AnchorFieldReportElement anchorFieldReportElement) throws ReportCreationException
    {
        try
        {
            Band band = (Band) freeParent;
            AnchorFieldElementFactory anchorFieldElementFactory = new AnchorFieldElementFactory();
            anchorFieldElementFactory.setName(anchorFieldReportElement.getName());
            anchorFieldElementFactory.setFieldname(anchorFieldReportElement.getFieldName());
            anchorFieldElementFactory.setAbsolutePosition(new Point2D.Double(anchorFieldReportElement.getRectangle().getX(), anchorFieldReportElement.getRectangle().getY()));
            anchorFieldElementFactory.setMinimumSize(new FloatDimension((float) anchorFieldReportElement.getRectangle().getWidth(), (float) anchorFieldReportElement.getRectangle().getHeight()));
            Element element = anchorFieldElementFactory.createElement();
            band.addElement(element);
            return element;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", anchorFieldReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull DrawableFieldReportElement drawableFieldReportElement) throws ReportCreationException
    {
        try
        {
            Band band = (Band) freeParent;
            DrawableFieldElementFactory factory = new DrawableFieldElementFactory();
            factory.setFieldname(drawableFieldReportElement.getFieldName());
            factory.setFormula("".equals(drawableFieldReportElement.getFormula().getText().trim()) ? null : drawableFieldReportElement.getFormula().getText());
            factory.setName(drawableFieldReportElement.getName());
            factory.setAbsolutePosition(new Point2D.Double(drawableFieldReportElement.getPosition().getX(), drawableFieldReportElement.getPosition().getY()));
            factory.setMinimumSize(new FloatDimension((float) drawableFieldReportElement.getMinimumSize().getWidth(), (float) drawableFieldReportElement.getMinimumSize().getHeight()));

            Element element = factory.createElement();

            addStyleExpressions(element, drawableFieldReportElement);
            band.addElement(element);
            return element;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", drawableFieldReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object parent, @NotNull DataSetsReportElement dataSetsReportElement) throws ReportCreationException
    {
        try
        {
            if (!dataSetsReportElement.getChildren().isEmpty())
            {
                multiDataFactory = new MultiDataFactory();
                jFreeReport.setDataFactory(multiDataFactory);
            }
            return parent;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", dataSetsReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object parent, @NotNull PropertiesDataSetReportElement propertiesDataSetReportElement) throws ReportCreationException
    {
        try
        {
            TreeSet<PropertyInfo> properties = propertiesDataSetReportElement.getProperties();
            for (PropertyInfo propertyInfo : properties)
            {
                jFreeReport.setProperty(propertyInfo.getKey(), propertyInfo.getValue());
            }
            return jFreeReport;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", propertiesDataSetReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object parent, @NotNull StaticFactoryDataSetReportElement staticFactoryDataSetReportElement) throws ReportCreationException
    {
        try
        {
            jFreeReport.setDataFactory(new StaticDataFactory());
            jFreeReport.setQuery(staticFactoryDataSetReportElement.getQuery());
            for (PropertyInfo parameter : staticFactoryDataSetReportElement.getParameters())
            {
                jFreeReport.setProperty(parameter.getKey(), parameter.getValue());
            }
            return jFreeReport;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", staticFactoryDataSetReportElement, e);
        }
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull MultiDataSetReportElement multiDataSetReportElement) throws ReportCreationException
    {
        if (multiDataSetReportElement.getConnectionType() == MultiDataSetReportElement.ConnectionType.JNDI)
        {
            final JNDISource jndiSource = multiDataSetReportElement.getSelectedJNDIDataSource();
            final String mondrianCubeDefinitionFile = multiDataSetReportElement.getMondrianCubeDefinitionFile();
            MultiDataFactory multiDataFactory = this.multiDataFactory;
            if (multiDataFactory != null && jndiSource != null)
            {
                if (!StringUtils.isEmpty(mondrianCubeDefinitionFile))
                {
                    ArrayList<Query> queries = multiDataSetReportElement.getQueries();
                    if (!queries.isEmpty())
                    {
                        try
                        {
                            MondrianDataFactory mdxDataFactory = new MondrianDataFactory(jndiSource, multiDataSetReportElement.getQueryName(), mondrianCubeDefinitionFile, multiDataSetReportElement.getQueries().get(0).getQuery());
                            multiDataFactory.addExtendedDataFactory(mdxDataFactory);
                        }
                        catch (Exception e)
                        {
                            if (LOG.isLoggable(Level.FINE))
                                LOG.log(Level.FINE, "JFreeReportVisitor.visit ", e);
                        }
                    }
                }
                else
                {
                    ArrayList<Query> queries = multiDataSetReportElement.getQueries();
                    if (!queries.isEmpty())
                    {
                        SQLReportDataFactory sqlReportDataFactory = new SQLReportDataFactory(new ConnectionProvider()
                        {
                            @NotNull
                            public Connection getConnection() throws SQLException
                            {
                                try
                                {
                                    return JDBCClassLoader.getConnection(jndiSource.getDriverClass(), jndiSource.getConnectionString(), jndiSource.getUsername(), jndiSource.getPassword());
                                }
                                catch (Exception e)
                                {
                                    if (LOG.isLoggable(Level.FINE))
                                        LOG.log(Level.FINE, "JFreeReportVisitor.getConnection ", e);
                                    throw new SQLException(e.getMessage());
                                }
                            }
                        });
                        Query query = queries.get(0);
                        sqlReportDataFactory.setQuery(query.getQueryName(), query.getQuery());
                        multiDataFactory.addDataFactory(sqlReportDataFactory);
                    }
                }
            }
        }
        else if (multiDataSetReportElement.getConnectionType() == MultiDataSetReportElement.ConnectionType.XQuery)
        {
            final String xQueryDataFile = multiDataSetReportElement.getXQueryDataFile();
            MultiDataFactory multiDataFactory = this.multiDataFactory;
            if (multiDataFactory != null && !StringUtils.isEmpty(xQueryDataFile))
            {
                ArrayList<Query> queries = multiDataSetReportElement.getQueries();
                if (!queries.isEmpty())
                {
                    try
                    {
                        XPathDataFactory xPathDataFactory = new XPathDataFactory(multiDataSetReportElement.getQueryName(), new File(xQueryDataFile).toURI().toURL(), multiDataSetReportElement.getQueries().get(0).getQuery());
                        multiDataFactory.addExtendedDataFactory(xPathDataFactory);
                    }
                    catch (MalformedURLException e)
                    {
                        if (LOG.isLoggable(Level.FINE))
                            LOG.log(Level.FINE, "JFreeReportVisitor.visit ", e);
                    }
                }
            }
        }
        else if (multiDataSetReportElement.getConnectionType() == MultiDataSetReportElement.ConnectionType.MQL)
        {
            final String xmiDefinitionFile = multiDataSetReportElement.getXmiDefinitionFile();
            MultiDataFactory multiDataFactory = this.multiDataFactory;
            if (multiDataFactory != null && !StringUtils.isEmpty(xmiDefinitionFile))
            {
                ArrayList<Query> queries = multiDataSetReportElement.getQueries();
                if (!queries.isEmpty())
                {
                    try
                    {
                        MQLDataFactory mqlDataFactory = new MQLDataFactory(multiDataSetReportElement.getQueryName(), xmiDefinitionFile, multiDataSetReportElement.getQueries().get(0).getQuery());
                        multiDataFactory.addExtendedDataFactory(mqlDataFactory);
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE))
                            LOG.log(Level.FINE, "JFreeReportVisitor.visit ", e);
                    }
                }
            }
        }
        return parent;
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull ReportGroup reportGroup) throws ReportCreationException
    {
        try
        {
            Group group = new Group();
            group.setName(reportGroup.getName());

            ArrayList<String> groupFields = new ArrayList<String>();
            addGroupFields(groupFields, reportGroup);
            for (String groupField : groupFields)
            {
                group.addField(groupField);
            }
            if (currentJFreeReport != null)
            {
                currentJFreeReport.addGroup(group);
            }
            else
            {
                jFreeReport.addGroup(group);
            }
            return group;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", reportGroup, e);
        }
    }


    private void addGroupFields(@NotNull ArrayList<String> groupFields, @NotNull ReportGroup reportGroup) throws ReportCreationException
    {
        for (int i = reportGroup.getGroupFields().length - 1; i >= 0; i--)
        {
            String s = reportGroup.getGroupFields()[i];
            groupFields.add(0, s);
        }

        ReportElement parent = reportGroup.getParent();
        if (parent instanceof ReportGroup)
        {
            addGroupFields(groupFields, (ReportGroup) parent);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull ResourceFieldReportElement resourceFieldReportElement) throws ReportCreationException
    {
        try
        {
            ResourceFieldElementFactory textFieldElementFactory = new ResourceFieldElementFactory();
            textFieldElementFactory.setFieldname(resourceFieldReportElement.getFieldName());
            textFieldElementFactory.setNullString(resourceFieldReportElement.getNullString());
            textFieldElementFactory.setResourceBase(resourceFieldReportElement.getResourceBase());
            textFieldElementFactory.setFormula("".equals(resourceFieldReportElement.getFormula().getText().trim()) ? null : resourceFieldReportElement.getFormula().getText());

            applyTextFieldValues(textFieldElementFactory, resourceFieldReportElement);
            applyElementValues(textFieldElementFactory, resourceFieldReportElement);

            return addTextElementToBand(freeParent, resourceFieldReportElement, textFieldElementFactory);
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", resourceFieldReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull ResourceMessageReportElement resourceMessageReportElement) throws ReportCreationException
    {
        try
        {
            ResourceMessageElementFactory textFieldElementFactory = new ResourceMessageElementFactory();
            textFieldElementFactory.setFormatKey(resourceMessageReportElement.getFormatKey());
            textFieldElementFactory.setNullString(resourceMessageReportElement.getNullString());
            textFieldElementFactory.setResourceBase(resourceMessageReportElement.getResourceBase());

            applyTextFieldValues(textFieldElementFactory, resourceMessageReportElement);
            applyElementValues(textFieldElementFactory, resourceMessageReportElement);

            return addTextElementToBand(freeParent, resourceMessageReportElement, textFieldElementFactory);
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", resourceMessageReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull ResourceLabelReportElement resourceLabelReportElement) throws ReportCreationException
    {
        try
        {
            ResourceLabelElementFactory textFieldElementFactory = new ResourceLabelElementFactory();
            textFieldElementFactory.setResourceKey(resourceLabelReportElement.getResourceKey());
            textFieldElementFactory.setNullString(resourceLabelReportElement.getNullString());
            textFieldElementFactory.setResourceBase(resourceLabelReportElement.getResourceBase());

            applyTextFieldValues(textFieldElementFactory, resourceLabelReportElement);
            applyElementValues(textFieldElementFactory, resourceLabelReportElement);

            return addTextElementToBand(freeParent, resourceLabelReportElement, textFieldElementFactory);
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", resourceLabelReportElement, e);
        }
    }


    @Nullable
    public Object visit(@Nullable Object freeParent, @NotNull ChartReportElement chartReportElement) throws ReportCreationException
    {
        try
        {
            Band band = (Band) freeParent;
            DrawableFieldElementFactory factory = new DrawableFieldElementFactory();
            factory.setFieldname(chartReportElement.getChartFunction().getName());
            factory.setName(chartReportElement.getName());
            factory.setAbsolutePosition(new Point2D.Double(chartReportElement.getPosition().getX(), chartReportElement.getPosition().getY()));
            factory.setMinimumSize(new FloatDimension((float) chartReportElement.getMinimumSize().getWidth(), (float) chartReportElement.getMinimumSize().getHeight()));
            Element element = factory.createElement();
            band.addElement(element);

            if (currentJFreeReport != null)
            {
                visit(currentJFreeReport.getExpressions(), chartReportElement.getChartFunction());
                visit(currentJFreeReport.getExpressions(), chartReportElement.getDataCollectorFunction());
            }
            else
            {
                visit(jFreeReport.getExpressions(), chartReportElement.getChartFunction());
                visit(jFreeReport.getExpressions(), chartReportElement.getDataCollectorFunction());
            }

            return element;
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", chartReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull TextFieldReportElement textFieldReportElement) throws ReportCreationException
    {
        try
        {
            TextFieldElementFactory textFieldElementFactory = new TextFieldElementFactory();
            textFieldElementFactory.setFieldname(textFieldReportElement.getFieldName());
            textFieldElementFactory.setNullString(textFieldReportElement.getNullString());
            textFieldElementFactory.setFormula("".equals(textFieldReportElement.getFormula().getText().trim()) ? null : textFieldReportElement.getFormula().getText());

            applyTextFieldValues(textFieldElementFactory, textFieldReportElement);
            applyElementValues(textFieldElementFactory, textFieldReportElement);

            return addTextElementToBand(freeParent, textFieldReportElement, textFieldElementFactory);
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", textFieldReportElement, e);
        }
    }


    @NotNull
    public Object visit(@Nullable Object parent, @NotNull ReportFunctionsElement reportFunctionsElement) throws ReportCreationException
    {
        try
        {
            AbstractReportDefinition report = (AbstractReportDefinition) parent;
            return report.getExpressions();
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", reportFunctionsElement, e);
        }
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull ReportFunctionElement reportFunctionElement) throws ReportCreationException
    {
        // ExpressionCollection expressionCollection = (ExpressionCollection) parent;
        // Expression freeExpression = ExpressionCreator.createFreeExpression(reportFunctionElement);
        // expressionCollection.add(freeExpression);

        if (parent instanceof ExpressionCollection)
        {
            ExpressionCollection expressionCollection = (ExpressionCollection) parent;
            Expression freeExpression = ExpressionRegistry.getInstance().createJFreeReportExpressionInstance(reportFunctionElement);
            expressionCollection.add(freeExpression);
            return freeExpression;
        }

        return null;
    }


    @NotNull
    public Object visit(@Nullable Object freeParent, @NotNull SubReportElement subReportElement) throws ReportCreationException
    {
        try
        {
            AbstractRootLevelBand abstractRootLevelBand = (AbstractRootLevelBand) freeParent;

            SubReport subReport = ReportDesignerUtils.getSubReport(subReportElement);
            JFreeReportVisitor subReportVisitor = new JFreeReportVisitor();
            if (subReport != null)
            {
                subReport.accept(null, subReportVisitor);

                org.jfree.report.SubReport jFreeSubReport = subReportVisitor.getSubReport();

                if (subReportElement.getQuery() != null && !"".equals(subReportElement.getQuery().trim()))
                {
                    jFreeSubReport.setQuery(subReportElement.getQuery());
                }

                applySubReportParameters(jFreeSubReport, subReportElement.getParameters());

                abstractRootLevelBand.addSubReport(jFreeSubReport);

                return jFreeSubReport;
            }
        }
        catch (RuntimeException e)
        {
            throw new ReportCreationException("Unknown error occured", subReportElement, e);
        }
        catch (Exception e)
        {
            throw new ReportCreationException("Unknown error occured", subReportElement, e);
        }

        return null;
    }


    @Nullable
    public Object visit(@Nullable Object parent, @NotNull SubReportDataElement subReportDataElement) throws ReportCreationException
    {
        return null;
    }


    private void applyTextFieldValues(@NotNull TextElementFactory textElementFactory, @NotNull TextReportElement textReportElement)
    {
        textElementFactory.setColor(textReportElement.getForeground());

        textElementFactory.setEmbedFont(Boolean.valueOf(textReportElement.isEmbedFont()));
        String encoding = textReportElement.getEncoding();
        if (encoding == null)
        {
            textElementFactory.setEncoding(null);
        }
        else
        {
            textElementFactory.setEncoding("".equals(encoding.trim()) ? null : encoding);
        }

        textElementFactory.setFontName(textReportElement.getFont().getName());
        textElementFactory.setFontSize(Integer.valueOf(textReportElement.getFont().getSize()));
        textElementFactory.setLineHeight(Float.valueOf((float) textReportElement.getLineHeight()));

        textElementFactory.setBold(Boolean.valueOf((textReportElement.getFont().getStyle() & Font.BOLD) == Font.BOLD));
        textElementFactory.setItalic(Boolean.valueOf((textReportElement.getFont().getStyle() & Font.ITALIC) == Font.ITALIC));
        textElementFactory.setStrikethrough(Boolean.valueOf(textReportElement.isStrikethrough()));
        textElementFactory.setUnderline(Boolean.valueOf(textReportElement.isUnderline()));

        textElementFactory.setTrimTextContent(Boolean.valueOf(textReportElement.isTrimTextContent()));
        textElementFactory.setWrapText(Boolean.valueOf(textReportElement.isWrapTextInExcel()));

        textElementFactory.setHorizontalAlignment(getElementAlignment(textReportElement.getHorizontalAlignment()));
        textElementFactory.setVerticalAlignment(getElementAlignment(textReportElement.getVerticalAlignment()));

        textElementFactory.setReservedLiteral(textReportElement.getReservedLiteral());
    }


    private void applyElementValues(@NotNull ElementFactory elementFactory, @NotNull ReportElement reportElement)
    {
        elementFactory.setName(reportElement.getName());
        elementFactory.setDynamicHeight(Boolean.valueOf(reportElement.isDynamicContent()));
        elementFactory.setAbsolutePosition(reportElement.getPosition());
        elementFactory.setMinimumSize(reportElement.getMinimumSize());

        if (reportElement.getMaximumSize().getHeight() > 0.000001 && reportElement.getMaximumSize().getWidth() > 0.000001)
        {
            elementFactory.setMaximumSize(reportElement.getMaximumSize());
        }
        if (reportElement.getPreferredSize().getHeight() > 0.000001 && reportElement.getPreferredSize().getWidth() > 0.000001)
        {
            elementFactory.setPreferredSize(reportElement.getPreferredSize());
        }
    }


    @NotNull
    private ElementAlignment getElementAlignment(@Nullable TextReportElementHorizontalAlignment horizontalAlignment)
    {
        if (horizontalAlignment == null)
        {
            return ElementAlignment.LEFT;
        }

        switch (horizontalAlignment)
        {
            case LEFT:
                return ElementAlignment.LEFT;
            case CENTER:
                return ElementAlignment.CENTER;
            case RIGHT:
                return ElementAlignment.RIGHT;
            default:
                if (LOG.isLoggable(Level.SEVERE))
                    LOG.log(Level.SEVERE, "JFreeReportVisitor.getElementAlignment horizontalAlignment = " + horizontalAlignment);
                return ElementAlignment.LEFT;
        }
    }


    @NotNull
    private ElementAlignment getElementAlignment(@Nullable TextReportElementVerticalAlignment verticalAlignment)
    {
        if (verticalAlignment == null)
        {
            return ElementAlignment.TOP;
        }

        switch (verticalAlignment)
        {
            case TOP:
                return ElementAlignment.TOP;
            case MIDDLE:
                return ElementAlignment.MIDDLE;
            case BOTTOM:
                return ElementAlignment.BOTTOM;
            default:
                if (LOG.isLoggable(Level.SEVERE))
                    LOG.log(Level.SEVERE, "JFreeReportVisitor.getElementAlignment verticalAlignment = " + verticalAlignment);
                return ElementAlignment.TOP;
        }
    }

}
