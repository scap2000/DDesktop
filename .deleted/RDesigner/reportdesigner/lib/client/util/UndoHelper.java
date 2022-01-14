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
package org.pentaho.reportdesigner.lib.client.util;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;
import java.awt.event.ActionEvent;

/**
 * User: Martin
 * Date: 24.01.2006
 * Time: 09:23:10
 */
public class UndoHelper
{
    @NotNull
    private static final String UNDO = "Undo";
    @NotNull
    private static final String REDO = "Redo";


    private UndoHelper()
    {
    }


    public static void installUndoSupport(@NotNull JTextComponent jTextComponent)
    {
        final UndoManager undo = new UndoManager();
        Document doc = jTextComponent.getDocument();

        UndoableEditSupport undoableEditSupport = new UndoableEditSupport();
        undoableEditSupport.addUndoableEditListener(undo);
        undoableEditSupport.beginUpdate();

        // Listen for undo and redo events
        doc.addUndoableEditListener(new UndoableEditListener()
        {
            public void undoableEditHappened(@NotNull UndoableEditEvent evt)
            {
                undo.addEdit(evt.getEdit());
            }
        });

        // Create an undo action and add it to the text component
        jTextComponent.getActionMap().put(UNDO,
                                          new AbstractAction(UNDO)
                                          {
                                              public void actionPerformed(@NotNull ActionEvent evt)
                                              {
                                                  try
                                                  {
                                                      if (undo.canUndo())
                                                      {
                                                          undo.undo();
                                                      }
                                                  }
                                                  catch (CannotUndoException e)
                                                  {
                                                      UncaughtExcpetionsModel.getInstance().addException(e);
                                                  }
                                              }
                                          });

        // Bind the undo action to ctl-Z
        jTextComponent.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "UndoHelper.Undo.Accelerator")), UNDO);

        // Create a redo action and add it to the text component
        jTextComponent.getActionMap().put(REDO,
                                          new AbstractAction(REDO)
                                          {
                                              public void actionPerformed(@NotNull ActionEvent evt)
                                              {
                                                  try
                                                  {
                                                      if (undo.canRedo())
                                                      {
                                                          undo.redo();
                                                      }
                                                  }
                                                  catch (CannotRedoException e)
                                                  {
                                                      UncaughtExcpetionsModel.getInstance().addException(e);
                                                  }
                                              }
                                          });

        // Bind the redo action to ctl-Y
        jTextComponent.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "UndoHelper.Redo.Accelerator")), REDO);
    }
}
