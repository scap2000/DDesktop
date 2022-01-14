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
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.ReportGroups;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;

import javax.swing.*;
import java.util.Arrays;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class AddGroupCommand extends AbstractCommand
{
    public AddGroupCommand()
    {
        super("AddGroupCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "AddGroupCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "AddGroupCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "AddGroupCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "AddGroupCommand.Text"));

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "AddGroupCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null && reportElements.length == 1 && reportElements[0] instanceof ReportGroups)
        {
            ReportGroups reportGroups = (ReportGroups) reportElements[0];
            if (reportGroups.getChildren().isEmpty())
            {
                event.getPresentation().setEnabled(true);
            }
            else
            {
                event.getPresentation().setEnabled(false);
            }
        }
        else if (reportElements != null && reportElements.length == 1 && reportElements[0] instanceof ReportGroup)
        {
            ReportGroup reportGroups = (ReportGroup) reportElements[0];
            if (reportGroups.getChildren().size() == 2)
            {
                event.getPresentation().setEnabled(true);
            }
            else
            {
                event.getPresentation().setEnabled(false);
            }
        }
        else
        {
            event.getPresentation().setEnabled(false);
            event.getPresentation().setVisible(false);
        }
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        Undo undo = reportDialog.getUndo();
        ReportElementSelectionModel elementModel = reportDialog.getReportElementModel();
        if (elementModel != null)
        {
            if (reportElements != null && reportElements.length == 1 && reportElements[0] instanceof ReportGroups)
            {
                undo.startTransaction(UndoConstants.ADD_GROUP);
                addReportGroup(elementModel, (ReportGroups) reportElements[0]);
                undo.endTransaction();
            }
            else if (reportElements != null && reportElements.length == 1 && reportElements[0] instanceof ReportGroup)
            {
                undo.startTransaction(UndoConstants.ADD_GROUP);
                addReportGroup(elementModel, (ReportGroup) reportElements[0]);
                undo.endTransaction();
            }
        }

    }


    private void addReportGroup(@NotNull ReportElementSelectionModel reportElementSelectionModel, @NotNull ReportGroups reportGroups)
    {
        ReportGroup reportElement = ReportElementInfoFactory.getInstance().getReportGroupElementInfo().createReportElement();
        reportElement.setName("group");
        reportElement.setGroupFields(new String[]{"FIELD1"});
        reportGroups.addChild(reportElement);
        reportElementSelectionModel.setSelection(Arrays.asList(reportElement));
    }


    private void addReportGroup(@NotNull ReportElementSelectionModel reportElementSelectionModel, @NotNull ReportGroup reportGroup)
    {
        ReportGroup reportElement = ReportElementInfoFactory.getInstance().getReportGroupElementInfo().createReportElement();
        reportElement.setGroupFields(new String[]{"FIELD1"});
        reportElement.setName("subgroup");
        reportGroup.addChild(reportElement);
        reportElementSelectionModel.setSelection(Arrays.asList(reportElement));
    }
}
