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
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDesignerUtils;
import org.pentaho.reportdesigner.crm.report.ReportDesignerWindows;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class OpenReportCommand extends AbstractCommand
{

    public OpenReportCommand()
    {
        super("OpenReportCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "OpenReportCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "OpenReportCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getOpenIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull CommandEvent event)
    {
        final ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        File currentReportFile = reportDialog.getCurrentReportFile();
        File f = getGuessedPath(reportDialog);

        JFileChooser fileChooser;
        if (currentReportFile != null && currentReportFile.canRead())
        {
            fileChooser = new JFileChooser(currentReportFile);
            fileChooser.setSelectedFile(currentReportFile);
        }
        else if (f != null && f.canRead())
        {
            fileChooser = new JFileChooser(f);
            fileChooser.setSelectedFile(f);
        }
        else
        {
            fileChooser = new JFileChooser();
        }

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter()
        {
            public boolean accept(@NotNull File f)
            {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(ReportDialogConstants.REPORT_FILE_ENDING);
            }


            @NotNull
            public String getDescription()
            {
                return TranslationManager.getInstance().getTranslation("R", "FileChooser.ReportFiles.Description");
            }
        });

        fileChooser.setFileView(new ReportFileView());


        boolean selectedExistingFile = false;

        while (!selectedExistingFile)
        {
            int option = fileChooser.showOpenDialog(reportDialog);
            if (option == JFileChooser.APPROVE_OPTION)
            {
                currentReportFile = fileChooser.getSelectedFile();
                if (currentReportFile.exists())
                {
                    selectedExistingFile = true;
                }
                else
                {
                    JOptionPane.showMessageDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.FileDoesNotExist"));
                }
            }
            else
            {
                return;
            }
        }

        ReportDialog openReportDialog = ReportDesignerUtils.getOpenReportDialog(currentReportFile);
        if (openReportDialog != null)
        {
            openReportDialog.toFront();
            return;
        }

        ReportDialog newReportDialog = reportDialog;

        //if the currently opened report dialog contains an unmodified and empty report, we open it in this window
        if (ReportDesignerUtils.isModifiedOrNotEmpty(reportDialog))
        {
            newReportDialog = ReportDesignerWindows.getInstance().createNewReportDialog(null);
        }

        ReportDesignerUtils.openReport(currentReportFile, newReportDialog, null);
    }


    @Nullable
    private static File getGuessedPath(@NotNull ReportDialog reportDialog)
    {
        String fileName = reportDialog.getWorkspaceSettings().getString(WorkspaceSettings.LAST_ACCESSED_REPORT_FILE);
        if (fileName != null)
        {
            return new File(fileName);
        }
        return null;
    }
}
