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
package org.pentaho.reportdesigner.crm.report.connection;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 18.01.2006
 * Time: 15:38:27
 */
public class ColumnInfo implements XMLExternalizable
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ColumnInfo.class.getName());

    @NotNull
    public static final ColumnInfo[] EMPTY_ARRAY = new ColumnInfo[0];


    @NotNull
    public static Class getJavaTypeFomrSQLType(int sqlType)
    {
        switch (sqlType)
        {
            case Types.ARRAY:
                return Object[].class;
            case Types.BIGINT:
                return Long.class;
            case Types.BINARY:
                return byte[].class;
            case Types.BIT:
                return Boolean.class;
            case Types.BLOB:
                return Blob.class;
            case Types.BOOLEAN:
                return Boolean.class;
            case Types.CHAR:
                return String.class;
            case Types.CLOB:
                return Clob.class;
            case Types.DATALINK:
                return Object.class;
            case Types.DATE:
                return Date.class;
            case Types.DECIMAL:
                return BigDecimal.class;
            case Types.DISTINCT:
                return Object.class;
            case Types.DOUBLE:
                return Double.class;
            case Types.FLOAT:
                return Double.class;
            case Types.INTEGER:
                return Integer.class;
            case Types.JAVA_OBJECT:
                return Object.class;
            case Types.LONGVARBINARY:
                return byte[].class;
            case Types.LONGVARCHAR:
                return String.class;
            case Types.NULL:
                return Object.class;
            case Types.NUMERIC:
                return BigDecimal.class;
            case Types.OTHER:
                return Object.class;
            case Types.REAL:
                return Float.class;
            case Types.REF:
                return Ref.class;
            case Types.SMALLINT:
                return Short.class;
            case Types.STRUCT:
                return Struct.class;
            case Types.TIME:
                return Time.class;
            case Types.TIMESTAMP:
                return Timestamp.class;
            case Types.TINYINT:
                return Byte.class;
            case Types.VARBINARY:
                return byte[].class;
            case Types.VARCHAR:
                return String.class;
            default:
                return Object.class;
        }
    }


    @NotNull
    private String tableName;
    @NotNull
    private String columnName;
    @NotNull
    private Class columnClass;


    public ColumnInfo(@NotNull String tableName, @NotNull String columnName, @NotNull Class columnClass)
    {
        //noinspection ConstantConditions
        if (tableName == null)
        {
            throw new IllegalArgumentException("tableName must not be null");
        }
        //noinspection ConstantConditions
        if (columnName == null)
        {
            throw new IllegalArgumentException("columnName must not be null");
        }
        //noinspection ConstantConditions
        if (columnClass == null)
        {
            throw new IllegalArgumentException("columnClass must not be null");
        }

        this.tableName = tableName;
        this.columnName = columnName;
        this.columnClass = columnClass;
    }


    @NotNull
    public String getTableName()
    {
        return tableName;
    }


    @NotNull
    public String getColumnName()
    {
        return columnName;
    }


    @NotNull
    public Class getColumnClass()
    {
        return columnClass;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ColumnInfo that = (ColumnInfo) o;

        if (!columnClass.equals(that.columnClass)) return false;
        if (!columnName.equals(that.columnName)) return false;
        return tableName.equals(that.tableName);

    }


    public int hashCode()
    {
        int result;
        result = tableName.hashCode();
        result = 29 * result + columnName.hashCode();
        result = 29 * result + columnClass.hashCode();
        return result;
    }


    @NotNull
    public String toString()
    {
        return "ColumnInfo{" +
               "tableName='" + tableName + "'" +
               ", columnName='" + columnName + "'" +
               ", columnClass=" + columnClass +
               "}";
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.COLUMN_NAME, columnName);
        xmlWriter.writeAttribute(PropertyKeys.COLUMN_CLASS_NAME, columnClass.getName());
        xmlWriter.writeAttribute(PropertyKeys.TABLE_NAME, tableName);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException
    {
        columnName = node.getAttributeValueFromRawName(PropertyKeys.COLUMN_NAME);
        try
        {
            columnClass = Class.forName(node.getAttributeValueFromRawName(PropertyKeys.COLUMN_CLASS_NAME));
        }
        catch (ClassNotFoundException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ColumnInfo.readObject ", e);
        }
        tableName = node.getAttributeValueFromRawName(PropertyKeys.TABLE_NAME);
    }
}
