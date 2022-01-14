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
package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.util.PageFormatFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;


/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 15:46:13
 */
@SuppressWarnings({"ALL"})
public class PageFormatsCreator
{
    public static void main(@NotNull String[] args) throws IllegalAccessException
    {
        ArrayList<String> values = new ArrayList<String>();


        Field[] fields = PageFormatFactory.class.getFields();
        for (Field field : fields)
        {
            if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(int[].class))
            {
                int[] dim = (int[]) field.get(null);
                String s = field.getName() + "(" + dim[0] + ", " + dim[1] + "), ";
                values.add(s);
            }
        }

        Collections.sort(values);

        for (String s : values)
        {
            System.out.println(s);
        }
    }
}
