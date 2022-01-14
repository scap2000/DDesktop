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
package org.pentaho.reportdesigner.lib.client.undo;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 24.01.2006
 * Time: 11:01:55
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr"})
public class Undo
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(Undo.class.getName());

    private static final int MAXIMUM_UNDO_SIZE = 10000;//to ensure not to be completely unbound, prevents OOME


    public static void main(@NotNull String[] args)
    {
        Undo undo = new Undo();

        undo.startTransaction("1");
        undo.add(new UndoEntry()
        {
            public void undo()
            {
                System.out.println("undo 1");//NON-NLS
            }


            public void redo()
            {
                System.out.println("redo 1");//NON-NLS
            }
        });
        undo.endTransaction();

        undo.startTransaction("2");
        undo.add(new UndoEntry()
        {
            public void undo()
            {
                System.out.println("undo 2");//NON-NLS
            }


            public void redo()
            {
                System.out.println("redo 2");//NON-NLS
            }
        });
        undo.endTransaction();

        undo.startTransaction("3");
        undo.add(new UndoEntry()
        {
            public void undo()
            {
                System.out.println("undo 3");//NON-NLS
            }


            public void redo()
            {
                System.out.println("redo 3");//NON-NLS
            }
        });
        undo.endTransaction();

        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS
        undo.undo();
        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS
        undo.undo();
        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS
        undo.undo();
        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS

        undo.redo();
        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS
        undo.redo();
        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS
        undo.redo();
        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS

        undo.undo();
        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS
        undo.undo();
        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS
        undo.undo();
        System.out.println("undo2.isUndoable() = " + undo.isUndoable());//NON-NLS
        System.out.println("undo2.isRedoable() = " + undo.isRedoable());//NON-NLS
    }


    @NotNull
    private ArrayList<UndoEntry> undos;
    private int index;

    @NotNull
    private LinkedHashSet<UndoListener> undoListeners;

    private boolean startCommand;
    private boolean inProgress;

    @NotNull
    private String transactionName;
    private int transactionCount;


    public Undo()
    {
        undoListeners = new LinkedHashSet<UndoListener>();
        undos = new ArrayList<UndoEntry>();

        inProgress = false;
        startCommand = true;
        index = -1;

        transactionCount = -1;
    }


    public void startTransaction(@NotNull String transactionName)
    {
        this.transactionName = transactionName;
        transactionCount++;
    }


    public void endTransaction()
    {
        transactionCount--;
        if (transactionCount == -1)
        {
            startCommand = true;

            removeOldEntries();
        }
    }


    public boolean isInProgress()
    {
        return inProgress;
    }


    public boolean isUndoable()
    {
        return index >= 0;
    }


    public boolean isRedoable()
    {
        return index < undos.size() - 1 || (index == -1 && !undos.isEmpty());
    }


    public void add(@NotNull UndoEntry e)
    {
        //remove all undos after index
        for (int i = undos.size() - 1; i >= index + 1; i--)
        {
            undos.remove(i);
        }
        if (transactionCount == -1)
        {
            throw new RuntimeException("missed to start a transaction!");
        }

        e.transactionName = transactionName;
        e.startCommand = startCommand;
        startCommand = false;
        undos.add(e);
        index++;

        fireStateChanged();
    }


    private void removeOldEntries()
    {
        while (undos.size() > MAXIMUM_UNDO_SIZE)
        {
            removeNextTransaction();
        }
    }


    private void removeNextTransaction()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "Undo.removeNextTransaction ");

        UndoEntry undoEntry = undos.remove(0);
        if (!undoEntry.startCommand)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "Undo.removeOldEntries was not a startCommand! eek!");
        }
        int removed = 1;
        //remove complete transaction
        while (!undos.isEmpty() && !undos.get(0).startCommand)
        {
            undos.remove(0);
            removed++;
        }
        if (removed > 0)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "Undo.removeOldEntries removed = " + removed);
        }
        index -= removed;
    }


    public void undo()
    {
        inProgress = true;

        for (int i = index; i >= 0; i--)
        {
            index--;
            if (i >= 0)
            {
                UndoEntry undoEntry = undos.get(i);
                undoEntry.undo();
                if (undoEntry.startCommand)
                {
                    break;
                }
            }
        }

        inProgress = false;
        fireStateChanged();
    }


    public void redo()
    {
        inProgress = true;

        for (int i = index + 1; i < undos.size(); i++)
        {
            index++;
            UndoEntry undoEntry = undos.get(i);
            undoEntry.redo();
            if (undos.size() < i + 2 || undos.get(i + 1).startCommand)
            {
                break;
            }
        }

        inProgress = false;
        fireStateChanged();
    }


    public void addUndoListener(@NotNull UndoListener undoListener)
    {
        undoListeners.add(undoListener);
    }


    public void removeUndoListener(@NotNull UndoListener undoListener)
    {
        undoListeners.remove(undoListener);
    }


    public void fireStateChanged()
    {
        ArrayList<UndoListener> undoListeners = new ArrayList<UndoListener>(this.undoListeners);
        for (UndoListener listener : undoListeners)
        {
            listener.stateChanged();
        }
    }


    public void debugPrint()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "UNDO Infos");
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "==========");
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "transactionCount = " + transactionCount);
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "transactionName = " + transactionName);
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "index = " + index);
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "undos.size() = " + undos.size());
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "undos = " + undos);
    }


}

