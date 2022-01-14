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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.util.FileRelativator;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 06.02.2006
 * Time: 13:30:43
 */
public class URLConverter implements ObjectConverter
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(URLConverter.class.getName());

    @Nullable
    private XMLContext xmlContext;


    @NotNull
    public URL getObject(@NotNull String s)
    {
        try
        {
            //noinspection ConstantConditions
            if (s == null)
            {
                throw new IllegalArgumentException("s must not be null");
            }

            try
            {
                return FileRelativator.getAbsoluteURL(xmlContext, s);
            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException("URL can not be read", e);
            }
        }
        finally
        {
            this.xmlContext = null;
        }
    }


    @NotNull
    public String getString(@NotNull Object obj)
    {
        try
        {
            return FileRelativator.getRelativePathFromURL(xmlContext, obj.toString());
        }
        finally
        {
            this.xmlContext = null;
        }
    }


    public void configure(@Nullable XMLContext xmlContext, @Nullable Field field)
    {
        this.xmlContext = xmlContext;
    }
}
