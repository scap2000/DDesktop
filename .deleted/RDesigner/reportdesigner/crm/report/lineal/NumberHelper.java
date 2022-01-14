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
package org.pentaho.reportdesigner.crm.report.lineal;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.Unit;

/**
 * User: Martin
 * Date: 07.02.2006
 * Time: 20:48:40
 */
public class NumberHelper
{
    private NumberHelper()
    {
    }


    public static double getFactorForUnitAndScale(@NotNull Unit unit, double sf)
    {
        switch (unit)
        {
            case POINTS:
            {
                if (sf < 0.2)
                {
                    return 200;
                }
                else if (sf < 0.5)
                {
                    return 100;
                }
                else if (sf < 2)
                {
                    return 50;
                }
                else if (sf < 3)
                {
                    return 20;
                }
                else
                {
                    return 10;
                }
            }
            case INCH:
            {
                if (sf < 0.2)
                {
                    return 2;
                }
                else if (sf < 0.5)
                {
                    return 1;
                }
                else if (sf < 2)
                {
                    return 0.5;
                }
                else if (sf < 3)
                {
                    return 0.2;
                }
                else
                {
                    return 0.1;
                }
            }
            case CM:
            {
                if (sf < 0.2)
                {
                    return 5;
                }
                else if (sf < 0.5)
                {
                    return 2;
                }
                else if (sf < 2)
                {
                    return 1;
                }
                else if (sf < 4)
                {
                    return 0.5;
                }
                else
                {
                    return 0.2;
                }
            }
            case MM:
            {
                if (sf < 0.5)
                {
                    return 50;
                }
                else if (sf < 1)
                {
                    return 20;
                }
                else if (sf < 3)
                {
                    return 10;
                }
                else if (sf < 4)
                {
                    return 5;
                }
                else
                {
                    return 2;
                }
            }
            case PICA:
            {
                if (sf < 0.2)
                {
                    return 20;
                }
                else if (sf < 0.5)
                {
                    return 10;
                }
                else if (sf < 2)
                {
                    return 5;
                }
                else if (sf < 3)
                {
                    return 2;
                }
                else
                {
                    return 1;
                }
            }
        }

        return 1;
    }
}
