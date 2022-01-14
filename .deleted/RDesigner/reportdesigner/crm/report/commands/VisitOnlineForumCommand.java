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
import org.pentaho.reportdesigner.crm.report.util.ExternalToolLauncher;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 20.02.2006
 * Time: 19:30:01
 */
public class VisitOnlineForumCommand extends AbstractCommand
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(VisitOnlineForumCommand.class.getName());


    public VisitOnlineForumCommand()
    {
        super("VisitOnlineForumCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "VisitOnlineForumCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "VisitOnlineForumCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "VisitOnlineForumCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "VisitOnlineForumCommand.Text"));
        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getVisitOnlineForumIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "VisitOnlineForumCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull final CommandEvent event)
    {
        Thread t = new Thread()
        {
            public void run()
            {
                try
                {
                    ExternalToolLauncher.openURL("http://forums.pentaho.org/forumdisplay.php?f=57");//NON-NLS
                }
                catch (IOException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "VisitOnlineForumCommand.run ", e);
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            JOptionPane.showMessageDialog(event.getPresentation().getCommandApplicationRoot().getRootJComponent(),
                                                          TranslationManager.getInstance().getTranslation("R", "ExternalToolLauncher.Error.Message"),
                                                          TranslationManager.getInstance().getTranslation("R", "ExternalToolLauncher.Error.Title"),
                                                          JOptionPane.ERROR_MESSAGE);
                        }
                    });

                }
            }
        };
        t.setDaemon(true);
        t.start();
    }

}
