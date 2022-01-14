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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 13:15:53
 */
public class CellEditorJTextFieldWithEllipsisURL extends CellEditorJTextFieldWithEllipsis<URL>
{
    public CellEditorJTextFieldWithEllipsisURL()
    {
    }


    @NotNull
    public String convertToText(@Nullable URL obj)
    {
        if (obj != null)
        {
            return obj.toExternalForm();
        }
        return "";
    }


    @Nullable
    public URL convertFromText(@NotNull String text) throws IllegalArgumentException
    {
        if (text.trim().length() == 0)
        {
            return null;
        }

        try
        {
            return new URL(text);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

}
