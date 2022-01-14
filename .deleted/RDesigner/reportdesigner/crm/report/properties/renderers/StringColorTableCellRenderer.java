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
import org.pentaho.reportdesigner.crm.report.util.ColorHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 09:15:46
 */
public class StringColorTableCellRenderer extends DefaultTableCellRenderer
{
    @NotNull
    private ColorIcon icon = new ColorIcon();


    @NotNull
    public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setEnabled(table.isEnabled());
        try
        {
            if (value instanceof HashSet)
            {
                HashSet hashSet = (HashSet) value;
                if (hashSet.size() > 1)
                {
                    label.setText(TranslationManager.getInstance().getTranslation("R", "Property.MultipleValues"));

                    String color = (String) hashSet.iterator().next();
                    Color c = ColorHelper.lookupColor(color);
                    icon.setColor(c);
                    label.setIcon(icon);
                }

                if (hashSet.size() == 1)
                {
                    setHorizontalTextPosition(JLabel.TRAILING);

                    String color = (String) hashSet.iterator().next();
                    Color c = ColorHelper.lookupColor(color);
                    if (c == null)
                    {
                        label.setText(TranslationManager.getInstance().getTranslation("R", "Property.None"));
                        label.setEnabled(false);
                        icon.setColor(c);
                        label.setIcon(icon);
                    }
                    else
                    {
                        label.setText(color);
                        icon.setColor(c);
                        label.setIcon(icon);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        return label;
    }


    private static class ColorIcon implements Icon
    {
        @Nullable
        private Color color;

        private int width;
        private int height;


        private ColorIcon()
        {
            width = 12;
            height = 12;
        }


        public void paintIcon(@NotNull Component c, @NotNull Graphics g, int x, int y)
        {
            g.translate(x, y);
            Color origColor = g.getColor();

            if (color == null)
            {
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(0, 0, width, height);
                g.drawLine(0, 0, width, height);
                g.drawLine(width, 0, 0, height);
            }
            else
            {
                g.setColor(Color.GRAY);
                g.drawRect(0, 0, width, height);
                g.setColor(color);
                g.fillRect(1, 1, width - 1, height - 1);
            }

            g.setColor(origColor);
            g.translate(-x, -y);
        }


        public int getIconWidth()
        {
            return width;
        }


        public int getIconHeight()
        {
            return height;
        }


        public void setColor(@Nullable Color color)
        {
            this.color = color;
        }
    }
}