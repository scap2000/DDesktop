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
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionGadget;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.TableModelDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport.JFreeReportExporter;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
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
public class CreateReportOnServerCommand extends AbstractCommand
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(CreateReportOnServerCommand.class.getName());


    public CreateReportOnServerCommand()
    {
        super("CreateReportOnServerCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "CreateReportOnServerCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "CreateReportOnServerCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "CreateReportOnServerCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "CreateReportOnServerCommand.Text"));

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "CreateReportOnServerCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        Report report = reportDialog.getReport();
        if (report != null)
        {
            DataSetsReportElement dataSetsReportElement = report.getDataSetsReportElement();
            ArrayList<ReportElement> children = dataSetsReportElement.getChildren();
            for (ReportElement reportElement : children)
            {
                if (reportElement instanceof TableModelDataSetReportElement)
                {
                    TableModelDataSetReportElement tableModelDataSetReportElement = (TableModelDataSetReportElement) reportElement;
                    if (tableModelDataSetReportElement.canCreateReportOnServer())
                    {
                        event.getPresentation().setEnabled(true);
                    }
                }
            }
        }
        event.getPresentation().setEnabled(false);
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        InspectionGadget inspectionGadget = reportDialog.getInspectionGadget();
        ToolWindow inspectionsToolWindow = reportDialog.getInspectionsToolWindow();

        Report report = reportDialog.getReport();

        if (report != null && inspectionGadget != null && inspectionsToolWindow != null)
        {
            Set<InspectionResult> inspectionResultsAfterRun = inspectionGadget.getInspectionResultsAfterRun(report);
            for (InspectionResult inspectionResult : inspectionResultsAfterRun)
            {
                if (inspectionResult.getSeverity() == InspectionResult.Severity.ERROR)
                {
                    inspectionsToolWindow.setSizeState(ToolWindow.SizeState.NORMAL);
                    JOptionPane.showMessageDialog(event.getPresentation().getCommandApplicationRoot().getRootJComponent(),
                                                  TranslationManager.getInstance().getTranslation("R", "CreateReportOnServerCommand.ReportDefinitionContainsErrors"),
                                                  TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        try
        {
            if (report != null)
            {
                ArrayList<ReportElement> children = report.getDataSetsReportElement().getChildren();
                TableModelDataSetReportElement tableModelDataSetReportElement = null;
                int tableModelDataSets = 0;
                for (ReportElement reportElement : children)
                {
                    if (reportElement instanceof TableModelDataSetReportElement)
                    {
                        tableModelDataSetReportElement = (TableModelDataSetReportElement) reportElement;
                        tableModelDataSets++;
                    }
                }

                if (tableModelDataSets > 1)
                {
                    JOptionPane.showMessageDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "CreateReportOnServerCommand.MoreThanOneTableModelDataSet"));
                }
                else if (tableModelDataSets == 1)
                {
                    if (tableModelDataSetReportElement.canCreateReportOnServer())
                    {
                        JFreeReportExporter jFreeReportExporter = new JFreeReportExporter();
                        jFreeReportExporter.exportReport(false, report);
                        tableModelDataSetReportElement.createReportOnServer(jFreeReportExporter.getExtendedReportDefinition());
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "CreateReportOnServerCommand.CanNotCreateReportOnServer"));
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "CreateReportOnServerCommand.NoTableModelDataSet"));
                }
            }
        }
        catch (Exception e1)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CreateReportOnServerCommand.execute ", e1);
            JOptionPane.showMessageDialog(event.getPresentation().getCommandApplicationRoot().getRootJComponent(),
                                          TranslationManager.getInstance().getTranslation("R", "CreateReportOnServerCommand.VerifyDataSetDefinition"),
                                          TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
}
