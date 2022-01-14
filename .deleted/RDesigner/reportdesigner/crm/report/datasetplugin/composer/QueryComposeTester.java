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
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.ResultSetPrinter;
import org.pentaho.reportdesigner.crm.report.datasetplugin.sampledb.SampleDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 09:30:31
 */
@SuppressWarnings({"ALL"})
public class QueryComposeTester
{
    public static void main(String[] args) throws Exception
    {
        SampleDB.initSampleDB();

        Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:sample", "sa", "");

        ResultSet catalogs = c.getMetaData().getCatalogs();
        ResultSetPrinter.printResultSet(catalogs);

        ResultSet schemas = c.getMetaData().getSchemas();
        ResultSetPrinter.printResultSet(schemas);

        JDBCGraph graph = JDBCAnalyzer.buildGraph("", "PUBLIC", c, new ProgressListener()
        {
            public void taskStarted(@NotNull String task)
            {
                System.out.println("task = " + task);//NON-NLS
            }
        });
        System.out.println("graph = " + graph);

        //e.g build query with
        //CUSTOMERS.FIRST_NAME, CUSTOMERS.LAST_NAME, PRODUCTS.PRODUCT_NAME, PRODUCTS.PRODUCT_DESCRIPTION, PRODUCTS.PRICE
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

        JDBCColumnInfo firstName = graph.getVertexForTable("CUSTOMERS").getTableInfo().getColumnInfos().get("FIRST_NAME");
        JDBCColumnInfo lastName = graph.getVertexForTable("CUSTOMERS").getTableInfo().getColumnInfos().get("LAST_NAME");
        JDBCColumnInfo productName = graph.getVertexForTable("PRODUCTS").getTableInfo().getColumnInfos().get("PRODUCT_NAME");
        JDBCColumnInfo productDescription = graph.getVertexForTable("PRODUCTS").getTableInfo().getColumnInfos().get("PRODUCT_DESCRIPTION");
        JDBCColumnInfo price = graph.getVertexForTable("PRODUCTS").getTableInfo().getColumnInfos().get("PRICE");

        ArrayList<QueryComposerColumn> queryComposerColumns = new ArrayList<QueryComposerColumn>();
        queryComposerColumns.add(new QueryComposerColumn(firstName, true, OrderDirection.ASCENDING, null, null, null));
        queryComposerColumns.add(new QueryComposerColumn(lastName, true, OrderDirection.ASCENDING, null, null, null));
        queryComposerColumns.add(new QueryComposerColumn(productName, true, null, null, null, null));
        queryComposerColumns.add(new QueryComposerColumn(productDescription, true, null, null, null, null));
        queryComposerColumns.add(new QueryComposerColumn(price, true, null, null, null, null));

        String query = QueryComposer.getQuery(graph, queryComposerColumns);
        System.out.println("query = \n" + query);

        /*
        SELECT
            aufwand.gueltig_von,
            aufwand.gueltig_bis,
            projekt.titel,
            task.beschreibung
        FROM
            aufwand, projekt, task
        WHERE
            aufwand.gueltig_von >= '02.01.2006 00:00' AND
            aufwand.gueltig_bis < '03.01.2006 00:00' AND
            task.benutzer_oid = 9991 AND
            task.task_oid = aufwand.task_oid AND
            projekt.projekt_oid=task.projekt_oid
        ORDER BY
            projekt.titel, task.beschreibung, aufwand.gueltig_von
        */
    }
}
