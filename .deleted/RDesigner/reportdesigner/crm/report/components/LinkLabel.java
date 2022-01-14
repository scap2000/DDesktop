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
package org.pentaho.reportdesigner.crm.report.components;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.util.ExternalToolLauncher;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 02.03.2006
 * Time: 14:01:58
 */
public class LinkLabel extends JTextField
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(LinkLabel.class.getName());


    public LinkLabel(@NotNull final String url)
    {
        super();

        //noinspection ConstantConditions
        if (url == null)
        {
            throw new IllegalArgumentException("url must not be null");
        }

        setText(url);

        JLabel helperLabel = new JLabel(url);
        setEditable(false);

        setBackground(helperLabel.getBackground());
        setForeground(Color.BLUE);
        setOpaque(helperLabel.isOpaque());

        setPreferredSize(helperLabel.getPreferredSize());
        setCaretPosition(0);

        setBorder(BorderFactory.createEmptyBorder());

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                Thread t = new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            ExternalToolLauncher.openURL(url);
                        }
                        catch (IOException e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "VisitOnlineHelpHTMLCommand.run ", e);
                            EventQueue.invokeLater(new Runnable()
                            {
                                public void run()
                                {
                                    JOptionPane.showMessageDialog(LinkLabel.this,
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
        });
    }
}
