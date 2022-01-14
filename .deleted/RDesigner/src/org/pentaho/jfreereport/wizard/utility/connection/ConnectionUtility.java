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
 *
 * @created Feb 01, 2006
 * @author Michael D'Amour
 */
package org.pentaho.jfreereport.wizard.utility.connection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.pentaho.core.admin.datasources.DataSourceInfo;
import org.pentaho.core.connection.IPentahoConnection;
import org.pentaho.core.connection.IPentahoResultSet;
import org.pentaho.core.runtime.IRuntimeContext;
import org.pentaho.core.solution.IActionCompleteListener;
import org.pentaho.core.util.TemplateUtil;
import org.pentaho.core.util.XmlHelper;
import org.pentaho.data.PentahoConnectionFactory;
import org.pentaho.data.connection.mdx.MDXConnection;
import org.pentaho.data.connection.mdx.MDXResultSet;
import org.pentaho.data.connection.xquery.XQConnection;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.dialog.PentahoSWTMessageBox;
import org.pentaho.jfreereport.wizard.utility.ActionSequenceUtility;
import org.pentaho.jfreereport.wizard.utility.PentahoUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportParameterUtility;
import org.pentaho.jfreereport.wizard.utility.report.ReportSpecUtility;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.pms.mql.MappedQuery;

import be.ibridge.kettle.core.database.DatabaseMeta;

