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
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.util.XMLContextKeys;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class SaveReportCommand extends AbstractCommand
{


    public SaveReportCommand()
    {
        super("SaveReportCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "SaveReportCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "SaveReportCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "SaveReportCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "SaveReportCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getSaveIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "SaveReportCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        event.getPresentation().setEnabled(reportDialog.getReport() != null && reportDialog.isModified());
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        save(false, reportDialog, true);
    }


    @Nullable
    public static File save(boolean saveAs, @NotNull ReportDialog reportDialog, boolean guessReportFile)
    {
        File currentReportFile = reportDialog.getCurrentReportFile();
        File f = SaveReportCommand.getGuessedPath(reportDialog);

        JFileChooser fileChooser = null;
        if (saveAs)
        {
            if (currentReportFile != null)
            {
                fileChooser = new JFileChooser(currentReportFile);
                fileChooser.setSelectedFile(currentReportFile);
            }
            else if (f != null)
            {
                if (guessReportFile)
                {
                    fileChooser = new JFileChooser(f);
                    fileChooser.setSelectedFile(f);
                }
                else
                {
                    fileChooser = new JFileChooser(f);
                }
            }
            else
            {
                fileChooser = new JFileChooser();
            }
        }
        else
        {
            if (currentReportFile == null || !currentReportFile.canWrite())
            {
                if (f != null)
                {
                    fileChooser = new JFileChooser(f);
                    fileChooser.setSelectedFile(f);
                }
                else
                {
                    fileChooser = new JFileChooser();
                }
            }
        }

        if (fileChooser != null)
        {
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

            int option = fileChooser.showSaveDialog(reportDialog);
            if (option == JFileChooser.APPROVE_OPTION)
            {
                currentReportFile = fileChooser.getSelectedFile();

                if (!currentReportFile.getName().toLowerCase().endsWith(ReportDialogConstants.REPORT_FILE_ENDING))
                {
                    currentReportFile = new File(currentReportFile.getParentFile(), currentReportFile.getName() + ReportDialogConstants.REPORT_FILE_ENDING);
                }
            }
            else
            {
                return null;
            }
        }

        if ((saveAs || fileChooser != null) && currentReportFile.exists())
        {
            int option = JOptionPane.showConfirmDialog(reportDialog,
                                                       TranslationManager.getInstance().getTranslation("R", "FileChooser.Overwrite.Message"),
                                                       TranslationManager.getInstance().getTranslation("R", "FileChooser.Overwrite.Title"),
                                                       JOptionPane.OK_CANCEL_OPTION,
                                                       JOptionPane.WARNING_MESSAGE);
            if (option != JOptionPane.OK_OPTION)
            {
                return null;
            }
        }

        try
        {
            //noinspection IOResourceOpenedButNotSafelyClosed
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(currentReportFile), true);
            xmlWriter.writeDefaultProlog();

            XMLContext xmlContext = new XMLContext();
            XMLContextKeys.CONTEXT_PATH.putObject(xmlContext, currentReportFile.getParentFile());

            Report report = reportDialog.getReport();
            if (report instanceof SubReport)
            {
                xmlWriter.startElement(XMLConstants.SUBREPORT);
                report.externalizeObject(xmlWriter, xmlContext);
                xmlWriter.closeElement(XMLConstants.SUBREPORT);
            }
            else if (report != null)
            {
                xmlWriter.startElement(XMLConstants.REPORT);
                report.externalizeObject(xmlWriter, xmlContext);
                xmlWriter.closeElement(XMLConstants.REPORT);
            }

            xmlWriter.close();

            reportDialog.getWorkspaceSettings().put("LastAccessedReportFile", currentReportFile.getCanonicalPath());
            reportDialog.setCurrentReportFile(currentReportFile);
            reportDialog.setModified(false);
        }
        catch (IOException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }

        return currentReportFile;
    }


    @Nullable
    private static File getGuessedPath(@NotNull ReportDialog reportDialog)
    {
        String fileName = reportDialog.getWorkspaceSettings().getString("LastAccessedReportFile");
        if (fileName != null)
        {
            return new File(fileName);
        }
        return null;
    }
}
