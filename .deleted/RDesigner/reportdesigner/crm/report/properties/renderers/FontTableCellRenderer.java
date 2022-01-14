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
import java.util.HashSet;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 09:15:46
 */
public class FontTableCellRenderer extends DefaultTableCellRenderer
{

    @NotNull
    public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        try
        {
            if (value instanceof HashSet)
            {
                HashSet hashSet = (HashSet) value;
                if (hashSet.size() > 1)
                {
                    label.setText(TranslationManager.getInstance().getTranslation("R", "Property.MultipleValues"));
                }

                if (hashSet.size() == 1)
                {
                    Font font = (Font) hashSet.iterator().next();
                    StringBuilder sb = new StringBuilder(font.getName());
                    sb.append(" ");
                    sb.append(font.getSize());
                    if ((font.getStyle() & Font.BOLD) == Font.BOLD)
                    {
                        sb.append(" ");
                        sb.append(TranslationManager.getInstance().getTranslation("R", "FontTableCellRenderer.Suffix.Bold"));
                    }
                    if ((font.getStyle() & Font.ITALIC) == Font.ITALIC)
                    {
                        sb.append(" ");
                        sb.append(TranslationManager.getInstance().getTranslation("R", "FontTableCellRenderer.Suffix.Italic"));
                    }

                    Font f = font;
                    if (f.getSize() > table.getFont().getSize())
                    {
                        f = StyleContext.getDefaultStyleContext().getFont(font.getName(), font.getStyle(), table.getFont().getSize());
                    }

                    label.setFont(f);
                    label.setText(sb.toString());
                }
            }
        }
        catch (Throwable e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        return label;
    }
}
