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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 21.11.2004
 * Time: 13:15:56
 */
public class CommandToolBar implements CommandComponent
{
    @NotNull
    private Command command;
    @NotNull
    private CommandApplicationRoot commandApplicationRoot;
    @Nullable
    private DataProvider dataProvider;
    @NotNull
    private String iconKey;

    @NotNull
    private ArrayList<Presentation> presentations;

    @NotNull
    private Presentation topLevelPresentation;


    public CommandToolBar(@NotNull CommandApplicationRoot commandApplicationRoot, @Nullable DataProvider dataProvider, @NotNull Command command)
    {
        this.commandApplicationRoot = commandApplicationRoot;
        this.dataProvider = dataProvider;
        this.iconKey = CommandSettings.getInstance().getToolbarIconKey();
        //noinspection ConstantConditions
        if (command == null)
        {
            throw new IllegalArgumentException("command must not be null");
        }

        this.command = command;
        presentations = new ArrayList<Presentation>();

        topLevelPresentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
    }


    @NotNull
    public JToolBar getToolBar()
    {
        //noinspection UNUSED_SYMBOL
        final JToolBar toolBar = new JToolBar()
        {
            //it's unused, but ensures the CommandToolBar is not GCed while the menuBar is still alive (see usage of WeakReferences). Fix this if you know a more elegant way to achieve this.
            @NotNull
            @SuppressWarnings({"UnusedDeclaration"})
            private CommandToolBar commandToolBar = CommandToolBar.this;
        };

        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        rebuildToolBar(toolBar);

        topLevelPresentation.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if ("structure".equals(evt.getPropertyName()))//NON-NLS
                {
                    rebuildToolBar(toolBar);
                }
            }
        });


        return toolBar;
    }


    private void rebuildToolBar(@NotNull final JToolBar toolBar)
    {
        toolBar.removeAll();

        for (Presentation presentation : presentations)
        {
            presentation.clearPropertyChangeListeners();
        }

        presentations.clear();


        addToolBarButtons(toolBar, command, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if ("structure".equals(evt.getPropertyName()))//NON-NLS
                {
                    rebuildToolBar(toolBar);
                }
            }
        });


        toolBar.revalidate();
        toolBar.repaint();
    }


    private void addToolBarButtons(@NotNull JToolBar toolBar, @NotNull final Command command, @NotNull PropertyChangeListener propertyChangeListener)
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
                    addToolBarButtons(toolBar, c, propertyChangeListener);
                }
            }
            else if (commandGroup.getType() == CommandGroup.Type.POPUP)
            {

                CommandDropDownToolBarButton toolBarButton = new CommandDropDownToolBarButton(commandGroup, presentation, iconKey);
                presentations.addAll(toolBarButton.getPresentations());
                toolBar.add(toolBarButton);
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
            toolBar.addSeparator();
        }
        else if (command instanceof ToggleCommand)
        {
            final Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);
            presentations.add(presentation);

            ToggleCommandToolBarButton toolBarButton = new ToggleCommandToolBarButton(presentation, iconKey);
            toolBar.add(toolBarButton);
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
                toolBar.add(customComponent);
            }
            else
            {
                CommandToolBarButton toolBarButton = new CommandToolBarButton(presentation, iconKey);
                toolBar.add(toolBarButton);
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
        DataProvider actualDataProvider = this.dataProvider;
        if (actualDataProvider == null)
        {
            actualDataProvider = dataProvider;
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

        return CommandManager.getCurrentCommandProvider();
    }


}
