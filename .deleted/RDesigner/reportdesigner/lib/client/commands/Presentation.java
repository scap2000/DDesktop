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
package org.pentaho.reportdesigner.lib.client.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 19.11.2004
 * Time: 13:56:43
 */
public class Presentation
{
    @NotNull
    private ArrayList<PropertyChangeListener> propertyChangeListeners;

    @NotNull
    private String text;
    @NotNull
    private String description;
    @NotNull
    private HashMap<String, ImageIcon> icons;
    private boolean enabled;
    private boolean visible;

    @NotNull
    private CommandApplicationRoot commandApplicationRoot;

    @NotNull
    private Command command;

    @NotNull
    private HashMap<String, Object> clientPropertiesMap;

    @Nullable
    private KeyStroke accelerator;
    private int mnemonic;
    private int displayedMnemonicIndex;


    public Presentation(@NotNull Command command, @NotNull String text, @NotNull String description, boolean enabled, boolean visible, @Nullable KeyStroke accelerator, int mnemonic, int displayedMnemonicIndex)
    {
        this.command = command;
        this.text = text;
        this.description = description;
        this.enabled = enabled;
        this.visible = visible;
        this.accelerator = accelerator;
        this.mnemonic = mnemonic;
        this.displayedMnemonicIndex = displayedMnemonicIndex;

        propertyChangeListeners = new ArrayList<PropertyChangeListener>();
        icons = new HashMap<String, ImageIcon>();

        clientPropertiesMap = new HashMap<String, Object>();
    }


    @NotNull
    public Command getCommand()
    {
        return command;
    }


    public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener)
    {
        if (!propertyChangeListeners.contains(propertyChangeListener))
        {
            propertyChangeListeners.add(propertyChangeListener);
        }
    }


    private void notifyPropertyChangeListeners(@NotNull String propertyName, @Nullable Object oldValue, @Nullable Object newValue)
    {
        ArrayList<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>(propertyChangeListeners);
        for (PropertyChangeListener propertyChangeListener : listeners)
        {
            propertyChangeListener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }


    @NotNull
    public CommandApplicationRoot getCommandApplicationRoot()
    {
        return commandApplicationRoot;
    }


    public void setCommandApplicationRoot(@NotNull CommandApplicationRoot commandApplicationRoot)
    {
        this.commandApplicationRoot = commandApplicationRoot;
    }


    @NotNull
    public String getText()
    {
        return text;
    }


    public void setText(@NotNull String text)
    {
        String oldText = this.text;
        this.text = text;

        if (!oldText.equals(text))
        {
            notifyPropertyChangeListeners("text", oldText, text);//NON-NLS
        }
    }


    @NotNull
    public String getDescription()
    {
        return description;
    }


    public void setDescription(@NotNull String description)
    {
        String oldDescription = this.description;
        this.description = description;

        if (!oldDescription.equals(description))
        {
            notifyPropertyChangeListeners("description", oldDescription, description);//NON-NLS
        }
    }


    @Nullable
    public ImageIcon getIcon(@Nullable String key)
    {
        return icons.get(key);
    }


    public void setIcon(@NotNull String key, @NotNull ImageIcon icon)
    {
        Icon oldIcon = icons.get(key);
        icons.put(key, icon);
        notifyPropertyChangeListeners("icon", oldIcon, icon);//NON-NLS
    }


    @NotNull
    public HashMap<String, ImageIcon> getIcons()
    {
        return new HashMap<String, ImageIcon>(icons);
    }


    public void setIcons(@NotNull HashMap<String, ImageIcon> newIcons)
    {
        HashMap<String, ImageIcon> oldIcons = icons;
        icons = new HashMap<String, ImageIcon>(newIcons);
        notifyPropertyChangeListeners("icons", oldIcons, icons);//NON-NLS
    }


    public void setClientProperty(@NotNull String key, @NotNull Object value)
    {
        //noinspection ConstantConditions
        if (key == null)
        {
            throw new IllegalArgumentException("key must not be null");
        }
        //noinspection ConstantConditions
        if (value == null)
        {
            throw new IllegalArgumentException("value must not be null");
        }

        Object oldValue = clientPropertiesMap.get(key);
        clientPropertiesMap.put(key, value);

        if (!value.equals(oldValue))
        {
            notifyPropertyChangeListeners(key, oldValue, value);
        }
    }


    @NotNull
    public Object getClientProperty(@NotNull String key)
    {
        //noinspection ConstantConditions
        if (key == null)
        {
            throw new IllegalArgumentException("key must not be null");
        }
        return clientPropertiesMap.get(key);
    }


    public void clearClientProperty(@NotNull String key)
    {
        //noinspection ConstantConditions
        if (key == null)
        {
            throw new IllegalArgumentException("key must not be null");
        }
        Object value = clientPropertiesMap.remove(key);

        if (value != null)
        {
            notifyPropertyChangeListeners(key, value, null);
        }
    }


    public boolean isEnabled()
    {
        return enabled;
    }


    public void setEnabled(boolean enabled)
    {
        boolean oldEnabled = this.enabled;
        this.enabled = enabled;

        if (oldEnabled != enabled)
        {
            notifyPropertyChangeListeners("enabled", Boolean.valueOf(oldEnabled), Boolean.valueOf(enabled));//NON-NLS
        }
    }


    public boolean isVisible()
    {
        return visible;
    }


    public void setVisible(boolean visible)
    {
        boolean oldVisible = this.visible;
        this.visible = visible;

        if (oldVisible != visible)
        {
            notifyPropertyChangeListeners("visible", Boolean.valueOf(oldVisible), Boolean.valueOf(visible));//NON-NLS
        }
    }


    public void fireStructureChanged()
    {
        notifyPropertyChangeListeners("structure", null, null);//NON-NLS
    }


    public void clearPropertyChangeListeners()
    {
        propertyChangeListeners.clear();
    }


    @Nullable
    public JComponent getCustomComponent()
    {
        return null;
    }


    public void setAccelerator(@Nullable KeyStroke accelerator)
    {
        KeyStroke oldAccelerator = this.accelerator;

        this.accelerator = accelerator;

        notifyPropertyChangeListeners("accelerator", oldAccelerator, accelerator);//NON-NLS
    }


    public void setMnemonic(int mnemonic)
    {
        int oldMnemonic = this.mnemonic;

        this.mnemonic = mnemonic;

        notifyPropertyChangeListeners("mnemonic", Integer.valueOf(oldMnemonic), Integer.valueOf(mnemonic));//NON-NLS
    }


    public void setDisplayedMnemonicIndex(int displayedMnemonicIndex)
    {
        int oldDisplayedMnemonicIndex = this.displayedMnemonicIndex;

        this.displayedMnemonicIndex = displayedMnemonicIndex;

        notifyPropertyChangeListeners("displayedMnemonicIndex", Integer.valueOf(oldDisplayedMnemonicIndex), Integer.valueOf(displayedMnemonicIndex));//NON-NLS
    }


    @Nullable
    public KeyStroke getAccelerator()
    {
        return accelerator;
    }


    public int getMnemonic()
    {
        return mnemonic;
    }


    public int getDisplayedMnemonicIndex()
    {
        return displayedMnemonicIndex;
    }
}
