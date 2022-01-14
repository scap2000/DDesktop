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
package org.pentaho.reportdesigner.crm.report.reportexporter;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jfree.report.JFreeReport;
import org.jfree.report.TableDataFactory;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.pdf.PdfOutputProcessor;
import org.jfree.report.modules.output.table.base.FlowReportProcessor;
import org.jfree.report.modules.output.table.base.StreamReportProcessor;
import org.jfree.report.modules.output.table.csv.StreamCSVOutputProcessor;
import org.jfree.report.modules.output.table.html.AllItemsHtmlPrinter;
import org.jfree.report.modules.output.table.html.FileSystemURLRewriter;
import org.jfree.report.modules.output.table.html.HtmlOutputProcessor;
import org.jfree.report.modules.output.table.html.HtmlPrinter;
import org.jfree.report.modules.output.table.html.StreamHtmlOutputProcessor;
import org.jfree.report.modules.output.table.rtf.StreamRTFOutputProcessor;
import org.jfree.report.modules.output.table.xls.FlowExcelOutputProcessor;
import org.jfree.report.modules.output.xml.XMLProcessor;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.dummy.DummyRepository;
import org.jfree.repository.file.FileRepository;
import org.pentaho.reportdesigner.crm.report.FileType;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.ReportDataTableModel;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionGadget;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport.JFreeReportVisitor;
import org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport.NullDataFactory;
import org.pentaho.reportdesigner.crm.report.util.ExternalToolLauncher;
import org.pentaho.reportdesigner.crm.report.util.JFreeReportBootingHelper;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.components.docking.ToolWindow;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 27.05.2006
 * Time: 13:18:02
 */
