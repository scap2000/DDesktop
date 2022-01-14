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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 21.11.2004
 * Time: 11:23:59
 */
public class CommandPopupMenu implements CommandComponent
{
    @NotNull
    private Command command;
    @NotNull
    private CommandApplicationRoot commandApplicationRoot;
    @NotNull
    private String iconKey;

    @NotNull
    private ArrayList<Presentation> presentations;


    public CommandPopupMenu(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull Command command, @NotNull String iconKey)
    {
        this.commandApplicationRoot = commandApplicationRoot;
        this.iconKey = iconKey;
        //noinspection ConstantConditions
        if (command == null)
        {
            throw new IllegalArgumentException("command must not be null");
        }

        this.command = command;
        presentations = new ArrayList<Presentation>();
    }


    public void showPopupMenu(@NotNull MouseEvent me)
    {
        getJPopupMenu().show(me.getComponent(), me.getX(), me.getY());
    }


    @NotNull
    public JPopupMenu getJPopupMenu()
    {
        JPopupMenu popupMenu = new MyJPopupMenu(this);

        addMenuItems(popupMenu, command, true);

        return popupMenu;
    }


    private void addMenuItems(@NotNull JPopupMenu popupMenu, @NotNull final Command command, boolean first)
    {
        if (command instanceof CommandGroup)
        {
            CommandGroup commandGroup = (CommandGroup) command;
            if (!first && commandGroup.getType() == CommandGroup.Type.POPUP)
            {
                Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
                presentations.add(presentation);

                CommandMenu menu = new CommandMenu(presentation, true, iconKey);
                popupMenu.add(menu);

                Command[] commands = commandGroup.getChildren();
                for (Command subCommand : commands)
                {
                    addMenuItems(menu, subCommand);
                }
            }
            else
            {
                Command[] commands = commandGroup.getChildren();
                for (Command c : commands)
                {
                    addMenuItems(popupMenu, c, false);
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


    private void addMenuItems(@NotNull CommandMenu popupMenu, @NotNull final Command command)
    {
        if (command instanceof CommandGroup)
        {
            CommandGroup commandGroup = (CommandGroup) command;
            if (commandGroup.getType() == CommandGroup.Type.POPUP)
            {
                Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
                presentations.add(presentation);

                CommandMenu menu = new CommandMenu(presentation, true, iconKey);
                popupMenu.add(menu);

                Command[] commands = commandGroup.getChildren();
                for (Command subCommand : commands)
                {
                    addMenuItems(menu, subCommand);
                }
            }
            else
            {
                Command[] commands = commandGroup.getChildren();
                for (Command c : commands)
                {
                    addMenuItems(popupMenu, c);
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


    private void update(@NotNull DataProvider dataProvider)
    {
        for (Presentation presentation : presentations)
        {
            CommandEvent event = new DefaultCommandEvent(null, dataProvider.getDataContext(), dataProvider.getPlace(), presentation);
            presentation.getCommand().update(event);
        }
    }


    private static class MyJPopupMenu extends JPopupMenu implements CommandComponent
    {
        @NotNull
        private CommandPopupMenu outer;


        private MyJPopupMenu(@NotNull CommandPopupMenu outer)
        {
            this.outer = outer;
        }


        public void show(@NotNull Component invoker, int x, int y)
        {
            outer.update(CommandManager.getCurrentCommandProvider());
            super.show(invoker, x, y);
        }
    }
}
