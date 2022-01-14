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
package org.pentaho.reportdesigner.lib.client.components.tabbedpane;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;

/**
 * User: Martin
 * Date: 17.03.2005
 * Time: 19:33:04
 */
public class HoverToggleButtonUI extends BasicToggleButtonUI
{
    @NotNull
    private static final HoverToggleButtonUI metalToggleButtonUI = new HoverToggleButtonUI();

    @NotNull
    protected Color focusColor;
    @NotNull
    protected Color selectColor;
    @NotNull
    private Color selectForegroundColor;
    @NotNull
    private Color fgColor;

    @NotNull
    protected Color disabledTextColor;

    private boolean defaults_initialized;


    @NotNull
    @SuppressWarnings({"UnusedDeclaration"})
    public static ComponentUI createUI(@NotNull JComponent b)
    {
        return metalToggleButtonUI;
    }


    public void installDefaults(@NotNull AbstractButton b)
    {
        super.installDefaults(b);
        if (!defaults_initialized)
        {
            focusColor = UIManager.getColor(getPropertyPrefix() + "focus");//NON-NLS
            selectColor = UIManager.getColor("List.selectionBackground");//NON-NLS
            selectForegroundColor = UIManager.getColor("List.selectionForeground");//NON-NLS
            fgColor = b.getForeground();
            disabledTextColor = UIManager.getColor(getPropertyPrefix() + "disabledText");//NON-NLS
            defaults_initialized = true;
        }
    }


    protected void uninstallDefaults(@NotNull AbstractButton b)
    {
        super.uninstallDefaults(b);
        defaults_initialized = false;
    }


    public void update(@NotNull Graphics g, @NotNull JComponent c)
    {

        AbstractButton button = (AbstractButton) c;

        Color background = c.getBackground();
        if ((background instanceof UIResource) &&
            button.isContentAreaFilled() && c.isEnabled())
        {
            ButtonModel model = button.getModel();
            g.setColor(background);
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
            g.setColor(selectColor/*.brighter()*/);
            g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
            if (model.isSelected() || model.isPressed())
            {
                c.setForeground(selectForegroundColor);
                paint(g, c);
                return;
            }
            else if (model.isRollover())
            {
                c.setForeground(fgColor);
                paint(g, c);
                return;
            }
            else
            {
                c.setForeground(fgColor);
            }
        }
        super.update(g, c);
    }


    protected void paintButtonPressed(@NotNull Graphics g, @NotNull AbstractButton b)
    {
        if (b.isContentAreaFilled())
        {
            g.setColor(selectColor/*.brighter()*/);
            g.fillRect(0, 0, b.getWidth(), b.getHeight());
            g.setColor(selectColor.darker());
            g.drawRect(0, 0, b.getWidth() - 1, b.getHeight() - 1);
        }
    }


    protected void paintFocus(@NotNull Graphics g, @NotNull AbstractButton b, @NotNull Rectangle viewRect, @NotNull Rectangle textRect, @NotNull Rectangle iconRect)
    {
    }


    @NotNull
    public Dimension getPreferredSize(@NotNull JComponent c)
    {
        AbstractButton b = (AbstractButton) c;
        Dimension preferredButtonSize = BasicGraphicsUtils.getPreferredButtonSize(b, b.getIconTextGap());
        return new Dimension(preferredButtonSize.width + 10, preferredButtonSize.height + 10);
    }


}
