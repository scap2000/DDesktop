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

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDesignerWindows;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.model.ReportFactory;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.crm.report.wizard.WizardDialog;
import org.pentaho.reportdesigner.crm.report.wizard.WizardPageDataSet;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class ReportGenerateWizardCommand extends AbstractCommand
{


    public ReportGenerateWizardCommand()
    {
        super("ReportGenerateWizardCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "ReportGenerateWizardCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "ReportGenerateWizardCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "ReportGenerateWizardCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "ReportGenerateWizardCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getOpenWizardIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "ReportGenerateWizardCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull CommandEvent event)
    {
        final ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        final WizardDialog wizardDialog = createWizardDialog(reportDialog);

        if (!reportDialog.getWorkspaceSettings().restoreDialogBounds(wizardDialog, "ReportGenerateWizard"))
        {
            wizardDialog.pack();
            GUIUtils.ensureMinimumDialogSize(wizardDialog, 750, 550);
        }
        WindowUtils.setLocationRelativeTo(wizardDialog, reportDialog);

        wizardDialog.setVisible(true);

        HashMap<String, WizardData> wizardDatas = wizardDialog.getWizardDatas();

        if (wizardDialog.isOk())
        {
            try
            {
                ReportDialog newReportDialog = ReportDesignerWindows.getInstance().createNewReportDialog(null);

                newReportDialog.setReport(ReportFactory.createReport(wizardDatas));
                newReportDialog.setCurrentReportFile(null);
                newReportDialog.setModified(true);
            }
            catch (Exception e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
        }
    }


    @NotNull
    private WizardDialog createWizardDialog(@NotNull ReportDialog reportDialog)
    {
        WizardDialog wizardDialog = new WizardDialog(reportDialog,
                                                     TranslationManager.getInstance().getTranslation("R", "ReportGenerateWizard.Title"),
                                                     true,
                                                     "ReportGenerateWizard",
                                                     new WizardPageDataSet());

        return wizardDialog;
    }
}

