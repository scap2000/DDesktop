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
package org.pentaho.reportdesigner.crm.report.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 09:58:06
 */
public class FieldNameFactory
{
    @NotNull
    private static final FieldNameFactory instance = new FieldNameFactory();


    @NotNull
    public static FieldNameFactory getInstance()
    {
        return instance;
    }


    @NotNull
    private HashMap<String, TreeSet<Integer>> numberRegistry;


    private FieldNameFactory()
    {
        numberRegistry = new HashMap<String, TreeSet<Integer>>();
    }


    public int getNextFreeNumber(@NotNull String prefix)
    {
        @Nullable
        TreeSet<Integer> integers = numberRegistry.get(prefix);
        if (integers == null)
        {
            integers = new TreeSet<Integer>();
            numberRegistry.put(prefix, integers);
        }

        for (int i = 1; i < Integer.MAX_VALUE; i++)
        {
            if (!integers.contains(Integer.valueOf(i)))
            {
                integers.add(Integer.valueOf(i));
                return i;
            }
        }

        return -1;
    }


}
