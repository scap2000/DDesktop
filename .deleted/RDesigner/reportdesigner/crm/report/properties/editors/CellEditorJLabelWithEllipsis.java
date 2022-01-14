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
package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 24.10.2005
 * Time: 09:23:33
 */
public abstract class CellEditorJLabelWithEllipsis<T> extends JPanel
{

    @NotNull
    protected JLabel label;
    @NotNull
    protected JButton ellipsisButton;
    @Nullable
    protected T uncommittedValue;
    @Nullable
    protected T origValue;


    public CellEditorJLabelWithEllipsis()
    {
        setLayout(new BorderLayout());

        setOpaque(false);

        label = new JLabel();
        label.setOpaque(false);
        ellipsisButton = new JButton("...");
        ellipsisButton.setDefaultCapable(false);
        ellipsisButton.setMargin(new Insets(0, 0, 0, 0));

        add(label, BorderLayout.CENTER);
        add(ellipsisButton, BorderLayout.EAST);
    }


    @NotNull
    public JLabel getLabel()
    {
        return label;
    }


    @NotNull
    public JButton getEllipsisButton()
    {
        return ellipsisButton;
    }


    public void setValue(@Nullable T value, boolean commit)
    {
        label.setText(convertToText(value));
        uncommittedValue = value;

        if (commit)
        {
            origValue = value;
        }
    }


    @Nullable
    public T getValue()
    {
        if (uncommittedValue != null)
        {
            return uncommittedValue;
        }

        return origValue;
    }


    @Nullable
    public abstract String convertToText(@Nullable T obj);
}
