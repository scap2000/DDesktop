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
package org.pentaho.reportdesigner.crm.report.model.dataset;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataFetchException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;

import javax.swing.table.TableModel;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 10:12:06
 */
public abstract class TableModelDataSetReportElement extends DataSetReportElement
{
    public TableModelDataSetReportElement()
    {
    }


    public abstract boolean canCreateReportOnServer();


    public abstract void createReportOnServer(@NotNull String jFreeReportDefinition);


    public abstract boolean canFetchPreviewDataTableModel();


    public abstract boolean canFetchRealDataTableModel();


    @NotNull
    public abstract TableModel fetchPreviewDataTableModel() throws DataFetchException;


    @NotNull
    public abstract TableModel fetchRealDataTableModel() throws DataFetchException;


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
    }
}
