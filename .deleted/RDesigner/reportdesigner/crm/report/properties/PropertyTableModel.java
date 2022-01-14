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
package org.pentaho.reportdesigner.crm.report.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.GroupingPropertyDescriptor;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.table.AbstractTableModel;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 07:42:31
 */
public class PropertyTableModel extends AbstractTableModel
{

    @NotNull
    private Style style;

    @NotNull
    private PropertyEditorPanel propertyEditorPanel;

    @NotNull
    private ArrayList<Object> propertyDescriptors;

    @NotNull
    private HashMap<String, HashSet<PropertyDescriptor>> groupedProperties;


    public PropertyTableModel(@NotNull Style style, @NotNull PropertyEditorPanel propertyEditorPanel)
    {
        this.style = style;
        this.propertyEditorPanel = propertyEditorPanel;
        propertyDescriptors = new ArrayList<Object>();

        groupedProperties = new HashMap<String, HashSet<PropertyDescriptor>>();
    }


    public int getRowCount()
    {
        return propertyDescriptors.size();
    }


    public int getColumnCount()
    {
        return 2;
    }


    @NotNull
    public String getColumnName(int column)
    {
        switch (column)
        {
            case 0:
                return TranslationManager.getInstance().getTranslation("R", "PropertyTable.PropertyName");
            case 1:
                return TranslationManager.getInstance().getTranslation("R", "PropertyTable.PropertyValue");
        }

        return TranslationManager.getInstance().getTranslation("R", "PropertyTable.Error");
    }


    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex == 1 && getObject(rowIndex) instanceof PropertyDescriptor;

    }


    public void setValueAt(@Nullable Object aValue, int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 1:
                propertyEditorPanel.setValues((PropertyDescriptor) propertyDescriptors.get(rowIndex), aValue);
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }


    @Nullable
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return propertyDescriptors.get(rowIndex);
            case 1:
                Object obj = propertyDescriptors.get(rowIndex);
                if (obj instanceof PropertyDescriptor)
                {
                    PropertyDescriptor propertyDescriptor = (PropertyDescriptor) obj;
                    return propertyEditorPanel.getValues(propertyDescriptor);
                }
                else
                {
                    return propertyDescriptors.get(rowIndex);
                }
        }

        return null;
    }


    @NotNull
    public Object getObject(int rowIndex)
    {
        return propertyDescriptors.get(rowIndex);
    }


    @NotNull
    public ArrayList<Object> getPropertyDescriptors()
    {
        return propertyDescriptors;
    }


    @NotNull
    public Style getStyle()
    {
        return style;
    }


    public void setStyle(@NotNull Style style)
    {
        this.style = style;

        setPropertyDescriptors(groupedProperties);//set the same properties again to update the sorting style
    }


    public void setPropertyDescriptors(@NotNull HashMap<String, HashSet<PropertyDescriptor>> groupedProperties)
    {
        this.groupedProperties = groupedProperties;
        propertyDescriptors.clear();

        if (style == Style.GROUPED)
        {
            @Nullable
            HashSet<PropertyDescriptor> propertyDescriptorsUnknownGroup = groupedProperties.get(PropertyKeys.GROUP_UNKNOWN);
            if (propertyDescriptorsUnknownGroup != null && !propertyDescriptorsUnknownGroup.isEmpty())
            {
                addGroup(propertyDescriptorsUnknownGroup);
            }

            @Nullable
            HashSet<PropertyDescriptor> propertyDescriptorsGroupRequired = groupedProperties.get(PropertyKeys.GROUP_REQUIRED);
            if (propertyDescriptorsGroupRequired != null && !propertyDescriptorsGroupRequired.isEmpty())
            {
                propertyDescriptors.add(PropertyKeys.GROUP_REQUIRED);
                addGroup(propertyDescriptorsGroupRequired);
            }

            @Nullable
            HashSet<PropertyDescriptor> propertyDescriptorsGroupOptional = groupedProperties.get(PropertyKeys.GROUP_OPTIONAL);
            if (propertyDescriptorsGroupOptional != null && !propertyDescriptorsGroupOptional.isEmpty())
            {
                propertyDescriptors.add(PropertyKeys.GROUP_OPTIONAL);
                addGroup(propertyDescriptorsGroupOptional);
            }


            @Nullable
            HashSet<PropertyDescriptor> propertyDescriptorsGroupID = groupedProperties.get(PropertyKeys.GROUP_ID);
            if (propertyDescriptorsGroupID != null && !propertyDescriptorsGroupID.isEmpty())
            {
                propertyDescriptors.add(PropertyKeys.GROUP_ID);
                addGroup(propertyDescriptorsGroupID);
            }

            @Nullable
            HashSet<PropertyDescriptor> propertyDescriptorsGroupAppearance = groupedProperties.get(PropertyKeys.GROUP_APPEARANCE);
            if (propertyDescriptorsGroupAppearance != null && !propertyDescriptorsGroupAppearance.isEmpty())
            {
                propertyDescriptors.add(PropertyKeys.GROUP_APPEARANCE);
                addGroup(propertyDescriptorsGroupAppearance);
            }

            @Nullable
            HashSet<PropertyDescriptor> propertyDescriptorsGroupSpatial = groupedProperties.get(PropertyKeys.GROUP_SPATIAL);
            if (propertyDescriptorsGroupSpatial != null && !propertyDescriptorsGroupSpatial.isEmpty())
            {
                propertyDescriptors.add(PropertyKeys.GROUP_SPATIAL);
                addGroup(propertyDescriptorsGroupSpatial);
            }

            @Nullable
            HashSet<PropertyDescriptor> propertyDescriptorsGroupOutput = groupedProperties.get(PropertyKeys.GROUP_OUTPUT);
            if (propertyDescriptorsGroupOutput != null && !propertyDescriptorsGroupOutput.isEmpty())
            {
                propertyDescriptors.add(PropertyKeys.GROUP_OUTPUT);
                addGroup(propertyDescriptorsGroupOutput);
            }
        }
        else
        {
            ArrayList<PropertyDescriptor> pds = new ArrayList<PropertyDescriptor>();
            Collection<HashSet<PropertyDescriptor>> sets = groupedProperties.values();
            for (HashSet<PropertyDescriptor> set : sets)
            {
                pds.addAll(set);
            }

            Comparator<PropertyDescriptor> c = new Comparator<PropertyDescriptor>()
            {
                public int compare(@NotNull PropertyDescriptor o1, @NotNull PropertyDescriptor o2)
                {
                    String name1 = o1.getName() + (o1.getReadMethod().getReturnType().isArray() ? "[]" : "");
                    String localizedName1 = TranslationManager.getInstance().getTranslation("R", "Property." + name1);

                    String name2 = o2.getName() + (o2.getReadMethod().getReturnType().isArray() ? "[]" : "");
                    String localizedName2 = TranslationManager.getInstance().getTranslation("R", "Property." + name2);

                    return localizedName1.compareToIgnoreCase(localizedName2);
                }
            };

            if (style == Style.ASCENDING)
            {
                Collections.sort(pds, c);
            }
            else
            {
                Collections.sort(pds, Collections.reverseOrder(c));
            }

            propertyDescriptors.addAll(pds);
        }
        fireTableDataChanged();
    }


    private void addGroup(@NotNull HashSet<PropertyDescriptor> propDes)
    {
        ArrayList<PropertyDescriptor> pds = new ArrayList<PropertyDescriptor>(propDes);
        Collections.sort(pds, new Comparator<PropertyDescriptor>()
        {
            public int compare(@NotNull PropertyDescriptor o1, @NotNull PropertyDescriptor o2)
            {
                if (o1 instanceof GroupingPropertyDescriptor && o2 instanceof GroupingPropertyDescriptor)
                {
                    GroupingPropertyDescriptor gpd1 = (GroupingPropertyDescriptor) o1;
                    GroupingPropertyDescriptor gpd2 = (GroupingPropertyDescriptor) o2;

                    if (gpd1.getSortingId() != -1 && gpd2.getSortingId() != -1)
                    {
                        return Integer.valueOf(gpd1.getSortingId()).compareTo(Integer.valueOf(gpd2.getSortingId()));
                    }
                }

                String name1 = o1.getName() + (o1.getReadMethod().getReturnType().isArray() ? "[]" : "");
                String localizedName1 = TranslationManager.getInstance().getTranslation("R", "Property." + name1);

                String name2 = o2.getName() + (o2.getReadMethod().getReturnType().isArray() ? "[]" : "");
                String localizedName2 = TranslationManager.getInstance().getTranslation("R", "Property." + name2);

                return localizedName1.compareToIgnoreCase(localizedName2);
            }
        });

        propertyDescriptors.addAll(pds);
    }


}
