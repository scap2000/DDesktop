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
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;

import javax.swing.*;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 31.01.2006
 * Time: 08:19:51
 */
public abstract class DataSetReportElement extends ReportElement
{
	
	Runnable callback;
	
    protected DataSetReportElement()
    {
    }


    public boolean showConfigurationComponent(@NotNull ReportDialog parent, boolean firsttime)
    {
        throw new RuntimeException("No configuration available for this DataSetReportElement");
    }


    public abstract boolean canConfigure();


    @NotNull
    public abstract JComponent getInfoComponent();

    @NotNull
    public abstract String getQueryName();

    @NotNull
    public abstract String getShortSummary();


    @NotNull
    public abstract HashSet<String> getDefinedFields();


	public Runnable getCallback() {
		return callback;
	}


	public void setCallback(Runnable callback) {
		this.callback = callback;
	}
}
