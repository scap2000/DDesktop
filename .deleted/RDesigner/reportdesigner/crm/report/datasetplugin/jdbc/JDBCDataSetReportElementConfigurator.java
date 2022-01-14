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
package org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.connection.ConnectionFactory;
import org.pentaho.reportdesigner.crm.report.connection.MetaDataService;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 11:48:14
 */
public class JDBCDataSetReportElementConfigurator
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JDBCDataSetReportElementConfigurator.class.getName());


    public static boolean showJDBCDataSetReportElementConfigurator(@NotNull ReportDialog parent, @NotNull JDBCDataSetReportElement jdbcDataSetReportElement, boolean firsttime)
    {
        JDBCDataSetReportElementConfigurator configurator = new JDBCDataSetReportElementConfigurator(parent, jdbcDataSetReportElement, firsttime);
        return configurator.ok;
    }


    private boolean ok;
    @NotNull
    private CenterPanelDialog centerPanelDialog;
    @NotNull
    private JDBCConnectionConfigurationPanel connectionConfigurationPanel;


    private JDBCDataSetReportElementConfigurator(@NotNull final ReportDialog parent, @NotNull final JDBCDataSetReportElement jdbcDataSetReportElement, boolean firsttime)
    {
        ok = false;

        centerPanelDialog = CenterPanelDialog.createDialog(parent, TranslationManager.getInstance().getTranslation("R", "JDBCDataSetReportElementConfigurator.Title"), true);

        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, fill:default, 4dlu, pref, 4dlu, fill:default:grow, 4dlu",
                                               "4dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "fill:10dlu:grow, " +
                                               "4dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        JPanel centerPanel = new JPanel(formLayout);

        JLabel connectionSettingsLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "JDBCDataSetReportElementConfigurator.ConnectionSettings.Label"));
        centerPanel.add(connectionSettingsLabel, cc.xy(2, 2));
        final JButton connectionSettingsButton = ComponentFactory.createButton("R", "JDBCDataSetReportElementConfigurator.ConnectionSettings.Button");
        connectionSettingsButton.setMargin(new Insets(1, 1, 1, 1));
        centerPanel.add(connectionSettingsButton, cc.xy(4, 2));


        final SQLQueryConfigurationPanel sqlQueryConfigurationPanel = new SQLQueryConfigurationPanel(parent, parent.getWorkspaceSettings());
        centerPanel.add(sqlQueryConfigurationPanel, cc.xyw(2, 4, 5));

        final JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        connectionConfigurationPanel = new JDBCConnectionConfigurationPanel(parent, parent.getWorkspaceSettings());

        connectionConfigurationPanel.setJars(jdbcDataSetReportElement.getJars());
        connectionConfigurationPanel.setDriverClass(jdbcDataSetReportElement.getDriverClass());
        connectionConfigurationPanel.setConnectionString(jdbcDataSetReportElement.getConnectionString());
        connectionConfigurationPanel.setUsername(jdbcDataSetReportElement.getUserName());
        connectionConfigurationPanel.setPassword(jdbcDataSetReportElement.getPassword());
        sqlQueryConfigurationPanel.setSQLQuery(jdbcDataSetReportElement.getSqlQuery());
        sqlQueryConfigurationPanel.setMaxPreviewRows(jdbcDataSetReportElement.getMaxPreviewRows());

        connectionSettingsButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                final CenterPanelDialog connectionDialog = CenterPanelDialog.createDialog(centerPanelDialog,
                                                                                          TranslationManager.getInstance().getTranslation("R", "JDBCDataSetReportElementConfigurator.ConnectionSettings.Title"),
                                                                                          true);
                connectionDialog.setCenterPanel(connectionConfigurationPanel);
                final JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
                JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));

                okButton.setEnabled(false);

                final PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
                {
                    public void propertyChange(@NotNull PropertyChangeEvent evt)
                    {
                        okButton.setEnabled(Boolean.TRUE.equals(evt.getNewValue()));
                    }
                };
                connectionConfigurationPanel.addPropertyChangeListener2(JDBCConnectionConfigurationPanel.CONNECTION_TEST, propertyChangeListener);

                okButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(parent,
                                                                                                  TranslationManager.getInstance().getTranslation("R", "JDBCDataSetReportElementConfigurator.ProgressDialog.Title"),
                                                                                                  "");

                        Thread t = new Thread()
                        {
                            public void run()
                            {
                                try
                                {
                                    final MetaDataService metaDataService = ConnectionFactory.getJDBCService(connectionConfigurationPanel.getJars(),
                                                                                                             connectionConfigurationPanel.getDriverClass(),
                                                                                                             connectionConfigurationPanel.getConnectionString(),
                                                                                                             connectionConfigurationPanel.getUsername(),
                                                                                                             connectionConfigurationPanel.getPassword());

                                    EventQueue.invokeLater(new Runnable()
                                    {
                                        public void run()
                                        {
                                            sqlQueryConfigurationPanel.setMetaDataService(metaDataService);
                                            connectionConfigurationPanel.removePropertyChangeListener2(JDBCConnectionConfigurationPanel.CONNECTION_TEST, propertyChangeListener);
                                            parent.getWorkspaceSettings().storeDialogBounds(connectionDialog, "JDBCDataSetReportElementConfigurator.ConnectionSettings");
                                            connectionDialog.dispose();
                                            progressDialog.dispose();
                                        }
                                    });
                                }
                                catch (Throwable e1)
                                {
                                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCDataSetReportElementConfigurator.run ", e1);
                                    EventQueue.invokeLater(new Runnable()
                                    {
                                        public void run()
                                        {
                                            sqlQueryConfigurationPanel.setMetaDataService(null);
                                            connectionConfigurationPanel.removePropertyChangeListener2(JDBCConnectionConfigurationPanel.CONNECTION_TEST, propertyChangeListener);
                                            parent.getWorkspaceSettings().storeDialogBounds(connectionDialog, "JDBCDataSetReportElementConfigurator.ConnectionSettings");
                                            connectionDialog.dispose();
                                            progressDialog.dispose();
                                        }
                                    });
                                }
                            }
                        };
                        t.setDaemon(true);
                        t.start();

                        if (t.isAlive())
                        {
                            progressDialog.setVisible(true);
                        }
                        else
                        {
                            progressDialog.dispose();
                        }
                    }
                });

                cancelButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        connectionConfigurationPanel.removePropertyChangeListener2(JDBCConnectionConfigurationPanel.CONNECTION_TEST, propertyChangeListener);
                        parent.getWorkspaceSettings().storeDialogBounds(connectionDialog, "JDBCDataSetReportElementConfigurator.ConnectionSettings");
                        connectionDialog.dispose();
                    }
                });

                connectionDialog.setButtons(okButton, cancelButton, okButton, cancelButton);
                if (!parent.getWorkspaceSettings().restoreDialogBounds(connectionDialog, "JDBCDataSetReportElementConfigurator.ConnectionSettings"))
                {
                    connectionDialog.pack();
                    GUIUtils.ensureMinimumDialogSize(connectionDialog, 500, 250);
                }

                WindowUtils.setLocationRelativeTo(connectionDialog, centerPanelDialog);
                connectionDialog.setVisible(true);
            }
        });

        sqlQueryConfigurationPanel.addPropertyChangeListener2(SQLQueryConfigurationPanel.OK, new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                okButton.setEnabled(Boolean.TRUE.equals(evt.getNewValue()));
            }
        });


        final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(parent, TranslationManager.getInstance().getTranslation("R", "JDBCDataSetReportElementConfigurator.ProgressDialog.Title"), "");
        Thread t = new Thread()
        {
            public void run()
            {
                try
                {
                    final MetaDataService jdbcService = ConnectionFactory.getJDBCService(connectionConfigurationPanel.getJars(),
                                                                                         connectionConfigurationPanel.getDriverClass(),
                                                                                         connectionConfigurationPanel.getConnectionString(),
                                                                                         connectionConfigurationPanel.getUsername(),
                                                                                         connectionConfigurationPanel.getPassword());
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            sqlQueryConfigurationPanel.setMetaDataService(jdbcService);
                            progressDialog.dispose();
                        }
                    });
                }
                catch (Throwable e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCDataSetReportElementConfigurator.run ", e);
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            sqlQueryConfigurationPanel.setMetaDataService(null);
                            progressDialog.dispose();
                        }
                    });
                }
            }
        };
        if (!firsttime)
        {
            t.setDaemon(true);
            t.start();
            if (t.isAlive())
            {
                progressDialog.setVisible(true);
            }
            else
            {
                progressDialog.dispose();
            }
        }

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                sqlQueryConfigurationPanel.dispose();
                ok = false;
                centerPanelDialog.dispose();
            }
        });

        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                parent.getUndo().startTransaction(UndoConstants.CONNECTION_SETTINGS);
                jdbcDataSetReportElement.setJars(connectionConfigurationPanel.getJars());
                jdbcDataSetReportElement.setDriverClass(connectionConfigurationPanel.getDriverClass());
                jdbcDataSetReportElement.setConnectionString(connectionConfigurationPanel.getConnectionString());
                jdbcDataSetReportElement.setUserName(connectionConfigurationPanel.getUsername());
                jdbcDataSetReportElement.setPassword(connectionConfigurationPanel.getPassword());
                jdbcDataSetReportElement.setSqlQuery(sqlQueryConfigurationPanel.getSQLQuery());
                jdbcDataSetReportElement.setColumnInfos(new ArrayList<ColumnInfo>(sqlQueryConfigurationPanel.getAvailableColumnInfos()));
                jdbcDataSetReportElement.setMaxPreviewRows(sqlQueryConfigurationPanel.getMaxPreviewRows());
                parent.getUndo().endTransaction();

                parent.getWorkspaceSettings().storeDialogBounds(centerPanelDialog, "JDBCDataSetReportElementConfigurator");
                sqlQueryConfigurationPanel.dispose();
                ok = true;
                centerPanelDialog.dispose();
            }
        });

        if (!parent.getWorkspaceSettings().restoreDialogBounds(centerPanelDialog, "JDBCDataSetReportElementConfigurator"))
        {
            centerPanelDialog.pack();
            GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 500, 400);
        }
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);

        if (firsttime)
        {
            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    connectionSettingsButton.doClick();
                }
            });
        }

        centerPanelDialog.setVisible(true);
    }
}
