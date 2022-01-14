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
package org.pentaho.reportdesigner.lib.client.components.tabbedpane;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.components.Category;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * User: Martin
 * Date: 14.03.2005
 * Time: 17:34:17
 */
public class ListTabbedPaneListCellRenderer extends DefaultListCellRenderer
{
    @NotNull
    private static final Border EMPTY_BORDER = new EmptyBorder(10, 5, 10, 5);


    public ListTabbedPaneListCellRenderer()
    {
    }


    @NotNull
    public Component getListCellRendererComponent(
            @NotNull JList list,
            @NotNull Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
        setComponentOrientation(list.getComponentOrientation());
        if (isSelected)
        {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else
        {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value instanceof Category)
        {
            Category category = (Category) value;
            setText(category.getTitle());
            setIcon(category.getIconBig());
        }

        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);

        setHorizontalAlignment(JLabel.CENTER);

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBorder(EMPTY_BORDER);

        Dimension ms = getPreferredSize();
        if (ms.width < 80)
        {
            ms.width = 80;
            setPreferredSize(ms);
        }
        else
        {
            setPreferredSize(null);
        }


        return this;
    }
}
