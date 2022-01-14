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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 24.02.2006
 * Time: 15:04:11
 */
public class LocaleDataLoader
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(LocaleDataLoader.class.getName());

    @SuppressWarnings({"RedundantFieldInitialization"})
    private static transient boolean loadingLocaleData = false;

    @NotNull
    private static final ArrayList<JComboBox> comboBoxes = new ArrayList<JComboBox>();
    @NotNull
    private static transient Locale[] availableLocales;


    private LocaleDataLoader()
    {
    }


    public static void fillLocales(@NotNull final JComboBox jComboBox)
    {
        if (availableLocales == null && !loadingLocaleData)
        {
            loadingLocaleData = true;

            comboBoxes.add(jComboBox);

            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    long l1 = System.currentTimeMillis();
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.run start loading locale data");
                    Locale[] availableLocales = Locale.getAvailableLocales();
                    Arrays.sort(availableLocales, new Comparator<Locale>()
                    {
                        public int compare(@NotNull Locale o1, @NotNull Locale o2)
                        {
                            int i = o1.getLanguage().compareTo(o2.getLanguage());
                            if (i == 0)
                            {
                                int i2 = o1.getCountry().compareTo(o2.getCountry());
                                if (i2 == 0)
                                {
                                    return o1.getVariant().compareTo(o2.getVariant());
                                }
                                return i2;
                            }
                            return i;
                        }
                    });
                    LocaleDataLoader.availableLocales = availableLocales;
                    loadingLocaleData = false;

                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            for (JComboBox comboBox : comboBoxes)
                            {
                                final DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(LocaleDataLoader.availableLocales);
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
        else if (availableLocales == null && loadingLocaleData)
        {
            comboBoxes.add(jComboBox);
        }
        else
        {
            jComboBox.setModel(new DefaultComboBoxModel(availableLocales));
        }
    }


    public static boolean hasLocales()
    {
        return availableLocales != null;
    }


}
