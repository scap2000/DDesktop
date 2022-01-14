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
package org.pentaho.reportdesigner.crm.report;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 20:55:17
 */
public class GrabberComponent extends JComponent
{
    private boolean mouseIsPressed;
    private boolean mouseInside;


    public GrabberComponent()
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));

        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull MouseEvent e)
            {
                mouseIsPressed = true;
                repaint();
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                mouseIsPressed = false;
                repaint();
            }


            public void mouseEntered(@NotNull MouseEvent e)
            {
                mouseInside = true;
                repaint();
            }


            public void mouseExited(@NotNull MouseEvent e)
            {
                mouseInside = false;
                repaint();
            }
        });

    }


    protected void paintComponent(@NotNull Graphics g)
    {
        super.paintComponent(g);
        if (mouseInside || mouseIsPressed)
        {
            g.setColor(new Color(183, 203, 255));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(132, 166, 255));
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        else
        {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }

    }


}
