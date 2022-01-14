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
package org.pentaho.reportdesigner.crm.report;

import org.jetbrains.annotations.NotNull;

/**
 * User: Martin
 * Date: 07.02.2006
 * Time: 20:14:37
 */
public enum Unit
{
    @NotNull POINTS(1),
    @NotNull PICA(12),
    @NotNull MM(72. / (2.54 * 10)),
    @NotNull CM(72. / 2.54),
    @NotNull INCH(72);

    private double dotsPerUnit;


    private Unit(double dotsPerUnit)
    {
        this.dotsPerUnit = dotsPerUnit;
    }


    public double getDotsPerUnit()
    {
        return dotsPerUnit;
    }


    public double convertFromPoints(double points)
    {
        return points / dotsPerUnit;
    }


    public double convertToPoints(double unit)
    {
        return unit * dotsPerUnit;
    }

}
