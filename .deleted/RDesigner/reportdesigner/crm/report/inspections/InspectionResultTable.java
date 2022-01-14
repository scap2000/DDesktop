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
package org.pentaho.reportdesigner.crm.report.inspections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Set;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 19:12:03
 */
public class InspectionResultTable extends JTable
{

    @NotNull
    private InspectionResultTableModel inspectionResultTableModel;

    private static final int COLUMN_0_WIDTH = 30;
    private static final int COLUMN_1_WIDTH = 200;


    public InspectionResultTable()
    {
        inspectionResultTableModel = new InspectionResultTableModel();
        setModel(inspectionResultTableModel);

        int origMode = getAutoResizeMode();
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        getColumnModel().getColumn(0).setMinWidth(COLUMN_0_WIDTH);
        getColumnModel().getColumn(0).setMaxWidth(COLUMN_0_WIDTH);
        getColumnModel().getColumn(0).setWidth(COLUMN_0_WIDTH);
        getColumnModel().getColumn(0).setPreferredWidth(COLUMN_0_WIDTH);

        getColumnModel().getColumn(1).setMinWidth(0);
        getColumnModel().getColumn(1).setMaxWidth(400);
        getColumnModel().getColumn(1).setWidth(COLUMN_1_WIDTH);
        getColumnModel().getColumn(1).setPreferredWidth(COLUMN_1_WIDTH);

        getColumnModel().getColumn(2).setMinWidth(0);
        getColumnModel().getColumn(2).setMaxWidth(Integer.MAX_VALUE);
        getColumnModel().getColumn(2).setWidth(800 - (COLUMN_0_WIDTH + COLUMN_1_WIDTH));
        getColumnModel().getColumn(2).setPreferredWidth(800 - (COLUMN_0_WIDTH + COLUMN_1_WIDTH));

        setAutoResizeMode(origMode);

        getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer()
        {
            @NotNull
            public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
                try
                {
                    InspectionResult.Severity severity = (InspectionResult.Severity) value;
                    if (severity == InspectionResult.Severity.ERROR)
                    {
                        label.setIcon(IconLoader.getInstance().getErrorIcon());
                    }
                    else if (severity == InspectionResult.Severity.WARNING)
                    {
                        label.setIcon(IconLoader.getInstance().getWarningIcon());
                    }
                    else if (severity == InspectionResult.Severity.HINT)
                    {
                        label.setIcon(IconLoader.getInstance().getInfoIcon());
                    }
                }
                catch (Throwable e)
                {
                    UncaughtExcpetionsModel.getInstance().addException(e);
                }
                return label;
            }
        });
    }


    public void setInspectionResults(@NotNull Set<InspectionResult> inspectionResults)
    {
        inspectionResultTableModel.updateInspectionResults(inspectionResults);
    }


    @NotNull
    public InspectionResult getInspectionResult(int row)
    {
        return inspectionResultTableModel.getInspectionResult(row);
    }
}
