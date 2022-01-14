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
public class DistributeGapsHorizontalCommand extends AbstractPositioningCommand
{
    public DistributeGapsHorizontalCommand()
    {
        super("DistributeGapsHorizontalCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "DistributeGapsHorizontalCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "DistributeGapsHorizontalCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "DistributeGapsHorizontalCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "DistributeGapsHorizontalCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getDistributeGapsHorizontalIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "DistributeGapsHorizontalCommand.Accelerator")));
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        reportDialog.getUndo().startTransaction(UndoConstants.DISTRIBUTE_GAPS_HORIZONTAL);
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);

        if (reportElements != null && reportElements.length > 2)
        {
            //sort array by X
            Arrays.sort(reportElements, new Comparator<ReportElement>()
            {
                public int compare(@NotNull ReportElement o1, @NotNull ReportElement o2)
                {
                    return Double.compare(o1.getPosition().getX(), o2.getPosition().getX());
                }
            });

            //sum total width of all elements
            double totalWidth = 0;
            for (ReportElement reportElement : reportElements)
            {
                totalWidth += reportElement.getMinimumSize().getWidth();
            }

            //find minX
            double leftmostX = Double.MAX_VALUE;
            for (ReportElement reportElement : reportElements)
            {
                leftmostX = Math.min(leftmostX, reportElement.getPosition().getX());
            }
            //findMaxX
            double rigthmostX = -Double.MAX_VALUE;
            for (ReportElement reportElement : reportElements)
            {
                rigthmostX = Math.max(rigthmostX, reportElement.getPosition().getX() + reportElement.getMinimumSize().getWidth());
            }

            double gap = (rigthmostX - leftmostX - totalWidth) / (reportElements.length - 1);//totalWidth/numberOfGaps

            double currentX = leftmostX;
            for (ReportElement reportElement : reportElements)
            {
                reportElement.setPosition(new Point2D.Double(currentX, reportElement.getPosition().getY()));
                currentX += reportElement.getMinimumSize().getWidth() + gap;
            }
        }
        reportDialog.getUndo().endTransaction();
    }
}
