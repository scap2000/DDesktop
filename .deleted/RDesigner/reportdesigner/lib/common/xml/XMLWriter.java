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
package org.pentaho.reportdesigner.lib.common.xml;

import org.apache.commons.lang.StringEscapeUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * User: Martin
 * Date: 06.02.2006
 * Time: 13:16:17
 */
public class XMLWriter
{
    @NotNull
    private static final String CDATA_START = "<![CDATA[";
    @NotNull
    private static final String CDATA_END = "]]>";


    @SuppressWarnings({"UseOfSystemOutOrSystemErr"})
    public static void main(@NotNull String[] args) throws IOException
    {
        XMLWriter xmlWriter = new XMLWriter(System.out, true);
        xmlWriter.writeDefaultProlog();
        xmlWriter.startElement("PriceCompareDB").startElement("PriceDevelopment");
        xmlWriter.startElement("Price").writeAttribute("amount", "123.98").closeElement("Price");
        xmlWriter.closeElement("PriceDevelopment").closeElement("PriceCompareDB");

        xmlWriter.close();
    }


    @NotNull
    private BufferedWriter bufferedWriter;
    private boolean elementStartOpen;
    private boolean indent;
    private int indetionLevel;
    @NotNull
    private String indentionCharacters;


    public XMLWriter(@NotNull OutputStream outputStream, boolean indent) throws UnsupportedEncodingException
    {
        //noinspection ConstantConditions
        if (outputStream == null)
        {
            throw new IllegalArgumentException("outputStream must not be null");
        }

        //noinspection IOResourceOpenedButNotSafelyClosed
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, XMLConstants.ENCODING));
        elementStartOpen = false;
        this.indent = indent;

        StringBuilder stringBuilder = new StringBuilder(100);
        for (int i = 0; i < 100; i++)
        {
            stringBuilder.append(' ');
        }
        indentionCharacters = stringBuilder.toString();
    }


    public void write(@NotNull @NonNls String value) throws IOException
    {
        bufferedWriter.write(value);
    }


    @NotNull
    public XMLWriter startElement(@NotNull @NonNls String name) throws IOException
    {
        indetionLevel++;

        if (elementStartOpen)
        {
            bufferedWriter.write('>');
            bufferedWriter.write('\n');
            elementStartOpen = false;
        }
        writeIndentation();
        bufferedWriter.write('<');
        bufferedWriter.write(name);
        elementStartOpen = true;
        return this;
    }


    @NotNull
    public XMLWriter closeElement(@NotNull @NonNls String name) throws IOException
    {
        if (elementStartOpen)
        {
            bufferedWriter.write('>');
            //bufferedWriter.write('\n');
            elementStartOpen = false;
        }
        else
        {
            writeIndentation();
        }
        bufferedWriter.write("</");
        bufferedWriter.write(name);
        bufferedWriter.write('>');
        bufferedWriter.write('\n');

        indetionLevel--;

        return this;
    }


    @NotNull
    public XMLWriter writeContent(@NotNull @NonNls String value) throws IOException
    {
        if (elementStartOpen)
        {
            bufferedWriter.write('>');
            bufferedWriter.write('\n');
            elementStartOpen = false;
        }
        indetionLevel++;
        writeIndentation();
        indetionLevel--;

        bufferedWriter.write(StringEscapeUtils.escapeXml(value));
        bufferedWriter.write('\n');

        return this;
    }


    private void writeIndentation() throws IOException
    {
        if (indent && indetionLevel > 0 && indetionLevel < 100)
        {
            bufferedWriter.write(indentionCharacters, 0, (indetionLevel - 1) * 4);
        }
    }


    @NotNull
    public XMLWriter writeNamelessProperty(@NotNull @NonNls String name, @NotNull @NonNls String value) throws IOException
    {
        startElement(name);

        if (elementStartOpen)
        {
            bufferedWriter.write('>');
            elementStartOpen = false;
        }

        bufferedWriter.write(StringEscapeUtils.escapeXml(value));

        bufferedWriter.write("</");
        bufferedWriter.write(name);
        bufferedWriter.write('>');
        bufferedWriter.write('\n');

        indetionLevel--;

        return this;
    }


    @NotNull
    public XMLWriter writeProperty(@NotNull @NonNls String name, @NotNull @NonNls String value) throws IOException
    {
        startElement(XMLConstants.PROPERTY);
        writeAttribute(XMLConstants.NAME, name);

        if (elementStartOpen)
        {
            bufferedWriter.write('>');
            elementStartOpen = false;
        }

        bufferedWriter.write(StringEscapeUtils.escapeXml(value));

        bufferedWriter.write("</");
        bufferedWriter.write(XMLConstants.PROPERTY);
        bufferedWriter.write('>');
        bufferedWriter.write('\n');

        indetionLevel--;

        return this;
    }


    @NotNull
    public XMLWriter writeProperty(@NotNull @NonNls String name, @NotNull @NonNls String type, @NotNull @NonNls String value) throws IOException
    {
        startElement(XMLConstants.PROPERTY);
        writeAttribute(XMLConstants.NAME, name);
        writeAttribute(XMLConstants.TYPE, type);

        if (elementStartOpen)
        {
            bufferedWriter.write('>');
            elementStartOpen = false;
        }

        bufferedWriter.write(StringEscapeUtils.escapeXml(value));

        bufferedWriter.write("</");
        bufferedWriter.write(XMLConstants.PROPERTY);
        bufferedWriter.write('>');
        bufferedWriter.write('\n');

        indetionLevel--;

        return this;
    }

    //public XMLWriter writeText(String text) throws IOException
    //{
    //    if (elementStartOpen)
    //    {
    //        bufferedWriter.write('>');
    //        bufferedWriter.write('\n');
    //        elementStartOpen = false;
    //    }
    //    else
    //    {
    //        throw new IllegalStateException("element has to be open to write text");
    //    }
    //    writeIndentation();
    //    bufferedWriter.write(StringEscapeUtils.escapeXml(text));
    //    bufferedWriter.write('\n');
    //    return this;
    //}


    @NotNull
    public XMLWriter writeAttribute(@NotNull @NonNls String name, @NotNull @NonNls String value) throws IOException
    {
        if (!elementStartOpen)
        {
            throw new IllegalStateException("element is already closed");
        }
        bufferedWriter.write(' ');
        bufferedWriter.write(name);
        bufferedWriter.write('=');
        bufferedWriter.write("\"");
        bufferedWriter.write(StringEscapeUtils.escapeXml(value));
        bufferedWriter.write("\"");
        return this;
    }


    @NotNull
    public XMLWriter writeCData(@NotNull @NonNls String value) throws IOException
    {
        if (elementStartOpen)
        {
            bufferedWriter.write('>');
            bufferedWriter.write('\n');
            elementStartOpen = false;
        }
        indetionLevel++;
        writeIndentation();
        indetionLevel--;

        bufferedWriter.write(CDATA_START);
        bufferedWriter.write(value);
        bufferedWriter.write(CDATA_END);
        bufferedWriter.write('\n');

        return this;
    }


    public void flush() throws IOException
    {
        bufferedWriter.flush();
    }


    public void close() throws IOException
    {
        if (elementStartOpen)
        {
            throw new IllegalStateException("element is still open");
        }
        bufferedWriter.close();
    }


    public void writeDefaultProlog() throws IOException
    {
        write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    }


}
