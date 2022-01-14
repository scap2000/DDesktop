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
package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 13:15:53
 */
public class CellEditorJTextFieldWithEllipsisColor extends CellEditorJTextFieldWithEllipsis<Color>
{
    public CellEditorJTextFieldWithEllipsisColor()
    {
    }


    @NotNull
    public String convertToText(@Nullable Color obj)
    {
        if (obj != null)
        {
            return obj.getRed() + "; " + obj.getGreen() + "; " + obj.getBlue();
        }
        return "";
    }


    @Nullable
    public Color convertFromText(@NotNull String text) throws IllegalArgumentException
    {
        if (text.trim().length() == 0)
        {
            return null;
        }

        int index = text.indexOf(';');
        int index2 = text.lastIndexOf(';');

        if (index != -1 && index2 != -1)
        {
            try
            {
                int n1 = Integer.parseInt(text.substring(0, index).trim());
                int n2 = Integer.parseInt(text.substring(index + 1, index2).trim());
                int n3 = Integer.parseInt(text.substring(index2 + 1, text.length()).trim());

                return new Color(n1, n2, n3);
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException(e);
            }
        }

        throw new IllegalArgumentException();
    }

}
