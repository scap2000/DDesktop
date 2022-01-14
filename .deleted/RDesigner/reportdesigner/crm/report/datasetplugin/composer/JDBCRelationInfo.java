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
 * Time: 08:51:38
 */
public class JDBCRelationInfo
{
    /*
PKTABLE_CAT String => primary key table catalog being imported (may be null)
PKTABLE_SCHEM String => primary key table schema being imported (may be null)
PKTABLE_NAME String => primary key table name being imported
PKCOLUMN_NAME String => primary key column name being imported
FKTABLE_CAT String => foreign key table catalog (may be null)
FKTABLE_SCHEM String => foreign key table schema (may be null)
FKTABLE_NAME String => foreign key table name
FKCOLUMN_NAME String => foreign key column name
KEY_SEQ short => sequence number within a foreign key
UPDATE_RULE short => What happens to a foreign key when the primary key is updated:
    importedNoAction - do not allow update of primary key if it has been imported
    importedKeyCascade - change imported key to agree with primary key update
    importedKeySetNull - change imported key to NULL if its primary key has been updated
    importedKeySetDefault - change imported key to default values if its primary key has been updated
    importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
DELETE_RULE short => What happens to the foreign key when primary is deleted.
    importedKeyNoAction - do not allow delete of primary key if it has been imported
    importedKeyCascade - delete rows that import a deleted key
    importedKeySetNull - change imported key to NULL if its primary key has been deleted
    importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
    importedKeySetDefault - change imported key to default if its primary key has been deleted
FK_NAME String => foreign key name (may be null)
PK_NAME String => primary key name (may be null)
DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit
    importedKeyInitiallyDeferred - see SQL92 for definition
    importedKeyInitiallyImmediate - see SQL92 for definition
    importedKeyNotDeferrable - see SQL92 for definition
    */

    //redundant fields (graph should know this too)
    @NotNull
    private String primaryKeyTableCatalog;
    @NotNull
    private String primaryKeyTableScheam;
    @NotNull
    private String primaryKeyColumnTable;
    @NotNull
    private String primaryKeyColumnName;
    @NotNull
    private String foreignKeyColumnCatalog;
    @NotNull
    private String foreignKeyColumnSchema;
    @NotNull
    private String foreignKeyColumnTable;
    @NotNull
    private String foreignKeyColumnName;

    private int sequenceNumber;
    private int updateRule;
    private int deleteRule;

    @NotNull
    private String foreignKeyName;
    @NotNull
    private String primaryKeyName;
    private int deferrability;


    public JDBCRelationInfo(@NotNull String primaryKeyTableCatalog, @NotNull String primaryKeyTableScheam, @NotNull String primaryKeyColumnTable, @NotNull String primaryKeyColumnName, @NotNull String foreignKeyColumnCatalog, @NotNull String foreignKeyColumnSchema, @NotNull String foreignKeyColumnTable, @NotNull String foreignKeyColumnName, int sequenceNumber, int updateRule, int deleteRule, @NotNull String foreignKeyName, @NotNull String primaryKeyName, int deferrability)
    {
        this.primaryKeyTableCatalog = primaryKeyTableCatalog;
        this.primaryKeyTableScheam = primaryKeyTableScheam;
        this.primaryKeyColumnTable = primaryKeyColumnTable;
        this.primaryKeyColumnName = primaryKeyColumnName;
        this.foreignKeyColumnCatalog = foreignKeyColumnCatalog;
        this.foreignKeyColumnSchema = foreignKeyColumnSchema;
        this.foreignKeyColumnTable = foreignKeyColumnTable;
        this.foreignKeyColumnName = foreignKeyColumnName;
        this.sequenceNumber = sequenceNumber;
        this.updateRule = updateRule;
        this.deleteRule = deleteRule;
        this.foreignKeyName = foreignKeyName;
        this.primaryKeyName = primaryKeyName;
        this.deferrability = deferrability;
    }


    @NotNull
    public String getPrimaryKeyTableCatalog()
    {
        return primaryKeyTableCatalog;
    }


    @NotNull
    public String getPrimaryKeyTableScheam()
    {
        return primaryKeyTableScheam;
    }


    @NotNull
    public String getPrimaryKeyColumnTable()
    {
        return primaryKeyColumnTable;
    }


    @NotNull
    public String getPrimaryKeyColumnName()
    {
        return primaryKeyColumnName;
    }


    @NotNull
    public String getForeignKeyColumnCatalog()
    {
        return foreignKeyColumnCatalog;
    }


    @NotNull
    public String getForeignKeyColumnSchema()
    {
        return foreignKeyColumnSchema;
    }


    @NotNull
    public String getForeignKeyColumnTable()
    {
        return foreignKeyColumnTable;
    }


    @NotNull
    public String getForeignKeyColumnName()
    {
        return foreignKeyColumnName;
    }


