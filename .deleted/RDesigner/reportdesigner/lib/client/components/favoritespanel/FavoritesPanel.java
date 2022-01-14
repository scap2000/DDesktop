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
package org.pentaho.reportdesigner.lib.client.components.favoritespanel;


import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.components.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * User: Martin
 * Date: 11.03.2005
 * Time: 10:09:48
 */
public class FavoritesPanel extends JPanel
{

    @NotNull
    private Category maximizedCategory;//the one currently maximized
    @NotNull
    private ArrayList<Category> minimizedCategoryList;//all minimized (visible on the buttons, without the maximized)
    @NotNull
    private LinkedHashMap<Object, Category> categoryMap;//all (including maximized and visible)

    @NotNull
    private AbstractHeaderComponent headerComponent;

    @NotNull
    private ArrayList<JButton> activateButtons;
    @NotNull
    private JPanel activateButtonsPanel;

    @NotNull
    private JPanel toolbarButtonPanel;

    @NotNull
    private JPanel southPanel;

    @NotNull
    private JButton showListButton;
    @NotNull
    private JPopupMenu popupMenu;

    private int visibleCategoryCount;

    private boolean toolbarVisible;

    @NotNull
    private JMenuItem moreElementsMenuItem;
    @NotNull
    private JMenuItem lessElementsMenuItem;

    @NotNull
    private PropertyChangeSupport propertyChangeSupport;


