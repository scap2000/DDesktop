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
package org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 13:09:04
 */
public class ColumnInfoListCellRenderer extends DefaultListCellRenderer
{
    @NotNull
    public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof ColumnInfo)
        {
            ColumnInfo columnInfo = (ColumnInfo) value;
            label.setText(/*columnInfo.getTableName() + "." + */columnInfo.getColumnName()/*+" "+columnInfo.getColumnClass()*/);
        }

        return label;
    }
}
