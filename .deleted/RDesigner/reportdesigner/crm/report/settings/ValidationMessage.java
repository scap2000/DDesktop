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
package org.pentaho.reportdesigner.crm.report.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Martin
 * Date: 01.03.2006
 * Time: 18:14:22
 */
public class ValidationMessage
{
    @NotNull
    public static final ValidationMessage[] EMPTY_ARRAY = new ValidationMessage[0];

    public enum Severity
    {
        @NotNull OK,
        @NotNull WARN,
        @NotNull ERROR
    }

    @NotNull
    private Severity severity;
    @NotNull
    private String key;
    @Nullable
    private String text;


    public ValidationMessage(@NotNull Severity severity, @NotNull String key)
    {
        this(severity, key, null);
    }


    public ValidationMessage(@NotNull Severity severity, @NotNull String key, @Nullable String text)
    {
        this.severity = severity;
        this.key = key;
        this.text = text;
    }


    @NotNull
    public Severity getSeverity()
    {
        return severity;
    }


    @NotNull
    public String getKey()
    {
        return key;
    }


    @Nullable
    public String getText()
    {
        return text;
    }
}
