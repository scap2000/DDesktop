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
package org.pentaho.reportdesigner.lib.client.plugin;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: Martin
 * Date: 11.02.2005
 * Time: 08:23:57
 */
public class ResourceLoader
{
    private ResourceLoader()
    {
    }


    @NotNull
    public static ImageIcon readImageIcon(@NotNull InputStream inputStream)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int length;
        try
        {
            while ((length = inputStream.read(buffer)) != -1)
            {
                byteArrayOutputStream.write(buffer, 0, length);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read image data", e);
        }

        return new ImageIcon(byteArrayOutputStream.toByteArray());
    }


}
