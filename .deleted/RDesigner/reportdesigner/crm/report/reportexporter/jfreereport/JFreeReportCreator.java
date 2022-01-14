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
package org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TableDataFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;

import javax.swing.table.TableModel;

/**
 * User: Martin
 * Date: 28.10.2005
 * Time: 08:37:52
 */
public class JFreeReportCreator
{

    public JFreeReportCreator()
    {
    }


    public void createReport(@NotNull Report report, @NotNull TableModel tableModel) throws ReportCreationException
    {
        JFreeReportVisitor reportVisitor = new JFreeReportVisitor();
        report.accept(null, reportVisitor);
        JFreeReport jFreeReport = reportVisitor.getJFreeReport();

        if (jFreeReport.getDataFactory() instanceof NullDataFactory)
        {
            jFreeReport.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, tableModel));
        }

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(jFreeReport);
        preview.pack();
        preview.setVisible(true);
    }


}