    public int getSequenceNumber()
    {
        return sequenceNumber;
    }


    public int getUpdateRule()
    {
        return updateRule;
    }


    public int getDeleteRule()
    {
        return deleteRule;
    }


    @NotNull
    public String getForeignKeyName()
    {
        return foreignKeyName;
    }


    @NotNull
    public String getPrimaryKeyName()
    {
        return primaryKeyName;
    }


    public int getDeferrability()
    {
        return deferrability;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JDBCRelationInfo that = (JDBCRelationInfo) o;

        if (deferrability != that.deferrability) return false;
        if (deleteRule != that.deleteRule) return false;
        if (sequenceNumber != that.sequenceNumber) return false;
        if (updateRule != that.updateRule) return false;
        if (foreignKeyColumnCatalog != null ? !foreignKeyColumnCatalog.equals(that.foreignKeyColumnCatalog) : that.foreignKeyColumnCatalog != null) return false;
        if (foreignKeyColumnName != null ? !foreignKeyColumnName.equals(that.foreignKeyColumnName) : that.foreignKeyColumnName != null) return false;
        if (foreignKeyColumnSchema != null ? !foreignKeyColumnSchema.equals(that.foreignKeyColumnSchema) : that.foreignKeyColumnSchema != null) return false;
        if (foreignKeyColumnTable != null ? !foreignKeyColumnTable.equals(that.foreignKeyColumnTable) : that.foreignKeyColumnTable != null) return false;
        if (foreignKeyName != null ? !foreignKeyName.equals(that.foreignKeyName) : that.foreignKeyName != null) return false;
        if (primaryKeyColumnName != null ? !primaryKeyColumnName.equals(that.primaryKeyColumnName) : that.primaryKeyColumnName != null) return false;
        if (primaryKeyColumnTable != null ? !primaryKeyColumnTable.equals(that.primaryKeyColumnTable) : that.primaryKeyColumnTable != null) return false;
        if (primaryKeyName != null ? !primaryKeyName.equals(that.primaryKeyName) : that.primaryKeyName != null) return false;
        if (primaryKeyTableCatalog != null ? !primaryKeyTableCatalog.equals(that.primaryKeyTableCatalog) : that.primaryKeyTableCatalog != null) return false;
        return !(primaryKeyTableScheam != null ? !primaryKeyTableScheam.equals(that.primaryKeyTableScheam) : that.primaryKeyTableScheam != null);

    }


    public int hashCode()
    {
        int result;
        result = (primaryKeyTableCatalog != null ? primaryKeyTableCatalog.hashCode() : 0);
        result = 29 * result + (primaryKeyTableScheam != null ? primaryKeyTableScheam.hashCode() : 0);
        result = 29 * result + (primaryKeyColumnTable != null ? primaryKeyColumnTable.hashCode() : 0);
        result = 29 * result + (primaryKeyColumnName != null ? primaryKeyColumnName.hashCode() : 0);
        result = 29 * result + (foreignKeyColumnCatalog != null ? foreignKeyColumnCatalog.hashCode() : 0);
        result = 29 * result + (foreignKeyColumnSchema != null ? foreignKeyColumnSchema.hashCode() : 0);
        result = 29 * result + (foreignKeyColumnTable != null ? foreignKeyColumnTable.hashCode() : 0);
        result = 29 * result + (foreignKeyColumnName != null ? foreignKeyColumnName.hashCode() : 0);
        result = 29 * result + sequenceNumber;
        result = 29 * result + updateRule;
        result = 29 * result + deleteRule;
        result = 29 * result + (foreignKeyName != null ? foreignKeyName.hashCode() : 0);
        result = 29 * result + (primaryKeyName != null ? primaryKeyName.hashCode() : 0);
        result = 29 * result + deferrability;
        return result;
    }


    @NotNull
    public String toString()
    {
        return "JDBCRelationInfo{" +
               "primaryKeyTableCatalog='" + primaryKeyTableCatalog + "'" +
               ", primaryKeyTableScheam='" + primaryKeyTableScheam + "'" +
               ", primaryKeyColumnTable='" + primaryKeyColumnTable + "'" +
               ", primaryKeyColumnName='" + primaryKeyColumnName + "'" +
               ", foreignKeyColumnCatalog='" + foreignKeyColumnCatalog + "'" +
               ", foreignKeyColumnSchema='" + foreignKeyColumnSchema + "'" +
               ", foreignKeyColumnTable='" + foreignKeyColumnTable + "'" +
               ", foreignKeyColumnName='" + foreignKeyColumnName + "'" +
               ", sequenceNumber=" + sequenceNumber +
               ", updateRule=" + updateRule +
               ", deleteRule=" + deleteRule +
               ", foreignKeyName='" + foreignKeyName + "'" +
               ", primaryKeyName='" + primaryKeyName + "'" +
               ", deferrability=" + deferrability +
               "}";
    }
}
