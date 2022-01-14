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
package org.pentaho.reportdesigner.crm.report.datasetplugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.TableModelDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.wizard.AbstractWizardPage;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 27.02.2006
 * Time: 10:00:41
 */
public class MultiDataSetPlugin implements DataSetPlugin
{

    public MultiDataSetPlugin()
    {
    }


    @NotNull
    public String getID()
    {
        return "MultiDataSetPlugin";
    }


    @NotNull
    public String getLocalizedName()
    {
        return TranslationManager.getInstance().getTranslation("R", "MultiDataSetPlugin.Name");
    }


    @NotNull
    public String getLocalizedDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "MultiDataSetPlugin.Description");
    }


    @NotNull
    public ImageIcon getSmallIcon()
    {
        return IconLoader.getInstance().getDataSetsIcon();
    }


    public boolean isWizardable()
    {
        return false;
    }


    public void initWizardPages()
    {
    }


    @Nullable
    public AbstractWizardPage getFirstWizardPage()
    {
        return null;
    }


    @Nullable
    public AbstractWizardPage getLastWizardPage()
    {
        return null;
    }


    @NotNull
    public WizardData[] getInitialWizardDatas()
    {
        return WizardData.EMPTY_ARRAY;
    }


    @NotNull
    public TableModelDataSetReportElement createDataSet(@NotNull HashMap<String, WizardData> wizardDatas)
    {
        return new MultiDataSetReportElement();
    }


    public boolean canRead(@Nullable String classname)
    {
        return "org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement".equals(classname);
    }


    @NotNull
    public ReportElement createEmptyInstance(@NotNull String className)
    {
        return new MultiDataSetReportElement();
    }
}
