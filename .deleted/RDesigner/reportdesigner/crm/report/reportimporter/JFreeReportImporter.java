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
package org.pentaho.reportdesigner.crm.report.reportimporter;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.AnchorElement;
import org.jfree.report.Band;
import org.jfree.report.DefaultImageReference;
import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.Group;
import org.jfree.report.GroupFooter;
import org.jfree.report.GroupHeader;
import org.jfree.report.ImageElement;
import org.jfree.report.JFreeReport;
import org.jfree.report.ShapeElement;
import org.jfree.report.TextElement;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.NumberFormatFilter;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.filter.templates.AnchorFieldTemplate;
import org.jfree.report.filter.templates.DateFieldTemplate;
import org.jfree.report.filter.templates.DrawableFieldTemplate;
import org.jfree.report.filter.templates.HorizontalLineTemplate;
import org.jfree.report.filter.templates.ImageURLElementTemplate;
import org.jfree.report.filter.templates.LabelTemplate;
import org.jfree.report.filter.templates.MessageFieldTemplate;
import org.jfree.report.filter.templates.NumberFieldTemplate;
import org.jfree.report.filter.templates.RectangleTemplate;
import org.jfree.report.filter.templates.ResourceFieldTemplate;
import org.jfree.report.filter.templates.ResourceLabelTemplate;
import org.jfree.report.filter.templates.ResourceMessageTemplate;
import org.jfree.report.filter.templates.StringFieldTemplate;
import org.jfree.report.filter.templates.VerticalLineTemplate;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionCollection;
import org.jfree.report.function.FormulaExpression;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.pentaho.reportdesigner.crm.report.model.AnchorFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelGroupReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.BorderDefinition;
import org.pentaho.reportdesigner.crm.report.model.DateFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.DrawableFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.Formula;
import org.pentaho.reportdesigner.crm.report.model.LabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.LineDefinition;
import org.pentaho.reportdesigner.crm.report.model.LineDirection;
import org.pentaho.reportdesigner.crm.report.model.LineReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.NumberFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
import org.pentaho.reportdesigner.crm.report.model.PageFormatPreset;
import org.pentaho.reportdesigner.crm.report.model.PageOrientation;
import org.pentaho.reportdesigner.crm.report.model.RectangleReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFactory;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.ResourceFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceLabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceMessageReportElement;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.StyleExpression;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElementHorizontalAlignment;
import org.pentaho.reportdesigner.crm.report.model.TextReportElementVerticalAlignment;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.model.layoutmanager.ReportLayoutManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 27.03.2006
 * Time: 13:16:28
 */
