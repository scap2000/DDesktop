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
package org.pentaho.reportdesigner.crm.report.datasetplugin.sampledb;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCDataSetReportElement;

/**
 * User: Martin
 * Date: 14.03.2006
 * Time: 08:14:29
 */
public class SampleDataSetReportElement extends JDBCDataSetReportElement
{
    public SampleDataSetReportElement()
    {
    }


    public boolean showConfigurationComponent(@NotNull ReportDialog parent, boolean firsttime)
    {
        if (firsttime)
        {
            @NonNls
            String sqlQuery = "SELECT\n" +
                              "CUSTOMERS.FIRST_NAME, CUSTOMERS.LAST_NAME,\n" +
                              "PRODUCTS.PRODUCT_NAME, PRODUCTS.PRODUCT_DESCRIPTION, PRODUCTS.PRICE\n" +
                              "FROM CUSTOMERS\n" +
                              "JOIN ORDERS ON CUSTOMERS.CUSTOMER_ID=ORDERS.CUSTOMER_ID\n" +
                              "JOIN ORDER_ITEMS ON ORDER_ITEMS.ORDER_ID=ORDERS.ORDER_ID\n" +
                              "JOIN PRODUCTS ON ORDER_ITEMS.PRODUCT_ID=PRODUCTS.PRODUCT_ID\n" +
                              "ORDER BY\n" +
                              "CUSTOMERS.FIRST_NAME, CUSTOMERS.LAST_NAME, PRODUCTS.PRODUCT_NAME";
            setSqlQuery(sqlQuery);
        }
        return super.showConfigurationComponent(parent, false);
    }
}
