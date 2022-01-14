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
package org.pentaho.reportdesigner.crm.report.datasetplugin.staticfactory;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertyInfo;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 16.02.2006
 * Time: 16:16:41
 */
public class ParametersTableModel extends AbstractTableModel
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ParametersTableModel.class.getName());

    @NotNull
    private ArrayList<PropertyInfo> propertyInfos;


    public ParametersTableModel(@NotNull ArrayList<PropertyInfo> propertyInfos)
    {
        this.propertyInfos = propertyInfos;
    }


    public int getRowCount()
    {
        return propertyInfos.size();
    }


    public int getColumnCount()
    {
        return 2;
    }


    @NotNull
    public ArrayList<PropertyInfo> getProperties()
    {
        return new ArrayList<PropertyInfo>(propertyInfos);
    }


    @Nullable
    public PropertyInfo getProperty(int index)
    {
        try
        {
            return propertyInfos.get(index);
        }
        catch (Exception e)
        {
            if (ParametersTableModel.LOG.isLoggable(Level.FINE)) ParametersTableModel.LOG.log(Level.FINE, "PropertiesTableModel.getProperty ", e);
        }
        return null;
    }


    public void addProperty(@NotNull PropertyInfo propertyInfo)
    {
        propertyInfos.add(propertyInfo);
        fireTableRowsInserted(propertyInfos.size() - 1, propertyInfos.size() - 1);
    }


    private void removeProperty(int index)
    {
        propertyInfos.remove(index);
        fireTableRowsDeleted(index, index);
    }


    public void removeProperties(@NotNull int[] indices)
    {
        for (int i = indices.length - 1; i >= 0; i--)
        {
            int index = indices[i];
            removeProperty(index);
        }
    }


    @Nullable
    public String getColumnName(int column)
    {
        if (column == 0)
        {
            return TranslationManager.getInstance().getTranslation("R", "PropertiesDataSetReportElement.Type.Title");
        }
        else if (column == 1)
        {
            return TranslationManager.getInstance().getTranslation("R", "PropertiesDataSetReportElement.Value.Title");
        }
        return null;
    }


    public void setValueAt(@Nullable Object aValue, int rowIndex, int columnIndex)
    {
        if (columnIndex == 1)
        {
            propertyInfos.get(rowIndex).setValue(aValue);
        }
    }


    @Nullable
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
        {
            HashSet<Class> hashSet = new HashSet<Class>();
            hashSet.add(propertyInfos.get(rowIndex).getClazz());
            return hashSet;
        }
        else if (columnIndex == 1)
        {
            HashSet<Object> hashSet = new HashSet<Object>();
            hashSet.add(propertyInfos.get(rowIndex).getValue());
            return hashSet;
        }
        return null;
    }
}
