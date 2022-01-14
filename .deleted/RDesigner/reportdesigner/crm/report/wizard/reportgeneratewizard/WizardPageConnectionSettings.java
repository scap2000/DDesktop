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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCConnectionConfigurationPanel;
import org.pentaho.reportdesigner.crm.report.wizard.AbstractWizardPage;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:48:17
 */
public class WizardPageConnectionSettings extends AbstractWizardPage
{
    @NotNull
    private JPanel centerPanel;

    @NotNull
    private JDBCConnectionConfigurationPanel jdbcConnectionConfigurationPanel;

    private boolean connectionTestSuccessful;


    public WizardPageConnectionSettings()
    {
        connectionTestSuccessful = false;
    }


    @NotNull
    public JComponent getCenterPanel()
    {
        if (centerPanel == null)
        {
            jdbcConnectionConfigurationPanel = new JDBCConnectionConfigurationPanel(getWizardDialog(), getWizardDialog().getReportDialog().getWorkspaceSettings());
            jdbcConnectionConfigurationPanel.addPropertyChangeListener2(JDBCConnectionConfigurationPanel.CONNECTION_TEST, new PropertyChangeListener()
            {
                public void propertyChange(@NotNull PropertyChangeEvent evt)
                {
                    connectionTestSuccessful = ((Boolean) evt.getNewValue()).booleanValue();
                    fireWizardPageStateChanged();
                }
            });

            centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(jdbcConnectionConfigurationPanel, BorderLayout.CENTER);
        }
        return centerPanel;
    }


    public boolean canNext()
    {
        return connectionTestSuccessful;
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
        return connectionTestSuccessful;
    }


    @NotNull
    public String getTitle()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageConnectionSettings.Title");
    }


    @Nullable
    public ImageIcon getIcon()
    {
        return IconLoader.getInstance().getWizardPageConnectionSettingsIcon();
    }


    @NotNull
    public String getDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageConnectionSettings.Description");
    }


    @NotNull
    public WizardData[] getWizardDatas()
    {
        jdbcConnectionConfigurationPanel.storeSettings();

        @Nullable
        WizardData groupData = getWizardDialog().getWizardDatas().get(WizardData.COLUMN_GROUPS);
        if (groupData != null)
        {
            //noinspection unchecked
            ArrayList<ColumnInfo> columnGroups = (ArrayList<ColumnInfo>) groupData.getValue();
            if (columnGroups != null)
            {
                columnGroups.clear();
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
                columnGroups.keySet().clear();
            }
        }

        return new WizardData[]{
                new WizardData(WizardData.JARS, jdbcConnectionConfigurationPanel.getJars()),
                new WizardData(WizardData.DRIVER_CLASS, jdbcConnectionConfigurationPanel.getDriverClass()),
                new WizardData(WizardData.CONNECTION_STRING, jdbcConnectionConfigurationPanel.getConnectionString()),
                new WizardData(WizardData.USERNAME, jdbcConnectionConfigurationPanel.getUsername()),
                new WizardData(WizardData.PASSWORD, jdbcConnectionConfigurationPanel.getPassword()),
        };

    }


    public void dispose()
    {
    }
}
