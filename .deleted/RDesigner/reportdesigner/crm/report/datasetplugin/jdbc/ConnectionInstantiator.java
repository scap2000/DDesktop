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

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * User: Martin
 * Date: 14.02.2006
 * Time: 19:56:34
 */
public class ConnectionInstantiator
{
    private ConnectionInstantiator()
    {
    }


    @NotNull
    public static Connection getConnection(@NotNull String driverClass, @NotNull String connectionString, @NotNull String userName, @NotNull String password) throws ClassNotFoundException, SQLException
    {
        Class.forName(driverClass);
        //noinspection JDBCResourceOpenedButNotSafelyClosed
        Connection connection = DriverManager.getConnection(connectionString, userName, password);
        return connection;
    }

}
