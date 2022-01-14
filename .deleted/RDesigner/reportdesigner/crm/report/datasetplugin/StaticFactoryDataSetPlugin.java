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
import org.pentaho.reportdesigner.crm.report.datasetplugin.staticfactory.StaticFactoryDataSetReportElement;
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
public class StaticFactoryDataSetPlugin implements DataSetPlugin
{

    public StaticFactoryDataSetPlugin()
    {
    }


    @NotNull
    public String getID()
    {
        return "StaticFactoryDataSetPlugin";
    }


    @NotNull
    public String getLocalizedName()
    {
        return TranslationManager.getInstance().getTranslation("R", "StaticFactoryDataSetPlugin.Name");
    }


    @NotNull
    public String getLocalizedDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "StaticFactoryDataSetPlugin.Description");
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
        return new StaticFactoryDataSetReportElement();
    }


    public boolean canRead(@Nullable String classname)
    {
        return "org.pentaho.reportdesigner.crm.report.datasetplugin.staticfactory.StaticFactoryDataSetReportElement".equals(classname);
    }


    @NotNull
    public ReportElement createEmptyInstance(@NotNull String className)
    {
        return new StaticFactoryDataSetReportElement();
    }
}
