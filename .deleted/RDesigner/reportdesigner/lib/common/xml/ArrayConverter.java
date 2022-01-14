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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 06.02.2006
 * Time: 19:44:32
 */
public class ArrayConverter implements ObjectConverter
{
    @NotNull
    private Class<?> arrayClazz;
    @NotNull
    private ObjectConverter objectConverter;


    public ArrayConverter(@NotNull Class<?> arrayClazz, @NotNull ObjectConverter objectConverter)
    {
        this.arrayClazz = arrayClazz;
        this.objectConverter = objectConverter;
    }


    @NotNull
    public Object getObject(@NotNull String s)
    {
        //noinspection ConstantConditions
        if (s == null)
        {
            throw new IllegalArgumentException("s must not be null");
        }

        ArrayList<Object> al = new ArrayList<Object>();
        int indexStart = 0;
        int index = -1;
        while ((index = s.indexOf('\u00AC', index + 1)) != -1)
        {
            al.add(objectConverter.getObject(s.substring(indexStart, index)));
            indexStart = index;
        }
        return al.toArray((Object[]) Array.newInstance(arrayClazz.getComponentType(), al.size()));
    }


    @NotNull
    public String getString(@NotNull Object p)
    {
        StringBuilder sb = new StringBuilder(32);
        Object[] objArray = (Object[]) p;
        for (int i = 0; i < objArray.length; i++)
        {
            Object o = objArray[i];
            sb.append(objectConverter.getString(o));
            if (i < objArray.length - 1)
            {
                sb.append('\u00AC');
            }
        }
        return sb.toString();
    }


    public void configure(@Nullable XMLContext xmlContext, @Nullable Field field)
    {
    }
}
