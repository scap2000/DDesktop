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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.jfreereport.wizard.utility.connection.ConnectionUtility;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 14.02.2006
 * Time: 19:49:29
 */
@NonNls
public class JDBCClassLoader extends URLClassLoader
{

    static
    {
        //    MB - This needs to be fixed for the final GA
        //    First part of the fix for PRD-108
        //    TODO: Fix the RD to only use the RDW driver discovery mechanism.
        ConnectionUtility.getDrivers(".");
    }


    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JDBCClassLoader.class.getName());

    @NotNull
    public static final String UNIQUE_RANDOM_NAME = "this.is.really.cool.code";


    @SuppressWarnings({"ALL"})
    public static void main(@NotNull String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        InputStream resourceAsStream = JDBCClassLoader.class.getResourceAsStream("ConnectionInstantiator.class");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[8192];
        while ((len = resourceAsStream.read(buffer)) != -1)
        {
            baos.write(buffer, 0, len);
        }

        resourceAsStream.close();
        JDBCClassLoader loader = new JDBCClassLoader(new URL[]{new File("C:\\Daten\\07_Projekte\\Report\\org.pentaho.reportdesigner.crm.report\\lib\\postgresql-8.1dev-403.jdbc3.jar").toURI().toURL()}, JDBCClassLoader.class.getClassLoader(), baos.toByteArray());
        Class<?> aClass = loader.loadClass(UNIQUE_RANDOM_NAME);
        System.out.println("aClass.getClassLoader() = " + aClass.getClassLoader());
        Method declaredMethod = aClass.getDeclaredMethod("getConnection", String.class, String.class, String.class, String.class);
        Connection conn = (Connection) declaredMethod.invoke(null, "org.postgresql.Driver", "jdbc:postgresql:reporttest", "*********", "********");
        System.out.println("conn = " + conn);
    }


    @NotNull
    public static Connection getConnection(@NotNull String driver, @NotNull String connectionString, @NotNull String user, @NotNull String password) throws Exception
    {
        return getConnection(ClasspathSearcher.getInstance().getFilePaths(), driver, connectionString, user, password);
    }


    @NotNull
    public static Connection getConnection(@NotNull String[] jars, @NotNull String driver, @NotNull String connectionString, @NotNull String user, @NotNull String password) throws Exception
    {
        Throwable firstException = null;
        Connection conn;
        InputStream resourceAsStream = null;
        try
        {
            resourceAsStream = JDBCClassLoader.class.getResourceAsStream("ConnectionInstantiator.class");//NON-NLS
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[8192];
            while ((len = resourceAsStream.read(buffer)) != -1)
            {
                baos.write(buffer, 0, len);
            }

            resourceAsStream.close();

            ArrayList<URL> urls = new ArrayList<URL>();
            for (String jar : jars)
            {
                urls.add(new File(jar).toURI().toURL());
            }

            JDBCClassLoader loader = new JDBCClassLoader(urls.toArray(new URL[urls.size()]), JDBCClassLoader.class.getClassLoader(), baos.toByteArray());
            Class<?> aClass = loader.loadClass(UNIQUE_RANDOM_NAME);
            Method declaredMethod = aClass.getDeclaredMethod("getConnection", String.class, String.class, String.class, String.class);//NON-NLS
            conn = (Connection) declaredMethod.invoke(null, driver, connectionString, user, password);
            return conn;
        }
        catch (InvocationTargetException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "MyClassLoader.getConnection ", e);
            firstException = e.getCause();
        }
        catch (IllegalAccessException e)
        {
            throw e;
        }
        catch (NoSuchMethodException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw e;
        }
        catch (ClassNotFoundException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "MyClassLoader.getConnection ", e);
        }
        finally
        {
            IOUtil.closeStream(resourceAsStream);
        }

        Connection connection;
        try
        {
            Class.forName(driver);
            //noinspection JDBCResourceOpenedButNotSafelyClosed
            connection = DriverManager.getConnection(connectionString, user, password);
        }
        catch (Exception e)
        {
            if (firstException != null)
            {
                throw new CompoundException(firstException.getMessage(), firstException, e);
            }
            else
            {
                //noinspection ProhibitedExceptionThrown
                throw e;
            }
        }

        return connection;
    }


    public static class CompoundException extends Exception
    {
        @NotNull
        private final Throwable cause2;


        public CompoundException(@NotNull String message, @NotNull Throwable cause1, @NotNull Throwable cause2)
        {
            super(message, cause1);
            this.cause2 = cause2;
        }


        @NotNull
        public Throwable getCause2()
        {
            return cause2;
        }
    }


    public static boolean tryToFindClass(@NotNull String[] jars, @NotNull String driver)
    {
        InputStream resourceAsStream = null;
        try
        {
            resourceAsStream = JDBCClassLoader.class.getResourceAsStream("ConnectionInstantiator.class");//NON-NLS
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[8192];
            while ((len = resourceAsStream.read(buffer)) != -1)
            {
                baos.write(buffer, 0, len);
            }

            resourceAsStream.close();
            ArrayList<URL> urls = new ArrayList<URL>();
            for (String jar : jars)
            {
                urls.add(new File(jar).toURI().toURL());
            }
            JDBCClassLoader loader = new JDBCClassLoader(urls.toArray(new URL[urls.size()]), JDBCClassLoader.class.getClassLoader(), baos.toByteArray());
            /*Class<?> aClass = */
            loader.loadClass(driver);
            return true;
        }
        catch (Exception e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "MyClassLoader.tryToFindClass ", e);
        }
        finally
        {
            IOUtil.closeStream(resourceAsStream);
        }

        try
        {
            Class.forName(driver);
        }
        catch (ClassNotFoundException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "MyClassLoader.tryToFindClass ", e);
            return false;
        }
        return true;
    }


    @NotNull
    private byte[] clazz;


    public JDBCClassLoader(@NotNull URL[] urls, @NotNull ClassLoader parent, @NotNull byte[] clazz)
    {
        super(urls, parent);
        this.clazz = clazz;
    }


    @NotNull
    protected Class<?> findClass(@NotNull final String name) throws ClassNotFoundException
    {
        if (UNIQUE_RANDOM_NAME.equals(name))
        {
            return defineClass("org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.ConnectionInstantiator", clazz, 0, clazz.length);
        }
        return super.findClass(name);
    }

}