public class ExternalPreviewHelper
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ExternalPreviewHelper.class.getName());


    private ExternalPreviewHelper()
    {
    }


    public static void createAndOpenReport(@NotNull ReportDialog reportDialog, @NotNull FileType fileType)
    {
        //noinspection ConstantConditions
        if (reportDialog == null)
        {
            throw new IllegalArgumentException("reportDialog must not be null");
        }
        //noinspection ConstantConditions
        if (fileType == null)
        {
            throw new IllegalArgumentException("fileType must not be null");
        }

        InspectionGadget inspectionGadget = reportDialog.getInspectionGadget();
        ToolWindow inspectionsToolWindow = reportDialog.getInspectionsToolWindow();
        Report report = reportDialog.getReport();

        if (inspectionGadget != null && inspectionsToolWindow != null && report != null)
        {
            Set<InspectionResult> inspectionResultsAfterRun = inspectionGadget.getInspectionResultsAfterRun(report);
            for (InspectionResult inspectionResult : inspectionResultsAfterRun)
            {
                if (inspectionResult.getSeverity() == InspectionResult.Severity.ERROR)
                {
                    inspectionsToolWindow.setSizeState(ToolWindow.SizeState.NORMAL);
                    JOptionPane.showMessageDialog(reportDialog.getRootJComponent(),
                                                  TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.ReportDefinitionContainsErrors"),
                                                  TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try
            {
                showReportInThread(reportDialog, report, new ReportDataTableModel(ColumnInfo.EMPTY_ARRAY, new ArrayList<ArrayList<Object>>()), fileType);
            }
            catch (ReportCreationException e1)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CreateReportCommand.execute ", e1);
                JOptionPane.showMessageDialog(reportDialog.getRootJComponent(),
                                              TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.CouldNotCreateReport", e1.getCause().getMessage()),
                                              TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                              JOptionPane.ERROR_MESSAGE);

                reportDialog.showDesignView();
                ReportElementSelectionModel selectionModel = reportDialog.getReportElementModel();
                if (selectionModel != null)
                {
                    selectionModel.setSelection(Arrays.asList(e1.getReportElement()));
                }
            }
            catch (Exception e1)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CreateReportCommand.execute ", e1);

                JOptionPane.showMessageDialog(reportDialog.getRootJComponent(),
                                              TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.CouldNotCreateReport", e1.getMessage()),
                                              TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                              JOptionPane.ERROR_MESSAGE);
            }
        }

    }


    private static void showReportInThread(@NotNull final ReportDialog reportDialog, @NotNull final Report report, @NotNull final TableModel tableModel, @NotNull final FileType fileType) throws ReportCreationException
    {
        final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "ExternalPreviewHelper.CreateReport.Title"), "");

        final ReportCreationException[] reportCreationExceptions = new ReportCreationException[1];//just used to pass the exception from thread to EDT

        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    showReport(reportDialog, report, tableModel, fileType);
                }
                catch (ReportCreationException e)
                {
                    reportCreationExceptions[0] = e;
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

        t.setPriority(Thread.NORM_PRIORITY - 1);
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

        if (reportCreationExceptions[0] != null)
        {
            throw reportCreationExceptions[0];
        }

    }


    private static void showReport(@NotNull ReportDialog reportDialog, @NotNull Report report, @NotNull TableModel tableModel, @NotNull FileType fileType) throws ReportCreationException
    {
        JFreeReportVisitor reportVisitor = new JFreeReportVisitor();
        report.accept(null, reportVisitor);
        final JFreeReport jFreeReport = reportVisitor.getJFreeReport();
        jFreeReport.getReportConfiguration().setConfigProperty("org.jfree.report.NoPrinterAvailable", Boolean.TRUE.toString());
        jFreeReport.getReportConfiguration().setConfigProperty("org.jfree.report.layout.fontrenderer.UseMaxCharBounds", Boolean.valueOf(report.isUseMaxCharBounds()).toString());//NON-NLS

        if (jFreeReport.getDataFactory() instanceof NullDataFactory)
        {
            jFreeReport.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, tableModel));
        }

        JFreeReportBootingHelper.boot(reportDialog);

        if (fileType == FileType.PDF)
        {
            FileOutputStream fos = null;
            File file = getTemporaryFile(ReportDialogConstants.PREVIEW_FILEPREFIX + System.currentTimeMillis() + ReportDialogConstants.PDF_FILE_ENDING);
            try
            {
                //noinspection IOResourceOpenedButNotSafelyClosed
                fos = new FileOutputStream(file);
                PageableReportProcessor processor = new PageableReportProcessor(jFreeReport, new PdfOutputProcessor(jFreeReport.getConfiguration(), fos));
                processor.processReport();
            }
            catch (Exception e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExternalPreviewHelper.showReport ", e);
                throw new ReportCreationException("", report, e);
            }
            finally
            {
                IOUtil.closeStream(fos);
            }
            ExternalToolLauncher.openInOwnThread(reportDialog, FileType.PDF, file);
        }
        else if (fileType == FileType.HTML)
        {
            FileOutputStream fos = null;
            File file = getTemporaryFile(ReportDialogConstants.PREVIEW_FILEPREFIX + System.currentTimeMillis() + ReportDialogConstants.HTML_FILE_ENDING);
            try
            {
                FileRepository targetRepository = new FileRepository(file.getParentFile());
                ContentLocation targetRoot = targetRepository.getRoot();

                DummyRepository dataRepository = new DummyRepository();
                ContentLocation dataRoot = dataRepository.getRoot();

                HtmlOutputProcessor outputProcessor = new StreamHtmlOutputProcessor(jFreeReport.getConfiguration());
                HtmlPrinter printer = new AllItemsHtmlPrinter(jFreeReport.getResourceManager());

                String filename = file.getName();
                printer.setContentWriter(targetRoot, new DefaultNameGenerator(targetRoot, filename.substring(0, filename.length() - 5), ReportDialogConstants.HTML_FILE_ENDING.substring(1)));
                printer.setDataWriter(dataRoot, new DefaultNameGenerator(dataRoot, "content"));//NON-NLS
                printer.setUrlRewriter(new FileSystemURLRewriter());
                outputProcessor.setPrinter(printer);

                StreamReportProcessor sp = new StreamReportProcessor(jFreeReport, outputProcessor);
                sp.processReport();
            }
            catch (Exception e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExternalPreviewHelper.showReport ", e);
                throw new ReportCreationException("", report, e);
            }
            finally
            {
                IOUtil.closeStream(fos);
            }
            ExternalToolLauncher.openInOwnThread(reportDialog, FileType.HTML, file);
        }
        else if (fileType == FileType.XLS)
        {
            FileOutputStream fos = null;
            File file = getTemporaryFile(ReportDialogConstants.PREVIEW_FILEPREFIX + System.currentTimeMillis() + ReportDialogConstants.XLS_FILE_ENDING);
            try
            {
                //noinspection IOResourceOpenedButNotSafelyClosed
                fos = new FileOutputStream(file);
                FlowExcelOutputProcessor target = new FlowExcelOutputProcessor(jFreeReport.getConfiguration(), fos);
                FlowReportProcessor reportProcessor = new FlowReportProcessor(jFreeReport, target);
                reportProcessor.processReport();
            }
            catch (Exception e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExternalPreviewHelper.showReport ", e);
                throw new ReportCreationException("", report, e);
            }
            finally
            {
                IOUtil.closeStream(fos);
            }
            ExternalToolLauncher.openInOwnThread(reportDialog, FileType.XLS, file);
        }
        else if (fileType == FileType.RTF)
        {
            FileOutputStream fos = null;
            File file = getTemporaryFile(ReportDialogConstants.PREVIEW_FILEPREFIX + System.currentTimeMillis() + ReportDialogConstants.RTF_FILE_ENDING);
            try
            {
                //noinspection IOResourceOpenedButNotSafelyClosed
                fos = new FileOutputStream(file);
                StreamRTFOutputProcessor target = new StreamRTFOutputProcessor(jFreeReport.getConfiguration(), fos);
                StreamReportProcessor proc = new StreamReportProcessor(jFreeReport, target);
                proc.processReport();
            }
            catch (Exception e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExternalPreviewHelper.showReport ", e);
                throw new ReportCreationException("", report, e);
            }
            finally
            {
                IOUtil.closeStream(fos);
            }
            ExternalToolLauncher.openInOwnThread(reportDialog, FileType.RTF, file);
        }
        else if (fileType == FileType.CSV)
        {
            FileOutputStream fos = null;
            File file = getTemporaryFile(ReportDialogConstants.PREVIEW_FILEPREFIX + System.currentTimeMillis() + ReportDialogConstants.CSV_FILE_ENDING);
            try
            {
                //noinspection IOResourceOpenedButNotSafelyClosed
                fos = new FileOutputStream(file);
                StreamCSVOutputProcessor target = new StreamCSVOutputProcessor(jFreeReport.getConfiguration(), fos);
                target.setEncoding("UTF-8");//NON-NLS
                StreamReportProcessor reportProcessor = new StreamReportProcessor(jFreeReport, target);
                reportProcessor.processReport();
            }
            catch (Exception e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExternalPreviewHelper.showReport ", e);
                throw new ReportCreationException("", report, e);
            }
            finally
            {
                IOUtil.closeStream(fos);
            }
            ExternalToolLauncher.openInOwnThread(reportDialog, FileType.CSV, file);
        }
        else if (fileType == FileType.XML)
        {
            FileOutputStream fos = null;
            File file = getTemporaryFile(ReportDialogConstants.PREVIEW_FILEPREFIX + System.currentTimeMillis() + ReportDialogConstants.XML_FILE_ENDING);
            try
            {
                XMLProcessor processor = new XMLProcessor(jFreeReport);
                fos = new FileOutputStream(file);
                //noinspection IOResourceOpenedButNotSafelyClosed
                OutputStreamWriter writer = new OutputStreamWriter(fos/*, platform encoding*/);
                processor.setWriter(writer);
                processor.processReport();
            }
            catch (Exception e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExternalPreviewHelper.showReport ", e);
                throw new ReportCreationException("", report, e);
            }
            finally
            {
                IOUtil.closeStream(fos);
            }
            ExternalToolLauncher.openInOwnThread(reportDialog, FileType.XML, file);
        }
    }


    @NotNull
    private static File getTemporaryFile(@NotNull String filename)
    {
        File reportDirectory = new File(System.getProperty(ReportDialogConstants.USER_HOME), ReportDialogConstants.REPORT_DIRECTORY);
        File previewDirectory = new File(reportDirectory, ReportDialogConstants.PREVIEW_DIRECTORY);
        if (!previewDirectory.exists() && !previewDirectory.mkdirs())
        {
            throw new RuntimeException("Could not create temporary preview directory");
        }

        File tempFile = new File(previewDirectory, filename);
        tempFile.deleteOnExit();
        return tempFile;
    }
}
