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
import org.jfree.report.function.ItemAvgFunction;
import org.jfree.report.function.ItemCountFunction;
import org.jfree.report.function.ItemMaxFunction;
import org.jfree.report.function.ItemMinFunction;
import org.jfree.report.function.ItemSumFunction;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.properties.editors.CommonCellEditor;
import org.pentaho.reportdesigner.crm.report.wizard.AbstractWizardPage;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:48:17
 */
public class WizardPageFieldExpressions extends AbstractWizardPage
{
    @NotNull
    private JPanel centerPanel;

    @NotNull
    private ColumnExpressionTableModel columnExpressionTableModel;


    public WizardPageFieldExpressions()
    {
        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, fill:10dlu:grow, 4dlu",
                                               "4dlu, " +
                                               "fill:10dlu:grow, " +
                                               "4dlu");
        centerPanel = new JPanel(formLayout);
        @NonNls
        CellConstraints cc = new CellConstraints();

        columnExpressionTableModel = new ColumnExpressionTableModel(new ArrayList<FieldInfo>());
        ExpressionTable table = new ExpressionTable(columnExpressionTableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(500);

        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer()
        {
            @NotNull
            public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try
                {
                    Class expressionInfo = (Class) value;
                    if (expressionInfo != null)
                    {
                        label.setText(expressionInfo.getName());
                    }
                    else
                    {
                        label.setText(" ");
                    }
                }
                catch (Throwable e)
                {
                    UncaughtExcpetionsModel.getInstance().addException(e);
                }
                return label;
            }
        });

        centerPanel.add(new JScrollPane(table), cc.xy(2, 2));
    }


    private static class ExpressionTable extends JTable
    {
        @NotNull
        private CommonCellEditor commonCellEditor;
        @NotNull
        private JComboBox comboBox;
        @NotNull
        private ColumnExpressionTableModel columnExpressionTableModel;


        private ExpressionTable(@NotNull ColumnExpressionTableModel columnExpressionTableModel)
        {
            super(columnExpressionTableModel);

            this.columnExpressionTableModel = columnExpressionTableModel;
            comboBox = new JComboBox();
            comboBox.setRenderer(new DefaultListCellRenderer()
            {
                @NotNull
                public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
                {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    Class expressionInfo = (Class) value;
                    if (expressionInfo != null)
                    {
                        label.setText(expressionInfo.getName());
                    }
                    else
                    {
                        label.setText(" ");
                    }
                    return label;
                }
            });
            commonCellEditor = new CommonCellEditor(comboBox);
        }


        @Nullable
        public TableCellEditor getCellEditor(int row, int column)
        {
            if (getColumnModel().getColumn(column).getModelIndex() == 2)
            {
                ColumnInfo columnInfo = columnExpressionTableModel.fieldInfos.get(row).columnInfo;
                ArrayList<Class> expressioninfosForType = getExpressioninfosForType(columnInfo);
                ArrayList<Class> v = new ArrayList<Class>();
                v.add(null);
                v.addAll(expressioninfosForType);
                comboBox.setModel(new DefaultComboBoxModel(v.toArray(new Class[v.size()])));
                comboBox.setSelectedItem(columnExpressionTableModel.getValueAt(row, column));
                return commonCellEditor;
            }

            return null;
        }


        @NotNull
        private ArrayList<Class> getExpressioninfosForType(@NotNull ColumnInfo columnInfo)
        {
            Class clazz = columnInfo.getColumnClass();
            if (Number.class.isAssignableFrom(clazz))
            {
                ArrayList<Class> expressionInfos = new ArrayList<Class>();
                expressionInfos.add(ItemAvgFunction.class);
                expressionInfos.add(ItemMaxFunction.class);
                expressionInfos.add(ItemMinFunction.class);
                expressionInfos.add(ItemSumFunction.class);
                //expressionInfos.add(new ExpressionInfo("org.pentaho.reportdesigner.crm.report.model.functions.wrapper.ItemAvgFunction", "ItemAvgFunction", new HashMap<String, String>()));
                //expressionInfos.add(new ExpressionInfo("org.pentaho.reportdesigner.crm.report.model.functions.wrapper.ItemMaxFunction", "ItemMaxFunction", new HashMap<String, String>()));
                //expressionInfos.add(new ExpressionInfo("org.pentaho.reportdesigner.crm.report.model.functions.wrapper.ItemMinFunction", "ItemMinFunction", new HashMap<String, String>()));
                //expressionInfos.add(new ExpressionInfo("org.pentaho.reportdesigner.crm.report.model.functions.wrapper.ItemSumFunction", "ItemSumFunction", new HashMap<String, String>()));
                return expressionInfos;
            }
            else
            {
                ArrayList<Class> expressionInfos = new ArrayList<Class>();
                expressionInfos.add(ItemCountFunction.class);
                //expressionInfos.add(new ExpressionInfo("org.pentaho.reportdesigner.crm.report.model.functions.wrapper.ItemCountFunction", "ItemCountFunction", new HashMap<String, String>()));
                return expressionInfos;
            }
        }
    }


    @NotNull
    public JComponent getCenterPanel()
    {
        @Nullable
        WizardData visibleColumnInfos = getWizardDialog().getWizardDatas().get(WizardData.VISIBLE_COLUMN_INFOS);
        @Nullable
        WizardData groupData = getWizardDialog().getWizardDatas().get(WizardData.COLUMN_GROUPS);

        ArrayList<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
        if (visibleColumnInfos != null)
        {
            //noinspection unchecked
            columnInfos = (ArrayList<ColumnInfo>) visibleColumnInfos.getValue();
        }

        ArrayList<ColumnInfo> remainingColumnInfos = new ArrayList<ColumnInfo>(columnInfos);
        if (groupData != null)
        {
            //noinspection unchecked
            remainingColumnInfos.removeAll((ArrayList<ColumnInfo>) groupData.getValue());
        }

        for (int i = columnExpressionTableModel.fieldInfos.size() - 1; i >= 0; i--)
        {
            FieldInfo fieldInfo = columnExpressionTableModel.fieldInfos.get(i);
            if (!remainingColumnInfos.contains(fieldInfo.columnInfo))
            {
                columnExpressionTableModel.removeFieldInfo(i);
            }
            else
            {
                remainingColumnInfos.remove(fieldInfo.columnInfo);
            }
        }

        for (ColumnInfo columnInfo : remainingColumnInfos)
        {
            columnExpressionTableModel.addFieldInfo(new FieldInfo(columnInfo, null));
        }


        return centerPanel;
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
    public String getTitle()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageFieldExpressions.Title");
    }


    @NotNull
    public ImageIcon getIcon()
    {
        return IconLoader.getInstance().getWizardPageFieldExpressionsIcon();
    }


    @NotNull
    public String getDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageFieldExpressions.Description");
    }


    public void dispose()
    {
    }


    @NotNull
    public WizardData[] getWizardDatas()
    {
        HashMap<ColumnInfo, Class> infos = new HashMap<ColumnInfo, Class>();
        for (FieldInfo fieldInfo : columnExpressionTableModel.fieldInfos)
        {
            if (fieldInfo.expressionInfo != null)
            {
                infos.put(fieldInfo.columnInfo, fieldInfo.expressionInfo);
            }
        }

        return new WizardData[]{
                new WizardData(WizardData.COLUMN_EXPRESSIONS, infos)
        };
    }


    private static class FieldInfo
    {
        @NotNull
        public ColumnInfo columnInfo;
        @Nullable
        public Class expressionInfo;


        private FieldInfo(@NotNull ColumnInfo columnInfo, @Nullable Class expressionInfo)
        {
            this.columnInfo = columnInfo;
            this.expressionInfo = expressionInfo;
        }
    }

    private static class ColumnExpressionTableModel extends AbstractTableModel
    {
        @NotNull
        private ArrayList<FieldInfo> fieldInfos;


        private ColumnExpressionTableModel(@NotNull ArrayList<FieldInfo> fieldInfos)
        {
            this.fieldInfos = fieldInfos;
        }


        public void addFieldInfo(@NotNull FieldInfo fieldInfo)
        {
            fieldInfos.add(fieldInfo);
            int index = fieldInfos.size() - 1;
            fireTableRowsInserted(index, index);
        }


        public void removeFieldInfo(int index)
        {
            fieldInfos.remove(index);
            fireTableRowsDeleted(index, index);
        }


        public int getRowCount()
        {
            return fieldInfos.size();
        }


        public int getColumnCount()
        {
            return 3;
        }


        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return columnIndex == 2;
        }


        public void setValueAt(@NotNull Object aValue, int rowIndex, int columnIndex)
        {
            if (columnIndex == 2)
            {
                fieldInfos.get(rowIndex).expressionInfo = (Class) aValue;
            }
        }


        @Nullable
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            if (columnIndex == 0)
            {
                return fieldInfos.get(rowIndex).columnInfo.getColumnName();
            }
            else if (columnIndex == 1)
            {
                return fieldInfos.get(rowIndex).columnInfo.getColumnClass().getSimpleName();
            }
            else if (columnIndex == 2)
            {
                return fieldInfos.get(rowIndex).expressionInfo;
            }
            return null;
        }


        @NotNull
        public String getColumnName(int column)
        {
            if (column == 0)
            {
                return TranslationManager.getInstance().getTranslation("R", "ColumnInfoTable.Name");
            }
            else if (column == 1)
            {
                return TranslationManager.getInstance().getTranslation("R", "ColumnInfoTable.Type");
            }
            else if (column == 2)
            {
                return TranslationManager.getInstance().getTranslation("R", "ColumnInfoTable.Expression");
            }

            return "ERROR";//NON-NLS
        }
    }

}
