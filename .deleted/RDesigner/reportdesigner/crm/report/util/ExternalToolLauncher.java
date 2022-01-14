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
import org.pentaho.reportdesigner.crm.report.FileType;
import org.pentaho.reportdesigner.crm.report.settings.ApplicationSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.BrowserLauncher;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 01.03.2006
 * Time: 17:55:15
 */
public class ExternalToolLauncher
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ExternalToolLauncher.class.getName());


    private ExternalToolLauncher()
    {
    }


    public static void openURL(@NotNull String url) throws IOException
    {
        //noinspection ConstantConditions
        if (url == null)
        {
            throw new IllegalArgumentException("url must not be null");
        }

        if (ApplicationSettings.getInstance().isUseDefaultBrowser())
        {
            BrowserLauncher.openURL(url);
        }
        else
        {
            BrowserLauncher.execute(ApplicationSettings.getInstance().getCustomBrowserExecutable(), ApplicationSettings.getInstance().getCustomBrowserParameters(), url);
        }
    }


    public static void openPDF(@NotNull File file) throws IOException
    {
        //noinspection ConstantConditions
        if (file == null)
        {
            throw new IllegalArgumentException("file must not be null");
        }

        if (ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultPDFViewer())
        {
            BrowserLauncher.openURL(file.toURI().toURL().toString());
        }
        else
        {
            BrowserLauncher.execute(ApplicationSettings.getInstance().getExternalToolSettings().getCustomPDFViewerExecutable(), ApplicationSettings.getInstance().getExternalToolSettings().getCustomPDFViewerParameters(), file.getCanonicalPath());
        }
    }


    public static void openXLS(@NotNull File file) throws IOException
    {
        //noinspection ConstantConditions
        if (file == null)
        {
            throw new IllegalArgumentException("file must not be null");
        }

        if (ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultXLSViewer())
        {
            BrowserLauncher.openURL(file.toURI().toURL().toString());
        }
        else
        {
            BrowserLauncher.execute(ApplicationSettings.getInstance().getExternalToolSettings().getCustomXLSViewerExecutable(), ApplicationSettings.getInstance().getExternalToolSettings().getCustomXLSViewerParameters(), file.getCanonicalPath());
        }
    }


    public static void openRTF(@NotNull File file) throws IOException
    {
        //noinspection ConstantConditions
        if (file == null)
        {
            throw new IllegalArgumentException("file must not be null");
        }

        if (ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultRTFViewer())
        {
            BrowserLauncher.openURL(file.toURI().toURL().toString());
        }
        else
        {
            BrowserLauncher.execute(ApplicationSettings.getInstance().getExternalToolSettings().getCustomRTFViewerExecutable(), ApplicationSettings.getInstance().getExternalToolSettings().getCustomRTFViewerParameters(), file.getCanonicalPath());
        }
    }


    public static void openCSV(@NotNull File file) throws IOException
    {
        //noinspection ConstantConditions
        if (file == null)
        {
            throw new IllegalArgumentException("file must not be null");
        }

        if (ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultCSVViewer())
        {
            BrowserLauncher.openURL(file.toURI().toURL().toString());
        }
        else
        {
            BrowserLauncher.execute(ApplicationSettings.getInstance().getExternalToolSettings().getCustomCSVViewerExecutable(), ApplicationSettings.getInstance().getExternalToolSettings().getCustomCSVViewerParameters(), file.getCanonicalPath());
        }
    }


    public static void openXML(@NotNull File file) throws IOException
    {
        //noinspection ConstantConditions
        if (file == null)
        {
            throw new IllegalArgumentException("file must not be null");
        }

        if (ApplicationSettings.getInstance().getExternalToolSettings().isUseDefaultXMLViewer())
        {
            BrowserLauncher.openURL(file.toURI().toURL().toString());
        }
        else
        {
            BrowserLauncher.execute(ApplicationSettings.getInstance().getExternalToolSettings().getCustomXMLViewerExecutable(), ApplicationSettings.getInstance().getExternalToolSettings().getCustomXMLViewerParameters(), file.getCanonicalPath());
        }
    }


    public static void openInOwnThread(@NotNull final Component component, @NotNull final FileType fileType, @NotNull final File file)
    {
        //noinspection ConstantConditions
        if (component == null)
        {
            throw new IllegalArgumentException("component must not be null");
        }
        //noinspection ConstantConditions
        if (fileType == null)
        {
            throw new IllegalArgumentException("fileType must not be null");
        }
        //noinspection ConstantConditions
        if (file == null)
        {
            throw new IllegalArgumentException("file must not be null");
        }

        Thread t = new Thread()
        {
            public void run()
            {
                try
                {
                    if (fileType == FileType.PDF)
                    {
                        ExternalToolLauncher.openPDF(file);
                    }
                    else if (fileType == FileType.HTML)
                    {
                        ExternalToolLauncher.openURL(file.toURI().toURL().toString());
                    }
                    else if (fileType == FileType.RTF)
                    {
                        ExternalToolLauncher.openRTF(file);
                    }
                    else if (fileType == FileType.XLS)
                    {
                        ExternalToolLauncher.openXLS(file);
                    }
                    else if (fileType == FileType.CSV)
                    {
                        ExternalToolLauncher.openCSV(file);
                    }
                    else if (fileType == FileType.XML)
                    {
                        ExternalToolLauncher.openXML(file);
                    }
                    else
                    {
                        //fallback solution if something unexpected happens (someone adding fields to the enum without checking usages)
                        if (LOG.isLoggable(Level.SEVERE)) LOG.log(Level.SEVERE, "Unhandled FileType: " + fileType);
                        ExternalToolLauncher.openURL(file.toURI().toURL().toString());
                    }
                }
                catch (IOException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExternalToolLauncher.run ", e);
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            JOptionPane.showMessageDialog(component,
                                                          TranslationManager.getInstance().getTranslation("R", "ExternalToolLauncher.Error.Message"),
                                                          TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                                          JOptionPane.ERROR_MESSAGE);
                        }
                    });

                }
            }
        };
        t.setDaemon(true);
        t.start();
    }
}
