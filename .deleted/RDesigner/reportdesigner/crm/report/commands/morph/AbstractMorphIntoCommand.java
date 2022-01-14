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
package org.pentaho.reportdesigner.crm.report.commands.morph;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.commands.CommandKeys;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;

import java.util.ArrayList;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public abstract class AbstractMorphIntoCommand extends AbstractCommand
{
    public AbstractMorphIntoCommand(@NotNull @NonNls String name)
    {
        super(name);
    }


    @NotNull
    public abstract Class getTargetClass();


    public void update(@NotNull CommandEvent event)
    {
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null && reportElements.length > 0)
        {
            for (ReportElement reportElement : reportElements)
            {
                if (!MorphingHelper.getInstance().hasMorpher(reportElement.getClass(), getTargetClass()))
                {
                    event.getPresentation().setEnabled(false);
                    return;
                }
            }
            event.getPresentation().setEnabled(true);
        }
        else
        {
            event.getPresentation().setEnabled(false);
        }
    }


    public void execute(@NotNull CommandEvent event)
    {
        ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);

        reportDialog.getUndo().startTransaction(UndoConstants.MORPH_INTO_TEXTFIELD);
        ReportElementSelectionModel elementModel = reportDialog.getReportElementModel();

        if (elementModel != null)
        {
            elementModel.clearSelection();

            ArrayList<ReportElement> morphedElements = new ArrayList<ReportElement>();

            if (reportElements != null)
            {
                for (ReportElement reportElement : reportElements)
                {
                    MorphingHelper.ReportElementMorpher morpher = MorphingHelper.getInstance().getMorpher(reportElement.getClass(), getTargetClass());
                    if (morpher != null)
                    {
                        //noinspection unchecked
                        ReportElement morphedReportElement = morpher.getMorphedReportElement(reportElement);
                        morphedElements.add(morphedReportElement);
                        ReportElement parent = reportElement.getParent();
                        if (parent != null)
                        {
                            int index = parent.getChildren().indexOf(reportElement);
                            if (index != -1)
                            {
                                parent.removeChild(reportElement);
                                parent.insertChild(morphedReportElement, index);
                            }
                        }
                    }
                }
            }
            elementModel.setSelection(morphedElements);
            reportDialog.getUndo().endTransaction();
        }
    }
}
