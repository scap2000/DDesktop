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
public class GoToToolWindowPropertyCommand extends AbstractCommand
{

    public GoToToolWindowPropertyCommand()
    {
        super("GoToToolWindowPropertyCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "GoToToolWindowPropertyCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "GoToToolWindowPropertyCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "GoToToolWindowPropertyCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "GoToToolWindowPropertyCommand.Text"));

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "GoToToolWindowPropertyCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        event.getPresentation().setEnabled(reportDialog.getPropertyToolWindow() != null);
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        ToolWindow propertyToolWindow = reportDialog.getPropertyToolWindow();
        if (propertyToolWindow != null)
        {
            JComponent mainComponent = propertyToolWindow.getCategory().getMainComponent();
            if (mainComponent != null)
            {
                mainComponent.requestFocusInWindow();
            }
        }
    }
}
