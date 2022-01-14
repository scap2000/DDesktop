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
package org.pentaho.reportdesigner.crm.report.properties.renderers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.StyleContext;
import java.awt.*;

/**
 * User: Martin
 * Date: 30.01.2006
 * Time: 10:57:31
 */
public class GroupCellRenderer extends DefaultTableCellRenderer
{

    @NotNull
    public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, TranslationManager.getInstance().getTranslation("R", "PropertyGroup." + value), isSelected, hasFocus, row, column);
        try
        {
            if (!isSelected)
            {
                label.setBackground(Color.GRAY);
                label.setForeground(Color.WHITE);
            }
            if (column == 1)
            {
                label.setText("");
            }
            else
            {
                Font font = StyleContext.getDefaultStyleContext().getFont(label.getFont().getName(), Font.BOLD, label.getFont().getSize());
                label.setFont(font);
            }
        }
        catch (Throwable e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        return label;
    }
}
