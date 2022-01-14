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
package org.pentaho.reportdesigner.crm.report.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.LabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.model.SubReportDataElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.lib.client.util.FontUtils;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * User: Martin
 * Date: 11.10.2005
 * Time: 15:40:36
 */
public class ElementTreeCellRenderer extends MultiLabelTreeCellRenderer
{
    @NotNull
    private static JLabel[] createLabels()
    {
        return new JLabel[]{new JLabel(), new JLabel(), new JLabel()};
    }


    public ElementTreeCellRenderer()
    {
        super(createLabels());
    }


    @NotNull
    public Component getTreeCellRendererComponent(@NotNull JTree tree, @Nullable Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        MultiLabelTreeCellRenderer renderer = (MultiLabelTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        try
        {
            renderer.setToolTipText(null);
            JLabel[] labels = renderer.getLabels();
            if (labels != null)
            {
                updateLabels(value, renderer, labels);
            }
        }
        catch (Throwable e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        return renderer;
    }


    private void updateLabels(@Nullable Object value, @NotNull MultiLabelTreeCellRenderer renderer, @NotNull JLabel[] labels)
    {
        if (value instanceof ReportElement)
        {
            ReportElement reportElement = (ReportElement) value;
            labels[0].setIcon(ReportElementInfoFactory.getInstance().getReportElementInfo(reportElement).getIcon());
        }

        if (value instanceof SubReport)
        {
            SubReport subReport = (SubReport) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(subReport).getTitle());
        }
        else if (value instanceof Report)
        {
            Report report = (Report) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(report).getTitle());
        }
        else if (value instanceof TextFieldReportElement)
        {
            TextFieldReportElement textReportElement = (TextFieldReportElement) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(textReportElement).getTitle());
            labels[1].setFont(FontUtils.getDerivedFont(labels[0].getFont(), Font.BOLD, labels[0].getFont().getSize()));
            labels[1].setText(textReportElement.getPaintText());
        }
        else if (value instanceof LabelReportElement)
        {
            LabelReportElement labelReportElement = (LabelReportElement) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(labelReportElement).getTitle());
            labels[1].setFont(FontUtils.getDerivedFont(labels[0].getFont(), Font.BOLD, labels[0].getFont().getSize()));
            labels[1].setText(getSuitableText(labelReportElement.getPaintText()));
        }
        else if (value instanceof ChartReportElement)
        {
            ChartReportElement chartReportElement = (ChartReportElement) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(chartReportElement).getTitle());
            labels[1].setText(getSuitableText(chartReportElement.getName()));
        }
        else if (value instanceof MessageFieldReportElement)
        {
            MessageFieldReportElement messageFieldReportElement = (MessageFieldReportElement) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(messageFieldReportElement).getTitle());
            labels[1].setFont(FontUtils.getDerivedFont(labels[0].getFont(), Font.BOLD, labels[0].getFont().getSize()));
            labels[1].setText(getSuitableText(messageFieldReportElement.getPaintText()));
        }
        else if (value instanceof ReportFunctionElement)
        {
            ReportFunctionElement reportElement = (ReportFunctionElement) value;

            @Nullable
            Class jFreeReportClass = ExpressionRegistry.getInstance().getWrapperToJFreeReportExpressionClassesMap().get(reportElement.getClass());
            String localizedExpressionName = null;
            String localizedDescription = null;
            if (jFreeReportClass != null)
            {
                localizedExpressionName = ExpressionRegistry.getInstance().getLocalizedExpressionName(jFreeReportClass);
                localizedDescription = ExpressionRegistry.getInstance().getLocalizedDescription(jFreeReportClass);
            }

            labels[0].setText(localizedExpressionName);
            renderer.setToolTipText("<html>" + localizedDescription + "</html>");//NON-NLS

            labels[1].setFont(FontUtils.getDerivedFont(labels[0].getFont(), Font.BOLD, labels[0].getFont().getSize()));
            labels[1].setText(reportElement.getName());
        }
        else if (value instanceof ReportGroup)
        {
            ReportGroup reportElement = (ReportGroup) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(reportElement).getTitle());
            labels[1].setFont(FontUtils.getDerivedFont(labels[0].getFont(), Font.BOLD, labels[0].getFont().getSize()));
            labels[1].setText(reportElement.getName());
            labels[2].setFont(FontUtils.getDerivedFont(labels[0].getFont(), Font.BOLD, labels[0].getFont().getSize()));
            labels[2].setText(Arrays.asList(reportElement.getGroupFields()).toString());
        }
        else if (value instanceof DataSetReportElement)
        {
            DataSetReportElement reportElement = (DataSetReportElement) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(reportElement).getTitle());
            labels[1].setFont(FontUtils.getDerivedFont(labels[0].getFont(), Font.BOLD, labels[0].getFont().getSize()));
            labels[1].setText(reportElement.getShortSummary());
        }
        else if (value instanceof SubReportDataElement)
        {
            SubReportDataElement reportElement = (SubReportDataElement) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(reportElement).getTitle());
        }
        else if (value instanceof SubReportElement)
        {
            SubReportElement reportElement = (SubReportElement) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(reportElement).getTitle());
            labels[1].setFont(FontUtils.getDerivedFont(labels[0].getFont(), Font.BOLD, labels[0].getFont().getSize()));
            labels[1].setText(reportElement.getFilePath().getPath());
        }
        else if (value instanceof ReportElement)
        {
            ReportElement reportElement = (ReportElement) value;
            labels[0].setText(ReportElementInfoFactory.getInstance().getReportElementInfo(reportElement).getTitle());
        }
    }


    @NotNull
    private String getSuitableText(@Nullable String text)
    {
        if (text != null)
        {
            text = text.replace('\n', '\u00AC');
            //noinspection ConstantConditions
            if (text.length() > 30)//MARKED noinspection should not be required
            {
                return text.substring(0, 30) + "...";
            }
            else
            {
                return text;
            }
        }
        return "";
    }

}
