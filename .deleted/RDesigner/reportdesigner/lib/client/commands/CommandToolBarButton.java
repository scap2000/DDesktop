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
package org.pentaho.reportdesigner.lib.client.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Martin
 * Date: 21.11.2004
 * Time: 13:21:43
 */
public class CommandToolBarButton extends CommandButton
{
    @NotNull
    private String iconKey;


    public CommandToolBarButton(@NotNull final Presentation presentation, @NotNull String iconKey)
    {
        this(null, presentation, iconKey);
    }


    public CommandToolBarButton(@Nullable final CommandGroup commandGroup, @NotNull final Presentation presentation, @NotNull String iconKey)
    {
        super(presentation);

        setHorizontalTextPosition(CommandButton.CENTER);
        setVerticalTextPosition(CommandButton.BOTTOM);

        this.iconKey = iconKey;
        setRolloverEnabled(true);
        setMargin(new Insets(4, 4, 4, 4));

        //noinspection ConstantConditions
        if (presentation == null)
        {
            throw new IllegalArgumentException("Presentation can not be null");
        }

        update();

        setFocusable(false);

        addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(@NotNull MouseEvent e)
            {
                presentation.getCommandApplicationRoot().setStatusText(presentation.getDescription());
            }


            public void mouseExited(@NotNull MouseEvent e)
            {
                presentation.getCommandApplicationRoot().clearStatusText();
            }
        });

        if (commandGroup != null)
        {
            final CommandGroup tempCommandGroup = commandGroup;
            addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    CommandPopupMenu commandPopupMenu = CommandManager.createCommandPopupMenu(presentation.getCommandApplicationRoot(), tempCommandGroup, CommandSettings.getInstance().getToolbarIconKey());
                    JPopupMenu popupMenu = commandPopupMenu.getJPopupMenu();
                    CommandToolBarButton button = CommandToolBarButton.this;
                    popupMenu.show(button, 0, button.getHeight());
                }
            });
        }
    }


    protected void update()
    {
        Presentation presentation = getPresentation();

        setIcon(presentation.getIcon(iconKey));
        setEnabled(presentation.isEnabled());
        setToolTipText(presentation.getText());
        setVisible(presentation.isVisible());
    }


}
