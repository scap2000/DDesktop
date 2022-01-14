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

import org.gjt.xpp.XmlPullNode;
import org.gjt.xpp.XmlPullParser;
import org.gjt.xpp.XmlPullParserFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.components.MessageDialog;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.reportexporter.MergingReportVisitor;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.crm.report.util.XMLContextKeys;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.ExceptionUtils;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class MergeCommand extends AbstractCommand
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(MergeCommand.class.getName());


    public MergeCommand()
    {
        super("MergeCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "MergeCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "MergeCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "MergeCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "MergeCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getMergeIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "MergeCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        final ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        event.getPresentation().setEnabled(reportDialog.getReport() != null);
    }


    public void execute(@NotNull CommandEvent event)
    {
        final ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        if (reportDialog.isModified())
        {
            int option = JOptionPane.showConfirmDialog(reportDialog,
                                                       TranslationManager.getInstance().getTranslation("R", "ReportModifiedWarning.Message"),
                                                       TranslationManager.getInstance().getTranslation("R", "ReportModifiedWarning.Title"),
                                                       JOptionPane.YES_NO_OPTION,
                                                       JOptionPane.WARNING_MESSAGE);
            if (option != JOptionPane.YES_OPTION)
            {
                return;
            }
        }

        File currentReportFile;
        File f = MergeCommand.getGuessedPath(reportDialog);

        JFileChooser fileChooser;
        if (f != null && f.canRead())
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

        int option = fileChooser.showOpenDialog(reportDialog);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            currentReportFile = fileChooser.getSelectedFile();
        }
        else
        {
            return;
        }

        openReport(currentReportFile, reportDialog);
    }


    private void openReport(@NotNull File currentReportFile, @NotNull final ReportDialog reportDialog)
    {
        try
        {
            long n1 = System.nanoTime();

            final File currentReportFile1 = currentReportFile;

            final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(reportDialog,
                                                                                      TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.ProgressDialog.Title"),
                                                                                      "");

            Thread t = new Thread()
            {
                public void run()
                {
                    BufferedReader bufferedReader = null;
                    try
                    {
                        final Report report;
                        try
                        {
                            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                            //noinspection IOResourceOpenedButNotSafelyClosed
                            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(currentReportFile1), XMLConstants.ENCODING));
                            xmlPullParser.setInput(bufferedReader);
                            xmlPullParser.next(); // get first start tag
                            XmlPullNode node = xmlPullParserFactory.newPullNode(xmlPullParser);

                            XMLContext xmlContext = new XMLContext();
                            XMLContextKeys.CONTEXT_PATH.putObject(xmlContext, currentReportFile1.getParentFile());

                            report = ReportElementInfoFactory.getInstance().getReportReportElementInfo().createReportElement();

                            if (XMLConstants.REPORT.equals(node.getRawName()))
                            {
                                report.readObject(node, xmlContext);
                            }

                            node.resetPullNode();
                        }
                        catch (Exception e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "MergeCommand.run", e);
                            ExceptionUtils.disposeDialogInEDT(progressDialog);
                            EventQueue.invokeLater(new Runnable()
                            {
                                public void run()
                                {
                                    JOptionPane.showMessageDialog(reportDialog,
                                                                  TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.InvalidFile.Message"),
                                                                  TranslationManager.getInstance().getTranslation("R", "OpenReportCommand.InvalidFile.Title"), JOptionPane.ERROR_MESSAGE);
                                }
                            });

                            return;
                        }

                        final String canonicalPath = currentReportFile1.getCanonicalPath();

                        EventQueue.invokeAndWait(new Runnable()
                        {
                            public void run()
                            {
                                reportDialog.getUndo().startTransaction(UndoConstants.MERGE_REPORTS);
                                try
                                {
                                    Report origReport = reportDialog.getReport();
                                    //that's almost guaranteed see #update
                                    if (origReport != null)
                                    {
                                        MergingReportVisitor mergingReportVisitor = new MergingReportVisitor(origReport);
                                        report.accept(null, mergingReportVisitor);
                                        reportDialog.setReport(origReport);//MARKED triggers a rebuildBandsPanel etc. undo will be impossible
                                    }
                                }
                                catch (ReportCreationException e)
                                {
                                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "MergeCommand.run ", e);
                                    MessageDialog.showExceptionDialog(reportDialog,
                                                                      TranslationManager.getInstance().getTranslation("R", "MessageDialog.Generic.Title"),
                                                                      TranslationManager.getInstance().getTranslation("R", "MessageDialog.Generic.Message", e.getMessage()),
                                                                      e);
                                }
                                //finally
                                //{
                                //    must not be done since setReport already resets/recreates the undo
                                //    reportDialog.getUndo().endTransaction();
                                //}

                                reportDialog.getWorkspaceSettings().put(WorkspaceSettings.LAST_ACCESSED_REPORT_MERGE_FILE, canonicalPath);
                                progressDialog.dispose();
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "OpenReportCommand.run ", e);
                        ExceptionUtils.disposeDialogInEDT(progressDialog);
                        UncaughtExcpetionsModel.getInstance().addException(e);
                    }
                    finally
                    {
                        IOUtil.closeStream(bufferedReader);
                    }
                }
            };

            t.setDaemon(true);
            t.setPriority(Thread.NORM_PRIORITY - 1);
            t.start();

            if (t.isAlive())
            {
                progressDialog.setVisible(true);
            }


            long n2 = System.nanoTime();
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "OpenReportCommand.openReport " + (n2 - n1) / (1000. * 1000.) + " ms");
        }
        catch (Exception e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
    }


    @Nullable
    private static File getGuessedPath(@NotNull ReportDialog reportDialog)
    {
        String fileName = reportDialog.getWorkspaceSettings().getString(WorkspaceSettings.LAST_ACCESSED_REPORT_MERGE_FILE);
        if (fileName != null)
        {
            return new File(fileName);
        }
        return null;
    }
}
