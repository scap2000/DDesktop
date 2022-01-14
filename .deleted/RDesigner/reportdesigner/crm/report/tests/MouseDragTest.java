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
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * User: Martin
 * Date: 19.03.2006
 * Time: 17:29:16
 */
@SuppressWarnings({"ALL"})
public class MouseDragTest
{
    public static void main(@NotNull String[] args)
    {
        //Object desktopProperty = Toolkit.getDefaultToolkit().getDesktopProperty("win.drag.height");
        //System.out.println("desktopProperty = " + desktopProperty.getClass());
        //
        //Field[] fields = Toolkit.getDefaultToolkit().getClass().getDeclaredFields();
        //for (int i = 0; i < fields.length; i++)
        //{
        //    Field field = fields[i];
        //    System.out.println("field = " + field);
        //}

        //System.setProperty("awt.dnd.drag.threshold", "1");
        int dragThreshold = DragSource.getDragThreshold();
        System.out.println("dragThreshold = " + dragThreshold);

        //Field field = Toolkit.getDefaultToolkit().getClass().getDeclaredField("wprops");
        //field.setAccessible(true);
        //Object obj = field.get(Toolkit.getDefaultToolkit());
        ////o.put("win.drag.height", new Integer(1));
        ////o.put("win.drag.width", new Integer(1));
        //
        //Method[] methods = obj.getClass().getDeclaredMethods();
        //for (int i = 0; i < methods.length; i++)
        //{
        //    Method method = methods[i];
        //    System.out.println("method = " + method);
        //}
        //
        //Method declaredMethod = obj.getClass().getDeclaredMethod("setIntegerProperty", String.class, Integer.TYPE);
        //declaredMethod.setAccessible(true);
        //declaredMethod.invoke(obj, "win.drag.height", new Integer(1));
        //declaredMethod.invoke(obj, "win.drag.width", new Integer(1));

        //Method method = Toolkit.getDefaultToolkit().getClass().getDeclaredMethod("setDesktopProperty", String.class, Object.class);
        //method.invoke(Toolkit.getDefaultToolkit(), "win.drag.height", new Integer(1));
        //method.invoke(Toolkit.getDefaultToolkit(), "win.drag.width", new Integer(1));


        JFrame frame = new JFrame();

        frame.getContentPane().addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull MouseEvent e)
            {
                System.out.println("pressed at (" + e.getX() + ", " + e.getY() + ")");
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
                {
                    System.out.println("true");
                }
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                System.out.println("released at (" + e.getX() + ", " + e.getY() + ")");
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
                {
                    System.out.println("true");
                }
            }


            public void mouseClicked(@NotNull MouseEvent e)
            {
                System.out.println("clicked at (" + e.getX() + ", " + e.getY() + ")");
            }
        });

        frame.getContentPane().addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseDragged(@NotNull MouseEvent e)
            {
                System.out.println("dragged at (" + e.getX() + ", " + e.getY() + ")");
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
                {
                    System.out.println("true");
                }
            }


            public void mouseMoved(@NotNull MouseEvent e)
            {
                System.out.println("moved at (" + e.getX() + ", " + e.getY() + ")");
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
                {
                    System.out.println("true");
                }
            }
        });

        //JTextArea textArea = new JTextArea("sample text\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        //textArea.setDragEnabled(true);
        //frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        //frame.getContentPane().add(new JScrollPane(new JTextArea()), BorderLayout.SOUTH);
        //frame.getContentPane().add(new JSlider(0, 100, 50), BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 200, 200);
        frame.setVisible(true);

        //new ActionListener()
        //{
        //    private JLabel label = new JLabel();
        //    {
        //        {
        //            System.out.println("eeek");
        //        }
        //    }
        //
        //
        //    private JLabel label2 = new JLabel()
        //    {
        //        {
        //            System.out.println("eeek2");
        //        }
        //    };
        //
        //
        //    public void actionPerformed(@NotNull ActionEvent e)
        //    {
        //    }
        //};
    }
}
