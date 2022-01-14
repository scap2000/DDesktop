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

/**
 * User: Martin
 * Date: 13.03.2006
 * Time: 11:21:13
 */
@SuppressWarnings({"ALL"})
public class FlowLayoutTest
{
    public static void main(@NotNull String[] args)
    {
        JFrame frame = new JFrame();
        TestPanel flowLayoutPanel = new TestPanel(new FlowLayout());
        for (int i = 0; i < 10; i++)
        {
            flowLayoutPanel.add(new JLabel("label" + i));//NON-NLS
        }
        frame.getContentPane().add(new JScrollPane(flowLayoutPanel));
        frame.setBounds(100, 100, 400, 400);
        frame.setVisible(true);
    }


    private static class TestPanel extends JPanel implements Scrollable
    {
        public TestPanel(@NotNull LayoutManager layout)
        {
            super(layout);
        }


        public Dimension getPreferredScrollableViewportSize()
        {
            return getPreferredSize();
        }


        public int getScrollableUnitIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
        {
            return 0;
        }


        public int getScrollableBlockIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
        {
            return 0;
        }


        public boolean getScrollableTracksViewportWidth()
        {
            return true;
        }


        public boolean getScrollableTracksViewportHeight()
        {
            return true;
        }
    }
}
