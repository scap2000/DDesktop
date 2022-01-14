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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.function.Expression;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.GroupingPropertyDescriptor;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionsElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportDataElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.crm.report.properties.editors.ChartEditor;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 07:40:47
 */
public class PropertyEditorPanel extends JPanel implements PropertyChangeListener
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(PropertyEditorPanel.class.getName());

    @NotNull
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    @NotNull
    private static final String TABLE = "table";
    @NotNull
    private static final String FUNCTIONS = "functions";

    @NotNull
    private static final String CUSTOM = "custom";
    @NotNull
    private static final String CHART = "chart";

    @NotNull
    private PropertyTable propertyTable;
    @NotNull
    private PropertyTableModel propertyTableModel;

    @NotNull
    private Object[] objects;
    @NotNull
    private ReportDialog reportDialog;

    @NotNull
    private ReportElementSelectionModel reportElementSelectionModel;

    @NotNull
    private CardLayout cardLayout;
    @NotNull
    private HashSet<String> exludedExpressionProperties;
    @NotNull
    private JPanel customPanel;
    @NotNull
    private JPanel chartPanel;


    public PropertyEditorPanel(@NotNull final ReportDialog reportDialog, @NotNull ReportElementSelectionModel reportElementSelectionModel)
    {
        this.reportDialog = reportDialog;
        this.reportElementSelectionModel = reportElementSelectionModel;
        propertyTableModel = new PropertyTableModel(Style.GROUPED, this);
        propertyTable = new PropertyTable(this, propertyTableModel);

        cardLayout = new CardLayout(0, 0);
        setLayout(cardLayout);

        JPanel tableHelperPanel = new JPanel(new BorderLayout());
        tableHelperPanel.add(propertyTable);
        JScrollPane scrollPane = new JScrollPane(tableHelperPanel);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setBlockIncrement(50);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        scrollPane.setPreferredSize(new Dimension(200, 300));

        JPanel groupSortPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JToggleButton groupButton = new JToggleButton(IconLoader.getInstance().getGroupIcon());
        JToggleButton sortAscendingButton = new JToggleButton(IconLoader.getInstance().getSortAscendingIcon());
        JToggleButton sortDescendingButton = new JToggleButton(IconLoader.getInstance().getSortDescendingIcon());

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(groupButton);
        buttonGroup.add(sortAscendingButton);
        buttonGroup.add(sortDescendingButton);

        groupButton.setSelected(true);

        groupButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                propertyTableModel.setStyle(Style.GROUPED);
            }
        });

        sortAscendingButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                propertyTableModel.setStyle(Style.ASCENDING);
            }
        });

        sortDescendingButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                propertyTableModel.setStyle(Style.DESCENDING);
            }
        });

        groupSortPanel.add(groupButton);
        groupSortPanel.add(sortAscendingButton);
        groupSortPanel.add(sortDescendingButton);

        scrollPane.setColumnHeaderView(groupSortPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, TABLE);
        add(new ExpressionChooser(reportDialog, reportElementSelectionModel.getReport(), reportElementSelectionModel), FUNCTIONS);

        customPanel = new JPanel(new BorderLayout());
        add(customPanel, CUSTOM);

        chartPanel = new JPanel(new GridBagLayout());
        JButton editButton = new JButton(TranslationManager.getInstance().getTranslation("R", "PropertyEditorPanel.EditChart"));
        chartPanel.add(editButton);
        add(chartPanel, CHART);
        editButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                ChartEditor.showChartEditor(reportDialog.getRootJComponent(), TranslationManager.getInstance().getTranslation("R", "ChartEditor.Title"), (ChartReportElement) objects[0], reportDialog);
            }
        });

        cardLayout.show(this, TABLE);

        objects = EMPTY_OBJECT_ARRAY;

        exludedExpressionProperties = new HashSet<String>();
        exludedExpressionProperties.add(PropertyKeys.VALUE);
        exludedExpressionProperties.add(PropertyKeys.ACTIVE);
        exludedExpressionProperties.add(PropertyKeys.DATA_ROW);
        exludedExpressionProperties.add(PropertyKeys.INSTANCE);
    }


    @NotNull
    public ReportDialog getReportDialog()
    {
        return reportDialog;
    }


    private boolean isExludedProperty(@NotNull BeanInfo beanInfo, @NotNull PropertyDescriptor propertyDescriptor)
    {
        boolean assignableFrom = Expression.class.isAssignableFrom(beanInfo.getBeanDescriptor().getBeanClass());
        boolean b = exludedExpressionProperties.contains(propertyDescriptor.getName());
        return assignableFrom && b;
    }


    public void selectProperty(@NotNull String propertyName, boolean requestFocus)
    {
        ArrayList<Object> propertyDescriptors = propertyTableModel.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.size(); i++)
        {
            Object o = propertyDescriptors.get(i);
            if (o instanceof PropertyDescriptor)
            {
                PropertyDescriptor propertyDescriptor = (PropertyDescriptor) o;
                if (propertyDescriptor.getName().equals(propertyName))
                {
                    propertyTable.getSelectionModel().setSelectionInterval(i, i);
                    if (requestFocus)
                    {
                        propertyTable.requestFocusInWindow();
                    }
                }
            }
        }
    }


    public void setBeans(@NotNull final Object[] objects)
    {
        //noinspection ConstantConditions
        if (objects == null)
        {
            throw new IllegalArgumentException("objects must not be null");
        }

        if (objects.length == 1 && objects[0] instanceof ReportFunctionsElement)
        {
            cardLayout.show(this, FUNCTIONS);
        }
        else if (objects.length == 1 && objects[0] instanceof ChartReportElement)
        {
            cardLayout.show(this, CHART);
        }
        else if (objects.length == 1 && objects[0] instanceof DataSetReportElement)
        {
            final DataSetReportElement dataSetReportElement = (DataSetReportElement) objects[0];
            customPanel.removeAll();
            customPanel.add(dataSetReportElement.getInfoComponent(), BorderLayout.CENTER);
            if (dataSetReportElement.canConfigure())
            {
                JButton configureButton = new JButton(TranslationManager.getInstance().getTranslation("R", "PropertyEditorPanel.Configure"));
                configureButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        dataSetReportElement.showConfigurationComponent(reportDialog, false);
                        setBeans(objects);//cheap way to refresh
                    }
                });
                customPanel.add(configureButton, BorderLayout.SOUTH);
            }

            customPanel.revalidate();
            cardLayout.show(this, CUSTOM);
        }
        else if (objects.length == 1 && objects[0] instanceof SubReportDataElement)
        {
            final SubReportDataElement subReportDataElement = (SubReportDataElement) objects[0];
            customPanel.removeAll();
            customPanel.add(subReportDataElement.getInfoComponent(), BorderLayout.CENTER);
            if (subReportDataElement.canConfigure())
            {
                JButton configureButton = new JButton(TranslationManager.getInstance().getTranslation("R", "PropertyEditorPanel.Configure"));
                configureButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        subReportDataElement.showConfigurationComponent(reportDialog);
                        setBeans(objects);//cheap way to refresh
                    }
                });
                customPanel.add(configureButton, BorderLayout.SOUTH);
            }

            customPanel.revalidate();
            cardLayout.show(this, CUSTOM);
        }
        else
        {
            cardLayout.show(this, TABLE);
        }

        propertyTable.editingStopped(new ChangeEvent(this));

        for (Object o : this.objects)
        {
            if (o instanceof ReportElement)
            {
                ReportElement reportElement = (ReportElement) o;
                reportElement.removePropertyChangeListener(this);
            }
        }

        this.objects = objects;
        //search properties available in all beans
        HashMap<String, HashSet<PropertyDescriptor>> groupedProperties = new HashMap<String, HashSet<PropertyDescriptor>>();
        try
        {
            if (objects.length > 0)
            {
                BeanInfo beanInfo = Introspector.getBeanInfo(objects[0].getClass(), Introspector.USE_ALL_BEANINFO);

                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
                {
                    Class<?> clazz1 = propertyDescriptor.getPropertyType();
                    if (clazz1 != null && !isExludedProperty(beanInfo, propertyDescriptor))
                    {
                        if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null)
                        {
                            addToGroupedProperties(groupedProperties, propertyDescriptor);
                        }
                    }
                }
            }

            if (objects.length > 1)
            {
                for (int i = 1; i < objects.length; i++)
                {
                    BeanInfo beanInfo = Introspector.getBeanInfo(objects[i].getClass());

                    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                    HashSet<PropertyDescriptor> beanProperties = new HashSet<PropertyDescriptor>();

                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
                    {
                        if (!isExludedProperty(beanInfo, propertyDescriptor))
                        {
                            beanProperties.add(propertyDescriptor);
                        }
                    }

                    retainProperties(groupedProperties, beanProperties);
                }
            }
        }
        catch (IntrospectionException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }

        for (Object o : this.objects)
        {
            if (o instanceof ReportElement)
            {
                ReportElement reportElement = (ReportElement) o;
                reportElement.addPropertyChangeListener(this);
            }
        }

        propertyTableModel.setPropertyDescriptors(groupedProperties);
    }


    private void addToGroupedProperties(@NotNull HashMap<String, HashSet<PropertyDescriptor>> groupedProperties, @NotNull PropertyDescriptor propertyDescriptor)
    {
        if (propertyDescriptor instanceof GroupingPropertyDescriptor)
        {
            GroupingPropertyDescriptor groupingPropertyDescriptor = (GroupingPropertyDescriptor) propertyDescriptor;
            @Nullable
            HashSet<PropertyDescriptor> propertyDescriptors = groupedProperties.get(groupingPropertyDescriptor.getPropertyGroupName());
            if (propertyDescriptors == null)
            {
                propertyDescriptors = new HashSet<PropertyDescriptor>();
                groupedProperties.put(groupingPropertyDescriptor.getPropertyGroupName(), propertyDescriptors);
            }

            //search all maps for the same property, remove this property if it is contained in another group
            Collection<HashSet<PropertyDescriptor>> hashSets = groupedProperties.values();
            for (HashSet<PropertyDescriptor> descriptors : hashSets)
            {
                descriptors.remove(groupingPropertyDescriptor);
            }

            //add it to the correct set
            propertyDescriptors.add(groupingPropertyDescriptor);
        }
        else
        {
            @Nullable
            HashSet<PropertyDescriptor> propertyDescriptors = groupedProperties.get(PropertyKeys.GROUP_UNKNOWN);
            if (propertyDescriptors == null)
            {
                propertyDescriptors = new HashSet<PropertyDescriptor>();
                groupedProperties.put(PropertyKeys.GROUP_UNKNOWN, propertyDescriptors);
            }

            //search all maps for the same property, remove this property if it is contained in another group
            boolean alreadyAdded = false;
            Collection<HashSet<PropertyDescriptor>> hashSets = groupedProperties.values();
            for (HashSet<PropertyDescriptor> descriptors : hashSets)
            {
                if (descriptors.contains(propertyDescriptor))
                {
                    alreadyAdded = true;
                }
            }

            if (!alreadyAdded)
            {
                propertyDescriptors.add(propertyDescriptor);
            }
        }
    }


    private void retainProperties(@NotNull HashMap<String, HashSet<PropertyDescriptor>> groupedProperties, @NotNull HashSet<PropertyDescriptor> beanProperties)
    {
        for (HashSet<PropertyDescriptor> propertyDescriptors : groupedProperties.values())
        {
            propertyDescriptors.retainAll(beanProperties);
        }
    }


    @NotNull
    public LinkedHashSet getValues(@NotNull PropertyDescriptor propertyDescriptor)
    {
        LinkedHashSet<Object> values = new LinkedHashSet<Object>();

        for (Object beanObject : objects)
        {
            try
            {
                Object value = propertyDescriptor.getReadMethod().invoke(beanObject, EMPTY_OBJECT_ARRAY);
                values.add(value);
            }
            catch (IllegalAccessException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
            catch (InvocationTargetException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
            catch (IllegalArgumentException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
        }

        return values;
    }


    public void setValues(@NotNull PropertyDescriptor propertyDescriptor, @Nullable Object newValue)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyEditorPanel.setValues newValue = " + newValue);

        reportDialog.getUndo().startTransaction(UndoConstants.PROPERTY_SETVALUES);
        for (Object o : objects)
        {
            try
            {
                propertyDescriptor.getWriteMethod().invoke(o, newValue);
            }
            catch (IllegalAccessException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
            catch (InvocationTargetException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
            catch (IllegalArgumentException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyEditorPanel.setValues ", e);
                //UncaughtExcpetionsModel.getInstance().addException(e);
            }
        }
        reportDialog.getUndo().endTransaction();
    }


    public void propertyChange(@NotNull PropertyChangeEvent evt)
    {
        propertyTableModel.fireTableDataChanged();

        if (!reportDialog.getUndo().isInProgress())
        {
            if (PropertyKeys.POSITION.equals(evt.getPropertyName()))
            {
                reportElementSelectionModel.refresh();
            }
            else if (PropertyKeys.MINIMUM_SIZE.equals(evt.getPropertyName()))
            {
                reportElementSelectionModel.refresh();
            }
            else if (PropertyKeys.REPORT_LAYOUT_MANAGER_TYPE.equals(evt.getPropertyName()))
            {
                reportElementSelectionModel.refresh();
            }
        }
    }
}
