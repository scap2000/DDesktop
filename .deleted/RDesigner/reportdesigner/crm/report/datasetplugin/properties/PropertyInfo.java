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
package org.pentaho.reportdesigner.crm.report.datasetplugin.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Martin
 * Date: 16.02.2006
 * Time: 16:16:32
 */
public class PropertyInfo implements Comparable
{
    @NotNull
    private String key;
    @NotNull
    private Class clazz;
    @Nullable
    private Object value;


    public PropertyInfo(@NotNull String key, @NotNull Class clazz, @Nullable Object value)
    {
        this.key = key;
        this.clazz = clazz;
        this.value = value;
    }


    @NotNull
    public String getKey()
    {
        return key;
    }


    public void setKey(@NotNull String key)
    {
        this.key = key;
    }


    @NotNull
    public Class getClazz()
    {
        return clazz;
    }


    public void setClazz(@NotNull Class clazz)
    {
        this.clazz = clazz;
    }


    @Nullable
    public Object getValue()
    {
        return value;
    }


    public void setValue(@Nullable Object value)
    {
        this.value = value;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyInfo that = (PropertyInfo) o;

        if (!clazz.equals(that.clazz)) return false;
        return key.equals(that.key);
    }


    public int hashCode()
    {
        int result;
        result = key.hashCode();
        result = 31 * result + clazz.hashCode();
        return result;
    }


    public int compareTo(@Nullable Object o)
    {
        if (o instanceof PropertyInfo)
        {
            PropertyInfo propertyInfo = (PropertyInfo) o;
            return key.compareTo(propertyInfo.key);
        }
        return 0;
    }
}
