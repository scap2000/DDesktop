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

import java.util.Locale;
import java.lang.reflect.Field;

/**
 * User: Martin
 * Date: 06.02.2006
 * Time: 13:30:43
 */
public class LocaleConverter implements ObjectConverter
{
    @NotNull
    public Locale getObject(@NotNull String s)
    {
        //noinspection ConstantConditions
        if (s == null)
        {
            throw new IllegalArgumentException("s must not be null");
        }

        int i = s.indexOf('_');
        int i2 = s.indexOf('_', i + 1);
        return new Locale(s.substring(0, i).trim(), s.substring(i + 1, i2).trim(), s.substring(i2 + 1).trim());
    }


    @NotNull
    public String getString(@NotNull Object obj)
    {
        Locale locale = (Locale) obj;
        return locale.getLanguage() + "_" + locale.getCountry() + "_" + locale.getVariant();
    }


    public void configure(@Nullable XMLContext xmlContext, @Nullable Field field)
    {
    }


}
