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
import org.pentaho.reportdesigner.crm.report.model.ElementBorderDefinition;
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
public class ElementBorderDefinitionTableCellRenderer extends DefaultTableCellRenderer
{

    @NotNull
    private ElementBorderDefinition borderDefinition;


    public ElementBorderDefinitionTableCellRenderer()
    {
        borderDefinition = new ElementBorderDefinition();
        setText(" ");
    }


    public void setText(@Nullable String text)
    {
        super.setText(" ");
    }


    public void setElementBorderDefinition(@NotNull ElementBorderDefinition elementBorderDefinition)
    {
        //noinspection ConstantConditions
        if (elementBorderDefinition == null)
        {
            throw new IllegalArgumentException("elementBorderDefinition must not be null");
        }

        this.borderDefinition = elementBorderDefinition;
    }


    protected void paintComponent(@NotNull Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Stroke origStroke = g2d.getStroke();
        Color origColor = g2d.getColor();

        if (borderDefinition.getTopSide().getType() != ElementBorderDefinition.BorderType.NONE)
        {
            g2d.setColor(borderDefinition.getTopSide().getColor());
            g2d.setStroke(borderDefinition.getTopSide().getStroke());

            g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        }

        g2d.setStroke(origStroke);
        g2d.setColor(origColor);
    }


    @NotNull
    public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        ElementBorderDefinitionTableCellRenderer label = (ElementBorderDefinitionTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
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
                    ElementBorderDefinition borderDefinition = (ElementBorderDefinition) hashSet.iterator().next();
                    label.setElementBorderDefinition(borderDefinition);
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