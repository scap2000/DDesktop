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
package org.pentaho.reportdesigner.lib.common.xml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * User: Martin
 * Date: 07.02.2006
 * Time: 09:36:24
 */
public class FontConverter implements ObjectConverter
{
    @NotNull
    public Font getObject(@NotNull String s)
    {
        //noinspection ConstantConditions
        if (s == null)
        {
            throw new IllegalArgumentException("s must not be null");
        }

        int i = s.indexOf(',');
        int i2 = s.indexOf(',', i + 1);
        return new Font(s.substring(0, i).trim(), Integer.parseInt(s.substring(i2 + 1).trim()), Integer.parseInt(s.substring(i + 1, i2).trim()));
    }


    @NotNull
    public String getString(@NotNull Object p)
    {
        Font f = (Font) p;
        return f.getName() + "," + f.getSize() + "," + f.getStyle();
    }


    public void configure(@Nullable XMLContext xmlContext, @Nullable Field field)
    {
    }
}
