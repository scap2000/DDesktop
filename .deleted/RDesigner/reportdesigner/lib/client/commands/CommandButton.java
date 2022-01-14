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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * User: Martin
 * Date: 19.05.2005
 * Time: 07:50:28
 */
public class CommandButton extends JButton implements CommandComponent
{
    @NotNull
    private Presentation presentation;
    @Nullable
    private Command command;
    @Nullable
    private String iconKey;
    @Nullable
    private ButtonType buttonType;
    @Nullable
    @SuppressWarnings({"UnusedDeclaration"})
    private CommandApplicationRoot commandApplicationRoot;//probably used in future
    @Nullable
    private DataProvider dataProvider;


    public CommandButton(@NotNull Presentation presentation)
    {
        //noinspection ConstantConditions
        if (presentation == null)
        {
            throw new IllegalArgumentException("presentation must not be null");
        }

        this.presentation = presentation;

        presentation.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                update();
            }
        });
    }


    public CommandButton(@NotNull CommandApplicationRoot commandApplicationRoot, @Nullable DataProvider dataProvider, @NotNull Command command, @NotNull String iconKey, @NotNull ButtonType buttonType)
    {
        //noinspection ConstantConditions
        if (commandApplicationRoot == null)
        {
            throw new IllegalArgumentException("commandApplicationRoot must not be null");
        }
        //noinspection ConstantConditions
        if (command == null)
        {
            throw new IllegalArgumentException("command must not be null");
        }
        //noinspection ConstantConditions
        if (iconKey == null)
        {
            throw new IllegalArgumentException("iconKey must not be null");
        }
        //noinspection ConstantConditions
        if (buttonType == null)
        {
            throw new IllegalArgumentException("buttonType must not be null");
        }

        this.commandApplicationRoot = commandApplicationRoot;
        this.dataProvider = dataProvider;

        this.command = command;
        this.iconKey = iconKey;
        this.buttonType = buttonType;

        presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
    }


    @NotNull
    protected Presentation getPresentation()
    {
        return presentation;
    }


    protected void update()
    {
        ButtonType type = buttonType;

        if (type != null)
        {
            Presentation presentation = getPresentation();

            //noinspection EnumSwitchStatementWhichMissesCases
            switch (type)
            {
                case ICON:
                    setIcon(presentation.getIcon(iconKey));
                    setToolTipText(presentation.getText());
                    break;
                case TEXT:
                    setIcon(null);
                    setToolTipText(presentation.getText());
                    break;
            }
            setEnabled(presentation.isEnabled());
            setVisible(presentation.isVisible());
        }
    }


    public void update(@NotNull DataProvider dataProvider)
    {
        DataProvider actualDataProvider = this.dataProvider;
        //if!=null -> we have a fixed command provider -> not context sensitive

        if (actualDataProvider == null)
        {
            actualDataProvider = dataProvider;
        }

        Command c = command;
        if (c != null)
        {
            c.update(new DefaultCommandEvent(null, actualDataProvider.getDataContext(), actualDataProvider.getPlace(), presentation));
        }
    }
}
