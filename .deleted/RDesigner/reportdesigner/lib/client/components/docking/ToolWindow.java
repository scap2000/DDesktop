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
import org.pentaho.reportdesigner.lib.client.components.Category;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * User: Martin
 * Date: 18.03.2005
 * Time: 10:46:34
 */
public class ToolWindow
{
    @NotNull
    public static final String PROPERTY_WINDOW_MODE = "windowMode";
    @NotNull
    public static final String PROPERTY_DOCKING_MODE = "dockingMode";
    @NotNull
    public static final String PROPERTY_PINNING_MODE = "pinningMode";
    @NotNull
    public static final String PROPERTY_SIZE_STATE = "sizeState";
    @NotNull
    public static final String PROPERTY_SIZE = "size";
    @NotNull
    public static final String PROPERTY_ALIGNMENT = "alignment";

    public enum Alignment
    {
        @NotNull TOP,
        @NotNull BOTTOM,
        @NotNull LEFT,
        @NotNull RIGHT
    }

    public enum WindowMode
    {
        @NotNull INSIDE,//inside component
        @NotNull WINDOW//as own window
    }

    public enum DockingMode
    {
        @NotNull DOCKED,
        @NotNull FLOATING
    }

    public enum PinningMode
    {
        @NotNull PINNED,//stays even if we lose focus
        @NotNull UNPINNED//autohides on focus lost
    }

    public enum SizeState
    {
        @NotNull MINIMIZED,
        @NotNull NORMAL
    }

    @NotNull
    private DockingPane dockingPane;

    @NotNull
    private Alignment alignment;
    @NotNull
    private WindowMode windowMode;
    @NotNull
    private DockingMode dockingMode;
    @NotNull
    private PinningMode pinningMode;
    @NotNull
    private SizeState sizeState;

    @NotNull
    private Dimension size;

    @NotNull
    private Category category;

    @NotNull
    private PropertyChangeSupport propertyChangeSupport;

    @NotNull
    private ImageToggleButton toolWindowButton;


    public ToolWindow(@NotNull Category category)
    {
        //noinspection ConstantConditions
        if (category == null)
        {
            throw new IllegalArgumentException("category must not be null");
        }

        alignment = Alignment.LEFT;
        windowMode = WindowMode.INSIDE;
        dockingMode = DockingMode.DOCKED;
        pinningMode = PinningMode.PINNED;
        sizeState = SizeState.MINIMIZED;

        size = new Dimension(100, 100);

        dockingPane = new DockingPane();

        this.category = category;

        initToolWindowButton(category);

        propertyChangeSupport = new PropertyChangeSupport(this);
    }


    public void setDockingPane(@NotNull DockingPane dockingPane)
    {
        this.dockingPane = dockingPane;
    }


