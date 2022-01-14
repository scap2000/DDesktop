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

/**
 * User: Martin
 * Date: 12.01.2006
 * Time: 14:28:14
 */
public class CellEditorJTextFieldAreaWithEllipsis extends CellEditorJTextFieldWithEllipsis<String>
{


    public CellEditorJTextFieldAreaWithEllipsis()
    {
    }


    @Nullable
    public String convertFromText(@NotNull String text)
    {
        return text.replace('\u00AC', '\n');
    }


    @NotNull
    public String convertToText(@Nullable String obj)
    {
        if (obj != null)
        {
            return obj.replace('\n', '\u00AC');
        }
        else
        {
            return "";
        }
    }
}
