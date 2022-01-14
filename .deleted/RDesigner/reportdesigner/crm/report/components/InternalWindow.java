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
package org.pentaho.reportdesigner.crm.report.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.Category;
import org.pentaho.reportdesigner.lib.client.components.favoritespanel.DefaultHeaderComponent;
import org.pentaho.reportdesigner.lib.client.util.HeaderBorder;
import org.pentaho.reportdesigner.lib.client.util.ShadowBorder;

import javax.swing.*;
import javax.swing.FocusManager;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * User: Martin
 * Date: 03.02.2006
 * Time: 08:11:42
 */
public class InternalWindow extends JPanel
{
    @NotNull
    private static final String PERMANENT_FOCUS_OWNER = "permanentFocusOwner";

    @NotNull
    private DefaultHeaderComponent defaultHeaderComponent;
    @NotNull
    private PropertyChangeListener target;


    public InternalWindow(@NotNull Category titleCategory)
    {
        setLayout(new BorderLayout());

        defaultHeaderComponent = new DefaultHeaderComponent(new Insets(1, 1, 1, 1), 0.9);
        defaultHeaderComponent.setBorder(new HeaderBorder());

        setCategory(titleCategory);

        add(defaultHeaderComponent, BorderLayout.NORTH);
        setBorder(new ShadowBorder());
        setBorder(BorderFactory.createLineBorder(new Color(170, 170, 170)));

        FocusManager currentManager = FocusManager.getCurrentManager();
        target = new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if (evt.getNewValue() instanceof Component)
                {
                    Component component = (Component) evt.getNewValue();
                    if (SwingUtilities.isDescendingFrom(component, InternalWindow.this))
                    {
                        defaultHeaderComponent.setFocused(true);
                    }
                    else
                    {
                        defaultHeaderComponent.setFocused(false);
                    }
                }
            }
        };

        currentManager.addPropertyChangeListener(PERMANENT_FOCUS_OWNER, target);
    }


    public void dispose()
    {
        FocusManager.getCurrentManager().removePropertyChangeListener(PERMANENT_FOCUS_OWNER, target);
    }


    public final void setFocusCycleRoot(boolean focusCycleRoot)
    {
    }


    public final boolean isFocusCycleRoot()
    {
        return true;
    }


    @Nullable
    public final Container getFocusCycleRootAncestor()
    {
        return null;
    }


    public void setCategory(@NotNull Category category)
    {
        defaultHeaderComponent.setCategory(category);
    }

}