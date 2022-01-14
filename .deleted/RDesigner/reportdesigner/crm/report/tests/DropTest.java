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
package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

/**
 * User: Martin
 * Date: 13.02.2006
 * Time: 18:05:40
 */
@SuppressWarnings({"ALL"})
public class DropTest
{
    public static void main(@NotNull String[] args)
    {
        JFrame frame = new JFrame("DropTest");//NON-NLS
        JLabel label = new JLabel("blub");//NON-NLS
        frame.getContentPane().add(label, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

        //noinspection UNUSED_SYMBOL
        DropTarget dropTarget = new DropTarget(label, new DropTargetListener()
        {
            public void dragEnter(@NotNull DropTargetDragEvent dtde)
            {
            }


            public void dragOver(@NotNull DropTargetDragEvent dtde)
            {
            }


            public void dropActionChanged(@NotNull DropTargetDragEvent dtde)
            {
            }


            public void dragExit(@NotNull DropTargetEvent dte)
            {
            }


            public void drop(@NotNull DropTargetDropEvent dtde)
            {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                try
                {
                    if (dtde.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                    {
                        java.util.List transferData = (java.util.List) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        for (Object o : transferData)
                        {
                            System.out.println("o = " + o);//NON-NLS
                        }
                    }
                }
                catch (UnsupportedFlavorException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
