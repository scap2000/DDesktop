/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package org.pentaho.jfreereport.wizard.utility.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

class DriverShim implements Driver {
    private Driver driver;
    DriverShim(Driver d) {
        this.driver = d;
    }
    public boolean acceptsURL(String u) throws SQLException {
        return this.driver.acceptsURL(u);
    }
    public Connection connect(String u, Properties p) throws SQLException {
        return this.driver.connect(u, p);
    }
    public int getMajorVersion() {
        return this.driver.getMajorVersion();
    }
    public int getMinorVersion() {
        return this.driver.getMinorVersion();
    }
    public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
        return this.driver.getPropertyInfo(u, p);
    }
    public boolean jdbcCompliant() {
        return this.driver.jdbcCompliant();
    }
}