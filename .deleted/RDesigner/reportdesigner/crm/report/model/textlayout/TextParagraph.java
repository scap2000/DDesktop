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
package org.pentaho.reportdesigner.crm.report.model.textlayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * User: Martin
 * Date: 17.02.2006
 * Time: 08:42:29
 */
public class TextParagraph
{
    @NotNull
    public static ArrayList<TextParagraph> breakStringIntoParagraphs(@NotNull String text, boolean preserveWhitespace)
    {
        ArrayList<TextParagraph> textParagraphs = new ArrayList<TextParagraph>();
        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine())
        {
            TextParagraph textParagraph = new TextParagraph(scanner.nextLine());
            textParagraphs.add(textParagraph);
        }

        return textParagraphs;
    }


    @NotNull
    private String content;


    public TextParagraph(@NotNull String content)
    {
        this.content = content;
    }


    @NotNull
    public String getContent()
    {
        return content;
    }
}
