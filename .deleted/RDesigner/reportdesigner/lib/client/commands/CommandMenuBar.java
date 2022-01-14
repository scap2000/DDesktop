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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 21.11.2004
 * Time: 11:23:59
 */
public class CommandMenuBar implements CommandComponent
{
    @NotNull
    private Command command;
    @NotNull
    private CommandApplicationRoot commandApplicationRoot;
    @NotNull
    private String iconKey;

    @NotNull
    private ArrayList<Presentation> presentations;


    public CommandMenuBar(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull Command command)
    {
        this.commandApplicationRoot = commandApplicationRoot;
        this.iconKey = CommandSettings.getInstance().getMenuIconKey();
        //noinspection ConstantConditions
        if (command == null)
        {
            throw new IllegalArgumentException("command must not be null");
        }

        this.command = command;
        presentations = new ArrayList<Presentation>();
    }


    @NotNull
    public JMenuBar getJMenuBar()
    {
        //noinspection UNUSED_SYMBOL
        final JMenuBar menuBar = new JMenuBar()
        {
            //it's unused, but ensures the CommandMenuBar is not GCed while the menuBar is still alive (see usage of WeakReferences). Fix this if you know a more elegant way to achieve this.
            @NotNull
            @SuppressWarnings({"UnusedDeclaration"})
            private CommandMenuBar commandMenuBar = CommandMenuBar.this;
        };

        rebuildMenuBar(menuBar);

        return menuBar;
    }


    private void rebuildMenuBar(@NotNull final JMenuBar menuBar)
    {
        menuBar.removeAll();

        for (Presentation presentation : presentations)
        {
            presentation.clearPropertyChangeListeners();
        }

        presentations.clear();

        PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if ("structure".equals(evt.getPropertyName()))//NON-NLS
                {
                    rebuildMenuBar(menuBar);
                }
            }
        };

        if (command instanceof CommandGroup)
        {
            CommandGroup commandGroup = (CommandGroup) command;
            Presentation presentation = commandGroup.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);
            presentations.add(presentation);

            Command[] commands = commandGroup.getChildren();

            for (Command c : commands)
            {
                addMenuItems(menuBar, c, propertyChangeListener);
            }
        }


        menuBar.revalidate();
        menuBar.repaint();
    }


    private void addMenuItems(@NotNull JMenuBar menuBar, @NotNull final Command command, @NotNull PropertyChangeListener propertyChangeListener)
    {
        if (command instanceof CommandGroup)
        {
            CommandGroup commandGroup = (CommandGroup) command;

            Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);
            presentations.add(presentation);

            CommandMenu menu = new CommandMenu(presentation, false, iconKey);
            menuBar.add(menu);

            Command[] commands = commandGroup.getChildren();
            for (Command subCommand : commands)
            {
                addMenuItems(menu, subCommand, propertyChangeListener);
            }
        }
        else if (command instanceof ToggleCommand)
        {
            final Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);
            presentations.add(presentation);

            ToggleCommandMenuItem menuItem = new ToggleCommandMenuItem(presentation, iconKey);
            menuBar.add(menuItem);
            menuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    DataProvider dataProvider = CommandManager.getCurrentCommandProvider();
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
                menuBar.add(customComponent);
            }
            else
            {
                CommandMenuItem menuItem = new CommandMenuItem(presentation, iconKey);
                menuBar.add(menuItem);
                menuItem.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        DataProvider dataProvider = CommandManager.getCurrentCommandProvider();
                        presentation.getCommandApplicationRoot().clearStatusText();
                        command.execute(new DefaultCommandEvent(null, dataProvider.getDataContext(), dataProvider.getPlace(), presentation));
                        CommandManager.dataProviderChanged();
                    }
                });
            }
        }
    }


    private void addMenuItems(@NotNull CommandMenu popupMenu, @NotNull final Command command, @NotNull PropertyChangeListener propertyChangeListener)
    {
        if (command instanceof CommandGroup)
        {
            CommandGroup commandGroup = (CommandGroup) command;
            if (commandGroup.getType() == CommandGroup.Type.POPUP)
            {
                Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
                presentation.addPropertyChangeListener(propertyChangeListener);
                presentations.add(presentation);

                CommandMenu menu = new CommandMenu(presentation, true, iconKey);
                popupMenu.add(menu);

                Command[] commands = commandGroup.getChildren();
                for (Command subCommand : commands)
                {
                    addMenuItems(menu, subCommand, propertyChangeListener);
                }

            }
            else
            {
                Command[] commands = commandGroup.getChildren();
                for (Command c : commands)
                {
                    addMenuItems(popupMenu, c, propertyChangeListener);
                }
            }
        }
        else if (command instanceof SeparatorCommand)
        {
            popupMenu.addSeparator();
        }
        else if (command instanceof ToggleCommand)
        {
            final Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);
            presentations.add(presentation);

            ToggleCommandMenuItem menuItem = new ToggleCommandMenuItem(presentation, iconKey);
            popupMenu.add(menuItem);
            menuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    DataProvider dataProvider = CommandManager.getCurrentCommandProvider();
                    presentation.getCommandApplicationRoot().clearStatusText();
                    command.execute(new DefaultCommandEvent(null, dataProvider.getDataContext(), dataProvider.getPlace(), presentation));
                    CommandManager.dataProviderChanged();
                }
            });
        }
        else
        {
            final Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentations.add(presentation);

            JComponent customComponent = command.getCustomComponent(presentation);
            if (customComponent != null)
            {
                popupMenu.add(customComponent);
            }
            else
            {
                CommandMenuItem menuItem = new CommandMenuItem(presentation, iconKey);
                popupMenu.add(menuItem);
                menuItem.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        DataProvider dataProvider = CommandManager.getCurrentCommandProvider();
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
        ArrayList<Presentation> presentations = new ArrayList<Presentation>(this.presentations);
        for (Presentation presentation : presentations)
        {
            CommandEvent event = new DefaultCommandEvent(null, dataProvider.getDataContext(), dataProvider.getPlace(), presentation);
            presentation.getCommand().update(event);
        }
    }


}
