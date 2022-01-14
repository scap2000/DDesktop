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
package org.pentaho.reportdesigner.lib.client.components.docking;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Martin
 * Date: 18.03.2005
 * Time: 11:49:05
 */
public class BorderComponent extends JPanel
{
    @NotNull
    private ArrayList<ToolWindow> toolWindows;

    @NotNull
    private JPanel dragPanel;

    @NotNull
    private DockingPane.Split split;
    @NotNull
    private DockingPane.SplitpaneDirection splitpaneDirection;

    private int startX;
    private int startY;


    public BorderComponent(@NotNull final DockingPane.Split split, @NotNull final DockingPane.SplitpaneDirection splitpaneDirection, @NotNull final DockingPane dockingPane)
    {
        this.split = split;
        this.splitpaneDirection = splitpaneDirection;

        setLayout(new BorderLayout());

        toolWindows = new ArrayList<ToolWindow>();

        dragPanel = new JPanel();
        dragPanel.setPreferredSize(new Dimension(5, 5));
        dragPanel.setSize(5, 5);
        dragPanel.setOpaque(true);

        switch (split)
        {
            case TOP:
                add(dragPanel, BorderLayout.SOUTH);
                dragPanel.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                break;
            case BOTTOM:
                add(dragPanel, BorderLayout.NORTH);
                dragPanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                break;
            case LEFT:
                add(dragPanel, BorderLayout.EAST);
                dragPanel.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                break;
            case RIGHT:
                add(dragPanel, BorderLayout.WEST);
                dragPanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                break;
        }

        dragPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));

        final Color origColor = dragPanel.getBackground();

        dragPanel.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull MouseEvent e)
            {
                startX = e.getX();
                startY = e.getY();
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                dragPanel.setBackground(origColor);
            }
        });

        dragPanel.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(@NotNull MouseEvent e)
            {
                if (split == DockingPane.Split.LEFT)
                {
                    int x = e.getX();
                    int xDiff = startX - x;
                    for (ToolWindow toolWindow1 : toolWindows)
                    {
                        int w = getWidth() - xDiff;
                        ToolWindow toolWindow = toolWindow1;
                        Dimension s = toolWindow.getSize();
                        int maxSize = dockingPane.getMaxSize(ToolWindow.Alignment.LEFT);
                        //int minSize = dockingPane.getMinSize(ToolWindow.Alignment.LEFT);
                        int minSize = dragPanel.getWidth();
                        if (w > maxSize)
                        {
                            w = maxSize;
                        }
                        if (w < minSize)
                        {
                            w = minSize;
                        }
                        s.width = w - dragPanel.getWidth();
                        toolWindow.setSize(s);
                    }
                }
                else if (split == DockingPane.Split.RIGHT)
                {
                    int x = e.getX();
                    int xDiff = startX - x;

                    for (ToolWindow toolWindow1 : toolWindows)
                    {
                        int w = getWidth() + xDiff;
                        ToolWindow toolWindow = toolWindow1;
                        Dimension s = toolWindow.getSize();
                        int maxSize = dockingPane.getMaxSize(ToolWindow.Alignment.RIGHT);
                        //int minSize = dockingPane.getMinSize(ToolWindow.Alignment.RIGHT);
                        int minSize = dragPanel.getWidth();
                        if (w > maxSize)
                        {
                            w = maxSize;
                        }
                        if (w < minSize)
                        {
                            w = minSize;
                        }
                        s.width = w - dragPanel.getWidth();
                        toolWindow.setSize(s);
                    }
                }
                else if (split == DockingPane.Split.TOP)
                {
                    int y = e.getY();
                    int yDiff = startY - y;
                    for (ToolWindow toolWindow1 : toolWindows)
                    {
                        int h = getHeight() - yDiff;
                        ToolWindow toolWindow = toolWindow1;
                        Dimension s = toolWindow.getSize();
                        int maxSize = dockingPane.getMaxSize(ToolWindow.Alignment.TOP);
                        //int minSize = dockingPane.getMinSize(ToolWindow.Alignment.TOP);
                        int minSize = dragPanel.getHeight();

                        if (h > maxSize)
                        {
                            h = maxSize;
                        }
                        if (h < minSize)
                        {
                            h = minSize;
                        }
                        s.height = h - dragPanel.getHeight();
                        toolWindow.setSize(s);
                    }
                }
                else if (split == DockingPane.Split.BOTTOM)
                {
                    int y = e.getY();
                    int yDiff = startY - y;
                    for (ToolWindow toolWindow1 : toolWindows)
                    {
                        int h = getHeight() + yDiff;
                        ToolWindow toolWindow = toolWindow1;
                        Dimension s = toolWindow.getSize();
                        int maxSize = dockingPane.getMaxSize(ToolWindow.Alignment.BOTTOM);
                        //int minSize = dockingPane.getMinSize(ToolWindow.Alignment.BOTTOM);
                        int minSize = dragPanel.getHeight();
                        if (h > maxSize)
                        {
                            h = maxSize;
                        }
                        if (h < minSize)
                        {
                            h = minSize;
                        }
                        s.height = h - dragPanel.getHeight();
                        toolWindow.setSize(s);
                    }
                }
            }
        });
    }


    public void setToolWindowHeight(int height)
    {
        for (ToolWindow toolWindow : toolWindows)
        {
            if (toolWindow.getSizeState() == ToolWindow.SizeState.NORMAL)
            {
                Dimension size = toolWindow.getSize();
                size.height = height - dragPanel.getHeight();
                toolWindow.setSize(size);
            }
        }
    }


    public void setToolWindowWidth(int width)
    {
        for (ToolWindow toolWindow : toolWindows)
        {
            if (toolWindow.getSizeState() == ToolWindow.SizeState.NORMAL)
            {
                Dimension size = toolWindow.getSize();
                size.width = width - dragPanel.getWidth();
                toolWindow.setSize(size);
            }
        }
    }


    @NotNull
    public JComponent getDragPanel()
    {
        return dragPanel;
    }


    public int getToolWindowCount()
    {
        return toolWindows.size();
    }


    public void add(@NotNull ToolWindow toolWindow)
    {
        toolWindows.add(toolWindow);
        rebuildComponent();
    }


    public void remove(@NotNull ToolWindow toolWindow)
    {
        toolWindows.remove(toolWindow);
        rebuildComponent();
    }


    public void setToolWindows(@NotNull ToolWindow[] tWs)
    {
        toolWindows.clear();
        toolWindows.addAll(Arrays.asList(tWs));

        rebuildComponent();
    }


    public void clearToolWindows()
    {
        toolWindows.clear();

        rebuildComponent();
    }


    @NotNull
    public Dimension getPreferredSize()
    {
        Dimension d = new Dimension();
        for (ToolWindow toolWindow : toolWindows)
        {
            if (toolWindow.getSizeState() == ToolWindow.SizeState.NORMAL)
            {
                if (split == DockingPane.Split.TOP || split == DockingPane.Split.BOTTOM)
                {
                    d.height = Math.max(d.height, toolWindow.getSize().height + dragPanel.getHeight());
                }
                else
                {
                    d.width = Math.max(d.width, toolWindow.getSize().width + dragPanel.getWidth());
                }
            }
        }

        return d;
    }


    public boolean isVisible()
    {
        for (ToolWindow toolWindow : toolWindows)
        {
            if (toolWindow.getSizeState() == ToolWindow.SizeState.NORMAL)
            {
                return true;
            }
        }

        return false;
    }


    private void rebuildComponent()
    {
        removeAll();

        switch (split)
        {
            case TOP:
                add(dragPanel, BorderLayout.SOUTH);
                break;
            case BOTTOM:
                add(dragPanel, BorderLayout.NORTH);
                break;
            case LEFT:
                add(dragPanel, BorderLayout.EAST);
                break;
            case RIGHT:
                add(dragPanel, BorderLayout.WEST);
                break;
        }

        if (toolWindows.isEmpty())
        {
            //noinspection UnnecessaryReturnStatement
            return;//just to be more clear
        }
        else if (toolWindows.size() == 1)
        {
            add(toolWindows.get(0).getCategory().getMainComponent(), BorderLayout.CENTER);
        }
        else
        {
            ToolWindow firstToolWindow = toolWindows.get(0);

            JSplitPane splitPane = new JSplitPane(splitpaneDirection == DockingPane.SplitpaneDirection.HORIZONTAL ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT);
            splitPane.setBorder(BorderFactory.createEmptyBorder());
            add(splitPane, BorderLayout.CENTER);
            splitPane.setTopComponent(firstToolWindow.getCategory().getMainComponent());
            splitPane.setDividerLocation(0.5);
            splitPane.setResizeWeight(0.5);
            splitPane.setContinuousLayout(true);

            for (int i = 1; i < toolWindows.size(); i++)
            {
                ToolWindow toolWindow = toolWindows.get(i);
                if (i == toolWindows.size() - 1)
                {
                    splitPane.setBottomComponent(toolWindow.getCategory().getMainComponent());
                    splitPane.setDividerLocation(0.5);
                    splitPane.setResizeWeight(0.5);
                }
                else
                {
                    JSplitPane childSplitPane = new JSplitPane(splitpaneDirection == DockingPane.SplitpaneDirection.HORIZONTAL ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT, true);
                    childSplitPane.setBorder(BorderFactory.createEmptyBorder());
                    splitPane.setBottomComponent(childSplitPane);
                    splitPane.setDividerLocation(0.5);
                    splitPane.setResizeWeight(0.5);
                    childSplitPane.setTopComponent(toolWindow.getCategory().getMainComponent());
                    childSplitPane.setDividerLocation(0.5);
                    childSplitPane.setResizeWeight(0.5);
                    splitPane = childSplitPane;
                }
            }

            revalidate();
            repaint();
        }
    }


    public void setSplitpaneDirection(@NotNull DockingPane.SplitpaneDirection splitpaneDirection)
    {
        this.splitpaneDirection = splitpaneDirection;
        rebuildComponent();
    }


    @NotNull
    public DockingPane.SplitpaneDirection getSplitpaneDirection()
    {
        return splitpaneDirection;
    }
}
