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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 01.11.2005
 * Time: 10:23:10
 */
public class DoubleValueChooser
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(DoubleValueChooser.class.getName());


    public static class Pair<T>
    {
        @NotNull
        private T value1;
        @NotNull
        private T value2;


        public Pair(@NotNull T value1, @NotNull T value2)
        {
            this.value1 = value1;
            this.value2 = value2;
        }


        @NotNull
        public T getValue1()
        {
            return value1;
        }


        @NotNull
        public T getValue2()
        {
            return value2;
        }
    }


    private DoubleValueChooser()
    {
    }


    @Nullable
    public static Pair<Double> showValueChooser(@NotNull JComponent parent, @NotNull String title, @NotNull String valueLabel1, @NotNull String valueLabel2, @Nullable Pair<Double> initialValue)
    {
        final CenterPanelDialog centerPanelDialog;
        Window windowAncestor = SwingUtilities.getWindowAncestor(parent);

        if (windowAncestor instanceof Dialog)
        {
            centerPanelDialog = new CenterPanelDialog((Dialog) windowAncestor, title, true);
        }
        else
        {
            centerPanelDialog = new CenterPanelDialog((Frame) windowAncestor, title, true);
        }

        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, right:max(60dlu;default), 4dlu, fill:default:grow, 0dlu",
                                               "0dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "0dlu:grow");

        JPanel centerPanel = new JPanel(formLayout);
        CellConstraints cc = new CellConstraints();

        JTextField textField1 = new JTextField(initialValue != null ? String.valueOf(initialValue.getValue1()) : "");
        UndoHelper.installUndoSupport(textField1);
        TextComponentHelper.installDefaultPopupMenu(textField1);

        JTextField textField2 = new JTextField(initialValue != null ? String.valueOf(initialValue.getValue2()) : "");
        UndoHelper.installUndoSupport(textField2);
        TextComponentHelper.installDefaultPopupMenu(textField2);

        centerPanel.add(new JLabel(valueLabel1), cc.xy(2, 2));
        centerPanel.add(textField1, cc.xy(4, 2));
        centerPanel.add(new JLabel(valueLabel2), cc.xy(2, 4));
        centerPanel.add(textField2, cc.xy(4, 4));

        final boolean[] action = new boolean[]{false};

        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                action[0] = true;
                centerPanelDialog.dispose();
            }
        });

        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                action[0] = false;
                centerPanelDialog.dispose();
            }
        });

        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.pack();
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            Double d1;
            Double d2;
            try
            {
                d1 = Double.valueOf(textField1.getText());
                d2 = Double.valueOf(textField2.getText());
            }
            catch (NumberFormatException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DoubleValueChooser.showValueChooser ", e);
                return initialValue;
            }

            Pair<Double> p = new Pair<Double>(d1, d2);
            return p;
        }
        return initialValue;
    }
}
