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

import javax.swing.*;
import java.util.List;
import java.util.Locale;

/**
 * User: Martin
 * Date: 01.03.2006
 * Time: 18:15:58
 */
public class LanguageSettingsPanel extends SettingsPanel
{
    @NotNull
    private JComboBox comboBoxLanguage;


    public LanguageSettingsPanel()
    {
        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, pref, 4dlu, pref:grow, 4dlu",
                                               "4dlu, " +
                                               "pref, " +
                                               "10dlu, " +
                                               "pref, " +
                                               "4dlu, ");

        CellConstraints cc = new CellConstraints();

        setLayout(formLayout);

        List<Locale> list = ApplicationSettings.getInstance().getAvailableLocales();

        String[] languages = new String[list.size()];
        int i = 0;
        for (Locale availableLocale : list)
        {
            languages[i] = availableLocale.getDisplayName(availableLocale);
            i++;
        }
        comboBoxLanguage = new JComboBox(languages);

        add(new JLabel(TranslationManager.getInstance().getTranslation("R", "LanguageSettingsPanel.Language")), cc.xy(2, 2));
        add(comboBoxLanguage, cc.xy(4, 2));

        reset();
    }


    @NotNull
    public ValidationResult getValidationResult()
    {
        return new ValidationResult();
    }


    public void apply()
    {
        if (hasChanged())
        {
            JOptionPane.showMessageDialog(this,
                                          TranslationManager.getInstance().getTranslation("R", "LanguageSettingsPanel.ChangedMessage.Text"),
                                          TranslationManager.getInstance().getTranslation("R", "LanguageSettingsPanel.ChangedMessage.Title"),
                                          JOptionPane.INFORMATION_MESSAGE);
        }

        ApplicationSettings.getInstance().setLanguage(ApplicationSettings.getInstance().getAvailableLocales().get(comboBoxLanguage.getSelectedIndex()));
    }


    public void reset()
    {
        boolean languageFound = false;
        for (int i = 0; i < ApplicationSettings.getInstance().getAvailableLocales().size(); i++)
        {
            Locale availableLocale = ApplicationSettings.getInstance().getAvailableLocales().get(i);
            if (ApplicationSettings.getInstance().getLanguage().equals(availableLocale))
            {
                comboBoxLanguage.setSelectedIndex(i);
                languageFound = true;
                break;
            }
        }
        if (!languageFound)
        {
            comboBoxLanguage.setSelectedIndex(0);
        }
    }


    public boolean hasChanged()
    {
        Locale selectedLang = ApplicationSettings.getInstance().getAvailableLocales().get(comboBoxLanguage.getSelectedIndex());
        Locale oldLang = ApplicationSettings.getInstance().getLanguage();
        return !oldLang.equals(selectedLang);

    }
}

