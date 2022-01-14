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
package org.pentaho.reportdesigner.crm.report.settings;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 03.03.2006
 * Time: 14:26:38
 */
public class ValidationUtils
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ValidationUtils.class.getName());


    private ValidationUtils()
    {
    }


    public static boolean isNumber(@NotNull String s)
    {
        try
        {
            Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ValidationUtils.isNumber ", e);
            return false;
        }

        return true;
    }
}
