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
package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

/**
 * User: Martin
 * Date: 10.08.2006
 * Time: 15:34:00
 */
@SuppressWarnings({"ALL"})
public class PropertySorter
{
    public static void main(@NotNull String[] args) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(PropertySorter.class.getResourceAsStream("/res/Translations.properties")));
        String line;
        TreeSet<String> strings = new TreeSet<String>();

        while ((line = br.readLine()) != null)
        {
            strings.add(line);
        }

        for (String s : strings)
        {
            System.out.println(s);
        }
    }
}
