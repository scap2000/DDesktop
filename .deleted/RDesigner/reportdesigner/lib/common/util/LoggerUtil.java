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
package org.pentaho.reportdesigner.lib.common.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 06:49:01
 */
@SuppressWarnings({"HardCodedStringLiteral"})
public class LoggerUtil
{
    private LoggerUtil()
    {
    }


    public static void initLogger()
    {
        Logger gridvisionLogger = Logger.getLogger("org.pentaho.reportdesigner");
        Logger pluginLogger = Logger.getLogger("org.pentaho.reportdesigner.lib.client.plugin");

        Handler[] handlers = gridvisionLogger.getHandlers();
        for (Handler handler : handlers)
        {
            gridvisionLogger.removeHandler(handler);
            pluginLogger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new OneLineFormatter());
        consoleHandler.setLevel(Level.ALL);

        BufferedHandler.getBufferedHandler().setFormatter(new OneLineFormatter());
        BufferedHandler.getBufferedHandler().setLevel(Level.ALL);

        gridvisionLogger.addHandler(consoleHandler);
        gridvisionLogger.addHandler(BufferedHandler.getBufferedHandler());
        pluginLogger.addHandler(consoleHandler);
        pluginLogger.addHandler(BufferedHandler.getBufferedHandler());

        Logger.getLogger("org.pentaho.reportdesigner").setLevel(Level.WARNING);
        Logger.getLogger("org.pentaho.reportdesigner.lib.client.plugin").setLevel(Level.WARNING);
    }

}
