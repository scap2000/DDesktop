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
package org.pentaho.reportdesigner.crm.report.datasetplugin.composer;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.components.ProgressListener;
import org.pentaho.reportdesigner.crm.report.datasetplugin.sampledb.SampleDB;
import org.pentaho.reportdesigner.lib.common.graph.Graph;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 08:06:26
 */
@SuppressWarnings({"ALL"})
public class AnalyzerTester
{
    public static void main(String[] args) throws Exception
    {
        SampleDB.initSampleDB();

        Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:sample", "sa", "");

        Graph graph = JDBCAnalyzer.buildGraph("", "PUBLIC", c, new ProgressListener()
        {
            public void taskStarted(@NotNull String task)
            {
                System.out.println("task = " + task);//NON-NLS
            }
        });
        System.out.println("graph = " + graph);

        //DatabaseMetaData metaData = c.getMetaData();
        //
        //String catalogTerm = metaData.getCatalogTerm();
        //System.out.println("catalogTerm = " + catalogTerm);
        //String catalogSeparator = metaData.getCatalogSeparator();
        //System.out.println("catalogSeparator = " + catalogSeparator);
        //
        //System.out.println("Schemas");
        //ResultSet schemas = metaData.getSchemas();
        //ResultSetPrinter.printResultSet(schemas);
        //
        //System.out.println("Tables");
        //ResultSet tables = metaData.getTables("", "PUBLIC", "%", null);
        //ResultSetPrinter.printResultSet(tables);
        //
        ////System.out.println("PrimaryKeys");
        ////ResultSet primaryKeys = metaData.getPrimaryKeys("", "PUBLIC", "ORDERS");
        ////ResultSetPrinter.printResultSet(primaryKeys);
        //
        ////System.out.println("ExportedKeys");
        ////ResultSet exportedKeys = metaData.getExportedKeys("", "PUBLIC", "ORDERS");
        ////ResultSetPrinter.printResultSet(exportedKeys);
        //
        //System.out.println("ImportedKeys");
        //ResultSet importedKeys = metaData.getImportedKeys("", "PUBLIC", "ORDERS");
        //ResultSetPrinter.printResultSet(importedKeys);

        /*
        SELECT
        CUSTOMERS.FIRST_NAME, CUSTOMERS.LAST_NAME,
        PRODUCTS.PRODUCT_NAME, PRODUCTS.PRODUCT_DESCRIPTION, PRODUCTS.PRICE
        FROM CUSTOMERS
        JOIN ORDERS ON CUSTOMERS.CUSTOMER_ID=ORDERS.CUSTOMER_ID
        JOIN ORDER_ITEMS ON ORDER_ITEMS.ORDER_ID=ORDERS.ORDER_ID
        JOIN PRODUCTS ON ORDER_ITEMS.PRODUCT_ID=PRODUCTS.PRODUCT_ID
        ORDER BY
        CUSTOMERS.FIRST_NAME, PRODUCTS.PRODUCT_NAME
        */
    }
}
