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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.connection.ConnectionFactory;
import org.pentaho.reportdesigner.crm.report.connection.MetaDataService;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.SQLQueryConfigurationPanel;
import org.pentaho.reportdesigner.crm.report.wizard.AbstractWizardPage;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:48:17
 */
public class WizardPageSQLQuery extends AbstractWizardPage
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(WizardPageSQLQuery.class.getName());


    @NotNull
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    @NotNull
    private SQLQueryConfigurationPanel sqlQueryConfigurationPanel;


    public WizardPageSQLQuery()
    {
    }


    @NotNull
    public WizardData[] getWizardDatas()
    {
        @Nullable
        WizardData groupData = getWizardDialog().getWizardDatas().get(WizardData.COLUMN_GROUPS);
        if (groupData != null)
        {
            //noinspection unchecked
            ArrayList<ColumnInfo> columnGroups = (ArrayList<ColumnInfo>) groupData.getValue();
            if (columnGroups != null)
            {
                columnGroups.retainAll(sqlQueryConfigurationPanel.getAvailableColumnInfos());
            }
        }

        @Nullable
        WizardData columnExpressionsData = getWizardDialog().getWizardDatas().get(WizardData.COLUMN_EXPRESSIONS);
        if (columnExpressionsData != null)
        {
            //noinspection unchecked
            HashMap<ColumnInfo, Class> columnGroups = (HashMap<ColumnInfo, Class>) columnExpressionsData.getValue();
            if (columnGroups != null)
            {
                columnGroups.keySet().retainAll(sqlQueryConfigurationPanel.getAvailableColumnInfos());
            }
        }

        @Nullable
        WizardData visibleColumnWizardData = getWizardDialog().getWizardDatas().get(WizardData.VISIBLE_COLUMN_INFOS);
        if (visibleColumnWizardData == null)
        {
            visibleColumnWizardData = new WizardData(WizardData.VISIBLE_COLUMN_INFOS, new ArrayList<ColumnInfo>(sqlQueryConfigurationPanel.getAvailableColumnInfos()));
        }

        return new WizardData[]{
                new WizardData(WizardData.SQL_QUERY, sqlQueryConfigurationPanel.getSQLQuery()),
                new WizardData(WizardData.MAX_PREVIEW_ROWS, Integer.valueOf(sqlQueryConfigurationPanel.getMaxPreviewRows())),
                new WizardData(WizardData.AVAILABLE_COLUMN_INFOS, new ArrayList<ColumnInfo>(sqlQueryConfigurationPanel.getAvailableColumnInfos())),
                new WizardData(WizardData.COLUMN_INFOS, new ArrayList<ColumnInfo>(sqlQueryConfigurationPanel.getColumnInfos())),
                visibleColumnWizardData
        };
    }


    @NotNull
    public JComponent getCenterPanel()
    {
        HashMap<String, WizardData> wizardDatas = getWizardDialog().getWizardDatas();

        if (sqlQueryConfigurationPanel == null)
        {
            sqlQueryConfigurationPanel = new SQLQueryConfigurationPanel(getWizardDialog(), getWizardDialog().getReportDialog().getWorkspaceSettings());
            sqlQueryConfigurationPanel.addPropertyChangeListener2(SQLQueryConfigurationPanel.OK, new PropertyChangeListener()
            {
                public void propertyChange(@NotNull PropertyChangeEvent evt)
                {
                    fireWizardPageStateChanged();
                }
            });

            @Nullable
            WizardData wizardData = wizardDatas.get(WizardData.SQL_QUERY);
            if (wizardData != null)
            {
                Object sqlQuery = wizardData.getValue();
                if (sqlQuery != null)
                {
                    sqlQueryConfigurationPanel.setSQLQuery(sqlQuery.toString());
                }
            }
        }

        @Nullable
        final String[] jars = (String[]) wizardDatas.get(WizardData.JARS).getValue();
        final String driverClass = getString(wizardDatas, WizardData.DRIVER_CLASS);
        final String connectionString = getString(wizardDatas, WizardData.CONNECTION_STRING);
        final String username = getString(wizardDatas, WizardData.USERNAME);
        final String password = getString(wizardDatas, WizardData.PASSWORD);

        final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(getWizardDialog(), TranslationManager.getInstance().getTranslation("R", "WizardPageSQLQuery.ProgressDialog.Title"), "");
        Thread t = new Thread()
        {
            public void run()
            {
                try
                {
                    final MetaDataService jdbcService = ConnectionFactory.getJDBCService(jars == null ? EMPTY_STRING_ARRAY : jars, driverClass, connectionString, username, password);

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

        return sqlQueryConfigurationPanel;
    }


    @NotNull
    private static String getString(@NotNull HashMap<String, WizardData> wizardDatas, @NotNull String key)
    {
        @Nullable
        WizardData wizardData = wizardDatas.get(key);
        if (wizardData != null)
        {
            Object o = wizardData.getValue();
            if (o != null)
            {
                return o.toString();
            }
        }
        return "";
    }


    public boolean canNext()
    {
        return sqlQueryConfigurationPanel != null && sqlQueryConfigurationPanel.isOk();
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
        return sqlQueryConfigurationPanel != null && sqlQueryConfigurationPanel.isOk();
    }


    @NotNull
    public String getTitle()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageSQLQuery.Title");
    }


    @NotNull
    public ImageIcon getIcon()
    {
        return IconLoader.getInstance().getWizardPageSQLQueryIcon();
    }


    @NotNull
    public String getDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageSQLQuery.Description");
    }


    public void dispose()
    {
        if (sqlQueryConfigurationPanel != null)
        {
            sqlQueryConfigurationPanel.dispose();
        }
    }
}
