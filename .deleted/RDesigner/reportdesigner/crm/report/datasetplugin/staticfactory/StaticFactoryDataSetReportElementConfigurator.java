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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.ReportDataFactoryException;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.RowNumberTableModel;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertyInfo;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.ExceptionUtils;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.client.util.TableUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 11:48:14
 */
public class StaticFactoryDataSetReportElementConfigurator
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(StaticFactoryDataSetReportElementConfigurator.class.getName());


    public static boolean showStaticFactoryDataSetReportElementConfigurator(@NotNull ReportDialog parent, @NotNull StaticFactoryDataSetReportElement staticFactoryDataSetReportElement)
    {
        StaticFactoryDataSetReportElementConfigurator configurator = new StaticFactoryDataSetReportElementConfigurator(parent, staticFactoryDataSetReportElement);
        return configurator.ok;
    }


    private boolean ok;
    @NotNull
    private CenterPanelDialog centerPanelDialog;
    @NotNull
    private JTextField queryNameTextField;
    @NotNull
    private JTextField classNameTextField;
    @NotNull
    private JTextField methodNameTextField;
    @NotNull
    private JTable table;
    @NotNull
    private ParametersTableModel parametersTableModel;
    @NotNull
    private ArrayList<ColumnInfo> columnInfos;


    private StaticFactoryDataSetReportElementConfigurator(@NotNull final ReportDialog parent, @NotNull final StaticFactoryDataSetReportElement staticFactoryDataSetReportElement)
    {
        ok = false;

        final JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        okButton.setEnabled(false);

        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));

        centerPanelDialog = CenterPanelDialog.createDialog(parent, TranslationManager.getInstance().getTranslation("R", "StaticFactoryDataSetReportElementConfigurator.Title"), true);

        queryNameTextField = new JTextField(staticFactoryDataSetReportElement.getQueryName());
        JLabel queryNameLabel = ComponentFactory.createLabel("R", "StaticFactoryDataSetReportElementConfigurator.QueryNameLabel", queryNameTextField);

        classNameTextField = new JTextField(staticFactoryDataSetReportElement.getClassName());
        JLabel classNameLabel = ComponentFactory.createLabel("R", "StaticFactoryDataSetReportElementConfigurator.ClassNameLabel", classNameTextField);

        methodNameTextField = new JTextField(staticFactoryDataSetReportElement.getMethodName());
        final JLabel methodNameLabel = ComponentFactory.createLabel("R", "StaticFactoryDataSetReportElementConfigurator.MethodNameLabel", methodNameTextField);

        columnInfos = staticFactoryDataSetReportElement.getColumnInfos();
        parametersTableModel = new ParametersTableModel(new ArrayList<PropertyInfo>(staticFactoryDataSetReportElement.getParameters()));
        table = new ParametersTable(parametersTableModel, true);
        JLabel parametersLabel = ComponentFactory.createLabel("R", "StaticFactoryDataSetReportElementConfigurator.ParametersLabel", table);

        JButton previewButton = ComponentFactory.createButton("R", "StaticFactoryDataSetReportElementConfigurator.PreviewButton");

        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, fill:default, 4dlu, fill:default:grow, 4dlu",
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "fill:10dlu:grow, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "4dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        final JPanel centerPanel = new JPanel(formLayout);

        centerPanel.add(queryNameLabel, cc.xy(2, 2));
        centerPanel.add(queryNameTextField, cc.xy(4, 2));
        centerPanel.add(classNameLabel, cc.xy(2, 4));
        centerPanel.add(classNameTextField, cc.xy(4, 4));
        centerPanel.add(methodNameLabel, cc.xy(2, 6));
        centerPanel.add(methodNameTextField, cc.xy(4, 6));

        centerPanel.add(parametersLabel, cc.xy(2, 8, "left, top"));
        centerPanel.add(new JScrollPane(table), cc.xy(4, 8));

        centerPanel.add(previewButton, cc.xy(4, 10, "right, center"));

        final Color origForegroundColor = classNameTextField.getForeground();
        classNameTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(@NotNull DocumentEvent e)
            {
                checkClass();
            }


            public void removeUpdate(@NotNull DocumentEvent e)
            {
                checkClass();
            }


            public void changedUpdate(@NotNull DocumentEvent e)
            {
                checkClass();
            }


            private void checkClass()
            {
                okButton.setEnabled(false);

                try
                {
                    Class.forName(classNameTextField.getText());
                    classNameTextField.setForeground(origForegroundColor);
                }
                catch (ClassNotFoundException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StaticFactoryDataSetReportElementConfigurator.checkClass ", e);
                    classNameTextField.setForeground(Color.RED);
                }
            }
        });

        methodNameTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(@NotNull DocumentEvent e)
            {
                checkMethod();
            }


            public void removeUpdate(@NotNull DocumentEvent e)
            {
                checkMethod();
            }


            public void changedUpdate(@NotNull DocumentEvent e)
            {
                checkMethod();
            }


            private void checkMethod()
            {
                okButton.setEnabled(false);

                try
                {
                    Class clazz = Class.forName(classNameTextField.getText());
                    Method[] methods = clazz.getMethods();
                    Method foundMethod = null;
                    for (Method method : methods)
                    {
                        if (TableModel.class.isAssignableFrom(method.getReturnType()) && method.getName().equals(methodNameTextField.getText()))
                        {
                            foundMethod = method;
                            break;
                        }
                    }
                    if (foundMethod != null)
                    {
                        methodNameTextField.setForeground(origForegroundColor);

                        fillParameters(foundMethod);
                    }
                    else
                    {
                        methodNameTextField.setForeground(Color.RED);
                    }
                }
                catch (ClassNotFoundException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StaticFactoryDataSetReportElementConfigurator.checkMethod ", e);
                    methodNameTextField.setForeground(Color.RED);
                }
            }
        });

        previewButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(parent,
                                                                                          TranslationManager.getInstance().getTranslation("R", "JDBCConnectionConfigurationPanel.ProgressDialog.Title"),
                                                                                          "");
                Thread t = new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            TableModel reportDataTableModel = StaticFactoryDataSetReportElement.fetchPreviewDataTableModel(classNameTextField.getText(), methodNameTextField.getText(), getParameters());
                            columnInfos = new ArrayList<ColumnInfo>();
                            for (int i = 0; i < reportDataTableModel.getColumnCount(); i++)
                            {
                                columnInfos.add(new ColumnInfo("", reportDataTableModel.getColumnName(i), reportDataTableModel.getColumnClass(i)));
                            }

                            ExceptionUtils.disposeDialogInEDT(progressDialog);

                            showPreviewDialog(centerPanel, reportDataTableModel);
                            okButton.setEnabled(true);
                        }
                        catch (ReportDataFactoryException e1)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StaticFactoryDataSetReportElementConfigurator.actionPerformed ", e1);
                            ExceptionUtils.disposeDialogInEDT(progressDialog);
                            JOptionPane.showMessageDialog(centerPanel, TranslationManager.getInstance().getTranslation("R", "StaticFactoryDataSetReportElementConfigurator.PreviewErrorMessage"), TranslationManager.getInstance().getTranslation("R", "Error.Title"), JOptionPane.ERROR_MESSAGE);
                            okButton.setEnabled(false);
                        }

                    }
                });

                t.setDaemon(true);
                t.setPriority(Thread.NORM_PRIORITY - 1);
                t.start();

                if (t.isAlive())
                {
                    progressDialog.setVisible(true);
                }
            }
        });

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                ok = false;
                centerPanelDialog.dispose();
            }
        });

        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                parent.getUndo().startTransaction(UndoConstants.CONNECTION_SETTINGS);
                staticFactoryDataSetReportElement.setColumnInfos(new ArrayList<ColumnInfo>(columnInfos));
                staticFactoryDataSetReportElement.setQueryName(classNameTextField.getText());
                staticFactoryDataSetReportElement.setClassName(classNameTextField.getText());
                staticFactoryDataSetReportElement.setMethodName(methodNameTextField.getText());
                staticFactoryDataSetReportElement.setParameters(new TreeSet<PropertyInfo>(parametersTableModel.getProperties()));

                parent.getUndo().endTransaction();

                parent.getWorkspaceSettings().storeDialogBounds(centerPanelDialog, "StaticFactoryDataSetReportElementConfigurator");
                ok = true;
                centerPanelDialog.dispose();
            }
        });

        if (!parent.getWorkspaceSettings().restoreDialogBounds(centerPanelDialog, "StaticFactoryDataSetReportElementConfigurator"))
        {
            centerPanelDialog.pack();
            GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 400, 400);
        }
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);

        centerPanelDialog.setVisible(true);
    }


    @NotNull
    private Collection<PropertyInfo> getParameters()
    {
        ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>(parametersTableModel.getProperties());
        return propertyInfos;
    }


    private void fillParameters(@NotNull Method method)
    {
        Class[] types = method.getParameterTypes();
        if (types.length == parametersTableModel.getProperties().size())
        {
            for (int i = 0; i < types.length; i++)
            {
                Class type = types[i];
                PropertyInfo info = parametersTableModel.getProperty(i);
                if (info != null)
                {
                    Class type2 = info.getClazz();
                    //noinspection ObjectEquality
                    if (type != type2)
                    {
                        refillModel(types);
                        break;
                    }
                }
            }
        }
        else
        {
            refillModel(types);
        }
    }


    private void refillModel(@NotNull Class[] types)
    {
        ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
        for (int i = 0; i < types.length; i++)
        {
            Class type = types[i];
            propertyInfos.add(new PropertyInfo("staticfactory.param" + (i + 1), type, null));//NON-NLS
        }
        parametersTableModel = new ParametersTableModel(propertyInfos);
        table.setModel(parametersTableModel);
    }


    private void showPreviewDialog(@NotNull Component parent, @NotNull TableModel reportDataTableModel)
    {
        final WorkspaceSettings workspaceSettings = WorkspaceSettings.getInstance();
        final CenterPanelDialog centerPanelDialog = CenterPanelDialog.createDialog(parent, TranslationManager.getInstance().getTranslation("R", "WizardPageSQLQuery.PreviewDialog.Title"), true);

        final JPanel centerPanel = new JPanel(new BorderLayout());
        JTable table = new JTable(reportDataTableModel);
        JTable rowHeaderTable = new JTable(new RowNumberTableModel(reportDataTableModel.getRowCount()));
        rowHeaderTable.setEnabled(false);
        rowHeaderTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer()
        {
            @NotNull
            public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                JLabel tableCellRendererComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                tableCellRendererComponent.setHorizontalAlignment(JLabel.TRAILING);
                tableCellRendererComponent.setBackground(centerPanel.getBackground());
                return tableCellRendererComponent;
            }
        });
        rowHeaderTable.setGridColor(centerPanel.getBackground().darker());
        rowHeaderTable.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, centerPanel.getBackground().darker()));

        JViewport viewport = new JViewport();
        viewport.setPreferredSize(new Dimension(40, 100));
        viewport.setView(rowHeaderTable);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableUtils.resizeAllColumns(table, 200);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeader(viewport);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanelDialog.setCenterPanel(centerPanel);
        JButton closeButton = ComponentFactory.createButton("R", "WizardPageSQLQuery.Close");
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                workspaceSettings.storeDialogBounds(centerPanelDialog, "WizardPageSQLQuery.PreviewDialog");
                centerPanelDialog.dispose();
            }
        });
        centerPanelDialog.setButtons(closeButton, closeButton, CenterPanelDialog.ButtonAlignment.CENTER, closeButton);

        if (!workspaceSettings.restoreDialogBounds(centerPanelDialog, "WizardPageSQLQuery.PreviewDialog"))
        {
            centerPanelDialog.pack();
        }

        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);
    }
}
