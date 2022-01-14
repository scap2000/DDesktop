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
package org.pentaho.reportdesigner.crm.report.datasetplugin.composer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 08:23:06
 */
public class JDBCColumnInfo
{
    @NotNull
    private String catalog;
    @NotNull
    private String schema;
    @NotNull
    private String tableName;
    @NotNull
    private String columnName;
    @NotNull
    private String dataType;
    @NotNull
    private String typeName;
    private int columnSize;
    //private void bufferLength;
    private int decimalDigits;
    private int numRecRadix;
    private int nullable;
    @NotNull
    private String remarks;
    @NotNull
    private String defaultValue;
    //private int sqlDataType;
    //private int sqlDateTimeSub;
    private int charOctetLength;
    private int ordinalPosition;
    @NotNull
    private String isNullable;
    @NotNull
    private String scopeCatalog;
    @NotNull
    private String scopeSchema;
    @NotNull
    private String sourceDataType;


    public JDBCColumnInfo()
    {
    }


    public JDBCColumnInfo(@NotNull String catalog, @NotNull String schema, @NotNull String tableName, @NotNull String columnName, @NotNull String dataType, @NotNull String typeName, int columnSize, int decimalDigits, int numRecRadix, int nullable, @NotNull String remarks, @NotNull String defaultValue, int charOctetLength, int ordinalPosition, @NotNull String isNullable, @NotNull String scopeCatalog, @NotNull String scopeSchema, @NotNull String sourceDataType)
    {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = tableName;
        this.columnName = columnName;
        this.dataType = dataType;
        this.typeName = typeName;
        this.columnSize = columnSize;
        this.decimalDigits = decimalDigits;
        this.numRecRadix = numRecRadix;
        this.nullable = nullable;
        this.remarks = remarks;
        this.defaultValue = defaultValue;
        this.charOctetLength = charOctetLength;
        this.ordinalPosition = ordinalPosition;
        this.isNullable = isNullable;
        this.scopeCatalog = scopeCatalog;
        this.scopeSchema = scopeSchema;
        this.sourceDataType = sourceDataType;
    }


    public void setCatalog(@NotNull String catalog)
    {
        this.catalog = catalog;
    }


    public void setSchema(@NotNull String schema)
    {
        this.schema = schema;
    }


    public void setTableName(@NotNull String tableName)
    {
        this.tableName = tableName;
    }


    public void setColumnName(@NotNull String columnName)
    {
        this.columnName = columnName;
    }


    public void setDataType(@NotNull String dataType)
    {
        this.dataType = dataType;
    }


    public void setTypeName(@NotNull String typeName)
    {
        this.typeName = typeName;
    }


    public void setColumnSize(int columnSize)
    {
        this.columnSize = columnSize;
    }


    public void setDecimalDigits(int decimalDigits)
    {
        this.decimalDigits = decimalDigits;
    }


    public void setNumRecRadix(int numRecRadix)
    {
        this.numRecRadix = numRecRadix;
    }


    @SuppressWarnings({"NullableProblems"})
    public void setNullable(int nullable)
    {
        this.nullable = nullable;
    }


    public void setRemarks(@NotNull String remarks)
    {
        this.remarks = remarks;
    }


