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
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.components.docking.ToolWindow;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class GoToToolWindowReportTreeCommand extends AbstractCommand
{

    public GoToToolWindowReportTreeCommand()
    {
        super("GoToToolWindowReportTreeCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "GoToToolWindowReportTreeCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "GoToToolWindowReportTreeCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "GoToToolWindowReportTreeCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "GoToToolWindowReportTreeCommand.Text"));

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "GoToToolWindowReportTreeCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        event.getPresentation().setEnabled(reportDialog.getReportTreeToolWindow() != null);
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        ToolWindow treeToolWindow = reportDialog.getReportTreeToolWindow();
        if (treeToolWindow != null)
        {
            JComponent mainComponent = treeToolWindow.getCategory().getMainComponent();
            if (mainComponent != null)
            {
                mainComponent.requestFocusInWindow();
            }
        }
    }
}
