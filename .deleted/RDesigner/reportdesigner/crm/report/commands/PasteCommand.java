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
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.ReportPanel;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionsElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.ReportGroups;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;
import org.pentaho.reportdesigner.lib.client.commands.CommandSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class PasteCommand extends AbstractCommand
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(PasteCommand.class.getName());


    public PasteCommand()
    {
        super("PasteCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "PasteCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "PasteCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "PasteCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "PasteCommand.Text"));

        getTemplatePresentation().setIcon(CommandSettings.SIZE_16, IconLoader.getInstance().getPasteIcon());

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PasteCommand.Accelerator")));
    }


    public void update(@NotNull CommandEvent event)
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
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PasteCommand.update ", e);
            event.getPresentation().setEnabled(false);
            return;
        }

        boolean ok = false;

        ReportPanel reportPanel = (ReportPanel) event.getDataContext().getData(CommandKeys.KEY_REPORT_PANEL);
        ReportElementSelectionModel reportElementSelectionModel = (ReportElementSelectionModel) event.getDataContext().getData(CommandKeys.KEY_REPORT_ELEMENT_MODEL);

        DataFlavor desirableDataFlavor = new DataFlavor(ReportElementList.class, DataFlavor.javaSerializedObjectMimeType);

        if (reportPanel != null || (reportElementSelectionModel != null && reportElementSelectionModel.getSelectedElementInfos().size() == 1 && reportElementSelectionModel.getSelectedElementInfos().get(0) instanceof BandToplevelReportElement))
        {
            boolean available = Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(desirableDataFlavor);

            if (available)
            {
                ok = true;

                Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
                try
                {
                    Object transferData = contents.getTransferData(desirableDataFlavor);
                    if (transferData instanceof ReportElementList)
                    {
                        ReportElementList reportElementList = (ReportElementList) transferData;
                        for (int j = 0; j < reportElementList.getReportElements().size(); j++)
                        {
                            ReportElement reportElement = reportElementList.getReportElements().get(j);
                            if (reportElement instanceof DataSetReportElement ||
                                reportElement instanceof DataSetsReportElement ||
                                reportElement instanceof ReportFunctionElement ||
                                reportElement instanceof ReportFunctionsElement ||
                                reportElement instanceof BandToplevelReportElement ||
                                reportElement instanceof ReportGroups ||
                                reportElement instanceof ReportGroup ||
                                reportElement instanceof Report)
                            {
                                ok = false;
                                break;
                            }
                        }
                    }
                }
                catch (UnsupportedFlavorException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PasteCommand.update ", e);
                    ok = false;
                }
                catch (IOException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PasteCommand.update ", e);
                    ok = false;
                }
            }

            event.getPresentation().setEnabled(ok);
        }
        else if (reportPanel != null || (reportElementSelectionModel != null && reportElementSelectionModel.getSelectedElementInfos().size() == 1 && reportElementSelectionModel.getSelectedElementInfos().get(0) instanceof DataSetsReportElement))
        {
            boolean available = Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(desirableDataFlavor);

            if (available)
            {
                ok = true;

                Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
                try
                {
                    Object transferData = contents.getTransferData(desirableDataFlavor);
                    if (transferData instanceof ReportElementList)
                    {
                        ReportElementList reportElementList = (ReportElementList) transferData;
                        for (int j = 0; j < reportElementList.getReportElements().size(); j++)
                        {
                            ReportElement reportElement = reportElementList.getReportElements().get(j);
                            if (!(reportElement instanceof DataSetReportElement))
                            {
                                ok = false;
                                break;
                            }
                        }
                    }
                }
                catch (UnsupportedFlavorException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PasteCommand.update ", e);
                    ok = false;
                }
                catch (IOException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PasteCommand.update ", e);
                    ok = false;
                }
            }

            event.getPresentation().setEnabled(ok);
        }
        else if (reportPanel != null || (reportElementSelectionModel != null && reportElementSelectionModel.getSelectedElementInfos().size() == 1 && reportElementSelectionModel.getSelectedElementInfos().get(0) instanceof ReportFunctionsElement))
        {
            boolean available = Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(desirableDataFlavor);

            if (available)
            {
                ok = true;

                Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
                try
                {
                    Object transferData = contents.getTransferData(desirableDataFlavor);
                    if (transferData instanceof ReportElementList)
                    {
                        ReportElementList reportElementList = (ReportElementList) transferData;
                        for (int j = 0; j < reportElementList.getReportElements().size(); j++)
                        {
                            ReportElement reportElement = reportElementList.getReportElements().get(j);
                            if (!(reportElement instanceof ReportFunctionElement))
                            {
                                ok = false;
                                break;
                            }
                        }
                    }
                }
                catch (UnsupportedFlavorException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PasteCommand.update ", e);
                    ok = false;
                }
                catch (IOException e)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PasteCommand.update ", e);
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

        try
        {
            ReportPanel reportPanel = (ReportPanel) event.getDataContext().getData(CommandKeys.KEY_REPORT_PANEL);
            ReportElementSelectionModel reportElementSelectionModel = (ReportElementSelectionModel) event.getDataContext().getData(CommandKeys.KEY_REPORT_ELEMENT_MODEL);

            if (reportPanel != null || (reportElementSelectionModel != null && reportElementSelectionModel.getSelectedElementInfos().size() == 1) && reportElementSelectionModel.getSelectedElementInfos().get(0) instanceof BandToplevelReportElement)
            {
                pasteRegularReportElement(event, reportDialog, reportPanel, reportElementSelectionModel);
            }
            else if ((reportElementSelectionModel != null && reportElementSelectionModel.getSelectedElementInfos().size() == 1) && reportElementSelectionModel.getSelectedElementInfos().get(0) instanceof DataSetsReportElement)
            {
                pasteDataSetReportElement(event, reportDialog, reportPanel, reportElementSelectionModel);
            }
            else if ((reportElementSelectionModel != null && reportElementSelectionModel.getSelectedElementInfos().size() == 1) && reportElementSelectionModel.getSelectedElementInfos().get(0) instanceof ReportFunctionsElement)
            {
                pasteReportFunctionElement(event, reportDialog, reportPanel, reportElementSelectionModel);
            }
        }
        catch (UnsupportedFlavorException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        catch (IOException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }

    }


    private void pasteReportFunctionElement(@NotNull CommandEvent event, @NotNull ReportDialog reportDialog, @Nullable ReportPanel reportPanel, @Nullable ReportElementSelectionModel reportElementSelectionModel) throws UnsupportedFlavorException, IOException
    {
        ReportElement[] res = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        DataFlavor desirableDataFlavor = new DataFlavor(ReportElementList.class, DataFlavor.javaSerializedObjectMimeType);
        boolean available = Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(desirableDataFlavor);
        if (available)
        {
            try
            {
                reportDialog.getUndo().startTransaction(UndoConstants.PASTE);
                ReportElementList reportElements = ((ReportElementList) Toolkit.getDefaultToolkit().getSystemClipboard().getData(desirableDataFlavor)).clone();

                for (int j = 0; j < reportElements.getReportElements().size(); j++)
                {
                    ReportElement reportElement = reportElements.getReportElements().get(j);
                    if (res != null && res.length == 1 && res[0] instanceof ReportFunctionsElement)
                    {
                        res[0].addChild(reportElement);
                    }
                }

                if (reportPanel != null)
                {
                    reportPanel.getBandElementModel().setSelection(reportElements.getReportElements());
                }
                else if (reportElementSelectionModel != null)
                {
                    reportElementSelectionModel.setSelection(reportElements.getReportElements());
                }
            }
            catch (CloneNotSupportedException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
            finally
            {
                reportDialog.getUndo().endTransaction();
            }
        }
    }

    private void pasteDataSetReportElement(@NotNull CommandEvent event, @NotNull ReportDialog reportDialog, @Nullable ReportPanel reportPanel, @Nullable ReportElementSelectionModel reportElementSelectionModel) throws UnsupportedFlavorException, IOException
    {
        ReportElement[] res = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        DataFlavor desirableDataFlavor = new DataFlavor(ReportElementList.class, DataFlavor.javaSerializedObjectMimeType);
        boolean available = Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(desirableDataFlavor);
        if (available)
        {
            try
            {
                reportDialog.getUndo().startTransaction(UndoConstants.PASTE);
                ReportElementList reportElements = ((ReportElementList) Toolkit.getDefaultToolkit().getSystemClipboard().getData(desirableDataFlavor)).clone();

                for (int j = 0; j < reportElements.getReportElements().size(); j++)
                {
                    ReportElement reportElement = reportElements.getReportElements().get(j);
                    if (res != null && res.length == 1 && res[0] instanceof DataSetsReportElement)
                    {
                        res[0].addChild(reportElement);
                    }
                }

                if (reportPanel != null)
                {
                    reportPanel.getBandElementModel().setSelection(reportElements.getReportElements());
                }
                else if (reportElementSelectionModel != null)
                {
                    reportElementSelectionModel.setSelection(reportElements.getReportElements());
                }
            }
            catch (CloneNotSupportedException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
            finally
            {
                reportDialog.getUndo().endTransaction();
            }
        }
    }



    private void pasteRegularReportElement(@NotNull CommandEvent event, @NotNull ReportDialog reportDialog, @Nullable ReportPanel reportPanel, @Nullable ReportElementSelectionModel reportElementSelectionModel) throws UnsupportedFlavorException, IOException
    {
        ReportElement[] res = (ReportElement[]) event.getDataContext().getData(CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY);
        DataFlavor desirableDataFlavor = new DataFlavor(ReportElementList.class, DataFlavor.javaSerializedObjectMimeType);
        boolean available = Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(desirableDataFlavor);
        if (available)
        {
            try
            {
                reportDialog.getUndo().startTransaction(UndoConstants.PASTE);
                ReportElementList reportElements = ((ReportElementList) Toolkit.getDefaultToolkit().getSystemClipboard().getData(desirableDataFlavor)).clone();

                for (int j = 0; j < reportElements.getReportElements().size(); j++)
                {
                    ReportElement reportElement = reportElements.getReportElements().get(j);
                    if (res != null && res.length == 1 && res[0] instanceof BandReportElement)
                    {
                        res[0].addChild(reportElement);
                    }
                    else
                    {
                        if (reportPanel != null)
                        {
                            reportPanel.getBandToplevelReportElement().addChild(reportElement);
                        }
                        else if (reportElementSelectionModel != null)
                        {
                            BandToplevelReportElement band = (BandToplevelReportElement) reportElementSelectionModel.getSelectedElementInfos().get(0);
                            band.addChild(reportElement);
                        }
                    }
                }

                if (reportPanel != null)
                {
                    reportPanel.getBandElementModel().setSelection(reportElements.getReportElements());
                }
                else if (reportElementSelectionModel != null)
                {
                    reportElementSelectionModel.setSelection(reportElements.getReportElements());
                }
            }
            catch (CloneNotSupportedException e)
            {
                UncaughtExcpetionsModel.getInstance().addException(e);
            }
            finally
            {
                reportDialog.getUndo().endTransaction();
            }
        }
    }


}
