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

import java.util.ArrayList;
import java.util.Iterator;


/**
 * User: Martin
 * Date: 23.11.2004
 * Time: 20:04:53
 */
public class TemplatePresentation extends Presentation
{
    @NotNull
    private WeakList<Presentation> createdPresentations;


    public TemplatePresentation(@NotNull Command command, @NotNull String text, @NotNull String description, boolean enabled, boolean visible)
    {
        super(command, text, description, enabled, visible, null, '\0', -1);
        createdPresentations = new WeakList<Presentation>();
    }


    @NotNull
    public Presentation getCopy(@NotNull CommandApplicationRoot commandApplicationRoot)
    {
        Presentation presentation = new Presentation(getCommand(), getText(), getDescription(), isEnabled(), isVisible(), getAccelerator(), getMnemonic(), getDisplayedMnemonicIndex());
        presentation.setIcons(getIcons());
        presentation.setCommandApplicationRoot(commandApplicationRoot);
        createdPresentations.add(presentation);
        return presentation;
    }


    @NotNull
    public ArrayList<Presentation> getCreatedPresentations()
    {
        Iterator<Presentation> iterator = createdPresentations.iterator();
        ArrayList<Presentation> presentations = new ArrayList<Presentation>();
        while (iterator.hasNext())
        {
            Presentation presentation = iterator.next();
            presentations.add(presentation);
        }

        return presentations;
    }


}
