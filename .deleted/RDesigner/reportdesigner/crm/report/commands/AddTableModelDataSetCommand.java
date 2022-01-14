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
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataSetPlugin;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.TableModelDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class AddTableModelDataSetCommand extends AbstractCommand
{
    @NotNull
    private DataSetPlugin dataSetPlugin;


    public AddTableModelDataSetCommand(@NotNull DataSetPlugin dataSetPlugin)
    {
        super(dataSetPlugin.getID());
        this.dataSetPlugin = dataSetPlugin;
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "AddTableModelDataSetCommand.Text", dataSetPlugin.getLocalizedName()));

        ImageIcon smallIcon = dataSetPlugin.getSmallIcon();
        if (smallIcon != null)
        {
            getTemplatePresentation().setIcon(CommandSettings.SIZE_16, smallIcon);
        }
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null && reportElements.length == 1 && reportElements[0] instanceof DataSetsReportElement)
        {
            event.getPresentation().setVisible(true);
            event.getPresentation().setEnabled(true);
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
            final DataSetsReportElement dataSetsReportElement = (DataSetsReportElement) reportElements[0];
            final TableModelDataSetReportElement dataSet = dataSetPlugin.createDataSet(new HashMap<String, WizardData>());
            final ReportElementSelectionModel elementModel = reportDialog.getReportElementModel();
        	Runnable callback = new Runnable() {
        		public void run() {
                    dataSetsReportElement.addChild(dataSet);
                    elementModel.setSelection(Arrays.asList(dataSet));
        		}
        	};
            dataSet.setCallback(callback);
            if (elementModel != null)
            {
                if (dataSet.canConfigure())
                {
                	boolean ok = dataSet.showConfigurationComponent(reportDialog, true);
                }
                else
                {
                    dataSetsReportElement.addChild(dataSet);
                    elementModel.setSelection(Arrays.asList(dataSet));
                }
            }
        }
    }
}
