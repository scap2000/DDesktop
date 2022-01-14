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
package org.pentaho.reportdesigner.lib.client.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 08.02.2006
 * Time: 15:46:26
 */
public class MathUtils
{
    public static final double SQRT_OF_2 = Math.sqrt(2);

    private static final double DEFAULT_TOLERANCE = 0.00001;


    private MathUtils()
    {
    }


    public static boolean approxEquals(double d1, double d2, double tolerance)
    {
        return d1 - tolerance < d2 && d1 + tolerance > d2;
    }


    public static boolean approxEquals(double d1, double d2)
    {
        return approxEquals(d1, d2, DEFAULT_TOLERANCE);
    }


    public static boolean approxEquals(@Nullable double[] a1, @Nullable double[] a2)
    {
        if (a1 != null && a2 != null)
        {
            if (a1.length == a2.length)
            {
                for (int i = 0; i < a1.length; i++)
                {
                    if (!MathUtils.approxEquals(a1[i], a2[i]))
                    {
                        return false;
                    }
                }
                return true;
            }
        }

        return a1 == a2;
    }


    public static boolean intersectRectangles(@NotNull Rectangle2D.Double r1, @NotNull Rectangle2D.Double r2, double tolerance)
    {
        return (r2.getX() + r2.getWidth() - r1.getX() > tolerance &&
                r2.getY() + r2.getHeight() - r1.getY() > tolerance &&
                tolerance < r1.getX() + r1.getWidth() - r2.getX() &&
                tolerance < r1.getY() + r1.getHeight() - r2.getY());
    }


    public static boolean intersectRectangles(@NotNull Rectangle2D.Double r1, @NotNull Rectangle2D.Double r2)
    {
        return intersectRectangles(r1, r2, DEFAULT_TOLERANCE);
    }


    public static double truncate(double value, double min, double max)
    {
        return Math.min(max, Math.max(min, value));
    }
}
