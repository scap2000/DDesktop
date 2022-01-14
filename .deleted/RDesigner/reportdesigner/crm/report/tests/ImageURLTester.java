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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * User: Martin
 * Date: 04.08.2006
 * Time: 10:15:26
 */
@SuppressWarnings({"ALL"})
public class ImageURLTester
{
    public static void main(@NotNull String[] args)
            throws IOException, URISyntaxException
    {
        File file = new File("C:\\temp\\chess.jpg");
        URL url = file.toURI().toURL();
        System.out.println("url = " + url);

        File file2 = new File(url.toURI());
        System.out.println("file2 = " + file2);

        URL url2 = new URL("file", "", "chess.jpg");
        System.out.println("url2 = " + url2);
        InputStream inputStream = url2.openStream();
    }
}
