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
package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 30.08.2006
 * Time: 19:15:15
 */
public class TranslationUtil
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(TranslationUtil.class.getName());


    @SuppressWarnings({"HardCodedStringLiteral"})
    public static void main(@NotNull String[] args)
    {
        printLocale(createLocaleFromString("de"));
        printLocale(createLocaleFromString("de_DE"));
        printLocale(createLocaleFromString("de_CH"));
        printLocale(createLocaleFromString("en"));
        printLocale(createLocaleFromString("en_US_WIN"));
        printLocale(createLocaleFromString("de__POSIX"));
        printLocale(createLocaleFromString("fr__MAC"));
        printLocale(createLocaleFromString("_GB"));
    }


    private static void printLocale(@NotNull Locale locale)
    {
        //noinspection UseOfSystemOutOrSystemErr
        System.out.println(locale.getDisplayLanguage() + " _ " + locale.getDisplayCountry() + " _ " + locale.getDisplayVariant());
    }


    private TranslationUtil()
    {
    }


    @NotNull
    public static List<Locale> findLocales()
    {
        TreeSet<Locale> locales = new TreeSet<Locale>(new Comparator<Locale>()
        {
            public int compare(@NotNull Locale o1, @NotNull Locale o2)
            {
                return o1.toString().compareTo(o2.toString());
            }
        });
        //whatever happens, english is supported and the first available locale

        try
        {
            URL url = TranslationUtil.class.getResource("/res/icons/ReportFrameIcon.png");//NON-NLS
            String urlStr = url.toString();
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute urlStr = " + urlStr);
            if (urlStr.startsWith("jar:"))//NON-NLS
            {
                //built version
                int from = "jar:".length();//NON-NLS
                int to = urlStr.indexOf("/lib/ReportDesigner.jar!/");//NON-NLS
                String libFolderName = urlStr.substring(from, to) + "/lib/";//NON-NLS
                String jarFileName = urlStr.substring(from, to) + "/lib/ReportDesigner.jar";//NON-NLS

                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute libFolderName = " + libFolderName);
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute jarFileName = " + jarFileName);

                URI jarFileURI = new URI(jarFileName);
                URI libFolderURI = new URI(libFolderName);

                JarFile jarFile = new JarFile(new File(jarFileURI));
                Enumeration<JarEntry> enumeration = jarFile.entries();
                while (enumeration.hasMoreElements())
                {
                    JarEntry jarEntry = enumeration.nextElement();
                    String entryName = jarEntry.getName();
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute entryName = " + entryName);

                    if (entryName.indexOf("Translations_") != -1 && entryName.endsWith(".properties"))//NON-NLS
                    {
                        int start = entryName.indexOf("Translations_") + "Translations_".length();//NON-NLS
                        String localeString = entryName.substring(start, entryName.length() - ".properties".length());//NON-NLS
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute localeString = " + localeString);
                        locales.add(createLocaleFromString(localeString));
                    }
                }

                //scan lib folder
                File libFolder = new File(new File(libFolderURI), "res");//NON-NLS
                File[] files = libFolder.listFiles();
                if (files != null)
                {
                    for (File file : files)
                    {
                        String entryName = file.getName();
                        //if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute entryName = " + entryName);
                        if (entryName.startsWith("Translations_") && entryName.endsWith(".properties"))//NON-NLS
                        {
                            String localeString = entryName.substring("Translations_".length(), entryName.length() - ".properties".length());//NON-NLS
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute localeString = " + localeString);
                            locales.add(createLocaleFromString(localeString));
                        }
                    }
                }
            }
            else
            {
                //devel version
                int to = urlStr.indexOf("/res/icons/ReportFrameIcon.png");//NON-NLS
                String resFolderName = urlStr.substring(0, to) + "/res/";//NON-NLS
                File resFolder = new File(new URI(resFolderName));
                File[] files = resFolder.listFiles();
                if (files != null)
                {
                    for (File file : files)
                    {
                        String entryName = file.getName();
                        //if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute entryName = " + entryName);
                        if (entryName.startsWith("Translations_") && entryName.endsWith(".properties"))//NON-NLS
                        {
                            String localeString = entryName.substring("Translations_".length(), entryName.length() - ".properties".length());//NON-NLS
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute localeString = " + localeString);
                            locales.add(createLocaleFromString(localeString));
                        }
                    }
                }
            }

            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FindTranslationsCommand.execute locales = " + locales);
        }
        catch (URISyntaxException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TranslationUtil.findLocales ", e);
        }
        catch (IOException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TranslationUtil.findLocales ", e);
        }
        catch (Throwable throwable)
        {
            UncaughtExcpetionsModel.getInstance().addException(throwable);
        }

        locales.remove(Locale.ENGLISH);

        ArrayList<Locale> orderedLocales = new ArrayList<Locale>();
        orderedLocales.add(Locale.ENGLISH);
        orderedLocales.addAll(locales);

        return Collections.unmodifiableList(orderedLocales);
    }


    @NotNull
    public static Locale createLocaleFromString(@NotNull String localeString)
    {
        Locale locale;
        int pos = -1;
        String temp = localeString.substring(pos + 1);
        pos = temp.indexOf('_');
        if (pos == -1)
        {
            locale = new Locale(temp, "", "");
            return locale;
        }

        String language = temp.substring(0, pos);
        temp = temp.substring(pos + 1);
        pos = temp.indexOf('_');
        if (pos == -1)
        {
            locale = new Locale(language, temp, "");
            return locale;
        }

        String country = temp.substring(0, pos);
        temp = temp.substring(pos + 1);

        locale = new Locale(language, country, temp);
        return locale;
    }
}
