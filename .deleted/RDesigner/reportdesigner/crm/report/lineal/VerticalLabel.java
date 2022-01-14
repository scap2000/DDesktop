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
package org.pentaho.reportdesigner.crm.report.lineal;

import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.docking.IconCreator;
import org.pentaho.reportdesigner.lib.client.components.docking.RotateTextIcon;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 27.01.2006
 * Time: 08:37:48
 */
public class VerticalLabel extends JLabel
{
    @Nullable
    private String text;


    public VerticalLabel(@Nullable String text)
    {
        this.text = text;
        if (text != null && getFont() != null)
        {
            ImageIcon imageIcon = IconCreator.createRotatedTextIcon(Color.GRAY, RotateTextIcon.CCW, getFont(), text);
            setIcon(imageIcon);
        }
    }


    public void setFont(@Nullable Font font)
    {
        super.setFont(font);
        String text = this.text;
        if (text != null && getFont() != null)
        {
            ImageIcon imageIcon = IconCreator.createRotatedTextIcon(Color.GRAY, RotateTextIcon.CCW, getFont(), text);
            setIcon(imageIcon);
        }
    }


    public void setText(@Nullable String text)
    {
        super.setText(null);
        this.text = text;
        if (text != null && getFont() != null)
        {
            ImageIcon imageIcon = IconCreator.createRotatedTextIcon(Color.GRAY, RotateTextIcon.CCW, getFont(), text);
            setIcon(imageIcon);
        }
    }

}
