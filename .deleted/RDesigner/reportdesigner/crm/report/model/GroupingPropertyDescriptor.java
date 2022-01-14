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

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 30.01.2006
 * Time: 10:11:00
 */
public class GroupingPropertyDescriptor extends PropertyDescriptor
{
    @NotNull
    private static final HashSet<String> FIELD_NAMES;
    @NotNull
    private static final HashSet<String> ELEMENT_NAMES;
    @NotNull
    private static final HashSet<String> STRING_COLOR_NAMES;
    @NotNull
    private static final HashSet<String> QUERIES_NAMES;
    @NotNull
    private static final HashSet<String> NULLABLE_NAMES;


    static
    {
        FIELD_NAMES = new HashSet<String>();
        FIELD_NAMES.add("field");//NON-NLS
        FIELD_NAMES.add("fieldName");//NON-NLS
        FIELD_NAMES.add("field1");//NON-NLS
        FIELD_NAMES.add("field2");//NON-NLS
        FIELD_NAMES.add("fieldName1");//NON-NLS
        FIELD_NAMES.add("fieldName2");//NON-NLS
        FIELD_NAMES.add("fields");//NON-NLS
        FIELD_NAMES.add("fieldNames");//NON-NLS
        FIELD_NAMES.add("otherField");//NON-NLS
        FIELD_NAMES.add("groupFields");//NON-NLS
        FIELD_NAMES.add("resetGroup");//NON-NLS
        FIELD_NAMES.add("seriesName");//NON-NLS

        FIELD_NAMES.add("dataSource");//NON-NLS
        FIELD_NAMES.add("categoryColumn");//NON-NLS
        FIELD_NAMES.add("valueColumn");//NON-NLS
        FIELD_NAMES.add("ignoreColumn");//NON-NLS
        FIELD_NAMES.add("seriesColumn");//NON-NLS
        FIELD_NAMES.add("dividend");//NON-NLS
        FIELD_NAMES.add("divisor");//NON-NLS      
        
        ELEMENT_NAMES = new HashSet<String>();
        ELEMENT_NAMES.add("element");//NON-NLS
        ELEMENT_NAMES.add("elementName");//NON-NLS
        ELEMENT_NAMES.add("elementNames");//NON-NLS

        STRING_COLOR_NAMES = new HashSet<String>();
        STRING_COLOR_NAMES.add("seriesColor");//NON-NLS
        STRING_COLOR_NAMES.add("borderColor");//NON-NLS
        STRING_COLOR_NAMES.add("backgroundColor");//NON-NLS

        QUERIES_NAMES = new HashSet<String>();
        QUERIES_NAMES.add("query");//NON-NLS

        NULLABLE_NAMES = new HashSet<String>();
        NULLABLE_NAMES.add("encoding");//NON-NLS
        NULLABLE_NAMES.add("title");//NON-NLS
        NULLABLE_NAMES.add("backgroundImage");//NON-NLS
        NULLABLE_NAMES.add("categoricalItemLabelRotation");//NON-NLS
        NULLABLE_NAMES.add("categoricalLabelDateFormat");//NON-NLS
        NULLABLE_NAMES.add("categoricalLabelDecimalFormat");//NON-NLS
        NULLABLE_NAMES.add("categoricalLabelFormat");//NON-NLS
        NULLABLE_NAMES.add("categoryAxisLabel");//NON-NLS
        NULLABLE_NAMES.add("valueAxisLabel");//NON-NLS
        NULLABLE_NAMES.add("chartDirectory");//NON-NLS
        NULLABLE_NAMES.add("chartFile");//NON-NLS
        NULLABLE_NAMES.add("chartUrlMask");//NON-NLS
        NULLABLE_NAMES.add("labelRotation");//NON-NLS
        NULLABLE_NAMES.add("categoryColumn");//NON-NLS
        NULLABLE_NAMES.add("resetGroup");//NON-NLS
        NULLABLE_NAMES.add("group");//NON-NLS
        NULLABLE_NAMES.add("lineStyle");//NON-NLS
        NULLABLE_NAMES.add("valueColumn");//NON-NLS
        NULLABLE_NAMES.add("seriesColumn");//NON-NLS

    }


    @NotNull
    private Class<?> beanClass;
    @NotNull
    private String propertyGroupName;
    private boolean important;
    private int sortingId;


    public GroupingPropertyDescriptor(@NotNull String propertyName, @NotNull String propertyGroupName, boolean important, int sortingId, @NotNull Class<?> beanClass) throws IntrospectionException
    {
        super(propertyName, beanClass);
        this.beanClass = beanClass;
        this.propertyGroupName = propertyGroupName;
        this.important = important;
        this.sortingId = sortingId;
    }


    @NotNull
    public String getPropertyGroupName()
    {
        return propertyGroupName;
    }


    public boolean isImportant()
    {
        return important;
    }


    public int getSortingId()
    {
        return sortingId;
    }


    @NotNull
    public Class getBeanClass()
    {
        return beanClass;
    }


    public boolean isField()
    {
        if (String.class.equals(getPropertyType()))
        {
            return FIELD_NAMES.contains(getName());
        }
        else if (String[].class.equals(getPropertyType()))
        {
            return FIELD_NAMES.contains(getName());
        }

        return false;
    }


    public boolean isElement()
    {
        if (String.class.equals(getPropertyType()))
        {
            return ELEMENT_NAMES.contains(getName());
        }
        else if (String[].class.equals(getPropertyType()))
        {
            return ELEMENT_NAMES.contains(getName());
        }

        return false;
    }


    public boolean isRegularFile()
    {
        return getBeanClass().getName().equals(SubReportElement.class.getName()) && getName().equals(PropertyKeys.FILE_PATH);
    }


    public boolean isStringColor()
    {
        if (String.class.equals(getPropertyType()))
        {
            return STRING_COLOR_NAMES.contains(getName());
        }
        else if (String[].class.equals(getPropertyType()))
        {
            return STRING_COLOR_NAMES.contains(getName());
        }

        return false;
    }


    public boolean isQuery()
    {
        if (String.class.equals(getPropertyType()))
        {
            return QUERIES_NAMES.contains(getName());
        }

        return false;
    }


    public boolean isNullable()
    {
        if (String.class.equals(getPropertyType()))
        {
            return NULLABLE_NAMES.contains(getName());
        }

        return false;
    }
}
