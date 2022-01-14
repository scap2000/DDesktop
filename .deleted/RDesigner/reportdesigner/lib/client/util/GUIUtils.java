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
package org.pentaho.reportdesigner.lib.client.util;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * User: Martin
 * Date: 07.02.2006
 * Time: 14:17:28
 */
public class GUIUtils
{
    private GUIUtils()
    {
    }


    public static void ensureMinimumDialogWidth(@NotNull Dialog dialog, int minWidth)
    {
        if (dialog.getWidth() < minWidth)
        {
            dialog.setSize(minWidth, dialog.getHeight());
        }
    }


    public static void ensureMinimumDialogHeight(@NotNull Dialog dialog, int minHeight)
    {
        if (dialog.getHeight() < minHeight)
        {
            dialog.setSize(dialog.getWidth(), minHeight);
        }
    }


    public static void ensureMinimumDialogSize(@NotNull Dialog dialog, int minWidth, int minHeight)
    {
        ensureMinimumDialogWidth(dialog, minWidth);
        ensureMinimumDialogHeight(dialog, minHeight);
    }
}
