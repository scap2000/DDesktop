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
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 23.09.2006
 * Time: 09:16:05
 */
public class ClasspathSearcher
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(ClasspathSearcher.class.getName());

    @NotNull
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    @NotNull
    private static ClasspathSearcher instance = new ClasspathSearcher();


    @NotNull
    public static ClasspathSearcher getInstance()
    {
        return instance;
    }


    @NotNull
    private String[] filePaths;
    @NotNull
    private String[] drivers;


    private ClasspathSearcher()
    {
        filePaths = EMPTY_STRING_ARRAY;

        try
        {
            URL url = getClass().getResource("/res/icons/ReportFrameIcon.png");//NON-NLS
            String urlStr = url.toString();
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute urlStr = " + urlStr);
            if (urlStr.startsWith("jar:"))//NON-NLS
            {
                //built version
                int from = "jar:".length();//NON-NLS
                int to = urlStr.indexOf("/lib/ReportDesigner.jar!/");//NON-NLS
                String libFolderName = urlStr.substring(from, to) + "/lib/";//NON-NLS
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute libFolderName = " + libFolderName);

                URI libFolderURI = new URI(libFolderName);

                //scan lib folder
                File libFolder = new File(new File(libFolderURI), "jdbc");//NON-NLS
                File[] files = libFolder.listFiles();
                ArrayList<String> paths = new ArrayList<String>();
                if (files != null)
                {
                    for (File file : files)
                    {
                        paths.add(file.getPath());
                    }
                }
                filePaths = paths.toArray(new String[paths.size()]);
            }
            else
            {
                //devel version
                //
                // MB - Fix problem of not being able to run from Eclipse because the
                // output folder for classes in the Eclipse project is
                // bin, not classes. This change should allow both situations
                // to work.
                int to = urlStr.indexOf("/res/icons/ReportFrameIcon.png");//NON-NLS
                String resFolderName = urlStr.substring(0, to) + "/../lib/jdbc/";//NON-NLS
                File resFolder = new File(new URI(resFolderName));
                File[] files = resFolder.listFiles();
                ArrayList<String> paths = new ArrayList<String>();
                if (files != null)
                {
                    for (File file : files)
                    {
                        paths.add(file.getPath());
                    }
                }
                filePaths = paths.toArray(new String[paths.size()]);
            }
        }
        catch (URISyntaxException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TranslationUtil.findLocales ", e);
        }
        catch (Throwable throwable)
        {
            UncaughtExcpetionsModel.getInstance().addException(throwable);
        }

        drivers = findDrivers();
    }


    @NotNull
    public String[] getFilePaths()
    {
        return filePaths;
    }


    @NotNull
    public String[] getDrivers()
    {
        return drivers;
    }


    @NotNull
    private String[] findDrivers()
    {
        ArrayList<URL> urls = new ArrayList<URL>();
        for (String jar : filePaths)
        {
            try
            {
                urls.add(new File(jar).toURI().toURL());
            }
            catch (MalformedURLException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ClasspathSearcher.findDrivers ", e);
            }
        }

        URLClassLoader urlClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));

        ArrayList<String> driverClassNames = new ArrayList<String>();
        for (String filePath1 : filePaths)
        {
            try
            {
                String filePath = filePath1;
                JarFile jarFile = new JarFile(new File(filePath));
                Enumeration<JarEntry> enumeration = jarFile.entries();
                while (enumeration.hasMoreElements())
                {
                    JarEntry jarEntry = enumeration.nextElement();
                    String name = jarEntry.getName();
                    if (!jarEntry.isDirectory() && name.endsWith(".class"))//NON-NLS
                    {
                        String className = name.substring(0, name.length() - 6).replace('/', '.');
                        try
                        {
                            Class<?> aClass = Class.forName(className, false, urlClassLoader);
                            if (Driver.class.isAssignableFrom(aClass))
                            {
                                driverClassNames.add(className);
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ClasspathSearcher.findDrivers aClass = " + aClass);
                            }
                        }
                        catch (Throwable e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ClasspathSearcher.findDrivers ", e);
                        }
                    }
                }
            }
            catch (IOException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ClasspathSearcher.findDrivers ", e);
            }
        }
        return driverClassNames.toArray(new String[driverClassNames.size()]);
    }
}
