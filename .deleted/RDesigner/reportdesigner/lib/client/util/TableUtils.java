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
package org.pentaho.reportdesigner.lib.client.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 10.02.2006
 * Time: 13:34:18
 */
public class TableUtils
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(TableUtils.class.getName());


    private TableUtils()
    {
    }


    public static void resizeAllColumns(@NotNull JTable table, int maxColumnWidth)
    {
        int columnCount = table.getColumnModel().getColumnCount();
        for (int i = 0; i < columnCount; i++)
        {
            int columnWidth = getColumnWidth(table, i, 100);
            columnWidth = Math.min(maxColumnWidth, columnWidth);
            table.getColumnModel().getColumn(i).setWidth(columnWidth);
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
        }
    }


    public static int getColumnWidth(@NotNull JTable table, int columnIndex, int maxRowsToCheck)
    {
        int width = -1;
        int rowCount = Math.min(table.getRowCount(), maxRowsToCheck);

        for (int i = 0; i < rowCount; i++)
        {
            TableCellRenderer tableCellRenderer = table.getCellRenderer(i, columnIndex);
            Component component = tableCellRenderer.getTableCellRendererComponent(table,
                                                                                  table.getValueAt(i, columnIndex),
                                                                                  false,
                                                                                  false,
                                                                                  i,
                                                                                  columnIndex);
            int thisWidth = component.getPreferredSize().width;
            if (thisWidth > width)
            {
                width = thisWidth;
            }
        }

        try
        {
            TableCellRenderer tableCellRenderer = table.getTableHeader().getColumnModel().getColumn(columnIndex).getHeaderRenderer();
            if (tableCellRenderer == null)
            {
                tableCellRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Object title = table.getTableHeader().getColumnModel().getColumn(columnIndex).getHeaderValue();
            Component component = tableCellRenderer.getTableCellRendererComponent(table,
                                                                                  title,
                                                                                  false,
                                                                                  false,
                                                                                  0,
                                                                                  columnIndex);
            int thisWidth = component.getPreferredSize().width;
            if (thisWidth > width)
            {
                width = thisWidth;
            }
        }
        catch (Throwable ex)
        {
            /* eat the exception */
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TableUtils.getColumnWidth ", ex);
        }

        width += 10;

        return width;
    }
}
