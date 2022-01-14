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

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.common.util.LoggerUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * User: Martin
 * Date: 03.06.2005
 * Time: 13:38:00
 */
@SuppressWarnings({"ALL"})
public class PluginTester
{

    public static void main(@NotNull String[] args) throws MalformedURLException
    {
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new File("C:\\Daten\\07_Projekte\\ReportDesignerSamplePlugin\\dist\\ReportDesignerSamplePlugin.jar").toURI().toURL()});
        URL resource = urlClassLoader.getResource("/res/SamplePluginIcon.png");

        LoggerUtil.initLogger();
        Logger.getLogger("ch.gridvision").setLevel(Level.ALL);//NON-NLS
        Logger.getLogger("org.pentaho.reportdesigner.lib.client.plugin").setLevel(Level.ALL);//NON-NLS

        PluginWatcher pluginWatcher = new PluginWatcher(null, new File("C:\\Daten\\07_Projekte\\ReportDesignerSamplePlugin\\dist"), 100000);


        Thread t = new Thread()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(1000000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        t.setDaemon(false);
        t.start();
    }
}
