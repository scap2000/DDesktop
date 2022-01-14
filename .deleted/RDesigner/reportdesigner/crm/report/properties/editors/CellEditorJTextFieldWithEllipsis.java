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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 24.10.2005
 * Time: 09:23:33
 */
public abstract class CellEditorJTextFieldWithEllipsis<T> extends JPanel
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(CellEditorJTextFieldWithEllipsisPoint2D.class.getName());

    @NotNull
    protected FocusTextField textField;
    @NotNull
    protected JButton ellipsisButton;
    @Nullable
    protected T origValue;

    private boolean nullable;


    public CellEditorJTextFieldWithEllipsis()
    {
        setLayout(new BorderLayout());

        ellipsisButton = new JButton("...");
        ellipsisButton.setDefaultCapable(false);
        ellipsisButton.setMargin(new Insets(0, 0, 0, 0));

        textField = new FocusTextField(ellipsisButton);
        UndoHelper.installUndoSupport(textField);
        TextComponentHelper.installDefaultPopupMenu(textField);
        textField.setBorder(BorderFactory.createEmptyBorder());

        add(textField, BorderLayout.CENTER);
        add(ellipsisButton, BorderLayout.EAST);

        nullable = false;
    }


    public boolean isNullable()
    {
        return nullable;
    }


    public void setNullable(boolean nullable)
    {
        this.nullable = nullable;
    }


    public void requestFocus()
    {
        textField.requestFocus();
    }


    protected boolean processKeyBinding(@NotNull KeyStroke ks, @NotNull KeyEvent e, int condition, boolean pressed)
    {
        return textField.processKeyBinding(ks, e, condition, pressed);
    }


    @NotNull
    public JTextField getTextField()
    {
        return textField;
    }


    @NotNull
    public JButton getEllipsisButton()
    {
        return ellipsisButton;
    }


    public void setValue(@Nullable T value, boolean commit)
    {
        textField.setText(convertToText(value));

        if (commit)
        {
            origValue = value;
        }
    }


    @Nullable
    public T getValue()
    {
        try
        {
            if (nullable && textField.getText().length() == 0)
            {
                return null;
            }
            return convertFromText(textField.getText());
        }
        catch (IllegalArgumentException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CellEditorJTextFieldWithEllipsis.getValue ", e);
        }

        return origValue;
    }


    @Nullable
    public abstract T convertFromText(@NotNull String text);


    @NotNull
    public abstract String convertToText(@Nullable T obj);


    private static class FocusTextField extends JTextField
    {
        @NotNull
        private static final String POPUP_EDITOR = "popupEditor";


        private FocusTextField(@NotNull final JButton button)
        {
            getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "CellEditorJTextFieldWithEllipsis.PopupEditor.Accelerator")), POPUP_EDITOR);

            getActionMap().put(POPUP_EDITOR, new AbstractAction()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    button.doClick();
                }
            });
        }


        @SuppressWarnings({"EmptyMethod"})
        public boolean processKeyBinding(@NotNull KeyStroke ks, @NotNull KeyEvent e, int condition, boolean pressed)
        {
            return super.processKeyBinding(ks, e, condition, pressed);
        }
    }
}
