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

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * User: Martin
 * Date: 21.11.2004
 * Time: 10:59:24
 */
public class DefaultCommandGroup implements CommandGroup
{
    @NotNull
    private static final Command[] EMPTY_ARRAY = new Command[0];

    @NotNull
    private TemplatePresentation templatePresentation;

    @NotNull
    private Command[] children;

    @NotNull
    private Type type;
    @NotNull
    private String name;


    public DefaultCommandGroup(@NotNull String name)
    {
        this.name = name;

        templatePresentation = new TemplatePresentation(this, "DefaultCommandGroup", "DefaultCommandGroup", false, true);//NON-NLS
        templatePresentation.setIcon(CommandSettings.SIZE_16, new ImageIcon(AbstractCommand.class.getResource("Empty16.png")));//NON-NLS
        templatePresentation.setIcon(CommandSettings.SIZE_32, new ImageIcon(AbstractCommand.class.getResource("Empty32.png")));//NON-NLS
        templatePresentation.setIcon(CommandSettings.SIZE_64, new ImageIcon(AbstractCommand.class.getResource("Empty64.png")));//NON-NLS
        children = EMPTY_ARRAY;
        type = Type.NORMAL;
    }


    public DefaultCommandGroup(@NotNull String text, @NotNull String description, @NotNull @NonNls String name, boolean enabled)
    {
        this.name = name;

        templatePresentation = new TemplatePresentation(this, text, description, enabled, true);
        templatePresentation.setIcon(CommandSettings.SIZE_16, new ImageIcon(AbstractCommand.class.getResource("Empty16.png")));//NON-NLS
        templatePresentation.setIcon(CommandSettings.SIZE_32, new ImageIcon(AbstractCommand.class.getResource("Empty32.png")));//NON-NLS
        templatePresentation.setIcon(CommandSettings.SIZE_64, new ImageIcon(AbstractCommand.class.getResource("Empty64.png")));//NON-NLS
        children = EMPTY_ARRAY;
        type = Type.NORMAL;
    }


    public void addChild(@NotNull Command child, @Nullable CommandConstraint commandConstraint)
    {
        ArrayList<Command> commands = new ArrayList<Command>(Arrays.asList(children));

        if (!commands.contains(child))
        {
            if (commandConstraint == null || CommandConstraint.LAST.equals(commandConstraint))
            {
                commands.add(child);
            }
            else if (CommandConstraint.FIRST.equals(commandConstraint))
            {
                commands.add(0, child);
            }
            else if (commandConstraint.getConstraintType() == CommandConstraint.ConstraintType.AFTER)
            {
                int index = getIndexOfCommand(commandConstraint.getReference(), commands);
                if (index == -1)
                {
                    //we did not find the command --> add as last
                    commands.add(child);
                }
                else
                {
                    commands.add(index + 1, child);
                }
            }
            else if (commandConstraint.getConstraintType() == CommandConstraint.ConstraintType.BEFORE)
            {
                int index = getIndexOfCommand(commandConstraint.getReference(), commands);
                if (index == -1)
                {
                    //we did not find the command --> add as last
                    commands.add(child);
                }
                else
                {
                    commands.add(index, child);
                }
            }
            else
            {
                commands.add(child);//nothing that makes sense->add as last command
            }
        }

        children = commands.toArray(new Command[commands.size()]);

        for (Presentation presentation : templatePresentation.getCreatedPresentations())
        {
            presentation.fireStructureChanged();
        }
    }


    private int getIndexOfCommand(@NotNull String reference, @NotNull ArrayList<Command> commands)
    {
        for (int i = 0; i < commands.size(); i++)
        {
            Command command = commands.get(i);
            if (command.getName().equals(reference))
            {
                return i;
            }
        }
        return -1;
    }


    public void removeChild(@NotNull Command child)
    {
        ArrayList<Command> commands = new ArrayList<Command>(Arrays.asList(children));
        commands.remove(child);

        children = commands.toArray(new Command[commands.size()]);

        for (Presentation presentation : templatePresentation.getCreatedPresentations())
        {
            presentation.fireStructureChanged();
        }
    }


    public void removeAllChildren()
    {
        children = EMPTY_ARRAY;

        for (Presentation presentation : templatePresentation.getCreatedPresentations())
        {
            presentation.fireStructureChanged();
        }
    }


    public void setChildren(@NotNull Command[] commands)
    {
        children = commands;

        for (Presentation presentation : templatePresentation.getCreatedPresentations())
        {
            presentation.fireStructureChanged();
        }
    }


    @NotNull
    public Command[] getChildren()
    {
        return children;
    }


    public void setType(@NotNull Type type)
    {
        this.type = type;
    }


    @NotNull
    public Type getType()
    {
        return type;
    }


    public void update(@NotNull CommandEvent event)
    {
    }


    @NotNull
    public String getName()
    {
        return name;
    }


    public void execute(@NotNull CommandEvent event)
    {
    }


    @NotNull
    public TemplatePresentation getTemplatePresentation()
    {
        return templatePresentation;
    }


    @Nullable
    public JComponent getCustomComponent(@NotNull Presentation presentation)
    {
        return null;
    }
}
