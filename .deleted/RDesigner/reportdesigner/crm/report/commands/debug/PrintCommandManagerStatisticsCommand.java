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
package org.pentaho.reportdesigner.crm.report.commands.debug;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandManager;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class PrintCommandManagerStatisticsCommand extends AbstractCommand
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(PrintCommandManagerStatisticsCommand.class.getName());


    public PrintCommandManagerStatisticsCommand()
    {
        super("PrintCommandManagerStatisticsCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "PrintCommandManagerStatisticsCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "PrintCommandManagerStatisticsCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "PrintCommandManagerStatisticsCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "PrintCommandManagerStatisticsCommand.Text"));

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PrintCommandManagerStatisticsCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull CommandEvent event)
    {
        int requestedRefreshes = CommandManager.getRequestedRefreshes();
        int executedRefreshes = CommandManager.getExecutedRefreshes();
        double usedRefreshMillis = CommandManager.getUsedRefreshNanos() / (1000. * 1000.);
        double estimatedRefreshMillis = usedRefreshMillis / executedRefreshes * requestedRefreshes;

        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PrintCommandManagerStatisticsCommand.execute requestedRefreshes = " + requestedRefreshes);
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PrintCommandManagerStatisticsCommand.execute executedRefreshes = " + executedRefreshes);
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PrintCommandManagerStatisticsCommand.execute usedRefreshMillis = " + usedRefreshMillis);
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PrintCommandManagerStatisticsCommand.execute estimatedRefreshMillis = " + estimatedRefreshMillis);
    }

}
