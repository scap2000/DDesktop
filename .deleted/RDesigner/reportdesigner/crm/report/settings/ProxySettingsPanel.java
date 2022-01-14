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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Martin
 * Date: 03.03.2006
 * Time: 14:14:22
 */
public class ProxySettingsPanel extends SettingsPanel
{
    @NotNull
    private JRadioButton radioButtonNoProxy;
    @NotNull
    private JRadioButton radioButtonAutoDetectProxy;
    @NotNull
    private JRadioButton radioButtonUserProxy;

    @NotNull
    private JTextField proxyHostTextField;
    @NotNull
    private JTextField proxyPortTextField;
    @NotNull
    private JLabel proxyHostLabel;
    @NotNull
    private JLabel proxyPortLabel;

    @NotNull
    private JLabel userLabel;
    @NotNull
    private JLabel passwordLabel;
    @NotNull
    private JTextField userTextField;
    @NotNull
    private JPasswordField passwordField;

    @NotNull
    private JCheckBox socksCheckBox;

    @NotNull
    private ButtonGroup buttonGroup;


    public ProxySettingsPanel()
    {
        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, pref, 4dlu, default:grow, 4dlu",
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "10dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu");

        CellConstraints cc = new CellConstraints();

        setLayout(formLayout);

        radioButtonNoProxy = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "ProxySettingsPanel.noProxy"));
        radioButtonAutoDetectProxy = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "ProxySettingsPanel.autoDetectProxy"));
        radioButtonUserProxy = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "ProxySettingsPanel.userProxy"));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonNoProxy);
        buttonGroup.add(radioButtonAutoDetectProxy);
        buttonGroup.add(radioButtonUserProxy);

        proxyHostTextField = new JTextField();
        proxyPortTextField = new JTextField();

        socksCheckBox = new JCheckBox(TranslationManager.getInstance().getTranslation("R", "ProxySettingsPanel.socks"));

        add(radioButtonNoProxy, cc.xyw(2, 2, 3));
        add(radioButtonAutoDetectProxy, cc.xyw(2, 4, 3));
        add(radioButtonUserProxy, cc.xyw(2, 6, 3));

        proxyHostLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "ProxySettingsPanel.proxyHost"));
        add(proxyHostLabel, cc.xy(2, 8));
        add(proxyHostTextField, cc.xy(4, 8));

        proxyPortLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "ProxySettingsPanel.proxyPort"));
        add(proxyPortLabel, cc.xy(2, 10));
        add(proxyPortTextField, cc.xy(4, 10));
        add(socksCheckBox, cc.xy(4, 12));

        userLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "ProxySettingsPanel.user"));
        passwordLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "ProxySettingsPanel.password"));

        userTextField = new JTextField();
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(userTextField.getPreferredSize());

        add(userLabel, cc.xy(2, 14));
        add(userTextField, cc.xy(4, 14));

        add(passwordLabel, cc.xy(2, 16));
        add(passwordField, cc.xy(4, 16));

        reset();

        radioButtonNoProxy.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                proxyHostTextField.setText("");
                proxyPortTextField.setText("");

                socksCheckBox.setSelected(false);

                enableProxyFields(false);
                enableAuthenticationFields(false);
            }
        });

        radioButtonAutoDetectProxy.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                String webstartHTTPProxyHost = ProxySettings.getWebstartHTTPProxyHost();
                String webstartSOCKSProxyHost = ProxySettings.getWebstartSOCKSProxyHost();
                if (webstartHTTPProxyHost != null && webstartHTTPProxyHost.trim().length() > 0)
                {
                    proxyHostTextField.setText(webstartHTTPProxyHost);
                    proxyPortTextField.setText(ProxySettings.getWebstartHTTPProxyPort());
                    socksCheckBox.setSelected(false);
                }
                else if (webstartSOCKSProxyHost != null && webstartSOCKSProxyHost.trim().length() > 0)
                {
                    proxyHostTextField.setText(webstartSOCKSProxyHost);
                    proxyPortTextField.setText(ProxySettings.getWebstartSOCKSProxyPort());
                    socksCheckBox.setSelected(true);
                }
                else
                {
                    proxyHostTextField.setText("");
                    proxyPortTextField.setText("");
                    socksCheckBox.setSelected(false);
                }

                enableProxyFields(false);
                enableAuthenticationFields(true);
            }
        });

        radioButtonUserProxy.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (ApplicationSettings.getInstance().getProxySettings().isUseSocksProxy())
                {
                    proxyHostTextField.setText(ApplicationSettings.getInstance().getProxySettings().getSocksProxyHost());
                    proxyPortTextField.setText(ApplicationSettings.getInstance().getProxySettings().getSocksProxyPort());
                }
                else
                {
                    proxyHostTextField.setText(ApplicationSettings.getInstance().getProxySettings().getHTTPProxyHost());
                    proxyPortTextField.setText(ApplicationSettings.getInstance().getProxySettings().getHTTPProxyPort());
                }

                socksCheckBox.setSelected(ApplicationSettings.getInstance().getProxySettings().isUseSocksProxy());

                enableProxyFields(true);
                enableAuthenticationFields(true);
            }
        });
    }


    private void enableProxyFields(boolean enable)
    {
        proxyHostTextField.setEnabled(enable);
        proxyPortTextField.setEnabled(enable);
        proxyHostLabel.setEnabled(enable);
        proxyPortLabel.setEnabled(enable);
        socksCheckBox.setEnabled(enable);
    }


    private void enableAuthenticationFields(boolean enable)
    {
        userLabel.setEnabled(enable);
        userTextField.setEnabled(enable);
        passwordLabel.setEnabled(enable);
        passwordField.setEnabled(enable);
    }


    @NotNull
    public ValidationResult getValidationResult()
    {
        ValidationResult validationResult = new ValidationResult();

        if (radioButtonUserProxy.isSelected())
        {
            if (!ValidationUtils.isNumber(proxyPortTextField.getText().trim()))
            {
                ValidationMessage validationMessage = new ValidationMessage(ValidationMessage.Severity.ERROR, "ProxySettingsPanel.invalidPortFormat");
                validationResult.addValidationMessage(validationMessage);
            }
        }
        return validationResult;
    }


    public void apply()
    {
        final ProxySettings proxySettings = ApplicationSettings.getInstance().getProxySettings();

        if (radioButtonNoProxy.isSelected())
        {
            proxySettings.setProxyType(ProxyType.NO_PROXY);
        }
        else if (radioButtonAutoDetectProxy.isSelected())
        {
            proxySettings.setProxyType(ProxyType.AUTO_DETECT_PROXY);
        }
        else if (radioButtonUserProxy.isSelected())
        {
            proxySettings.setProxyType(ProxyType.USER_PROXY);
            proxySettings.setUseSocksProxy(socksCheckBox.isSelected());
            if (socksCheckBox.isSelected())
            {
                proxySettings.setSocksProxyHost(proxyHostTextField.getText().trim());
                proxySettings.setSocksProxyPort(proxyPortTextField.getText().trim());
            }
            else
            {
                proxySettings.setHTTPProxyHost(proxyHostTextField.getText().trim());
                proxySettings.setHTTPProxyPort(proxyPortTextField.getText().trim());
            }
        }

        proxySettings.setProxyUser(userTextField.getText());
        proxySettings.setProxyPassword(new String(passwordField.getPassword()));

        proxySettings.applySettings();
    }


    public void reset()
    {
        final ProxySettings proxySettings = ApplicationSettings.getInstance().getProxySettings();

        switch (proxySettings.getProxyType())
        {
            case NO_PROXY:
            {
                buttonGroup.setSelected(radioButtonNoProxy.getModel(), true);

                proxyHostTextField.setText("");
                proxyPortTextField.setText("");

                userTextField.setText("");
                passwordField.setText("");

                enableProxyFields(false);
                enableAuthenticationFields(false);
                break;
            }
            case AUTO_DETECT_PROXY:
            {
                buttonGroup.setSelected(radioButtonAutoDetectProxy.getModel(), true);

                String webstartHTTPProxyHost = ProxySettings.getWebstartHTTPProxyHost();
                String webstartSOCKSProxyHost = ProxySettings.getWebstartSOCKSProxyHost();
                if (webstartHTTPProxyHost != null && webstartHTTPProxyHost.trim().length() > 0)
                {
                    proxyHostTextField.setText(webstartHTTPProxyHost);
                    proxyPortTextField.setText(ProxySettings.getWebstartHTTPProxyPort());
                    socksCheckBox.setSelected(false);
                }
                else if (webstartSOCKSProxyHost != null && webstartSOCKSProxyHost.trim().length() > 0)
                {
                    proxyHostTextField.setText(webstartSOCKSProxyHost);
                    proxyPortTextField.setText(ProxySettings.getWebstartSOCKSProxyPort());
                    socksCheckBox.setSelected(true);
                }
                else
                {
                    proxyHostTextField.setText("");
                    proxyPortTextField.setText("");
                    socksCheckBox.setSelected(false);
                }

                enableProxyFields(false);

                userTextField.setText("");
                passwordField.setText("");

                enableAuthenticationFields(true);
                break;
            }
            case USER_PROXY:
            {
                buttonGroup.setSelected(radioButtonUserProxy.getModel(), true);

                if (proxySettings.isUseSocksProxy())
                {
                    proxyHostTextField.setText(proxySettings.getSocksProxyHost());
                    proxyPortTextField.setText(proxySettings.getSocksProxyPort());
                }
                else
                {
                    proxyHostTextField.setText(proxySettings.getHTTPProxyHost());
                    proxyPortTextField.setText(proxySettings.getHTTPProxyPort());
                }

                socksCheckBox.setSelected(proxySettings.isUseSocksProxy());

                enableProxyFields(true);

                enableAuthenticationFields(true);
                userTextField.setText(proxySettings.getProxyUser());
                passwordField.setText(proxySettings.getProxyPassword());

                break;
            }
        }

        userTextField.setText(proxySettings.getProxyUser());
        passwordField.setText(proxySettings.getProxyPassword());
    }


    public boolean hasChanged()
    {
        return true;
    }
}