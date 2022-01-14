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

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.common.util.BufferedHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Martin
 * Date: 20.02.2006
 * Time: 19:30:01
 */
public class ShowLogCommand extends AbstractCommand
{

    public ShowLogCommand()
    {
        super("ShowLogCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "ShowLogCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "ShowLogCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "ShowLogCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "ShowLogCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getShowLogIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "ShowLogCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        final CenterPanelDialog centerPanelDialog = new CenterPanelDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "ShowLogCommand.DialogTitle"), true);
        JPanel centerPanel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea(BufferedHandler.getBufferedHandler().getBufferedText());
        textArea.setEditable(false);
        TextComponentHelper.installDefaultPopupMenu(textArea);

        centerPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        textArea.setCaretPosition(textArea.getDocument().getLength());

        centerPanelDialog.setCenterPanel(centerPanel);

        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                centerPanelDialog.dispose();
            }
        });

        centerPanelDialog.setButtons(okButton, okButton, CenterPanelDialog.ButtonAlignment.CENTER, okButton);

        centerPanelDialog.setSize(800, 600);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, reportDialog);
        centerPanelDialog.setVisible(true);
    }

}
