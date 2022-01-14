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
package org.pentaho.reportdesigner.crm.report.properties.editors;


import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UIConstants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.net.URL;
import java.util.EventObject;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CommonCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(CommonCellEditor.class.getName());

    @NotNull
    protected JComponent editorComponent;
    @NotNull
    protected EditorDelegate delegate;

    protected int clickCountToStart = 1;

    private static final boolean DRAG_FIX = false;

    public enum TextFieldType
    {
        @NotNull INTEGER,
        @NotNull LONG,
        @NotNull FLOAT,
        @NotNull DOUBLE,
        @NotNull TEXT,
        @NotNull URL
    }


    public CommonCellEditor(@NotNull final CellEditorJTextFieldWithEllipsis textFieldWithEllipsis)
    {
        editorComponent = textFieldWithEllipsis;
        this.clickCountToStart = 1;
        delegate = new EditorDelegate()
        {
            public void setValue(@Nullable Object value)
            {
                Border border = UIManager.getBorder(UIConstants.TABLE_FOCUS_CELL_HIGHLIGHT_BORDER);
                textFieldWithEllipsis.setBorder(border);
                //noinspection unchecked
                textFieldWithEllipsis.setValue(value, true);
            }


            @Nullable
            public Object getCellEditorValue()
            {
                return textFieldWithEllipsis.getValue();
            }
        };

        textFieldWithEllipsis.getEllipsisButton().addActionListener(delegate);
        textFieldWithEllipsis.getTextField().addActionListener(delegate);
    }


    public CommonCellEditor(@NotNull final CellEditorJLabelWithEllipsis labelWithEllipsis)
    {
        editorComponent = labelWithEllipsis;
        this.clickCountToStart = 1;
        delegate = new EditorDelegate()
        {
            public void setValue(@Nullable Object value)
            {
                Border border = UIManager.getBorder(UIConstants.TABLE_FOCUS_CELL_HIGHLIGHT_BORDER);
                labelWithEllipsis.setBorder(border);
                //noinspection unchecked
                labelWithEllipsis.setValue(value, true);
            }


            @Nullable
            public Object getCellEditorValue()
            {
                return labelWithEllipsis.getValue();
            }
        };

        labelWithEllipsis.getEllipsisButton().addActionListener(delegate);
    }


    public CommonCellEditor(@NotNull final JTextField textField, @NotNull final TextFieldType textFieldType)
    {
        editorComponent = textField;
        this.clickCountToStart = 2;
        delegate = new EditorDelegate()
        {
            @Nullable
            private Object origValue;


            public void setValue(@Nullable Object value)
            {
                Border border = UIManager.getBorder(UIConstants.TABLE_FOCUS_CELL_HIGHLIGHT_BORDER);
                textField.setBorder(border);
                textField.setText((value != null) ? value.toString() : "");
                origValue = value;
            }


            @Nullable
            public Object getCellEditorValue()
            {
                if (textFieldType == TextFieldType.INTEGER)
                {
                    if (textField.getText().length() == 0)
                    {
                        return null;
                    }

                    try
                    {
                        return Integer.valueOf(textField.getText());
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CommonCellEditor.getCellEditorValue ", e);
                        return origValue;
                    }
                }
                else if (textFieldType == TextFieldType.LONG)
                {
                    if (textField.getText().length() == 0)
                    {
                        return null;
                    }

                    try
                    {
                        return Long.valueOf(textField.getText());
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CommonCellEditor.getCellEditorValue ", e);
                        return origValue;
                    }
                }
                else if (textFieldType == TextFieldType.DOUBLE)
                {
                    if (textField.getText().length() == 0)
                    {
                        return null;
                    }

                    try
                    {
                        return Double.valueOf(textField.getText());
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CommonCellEditor.getCellEditorValue ", e);
                        return origValue;
                    }
                }
                else if (textFieldType == TextFieldType.FLOAT)
                {
                    if (textField.getText().length() == 0)
                    {
                        return null;
                    }

                    try
                    {
                        return Float.valueOf(textField.getText());
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CommonCellEditor.getCellEditorValue ", e);
                        return origValue;
                    }
                }
                else if (textFieldType == TextFieldType.URL)
                {
                    if (textField.getText().length() == 0)
                    {
                        return null;
                    }

                    try
                    {
                        return new URL(textField.getText());
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CommonCellEditor.getCellEditorValue ", e);
                        return origValue;
                    }
                }
                else
                {
                    return textField.getText();
                }
            }
        };
        textField.addActionListener(delegate);
    }


    @NotNull
    public JComponent getEditorComponent()
    {
        return editorComponent;
    }


    public CommonCellEditor(@NotNull final JCheckBox checkBox)
    {
        editorComponent = checkBox;
        delegate = new EditorDelegate()
        {
            public void setValue(@Nullable Object value)
            {
                boolean selected = false;
                if (value instanceof Boolean)
                {
                    selected = ((Boolean) value).booleanValue();
                }
                else if (value instanceof String)
                {
                    selected = Boolean.valueOf((String) value).booleanValue();
                }
                Border border = null;
                if (selected)
                {
                    border = UIManager.getBorder(UIConstants.TABLE_FOCUS_SELECTED_CELL_HIGHLIGHT_BORDER);
                }
                if (border == null)
                {
                    border = UIManager.getBorder(UIConstants.TABLE_FOCUS_CELL_HIGHLIGHT_BORDER);
                }
                checkBox.setBorder(border);
                if (selected)
                {
                    checkBox.setText(TranslationManager.getInstance().getTranslation("R", "Property.Boolean.True"));
                }
                else
                {
                    checkBox.setText(TranslationManager.getInstance().getTranslation("R", "Property.Boolean.False"));
                }
                checkBox.setSelected(selected);
            }


            @NotNull
            public Object getCellEditorValue()
            {
                return Boolean.valueOf(checkBox.isSelected());
            }
        };
        checkBox.addActionListener(delegate);

        if (DRAG_FIX)
        {
            checkBox.setRequestFocusEnabled(false);
        }
    }


    public CommonCellEditor(@NotNull final JComboBox comboBox)
    {
        editorComponent = comboBox;
        comboBox.putClientProperty(UIConstants.JCOMBO_BOX_IS_TABLE_CELL_EDITOR, Boolean.TRUE);
        delegate = new EditorDelegate()
        {
            public void setValue(@Nullable Object value)
            {
                comboBox.setSelectedItem(value);
            }


            @NotNull
            public Object getCellEditorValue()
            {
                return comboBox.getSelectedItem();
            }


            public boolean shouldSelectCell(@NotNull EventObject anEvent)
            {
                if (anEvent instanceof MouseEvent)
                {
                    MouseEvent e = (MouseEvent) anEvent;
                    return e.getID() != MouseEvent.MOUSE_DRAGGED;
                }
                return true;
            }


            public boolean stopCellEditing()
            {
                if (comboBox.isEditable())
                {
                    // Commit edited value.
                    comboBox.actionPerformed(new ActionEvent(
                            CommonCellEditor.this, 0, ""));
                }
                return super.stopCellEditing();
            }
        };
        comboBox.addActionListener(delegate);
    }


    @NotNull
    public Component getComponent()
    {
        return editorComponent;
    }


    public void setClickCountToStart(int count)
    {
        clickCountToStart = count;
    }


    public int getClickCountToStart()
    {
        return clickCountToStart;
    }


    @Nullable
    public Object getCellEditorValue()
    {
        return delegate.getCellEditorValue();
    }


    public boolean isCellEditable(@Nullable EventObject anEvent)
    {
        return delegate.isCellEditable(anEvent);
    }


    public boolean shouldSelectCell(@NotNull EventObject anEvent)
    {
        return delegate.shouldSelectCell(anEvent);
    }


    public boolean stopCellEditing()
    {
        return delegate.stopCellEditing();
    }


    public void cancelCellEditing()
    {
        delegate.cancelCellEditing();
    }


    @NotNull
    @SuppressWarnings({"unchecked"})
    public Component getTreeCellEditorComponent(@NotNull JTree tree, @Nullable Object value,
                                                boolean isSelected,
                                                boolean expanded,
                                                boolean leaf, int row)
    {
        /*String stringValue = */
        tree.convertValueToText(value, isSelected,
                                expanded, leaf, row, false);

        if (value instanceof HashSet)
        {
            HashSet<Object> hashSet = (HashSet<Object>) value;
            if (!hashSet.isEmpty())
            {
                delegate.setValue(hashSet.iterator().next());
            }
        }
        return editorComponent;
    }


    @NotNull
    public Component getTableCellEditorComponent(@NotNull JTable table, @Nullable Object value,
                                                 boolean isSelected,
                                                 int row, int column)
    {
        if (value instanceof HashSet)
        {
            HashSet hashSet = (HashSet) value;
            if (!hashSet.isEmpty())
            {
                delegate.setValue(hashSet.iterator().next());
            }
        }
        return editorComponent;
    }


    protected class EditorDelegate implements ActionListener, ItemListener, Serializable
    {
        private static final long serialVersionUID = 1L;

        @Nullable
        protected Object value;


        @Nullable
        public Object getCellEditorValue()
        {
            return value;
        }


        public void setValue(@Nullable Object value)
        {
            this.value = value;
        }


        public boolean isCellEditable(@Nullable EventObject anEvent)
        {
            if (anEvent instanceof MouseEvent)
            {
                return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
            }
            return true;
        }


        public boolean shouldSelectCell(@NotNull EventObject anEvent)
        {
            return true;
        }


        public boolean startCellEditing(@NotNull EventObject anEvent)
        {
            return true;
        }


        public boolean stopCellEditing()
        {
            fireEditingStopped();
            return true;
        }


        public void cancelCellEditing()
        {
            fireEditingCanceled();
        }


        public void actionPerformed(@NotNull ActionEvent e)
        {
            CommonCellEditor.this.stopCellEditing();
        }


        public void itemStateChanged(@NotNull ItemEvent e)
        {
            CommonCellEditor.this.stopCellEditing();
        }
    }

}
