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
package org.pentaho.reportdesigner.crm.report.settings;

import org.gjt.xpp.XmlPullNode;
import org.gjt.xpp.XmlPullParser;
import org.gjt.xpp.XmlPullParserException;
import org.gjt.xpp.XmlPullParserFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 07.02.2006
 * Time: 14:14:08
 */
public class WorkspaceSettings implements XMLExternalizable
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(WorkspaceSettings.class.getName());

    @NotNull
    private static final WorkspaceSettings instance = new WorkspaceSettings();


    @NotNull
    public static WorkspaceSettings getInstance()
    {
        return instance;
    }


    @NotNull
    private static final String PROPERTIES = "properties";


    @NotNull
    private static final String PROPERTYLISTS = "propertylists";


    @NotNull
    private static final String PROPERTYLIST = "propertylist";
    @NotNull
    private static final String ENTRY = "entry";

    @NotNull
    public static final String PUBLISH_WEB_LOCATION = "publishWebLocation";
    @NotNull
    public static final String PUBLISH_REPORT_NAME = "publishReportName";
    @NotNull
    public static final String PUBLISH_EXPORT_TYPE = "publishExportType";
    @NotNull
    public static final String PUBLISH_LOCATION = "publishServerLocation";
    @NotNull
    public static final String PUBLISH_USER_ID = "publishUserID";
    @NotNull
    public static final String PUBLISH_XACTION = "publishXAction";
    @NotNull
    public static final String PUBLISH_USE_JNDI = "publishUseJNDI";


    @NotNull
    public static final String LAST_ACCESSED_REPORT_FILE = "LastAccessedReportFile";
    @NotNull
    public static final String LAST_ACCESSED_JFREEREPORT_FILE = "LastAccessedJFreeReportFile";
    @NotNull
    public static final String LAST_ACCESSED_REPORT_MERGE_FILE = "LastAccessedReportMergeFile";

    @NotNull
    private HashMap<String, String> properties;
    @NotNull
    private HashMap<String, ArrayList<String>> propertyLists;

    private boolean loaded;


    private WorkspaceSettings()
    {
        properties = new HashMap<String, String>();
        propertyLists = new HashMap<String, ArrayList<String>>();
        loaded = false;
    }


    public boolean isLoaded()
    {
        return loaded;
    }


    public void loadFromFile(@NotNull File file)
    {
        BufferedReader bufferedReader = null;
        try
        {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            //noinspection IOResourceOpenedButNotSafelyClosed
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), XMLConstants.ENCODING));
            XMLContext xmlContext = new XMLContext();

            xmlPullParser.setInput(bufferedReader);
            xmlPullParser.next(); // get first start tag
            XmlPullNode node = xmlPullParserFactory.newPullNode(xmlPullParser);

            if (XMLConstants.WORKSPACE_SETTINGS.equals(node.getRawName()))
            {
                readObject(node, xmlContext);
            }

            node.resetPullNode();

            loaded = true;
        }
        catch (XmlPullParserException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        catch (FileNotFoundException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.loadWorkspaceSettings ", e);
        }
        catch (IOException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        finally
        {
            IOUtil.closeStream(bufferedReader);
        }
    }


    public void writeSettings(@NotNull File file) throws IOException
    {
        FileOutputStream outputStream = null;
        try
        {
            //noinspection IOResourceOpenedButNotSafelyClosed
            outputStream = new FileOutputStream(file);
            XMLContext xmlContext = new XMLContext();
            XMLWriter xmlWriter0 = new XMLWriter(outputStream, true);
            xmlWriter0.writeDefaultProlog();
            xmlWriter0.startElement(XMLConstants.WORKSPACE_SETTINGS);
            externalizeObject(xmlWriter0, xmlContext);
            xmlWriter0.closeElement(XMLConstants.WORKSPACE_SETTINGS);
            xmlWriter0.close();
        }
        finally
        {
            IOUtil.closeStream(outputStream);
        }
    }


    public void put(@NonNls @NotNull String key, @NotNull String value)
    {
        //noinspection ConstantConditions
        if (key == null)
        {
            throw new IllegalArgumentException("key must not be null");
        }
        //noinspection ConstantConditions
        if (value == null)
        {
            throw new IllegalArgumentException("value must not be null");
        }
        properties.put(key, value);
    }


    public void put(@NonNls @NotNull String key, @NotNull Collection<String> values)
    {
        //noinspection ConstantConditions
        if (key == null)
        {
            throw new IllegalArgumentException("key must not be null");
        }
        //noinspection ConstantConditions
        if (values == null)
        {
            throw new IllegalArgumentException("values must not be null");
        }
        propertyLists.put(key, new ArrayList<String>(values));
    }


    @NotNull
    public ArrayList<String> getList(@NonNls @NotNull String key)
    {
        @Nullable
        ArrayList<String> strings = propertyLists.get(key);
        if (strings == null)
        {
            return new ArrayList<String>();
        }
        else
        {
            return new ArrayList<String>(strings);
        }
    }


    @Nullable
    public Integer getInt(@NonNls @NotNull String key)
    {
        @Nullable
        String value = properties.get(key);
        try
        {
            return Integer.valueOf(value);
        }
        catch (NumberFormatException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "WorkspaceSettings.getInt ", e);
            return null;
        }
    }


    @Nullable
    public Long getLong(@NonNls @NotNull String key)
    {
        @Nullable
        String value = properties.get(key);
        try
        {
            return Long.valueOf(value);
        }
        catch (NumberFormatException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "WorkspaceSettings.getLong ", e);
            return null;
        }
    }


    @Nullable
    public Boolean getBoolean(@NonNls @NotNull String key)
    {
        @Nullable
        String value = properties.get(key);
        return Boolean.valueOf(value);
    }


    @Nullable
    public File getFile(@NonNls @NotNull String key)
    {
        String string = getString(key);
        if (string == null)
        {
            return null;
        }
        try
        {
            return new File(string);
        }
        catch (Exception e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "WorkspaceSettings.getFile ", e);
            return null;
        }
    }


    @Nullable
    public String getString(@NonNls @NotNull String key)
    {
        return properties.get(key);
    }


    @Nullable
    public String remove(@NonNls @NotNull String key)
    {
        return properties.remove(key);
    }


    public void storeDialogBounds(@NotNull JDialog dialog, @NonNls @NotNull String dialogId)
    {
        put(dialogId + ".Bounds.x", String.valueOf(dialog.getLocationOnScreen().x));
        put(dialogId + ".Bounds.y", String.valueOf(dialog.getLocationOnScreen().y));
        put(dialogId + ".Bounds.width", String.valueOf(dialog.getWidth()));
        put(dialogId + ".Bounds.height", String.valueOf(dialog.getHeight()));
    }


    public void storeFrameBounds(@NotNull JFrame frame, @NonNls @NotNull String frameId)
    {
        put(frameId + ".Bounds.x", String.valueOf(frame.getLocationOnScreen().x));
        put(frameId + ".Bounds.y", String.valueOf(frame.getLocationOnScreen().y));
        put(frameId + ".Bounds.width", String.valueOf(frame.getWidth()));
        put(frameId + ".Bounds.height", String.valueOf(frame.getHeight()));
        put(frameId + ".extendedState", String.valueOf(frame.getExtendedState()));
    }


    public boolean restoreDialogBounds(@NotNull JDialog dialog, @NonNls @NotNull String dialogId)
    {
        Integer x = getInt(dialogId + ".Bounds.x");
        Integer y = getInt(dialogId + ".Bounds.y");
        Integer width = getInt(dialogId + ".Bounds.width");
        Integer height = getInt(dialogId + ".Bounds.height");

        if (x != null && y != null && width != null && height != null)
        {
            dialog.setBounds(x.intValue(), y.intValue(), width.intValue(), height.intValue());
            dialog.invalidate();
            dialog.validate();
            dialog.repaint();
            return true;
        }

        return false;
    }


    public boolean restoreFrameBounds(@NotNull JFrame frame, @NonNls @NotNull String frameId)
    {
        Integer x = getInt(frameId + ".Bounds.x");
        Integer y = getInt(frameId + ".Bounds.y");
        Integer width = getInt(frameId + ".Bounds.width");
        Integer height = getInt(frameId + ".Bounds.height");
        Integer extendedState = getInt(frameId + ".extendedState");

        if (x != null && y != null && width != null && height != null)
        {
            frame.setBounds(x.intValue(), y.intValue(), width.intValue(), height.intValue());
            frame.invalidate();
            frame.validate();
            frame.repaint();

            if (extendedState != null)
            {
                frame.setExtendedState(extendedState.intValue());
            }

            return true;
        }

        return false;
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        Set<String> keys = properties.keySet();
        xmlWriter.startElement(PROPERTIES);
        for (String key : keys)
        {
            @Nullable
            String value = properties.get(key);

            if (key != null && value != null)
            {
                xmlWriter.writeProperty(key, value);
            }
        }
        xmlWriter.closeElement(PROPERTIES);

        xmlWriter.startElement(PROPERTYLISTS);
        for (String key : propertyLists.keySet())
        {
            @Nullable
            ArrayList<String> values = propertyLists.get(key);
            if (key != null && values != null)
            {
                xmlWriter.startElement(PROPERTYLIST).writeAttribute(XMLConstants.NAME, key);
                for (String value : values)
                {
                    xmlWriter.writeNamelessProperty(ENTRY, value);
                }
                xmlWriter.closeElement(PROPERTYLIST);
            }
        }
        xmlWriter.closeElement(PROPERTYLISTS);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException, XmlPullParserException
    {
        while (!node.isFinished())
        {
            Object obj = node.readNextChild();
            if (obj instanceof XmlPullNode)
            {
                XmlPullNode xmlPullNode = (XmlPullNode) obj;
                if (PROPERTIES.equals(xmlPullNode.getRawName()))
                {
                    readProperties(xmlPullNode);
                }
                else if (PROPERTYLISTS.equals(xmlPullNode.getRawName()))
                {
                    while (!xmlPullNode.isFinished())
                    {
                        Object o = xmlPullNode.readNextChild();
                        if (o instanceof XmlPullNode)
                        {
                            XmlPullNode pullNode = (XmlPullNode) o;
                            if (PROPERTYLIST.equals(pullNode.getRawName()))
                            {
                                String key = pullNode.getAttributeValueFromRawName(XMLConstants.NAME);
                                ArrayList<String> values = new ArrayList<String>();
                                while (!pullNode.isFinished())
                                {
                                    Object obj2 = pullNode.readNextChild();
                                    if (obj2 instanceof XmlPullNode)
                                    {
                                        XmlPullNode xmlPullNode1 = (XmlPullNode) obj2;
                                        if (ENTRY.equals(xmlPullNode1.getRawName()))
                                        {
                                            while (!xmlPullNode1.isFinished())
                                            {
                                                Object value = xmlPullNode1.readNextChild();
                                                if (value instanceof String)
                                                {
                                                    String s = (String) value;
                                                    values.add(s);
                                                }
                                            }
                                        }
                                    }
                                }
                                propertyLists.put(key, values);
                            }
                        }
                    }
                }
                xmlPullNode.resetPullNode();
            }
        }
    }


    private void readProperties(@NotNull XmlPullNode xmlPullNode) throws XmlPullParserException, IOException
    {
        while (!xmlPullNode.isFinished())
        {
            Object obj2 = xmlPullNode.readNextChild();
            if (obj2 instanceof XmlPullNode)
            {
                XmlPullNode propertyNode = (XmlPullNode) obj2;
                if (XMLConstants.PROPERTY.equals(propertyNode.getRawName()))
                {
                    String key = propertyNode.getAttributeValueFromRawName(XMLConstants.NAME);
                    if (key != null)
                    {
                        while (!propertyNode.isFinished())
                        {
                            Object value = propertyNode.readNextChild();
                            if (value instanceof String)
                            {
                                String s = (String) value;
                                properties.put(key, s);
                            }
                        }
                    }
                }
                propertyNode.resetPullNode();
            }
        }
    }


}