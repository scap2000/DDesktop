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
import java.util.Arrays;
import java.util.Comparator;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class DistributeMiddleCommand extends AbstractPositioningCommand
{
    public DistributeMiddleCommand()
    {
        super("DistributeMiddleCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "DistributeMiddleCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "DistributeMiddleCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "DistributeMiddleCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "DistributeMiddleCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getDistributeMiddleIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "DistributeMiddleCommand.Accelerator")));
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        reportDialog.getUndo().startTransaction(UndoConstants.DISTRIBUTE_MIDDLE);
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);

        if (reportElements != null && reportElements.length > 2)
        {
            //sort array by Y
            Arrays.sort(reportElements, new Comparator<ReportElement>()
            {
                public int compare(@NotNull ReportElement o1, @NotNull ReportElement o2)
                {
                    return Double.compare(o1.getPosition().getY() + o1.getMinimumSize().getHeight() / 2, o2.getPosition().getY() + o2.getMinimumSize().getHeight() / 2);
                }
            });

            double height = (reportElements[reportElements.length - 1].getPosition().getY() + reportElements[reportElements.length - 1].getMinimumSize().getHeight() / 2) - (reportElements[0].getPosition().getY() + reportElements[0].getMinimumSize().getHeight() / 2);
            double incr = height / (reportElements.length - 1);
            double currentY = reportElements[0].getPosition().getY() + reportElements[0].getMinimumSize().getHeight() / 2;
            currentY += incr;//start from second element
            for (int i = 1; i < reportElements.length - 1; i++)
            {
                ReportElement reportElement = reportElements[i];
                reportElement.setPosition(new Point2D.Double(reportElement.getPosition().getX(), currentY - reportElement.getMinimumSize().getHeight() / 2));
                currentY += incr;
            }
        }
        reportDialog.getUndo().endTransaction();
    }
}
