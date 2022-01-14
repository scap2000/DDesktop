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
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 18.03.2005
 * Time: 10:43:01
 */
public class DockingPane extends JPanel
{


    public enum SplitpaneDirection
    {
        @NotNull HORIZONTAL,
        @NotNull VERTICAL
    }

    public enum Split
    {
        @NotNull TOP,
        @NotNull BOTTOM,
        @NotNull LEFT,
        @NotNull RIGHT
    }

    @NotNull
    private ArrayList<ToolWindow> toolWindows;

    @NotNull
    private JComponent centerComponent;

    @NotNull
    private JPanel[] buttonBorders = new JPanel[4];

    @NotNull
    @SuppressWarnings({"unchecked"})
    private ArrayList<ToolWindow>[] toolWindowsAtSide = (ArrayList<ToolWindow>[]) new ArrayList[4];

    @NotNull
    private BorderComponent[] dockedComponents = new BorderComponent[4];

    @SuppressWarnings({"MismatchedReadAndWriteOfArray"})
    @NotNull
    private BorderComponent[] floatingComponents = new BorderComponent[4];


    public DockingPane()
    {
        setLayout(new DockingPaneLayout());

        centerComponent = new JPanel();
        toolWindows = new ArrayList<ToolWindow>();

        buttonBorders[0] = new JPanel();
        BoxLayout boxLayout0 = new BoxLayout(buttonBorders[0], BoxLayout.X_AXIS);
        buttonBorders[0].setLayout(boxLayout0);

        buttonBorders[1] = new JPanel();
        BoxLayout boxLayout1 = new BoxLayout(buttonBorders[1], BoxLayout.X_AXIS);
        buttonBorders[1].setLayout(boxLayout1);

        buttonBorders[2] = new JPanel();
        BoxLayout boxLayout2 = new BoxLayout(buttonBorders[2], BoxLayout.Y_AXIS);
        buttonBorders[2].setLayout(boxLayout2);

        buttonBorders[3] = new JPanel();
        BoxLayout boxLayout3 = new BoxLayout(buttonBorders[3], BoxLayout.Y_AXIS);
        buttonBorders[3].setLayout(boxLayout3);

        for (JPanel buttonBorder : buttonBorders)
        {
            buttonBorder.setOpaque(true);
        }

        setBackground(buttonBorders[0].getBackground());

        dockedComponents[0] = new BorderComponent(Split.TOP, SplitpaneDirection.HORIZONTAL, this);
        dockedComponents[1] = new BorderComponent(Split.BOTTOM, SplitpaneDirection.HORIZONTAL, this);
        dockedComponents[2] = new BorderComponent(Split.LEFT, SplitpaneDirection.VERTICAL, this);
        dockedComponents[3] = new BorderComponent(Split.RIGHT, SplitpaneDirection.VERTICAL, this);

        floatingComponents[0] = new BorderComponent(Split.TOP, SplitpaneDirection.HORIZONTAL, this);
        floatingComponents[1] = new BorderComponent(Split.BOTTOM, SplitpaneDirection.HORIZONTAL, this);
        floatingComponents[2] = new BorderComponent(Split.LEFT, SplitpaneDirection.VERTICAL, this);
        floatingComponents[3] = new BorderComponent(Split.RIGHT, SplitpaneDirection.VERTICAL, this);

        toolWindowsAtSide[0] = new ArrayList<ToolWindow>();
        toolWindowsAtSide[1] = new ArrayList<ToolWindow>();
        toolWindowsAtSide[2] = new ArrayList<ToolWindow>();
        toolWindowsAtSide[3] = new ArrayList<ToolWindow>();

        for (JPanel buttonBorder : buttonBorders)
        {
            add(buttonBorder);
        }
    }


