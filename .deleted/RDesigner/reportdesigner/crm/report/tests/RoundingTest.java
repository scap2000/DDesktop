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

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * User: Martin
 * Date: 26.06.2006
 * Time: 17:36:31
 */
@SuppressWarnings({"ALL"})
public class RoundingTest
{
    public static void main(@NotNull String[] args)
    {
        Double d1 = new Double(2.0);
        Double d2 = new Double(1.1);

        Double result = new Double(d1.doubleValue() - d2.doubleValue());
        System.out.println("result = " + result);//NON-NLS

        BigDecimal bd1 = new BigDecimal("2");
        BigDecimal bd2 = new BigDecimal("1.1");
        BigDecimal bd3 = new BigDecimal("3");

        BigDecimal bdResult = bd1.subtract(bd2);
        System.out.println("bdResult = " + bdResult);//NON-NLS

        BigDecimal bdResult2 = bd1.divide(bd3, RoundingMode.HALF_UP);
        System.out.println("bdResult2 = " + bdResult2);//NON-NLS

    }
}
