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
package org.pentaho.jfreereport.legacy;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.Expression;

import java.util.Date;


public class GetCurrentDateFunction extends AbstractFunction
{


    public GetCurrentDateFunction()
    {
    }


    public GetCurrentDateFunction(@NotNull final String name)
    {
        this();
        setName(name);
    }


    @NotNull
    public Object getValue()
    {
        return new Date();
    }


    @NotNull
    public Expression getInstance()
    {
        final GetCurrentDateFunction function = (GetCurrentDateFunction) super.getInstance();
        return function;
    }

}
