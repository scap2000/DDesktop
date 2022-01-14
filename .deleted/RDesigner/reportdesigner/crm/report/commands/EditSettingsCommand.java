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
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.settings.SettingsDialog;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 01.03.2006
 * Time: 18:57:04
 */
public class EditSettingsCommand extends AbstractCommand
{
    public EditSettingsCommand()
    {
        super("EditSettingsCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "EditSettingsCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "EditSettingsCommand.Description"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getSettingsIcon());
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull CommandEvent event)
    {
        JComponent component = event.getPresentation().getCommandApplicationRoot().getRootJComponent();
        Window window = SwingUtilities.getWindowAncestor(component);
        SettingsDialog settingsDialog;
        if (window instanceof Dialog)
        {
            settingsDialog = new SettingsDialog((Dialog) window);
        }
        else
        {
            settingsDialog = new SettingsDialog((Frame) window);
        }

        settingsDialog.setVisible(true);
    }
}
