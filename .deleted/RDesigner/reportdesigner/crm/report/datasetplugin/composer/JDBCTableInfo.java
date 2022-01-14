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

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 08:13:01
 */
public class JDBCTableInfo
{
    @NotNull
    private String catalog;
    @NotNull
    private String schema;
    @NotNull
    private String tableName;
    @NotNull
    private String type;
    @NotNull
    private String remarks;
    @NotNull
    private String typesCatalog;
    @NotNull
    private String typesSchema;
    @NotNull
    private String typeName;
    @NotNull
    private String selfReferencingColumnName;
    @NotNull
    private String refGeneration;
    @NotNull
    private LinkedHashMap<String, JDBCColumnInfo> columnInfos;


    public JDBCTableInfo()
    {
    }


    public JDBCTableInfo(@NotNull String catalog, @NotNull String schema, @NotNull String name, @NotNull String type, @NotNull String remarks, @NotNull String typesCatalog, @NotNull String typesSchema, @NotNull String typeName, @NotNull String selfReferencingColumnName, @NotNull String refGeneration, @NotNull ArrayList<JDBCColumnInfo> columnInfos)
    {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = name;
        this.type = type;
        this.remarks = remarks;
        this.typesCatalog = typesCatalog;
        this.typesSchema = typesSchema;
        this.typeName = typeName;
        this.selfReferencingColumnName = selfReferencingColumnName;
        this.refGeneration = refGeneration;

        this.columnInfos = new LinkedHashMap<String, JDBCColumnInfo>();
        for (JDBCColumnInfo jdbcColumnInfo : columnInfos)
        {
            this.columnInfos.put(jdbcColumnInfo.getColumnName(), jdbcColumnInfo);
        }
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


    public void setType(@NotNull String type)
    {
        this.type = type;
    }


    public void setRemarks(@NotNull String remarks)
    {
        this.remarks = remarks;
    }


    public void setTypesCatalog(@NotNull String typesCatalog)
    {
        this.typesCatalog = typesCatalog;
    }


    public void setTypesSchema(@NotNull String typesSchema)
    {
        this.typesSchema = typesSchema;
    }


    public void setTypeName(@NotNull String typeName)
    {
        this.typeName = typeName;
    }


    public void setSelfReferencingColumnName(@NotNull String selfReferencingColumnName)
    {
        this.selfReferencingColumnName = selfReferencingColumnName;
    }


    public void setRefGeneration(@NotNull String refGeneration)
    {
        this.refGeneration = refGeneration;
    }


    public void setColumnInfos(@NotNull ArrayList<JDBCColumnInfo> columnInfos)
    {
        this.columnInfos = new LinkedHashMap<String, JDBCColumnInfo>();
        for (JDBCColumnInfo jdbcColumnInfo : columnInfos)
        {
            this.columnInfos.put(jdbcColumnInfo.getColumnName(), jdbcColumnInfo);
        }
    }


    @NotNull
    public LinkedHashMap<String, JDBCColumnInfo> getColumnInfos()
    {
        return columnInfos;
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
    public String getType()
    {
        return type;
    }


    @NotNull
    public String getRemarks()
    {
        return remarks;
    }


    @NotNull
    public String getTypesCatalog()
    {
        return typesCatalog;
    }


    @NotNull
    public String getTypesSchema()
    {
        return typesSchema;
    }


    @NotNull
    public String getTypeName()
    {
        return typeName;
    }


    @NotNull
    public String getSelfReferencingColumnName()
    {
        return selfReferencingColumnName;
    }


    @NotNull
    public String getRefGeneration()
    {
        return refGeneration;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JDBCTableInfo that = (JDBCTableInfo) o;

        if (catalog != null ? !catalog.equals(that.catalog) : that.catalog != null) return false;
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        if (refGeneration != null ? !refGeneration.equals(that.refGeneration) : that.refGeneration != null) return false;
        if (remarks != null ? !remarks.equals(that.remarks) : that.remarks != null) return false;
        if (schema != null ? !schema.equals(that.schema) : that.schema != null) return false;
        if (selfReferencingColumnName != null ? !selfReferencingColumnName.equals(that.selfReferencingColumnName) : that.selfReferencingColumnName != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;
        if (typesCatalog != null ? !typesCatalog.equals(that.typesCatalog) : that.typesCatalog != null) return false;
        return !(typesSchema != null ? !typesSchema.equals(that.typesSchema) : that.typesSchema != null);

    }


    public int hashCode()
    {
        int result;
        result = (catalog != null ? catalog.hashCode() : 0);
        result = 29 * result + (schema != null ? schema.hashCode() : 0);
        result = 29 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 29 * result + (type != null ? type.hashCode() : 0);
        result = 29 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 29 * result + (typesCatalog != null ? typesCatalog.hashCode() : 0);
        result = 29 * result + (typesSchema != null ? typesSchema.hashCode() : 0);
        result = 29 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 29 * result + (selfReferencingColumnName != null ? selfReferencingColumnName.hashCode() : 0);
        result = 29 * result + (refGeneration != null ? refGeneration.hashCode() : 0);
        return result;
    }


    @NotNull
    public String toString()
    {
        return "JDBCTableInfo{" +
               "catalog='" + catalog + "'" +
               ", schema='" + schema + "'" +
               ", tableName='" + tableName + "'" +
               ", type='" + type + "'" +
               ", remarks='" + remarks + "'" +
               ", typesCatalog='" + typesCatalog + "'" +
               ", typesSchema='" + typesSchema + "'" +
               ", typeName='" + typeName + "'" +
               ", selfReferencingColumnName='" + selfReferencingColumnName + "'" +
               ", refGeneration='" + refGeneration + "'" +
               ", columnInfos=" + columnInfos +
               "}";
    }
}
