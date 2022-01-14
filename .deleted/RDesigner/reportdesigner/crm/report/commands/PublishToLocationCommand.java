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
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionGadget;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport.JFreeReportFileExporter;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.crm.report.util.FileChooserUtils;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class PublishToLocationCommand extends AbstractCommand
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(PublishToLocationCommand.class.getName());


    public PublishToLocationCommand()
    {
        super("PublishToLocationCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "PublishToLocationCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "PublishToLocationCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "PublishToLocationCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "PublishToLocationCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getExportXMLIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PublishToLocationCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        boolean enable = reportDialog.getReport() != null && !(reportDialog.getReport() instanceof SubReport);
        event.getPresentation().setEnabled(enable);
    }


    public void execute(@NotNull CommandEvent event)
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
                    JOptionPane.showMessageDialog(event.getPresentation().getCommandApplicationRoot().getRootJComponent(),
                                                  TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.ReportDefinitionContainsErrors"),
                                                  TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        final CenterPanelDialog exportInformationDialog = CenterPanelDialog.createDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "ExportInformationDialog.Title"), true);
        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));
        exportInformationDialog.setButtons(okButton, cancelButton, okButton, cancelButton);
        final ExportInformationPanel centerPanel = new ExportInformationPanel(reportDialog);
        exportInformationDialog.setCenterPanel(centerPanel);

        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                //validate values
                String reportFileString = centerPanel.getReportFileTextField().getText();
                String xactionFileString = centerPanel.getXActionFileTextField().getText();
                String reportTitle = centerPanel.getReportTitleTextField().getText();
                String reportDescription = centerPanel.getReportDescriptionTextField().getText();
                String type = centerPanel.getTypeComboBox().getSelectedItem().toString();
                boolean exportXActionFile = centerPanel.getPublishXActionFileCheckBox().isSelected();
                boolean useJNDIName = centerPanel.getUseJNDINameCheckBox().isSelected();

                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_EXPORT_TYPE, type);
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_XACTION, Boolean.valueOf(exportXActionFile).toString());
                WorkspaceSettings.getInstance().put(WorkspaceSettings.PUBLISH_USE_JNDI, Boolean.valueOf(useJNDIName).toString());


                if ("".equals(reportFileString.trim()))
                {
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.NoFileSpecified.Message", reportFileString),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.NoFileSpecified.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (exportXActionFile && "".equals(xactionFileString.trim()))
                {
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.NoFileSpecified.Message", xactionFileString),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.NoFileSpecified.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (reportFileString.indexOf(File.separator) == -1)
                {
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.FileLocationMissing.Message", reportFileString),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.FileLocationMissing.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (xactionFileString.indexOf(File.separator) == -1)
                {
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.FileLocationMissing.Message", xactionFileString),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.SaveFileProblem.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File reportFile;
                File xactionFile = null;


                try
                {
                    reportFile = new File(reportFileString).getCanonicalFile();
                    if (exportXActionFile)
                    {
                        xactionFile = new File(xactionFileString).getCanonicalFile();
                    }
                }
                catch (IOException e1)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PublishToLocationCommand.actionPerformed ", e1);
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.SaveFileProblem.Message"),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.SaveFileProblem.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (reportFile.isDirectory() || (exportXActionFile && xactionFile.isDirectory()))
                {
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.SaveFileProblem.Message"),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.SaveFileProblem.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (reportFile.exists() && !reportFile.canWrite())
                {
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.NoWritePermission.Message", reportFile.getName()),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.NoWritePermission.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (exportXActionFile && (xactionFile.exists() && !xactionFile.canWrite()))
                {
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.NoWritePermission.Message", xactionFile.getName()),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.NoWritePermission.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (reportFile.exists())
                {
                    int warnOption = JOptionPane.showConfirmDialog(reportDialog,
                                                                   TranslationManager.getInstance().getTranslation("R", "FileChooser.Overwrite.File.Message", reportFile.getName()),
                                                                   TranslationManager.getInstance().getTranslation("R", "FileChooser.Overwrite.Title"),
                                                                   JOptionPane.OK_CANCEL_OPTION,
                                                                   JOptionPane.WARNING_MESSAGE);
                    if (warnOption != JOptionPane.OK_OPTION)
                    {
                        return;
                    }
                }

                if (exportXActionFile && xactionFile.exists())
                {
                    int warnOption = JOptionPane.showConfirmDialog(reportDialog,
                                                                   TranslationManager.getInstance().getTranslation("R", "FileChooser.Overwrite.File.Message", xactionFile.getName()),
                                                                   TranslationManager.getInstance().getTranslation("R", "FileChooser.Overwrite.Title"),
                                                                   JOptionPane.OK_CANCEL_OPTION,
                                                                   JOptionPane.WARNING_MESSAGE);
                    if (warnOption != JOptionPane.OK_OPTION)
                    {
                        return;
                    }
                }

                if (reportFile.getParentFile() == null)
                {
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.SaveFileProblem.Message", reportFile.getName()),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.SaveFileProblem.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (exportXActionFile && xactionFile.getParentFile() == null)
                {
                    JOptionPane.showMessageDialog(reportDialog,
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.SaveFileProblem.Message", xactionFile.getName()),
                                                  TranslationManager.getInstance().getTranslation("R", "FileChooser.SaveFileProblem.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!reportFile.getParentFile().exists())
                {
                    boolean success = reportFile.getParentFile().mkdirs();
                    if (!success)
                    {
                        JOptionPane.showMessageDialog(reportDialog,
                                                      TranslationManager.getInstance().getTranslation("R", "FileChooser.CreateDrectories.Message", reportFile.getName()),
                                                      TranslationManager.getInstance().getTranslation("R", "FileChooser.CreateDrectories.Title"),
                                                      JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                export(exportInformationDialog, report, reportFile, xactionFile, exportXActionFile, reportTitle, reportDescription, type, useJNDIName);
                reportDialog.getWorkspaceSettings().put("PublishToLocationCommand.LastAccessedFile", reportFile.getAbsolutePath());

                reportDialog.getWorkspaceSettings().storeDialogBounds(exportInformationDialog, "ExportInformationDialog");
                exportInformationDialog.dispose();
            }

        });

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                reportDialog.getWorkspaceSettings().storeDialogBounds(exportInformationDialog, "ExportInformationDialog");
                exportInformationDialog.dispose();
            }
        });


        if (!reportDialog.getWorkspaceSettings().restoreDialogBounds(exportInformationDialog, "ExportInformationDialog"))
        {
            exportInformationDialog.pack();
            GUIUtils.ensureMinimumDialogSize(exportInformationDialog, 400, 250);
        }

        WindowUtils.setLocationRelativeTo(exportInformationDialog, reportDialog);
        exportInformationDialog.setVisible(true);
    }


    private void export(@NotNull CenterPanelDialog exportInformationDialog, @NotNull final Report report, @NotNull final File reportFile, @Nullable final File xactionFile, final boolean exportXActionFile, @NotNull final String reportTitle, @NotNull final String reportDescription, @NotNull final String type, final boolean useJNDIName)
    {
        final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(exportInformationDialog, TranslationManager.getInstance().getTranslation("R", "PublishToLocationCommand.ProgressDialog.Title"), "");

        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                JFreeReportFileExporter reportExporter = new JFreeReportFileExporter(reportFile, xactionFile, exportXActionFile, reportTitle, reportDescription, type, useJNDIName);
                try
                {
                    reportExporter.exportReport(false, report);
                }
                catch (final Exception e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PublishToLocationCommand.execute ", e);
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            progressDialog.dispose();
                            UncaughtExcpetionsModel.getInstance().addException(e);
                        }
                    });
                }
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        progressDialog.dispose();
                    }
                });
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

    }


    private static class ExportInformationPanel extends JPanel
    {
        @NotNull
        private JTextField reportFileTextField;
        @NotNull
        private JTextField xactionFileTextField;
        private boolean xactionFieldModified;
        @NotNull
        private JCheckBox publishXActionFileCheckBox;
        @NotNull
        private JCheckBox useJNDINameCheckBox;

        @NotNull
        private JButton reportFileButton;
        @NotNull
        private JButton xactionFileButton;

        @NotNull
        private JTextField reportTitleTextField;
        @NotNull
        private JTextField reportDescriptionTextField;

        @NotNull
        private JComboBox typeComboBox;


        private ExportInformationPanel(@NotNull final ReportDialog reportDialog)
        {
            reportFileTextField = new JTextField();
            xactionFileTextField = new JTextField();
            xactionFieldModified = false;

            reportFileButton = new JButton("...");
            reportFileButton.setDefaultCapable(false);
            reportFileButton.setMargin(new Insets(1, 1, 1, 1));
            xactionFileButton = new JButton("...");
            xactionFileButton.setDefaultCapable(false);
            xactionFileButton.setMargin(new Insets(1, 1, 1, 1));
            publishXActionFileCheckBox = ComponentFactory.createCheckBox("R", "ExportInformationPanel.PublishXActionFile");
            useJNDINameCheckBox = ComponentFactory.createCheckBox("R", "ExportInformationPanel.UseJNDIName");

            JLabel reportFileLabel = ComponentFactory.createLabel("R", "ExportInformationPanel.ReportFileLabel", reportFileTextField);
            final JLabel xactionFileLabel = ComponentFactory.createLabel("R", "ExportInformationPanel.XActionFileLabel", xactionFileTextField);

            Report report = reportDialog.getReport();
            reportTitleTextField = new JTextField(report != null ? report.getName() : "");
            reportDescriptionTextField = new JTextField(TranslationManager.getInstance().getTranslation("R", "ExportInformationPanel.InitialDescription"));

            JLabel reportTitleLabel = ComponentFactory.createLabel("R", "ExportInformationPanel.TitleLabel", reportTitleTextField);
            JLabel reportDescriptionLabel = ComponentFactory.createLabel("R", "ExportInformationPanel.DescriptionLabel", reportDescriptionTextField);

            typeComboBox = new JComboBox(new Object[]{"pdf", "html", "csv", "xml", "rtf", "xls"});//NON-NLS

            JLabel typeLabel = ComponentFactory.createLabel("R", "ExportInformationPanel.TypeLabel", typeComboBox);

            String exportType = WorkspaceSettings.getInstance().getString(WorkspaceSettings.PUBLISH_EXPORT_TYPE);
            if (exportType != null)
            {
                typeComboBox.setSelectedItem(exportType);
            }
            Boolean publishXAction = WorkspaceSettings.getInstance().getBoolean(WorkspaceSettings.PUBLISH_XACTION);
            //noinspection SimplifiableConditionalExpression
            publishXActionFileCheckBox.setSelected(publishXAction != null ? publishXAction.booleanValue() : false);

            Boolean useJNDI = WorkspaceSettings.getInstance().getBoolean(WorkspaceSettings.PUBLISH_USE_JNDI);
            //noinspection SimplifiableConditionalExpression
            useJNDINameCheckBox.setSelected(useJNDI != null ? useJNDI.booleanValue() : false);


            @NonNls
            FormLayout formLayout = new FormLayout("0dlu, default, 4dlu, fill:default:grow, 4dlu, default, 0dlu",
                                                   "0dlu, " +
                                                   "default, " +
                                                   "4dlu, " +
                                                   "default, " +
                                                   "4dlu, " +
                                                   "default, " +
                                                   "10dlu, " +
                                                   "default, " +
                                                   "4dlu, " +
                                                   "default, " +
                                                   "4dlu, " +
                                                   "default, "
                                                   + "4dlu, " +
                                                   "default, " +
                                                   "0dlu");
            @NonNls
            CellConstraints cc = new CellConstraints();

            setLayout(formLayout);

            add(reportFileLabel, cc.xy(2, 2));
            add(reportFileTextField, cc.xy(4, 2));
            add(reportFileButton, cc.xy(6, 2));

            add(publishXActionFileCheckBox, cc.xy(4, 4));
            add(useJNDINameCheckBox, cc.xy(4, 6));

            add(xactionFileLabel, cc.xy(2, 8));
            add(xactionFileTextField, cc.xy(4, 8));
            add(xactionFileButton, cc.xy(6, 8));

            add(reportTitleLabel, cc.xy(2, 10));
            add(reportTitleTextField, cc.xy(4, 10));

            add(reportDescriptionLabel, cc.xy(2, 12));
            add(reportDescriptionTextField, cc.xy(4, 12));

            add(typeLabel, cc.xy(2, 14));
            add(typeComboBox, cc.xy(4, 14));

            publishXActionFileCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    xactionFileLabel.setEnabled(publishXActionFileCheckBox.isSelected());
                    xactionFileTextField.setEnabled(publishXActionFileCheckBox.isSelected());
                    xactionFileButton.setEnabled(publishXActionFileCheckBox.isSelected());
                }
            });

            reportFileButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    final File reportFile = FileChooserUtils.requestSaveFile(reportDialog.getRootJComponent(), reportDialog.getWorkspaceSettings(), "PublishToLocationCommand.LastAccessedFile", ReportDialogConstants.XML_FILE_ENDING, TranslationManager.getInstance().getTranslation("R", "PublishToLocationCommand.XMLFiles.Description"), false);//NON-NLS
                    if (reportFile == null)
                    {
                        return;
                    }

                    reportFileTextField.setText(reportFile.getAbsolutePath());

                    if (!xactionFieldModified)
                    {
                        String xactionFileName = reportFile.getAbsolutePath();
                        if (xactionFileName.toLowerCase().endsWith(ReportDialogConstants.XML_FILE_ENDING))//NON-NLS
                        {
                            xactionFileName = xactionFileName.substring(0, xactionFileName.length() - 4) + ReportDialogConstants.XACTION_FILE_ENDING;
                        }
                        else
                        {
                            xactionFileName = xactionFileName + ReportDialogConstants.XACTION_FILE_ENDING;
                        }
                        xactionFileTextField.setText(xactionFileName);
                        xactionFieldModified = false;//revert, documentlistener set it to true
                    }
                }
            });

            xactionFileButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    final File xactionFile = FileChooserUtils.requestSaveFile(reportDialog.getRootJComponent(), reportDialog.getWorkspaceSettings(), "PublishToLocationCommand.LastAccessedFile", ReportDialogConstants.XACTION_FILE_ENDING, TranslationManager.getInstance().getTranslation("R", "PublishToLocationCommand.XActionFiles.Description"));//NON-NLS
                    if (xactionFile == null)
                    {
                        return;
                    }

                    xactionFileTextField.setText(xactionFile.getAbsolutePath());
                    xactionFieldModified = true;
                }
            });

            final DocumentListener xactionModificationListener = new DocumentListener()
            {
                public void insertUpdate(@NotNull DocumentEvent e)
                {
                    xactionFieldModified = true;
                }


                public void removeUpdate(@NotNull DocumentEvent e)
                {
                    xactionFieldModified = true;
                }


                public void changedUpdate(@NotNull DocumentEvent e)
                {
                    xactionFieldModified = true;
                }
            };

            xactionFileTextField.getDocument().addDocumentListener(xactionModificationListener);

            reportFileTextField.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(@NotNull DocumentEvent e)
                {
                    updateXActionFile();
                }


                public void removeUpdate(@NotNull DocumentEvent e)
                {
                    updateXActionFile();
                }


                public void changedUpdate(@NotNull DocumentEvent e)
                {
                    updateXActionFile();
                }


                private void updateXActionFile()
                {
                    if (!xactionFieldModified)
                    {
                        xactionFileTextField.getDocument().removeDocumentListener(xactionModificationListener);

                        String xactionFileName = reportFileTextField.getText();
                        if (xactionFileName.toLowerCase().endsWith(ReportDialogConstants.XML_FILE_ENDING))//NON-NLS
                        {
                            xactionFileName = xactionFileName.substring(0, xactionFileName.length() - 4) + ReportDialogConstants.XACTION_FILE_ENDING;
                        }
                        else
                        {
                            xactionFileName = xactionFileName + ReportDialogConstants.XACTION_FILE_ENDING;
                        }
                        xactionFileTextField.setText(xactionFileName);

                        xactionFileTextField.getDocument().addDocumentListener(xactionModificationListener);
                    }
                }
            });
        }


        @NotNull
        public JTextField getReportFileTextField()
        {
            return reportFileTextField;
        }


        @NotNull
        public JTextField getXActionFileTextField()
        {
            return xactionFileTextField;
        }


        @NotNull
        public JTextField getReportTitleTextField()
        {
            return reportTitleTextField;
        }


        @NotNull
        public JTextField getReportDescriptionTextField()
        {
            return reportDescriptionTextField;
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
        public JCheckBox getUseJNDINameCheckBox()
        {
            return useJNDINameCheckBox;
        }
    }
}
