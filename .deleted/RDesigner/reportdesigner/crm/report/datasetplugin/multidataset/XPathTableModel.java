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
package org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.DataRow;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.swing.table.AbstractTableModel;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * User: Martin Date: 22.07.2006 Time: 11:16:28
 */
public class XPathTableModel extends AbstractTableModel
{
    @NotNull
    private static final Map<String, Class> SUPPORTED_TYPES;


    static
    {
        HashMap<String, Class> types = new HashMap<String, Class>();
        types.put("java.lang.String", String.class);
        types.put("java.sql.Date", Date.class);
        types.put("java.math.BigDecimal", BigDecimal.class);
        types.put("java.sql.Timestamp", Timestamp.class);
        types.put("java.lang.Integer", Integer.class);
        types.put("java.lang.Double", Double.class);
        types.put("java.lang.Long", Long.class);

        SUPPORTED_TYPES = Collections.unmodifiableMap(types);
    }


    @NotNull
    private ArrayList<Class> columnTypes;
    @NotNull
    private ArrayList<String> columnNames;
    @NotNull
    private ArrayList<HashMap<String, Object>> data;


    @SuppressWarnings({"ObjectEquality"})
    public XPathTableModel(@NotNull URL xmlResource, @NotNull String xPathExpression, @Nullable final DataRow parameters, int maxRowsToProcess) throws IOException, XPathExpressionException
    {
        columnTypes = new ArrayList<Class>();
        columnNames = new ArrayList<String>();
        data = new ArrayList<HashMap<String, Object>>();

        System.setProperty("javax.xml.xpath.XPathFactory:http://java.sun.com/jaxp/xpath/dom", "com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl");// NON-NLS

        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setXPathVariableResolver(new XPathVariableResolver()
        {
            @Nullable
            public Object resolveVariable(@NotNull QName variableName)
            {
                if (parameters != null)
                {
                    String var = variableName.getLocalPart();
                    return parameters.get(var);
                }
                return null;
            }
        });

        // load metadata (number of rows, row names, row types)
        Node types = (Node) xPath.evaluate("/comment()", new InputSource(xmlResource.openStream()), XPathConstants.NODE);// NON-NLS
        if (types == null)
        {
            types = (Node) xPath.evaluate("/result-set/comment()", new InputSource(xmlResource.openStream()), XPathConstants.NODE);// NON-NLS
        }
        if (types != null)
        {
            String nodeValue = types.getNodeValue();
            for (StringTokenizer stringTokenizer = new StringTokenizer(nodeValue, ","); stringTokenizer.hasMoreTokens();)
            {
                String className = stringTokenizer.nextToken();
                if (SUPPORTED_TYPES.containsKey(className))
                {
                    columnTypes.add(SUPPORTED_TYPES.get(className));
                }
                else
                {
                    columnTypes.add(String.class);
                }
            }
        }
        // try to find all valid column names
        // visit all entries and add the names as we find them
        NodeList rows = (NodeList) xPath.evaluate(xPathExpression, new InputSource(xmlResource.openStream()), XPathConstants.NODESET);

        for (int i = 0; i < rows.getLength(); i++)
        {
            if (data.size() >= maxRowsToProcess)
            {
                break;
            }

            Node node = rows.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                HashMap<String, Object> rowData = new HashMap<String, Object>();

                NodeList childNodes = node.getChildNodes();
                for (int n = 0; n < childNodes.getLength(); n++)
                {
                    Node child = childNodes.item(n);
                    if (child.getNodeType() == Node.ELEMENT_NODE)
                    {
                        if (!columnNames.contains(child.getNodeName()))
                        {
                            columnNames.add(child.getNodeName());
                        }

                        while (columnTypes.size() < columnNames.size())
                        {
                            columnTypes.add(String.class);
                        }

                        NodeList contentNodes = child.getChildNodes();
                        StringBuilder textContent = new StringBuilder(32);
                        for (int k = 0; k < contentNodes.getLength(); k++)
                        {
                            Node t = contentNodes.item(k);
                            if (t.getNodeType() == Node.TEXT_NODE)
                            {
                                textContent.append(t.getNodeValue());
                            }
                        }

                        Class columnClass = columnTypes.get(columnNames.indexOf(child.getNodeName()));
                        if (columnClass == String.class)
                        {
                            rowData.put(child.getNodeName(), textContent.toString());
                        }
                        else if (columnClass == Date.class)
                        {
                            rowData.put(child.getNodeName(), new Date(Long.parseLong(textContent.toString())));
                        }
                        else if (columnClass == BigDecimal.class)
                        {
                            rowData.put(child.getNodeName(), new BigDecimal(textContent.toString()));
                        }
                        else if (columnClass == Timestamp.class)
                        {
                            rowData.put(child.getNodeName(), new Timestamp(Long.parseLong(textContent.toString())));
                        }
                        else if (columnClass == Integer.class)
                        {
                            rowData.put(child.getNodeName(), Integer.valueOf(textContent.toString()));
                        }
                        else if (columnClass == Double.class)
                        {
                            rowData.put(child.getNodeName(), Double.valueOf(textContent.toString()));
                        }
                        else if (columnClass == Long.class)
                        {
                            rowData.put(child.getNodeName(), Long.valueOf(textContent.toString()));
                        }
                    }
                }
                data.add(rowData);
            }
        }

        // System.out.println("columnTypes = " + columnTypes);
    }


    @NotNull
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnTypes.get(columnIndex);
    }


    public int getRowCount()
    {
        return data.size();
    }


    public int getColumnCount()
    {
        return columnTypes.size();
    }


    @NotNull
    public String getColumnName(int column)
    {
        return columnNames.get(column);
    }


    @Nullable
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data.get(rowIndex).get(columnNames.get(columnIndex));
    }
}