    public void addToolWindow(@NotNull ToolWindow toolWindow)
    {
        toolWindows.add(toolWindow);
        toolWindow.setDockingPane(this);
        toolWindow.setSizeState(ToolWindow.SizeState.MINIMIZED);

        toolWindowsAtSide[indexForAlignment(toolWindow.getAlignment())].add(toolWindow);

        buttonBorders[indexForAlignment(toolWindow.getAlignment())].add(toolWindow.getToolWindowButton());

        for (BorderComponent dockedComponent : dockedComponents)
        {
            add(dockedComponent);
        }

        toolWindow.addPropertyChangeListener(ToolWindow.PROPERTY_ALIGNMENT, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                ToolWindow toolWindow = (ToolWindow) evt.getSource();
                ToolWindow.Alignment alignmentOld = (ToolWindow.Alignment) evt.getOldValue();
                ToolWindow.Alignment alignmentNew = (ToolWindow.Alignment) evt.getNewValue();

                if (toolWindow.getDockingMode() == ToolWindow.DockingMode.DOCKED)
                {
                    for (ArrayList<ToolWindow> arrayList : toolWindowsAtSide)
                    {
                        arrayList.remove(toolWindow);
                    }

                    toolWindowsAtSide[indexForAlignment(alignmentNew)].add(toolWindow);

                    rebuildDockedComponent(alignmentOld);
                    rebuildDockedComponent(alignmentNew);
                }
                else if (toolWindow.getDockingMode() == ToolWindow.DockingMode.FLOATING)
                {
                    //TODO
                }
            }

        });

        toolWindow.addPropertyChangeListener(ToolWindow.PROPERTY_WINDOW_MODE, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                //TODO
            }
        });

        toolWindow.addPropertyChangeListener(ToolWindow.PROPERTY_DOCKING_MODE, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                //TODO
            }
        });

        toolWindow.addPropertyChangeListener(ToolWindow.PROPERTY_PINNING_MODE, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                //TODO
            }
        });

        toolWindow.addPropertyChangeListener(ToolWindow.PROPERTY_SIZE_STATE, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                ToolWindow toolWindow = (ToolWindow) evt.getSource();
                rebuildDockedComponent(toolWindow.getAlignment());
            }
        });

        toolWindow.addPropertyChangeListener(ToolWindow.PROPERTY_SIZE, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                revalidate();
                repaint();
            }
        });
    }


    public int getToolWindowCountForSide(@NotNull ToolWindow.Alignment alignment)
    {
        return toolWindowsAtSide[indexForAlignment(alignment)].size();
    }


    private int indexForAlignment(@NotNull ToolWindow.Alignment alignment)
    {
        switch (alignment)
        {
            case TOP:
                return 0;
            case BOTTOM:
                return 1;
            case LEFT:
                return 2;
            case RIGHT:
                return 3;
        }
        return 0;
    }


    private void rebuildDockedComponent(@NotNull ToolWindow.Alignment alignment)
    {
        JPanel buttonBorder = buttonBorders[indexForAlignment(alignment)];
        buttonBorder.removeAll();

        dockedComponents[indexForAlignment(alignment)].clearToolWindows();

        ArrayList arrayList = toolWindowsAtSide[indexForAlignment(alignment)];
        for (Object anArrayList : arrayList)
        {
            ToolWindow toolWindow = (ToolWindow) anArrayList;
            buttonBorder.add(toolWindow.getToolWindowButton());
            if (toolWindow.getSizeState() == ToolWindow.SizeState.NORMAL)
            {
                dockedComponents[indexForAlignment(alignment)].add(toolWindow);
            }
        }

        revalidate();
        repaint();
    }


    public int getMaxSize(@NotNull ToolWindow.Alignment alignment)
    {
        if (alignment == ToolWindow.Alignment.LEFT)
        {
            return getWidth() - (buttonBorders[2].getWidth() + buttonBorders[3].getWidth() + dockedComponents[3].getWidth());
        }
        else if (alignment == ToolWindow.Alignment.RIGHT)
        {
            return getWidth() - (buttonBorders[2].getWidth() + buttonBorders[3].getWidth() + dockedComponents[2].getWidth());
        }
        else if (alignment == ToolWindow.Alignment.TOP)
        {
            return getHeight() - (buttonBorders[0].getHeight() + buttonBorders[1].getHeight() + dockedComponents[1].getHeight());
        }
        else if (alignment == ToolWindow.Alignment.BOTTOM)
        {
            return getHeight() - (buttonBorders[0].getHeight() + buttonBorders[1].getHeight() + dockedComponents[0].getHeight());
        }

        return 100;
    }


    @Nullable
    public ToolWindow getToolWindow(@NotNull String key)
    {
        for (ToolWindow toolWindow : toolWindows)
        {
            if (toolWindow.getCategory().getKey().equals(key))
            {
                return toolWindow;
            }
        }

        return null;
    }


    @NotNull
    public ToolWindow[] getToolWindows()
    {
        return toolWindows.toArray(new ToolWindow[toolWindows.size()]);
    }


    @NotNull
    public JComponent getCenterComponent()
    {
        return centerComponent;
    }


    public void setCenterComponent(@NotNull JComponent centerComponent)
    {
        remove(this.centerComponent);
        this.centerComponent = centerComponent;
        add(centerComponent);

        revalidate();
        repaint();
    }


    public void setSplitpaneDirection(@NotNull ToolWindow.Alignment alignment, @NotNull SplitpaneDirection splitpaneDirection)
    {
        dockedComponents[indexForAlignment(alignment)].setSplitpaneDirection(splitpaneDirection);
    }


    @NotNull
    public SplitpaneDirection getSplitpaneDirection(@NotNull ToolWindow.Alignment alignment)
    {
        return dockedComponents[indexForAlignment(alignment)].getSplitpaneDirection();
    }


    @NotNull
    public JPanel[] getButtonBorders()
    {
        return buttonBorders;
    }


    @NotNull
    public BorderComponent[] getDockedComponents()
    {
        return dockedComponents;
    }


}
