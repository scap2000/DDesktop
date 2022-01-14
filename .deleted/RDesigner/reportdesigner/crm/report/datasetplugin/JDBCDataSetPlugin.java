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
package org.pentaho.reportdesigner.crm.report.datasetplugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.TableModelDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.wizard.AbstractWizardPage;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard.WizardPageConnectionSettings;
import org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard.WizardPageFieldExpressions;
import org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard.WizardPageGroups;
import org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard.WizardPageSQLQuery;
import org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard.WizardPageVisibleFields;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 27.02.2006
 * Time: 10:00:41
 */
public class JDBCDataSetPlugin implements DataSetPlugin
{
    @NotNull
    private WizardPageConnectionSettings wizardPageConnectionSettings;
    @NotNull
    private WizardPageFieldExpressions wizardPageFieldExpressions;


    public JDBCDataSetPlugin()
    {
    }


    @NotNull
    public String getID()
    {
        return "JDBCDataSetPlugin";
    }


    @NotNull
    public String getLocalizedName()
    {
        return TranslationManager.getInstance().getTranslation("R", "JDBCDataSetPlugin.Name");
    }


    @NotNull
    public String getLocalizedDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "JDBCDataSetPlugin.Description");
    }


    @NotNull
    public ImageIcon getSmallIcon()
    {
        return IconLoader.getInstance().getDataSetsIcon();
    }


    public boolean isWizardable()
    {
        return true;
    }


    public void initWizardPages()
    {
        wizardPageConnectionSettings = new WizardPageConnectionSettings();
        WizardPageSQLQuery wizardPageSQLQuery = new WizardPageSQLQuery();
        WizardPageVisibleFields wizardPageVisibleFields = new WizardPageVisibleFields();
        WizardPageGroups wizardPageGroups = new WizardPageGroups();
        wizardPageFieldExpressions = new WizardPageFieldExpressions();

        wizardPageConnectionSettings.setNext(wizardPageSQLQuery);

        wizardPageSQLQuery.setPrevious(wizardPageConnectionSettings);
        wizardPageSQLQuery.setNext(wizardPageVisibleFields);

        wizardPageVisibleFields.setPrevious(wizardPageSQLQuery);
        wizardPageVisibleFields.setNext(wizardPageGroups);

        wizardPageGroups.setPrevious(wizardPageVisibleFields);
        wizardPageGroups.setNext(wizardPageFieldExpressions);

        wizardPageFieldExpressions.setPrevious(wizardPageGroups);
    }


    @NotNull
    public AbstractWizardPage getFirstWizardPage()
    {
        return wizardPageConnectionSettings;
    }


    @NotNull
    public AbstractWizardPage getLastWizardPage()
    {
        return wizardPageFieldExpressions;
    }


    @NotNull
    public WizardData[] getInitialWizardDatas()
    {
        return WizardData.EMPTY_ARRAY;
    }


    @NotNull
    public TableModelDataSetReportElement createDataSet(@NotNull HashMap<String, WizardData> wizardDatas)
    {
        JDBCDataSetReportElement jdbcDataSetReportElement = ReportElementInfoFactory.getInstance().getJDBCDataSetReportElementInfo().createReportElement();

        @Nullable
        WizardData columnInfoWizardData = wizardDatas.get(WizardData.COLUMN_INFOS);
        if (columnInfoWizardData != null && columnInfoWizardData.getValue() != null)
        {
            //noinspection unchecked
            jdbcDataSetReportElement.setColumnInfos(new ArrayList<ColumnInfo>((ArrayList<ColumnInfo>) columnInfoWizardData.getValue()));
        }

        @Nullable
        WizardData driverClassWizardData = wizardDatas.get(WizardData.DRIVER_CLASS);
        if (driverClassWizardData != null)
        {
            Object driverClass = driverClassWizardData.getValue();
            if (driverClass != null)
            {
                jdbcDataSetReportElement.setDriverClass(driverClass.toString());
            }
        }

        @Nullable
        WizardData maxPreviewRowsWizardData = wizardDatas.get(WizardData.MAX_PREVIEW_ROWS);
        if (maxPreviewRowsWizardData != null)
        {
            Object maxPreviewRows = maxPreviewRowsWizardData.getValue();
            if (maxPreviewRows != null)
            {
                jdbcDataSetReportElement.setMaxPreviewRows(((Integer) maxPreviewRows).intValue());
            }
        }

        @Nullable
        WizardData jarWizardData = wizardDatas.get(WizardData.JARS);
        if (jarWizardData != null)
        {
            Object jars = jarWizardData.getValue();
            if (jars != null)
            {
                jdbcDataSetReportElement.setJars((String[]) jars);
            }
        }

        @Nullable
        WizardData usernameWizardData = wizardDatas.get(WizardData.USERNAME);
        if (usernameWizardData != null)
        {
            Object username = usernameWizardData.getValue();
            if (username != null)
            {
                jdbcDataSetReportElement.setUserName(username.toString());
            }
        }

        @Nullable
        WizardData passwordWizardData = wizardDatas.get(WizardData.PASSWORD);
        if (passwordWizardData != null)
        {
            Object password = passwordWizardData.getValue();
            if (password != null)
            {
                jdbcDataSetReportElement.setPassword(password.toString());
            }
        }

        @Nullable
        WizardData sqlQueryWizardData = wizardDatas.get(WizardData.SQL_QUERY);
        if (sqlQueryWizardData != null)
        {
            Object sqlQuery = sqlQueryWizardData.getValue();
            if (sqlQuery != null)
            {
                jdbcDataSetReportElement.setSqlQuery(sqlQuery.toString());
            }
        }

        @Nullable
        WizardData connectionStringWizardData = wizardDatas.get(WizardData.CONNECTION_STRING);
        if (connectionStringWizardData != null)
        {
            Object connectionString = connectionStringWizardData.getValue();
            if (connectionString != null)
            {
                jdbcDataSetReportElement.setConnectionString(connectionString.toString());
            }
        }

        return jdbcDataSetReportElement;
    }


    public boolean canRead(@Nullable String classname)
    {
        return "org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCDataSetReportElement".equals(classname);
    }


    //TODO check if this implementation is OK
    @NotNull
    public ReportElement createEmptyInstance(@NotNull String className)
    {
        return ReportElementInfoFactory.getInstance().getJDBCDataSetReportElementInfo().createReportElement();
    }
}
