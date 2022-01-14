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

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.Category;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.tabbedpane.ButtonTabbedPane;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Martin
 * Date: 01.03.2006
 * Time: 18:14:55
 */
public class SettingsDialog extends CenterPanelDialog
{
    @NotNull
    private List<Category<JPanel>> categories;
    @NotNull
    private List<SettingsPanel> settingsPanels;

    @NotNull
    private ButtonTabbedPane listTabbedPane;

    @NotNull
    private JButton applyButton;
    @NotNull
    private JButton resetButton;
    @NotNull
    private JButton cancelButton;


    public SettingsDialog(@NotNull Frame owner)
    {
        super(owner, TranslationManager.getInstance().getTranslation("R", "SettingsDialog.Title"), true);

        init();
    }


    public SettingsDialog(@NotNull Dialog owner)
    {
        super(owner, TranslationManager.getInstance().getTranslation("R", "SettingsDialog.Title"), true);

        init();
    }


    private void init()
    {
        categories = new ArrayList<Category<JPanel>>();
        settingsPanels = new ArrayList<SettingsPanel>();

        LanguageSettingsPanel languageSettingsPanel = new LanguageSettingsPanel();
        Category<JPanel> languageCategory = new Category<JPanel>("languageSettings", IconLoader.getInstance().getLanguageIcon32(), IconLoader.getInstance().getLanguageIcon32(), TranslationManager.getInstance().getTranslation("R", "SettingsDialog.Language"), languageSettingsPanel);
        categories.add(languageCategory);
        settingsPanels.add(languageSettingsPanel);

        ProxySettingsPanel proxySettingsPanel = new ProxySettingsPanel();
        Category<JPanel> proxyCategory = new Category<JPanel>("proxySettings", IconLoader.getInstance().getNetworkIcon32(), IconLoader.getInstance().getNetworkIcon32(), TranslationManager.getInstance().getTranslation("R", "SettingsDialog.Proxy"), proxySettingsPanel);
        categories.add(proxyCategory);
        settingsPanels.add(proxySettingsPanel);

        BrowserSettingsPanel browserSettingsPanel = new BrowserSettingsPanel();
        Category<JPanel> browserCategory = new Category<JPanel>("browserSettings", IconLoader.getInstance().getBrowserIcon32(), IconLoader.getInstance().getBrowserIcon32(), TranslationManager.getInstance().getTranslation("R", "SettingsDialog.Browser"), browserSettingsPanel);
        categories.add(browserCategory);
        settingsPanels.add(browserSettingsPanel);

        ExternalToolSettingsPanel externalToolSettingsPanel = new ExternalToolSettingsPanel();
        Category<JPanel> externalToolCategory = new Category<JPanel>("externalTool", IconLoader.getInstance().getExternalToolsIcon32(), IconLoader.getInstance().getExternalToolsIcon32(), TranslationManager.getInstance().getTranslation("R", "SettingsDialog.ExternalTool"), externalToolSettingsPanel);
        categories.add(externalToolCategory);
        settingsPanels.add(externalToolSettingsPanel);

        listTabbedPane = new ButtonTabbedPane(true);
        for (Category<JPanel> category1 : categories)
        {
            listTabbedPane.addCard(category1);
        }

        listTabbedPane.setSelectedIndex(0);

        applyButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.apply"));
        resetButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.reset"));
        cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));

        applyButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                for (SettingsPanel settingsPanel : settingsPanels)
                {
                    ValidationResult validationResult = settingsPanel.getValidationResult();
                    ArrayList<ValidationMessage> validationMessages = validationResult.getValidationMessages(new ValidationMessage.Severity[]{ValidationMessage.Severity.WARN, ValidationMessage.Severity.ERROR});

                    if (!validationMessages.isEmpty())
                    {
                        StringBuilder messages = new StringBuilder(100);
                        for (ValidationMessage validationMessage : validationMessages)
                        {
                            if (validationMessage.getText() != null)
                            {
                                messages.append(validationMessage.getText());
                            }
                            else
                            {
                                messages.append(TranslationManager.getInstance().getTranslation("R", validationMessage.getKey()));
                            }
                            messages.append('\n');
                        }
                        JOptionPane.showMessageDialog(SettingsDialog.this, messages, TranslationManager.getInstance().getTranslation("R", "Error.Title"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                for (SettingsPanel settingsPanel : settingsPanels)
                {
                    settingsPanel.apply();
                }

                dispose();
            }
        });

        resetButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                for (SettingsPanel settingsPanel : settingsPanels)
                {
                    settingsPanel.reset();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                for (SettingsPanel settingsPanel : settingsPanels)
                {
                    settingsPanel.reset();
                }

                dispose();
            }
        });


        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(listTabbedPane, BorderLayout.CENTER);

        setCenterPanel(centerPanel);

        setButtons(applyButton, cancelButton, resetButton, cancelButton, null, applyButton);

        pack();
        GUIUtils.ensureMinimumDialogSize(this, 500, 400);
        WindowUtils.setLocationRelativeTo(this, getParent());
    }


}