public class JFreeReportImporter
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JFreeReportImporter.class.getName());

    @NotNull
    private Report report;
    @NotNull
    private PageDefinition pageDefinition;


    public JFreeReportImporter(@NotNull JFreeReport freeReport)
    {
        report = ReportFactory.createEmptyReport();
        PageFormat pageFormat = freeReport.getPageDefinition().getPageFormat(0);
        PageOrientation pageOrientation = pageFormat.getOrientation() == PageFormat.LANDSCAPE ? PageOrientation.LANDSCAPE : PageOrientation.PORTRAIT;
        pageDefinition = new PageDefinition(PageFormatPreset.getPreset(pageFormat.getWidth(), pageFormat.getHeight()),
                                            pageOrientation,
                                            pageFormat.getImageableY(),
                                            pageFormat.getHeight() - pageFormat.getImageableHeight() - pageFormat.getImageableY(),
                                            pageFormat.getImageableX(),
                                            pageFormat.getWidth() - pageFormat.getImageableWidth() - pageFormat.getImageableX(),
                                            pageFormat.getWidth(),
                                            pageFormat.getHeight());

        report.setPageDefinition(pageDefinition);
        report.setName(freeReport.getName());
        report.setUseMaxCharBounds(false);

        importStandardBands(freeReport);
        importGroups(freeReport);
        importFunctions(freeReport);
    }


    private void importFunctions(@NotNull JFreeReport freeReport)
    {
        ExpressionCollection expressions = freeReport.getExpressions();
        for (int i = 0; i < expressions.size(); i++)
        {
            Expression expression = expressions.getExpression(i);
            ReportFunctionElement reportFunctionElement = ExpressionRegistry.getInstance().createWrapperInstance(expression);
            report.getReportFunctions().addChild(reportFunctionElement);
        }
    }


    private void importStandardBands(@NotNull JFreeReport freeReport)
    {
        applyGeneralProperties(report.getPageHeaderBand(), freeReport.getPageHeader());
        report.getPageHeaderBand().getMinimumSize().setWidth(pageDefinition.getInnerPageSize().getWidth());
        addElements(report.getPageHeaderBand(), freeReport.getPageHeader());

        applyGeneralProperties(report.getReportHeaderBand(), freeReport.getReportHeader());
        report.getReportHeaderBand().getMinimumSize().setWidth(pageDefinition.getInnerPageSize().getWidth());
        addElements(report.getReportHeaderBand(), freeReport.getReportHeader());

        applyGeneralProperties(report.getItemBand(), freeReport.getItemBand());
        report.getItemBand().getMinimumSize().setWidth(pageDefinition.getInnerPageSize().getWidth());
        addElements(report.getItemBand(), freeReport.getItemBand());

        applyGeneralProperties(report.getReportFooterBand(), freeReport.getReportFooter());
        report.getReportFooterBand().getMinimumSize().setWidth(pageDefinition.getInnerPageSize().getWidth());
        addElements(report.getReportFooterBand(), freeReport.getReportFooter());

        applyGeneralProperties(report.getPageFooterBand(), freeReport.getPageFooter());
        report.getPageFooterBand().getMinimumSize().setWidth(pageDefinition.getInnerPageSize().getWidth());
        addElements(report.getPageFooterBand(), freeReport.getPageFooter());

        applyGeneralProperties(report.getWatermarkBand(), freeReport.getWatermark());
        report.getWatermarkBand().getMinimumSize().setWidth(pageDefinition.getInnerPageSize().getWidth());
        addElements(report.getWatermarkBand(), freeReport.getWatermark());

        applyGeneralProperties(report.getNoDataBand(), freeReport.getNoDataBand());
        report.getNoDataBand().getMinimumSize().setWidth(pageDefinition.getInnerPageSize().getWidth());
        addElements(report.getNoDataBand(), freeReport.getNoDataBand());

        layoutBand(report.getPageHeaderBand());
        removeMinimumSizeWhereChildrenEnforceSameSize(report.getPageHeaderBand());

        layoutBand(report.getReportHeaderBand());
        removeMinimumSizeWhereChildrenEnforceSameSize(report.getReportHeaderBand());

        layoutBand(report.getItemBand());
        removeMinimumSizeWhereChildrenEnforceSameSize(report.getItemBand());

        layoutBand(report.getReportFooterBand());
        removeMinimumSizeWhereChildrenEnforceSameSize(report.getReportFooterBand());

        layoutBand(report.getPageFooterBand());
        removeMinimumSizeWhereChildrenEnforceSameSize(report.getPageFooterBand());

        layoutBand(report.getWatermarkBand());
        removeMinimumSizeWhereChildrenEnforceSameSize(report.getWatermarkBand());

        layoutBand(report.getNoDataBand());
        removeMinimumSizeWhereChildrenEnforceSameSize(report.getNoDataBand());
    }


    private void importGroups(@NotNull JFreeReport freeReport)
    {
        ArrayList<String> groupFields = new ArrayList<String>();
        ReportGroup parentGroup = null;
        for (int i = 0; i < freeReport.getGroupCount(); i++)
        {
            Group group = freeReport.getGroup(i);

            ArrayList<String> fields = new ArrayList<String>(Arrays.asList(group.getFieldsArray()));
            fields.removeAll(groupFields);

            ReportGroup reportGroup = new ReportGroup();
            reportGroup.setGroupFields(fields.toArray(new String[fields.size()]));
            reportGroup.setName(group.getName());

            applyGeneralProperties(reportGroup.getGroupHeader(), group.getHeader());
            reportGroup.getGroupHeader().getMinimumSize().setWidth(pageDefinition.getInnerPageSize().getWidth());
            addElements(reportGroup.getGroupHeader(), group.getHeader());
            if (!reportGroup.getGroupHeader().getChildren().isEmpty())
            {
                reportGroup.getGroupHeader().setShowInLayoutGUI(true);
            }

            applyGeneralProperties(reportGroup.getGroupFooter(), group.getFooter());
            reportGroup.getGroupFooter().getMinimumSize().setWidth(pageDefinition.getInnerPageSize().getWidth());
            addElements(reportGroup.getGroupFooter(), group.getFooter());
            if (!reportGroup.getGroupFooter().getChildren().isEmpty())
            {
                reportGroup.getGroupFooter().setShowInLayoutGUI(true);
            }

            layoutBand(reportGroup.getGroupHeader());
            layoutBand(reportGroup.getGroupFooter());

            if (reportGroup.getGroupHeader().getChildren().isEmpty())
            {
                reportGroup.getGroupHeader().setMinimumSize(new DoubleDimension(0, 0));
            }
            else
            {
                reportGroup.getGroupHeader().setShowInLayoutGUI(true);//show nonempty group band in GUI
            }

            if (reportGroup.getGroupFooter().getChildren().isEmpty())
            {
                reportGroup.getGroupFooter().setMinimumSize(new DoubleDimension(0, 0));
            }
            else
            {
                reportGroup.getGroupFooter().setShowInLayoutGUI(true);//show nonempty group band in GUI
            }

            removeMinimumSizeWhereChildrenEnforceSameSize(reportGroup.getGroupHeader());
            removeMinimumSizeWhereChildrenEnforceSameSize(reportGroup.getGroupFooter());

            if (reportGroup.getGroupFields().length == 0)
            {
                reportGroup.setName("default");

                if (reportGroup.getGroupHeader().getChildren().isEmpty() && reportGroup.getGroupFooter().getChildren().isEmpty())
                {
                    //empty default group, we ignore it to not mess the UI
                    continue;
                }
            }

            //prepare for next group
            groupFields.addAll(fields);

            if (parentGroup == null)
            {
                report.getReportGroups().addChild(reportGroup);
                parentGroup = reportGroup;
            }
            else
            {
                parentGroup.addChild(reportGroup);
                parentGroup = reportGroup;
            }
        }
    }


    private void removeMinimumSizeWhereChildrenEnforceSameSize(@NotNull BandToplevelReportElement reportElement)
    {
        ArrayList<ReportElement> reportElements = reportElement.getChildren();
        double m = 0;
        for (ReportElement element : reportElements)
        {
            m = Math.max(m, getEnforcedHeight(element));
        }

        if (MathUtils.approxEquals(reportElement.getMinimumSize().getHeight(), m))
        {
            reportElement.setMinimumSize(new DoubleDimension(0, 0));
        }
    }


    private double getEnforcedHeight(@NotNull ReportElement reportElement)
    {
        double m = reportElement.getMinimumSize().getHeight() + reportElement.getPosition().y;
        for (ReportElement element : reportElement.getChildren())
        {
            m = Math.max(m, getEnforcedHeight(element));
        }
        return m;
    }


    private void layoutBand(@NotNull BandToplevelReportElement bandToplevelReportElement)
    {
        DoubleDimension bandSize = getSize(bandToplevelReportElement);
        //watermark always uses whole page size
        //noinspection ObjectEquality
        if (bandToplevelReportElement == report.getWatermarkBand())
        {
            bandSize.setHeight(pageDefinition.getInnerPageSize().getHeight());
        }
        bandSize.setWidth(pageDefinition.getInnerPageSize().getWidth());
        bandToplevelReportElement.setMinimumSize(bandSize);
        layoutRelativeReportElements(bandToplevelReportElement.getPosition(), bandSize, bandToplevelReportElement);
    }


    private void layoutRelativeReportElements(@NotNull Point2D.Double parentPosition, @NotNull DoubleDimension parentSize, @NotNull ReportElement reportElement)
    {
        if (reportElement.getPosition().x < 0)
        {
            reportElement.getPosition().x = -reportElement.getPosition().x / 100 * parentSize.getWidth();
        }
        if (reportElement.getPosition().y < 0)
        {
            reportElement.getPosition().y = -reportElement.getPosition().y / 100 * parentSize.getHeight();
        }

        DoubleDimension minimumSize = getDimension(parentPosition, reportElement.getPosition(), parentSize, reportElement.getMinimumSize());
        reportElement.setMinimumSize(minimumSize);
        if (reportElement.getMinimumSize().getWidth() == 0 && reportElement.getMinimumSize().getHeight() == 0)
        {
            reportElement.getMinimumSize().setSize(parentSize);
        }

        if (reportElement instanceof BandReportElement)
        {
            reportElement.setMinimumSize(new DoubleDimension(parentSize.getWidth() - reportElement.getPosition().x, reportElement.getMinimumSize().getHeight()));
        }

        reportElement.setOrigRectangle(new Rectangle2D.Double(reportElement.getPosition().x, reportElement.getPosition().y, reportElement.getMinimumSize().getWidth(), reportElement.getMinimumSize().getHeight()));
        reportElement.setRectangle(new Rectangle2D.Double(reportElement.getPosition().x, reportElement.getPosition().y, reportElement.getMinimumSize().getWidth(), reportElement.getMinimumSize().getHeight()));

        reportElement.setPreferredSize(getDimension(parentPosition, reportElement.getPosition(), parentSize, reportElement.getPreferredSize()));
        reportElement.setMaximumSize(getDimension(parentPosition, reportElement.getPosition(), parentSize, reportElement.getMaximumSize()));

        ArrayList<ReportElement> children = reportElement.getChildren();

        for (ReportElement re : children)
        {
            layoutRelativeReportElements(reportElement.getPosition(), reportElement.getMinimumSize(), re);
        }
    }


    @NotNull
    private DoubleDimension getSize(@NotNull ReportElement reportElement)
    {
        //all position-minimumSize of absolute children
        DoubleDimension totW = new DoubleDimension(reportElement.getMinimumSize());
        for (int i = 0; i < reportElement.getChildren().size(); i++)
        {
            ReportElement element = reportElement.getChildren().get(i);
            DoubleDimension w = getSize(element);
            totW.setWidth(Math.max(totW.getWidth(), w.getWidth() + (element.getPosition().x > 0 ? element.getPosition().x : 0)));
            totW.setHeight(Math.max(totW.getHeight(), w.getHeight() + (element.getPosition().y > 0 ? element.getPosition().y : 0)));

            if (reportElement.getMaximumSize().getWidth() > 0)
            {
                totW.setWidth(Math.min(totW.getWidth(), reportElement.getMaximumSize().getWidth()));
            }
            if (reportElement.getMaximumSize().getHeight() > 0)
            {
                totW.setHeight(Math.min(totW.getHeight(), reportElement.getMaximumSize().getHeight()));
            }
        }

        if (totW.getWidth() > reportElement.getMinimumSize().getWidth())
        {
            reportElement.setMinimumSize(new DoubleDimension(totW.getWidth(), reportElement.getMinimumSize().getHeight()));
        }
        if (totW.getHeight() > reportElement.getMinimumSize().getHeight())
        {
            reportElement.setMinimumSize(new DoubleDimension(reportElement.getMinimumSize().getWidth(), totW.getHeight()));
        }

        return totW;
    }


    @NotNull
    public Report getReport()
    {
        return report;
    }


    private void addElements(@NotNull ReportElement parent, @NotNull Element freeParent)
    {
        if (freeParent instanceof Band)
        {
            Band band = (Band) freeParent;
            for (int i = 0; i < band.getElementCount(); i++)
            {
                Element element = band.getElement(i);
                addElement(parent, element);
            }
        }
    }


    private void addElement(@NotNull ReportElement parent, @NotNull Element element)
    {
        if (parent == null)
        {
            throw new IllegalArgumentException("parent must not be null");
        }
        if (element == null)
        {
            throw new IllegalArgumentException("element must not be null");
        }

        if (element instanceof TextElement)
        {
            TextElement textElement = (TextElement) element;
            TextReportElement textReportElement;
            if (textElement.getDataSource() instanceof LabelTemplate)
            {
                LabelTemplate labelTemplate = (LabelTemplate) textElement.getDataSource();
                LabelReportElement labelReportElement = new LabelReportElement();
                labelReportElement.setText(labelTemplate.getContent());

                textReportElement = labelReportElement;
            }
            else if (textElement.getDataSource() instanceof StringFieldTemplate)
            {
                TextFieldReportElement textFieldReportElement = new TextFieldReportElement();
                StringFieldTemplate stringFieldTemplate = (StringFieldTemplate) textElement.getDataSource();
                textFieldReportElement.setFieldName(stringFieldTemplate.getField());
                textFieldReportElement.setNullString(stringFieldTemplate.getNullValue() != null ? stringFieldTemplate.getNullValue() : "");
                textFieldReportElement.setFormula(new Formula(stringFieldTemplate.getFormula() != null ? stringFieldTemplate.getFormula() : ""));

                textReportElement = textFieldReportElement;
            }
            else if (textElement.getDataSource() instanceof MessageFieldTemplate)
            {
                MessageFieldReportElement textFieldReportElement = new MessageFieldReportElement();
                MessageFieldTemplate stringFieldTemplate = (MessageFieldTemplate) textElement.getDataSource();
                textFieldReportElement.setFormatString(stringFieldTemplate.getFormat());
                textFieldReportElement.setNullString(stringFieldTemplate.getNullValue() != null ? stringFieldTemplate.getNullValue() : "");
                //textFieldReportElement.setFormula(stringFieldTemplate.getFormula());

                textReportElement = textFieldReportElement;
            }
            else if (textElement.getDataSource() instanceof DateFieldTemplate)
            {
                DateFieldReportElement textFieldReportElement = new DateFieldReportElement();
                DateFieldTemplate stringFieldTemplate = (DateFieldTemplate) textElement.getDataSource();
                textFieldReportElement.setFieldName(stringFieldTemplate.getField());
                textFieldReportElement.setFormat(stringFieldTemplate.getDateFormat());
                textFieldReportElement.setNullString(stringFieldTemplate.getNullValue() != null ? stringFieldTemplate.getNullValue() : "");
                textFieldReportElement.setFormula(new Formula(stringFieldTemplate.getFormula() != null ? stringFieldTemplate.getFormula() : ""));

                textReportElement = textFieldReportElement;
            }
            else if (textElement.getDataSource() instanceof NumberFieldTemplate)
            {
                NumberFieldReportElement textFieldReportElement = new NumberFieldReportElement();
                NumberFieldTemplate stringFieldTemplate = (NumberFieldTemplate) textElement.getDataSource();
                textFieldReportElement.setFieldName(stringFieldTemplate.getField());
                textFieldReportElement.setFormat(stringFieldTemplate.getDecimalFormat());
                textFieldReportElement.setNullString(stringFieldTemplate.getNullValue() != null ? stringFieldTemplate.getNullValue() : "");
                textFieldReportElement.setFormula(new Formula(stringFieldTemplate.getFormula() != null ? stringFieldTemplate.getFormula() : ""));

                textReportElement = textFieldReportElement;
            }
            else if (textElement.getDataSource() instanceof NumberFormatFilter)
            {
                NumberFieldReportElement textFieldReportElement = new NumberFieldReportElement();
                NumberFormatFilter stringFieldTemplate = (NumberFormatFilter) textElement.getDataSource();
                DataSource dataSource = stringFieldTemplate.getDataSource();
                if (dataSource instanceof DataRowDataSource)
                {
                    DataRowDataSource dataRowDataSource = (DataRowDataSource) dataSource;
                    textFieldReportElement.setFieldName(dataRowDataSource.getDataSourceColumnName());
                }
                textFieldReportElement.setFormat(stringFieldTemplate.getNumberFormat());
                textFieldReportElement.setNullString(stringFieldTemplate.getNullValue() != null ? stringFieldTemplate.getNullValue() : "");

                textReportElement = textFieldReportElement;
            }
            else if (textElement.getDataSource() instanceof ResourceFieldTemplate)
            {
                ResourceFieldReportElement textFieldReportElement = new ResourceFieldReportElement();
                ResourceFieldTemplate stringFieldTemplate = (ResourceFieldTemplate) textElement.getDataSource();
                textFieldReportElement.setFieldName(stringFieldTemplate.getField());
                textFieldReportElement.setResourceBase(stringFieldTemplate.getResourceIdentifier());
                textFieldReportElement.setNullString(stringFieldTemplate.getNullValue() != null ? stringFieldTemplate.getNullValue() : "");
                textFieldReportElement.setFormula(new Formula(stringFieldTemplate.getFormula() != null ? stringFieldTemplate.getFormula() : ""));

                textReportElement = textFieldReportElement;
            }
            else if (textElement.getDataSource() instanceof ResourceLabelTemplate)
            {
                ResourceLabelReportElement textFieldReportElement = new ResourceLabelReportElement();
                ResourceLabelTemplate stringFieldTemplate = (ResourceLabelTemplate) textElement.getDataSource();
                textFieldReportElement.setResourceKey(stringFieldTemplate.getContent());
                textFieldReportElement.setResourceBase(stringFieldTemplate.getResourceIdentifier());
                textFieldReportElement.setNullString(stringFieldTemplate.getNullValue() != null ? stringFieldTemplate.getNullValue() : "");

                textReportElement = textFieldReportElement;
            }
            else if (textElement.getDataSource() instanceof ResourceMessageTemplate)
            {
                ResourceMessageReportElement textFieldReportElement = new ResourceMessageReportElement();
                ResourceMessageTemplate stringFieldTemplate = (ResourceMessageTemplate) textElement.getDataSource();
                textFieldReportElement.setFormatKey(stringFieldTemplate.getFormatKey());
                textFieldReportElement.setResourceBase(stringFieldTemplate.getResourceIdentifier());
                textFieldReportElement.setNullString(stringFieldTemplate.getNullValue() != null ? stringFieldTemplate.getNullValue() : "");

                textReportElement = textFieldReportElement;
            }
            else
            {
                //noinspection ThrowableInstanceNeverThrown
                UncaughtExcpetionsModel.getInstance().addException(new RuntimeException("Unsupported datasource encountered while importing textelement: " + textElement.getDataSource()));
                return;
            }

            applyGeneralProperties(textReportElement, element);
            applyTextProperties(textReportElement, textElement);
            parent.addChild(textReportElement);
        }
        else if (element instanceof ShapeElement)
        {
            ShapeElement shapeElement = (ShapeElement) element;
            if (shapeElement.getDataSource() instanceof RectangleTemplate)
            {
                RectangleReportElement rectangleReportElement = new RectangleReportElement();
                rectangleReportElement.setColor((Color) shapeElement.getStyle().getStyleProperty(ElementStyleSheet.PAINT, Color.GRAY));
                rectangleReportElement.setDrawBorder(shapeElement.isShouldDraw());
                rectangleReportElement.setFill(shapeElement.isShouldFill());
                if (shapeElement.getStroke() instanceof BasicStroke)
                {
                    BasicStroke basicStroke = (BasicStroke) shapeElement.getStroke();
                    rectangleReportElement.setBorderDefinition(new BorderDefinition((Color) shapeElement.getStyle().getStyleProperty(ElementStyleSheet.PAINT, Color.GRAY), basicStroke));
                }

                applyGeneralProperties(rectangleReportElement, element);
                parent.addChild(rectangleReportElement);
            }
            else if (shapeElement.getDataSource() instanceof StaticDataSource)
            {
                StaticDataSource staticDataSource = (StaticDataSource) shapeElement.getDataSource();
                if (staticDataSource.getValue() instanceof Rectangle2D)
                {
                    RectangleReportElement rectangleReportElement = new RectangleReportElement();
                    rectangleReportElement.setColor((Color) shapeElement.getStyle().getStyleProperty(ElementStyleSheet.PAINT, Color.GRAY));
                    rectangleReportElement.setDrawBorder(shapeElement.isShouldDraw());
                    rectangleReportElement.setFill(shapeElement.isShouldFill());
                    if (shapeElement.getStroke() instanceof BasicStroke)
                    {
                        BasicStroke basicStroke = (BasicStroke) shapeElement.getStroke();
                        rectangleReportElement.setBorderDefinition(new BorderDefinition((Color) shapeElement.getStyle().getStyleProperty(ElementStyleSheet.PAINT, Color.GRAY), basicStroke));
                    }

                    applyGeneralProperties(rectangleReportElement, element);
                    parent.addChild(rectangleReportElement);
                }
                else if (staticDataSource.getValue() instanceof Line2D)
                {
                    Line2D rect = (Line2D) staticDataSource.getValue();
                    LineReportElement lineReportElement = new LineReportElement();
                    lineReportElement.setLineDefinition(lineReportElement.getLineDefinition().derive((Color) shapeElement.getStyle().getStyleProperty(ElementStyleSheet.PAINT, Color.GRAY)));
                    if (shapeElement.getStroke() instanceof BasicStroke)
                    {
                        BasicStroke basicStroke = (BasicStroke) shapeElement.getStroke();
                        LineDefinition lineDefinition = new LineDefinition((Color) shapeElement.getStyle().getStyleProperty(ElementStyleSheet.PAINT, Color.GRAY), basicStroke);
                        lineReportElement.setLineDefinition(lineDefinition);
                    }
                    else
                    {
                        lineReportElement.setLineDefinition(lineReportElement.getLineDefinition().derive(1));
                    }
                    if (rect.getX1() == rect.getX2())
                    {
                        lineReportElement.setDirection(LineDirection.VERTICAL);
                    }
                    else if (rect.getY1() == rect.getY2())
                    {
                        lineReportElement.setDirection(LineDirection.HORIZONTAL);
                    }
                    else
                    {
                        //noinspection ThrowableInstanceNeverThrown
                        UncaughtExcpetionsModel.getInstance().addException(new RuntimeException("Can not import arbitrary lines"));
                        return;
                    }

                    applyGeneralProperties(lineReportElement, element);
                    parent.addChild(lineReportElement);
                }
                else
                {
                    //noinspection ThrowableInstanceNeverThrown
                    UncaughtExcpetionsModel.getInstance().addException(new RuntimeException("Unsupported element encountered while importing staticdatasource: " + staticDataSource.getValue()));
                    //noinspection UnnecessaryReturnStatement
                    return;//just to be clear
                }
            }
            else if (shapeElement.getDataSource() instanceof HorizontalLineTemplate)
            {
                LineReportElement lineReportElement = new LineReportElement();
                lineReportElement.setLineDefinition(lineReportElement.getLineDefinition().derive((Color) shapeElement.getStyle().getStyleProperty(ElementStyleSheet.PAINT, Color.GRAY)));
                lineReportElement.setDirection(LineDirection.HORIZONTAL);
                if (shapeElement.getStroke() instanceof BasicStroke)
                {
                    BasicStroke basicStroke = (BasicStroke) shapeElement.getStroke();
                    lineReportElement.setLineDefinition(lineReportElement.getLineDefinition().derive(basicStroke.getLineWidth()));
                }
                else
                {
                    lineReportElement.setLineDefinition(lineReportElement.getLineDefinition().derive(1));
                }

                applyGeneralProperties(lineReportElement, element);
                parent.addChild(lineReportElement);
            }
            else if (shapeElement.getDataSource() instanceof VerticalLineTemplate)
            {
                LineReportElement lineReportElement = new LineReportElement();
                lineReportElement.setLineDefinition(lineReportElement.getLineDefinition().derive((Color) shapeElement.getStyle().getStyleProperty(ElementStyleSheet.PAINT, Color.GRAY)));
                lineReportElement.setDirection(LineDirection.VERTICAL);
                if (shapeElement.getStroke() instanceof BasicStroke)
                {
                    BasicStroke basicStroke = (BasicStroke) shapeElement.getStroke();
                    lineReportElement.setLineDefinition(lineReportElement.getLineDefinition().derive(basicStroke.getLineWidth()));
                }
                else
                {
                    lineReportElement.setLineDefinition(lineReportElement.getLineDefinition().derive(1));
                }

                applyGeneralProperties(lineReportElement, element);
                parent.addChild(lineReportElement);
            }
            else
            {
                //noinspection ThrowableInstanceNeverThrown
                UncaughtExcpetionsModel.getInstance().addException(new RuntimeException("Unsupported datasource encountered while importing shapeelement: " + shapeElement.getDataSource()));
                //noinspection UnnecessaryReturnStatement
                return;//just to be clear
            }
        }
        else if (element instanceof Band)
        {
            Band band = (Band) element;
            BandReportElement bandReportElement = new BandReportElement();

            if ("block".equals(band.getStyle().getStyleProperty(BandStyleKeys.LAYOUT, "")))//NON-NLS
            {

            }
            else
            {
                bandReportElement.setReportLayoutManagerType(ReportLayoutManager.Type.NULL);
            }

            applyGeneralProperties(bandReportElement, element);

            parent.addChild(bandReportElement);

            addElements(bandReportElement, band);
        }
        else if (element instanceof ImageElement)
        {
            ImageElement imageElement = (ImageElement) element;

            if (imageElement.getDataSource() instanceof ImageURLElementTemplate)
            {
                ImageURLElementTemplate imageURLElementTemplate = (ImageURLElementTemplate) imageElement.getDataSource();
                Object urlObject = imageURLElementTemplate.getValue(null);
                StaticImageReportElement staticImageReportElement = new StaticImageReportElement();

                if (urlObject instanceof DefaultImageReference)
                {
                    DefaultImageReference url = (DefaultImageReference) urlObject;
                    staticImageReportElement.setUrl(url.getSourceURL());
                }
                else
                {
                    String content = imageURLElementTemplate.getContent();
                    try
                    {
                        staticImageReportElement.setUrl(new URL(content));
                    }
                    catch (MalformedURLException e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JFreeReportImporter.addElement ", e);
                    }
                }
                staticImageReportElement.setKeepAspect(imageElement.isKeepAspectRatio());

                applyGeneralProperties(staticImageReportElement, element);
                parent.addChild(staticImageReportElement);
            }

            //DataSource dataSource = imageElement.getDataSource();

            //applyGeneralProperties(bandReportElement, element);

            //parent.addChild(bandReportElement);
        }
        else if (element instanceof DrawableElement)
        {
            DrawableElement imageElement = (DrawableElement) element;

            if (imageElement.getDataSource() instanceof DrawableFieldTemplate)
            {
                DrawableFieldTemplate imageURLElementTemplate = (DrawableFieldTemplate) imageElement.getDataSource();

                DrawableFieldReportElement staticImageReportElement = new DrawableFieldReportElement();
                staticImageReportElement.setFieldName(imageURLElementTemplate.getField());
                staticImageReportElement.setFormula(new Formula(imageURLElementTemplate.getFormula() != null ? imageURLElementTemplate.getFormula() : ""));

                applyGeneralProperties(staticImageReportElement, element);
                parent.addChild(staticImageReportElement);
            }
            else
            {
                //noinspection ThrowableInstanceNeverThrown
                UncaughtExcpetionsModel.getInstance().addException(new RuntimeException("Unsupported datasource encountered while importing drawableelement: " + element.getClass()));
            }

            //DataSource dataSource = imageElement.getDataSource();

            //applyGeneralProperties(bandReportElement, element);

            //parent.addChild(bandReportElement);
        }
        else if (element instanceof AnchorElement)
        {
            AnchorElement anchorElement = (AnchorElement) element;

            if (anchorElement.getDataSource() instanceof AnchorFieldTemplate)
            {
                AnchorFieldTemplate anchorFieldTemplate = (AnchorFieldTemplate) anchorElement.getDataSource();

                AnchorFieldReportElement anchorFieldReportElement = new AnchorFieldReportElement();
                anchorFieldReportElement.setFieldName(anchorFieldTemplate.getField());

                applyGeneralProperties(anchorFieldReportElement, element);
                parent.addChild(anchorFieldReportElement);
            }
        }
        else
        {
            if (LOG.isLoggable(Level.SEVERE)) LOG.log(Level.SEVERE, "Unsupported element encountered while importing: " + element.getClass());
        }
    }


    private void applyTextProperties(@NotNull TextReportElement textReportElement, @NotNull TextElement textElement)
    {
        textReportElement.setFont(textElement.getFont().getFont());
        textReportElement.setEmbedFont(textElement.getFont().isEmbeddedFont());
        textReportElement.setEncoding(textElement.getFont().getFontEncoding(null));//NON-NLS
        textReportElement.setForeground((Color) textElement.getStyle().getStyleProperty(ElementStyleSheet.PAINT, Color.BLACK));
        textReportElement.setHorizontalAlignment(getElementHorizontalAlignment(textElement));
        textReportElement.setVerticalAlignment(getElementVerticalAlignment(textElement));
        textReportElement.setLineHeight(textElement.getLineHeight());
        textReportElement.setReservedLiteral(textElement.getReservedLiteral() != null ? textElement.getReservedLiteral() : "..");
        textReportElement.setStrikethrough(textElement.isStrikethrough());
        textReportElement.setTrimTextContent(((Boolean) textElement.getStyle().getStyleProperty(ElementStyleSheet.TRIM_TEXT_CONTENT, Boolean.TRUE)).booleanValue());
        textReportElement.setUnderline(textElement.isUnderline());
        textReportElement.setWrapTextInExcel(((Boolean) textElement.getStyle().getStyleProperty(ElementStyleSheet.EXCEL_WRAP_TEXT, Boolean.FALSE)).booleanValue());
    }


    private void applyGeneralProperties(@NotNull ReportElement reportElement, @NotNull Element element)
    {
        reportElement.setName(element.getName());

        reportElement.setDynamicContent(element.isDynamicContent());

        reportElement.getMinimumSize().setSize(element.getMinimumSize());
        reportElement.getPreferredSize().setSize(getDimension(element.getPreferredSize()));
        reportElement.getMaximumSize().setSize(getDimension(element.getMaximumSize()));

        reportElement.setPosition(getAbsolutePosition(element));

        Map map = element.getStyleExpressions();
        Set styleKeys = map.keySet();
        for (Object key : styleKeys)
        {
            StyleKey styleKey = (StyleKey) key;
            Expression exp = (Expression) map.get(styleKey);
            if (styleKey != null && exp instanceof FormulaExpression)
            {
                FormulaExpression formulaExpression = (FormulaExpression) exp;
                reportElement.getStyleExpressions().addStyleExpression(new StyleExpression(styleKey.getName(), formulaExpression.getFormula()));
            }
        }


        if (reportElement instanceof BandToplevelReportElement && element instanceof Band)
        {
            BandToplevelReportElement bandToplevelReportElement = (BandToplevelReportElement) reportElement;
            Band band = (Band) element;
            bandToplevelReportElement.setPageBreakBefore(band.isPagebreakBeforePrint());
            bandToplevelReportElement.setPageBreakAfter(band.isPagebreakAfterPrint());
        }

        if (reportElement instanceof BandToplevelGroupReportElement && element instanceof GroupHeader)
        {
            BandToplevelGroupReportElement bandToplevelReportElement = (BandToplevelGroupReportElement) reportElement;
            GroupHeader band = (GroupHeader) element;
            bandToplevelReportElement.setRepeat(band.isRepeat());
        }

        if (reportElement instanceof BandToplevelGroupReportElement && element instanceof GroupFooter)
        {
            BandToplevelGroupReportElement bandToplevelReportElement = (BandToplevelGroupReportElement) reportElement;
            GroupFooter band = (GroupFooter) element;
            bandToplevelReportElement.setRepeat(band.isRepeat());
        }
    }


    @NotNull
    private Point2D.Double getAbsolutePosition(@NotNull Element element)
    {
        Point2D pos = (Point2D) element.getStyle().getStyleProperty(StyleKey.getStyleKey("absolute_pos"));//NON-NLS
        if (pos != null)
        {
            double posX = pos.getX();
            double posY = pos.getY();

            return new Point2D.Double(posX, posY);
        }
        return new Point2D.Double(0, 0);
    }


    @NotNull
    private DoubleDimension getDimension(@Nullable Dimension2D dimension2D)
    {
        if (dimension2D == null)
        {
            return new DoubleDimension(0, 0);
        }

        double width = dimension2D.getWidth();
        double heigth = dimension2D.getHeight();

        return new DoubleDimension(width, heigth);
    }


    @SuppressWarnings({"UnusedDeclaration"})
    @NotNull
    private DoubleDimension getDimension(@NotNull Point2D.Double parentPosition, @NotNull Point2D.Double position, @NotNull DoubleDimension parent, @NotNull DoubleDimension child)
    {
        double width = child.getWidth();
        double heigth = child.getHeight();

        if (width < 0)
        {
            width = -width / 100. * parent.getWidth();
            width -= parentPosition.getX();
        }

        if (heigth < 0)
        {
            heigth = -heigth / 100. * parent.getHeight();
            heigth -= parentPosition.getY();
        }

        return new DoubleDimension(width, heigth);
    }


    @NotNull
    @SuppressWarnings({"ObjectEquality"})
    private TextReportElementHorizontalAlignment getElementHorizontalAlignment(@NotNull TextElement textElement)
    {
        ElementAlignment elementAlignment = (ElementAlignment) textElement.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT, ElementAlignment.LEFT);
        if (elementAlignment == ElementAlignment.LEFT)
        {
            return TextReportElementHorizontalAlignment.LEFT;
        }
        else if (elementAlignment == ElementAlignment.CENTER)
        {
            return TextReportElementHorizontalAlignment.CENTER;
        }
        else if (elementAlignment == ElementAlignment.RIGHT)
        {
            return TextReportElementHorizontalAlignment.RIGHT;
        }
        else
        {
            return TextReportElementHorizontalAlignment.LEFT;
        }
    }


    @NotNull
    @SuppressWarnings({"ObjectEquality"})
    private TextReportElementVerticalAlignment getElementVerticalAlignment(@NotNull TextElement textElement)
    {
        ElementAlignment elementAlignment = (ElementAlignment) textElement.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT, ElementAlignment.TOP);
        if (elementAlignment == ElementAlignment.TOP)
        {
            return TextReportElementVerticalAlignment.TOP;
        }
        else if (elementAlignment == ElementAlignment.MIDDLE)
        {
            return TextReportElementVerticalAlignment.MIDDLE;
        }
        else if (elementAlignment == ElementAlignment.BOTTOM)
        {
            return TextReportElementVerticalAlignment.BOTTOM;
        }
        else
        {
            return TextReportElementVerticalAlignment.TOP;
        }
    }
}
