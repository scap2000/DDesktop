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
package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 13:15:53
 */
public class CellEditorJTextFieldWithEllipsisPoint2D extends CellEditorJTextFieldWithEllipsis<Point2D>
{
    @NotNull
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.##");


    public CellEditorJTextFieldWithEllipsisPoint2D()
    {
    }


    @NotNull
    public String convertToText(@Nullable Point2D obj)
    {
        if (obj != null)
        {
            return decimalFormat.format(obj.getX()) + "; " + decimalFormat.format(obj.getY());
        }

        return "";
    }


    @NotNull
    public Point2D convertFromText(@NotNull String text) throws IllegalArgumentException
    {
        int index = text.indexOf(';');
        if (index != -1)
        {
            try
            {
                Number n1 = decimalFormat.parse(text.substring(0, index).trim());
                Number n2 = decimalFormat.parse(text.substring(index + 1, text.length()).trim());
                return new Point2D.Double(n1.doubleValue(), n2.doubleValue());
            }
            catch (ParseException e)
            {
                throw new IllegalArgumentException(e);
            }
        }

        throw new IllegalArgumentException();
    }

}
