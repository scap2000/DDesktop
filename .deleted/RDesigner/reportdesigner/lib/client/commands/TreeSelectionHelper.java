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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: smarty
 * Date: 10.10.2005
 * Time: 13:46:22
 */
public class TreeSelectionHelper
{
    private TreeSelectionHelper()
    {
    }


    public static void register(@NotNull CommandApplicationRoot commandApplicationRoot, @NotNull DataProvider dataProvider, @NotNull final JTree tree)
    {
        tree.addTreeSelectionListener(new TreeSelectionListener()
        {
            public void valueChanged(@NotNull TreeSelectionEvent e)
            {
                CommandManager.dataProviderChanged();
            }
        });

        tree.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull final MouseEvent me)
            {
                if (me.getButton() != MouseEvent.BUTTON1)
                {
                    TreePath[] treePaths = tree.getSelectionPaths();
                    TreePath treePath = tree.getPathForLocation(me.getX(), me.getY());
                    if (treePath != null && !contains(treePaths, treePath))
                    {
                        tree.setSelectionPath(treePath);
                    }
                }
            }


            private boolean contains(@Nullable TreePath[] treePaths, @Nullable TreePath treePath)
            {
                if (treePaths != null && treePath != null)
                {
                    for (TreePath path : treePaths)
                    {
                        if (path.equals(treePath))
                        {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        tree.addMouseListener(new PopupMenuMouseAdapter(commandApplicationRoot, dataProvider, CommandSettings.getInstance().getPopupmenuIconKey()));
    }
}