    public void setDefaultValue(@NotNull String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    public void setCharOctetLength(int charOctetLength)
    {
        this.charOctetLength = charOctetLength;
    }


    public void setOrdinalPosition(int ordinalPosition)
    {
        this.ordinalPosition = ordinalPosition;
    }


    public void setNullable(@NotNull String nullable)
    {
        isNullable = nullable;
    }


    public void setScopeCatalog(@NotNull String scopeCatalog)
    {
        this.scopeCatalog = scopeCatalog;
    }


    public void setScopeSchema(@NotNull String scopeSchema)
    {
        this.scopeSchema = scopeSchema;
    }


    public void setSourceDataType(@NotNull String sourceDataType)
    {
        this.sourceDataType = sourceDataType;
    }


    @NotNull
    public String getCatalog()
    {
        return catalog;
    }


    @NotNull
    public String getSchema()
    {
        return schema;
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
    public String getDataType()
    {
        return dataType;
    }


    @NotNull
    public String getTypeName()
    {
        return typeName;
    }


    public int getColumnSize()
    {
        return columnSize;
    }


    public int getDecimalDigits()
    {
        return decimalDigits;
    }


    public int getNumRecRadix()
    {
        return numRecRadix;
    }


    @SuppressWarnings({"NullableProblems"})
    public int getNullable()
    {
        return nullable;
    }


    @NotNull
    public String getRemarks()
    {
        return remarks;
    }


    @NotNull
    public String getDefaultValue()
    {
        return defaultValue;
    }


    public int getCharOctetLength()
    {
        return charOctetLength;
    }


    public int getOrdinalPosition()
    {
        return ordinalPosition;
    }


    @NotNull
    public String getIsNullable()
    {
        return isNullable;
    }


    @NotNull
    public String getScopeCatalog()
    {
        return scopeCatalog;
    }


    @NotNull
    public String getScopeSchema()
    {
        return scopeSchema;
    }


    @NotNull
    public String getSourceDataType()
    {
        return sourceDataType;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JDBCColumnInfo that = (JDBCColumnInfo) o;

        if (charOctetLength != that.charOctetLength) return false;
        if (columnSize != that.columnSize) return false;
        if (decimalDigits != that.decimalDigits) return false;
        if (nullable != that.nullable) return false;
        if (numRecRadix != that.numRecRadix) return false;
        if (ordinalPosition != that.ordinalPosition) return false;
        if (catalog != null ? !catalog.equals(that.catalog) : that.catalog != null) return false;
        if (columnName != null ? !columnName.equals(that.columnName) : that.columnName != null) return false;
        if (dataType != null ? !dataType.equals(that.dataType) : that.dataType != null) return false;
        if (defaultValue != null ? !defaultValue.equals(that.defaultValue) : that.defaultValue != null) return false;
        if (isNullable != null ? !isNullable.equals(that.isNullable) : that.isNullable != null) return false;
        if (remarks != null ? !remarks.equals(that.remarks) : that.remarks != null) return false;
        if (schema != null ? !schema.equals(that.schema) : that.schema != null) return false;
        if (scopeCatalog != null ? !scopeCatalog.equals(that.scopeCatalog) : that.scopeCatalog != null) return false;
        if (scopeSchema != null ? !scopeSchema.equals(that.scopeSchema) : that.scopeSchema != null) return false;
        if (sourceDataType != null ? !sourceDataType.equals(that.sourceDataType) : that.sourceDataType != null) return false;
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        return !(typeName != null ? !typeName.equals(that.typeName) : that.typeName != null);

    }


    public int hashCode()
    {
        int result;
        result = (catalog != null ? catalog.hashCode() : 0);
        result = 29 * result + (schema != null ? schema.hashCode() : 0);
        result = 29 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 29 * result + (columnName != null ? columnName.hashCode() : 0);
        result = 29 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 29 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 29 * result + columnSize;
        result = 29 * result + decimalDigits;
        result = 29 * result + numRecRadix;
        result = 29 * result + nullable;
        result = 29 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 29 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        result = 29 * result + charOctetLength;
        result = 29 * result + ordinalPosition;
        result = 29 * result + (isNullable != null ? isNullable.hashCode() : 0);
        result = 29 * result + (scopeCatalog != null ? scopeCatalog.hashCode() : 0);
        result = 29 * result + (scopeSchema != null ? scopeSchema.hashCode() : 0);
        result = 29 * result + (sourceDataType != null ? sourceDataType.hashCode() : 0);
        return result;
    }


    @NotNull
    public String toString()
    {
        return "JDBCColumnInfo{" +
               "catalog='" + catalog + "'" +
               ", schema='" + schema + "'" +
               ", tableName='" + tableName + "'" +
               ", columnName='" + columnName + "'" +
               ", dataType='" + dataType + "'" +
               ", typeName='" + typeName + "'" +
               ", columnSize=" + columnSize +
               ", decimalDigits=" + decimalDigits +
               ", numRecRadix=" + numRecRadix +
               ", nullable=" + nullable +
               ", remarks='" + remarks + "'" +
               ", defaultValue='" + defaultValue + "'" +
               ", charOctetLength=" + charOctetLength +
               ", ordinalPosition=" + ordinalPosition +
               ", isNullable='" + isNullable + "'" +
               ", scopeCatalog='" + scopeCatalog + "'" +
               ", scopeSchema='" + scopeSchema + "'" +
               ", sourceDataType='" + sourceDataType + "'" +
               "}";
    }
}
