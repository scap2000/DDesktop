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
package org.pentaho.reportdesigner.crm.report.commands;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionGadget;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport.JFreeReportPublishOnServerExporter;
import org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport.PublishException;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.components.docking.ToolWindow;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin Date: 25.01.2006 Time: 11:26:24
 */
public class PublishToServerCommand extends AbstractCommand
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(PublishToServerCommand.class.getName());


    public PublishToServerCommand()
    {
        super("PublishToServerCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "PublishToServerCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "PublishToServerCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "PublishToServerCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "PublishToServerCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getPublishToServerIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PublishToServerCommand.Accelerator")));
    }


    public void update(@NotNull
    CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        boolean enable = reportDialog.getReport() != null && !(reportDialog.getReport() instanceof SubReport);
        event.getPresentation().setEnabled(enable);
    }


    public void execute(@NotNull
    CommandEvent event)
    {
        final ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        InspectionGadget inspectionGadget = reportDialog.getInspectionGadget();
        final Report report = reportDialog.getReport();
        if (inspectionGadget != null && report != null)
        {
            Set<InspectionResult> inspectionResultsAfterRun = inspectionGadget.getInspectionResultsAfterRun(report);
            for (InspectionResult inspectionResult : inspectionResultsAfterRun)
            {
                if (inspectionResult.getSeverity() == InspectionResult.Severity.ERROR)
                {
                    ToolWindow toolWindow = reportDialog.getInspectionsToolWindow();
                    if (toolWindow != null)
                    {
                        toolWindow.setSizeState(ToolWindow.SizeState.NORMAL);
                    }
                    JOptionPane.showMessageDialog(event.getPresentation().getCommandApplicationRoot().getRootJComponent(), TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.ReportDefinitionContainsErrors"), TranslationManager
                            .getInstance().getTranslation("R", "Error.Title"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        final CenterPanelDialog publishToServerInformationDialog = CenterPanelDialog.createDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "PublishToServerDialog.Title"), true);
        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));
        publishToServerInformationDialog.setButtons(okButton, cancelButton, okButton, cancelButton);
        final PublishToServerCommand.PublishToServerInformationPanel centerPanel = new PublishToServerCommand.PublishToServerInformationPanel(reportDialog);
        publishToServerInformationDialog.setCenterPanel(centerPanel);

        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull
            ActionEvent e)
            {
                // validate values
                String reportNameString = centerPanel.getReportNameTextField().getText();
                String publishLocation = centerPanel.getPublishLocationTextField().getText();
                String type = centerPanel.getTypeComboBox().getSelectedItem().toString();
                String webPublishURL = centerPanel.getWebPublishURLTextField().getText();
                char[] publishPassword = centerPanel.getPublishPasswordField().getPassword();
                String serverUserId = centerPanel.getServerUserIdTextField().getText();
                char[] serverPassword = centerPanel.getServerPasswordField().getPassword();
                boolean publishXActionFile = centerPanel.getPublishXActionFileCheckBox().isSelected();
                boolean useJNDIName = centerPanel.getUseJNDINameCheckBox().isSelected();

                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_REPORT_NAME, reportNameString);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_EXPORT_TYPE, type);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_LOCATION, publishLocation);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_WEB_LOCATION, webPublishURL);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_USER_ID, serverUserId);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_XACTION, Boolean.valueOf(publishXActionFile).toString());
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_USE_JNDI, Boolean.valueOf(useJNDIName).toString());

                try
                {
                    String message = export(publishToServerInformationDialog, report, reportNameString, publishLocation, type, webPublishURL, publishXActionFile, publishPassword, serverUserId, serverPassword, useJNDIName);
                    String successMessage = TranslationManager.getInstance().getTranslation("R", "JFreeReportPublishOnServerExporter.Successful");
                    if (!successMessage.equals(message))
                    {
                        JOptionPane.showMessageDialog(publishToServerInformationDialog, message.trim(), TranslationManager.getInstance().getTranslation("R", "PublishToServerCommand.Information.Title"), JOptionPane.INFORMATION_MESSAGE);
                        return;// do not dispose dialog, because the message might say "password wrong"
                    }
                    JOptionPane.showMessageDialog(publishToServerInformationDialog, TranslationManager.getInstance().getTranslation("R", "PublishToServerCommand.Success.Message"), TranslationManager.getInstance().getTranslation("R", "PublishToServerCommand.Success.Title"), JOptionPane.INFORMATION_MESSAGE);
                    reportDialog.getWorkspaceSettings().storeDialogBounds(publishToServerInformationDialog, "PublishToServerDialog");
                    publishToServerInformationDialog.dispose();
                }
                catch (PublishException e1)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PublishToServerCommand.actionPerformed ", e1);
                    JOptionPane.showMessageDialog(publishToServerInformationDialog, TranslationManager.getInstance().getTranslation("R", "PublishToServerCommand.Error.Message", e1.getMessage()), TranslationManager.getInstance().getTranslation("R", "Error.Title"), JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull
            ActionEvent e)
            {
                String reportNameString = centerPanel.getReportNameTextField().getText();
                String publishLocation = centerPanel.getPublishLocationTextField().getText();
                String type = centerPanel.getTypeComboBox().getSelectedItem().toString();
                String webPublishURL = centerPanel.getWebPublishURLTextField().getText();
                String serverUserId = centerPanel.getServerUserIdTextField().getText();
                boolean publishXActionFile = centerPanel.getPublishXActionFileCheckBox().isSelected();
                boolean useJNDIName = centerPanel.getUseJNDINameCheckBox().isSelected();

                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_REPORT_NAME, reportNameString);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_EXPORT_TYPE, type);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_LOCATION, publishLocation);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_WEB_LOCATION, webPublishURL);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_USER_ID, serverUserId);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_XACTION, Boolean.valueOf(publishXActionFile).toString());
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_USE_JNDI, Boolean.valueOf(useJNDIName).toString());

                reportDialog.getWorkspaceSettings().storeDialogBounds(publishToServerInformationDialog, "PublishToServerDialog");
                publishToServerInformationDialog.dispose();
            }
        });

        if (!reportDialog.getWorkspaceSettings().restoreDialogBounds(publishToServerInformationDialog, "PublishToServerDialog"))
        {
            publishToServerInformationDialog.pack();
        }
        GUIUtils.ensureMinimumDialogSize(publishToServerInformationDialog, 400, 350);

        WindowUtils.setLocationRelativeTo(publishToServerInformationDialog, reportDialog);
        publishToServerInformationDialog.setVisible(true);
    }


    @NotNull
    private String export(@NotNull final CenterPanelDialog exportInformationDialog,
                          @NotNull final Report report,
                          @NotNull final String reportNameString,
                          @NotNull final String publishLocation,
                          @NotNull final String type,
                          @NotNull final String webPublishURL,
                          final boolean publishXActionFile,
                          @NotNull final char[] publishPassword,
                          @NotNull final String serverUserId,
                          @NotNull final char[] serverPassword,
                          final boolean useJNDIName) throws PublishException
    {
        final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(exportInformationDialog, TranslationManager.getInstance().getTranslation("R", "PublishToServerCommand.ProgressDialog.Title"), "");

        final PublishException[] ex = new PublishException[1];
        final String[] message = new String[1];
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                JFreeReportPublishOnServerExporter reportExporter = new JFreeReportPublishOnServerExporter(reportNameString, publishLocation, type, webPublishURL, publishXActionFile, publishPassword, serverUserId, serverPassword, useJNDIName);
                try
                {
                    reportExporter.exportReport(false, report);
                    message[0] = reportExporter.getMessage();

                    EventQueue.invokeAndWait(new Runnable()
                    {
                        public void run()
                        {
                            progressDialog.dispose();
                        }
                    });
                }
                catch (final PublishException e)
                {
                    ex[0] = e;
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            progressDialog.dispose();
                        }
                    });
                }
                catch (final Throwable e)
                {
                    ex[0] = new PublishException(e);
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            progressDialog.dispose();
                            UncaughtExcpetionsModel.getInstance().addException(e);
                        }
                    });
                }
            }
        });

        t.setDaemon(true);
        t.start();

        if (t.isAlive())
        {
            progressDialog.setVisible(true);
        }
        else
        {
            progressDialog.dispose();
        }

        if (ex[0] != null)
        {
            throw ex[0];
        }

        return message[0];
    }


    private static class PublishToServerInformationPanel extends JPanel
    {
        @NotNull
        private static final String DEFAULT_SOLUTION_PATH = "/samples/reporting";
        @NotNull
        private static final String DEFAULT_SERVER_LOCATION = "http://localhost:8080/pentaho/RepositoryFilePublisher";
        @NotNull
        private static final String[] EXPORT_TYPES = new String[]{"pdf", "html", "csv", "rtf", "xls"};

        @NotNull
        private JTextField reportNameTextField;
        @NotNull
        private JTextField publishLocationTextField;
        @NotNull
        private JComboBox typeComboBox;
        @NotNull
        private JTextField webPublishURLTextField;
        @NotNull
        private JPasswordField serverPasswordField;
        @NotNull
        private JTextField serverUserIdTextField;
        @NotNull
        private JPasswordField publishPasswordField;
        @NotNull
        private JCheckBox publishXActionFileCheckBox;
        @NotNull
        private JCheckBox useJNDINameCheckBox;


        private PublishToServerInformationPanel(@NotNull
        final ReportDialog reportDialog)
        {
            Report report = reportDialog.getReport();

            String rn = WorkspaceSettings.getInstance().getString(WorkspaceSettings.PUBLISH_REPORT_NAME);
            String exportType = WorkspaceSettings.getInstance().getString(WorkspaceSettings.PUBLISH_EXPORT_TYPE);
            String publishLocation = WorkspaceSettings.getInstance().getString(WorkspaceSettings.PUBLISH_LOCATION);
            String webPublishLocation = WorkspaceSettings.getInstance().getString(WorkspaceSettings.PUBLISH_WEB_LOCATION);
            String userId = WorkspaceSettings.getInstance().getString(WorkspaceSettings.PUBLISH_USER_ID);
            Boolean publishXAction = WorkspaceSettings.getInstance().getBoolean(WorkspaceSettings.PUBLISH_XACTION);
            Boolean useJNDI = WorkspaceSettings.getInstance().getBoolean(WorkspaceSettings.PUBLISH_USE_JNDI);

            reportNameTextField = new JTextField(rn != null ? rn : (report != null ? report.getName() : ""));
            publishLocationTextField = new JTextField(publishLocation != null ? publishLocation : DEFAULT_SOLUTION_PATH);
            typeComboBox = new JComboBox(EXPORT_TYPES);
            if (exportType != null)
            {
                typeComboBox.setSelectedItem(exportType);
            }
            webPublishURLTextField = new JTextField(webPublishLocation != null ? webPublishLocation : DEFAULT_SERVER_LOCATION);
            serverPasswordField = new JPasswordField();
            serverPasswordField.addFocusListener(new FocusListener()
            {

                public void focusGained(@NotNull FocusEvent e)
                {
                    serverPasswordField.selectAll();
                }


                public void focusLost(@NotNull FocusEvent e)
                {
                }

            });
            serverUserIdTextField = new JTextField(userId != null ? userId : "");
            publishPasswordField = new JPasswordField();
            publishPasswordField.addFocusListener(new FocusListener()
            {

                public void focusGained(@NotNull FocusEvent e)
                {
                    publishPasswordField.selectAll();
                }


                public void focusLost(@NotNull FocusEvent e)
                {
                }

            });
            publishXActionFileCheckBox = ComponentFactory.createCheckBox("R", "ExportInformationPanel.PublishXActionFile");
            // noinspection SimplifiableConditionalExpression
            publishXActionFileCheckBox.setSelected(publishXAction != null ? publishXAction.booleanValue() : true);
            useJNDINameCheckBox = ComponentFactory.createCheckBox("R", "ExportInformationPanel.UseJNDIName");
            // noinspection SimplifiableConditionalExpression
            useJNDINameCheckBox.setSelected(useJNDI != null ? useJNDI.booleanValue() : false);

            JLabel reportNameLabel = ComponentFactory.createLabel("R", "PublishToServerInformationPanel.ReportNameLabel", reportNameTextField);
            JLabel publishLocationLabel = ComponentFactory.createLabel("R", "PublishToServerInformationPanel.PublishLocationLabel", publishLocationTextField);
            JLabel typeLabel = ComponentFactory.createLabel("R", "PublishToServerInformationPanel.TypeLabel", typeComboBox);
            JLabel webPublishURLLabel = ComponentFactory.createLabel("R", "PublishToServerInformationPanel.WebPublishURLLabel", webPublishURLTextField);
            JLabel serverPasswordLabel = ComponentFactory.createLabel("R", "PublishToServerInformationPanel.ServerPasswordLabel", serverPasswordField);
            JLabel serverUserIdLabel = ComponentFactory.createLabel("R", "PublishToServerInformationPanel.ServerUserIdLabel", serverUserIdTextField);
            JLabel publishPasswordLabel = ComponentFactory.createLabel("R", "PublishToServerInformationPanel.PublishPasswordLabel", publishPasswordField);

            @NonNls
            FormLayout formLayout = new FormLayout("0dlu, default, 4dlu, fill:default:grow, 0dlu", "0dlu, " + "pref, " + "4dlu, " + "pref, " + "4dlu, " + "pref, " + "4dlu, " + "pref, " + "10dlu, " + "pref, " + "4dlu, " + "pref, " + "4dlu, "
                                                                                                   + "pref, " + "4dlu, " + "pref, " + "10dlu, " + "pref, " + "0dlu");
            @NonNls
            CellConstraints cc = new CellConstraints();

            setLayout(formLayout);

            add(reportNameLabel, cc.xy(2, 2));
            add(reportNameTextField, cc.xy(4, 2));

            add(publishLocationLabel, cc.xy(2, 4));
            add(publishLocationTextField, cc.xy(4, 4));

            add(typeLabel, cc.xy(2, 6));
            add(typeComboBox, cc.xy(4, 6));

            add(webPublishURLLabel, cc.xy(2, 8));
            add(webPublishURLTextField, cc.xy(4, 8));

            add(publishPasswordLabel, cc.xy(2, 10));
            add(publishPasswordField, cc.xy(4, 10));

            add(serverUserIdLabel, cc.xy(2, 12));
            add(serverUserIdTextField, cc.xy(4, 12));

            add(serverPasswordLabel, cc.xy(2, 14));
            add(serverPasswordField, cc.xy(4, 14));

            add(publishXActionFileCheckBox, cc.xy(4, 16));
            add(useJNDINameCheckBox, cc.xy(4, 18));
        }


        @NotNull
        public JTextField getPublishLocationTextField()
        {
            return publishLocationTextField;
        }


        @NotNull
        public JTextField getReportNameTextField()
        {
            return reportNameTextField;
        }


        @NotNull
        public JTextField getWebPublishURLTextField()
        {
            return webPublishURLTextField;
        }


        @NotNull
        public JComboBox getTypeComboBox()
        {
            return typeComboBox;
        }


        @NotNull
        public JCheckBox getPublishXActionFileCheckBox()
        {
            return publishXActionFileCheckBox;
        }


        @NotNull
        public JPasswordField getPublishPasswordField()
        {
            return publishPasswordField;
        }


        @NotNull
        public JPasswordField getServerPasswordField()
        {
            return serverPasswordField;
        }


        @NotNull
        public JTextField getServerUserIdTextField()
        {
            return serverUserIdTextField;
        }


        @NotNull
        public JCheckBox getUseJNDINameCheckBox()
        {
            return useJNDINameCheckBox;
        }
    }
}
