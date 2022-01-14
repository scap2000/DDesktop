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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 11.01.2006
 * Time: 11:02:54
 */
public class StringArrayChooser
{
    public enum Type
    {
        @NotNull FIELD,
        @NotNull ELEMENT,
        @NotNull STRING
    }


    private StringArrayChooser()
    {
    }


    @Nullable
    public static String[] showStringArrayChooser(@NotNull final Type type, @Nullable final ReportElement reportElement, @NotNull JComponent parent, @NotNull String title, @NotNull final String labelPrefix, @Nullable String[] stringArray)
    {
        //noinspection ConstantConditions
        if (type == null)
        {
            throw new IllegalArgumentException("type must not be null");
        }
        //noinspection ConstantConditions
        if (parent == null)
        {
            throw new IllegalArgumentException("parent must not be null");
        }
        //noinspection ConstantConditions
        if (title == null)
        {
            throw new IllegalArgumentException("title must not be null");
        }
        //noinspection ConstantConditions
        if (labelPrefix == null)
        {
            throw new IllegalArgumentException("labelPrefix must not be null");
        }

        final CenterPanelDialog centerPanelDialog;
        Window windowAncestor = SwingUtilities.getWindowAncestor(parent);

        if (windowAncestor instanceof Dialog)
        {
            centerPanelDialog = new CenterPanelDialog((Dialog) windowAncestor, title, true);
        }
        else
        {
            centerPanelDialog = new CenterPanelDialog((Frame) windowAncestor, title, true);
        }

        @NonNls
        final FormLayout formLayout = new FormLayout("4dlu, pref, 4dlu, fill:default:grow, 4dlu, pref, 4dlu", "4dlu");
        final JPanel centerPanel = new JPanel(formLayout);

        @NonNls
        FormLayout fl = new FormLayout("4dlu, fill:pref:grow, 4dlu", "4dlu, fill:default, 4dlu, pref, 4dlu:grow");
        @NonNls
        final CellConstraints cc = new CellConstraints();
        final JPanel panel = new JPanel(fl);
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, cc.xy(2, 2));

        final ArrayList<Row> rows = new ArrayList<Row>();

        JButton addButton = new JButton(TranslationManager.getInstance().getTranslation("R", "StringArrayChooser.AddButton"));
        panel.add(addButton, cc.xy(2, 4, "right, center"));

