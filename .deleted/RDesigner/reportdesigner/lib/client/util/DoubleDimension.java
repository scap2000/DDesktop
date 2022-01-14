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

import java.awt.geom.Dimension2D;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 18:06:10
 */
public class DoubleDimension extends Dimension2D
{
    private double width;
    private double height;


    public DoubleDimension()
    {
        this(0, 0);
    }


    public DoubleDimension(@NotNull DoubleDimension doubleDimension)
    {
        this(doubleDimension.width, doubleDimension.height);
    }


    public DoubleDimension(double width, double height)
    {
        this.width = width;
        this.height = height;
    }


    public double getWidth()
    {
        return width;
    }


    public double getHeight()
    {
        return height;
    }


    public void setWidth(double width)
    {
        this.width = width;
    }


    public void setHeight(double height)
    {
        this.height = height;
    }


    public void setSize(double width, double height)
    {
        this.width = width;
        this.height = height;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DoubleDimension that = (DoubleDimension) o;

        if (!MathUtils.approxEquals(that.height, height)) return false;
        return MathUtils.approxEquals(that.width, width);
    }


    public int hashCode()
    {
        int result;
        long temp;
        temp = width != +0.0d ? Double.doubleToLongBits(width) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = height != +0.0d ? Double.doubleToLongBits(height) : 0L;
        result = 29 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @NotNull
    public String toString()
    {
        return "DoubleDimension{" +
               "width=" + width +
               ", height=" + height +
               "}";
    }
}
