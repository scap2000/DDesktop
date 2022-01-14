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
package org.pentaho.reportdesigner.crm.report.connection;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: Martin
 * Date: 18.01.2006
 * Time: 15:34:48
 */
public class SQLUtil
{
    private SQLUtil()
    {
    }


    public static void closeResultSet(@Nullable ResultSet resultSet)
    {
        if (resultSet != null)
        {
            try
            {
                resultSet.close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    public static void closeStatement(@Nullable Statement statement)
    {
        if (statement != null)
        {
            try
            {
                statement.close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    public static void closeConnection(@Nullable Connection db)
    {
        if (db != null)
        {
            try
            {
                db.close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
