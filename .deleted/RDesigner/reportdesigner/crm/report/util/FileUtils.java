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
import org.pentaho.reportdesigner.lib.client.util.IOUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * User: Martin
 * Date: 11.08.2006
 * Time: 09:44:12
 */
public class FileUtils
{
    private FileUtils()
    {
    }


    public static void writeTextToFile(@NotNull File file, @NotNull String text, @NotNull String charset) throws IOException
    {
        BufferedWriter bw = null;
        try
        {
            //noinspection IOResourceOpenedButNotSafelyClosed
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
            bw.write(text);
        }
        finally
        {
            IOUtil.closeStream(bw);
        }
    }


    public static void writeTextToFile(@NotNull File file, @NotNull String text) throws IOException
    {
        BufferedWriter bw = null;
        try
        {
            //noinspection IOResourceOpenedButNotSafelyClosed
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bw.write(text);
        }
        finally
        {
            IOUtil.closeStream(bw);
        }
    }

}
