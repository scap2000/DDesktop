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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpecChoice;
import org.pentaho.jfreereport.wizard.utility.CastorUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportGenerationUtility;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDesignerWindows;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.SideLinealComponent;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.JNDISource;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.Query;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.reportimporter.JFreeReportImporter;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.crm.report.util.JFreeReportBootingHelper;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.ExceptionUtils;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.xml.sax.InputSource;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class ImportReportDefinitionAsXMLCommand extends AbstractCommand
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ImportReportDefinitionAsXMLCommand.class.getName());


    public ImportReportDefinitionAsXMLCommand()
    {
        super("ImportReportDefinitionAsXMLCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "ImportReportDefinitionAsXMLCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "ImportReportDefinitionAsXMLCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "ImportReportDefinitionAsXMLCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "ImportReportDefinitionAsXMLCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getImportXMLIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "ImportReportDefinitionAsXMLCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull CommandEvent event)
    {
        final ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        File currentReportFile = reportDialog.getCurrentReportFile();
        File f = ImportReportDefinitionAsXMLCommand.getGuessedPath(reportDialog);

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
                return f.isDirectory() || f.getName().toLowerCase().endsWith(ReportDialogConstants.XML_FILE_ENDING) || f.getName().toLowerCase().endsWith(ReportDialogConstants.XREPORTSPEC_FILE_ENDING);
            }


            @NotNull
            public String getDescription()
            {
                return TranslationManager.getInstance().getTranslation("R", "FileChooser.JFreeReportFiles.Description");
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

        ReportDialog newReportDialog = ReportDesignerWindows.getInstance().createNewReportDialog(null);

        openReport(currentReportFile, newReportDialog);
    }


    private void openReport(@NotNull File currentReportFile, @NotNull final ReportDialog reportDialog)
    {
        try
        {
            long n1 = System.nanoTime();

            final File currentReportFile1 = currentReportFile;

            JFreeReportBootingHelper.boot(reportDialog);

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
                        try
                        {
                            final Report report;
                            if (currentReportFile1.getName().endsWith(ReportDialogConstants.XREPORTSPEC_FILE_ENDING))
                            {
                                ReportSpec reportSpec = loadReportSpec(currentReportFile1);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ReportGenerationUtility.createJFreeReportXML(reportSpec, baos, 0, 0, false, "", 0, 0);

                                final ReportGenerator generator = ReportGenerator.getInstance();
                                JFreeReport jFreeReport = generator.parseReport(new InputSource(new ByteArrayInputStream(baos.toByteArray())), new File(".").toURI().toURL());

                                JFreeReportImporter jFreeReportImporter = new JFreeReportImporter(jFreeReport);
                                report = jFreeReportImporter.getReport();
                                // Add the Dataset Element - MB
                                ReportSpecChoice choice = reportSpec.getReportSpecChoice();
                                if (choice != null)
                                {
                                    MultiDataSetReportElement dataSet = new MultiDataSetReportElement();
                                    ArrayList<Query> queries = new ArrayList<Query>();
                                    queries.add(new Query(ReportDialogConstants.DEFAULT_DATA_FACTORY, reportSpec.getQuery()));
                                    dataSet.setQueries(queries);
                                    DataSetsReportElement dataSetsReportElement = report.getDataSetsReportElement();
                                    if (choice.getJndiSource() != null)
                                    {
                                        ArrayList<JNDISource> tmp = new ArrayList<JNDISource>();
                                        tmp.add(new JNDISource(choice.getJndiSource(), null, null, null, null));
                                        dataSet.setJndiSources(tmp);
                                    }
                                    else if (choice.getXqueryUrl() != null)
                                    {
                                        dataSet.setXQueryDataFile(choice.getXqueryUrl());
                                    }
                                    else if (choice.getKettleUrl() != null)
                                    {
                                        //noinspection ThrowCaughtLocally
                                        throw new UnsupportedOperationException(TranslationManager.getInstance().getTranslation("R", "ImportReportDefinitionAsXMLCommand.InvalidDatasource"));
                                    }
                                    else
                                    {
                                        LOG.warning(TranslationManager.getInstance().getTranslation("R", "ImportReportDefinitionAsXMLCommand.UnknownDatasource"));
                                    }
                                    dataSetsReportElement.addChild(dataSet);
                                    reportDialog.getReportElementModel().setSelection(Arrays.asList(dataSet));
                                }

                            }
                            else
                            {
                                final ReportGenerator generator = ReportGenerator.getInstance();
                                JFreeReport jFreeReport = generator.parseReport(currentReportFile1.toURI().toURL());

                                JFreeReportImporter jFreeReportImporter = new JFreeReportImporter(jFreeReport);
                                report = jFreeReportImporter.getReport();
                            }
                            final String canonicalPath = currentReportFile1.getCanonicalPath();

                            EventQueue.invokeAndWait(new Runnable()
                            {
                                public void run()
                                {
                                    reportDialog.setReport(report);
                                    SideLinealComponent sideLinealComponent = reportDialog.getSideLinealComponent();
                                    if (sideLinealComponent != null)
                                    {
                                        sideLinealComponent.adjustToPerfectSize();
                                    }
                                    reportDialog.getWorkspaceSettings().put(WorkspaceSettings.LAST_ACCESSED_JFREEREPORT_FILE, canonicalPath);
                                    reportDialog.setModified(false);

                                    progressDialog.dispose();
                                }
                            });
                        }
                        catch (final Exception e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ImportReportDefinitionAsXMLCommand.run ", e);
                            ExceptionUtils.disposeDialogInEDT(progressDialog);
                            EventQueue.invokeLater(new Runnable()
                            {
                                public void run()
                                {
                                    JOptionPane.showMessageDialog(reportDialog,
                                                                  TranslationManager.getInstance().getTranslation("R", "ImportReportDefinitionAsXMLCommand.InvalidFile.Message"),
                                                                  TranslationManager.getInstance().getTranslation("R", "ImportReportDefinitionAsXMLCommand.InvalidFile.Title"), JOptionPane.ERROR_MESSAGE);
                                    UncaughtExcpetionsModel.getInstance().addException(e);
                                }
                            });

                            //noinspection UnnecessaryReturnStatement
                            return;//just to be clear
                        }

                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ImportReportDefinitionAsXMLCommand.run ", e);
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
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ImportReportDefinitionAsXMLCommand.openReport " + (n2 - n1) / (1000. * 1000.) + " ms");
        }
        catch (Exception e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
    }


    @Nullable
    private static File getGuessedPath(@NotNull ReportDialog reportDialog)
    {
        String fileName = reportDialog.getWorkspaceSettings().getString(WorkspaceSettings.LAST_ACCESSED_JFREEREPORT_FILE);
        if (fileName != null)
        {
            return new File(fileName);
        }
        return null;
    }


    @NotNull
    private ReportSpec loadReportSpec(@NotNull File reportSpecFilePath) throws FileNotFoundException
    {
        try
        {
            ZipFile zipFile = new ZipFile(reportSpecFilePath);
            ZipEntry reportSpecEntry = zipFile.getEntry("report.xreportspec"); //$NON-NLS-1$
            InputStream inputStream = zipFile.getInputStream(reportSpecEntry);
            return (ReportSpec) CastorUtility.getInstance().readCastorObject(inputStream, ReportSpec.class);
        }
        catch (Exception e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ImportReportDefinitionAsXMLCommand.loadReportSpec ", e);

            FileInputStream fis = null;
            try
            {
                //noinspection IOResourceOpenedButNotSafelyClosed
                fis = new FileInputStream(reportSpecFilePath);
                return (ReportSpec) CastorUtility.getInstance().readCastorObject(fis, ReportSpec.class);
            }
            finally
            {
                IOUtil.closeStream(fis);
            }
        }
    }
}
