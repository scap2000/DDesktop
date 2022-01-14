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
package org.pentaho.reportdesigner.lib.client.components;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Martin
 * Date: 23.02.2006
 * Time: 13:19:01
 */
public class TextComponentHelper
{
    private TextComponentHelper()
    {
    }


    public static void installDefaultPopupMenu(@NotNull final JTextComponent textComponent)
    {
        textComponent.addMouseListener(new MouseAdapter()
        {
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
                    JPopupMenu popupMenu = new JPopupMenu();
                    AbstractAction cutAction = new AbstractAction(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "CutCommand.Text"))
                    {
                        public void actionPerformed(@NotNull ActionEvent e)
                        {
                            textComponent.cut();
                        }
                    };
                    popupMenu.add(cutAction);
                    AbstractAction copyAction = new AbstractAction(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "CopyCommand.Text"))
                    {
                        public void actionPerformed(@NotNull ActionEvent e)
                        {
                            textComponent.copy();
                        }
                    };
                    popupMenu.add(copyAction);
                    AbstractAction pasteAction = new AbstractAction(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "PasteCommand.Text"))
                    {
                        public void actionPerformed(@NotNull ActionEvent e)
                        {
                            textComponent.paste();
                        }
                    };
                    popupMenu.add(pasteAction);

                    cutAction.setEnabled(textComponent.isEditable());
                    pasteAction.setEnabled(textComponent.isEditable());

                    popupMenu.show(textComponent, me.getX(), me.getY());
                }
            }
        });
    }
}
