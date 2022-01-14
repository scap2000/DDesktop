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
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.lang.reflect.Field;

/**
 * User: Martin
 * Date: 06.02.2006
 * Time: 13:30:43
 */
public class DoubleDimensionConverter implements ObjectConverter
{
    @NotNull
    public DoubleDimension getObject(@NotNull String s)
    {
        //noinspection ConstantConditions
        if (s == null)
        {
            throw new IllegalArgumentException("s must not be null");
        }

        int i = s.indexOf(',');
        double d1 = Double.parseDouble(s.substring(0, i).trim());
        double d2 = Double.parseDouble(s.substring(i + 1).trim());
        return new DoubleDimension(d1, d2);
    }


    @NotNull
    public String getString(@NotNull Object obj)
    {
        DoubleDimension p = (DoubleDimension) obj;
        return p.getWidth() + ", " + p.getHeight();
    }


    public void configure(@Nullable XMLContext xmlContext, @Nullable Field field)
    {
    }

}
