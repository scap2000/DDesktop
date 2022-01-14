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
import org.pentaho.reportdesigner.crm.report.model.RowBandingDefinition;
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
public class RowBandingDefinitionTableCellRenderer extends DefaultTableCellRenderer
{

    @NotNull
    private RowBandingDefinition rowBandingDefinition;


    public RowBandingDefinitionTableCellRenderer()
    {
        rowBandingDefinition = new RowBandingDefinition();
        setText(" ");
    }


    public void setText(@Nullable String text)
    {
        super.setText(" ");
    }


    public void setRowBandingDefinition(@NotNull RowBandingDefinition rowBandingDefinition)
    {
        //noinspection ConstantConditions
        if (rowBandingDefinition == null)
        {
            throw new IllegalArgumentException("rowBandingDefinition must not be null");
        }

        this.rowBandingDefinition = rowBandingDefinition;
    }


    protected void paintComponent(@NotNull Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Stroke origStroke = g2d.getStroke();
        Color origColor = g2d.getColor();

        if (rowBandingDefinition.isEnabled())
        {
            g2d.setColor(rowBandingDefinition.getColor());
            g2d.fillRect(1, 3, getWidth() - 2, getHeight() - 6);
        }

        g2d.setStroke(origStroke);
        g2d.setColor(origColor);
    }


    @NotNull
    public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        RowBandingDefinitionTableCellRenderer label = (RowBandingDefinitionTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
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
                    RowBandingDefinition rowBandingDefinition = (RowBandingDefinition) hashSet.iterator().next();
                    label.setRowBandingDefinition(rowBandingDefinition);
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
