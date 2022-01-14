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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * User: Martin
 * Date: 27.06.2006
 * Time: 17:41:34
 */
@SuppressWarnings({"ALL"})
public class TestTableModelFactory
{
    private String param1;


    public TestTableModelFactory()
    {
    }


    public TestTableModelFactory(String param1)
    {
        System.out.println("TestTableModelFactory.TestTableModelFactory");
        System.out.println("param1 = " + param1);
        this.param1 = param1;
    }


    public TableModel createSubReportTable(String param1)
    {
        System.out.println("TestTableModelFactory.createSubReportTable");
        System.out.println("param1 = " + param1);
        return new DefaultTableModel(new Object[][]{{param1, "1.2"}, {param1, "2.2"}}, new Object[]{"c1", "c2"});
    }


    public TableModel createTable0()
    {
        return new DefaultTableModel(new Object[][]{{"1.1", "1.2"}, {"2.1", "2.2"}}, new Object[]{"c1", "c2"});
    }


    public TableModel createTable(@NotNull String param1, @NotNull String param2)
    {
        System.out.println("TestTableModelFactory.createTable");
        return new DefaultTableModel(new Object[][]{{"1.1", "1.2"}, {"2.1", "2.2"}}, new Object[]{"c1", "c2"});
    }


    public TableModel createTable2(@NotNull String param1, @NotNull String param2)
    {
        return new DefaultTableModel(new Object[][]{{"1.1", "1.2"}, {"2.1", "2.2"}}, new Object[]{"c1", "c2"});
    }


    public TableModel createTable3(@NotNull String param1, @NotNull String param2, @NotNull String param3)
    {
        return new DefaultTableModel(new Object[][]{{"1.1", "1.2"}, {"2.1", "2.2"}}, new Object[]{"c1", "c2"});
    }


    public TableModel createTable4(@NotNull String param1)
    {
        throw new RuntimeException("evil");
    }


    public DefaultTableModel createTable5(@NotNull String param1)
    {
        throw new RuntimeException("evil");
    }


    public JButton createTable6()
    {
        return null;
    }
}
