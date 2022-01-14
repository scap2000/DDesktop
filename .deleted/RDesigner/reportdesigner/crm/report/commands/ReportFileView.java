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
package org.pentaho.reportdesigner.crm.report.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;

/**
 * User: Martin
 * Date: 09.02.2006
 * Time: 12:59:44
 */
public class ReportFileView extends FileView
{

    @Nullable
    public Icon getIcon(@NotNull File f)
    {
        if (!f.isDirectory() && f.getName().toLowerCase().endsWith(ReportDialogConstants.REPORT_FILE_ENDING))
        {
            return IconLoader.getInstance().getReportReportElementIcon();
        }

        return null;
    }

}
