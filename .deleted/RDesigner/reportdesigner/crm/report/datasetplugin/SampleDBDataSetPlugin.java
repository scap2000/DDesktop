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
public class SampleDBDataSetPlugin implements DataSetPlugin
{
    @NotNull
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    @NotNull
    private WizardPageSQLQuery wizardPageSQLQuery;
    @NotNull
    private WizardPageFieldExpressions wizardPageFieldExpressions;


    public SampleDBDataSetPlugin()
    {
    }


    @NotNull
    public String getID()
    {
        return "SampleDBDataSetPlugin";
    }


    @NotNull
    public String getLocalizedName()
    {
        return TranslationManager.getInstance().getTranslation("R", "SampleDBDataSetPlugin.Name");
    }


    @NotNull
    public String getLocalizedDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "SampleDBDataSetPlugin.Description");
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
        wizardPageSQLQuery = new WizardPageSQLQuery();
        WizardPageVisibleFields wizardPageVisibleFields = new WizardPageVisibleFields();
        WizardPageGroups wizardPageGroups = new WizardPageGroups();
        wizardPageFieldExpressions = new WizardPageFieldExpressions();

        wizardPageSQLQuery.setNext(wizardPageVisibleFields);

        wizardPageVisibleFields.setPrevious(wizardPageSQLQuery);
        wizardPageVisibleFields.setNext(wizardPageGroups);

        wizardPageGroups.setPrevious(wizardPageVisibleFields);
        wizardPageGroups.setNext(wizardPageFieldExpressions);

        wizardPageFieldExpressions.setPrevious(wizardPageGroups);
    }


    @NotNull
    public WizardData[] getInitialWizardDatas()
    {
        //noinspection HardCodedStringLiteral,HardcodedLineSeparator
        return new WizardData[]{new WizardData(WizardData.JARS, EMPTY_STRING_ARRAY),
                                new WizardData(WizardData.DRIVER_CLASS, "org.hsqldb.jdbcDriver"),//NON-NLS
                                new WizardData(WizardData.CONNECTION_STRING, "jdbc:hsqldb:mem:sample"),//NON-NLS
                                new WizardData(WizardData.SQL_QUERY, "SELECT\n" +
                                                                     "CUSTOMERS.FIRST_NAME, CUSTOMERS.LAST_NAME,\n" +
                                                                     "PRODUCTS.PRODUCT_NAME, PRODUCTS.PRODUCT_DESCRIPTION, PRODUCTS.PRICE\n" +
                                                                     "FROM CUSTOMERS\n" +
                                                                     "JOIN ORDERS ON CUSTOMERS.CUSTOMER_ID=ORDERS.CUSTOMER_ID\n" +
                                                                     "JOIN ORDER_ITEMS ON ORDER_ITEMS.ORDER_ID=ORDERS.ORDER_ID\n" +
                                                                     "JOIN PRODUCTS ON ORDER_ITEMS.PRODUCT_ID=PRODUCTS.PRODUCT_ID\n" +
                                                                     "ORDER BY\n" +
                                                                     "CUSTOMERS.FIRST_NAME, CUSTOMERS.LAST_NAME, PRODUCTS.PRODUCT_NAME"),
                                new WizardData(WizardData.USERNAME, "sa"),//NON-NLS
                                new WizardData(WizardData.PASSWORD, "")};
    }


    @NotNull
    public AbstractWizardPage getFirstWizardPage()
    {
        return wizardPageSQLQuery;
    }


    @NotNull
    public AbstractWizardPage getLastWizardPage()
    {
        return wizardPageFieldExpressions;
    }


    @NotNull
    public TableModelDataSetReportElement createDataSet(@NotNull HashMap<String, WizardData> wizardDatas)
    {
        JDBCDataSetReportElement jdbcDataSetReportElement = ReportElementInfoFactory.getInstance().getSampleDataSetReportElementInfo().createReportElement();

        @Nullable
        WizardData columnInfosWizardData = wizardDatas.get(WizardData.COLUMN_INFOS);
        if (columnInfosWizardData != null && columnInfosWizardData.getValue() != null)
        {
            //noinspection unchecked
            jdbcDataSetReportElement.setColumnInfos(new ArrayList<ColumnInfo>((ArrayList<ColumnInfo>) columnInfosWizardData.getValue()));
        }
        jdbcDataSetReportElement.setJars(EMPTY_STRING_ARRAY);
        jdbcDataSetReportElement.setDriverClass("org.hsqldb.jdbcDriver");
        jdbcDataSetReportElement.setMaxPreviewRows(100);
        jdbcDataSetReportElement.setUserName("sa");//NON-NLS
        jdbcDataSetReportElement.setPassword("");

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
        jdbcDataSetReportElement.setConnectionString("jdbc:hsqldb:mem:sample");//NON-NLS

        return jdbcDataSetReportElement;
    }


    public boolean canRead(@Nullable String classname)
    {
        return "org.pentaho.reportdesigner.crm.report.datasetplugin.sampledb.SampleDataSetReportElement".equals(classname);
    }


    @NotNull
    public ReportElement createEmptyInstance(@NotNull String className)
    {
        return ReportElementInfoFactory.getInstance().getSampleDataSetReportElementInfo().createReportElement();
    }
}
