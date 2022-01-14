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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Martin
 * Date: 03.03.2006
 * Time: 16:48:34
 */
@SuppressWarnings({"ALL"})
public class ThreadExitTest
{
    public static void main(@NotNull String[] args)
    {
        final JFrame frame = new JFrame();
        JButton button = new JButton("dispose");
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                frame.dispose();
            }
        });

        frame.getContentPane().add(button, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        Timer timer = new Timer(100, new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                System.out.println("ThreadExitTest.actionPerformed");
            }
        });

        timer.setRepeats(true);
        timer.start();

        //Thread t = new Thread(new Runnable()
        //{
        //    public void run()
        //    {
        //        while(true)
        //        {
        //            try
        //            {
        //                synchronized(this)
        //                {
        //                    wait(1000);
        //                }
        //                //Thread.sleep(1000);
        //                System.out.println("ThreadExitTest.run");
        //            }
        //            catch (InterruptedException e)
        //            {
        //                e.printStackTrace();
        //            }
        //        }
        //    }
        //});
        //
        //t.setDaemon(true);
        //t.start();
    }
}