public class ConnectionUtility {
  public static final String[] driverNames = new String[] { "Hypersonic", "Oracle", "DB2", "MySQL", "Teradata" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

  public static final String[] drivers = new String[] {
      "org.hsqldb.jdbcDriver", "oracle.jdbc.driver.OracleDriver", "com.ibm.db2.jcc.DB2Driver", "com.mysql.jdbc.Driver", "com.ncr.teradata.TeraDriver" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

  public static final String[] connectStrings = new String[] {
      "jdbc:hsqldb:hsql://localhost/sampledata", "jdbc:oracle:thin:@localhost:1521:orcl", "jdbc:db2:DATABASE_NAME", "jdbc:mysql://localhost:PORT/DATABASE_NAME", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      "jdbc:teradata://teradata.server.ip.swap" }; //$NON-NLS-1$

  public static HashMap queryColumnMap = new HashMap();

  public static HashMap queryResultSetMap = new HashMap();

  public static int rowLimit = 250;

  private static Driver[] pluginDrivers = null;

  private static boolean implementsSQLDriver(Class aClass) {
    Class[] interfaces = aClass.getInterfaces();
    for (int i = 0; i < interfaces.length; i++) {
      Class anInterface = interfaces[i];
      if ("java.sql.Driver".equals(anInterface.getName())) { //$NON-NLS-1$
        return true;
      }
    }
    return false;
  }

  private static boolean instanceOfSQLDriver(Class aClass) {
    Class superClass = aClass;
    while ((superClass = superClass.getSuperclass()) != null) {
      if (superClass.isInterface() == true && "java.sql.Driver".equals(superClass.getName())) { //$NON-NLS-1$
        return true;
      } else if (implementsSQLDriver(aClass)) {
        return true;
      }
    }
    return false;
  }

  private static String[] getJarFileEntries(File jarFile) {
    ArrayList jarEntries = new ArrayList();
    try {
      ZipFile zf = new ZipFile(jarFile);
      Enumeration e = zf.entries();
      while (e.hasMoreElements()) {
        ZipEntry ze = (ZipEntry) e.nextElement();
        if (!ze.isDirectory()) {
          jarEntries.add(ze.getName());
        }
      }
      zf.close();
    } catch (IOException e1) {
    }
    return (String[]) jarEntries.toArray(new String[jarEntries.size()]);
  }

  public static Driver[] findAllDrivers(String root) {
    if (pluginDrivers == null) {
      ArrayList allDrivers = new ArrayList();
      try {
        Class jdbcOdbcDriverClass = Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); //$NON-NLS-1$
        Driver jdbcOdbcDriver = (Driver) jdbcOdbcDriverClass.newInstance();
        allDrivers.add(jdbcOdbcDriver);
      } catch (Exception e) {
      }
      File driverDirectory = new File(root + "/lib/jdbc"); //$NON-NLS-1$
      if (driverDirectory.isDirectory()) {
        File[] driverFiles = driverDirectory.listFiles();
        ArrayList urls = new ArrayList();
        for (int i = 0; i < driverFiles.length; i++) {
          File driverFile = driverFiles[i];
          if (driverFile.getName().endsWith(".jar") || driverFile.getName().endsWith(".zip")) { //$NON-NLS-1$ //$NON-NLS-2$
            try {
              urls.add(driverFile.toURL());
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        URLClassLoader classLoader = new URLClassLoader((URL[]) urls.toArray(new URL[0]), ClassLoader
            .getSystemClassLoader());
        for (int i = 0; i < driverFiles.length; i++) {
          File driverFile = driverFiles[i];
          if (driverFile.getName().endsWith(".jar")) { //$NON-NLS-1$
            try {
              String[] resourceNames = getJarFileEntries(driverFile);
              for (int j = 0; j < resourceNames.length; j++) {
                String resourceName = resourceNames[j];
                if (resourceName.endsWith(".class")) { //$NON-NLS-1$
                  try {
                    String className = resourceName.substring(0, resourceName.length() - 6);
                    className = className.replace('/', '.');
                    Class aClass = classLoader.loadClass(className);
                    if ((instanceOfSQLDriver(aClass) || resourceName.endsWith("Driver.class")) && !Modifier.isAbstract(aClass.getModifiers())) { //$NON-NLS-1$
                      Driver d = (Driver) Class.forName(className, true, classLoader).newInstance();
                      DriverManager.registerDriver(new DriverShim(d));
                      System.out.println("Registering: " + className); //$NON-NLS-1$
                      allDrivers.add(d);
                    }
                  } catch (Throwable e) {
                  }
                }
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
      pluginDrivers = (Driver[]) allDrivers.toArray(new Driver[allDrivers.size()]);
      return pluginDrivers;
    }
    return pluginDrivers;
  }

  public static Driver[] getLoadedDrivers() {
    ArrayList list = Collections.list(DriverManager.getDrivers());
    return (Driver[]) list.toArray(new Driver[list.size()]);
  }

  public static Connection initializeDataConnection(String driverName, String location, String userName, String password) {
    try {
        /*
         * TODO This is where we use the java.sql package to provide a SQL connection object back to the caller
         */
        Driver driver = null;
        try {
          driver = DriverManager.getDriver(location);
        } catch (Exception e) {
          // if we don't find this connection, it isn't registered, so we'll try to find it on the classpath
        }
        if (driver == null) {
          Class driverClass = Class.forName(driverName);
          driver = (Driver) driverClass.newInstance();
          DriverManager.registerDriver(driver);
        }
        Properties info = new Properties();
        info.put("user", userName); //$NON-NLS-1$
        info.put("password", password); //$NON-NLS-1$
        Connection nativeConnection = driver.connect(location, info);
        if (nativeConnection == null) {
//          logger.error(Messages.getErrorString("ConnectFactory.ERROR_0001_INVALID_CONNECTION2", driverName, location)); //$NON-NLS-1$
        }
        return nativeConnection;
      } catch (Throwable t) {
        t.printStackTrace();
      }
      return null;
}  
  
  
  public static boolean testConnection(String connectionInfo, boolean isXQuery) {
    IPentahoConnection connection = null;
    boolean result = false;
    try {
      if (isXQuery && connectionInfo != null && !connectionInfo.equalsIgnoreCase("")) { //$NON-NLS-1$
        connection = getConnection(connectionInfo, null, IPentahoConnection.XML_DATASOURCE);
        if (connection != null) {
          result = true;
        }
      } else if (connectionInfo != null && !connectionInfo.equalsIgnoreCase("")) { //$NON-NLS-1$
        connection = getConnection(connectionInfo);
        if (connection != null) {
          result = true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static boolean testConnection(String driver, String connectString, String user, String password) {
    boolean result = false;
    IPentahoConnection connection = null;
    try {
      connection = getConnection(driver, connectString, user, password);
      if (connection != null) {
        result = true;
      }
    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static boolean testMDXConnection(String jndiName, String catalog) {
    boolean result = false;
    IPentahoConnection connection = null;
    try {
      connection = getConnection(jndiName, catalog, IPentahoConnection.MDX_DATASOURCE);
      if (connection != null) {
        result = true;
      }
    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (Exception e) {
      }
    }
    return result;
  }

  public static IPentahoConnection getMQLConnection(ReportSpec reportSpec) {
    try {
      MQLQuery mqlQuery = new MQLQuery(reportSpec.getMqlQuery(), "en_US", new CwmSchemaFactory()); //$NON-NLS-1$
      DatabaseMeta databaseMeta = mqlQuery.getDatabaseMeta();
      IPentahoConnection localConnection = null;
      if (databaseMeta.getAccessType() == DatabaseMeta.TYPE_ACCESS_JNDI) {
        String jndiName = databaseMeta.getDatabaseName();
        if (jndiName != null) {
          localConnection = PentahoConnectionFactory.getConnection(IPentahoConnection.SQL_DATASOURCE, jndiName, null);
        }
      }
      if (localConnection == null) {
        String driver = databaseMeta.getDriverClass();
        String userId = databaseMeta.getUsername();
        String password = databaseMeta.getPassword();
        String connectionInfo;
        connectionInfo = databaseMeta.getURL();
        if (driver == null && connectionInfo == null) {
          // TODO raise an error?
        }
        localConnection = PentahoConnectionFactory.getConnection(IPentahoConnection.SQL_DATASOURCE, driver,
            connectionInfo, userId, password, null);
      }
      
      //TODO Need to cache map of column identifiers to reinstate the long identifiers 
      MappedQuery mappedQuery = mqlQuery.getQuery();
      String sqlQuery = mappedQuery.getQuery();
      Object[] cols = ConnectionUtility.getColumns(localConnection, sqlQuery, null);
      if (cols == null) {
        PentahoSWTMessageBox mb = new PentahoSWTMessageBox(
            Messages.getString("QueryPanel.31"), Messages.getString("QueryPanel.40"), 360, 140); //$NON-NLS-1$ //$NON-NLS-2$
        mb.open();
        return null;
      }
      reportSpec.setQuery(sqlQuery);
      return localConnection;
    } catch (Exception e) {
    }
    return null;
  }

  public static IPentahoConnection getConnection(String connectionInfo, String catalog, int type) {
    // Thread.dumpStack();
    IPentahoConnection connection = null;
    if (type == IPentahoConnection.XML_DATASOURCE) {
      try {
        PentahoUtility.startup();
        connection = PentahoConnectionFactory.getConnection(IPentahoConnection.XML_DATASOURCE, null);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
      }
    } else if (type == IPentahoConnection.MDX_DATASOURCE) {
      try {
        if (connectionInfo != null) {
          PentahoUtility.startup();
          DataSourceInfo info = ReportWizard.dataSourceAdmin.getDataSourceInfo(connectionInfo);
          File catalogFile = new File(catalog);
          if (catalogFile.exists()) {
            catalog = catalogFile.toURL().toString();
          }
          String jdbcConnectStr = info.getUrl();
          if (info.getDriver().equalsIgnoreCase("org.postgresql.Driver") || info.getDriver().equalsIgnoreCase("com.mysql.jdbc.Driver") || //$NON-NLS-1$ //$NON-NLS-2$
              info.getDriver().equalsIgnoreCase("org.gjt.mm.mysql.Driver")) { //$NON-NLS-1$
            jdbcConnectStr += "?user=" + info.getUserId() + "&password=" + info.getPassword(); //$NON-NLS-1$ //$NON-NLS-2$
          }
          jdbcConnectStr += "; Catalog=" + catalog; //$NON-NLS-1$
          connection = PentahoConnectionFactory.getConnection(IPentahoConnection.MDX_DATASOURCE, jdbcConnectStr,
              "mondrian; JdbcDrivers=" + info.getDriver(), info.getUserId(), info.getPassword(), null); //$NON-NLS-1$
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
      }
    } else {
      try {
        if (connectionInfo != null) {
          PentahoUtility.startup();
          connection = PentahoConnectionFactory.getConnection(IPentahoConnection.SQL_DATASOURCE, connectionInfo, null);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
      }
    }
    return connection;
  }

  public static IPentahoConnection getConnection(String jndiName) {
    return getConnection(jndiName, null, IPentahoConnection.SQL_DATASOURCE);
  }

  public static IPentahoConnection getConnection(String driver, String connectString, String user, String password) {
    IPentahoConnection connection = null;
    try {
      PentahoUtility.startup();
      connection = PentahoConnectionFactory.getConnection(IPentahoConnection.SQL_DATASOURCE, driver, connectString,
          user, password, null);
    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
    }
    return connection;
  }

  public static IPentahoResultSet executeQuery(IPentahoConnection connection, String rawQuery, String documentPath) {
    // cache query/metadata information
    IPentahoResultSet resultSet = null;
    if (queryResultSetMap.get(rawQuery) != null) {
      resultSet = (IPentahoResultSet) queryResultSetMap.get(rawQuery);
      return resultSet;
    }
    try {
      if (connection != null && rawQuery != null) {
        if (queryResultSetMap.get(rawQuery) == null) {
          if (connection instanceof XQConnection) {
            // check that the document exists
            File documentFile = new File(documentPath);
            if (!documentFile.exists()) {
              return null;
            }
            // convert any '\' to '/'
            documentPath = documentFile.getCanonicalPath();
            documentPath = documentPath.replaceAll("\\\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
            SAXReader reader = new SAXReader();
            try {
              Document document = reader.read(documentFile);
              Node commentNode = document.selectSingleNode("/result-set/comment()");
              String commentString = commentNode != null ? commentNode.getText() : ""; //$NON-NLS-1$
              StringTokenizer st = new StringTokenizer(commentString, ","); //$NON-NLS-1$
              List columnTypesList = new LinkedList();
              while (st.hasMoreTokens()) {
                String token = st.nextToken();
                columnTypesList.add(token);
              }
              String[] columnTypes = (String[]) columnTypesList.toArray(new String[0]);
              // create the query
              String query = null;
              if (rawQuery != null) {
                if (rawQuery.indexOf("{XML_DOCUMENT}") >= 0) { //$NON-NLS-1$//$NON-NLS-2$
                  query = TemplateUtil.applyTemplate(rawQuery, "XML_DOCUMENT", documentPath);
                } else {
                  query = "doc(\"" + documentPath + "\")" + rawQuery; //$NON-NLS-1$ //$NON-NLS-2$
                }
              }
              connection.setMaxRows(rowLimit);
              resultSet = ((XQConnection) connection).executeQuery(query, columnTypes);
              resultSet = resultSet.memoryCopy();
              queryResultSetMap.put(rawQuery, resultSet);
              Object[] columnHeaders = resultSet.getMetaData().getColumnHeaders()[0];
              queryColumnMap.put(rawQuery, columnHeaders);
            } catch (Exception e) {
              e.printStackTrace();
            }
          } else {
            String query = rawQuery;
            if (!(connection instanceof MDXConnection)) {
              connection.setMaxRows(rowLimit);
              query = ReportParameterUtility.setupParameters(rawQuery);
            }
            resultSet = connection.executeQuery(query);
            if (!(resultSet instanceof MDXResultSet)) {
              resultSet = resultSet.memoryCopy();
            }
            queryResultSetMap.put(rawQuery, resultSet);
            Object[] columnHeaders = resultSet.getMetaData().getColumnHeaders()[0];
            queryColumnMap.put(rawQuery, columnHeaders);
          }
        }
        if (resultSet != null) {
          try {
            resultSet.beforeFirst();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        return resultSet;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }



  public static Object[] getKettleColumns(ReportSpec spec, String kettleTransformationFile) {
    // need to run the transformation and push the result into an IPentahoResultSet
    // this can be done by running the KettleComponent and getting that output
    String solutionRootPath = PentahoUtility.getSolutionRoot();
    String solution = "samples"; //$NON-NLS-1$
    String path = "reporting"; //$NON-NLS-1$
    String actionSequenceTemplateName = "KettleTransformation.xaction"; //$NON-NLS-1$
    String actionSequenceName = spec.getReportName() + ".xaction"; //$NON-NLS-1$
    String actionSeqDocPath = solutionRootPath + "/" + solution + "/" + path + "/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    String actionSeqDocInput = actionSeqDocPath + actionSequenceTemplateName;
    String actionSeqDocOutput = actionSeqDocPath + actionSequenceName;
    File f = new File(kettleTransformationFile);
    String tmpKettleTransformationFile = f.getName();
    String kettleDestFilePath = actionSeqDocPath + "/" + tmpKettleTransformationFile; //$NON-NLS-1$ //$NON-NLS-2$
    ReportSpecUtility.copyFile(f.getAbsolutePath(), kettleDestFilePath);
    ActionSequenceUtility.getInstance().createKettleActionSequence(actionSeqDocInput, actionSeqDocOutput,
        kettleDestFilePath, spec.getKettleStep());
    IActionCompleteListener acl = new IActionCompleteListener() {
      public void actionComplete(IRuntimeContext runtime) {
      }
    };
    try {
      IRuntimeContext runtimeContext = ActionSequenceUtility.getInstance().executeKettleSequence(solutionRootPath,
          solution, path, actionSequenceName, acl);
      IPentahoResultSet resultSet = (IPentahoResultSet) runtimeContext.getOutputParameter("rule-result").getValue();
      queryResultSetMap.put(kettleTransformationFile, resultSet);
      Object[] columnHeaders = resultSet.getMetaData().getColumnHeaders()[0];
      queryColumnMap.put(kettleTransformationFile, columnHeaders);
      return columnHeaders;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Object[] getColumns(IPentahoConnection connection, String rawQuery, String documentPath) throws Exception {
    // unforunately this has to throw an exception and nothing more specific thanks to connection throwing an exception.
    // nice.
    if (connection == null || rawQuery == null) {
      return null;
    }
    
    
    // cache query/metadata information
    if (queryColumnMap.get(rawQuery) != null) {
      return (Object[]) queryColumnMap.get(rawQuery);
    }
      if (connection != null && rawQuery != null) {
        if (connection instanceof XQConnection) {
          // check that the document exists
          File documentFile = new File(documentPath);
          if (!documentFile.exists()) {
            return null;
          }
          // convert any '\' to '/'
          documentPath = documentFile.getCanonicalPath();
          documentPath = documentPath.replaceAll("\\\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
          SAXReader reader = new SAXReader();
            Document document = reader.read(documentFile);
            String commentString = XmlHelper.getNodeText("/result-set/comment()", document, ""); //$NON-NLS-1$ //$NON-NLS-2$
            StringTokenizer st = new StringTokenizer(commentString, ","); //$NON-NLS-1$
            List columnTypesList = new LinkedList();
            while (st.hasMoreTokens()) {
              String token = st.nextToken();
              columnTypesList.add(token);
            }
            String[] columnTypes = (String[]) columnTypesList.toArray(new String[0]);
            // create the query
            String query = null;
            if (rawQuery != null) {
              if (rawQuery.indexOf("{XML_DOCUMENT}") >= 0) { //$NON-NLS-1$//$NON-NLS-2$
                query = TemplateUtil.applyTemplate(rawQuery, "XML_DOCUMENT", documentPath);
              } else {
                query = "doc(\"" + documentPath + "\")" + rawQuery; //$NON-NLS-1$ //$NON-NLS-2$
              }
            }
            connection.setMaxRows(rowLimit);
            IPentahoResultSet resultSet = ((XQConnection) connection).executeQuery(query, columnTypes);
            resultSet = resultSet.memoryCopy();
            Object[] columnHeaders = resultSet.getMetaData().getColumnHeaders()[0];
            queryColumnMap.put(rawQuery, columnHeaders);
            queryResultSetMap.put(rawQuery, resultSet);
        } else if (connection instanceof MDXConnection) {
          IPentahoResultSet resultSet = connection.executeQuery(rawQuery);
          // resultSet = resultSet.memoryCopy();bad news
          Object[][] columnHeaders = resultSet.getMetaData().getColumnHeaders();
          Object[] mdxHeaders = new Object[columnHeaders[0].length];
          int depth = columnHeaders.length;
          for (int x = 0; x < columnHeaders[0].length; x++) {
            for (int y = 0; y < depth; y++) {
              if (mdxHeaders[x] == null) {
                mdxHeaders[x] = columnHeaders[y][x];
              } else {
                mdxHeaders[x] = mdxHeaders[x] + "/" + columnHeaders[y][x]; //$NON-NLS-1$
              }
            }
            // System.out.println("mdxHeader = " + mdxHeaders[x]);
          }
          queryColumnMap.put(rawQuery, mdxHeaders);
          queryResultSetMap.put(rawQuery, resultSet);
        } else {
          if (!(connection instanceof MDXConnection)) {
            connection.setMaxRows(rowLimit);
          }
          String query = ReportParameterUtility.setupParameters(rawQuery);
          IPentahoResultSet resultSet = connection.executeQuery(query);
          if (!(resultSet instanceof MDXResultSet)) {
            resultSet = resultSet.memoryCopy();
          }
          Object[] columnHeaders = resultSet.getMetaData().getColumnHeaders()[0];
          queryColumnMap.put(rawQuery, columnHeaders);
          queryResultSetMap.put(rawQuery, resultSet);
        }
      }
    if (queryColumnMap.get(rawQuery) != null) {
      return (Object[]) queryColumnMap.get(rawQuery);
    }
    return null;
  }

  public static String[] getDrivers(String root) {
    Driver[] d = findAllDrivers(root);
    String drivers[] = new String[d.length];
    for (int i = 0; i < d.length; i++) {
      drivers[i] = d[i].getClass().getName();
    }
    return drivers;
  }

  public static String[] getConnectStrings() {
    return connectStrings;
  }

  public static String[] getDriverNames() {
    return driverNames;
  }

  public static void setRowLimit(int rowLimit) {
    ConnectionUtility.rowLimit = rowLimit;
  }
}
