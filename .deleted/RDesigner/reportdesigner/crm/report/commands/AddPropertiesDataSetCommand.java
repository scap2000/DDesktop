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
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertiesDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class AddPropertiesDataSetCommand extends AbstractCommand
{
    public AddPropertiesDataSetCommand()
    {
        super("AddPropertiesDataSetCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "AddPropertiesDataSetCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "AddPropertiesDataSetCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "AddPropertiesDataSetCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "AddPropertiesDataSetCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getPropertiesDataSetIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "AddPropertiesDataSetCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null && reportElements.length == 1 && reportElements[0] instanceof DataSetsReportElement)
        {
            event.getPresentation().setVisible(true);

            boolean ok = true;
            DataSetsReportElement dataSetsReportElement = (DataSetsReportElement) reportElements[0];
            for (ReportElement reportElement : dataSetsReportElement.getChildren())
            {
                if (reportElement instanceof PropertiesDataSetReportElement)
                {
                    ok = false;
                    break;
                }
            }

            event.getPresentation().setEnabled(ok);
        }
        else
        {
            event.getPresentation().setEnabled(false);
            event.getPresentation().setVisible(false);
        }
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null && reportElements.length == 1 && reportElements[0] instanceof DataSetsReportElement)
        {
            DataSetsReportElement dataSetsReportElement = (DataSetsReportElement) reportElements[0];
            for (ReportElement reportElement : dataSetsReportElement.getChildren())
            {
                if (reportElement instanceof PropertiesDataSetReportElement)
                {
                    return;
                }
            }

            PropertiesDataSetReportElement reportElement = ReportElementInfoFactory.getInstance().getPropertiesDataSetReportElementInfo().createReportElement();
            dataSetsReportElement.addChild(reportElement);
            if (reportElement.canConfigure())
            {
                reportElement.showConfigurationComponent(reportDialog, true);
            }
        }
    }
}
