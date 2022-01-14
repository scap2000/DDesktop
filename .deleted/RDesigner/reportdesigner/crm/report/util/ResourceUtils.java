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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * User: Martin
 * Date: 01.03.2006
 * Time: 14:26:58
 */
public class ResourceUtils
{
    private ResourceUtils()
    {
    }


    @NotNull
    public static String readString(@NonNls @NotNull String resource)
    {
        BufferedReader br = null;
        try
        {
            InputStream resourceAsStream = ResourceUtils.class.getResourceAsStream(resource);
            //noinspection IOResourceOpenedButNotSafelyClosed
            br = new BufferedReader(new InputStreamReader(resourceAsStream));
            StringBuilder text = new StringBuilder(100);
            String line;
            while ((line = br.readLine()) != null)
            {
                text.append(line);
                text.append('\n');
            }

            return text.toString();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            IOUtil.closeStream(br);
        }
    }
}
