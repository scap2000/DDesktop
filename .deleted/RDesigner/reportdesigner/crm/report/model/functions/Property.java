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
package org.pentaho.reportdesigner.crm.report.model.functions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Martin
 * Date: 26.07.2006
 * Time: 17:46:43
 */
public class Property
{
    @NotNull
    private Class type;
    @NotNull
    private String name;
    @NotNull
    private String typeString;
    @NotNull
    private String getterName;
    @NotNull
    private String setterName;
    @Nullable
    private String referenceTypeString;

    @NotNull
    private String group;
    private int sortingID;


    public Property(@NotNull Class type, @NotNull String name, @NotNull String group, int sortingID)
    {
        this.type = type;
        this.name = name;
        this.group = group;
        this.sortingID = sortingID;

        typeString = getTypeString(type);

        String firstCharacter = name.substring(0, 1);
        String rest = name.substring(1);
        String capitalizedName = firstCharacter.toUpperCase() + rest;

        //noinspection ObjectEquality
        if (type == boolean.class || type == Boolean.class)
        {
            getterName = "is" + capitalizedName;//NON-NLS
        }
        else
        {
            getterName = "get" + capitalizedName;//NON-NLS
        }
        setterName = "set" + capitalizedName;//NON-NLS

        referenceTypeString = getReferenceTypeString(type);
    }


    @NotNull
    public String getName()
    {
        return name;
    }


    @NotNull
    public Class getType()
    {
        return type;
    }


    @NotNull
    public String getGetterName()
    {
        return getterName;
    }


    @NotNull
    public String getSetterName()
    {
        return setterName;
    }


    @NotNull
    public String getGroup()
    {
        return group;
    }


    public int getSortingID()
    {
        return sortingID;
    }


    @SuppressWarnings({"ObjectEquality"})
    public boolean isPrimitiveType()
    {
        if (type == boolean.class)
        {
            return true;
        }
        else if (type == byte.class)
        {
            return true;
        }
        else if (type == char.class)
        {
            return true;
        }
        else if (type == short.class)
        {
            return true;
        }
        else if (type == int.class)
        {
            return true;
        }
        else if (type == long.class)
        {
            return true;
        }
        else if (type == float.class)
        {
            return true;
        }
        else if (type == double.class)
        {
            return true;
        }
        return false;
    }


    @NotNull
    public String getTypeString()
    {
        return typeString;
    }


    @SuppressWarnings({"ObjectEquality"})
    @NotNull
    public static String getTypeString(@NotNull Class type)
    {
        if (type == boolean.class)
        {
            return "Z";//NON-NLS
        }
        else if (type == byte.class)
        {
            return "B";//NON-NLS
        }
        else if (type == char.class)
        {
            return "C";//NON-NLS
        }
        else if (type == short.class)
        {
            return "S";//NON-NLS
        }
        else if (type == int.class)
        {
            return "I";//NON-NLS
        }
        else if (type == long.class)
        {
            return "J";//NON-NLS
        }
        else if (type == float.class)
        {
            return "F";//NON-NLS
        }
        else if (type == double.class)
        {
            return "D";//NON-NLS
        }
        else if (!type.isArray())
        {
            return "L" + type.getName().replace('.', '/') + ";";//NON-NLS
        }
        else
        {
            return type.getName().replace('.', '/');
        }
    }


    @Nullable
    public String getReferenceTypeString()
    {
        return referenceTypeString;
    }


    @SuppressWarnings({"ObjectEquality"})
    @Nullable
    public static String getReferenceTypeString(@NotNull Class type)
    {
        if (type == boolean.class)
        {
            return "java/lang/Boolean";//NON-NLS
        }
        else if (type == byte.class)
        {
            return "java/lang/Byte";//NON-NLS
        }
        else if (type == char.class)
        {
            return "java/lang/Character";//NON-NLS
        }
        else if (type == short.class)
        {
            return "java/lang/Short";//NON-NLS
        }
        else if (type == int.class)
        {
            return "java/lang/Integer";//NON-NLS
        }
        else if (type == long.class)
        {
            return "java/lang/Long";//NON-NLS
        }
        else if (type == float.class)
        {
            return "java/lang/Float";//NON-NLS
        }
        else if (type == double.class)
        {
            return "java/lang/Double";//NON-NLS
        }
        else
        {
            //is already a reference type
            return null;
        }
    }


    @NotNull
    public String toString()
    {
        return "Property{" +
               "getterName='" + getterName + "'" +
               ", type=" + type +
               ", name='" + name + "'" +
               ", typeString='" + typeString + "'" +
               ", setterName='" + setterName + "'" +
               ", referenceTypeString='" + referenceTypeString + "'" +
               "}";
    }
}
