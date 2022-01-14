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

import javax.swing.border.AbstractBorder;
import java.awt.*;


public class FlatBorder extends AbstractBorder
{
    private boolean lowered;


    public FlatBorder()
    {
    }


    public FlatBorder(boolean lowered)
    {
        this.lowered = lowered;
    }


    @NotNull
    public Insets getBorderInsets(@NotNull Component c)
    {
        return new Insets(1, 1, 1, 1);
    }


    public void paintBorder(@NotNull Component c, @NotNull Graphics g, int x, int y, int width, int height)
    {
        if (lowered)
        {
            g.setColor(Color.DARK_GRAY);
            g.drawLine(x, y, x + width - 1, y);
            g.drawLine(x, y, x, y + height - 1);
            g.setColor(Color.white);
            g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
            g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
        }
        else
        {
            g.setColor(Color.white);
            g.drawLine(x, y, x + width - 1, y);
            g.drawLine(x, y, x, y + height - 1);
            g.setColor(Color.DARK_GRAY);
            g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
            g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
        }
    }
}