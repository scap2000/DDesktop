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
package org.pentaho.reportdesigner.crm.report.tests.xpath;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.XPathTableModel;

import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.io.IOException;

/**
 * User: Martin
 * Date: 21.07.2006
 * Time: 19:29:40
 */
@SuppressWarnings({"ALL"})
public class XPathTest
{
    public static void main(@NotNull String[] args)
            throws XPathExpressionException, IOException
    {
        //System.setProperty("jaxp.debug", Boolean.TRUE.toString());

        //System.setProperty("javax.xml.xpath.XPathFactory:http://java.sun.com/jaxp/xpath/dom", "com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl");

        //XPathFactory xPathFactory = XPathFactory.newInstance();
        //System.out.println("xPathFactory = " + xPathFactory);
        //XPath xPath = XPathFactory.newInstance().newXPath();
        ////NodeList types = (NodeList) xPath.evaluate("/result-set/*", new InputSource(XPathTest.class.getResourceAsStream("Quad_Data.xml")), XPathConstants.NODESET);
        //
        //NodeList types = (NodeList) xPath.evaluate("/descendant::row[REGION='Central']", new InputSource(XPathTest.class.getResourceAsStream("Quad_Data.xml")), XPathConstants.NODESET);
        //System.out.println("types = " + types);
        //for (int i = 0; i < types.getLength(); i++)
        //{
        //    Node node = types.item(i);
        //    System.out.println("node = " + node.getNodeName());
        //    System.out.println("node.getNodeValue() = " + node.getNodeValue());
        //    NodeList childNodes = node.getChildNodes();
        //    for (int n = 0; n < childNodes.getLength(); n++)
        //    {
        //        Node child = childNodes.item(n);
        //        System.out.println("child.getNodeName() = " + child.getNodeName());
        //    }
        //}

        //String value = types.getNodeValue();
        //System.out.println("value = " + value);

        //XPathTableModel xPathTableModel = new XPathTableModel(XPathTest.class.getResource("Quad_Data.xml"), "/descendant::row[REGION='Central']", Integer.MAX_VALUE);
        XPathTableModel xPathTableModel = new XPathTableModel(XPathTest.class.getResource("Quad_Data.xml"), "/result-set/*", null, Integer.MAX_VALUE);
        JFrame frame = new JFrame();
        JTable table = new JTable(xPathTableModel);
        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 600, 600);
        frame.setVisible(true);
    }
}
