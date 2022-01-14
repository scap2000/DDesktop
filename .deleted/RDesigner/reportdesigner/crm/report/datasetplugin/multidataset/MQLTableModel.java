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
package org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.jetbrains.annotations.NotNull;
import org.pentaho.commons.mql.ui.mqldesigner.CWMStartup;
import org.pentaho.core.admin.datasources.DataSourceInfo;
import org.pentaho.core.admin.datasources.StandaloneSimpleJNDIDatasourceAdmin;
import org.pentaho.core.session.StandaloneSession;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.reportdesigner.crm.report.connection.SQLUtil;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCClassLoader;
import org.pentaho.util.logging.ILogger;

import be.ibridge.kettle.core.database.DatabaseMeta;

/**
 * User: mdamour Date: 27.07.2006 Time: 16:39:51
 */
public class MQLTableModel extends AbstractTableModel {
  @NotNull
  private ArrayList<Object[]> data;
  @NotNull
  private ArrayList<String> columnNames;
  @NotNull
  private ArrayList<Class> columnTypes;

  public MQLTableModel(@NotNull
  String xmiDefinitionFile, @NotNull
  String inMqlQuery, int maxRowsToProcess) throws Exception {
    // noinspection ConstantConditions
    if (xmiDefinitionFile == null) {
      throw new IllegalArgumentException("xmlDefinitionFile must not be null");
    }
    // noinspection ConstantConditions
    if (inMqlQuery == null) {
      throw new IllegalArgumentException("mqlQuery must not be null");
    }

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    try {
      CWMStartup.loadCWMInstance("resources/metadata/repository.properties", "resources/metadata/PentahoCWM.xml");
      CWMStartup.loadMetadata(xmiDefinitionFile, (new File(xmiDefinitionFile)).getParentFile().getName());
      MQLQuery mqlQuery = new MQLQuery(inMqlQuery, "en_US", new CwmSchemaFactory()); //$NON-NLS-1$
      DatabaseMeta databaseMeta = mqlQuery.getDatabaseMeta();

      if (databaseMeta.getAccessType() == DatabaseMeta.TYPE_ACCESS_JNDI) {
        String jndiName = databaseMeta.getDatabaseName();
        if (jndiName != null) {
          // update simple-jndi file
          StandaloneSession session = new StandaloneSession("Datasource-Session"); //$NON-NLS-1$
          session.setLoggingLevel(ILogger.ERROR);
          StandaloneSimpleJNDIDatasourceAdmin dataSourceAdmin = new StandaloneSimpleJNDIDatasourceAdmin(ReportWizard.simpleJNDIPath, session);
          DataSourceInfo dsi = (DataSourceInfo) dataSourceAdmin.getDataSourceInfo(jndiName);
          String driver = dsi.getDriver();
          String userId = dsi.getUserId();
          String password = dsi.getPassword();
          String connectionInfo = dsi.getUrl();
          if (driver == null && connectionInfo == null) {
            // TODO raise an error?
          }
          // noinspection JDBCResourceOpenedButNotSafelyClosed
          connection = JDBCClassLoader.getConnection(driver, connectionInfo, userId, password);
        }
      }

      if (connection == null) {
        String driver = databaseMeta.getDriverClass();
        String userId = databaseMeta.getUsername();
        String password = databaseMeta.getPassword();
        String connectionInfo = databaseMeta.getURL();
        if (driver == null && connectionInfo == null) {
          // TODO raise an error?
        }

        // noinspection JDBCResourceOpenedButNotSafelyClosed
        connection = JDBCClassLoader.getConnection(driver, connectionInfo, userId, password);
      }
      statement = connection.createStatement();
      try {
        if (maxRowsToProcess > 0) {
          statement.setMaxRows(maxRowsToProcess);
        }
      } catch (Exception e) {
        // if this fails, we need not blow out, we just want to try to set this here
        // since it is will do a significantly better job at limited the data
      }
      String sqlQuery = mqlQuery.getQuery().getDisplayQuery();
      resultSet = statement.executeQuery(sqlQuery);
      ResultSetMetaData metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();

      columnNames = new ArrayList<String>();
      columnTypes = new ArrayList<Class>();

      for (int i = 1; i <= columnCount; i++) {
        String columnName = metaData.getColumnName(i);
        Class type = Class.forName(metaData.getColumnClassName(i));
        columnNames.add(columnName);
        columnTypes.add(type);
      }

      data = new ArrayList<Object[]>();

      while (resultSet.next()) {
        Object[] rowData = new Object[columnCount];
        for (int i = 1; i <= columnCount; i++) {
          rowData[i - 1] = resultSet.getObject(i);
        }
        data.add(rowData);
        if (data.size() >= maxRowsToProcess) {
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      SQLUtil.closeResultSet(resultSet);
      SQLUtil.closeStatement(statement);
      SQLUtil.closeConnection(connection);
    }
  }

  @NotNull
  public String getColumnName(int column) {
    return columnNames.get(column);
  }

  @NotNull
  public Class<?> getColumnClass(int columnIndex) {
    return columnTypes.get(columnIndex);
  }

  public int getColumnCount() {
    return columnNames.size();
  }

  public int getRowCount() {
    return data.size();
  }

  @NotNull
  public Object getValueAt(int rowIndex, int columnIndex) {
    return data.get(rowIndex)[columnIndex];
  }
}
