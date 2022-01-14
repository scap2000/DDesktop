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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.Category;
import org.pentaho.reportdesigner.lib.client.components.favoritespanel.FavoritesPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * User: Martin
 * Date: 19.11.2004
 * Time: 11:03:30
 */
public class CommandManager
{

    @NotNull
    private static final CommandRegistryManager commandRegistryManager = new CommandRegistryManager();

    @NotNull
    private static Component lastFocusedComponent;

    @NotNull
    private static final TaskCoalescHelper taskCoalescHelper = new TaskCoalescHelper(50, true);

    private static int requestedRefreshes;
    private static int executedRefreshes;
    private static long usedRefreshNanos;

    @NotNull
    private static final String PERMANENT_FOCUS_OWNER = "permanentFocusOwner";


    private CommandManager()
    {
    }


    public static void addCommandToGroup(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull @NonNls String place, @NotNull Command command, @NotNull CommandConstraint commandConstraint)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);
        defaultCommandGroup.addChild(command, commandConstraint);
    }


    public static void registerCommandGroup(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull @NonNls String place, @NotNull DefaultCommandGroup defaultCommandGroup)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        applicationCommandRegistry.registerDefaultCommandGroup(place, defaultCommandGroup);
    }


    public static void removeCommandFromGroup(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull @NonNls String place, @NotNull Command command)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);
        defaultCommandGroup.removeChild(command);
    }


    @NotNull
    public static DefaultCommandGroup getCommandGroup(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull @NonNls String place)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);
        return defaultCommandGroup;
    }


    public static void initAWTListeners()
    {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if (PERMANENT_FOCUS_OWNER.equals(evt.getPropertyName()))
                {
                    lastFocusedComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
                    dataProviderChanged();
                }
            }
        });
    }


    @NotNull
    public static DataProvider getCurrentCommandProvider()
    {
        if (CommandManager.lastFocusedComponent != null)
        {
            Component cc = CommandManager.lastFocusedComponent;

            while (cc != null)
            {
                if (cc instanceof DataProvider)
                {
                    return (DataProvider) cc;
                }
                cc = cc.getParent();
            }
        }

        return new NullDataProvider();
    }


    //ToolBars
    @NotNull
    private static final WeakList<CommandToolBar> commandToolBars = new WeakList<CommandToolBar>();


    @NotNull
    public static CommandToolBar createCommandToolBar(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull DataProvider dataProvider, @NotNull @NonNls String place)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);
        return createCommandToolBar(commandApplicationRoot, dataProvider, defaultCommandGroup);
    }


    @NotNull
    public static CommandToolBar createCommandToolBar(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull @NonNls String place)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);
        return createCommandToolBar(commandApplicationRoot, null, defaultCommandGroup);
    }


    @NotNull
    private static CommandToolBar createCommandToolBar(@NotNull CommandApplicationRoot commandApplicationRoot, @Nullable DataProvider dataProvider, @NotNull Command command)
    {
        CommandToolBar commandToolBar = new CommandToolBar(commandApplicationRoot, dataProvider, command);
        commandToolBars.add(commandToolBar);
        return commandToolBar;
    }


    //MenuBars
    @NotNull
    private static final WeakList<CommandMenuBar> commandMenuBars = new WeakList<CommandMenuBar>();


    @NotNull
    public static CommandMenuBar createCommandMenuBar(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull @NonNls String place)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);
        return createCommandMenuBar(commandApplicationRoot, defaultCommandGroup);
    }


    @NotNull
    private static CommandMenuBar createCommandMenuBar(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull Command command)
    {
        CommandMenuBar commandMenuBar = new CommandMenuBar(commandApplicationRoot, command);
        commandMenuBars.add(commandMenuBar);
        return commandMenuBar;
    }


    //CommandCategories
    @NotNull
    private static final WeakList<CommandCategories> commandCategoriesList = new WeakList<CommandCategories>();


    @NotNull
    public static CommandCategories createCommandCategories(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull @NonNls String place, @NotNull FavoritesPanel favoritesPanel, @NotNull String iconKey)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);

        CommandCategories commandCategories = new CommandCategories(commandApplicationRoot, defaultCommandGroup, favoritesPanel, iconKey);
        commandCategoriesList.add(commandCategories);
        return commandCategories;
    }


    //Popup
    @NotNull
    public static CommandPopupMenu createCommandPopupMenu(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull @NonNls String place, @NotNull String iconKey)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);
        return createCommandPopupMenu(commandApplicationRoot, defaultCommandGroup, iconKey);
    }


    @NotNull
    public static CommandPopupMenu createCommandPopupMenu(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull Command command, @NotNull String iconKey)
    {
        //a popup menu is temporary, commandproviders should not change while a popup is visible
        return new CommandPopupMenu(commandApplicationRoot, command, iconKey);
    }


    @NotNull
    private static final WeakList<CommandButton> commandButtons = new WeakList<CommandButton>();


    @NotNull
    public static JButton createCommandButtonContextual(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull Command command, @NotNull String iconKey, @NotNull ButtonType buttonType)
    {
        CommandButton commandButton = new CommandButton(commandApplicationRoot, null, command, iconKey, buttonType);
        commandButtons.add(commandButton);
        return commandButton;
    }


    @NotNull
    public static JButton createCommandButton(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull DataProvider dataProvider, @NotNull Command command, @NotNull String iconKey, @NotNull ButtonType buttonType)
    {
        CommandButton commandButton = new CommandButton(commandApplicationRoot, dataProvider, command, iconKey, buttonType);
        commandButtons.add(commandButton);
        return commandButton;
    }


    //ActionComponentsCommandHelper
    @NotNull
    private static final WeakList<ActionComponentsCommandHelper> commandActionComponentsCommandHelperList = new WeakList<ActionComponentsCommandHelper>();


    @NotNull
    public static ActionComponentsCommandHelper createActionComponentsCommandHelperContextual(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull @NonNls String place, @NotNull Category category, @NotNull String iconKey)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);

        ActionComponentsCommandHelper commandCategories = new ActionComponentsCommandHelper(commandApplicationRoot, null, defaultCommandGroup, category, iconKey);
        commandActionComponentsCommandHelperList.add(commandCategories);
        return commandCategories;
    }


    @NotNull
    public static ActionComponentsCommandHelper createActionComponentsCommandHelper(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull DataProvider dataProvider, @NotNull @NonNls String place, @NotNull Category category, @NotNull String iconKey)
    {
        ApplicationCommandRegistry applicationCommandRegistry = commandRegistryManager.getApplicationCommandRegistry(commandApplicationRoot.getApplicationID());
        DefaultCommandGroup defaultCommandGroup = applicationCommandRegistry.getDefaultCommandGroup(place);

        ActionComponentsCommandHelper commandCategories = new ActionComponentsCommandHelper(commandApplicationRoot, dataProvider, defaultCommandGroup, category, iconKey);
        commandActionComponentsCommandHelperList.add(commandCategories);
        return commandCategories;
    }


    /**
     * Use when a selection within a light weight component like JTree, JList, JTable changes, and focus does not change.
     * It is safe to call this method whenever/as many times as you like.
     * Refresh requests are coalesced to prevent performance issues.
     */
    public static void dataProviderChanged()
    {
        requestedRefreshes++;
        taskCoalescHelper.addTask(new TaskCoalescHelper.CoalesceableTask()
        {
            public void run()
            {
                refresh();
            }
        });
    }


    private static void refresh()
    {
        long l1 = System.nanoTime();
        DataProvider dataProvider = getCurrentCommandProvider();

        //update toolbars
        for (CommandToolBar commandToolBar : commandToolBars)
        {
            commandToolBar.update(dataProvider);
        }

        //update menubars
        for (CommandMenuBar commandMenuBar : commandMenuBars)
        {
            commandMenuBar.update(dataProvider);
        }

        //update commandCategories
        for (CommandCategories commandCategories : commandCategoriesList)
        {
            commandCategories.update(dataProvider);
        }

        for (CommandButton commandButton : commandButtons)
        {
            commandButton.update(dataProvider);
        }

        for (ActionComponentsCommandHelper actionComponentsCommandHelper : commandActionComponentsCommandHelperList)
        {
            actionComponentsCommandHelper.update(dataProvider);
        }
        long l2 = System.nanoTime();

        usedRefreshNanos += l2 - l1;
        executedRefreshes++;
    }


    public static int getRequestedRefreshes()
    {
        return requestedRefreshes;
    }


    public static int getExecutedRefreshes()
    {
        return executedRefreshes;
    }


    public static long getUsedRefreshNanos()
    {
        return usedRefreshNanos;
    }


    public static void resetStatistics()
    {
        requestedRefreshes = 0;
        executedRefreshes = 0;
        usedRefreshNanos = 0;
    }
}
