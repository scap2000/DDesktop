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
public class ExternalToolSettingsPanel extends SettingsPanel
{
    @NotNull
    private JTabbedPane tabbedPane;

    @NotNull
    private ToolSettingsPanel toolSettingsPanelPDF;
    @NotNull
    private ToolSettingsPanel toolSettingsPanelRTF;
    @NotNull
    private ToolSettingsPanel toolSettingsPanelXLS;
    @NotNull
    private ToolSettingsPanel toolSettingsPanelCSV;
    @NotNull
    private ToolSettingsPanel toolSettingsPanelXML;


    public ExternalToolSettingsPanel()
    {
        tabbedPane = new JTabbedPane();

        toolSettingsPanelPDF = new ToolSettingsPanel();
        toolSettingsPanelRTF = new ToolSettingsPanel();
        toolSettingsPanelXLS = new ToolSettingsPanel();
        toolSettingsPanelCSV = new ToolSettingsPanel();
        toolSettingsPanelXML = new ToolSettingsPanel();

        tabbedPane.addTab(TranslationManager.getInstance().getTranslation("R", "ExternalToolSettingsPanel.Tab.PDF"), toolSettingsPanelPDF);
        tabbedPane.addTab(TranslationManager.getInstance().getTranslation("R", "ExternalToolSettingsPanel.Tab.RTF"), toolSettingsPanelRTF);
        tabbedPane.addTab(TranslationManager.getInstance().getTranslation("R", "ExternalToolSettingsPanel.Tab.XLS"), toolSettingsPanelXLS);
        tabbedPane.addTab(TranslationManager.getInstance().getTranslation("R", "ExternalToolSettingsPanel.Tab.CSV"), toolSettingsPanelCSV);
        tabbedPane.addTab(TranslationManager.getInstance().getTranslation("R", "ExternalToolSettingsPanel.Tab.XML"), toolSettingsPanelXML);

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        reset();
    }


    @NotNull
    public ValidationResult getValidationResult()
    {
        ValidationResult validationResult = new ValidationResult();
        validationResult.addValidationMessages(toolSettingsPanelPDF.getValidationMessages());
        validationResult.addValidationMessages(toolSettingsPanelRTF.getValidationMessages());
        validationResult.addValidationMessages(toolSettingsPanelXLS.getValidationMessages());
        validationResult.addValidationMessages(toolSettingsPanelCSV.getValidationMessages());
        validationResult.addValidationMessages(toolSettingsPanelXML.getValidationMessages());
        return validationResult;
    }


    public void apply()
    {
        ApplicationSettings.getInstance().getExternalToolSettings().setUseDefaultPDFViewer(toolSettingsPanelPDF.getUseDefaultRadioButton().isSelected());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomPDFViewerExecutable(toolSettingsPanelPDF.getCustomExecutableTextField().getText());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomPDFViewerParameters(toolSettingsPanelPDF.getCustomParametersTextField().getText());

        ApplicationSettings.getInstance().getExternalToolSettings().setUseDefaultRTFViewer(toolSettingsPanelRTF.getUseDefaultRadioButton().isSelected());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomRTFViewerExecutable(toolSettingsPanelRTF.getCustomExecutableTextField().getText());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomRTFViewerParameters(toolSettingsPanelRTF.getCustomParametersTextField().getText());

        ApplicationSettings.getInstance().getExternalToolSettings().setUseDefaultXLSViewer(toolSettingsPanelXLS.getUseDefaultRadioButton().isSelected());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomXLSViewerExecutable(toolSettingsPanelXLS.getCustomExecutableTextField().getText());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomXLSViewerParameters(toolSettingsPanelXLS.getCustomParametersTextField().getText());

        ApplicationSettings.getInstance().getExternalToolSettings().setUseDefaultCSVViewer(toolSettingsPanelCSV.getUseDefaultRadioButton().isSelected());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomCSVViewerExecutable(toolSettingsPanelCSV.getCustomExecutableTextField().getText());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomCSVViewerParameters(toolSettingsPanelCSV.getCustomParametersTextField().getText());

        ApplicationSettings.getInstance().getExternalToolSettings().setUseDefaultXMLViewer(toolSettingsPanelXML.getUseDefaultRadioButton().isSelected());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomXMLViewerExecutable(toolSettingsPanelXML.getCustomExecutableTextField().getText());
        ApplicationSettings.getInstance().getExternalToolSettings().setCustomXMLViewerParameters(toolSettingsPanelXML.getCustomParametersTextField().getText());
    }


