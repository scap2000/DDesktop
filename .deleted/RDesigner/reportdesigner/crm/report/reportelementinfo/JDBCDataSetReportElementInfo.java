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
package org.pentaho.reportdesigner.crm.report.reportelementinfo;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCDataSetReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

/**
 * User: Martin
 * Date: 31.01.2006
 * Time: 11:38:03
 */
public class JDBCDataSetReportElementInfo extends ReportElementInfo
{
    public JDBCDataSetReportElementInfo()
    {
        super(IconLoader.getInstance().getDataSetsIcon(), TranslationManager.getInstance().getTranslation("R", "ReportElementInfo.JDBCDataSetReportElementInfo"));
    }


    @NotNull
    public JDBCDataSetReportElement createReportElement()
    {
        JDBCDataSetReportElement jdbcDataSetReportElement = new JDBCDataSetReportElement();
        return jdbcDataSetReportElement;
    }
}