    public FavoritesPanel()
    {
        categoryMap = new LinkedHashMap<Object, Category>();
        minimizedCategoryList = new ArrayList<Category>();

        setLayout(new BorderLayout());

        headerComponent = new DefaultHeaderComponent();
        add(headerComponent, BorderLayout.NORTH);

        visibleCategoryCount = 0;

        toolbarVisible = false;

        activateButtons = new ArrayList<JButton>();
        activateButtonsPanel = new JPanel(new GridLayout(0, 1));

        southPanel = new JPanel(new BorderLayout());
        southPanel.add(activateButtonsPanel, BorderLayout.CENTER);

        JPanel moreLessGroupPanel = new JPanel(new BorderLayout());

        toolbarButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));


        showListButton = new JButton("...");
        showListButton.setDefaultCapable(false);

        popupMenu = new JPopupMenu();
        popupMenu.setInvoker(showListButton);

        showListButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Point p = showListButton.getLocationOnScreen();
                int h = popupMenu.getPreferredSize().height;
                p.y -= h;
                p.y += showListButton.getHeight();
                p.x += showListButton.getWidth();
                popupMenu.setLocation(p);
                popupMenu.setVisible(true);
            }
        });


        JPanel helperPanel = new JPanel(new BorderLayout());
        helperPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        helperPanel.add(showListButton, BorderLayout.SOUTH);
        moreLessGroupPanel.add(helperPanel, BorderLayout.EAST);

        moreLessGroupPanel.add(toolbarButtonPanel, BorderLayout.CENTER);

        southPanel.add(moreLessGroupPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);


        moreElementsMenuItem = new JMenuItem("Mehr Elemente");//NON-NLS
        moreElementsMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (getVisibleCategoryCount() == categoryMap.size())
                {
                    setEnabled(false);
                }

                setVisibleCategoryCount(getVisibleCategoryCount() + 1);
            }
        });

        lessElementsMenuItem = new JMenuItem("Weniger Elemente");//NON-NLS
        lessElementsMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (getVisibleCategoryCount() == 1)
                {
                    lessElementsMenuItem.setEnabled(false);
                }

                setVisibleCategoryCount(getVisibleCategoryCount() - 1);
            }
        });

        propertyChangeSupport = new PropertyChangeSupport(this);
    }


    @NotNull
    public AbstractHeaderComponent getHeaderComponent()
    {
        return headerComponent;
    }


    public void setHeaderComponent(@NotNull AbstractHeaderComponent headerComponent)
    {
        remove(this.headerComponent);
        this.headerComponent = headerComponent;
        add(headerComponent, BorderLayout.NORTH);
    }


    @NotNull
    public JButton getShowListButton()
    {
        return showListButton;
    }


    @NotNull
    public JMenuItem getMoreElementsMenuItem()
    {
        return moreElementsMenuItem;
    }


    @NotNull
    public JMenuItem getLessElementsMenuItem()
    {
        return lessElementsMenuItem;
    }


    public void setToolbarVisible(boolean toolbarVisible)
    {
        boolean oldToolbarVisible = this.toolbarVisible;

        this.toolbarVisible = toolbarVisible;
        refreshComponent();

        propertyChangeSupport.firePropertyChange("toolbarVisible", oldToolbarVisible, toolbarVisible);//NON-NLS
    }


    public int getVisibleCategoryCount()
    {
        return visibleCategoryCount;
    }


    /**
     * Sets how many items should be displayed as favorites buttons. This does not include the maximized Category.
     *
     * @param visibleCategoryCount
     */
    public void setVisibleCategoryCount(int visibleCategoryCount)
    {
        if (visibleCategoryCount < 0 || visibleCategoryCount > categoryMap.size())
        {
            throw new IllegalArgumentException("visibleCategoryCount has to be in range [0, " + (categoryMap.size()) + "]");
        }

        int oldVisibleCategoryCount = this.visibleCategoryCount;

        this.visibleCategoryCount = visibleCategoryCount;

        activateButtonsPanel.removeAll();
        activateButtons.clear();

        for (int i = 0; i < visibleCategoryCount; i++)
        {
            JButton activateButton = new JButton("activateButton " + i);//NON-NLS
            activateButton.setDefaultCapable(false);
            activateButton.setHorizontalAlignment(JButton.LEFT);

            final int index = i;
            activateButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    Category category = minimizedCategoryList.get(index);
                    setMaximizedCategory(category);
                }
            });

            activateButtons.add(activateButton);
            activateButtonsPanel.add(activateButton);
        }

        refreshComponent();

        propertyChangeSupport.firePropertyChange("visibleCategoryCount", oldVisibleCategoryCount, visibleCategoryCount);//NON-NLS
    }


    public void addCategory(@NotNull Category category)
    {
        categoryMap.put(category.getKey(), category);

        category.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                refreshComponent();
            }
        });

        refreshComponent();
    }


    public void removeCategory(@NotNull Category<JComponent> category)
    {
        categoryMap.remove(category.getKey());

        refreshComponent();
    }


    public void setCategories(@NotNull Category... categories)
    {
        //noinspection ConstantConditions
        if (categories == null || categories.length == 0)
        {
            throw new IllegalArgumentException("categories can not be null or empty");
        }

        categoryMap.clear();

        for (Category category : categories)
        {
            categoryMap.put(category.getKey(), category);

            category.addPropertyChangeListener(new PropertyChangeListener()
            {
                public void propertyChange(@NotNull PropertyChangeEvent evt)
                {
                    refreshComponent();
                }
            });
        }
        refreshComponent();
    }


    public void setMaximizedCategory(@NotNull Category category)
    {
        Category oldMaximizedCategory = this.maximizedCategory;

        category.setLastAccessedMillis(System.currentTimeMillis());

        refreshComponent();

        propertyChangeSupport.firePropertyChange("maximizedCategory", oldMaximizedCategory, maximizedCategory);//NON-NLS
    }


    @NotNull
    public Category getMaximizedCategory()
    {
        return maximizedCategory;
    }


    public void executeMaximizedCategory()
    {
        maximizedCategory.setLastAccessedMillis(System.currentTimeMillis());
    }


    private void refreshComponent()
    {
        if (categoryMap.isEmpty())
        {
            return;
        }

        ArrayList<Category> categoryListByTime = new ArrayList<Category>(categoryMap.values());
        Collections.sort(categoryListByTime, new Comparator<Category>()
        {
            public int compare(@NotNull Category c1, @NotNull Category c2)
            {
                return Long.valueOf(c2.getLastAccessedMillis()).compareTo(new Long(c1.getLastAccessedMillis()));
            }
        });

        Category maxCategory = categoryListByTime.get(0);

        if (maxCategory != null)
        {
            headerComponent.setCategory(maxCategory);

            //noinspection ConstantConditions
            if ((maximizedCategory != null) && (maximizedCategory.getMainComponent() != null))
            {
                remove(maximizedCategory.getMainComponent());
            }

            if (maxCategory.getMainComponent() != null)
            {
                add(maxCategory.getMainComponent(), BorderLayout.CENTER);
            }
            else
            {
                add(new JPanel(), BorderLayout.CENTER);
            }

            maximizedCategory = maxCategory;
        }

        //create a list containing all visible (not maximized) categories
        ArrayList<Category> visibleCategoryList = new ArrayList<Category>();
        for (int i = 0; i < visibleCategoryCount; i++)
        {
            visibleCategoryList.add(categoryListByTime.get(i));
        }

        //order list by index of original list
        Collections.sort(visibleCategoryList, new Comparator<Category>()
        {
            public int compare(@NotNull Category o1, @NotNull Category o2)
            {
                int index1 = new ArrayList<Category>(categoryMap.values()).indexOf(o1);
                int index2 = new ArrayList<Category>(categoryMap.values()).indexOf(o2);
                return Integer.valueOf(index1).compareTo(Integer.valueOf(index2));
            }
        });

        for (int i = 0; i < visibleCategoryList.size(); i++)
        {
            Category category = visibleCategoryList.get(i);
            JButton button = activateButtons.get(i);

            button.setMargin(new Insets(0, 0, 0, 0));
            button.setIcon(category.getIconBig());
            button.setText(category.getTitle());
        }

        minimizedCategoryList.clear();
        minimizedCategoryList.addAll(visibleCategoryList);

        final ArrayList<Category> toolbarCategoryList = new ArrayList<Category>(categoryMap.values());

        toolbarButtonPanel.removeAll();

        if (toolbarVisible)
        {
            for (int i = 0; i < toolbarCategoryList.size(); i++)
            {
                Category category = toolbarCategoryList.get(i);
                JButton toolbarButton = new JButton(category.getIconSmall());
                toolbarButton.setDefaultCapable(false);

                toolbarButton.setMargin(new Insets(0, 0, 0, 0));
                final int index = i;
                toolbarButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        setMaximizedCategory(toolbarCategoryList.get(index));
                    }
                });
                toolbarButton.setToolTipText(category.getTitle());

                toolbarButtonPanel.add(toolbarButton);
            }
        }

        updatePopupMenu();

        revalidate();
        repaint();
    }


    private void updatePopupMenu()
    {
        popupMenu.removeAll();

        ArrayList<Category> categoryList = new ArrayList<Category>(categoryMap.values());
        for (final Category category : categoryList)
        {
            JMenuItem menuItem = new JMenuItem(category.getTitle(), category.getIconBig());
            menuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    setMaximizedCategory(category);
                }
            });

            popupMenu.add(menuItem);
        }

        popupMenu.addSeparator();

        moreElementsMenuItem.setEnabled(getVisibleCategoryCount() < categoryList.size());
        popupMenu.add(moreElementsMenuItem);

        lessElementsMenuItem.setEnabled(getVisibleCategoryCount() > 0);
        popupMenu.add(lessElementsMenuItem);
    }


    @NotNull
    public Category getCategory(@NotNull Object key)
    {
        return categoryMap.get(key);
    }


    @NotNull
    public Category[] getCategories()
    {
        return categoryMap.values().toArray(new Category[categoryMap.size()]);
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


    public boolean hasListeners(@NotNull String propertyName)
    {
        return propertyChangeSupport.hasListeners(propertyName);
    }


    public void updateUI()
    {
        super.updateUI();
        //noinspection ConstantConditions
        if (popupMenu != null)
        {
            SwingUtilities.updateComponentTreeUI(popupMenu);
        }

        //noinspection ConstantConditions
        if (categoryMap != null)
        {
            Collection<Category> collection = categoryMap.values();
            for (Category category : collection)
            {
                SwingUtilities.updateComponentTreeUI(category.getMainComponent());
            }
        }
    }


}
