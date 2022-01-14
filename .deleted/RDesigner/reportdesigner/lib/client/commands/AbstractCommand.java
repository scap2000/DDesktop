/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is the Pentaho BI Platform.
 *
 * The Initial Developer of the Original Code is Pentaho Corporation
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s): Martin Schmgridvision engineering GmbH
 */
package org.pentaho.reportdesigner.lib.client.commands;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * User: Martin
 * Date: 21.11.2004
 * Time: 10:47:22
 */
public abstract class AbstractCommand implements Command
{
    @NotNull
    private TemplatePresentation templatePresentation;
    @NotNull
    private String name;


    protected AbstractCommand(@NotNull @NonNls String name)
    {
        this.name = name;

        templatePresentation = new TemplatePresentation(this, "", "", false, true);

        //ensure that an icon is set for the most common cases, this makes the "bad looking" look and feels a bit better. metal/ocean indents menus in an ugly way.
        templatePresentation.setIcon(CommandSettings.SIZE_16, new ImageIcon(AbstractCommand.class.getResource("Empty16.png")));//NON-NLS
        templatePresentation.setIcon(CommandSettings.SIZE_32, new ImageIcon(AbstractCommand.class.getResource("Empty32.png")));//NON-NLS
        templatePresentation.setIcon(CommandSettings.SIZE_64, new ImageIcon(AbstractCommand.class.getResource("Empty64.png")));//NON-NLS
    }


    @NotNull
    public TemplatePresentation getTemplatePresentation()
    {
        return templatePresentation;
    }


    @NotNull
    public String getName()
    {
        return name;
    }


    @Nullable
    public JComponent getCustomComponent(@NotNull Presentation presentation)
    {
        return null;
    }

}
