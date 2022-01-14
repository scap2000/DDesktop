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

import org.gjt.xpp.XmlPullNode;
import org.gjt.xpp.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;

/**
 * User: Martin
 * Date: 21.07.2006
 * Time: 12:54:42
 */
public class JNDISource implements XMLExternalizable
{
    @NotNull
    private String jndiName;
    @NotNull
    private String driverClass;
    @NotNull
    private String connectionString;
    @NotNull
    private String username;
    @NotNull
    private String password;


    public JNDISource()
    {
        jndiName = "";
        driverClass = "";
        connectionString = "";
        username = "";
        password = "";
    }


    public JNDISource(@NotNull String jndiName, @NotNull String driverClass, @NotNull String connectionString, @NotNull String username, @NotNull String password)
    {
        this.jndiName = jndiName;
        this.driverClass = driverClass;
        this.connectionString = connectionString;
        this.username = username;
        this.password = password;
    }


    @NotNull
    public String getConnectionString()
    {
        return connectionString;
    }


    public void setConnectionString(@NotNull String connectionString)
    {
        this.connectionString = connectionString;
    }


    @NotNull
    public String getDriverClass()
    {
        return driverClass;
    }


    public void setDriverClass(@NotNull String driverClass)
    {
        this.driverClass = driverClass;
    }


    @NotNull
    public String getJndiName()
    {
        return jndiName;
    }


    public void setJndiName(@NotNull String jndiName)
    {
        this.jndiName = jndiName;
    }


    @NotNull
    public String getPassword()
    {
        return password;
    }


    public void setPassword(@NotNull String password)
    {
        this.password = password;
    }


    @NotNull
    public String getUsername()
    {
        return username;
    }


    public void setUsername(@NotNull String username)
    {
        this.username = username;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JNDISource that = (JNDISource) o;

        return !(jndiName != null ? !jndiName.equals(that.jndiName) : that.jndiName != null);
    }


    public int hashCode()
    {
        return (jndiName != null ? jndiName.hashCode() : 0);
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.JNDI_NAME, jndiName);
        xmlWriter.writeAttribute(PropertyKeys.DRIVER_CLASS, driverClass);
        xmlWriter.writeAttribute(PropertyKeys.CONNECTION_STRING, connectionString);
        xmlWriter.writeAttribute(PropertyKeys.USER_NAME, username);
        xmlWriter.writeAttribute(PropertyKeys.PASSWORD, password);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException, XmlPullParserException
    {
        jndiName = node.getAttributeValueFromRawName(PropertyKeys.JNDI_NAME);
        driverClass = node.getAttributeValueFromRawName(PropertyKeys.DRIVER_CLASS);
        connectionString = node.getAttributeValueFromRawName(PropertyKeys.CONNECTION_STRING);
        username = node.getAttributeValueFromRawName(PropertyKeys.USER_NAME);
        password = node.getAttributeValueFromRawName(PropertyKeys.PASSWORD);

    }
}