    public void reset()
    {
        toolSettingsPanelPDF.getUseDefaultRadioButton().setSelected(ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultPDFViewer());
        toolSettingsPanelPDF.getUseCustomExecutableRadioButton().setSelected(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultPDFViewer());
        toolSettingsPanelPDF.getCustomExecutableTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomPDFViewerExecutable());
        toolSettingsPanelPDF.getCustomParametersTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomPDFViewerParameters());
        toolSettingsPanelPDF.changeTextFieldState(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultPDFViewer());

        toolSettingsPanelRTF.getUseDefaultRadioButton().setSelected(ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultRTFViewer());
        toolSettingsPanelRTF.getUseCustomExecutableRadioButton().setSelected(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultRTFViewer());
        toolSettingsPanelRTF.getCustomExecutableTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomRTFViewerExecutable());
        toolSettingsPanelRTF.getCustomParametersTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomRTFViewerParameters());
        toolSettingsPanelRTF.changeTextFieldState(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultRTFViewer());

        toolSettingsPanelXLS.getUseDefaultRadioButton().setSelected(ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultXLSViewer());
        toolSettingsPanelXLS.getUseCustomExecutableRadioButton().setSelected(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultXLSViewer());
        toolSettingsPanelXLS.getCustomExecutableTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomXLSViewerExecutable());
        toolSettingsPanelXLS.getCustomParametersTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomXLSViewerParameters());
        toolSettingsPanelXLS.changeTextFieldState(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultXLSViewer());

        toolSettingsPanelCSV.getUseDefaultRadioButton().setSelected(ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultCSVViewer());
        toolSettingsPanelCSV.getUseCustomExecutableRadioButton().setSelected(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultCSVViewer());
        toolSettingsPanelCSV.getCustomExecutableTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomCSVViewerExecutable());
        toolSettingsPanelCSV.getCustomParametersTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomCSVViewerParameters());
        toolSettingsPanelCSV.changeTextFieldState(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultCSVViewer());

        toolSettingsPanelXML.getUseDefaultRadioButton().setSelected(ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultXMLViewer());
        toolSettingsPanelXML.getUseCustomExecutableRadioButton().setSelected(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultXMLViewer());
        toolSettingsPanelXML.getCustomExecutableTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomXMLViewerExecutable());
        toolSettingsPanelXML.getCustomParametersTextField().setText(ApplicationSettings.getInstance().getExternalToolSettings().getCustomXMLViewerParameters());
        toolSettingsPanelXML.changeTextFieldState(!ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultXMLViewer());

    }


    public boolean hasChanged()
    {
        return true;
    }


    private static class ToolSettingsPanel extends JPanel
    {
        @NotNull
        private JRadioButton useDefaultRadioButton;
        @NotNull
        private JRadioButton useCustomExecutableRadioButton;
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


        private ToolSettingsPanel()
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

            useDefaultRadioButton = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "ToolSettingsPanel.defaultBrowserRadioButton"));
            useCustomExecutableRadioButton = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "ToolSettingsPanel.customExecutableRadioButton"));

            showFileChooserButton = new JButton(TranslationManager.getInstance().getTranslation("R", "ToolSettingsPanel.showFileChooserButton"));
            showFileChooserButton.setMargin(new Insets(0, 0, 0, 0));

            customExecutableTextField = new JTextField();
            customParametersTextField = new JTextField("{0}");
            sampleCustomExecutableLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "ToolSettingsPanel.sampleCustomExecutableTextArea"));

            customExecutableLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "ToolSettingsPanel.customExecutableTextField"));
            customParametersLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "ToolSettingsPanel.customParametersTextField"));

            add(useDefaultRadioButton, cc.xyw(2, 2, 3));
            add(useCustomExecutableRadioButton, cc.xyw(2, 4, 3));
            add(customExecutableLabel, cc.xy(2, 6));
            add(customExecutableTextField, cc.xy(4, 6));
            add(customParametersLabel, cc.xy(2, 8));
            add(customParametersTextField, cc.xy(4, 8));
            add(showFileChooserButton, cc.xy(6, 6, "fill, fill"));
            add(sampleCustomExecutableLabel, cc.xy(4, 10));

            buttonGroup = new ButtonGroup();
            buttonGroup.add(useDefaultRadioButton);
            buttonGroup.add(useCustomExecutableRadioButton);

            showFileChooserButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    JFileChooser fileChooser = new JFileChooser();
                    int value = fileChooser.showOpenDialog(ToolSettingsPanel.this);
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

            useCustomExecutableRadioButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    changeTextFieldState(useCustomExecutableRadioButton.isSelected());
                }
            });

            useDefaultRadioButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    changeTextFieldState(useCustomExecutableRadioButton.isSelected());
                }
            });
        }


        public void changeTextFieldState(boolean enable)
        {
            customExecutableLabel.setEnabled(enable);
            customParametersLabel.setEnabled(enable);
            customExecutableTextField.setEnabled(enable);
            customParametersTextField.setEnabled(enable);
            sampleCustomExecutableLabel.setEnabled(enable);
            showFileChooserButton.setEnabled(enable);
        }


        @NotNull
        public JRadioButton getUseCustomExecutableRadioButton()
        {
            return useCustomExecutableRadioButton;
        }


        @NotNull
        public JRadioButton getUseDefaultRadioButton()
        {
            return useDefaultRadioButton;
        }


        @NotNull
        public JTextField getCustomExecutableTextField()
        {
            return customExecutableTextField;
        }


        @NotNull
        public JTextField getCustomParametersTextField()
        {
            return customParametersTextField;
        }


        @NotNull
        public ValidationMessage[] getValidationMessages()
        {
            if (!getCustomParametersTextField().getText().contains("{0}"))
            {
                return new ValidationMessage[]{new ValidationMessage(ValidationMessage.Severity.ERROR, "ExternalToolSettingsPanel.MissingVariable")};
            }
            return ValidationMessage.EMPTY_ARRAY;
        }
    }
}
