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

import org.jetbrains.annotations.NotNull;
import org.jfree.report.JFreeReportBoot;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import java.awt.*;

/**
 * User: Martin
 * Date: 28.02.2006
 * Time: 09:36:04
 */
public class JFreeReportBootingHelper
{
    private JFreeReportBootingHelper()
    {
    }


    public static void boot(@NotNull Component parent)
    {
        if (!JFreeReportBoot.getInstance().isBootDone())
        {
            final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(parent, TranslationManager.getInstance().getTranslation("R", "ReportDialog.BootingReportingEngine.Title"), "");

            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        JFreeReportBoot.getInstance().getEditableConfig().setConfigProperty("org.jfree.base.LogLevel", "Error");//NON-NLS
                        JFreeReportBoot.getInstance().getEditableConfig().setConfigProperty("org.jfree.report.NoPrinterAvailable", Boolean.TRUE.toString());
                        JFreeReportBoot.getInstance().start();
                    }
                    catch (Throwable e)
                    {
                        UncaughtExcpetionsModel.getInstance().addException(e);
                    }

                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            progressDialog.dispose();
                        }
                    });
                }
            });

            t.setPriority(Thread.NORM_PRIORITY - 1);
            t.setDaemon(true);
            t.start();
            if (t.isAlive())
            {
                progressDialog.setVisible(true);
            }
            else
            {
                progressDialog.dispose();
            }
        }
    }
}
