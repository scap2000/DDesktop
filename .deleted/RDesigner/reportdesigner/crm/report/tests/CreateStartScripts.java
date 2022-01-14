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
import org.pentaho.reportdesigner.crm.report.util.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * User: Martin
 * Date: 11.08.2006
 * Time: 09:32:05
 */
@SuppressWarnings({"ALL"})
public class CreateStartScripts
{
    public static void main(@NotNull String[] args) throws IOException
    {
        StringBuilder windows = new StringBuilder("start javaw -cp resources;lib;lib\\ReportDesigner.jar;");

        StringBuilder linux = new StringBuilder("#!/bin/sh\n" +
                                                "\n" +
                                                "if [ \"$LD_LIBRARY_PATH\" = \"\" ]; then\n" +
                                                "     LD_LIBRARY_PATH=/usr/lib/xulrunner-1.8.0.1\n" +
                                                "     export LD_LIBRARY_PATH\n" +
                                                "fi\n" +
                                                "\n" +
                                                "if [ \"$MOZILLA_FIVE_HOME\" = \"\" ]; then\n" +
                                                "     MOZILLA_FIVE_HOME=/usr/lib/xulrunner-1.8.0.1\n" +
                                                "     export MOZILLA_FIVE_HOME\n" +
                                                "fi\n" +
                                                "    \n" +
                                                "java -cp resources:lib:lib/ReportDesigner.jar:");

        StringBuilder osx = new StringBuilder("#!/bin/sh\n" +
                                              "\n" +
                                              "export LD_LIBRARY_PATH=./swt-osx\n" +
                                              "java -XstartOnFirstThread -cp \"resources:lib:lib/ReportDesigner.jar:");

        File libraryDirectory = new File("lib");
        File[] files = libraryDirectory.listFiles(new FilenameFilter()
        {
            public boolean accept(@NotNull File dir, @NotNull String name)
            {
                return name.endsWith(".jar");
            }
        });

        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];
            String filename = file.getName();


            windows.append("lib\\");
            windows.append(filename);

            linux.append("lib/");
            linux.append(filename);

            osx.append("lib/");
            osx.append(filename);

            windows.append(";");
            linux.append(":");
            osx.append(":");
        }

        windows.append("lib\\swt-win32\\swt.jar -Djava.library.path=lib\\swt-win32 org.pentaho.reportdesigner.crm.report.ReportDialog");
        linux.append("lib/swt-linux/linux-swt.jar -Djava.library.path=lib/swt-linux/:/usr/lib/mozilla-1.7.8 org.pentaho.reportdesigner.crm.report.ReportDialog");
        osx.append("lib/swt-osx/swt-osx.jar\" -Djava.library.path=\"lib/swt-osx\" org.pentaho.reportdesigner.crm.report.ReportDialog");

        FileUtils.writeTextToFile(new File("./src/startdesigner.bat"), windows.toString());
        FileUtils.writeTextToFile(new File("./src/startdesigner_linux.sh"), linux.toString());
        FileUtils.writeTextToFile(new File("./src/startdesigner_osx.sh"), osx.toString());

        System.out.println(windows.toString());
        System.out.println();
        System.out.println(linux.toString());
        System.out.println();
        System.out.println(osx.toString());
    }
}
