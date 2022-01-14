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
package org.pentaho.reportdesigner.crm.report.commands.align;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.commands.CommandKeys;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.geom.Point2D;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class AlignMiddleCommand extends AbstractPositioningCommand
{
    public AlignMiddleCommand()
    {
        super("AlignMiddleCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "AlignMiddleCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "AlignMiddleCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "AlignMiddleCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "AlignMiddleCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getAlignMiddleIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "AlignMiddleCommand.Accelerator")));
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        reportDialog.getUndo().startTransaction(UndoConstants.ALIGN_MIDDLE);
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null)
        {
            //find minimum Y & maximum Y
            double topmostY = Double.MAX_VALUE;
            double bottommostY = -Double.MAX_VALUE;
            for (ReportElement reportElement : reportElements)
            {
                topmostY = Math.min(topmostY, reportElement.getPosition().getY());
                bottommostY = Math.max(bottommostY, reportElement.getPosition().getY() + reportElement.getMinimumSize().getHeight());
            }

            double centerY = (topmostY + bottommostY) / 2;

            for (ReportElement reportElement : reportElements)
            {
                reportElement.setPosition(new Point2D.Double(reportElement.getPosition().getX(), centerY - reportElement.getMinimumSize().getHeight() / 2));
            }
        }
        reportDialog.getUndo().endTransaction();
    }
}
