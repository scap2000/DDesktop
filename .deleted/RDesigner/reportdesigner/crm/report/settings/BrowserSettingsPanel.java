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
package org.pentaho.reportdesigner.crm.report.settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * User: Martin
 * Date: 02.03.2006
 * Time: 07:37:12
 */
public class BrowserSettingsPanel extends SettingsPanel
{
    @NotNull
    private JRadioButton defaultBrowserRadioButton;
    @NotNull
    private JRadioButton customExecutableRadioButton;
    @NotNull
    private JButton showFileChooserButton;
    @NotNull
    private JTextField customExecutableTextField;
    @NotNull
    private JTextField customParametersTextField;
    @NotNull
    private JLabel sampleCustomExecutableLabel;

    @NotNull
    private ButtonGroup buttonGroup;
    @NotNull
    private JLabel customExecutableLabel;
    @NotNull
    private JLabel customParametersLabel;


    public BrowserSettingsPanel()
    {
        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, pref, 4dlu, 10dlu:grow, 0dlu, default, 4dlu",
                                               "4dlu, pref, " +
                                               "4dlu, pref, " +
                                               "4dlu, pref, " +
                                               "4dlu, pref, " +
                                               "4dlu, pref, " +
                                               "4dlu, ");

        @NonNls
        CellConstraints cc = new CellConstraints();

        setLayout(formLayout);

        defaultBrowserRadioButton = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "BrowserSettingsPanel.defaultBrowserRadioButton"));
        customExecutableRadioButton = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "BrowserSettingsPanel.customExecutableRadioButton"));

        showFileChooserButton = new JButton(TranslationManager.getInstance().getTranslation("R", "BrowserSettingsPanel.showFileChooserButton"));
        showFileChooserButton.setMargin(new Insets(0, 0, 0, 0));

        customExecutableTextField = new JTextField();
        customParametersTextField = new JTextField("{0}");
        sampleCustomExecutableLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "BrowserSettingsPanel.sampleCustomExecutableTextArea"));

        customExecutableLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "BrowserSettingsPanel.customExecutableTextField"));
        customParametersLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "BrowserSettingsPanel.customParametersTextField"));

        add(defaultBrowserRadioButton, cc.xyw(2, 2, 3));
        add(customExecutableRadioButton, cc.xyw(2, 4, 3));
        add(customExecutableLabel, cc.xy(2, 6));
        add(customExecutableTextField, cc.xy(4, 6));
        add(customParametersLabel, cc.xy(2, 8));
        add(customParametersTextField, cc.xy(4, 8));
        add(showFileChooserButton, cc.xy(6, 6, "fill, fill"));
        add(sampleCustomExecutableLabel, cc.xy(4, 10));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(defaultBrowserRadioButton);
        buttonGroup.add(customExecutableRadioButton);

        showFileChooserButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser();
                int value = fileChooser.showOpenDialog(BrowserSettingsPanel.this);
                if (value == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = fileChooser.getSelectedFile();
                    try
                    {
                        customExecutableTextField.setText(selectedFile.getCanonicalPath());
                    }
                    catch (IOException e1)
                    {
                        UncaughtExcpetionsModel.getInstance().addException(e1);
                    }
                }
            }
        });

        customExecutableRadioButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                changeTextFieldState(customExecutableRadioButton.isSelected());
            }
        });

        defaultBrowserRadioButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                changeTextFieldState(customExecutableRadioButton.isSelected());
            }
        });

        reset();
    }


    @NotNull
    public ValidationResult getValidationResult()
    {
        return new ValidationResult();
    }


    public void apply()
    {
        ApplicationSettings.getInstance().setUseDefaultBrowser(defaultBrowserRadioButton.isSelected());
        ApplicationSettings.getInstance().setCustomBrowserExecutable(customExecutableTextField.getText());
        ApplicationSettings.getInstance().setCustomBrowserParameters(customParametersTextField.getText());
    }


    public void reset()
    {
        buttonGroup.setSelected(defaultBrowserRadioButton.getModel(), ApplicationSettings.getInstance().isUseDefaultBrowser());
        buttonGroup.setSelected(customExecutableRadioButton.getModel(), !ApplicationSettings.getInstance().isUseDefaultBrowser());
        customExecutableTextField.setText(ApplicationSettings.getInstance().getCustomBrowserExecutable());
        customParametersTextField.setText(ApplicationSettings.getInstance().getCustomBrowserParameters());

        changeTextFieldState(customExecutableRadioButton.isSelected());
    }


    private void changeTextFieldState(boolean enable)
    {
        customExecutableLabel.setEnabled(enable);
        customParametersLabel.setEnabled(enable);
        customExecutableTextField.setEnabled(enable);
        customParametersTextField.setEnabled(enable);
        sampleCustomExecutableLabel.setEnabled(enable);
        showFileChooserButton.setEnabled(enable);
    }


    public boolean hasChanged()
    {
        return true;
    }
}