    private void initToolWindowButton(@NotNull Category category)
    {
        ImageIcon iconSmall = category.getIconSmall();
        if (iconSmall == null)
        {
            iconSmall = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        }
        toolWindowButton = new ImageToggleButton(iconSmall, category.getTitle());
        toolWindowButton.setToolTipText(category.getTitle());

        toolWindowButton.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    showPopupMenu(e);
                }
            }


            public void mousePressed(@NotNull MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    showPopupMenu(e);
                }
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    showPopupMenu(e);
                }
            }


            private void showPopupMenu(@NotNull MouseEvent event)
            {
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem menuItemLeft = new JMenuItem(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "ToolWindow.MenuItem.Left"));
                menuItemLeft.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        setAlignment(Alignment.LEFT);
                    }
                });

                JMenuItem menuItemRight = new JMenuItem(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "ToolWindow.MenuItem.Right"));
                menuItemRight.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        setAlignment(Alignment.RIGHT);
                    }
                });

                JMenuItem menuItemTop = new JMenuItem(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "ToolWindow.MenuItem.Top"));
                menuItemTop.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        setAlignment(Alignment.TOP);
                    }
                });

                JMenuItem menuItemBottom = new JMenuItem(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "ToolWindow.MenuItem.Bottom"));
                menuItemBottom.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        setAlignment(Alignment.BOTTOM);
                    }
                });

                JMenuItem menuItemHorizontal = new JMenuItem(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "ToolWindow.MenuItem.Horizontal"));
                menuItemHorizontal.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        Alignment alignment = getAlignment();
                        dockingPane.setSplitpaneDirection(alignment, DockingPane.SplitpaneDirection.HORIZONTAL);
                    }
                });

                JMenuItem menuItemVertical = new JMenuItem(TranslationManager.getInstance().getTranslation(TranslationManager.COMMON_BUNDLE_PREFIX, "ToolWindow.MenuItem.Vertical"));
                menuItemVertical.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        Alignment alignment = getAlignment();
                        dockingPane.setSplitpaneDirection(alignment, DockingPane.SplitpaneDirection.VERTICAL);
                    }
                });

                if (dockingPane == null || dockingPane.getToolWindowCountForSide(getAlignment()) < 2)
                {
                    menuItemHorizontal.setEnabled(false);
                    menuItemVertical.setEnabled(false);
                }
                else
                {
                    if (dockingPane.getSplitpaneDirection(getAlignment()) == DockingPane.SplitpaneDirection.HORIZONTAL)
                    {
                        menuItemHorizontal.setEnabled(false);
                    }
                    else
                    {
                        menuItemVertical.setEnabled(false);
                    }
                }

                switch (getAlignment())
                {
                    case TOP:
                        menuItemTop.setEnabled(false);
                        break;
                    case BOTTOM:
                        menuItemBottom.setEnabled(false);
                        break;
                    case LEFT:
                        menuItemLeft.setEnabled(false);
                        break;
                    case RIGHT:
                        menuItemRight.setEnabled(false);
                        break;
                }

                popupMenu.add(menuItemLeft);
                popupMenu.add(menuItemRight);
                popupMenu.add(menuItemTop);
                popupMenu.add(menuItemBottom);
                popupMenu.addSeparator();
                popupMenu.add(menuItemHorizontal);
                popupMenu.add(menuItemVertical);

                popupMenu.show(toolWindowButton, event.getX(), event.getY());
            }
        });

        toolWindowButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (toolWindowButton.isSelected())
                {
                    setSizeState(SizeState.NORMAL);
                }
                else
                {
                    setSizeState(SizeState.MINIMIZED);
                }
            }
        });
    }


    @NotNull
    public Alignment getAlignment()
    {
        return alignment;
    }


    public void setAlignment(@NotNull Alignment alignment)
    {
        Alignment alignmentOld = this.alignment;
        this.alignment = alignment;

        toolWindowButton.setAlignmentX(alignment);

        if (alignmentOld != alignment)
        {
            propertyChangeSupport.firePropertyChange(PROPERTY_ALIGNMENT, alignmentOld, alignment);
        }
    }


    @NotNull
    public WindowMode getWindowMode()
    {
        return windowMode;
    }


    public void setWindowMode(@NotNull WindowMode windowMode)
    {
        WindowMode windowModeOld = this.windowMode;
        this.windowMode = windowMode;

        if (windowModeOld != windowMode)
        {
            propertyChangeSupport.firePropertyChange(PROPERTY_WINDOW_MODE, windowModeOld, windowMode);
        }
    }


    @NotNull
    public DockingMode getDockingMode()
    {
        return dockingMode;
    }


    public void setDockingMode(@NotNull DockingMode dockingMode)
    {
        DockingMode dockingModeOld = this.dockingMode;
        this.dockingMode = dockingMode;

        if (dockingModeOld != dockingMode)
        {
            propertyChangeSupport.firePropertyChange(PROPERTY_DOCKING_MODE, dockingModeOld, dockingMode);
        }
    }


    @NotNull
    public PinningMode getPinningMode()
    {
        return pinningMode;
    }


    public void setPinningMode(@NotNull PinningMode pinningMode)
    {
        PinningMode pinningModeOld = this.pinningMode;
        this.pinningMode = pinningMode;

        if (pinningModeOld != pinningMode)
        {
            propertyChangeSupport.firePropertyChange(PROPERTY_PINNING_MODE, pinningModeOld, pinningMode);
        }
    }


    @NotNull
    public SizeState getSizeState()
    {
        return sizeState;
    }


    public void setSizeState(@NotNull SizeState sizeState)
    {
        SizeState sizeStateOld = this.sizeState;
        this.sizeState = sizeState;
        toolWindowButton.setSelected(sizeState != SizeState.MINIMIZED);

        if (sizeStateOld != sizeState)
        {
            propertyChangeSupport.firePropertyChange(PROPERTY_SIZE_STATE, sizeStateOld, sizeState);
        }
    }


    @NotNull
    public Dimension getSize()
    {
        return new Dimension(size);
    }


    public void setSize(@NotNull Dimension size)
    {
        Dimension sizeOld = this.size;
        this.size = size;

        if (!sizeOld.equals(size))
        {
            propertyChangeSupport.firePropertyChange(PROPERTY_SIZE, sizeOld, size);
        }
    }


    @NotNull
    public Category getCategory()
    {
        return category;
    }


    @NotNull
    public ImageToggleButton getToolWindowButton()
    {
        return toolWindowButton;
    }


    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }


    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


    @NotNull
    public PropertyChangeListener[] getPropertyChangeListeners()
    {
        return propertyChangeSupport.getPropertyChangeListeners();
    }


    public void addPropertyChangeListener(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }


    public void removePropertyChangeListener(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }


    @NotNull
    public PropertyChangeListener[] getPropertyChangeListeners(@NotNull String propertyName)
    {
        return propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }
}
