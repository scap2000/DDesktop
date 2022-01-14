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
package org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.wizard.AbstractWizardPage;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:48:17
 */
public class WizardPageVisibleFields extends AbstractWizardPage
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(WizardPageVisibleFields.class.getName());

    @NotNull
    private JPanel centerPanel;

    @NotNull
    private JTable table;

    @NotNull
    private JButton upButton;
    @NotNull
    private JButton downButton;

    @NotNull
    private ColumnInfoTableModel columnInfoTableModel;


    public WizardPageVisibleFields()
    {
        upButton = ComponentFactory.createButton("R", "WizardPageVisibleFields.ButtonUp");
        downButton = ComponentFactory.createButton("R", "WizardPageVisibleFields.ButtonDown");

        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, fill:10dlu:grow, 4dlu, default, 4dlu",
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "fill:10dlu:grow, " +
                                               "4dlu");

        centerPanel = new JPanel(formLayout);
        @NonNls
        CellConstraints cc = new CellConstraints();

        ArrayList<FieldInfo> fieldInfos = new ArrayList<FieldInfo>();
        columnInfoTableModel = new ColumnInfoTableModel(fieldInfos);
        table = new JTable(columnInfoTableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);

        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(20, 20));
        centerPanel.add(scrollPane, cc.xywh(2, 2, 1, 3, "fill, fill"));

        centerPanel.add(upButton, cc.xy(4, 2));
        centerPanel.add(downButton, cc.xy(4, 4, "center, top"));

        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer()
        {
            @NotNull
            public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try
                {
                    String s = (String) value;
                    try
                    {
                        label.setText(Class.forName(s).getSimpleName());
                    }
                    catch (ClassNotFoundException e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "WizardPageVisibleFields.getTableCellRendererComponent ", e);
                        label.setText(s);
                    }
                }
                catch (Throwable e)
                {
                    UncaughtExcpetionsModel.getInstance().addException(e);
                }
                return label;
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(@NotNull ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting())
                {
                    return;
                }

                updateButtonState();
            }
        });

        upButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                int index = table.getSelectionModel().getMinSelectionIndex() - 1;
                int lastIndex = table.getSelectionModel().getMaxSelectionIndex();

                FieldInfo fieldInfo = WizardPageVisibleFields.this.columnInfoTableModel.fieldInfos.get(index);
                WizardPageVisibleFields.this.columnInfoTableModel.removeFieldInfo(index);
                WizardPageVisibleFields.this.columnInfoTableModel.insertFieldInfo(lastIndex, fieldInfo);

                updateButtonState();
            }
        });

        downButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                int index = table.getSelectionModel().getMaxSelectionIndex() + 1;
                int first = table.getSelectionModel().getMinSelectionIndex();

                FieldInfo fieldInfo = WizardPageVisibleFields.this.columnInfoTableModel.fieldInfos.get(index);
                WizardPageVisibleFields.this.columnInfoTableModel.removeFieldInfo(index);
                WizardPageVisibleFields.this.columnInfoTableModel.insertFieldInfo(first, fieldInfo);

                table.getSelectionModel().setSelectionInterval(first + 1, index);
            }
        });
    }


    private void updateButtonState()
    {
        if (table.getSelectionModel().getMinSelectionIndex() <= 0)
        {
            upButton.setEnabled(false);
        }
        else
        {
            upButton.setEnabled(true);
        }
        if (table.getSelectionModel().getMaxSelectionIndex() == -1 || table.getSelectionModel().getMaxSelectionIndex() >= this.columnInfoTableModel.getRowCount() - 1)
        {
            downButton.setEnabled(false);
        }
        else
        {
            downButton.setEnabled(true);
        }
    }


    @NotNull
    public JComponent getCenterPanel()
    {
        @Nullable
        WizardData wizardData = getWizardDialog().getWizardDatas().get(WizardData.AVAILABLE_COLUMN_INFOS);

        ArrayList<ColumnInfo> availableColumnInfos = null;
        if (wizardData != null)
        {
            //noinspection unchecked
            availableColumnInfos = (ArrayList<ColumnInfo>) wizardData.getValue();
        }
        if (availableColumnInfos == null)
        {
            availableColumnInfos = new ArrayList<ColumnInfo>();
        }

        ArrayList<ColumnInfo> tempColumnInfos = new ArrayList<ColumnInfo>(availableColumnInfos);

        //remove all columns in tablemodel, but not in available
        for (int i = columnInfoTableModel.fieldInfos.size() - 1; i >= 0; i--)
        {
            FieldInfo fieldInfo = columnInfoTableModel.fieldInfos.get(i);
            if (!tempColumnInfos.contains(fieldInfo.columnInfo))
            {
                columnInfoTableModel.removeFieldInfo(i);
            }
            else
            {
                tempColumnInfos.remove(fieldInfo.columnInfo);
            }
        }

        //add all not already in table model
        for (ColumnInfo columnInfo : tempColumnInfos)
        {
            columnInfoTableModel.addFieldInfo(new FieldInfo(columnInfo, true));
        }

        return centerPanel;
    }


    @NotNull
    public String getTitle()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageVisibleFields.Title");
    }


    @Nullable
    public ImageIcon getIcon()
    {
        return IconLoader.getInstance().getWizardPageGroupIcon();
    }


    @NotNull
    public String getDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageVisibleFields.Description");
    }


    public void dispose()
    {
    }


    public boolean canNext()
    {
        return true;
    }


    public boolean canPrevious()
    {
        return true;
    }


    public boolean canCancel()
    {
        return true;
    }


    public boolean canFinish()
    {
        return true;
    }


    @NotNull
    public WizardData[] getWizardDatas()
    {
        return new WizardData[]{
                new WizardData(WizardData.VISIBLE_COLUMN_INFOS, columnInfoTableModel.getVisibleColumnInfos())
        };
    }


    private static class FieldInfo
    {
        @NotNull
        public ColumnInfo columnInfo;
        public boolean visible;


        private FieldInfo(@NotNull ColumnInfo columnInfo, boolean visible)
        {
            this.columnInfo = columnInfo;
            this.visible = visible;
        }
    }


    private static class ColumnInfoTableModel extends AbstractTableModel
    {
        @NotNull
        private ArrayList<FieldInfo> fieldInfos;


        private ColumnInfoTableModel(@NotNull ArrayList<FieldInfo> fieldInfos)
        {
            //noinspection ConstantConditions
            if (fieldInfos == null)
            {
                throw new IllegalArgumentException("fieldInfos must not be null");
            }
            this.fieldInfos = fieldInfos;
        }


        @Nullable
        public String getColumnName(int column)
        {
            if (column == 0)
            {
                return TranslationManager.getInstance().getTranslation("R", "ColumnInfoTable.Visible");
            }
            else if (column == 1)
            {
                return TranslationManager.getInstance().getTranslation("R", "ColumnInfoTable.Name");
            }
            else if (column == 2)
            {
                return TranslationManager.getInstance().getTranslation("R", "ColumnInfoTable.Type");
            }

            return null;
        }


        @NotNull
        public ArrayList<ColumnInfo> getVisibleColumnInfos()
        {
            ArrayList<ColumnInfo> visible = new ArrayList<ColumnInfo>();
            for (FieldInfo fieldInfo : fieldInfos)
            {
                if (fieldInfo.visible)
                {
                    visible.add(fieldInfo.columnInfo);
                }
            }
            return visible;
        }


        public int getRowCount()
        {
            return fieldInfos.size();
        }


        public int getColumnCount()
        {
            return 3;
        }


        public void addFieldInfo(@NotNull FieldInfo fieldInfo)
        {
            fieldInfos.add(fieldInfo);
            int index = fieldInfos.size() - 1;
            fireTableRowsInserted(index, index);
        }


        public void insertFieldInfo(int index, @NotNull FieldInfo fieldInfo)
        {
            fieldInfos.add(index, fieldInfo);
            fireTableRowsInserted(index, index);
        }


        public void removeFieldInfo(int index)
        {
            fieldInfos.remove(index);
            fireTableRowsDeleted(index, index);
        }


        @Nullable
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            if (columnIndex == 0)
            {
                return Boolean.valueOf(fieldInfos.get(rowIndex).visible);
            }
            else if (columnIndex == 1)
            {
                return fieldInfos.get(rowIndex).columnInfo.getColumnName();
            }
            else if (columnIndex == 2)
            {
                return fieldInfos.get(rowIndex).columnInfo.getColumnClass().getSimpleName();
            }
            return null;
        }


        public void setValueAt(@NotNull Object aValue, int rowIndex, int columnIndex)
        {
            if (columnIndex == 0)
            {
                fieldInfos.get(rowIndex).visible = !fieldInfos.get(rowIndex).visible;
            }
        }


        @NotNull
        public Class<?> getColumnClass(int columnIndex)
        {
            if (columnIndex == 0)
            {
                return Boolean.class;
            }
            else
            {
                return String.class;
            }
        }


        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return columnIndex == 0;
        }
    }
}
