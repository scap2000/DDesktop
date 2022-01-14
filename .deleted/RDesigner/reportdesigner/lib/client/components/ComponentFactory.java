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
package org.pentaho.reportdesigner.lib.client.components;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

/**
 * User: Martin
 * Date: 10.08.2006
 * Time: 16:28:10
 */
public class ComponentFactory
{
    private ComponentFactory()
    {
    }


    @NotNull
    public static JLabel createLabel(@NotNull @NonNls String prefix, @NotNull @NonNls String key, @NotNull Component componentToLabel)
    {
        JLabel label = createLabel(prefix, key);
        label.setLabelFor(componentToLabel);

        if (componentToLabel instanceof JButton && label.getDisplayedMnemonic() != -1)
        {
            final JButton button = (JButton) componentToLabel;
            int displayedMnemonic = label.getDisplayedMnemonic();

            String customeMnemonicHandlerKey = "customMnemonicHandler";//NON-NLS
            label.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(displayedMnemonic, InputEvent.ALT_MASK), customeMnemonicHandlerKey);
            label.getActionMap().put(customeMnemonicHandlerKey, new AbstractAction()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    button.doClick();
                }
            });
        }

        return label;
    }


    @NotNull
    public static JLabel createLabel(@NotNull @NonNls String prefix, @NotNull @NonNls String key)
    {
        JLabel label = new JLabel(TranslationManager.getInstance().getTranslation(prefix, key));
        label.setDisplayedMnemonic(TranslationManager.getInstance().getMnemonic(prefix, key));
        label.setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex(prefix, key));
        return label;
    }


    @NotNull
    public static JButton createButton(@NotNull @NonNls String prefix, @NotNull @NonNls String key)
    {
        JButton button = new JButton(TranslationManager.getInstance().getTranslation(prefix, key));
        button.setMnemonic(TranslationManager.getInstance().getMnemonic(prefix, key));
        button.setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex(prefix, key));
        return button;
    }


    @NotNull
    public static JToggleButton createToggleButton(@NotNull @NonNls String prefix, @NotNull @NonNls String key)
    {
        JToggleButton button = new JToggleButton(TranslationManager.getInstance().getTranslation(prefix, key));
        button.setMnemonic(TranslationManager.getInstance().getMnemonic(prefix, key));
        button.setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex(prefix, key));
        return button;
    }


    @NotNull
    public static JCheckBox createCheckBox(@NotNull @NonNls String prefix, @NotNull @NonNls String key)
    {
        JCheckBox checkBox = new JCheckBox(TranslationManager.getInstance().getTranslation(prefix, key));
        checkBox.setMnemonic(TranslationManager.getInstance().getMnemonic(prefix, key));
        checkBox.setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex(prefix, key));
        return checkBox;
    }


    @NotNull
    public static JRadioButton createRadioButton(@NotNull @NonNls String prefix, @NotNull @NonNls String key)
    {
        JRadioButton radioButton = new JRadioButton(TranslationManager.getInstance().getTranslation(prefix, key));
        radioButton.setMnemonic(TranslationManager.getInstance().getMnemonic(prefix, key));
        radioButton.setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex(prefix, key));
        return radioButton;
    }


    @NotNull
    public static JTextField createTextField(boolean supportUndo, boolean installPopupMenu)
    {
        JTextField textField = new JTextField();
        if (supportUndo)
        {
            UndoHelper.installUndoSupport(textField);
        }

        if (installPopupMenu)
        {
            TextComponentHelper.installDefaultPopupMenu(textField);
        }

        return textField;
    }


    @NotNull
    public static JTextArea createTextArea(boolean supportUndo, boolean installPopupMenu)
    {
        JTextArea textArea = new JTextArea();
        if (supportUndo)
        {
            UndoHelper.installUndoSupport(textArea);
        }

        if (installPopupMenu)
        {
            TextComponentHelper.installDefaultPopupMenu(textArea);
        }

        return textArea;
    }
}
