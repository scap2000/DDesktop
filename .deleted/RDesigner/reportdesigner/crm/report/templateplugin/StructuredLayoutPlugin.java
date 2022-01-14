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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.function.Expression;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.lineal.GuideLine;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelType;
import org.pentaho.reportdesigner.crm.report.model.LabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
import org.pentaho.reportdesigner.crm.report.model.PageFormatPreset;
import org.pentaho.reportdesigner.crm.report.model.PageOrientation;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionsElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.ReportGroups;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard.GroupInfo;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.client.util.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 27.02.2006
 * Time: 18:22:59
 */
public class StructuredLayoutPlugin extends AbstractLayoutPlugin
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(StructuredLayoutPlugin.class.getName());


    public StructuredLayoutPlugin()
    {
    }


    @NotNull
    public String getLocalizedName()
    {
        return TranslationManager.getInstance().getTranslation("R", "StructuredLayoutPlugin.Name");
    }


    @NotNull
    public ImageIcon getSampleImage()
    {
        return IconLoader.getInstance().getTemplateStructuredIcon();
    }


    public void addVisualReportElements(@NotNull Report report, @NotNull HashMap<String, WizardData> wizardDatas, @NotNull LayoutStyle layoutStyle) throws LayoutException
    {
        if (layoutStyle == null)
        {
            throw new IllegalArgumentException("layoutStyle must not be null");
        }

        @Nullable
        WizardData visibleColumnInfoData = wizardDatas.get(WizardData.VISIBLE_COLUMN_INFOS);
        ArrayList<ColumnInfo> visibleColumnInfos = new ArrayList<ColumnInfo>();
        if (visibleColumnInfoData != null)
        {
            //noinspection unchecked
            visibleColumnInfos = new ArrayList<ColumnInfo>((ArrayList<ColumnInfo>) visibleColumnInfoData.getValue());
        }

        @Nullable
        WizardData columnGroupsWizardData = wizardDatas.get(WizardData.COLUMN_GROUPS);
        ArrayList<GroupInfo> columnGroups = new ArrayList<GroupInfo>();
        if (columnGroupsWizardData != null)
        {
            //noinspection unchecked
            columnGroups = (ArrayList<GroupInfo>) columnGroupsWizardData.getValue();
        }

        @Nullable
        WizardData pageDefinitionWizardData = wizardDatas.get(WizardData.PAGE_DEFINITION);
        PageDefinition pageDefinition;
        if (pageDefinitionWizardData == null)
        {
            pageDefinition = new PageDefinition(PageFormatPreset.A4, PageOrientation.PORTRAIT, 20, 20, 20, 20);
        }
        else
        {
            pageDefinition = (PageDefinition) pageDefinitionWizardData.getValue();
        }

        double pageWidth = pageDefinition.getInnerPageSize().getWidth();

        BandToplevelReportElement reportHeaderBand = ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.REPORT_HEADER).createReportElement();
        LabelReportElement titleLabel = ReportElementInfoFactory.getInstance().getLabelReportElementInfo().createReportElement();
        titleLabel.setText(TranslationManager.getInstance().getTranslation("R", "StructuredLayoutPlugin.ReportTitle"));
        titleLabel.setFont(FontUtils.getFont(layoutStyle.getBaseFontName(), Font.BOLD, 26));
        titleLabel.setMinimumSize(new DoubleDimension(pageWidth, 40));
        reportHeaderBand.addChild(titleLabel);

        ReportGroups reportGroups = ReportElementInfoFactory.getInstance().getReportGroupsElementInfo().createReportElement();
        boolean addedToToplevelReportGroups = false;

        ReportGroup parentGroup = null;

        double tableHeaderPosition = 40 + 10;
        BandToplevelReportElement tableHeaderBand = reportHeaderBand;

        for (int i = 0; i < columnGroups.size(); i++)
        {
            GroupInfo columnInfo = columnGroups.get(i);
            ReportGroup reportGroupElement = ReportElementInfoFactory.getInstance().getReportGroupElementInfo().createReportElement();
            ArrayList<String> groupFields = new ArrayList<String>();
            StringBuilder sb = new StringBuilder(100);
            for (ColumnInfo info : columnInfo.getColumnInfos())
            {
                groupFields.add(info.getColumnName());
                sb.append(info.getColumnName());
                sb.append("_");
            }
            reportGroupElement.setGroupFields(groupFields.toArray(new String[groupFields.size()]));
            reportGroupElement.setName(sb.toString() + "group");
            boolean visible = false;

            double yStart = 0;
            double yEnd = 30;
            double xEnd = pageWidth;

            int visibleColumnInfoCount = 0;

            for (int j = 0; j < columnInfo.getColumnInfos().size(); j++)
            {
                ColumnInfo info = columnInfo.getColumnInfos().get(j);
                if (visibleColumnInfos.contains(info))
                {
                    visibleColumnInfoCount++;
                }
            }

            if (visibleColumnInfoCount > 0)
            {
                visible = true;
                int visibleIndex = 0;
                for (int j = 0; j < columnInfo.getColumnInfos().size(); j++)
                {
                    ColumnInfo info = columnInfo.getColumnInfos().get(j);

                    if (visibleColumnInfos.contains(info))
                    {
                        MessageFieldReportElement groupTitleLabel = ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo().createReportElement();
                        groupTitleLabel.setFormatString(info.getColumnName() + ": $(" + info.getColumnName() + ")");
                        groupTitleLabel.setFont(FontUtils.getFont(layoutStyle.getBaseFontName(), Font.BOLD, 16));
                        if (layoutStyle.getBaseColor() != null)
                        {
                            groupTitleLabel.setBackground(getBackgroundColor(layoutStyle.getBaseColor(), columnGroups.size(), i));
                        }
                        groupTitleLabel.setPosition(new Point2D.Double(xEnd / visibleColumnInfoCount * visibleIndex, yStart));
                        groupTitleLabel.setMinimumSize(new DoubleDimension(xEnd / visibleColumnInfoCount, yEnd - yStart));
                        reportGroupElement.getGroupHeader().addChild(groupTitleLabel);
                        reportGroupElement.getGroupHeader().setShowInLayoutGUI(true);
                        reportGroupElement.getGroupHeader().setRepeat(true);

                        visibleIndex++;
                    }
                }
            }

            if (!addedToToplevelReportGroups)
            {
                reportGroups.addChild(reportGroupElement);
                addedToToplevelReportGroups = true;
            }
            else
            {
                parentGroup.addChild(reportGroupElement);
            }

            parentGroup = reportGroupElement;

            if (visible)
            {
                tableHeaderPosition = yEnd;
            }
            else
            {
                tableHeaderPosition = yStart;
            }
            tableHeaderBand = reportGroupElement.getGroupHeader();
        }


        visibleColumnInfos.removeAll(columnGroups);

        addTableHeader(tableHeaderPosition, pageWidth, tableHeaderBand, visibleColumnInfos, columnGroups, layoutStyle);

        BandToplevelReportElement itemBand = addItems(pageWidth, visibleColumnInfos, columnGroups, layoutStyle);

        //set up functions
        BandToplevelReportElement reportFooter = ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.REPORT_FOOTER).createReportElement();

        ReportFunctionsElement reportFunctions = ReportElementInfoFactory.getInstance().getReportFunctionsElementInfo().createReportElement();

        @Nullable
        WizardData expressionsWizardData = wizardDatas.get(WizardData.COLUMN_EXPRESSIONS);
        if (expressionsWizardData != null)
        {
            //noinspection unchecked
            HashMap<ColumnInfo, Class> infos = (HashMap<ColumnInfo, Class>) expressionsWizardData.getValue();
            for (ColumnInfo columnInfo : infos.keySet())
            {
                ArrayList<ReportElement> children = itemBand.getChildren();
                for (ReportElement reportElement : children)
                {
                    if (reportElement instanceof TextFieldReportElement)
                    {
                        TextFieldReportElement textFieldReportElement = (TextFieldReportElement) reportElement;
                        if (textFieldReportElement.getFieldName().equals(columnInfo.getColumnName()))
                        {
                            @Nullable
                            Class expressionInfo = infos.get(columnInfo);
                            ReportFunctionElement functionElement;
                            try
                            {
                                functionElement = ExpressionRegistry.getInstance().createWrapperInstance((Expression) expressionInfo.newInstance());
                            }
                            catch (InstantiationException e)
                            {
                                throw new LayoutException("Expression not accessible", e);
                            }
                            catch (IllegalAccessException e)
                            {
                                throw new LayoutException("Expression not accessible", e);
                            }
                            //ReportFunctionElement functionElement = ReportElementInfoFactory.getInstance().getReportFunctionElementInfo(expressionInfo.getClassname()).createReportElement();
                            String name = functionElement.getClass().getName().substring(functionElement.getClass().getName().lastIndexOf('.') + 1);
                            functionElement.setName(name);
                            try
                            {
                                String groupName = "default";//NON-NLS
                                if (parentGroup != null)
                                {
                                    groupName = parentGroup.getName();
                                }
                                functionElement.getClass().getMethod("setGroup", String.class).invoke(functionElement, groupName);//NON-NLS
                            }
                            catch (NoSuchMethodException e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportFactory.createReport ", e);
                                //ok
                            }
                            catch (IllegalAccessException e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StructuredLayoutPlugin.addVisualReportElements ", e);
                                throw new LayoutException("Method not accessible", e);
                            }
                            catch (InvocationTargetException e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StructuredLayoutPlugin.addVisualReportElements ", e);
                                throw new LayoutException("Unexpected error", e);
                            }

                            try
                            {
                                functionElement.getClass().getMethod("setField", String.class).invoke(functionElement, columnInfo.getColumnName());//NON-NLS
                            }
                            catch (NoSuchMethodException e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportFactory.createReport ", e);
                                //ok
                            }
                            catch (IllegalAccessException e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StructuredLayoutPlugin.addVisualReportElements ", e);
                                throw new LayoutException("Method not accessible", e);
                            }
                            catch (InvocationTargetException e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StructuredLayoutPlugin.addVisualReportElements ", e);
                                throw new LayoutException("Unexpected error", e);
                            }

                            TextFieldReportElement columnValueTextField = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                            columnValueTextField.setFieldName(name);
                            columnValueTextField.setFont(FontUtils.getFont(layoutStyle.getBaseFontName(), Font.PLAIN, 12));
                            if (layoutStyle.getBaseColor() != null)
                            {
                                columnValueTextField.setBackground(getBackgroundColor(layoutStyle.getBaseColor(), columnGroups.size(), columnGroups.size() + 2));
                            }
                            columnValueTextField.setMinimumSize(new DoubleDimension(textFieldReportElement.getMinimumSize().getWidth(), 16));
                            double yOff = 0;
                            columnValueTextField.setPosition(new Point2D.Double(textFieldReportElement.getPosition().getX(), yOff));


                            if (parentGroup != null)
                            {
                                parentGroup.getGroupFooter().addChild(columnValueTextField);
                                parentGroup.getGroupFooter().setShowInLayoutGUI(true);
                                parentGroup.getGroupFooter().setMinimumSize(new DoubleDimension(0, columnValueTextField.getMinimumSize().getHeight() + 20));
                            }
                            else
                            {
                                reportFooter.addChild(columnValueTextField);
                            }

                            reportFunctions.addChild(functionElement);
                        }
                    }
                }
            }
        }


        BandToplevelReportElement pageHeaderBand = ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.PAGE_HEADER).createReportElement();
        BandToplevelReportElement pageFooterBand = ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.PAGE_FOOTER).createReportElement();

        composePageHeader(reportFunctions, pageWidth, pageHeaderBand);
        composePageFooter(reportFunctions, pageWidth, pageFooterBand);

        report.addChild(reportFunctions);
        report.addChild(reportHeaderBand);
        report.addChild(reportFooter);
        report.addChild(pageHeaderBand);
        report.addChild(pageFooterBand);
        report.addChild(itemBand);
        report.addChild(ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.WATERMARK).createReportElement());
        report.addChild(ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.NO_DATA_BAND).createReportElement());
        report.addChild(reportGroups);
        report.setPageDefinition(pageDefinition);

        tableHeaderBand.getVerticalLinealModel().addGuidLine(new GuideLine(tableHeaderPosition, true));

        ArrayList<ColumnInfo> remainingCoumnInfos = new ArrayList<ColumnInfo>(visibleColumnInfos);
        for (GroupInfo groupInfo : columnGroups)
        {
            remainingCoumnInfos.removeAll(groupInfo.getColumnInfos());
        }

        for (int i = 1; i < remainingCoumnInfos.size(); i++)
        {
            report.getHorizontalLinealModel().addGuidLine(new GuideLine(pageWidth / (double) remainingCoumnInfos.size() * i, true));
        }
    }


    private void addTableHeader(double tableHeaderPosition, double pageWidth, @NotNull BandToplevelReportElement tableHeaderBand, @NotNull ArrayList<ColumnInfo> visibleColumnInfos, @NotNull ArrayList<GroupInfo> columnGroups, @NotNull LayoutStyle layoutStyle)
    {
        ArrayList<ColumnInfo> remainingCoumnInfos = new ArrayList<ColumnInfo>(visibleColumnInfos);
        for (GroupInfo groupInfo : columnGroups)
        {
            remainingCoumnInfos.removeAll(groupInfo.getColumnInfos());
        }

        if (remainingCoumnInfos.isEmpty())
        {
            return;
        }

        double xStart = 0;
        double columnWidth = pageWidth / remainingCoumnInfos.size();

        for (ColumnInfo columnInfo : remainingCoumnInfos)
        {
            LabelReportElement columnTitleLabel = ReportElementInfoFactory.getInstance().getLabelReportElementInfo().createReportElement();
            columnTitleLabel.setText(columnInfo.getColumnName());
            columnTitleLabel.setFont(FontUtils.getFont(layoutStyle.getBaseFontName(), Font.BOLD, 12));
            Color baseColor = layoutStyle.getBaseColor();
            if (baseColor != null)
            {
                columnTitleLabel.setBackground(getBackgroundColor(baseColor, columnGroups.size(), columnGroups.size() + 1));
            }
            double xOff = 0;
            double yOff = 0;
            columnTitleLabel.setMinimumSize(new DoubleDimension(columnWidth, 16));
            columnTitleLabel.setPosition(new Point2D.Double(xStart + xOff, tableHeaderPosition + yOff));
            tableHeaderBand.addChild(columnTitleLabel);
            tableHeaderBand.setShowInLayoutGUI(true);

            xStart += columnWidth + xOff;
        }
    }


    @NotNull
    private BandToplevelReportElement addItems(double pageWidth, @NotNull ArrayList<ColumnInfo> visibleColumnInfos, @NotNull ArrayList<GroupInfo> columnGroups, @NotNull LayoutStyle layoutStyle)
    {
        ArrayList<ColumnInfo> remainingCoumnInfos = new ArrayList<ColumnInfo>(visibleColumnInfos);
        for (GroupInfo groupInfo : columnGroups)
        {
            remainingCoumnInfos.removeAll(groupInfo.getColumnInfos());
        }

        BandToplevelReportElement itemBand = ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(BandToplevelType.ITEM_BAND).createReportElement();

        double xStart = 0;
        double columnWidth = pageWidth / remainingCoumnInfos.size();
        double tableHeaderPosition = 0;


        for (ColumnInfo columnInfo : remainingCoumnInfos)
        {
            TextFieldReportElement columnValueTextField = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
            columnValueTextField.setFieldName(columnInfo.getColumnName());
            columnValueTextField.setFont(FontUtils.getFont(layoutStyle.getBaseFontName(), Font.PLAIN, 12));
            Color baseColor = layoutStyle.getBaseColor();
            if (baseColor != null)
            {
                columnValueTextField.setBackground(getBackgroundColor(baseColor, columnGroups.size(), columnGroups.size() + 2));
            }
            double xOff = 0;
            double yOff = 0;
            columnValueTextField.setMinimumSize(new DoubleDimension(columnWidth, 16));
            columnValueTextField.setPosition(new Point2D.Double(xStart + xOff, tableHeaderPosition + yOff));
            itemBand.addChild(columnValueTextField);

            xStart += columnWidth + xOff;
        }
        return itemBand;
    }
}
