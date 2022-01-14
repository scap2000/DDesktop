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
import org.pentaho.reportdesigner.crm.report.zoom.ZoomModel;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.components.docking.IconCreator;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class ZoomCommand extends AbstractCommand
{
    private int percentage;


    public ZoomCommand(int percentage)
    {
        super("ZoomCommand" + percentage);
        this.percentage = percentage;
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "ZoomCommand.Text", Integer.valueOf(percentage)));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "ZoomCommand.Description", Integer.valueOf(percentage)));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "ZoomCommand.Text", Integer.valueOf(percentage)));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "ZoomCommand.Text", Integer.valueOf(percentage)));

        if (percentage == 50)
        {
            getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconCreator.createOverlayImageIcon(IconLoader.getInstance().getZoomIcon(),
                                                                                                          IconLoader.getInstance().getZoomOverlay50Icon()));
        }
        else if (percentage == 100)
        {
            getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconCreator.createOverlayImageIcon(IconLoader.getInstance().getZoomIcon(),
                                                                                                          IconLoader.getInstance().getZoomOverlay100Icon()));
        }
        else if (percentage == 200)
        {
            getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconCreator.createOverlayImageIcon(IconLoader.getInstance().getZoomIcon(),
                                                                                                          IconLoader.getInstance().getZoomOverlay200Icon()));
        }
        else if (percentage == 400)
        {
            getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconCreator.createOverlayImageIcon(IconLoader.getInstance().getZoomIcon(),
                                                                                                          IconLoader.getInstance().getZoomOverlay400Icon()));
        }
        else
        {
            getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getZoomIcon());
        }
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        event.getPresentation().setEnabled(reportDialog.getZoomModel() != null);
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        ZoomModel zoomModel = reportDialog.getZoomModel();
        if (zoomModel != null)
        {
            zoomModel.setZoomFactor(percentage * 10);
        }
    }
}
