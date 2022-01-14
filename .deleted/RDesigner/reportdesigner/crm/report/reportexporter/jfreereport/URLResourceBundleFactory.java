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
package org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.ResourceBundleFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * User: Martin
 * Date: 10.03.2006
 * Time: 10:42:17
 */
public class URLResourceBundleFactory implements ResourceBundleFactory
{
    @NotNull
    private Locale locale;
    @NotNull
    private URLClassLoader urlClassLoader;


    public URLResourceBundleFactory(@NotNull Locale locale, @NotNull URL url)
    {
        this.locale = locale;
        urlClassLoader = new URLClassLoader(new URL[]{url});
    }


    @NotNull
    public ResourceBundle getResourceBundle(@NotNull String key)
    {
        ResourceBundle bundle = ResourceBundle.getBundle(key, locale, urlClassLoader);
        return bundle;
    }


    @NotNull
    public Locale getLocale()
    {
        return locale;
    }
}
