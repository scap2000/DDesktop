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
package org.pentaho.reportdesigner.crm.report.properties;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 24.02.2006
 * Time: 15:04:11
 */
public class TimeZoneDataLoader
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(TimeZoneDataLoader.class.getName());

    @SuppressWarnings({"RedundantFieldInitialization"})
    private static transient boolean loadingTimeZoneData = false;

    @NotNull
    private static final ArrayList<JComboBox> comboBoxes = new ArrayList<JComboBox>();
    @NotNull
    private static transient TimeZone[] availableTimeZones;


    private TimeZoneDataLoader()
    {
    }


    public static void fillLocales(@NotNull final JComboBox jComboBox)
    {
        if (TimeZoneDataLoader.availableTimeZones == null && !TimeZoneDataLoader.loadingTimeZoneData)
        {
            TimeZoneDataLoader.loadingTimeZoneData = true;

            TimeZoneDataLoader.comboBoxes.add(jComboBox);

            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    long l1 = System.currentTimeMillis();
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.run start loading locale data");

                    String[] availableIDs = TimeZone.getAvailableIDs();
                    TimeZone[] availableTimeZones = new TimeZone[availableIDs.length];
                    for (int i = 0; i < availableIDs.length; i++)
                    {
                        try
                        {
                            availableTimeZones[i] = TimeZone.getTimeZone(availableIDs[i]);
                        }
                        catch (Exception e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TimeZoneDataLoader.run ", e);
                        }
                    }

                    TimeZoneDataLoader.availableTimeZones = availableTimeZones;
                    TimeZoneDataLoader.loadingTimeZoneData = false;

                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            for (JComboBox comboBox : TimeZoneDataLoader.comboBoxes)
                            {
                                final DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(TimeZoneDataLoader.availableTimeZones);
                                comboBox.setModel(defaultComboBoxModel);
                            }
                        }
                    });
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.run System.currentTimeMillis()-l1 = " + (System.currentTimeMillis() - l1) + " ms");
                }
            });
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        }
        else if (TimeZoneDataLoader.availableTimeZones == null && TimeZoneDataLoader.loadingTimeZoneData)
        {
            TimeZoneDataLoader.comboBoxes.add(jComboBox);
        }
        else
        {
            jComboBox.setModel(new DefaultComboBoxModel(TimeZoneDataLoader.availableTimeZones));
        }
    }


    public static boolean hasTimeZones()
    {
        return availableTimeZones != null;
    }


}
