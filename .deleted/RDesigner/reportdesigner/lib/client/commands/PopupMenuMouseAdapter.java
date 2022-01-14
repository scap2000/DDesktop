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

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Martin
 * Date: 21.11.2004
 * Time: 12:33:12
 */
public class PopupMenuMouseAdapter extends MouseAdapter
{
    @NotNull
    private CommandApplicationRoot commandApplicationRoot;
    @NotNull
    private DataProvider dataProvider;
    @NotNull
    private String iconKey;


    public PopupMenuMouseAdapter(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull DataProvider dataProvider, @NotNull String iconKey)
    {
        this.commandApplicationRoot = commandApplicationRoot;
        this.dataProvider = dataProvider;
        this.iconKey = iconKey;
    }


    public void mouseClicked(@NotNull MouseEvent e)
    {
        popup(e);
    }


    public void mousePressed(@NotNull MouseEvent e)
    {
        popup(e);
    }


    public void mouseReleased(@NotNull MouseEvent e)
    {
        popup(e);
    }


    private void popup(@NotNull final MouseEvent me)
    {
        if (me.isPopupTrigger())
        {
            dataProvider.requestFocus();

            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    CommandManager.createCommandPopupMenu(commandApplicationRoot, dataProvider.getPlace(), iconKey).showPopupMenu(me);
                }
            });
        }
    }

}
