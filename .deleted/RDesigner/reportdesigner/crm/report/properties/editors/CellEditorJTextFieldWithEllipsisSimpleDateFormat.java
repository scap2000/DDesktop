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

import java.text.SimpleDateFormat;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 13:15:53
 */
public class CellEditorJTextFieldWithEllipsisSimpleDateFormat extends CellEditorJTextFieldWithEllipsis<SimpleDateFormat>
{
    public CellEditorJTextFieldWithEllipsisSimpleDateFormat()
    {
    }


    @NotNull
    public String convertToText(@Nullable SimpleDateFormat obj)
    {
        if (obj != null)
        {
            return obj.toPattern();
        }

        return "";
    }


    @NotNull
    public SimpleDateFormat convertFromText(@NotNull String text) throws IllegalArgumentException
    {
        return new SimpleDateFormat(text);
    }

}
