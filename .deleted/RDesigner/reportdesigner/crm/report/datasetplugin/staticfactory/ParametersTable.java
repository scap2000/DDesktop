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
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorCheckBox;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldAreaWithEllipsis;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisDate;
import org.pentaho.reportdesigner.crm.report.properties.editors.CommonCellEditor;
import org.pentaho.reportdesigner.crm.report.properties.editors.StringChooser;
import org.pentaho.reportdesigner.crm.report.properties.renderers.BooleanTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.ClassTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.DateTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.DoubleTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.IntegerTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.StringTableCellRenderer;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 16.02.2006
 * Time: 16:16:11
 */
public class ParametersTable extends JTable
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ParametersTable.class.getName());

    @NotNull
    private ParametersTableModel tableModel;
    private boolean editable;

    @NotNull
    private BooleanTableCellRenderer booleanTableCellRenderer;
    @NotNull
    private ClassTableCellRenderer classTableCellRenderer;
    @NotNull
    private StringTableCellRenderer stringTableCellRenderer;
    @NotNull
    private IntegerTableCellRenderer integerTableCellRenderer;
    @NotNull
    private DoubleTableCellRenderer doubleTableCellRenderer;
    @NotNull
    private DateTableCellRenderer dateTableCellRenderer;

    @NotNull
    private CommonCellEditor booleanCellEditor;
    @NotNull
    private CommonCellEditor onelineStringCellEditor;
    @NotNull
    private CommonCellEditor stringCellEditor;
    @NotNull
    private CommonCellEditor integerCellEditor;
    @NotNull
    private CommonCellEditor doubleCellEditor;
    @NotNull
    private CommonCellEditor dateCellEditor;
    @NotNull
    private CommonCellEditor classCellEditor;


    public ParametersTable(@NotNull ParametersTableModel tableModel, boolean editable)
    {
        super(tableModel);

        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);//NON-NLS

        this.tableModel = tableModel;
        this.editable = editable;

        booleanTableCellRenderer = new BooleanTableCellRenderer();
        stringTableCellRenderer = new StringTableCellRenderer();
        classTableCellRenderer = new ClassTableCellRenderer();
        integerTableCellRenderer = new IntegerTableCellRenderer();
        doubleTableCellRenderer = new DoubleTableCellRenderer();
        dateTableCellRenderer = new DateTableCellRenderer();

        booleanCellEditor = new CommonCellEditor(new CellEditorCheckBox());
        onelineStringCellEditor = new CommonCellEditor(new JTextField(), CommonCellEditor.TextFieldType.TEXT);
        onelineStringCellEditor.setClickCountToStart(1);

        final CellEditorJTextFieldAreaWithEllipsis textFieldWithEllipsis = new CellEditorJTextFieldAreaWithEllipsis();
        stringCellEditor = new CommonCellEditor(textFieldWithEllipsis);
        textFieldWithEllipsis.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                String value = textFieldWithEllipsis.getValue();
                String val = StringChooser.showStringArrayChooser(ParametersTable.this, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Text.Title"), value);
                if (val != null)
                {
                    textFieldWithEllipsis.setValue(val, true);
                }
            }
        });

        JTextField integerTextField = new JTextField();
        UndoHelper.installUndoSupport(integerTextField);
        TextComponentHelper.installDefaultPopupMenu(integerTextField);
        integerCellEditor = new CommonCellEditor(integerTextField, CommonCellEditor.TextFieldType.INTEGER);
        integerCellEditor.setClickCountToStart(1);

        JTextField doubleTextField = new JTextField();
        UndoHelper.installUndoSupport(doubleTextField);
        TextComponentHelper.installDefaultPopupMenu(doubleTextField);
        doubleCellEditor = new CommonCellEditor(doubleTextField, CommonCellEditor.TextFieldType.DOUBLE);
        doubleCellEditor.setClickCountToStart(1);

        final CellEditorJTextFieldWithEllipsisDate textFieldWithEllipsisDate = new CellEditorJTextFieldWithEllipsisDate();
        dateCellEditor = new CommonCellEditor(textFieldWithEllipsisDate);
        textFieldWithEllipsisDate.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                //MARKED perhaps add a full fletched date editor
                textFieldWithEllipsisDate.setValue(new Date(), true);
            }
        });

        JComboBox classComboBox = new JComboBox(new Class[]{String.class, Boolean.class, Integer.class, Double.class, Date.class});
        classComboBox.setRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null)
                {
                    label.setText(((Class) value).getSimpleName());
                }
                else
                {
                    if (ParametersTable.LOG.isLoggable(Level.FINE)) ParametersTable.LOG.log(Level.FINE, "PropertiesTable.getListCellRendererComponent label = " + label);
                }
                return label;
            }
        });
        classCellEditor = new CommonCellEditor(classComboBox);
    }


    public void setModel(@NotNull TableModel dataModel)
    {
        tableModel = (ParametersTableModel) dataModel;
        super.setModel(dataModel);
    }


    @NotNull
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        if (getTableHeader().getColumnModel().getColumn(column).getModelIndex() == 0)
        {
            return classTableCellRenderer;
        }
        else if (getTableHeader().getColumnModel().getColumn(column).getModelIndex() == 1)
        {
            PropertyInfo info = tableModel.getProperty(row);
            if (info != null)
            {
                Class clazz = info.getClazz();
                if (Boolean.class.isAssignableFrom(clazz))
                {
                    return booleanTableCellRenderer;
                }
                else if (Boolean.TYPE.isAssignableFrom(clazz))
                {
                    return booleanTableCellRenderer;
                }
                else if (String.class.isAssignableFrom(clazz))
                {
                    return stringTableCellRenderer;
                }
                else if (Integer.class.isAssignableFrom(clazz))
                {
                    return integerTableCellRenderer;
                }
                else if (Integer.TYPE.isAssignableFrom(clazz))
                {
                    return integerTableCellRenderer;
                }
                else if (Double.class.isAssignableFrom(clazz))
                {
                    return doubleTableCellRenderer;
                }
                else if (Double.TYPE.isAssignableFrom(clazz))
                {
                    return doubleTableCellRenderer;
                }
                else if (Date.class.isAssignableFrom(clazz))
                {
                    return dateTableCellRenderer;
                }
            }
        }

        return super.getCellRenderer(row, column);
    }


    public void editingStopped(@NotNull ChangeEvent e)
    {
        int row = editingRow;
        super.editingStopped(e);
        requestFocusInWindow();
        getSelectionModel().setSelectionInterval(row, row);
        revalidate();
        repaint();
    }


    public boolean getSurrendersFocusOnKeystroke()
    {
        return true;
    }


    @Nullable
    public TableCellEditor getCellEditor(int row, int column)
    {
        if (getTableHeader().getColumnModel().getColumn(column).getModelIndex() == 0)
        {
            return classCellEditor;
        }
        else if (getTableHeader().getColumnModel().getColumn(column).getModelIndex() == 1)
        {
            PropertyInfo propertyInfo = tableModel.getProperty(row);
            if (propertyInfo != null)
            {
                Class aClass = propertyInfo.getClazz();
                if (Boolean.class.isAssignableFrom(aClass))
                {
                    return booleanCellEditor;
                }
                else if (Boolean.TYPE.isAssignableFrom(aClass))
                {
                    return booleanCellEditor;
                }
                else if (String.class.isAssignableFrom(aClass))
                {
                    return stringCellEditor;
                }
                else if (Integer.TYPE.isAssignableFrom(aClass))
                {
                    return integerCellEditor;
                }
                else if (Integer.class.isAssignableFrom(aClass))
                {
                    return integerCellEditor;
                }
                else if (Double.TYPE.isAssignableFrom(aClass))
                {
                    return doubleCellEditor;
                }
                else if (Double.class.isAssignableFrom(aClass))
                {
                    return doubleCellEditor;
                }
                else if (Date.class.isAssignableFrom(aClass))
                {
                    return dateCellEditor;
                }
            }
        }

        return null;
    }


    public boolean isCellEditable(int row, int column)
    {
        return editable && column == 1;
    }
}
