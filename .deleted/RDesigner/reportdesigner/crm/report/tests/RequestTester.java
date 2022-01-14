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

import java.io.InputStream;
import java.net.Socket;

/**
 * User: Martin
 * Date: 13.08.2006
 * Time: 13:21:40
 */

@SuppressWarnings({"ALL"})
public class RequestTester
{
    public static void main(@NotNull String[] args) throws Exception
    {
        Socket s = new Socket("localhost", 15657);
        s.getOutputStream().write("GET ../../../../../../../pagefile.sys HTTP/1.1\n".getBytes());
        int len;
        InputStream is = s.getInputStream();
        byte[] buffer = new byte[8192];
        while ((len = is.read(buffer)) != -1)
        {
            System.out.println(new String(buffer, 0, len));
        }
    }
}