        if (stringArray != null)
        {
            for (int i = 0; i < stringArray.length; i++)
            {
                String s = stringArray[i];
                JLabel label = new JLabel(labelPrefix + "[" + i + "]");
                final JTextField textField = new JTextField(s);
                UndoHelper.installUndoSupport(textField);
                TextComponentHelper.installDefaultPopupMenu(textField);

                JButton removeButton = new JButton("-");
                removeButton.setMargin(new Insets(1, 1, 1, 1));

                JButton chooseFieldButton = null;
                if (reportElement != null)
                {
                    chooseFieldButton = new JButton("...");
                    final JButton chooseFieldButton1 = chooseFieldButton;
                    chooseFieldButton.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(@NotNull ActionEvent e)
                        {
                            String newValue = null;
                            if (type == Type.FIELD)
                            {
                                newValue = FieldChooser.showFieldChooser(reportElement, chooseFieldButton1, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Field.Title"), textField.getText());
                            }
                            else if (type == Type.ELEMENT)
                            {
                                newValue = ElementNameChooser.showElementChooser(reportElement, chooseFieldButton1, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Field.Title"), textField.getText());
                            }

                            if (newValue != null)
                            {
                                textField.setText(newValue);
                            }
                        }
                    });
                }

                final Row row = new Row(i, label, textField, chooseFieldButton, removeButton);
                rows.add(row);
                removeButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        rows.remove(row);
                        rebuildPanel(rows, labelPrefix, centerPanel);

                        panel.revalidate();
                        panel.repaint();
                    }
                });
            }

            rebuildPanel(rows, labelPrefix, centerPanel);

            panel.revalidate();
            panel.repaint();
        }

        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                addRow(type, reportElement, labelPrefix, rows, centerPanel, panel);
            }
        });

        if (stringArray == null || stringArray.length == 0)
        {
            addRow(type, reportElement, labelPrefix, rows, centerPanel, panel);
        }

        final boolean[] action = new boolean[]{false};

        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                action[0] = true;
                centerPanelDialog.dispose();
            }
        });

        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                action[0] = false;
                centerPanelDialog.dispose();
            }
        });

        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        centerPanelDialog.setCenterPanel(panel);
        centerPanelDialog.pack();
        centerPanelDialog.setSize(300, 300);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return getValues(rows);
        }
        return stringArray;
    }


    private static void addRow(@NotNull final Type type, @Nullable final ReportElement reportElement, @NotNull final String labelPrefix, @NotNull final ArrayList<Row> rows, @NotNull final JPanel centerPanel, @NotNull final JPanel panel)
    {
        JLabel label = new JLabel(labelPrefix);
        final JTextField textField = new JTextField("");
        UndoHelper.installUndoSupport(textField);
        TextComponentHelper.installDefaultPopupMenu(textField);

        JButton removeButton = new JButton("-");
        removeButton.setMargin(new Insets(1, 1, 1, 1));

        JButton chooseFieldButton = null;
        if (reportElement != null)
        {
            chooseFieldButton = new JButton("...");
            final JButton chooseFieldButton1 = chooseFieldButton;
            chooseFieldButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    String newValue = null;
                    if (type == Type.FIELD)
                    {
                        newValue = FieldChooser.showFieldChooser(reportElement, chooseFieldButton1, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Field.Title"), textField.getText());
                    }
                    else if (type == Type.ELEMENT)
                    {
                        newValue = ElementNameChooser.showElementChooser(reportElement, chooseFieldButton1, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Field.Title"), textField.getText());
                    }

                    if (newValue != null)
                    {
                        textField.setText(newValue);
                    }
                }
            });
        }

        final Row row = new Row(rows.size(), label, textField, chooseFieldButton, removeButton);
        rows.add(row);

        removeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                rows.remove(row);
                rebuildPanel(rows, labelPrefix, centerPanel);

                panel.revalidate();
                panel.repaint();
            }
        });
        rebuildPanel(rows, labelPrefix, centerPanel);

        textField.requestFocusInWindow();

        panel.revalidate();
        panel.repaint();
    }


    @NotNull
    private static String[] getValues(@NotNull ArrayList<Row> rows)
    {
        String[] array = new String[rows.size()];
        for (int i = 0; i < rows.size(); i++)
        {
            Row row = rows.get(i);
            array[i] = row.getTextField().getText();
        }

        return array;
    }


    private static void rebuildPanel(@NotNull ArrayList<Row> rows, @NotNull String labelPrefix, @NotNull JPanel panel)
    {
        panel.removeAll();

        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, default, 4dlu, fill:10dlu:grow, 2dlu, default, 4dlu, default, 4dlu", "4dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        panel.setLayout(formLayout);

        int currentRow = 1;
        for (int i = 0; i < rows.size(); i++)
        {
            Row row = rows.get(i);

            formLayout.appendRow(new RowSpec("pref"));//NON-NLS
            currentRow++;
            row.setCurrentRow(i);
            row.getLabel().setText(labelPrefix + "[" + i + "]");
            panel.add(row.getLabel(), cc.xy(2, currentRow));
            panel.add(row.getTextField(), cc.xy(4, currentRow));
            if (row.getChooseFieldButton() != null)
            {
                panel.add(row.getChooseFieldButton(), cc.xy(6, currentRow));
            }
            panel.add(row.getRemoveButton(), cc.xy(8, currentRow));
            formLayout.appendRow(new RowSpec("4dlu"));//NON-NLS
            currentRow++;
        }

        panel.revalidate();
        panel.repaint();
    }


    private static class Row
    {
        private int currentRow;
        @NotNull
        private JLabel label;
        @NotNull
        private JTextField textField;
        @Nullable
        private JButton chooseFieldButton;
        @NotNull
        private JButton removeButton;


        private Row(int currentRow, @NotNull JLabel label, @NotNull JTextField textField, @Nullable JButton chooseFieldButton, @NotNull JButton removeButton)
        {
            this.currentRow = currentRow;
            this.label = label;
            this.textField = textField;
            this.chooseFieldButton = chooseFieldButton;
            this.removeButton = removeButton;
        }


        public int getCurrentRow()
        {
            return currentRow;
        }


        public void setCurrentRow(int currentRow)
        {
            this.currentRow = currentRow;
        }


        @NotNull
        public JLabel getLabel()
        {
            return label;
        }


        @NotNull
        public JTextField getTextField()
        {
            return textField;
        }


        @NotNull
        public JButton getRemoveButton()
        {
            return removeButton;
        }


        @Nullable
        public JButton getChooseFieldButton()
        {
            return chooseFieldButton;
        }
    }
}
