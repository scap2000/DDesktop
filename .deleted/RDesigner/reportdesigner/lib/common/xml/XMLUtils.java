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

import org.gjt.xpp.XmlPullNode;
import org.gjt.xpp.XmlPullParser;
import org.gjt.xpp.XmlPullParserException;
import org.gjt.xpp.XmlPullParserFactory;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * User: Martin
 * Date: 08.02.2006
 * Time: 09:36:26
 */
public class XMLUtils
{
    private XMLUtils()
    {
    }


    @SuppressWarnings({"HardCodedStringLiteral", "UseOfSystemOutOrSystemErr"})
    public static void main(@NotNull String[] args) throws IOException, XmlPullParserException
    {
        //write
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLWriter xmlWriter0 = new XMLWriter(baos, true);
        xmlWriter0.writeDefaultProlog();
        xmlWriter0.startElement("test");
        xmlWriter0.writeProperty("text", "bla\neek\ncool");
        xmlWriter0.writeProperty("hugo", "");
        xmlWriter0.closeElement("test");
        xmlWriter0.close();

        String s = baos.toString(XMLConstants.ENCODING);
        System.out.println(s);

        //read
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
        //noinspection IOResourceOpenedButNotSafelyClosed
        xmlPullParser.setInput(new BufferedReader(new StringReader(s)));
        xmlPullParser.next(); // get first start tag
        XmlPullNode node = xmlPullParserFactory.newPullNode(xmlPullParser);

        if ("test".equals(node.getRawName()))
        {
            while (!node.isFinished())
            {
                Object childNodeList = node.readNextChild();
                if (childNodeList instanceof XmlPullNode)
                {
                    XmlPullNode child = (XmlPullNode) childNodeList;
                    if ("text".equals(child.getAttributeValueFromRawName(XMLConstants.NAME)))
                    {
                        String property = readProperty("text", child);
                        System.out.println("property = " + property);
                    }
                    else if ("hugo".equals(child.getAttributeValueFromRawName(XMLConstants.NAME)))
                    {
                        String property = readProperty("hugo", child);
                        System.out.println("property = " + property);
                    }

                }
            }
        }

        node.resetPullNode();
    }


    @NotNull
    public static String readProperty(@NotNull String name/*TODO remove?*/, @NotNull XmlPullNode node) throws IOException, XmlPullParserException
    {
        String rawName = node.getRawName();
        if (XMLConstants.PROPERTY.equals(rawName))
        {
            while (!node.isFinished())
            {
                Object childNodeList = node.readNextChild();
                if (childNodeList instanceof String)
                {
                    node.resetPullNode();
                    return (String) childNodeList;
                }
            }

            return "";//was written, but without content
        }

        throw new IOException("property is not available");
    }
}
