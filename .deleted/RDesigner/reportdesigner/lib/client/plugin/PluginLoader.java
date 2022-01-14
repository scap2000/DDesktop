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
package org.pentaho.reportdesigner.lib.client.plugin;


import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;


/**
 * User: Martin
 * Date: 01.01.2005
 * Time: 15:00:34
 */
public class PluginLoader extends ClassLoader
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(PluginLoader.class.getName());

    @NotNull
    private HashMap<String, Class> classesMap;
    @NotNull
    private File jarFile;
    @NotNull
    private JarFile jar;
    @NotNull
    private String pluginClassname;
    private int pluginVersion;
    private boolean library;
    @NotNull
    private String[] depends;


    private boolean preload;
    private boolean preloading;
    @NotNull
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    @NotNull
    private static final String PLUGIN_CLASSNAME = "PluginClassname";
    @NotNull
    private static final String LIBRARY = "Library";
    @NotNull
    private static final String PLUGIN_VERSION = "PluginVersion";
    @NotNull
    private static final String DEPENDS = "Depends";
    @NotNull
    private static final String CLASS_ENDING = ".class";


    protected PluginLoader(@NotNull ClassLoader parent, @NotNull File jarFile) throws IOException
    {
        super(parent);
        this.jarFile = jarFile;
        library = false;
        classesMap = new HashMap<String, Class>();

        jar = new JarFile(jarFile, true, JarFile.OPEN_READ);

        try
        {
            Manifest manifest = jar.getManifest();
            if (manifest != null)
            {
                pluginClassname = manifest.getMainAttributes().getValue(PLUGIN_CLASSNAME);
                library = Boolean.valueOf(manifest.getMainAttributes().getValue(LIBRARY)).booleanValue();
                pluginVersion = Integer.parseInt(manifest.getMainAttributes().getValue(PLUGIN_VERSION));
                String depends = manifest.getMainAttributes().getValue(DEPENDS);
                if (depends != null)
                {
                    ArrayList<String> deps = new ArrayList<String>();
                    for (StringTokenizer tokenizer = new StringTokenizer(depends); tokenizer.hasMoreTokens();)
                    {
                        String token = tokenizer.nextToken();
                        deps.add(token);
                    }
                    this.depends = deps.toArray(new String[deps.size()]);
                }
            }
        }
        catch (IOException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.PluginLoader ", e);
        }

        jar.close();

        preload = true;
    }


    @NotNull
    protected String findLibrary(@NotNull String libname)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.findLibrary enter");
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.findLibrary libname = " + libname);

        return super.findLibrary(libname);
    }


    private void preloadClasses()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.preloadClasses enter");

        Enumeration<JarEntry> enumeration = jar.entries();
        while (enumeration.hasMoreElements())
        {
            JarEntry jarEntry = enumeration.nextElement();

            String name = jarEntry.getName();
            if (name.endsWith(CLASS_ENDING))
            {
                String classname = name.substring(0, name.length() - 6);
                String cleanClassname = classname.replace('/', '.');
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.preloadClasses cleanClassname = " + cleanClassname);

                try
                {
                    loadClass(cleanClassname);
                }
                catch (ClassNotFoundException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.preloadClasses ", e);
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.preloadClasses could not load class " + cleanClassname);
                }
            }
        }
    }


    @NotNull
    public String getMainPluginClassname()
    {
        return pluginClassname;
    }


    public int getPluginVersion()
    {
        return pluginVersion;
    }


    public boolean isLibrary()
    {
        return library;
    }


    @NotNull
    public String[] getDepends()
    {
        return depends;
    }


    @NotNull
    public synchronized Class<?> loadClass(@NotNull String name) throws ClassNotFoundException
    {
        if (preload)
        {
            preloading = true;
            preload = false;

            try
            {
                jar = new JarFile(jarFile, true, JarFile.OPEN_READ);
            }
            catch (IOException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.loadClass could not open jar", e);
            }

            preloadClasses();
            preloading = false;

            try
            {
                jar.close();
            }
            catch (IOException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.loadClass could not clase jar", e);
            }
        }

        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.loadClass name = " + name);

        try
        {
            return getParent().loadClass(name);
        }
        catch (ClassNotFoundException e)
        {
            //ok
        }

        //class not found by parent
        Class c = classesMap.get(name);
        if (c != null)
        {
            //already loaded
            return c;
        }

        //not loaded && can not be found by parent

        if (jarFile.canRead())
        {
            try
            {
                if (!preloading)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.loadClass opening jar file " + jarFile);
                    jar = new JarFile(jarFile, true, JarFile.OPEN_READ);
                }

                String entryName = name.replace('.', '/');
                entryName += CLASS_ENDING;

                ZipEntry zipEntry = jar.getEntry(entryName);
                if (zipEntry != null)
                {
                    InputStream is = jar.getInputStream(zipEntry);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = is.read(buffer)) != -1)
                    {
                        bos.write(buffer, 0, len);
                    }

                    byte[] b = bos.toByteArray();

                    if (!preloading)
                    {
                        jar.close();
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.loadClass closed jar file " + jarFile);
                    }

                    Class<?> aClass = defineClass(name, b, 0, b.length);
                    classesMap.put(name, aClass);
                    return aClass;
                }
            }
            catch (IOException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.loadClass ", e);
                throw new ClassNotFoundException(name);
            }

        }

        throw new ClassNotFoundException(name);
    }


    @NotNull
    public synchronized Class<?> loadClass(@NotNull String name, int askParent) throws ClassNotFoundException
    {
        Class aClass = classesMap.get(name);
        if (aClass != null)
        {
            return aClass;
        }

        throw new ClassNotFoundException(name);
    }


    @Nullable
    protected synchronized URL findResource(@NotNull String name)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.findResource ");
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.findResource name = " + name);

        try
        {
            URL context = jarFile.toURI().toURL();
            URL url = new URL("jar", "", -1, context + "!" + (name.startsWith("/") ? name : '/' + name));//NON-NLS
            return url;
        }
        catch (MalformedURLException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.findResource ", e);
        }

        return super.findResource(name);
    }


    @Nullable
    protected Enumeration<URL> findResources(@NotNull String name) throws IOException
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.findResources ");
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.findResources name = " + name);

        URL resource = findResource(name);
        if (resource != null)
        {
            ArrayList<URL> v = new ArrayList<URL>();
            v.add(resource);
            return Collections.enumeration(v);
        }
        return super.findResources(name);
    }


    @Nullable
    public synchronized InputStream getResourceAsStream(@NotNull String name)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.getResourceAsStream ");
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.getResourceAsStream name = " + name);

        if (jarFile.canRead())
        {
            try
            {
                if (!preloading)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.loadClass opening jar file " + jarFile);
                    jar = new JarFile(jarFile, true, JarFile.OPEN_READ);
                }

                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.getResourceAsStream name = " + name);
                Enumeration<JarEntry> enumeration = jar.entries();
                while (enumeration.hasMoreElements())
                {
                    JarEntry jarEntry = enumeration.nextElement();
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.getResourceAsStream jarEntry = " + jarEntry);
                }

                ZipEntry zipEntry = jar.getEntry(name);
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.getResourceAsStream zipEntry = " + zipEntry);
                InputStream is = jar.getInputStream(zipEntry);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] buffer = new byte[8192];
                int len;
                while ((len = is.read(buffer)) != -1)
                {
                    bos.write(buffer, 0, len);
                }

                byte[] b = bos.toByteArray();

                if (!preloading)
                {
                    jar.close();
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.getResourceAsStream closed jar file " + jarFile);
                }

                return new ByteArrayInputStream(b);
            }
            catch (IOException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PluginLoader.getResourceAsStream ", e);
                return null;
            }

        }

        return new ByteArrayInputStream(EMPTY_BYTE_ARRAY);
    }


}
