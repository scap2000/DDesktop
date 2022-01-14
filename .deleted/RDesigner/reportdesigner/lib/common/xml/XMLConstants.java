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

/**
 * User: Martin
 * Date: 09.02.2006
 * Time: 08:16:02
 */
public class XMLConstants
{
    @NotNull
    public static final String ENCODING = "UTF-8";

    @NotNull
    public static final String REPORT = "report";
    @NotNull
    public static final String SUBREPORT = "subreport";
    @NotNull
    public static final String ARRAY = "array";
    @NotNull
    public static final String APPLICATION_SETTINGS = "applicationSettings";
    @NotNull
    public static final String WORKSPACE_SETTINGS = "workspaceSettings";
    @NotNull
    public static final String KEY = "key";
    @NotNull
    public static final String VALUE = "value";
    @NotNull
    public static final String TYPE = "type";


    private XMLConstants()
    {
    }


    @NotNull
    public static final String PROPERTY = "property";
    @NotNull
    public static final String NAME = "name";
}
