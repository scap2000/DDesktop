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
package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.JDBCTableModel;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.JNDISource;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 01.09.2006
 * Time: 08:55:46
 */
public class JDBCTest
{
    public static void main(@NotNull String[] args) throws Exception
    {
        Class.forName("org.hsqldb.jdbcDriver");

        JDBCTableModel tableModel = new JDBCTableModel(new JNDISource("SampleData", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/sampledata", "pentaho_user", "password"), "select * from customers", 1000);//NON-NLS

        JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 400, 400);
        frame.setVisible(true);
    }
}
