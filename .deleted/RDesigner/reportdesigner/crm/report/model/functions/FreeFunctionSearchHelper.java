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
package org.pentaho.reportdesigner.crm.report.model.functions;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.function.Expression;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * User: Martin
 * Date: 21.07.2006
 * Time: 07:59:25
 */
@SuppressWarnings({"ALL"})
public class FreeFunctionSearchHelper
{
    public static void main(@NotNull String[] args)
            throws IOException
    {
        JarFile jarFile = new JarFile("./lib/jfreereport-0.8.7-6.jar");
        Enumeration<JarEntry> enumeration = jarFile.entries();
        while (enumeration.hasMoreElements())
        {
            JarEntry jarEntry = enumeration.nextElement();
            if (!jarEntry.getName().contains("/modules/output/") && jarEntry.getName().endsWith(".class"))
            {
                String className = jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - 6);
                //System.out.println("<expression class=\"" + className + "\"></expression>");

                try
                {
                    Class<?> aClass = Class.forName(className);
                    if (Expression.class.isAssignableFrom(aClass) && !Modifier.isAbstract(aClass.getModifiers()))
                    {
                        System.out.println("generateAndAddClassIfPossible(\"" + className + "\", jFreeReportExpressionToWrapperClassesMap);");
                    }
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }
}
