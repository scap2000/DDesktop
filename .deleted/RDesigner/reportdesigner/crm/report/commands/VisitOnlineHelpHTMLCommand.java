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
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 20.02.2006
 * Time: 19:30:01
 */
public class VisitOnlineHelpHTMLCommand extends AbstractCommand
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(VisitOnlineHelpHTMLCommand.class.getName());


    public VisitOnlineHelpHTMLCommand()
    {
        super("VisitOnlineHelpHTMLCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "VisitOnlineHelpHTMLCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "VisitOnlineHelpHTMLCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "VisitOnlineHelpHTMLCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "VisitOnlineHelpHTMLCommand.Text"));
        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getVisitOnlineHelpHTMLIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "VisitOnlineHelpHTMLCommand.Accelerator")));
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
                    URL url = getClass().getResource("/res/icons/ReportFrameIcon.png");//NON-NLS
                    String urlStr = url.toString();
                    int from = "jar:".length();//NON-NLS
                    int to = urlStr.indexOf("/lib/ReportDesigner.jar!/");//NON-NLS
                    String fileName = urlStr.substring(from, to) + "/doc/index.html";//NON-NLS

                    ExternalToolLauncher.openURL(fileName);
                }
                catch (IOException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "VisitOnlineHelpHTMLCommand.run ", e);
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
