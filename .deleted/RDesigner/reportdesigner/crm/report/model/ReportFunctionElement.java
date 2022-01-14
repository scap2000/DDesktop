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
package org.pentaho.reportdesigner.crm.report.model;


import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverter;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 10.01.2006
 * Time: 13:07:11
 */
public class ReportFunctionElement extends ReportElement
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ReportFunctionElement.class.getName());


    public ReportFunctionElement()
    {
        setName("");
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        Object newParent = reportVisitor.visit(parent, this);

        ArrayList<ReportElement> children = new ArrayList<ReportElement>(getChildren());
        for (ReportElement reportElement : children)
        {
            reportElement.accept(newParent, reportVisitor);
        }
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            try
            {
                Object o = field.get(this);
                if (field.getType().isArray() && o != null)
                {
                    Object[] objArray = (Object[]) o;
                    ObjectConverter converter = ObjectConverterFactory.getInstance().getConverter(field.getType().getComponentType(), field, xmlContext);
                    xmlWriter.startElement(XMLConstants.PROPERTY);
                    xmlWriter.writeAttribute(XMLConstants.NAME, field.getName());
                    xmlWriter.writeAttribute(XMLConstants.ARRAY, "true");
                    for (int j = 0; j < objArray.length; j++)
                    {
                        Object o1 = objArray[j];
                        if (o1 != null && converter != null)
                        {
                            xmlWriter.writeProperty(String.valueOf(j), converter.getString(o1));
                        }
                    }
                    xmlWriter.closeElement(XMLConstants.PROPERTY);
                }
                else
                {
                    if (o != null)
                    {
                        ObjectConverter converter = ObjectConverterFactory.getInstance().getConverter(field.getType(), field, xmlContext);
                        if (converter != null)
                        {
                            xmlWriter.writeProperty(field.getName(), converter.getString(o));
                        }
                    }
                }
            }
            catch (IllegalAccessException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportFunctionElement.externalizeAttributes ", e);
            }
        }
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            try
            {
                if (XMLConstants.PROPERTY.equals(node.getRawName()) && field.getName().equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
                {
                    if (node.getAttributeValueFromRawName(XMLConstants.ARRAY) != null)
                    {
                        ObjectConverter converter = ObjectConverterFactory.getInstance().getConverter(field.getType().getComponentType(), field, xmlContext);
                        if (converter != null)
                        {
                            ArrayList<Object> al = new ArrayList<Object>();
                            int n = 0;
                            while (!node.isFinished())
                            {
                                Object childNodeList = node.readNextChild();
                                if (childNodeList instanceof XmlPullNode)
                                {
                                    XmlPullNode child = (XmlPullNode) childNodeList;
                                    if (XMLConstants.PROPERTY.equals(child.getRawName()))
                                    {
                                        //int index = Integer.valueOf(child.getAttributeValueFromRawName(XMLConstants.NAME));//MARKED perhaps obey index/subarrays as necessary?
                                        al.add(converter.getObject(XMLUtils.readProperty(String.valueOf(n), child)));
                                        n++;
                                    }
                                }
                            }
                            Object[] o = (Object[]) Array.newInstance(field.getType().getComponentType(), al.size());
                            for (int j = 0; j < o.length; j++)
                            {
                                o[j] = al.get(j);
                            }
                            field.set(this, o);
                        }
                    }
                    else
                    {
                        String s = XMLUtils.readProperty(field.getName(), node);
                        ObjectConverter converter = ObjectConverterFactory.getInstance().getConverter(field.getType(), field, xmlContext);

                        if (converter != null)
                        {
                            field.set(this, converter.getObject(s));
                        }
                    }
                }

            }
            catch (IllegalAccessException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportFunctionElement.readAttributes ", e);
            }
        }
    }

    //@NotNull
    //private String getAbsolutePath(@NotNull XMLContext xmlContext, @NotNull Field field, @NotNull ObjectConverter objectConverter, @NotNull String path)
    //{
    //    if (objectConverter instanceof URLConverter)
    //    {
    //        try
    //        {
    //            return FileRelativator.getAbsoluteURLString(xmlContext, path);
    //        }
    //        catch (MalformedURLException e)
    //        {
    //            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportFunctionElement.getAbsolutePath ", e);
    //            return path;
    //        }
    //    }
    //
    //    return path;
    //}
    //
    //
    //private boolean isPathProperty(@NotNull XMLContext xmlContext, @NotNull Field field, @NotNull ObjectConverter objectConverter)
    //{
    //    return objectConverter instanceof URLConverter;
    //}
    //
    //
    //@NotNull
    //private String getRelativePath(@NotNull XMLContext xmlContext, @NotNull Object object)
    //{
    //    if (object instanceof URL)
    //    {
    //        URL url = (URL) object;
    //
    //    }
    //}


}
