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
import org.pentaho.reportdesigner.lib.client.components.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 14.06.2005
 * Time: 17:11:16
 */
public class ActionComponentsCommandHelper
{
    @NotNull
    private Command command;
    @NotNull
    private CommandApplicationRoot commandApplicationRoot;
    @Nullable
    private DataProvider dataProvider;
    @NotNull
    private Category category;
    @NotNull
    private String iconKey;

    @NotNull
    private ArrayList<Presentation> presentations;

    @NotNull
    private Presentation templatePresentation;


    public ActionComponentsCommandHelper(@NotNull CommandApplicationRoot commandApplicationRoot, @Nullable DataProvider dataProvider, @NotNull Command command, @NotNull Category category, @NotNull String iconKey)
    {
        //noinspection ConstantConditions
        if (commandApplicationRoot == null)
        {
            throw new IllegalArgumentException("commandApplicationRoot must not be null");
        }
        //noinspection ConstantConditions
        if (category == null)
        {
            throw new IllegalArgumentException("category must not be null");
        }
        //noinspection ConstantConditions
        if (iconKey == null)
        {
            throw new IllegalArgumentException("iconKey must not be null");
        }
        //noinspection ConstantConditions
        if (command == null)
        {
            throw new IllegalArgumentException("command must not be null");
        }

        this.commandApplicationRoot = commandApplicationRoot;
        this.dataProvider = dataProvider;
        this.category = category;
        this.iconKey = iconKey;

        this.command = command;
        presentations = new ArrayList<Presentation>();

        rebuildActionComponents();

        templatePresentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
        templatePresentation.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if ("structure".equals(evt.getPropertyName()))//NON-NLS
                {
                    rebuildActionComponents();
                }
            }
        });
    }


    private void rebuildActionComponents()
    {
        for (Presentation presentation : presentations)
        {
            presentation.clearPropertyChangeListeners();
        }

        presentations.clear();

        ArrayList<JComponent> categories = new ArrayList<JComponent>();

        addToolBarButtons(categories, command, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if ("structure".equals(evt.getPropertyName()))//NON-NLS
                {
                    rebuildActionComponents();
                }
            }
        });

        category.setActionComponents(categories.toArray(new JComponent[categories.size()]));
    }


    private void addToolBarButtons(@NotNull ArrayList<JComponent> categories, @NotNull final Command command, @NotNull PropertyChangeListener propertyChangeListener)
    {
        if (command instanceof CommandGroup)
        {
            CommandGroup commandGroup = (CommandGroup) command;
            final Presentation presentation = commandGroup.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);

            presentations.add(presentation);

            if (commandGroup.getType() == CommandGroup.Type.NORMAL)
            {
                Command[] commands = commandGroup.getChildren();
                for (Command c : commands)
                {
                    addToolBarButtons(categories, c, propertyChangeListener);
                }
            }
            else if (commandGroup.getType() == CommandGroup.Type.POPUP)
            {

                CommandDropDownToolBarButton toolBarButton = new CommandDropDownToolBarButton(commandGroup, presentation, iconKey);
                presentations.addAll(toolBarButton.getPresentations());
                categories.add(toolBarButton);
                toolBarButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        presentation.getCommandApplicationRoot().clearStatusText();
                    }
                });
            }
        }
        else if (command instanceof SeparatorCommand)
        {
            categories.add(new JToolBar.Separator());
        }
        else if (command instanceof ToggleCommand)
        {
            final Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);
            presentations.add(presentation);

            ToggleCommandToolBarButton toolBarButton = new ToggleCommandToolBarButton(presentation, iconKey);
            toolBarButton.setMargin(new Insets(2, 2, 2, 2));
            toolBarButton.setRolloverEnabled(true);
            categories.add(toolBarButton);
            toolBarButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    DataProvider dataProvider = getCommandProvider();
                    presentation.getCommandApplicationRoot().clearStatusText();
                    command.execute(new DefaultCommandEvent(null, dataProvider.getDataContext(), dataProvider.getPlace(), presentation));
                    CommandManager.dataProviderChanged();
                }
            });
        }
        else
        {
            final Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);
            presentations.add(presentation);

            JComponent customComponent = command.getCustomComponent(presentation);
            if (customComponent != null)
            {
                categories.add(customComponent);
            }
            else
            {
                CommandToolBarButton toolBarButton = new CommandToolBarButton(presentation, iconKey);
                toolBarButton.setMargin(new Insets(2, 2, 2, 2));
                toolBarButton.setRolloverEnabled(true);
                categories.add(toolBarButton);
                toolBarButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        DataProvider dataProvider = getCommandProvider();
                        presentation.getCommandApplicationRoot().clearStatusText();
                        command.execute(new DefaultCommandEvent(null, dataProvider.getDataContext(), dataProvider.getPlace(), presentation));
                        CommandManager.dataProviderChanged();
                    }
                });
            }
        }
    }


    public void update(@NotNull DataProvider dataProvider)
    {
        DataProvider actualDataProvider = dataProvider;
        if (this.dataProvider != null)
        {
            actualDataProvider = this.dataProvider;
        }

        ArrayList<Presentation> presentations = new ArrayList<Presentation>(this.presentations);
        for (Presentation commandPresentation : presentations)
        {
            CommandEvent event = new DefaultCommandEvent(null, actualDataProvider.getDataContext(), actualDataProvider.getPlace(), commandPresentation);
            commandPresentation.getCommand().update(event);
        }
    }


    @NotNull
    private DataProvider getCommandProvider()
    {
        if (dataProvider != null)
        {
            return dataProvider;
        }
        else
        {
            return CommandManager.getCurrentCommandProvider();
        }
    }
}
