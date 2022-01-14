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
import org.pentaho.reportdesigner.lib.client.components.Category;
import org.pentaho.reportdesigner.lib.client.components.favoritespanel.FavoritesPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 21.11.2004
 * Time: 13:15:56
 */
public class CommandCategories implements CommandComponent
{
    @NotNull
    private Command command;
    @NotNull
    private CommandApplicationRoot commandApplicationRoot;
    @NotNull
    private FavoritesPanel favoritesPanel;
    @NotNull
    private String iconKey;

    @NotNull
    private ArrayList<Presentation> presentations;


    public CommandCategories(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull Command command, @NotNull FavoritesPanel favoritesPanel, @NotNull String iconKey)
    {
        this.commandApplicationRoot = commandApplicationRoot;
        this.favoritesPanel = favoritesPanel;
        this.iconKey = iconKey;
        //noinspection ConstantConditions
        if (command == null)
        {
            throw new IllegalArgumentException("command must not be null");
        }

        this.command = command;
        presentations = new ArrayList<Presentation>();

        rebuildFavoritesPanel();
    }


    private void rebuildFavoritesPanel()
    {
        for (Presentation presentation : presentations)
        {
            presentation.clearPropertyChangeListeners();
        }

        presentations.clear();

        ArrayList<Category> categories = new ArrayList<Category>();

        addCategories(categories, command, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if ("structure".equals(evt.getPropertyName()))//NON-NLS
                {
                    rebuildFavoritesPanel();
                }
            }
        });

        Category[] cs = categories.toArray(new Category[categories.size()]);
        favoritesPanel.setCategories(cs);

        favoritesPanel.revalidate();
        favoritesPanel.repaint();
    }


    private void addCategories(@NotNull ArrayList<Category> categories, @NotNull final Command command, @NotNull PropertyChangeListener propertyChangeListener)
    {
        if (command instanceof CommandGroup)
        {
            CommandGroup commandGroup = (CommandGroup) command;
            final Presentation presentation = commandGroup.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);

            presentations.add(presentation);

            Command[] commands = commandGroup.getChildren();
            for (Command c : commands)
            {
                addCategories(categories, c, propertyChangeListener);
            }
        }
        else if (command instanceof SeparatorCommand)
        {
            throw new IllegalArgumentException("CommandCategories can not contain Separators");
        }
        else if (command instanceof ToggleCommand)
        {
            throw new IllegalArgumentException("CommandCategories can not contain ToggleCommands");
        }
        else
        {
            final Presentation presentation = command.getTemplatePresentation().getCopy(commandApplicationRoot);
            presentation.addPropertyChangeListener(propertyChangeListener);
            presentations.add(presentation);

            final Category<JComponent> category = new Category<JComponent>(command.getName(), presentation.getIcon(iconKey), presentation.getIcon(iconKey), presentation.getText(), command.getCustomComponent(presentation));
            if (command instanceof AbstractToolbarDelivererCommand)
            {
                AbstractToolbarDelivererCommand abstractToolbarDelivererCommand = (AbstractToolbarDelivererCommand) command;
                CommandToolBar commandToolBar = CommandManager.createCommandToolBar(commandApplicationRoot, abstractToolbarDelivererCommand.getToolbarPlace());
                category.setToolBar(commandToolBar.getToolBar());
            }

            categories.add(category);
            category.addPropertyChangeListener("lastAccessedMillis", new PropertyChangeListener()//NON-NLS
            {
                public void propertyChange(@NotNull PropertyChangeEvent evt)
                {
                    DataProvider dataProvider = CommandManager.getCurrentCommandProvider();
                    presentation.getCommandApplicationRoot().clearStatusText();
                    command.execute(new DefaultCommandEvent(null, dataProvider.getDataContext(), dataProvider.getPlace(), presentation));
                    CommandManager.dataProviderChanged();
                }
            });

            presentation.addPropertyChangeListener(new PropertyChangeListener()
            {
                public void propertyChange(@NotNull PropertyChangeEvent evt)
                {
                    category.setTitle(presentation.getText());
                    category.setIconBig(presentation.getIcon(iconKey));
                    category.setIconSmall(presentation.getIcon(iconKey));
                    category.setMainComponent(command.getCustomComponent(presentation));
                }
            });

        }
    }


    public void update(@NotNull DataProvider dataProvider)
    {
        ArrayList<Presentation> presentations = new ArrayList<Presentation>(this.presentations);
        for (Presentation commandPresentation : presentations)
        {
            CommandEvent event = new DefaultCommandEvent(null, dataProvider.getDataContext(), dataProvider.getPlace(), commandPresentation);
            commandPresentation.getCommand().update(event);
        }
    }


}
