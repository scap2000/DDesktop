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
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionsElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroups;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.geom.Point2D;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class MoveLeftOneCommand extends AbstractCommand
{
    public MoveLeftOneCommand()
    {
        super("MoveLeftOneCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "MoveLeftOneCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "MoveLeftOneCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "MoveLeftOneCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "MoveLeftOneCommand.Text"));

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "MoveLeftOneCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null && reportElements.length > 0)
        {
            boolean ok = true;
            for (ReportElement reportElement : reportElements)
            {
                if (reportElement instanceof BandToplevelReportElement ||
                    reportElement instanceof Report ||
                    reportElement instanceof ReportGroups ||
                    reportElement instanceof ReportFunctionsElement)
                {
                    ok = false;
                }
            }
            event.getPresentation().setEnabled(ok);
        }
        else
        {
            event.getPresentation().setEnabled(false);
        }
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        reportDialog.getUndo().startTransaction(UndoConstants.MOVE_LEFT);
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null)
        {
            for (ReportElement reportElement : reportElements)
            {
                double x = reportElement.getPosition().x - 1;
                double y = reportElement.getPosition().y;
                reportElement.setPosition(new Point2D.Double(x, y));
            }
        }
        reportDialog.getUndo().endTransaction();
    }
}
