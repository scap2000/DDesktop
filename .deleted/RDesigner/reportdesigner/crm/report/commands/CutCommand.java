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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionsElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.ReportGroups;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandManager;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class CutCommand extends AbstractCommand
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(CutCommand.class.getName());


    public CutCommand()
    {
        super("CutCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "CutCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "CutCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "CutCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "CutCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getCutIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "CutCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
    {
        ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null && reportElements.length > 0)
        {
            try
            {
                if (System.getSecurityManager() != null)
                {
                    System.getSecurityManager().checkSystemClipboardAccess();
                }
            }
            catch (SecurityException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "CutCommand.update ", e);
                event.getPresentation().setEnabled(false);
                return;
            }

            boolean ok = true;
            for (ReportElement reportElement : reportElements)
            {
                if (reportElement instanceof BandToplevelReportElement ||
                    reportElement instanceof Report ||
                    reportElement instanceof ReportGroups ||
                    reportElement instanceof ReportFunctionsElement ||
                    reportElement instanceof ReportGroup ||
                    reportElement instanceof DataSetsReportElement)
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

        final ReportElement[] reportElements = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        if (reportElements != null && reportElements.length > 0)
        {

            Undo undo = reportDialog.getUndo();

            undo.startTransaction(UndoConstants.CUT);

            try
            {
                final ReportElementList reportElementList = new ReportElementList(reportElements).clone();

                for (ReportElement reportElement : reportElements)
                {
                    ReportElement parent = reportElement.getParent();
                    if (parent != null)
                    {
                        parent.removeChild(reportElement);
                    }
                }

                ReportElementSelectionModel elementModel = reportDialog.getReportElementModel();
                if (elementModel != null)
                {
                    elementModel.clearSelection();
                }

                undo.endTransaction();

                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new Transferable()
                {
                    @NotNull
                    private DataFlavor dataFlavor = new DataFlavor(ReportElementList.class, TranslationManager.getInstance().getTranslation("R", "Clipboard.ReportElements.Title"));


                    @NotNull
                    public DataFlavor[] getTransferDataFlavors()
                    {
                        return new DataFlavor[]{dataFlavor};
                    }


                    public boolean isDataFlavorSupported(@NotNull DataFlavor flavor)
                    {
                        return ReportElementList.class.isAssignableFrom(flavor.getRepresentationClass());
                    }


                    @NotNull
                    public Object getTransferData(@NotNull DataFlavor flavor)
                    {
                        return reportElementList;
                    }
                }, new ClipboardOwner()
                {
                    public void lostOwnership(@NotNull Clipboard clipboard, @NotNull Transferable contents)
                    {
                        if (SwingUtilities.isEventDispatchThread())
                        {
                            CommandManager.dataProviderChanged();
                        }
                    }
                });
            }
            catch (CloneNotSupportedException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
        }

    }


}
