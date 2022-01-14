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
import org.pentaho.reportdesigner.crm.report.model.GroupingPropertyDescriptor;
import org.pentaho.reportdesigner.crm.report.properties.PropertyEditorPanel;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.beans.PropertyDescriptor;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 09:20:01
 */
public class PropertyDescriptorCellRenderer extends DefaultTableCellRenderer
{
    @SuppressWarnings({"UnusedDeclaration"})
    @NotNull
    private PropertyEditorPanel propertyEditorPanel;


    public PropertyDescriptorCellRenderer(@NotNull PropertyEditorPanel propertyEditorPanel)
    {
        this.propertyEditorPanel = propertyEditorPanel;
    }


    @NotNull
    public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel tableCellRendererComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        try
        {
            if (value instanceof PropertyDescriptor)
            {
                PropertyDescriptor propertyDescriptor = (PropertyDescriptor) value;
                String name = propertyDescriptor.getName() + (propertyDescriptor.getReadMethod().getReturnType().isArray() ? "[]" : "");
                tableCellRendererComponent.setText("   " + TranslationManager.getInstance().getTranslation("R", "Property." + name));

                if (propertyDescriptor instanceof GroupingPropertyDescriptor)
                {
                    GroupingPropertyDescriptor groupingPropertyDescriptor = (GroupingPropertyDescriptor) propertyDescriptor;
                    if (groupingPropertyDescriptor.isImportant())
                    {
                        Font font = StyleContext.getDefaultStyleContext().getFont(tableCellRendererComponent.getFont().getName(), Font.BOLD, tableCellRendererComponent.getFont().getSize());
                        tableCellRendererComponent.setFont(font);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        return tableCellRendererComponent;
    }
}
