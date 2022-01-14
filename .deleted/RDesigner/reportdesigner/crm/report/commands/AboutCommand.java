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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.util.VersionHelper;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 20.02.2006
 * Time: 19:30:01
 */
public class AboutCommand extends AbstractCommand
{

    public AboutCommand()
    {
        super("AboutCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "AboutCommand.Text"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "AboutCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "AboutCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "AboutCommand.Description"));
        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getAboutIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "AboutCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull CommandEvent event)
    {
        @Nullable
        Window w = SwingUtilities.getWindowAncestor(event.getPresentation().getCommandApplicationRoot().getRootJComponent());
        final JDialog dialog;
        if (w instanceof Frame)
        {
            dialog = new JDialog((Frame) w, true);
        }
        else
        {
            dialog = new JDialog((Dialog) w, true);
        }

        JButton applyButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        applyButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                dialog.dispose();
            }
        });

        VersionHelper.getInstance();

        JLabel label = new JLabel(IconLoader.getInstance().getAboutDialogPicture())
        {
            protected void paintComponent(@NotNull Graphics g)
            {
                super.paintComponent(g);

                final String versionText = VersionHelper.getInstance().getVersion();
                Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(versionText, g);
                g.drawString(versionText, (int) (stringBounds.getX() + 5), getHeight() - (int) (stringBounds.getHeight() + stringBounds.getY()) - 5);
            }
        };

        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, pref, 0dlu", "0dlu, pref, 0dlu");

        JPanel contentPanel = new JPanel(formLayout);

        CellConstraints cc = new CellConstraints();

        contentPanel.add(label, cc.xy(2, 2));

        dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
        label.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                dialog.dispose();
            }


            public void mousePressed(@NotNull MouseEvent e)
            {
                dialog.dispose();
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                dialog.dispose();
            }
        });

        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        dialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "close");//NON-NLS
        dialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "close");//NON-NLS

        dialog.getRootPane().getActionMap().put("close", new AbstractAction()//NON-NLS
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                dialog.dispose();
            }
        });

        dialog.getRootPane().setDefaultButton(applyButton);
        dialog.setUndecorated(true);

        dialog.pack();
        dialog.setResizable(false);
        WindowUtils.setLocationRelativeTo(dialog, w);
        dialog.setVisible(true);
    }

}

