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
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.FontUtils;

import java.awt.*;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 07:24:52
 */
public class CellEditorJLabelWithEllipsisFont extends CellEditorJLabelWithEllipsis<Font>
{
    @NotNull
    private Font tableFont;


    public CellEditorJLabelWithEllipsisFont(@NotNull Font tableFont)
    {
        this.tableFont = tableFont;
    }


    public void setValue(@Nullable Font font, boolean commit)
    {
        super.setValue(font, commit);

        Font f = getValue();
        if (f != null && font != null)
        {
            if (f.getSize() > tableFont.getSize())
            {
                f = FontUtils.getDerivedFont(font, font.getStyle(), tableFont.getSize());
            }

            label.setFont(f);
        }
    }


    @Nullable
    public String convertToText(@Nullable Font font)
    {
        if (font == null)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder(font.getName());
        sb.append(" ");
        sb.append(font.getSize());
        if ((font.getStyle() & Font.BOLD) == Font.BOLD)
        {
            sb.append(" ");
            sb.append(TranslationManager.getInstance().getTranslation("R", "CellEditorJLabelWithEllipsisFont.Suffix.Bold"));
        }
        if ((font.getStyle() & Font.ITALIC) == Font.ITALIC)
        {
            sb.append(" ");
            sb.append(TranslationManager.getInstance().getTranslation("R", "CellEditorJLabelWithEllipsisFont.Suffix.Italic"));
        }

        return sb.toString();
    }
}
