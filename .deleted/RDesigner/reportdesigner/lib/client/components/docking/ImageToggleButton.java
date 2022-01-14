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
import javax.swing.plaf.ButtonUI;
import java.awt.*;

/**
 * User: Martin
 * Date: 24.03.2005
 * Time: 13:50:20
 */
public class ImageToggleButton extends JToggleButton
{

    @NotNull
    private ImageIcon imageIcon;
    @NotNull
    private String text;
    @NotNull
    private ToolWindow.Alignment alignment;


    public ImageToggleButton(@NotNull ImageIcon icon, @NotNull String text)
    {
        super();
        this.imageIcon = icon;
        this.text = text;

        setMargin(new Insets(1, 1, 1, 1));

        setFont(getFont().deriveFont(Font.PLAIN));

        alignment = ToolWindow.Alignment.LEFT;
        setAlignmentX(ToolWindow.Alignment.LEFT);

        setFocusable(false);
    }


    public void setAlignmentX(@NotNull ToolWindow.Alignment alignment)
    {
        this.alignment = alignment;

        switch (alignment)
        {
            case TOP:
            case BOTTOM:
            {
                ImageIcon icon = IconCreator.createRotatedTextIcon(getForeground(), RotateTextIcon.NONE, getFont(), text);
                icon = IconCreator.createComposedImageIcon(this.imageIcon, icon, IconCreator.FirstIconAlignment.LEFT);
                setIcon(icon);
                break;
            }
            case LEFT:
            {
                ImageIcon icon = IconCreator.createRotatedTextIcon(getForeground(), RotateTextIcon.CCW, getFont(), text);
                icon = IconCreator.createComposedImageIcon(this.imageIcon, icon, IconCreator.FirstIconAlignment.BOTTOM);
                setIcon(icon);
                break;
            }
            case RIGHT:
            {
                ImageIcon icon = IconCreator.createRotatedTextIcon(getForeground(), RotateTextIcon.CW, getFont(), text);
                icon = IconCreator.createComposedImageIcon(this.imageIcon, icon, IconCreator.FirstIconAlignment.TOP);
                setIcon(icon);
                break;
            }
        }
    }


    public void setUI(@NotNull ButtonUI ui)
    {
        super.setUI(ui);

        if (alignment != null)
        {
            setAlignmentX(alignment);
        }
    }
}