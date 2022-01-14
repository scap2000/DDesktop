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
import org.pentaho.reportdesigner.crm.report.model.ElementPadding;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 13:15:53
 */
public class CellEditorJTextFieldWithEllipsisPadding extends CellEditorJTextFieldWithEllipsis<ElementPadding>
{
    @NotNull
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.##");


    public CellEditorJTextFieldWithEllipsisPadding()
    {
    }


    @NotNull
    public String convertToText(@Nullable ElementPadding obj)
    {
        if (obj != null)
        {
            return decimalFormat.format(obj.getTop()) + "; " + decimalFormat.format(obj.getBottom()) + "; " + decimalFormat.format(obj.getLeft()) + "; " + decimalFormat.format(obj.getRight());
        }

        return "";
    }


    @NotNull
    public ElementPadding convertFromText(@NotNull String text) throws IllegalArgumentException
    {
        try
        {
            StringTokenizer strTok = new StringTokenizer(text, ";");
            Number n1 = decimalFormat.parse(strTok.nextToken().trim());
            Number n2 = decimalFormat.parse(strTok.nextToken().trim());
            Number n3 = decimalFormat.parse(strTok.nextToken().trim());
            Number n4 = decimalFormat.parse(strTok.nextToken().trim());

            return new ElementPadding(n1.doubleValue(), n2.doubleValue(), n3.doubleValue(), n4.doubleValue());
        }
        catch (NoSuchElementException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (ParseException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

}