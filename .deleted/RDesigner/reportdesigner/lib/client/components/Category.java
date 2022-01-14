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
package org.pentaho.reportdesigner.lib.client.components;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.commands.CommandToolBar;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * User: Martin
 * Date: 11.03.2005
 * Time: 10:11:41
 */
public class Category<T extends JComponent>
{
    @NotNull
    private Object key;
    @Nullable
    private ImageIcon iconBig;
    @Nullable
    private ImageIcon iconSmall;
    @NotNull
    private String title;
    @NotNull
    private JComponent[] actionComponents;
    @Nullable
    private T mainComponent;

    private long lastAccessedMillis;

    @NotNull
    private PropertyChangeSupport propertyChangeSupport;

    @Nullable
    private JToolBar jToolBar;

    @Nullable
    @SuppressWarnings({"UnusedDeclaration"})
    private CommandToolBar commandToolBar;
    @NotNull
    private static final JComponent[] EMPTY_JCOMPONENT_ARRAY = new JComponent[0];


    public Category(@NotNull @NonNls Object key, @Nullable ImageIcon iconBig, @Nullable ImageIcon iconSmall, @NotNull String title, @Nullable T mainComponent)
    {
        //noinspection ConstantConditions
        if (key == null)
        {
            throw new IllegalArgumentException("key must not be null");
        }

        this.key = key;
        this.iconBig = iconBig;
        this.iconSmall = iconSmall;
        this.title = title;
        this.actionComponents = EMPTY_JCOMPONENT_ARRAY;
        this.mainComponent = mainComponent;

        propertyChangeSupport = new PropertyChangeSupport(this);
    }


    @NotNull
    public Object getKey()
    {
        return key;
    }


    @Nullable
    public ImageIcon getIconBig()
    {
        return iconBig;
    }


    public void setIconBig(@Nullable ImageIcon iconBig)
    {
        ImageIcon oldIconBig = this.iconBig;
        this.iconBig = iconBig;

        propertyChangeSupport.firePropertyChange("iconBig", oldIconBig, iconBig);//NON-NLS
    }


    @Nullable
    public ImageIcon getIconSmall()
    {
        return iconSmall;
    }


    public void setIconSmall(@Nullable ImageIcon iconSmall)
    {
        ImageIcon oldIconSmall = this.iconSmall;
        this.iconSmall = iconSmall;

        propertyChangeSupport.firePropertyChange("iconSmall", oldIconSmall, iconSmall);//NON-NLS
    }


    @NotNull
    public String getTitle()
    {
        return title;
    }


    public void setTitle(@NotNull String title)
    {
        String oldTitle = this.title;
        this.title = title;

        propertyChangeSupport.firePropertyChange("title", oldTitle, title);//NON-NLS
    }


    @NotNull
    public JComponent[] getActionComponents()
    {
        return actionComponents;
    }


    public void setActionComponents(@NotNull JComponent[] actionComponents)
    {
        JComponent[] oldActionComponents = this.actionComponents;
        this.actionComponents = actionComponents;

        propertyChangeSupport.firePropertyChange("actionComponents", oldActionComponents, actionComponents);//NON-NLS
    }


    @Nullable
    public JToolBar getToolBar()
    {
        return jToolBar;
    }


    public void setToolBar(@NotNull JToolBar jToolBar)
    {
        JToolBar oldToolBar = this.jToolBar;
        this.jToolBar = jToolBar;

        propertyChangeSupport.firePropertyChange("toolBar", oldToolBar, jToolBar);//NON-NLS
    }


    public void setToolBar(@NotNull CommandToolBar commandToolBar)
    {
        this.commandToolBar = commandToolBar;
        JToolBar oldToolBar = this.jToolBar;
        this.jToolBar = commandToolBar.getToolBar();

        propertyChangeSupport.firePropertyChange("toolBar", oldToolBar, jToolBar);//NON-NLS
    }


    @Nullable
    public T getMainComponent()
    {
        return mainComponent;
    }


    public void setMainComponent(@Nullable T mainComponent)
    {
        JComponent oldMainComponent = this.mainComponent;
        this.mainComponent = mainComponent;

        propertyChangeSupport.firePropertyChange("mainComponent", oldMainComponent, mainComponent);//NON-NLS
    }


    public long getLastAccessedMillis()
    {
        return lastAccessedMillis;
    }


    public void setLastAccessedMillis(long lastAccessedMillis)
    {
        long oldLastAccessedMillis = this.lastAccessedMillis;
        this.lastAccessedMillis = lastAccessedMillis;

        propertyChangeSupport.firePropertyChange("lastAccessedMillis", Long.valueOf(oldLastAccessedMillis), Long.valueOf(lastAccessedMillis));//NON-NLS
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


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        final Category category = (Category) o;

        return key.equals(category.key);
    }


    public int hashCode()
    {
        return (key.hashCode());
    }


    @NotNull
    public String toString()
    {
        return "Category{" +
               "key=" + key +
               ", title='" + title + "'" +
               ", mainComponent=" + mainComponent +
               "}";
    }
}
