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
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.ReportDataTableModel;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionGadget;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport.JFreeReportFileExporter;
import org.pentaho.reportdesigner.crm.report.util.JFreeReportBootingHelper;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.components.docking.ToolWindow;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class CreateReportCommand extends AbstractCommand
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(CreateReportCommand.class.getName());


    public CreateReportCommand()
    {
        super("CreateReportCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "CreateReportCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "CreateReportCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getCreateReportIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        boolean enable = reportDialog.getReport() != null && !(reportDialog.getReport() instanceof SubReport);
        event.getPresentation().setEnabled(enable);
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

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
                    JOptionPane.showMessageDialog(event.getPresentation().getCommandApplicationRoot().getRootJComponent(),
                                                  TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.ReportDefinitionContainsErrors"),
                                                  TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        JFreeReportBootingHelper.boot(reportDialog);

        JFreeReportFileExporter reportExporter = new JFreeReportFileExporter(null, null, false, null, null, null, false);
        try
        {
            if (report != null)
            {
                reportExporter.createReport(reportDialog, report, new ReportDataTableModel(ColumnInfo.EMPTY_ARRAY, new ArrayList<ArrayList<Object>>()));
            }
        }
        catch (Exception e1)
        {
            if (LOG.isLoggable(Level.WARNING)) LOG.log(Level.WARNING, "CreateReportCommand.execute ", e1);
            JOptionPane.showMessageDialog(event.getPresentation().getCommandApplicationRoot().getRootJComponent(),
                                          TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.VerifyDataSetDefinition"),
                                          TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
}
